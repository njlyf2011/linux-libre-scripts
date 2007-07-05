/* CalculosApuracaoResultadoARBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.atividaderural.brasil.ApuracaoResultadoBrasil;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosApuracaoResultadoARBrasil extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosApuracaoResultadoARBrasil (DeclaracaoIRPF dec)
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
    ApuracaoResultadoBrasil apuracaoResultadoBrasil = declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ();
    apuracaoResultadoBrasil.getReceitaBrutaTotal ().setConteudo (declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getTotalReceita ());
    apuracaoResultadoBrasil.getDespesaCusteio ().setConteudo (declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getTotalDespesas ());
    apuracaoResultadoBrasil.getResultadoI ().setConteudo (apuracaoResultadoBrasil.getReceitaBrutaTotal ().operacao ('-', apuracaoResultadoBrasil.getDespesaCusteio ()));
    apuracaoResultadoBrasil.getResultadoAposCompensacaoPrejuizo ().setConteudo (apuracaoResultadoBrasil.getResultadoI ().operacao ('-', apuracaoResultadoBrasil.getPrejuizoExercicioAnterior ()));
    apuracaoResultadoBrasil.getOpcaoArbitramento ().setConteudo (apuracaoResultadoBrasil.getReceitaBrutaTotal ().operacao ('*', "0,20"));
    if (apuracaoResultadoBrasil.getResultadoAposCompensacaoPrejuizo ().comparacao ("<", apuracaoResultadoBrasil.getOpcaoArbitramento ()))
      apuracaoResultadoBrasil.getResultadoTributavel ().setConteudo (apuracaoResultadoBrasil.getResultadoAposCompensacaoPrejuizo ());
    else
      apuracaoResultadoBrasil.getResultadoTributavel ().setConteudo (apuracaoResultadoBrasil.getOpcaoArbitramento ());
    if (apuracaoResultadoBrasil.getResultadoTributavel ().comparacao ("<", "0,00") && declaracaoIRPF.getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("0"))
      apuracaoResultadoBrasil.getPrejuizoCompensar ().setConteudo (apuracaoResultadoBrasil.getResultadoTributavel ().getConteudoAbsoluto ());
    else
      apuracaoResultadoBrasil.getPrejuizoCompensar ().clear ();
    Valor resultNaoTributavel = new Valor ();
    resultNaoTributavel.append ('+', apuracaoResultadoBrasil.getReceitaBrutaTotal ());
    resultNaoTributavel.append ('-', apuracaoResultadoBrasil.getDespesaCusteio ());
    resultNaoTributavel.append ('+', apuracaoResultadoBrasil.getReceitaRecebidaContaVenda ());
    resultNaoTributavel.append ('-', apuracaoResultadoBrasil.getValorAdiantamento ());
    if (apuracaoResultadoBrasil.getResultadoTributavel ().comparacao (">=", "0,00"))
      resultNaoTributavel.append ('-', apuracaoResultadoBrasil.getResultadoTributavel ());
    if (resultNaoTributavel.comparacao ("<", "0,00"))
      resultNaoTributavel.clear ();
    apuracaoResultadoBrasil.getResultadoNaoTributavel ().setConteudo (resultNaoTributavel);
  }
}
