/* ObservadorRendaVariavel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Observador;

public class ObservadorRendaVariavel extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public ObservadorRendaVariavel (DeclaracaoIRPF aDeclaracaoIRPF)
  {
    declaracaoIRPF = aDeclaracaoIRPF;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (! declaracaoIRPF.getRendaVariavel ().getTotalImpostoRetidoFonteLei11033 ().isVazio ())
      declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ().setHabilitado (true);
    else
      {
	declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ().clear ();
	declaracaoIRPF.getImpostoPago ().getImpostoRetidoFonte ().setHabilitado (false);
      }
  }
}
