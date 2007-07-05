/* CalculosPagamentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.pagamentos.Pagamento;
import serpro.ppgd.irpf.pagamentos.Pagamentos;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosPagamentos extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosPagamentos (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    ((Pagamento) valorNovo).getCodigo ().addObservador (this);
	    ((Pagamento) valorNovo).getValorPago ().addObservador (this);
	    ((Pagamento) valorNovo).getParcelaNaoDedutivel ().addObservador (this);
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    ((Pagamento) valorNovo).getValorPago ().removeObservador (this);
	    ((Pagamento) valorNovo).getParcelaNaoDedutivel ().removeObservador (this);
	    ((Pagamento) valorNovo).getCodigo ().removeObservador (this);
	  }
      }
    calculaTotalDeducoesInstrucao ();
    calculaTotalContribuicaoFAPI ();
    calculaTotalDespesasMedicas ();
    CalculosDeducoesIncentivos.calculaDeducaoIncentivo (declaracaoIRPF);
    calculaTotalPensao ();
    calculaTotalContribEmpregadoDomestico ();
  }
  
  private void calculaTotalContribEmpregadoDomestico ()
  {
    Valor total = new Valor ();
    Iterator it = declaracaoIRPF.getPagamentos ().recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Pagamento pag = (Pagamento) it.next ();
	if (pag.getCodigo ().asString ().equals ("18"))
	  total.append ('+', pag.getValorPago ());
      }
    declaracaoIRPF.getPagamentos ().getTotalContribEmpregadoDomestico ().setConteudo (total);
  }
  
  private void calculaTotalPensao ()
  {
    declaracaoIRPF.getPagamentos ().getTotalPensao ().setConteudo (totalizarPagamentos (declaracaoIRPF.getPagamentos (), new String[] { "12" }, true));
  }
  
  private void calculaTotalDeducoesInstrucao ()
  {
    Valor valTotalDespesasInstrucao = new Valor ();
    Valor despesasInstrucaoPropria = new Valor ();
    despesasInstrucaoPropria.setConteudo (totalizarPagamentos (declaracaoIRPF.getPagamentos (), new String[] { "01", "02" }, true));
    if (despesasInstrucaoPropria.comparacao (">", "2.373,84"))
      despesasInstrucaoPropria.setConteudo ("2.373,84");
    Valor despesasInstrucaoDep = new Valor ();
    despesasInstrucaoDep.setConteudo (totalizarPagamentosPorBeneficiarioComLimite (declaracaoIRPF.getPagamentos (), new String[] { "03", "04" }));
    Valor despesasInstrucaoAlimentando = new Valor ();
    despesasInstrucaoAlimentando.setConteudo (totalizarPagamentosPorBeneficiarioComLimite (declaracaoIRPF.getPagamentos (), new String[] { "05", "06" }));
    valTotalDespesasInstrucao.append ('+', despesasInstrucaoPropria);
    valTotalDespesasInstrucao.append ('+', despesasInstrucaoDep);
    valTotalDespesasInstrucao.append ('+', despesasInstrucaoAlimentando);
    declaracaoIRPF.getPagamentos ().getTotalDeducoesInstrucao ().setConteudo (valTotalDespesasInstrucao);
  }
  
  private void calculaTotalContribuicaoFAPI ()
  {
    Valor contribPrevPrivadaFAPI = new Valor ();
    contribPrevPrivadaFAPI.setConteudo (totalizarPagamentos (declaracaoIRPF.getPagamentos (), new String[] { "13", "14" }, false));
    if (contribPrevPrivadaFAPI.comparacao (">", declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getTotalRendimentos ().operacao ('*', "0,12")))
      contribPrevPrivadaFAPI.setConteudo (declaracaoIRPF.getResumo ().getRendimentosTributaveisDeducoes ().getTotalRendimentos ().operacao ('*', "0,12"));
    declaracaoIRPF.getPagamentos ().getTotalContribuicaoFAPI ().setConteudo (contribPrevPrivadaFAPI);
  }
  
  private void calculaTotalDespesasMedicas ()
  {
    Valor totalDespMedicas = totalizarPagamentos (declaracaoIRPF.getPagamentos (), new String[] { "07", "08", "09", "10", "11" }, true);
    declaracaoIRPF.getPagamentos ().getTotalDespesasMedicas ().setConteudo (totalDespMedicas);
  }
  
  public static Valor totalizarPagamentosGlosado (Pagamentos pagamentos, String[] codigo, boolean abateParcelaNaoDedutivel)
  {
    Valor result = totalizarPagamentos (pagamentos, codigo, abateParcelaNaoDedutivel);
    if (result.comparacao ("<", "0,00"))
      result.clear ();
    return result;
  }
  
  public static Valor totalizarPagamentos (Pagamentos pagamentos, String[] codigo, boolean abateParcelaNaoDedutivel)
  {
    Valor total = new Valor ();
    List objColecao = pagamentos.recuperarLista ();
    for (int i = 0; i <= pagamentos.recuperarLista ().size () - 1; i++)
      {
	Pagamento obj = (Pagamento) objColecao.get (i);
	for (int j = 0; j < codigo.length; j++)
	  {
	    if (obj.getCodigo ().toString ().equals (codigo[j]) && obj.getValorPago ().comparacao (">=", obj.getParcelaNaoDedutivel ()))
	      {
		total.append ('+', obj.getValorPago ());
		if (abateParcelaNaoDedutivel)
		  total.append ('-', obj.getParcelaNaoDedutivel ());
	      }
	  }
      }
    return total;
  }
  
  public static Valor totalizarPagamentosPorBeneficiarioComLimite (Pagamentos pagamentos, String[] codigo)
  {
    Valor retorno = new Valor ();
    java.util.Map mapBeneficiarios = new Hashtable ();
    List objColecao = pagamentos.recuperarLista ();
    class Beneficiario
    {
      public String nome = null;
      public Valor total = new Valor ();
      
      public Beneficiario (String pNome)
      {
	nome = pNome;
      }
    };
    for (int i = 0; i <= pagamentos.recuperarLista ().size () - 1; i++)
      {
	Pagamento obj = (Pagamento) objColecao.get (i);
	for (int j = 0; j < codigo.length; j++)
	  {
	    if (obj.getCodigo ().toString ().equals (codigo[j]))
	      {
		Beneficiario beneficiario = null;
		if (! mapBeneficiarios.containsKey (obj.getDependenteOuAlimentando ().asString ()))
		  {
		    beneficiario = new Beneficiario (obj.getDependenteOuAlimentando ().asString ());
		    mapBeneficiarios.put (beneficiario.nome, beneficiario);
		  }
		else
		  beneficiario = (Beneficiario) mapBeneficiarios.get (obj.getDependenteOuAlimentando ().asString ());
		if (obj.getValorPago ().comparacao (">=", obj.getParcelaNaoDedutivel ()))
		  {
		    beneficiario.total.append ('+', obj.getValorPago ());
		    beneficiario.total.append ('-', obj.getParcelaNaoDedutivel ());
		  }
	      }
	  }
      }
    Iterator it = mapBeneficiarios.values ().iterator ();
    while (it.hasNext ())
      {
	Beneficiario b = (Beneficiario) it.next ();
	if (b.total.comparacao (">", "2.373,84"))
	  b.total.setConteudo ("2.373,84");
	retorno.append ('+', b.total);
      }
    return retorno;
  }
}
