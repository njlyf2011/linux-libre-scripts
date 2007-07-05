/* CalculosApuracaoResultadoARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.atividaderural.exterior.ApuracaoResultadoExterior;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosApuracaoResultadoARExterior extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosApuracaoResultadoARExterior (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      calculaApuracao ();
  }
  
  private void calculaApuracao ()
  {
    ApuracaoResultadoExterior apuracaoResultadoExterior = declaracaoIRPF.getAtividadeRural ().getExterior ().getApuracaoResultado ();
    apuracaoResultadoExterior.getResultadoI_EmDolar ().setConteudo (declaracaoIRPF.getAtividadeRural ().getExterior ().getReceitasDespesas ().getTotais ());
    Valor resultadoIReais = new Valor ();
    resultadoIReais.converteQtdCasasDecimais (4);
    resultadoIReais.setConteudo (apuracaoResultadoExterior.getResultadoI_EmDolar ());
    resultadoIReais.append ('*', "2,1372");
    apuracaoResultadoExterior.getResultadoI_EmReais ().setConteudo (resultadoIReais);
    apuracaoResultadoExterior.getResultadoAposCompensacaoPrejuizo ().setConteudo (apuracaoResultadoExterior.getResultadoI_EmReais ().operacao ('-', apuracaoResultadoExterior.getPrejuizoExercicioAnterior ()));
    if (apuracaoResultadoExterior.getResultadoAposCompensacaoPrejuizo ().comparacao ("<", apuracaoResultadoExterior.getOpcaoArbitramento ()) || apuracaoResultadoExterior.getOpcaoArbitramento ().isVazio ())
      apuracaoResultadoExterior.getResultadoTributavel ().setConteudo (apuracaoResultadoExterior.getResultadoAposCompensacaoPrejuizo ());
    else
      apuracaoResultadoExterior.getResultadoTributavel ().setConteudo (apuracaoResultadoExterior.getOpcaoArbitramento ());
    if (apuracaoResultadoExterior.getResultadoTributavel ().comparacao ("<", "0,00") && declaracaoIRPF.getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("0"))
      apuracaoResultadoExterior.getPrejuizoCompensar ().setConteudo (apuracaoResultadoExterior.getResultadoTributavel ().getConteudoAbsoluto ());
    else
      apuracaoResultadoExterior.getPrejuizoCompensar ().clear ();
    Valor resultNaoTributavel = new Valor ();
    resultNaoTributavel.append ('+', apuracaoResultadoExterior.getResultadoI_EmReais ());
    resultNaoTributavel.append ('+', apuracaoResultadoExterior.getReceitaRecebidaContaVenda ());
    resultNaoTributavel.append ('-', apuracaoResultadoExterior.getValorAdiantamento ());
    if (apuracaoResultadoExterior.getResultadoTributavel ().comparacao (">=", "0,00"))
      resultNaoTributavel.append ('-', apuracaoResultadoExterior.getResultadoTributavel ());
    if (resultNaoTributavel.comparacao ("<", "0,00"))
      resultNaoTributavel.clear ();
    apuracaoResultadoExterior.getResultadoNaoTributavel ().setConteudo (resultNaoTributavel);
  }
}
