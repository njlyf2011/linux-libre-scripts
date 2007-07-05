/* InterpretadorOperacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.interpretador;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class InterpretadorOperacoes
{
  private Map observadores = new Hashtable ();
  private List operacoes = new Vector ();
  private String xmlOperacoes = null;
  private ObjetoNegocio instanciaObjetoNegocio = null;
  private Integer bitObservadorAtivo = new Integer (0);
  private static Map xmlsCarregados = new Hashtable ();
  
  class ObservadorGeral implements PropertyChangeListener
  {
    private String identificador = null;
    private boolean ativo = true;
    
    public ObservadorGeral (String pIdentificador)
    {
      identificador = pIdentificador;
    }
    
    public void propertyChange (PropertyChangeEvent evt)
    {
      atualizacao ();
    }
    
    private synchronized void atualizacao ()
    {
      if (ativo)
	{
	  Integer integer;
	  synchronized (integer = bitObservadorAtivo)
	    {
	      ativo = false;
	      Iterator itOperacoes = operacoes.iterator ();
	      while (itOperacoes.hasNext ())
		{
		  OperadorMatematico op = (OperadorMatematico) itOperacoes.next ();
		  if (op.getIdentificador ().equals ("todos") || op.getIdentificador ().equals (identificador))
		    op.atualiza ();
		}
	      ativo = true;
	    }
	}
    }
    
    public boolean equals (Object o)
    {
      if (o instanceof ObservadorGeral)
	{
	  ObservadorGeral outro = (ObservadorGeral) o;
	  return outro.getIdentificador ().equals (getIdentificador ());
	}
      return super.equals (o);
    }
    
    public String getIdentificador ()
    {
      return identificador;
    }
  }
  
  public InterpretadorOperacoes (ObjetoNegocio pObj, String pXmlOperacoes)
  {
    xmlOperacoes = pXmlOperacoes;
    instanciaObjetoNegocio = pObj;
    InstanciadorOperacoes.getInstance ().instanciaOperacao (this);
  }
  
  private synchronized Document obtemDocumento (String xmlOperacoes)
  {
    Document definicoes = null;
    if (! xmlsCarregados.containsKey (xmlOperacoes))
      {
	definicoes = FabricaUtilitarios.carregarDOM (xmlOperacoes);
	xmlsCarregados.put (xmlOperacoes, definicoes);
      }
    else
      definicoes = (Document) xmlsCarregados.get (xmlOperacoes);
    return definicoes;
  }
  
  public void traduzXML ()
  {
    Document definicoes = obtemDocumento (xmlOperacoes);
    Element raiz = definicoes.getDocumentElement ();
    if (raiz.hasChildNodes ())
      {
	NodeList filhos = raiz.getChildNodes ();
	for (int i = 0; i < filhos.getLength (); i++)
	  {
	    if (filhos.item (i).getNodeType () == 1)
	      {
		Element elementFilho = (Element) filhos.item (i);
		if (elementFilho.getNodeName ().trim ().equalsIgnoreCase ("operacao"))
		  carregaOperacao (elementFilho);
	      }
	  }
      }
  }
  
  private void carregaOperacao (Element nodoOperacao)
  {
    Map atributos = carregaIdApelidos (nodoOperacao);
    String identificador = nodoOperacao.getAttribute ("identificador");
    String receptor = nodoOperacao.getAttribute ("receptor");
    String strOperacao = null;
    for (int n = 0; n < nodoOperacao.getChildNodes ().getLength () - 1; n++)
      {
	if (nodoOperacao.getChildNodes ().item (n).getNodeType () == 4)
	  {
	    strOperacao = nodoOperacao.getChildNodes ().item (n).getNodeValue ();
	    n = nodoOperacao.getChildNodes ().getLength () + 1;
	  }
      }
    strOperacao = strOperacao.replaceAll ("\n", " ");
    strOperacao = strOperacao.replaceAll ("\t", " ");
    if (identificador == null || identificador.trim ().length () == 0)
      identificador = "todos";
    OperadorMatematico operacao = new OperadorMatematico (strOperacao, instanciaObjetoNegocio, atributos, receptor, identificador, getObservador (identificador));
    Integer integer;
    synchronized (integer = bitObservadorAtivo)
      {
	operacoes.add (operacao);
      }
    operacao.atualiza ();
  }
  
  private PropertyChangeListener getObservador (String nome)
  {
    if (! observadores.containsKey (nome))
      observadores.put (nome, new ObservadorGeral (nome));
    return (PropertyChangeListener) observadores.get (nome);
  }
  
  private Map carregaIdApelidos (Element nodoOperacao)
  {
    Map atributos = new Hashtable ();
    if (nodoOperacao.hasChildNodes ())
      {
	NodeList filhos = nodoOperacao.getChildNodes ();
	for (int i = 0; i < filhos.getLength (); i++)
	  {
	    if (filhos.item (i).getNodeType () == 1)
	      {
		Element elementFilho = (Element) filhos.item (i);
		if (elementFilho.getNodeName ().trim ().equalsIgnoreCase ("atributo"))
		  {
		    String acesso = elementFilho.getAttribute ("conteudo");
		    String apelido = elementFilho.getAttribute ("id");
		    if (apelido == null || apelido.trim ().length () == 0)
		      apelido = acesso;
		    atributos.put (apelido, acesso);
		  }
	      }
	  }
      }
    return atributos;
  }
  
  private Map insereObservadoresOutrosAtributos (Element nodoOperacao)
  {
    Map atributos = new Hashtable ();
    if (nodoOperacao.hasChildNodes ())
      {
	NodeList filhos = nodoOperacao.getChildNodes ();
	for (int i = 0; i < filhos.getLength (); i++)
	  {
	    if (filhos.item (i).getNodeType () == 1)
	      {
		Element elementFilho = (Element) filhos.item (i);
		if (elementFilho.getNodeName ().trim ().equalsIgnoreCase ("observador"))
		  {
		    String atributo = elementFilho.getAttribute ("atributo");
		    if (atributo != null && atributo.trim ().length () != 0)
		      {
			Informacao info = (Informacao) FabricaUtilitarios.obtemAtributo (instanciaObjetoNegocio, atributo);
			String identificador = nodoOperacao.getAttribute ("identificador");
			if (identificador == null || identificador.trim ().length () == 0)
			  identificador = "todos";
			info.getObservadores ().addPropertyChangeListener (getObservador (identificador));
		      }
		  }
	      }
	  }
      }
    return atributos;
  }
}
