package cn.adelyn.framework.crypto.utils;

import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.crypto.constant.AlgoConstant;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;

import java.security.*;

public class KeyUtil {

    private static final String BC = BouncyCastleProvider.PROVIDER_NAME;

    public static KeyPair generateKeyPair(String algo) throws NoSuchProviderException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator;
        SecureRandom random = new SecureRandom();
        if(AlgoConstant.RSA.equalsIgnoreCase(algo)) {
            keyPairGenerator = KeyPairGenerator.getInstance(AlgoConstant.RSA, BC);
            keyPairGenerator.initialize(2048,random);
        } else if(AlgoConstant.SM2.equalsIgnoreCase(algo)) {
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
            keyPairGenerator = KeyPairGenerator.getInstance(AlgoConstant.SM2, BC);
            keyPairGenerator.initialize(ecSpec,random);
        } else if(AlgoConstant.ECDSA.equalsIgnoreCase(algo)) {
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
            keyPairGenerator = KeyPairGenerator.getInstance(AlgoConstant.ECDSA, BC);
            keyPairGenerator.initialize(ecSpec,random);
        } else {
            throw new AdelynException("算法不支持");
        }
        return keyPairGenerator.generateKeyPair();
    }

    public static PublicKey getPublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) throws Exception{
        BouncyCastleProvider bouncyCastleProvider = ((BouncyCastleProvider)Security.getProvider(BC));
        bouncyCastleProvider.addKeyInfoConverter(PKCSObjectIdentifiers.rsaEncryption,
                new org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi());
        bouncyCastleProvider.addKeyInfoConverter(X9ObjectIdentifiers.id_ecPublicKey,
                new org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi.EC());
        return BouncyCastleProvider.getPublicKey(subjectPublicKeyInfo);
    }
}
