/* CalculosTotaisLivroCaixa - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Observador;

public class CalculosTotaisLivroCaixa extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosTotaisLivroCaixa (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null && nomePropriedade.equals ("Total Livro Caixa"))
      calculaTotalLivroCaixa ();
  }
  
  private void calculaTotalLivroCaixa ()
  {
    declaracaoIRPF.getModelo ().getTotalLivroCaixa ().clear ();
    declaracaoIRPF.getModelo ().getTotalLivroCaixa ().append ('+', declaracaoIRPF.getRendPFTitular ().getTotalLivroCaixa ());
    declaracaoIRPF.getModelo ().getTotalLivroCaixa ().append ('+', declaracaoIRPF.getRendPFDependente ().getTotalLivroCaixa ());
  }
}
