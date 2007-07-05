/* AdaptadorXMLObjeto - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import serpro.ppgd.negocio.util.LogPPGD;

class AdaptadorXMLObjeto
{
  private String elementoXML;
  private String classeJava;
  private String conteiner = null;
  private String metodoAcesso = null;
  private List atributos = null;
  private boolean marshalling = true;
  private boolean unmarshalling = true;
  
  public AdaptadorXMLObjeto (Element nodeXmlMapeamento)
  {
    String nomeElementoRaiz = nodeXmlMapeamento.getOwnerDocument ().getDocumentElement ().getAttribute ("ElementoXML");
    if (nodeXmlMapeamento.getNodeName ().compareTo ("Classe") != 0 && nodeXmlMapeamento.getNodeName ().compareTo ("Mapeamento") != 0)
      {
	LogPPGD.erro ("Mapeamento: Par\u00e2metro inv\u00e1lido - elemento XML n\u00e3o \u00e9 TClasse");
	throw new IllegalArgumentException ("Mapeamento: N\u00e3o \u00e9 Classe");
      }
    elementoXML = nodeXmlMapeamento.getAttribute ("ElementoXML");
    if (elementoXML.length () == 0)
      {
	LogPPGD.erro ("Mapeamento inv\u00e1lido");
	throw new DOMException ((short) 12, "Mapeamento: ElementoXML n\u00e3o pode ser vazio");
      }
    classeJava = nodeXmlMapeamento.getAttribute ("ClasseJava");
    if (classeJava.length () == 0)
      classeJava = elementoXML;
    if (nodeXmlMapeamento.getAttribute ("Marshalling").toLowerCase ().compareTo ("false") == 0)
      marshalling = false;
    if (nodeXmlMapeamento.getAttribute ("Unmarshalling").toLowerCase ().compareTo ("false") == 0)
      unmarshalling = false;
    conteiner = "";
    metodoAcesso = "get" + elementoXML;
    if (nodeXmlMapeamento.hasChildNodes ())
      {
	NodeList filhos = nodeXmlMapeamento.getChildNodes ();
	for (int i = 0; i < filhos.getLength (); i++)
	  {
	    if (filhos.item (i).getNodeType () == 1)
	      {
		Element nodeFilho = (Element) filhos.item (i);
		if (nodeFilho.getNodeName ().compareTo ("PertenceA") == 0)
		  {
		    String nomeConteiner = nodeFilho.getAttribute ("ElementoXML");
		    if (nomeConteiner.length () == 0 || nomeConteiner.compareTo (nomeElementoRaiz) == 0)
		      conteiner = "";
		    else
		      conteiner = nomeConteiner;
		    metodoAcesso = nodeFilho.getAttribute ("MetodoAcesso");
		  }
		else if (nodeFilho.getNodeName ().compareTo ("Atributo") == 0)
		  {
		    if (atributos == null)
		      atributos = new Vector ();
		    atributos.add (new AdaptadorXMLAtributo (nodeFilho));
		  }
	      }
	  }
      }
  }
  
  public Class getClasseJava () throws ClassNotFoundException
  {
    return Class.forName (classeJava);
  }
  
  public String getConteiner ()
  {
    return conteiner;
  }
  
  public String getMetodoAcesso ()
  {
    return metodoAcesso;
  }
  
  public String toString ()
  {
    String resultado = "ElementoXML: " + getElementoXML () + ", ClasseJava: " + classeJava + ", Conteiner: " + getConteiner () + ", MetodoAcesso: " + getMetodoAcesso ();
    if (atributos != null)
      {
	resultado += ". Atributos: ";
	Iterator iterator = atributos.iterator ();
	while (iterator.hasNext ())
	  resultado += iterator.next ();
      }
    return resultado;
  }
  
  public boolean isMarshalling ()
  {
    return marshalling;
  }
  
  public String getElementoXML ()
  {
    return elementoXML;
  }
  
  public List getAtributos ()
  {
    return atributos;
  }
  
  public boolean isUnmarshalling ()
  {
    return unmarshalling;
  }
}
