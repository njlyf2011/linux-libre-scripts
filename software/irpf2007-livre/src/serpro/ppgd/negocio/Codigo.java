/* Codigo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.List;
import java.util.Vector;

public class Codigo extends Informacao
{
  private List colecao;
  private boolean isSimples = false;
  private int colunaFiltro = -1;
  public static final String REINICIADO = "ComboReiniciado";
  
  public Codigo ()
  {
    this (null, "", new Vector ());
  }
  
  public Codigo (String nomeCampo)
  {
    this (null, nomeCampo, new Vector ());
  }
  
  public Codigo (ObjetoNegocio owner, String nomeCampo, List colecao)
  {
    super (owner, nomeCampo);
    this.colecao = colecao;
  }
  
  public Codigo (ObjetoNegocio owner, String nomeCampo, List colecao, boolean aIsSimples)
  {
    super (owner, nomeCampo);
    this.colecao = colecao;
    setSimples (aIsSimples);
  }
  
  public String getConteudoFormatado ()
  {
    return asString ();
  }
  
  public void setConteudo (int valor)
  {
    this.setConteudo (String.valueOf (valor));
  }
  
  public int asInteger ()
  {
    int i;
    try
      {
	i = Integer.parseInt (getConteudoFormatado ());
      }
    catch (Exception e)
      {
	return 0;
      }
    return i;
  }
  
  public boolean existeElementoTabela (int col, String param)
  {
    ElementoTabela e = retornaElementoTabela (col, param);
    return e != null;
  }
  
  public ElementoTabela retornaElementoTabela (int col, String param)
  {
    for (int i = 0; i < colecao.size (); i++)
      {
	ElementoTabela elementoTabela = (ElementoTabela) colecao.get (i);
	if (elementoTabela.getConteudo (col).equalsIgnoreCase (param))
	  return elementoTabela;
      }
    return null;
  }
  
  public List getColecaoElementoTabela ()
  {
    return colecao;
  }
  
  public void setColecaoElementoTabela (List colecao)
  {
    this.colecao = colecao;
    getObservadores ().firePropertyChange ("ComboReiniciado", "1", "2");
    if (getIndiceElementoTabela () == -1)
      clear ();
  }
  
  public ElementoTabela getElementoTabela (int i)
  {
    if (i == -1)
      return null;
    return (ElementoTabela) colecao.get (i);
  }
  
  public ElementoTabela getElementoTabela ()
  {
    int i = getIndiceElementoTabela ();
    if (i == -1)
      return null;
    return (ElementoTabela) colecao.get (i);
  }
  
  public int getIndiceElementoTabela ()
  {
    if (colecao != null)
      {
	for (int i = 0; i < colecao.size (); i++)
	  {
	    ElementoTabela elementoTabela = (ElementoTabela) colecao.get (i);
	    if (elementoTabela.getConteudo (0).toString ().equalsIgnoreCase (getConteudoFormatado ()))
	      return i;
	  }
      }
    return -1;
  }
  
  public String getConteudoAtual (int coluna)
  {
    ElementoTabela elementoTabela = getElementoTabela ();
    if (elementoTabela != null)
      return elementoTabela.getConteudo (coluna).toString ();
    return "";
  }
  
  public String getDescricaoDefault ()
  {
    ElementoTabela elementoTabela = getElementoTabela ();
    if (elementoTabela != null && elementoTabela.size () > 2)
      {
	String descricao = elementoTabela.getConteudo (1);
	if (descricao != null)
	  return descricao;
      }
    return "";
  }
  
  public boolean isSimples ()
  {
    return isSimples;
  }
  
  public void setSimples (boolean isSimples)
  {
    this.isSimples = isSimples;
  }
  
  public int getColunaFiltro ()
  {
    return colunaFiltro;
  }
  
  public void setColunaFiltro (int colunaFiltro)
  {
    this.colunaFiltro = colunaFiltro;
  }
}
