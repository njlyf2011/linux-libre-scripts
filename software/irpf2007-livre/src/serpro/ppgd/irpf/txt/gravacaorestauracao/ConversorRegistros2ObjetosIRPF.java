/* ConversorRegistros2ObjetosIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.gravacaorestauracao;
import java.util.Vector;
import java.util.logging.Logger;

import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.ItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.alimentandos.Alimentando;
import serpro.ppgd.irpf.atividaderural.BemAR;
import serpro.ppgd.irpf.atividaderural.DividaAR;
import serpro.ppgd.irpf.atividaderural.ImovelAR;
import serpro.ppgd.irpf.atividaderural.ItemMovimentacaoRebanho;
import serpro.ppgd.irpf.atividaderural.MovimentacaoRebanho;
import serpro.ppgd.irpf.atividaderural.brasil.ApuracaoResultadoBrasil;
import serpro.ppgd.irpf.atividaderural.brasil.ImovelARBrasil;
import serpro.ppgd.irpf.atividaderural.brasil.MesReceitaDespesa;
import serpro.ppgd.irpf.atividaderural.exterior.ApuracaoResultadoExterior;
import serpro.ppgd.irpf.atividaderural.exterior.BemARExterior;
import serpro.ppgd.irpf.atividaderural.exterior.ReceitaDespesa;
import serpro.ppgd.irpf.bens.Bem;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.irpf.dependentes.Dependente;
import serpro.ppgd.irpf.dividas.Divida;
import serpro.ppgd.irpf.eleicoes.Doacao;
import serpro.ppgd.irpf.pagamentos.Pagamento;
import serpro.ppgd.irpf.rendIsentos.RendIsentos;
import serpro.ppgd.irpf.rendavariavel.GanhosLiquidosOuPerdas;
import serpro.ppgd.irpf.rendavariavel.MesFundosInvestimentos;
import serpro.ppgd.irpf.rendavariavel.Operacoes;
import serpro.ppgd.irpf.rendpf.CPFDependente;
import serpro.ppgd.irpf.rendpf.MesRendPF;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.irpf.rendpj.RendPJDependente;
import serpro.ppgd.irpf.rendpj.RendPJTitular;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.Validador;

public class ConversorRegistros2ObjetosIRPF
{
  private static final Logger logger;
  
  static
  {
    Class var_class = serpro.ppgd.irpf.txt.gravacaorestauracao.ConversorRegistros2ObjetosIRPF.class;
    logger = Logger.getLogger (var_class.getName ());
  }
  
  public String getNumReciboComDV (String numRecibo)
  {
    if (numRecibo.trim ().length () == 10)
      {
	String dvNumRecibo1 = "" + Validador.calcularModulo11 (numRecibo, null, 2);
	String dvNumRecibo2 = "" + Validador.calcularModulo11 (numRecibo + dvNumRecibo1, null, 2);
	return numRecibo + dvNumRecibo1 + dvNumRecibo2;
      }
    return numRecibo;
  }
  
  public IdentificadorDeclaracao montarIdDeclaracao (Vector vRegHeader, Vector vRegIdentif) throws GeracaoTxtException
  {
    RegistroTxt objRegTXT = (RegistroTxt) vRegHeader.elementAt (0);
    String cpf = objRegTXT.fieldByName ("NR_CPF").asString ();
    boolean jahExisteIdDeclaracao = IRPFFacade.existeDeclaracao (cpf);
    IdentificadorDeclaracao idDeclaracao;
    if (jahExisteIdDeclaracao)
      idDeclaracao = IRPFFacade.getInstancia ().recuperarIdDeclaracao (cpf);
    else
      {
	idDeclaracao = new IdentificadorDeclaracao ();
	idDeclaracao.getCpf ().setConteudo (cpf);
	IRPFFacade.criarDeclaracao (idDeclaracao);
      }
    idDeclaracao.getNome ().setConteudo (objRegTXT.fieldByName ("NM_NOME").asString ());
    String decRetif = objRegTXT.fieldByName ("IN_RETIFICADORA").asString ();
    decRetif = decRetif.equals ("S") || decRetif.equals ("1") ? Logico.SIM : Logico.NAO;
    idDeclaracao.getDeclaracaoRetificadora ().setConteudo (decRetif);
    idDeclaracao.getTipoDeclaracao ().setConteudo (objRegTXT.fieldByName ("IN_COMPLETA").asBoolean () ? "0" : "1");
    idDeclaracao.getExercicio ().setConteudo (objRegTXT.fieldByName ("EXERCICIO").asString ());
    try
      {
	objRegTXT = (RegistroTxt) vRegIdentif.elementAt (0);
      }
    catch (Exception e)
      {
	throw new GeracaoTxtException ("Registro Identifica\u00e7\u00e3o n\u00e3o encontrado no arquivo.");
      }
    idDeclaracao.getEnderecoDiferente ().setConteudo (objRegTXT.fieldByName ("IN_ENDERECO").asString ());
    if (decRetif == Logico.SIM)
      {
	String numReciboUltDecTransmitidaExercicioAtual = objRegTXT.fieldByName ("NR_CONTROLE_ORIGINAL").asString ();
	idDeclaracao.getNumReciboDecRetif ().setConteudo (numReciboUltDecTransmitidaExercicioAtual);
      }
    return idDeclaracao;
  }
  
  public IdentificadorDeclaracao montarIdDeclaracaoAnoAnterior (Vector vRegHeader, Vector vRegIdentif)
  {
    try
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegHeader.elementAt (0);
	String cpf = objRegTXT.fieldByName ("NR_CPF").asString ();
	IdentificadorDeclaracao idDeclaracao;
	if (IRPFFacade.existeDeclaracao (cpf))
	  idDeclaracao = IRPFFacade.getInstancia ().recuperarIdDeclaracao (cpf);
	else
	  {
	    idDeclaracao = new IdentificadorDeclaracao ();
	    idDeclaracao.getCpf ().setConteudo (cpf);
	  }
	idDeclaracao.getNome ().setConteudo (objRegTXT.fieldByName ("NM_NOME").asString ());
	idDeclaracao.getDeclaracaoRetificadora ().setConteudo (objRegTXT.fieldByName ("IN_RETIFICADORA").asString ());
	idDeclaracao.getTipoDeclaracao ().setConteudo (objRegTXT.fieldByName ("IN_COMPLETA").asBoolean () ? "0" : "1");
	idDeclaracao.getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
	objRegTXT = (RegistroTxt) vRegIdentif.elementAt (0);
	return idDeclaracao;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	return null;
      }
  }
  
  public IdentificadorDeclaracao montarIdDeclaracaoNaoPersistido (Vector vRegistro)
  {
    try
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	String cpf = objRegTXT.fieldByName ("NR_CPF").asString ();
	IdentificadorDeclaracao idDeclaracao = new IdentificadorDeclaracao ();
	idDeclaracao.getCpf ().setConteudo (cpf);
	idDeclaracao.getNome ().setConteudo (objRegTXT.fieldByName ("NM_NOME").asString ());
	idDeclaracao.getNome ().setConteudo (objRegTXT.fieldByName ("NM_NOME").asString ());
	try
	  {
	    String numReciboUltDecRec = objRegTXT.fieldByName ("NR_RECIBO_DECLARACAO_TRANSMITIDA").asString ();
	    numReciboUltDecRec = getNumReciboComDV (numReciboUltDecRec);
	    idDeclaracao.getNumReciboDecRetif ().setConteudo (numReciboUltDecRec);
	  }
	catch (Exception exception)
	  {
	    /* empty */
	  }
	idDeclaracao.getDeclaracaoRetificadora ().setConteudo (objRegTXT.fieldByName ("IN_RETIFICADORA").asString ());
	idDeclaracao.getTipoDeclaracao ().setConteudo (objRegTXT.fieldByName ("IN_COMPLETA").asBoolean () ? "0" : "1");
	idDeclaracao.getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
	return idDeclaracao;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	return null;
      }
  }
  
  public void montarContribuinteIRPF (Vector vRegistro, Contribuinte objContrib) throws GeracaoTxtException
  {
    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
    objContrib.getTituloEleitor ().setConteudo (objRegTXT.fieldByName ("NR_TITELEITOR").asString ());
    objContrib.getDataNascimento ().setConteudo (objRegTXT.fieldByName ("DT_NASCIM").asString ());
    objContrib.getNaturezaOcupacao ().setConteudo (objRegTXT.fieldByName ("CD_NATUR").asString ());
    objContrib.getOcupacaoPrincipal ().setConteudo (objRegTXT.fieldByName ("CD_OCUP").asString ());
    if (objRegTXT.fieldByName ("SG_UF").asString ().toUpperCase ().equals ("EX"))
      {
	objContrib.getExterior ().setConteudo (Logico.SIM);
	objContrib.getCodigoExterior ().setConteudo (objRegTXT.fieldByName ("CD_EX").asString ());
	objContrib.getPais ().setConteudo (objRegTXT.fieldByName ("CD_PAIS").asString ());
	objContrib.getCidade ().setConteudo (objRegTXT.fieldByName ("NM_MUNICIP").asString ());
	objContrib.getLogradouroExt ().setConteudo (objRegTXT.fieldByName ("NM_LOGRA").asString ());
	objContrib.getNumeroExt ().setConteudo (objRegTXT.fieldByName ("NR_NUMERO").asString ());
	objContrib.getBairroExt ().setConteudo (objRegTXT.fieldByName ("NM_BAIRRO").asString ());
	objContrib.getComplementoExt ().setConteudo (objRegTXT.fieldByName ("NM_COMPLEM").asString ());
      }
    else
      {
	objContrib.getExterior ().setConteudo (Logico.NAO);
	objContrib.getPais ().setConteudo ("105");
	objContrib.getLogradouro ().setConteudo (objRegTXT.fieldByName ("NM_LOGRA").asString ());
	objContrib.getNumero ().setConteudo (objRegTXT.fieldByName ("NR_NUMERO").asString ());
	objContrib.getBairro ().setConteudo (objRegTXT.fieldByName ("NM_BAIRRO").asString ());
	objContrib.getComplemento ().setConteudo (objRegTXT.fieldByName ("NM_COMPLEM").asString ());
      }
    objContrib.getUf ().setConteudo (objRegTXT.fieldByName ("SG_UF").asString ());
    objContrib.getMunicipio ().setConteudo (objRegTXT.fieldByName ("CD_MUNICIP").asString ());
    objContrib.getCep ().setConteudo (objRegTXT.fieldByName ("NR_CEP").asString ());
    objContrib.getTipoLogradouro ().setConteudo (objRegTXT.fieldByName ("TIP_LOGRA").asString ());
    objContrib.getDdd ().setConteudo (objRegTXT.fieldByName ("NR_DDD_TELEFONE").asString ());
    objContrib.getTelefone ().setConteudo (objRegTXT.fieldByName ("NR_TELEFONE").asString ());
    String reciboAnoAnterior = objRegTXT.fieldByName ("NR_RECIBO_ULTIMA_DEC_ANO_ANTERIOR").asString ();
    if (reciboAnoAnterior != null && ! reciboAnoAnterior.trim ().equals (""))
      {
	reciboAnoAnterior = getNumReciboComDV (reciboAnoAnterior);
	objContrib.getNumeroReciboDecAnterior ().setConteudo (reciboAnoAnterior);
      }
  }
  
  public void montarInformacoesObrigatorias (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
    String tipoDec = objRegTXT.fieldByName ("IN_COMPLETA").asBoolean () ? "0" : "1";
    objDecl.getIdentificadorDeclaracao ().getTipoDeclaracao ().setConteudo (tipoDec);
    objDecl.getContribuinte ().getEnderecoDiferente ().setConteudo (objRegTXT.fieldByName ("IN_ENDERECO").asBoolean () ? Logico.SIM : Logico.NAO);
    String decRetif = objRegTXT.fieldByName ("IN_RETIFICADORA").asString ();
    decRetif = decRetif.equals ("S") || decRetif.equals ("1") ? Logico.SIM : Logico.NAO;
    objDecl.getIdentificadorDeclaracao ().getDeclaracaoRetificadora ().setConteudo (decRetif);
    objDecl.getResumo ().getCalculoImposto ().getNumQuotas ().setConteudo (objRegTXT.fieldByName ("NR_QUOTAS").asInteger ());
    if (objDecl.getIdentificadorDeclaracao ().isRetificadora ())
      objDecl.getIdentificadorDeclaracao ().getNumReciboDecRetif ().setConteudoAntigo (objRegTXT.fieldByName ("NR_CONTROLE_ORIGINAL").asString ());
  }
  
  public void montarInformacoesBancarias (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
    objDecl.getResumo ().getCalculoImposto ().getBanco ().setConteudo (objRegTXT.fieldByName ("NR_BANCO").asString ());
    objDecl.getResumo ().getCalculoImposto ().getAgencia ().setConteudo (objRegTXT.fieldByName ("NR_AGENCIA").asString ());
    objDecl.getResumo ().getCalculoImposto ().getDvAgencia ().setConteudo (objRegTXT.fieldByName ("NR_DV_AGENCIA").asString ());
    objDecl.getResumo ().getCalculoImposto ().getContaCredito ().setConteudo (objRegTXT.fieldByName ("NR_CONTA").asString ());
    objDecl.getResumo ().getCalculoImposto ().getDvContaCredito ().setConteudo (objRegTXT.fieldByName ("NR_DV_CONTA").asString ());
    String debitoAutom = objRegTXT.fieldByName ("IN_DEBITO_AUTOM").asString ();
    debitoAutom = debitoAutom.equals ("S") ? "autorizado" : "N";
    objDecl.getResumo ().getCalculoImposto ().getDebitoAutomatico ().setConteudo (debitoAutom);
  }
  
  public void montarBem (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getBens ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	Bem objBem = new Bem ();
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	objBem.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_BEM").asString ());
	objBem.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_BEM").asString ());
	objBem.getValorExercicioAnterior ().setConteudo (objRegTXT.fieldByName ("VR_ANTER").asValor ());
	objBem.getValorExercicioAtual ().setConteudo (objRegTXT.fieldByName ("VR_ATUAL").asValor ());
	int inExterior = objRegTXT.fieldByName ("IN_EXTERIOR").asInteger ();
	if (inExterior == 1)
	  objBem.getPais ().setConteudo (objRegTXT.fieldByName ("CD_PAIS").asString ());
	else
	  objBem.getPais ().setConteudo ("105");
	objDecl.getBens ().recuperarLista ().add (objBem);
      }
  }
  
  public void montarDividas (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getDividas ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	Divida objDivida = new Divida ();
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	objDivida.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_DIV").asString ());
	objDivida.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_DIV").asString ());
	objDivida.getValorExercicioAnterior ().setConteudo (objRegTXT.fieldByName ("VR_ANTER").asValor ());
	objDivida.getValorExercicioAtual ().setConteudo (objRegTXT.fieldByName ("VR_ATUAL").asValor ());
	objDecl.getDividas ().recuperarLista ().add (objDivida);
      }
  }
  
  public void montarRendPJDependentesCompleta (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getColecaoRendPJDependente ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RendPJDependente objRendPJ = new RendPJDependente (objDecl.getIdentificadorDeclaracao ());
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	objRendPJ.getNIFontePagadora ().setConteudo (objRegTXT.fieldByName ("NR_PAGADOR").asString ());
	objRendPJ.getNomeFontePagadora ().setConteudo (objRegTXT.fieldByName ("NM_PAGADOR").asString ());
	objRendPJ.getRendRecebidoPJ ().setConteudo (objRegTXT.fieldByName ("VR_RENDTO").asValor ());
	objRendPJ.getContribuicaoPrevOficial ().setConteudo (objRegTXT.fieldByName ("VR_CONTRIB").asValor ());
	objRendPJ.getImpostoRetidoFonte ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTO").asValor ());
	objRendPJ.getDecimoTerceiro ().setConteudo (objRegTXT.fieldByName ("VR_DECTERC").asValor ());
	objRendPJ.getCpfDependente ().setConteudo (objRegTXT.fieldByName ("CPF_BENEF").asString ());
	objDecl.getColecaoRendPJDependente ().recuperarLista ().add (objRendPJ);
      }
  }
  
  public void montarDeclaracaoCompleta (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	objDecl.getImpostoPago ().getImpostoPagoExterior ().setConteudo (objRegTXT.fieldByName ("VR_IMPEXT").asValor ());
	objDecl.getImpostoPago ().getImpostoComplementar ().setConteudo (objRegTXT.fieldByName ("VR_IMPCOMP").asValor ());
	objDecl.getImpostoPago ().getImpostoRetidoFonte ().setConteudo (objRegTXT.fieldByName ("VR_IRFONTELEI11033").asValor ());
      }
  }
  
  public void montarRendPJTitularCompleta (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	objDecl.getColecaoRendPJTitular ().recuperarLista ().clear ();
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    RendPJTitular objRendPJ = new RendPJTitular (objDecl.getIdentificadorDeclaracao ());
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    objRendPJ.getNIFontePagadora ().setConteudo (objRegTXT.fieldByName ("NR_PAGADOR").asString ());
	    objRendPJ.getNomeFontePagadora ().setConteudo (objRegTXT.fieldByName ("NM_PAGADOR").asString ());
	    objRendPJ.getRendRecebidoPJ ().setConteudo (objRegTXT.fieldByName ("VR_RENDTO").asValor ());
	    objRendPJ.getContribuicaoPrevOficial ().setConteudo (objRegTXT.fieldByName ("VR_CONTRIB").asValor ());
	    objRendPJ.getImpostoRetidoFonte ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTO").asValor ());
	    objRendPJ.getDecimoTerceiro ().setConteudo (objRegTXT.fieldByName ("VR_DECTERC").asValor ());
	    objDecl.getColecaoRendPJTitular ().recuperarLista ().add (objRendPJ);
	  }
      }
  }
  
  public void recuperarRendIsentosNaoTributaveis (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RendIsentos objRendIsentos = objDecl.getRendIsentos ();
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	objRendIsentos.getBolsaEstudos ().setConteudo (objRegTXT.fieldByName ("VR_BOLSA").asValor ());
	objRendIsentos.getCapitalApolices ().setConteudo (objRegTXT.fieldByName ("VR_PREVID").asValor ());
	objRendIsentos.getIndenizacoes ().setConteudo (objRegTXT.fieldByName ("VR_FGTS").asValor ());
	objRendIsentos.getLucroAlienacao ().setConteudo (objRegTXT.fieldByName ("VR_GCISENTO").asValor ());
	objRendIsentos.getLucroRecebido ().setConteudo (objRegTXT.fieldByName ("VR_LUCROS").asValor ());
	objRendIsentos.getParcIsentaAposentadoria ().setConteudo (objRegTXT.fieldByName ("VR_65ANOS").asValor ());
	objRendIsentos.getPensao ().setConteudo (objRegTXT.fieldByName ("VR_INVALIDEZ").asValor ());
	objRendIsentos.getPoupanca ().setConteudo (objRegTXT.fieldByName ("VR_POUPANCA").asValor ());
	objRendIsentos.getRendSocio ().setConteudo (objRegTXT.fieldByName ("VR_SOCIO").asValor ());
	objRendIsentos.getTransferencias ().setConteudo (objRegTXT.fieldByName ("VR_HERANCA").asValor ());
	objRendIsentos.getOutros ().setConteudo (objRegTXT.fieldByName ("VR_OUTROS").asValor ());
	objRendIsentos.getDescOutros ().setConteudo (objRegTXT.fieldByName ("TX_OUTROS").asString ());
	objRendIsentos.getRendDependentes ().setConteudo (objRegTXT.fieldByName ("VR_ISENTOSDEPENDENTES").asValor ());
      }
  }
  
  public void recuperarRendTributacaoExclusiva (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	objDecl.getRendTributacaoExclusiva ().getRendAplicacoes ().setConteudo (objRegTXT.fieldByName ("VR_FINANCEIRAS").asValor ());
	objDecl.getRendTributacaoExclusiva ().getOutros ().setConteudo (objRegTXT.fieldByName ("VR_OUTROS").asValor ());
	objDecl.getRendTributacaoExclusiva ().getDescTotal ().setConteudo (objRegTXT.fieldByName ("TX_OUTROS").asString ());
	objDecl.getRendTributacaoExclusiva ().getRendExcetoDecimoTerceiro ().setConteudo (objRegTXT.fieldByName ("VR_EXCLUSIVAEXCETO13SALDEP").asValor ());
      }
  }
  
  public void montarAlimentandos (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	objDecl.getAlimentandos ().recuperarLista ().clear ();
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    Alimentando objAlimentando = new Alimentando ();
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    objAlimentando.setChave (objRegTXT.fieldByName ("NR_CHAVE").asString ());
	    objAlimentando.getNome ().setConteudo (objRegTXT.fieldByName ("NM_NOME").asString ());
	    objDecl.getAlimentandos ().recuperarLista ().add (objAlimentando);
	  }
      }
  }
  
  public void montarPagamentos (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	objDecl.getPagamentos ().recuperarLista ().clear ();
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    Pagamento pagamento = new Pagamento (objDecl.getIdentificadorDeclaracao ());
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    pagamento.getNiBeneficiario ().setConteudo (objRegTXT.fieldByName ("NR_BENEF").asString ());
	    pagamento.getNomeBeneficiario ().setConteudo (objRegTXT.fieldByName ("NM_BENEF").asString ());
	    pagamento.getNitEmpregadoDomestico ().setConteudo (objRegTXT.fieldByName ("NR_NIT").asString ());
	    pagamento.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_PAGTO").asString ());
	    pagamento.getDependenteOuAlimentando ().setConteudo (objDecl.getNomeDependenteOuAlimentandoPorChave (pagamento.getCodigo ().getConteudoAtual (0), objRegTXT.fieldByName ("NR_CHAVE_DEPEND").asString ()));
	    pagamento.getValorPago ().setConteudo (objRegTXT.fieldByName ("VR_PAGTO").asValor ());
	    pagamento.getParcelaNaoDedutivel ().setConteudo (objRegTXT.fieldByName ("VR_REDUC").asValor ());
	    objDecl.getPagamentos ().recuperarLista ().add (pagamento);
	  }
      }
  }
  
  public void montarLucrosDividendos (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	objDecl.getRendIsentos ().getLucroRecebidoQuadroAuxiliar ().recuperarLista ().clear ();
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    ItemQuadroLucrosDividendos item = new ItemQuadroLucrosDividendos ();
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    item.getTipo ().setConteudo (objRegTXT.fieldByName ("NR_TIPO").asString ().equals ("T") ? "Titular" : "Dependente");
	    item.getCnpjEmpresa ().setConteudo (objRegTXT.fieldByName ("NR_PAGADORA").asString ());
	    item.getNomeFonte ().setConteudo (objRegTXT.fieldByName ("NM_PAGADORA").asString ());
	    item.getValor ().setConteudo (objRegTXT.fieldByName ("VR_LUCRO").asValor ());
	    objDecl.getRendIsentos ().getLucroRecebidoQuadroAuxiliar ().recuperarLista ().add (item);
	  }
      }
  }
  
  public void montarDoacoesCampanha (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	objDecl.getDoacoes ().recuperarLista ().clear ();
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    Doacao item = new Doacao ();
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    item.getCNPJ ().setConteudo (objRegTXT.fieldByName ("NR_PARTIDO").asString ());
	    item.getNome ().setConteudo (objRegTXT.fieldByName ("NM_PARTIDO").asString ());
	    item.getValor ().setConteudo (objRegTXT.fieldByName ("VR_DOACAO").asValor ());
	    objDecl.getDoacoes ().recuperarLista ().add (item);
	  }
      }
  }
  
  public void montarPagamentosAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	objDecl.getPagamentos ().recuperarLista ().clear ();
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    Pagamento pagamento = new Pagamento (objDecl.getIdentificadorDeclaracao ());
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    pagamento.getNiBeneficiario ().setConteudo (objRegTXT.fieldByName ("NR_BENEF").asString ());
	    pagamento.getNomeBeneficiario ().setConteudo (objRegTXT.fieldByName ("NM_BENEF").asString ());
	    pagamento.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_PAGTO").asString ());
	    pagamento.getDependenteOuAlimentando ().setConteudo (objDecl.getNomeDependenteOuAlimentandoPorChave (pagamento.getCodigo ().getConteudoAtual (0), objRegTXT.fieldByName ("NR_CHAVE_DEPEND").asString ()));
	    pagamento.getValorPago ().setConteudo (objRegTXT.fieldByName ("VR_PAGTO").asValor ());
	    pagamento.getParcelaNaoDedutivel ().setConteudo (objRegTXT.fieldByName ("VR_REDUC").asValor ());
	    objDecl.getPagamentos ().recuperarLista ().add (pagamento);
	  }
      }
  }
  
  public void montarCPFDependentesComRendPF (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	objDecl.getRendPFDependente ().getColecaoCPFDependentes ().recuperarLista ().clear ();
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    CPFDependente cpfDependente = new CPFDependente ();
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    cpfDependente.getCpf ().setConteudo (objRegTXT.fieldByName ("RENDPF_DEP_CPF").asString ());
	    objDecl.getRendPFDependente ().getColecaoCPFDependentes ().recuperarLista ().add (cpfDependente);
	  }
      }
  }
  
  public void montarRendimentosPF (Vector vRegistro, DeclaracaoIRPF objDecl, boolean ehDependente) throws GeracaoTxtException
  {
    RendPF rendPF;
    if (! ehDependente)
      {
	objDecl.getRendPFTitular ().clear ();
	rendPF = objDecl.getRendPFTitular ();
      }
    else
      {
	objDecl.getRendPFDependente ().clear ();
	rendPF = objDecl.getRendPFDependente ();
      }
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	if (objRegTXT.fieldByName ("E_DEPENDENTE").asBoolean () == ehDependente)
	  {
	    int mes = objRegTXT.fieldByName ("NR_MES").asInteger ();
	    MesRendPF objRendPF = rendPF.getMesRendPFPorIndice (mes - 1);
	    objRendPF.getMes ().setConteudo (mes - 1);
	    objRendPF.getPessoaFisica ().setConteudo (objRegTXT.fieldByName ("VR_RENDTO").asValor ());
	    objRendPF.getExterior ().setConteudo (objRegTXT.fieldByName ("VR_EXTER").asValor ());
	    objRendPF.getPrevidencia ().setConteudo (objRegTXT.fieldByName ("VR_PREVID").asValor ());
	    objRendPF.getDependentes ().setConteudo (objRegTXT.fieldByName ("VR_DEDUC").asValor ());
	    objRendPF.getPensao ().setConteudo (objRegTXT.fieldByName ("VR_ALIMENT").asValor ());
	    objRendPF.getLivroCaixa ().setConteudo (objRegTXT.fieldByName ("VR_LIVCAIX").asValor ());
	    objRendPF.getDarfPago ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTO").asValor ());
	  }
      }
  }
  
  public void montarDependentes (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getDependentes ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	Dependente objDependente = new Dependente ();
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	objDependente.setChave (objRegTXT.fieldByName ("NR_CHAVE").asString ());
	objDependente.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_DEPEND").asString ());
	objDependente.getNome ().setConteudo (objRegTXT.fieldByName ("NM_DEPEND").asString ());
	objDependente.getDataNascimento ().setConteudo (objRegTXT.fieldByName ("DT_NASCIM").asString ());
	objDependente.getCpfDependente ().setConteudo (objRegTXT.fieldByName ("NI_DEPEND").asString ());
	objDecl.getDependentes ().recuperarLista ().add (objDependente);
      }
  }
  
  public void montarConjuge (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	objDecl.getConjuge ().getCpfConjuge ().setConteudo (objRegTXT.fieldByName ("NR_CONJ").asString ());
	objDecl.getConjuge ().getBaseCalculoImposto ().setConteudo (objRegTXT.fieldByName ("VR_BASE").asValor ());
	objDecl.getConjuge ().getDecSimplificada ().setConteudo (objRegTXT.fieldByName ("IN_TIPO").asBoolean () ? Logico.SIM : Logico.NAO);
	if (objDecl.getConjuge ().getDecSimplificada ().asString ().equals (Logico.SIM))
	  {
	    objDecl.getConjuge ().getImpRetidoFonte ().setConteudo (objRegTXT.fieldByName ("VR_RETIDO_S").asValor ());
	    objDecl.getConjuge ().getCarneComImpComplementar ().setConteudo (objRegTXT.fieldByName ("VR_LEAO_S").asValor ());
	  }
	else
	  objDecl.getConjuge ().getImpRetidoFonte ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTO_C").asValor ());
	objDecl.getConjuge ().getRendIsentoNaoTributaveis ().setConteudo (objRegTXT.fieldByName ("VR_ISENTO_C").asValor ());
	objDecl.getConjuge ().getRendSujeitosTribExcl ().setConteudo (objRegTXT.fieldByName ("VR_EXCLUSIVO_C").asValor ());
      }
  }
  
  public void montarInventariante (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	objDecl.getEspolio ().getNomeInventariante ().setConteudo (objRegTXT.fieldByName ("NM_INVENT").asString ());
	objDecl.getEspolio ().getEndInventariante ().setConteudo (objRegTXT.fieldByName ("NM_END_INVENT").asString ());
	objDecl.getEspolio ().getCpfInventariante ().setConteudo (objRegTXT.fieldByName ("NR_INVENT").asString ());
      }
  }
  
  public void montarInventarianteAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	objDecl.getEspolio ().getNomeInventariante ().setConteudo (objRegTXT.fieldByName ("NM_INVENT").asString ());
	objDecl.getEspolio ().getEndInventariante ().setConteudo (objRegTXT.fieldByName ("NM_END_INVENT").asString ());
	objDecl.getEspolio ().getCpfInventariante ().setConteudo (objRegTXT.fieldByName ("NR_INVENT").asString ());
      }
  }
  
  public void montarRendPJTitularSimplificada (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getColecaoRendPJTitular ().recuperarLista ().clear ();
    if (vRegistro.size () > 0)
      {
	for (int i = 0; i < vRegistro.size (); i++)
	  {
	    RendPJTitular objRendPJ = new RendPJTitular (objDecl.getIdentificadorDeclaracao ());
	    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	    objRendPJ.getNIFontePagadora ().setConteudo (objRegTXT.fieldByName ("NR_PAGADOR").asString ());
	    objRendPJ.getNomeFontePagadora ().setConteudo (objRegTXT.fieldByName ("NM_PAGADOR").asString ());
	    objRendPJ.getRendRecebidoPJ ().setConteudo (objRegTXT.fieldByName ("VR_RENDTO").asValor ());
	    objRendPJ.getImpostoRetidoFonte ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTO").asValor ());
	    objDecl.getColecaoRendPJTitular ().recuperarLista ().add (objRendPJ);
	  }
      }
  }
  
  public void montarRendaVariavel (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getRendaVariavel ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	int mes = objRegTXT.fieldByName ("NR_MES").asInteger ();
	GanhosLiquidosOuPerdas objGanhosLiq = objDecl.getRendaVariavel ().getGanhosPorIndice (mes - 1);
	Operacoes opComuns = objGanhosLiq.getOperacoesComuns ();
	opComuns.getMercadoVistaAcoes ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MVISTA_ACOES").asValor ());
	opComuns.getMercadoVistaOuro ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MVISTA_OURO").asValor ());
	opComuns.getMercadoVistaForaBolsa ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MVISTA_OUROFORA").asValor ());
	opComuns.getMercadoOpcoesAcoes ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MOPC_ACOES").asValor ());
	opComuns.getMercadoOpcoesOuro ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MOPC_OURO").asValor ());
	opComuns.getMercadoOpcoesForaDeBolsa ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MOPC_OUROFORA").asValor ());
	opComuns.getMercadoOpcoesOutros ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MOPC_OUTROS").asValor ());
	opComuns.getMercadoFuturoDolar ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MFUT_DOLAR").asValor ());
	opComuns.getMercadoFuturoIndices ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MFUT_INDICES").asValor ());
	opComuns.getMercadoFuturoJuros ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MFUT_JUROS").asValor ());
	opComuns.getMercadoFuturoOutros ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MFUT_OUTROS").asValor ());
	opComuns.getMercadoTermoAcoes ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MTERMO_ACOESOURO").asValor ());
	opComuns.getMercadoTermoOutros ().setConteudo (objRegTXT.fieldByName ("VR_COMUM_MTERMO_OUTROS").asValor ());
	Operacoes opDayTrade = objGanhosLiq.getOperacoesDayTrade ();
	opDayTrade.getMercadoVistaAcoes ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MVISTA_ACOES").asValor ());
	opDayTrade.getMercadoVistaOuro ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MVISTA_OURO").asValor ());
	opDayTrade.getMercadoVistaForaBolsa ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MVISTA_OUROFORA").asValor ());
	opDayTrade.getMercadoOpcoesAcoes ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MOPC_ACOES").asValor ());
	opDayTrade.getMercadoOpcoesOuro ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MOPC_OURO").asValor ());
	opDayTrade.getMercadoOpcoesForaDeBolsa ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MOPC_OUROFORA").asValor ());
	opDayTrade.getMercadoOpcoesOutros ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MOPC_OUTROS").asValor ());
	opDayTrade.getMercadoFuturoDolar ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MFUT_DOLAR").asValor ());
	opDayTrade.getMercadoFuturoIndices ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MFUT_INDICES").asValor ());
	opDayTrade.getMercadoFuturoJuros ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MFUT_JUROS").asValor ());
	opDayTrade.getMercadoFuturoOutros ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MFUT_OUTROS").asValor ());
	opDayTrade.getMercadoTermoAcoes ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MTERMO_ACOESOURO").asValor ());
	opDayTrade.getMercadoTermoOutros ().setConteudo (objRegTXT.fieldByName ("VR_DAYTR_MTERMO_OUTROS").asValor ());
	opComuns.getResultadoNegativoMesAnterior ().setConteudo (objRegTXT.fieldByName ("VR_RESULTNEG_MESANT_COMUM").asValor ());
	opDayTrade.getResultadoNegativoMesAnterior ().setConteudo (objRegTXT.fieldByName ("VR_RESULTNEG_MESANT_DAYTR").asValor ());
	objGanhosLiq.getIrFonteDayTradeMesAtual ().setConteudo (objRegTXT.fieldByName ("VR_FONTE_DAYTRADE").asValor ());
	objGanhosLiq.getImpostoRetidoFonteLei11033 ().setConteudo (objRegTXT.fieldByName ("VR_IMPRENDAFONTE").asValor ());
	objGanhosLiq.getImpostoPago ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTO_PAGO").asValor ());
      }
  }
  
  public void montarRendaVariavelFII (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	int mes = objRegTXT.fieldByName ("NR_MES").asInteger ();
	if (mes < 1 || mes > 12)
	  throw new GeracaoTxtException ("Registro Fundo de Investimentos, M\u00eas Inv\u00e1lido.");
	MesFundosInvestimentos fundoInvest = objDecl.getRendaVariavel ().getFundInvest ().getMeses ()[mes - 1];
	fundoInvest.clear ();
	fundoInvest.getResultLiquidoMes ().setConteudo (objRegTXT.fieldByName ("VR_RESLIQUIDO_MES").asValor ());
	fundoInvest.getResultNegativoAnterior ().setConteudo (objRegTXT.fieldByName ("VR_RESULT_NEG_MESANT").asValor ());
	fundoInvest.getBaseCalcImposto ().setConteudo (objRegTXT.fieldByName ("VR_BASECALCULO_MES").asValor ());
	fundoInvest.getPrejuizoCompensar ().setConteudo (objRegTXT.fieldByName ("VR_PREJACOMPENSAR_MES_OPCOMUNS").asValor ());
	fundoInvest.getAliquotaImposto ().setConteudo (objRegTXT.fieldByName ("VR_ALIQUOTA_IMPOSTO_OPCOMUNS").asValor ());
	fundoInvest.getImpostoDevido ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTODEVIDO_MES_OPCOMUNS").asValor ());
	fundoInvest.getImpostoPago ().setConteudo (objRegTXT.fieldByName ("VR_IMPOSTOPAGO").asValor ());
      }
  }
  
  public void montarAtividadeRuralImoveis (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getIdentificacaoImovel ().recuperarLista ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getIdentificacaoImovel ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	ImovelAR imovelAtual;
	if (inExt.equals ("0"))
	  imovelAtual = new ImovelARBrasil ();
	else
	  imovelAtual = new ImovelAR ();
	imovelAtual.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_ATIV").asString ());
	imovelAtual.getCondicaoExploracao ().setConteudo (objRegTXT.fieldByName ("CD_EXPLOR").asString ());
	imovelAtual.getArea ().setConteudo (objRegTXT.fieldByName ("QT_AREA").asValor ());
	imovelAtual.getLocalizacao ().setConteudo (objRegTXT.fieldByName ("NM_LOCAL").asString ());
	imovelAtual.getNome ().setConteudo (objRegTXT.fieldByName ("NM_IMOVEL").asString ());
	imovelAtual.getParticipacao ().setConteudo (objRegTXT.fieldByName ("PC_PARTIC").asValor ());
	if (imovelAtual instanceof ImovelARBrasil)
	  {
	    objDecl.getAtividadeRural ().getBrasil ().getIdentificacaoImovel ().recuperarLista ().add (imovelAtual);
	    ((ImovelARBrasil) imovelAtual).getNirf ().setConteudo (objRegTXT.fieldByName ("NR_INCRA").asString ());
	  }
	else
	  objDecl.getAtividadeRural ().getExterior ().getIdentificacaoImovel ().recuperarLista ().add (imovelAtual);
      }
  }
  
  public void montarAtividadeRuralImoveisAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getIdentificacaoImovel ().recuperarLista ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getIdentificacaoImovel ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	ImovelAR imovelAtual;
	if (inExt.equals ("0"))
	  imovelAtual = new ImovelARBrasil ();
	else
	  imovelAtual = new ImovelAR ();
	imovelAtual.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_ATIV").asString ());
	imovelAtual.getCondicaoExploracao ().setConteudo (objRegTXT.fieldByName ("CD_EXPLOR").asString ());
	imovelAtual.getArea ().setConteudo (objRegTXT.fieldByName ("QT_AREA").asValor ());
	imovelAtual.getLocalizacao ().setConteudo (objRegTXT.fieldByName ("NM_LOCAL").asString ());
	imovelAtual.getNome ().setConteudo (objRegTXT.fieldByName ("NM_IMOVEL").asString ());
	imovelAtual.getParticipacao ().setConteudo (objRegTXT.fieldByName ("PC_PARTIC").asValor ());
	if (imovelAtual instanceof ImovelARBrasil)
	  {
	    objDecl.getAtividadeRural ().getBrasil ().getIdentificacaoImovel ().recuperarLista ().add (imovelAtual);
	    ((ImovelARBrasil) imovelAtual).getNirf ().setConteudo (objRegTXT.fieldByName ("NR_INCRA").asString ());
	  }
	else
	  objDecl.getAtividadeRural ().getExterior ().getIdentificacaoImovel ().recuperarLista ().add (imovelAtual);
      }
  }
  
  public void montarAtividadeRuralBens (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getBens ().recuperarLista ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getBens ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	BemAR bemAtual;
	if (inExt.equals ("0"))
	  bemAtual = new BemAR ();
	else
	  bemAtual = new BemARExterior ();
	bemAtual.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_BEMAR").asString ());
	bemAtual.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_BEM").asString ());
	bemAtual.getValor ().setConteudo (objRegTXT.fieldByName ("VR_BEM").asValor ());
	if (bemAtual instanceof BemARExterior)
	  {
	    ((BemARExterior) bemAtual).getPais ().setConteudo (objRegTXT.fieldByName ("CD_PAIS").asString ());
	    objDecl.getAtividadeRural ().getExterior ().getBens ().recuperarLista ().add (bemAtual);
	  }
	else
	  objDecl.getAtividadeRural ().getBrasil ().getBens ().recuperarLista ().add (bemAtual);
      }
  }
  
  public void montarAtividadeRuralBensAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getBens ().recuperarLista ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getBens ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	BemAR bemAtual;
	if (inExt.equals ("0"))
	  bemAtual = new BemAR ();
	else
	  bemAtual = new BemARExterior ();
	bemAtual.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_BEMAR").asString ());
	bemAtual.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_BEM").asString ());
	if (bemAtual instanceof BemARExterior)
	  {
	    ((BemARExterior) bemAtual).getPais ().setConteudo (objRegTXT.fieldByName ("CD_PAIS").asString ());
	    objDecl.getAtividadeRural ().getExterior ().getBens ().recuperarLista ().add (bemAtual);
	  }
	else
	  objDecl.getAtividadeRural ().getBrasil ().getBens ().recuperarLista ().add (bemAtual);
      }
  }
  
  public void montarAtividadeRuralDividas (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getDividas ().recuperarLista ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getDividas ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	DividaAR dividaAtual = new DividaAR ();
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	dividaAtual.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_DIVIDA").asString ());
	dividaAtual.getContraidasAteExercicioAnterior ().setConteudo (objRegTXT.fieldByName ("VR_DIVATE").asValor ());
	dividaAtual.getContraidasAteExercicioAtual ().setConteudo (objRegTXT.fieldByName ("VR_DIVATU").asValor ());
	dividaAtual.getEfetivamentePagas ().setConteudo (objRegTXT.fieldByName ("VR_DIVPAG").asValor ());
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	if (inExt.equals ("0"))
	  objDecl.getAtividadeRural ().getBrasil ().getDividas ().recuperarLista ().add (dividaAtual);
	else
	  objDecl.getAtividadeRural ().getExterior ().getDividas ().recuperarLista ().add (dividaAtual);
      }
  }
  
  public void montarAtividadeRuralDividasAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getDividas ().recuperarLista ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getDividas ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	DividaAR dividaAtual = new DividaAR ();
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	Valor contraidasAteAnoCalendario = new Valor ();
	Valor efetivamentePagas = new Valor ();
	contraidasAteAnoCalendario = objRegTXT.fieldByName ("VR_DIVATU").asValor ();
	efetivamentePagas = objRegTXT.fieldByName ("VR_DIVPAG").asValor ();
	if (! contraidasAteAnoCalendario.isVazio () && efetivamentePagas.comparacao ("<", contraidasAteAnoCalendario))
	  {
	    dividaAtual.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_DIVIDA").asString ());
	    String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	    if (inExt.equals ("0"))
	      objDecl.getAtividadeRural ().getBrasil ().getDividas ().recuperarLista ().add (dividaAtual);
	    else
	      objDecl.getAtividadeRural ().getExterior ().getDividas ().recuperarLista ().add (dividaAtual);
	  }
      }
  }
  
  public void montarAtividadeRuralMovimentacaoRebanho (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getMovimentacaoRebanho ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getMovimentacaoRebanho ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	MovimentacaoRebanho movRebAtual;
	if (inExt.equals ("0"))
	  movRebAtual = objDecl.getAtividadeRural ().getBrasil ().getMovimentacaoRebanho ();
	else
	  movRebAtual = objDecl.getAtividadeRural ().getExterior ().getMovimentacaoRebanho ();
	int tipoDadoMov = objRegTXT.fieldByName ("CD_ESPEC").asInteger ();
	switch (tipoDadoMov)
	  {
	  case 1:
	    setarValoresTipoDadoMovimentacaoRebanho (movRebAtual.getBovinos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 2:
	    setarValoresTipoDadoMovimentacaoRebanho (movRebAtual.getSuinos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 3:
	    setarValoresTipoDadoMovimentacaoRebanho (movRebAtual.getCaprinos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 4:
	    setarValoresTipoDadoMovimentacaoRebanho (movRebAtual.getAsininos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 5:
	    setarValoresTipoDadoMovimentacaoRebanho (movRebAtual.getOutros (), vRegistro, objDecl, objRegTXT);
	    break;
	  }
      }
  }
  
  private void setarValoresTipoDadoMovimentacaoRebanho (ItemMovimentacaoRebanho pTipo, Vector vRegistro, DeclaracaoIRPF objDecl, RegistroTxt objRegTXT) throws GeracaoTxtException
  {
    pTipo.getEstoqueInicial ().setConteudo (objRegTXT.fieldByName ("QT_INIC").asValor ());
    pTipo.getAquisicoesAno ().setConteudo (objRegTXT.fieldByName ("QT_COMPRA").asValor ());
    pTipo.getNascidosAno ().setConteudo (objRegTXT.fieldByName ("QT_NASCIM").asValor ());
    pTipo.getEstoqueFinal ().setConteudo (objRegTXT.fieldByName ("QT_ESTFINAL").asValor ());
    pTipo.getConsumo ().setConteudo (objRegTXT.fieldByName ("QT_PERDA").asValor ());
    pTipo.getVendas ().setConteudo (objRegTXT.fieldByName ("QT_VENDA").asValor ());
  }
  
  public void montarAtividadeRuralMovimentacaoRebanhoAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getMovimentacaoRebanho ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getMovimentacaoRebanho ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	MovimentacaoRebanho movRebAtual;
	if (inExt.equals ("0"))
	  movRebAtual = objDecl.getAtividadeRural ().getBrasil ().getMovimentacaoRebanho ();
	else
	  movRebAtual = objDecl.getAtividadeRural ().getExterior ().getMovimentacaoRebanho ();
	int tipoDadoMov = objRegTXT.fieldByName ("CD_ESPEC").asInteger ();
	switch (tipoDadoMov)
	  {
	  case 1:
	    setarValoresTipoDadoMovimentacaoRebanhoAnoAnterior (movRebAtual.getBovinos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 2:
	    setarValoresTipoDadoMovimentacaoRebanhoAnoAnterior (movRebAtual.getSuinos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 3:
	    setarValoresTipoDadoMovimentacaoRebanhoAnoAnterior (movRebAtual.getCaprinos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 4:
	    setarValoresTipoDadoMovimentacaoRebanhoAnoAnterior (movRebAtual.getAsininos (), vRegistro, objDecl, objRegTXT);
	    break;
	  case 5:
	    setarValoresTipoDadoMovimentacaoRebanhoAnoAnterior (movRebAtual.getOutros (), vRegistro, objDecl, objRegTXT);
	    break;
	  }
      }
  }
  
  private void setarValoresTipoDadoMovimentacaoRebanhoAnoAnterior (ItemMovimentacaoRebanho pTipo, Vector vRegistro, DeclaracaoIRPF objDecl, RegistroTxt objRegTXT) throws GeracaoTxtException
  {
    pTipo.getEstoqueInicial ().setConteudo (objRegTXT.fieldByName ("QT_ESTFINAL").asValor ());
  }
  
  public void montarAtividadeRuralApuracaoResultado (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getApuracaoResultado ().clear ();
    objDecl.getAtividadeRural ().getExterior ().getApuracaoResultado ().clear ();
    ApuracaoResultadoBrasil apurBR = objDecl.getAtividadeRural ().getBrasil ().getApuracaoResultado ();
    ApuracaoResultadoExterior apurEXT = objDecl.getAtividadeRural ().getExterior ().getApuracaoResultado ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	String inExt = objRegTXT.fieldByName ("IN_EXTERIOR").asString ();
	if (inExt.equals ("0"))
	  {
	    apurBR.getValorAdiantamento ().setConteudo (objRegTXT.fieldByName ("VR_ADIANT").asValor ());
	    apurBR.getDespesaCusteio ().setConteudo (objRegTXT.fieldByName ("VR_DESPTOTAL").asValor ());
	    apurBR.getOpcaoArbitramento ().setConteudo (objRegTXT.fieldByName ("VR_OPCAO").asValor ());
	    apurBR.getPrejuizoExercicioAnterior ().setConteudo (objRegTXT.fieldByName ("VR_PREJEXERCANT").asValor ());
	    apurBR.getPrejuizoCompensar ().setConteudo (objRegTXT.fieldByName ("VR_PREJUIZO").asValor ());
	    apurBR.getReceitaBrutaTotal ().setConteudo (objRegTXT.fieldByName ("VR_RECTOTAL").asValor ());
	    apurBR.getReceitaRecebidaContaVenda ().setConteudo (objRegTXT.fieldByName ("VR_RECVENDAFUTURA").asValor ());
	    apurBR.getResultadoI ().setConteudo (objRegTXT.fieldByName ("VR_RES1REAL").asValor ());
	    apurBR.getResultadoAposCompensacaoPrejuizo ().setConteudo (objRegTXT.fieldByName ("VR_RESAPOS").asValor ());
	    apurBR.getResultadoNaoTributavel ().setConteudo (objRegTXT.fieldByName ("VR_RESNAOTRIBAR").asValor ());
	    apurBR.getResultadoTributavel ().setConteudo (objRegTXT.fieldByName ("VR_RESTRIB").asValor ());
	  }
	else
	  {
	    apurEXT.getValorAdiantamento ().setConteudo (objRegTXT.fieldByName ("VR_ADIANT").asValor ());
	    apurEXT.getOpcaoArbitramento ().setConteudo (objRegTXT.fieldByName ("VR_OPCAO").asValor ());
	    apurEXT.getPrejuizoExercicioAnterior ().setConteudo (objRegTXT.fieldByName ("VR_PREJEXERCANT").asValor ());
	    apurEXT.getPrejuizoCompensar ().setConteudo (objRegTXT.fieldByName ("VR_PREJUIZO").asValor ());
	    apurEXT.getReceitaRecebidaContaVenda ().setConteudo (objRegTXT.fieldByName ("VR_RECVENDAFUTURA").asValor ());
	    apurEXT.getResultadoI_EmReais ().setConteudo (objRegTXT.fieldByName ("VR_RES1REAL").asValor ());
	    apurEXT.getResultadoI_EmDolar ().setConteudo (objRegTXT.fieldByName ("VR_RES1DOLAR").asValor ());
	    apurEXT.getResultadoTributavel ().setConteudo (objRegTXT.fieldByName ("VR_RES1DOLAR").asValor ());
	    apurEXT.getResultadoAposCompensacaoPrejuizo ().setConteudo (objRegTXT.fieldByName ("VR_RESAPOS").asValor ());
	    apurEXT.getResultadoNaoTributavel ().setConteudo (objRegTXT.fieldByName ("VR_RESNAOTRIBAR").asValor ());
	    apurEXT.getResultadoTributavel ().setConteudo (objRegTXT.fieldByName ("VR_RESTRIB").asValor ());
	  }
      }
  }
  
  public void montarAtividadeRuralReceitasDespesasBrasil (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getBrasil ().getReceitasDespesas ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	int mes = objRegTXT.fieldByName ("NR_MES").asInteger ();
	mes--;
	MesReceitaDespesa receitaAtual = objDecl.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getMesReceitaPorIndice (mes);
	receitaAtual.getDespesaCusteioInvestimento ().setConteudo (objRegTXT.fieldByName ("VR_DESP").asValor ());
	receitaAtual.getReceitaBrutaMensal ().setConteudo (objRegTXT.fieldByName ("VR_REC").asValor ());
      }
  }
  
  public void montarAtividadeRuralReceitasDespesasExterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getAtividadeRural ().getExterior ().getReceitasDespesas ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	ReceitaDespesa receitaAtual = new ReceitaDespesa ();
	receitaAtual.getPais ().setConteudo (objRegTXT.fieldByName ("CD_PAIS").asString ());
	receitaAtual.getDespesaCusteio ().setConteudo (objRegTXT.fieldByName ("DESPCUSTEIO").asValor ());
	receitaAtual.getReceitaBruta ().setConteudo (objRegTXT.fieldByName ("RECBRUTA").asValor ());
	receitaAtual.getResultadoI_EmDolar ().setConteudo (objRegTXT.fieldByName ("RESDOLAR").asValor ());
	receitaAtual.getResultadoIMoedaOriginal ().setConteudo (objRegTXT.fieldByName ("RESORIGINAL").asValor ());
	objDecl.getAtividadeRural ().getExterior ().getReceitasDespesas ().recuperarLista ().add (receitaAtual);
      }
  }
  
  public void montarContribuinteIRPFAnoAnterior (Vector vRegistro, Contribuinte objContrib) throws GeracaoTxtException
  {
    RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
    objContrib.getTituloEleitor ().setConteudo (objRegTXT.fieldByName ("NR_TITELEITOR").asString ());
    objContrib.getDataNascimento ().setConteudo (objRegTXT.fieldByName ("DT_NASCIM").asString ());
    objContrib.getNaturezaOcupacao ().setConteudo (objRegTXT.fieldByName ("CD_NATUR").asString ());
    objContrib.getOcupacaoPrincipal ().setConteudo (objRegTXT.fieldByName ("CD_OCUP").asString ());
    objContrib.getTipoLogradouro ().setConteudo (objRegTXT.fieldByName ("TIP_LOGRA").asString ());
    if (objRegTXT.fieldByName ("SG_UF").asString ().trim ().equals ("EX"))
      {
	objContrib.getExterior ().setConteudo (Logico.SIM);
	objContrib.getPais ().setConteudo (objRegTXT.fieldByName ("CD_PAIS").asString ());
	objContrib.getCodigoExterior ().setConteudo (objRegTXT.fieldByName ("CD_EX").asString ());
	objContrib.getCidade ().setConteudo (objRegTXT.fieldByName ("NM_MUNICIP").asString ());
	objContrib.getLogradouroExt ().setConteudo (objRegTXT.fieldByName ("NM_LOGRA").asString ());
	objContrib.getNumeroExt ().setConteudo (objRegTXT.fieldByName ("NR_NUMERO").asString ());
	objContrib.getBairroExt ().setConteudo (objRegTXT.fieldByName ("NM_BAIRRO").asString ());
	objContrib.getComplementoExt ().setConteudo (objRegTXT.fieldByName ("NM_COMPLEM").asString ());
      }
    else
      {
	objContrib.getExterior ().setConteudo (Logico.NAO);
	objContrib.getUf ().setConteudo (objRegTXT.fieldByName ("SG_UF").asString ());
	objContrib.getMunicipio ().setConteudo (objRegTXT.fieldByName ("CD_MUNICIP").asString ());
	objContrib.getPais ().setConteudo ("105");
	objContrib.getLogradouro ().setConteudo (objRegTXT.fieldByName ("NM_LOGRA").asString ());
	objContrib.getNumero ().setConteudo (objRegTXT.fieldByName ("NR_NUMERO").asString ());
	objContrib.getBairro ().setConteudo (objRegTXT.fieldByName ("NM_BAIRRO").asString ());
	objContrib.getComplemento ().setConteudo (objRegTXT.fieldByName ("NM_COMPLEM").asString ());
      }
    objContrib.getCep ().setConteudo (objRegTXT.fieldByName ("NR_CEP").asString ());
    objContrib.getDdd ().setConteudo (objRegTXT.fieldByName ("NR_DDD_TELEFONE").asString ());
    objContrib.getTelefone ().setConteudo (objRegTXT.fieldByName ("NR_TELEFONE").asString ());
  }
  
  public void montarDependentesAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getDependentes ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	Dependente objDependente = new Dependente ();
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	objDependente.setChave (objRegTXT.fieldByName ("NR_CHAVE").asString ());
	objDependente.getNome ().setConteudo (objRegTXT.fieldByName ("NM_DEPEND").asString ());
	objDependente.getDataNascimento ().setConteudo (objRegTXT.fieldByName ("DT_NASCIM").asString ());
	objDependente.getCpfDependente ().setConteudo (objRegTXT.fieldByName ("NI_DEPEND").asString ());
	objDecl.getDependentes ().recuperarLista ().add (objDependente);
      }
  }
  
  public void montarBensAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getBens ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	if (objRegTXT.fieldByName ("VR_ATUAL").asValor ().comparacao (">", "0,00"))
	  {
	    Bem objBem = new Bem ();
	    objBem.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_BEM").asString ());
	    objBem.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_BEM").asString ());
	    objBem.getValorExercicioAnterior ().setConteudo (objRegTXT.fieldByName ("VR_ATUAL").asValor ());
	    objBem.getValorExercicioAtual ().clear ();
	    int inExterior = objRegTXT.fieldByName ("IN_EXTERIOR").asInteger ();
	    if (inExterior == 1)
	      objBem.getPais ().setConteudo (objRegTXT.fieldByName ("CD_PAIS").asString ());
	    else
	      objBem.getPais ().setConteudo ("105");
	    objDecl.getBens ().recuperarLista ().add (objBem);
	  }
      }
  }
  
  public void montarDividasAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    objDecl.getDividas ().recuperarLista ().clear ();
    for (int i = 0; i < vRegistro.size (); i++)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (i);
	if (objRegTXT.fieldByName ("VR_ATUAL").asValor ().comparacao (">", "0,00"))
	  {
	    Divida objDivida = new Divida ();
	    objDivida.getDiscriminacao ().setConteudo (objRegTXT.fieldByName ("TX_DIV").asString ());
	    objDivida.getCodigo ().setConteudo (objRegTXT.fieldByName ("CD_DIV").asString ());
	    objDivida.getValorExercicioAnterior ().setConteudo (objRegTXT.fieldByName ("VR_ATUAL").asValor ());
	    objDivida.getValorExercicioAtual ().clear ();
	    objDecl.getDividas ().recuperarLista ().add (objDivida);
	  }
      }
  }
  
  public void montarConjugeAnoAnterior (Vector vRegistro, DeclaracaoIRPF objDecl) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	objDecl.getConjuge ().getCpfConjuge ().setConteudo (objRegTXT.fieldByName ("NR_CONJ").asString ());
      }
  }
  
  public RegistroTxt getRegistroRecibo (Vector vRegistro) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	return objRegTXT;
      }
    throw new GeracaoTxtException ("Detalhe do recibo n\u00e3o encontrado.");
  }
  
  public RegistroTxt getRegistroHeader (Vector vRegistro) throws GeracaoTxtException
  {
    if (vRegistro.size () > 0)
      {
	RegistroTxt objRegTXT = (RegistroTxt) vRegistro.elementAt (0);
	return objRegTXT;
      }
    throw new GeracaoTxtException ("Registro Header do arquivo n\u00e3o encontrado.");
  }
}
