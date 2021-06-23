package com.gnosis.sign;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

@SpringBootTest
class GnosisSignApplicationTests {

	@Test
	void contextLoads() throws IOException {
		String privateKey = Hash.sha3String("cow");
		String safe = "0x8feDD9E43C4B6280412921Cc16Ff9A6228a2Bc6b";
		String sender = "0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826";
		SafeTransaction transaction = new SafeTransaction(sender);
		String hash = "0x34f8ec04a9d5e96a5efcb68ab8bdc6a1bf3afae53deb49dbbe8cb35be7770160";
		ECKeyPair keyPair = ECKeyPair.create(Numeric.hexStringToByteArray(privateKey));
		String signature = SigningService.signHash(hash, keyPair);
		TransactionService.submitTx(safe, sender, hash, signature, transaction);
	}

}
