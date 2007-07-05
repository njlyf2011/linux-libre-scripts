/* ImovelAR - Decompiled by JODE
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

public class ImovelAR extends ObjetoNegocio
{
  protected Codigo codigo = new Codigo (this, "C\u00f3digo", CadastroTabelasIRPF.recuperarTipoAtividadesRural ());
  protected Alfa nome = new Alfa (this, "Nome do Im\u00f3vel", 60);
  protected Alfa localizacao = new Alfa (this, "Localiza\u00e7\u00e3o do Im\u00f3vel", 55);
  protected Valor area = new Valor (this, "\u00c1rea(ha)");
  protected Valor participacao = new Valor (this, "Participa\u00e7\u00e3o(%)");
  protected Codigo condicaoExploracao = new Codigo (this, "Condi\u00e7\u00e3o de Explora\u00e7\u00e3o", CadastroTabelasIRPF.recuperarCondicoesExploracao ());
  
  public ImovelAR ()
  {
    getParticipacao ().setMaximoDigitosParteInteira (3);
    getArea ().setMaximoDigitosParteInteira (9);
    getArea ().converteQtdCasasDecimais (1);
    getCodigo ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_imovel_ar_codigo"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    });
    getNome ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_imovel_ar_nome"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCodigo ().isVazio ())
	  return null;
	String codigo = getCodigo ().getConteudoAtual (0);
	if (codigo.trim ().equals ("13") || codigo.trim ().equals ("14") || codigo.trim ().equals ("15") || codigo.trim ().equals ("99"))
	  setSeveridade ((byte) 2);
	else if (codigo.trim ().equals ("10") || codigo.trim ().equals ("11") || codigo.trim ().equals ("12"))
	  setSeveridade ((byte) 3);
	else
	  return null;
	return super.validarImplementado ();
      }
    });
    getLocalizacao ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_imovel_ar_localizacao"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCodigo ().isVazio ())
	  return null;
	String codigo = getCodigo ().getConteudoAtual (0);
	if (codigo.trim ().equals ("10") || codigo.trim ().equals ("11") || codigo.trim ().equals ("12"))
	  setSeveridade ((byte) 3);
	else if (codigo.trim ().equals ("13") || codigo.trim ().equals ("14") || codigo.trim ().equals ("99"))
	  setSeveridade ((byte) 2);
	else
	  return null;
	return super.validarImplementado ();
      }
    });
    getArea ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_imovel_ar_area"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCodigo ().isVazio ())
	  return null;
	String codigo = getCodigo ().getConteudoAtual (0);
	if (codigo.trim ().equals ("10") || codigo.trim ().equals ("11") || codigo.trim ().equals ("12"))
	  setSeveridade ((byte) 3);
	else if (codigo.trim ().equals ("13") || codigo.trim ().equals ("14") || codigo.trim ().equals ("99"))
	  setSeveridade ((byte) 2);
	else
	  return null;
	return super.validarImplementado ();
      }
    });
    getParticipacao ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_imovel_ar_participacao_exploracao_1"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getParticipacao ().comparacao (">", "100,00"))
	  return new RetornoValidacao (tab.msg ("ficha_imovel_ar_participacao_exploracao_3"), (byte) 3);
	if (getCondicaoExploracao ().isVazio ())
	  return null;
	String codCondExploracao = getCondicaoExploracao ().getConteudoAtual (0);
	if (codCondExploracao.equals ("1"))
	  {
	    if (getParticipacao ().comparacao ("=", "100,00"))
	      return null;
	    return new RetornoValidacao (tab.msg ("ficha_imovel_ar_participacao_exploracao_1"), (byte) 3);
	  }
	if (codCondExploracao.equals ("2") || codCondExploracao.equals ("3"))
	  {
	    if (! getParticipacao ().comparacao ("=", "100,00"))
	      return null;
	    return new RetornoValidacao (tab.msg ("ficha_imovel_ar_participacao_exploracao_2"), (byte) 3);
	  }
	return null;
      }
    });
    getCondicaoExploracao ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_imovel_ar_cond_exploracao"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    });
  }
  
  public Valor getArea ()
  {
    return area;
  }
  
  public Codigo getCodigo ()
  {
    return codigo;
  }
  
  public Codigo getCondicaoExploracao ()
  {
    return condicaoExploracao;
  }
  
  public Alfa getLocalizacao ()
  {
    return localizacao;
  }
  
  public Alfa getNome ()
  {
    return nome;
  }
  
  public Valor getParticipacao ()
  {
    return participacao;
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
