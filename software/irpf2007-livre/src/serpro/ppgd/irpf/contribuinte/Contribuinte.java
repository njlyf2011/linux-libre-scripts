/* Contribuinte - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.contribuinte;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CEP;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCEP;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorData;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorTituloEleitor;

public class Contribuinte extends ObjetoNegocio
{
  public static final String CODIGO_NATUREZA_OCUPACAO_CAPITALISTA = "13";
  public static final String CODIGO_NATUREZA_OCUPACAO_APOSENTADO = "61";
  public static final String CODIGO_NATUREZA_OCUPACAO_APOSENTADO_PORTADOR_MOLESTIA = "62";
  public static final String CODIGO_NATUREZA_OCUPACAO_PENSAO_ALIMENTICIA_JUDICIAL = "71";
  public static final String CODIGO_NATUREZA_OCUPACAO_BOLSISTA = "72";
  public static final String CODIGO_NATUREZA_ESPOLIO = "81";
  protected transient IdentificadorDeclaracao identificadorDeclaracao = null;
  private Data dataNascimento = new Data (this, "Data de Nascimento");
  private Alfa tituloEleitor = new Alfa (this, "T\u00edtulo de Eleitor", 10);
  private Alfa exterior = new Alfa (this, "Exterior?", 2);
  private Codigo tipoLogradouro = new Codigo (this, "Tipo de Logradouro", CadastroTabelasIRPF.recuperarTiposLogradouro ());
  private Alfa logradouro = new Alfa (this, "Logradouro", 40);
  private Alfa logradouroExt = new Alfa (this, "Logradouro", 40);
  private Alfa numero = new Alfa (this, "N\u00famero", 6);
  private Alfa numeroExt = new Alfa (this, "N\u00famero", 6);
  private Alfa complemento = new Alfa (this, "Complemento", 21);
  private Alfa complementoExt = new Alfa (this, "Complemento", 21);
  private Alfa bairro = new Alfa (this, "Bairro", 19);
  private Alfa bairroExt = new Alfa (this, "Bairro", 19);
  private Codigo pais = new Codigo (this, "Pa\u00eds", CadastroTabelasIRPF.recuperarPaises ());
  private Codigo codigoExterior = new Codigo (this, "Cod. EX", CadastroTabelasIRPF.recuperarRepresentacoes ());
  private Codigo uf = new Codigo (this, "UF", CadastroTabelasIRPF.recuperarUFs (1));
  private Codigo municipio = new Codigo (this, "Munic\u00edpio", new Vector ());
  private Alfa cidade = new Alfa (this, "Cidade", 40);
  private CEP cep = new CEP (this, "CEP");
  private CEP cepExt = new CEP (this, "ZIP");
  private Alfa ddd = new Alfa (this, "DDD", 2);
  private Alfa telefone = new Alfa (this, "Telefone", 8);
  private Codigo naturezaOcupacao = new Codigo (this, "Natureza da Ocup.", CadastroTabelasIRPF.recuperarNaturezasOcupacao ());
  private Codigo ocupacaoPrincipal = new Codigo (this, "Ocup. Principal", CadastroTabelasIRPF.recuperarOcupacoesPrincipal ());
  
  public Contribuinte (IdentificadorDeclaracao id)
  {
    identificadorDeclaracao = id;
    getPais ().setConteudo ("105");
    getPais ().setColunaFiltro (1);
    getExterior ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	if (getExterior ().asString ().equals (Logico.NAO))
	  {
	    pais.setConteudo ("105");
	    ddd.setMaximoCaracteres (2);
	  }
	else
	  {
	    pais.clear ();
	    ddd.setMaximoCaracteres (4);
	  }
      }
    });
    getExterior ().setConteudo (Logico.NAO);
    getPais ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getExterior ().asString ().equals (Logico.SIM) && getPais ().isVazio ())
	  return new RetornoValidacao (tab.msg ("hint_pais"), getSeveridade ());
	return null;
      }
    });
    getDdd ().addValidador (new ValidadorDefault ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getExterior ().asString ().equals (Logico.NAO) && getDdd ().getConteudoFormatado ().trim ().length () == 1)
	  return new RetornoValidacao (tab.msg ("ddd_um_digito"), getSeveridade ());
	return null;
      }
    });
    getUf ().setColunaFiltro (1);
    getUf ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("hint_uf_invalida"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getExterior ().asString ().equals ("1"))
	  return null;
	return super.validarImplementado ();
      }
    });
    getUf ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	if (uf.isVazio () || uf.asString ().equals ("EX"))
	  municipio.setColecaoElementoTabela (new Vector ());
	else
	  {
	    String strUf = uf.getConteudoAtual (0);
	    municipio.setColecaoElementoTabela (CadastroTabelasIRPF.recuperarMunicipios (strUf, 1));
	  }
      }
    });
    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3));
    getDataNascimento ().addValidador (new ValidadorData ((byte) 3));
    getTituloEleitor ().addValidador (new ValidadorNaoNulo ((byte) 2, "N\u00famero do T\u00edtulo Eleitoral em branco. Informe-o, se houver."));
    getTituloEleitor ().addValidador (new ValidadorTituloEleitor ((byte) 2));
    getTipoLogradouro ().setColunaFiltro (1);
    getTipoLogradouro ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getExterior ().getConteudoFormatado ().equals (Logico.SIM))
	  return null;
	return super.validarImplementado ();
      }
    });
    getLogradouro ().addValidador (new ValidadorCampoEndereco ((byte) 3));
    getLogradouroExt ().addValidador (new ValidadorCampoEndereco ((byte) 3));
    getNumero ().addValidador (new ValidadorCampoEndereco ((byte) 3));
    getNumeroExt ().addValidador (new ValidadorCampoEndereco ((byte) 3));
    getComplemento ().addValidador (new ValidadorCampoEndereco ((byte) 3));
    getBairro ().addValidador (new ValidadorCampoEndereco ((byte) 3));
    getMunicipio ().addValidador (new ValidadorCampoEndereco ((byte) 3));
    getMunicipio ().addObservador (new ObservadorCEPMunicipio (municipio, uf, pais, cep));
    getMunicipio ().setColunaFiltro (1);
    getMunicipio ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.SIM))
	  return null;
	setMensagemValidacao (tab.msg ("municipio"));
	RetornoValidacao ret = super.validarImplementado ();
	return ret;
      }
    });
    getCidade ().addValidador (new ValidadorCampoEndereco ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.NAO))
	  return null;
	RetornoValidacao ret = super.validarImplementado ();
	return ret;
      }
    });
    getCidade ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.NAO))
	  return null;
	String msg = "Endere\u00e7o  inv\u00e1lido. Falta nome da cidade";
	RetornoValidacao ret = super.validarImplementado ();
	if (ret != null)
	  ret.setMensagemValidacao (msg);
	return ret;
      }
    });
    getCodigoExterior ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.NAO))
	  return null;
	String msg = "C\u00f3digo do Exterior em branco/inv\u00e1lido.";
	RetornoValidacao ret = super.validarImplementado ();
	if (ret != null)
	  ret.setMensagemValidacao (msg);
	return ret;
      }
    });
    getCep ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("hint_cep"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.SIM))
	  return null;
	return super.validarImplementado ();
      }
    });
    getCep ().addValidador (new ValidadorCepMunicipio ((byte) 2, pais, municipio, cep, "CEP n\u00e3o pertence ao munic\u00edpio")
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.SIM))
	  return null;
	return super.validarImplementado ();
      }
    });
    getCep ().addValidador (new ValidadorCEP ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (! pais.getConteudoFormatado ().equals ("105"))
	  return null;
	return super.validarImplementado ();
      }
    });
    getNaturezaOcupacao ().setColunaFiltro (1);
    getNaturezaOcupacao ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (naturezaOcupacao.isVazio () || naturezaOcupacao.getIndiceElementoTabela () == -1)
	  return new RetornoValidacao (tab.msg ("natureza_ocup"), (byte) 3);
	return super.validarImplementado ();
      }
    });
    getNaturezaOcupacao ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	ocupacaoPrincipal.setHabilitado (true);
	String codigoNaturezaOcupacaoAtual = naturezaOcupacao.asString ();
	if (codigoNaturezaOcupacaoAtual.equals ("13") || codigoNaturezaOcupacaoAtual.equals ("61") || codigoNaturezaOcupacaoAtual.equals ("62") || codigoNaturezaOcupacaoAtual.equals ("71") || codigoNaturezaOcupacaoAtual.equals ("72"))
	  ocupacaoPrincipal.sinalizaValidoEdit ();
	else if (codigoNaturezaOcupacaoAtual.equals ("81"))
	  {
	    ocupacaoPrincipal.clear ();
	    ocupacaoPrincipal.setHabilitado (false);
	  }
      }
    });
    getNaturezaOcupacao ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	if (getNaturezaOcupacao ().getConteudoFormatado ().trim ().length () == 1)
	  {
	    String s = getNaturezaOcupacao ().getConteudoFormatado ().trim ();
	    s = "0" + s;
	    getNaturezaOcupacao ().setConteudo (s);
	  }
      }
    });
    getOcupacaoPrincipal ().setColunaFiltro (1);
    getOcupacaoPrincipal ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	String codigoNaturezaOcupacaoAtual = naturezaOcupacao.asString ();
	if (codigoNaturezaOcupacaoAtual.equals ("13") || codigoNaturezaOcupacaoAtual.equals ("61") || codigoNaturezaOcupacaoAtual.equals ("62") || codigoNaturezaOcupacaoAtual.equals ("71") || codigoNaturezaOcupacaoAtual.equals ("72") || codigoNaturezaOcupacaoAtual.equals ("81"))
	  return null;
	if (ocupacaoPrincipal.isVazio () || ocupacaoPrincipal.getIndiceElementoTabela () == -1)
	  return new RetornoValidacao (tab.msg ("ocup_principal"), (byte) 3);
	return null;
      }
    });
    getNaturezaOcupacao ().addValidador (new ValidadorNaoNulo ((byte) 2)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (! getOcupacaoPrincipal ().isVazio ())
	  {
	    String codigoNaturezaOcupacaoAtual = naturezaOcupacao.asString ();
	    if (codigoNaturezaOcupacaoAtual.equals ("13") || codigoNaturezaOcupacaoAtual.equals ("61") || codigoNaturezaOcupacaoAtual.equals ("62") || codigoNaturezaOcupacaoAtual.equals ("71") || codigoNaturezaOcupacaoAtual.equals ("72"))
	      return new RetornoValidacao (tab.msg ("ocup_desnecessaria"), (byte) 2);
	  }
	return null;
      }
    });
    getOcupacaoPrincipal ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	if (getOcupacaoPrincipal ().getConteudoFormatado ().trim ().length () == 1)
	  {
	    String s = getOcupacaoPrincipal ().getConteudoFormatado ().trim ();
	    s = "0" + s;
	    getOcupacaoPrincipal ().setConteudo (s);
	  }
      }
    });
    getNumeroReciboDecAnterior ().addValidador (new ValidadorDefault ((byte) 2)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (Validador.validarNrRecibo (getInformacao ().asString ()))
	  return null;
	return new RetornoValidacao (tab.msg ("num_recibo_ano_anterior_invalido", new String[] { ConstantesGlobais.EXERCICIO_ANTERIOR }), getSeveridade ());
      }
    });
    getDeclaracaoRetificadora ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	String antigo = (String) valorAntigo;
	String novo = (String) valorNovo;
	if (Logico.SIM.equals (novo) && Logico.NAO.equals (antigo))
	  {
	    getNumReciboDecRetif ().clear ();
	    getNumeroReciboDecAnterior ().clear ();
	  }
	else if (Logico.NAO.equals (novo) && Logico.SIM.equals (antigo))
	  {
	    getNumReciboDecRetif ().clear ();
	    getNumeroReciboDecAnterior ().clear ();
	  }
      }
    });
    getLogradouro ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.NAO) && logradouro.isVazio () && complemento.isVazio ())
	  return new RetornoValidacao (tab.msg ("endereco_invalido"), (byte) 3);
	return null;
      }
    });
    getLogradouroExt ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (! exterior.getConteudoFormatado ().equals (Logico.NAO) && logradouroExt.isVazio () && complementoExt.isVazio ())
	  return new RetornoValidacao (tab.msg ("endereco_invalido"), (byte) 3);
	return null;
      }
    });
    getUf ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (exterior.getConteudoFormatado ().equals (Logico.SIM))
	  return null;
	return super.validarImplementado ();
      }
    });
    setFicha ("Identifica\u00e7\u00e3o do Contribuinte");
  }
  
  public Codigo getCodigoExterior ()
  {
    return codigoExterior;
  }
  
  public Codigo getPais ()
  {
    return pais;
  }
  
  public Alfa getBairro ()
  {
    return bairro;
  }
  
  public CEP getCep ()
  {
    return cep;
  }
  
  public Alfa getComplemento ()
  {
    return complemento;
  }
  
  public Data getDataNascimento ()
  {
    return dataNascimento;
  }
  
  public Alfa getDdd ()
  {
    return ddd;
  }
  
  public Alfa getDeclaracaoRetificadora ()
  {
    return identificadorDeclaracao.getDeclaracaoRetificadora ();
  }
  
  public Alfa getEnderecoDiferente ()
  {
    return identificadorDeclaracao.getEnderecoDiferente ();
  }
  
  public Alfa getExterior ()
  {
    return exterior;
  }
  
  public Codigo getMunicipio ()
  {
    return municipio;
  }
  
  public Codigo getNaturezaOcupacao ()
  {
    return naturezaOcupacao;
  }
  
  public Alfa getNumero ()
  {
    return numero;
  }
  
  public Alfa getNumeroReciboDecAnterior ()
  {
    return identificadorDeclaracao.getNumeroReciboDecAnterior ();
  }
  
  public Codigo getOcupacaoPrincipal ()
  {
    return ocupacaoPrincipal;
  }
  
  public Alfa getTelefone ()
  {
    return telefone;
  }
  
  public Codigo getTipoLogradouro ()
  {
    return tipoLogradouro;
  }
  
  public Alfa getTituloEleitor ()
  {
    return tituloEleitor;
  }
  
  public Alfa getLogradouro ()
  {
    return logradouro;
  }
  
  public Codigo getUf ()
  {
    return uf;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    retorno.add (identificadorDeclaracao.getCpf ());
    retorno.add (identificadorDeclaracao.getNome ());
    retorno.add (identificadorDeclaracao.getDeclaracaoRetificadora ());
    retorno.add (identificadorDeclaracao.getNumReciboDecRetif ());
    retorno.add (identificadorDeclaracao.getEnderecoDiferente ());
    return retorno;
  }
  
  public Alfa getCidade ()
  {
    return cidade;
  }
  
  public Alfa getNumReciboDecRetif ()
  {
    return identificadorDeclaracao.getNumReciboDecRetif ();
  }
  
  public void setLogradouroExt (Alfa logradouroExt)
  {
    this.logradouroExt = logradouroExt;
  }
  
  public Alfa getLogradouroExt ()
  {
    return logradouroExt;
  }
  
  public void setNumeroExt (Alfa numeroExt)
  {
    this.numeroExt = numeroExt;
  }
  
  public Alfa getNumeroExt ()
  {
    return numeroExt;
  }
  
  public void setComplementoExt (Alfa complementoExt)
  {
    this.complementoExt = complementoExt;
  }
  
  public Alfa getComplementoExt ()
  {
    return complementoExt;
  }
  
  public void setBairroExt (Alfa bairroExt)
  {
    this.bairroExt = bairroExt;
  }
  
  public Alfa getBairroExt ()
  {
    return bairroExt;
  }
  
  public void setCepExt (CEP cepExt)
  {
    this.cepExt = cepExt;
  }
  
  public CEP getCepExt ()
  {
    return cepExt;
  }
}
