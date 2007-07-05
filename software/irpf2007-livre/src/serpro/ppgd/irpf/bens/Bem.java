/* Bem - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.bens;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class Bem extends ObjetoNegocio
{
  public static final String NOME_CAMPO_VALOR_ANTERIOR = "Situa\u00e7\u00e3o em 31/12/" + ConstantesGlobais.EXERCICIO_ANTERIOR;
  public static final String NOME_CAMPO_VALOR_ATUAL = "Situa\u00e7\u00e3o em 31/12/" + ConstantesGlobais.EXERCICIO;
  private Codigo codigo = new Codigo (this, "C\u00f3digo", CadastroTabelasIRPF.recuperarTipoBens ());
  private Codigo pais = new Codigo (this, "Localiza\u00e7\u00e3o(Pa\u00eds)", CadastroTabelasIRPF.recuperarPaises ());
  private Alfa discriminacao = new Alfa (this, "Discrimina\u00e7\u00e3o", 510);
  private Valor valorExercicioAnterior = new Valor (this, NOME_CAMPO_VALOR_ANTERIOR);
  private Valor valorExercicioAtual = new Valor (this, NOME_CAMPO_VALOR_ATUAL);
  public static final String BRASIL = "0";
  public static final String EXTERIOR = "1";
  
  public Bem ()
  {
    getCodigo ().setColunaFiltro (1);
    getPais ().setColunaFiltro (1);
    getCodigo ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("bem_codigo")));
    getDiscriminacao ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("bem_discriminacao")));
    getPais ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("bem_pais_branco")));
    getPais ().addValidador (new ValidadorDefault ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getPais ().getConteudoFormatado ().equals ("105"))
	  {
	    if (getCodigo ().getConteudoAtual (0).equals ("62"))
	      return new RetornoValidacao (tab.msg ("bem_pais_imcompativel"), (byte) 3);
	  }
	else if (getCodigo ().getConteudoAtual (0).equals ("61"))
	  return new RetornoValidacao (tab.msg ("bem_pais_imcompativel"), (byte) 3);
	return null;
      }
    });
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarCamposInformacao ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio () && ! informacao.getNomeCampo ().equals ("Localiza\u00e7\u00e3o(Pa\u00eds)"))
	  return false;
      }
    return true;
  }
  
  public Codigo getCodigo ()
  {
    return codigo;
  }
  
  public Alfa getDiscriminacao ()
  {
    return discriminacao;
  }
  
  public Codigo getPais ()
  {
    return pais;
  }
  
  public Valor getValorExercicioAnterior ()
  {
    return valorExercicioAnterior;
  }
  
  public Valor getValorExercicioAtual ()
  {
    return valorExercicioAtual;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = super.recuperarListaCamposPendencia ();
    retorno.add (codigo);
    retorno.add (pais);
    retorno.add (discriminacao);
    retorno.add (valorExercicioAtual);
    retorno.add (valorExercicioAnterior);
    return retorno;
  }
  
  public List verificarPendencias (int numeroItem)
  {
    List retorno = super.verificarPendencias (numeroItem);
    if (getValorExercicioAtual ().isVazio () && getValorExercicioAnterior ().isVazio ())
      {
	Pendencia p = new Pendencia ((byte) 2, getValorExercicioAnterior (), getValorExercicioAnterior ().getNomeCampo (), tab.msg ("bem_valor_nao_informado"), numeroItem);
	retorno.add (p);
      }
    return retorno;
  }
}
