/* RendPJTitular - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpj;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.Validador;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class RendPJTitular extends ObjetoNegocio
{
  public static final String NOME_NI_FONTE_PAGADORA = "NI da Fonte Pagadora";
  public static final String NOME_REND_RECEB_PJ = "Rend. Recebid";
  public static final String NOME_CONTRIB_PREV = "Contr. Prev. Oficial";
  public static final String NOME_IMPOSTO_RETIDO = "IR Retido na Fonte";
  public static final String NOME_DECIMO_TERCEIRO = "13\u00ba Sal\u00e1rio";
  protected transient IdentificadorDeclaracao identificadorDeclaracao = null;
  protected Alfa nomeFontePagadora = new Alfa (this, "Nome da Fonte Pagadora");
  protected NI NIFontePagadora = new NI (this, "NI da Fonte Pagadora");
  protected Valor rendRecebidoPJ = new Valor (this, "Rend. Recebid");
  protected Valor contribuicaoPrevOficial = new Valor (this, "Contr. Prev. Oficial");
  protected Valor impostoRetidoFonte = new Valor (this, "IR Retido na Fonte");
  protected Valor decimoTerceiro = new Valor (this, "13\u00ba Sal\u00e1rio");
  
  public RendPJTitular (IdentificadorDeclaracao id)
  {
    identificadorDeclaracao = id;
    addValidadores ();
  }
  
  public void addValidadores ()
  {
    ValidadorNaoNulo validadorNaoNuloNomeFontePagadora = new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	RetornoValidacao retornoValidacao = Validador.validarNI (getNIFontePagadora ().asString ());
	if (retornoValidacao == null && getNomeFontePagadora ().isVazio ())
	  {
	    setSeveridade ((byte) 2);
	    return new RetornoValidacao (tab.msg ("nome_fonte_pagadora_ausente"), (byte) 2);
	  }
	if (retornoValidacao != null && getNomeFontePagadora ().isVazio ())
	  {
	    setSeveridade ((byte) 3);
	    return new RetornoValidacao (tab.msg ("nome_fonte_pagadora"), (byte) 3);
	  }
	return null;
      }
    };
    getNomeFontePagadora ().addValidador (validadorNaoNuloNomeFontePagadora);
    ValidadorNaoNulo validadorNaoNuloNI = new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	RetornoValidacao retornoValidacao = Validador.validarNI (getNIFontePagadora ().asString ());
	if (getNIFontePagadora ().asString ().equals (identificadorDeclaracao.getCpf ().asString ()))
	  {
	    setSeveridade ((byte) 3);
	    return new RetornoValidacao (tab.msg ("ni_fonte_pagadora_igual_declarante"), (byte) 3);
	  }
	if (retornoValidacao != null && ! getImpostoRetidoFonte ().isVazio ())
	  {
	    setSeveridade ((byte) 3);
	    return new RetornoValidacao (tab.msg ("ni_fonte_pagadora"), (byte) 3);
	  }
	if (retornoValidacao != null && getImpostoRetidoFonte ().isVazio ())
	  {
	    setSeveridade ((byte) 2);
	    return new RetornoValidacao (tab.msg ("ni_fonte_pagadora"), (byte) 2);
	  }
	return null;
      }
    };
    getNIFontePagadora ().addValidador (validadorNaoNuloNI);
  }
  
  public void addObservador (Observador obs)
  {
    rendRecebidoPJ.addObservador (obs);
    contribuicaoPrevOficial.addObservador (obs);
    impostoRetidoFonte.addObservador (obs);
    decimoTerceiro.addObservador (obs);
    NIFontePagadora.addObservador (obs);
  }
  
  public void removeObservador (Observador obs)
  {
    rendRecebidoPJ.removeObservador (obs);
    contribuicaoPrevOficial.removeObservador (obs);
    impostoRetidoFonte.removeObservador (obs);
    decimoTerceiro.removeObservador (obs);
    NIFontePagadora.removeObservador (obs);
  }
  
  public Valor getContribuicaoPrevOficial ()
  {
    return contribuicaoPrevOficial;
  }
  
  public Valor getDecimoTerceiro ()
  {
    return decimoTerceiro;
  }
  
  public Valor getImpostoRetidoFonte ()
  {
    return impostoRetidoFonte;
  }
  
  public NI getNIFontePagadora ()
  {
    return NIFontePagadora;
  }
  
  public Alfa getNomeFontePagadora ()
  {
    return nomeFontePagadora;
  }
  
  public Valor getRendRecebidoPJ ()
  {
    return rendRecebidoPJ;
  }
  
  public Pendencia verificaValores (int numItem)
  {
    Pendencia retorno = null;
    if (getImpostoRetidoFonte ().isVazio () && getContribuicaoPrevOficial ().isVazio () && getDecimoTerceiro ().isVazio () && getRendRecebidoPJ ().isVazio ())
      retorno = new Pendencia ((byte) 2, getRendRecebidoPJ (), "Valores RendPJ", tab.msg ("rendpj_faltam_valores"), numItem);
    return retorno;
  }
  
  public List verificarPendencias (int numeroItem)
  {
    List retorno = super.verificarPendencias (numeroItem);
    Pendencia pendValores = verificaValores (numeroItem);
    if (pendValores != null)
      retorno.add (pendValores);
    return retorno;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    return retorno;
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
}
