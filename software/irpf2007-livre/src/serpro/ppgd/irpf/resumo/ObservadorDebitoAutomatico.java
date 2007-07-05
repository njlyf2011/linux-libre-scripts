/* ObservadorDebitoAutomatico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.resumo;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.Observador;

public class ObservadorDebitoAutomatico extends Observador
{
  private CalculoImposto calculoImposto = null;
  private DeclaracaoIRPF dec;
  private IdentificadorDeclaracao identificadorDec;
  private Contribuinte contribuinte;
  private boolean eraImpostoRestituir = false;
  
  public ObservadorDebitoAutomatico (CalculoImposto calc, IdentificadorDeclaracao aIdentificadorDec, Contribuinte aContribuinte)
  {
    calculoImposto = calc;
    identificadorDec = aIdentificadorDec;
    contribuinte = aContribuinte;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (! "habilitado".equals (nomePropriedade))
      habilitadesabilitaDadosBancarios ();
  }
  
  public void habilitadesabilitaDadosBancarios ()
  {
    boolean temDebitoAutomatico = calculoImposto.getDebitoAutomatico ().asString ().equals ("autorizado");
    boolean impostoArestituir = calculoImposto.getImpostoRestituir ().comparacao (">", "0,00");
    if (impostoArestituir)
      {
	calculoImposto.getBanco ().setHabilitado (true);
	calculoImposto.getAgencia ().setReadOnly (false);
	calculoImposto.getDvAgencia ().setReadOnly (false);
	calculoImposto.getContaCredito ().setReadOnly (false);
	calculoImposto.getDvContaCredito ().setReadOnly (false);
	calculoImposto.getDebitoAutomatico ().setConteudo ("N");
	if (! eraImpostoRestituir)
	  {
	    calculoImposto.getBanco ().clear ();
	    calculoImposto.getAgencia ().clear ();
	    calculoImposto.getDvAgencia ().clear ();
	    calculoImposto.getContaCredito ().clear ();
	    calculoImposto.getDvContaCredito ().clear ();
	  }
	calculoImposto.getBanco ().setColecaoElementoTabela (CadastroTabelasIRPF.recuperarBancos ());
	if (contribuinte.getExterior ().asString ().equals (Logico.SIM))
	  {
	    calculoImposto.getBanco ().setHabilitado (false);
	    calculoImposto.getAgencia ().setReadOnly (true);
	    calculoImposto.getDvAgencia ().setReadOnly (true);
	    calculoImposto.getContaCredito ().setReadOnly (true);
	    calculoImposto.getDvContaCredito ().setReadOnly (true);
	    calculoImposto.getBanco ().setConteudo ("001");
	    calculoImposto.getAgencia ().setConteudo ("0686");
	    calculoImposto.getDvAgencia ().setConteudo ("6");
	  }
	eraImpostoRestituir = impostoArestituir;
      }
    else if (identificadorDec.getDeclaracaoRetificadora ().asString ().equals (Logico.SIM))
      {
	calculoImposto.getBanco ().setHabilitado (false);
	calculoImposto.getAgencia ().setReadOnly (true);
	calculoImposto.getDvAgencia ().setReadOnly (true);
	calculoImposto.getContaCredito ().setReadOnly (true);
	calculoImposto.getDvContaCredito ().setReadOnly (true);
	calculoImposto.getDebitoAutomatico ().setConteudo ("N");
	calculoImposto.getBanco ().clear ();
	calculoImposto.getAgencia ().clear ();
	calculoImposto.getDvAgencia ().clear ();
	calculoImposto.getContaCredito ().clear ();
	calculoImposto.getDvContaCredito ().clear ();
      }
    else
      {
	calculoImposto.getBanco ().setHabilitado (temDebitoAutomatico);
	calculoImposto.getAgencia ().setReadOnly (! temDebitoAutomatico);
	calculoImposto.getDvAgencia ().setReadOnly (! temDebitoAutomatico);
	calculoImposto.getContaCredito ().setReadOnly (! temDebitoAutomatico);
	calculoImposto.getDvContaCredito ().setReadOnly (! temDebitoAutomatico);
	if (! temDebitoAutomatico)
	  {
	    calculoImposto.getBanco ().clear ();
	    calculoImposto.getAgencia ().clear ();
	    calculoImposto.getDvAgencia ().clear ();
	    calculoImposto.getContaCredito ().clear ();
	    calculoImposto.getDvContaCredito ().clear ();
	  }
	if (temDebitoAutomatico)
	  calculoImposto.getBanco ().setColecaoElementoTabela (CadastroTabelasIRPF.recuperarBancosDebito ());
      }
  }
}
