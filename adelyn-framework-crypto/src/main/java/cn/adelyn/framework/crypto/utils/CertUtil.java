package cn.adelyn.framework.crypto.utils;

import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.crypto.constant.AlgoConstant;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class CertUtil {

    public static PKCS10CertificationRequest generateCSR(X500Name subject, String algorithm) {
        try {
            KeyPair keyPair = KeyUtil.generateKeyPair(algorithm);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
            CertificationRequestInfo info = new CertificationRequestInfo(subject, subjectPublicKeyInfo, new DERSet());

            byte[] signature;
            AlgorithmIdentifier signAlgo = getSignAlgo(subjectPublicKeyInfo.getAlgorithm());
            if(signAlgo.getAlgorithm().equals(GMObjectIdentifiers.sm2sign_with_sm3)) {
                signature = SignUtil.SM2Sign(info.getEncoded(ASN1Encoding.DER),privateKey);
            } else if(signAlgo.getAlgorithm().equals(X9ObjectIdentifiers.ecdsa_with_SHA256)) {
                signature = SignUtil.ECDSASign(info.getEncoded(ASN1Encoding.DER),privateKey);
            } else if(signAlgo.getAlgorithm().equals(PKCSObjectIdentifiers.sha256WithRSAEncryption)) {
                signature = SignUtil.RSASign(info.getEncoded(ASN1Encoding.DER),privateKey);
            } else {
                throw new AdelynException("密钥算法不支持");
            }
            return new PKCS10CertificationRequest(
                    new CertificationRequest(info, signAlgo, new DERBitString(signature))
            );
        } catch (Exception e) {
            throw new AdelynException("密钥结构错误: " + e.getMessage());
        }
    }

    //生成实体证书
    public static Certificate signCert(PKCS10CertificationRequest csr, PrivateKey issuerPrivateKey, byte[] issuerCert, Date notBefore, Date notAfter) throws Exception {
        X509CertificateHolder issuer = new X509CertificateHolder(issuerCert);
        if(!verifyCSR(csr)) {
            throw new AdelynException("证书请求验证失败");
        }
        X500Name subject = csr.getSubject();
        BcX509ExtensionUtils extUtils = new BcX509ExtensionUtils();
        ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
        // entity cert
        extensionsGenerator.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
        extensionsGenerator.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature));
        // 授权密钥标识
        extensionsGenerator.addExtension(Extension.authorityKeyIdentifier,false,extUtils.createAuthorityKeyIdentifier(issuer));
        // 使用者密钥标识
        extensionsGenerator.addExtension(Extension.subjectKeyIdentifier,false,extUtils.createSubjectKeyIdentifier(csr.getSubjectPublicKeyInfo()));
        V3TBSCertificateGenerator tbsGen = new V3TBSCertificateGenerator();
        tbsGen.setSerialNumber(new ASN1Integer(UUID.randomUUID().getMostSignificantBits()&Long.MAX_VALUE));
        tbsGen.setIssuer(issuer.getSubject());
        tbsGen.setStartDate(new Time(notBefore, Locale.CHINA));
        tbsGen.setEndDate(new Time(notAfter,Locale.CHINA));
        tbsGen.setSubject(subject);
        tbsGen.setSubjectPublicKeyInfo(csr.getSubjectPublicKeyInfo());
        tbsGen.setExtensions(extensionsGenerator.generate());
        // 签名算法标识等于颁发者证书的密钥算法标识
        tbsGen.setSignature(issuer.getSubjectPublicKeyInfo().getAlgorithm());
        TBSCertificate tbs = tbsGen.generateTBSCertificate();
        return assembleCert(tbs,issuer.getSubjectPublicKeyInfo(),issuerPrivateKey);
    }

    //生成自签名证书
    public static Certificate getSelfSignedCert(X500Name subject, String algorithm, Date notBefore, Date notAfter) throws Exception{
        KeyPair keyPair = KeyUtil.generateKeyPair(algorithm);
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        BcX509ExtensionUtils extUtils = new BcX509ExtensionUtils();
        ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();
        // ca cert
        extensionsGenerator.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        extensionsGenerator.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));
        extensionsGenerator.addExtension(Extension.subjectKeyIdentifier,false,extUtils.createSubjectKeyIdentifier(subjectPublicKeyInfo));
        extensionsGenerator.addExtension(Extension.authorityKeyIdentifier,false,extUtils.createAuthorityKeyIdentifier(subjectPublicKeyInfo));
        V3TBSCertificateGenerator   tbsGen = new V3TBSCertificateGenerator();
        tbsGen.setSerialNumber(new ASN1Integer(UUID.randomUUID().getMostSignificantBits()&Long.MAX_VALUE));
        // 自签证书颁发者等于使用者
        tbsGen.setIssuer(subject);
        tbsGen.setStartDate(new Time(notBefore, Locale.CHINA));
        tbsGen.setEndDate(new Time(notAfter,Locale.CHINA));
        tbsGen.setSubject(subject);
        tbsGen.setSubjectPublicKeyInfo(subjectPublicKeyInfo);
        tbsGen.setExtensions(extensionsGenerator.generate());
        // 签名算法标识等于密钥算法标识
        tbsGen.setSignature(getSignAlgo(subjectPublicKeyInfo.getAlgorithm()));
        TBSCertificate tbs = tbsGen.generateTBSCertificate();
        return assembleCert(tbs,subjectPublicKeyInfo,keyPair.getPrivate());
    }

    public static Certificate assembleCert(TBSCertificate tbsCertificate, SubjectPublicKeyInfo issuerSubjectPublicKeyInfo,
                                           PrivateKey issuerPrivateKey) throws Exception{
        byte[] signature = null;
        if(issuerPrivateKey.getAlgorithm().equalsIgnoreCase(AlgoConstant.ECDSA)) {
            if(issuerSubjectPublicKeyInfo.getAlgorithm().getParameters().equals(GMObjectIdentifiers.sm2p256v1)) {
                signature = SignUtil.SM2Sign(tbsCertificate.getEncoded(),issuerPrivateKey);
            } else if(issuerSubjectPublicKeyInfo.getAlgorithm().getParameters().equals(SECObjectIdentifiers.secp256k1)) {
                signature = SignUtil.ECDSASign(tbsCertificate.getEncoded(),issuerPrivateKey);
            } else {
                throw new AdelynException("不支持的曲线");
            }
        } else if(issuerPrivateKey.getAlgorithm().equalsIgnoreCase(AlgoConstant.RSA)) {
            signature = SignUtil.RSASign(tbsCertificate.getEncoded(),issuerPrivateKey);
        } else {
            throw new AdelynException("不支持的密钥算法");
        }
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(tbsCertificate);
        v.add(getSignAlgo(issuerSubjectPublicKeyInfo.getAlgorithm()));
        v.add(new DERBitString(signature));
        return Certificate.getInstance(new DERSequence(v));
    }

    public static boolean verifyCSR(PKCS10CertificationRequest csr) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {
        byte[] signature = csr.getSignature();
        if(csr.getSignatureAlgorithm().getAlgorithm().equals(GMObjectIdentifiers.sm2sign_with_sm3)) {
            return  SignUtil.SM2VerifySign(
                    csr.toASN1Structure().getCertificationRequestInfo().getEncoded(ASN1Encoding.DER),
                    signature, KeyUtil.getPublicKey(csr.getSubjectPublicKeyInfo())
            );
        } else if(csr.getSignatureAlgorithm().getAlgorithm().equals(X9ObjectIdentifiers.ecdsa_with_SHA256)) {
            return SignUtil.ECDSAVerifySign(
                    csr.toASN1Structure().getCertificationRequestInfo().getEncoded(ASN1Encoding.DER),
                    signature, KeyUtil.getPublicKey(csr.getSubjectPublicKeyInfo())
            );
        } else if(csr.getSignatureAlgorithm().getAlgorithm().equals(PKCSObjectIdentifiers.sha256WithRSAEncryption)) {
            return SignUtil.RSAVerifySign(
                    csr.toASN1Structure().getCertificationRequestInfo().getEncoded(ASN1Encoding.DER),
                    signature, KeyUtil.getPublicKey(csr.getSubjectPublicKeyInfo())
            );
        } else {
            throw new AdelynException("不支持的签名算法");
        }
    }

    static AlgorithmIdentifier getSignAlgo(AlgorithmIdentifier asymAlgo) {  //根据公钥算法标识返回对应签名算法标识
        if(asymAlgo.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey) &&
                asymAlgo.getParameters().equals(GMObjectIdentifiers.sm2p256v1)) {
            return new AlgorithmIdentifier(GMObjectIdentifiers.sm2sign_with_sm3, DERNull.INSTANCE);
        } else if(asymAlgo.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey) &&
                asymAlgo.getParameters().equals(SECObjectIdentifiers.secp256k1)) {
            return new AlgorithmIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA256);
        } else if(asymAlgo.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
            return new AlgorithmIdentifier(PKCSObjectIdentifiers.sha256WithRSAEncryption, DERNull.INSTANCE);
        } else {
            throw new AdelynException("密钥算法不支持");
        }
    }

    /**
     * 读取X.509证书
     */
    public static X509Certificate parseX509Certificate(byte[] cer) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
            X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cer));
            return cert;
        } catch (Exception e) {
            throw new AdelynException("证书解析失败" + e.getMessage());
        }

    }

    /**
     * 获取公钥证书链
     */
    public static java.security.cert.Certificate[] getCertificateChain(byte[] cer) {
        try {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509",
                    BouncyCastleProvider.PROVIDER_NAME);
            // 获取证书链
            Collection<java.security.cert.Certificate> chainList = new ArrayList<>(
                    certificatefactory.generateCertificates(new ByteArrayInputStream(cer)));
            return chainList.toArray(new java.security.cert.Certificate[] {});
        } catch (Exception e) {
            throw new AdelynException("获取证书链失败" + e.getMessage());
        }
    }

    public static PrivateKey getPrivateKeyFromPfx(String alias, byte[] pfxByte, char[] pwd) {
        try {
            KeyStore keyStore = getKeyStore(pfxByte, pwd, "PKCS12");
            return (PrivateKey) keyStore.getKey(alias, pwd);
        } catch (Exception e) {
            throw new AdelynException("获取私钥失败" + e.getMessage());
        }
    }

    public static PublicKey getPublicKeyFromPfx(String alias, byte[] pfxByte, char[] pwd) {
        return getCertFromPfx(alias, pfxByte, pwd).getPublicKey();
    }

    public static java.security.cert.Certificate getCertFromPfx(String alias, byte[] pfxByte, char[] pwd) {
        KeyStore keyStore = getKeyStore(pfxByte, pwd, "PKCS12");
        try {
            return keyStore.getCertificate(alias);
        } catch (KeyStoreException e) {
            throw new AdelynException("获取证书失败" + e.getMessage());
        }
    }

    public static KeyStore getKeyStore (byte[] pfxByte, char[] pwd, String type) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pfxByte)) {
            KeyStore keyStore = KeyStore.getInstance(type);
            keyStore.load(byteArrayInputStream, pwd);
            return keyStore;
        } catch (Exception e) {
            throw new AdelynException("解析pfx证书失败" + e.getMessage());
        }
    }
}
