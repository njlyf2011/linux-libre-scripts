package org.bouncycastle.cms;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetStringParser;
import org.bouncycastle.asn1.ASN1SequenceParser;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1SetParser;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERTags;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.EncryptedContentInfoParser;
import org.bouncycastle.asn1.cms.EnvelopedDataParser;
import org.bouncycastle.asn1.cms.KEKRecipientInfo;
import org.bouncycastle.asn1.cms.KeyAgreeRecipientInfo;
import org.bouncycastle.asn1.cms.KeyTransRecipientInfo;
import org.bouncycastle.asn1.cms.PasswordRecipientInfo;
import org.bouncycastle.asn1.cms.RecipientInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Parsing class for an CMS Enveloped Data object from an input stream.
 * <p>
 * Note: that because we are in a streaming mode only one recipient can be tried and it is important 
 * that the methods on the parser are called in the appropriate order.
 * </p>
 * <p>
 * Example of use - assuming the first recipient matches the private key we have.
 * <pre>
 *      CMSEnvelopedDataParser     ep = new CMSEnvelopedDataParser(inputStream);
 *
 *      RecipientInformationStore  recipients = ep.getRecipientInfos();
 *
 *      Collection  c = recipients.getRecipients();
 *      Iterator    it = c.iterator();
 *      
 *      if (it.hasNext())
 *      {
 *          RecipientInformation   recipient = (RecipientInformation)it.next();
 *
 *          CMSTypedStream recData = recipient.getContentStream(privateKey, "BC");
 *          
 *          processDataStream(recData.getContentStream());
 *      }
 *  </pre>
 *  Note: this class does not introduce buffering - if you are processing large files you should create
 *  the parser with:
 *  <pre>
 *          CMSEnvelopedDataParser     ep = new CMSEnvelopedDataParser(new BufferedInputStream(inputStream, bufSize));
 *  </pre>
 *  where bufSize is a suitably large buffer size.
 */
public class CMSEnvelopedDataParser
    extends CMSContentInfoParser
{
    RecipientInformationStore   _recipientInfoStore;
    EnvelopedDataParser _envelopedData;
    
    private AlgorithmIdentifier _encAlg;
    private AttributeTable      _unprotectedAttributes;
    private boolean             _attrNotRead;

    public CMSEnvelopedDataParser(
        byte[]    envelopedData) 
        throws CMSException, IOException
    {
        this(new ByteArrayInputStream(envelopedData));
    }

    public CMSEnvelopedDataParser(
        InputStream    envelopedData) 
        throws CMSException, IOException
    {
        super(envelopedData);

        this._attrNotRead = true;
        this._envelopedData = new EnvelopedDataParser((ASN1SequenceParser)_contentInfo.getContent(DERTags.SEQUENCE));

        //
        // load the RecepientInfoStore
        //
        ASN1SetParser s = _envelopedData.getRecipientInfos();
        List          baseInfos = new ArrayList();
        ASN1Set       set = ASN1Set.getInstance(s.getDERObject());

        for (Enumeration en = set.getObjects(); en.hasMoreElements();)
        {
            baseInfos.add(RecipientInfo.getInstance(en.nextElement()));
        }

        //
        // read the encrypted content info
        //
        EncryptedContentInfoParser encInfo = _envelopedData.getEncryptedContentInfo();
        
        this._encAlg = encInfo.getContentEncryptionAlgorithm();
        
        //
        // prime the recepients
        //
        List        infos = new ArrayList();
        Iterator    it = baseInfos.iterator();
        InputStream dataStream = ((ASN1OctetStringParser)encInfo.getEncryptedContent(DERTags.OCTET_STRING)).getOctetStream();
        
        while (it.hasNext())
        {
            RecipientInfo   info = (RecipientInfo)it.next();

            if (info.getInfo() instanceof KeyTransRecipientInfo)
            {
                infos.add(new KeyTransRecipientInformation(
                            (KeyTransRecipientInfo)info.getInfo(), _encAlg, dataStream));
            }
            else if (info.getInfo() instanceof KEKRecipientInfo)
            {
                infos.add(new KEKRecipientInformation(
                            (KEKRecipientInfo)info.getInfo(), _encAlg, dataStream));
            }
            else if (info.getInfo() instanceof KeyAgreeRecipientInfo)
            {
                infos.add(new KeyAgreeRecipientInformation(
                            (KeyAgreeRecipientInfo)info.getInfo(), _encAlg, dataStream));
            }
            else if (info.getInfo() instanceof PasswordRecipientInfo)
            {
                infos.add(new PasswordRecipientInformation(
                            (PasswordRecipientInfo)info.getInfo(), _encAlg, dataStream));
            }
        }
        
        _recipientInfoStore = new RecipientInformationStore(infos);
    }
    
    /**
     * return the object identifier for the content encryption algorithm.
     */
    public String getEncryptionAlgOID()
    {
        return _encAlg.getObjectId().toString();
    }

    /**
     * return the ASN.1 encoded encryption algorithm parameters, or null if
     * there aren't any.
     */
    public byte[] getEncryptionAlgParams()
    {
        try
        {
            return _encAlg.getParameters().getDERObject().getEncoded();
        }
        catch (Exception e)
        {
            throw new RuntimeException("exception getting encryption parameters " + e);
        }
    }
    
    /**
     * Return an AlgorithmParameters object giving the encryption parameters
     * used to encrypt the message content.
     * 
     * @param provider the provider to generate the parameters for.
     * @return the parameters object, null if there is not one.
     * @throws CMSException if the algorithm cannot be found, or the parameters can't be parsed.
     * @throws NoSuchProviderException if the provider cannot be found.
     */
    public AlgorithmParameters getEncryptionAlgorithmParameters(
        String  provider) 
        throws CMSException, NoSuchProviderException
    {        
        try
        {
            byte[]  enc = this.getEncryptionAlgParams();
            if (enc == null)
            {
                return null;
            }
            
            AlgorithmParameters params = AlgorithmParameters.getInstance(getEncryptionAlgOID(), provider); 
            
            params.init(enc, "ASN.1");
            
            return params;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new CMSException("can't find parameters for algorithm", e);
        }
        catch (IOException e)
        {
            throw new CMSException("can't find parse parameters", e);
        }  
    }
    
    /**
     * return a store of the intended recipients for this message
     */
    public RecipientInformationStore getRecipientInfos()
    {
        return _recipientInfoStore;
    }

    /**
     * return a table of the unprotected attributes indexed by
     * the OID of the attribute.
     * @exception IOException 
     */
    public AttributeTable getUnprotectedAttributes() 
        throws IOException
    {
        if (_unprotectedAttributes == null && _attrNotRead)
        {
            ASN1SetParser             set = _envelopedData.getUnprotectedAttrs();
            
            _attrNotRead = false;
            
            if (set != null)
            {
                ASN1EncodableVector v = new ASN1EncodableVector();
                DEREncodable        o;
                
                while ((o = set.readObject()) != null)
                {
                    ASN1SequenceParser    seq = (ASN1SequenceParser)o;
                    
                    v.add(seq.getDERObject());
                }
                
                _unprotectedAttributes = new AttributeTable(new DERSet(v));
            }
        }

        return _unprotectedAttributes;
    }
}
