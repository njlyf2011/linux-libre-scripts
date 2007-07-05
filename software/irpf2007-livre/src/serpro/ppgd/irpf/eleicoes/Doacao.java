/* Doacao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.eleicoes;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.gui.xbeans.JEditObjetoNegocioItemIf;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CNPJ;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCNPJ;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNome;

public class Doacao extends ObjetoNegocio implements JEditObjetoNegocioItemIf
{
  public static final String PROP_CAMPO_VALOR = "Valor da doa\u00e7\u00e3o";
  private String chave = "";
  private Alfa nome = new Alfa (this, "Nome do candidato, partido pol\u00edtico, ou comit\u00ea financeiro", 60);
  private CNPJ cnpj = new CNPJ (this, "CNPJ do candidato, partido pol\u00edtico, ou comit\u00ea financeiro");
  private Valor valor = new Valor (this, "Valor da doa\u00e7\u00e3o");
  
  public Doacao ()
  {
    getNome ().addValidador (new ValidadorNome ((byte) 3));
    getNome ().addValidador (new ValidadorNaoNulo ((byte) 3));
    getCNPJ ().addValidador (new ValidadorNaoNulo ((byte) 3));
    getCNPJ ().addValidador (new ValidadorCNPJ ((byte) 3));
    getValor ().addValidador (new ValidadorNaoNulo ((byte) 3));
  }
  
  public Alfa getNome ()
  {
    return nome;
  }
  
  public String toString ()
  {
    return nome.asString ();
  }
  
  public String getConteudo (int i)
  {
    return getNome ().getConteudoFormatado ();
  }
  
  public int getTotalAtributos ()
  {
    return 1;
  }
  
  public int getColunaFiltro ()
  {
    return 0;
  }
  
  public String getChave ()
  {
    return chave;
  }
  
  public void setChave (String chave)
  {
    this.chave = chave;
  }
  
  public CNPJ getCNPJ ()
  {
    return cnpj;
  }
  
  public void setCnpj (CNPJ cnpj)
  {
    this.cnpj = cnpj;
  }
  
  public Valor getValor ()
  {
    return valor;
  }
  
  public void setValor (Valor valor)
  {
    this.valor = valor;
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarCamposInformacao ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio ())
	  return false;
      }
    return true;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = super.recuperarListaCamposPendencia ();
    retorno.add (getNome ());
    retorno.add (getCNPJ ());
    retorno.add (getValor ());
    return retorno;
  }
}
