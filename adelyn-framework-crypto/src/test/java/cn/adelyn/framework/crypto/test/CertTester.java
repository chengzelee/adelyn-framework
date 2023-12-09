package cn.adelyn.framework.crypto.test;

import cn.adelyn.framework.crypto.constant.AlgoConstant;
import cn.adelyn.framework.crypto.utils.CertUtil;
import cn.adelyn.framework.crypto.utils.KeyUtil;
import cn.adelyn.framework.crypto.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Date;

@Slf4j
public class CertTester {

    @BeforeAll
    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void signCert() throws Exception{
        //生成密钥对
        KeyPair eccrootcakey = KeyUtil.generateKeyPair(AlgoConstant.ECDSA);
        KeyPair sm2rootcakey  = KeyUtil.generateKeyPair(AlgoConstant.SM2);
        KeyPair rsarootcakey  = KeyUtil.generateKeyPair(AlgoConstant.RSA);
        //生成证书请求
        PKCS10CertificationRequest eccCsr = CertUtil.generateCSR(new X500Name("C=CN,CN=ECCEntityCert"), AlgoConstant.ECDSA);
        PKCS10CertificationRequest sm2Csr = CertUtil.generateCSR(new X500Name("C=CN,CN=ECCEntityCert"), AlgoConstant.SM2);
        PKCS10CertificationRequest rsaCsr = CertUtil.generateCSR(new X500Name("C=CN,CN=ECCEntityCert"), AlgoConstant.RSA);
        //验证证书请求
        assert CertUtil.verifyCSR(sm2Csr);
        assert CertUtil.verifyCSR(eccCsr);
        assert CertUtil.verifyCSR(rsaCsr);
        log.info(getPemStr(eccCsr));
        log.info(getPemStr(sm2Csr));
        log.info(getPemStr(rsaCsr));
        //自签发根证书
        Date notBefore = new Date();
        Date notAfter = new Date(System.currentTimeMillis() + 1000L*60L*60L*24L*365L);   //365天
        Certificate eccCaCert = CertUtil.getSelfSignedCert(new X500Name("C=CN,CN=ECCRootCa"),AlgoConstant.ECDSA,notBefore,notAfter);
        Certificate sm2CaCert = CertUtil.getSelfSignedCert(new X500Name("C=CN,CN=SM2RootCa"),AlgoConstant.SM2,notBefore,notAfter);
        Certificate rsaCaCert = CertUtil.getSelfSignedCert(new X500Name("C=CN,CN=RSARootCa"),AlgoConstant.RSA,notBefore,notAfter);
        //根证书签发生成实体证书
        Certificate eccEntityCert = CertUtil.signCert(eccCsr,eccrootcakey.getPrivate(),eccCaCert.getEncoded(),notBefore,notAfter);
        Certificate sm2EntityCert = CertUtil.signCert(sm2Csr,sm2rootcakey.getPrivate(),sm2CaCert.getEncoded(),notBefore,notAfter);
        Certificate rsaEntityCert = CertUtil.signCert(rsaCsr,rsarootcakey.getPrivate(),rsaCaCert.getEncoded(),notBefore,notAfter);
        //写入证书DER到文件
//        writeFile(eccCaCert.getEncoded(),"eccRootCert.cer");
//        writeFile(sm2CaCert.getEncoded(),"sm2RootCert.cer");
//        writeFile(rsaCaCert.getEncoded(),"rsaRootCert.cer");
//        writeFile(eccEntityCert.getEncoded(),"eccEntityCert.cer");
//        writeFile(sm2EntityCert.getEncoded(),"sm2EntityCert.cer");
//        writeFile(rsaEntityCert.getEncoded(),"rsaEntityCert.cer");
    }

    public String getPemStr(Object obj) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JcaPEMWriter pemWriter = new JcaPEMWriter(new OutputStreamWriter(bos));
        pemWriter.writeObject(obj);
        pemWriter.flush();
        pemWriter.close();
        return bos.toString();
    }

    public void writeFile(byte[] data, String path) throws Exception{
        File f = new File(path);
        if(!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(f);
        out.write(data);
        out.close();
    }

    @Test
    public void loadCert() {
        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\10518\\Desktop\\2.pfx")) {
            byte[] pfxCertByte = fileInputStream.readAllBytes();
            PrivateKey privateKey = CertUtil.getPrivateKeyFromPfx("te-f5349058-3bc6-4a38-883b-bebd51e4cae3", pfxCertByte, "123456".toCharArray());
            PublicKey publicKey = CertUtil.getPublicKeyFromPfx("te-f5349058-3bc6-4a38-883b-bebd51e4cae3", pfxCertByte, "123456".toCharArray());
            byte[] tbs = {1, 2, 3};
            SignUtil.RSASign(tbs, privateKey);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }
}
