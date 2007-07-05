/* CalculosResumo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Observador;

public class CalculosResumo extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosResumo (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    declaracaoIRPF.getModeloCompleta ().resumoRendimentosTributaveis ();
    declaracaoIRPF.getModeloCompleta ().resumoCalculoImposto ();
    declaracaoIRPF.getModeloCompleta ().resumoOutrasInformacoes ();
    declaracaoIRPF.getModeloSimplificada ().resumoRendimentosTributaveis ();
    declaracaoIRPF.getModeloSimplificada ().resumoCalculoImposto ();
    declaracaoIRPF.getModeloSimplificada ().resumoOutrasInformacoes ();
    declaracaoIRPF.getModelo ().aplicaValoresNaDeclaracao ();
    declaracaoIRPF.getComparativo ().getTotalRendTribCompleta ().setConteudo (declaracaoIRPF.getModeloCompleta ().getTotalRendimentos ());
    declaracaoIRPF.getComparativo ().getBaseCalcCompleta ().setConteudo (declaracaoIRPF.getModeloCompleta ().getBaseCalculo ());
    declaracaoIRPF.getComparativo ().getSaldoPagarCompleta ().setConteudo (declaracaoIRPF.getModeloCompleta ().getSaldoImpostoPagar ());
    declaracaoIRPF.getComparativo ().getImpRestituirCompleta ().setConteudo (declaracaoIRPF.getModeloCompleta ().getImpostoRestituir ());
    declaracaoIRPF.getComparativo ().getTotalRendTribSimplificada ().setConteudo (declaracaoIRPF.getModeloSimplificada ().getTotalResultadosTributaveis ());
    declaracaoIRPF.getComparativo ().getBaseCalcSimplificada ().setConteudo (declaracaoIRPF.getModeloSimplificada ().getBaseCalculo ());
    declaracaoIRPF.getComparativo ().getSaldoPagarSimplificada ().setConteudo (declaracaoIRPF.getModeloSimplificada ().getSaldoImpostoPagar ());
    declaracaoIRPF.getComparativo ().getImpRestituirSimplificada ().setConteudo (declaracaoIRPF.getModeloSimplificada ().getImpostoRestituir ());
  }
}
