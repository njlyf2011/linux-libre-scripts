/* ObservadorCPFDependente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.dependentes.Dependente;
import serpro.ppgd.negocio.Observador;

public class ObservadorCPFDependente extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public ObservadorCPFDependente (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    Dependente dependente = (Dependente) valorNovo;
	    dependente.getCpfDependente ().addObservador (this);
	    dependente.setIdentificadorDeclaracao (declaracaoIRPF.getIdentificadorDeclaracao ());
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    Dependente dependente = (Dependente) valorNovo;
	    dependente.getCpfDependente ().removeObservador (this);
	  }
      }
  }
}
