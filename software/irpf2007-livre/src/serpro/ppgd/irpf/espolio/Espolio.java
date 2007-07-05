/* Espolio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.espolio;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCPF;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNome;

public class Espolio extends ObjetoNegocio
{
  protected transient IdentificadorDeclaracao identificadorDeclaracao = null;
  private CPF cpfInventariante = new CPF (this, "CPF");
  private Alfa nomeInventariante = new Alfa (this, "Nome", 60);
  private Alfa endInventariante = new Alfa (this, "Endere\u00e7o", 70);
  
  public Espolio (IdentificadorDeclaracao id)
  {
    identificadorDeclaracao = id;
    setFicha ("Esp\u00f3lio");
    getCpfInventariante ().addValidador (new ValidadorCPF ((byte) 3));
    getCpfInventariante ().addValidador (new ValidadorDefault ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCpfInventariante ().asString ().equals (identificadorDeclaracao.getCpf ().asString ()))
	  return new RetornoValidacao (tab.msg ("espolio_cpf"), getSeveridade ());
	return null;
      }
    });
    getNomeInventariante ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("espolio_nome_branco"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCpfInventariante ().isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    });
    getCpfInventariante ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (! getNomeInventariante ().isVazio () && getCpfInventariante ().isVazio ())
	  return new RetornoValidacao (tab.msg ("espolio_cpf_branco"), getSeveridade ());
	return null;
      }
    });
    getNomeInventariante ().addValidador (new ValidadorNome ((byte) 3));
  }
  
  public void setCpfInventariante (CPF cpfInventariante)
  {
    this.cpfInventariante = cpfInventariante;
  }
  
  public CPF getCpfInventariante ()
  {
    return cpfInventariante;
  }
  
  public void setNomeInventariante (Alfa nomeInventariante)
  {
    this.nomeInventariante = nomeInventariante;
  }
  
  public Alfa getNomeInventariante ()
  {
    return nomeInventariante;
  }
  
  public void setEndInventariante (Alfa endInventariante)
  {
    this.endInventariante = endInventariante;
  }
  
  public Alfa getEndInventariante ()
  {
    return endInventariante;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    return retorno;
  }
}
