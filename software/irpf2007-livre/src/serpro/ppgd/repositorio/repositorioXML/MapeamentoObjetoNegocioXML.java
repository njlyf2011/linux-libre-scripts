/* MapeamentoObjetoNegocioXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.util.Hashtable;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import serpro.ppgd.negocio.util.LogPPGD;

public class MapeamentoObjetoNegocioXML
{
  private static Hashtable mapeamentos = new Hashtable ();
  private Hashtable tabelaMapeamento = new Hashtable ();
  
  private MapeamentoObjetoNegocioXML (Document documentoMapeamento)
  {
    Element documentElement = documentoMapeamento.getDocumentElement ();
    AdaptadorXMLObjeto adaptadorRaiz = new AdaptadorXMLObjeto (documentElement);
    tabelaMapeamento.put ("Mapeamento", adaptadorRaiz);
    tabelaMapeamento.put (documentElement.getAttribute ("ElementoXML"), adaptadorRaiz);
    mapearChildNodes ("", documentElement);
  }
  
  private void mapearChildNodes (String identificaoAncestrais, Element nodo)
  {
    if (nodo.hasChildNodes ())
      {
	NodeList filhos = nodo.getChildNodes ();
	for (int i = 0; i < filhos.getLength (); i++)
	  {
	    if (filhos.item (i).getNodeType () == 1 && filhos.item (i).getNodeName ().equals ("Classe"))
	      {
		Element nodeFilho = (Element) filhos.item (i);
		String identificaoDesteNode;
		if (identificaoAncestrais.length () == 0)
		  identificaoDesteNode = nodeFilho.getAttribute ("ElementoXML");
		else
		  identificaoDesteNode = identificaoAncestrais + "." + nodeFilho.getAttribute ("ElementoXML");
		tabelaMapeamento.put (identificaoDesteNode, new AdaptadorXMLObjeto (nodeFilho));
		mapearChildNodes (identificaoDesteNode, nodeFilho);
	      }
	  }
      }
  }
  
  public static MapeamentoObjetoNegocioXML getMapeamento (Document documentoMapeamento)
  {
    String nomeObjetoRaiz = documentoMapeamento.getDocumentElement ().getAttribute ("ClasseJava");
    if (nomeObjetoRaiz == null && nomeObjetoRaiz.length () == 0)
      {
	LogPPGD.erro ("Mapeamento incorreto: o atributo \"ClasseJava\" est\u00e1 ausente ou incorreto");
	throw new IllegalArgumentException ("Mapeamento incorreto: o atributo \"ClasseJava\" est\u00e1 ausente ou incorreto");
      }
    if (mapeamentos.containsKey (nomeObjetoRaiz))
      return (MapeamentoObjetoNegocioXML) mapeamentos.get (nomeObjetoRaiz);
    MapeamentoObjetoNegocioXML novoMapeamento = new MapeamentoObjetoNegocioXML (documentoMapeamento);
    mapeamentos.put (nomeObjetoRaiz, novoMapeamento);
    return novoMapeamento;
  }
  
  private String determinaNomeCompleto (Node node)
  {
    if (node.getParentNode () != node.getOwnerDocument ().getDocumentElement ())
      return determinaNomeCompleto (node.getParentNode ()) + "." + node.getNodeName ();
    return node.getNodeName ();
  }
  
  public AdaptadorXMLObjeto getAdaptadorXMLObjeto (String nomeCompleto)
  {
    LogPPGD.debug ("Solicitado AdaptadorXMLObjeto de: " + nomeCompleto);
    return (AdaptadorXMLObjeto) tabelaMapeamento.get (nomeCompleto);
  }
  
  public Iterator iteratorAdaptadoresXMLObjeto ()
  {
    return tabelaMapeamento.keySet ().iterator ();
  }
}
