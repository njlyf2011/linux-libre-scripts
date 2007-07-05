/* Divida - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.dividas;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class Divida extends ObjetoNegocio
{
  public static final String NOME_CAMPO_VALOR_ANTERIOR = "Situa\u00e7\u00e3o em 31/12/" + ConstantesGlobais.EXERCICIO_ANTERIOR;
  public static final String NOME_CAMPO_VALOR_ATUAL = "Situa\u00e7\u00e3o em 31/12/" + ConstantesGlobais.EXERCICIO;
  private Codigo codigo = new Codigo (this, "C\u00f3digo", CadastroTabelasIRPF.recuperarTipoDividas ());
  private Alfa discriminacao = new Alfa (this, "Discrimina\u00e7\u00e3o", 120);
  private Valor valorExercicioAnterior = new Valor (this, NOME_CAMPO_VALOR_ANTERIOR);
  private Valor valorExercicioAtual = new Valor (this, NOME_CAMPO_VALOR_ATUAL);
  
  public Divida ()
  {
    getCodigo ().setColunaFiltro (1);
    getCodigo ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("divida_codigo")));
    getDiscriminacao ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("divida_discriminacao")));
  }
  
  public Codigo getCodigo ()
  {
    return codigo;
  }
  
  public Alfa getDiscriminacao ()
  {
    return discriminacao;
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
    retorno.add (discriminacao);
    retorno.add (valorExercicioAtual);
    retorno.add (valorExercicioAnterior);
    return retorno;
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
  
  public List verificarPendencias (int numeroItem)
  {
    List retorno = super.verificarPendencias (numeroItem);
    if (getValorExercicioAtual ().isVazio () && getValorExercicioAnterior ().isVazio ())
      {
	Pendencia p = new Pendencia ((byte) 2, getValorExercicioAnterior (), getValorExercicioAnterior ().getNomeCampo (), tab.msg ("divida_valor_nao_informado"), numeroItem);
	retorno.add (p);
      }
    return retorno;
  }
}
