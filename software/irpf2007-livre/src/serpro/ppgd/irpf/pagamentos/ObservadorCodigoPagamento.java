/* ObservadorCodigoPagamento - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Observador;

public class ObservadorCodigoPagamento extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public ObservadorCodigoPagamento (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    Pagamento pagamento = (Pagamento) valorNovo;
	    pagamento.getCodigo ().addObservador (this);
	    String codigoPagamento = pagamento.getCodigo ().getConteudoAtual (0);
	    pagamento.getDependenteOuAlimentando ().setHabilitado (codigoPagamento.equals ("03") || codigoPagamento.equals ("04") || codigoPagamento.equals ("05") || codigoPagamento.equals ("06"));
	    pagamento.getCodigo ().disparaObservadores ();
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    Pagamento pagamento = (Pagamento) valorNovo;
	    pagamento.getCodigo ().removeObservador (this);
	  }
	else if (nomePropriedade.equals ("C\u00f3digo"))
	  {
	    Pagamento pagamento = (Pagamento) ((Informacao) observado).getOwner ();
	    String codigoPagamento = pagamento.getCodigo ().getConteudoAtual (0);
	    if (codigoPagamento.equals ("03") || codigoPagamento.equals ("04"))
	      {
		pagamento.getDependenteOuAlimentando ().setHabilitado (true);
		pagamento.getParcelaNaoDedutivel ().setHabilitado (true);
		pagamento.getNitEmpregadoDomestico ().setHabilitado (false);
		if (valorAntigo != null && (valorAntigo.equals ("05") || valorAntigo.equals ("06")))
		  pagamento.getDependenteOuAlimentando ().clear ();
	      }
	    else if (codigoPagamento.equals ("05") || codigoPagamento.equals ("06"))
	      {
		pagamento.getDependenteOuAlimentando ().setHabilitado (true);
		pagamento.getParcelaNaoDedutivel ().setHabilitado (true);
		pagamento.getNitEmpregadoDomestico ().setHabilitado (false);
		if (valorAntigo != null && (valorAntigo.equals ("03") || valorAntigo.equals ("04")))
		  pagamento.getDependenteOuAlimentando ().clear ();
	      }
	    else if (codigoPagamento.equals ("13") || codigoPagamento.equals ("14") || codigoPagamento.equals ("15") || codigoPagamento.equals ("17") || codigoPagamento.equals ("18") || codigoPagamento.equals ("19") || codigoPagamento.equals ("20") || codigoPagamento.equals ("21") || codigoPagamento.equals ("22") || codigoPagamento.equals ("23") || codigoPagamento.equals ("24") || codigoPagamento.equals ("25"))
	      {
		pagamento.getParcelaNaoDedutivel ().setHabilitado (false);
		pagamento.getParcelaNaoDedutivel ().clear ();
		if (codigoPagamento.equals ("18"))
		  {
		    pagamento.getNitEmpregadoDomestico ().setHabilitado (true);
		    pagamento.getNiBeneficiario ().setHabilitado (false);
		  }
		else
		  {
		    pagamento.getNitEmpregadoDomestico ().setHabilitado (false);
		    pagamento.getNitEmpregadoDomestico ().clear ();
		    pagamento.getNiBeneficiario ().setHabilitado (true);
		  }
	      }
	    else
	      {
		pagamento.getDependenteOuAlimentando ().clear ();
		pagamento.getDependenteOuAlimentando ().setHabilitado (false);
		pagamento.getParcelaNaoDedutivel ().clear ();
		pagamento.getParcelaNaoDedutivel ().setHabilitado (true);
		pagamento.getNitEmpregadoDomestico ().setHabilitado (false);
		pagamento.getNitEmpregadoDomestico ().clear ();
		pagamento.getNiBeneficiario ().setHabilitado (true);
	      }
	  }
      }
  }
}
