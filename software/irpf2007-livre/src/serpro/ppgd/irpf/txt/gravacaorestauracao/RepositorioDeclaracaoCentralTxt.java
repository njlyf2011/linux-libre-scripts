/* RepositorioDeclaracaoCentralTxt - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.gravacaorestauracao;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.util.UtilitariosArquivo;

public class RepositorioDeclaracaoCentralTxt
{
  public static byte FINALIDADE_ENTREGA = 0;
  public static byte FINALIDADE_BACKUP = 1;
  private DocumentoAjusteTXT arquivo;
  private File file;
  private boolean fLido = false;
  private IdentificadorDeclaracao objIdArquivo;
  private ConversorRegistros2ObjetosIRPF conversor2ObjIRPF;
  private ConversorObjetosIRPF2Registros conversor2Registros;
  
  public RepositorioDeclaracaoCentralTxt (String tipoArq, File file) throws GeracaoTxtException
  {
    this.file = file;
    arquivo = new DocumentoAjusteTXT (tipoArq, file.getPath ());
    conversor2ObjIRPF = new ConversorRegistros2ObjetosIRPF ();
    conversor2Registros = new ConversorObjetosIRPF2Registros ();
  }
  
  public IdentificadorDeclaracao recuperarIdDeclaracao () throws Exception
  {
    lerDeclaracao ();
    Vector vetorRegHeader = arquivo.getRegistrosTxt ("IR");
    Vector vetorRegIdentificacao = arquivo.getRegistrosTxt ("16");
    objIdArquivo = conversor2ObjIRPF.montarIdDeclaracao (vetorRegHeader, vetorRegIdentificacao);
    return objIdArquivo;
  }
  
  public void atualizarNroReciboTransmitida (String nrRecibo) throws Exception
  {
    arquivo.atualizarNrReciboTransmitida (nrRecibo);
    arquivo.setBKPno ();
    arquivo.salvar ();
  }
  
  public String recuperarNroRecibo () throws Exception
  {
    return recuperarRegistroHeader ().fieldByName ("NR_HASH").asString ();
  }
  
  public IdentificadorDeclaracao recuperarIdDeclaracaoAnoAnterior () throws GeracaoTxtException, IOException
  {
    lerDeclaracao ();
    Vector vetorRegHeader = arquivo.getRegistrosTxt ("IR");
    Vector vetorRegIdentificacao = arquivo.getRegistrosTxt ("16");
    objIdArquivo = conversor2ObjIRPF.montarIdDeclaracaoAnoAnterior (vetorRegHeader, vetorRegIdentificacao);
    return objIdArquivo;
  }
  
  public IdentificadorDeclaracao recuperarIdDeclaracaoNaoPersistido () throws GeracaoTxtException, IOException
  {
    lerDeclaracao ();
    Vector vetorRegistros = arquivo.getRegistrosTxt ("IR");
    objIdArquivo = conversor2ObjIRPF.montarIdDeclaracaoNaoPersistido (vetorRegistros);
    return objIdArquivo;
  }
  
  public void recuperarDeclaracao (IdentificadorDeclaracao idDecl) throws GeracaoTxtException
  {
    arquivo.validarHeader (idDecl);
    DeclaracaoIRPF objDecl = IRPFFacade.getInstancia ().recuperarDeclaracaoIRPF (idDecl.getCpf ().asString ());
    Vector vetorRegistros = arquivo.getRegistrosTxt ("16");
    conversor2ObjIRPF.montarContribuinteIRPF (vetorRegistros, objDecl.getContribuinte ());
    vetorRegistros = arquivo.getRegistrosTxt ("29");
    conversor2ObjIRPF.montarConjuge (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("27");
    conversor2ObjIRPF.montarBem (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("28");
    conversor2ObjIRPF.montarDividas (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("19");
    conversor2ObjIRPF.montarDeclaracaoCompleta (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("21");
    conversor2ObjIRPF.montarRendPJTitularCompleta (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("32");
    conversor2ObjIRPF.montarRendPJDependentesCompleta (vetorRegistros, objDecl);
    if (arquivo.getRegistrosTxt ("21").size () == 0)
      {
	vetorRegistros = arquivo.getRegistrosTxt ("31");
	conversor2ObjIRPF.montarRendPJTitularSimplificada (vetorRegistros, objDecl);
      }
    vetorRegistros = arquivo.getRegistrosTxt ("22");
    conversor2ObjIRPF.montarRendimentosPF (vetorRegistros, objDecl, false);
    conversor2ObjIRPF.montarRendimentosPF (vetorRegistros, objDecl, true);
    vetorRegistros = arquivo.getRegistrosTxt ("23");
    conversor2ObjIRPF.recuperarRendIsentosNaoTributaveis (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("24");
    conversor2ObjIRPF.recuperarRendTributacaoExclusiva (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("35");
    conversor2ObjIRPF.montarAlimentandos (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("25");
    conversor2ObjIRPF.montarDependentes (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("26");
    conversor2ObjIRPF.montarPagamentos (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("33");
    conversor2ObjIRPF.montarLucrosDividendos (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("34");
    conversor2ObjIRPF.montarDoacoesCampanha (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("40");
    conversor2ObjIRPF.montarRendaVariavel (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("42");
    conversor2ObjIRPF.montarRendaVariavelFII (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("30");
    conversor2ObjIRPF.montarInventariante (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("36");
    conversor2ObjIRPF.montarCPFDependentesComRendPF (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("50");
    conversor2ObjIRPF.montarAtividadeRuralImoveis (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("54");
    conversor2ObjIRPF.montarAtividadeRuralBens (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("55");
    conversor2ObjIRPF.montarAtividadeRuralDividas (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("53");
    conversor2ObjIRPF.montarAtividadeRuralMovimentacaoRebanho (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("52");
    conversor2ObjIRPF.montarAtividadeRuralApuracaoResultado (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("51");
    conversor2ObjIRPF.montarAtividadeRuralReceitasDespesasBrasil (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("56");
    conversor2ObjIRPF.montarAtividadeRuralReceitasDespesasExterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("16");
    conversor2ObjIRPF.montarContribuinteIRPF (vetorRegistros, objDecl.getContribuinte ());
    conversor2ObjIRPF.montarInformacoesBancarias (vetorRegistros, objDecl);
    conversor2ObjIRPF.montarInformacoesObrigatorias (vetorRegistros, objDecl);
    objDecl.adicionaObservadoresCalculosLate ();
    IRPFFacade.getInstancia ().salvarDeclaracao (idDecl.getCpf ().asString ());
  }
  
  public void gravarDeclaracao (IdentificadorDeclaracao objIdDecl) throws GeracaoTxtException, IOException
  {
    DeclaracaoIRPF objDecl = IRPFFacade.getInstancia ().recuperarDeclaracaoIRPF (objIdDecl.getCpf ().asString ());
    atualizarDeclaracao (objDecl, FINALIDADE_ENTREGA);
    String hash = arquivo.calcularHash ();
    Vector vRegistros = conversor2Registros.montarRegistroHeader (objDecl);
    arquivo.atualizaHeader (vRegistros, hash);
    vRegistros = conversor2Registros.montarRecibo (objDecl);
    arquivo.incluirRecibo (vRegistros, hash);
    arquivo.salvar ();
    IRPFFacade.getInstancia ().salvarDeclaracao (objIdDecl.getCpf ().asString ());
  }
  
  public void salvarDeclaracao (IdentificadorDeclaracao objIdDecl) throws GeracaoTxtException, IOException
  {
    DeclaracaoIRPF objDecl = IRPFFacade.getInstancia ().recuperarDeclaracaoIRPF (objIdDecl.getCpf ().asString ());
    atualizarDeclaracao (objDecl, FINALIDADE_BACKUP);
    String hash = arquivo.calcularHash ();
    Vector vRegistros = conversor2Registros.montarRegistroHeader (objDecl);
    arquivo.atualizaHeader (vRegistros, hash);
    arquivo.salvar ();
  }
  
  public void atualizarDeclaracao (DeclaracaoIRPF objDecl, byte finalidade) throws GeracaoTxtException
  {
    boolean gravarRegistrosDaCompleta = objDecl.getIdentificadorDeclaracao ().isCompleta () || finalidade == FINALIDADE_BACKUP;
    arquivo.clear ();
    Vector vetorRegistros = conversor2Registros.montarRegistroHeader (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarRegistroContribuinte (objDecl);
    arquivo.setFicha (vetorRegistros);
    if (gravarRegistrosDaCompleta)
      {
	if (! objDecl.getIdentificadorDeclaracao ().isCompleta ())
	  {
	    vetorRegistros = conversor2Registros.montarFichaSimplificada (objDecl);
	    arquivo.setFicha (vetorRegistros);
	  }
	vetorRegistros = conversor2Registros.montarRegistroDeclaracaoCompleta (objDecl);
	arquivo.setFicha (vetorRegistros);
	if (finalidade == FINALIDADE_ENTREGA)
	  {
	    vetorRegistros = conversor2Registros.montarFichaResumoCompleta (objDecl);
	    arquivo.setFicha (vetorRegistros);
	  }
	vetorRegistros = conversor2Registros.montarFichaRendPJ (objDecl);
	arquivo.setFicha (vetorRegistros);
	vetorRegistros = conversor2Registros.montarFichaRendPF (objDecl, false);
	arquivo.setFicha (vetorRegistros);
	vetorRegistros = conversor2Registros.montarFichaRendPF (objDecl, true);
	arquivo.setFicha (vetorRegistros);
	vetorRegistros = conversor2Registros.montarFichaRendIsentos (objDecl);
	arquivo.setFicha (vetorRegistros);
	vetorRegistros = conversor2Registros.montarFichaRendTribExcl (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    else
      {
	vetorRegistros = conversor2Registros.montarFichaSimplificada (objDecl);
	arquivo.setFicha (vetorRegistros);
	vetorRegistros = conversor2Registros.montarFichaResumoSimplificada (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    if (finalidade != FINALIDADE_ENTREGA || objDecl.getIdentificadorDeclaracao ().isCompleta ())
      {
	vetorRegistros = conversor2Registros.montarFichaDependentes (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    if (gravarRegistrosDaCompleta)
      {
	vetorRegistros = conversor2Registros.montarFichaPagamentos (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    vetorRegistros = conversor2Registros.montarFichaBem (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaDividas (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaConjuge (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaInventariante (objDecl);
    arquivo.setFicha (vetorRegistros);
    if (! objDecl.getIdentificadorDeclaracao ().isCompleta ())
      {
	vetorRegistros = conversor2Registros.montarFichaRendPJSimplificada (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    vetorRegistros = conversor2Registros.montarFichaRendPJDependente (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaLucrosDividendos (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaDoacoesCampanha (objDecl);
    arquivo.setFicha (vetorRegistros);
    if (gravarRegistrosDaCompleta)
      {
	vetorRegistros = conversor2Registros.montarFichaAlimentandos (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    if (gravarRegistrosDaCompleta)
      {
	vetorRegistros = conversor2Registros.montarFichaCPFDependentesRendPF (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    if (! objDecl.getRendaVariavel ().isVazio ())
      {
	vetorRegistros = conversor2Registros.montarFichaRendaVariavel (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    if (! objDecl.getRendaVariavel ().isVazio ())
      {
	vetorRegistros = conversor2Registros.montarFichaRendaVariavelAnual (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    if (! objDecl.getRendaVariavel ().getFundInvest ().isVazio ())
      {
	vetorRegistros = conversor2Registros.montarFichaRendaVariavelFII (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    if (! objDecl.getRendaVariavel ().getFundInvest ().isVazio ())
      {
	vetorRegistros = conversor2Registros.montarFichaRendaVariavelTotaisFII (objDecl);
	arquivo.setFicha (vetorRegistros);
      }
    vetorRegistros = conversor2Registros.montarFichaAtividadeRuralIdentificacaoImovel (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaAtividadeRuralReceitasDespesasBrasil (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaAtividadeRuralApuracaoResultado (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaAtividadeRuralMovimentacaoRebanho (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaAtividadeRuralBens (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaAtividadeRuralDividas (objDecl);
    arquivo.setFicha (vetorRegistros);
    vetorRegistros = conversor2Registros.montarFichaAtividadeRuralReceitasDespesasExterior (objDecl);
    arquivo.setFicha (vetorRegistros);
    arquivo.incluirTrailler (objDecl.getIdentificadorDeclaracao ().getCpf ().asString ());
  }
  
  public void importarDeclaracaoAnoAnterior (IdentificadorDeclaracao idDecl) throws GeracaoTxtException
  {
    arquivo.validarHeaderAnoAnt (idDecl, getValidaHash ());
    DeclaracaoIRPF objDecl = IRPFFacade.getInstancia ().recuperarDeclaracaoIRPF (idDecl.getCpf ().asString ());
    Vector vetorRegistros = arquivo.getRegistrosTxt ("16");
    conversor2ObjIRPF.montarContribuinteIRPFAnoAnterior (vetorRegistros, objDecl.getContribuinte ());
    vetorRegistros = arquivo.getRegistrosTxt ("27");
    conversor2ObjIRPF.montarBensAnoAnterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("28");
    conversor2ObjIRPF.montarDividasAnoAnterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("29");
    conversor2ObjIRPF.montarConjugeAnoAnterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("25");
    conversor2ObjIRPF.montarDependentesAnoAnterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("30");
    conversor2ObjIRPF.montarInventarianteAnoAnterior (vetorRegistros, objDecl);
    idDecl.getDeclaracaoRetificadora ().clear ();
    idDecl.getNumReciboDecRetif ().clear ();
    objDecl.getIdentificadorDeclaracao ().getEnderecoDiferente ().clear ();
    vetorRegistros = arquivo.getRegistrosTxt ("50");
    conversor2ObjIRPF.montarAtividadeRuralImoveisAnoAnterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("54");
    conversor2ObjIRPF.montarAtividadeRuralBensAnoAnterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("55");
    conversor2ObjIRPF.montarAtividadeRuralDividasAnoAnterior (vetorRegistros, objDecl);
    vetorRegistros = arquivo.getRegistrosTxt ("53");
    conversor2ObjIRPF.montarAtividadeRuralMovimentacaoRebanhoAnoAnterior (vetorRegistros, objDecl);
    IRPFFacade.getInstancia ().salvarDeclaracao (idDecl.getCpf ().asString ());
  }
  
  public void validarDeclaracao () throws Exception
  {
    if (objIdArquivo == null)
      objIdArquivo = recuperarIdDeclaracao ();
    arquivo.validarCRC ();
    arquivo.validarHeader (objIdArquivo);
  }
  
  public void validarDeclaracaoNaoPersistido () throws GeracaoTxtException, IOException
  {
    if (objIdArquivo == null)
      objIdArquivo = recuperarIdDeclaracaoNaoPersistido ();
    arquivo.validarCRC ();
    arquivo.validarHeader (objIdArquivo);
  }
  
  public void lerDeclaracao () throws GeracaoTxtException, IOException
  {
    if (arquivo == null)
      arquivo = new DocumentoAjusteTXT ("ARQ_IRPF", file.getPath ());
    if (! fLido)
      arquivo.ler ();
    fLido = true;
  }
  
  public RegistroTxt recuperarRegistroHeader () throws GeracaoTxtException, IOException
  {
    lerDeclaracao ();
    Vector vetorHeader = arquivo.getRegistrosTxt ("IR");
    return conversor2ObjIRPF.getRegistroHeader (vetorHeader);
  }
  
  public RegistroTxt recuperarRegistroRecibo () throws GeracaoTxtException, IOException
  {
    lerDeclaracao ();
    Vector vetorRecibo = arquivo.getRegistrosTxt ("DR");
    return conversor2ObjIRPF.getRegistroRecibo (vetorRecibo);
  }
  
  public RegistroTxt recuperarRegistroComplementoRecibo () throws GeracaoTxtException, IOException
  {
    lerDeclaracao ();
    Vector vetorRecibo = arquivo.getRegistrosTxt ("RC");
    return conversor2ObjIRPF.getRegistroRecibo (vetorRecibo);
  }
  
  public RegistroTxt recuperarRegistroComplementoReciboMulta () throws GeracaoTxtException, IOException
  {
    lerDeclaracao ();
    Vector vetorRecibo = arquivo.getRegistrosTxt ("NC");
    if (vetorRecibo.isEmpty ())
      return null;
    return conversor2ObjIRPF.getRegistroRecibo (vetorRecibo);
  }
  
  public String getPath ()
  {
    return file.getPath ();
  }
  
  private boolean getValidaHash ()
  {
    if (UtilitariosArquivo.extraiExtensaoAquivo (getPath ()).toUpperCase ().equals (".F2B"))
      return false;
    return true;
  }
  
  public void validarComplementoRecibo (IdentificadorDeclaracao idDecl) throws GeracaoTxtException, IOException
  {
    lerDeclaracao ();
    arquivo.validarCRCAcumulado ();
    arquivo.validarComplRecibo (idDecl);
  }
  
  public DocumentoAjusteTXT getArquivo ()
  {
    return arquivo;
  }

  public ConversorObjetosIRPF2Registros getConversor2Registros ()
  {
    return conversor2Registros;
  }
}
