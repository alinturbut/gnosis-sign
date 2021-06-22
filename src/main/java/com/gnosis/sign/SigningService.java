package com.gnosis.sign;

import static org.web3j.crypto.Sign.recoverFromSignature;

import java.math.BigInteger;
import java.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

@Slf4j
public class SigningService {

    /**
     * Function used to sign a hashed string message using an {@link ECKeyPair} predefined. Returns r + s + v of a signature.
     */
    public static String signHash(String hash, ECKeyPair ecKeyPair) {
        try {
            byte[] hexMessage = Numeric.hexStringToByteArray(hash);

            Sign.SignatureData signature = Sign.signPrefixedMessage(hexMessage, ecKeyPair);

            // Match the signature output format as Ethers.js v5.0.31
            // https://github.com/ethers-io/ethers.js/blob/v5.0.31/packages/bytes/src.ts/index.ts#L444-L448
            byte[] retval = new byte[65];
            System.arraycopy(signature.getR(), 0, retval, 0, 32);
            System.arraycopy(signature.getS(), 0, retval, 32, 32);
            System.arraycopy(Numeric.toBigInt(signature.getV()).add(BigInteger.valueOf(4)).toByteArray(), 0, retval, 64, 1);

            String pubKey = Sign.signedPrefixedMessageToKey(hexMessage, signature).toString(16);
            String signerAddress = Keys.getAddress(pubKey);

            log.info("Message to sign: {}", hash);
            log.info("Signer address: {}", signerAddress);

            String hexSignature = Numeric.toHexString(retval);
            log.debug("Signature: {}", hexSignature);

            return hexSignature;
        } catch (SignatureException ex) {
            log.error("Failed signing message.", ex);
        }

        return "";
    }

    public static String signRawHash(String hash, ECKeyPair ecKeyPair) {
        byte[] hexMessage = Numeric.hexStringToByteArray(hash);

        ECDSASignature sig = ecKeyPair.sign(Hash.sha3(hexMessage));
        // Now we have to work backwards to figure out the recId needed to recover the signature.
        int recId = -1;
        for (int i = 0; i < 4; i++) {
            BigInteger k = recoverFromSignature(i, sig, Hash.sha3(hexMessage));
            if (k != null && k.equals(ecKeyPair.getPublicKey())) {
                recId = i;
                break;
            }
        }
        if (recId == -1) {
            throw new RuntimeException(
                "Could not construct a recoverable key. Are your credentials valid?");
        }

        int headerByte = recId + 31;

        // 1 header + 32 bytes for R + 32 bytes for S
        byte[] v = new byte[]{(byte) headerByte};

        // Match the signature output format as Ethers.js v5.0.31
        // https://github.com/ethers-io/ethers.js/blob/v5.0.31/packages/bytes/src.ts/index.ts#L444-L448
        byte[] retval = new byte[65];
        System.arraycopy(sig.r.toByteArray(), 0, retval, 0, 32);
        System.arraycopy(sig.s.toByteArray(), 0, retval, 32, 32);
        System.arraycopy(v, 0, retval, 64, 1);

        String hexSignature = Numeric.toHexString(retval);
        log.debug("Signature: {}", hexSignature);

        return hexSignature;
    }
}
