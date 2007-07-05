/* CalculosTotalRendRecebidosMaisExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Observador;

public class CalculosTotalRendRecebidosMaisExterior extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosTotalRendRecebidosMaisExterior (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null && (nomePropriedade.equals ("Totais Rend. Recebid") || nomePropriedade.equals ("Total Pessoa F\u00edsica") || nomePropriedade.equals ("Total Exterior")))
      calculaTotalRendimentosTributaveis ();
  }
  
  private void calculaTotalRendimentosTributaveis ()
  {
    declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().clear ();
    declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().append ('+', declaracaoIRPF.getColecaoRendPJTitular ().getTotaisRendRecebidoPJ ());
    declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().append ('+', declaracaoIRPF.getColecaoRendPJDependente ().getTotaisRendRecebidoPJ ());
    declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().append ('+', declaracaoIRPF.getRendPFTitular ().getTotalPessoaFisica ());
    declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().append ('+', declaracaoIRPF.getRendPFTitular ().getTotalExterior ());
    declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().append ('+', declaracaoIRPF.getRendPFDependente ().getTotalPessoaFisica ());
    declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ().append ('+', declaracaoIRPF.getRendPFDependente ().getTotalExterior ());
    declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getTotalRendimentos ().setConteudo (declaracaoIRPF.getModelo ().getTotalRendRecebidosMaisExterior ());
  }
}
