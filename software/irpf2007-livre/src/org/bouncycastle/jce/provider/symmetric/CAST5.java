package org.bouncycastle.jce.provider.symmetric;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.misc.CAST5CBCParameters;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.CAST5Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jce.provider.JCEBlockCipher;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.jce.provider.JDKAlgorithmParameterGenerator;
import org.bouncycastle.jce.provider.JDKAlgorithmParameters;

import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;

public final class CAST5
{
    private CAST5()
    {
    }
    
    public static class ECB
        extends JCEBlockCipher
    {
        public ECB()
        {
            super(new CAST5Engine());
        }
    }

    public static class CBC
       extends JCEBlockCipher
    {
        public CBC()
        {
            super(new CBCBlockCipher(new CAST5Engine()));
        }
    }

    public static class KeyGen
        extends JCEKeyGenerator
    {
        public KeyGen()
        {
            super("CAST5", 128, new CipherKeyGenerator());
        }
    }

    public static class AlgParamGen
        extends JDKAlgorithmParameterGenerator
    {
        protected void engineInit(
            AlgorithmParameterSpec  genParamSpec,
            SecureRandom            random)
            throws InvalidAlgorithmParameterException
        {
            throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for CAST5 parameter generation.");
        }

        protected AlgorithmParameters engineGenerateParameters()
        {
            byte[]  iv = new byte[8];

            if (random == null)
            {
                random = new SecureRandom();
            }

            random.nextBytes(iv);

            AlgorithmParameters params;

            try
            {
                params = AlgorithmParameters.getInstance("CAST5", "BC");
                params.init(new IvParameterSpec(iv));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e.getMessage());
            }

            return params;
        }
    }

    public static class AlgParams
        extends JDKAlgorithmParameters
    {
        private byte[]  iv;
        private int     keyLength = 128;

        protected byte[] engineGetEncoded()
        {
            byte[]  tmp = new byte[iv.length];

            System.arraycopy(iv, 0, tmp, 0, iv.length);
            return tmp;
        }

        protected byte[] engineGetEncoded(
            String format)
            throws IOException
        {
            if (isASN1FormatString(format))
            {
                return new CAST5CBCParameters(engineGetEncoded(), keyLength).getEncoded();
            }

            if (format.equals("RAW"))
            {
                return engineGetEncoded();
            }


            return null;
        }

        protected AlgorithmParameterSpec localEngineGetParameterSpec(
            Class paramSpec)
            throws InvalidParameterSpecException
        {
            if (paramSpec == IvParameterSpec.class)
            {
                return new IvParameterSpec(iv);
            }

            throw new InvalidParameterSpecException("unknown parameter spec passed to CAST5 parameters object.");
        }

        protected void engineInit(
            AlgorithmParameterSpec paramSpec)
            throws InvalidParameterSpecException
        {
            if (paramSpec instanceof IvParameterSpec)
            {
                this.iv = ((IvParameterSpec)paramSpec).getIV();
            }
            else
            {
                throw new InvalidParameterSpecException("IvParameterSpec required to initialise a CAST5 parameters algorithm parameters object");
            }
        }

        protected void engineInit(
            byte[] params)
            throws IOException
        {
            this.iv = new byte[params.length];

            System.arraycopy(params, 0, iv, 0, iv.length);
        }

        protected void engineInit(
            byte[] params,
            String format)
            throws IOException
        {
            if (isASN1FormatString(format))
            {
                ASN1InputStream aIn = new ASN1InputStream(params);
                CAST5CBCParameters      p = CAST5CBCParameters.getInstance(aIn.readObject());

                keyLength = p.getKeyLength();

                iv = p.getIV();

                return;
            }

            if (format.equals("RAW"))
            {
                engineInit(params);
                return;
            }

            throw new IOException("Unknown parameters format in IV parameters object");
        }

        protected String engineToString()
        {
            return "CAST5 Parameters";
        }
    }
}
