/* BemAR - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class BemAR extends ObjetoNegocio
{
  protected Codigo codigo = new Codigo (this, "C\u00f3digo", CadastroTabelasIRPF.recuperarTipoBensAR ());
  protected Alfa discriminacao = new Alfa (this, "Discrimina\u00e7\u00e3o", 150);
  protected Valor valor = new Valor (this, "Valor(R$)");
  
  public BemAR ()
  {
    getCodigo ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_bem_ar_codigo"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    });
    getDiscriminacao ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_bem_ar_discriminacao"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCodigo ().isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    });
  }
  
  public Codigo getCodigo ()
  {
    return codigo;
  }
  
  public Alfa getDiscriminacao ()
  {
    return discriminacao;
  }
  
  public Valor getValor ()
  {
    return valor;
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarCamposInformacao ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio ())
	  return false;
      }
    return true;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    return retorno;
  }
}
