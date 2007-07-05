/* AdaptadorXMLAtributo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

import serpro.ppgd.negocio.util.LogPPGD;

class AdaptadorXMLAtributo
{
  private String elementoXML;
  private String metodoAcesso;
  private boolean marshalling = true;
  private boolean unmarshalling = true;
  
  public AdaptadorXMLAtributo (Element nodeXmlMapeamento)
  {
    if (! nodeXmlMapeamento.getNodeName ().equals ("Atributo"))
      {
	LogPPGD.erro ("Par\u00e2metro inv\u00e1lido - n\u00e3o \u00e9 Atributo");
	throw new IllegalArgumentException ("N\u00e3o \u00e9 Atributo");
      }
    elementoXML = nodeXmlMapeamento.getAttribute ("ElementoXML");
    if (elementoXML.length () == 0)
      {
	LogPPGD.erro ("Mapeamento inv\u00e1lido");
	throw new DOMException ((short) 12, "ElementoXML n\u00e3o pode ser vazio");
      }
    metodoAcesso = nodeXmlMapeamento.getAttribute ("MetodoAcesso");
    if (metodoAcesso == "")
      metodoAcesso = "get" + elementoXML;
    if (nodeXmlMapeamento.getAttribute ("Marshalling").toLowerCase ().equals ("false"))
      marshalling = false;
    if (nodeXmlMapeamento.getAttribute ("Unmarshalling").toLowerCase ().equals ("false"))
      unmarshalling = false;
  }
  
  public String toString ()
  {
    return "[Atributo: " + elementoXML + ", MetodoAcesso: " + metodoAcesso + "]";
  }
  
  public String getElementoXML ()
  {
    return elementoXML;
  }
  
  public String getMetodoAcesso ()
  {
    return metodoAcesso;
  }
  
  public boolean isMarshalling ()
  {
    return marshalling;
  }
  
  public boolean isUnmarshalling ()
  {
    return unmarshalling;
  }
}
