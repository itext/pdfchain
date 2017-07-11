package sign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Default implementation of AbstractExternalSignature
 */
public class DefaultExternalSignature extends AbstractExternalSignature {

    private KeyStore ks;
    private String alias;
    private String password;

    public DefaultExternalSignature(File keystoreFile, String alias, String password) {
        this.alias = alias;
        this.password = password;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream(keystoreFile), password.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHashAlgorithm() {
        return "SHA-256";
    }

    @Override
    public String getEncryptionAlgorithm() {
        return "RSA";
    }

    @Override
    public Key getPrivateKey() {
        try {
            return ks.getKey(alias, password.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Key getPublicKey() {
        try {
            return ks.getCertificate(alias).getPublicKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }

}
