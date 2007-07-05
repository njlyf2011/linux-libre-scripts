/* RepositorioXMLJavaBean - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.persistenciagenerica;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class RepositorioXMLJavaBean
{
  public void salva (Object objeto, FileOutputStream file)
  {
    XMLEncoder encoder = new XMLEncoder (new BufferedOutputStream (file));
    encoder.writeObject (objeto);
    encoder.close ();
  }
  
  public Object getObjeto (FileInputStream file)
  {
    XMLDecoder decoder = new XMLDecoder (new BufferedInputStream (file));
    Object retorno = decoder.readObject ();
    decoder.close ();
    return retorno;
  }
  
  public static void main (String[] args)
  {
    /* empty */
  }
}
