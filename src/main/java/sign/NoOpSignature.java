package sign;

import java.security.Key;

/**
 * Use this implementation of AbstractExternalSignature when you do not want to sign documents
 * A hashing algorithm remains required.
 */
public class NoOpSignature extends AbstractExternalSignature {

    @Override
    public String getHashAlgorithm() {
        return "SHA-256";
    }

    @Override
    public String getEncryptionAlgorithm() {
        return "";
    }

    @Override
    public Key getPrivateKey() {
        return null;
    }

    @Override
    public Key getPublicKey() {
        return null;
    }

}
