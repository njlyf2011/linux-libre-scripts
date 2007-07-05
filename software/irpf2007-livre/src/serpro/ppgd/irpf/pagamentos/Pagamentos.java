/* Pagamentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class Pagamentos extends Colecao
{
  protected transient IdentificadorDeclaracao identificadorDeclaracao;
  private Valor totalDeducoesInstrucao;
  private Valor totalContribuicaoFAPI;
  private Valor totalDespesasMedicas;
  private Valor totalPensao;
  private Valor totalDeducaoIncentivo;
  private Valor totalContribEmpregadoDomestico;
  
  public Pagamentos (IdentificadorDeclaracao id)
  {
    super (serpro.ppgd.irpf.pagamentos.Pagamento.class.getName ());
    identificadorDeclaracao = null;
    totalDeducoesInstrucao = new Valor (this, "");
    totalContribuicaoFAPI = new Valor (this, "");
    totalDespesasMedicas = new Valor (this, "");
    totalPensao = new Valor (this, "");
    totalDeducaoIncentivo = new Valor (this, "");
    totalContribEmpregadoDomestico = new Valor (this, "");
    identificadorDeclaracao = id;
    setFicha ("Pagamentos e Doa\u00e7\u00f5es Efetuados");
  }
  
  public void objetoInserido (Object o)
  {
    ((ObjetoNegocio) o).setFicha (getFicha ());
  }
  
  public ObjetoNegocio instanciaNovoObjeto ()
  {
    return new Pagamento (identificadorDeclaracao);
  }
  
  public Valor getTotalDeducoesInstrucao ()
  {
    return totalDeducoesInstrucao;
  }
  
  public Valor getTotalContribuicaoFAPI ()
  {
    return totalContribuicaoFAPI;
  }
  
  public Valor getTotalDespesasMedicas ()
  {
    return totalDespesasMedicas;
  }
  
  public Valor getTotalPensao ()
  {
    return totalPensao;
  }
  
  public Valor getTotalDeducaoIncentivo ()
  {
    return totalDeducaoIncentivo;
  }
  
  public String recuperarCpfMaiorPensaoAlimenticia ()
  {
    Iterator it = recuperarLista ().iterator ();
    String cpfMaior = "";
    Valor maiorValor = null;
    while (it.hasNext ())
      {
	Pagamento pgto = (Pagamento) it.next ();
	if (pgto.getCodigo ().asString ().equals ("12"))
	  {
	    if (maiorValor == null)
	      {
		maiorValor = pgto.getValorPago ();
		cpfMaior = pgto.getNiBeneficiario ().asString ().substring (0, 11);
	      }
	    else if (pgto.getValorPago ().comparacao (">", maiorValor))
	      {
		maiorValor = pgto.getValorPago ();
		cpfMaior = pgto.getNiBeneficiario ().asString ().substring (0, 11);
	      }
	  }
      }
    return cpfMaior;
  }
  
  public NI recuperarNIMaiorDespMedicas ()
  {
    Hashtable beneficiariosMedicas = new Hashtable ();
    List objColecao = recuperarLista ();
    class BeneficiarioMedico
    {
      public Valor total;
      public NI ni;
    };
    for (int i = 0; i < recuperarLista ().size (); i++)
      {
	Pagamento pgto = (Pagamento) objColecao.get (i);
	if (pgto.getCodigo ().toString ().equals ("07") || pgto.getCodigo ().toString ().equals ("09") || pgto.getCodigo ().toString ().equals ("11"))
	  {
	    Valor total = new Valor ();
	    if (pgto.getValorPago ().comparacao (">=", pgto.getParcelaNaoDedutivel ()))
	      {
		total.append ('+', pgto.getValorPago ());
		total.append ('-', pgto.getParcelaNaoDedutivel ());
	      }
	    BeneficiarioMedico medico = (BeneficiarioMedico) beneficiariosMedicas.get (pgto.getNiBeneficiario ());
	    if (medico == null)
	      {
		medico = new BeneficiarioMedico ();
		medico.total = total;
		medico.ni = pgto.getNiBeneficiario ();
		beneficiariosMedicas.put (medico.ni.asString (), medico);
	      }
	    else
	      medico.total.append ('+', total);
	  }
      }
    Iterator it = beneficiariosMedicas.values ().iterator ();
    BeneficiarioMedico benefMaior = null;
    if (it.hasNext ())
      {
	benefMaior = (BeneficiarioMedico) it.next ();
	while (it.hasNext ())
	  {
	    BeneficiarioMedico aux = (BeneficiarioMedico) it.next ();
	    if (aux.total.comparacao (">", benefMaior.total))
	      benefMaior = aux;
	  }
      }
    if (benefMaior == null)
      return new NI (this, "");
    return benefMaior.ni;
  }
  
  public NI recuperarNISegundoMaiorDespMedicas ()
  {
    Hashtable beneficiariosMedicas = new Hashtable ();
    List objColecao = recuperarLista ();
    class BeneficiarioMedico
    {
      public Valor total;
      public NI ni;
    };
    for (int i = 0; i < recuperarLista ().size (); i++)
      {
	Pagamento pgto = (Pagamento) objColecao.get (i);
	if (pgto.getCodigo ().toString ().equals ("07") || pgto.getCodigo ().toString ().equals ("09") || pgto.getCodigo ().toString ().equals ("11"))
	  {
	    Valor total = new Valor ();
	    if (pgto.getValorPago ().comparacao (">=", pgto.getParcelaNaoDedutivel ()))
	      {
		total.append ('+', pgto.getValorPago ());
		total.append ('-', pgto.getParcelaNaoDedutivel ());
	      }
	    BeneficiarioMedico medico = (BeneficiarioMedico) beneficiariosMedicas.get (pgto.getNiBeneficiario ());
	    if (medico == null)
	      {
		medico = new BeneficiarioMedico ();
		medico.total = total;
		medico.ni = pgto.getNiBeneficiario ();
		beneficiariosMedicas.put (medico.ni.asString (), medico);
	      }
	    else
	      medico.total.append ('+', total);
	  }
      }
    Iterator it = beneficiariosMedicas.values ().iterator ();
    BeneficiarioMedico benefMaior = null;
    BeneficiarioMedico segundoBenefMaior = null;
    if (it.hasNext ())
      {
	benefMaior = (BeneficiarioMedico) it.next ();
	while (it.hasNext ())
	  {
	    BeneficiarioMedico aux = (BeneficiarioMedico) it.next ();
	    if (aux.total.comparacao (">", benefMaior.total))
	      {
		segundoBenefMaior = benefMaior;
		benefMaior = aux;
	      }
	    else if (segundoBenefMaior == null || aux.total.comparacao (">", segundoBenefMaior.total))
	      segundoBenefMaior = aux;
	  }
      }
    if (segundoBenefMaior == null)
      return new NI (this, "");
    return segundoBenefMaior.ni;
  }
  
  public int obterTotalDependentesEnvolvidos ()
  {
    java.util.Set listaDependentes = new HashSet ();
    excluirRegistrosEmBranco ();
    Iterator itPagamentos = recuperarLista ().iterator ();
    while (itPagamentos.hasNext ())
      {
	Pagamento pagamento = (Pagamento) itPagamentos.next ();
	if (! pagamento.getDependenteOuAlimentando ().isVazio () && ! pagamento.getCodigo ().isVazio () && (pagamento.getCodigo ().getConteudoFormatado ().equals ("03") || pagamento.getCodigo ().getConteudoFormatado ().equals ("04")))
	  listaDependentes.add (pagamento.getDependenteOuAlimentando ().getConteudoFormatado ());
      }
    int tam = 0;
    if (! listaDependentes.isEmpty ())
      tam = listaDependentes.size ();
    return tam;
  }
  
  public int obterTotalAlimentandosEnvolvidos ()
  {
    java.util.Set listaDependentes = new HashSet ();
    excluirRegistrosEmBranco ();
    Iterator itPagamentos = recuperarLista ().iterator ();
    while (itPagamentos.hasNext ())
      {
	Pagamento pagamento = (Pagamento) itPagamentos.next ();
	if (! pagamento.getDependenteOuAlimentando ().isVazio () && ! pagamento.getCodigo ().isVazio () && (pagamento.getCodigo ().getConteudoFormatado ().equals ("05") || pagamento.getCodigo ().getConteudoFormatado ().equals ("06")))
	  listaDependentes.add (pagamento.getDependenteOuAlimentando ().getConteudoFormatado ());
      }
    int tam = 0;
    if (! listaDependentes.isEmpty ())
      tam = listaDependentes.size ();
    return tam;
  }
  
  public void setTotalContribEmpregadoDomestico (Valor totalContribEmpregadoDomestico)
  {
    this.totalContribEmpregadoDomestico = totalContribEmpregadoDomestico;
  }
  
  public Valor getTotalContribEmpregadoDomestico ()
  {
    return totalContribEmpregadoDomestico;
  }
}
