package com.gnosis.sign;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.web3j.crypto.Credentials;

@RunWith(MockitoJUnitRunner.class)
public class SigningServiceTests {

    @Test
    public void test_contract_hash_signing() {
        String privateKey = "0xa051d9cf9c1f2499b129ca3387070ef83aeb84574c01757d0532a61bc6124efd";

        String signature = SigningService
            .signHash("0x45f24f934ebb77afb37baeb132fe84f0c4dc0fbc2cf0e480f9d190581e958cd4", Credentials.create(privateKey).getEcKeyPair());

        assertThat(signature).isNotNull().isEqualTo(
            "0x643df6e6775e1b900ea6caaaf3cd6b5970d474b2dc749b97a3256f55c0aa8bc9383db8ae2a2d74129142ad55ec7d97768195715051da34773616b1b93a1c79be1c");

        signature = SigningService
            .signHash("0x3a1b8d80b71f039b35c0751a34c276175c78041bf0c0540cd2a71acff52a65f1", Credentials.create(privateKey).getEcKeyPair());

        assertThat(signature).isNotNull().isEqualTo(
            "0xb588e8a36b80ca1a9bfd37fdfcf1d95b2ab67198d28dda16a2534bfbebf19fb20a4378b379c059fccb8b1e1887bc4a5068e103c4646fc307bd43b91d102d14471c");
    }
}
