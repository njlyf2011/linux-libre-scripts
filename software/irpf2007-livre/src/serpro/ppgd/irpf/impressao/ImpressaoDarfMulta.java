/* ImpressaoDarfMulta - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.impressao;
import java.io.File;
import java.util.HashMap;

import serpro.ppgd.formatosexternos.RelatorioMultiploXML;
import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.negocio.util.UtilitariosString;

public class ImpressaoDarfMulta
{
  private static final int MAX_QUOTAS = 8;
  public String titulo;
  public String relatorioJasper;
  public String xml;
  public String raiz;
  public RelatorioMultiploXML relatorio;
  public String xmlDados;
  private HashMap parametros = new HashMap ();
  private String valorMulta = "";
  private String valorJuros = "";
  RepositorioDeclaracaoCentralTxt repositorioDeclaracao = null;
  RegistroTxt registroDeclaracao = null;
  RegistroTxt registroHeader = null;
  RepositorioDeclaracaoCentralTxt repositorioRecibo = null;
  RegistroTxt registroRecibo = null;
  RegistroTxt registroMulta = null;
  IdentificadorDeclaracao idDecl;
  File arquivoDeclaracao;
  File arquivoRecibo;
  
  public ImpressaoDarfMulta (String aTitulo, String aRelatorioJasper, File arquivoDeclaracao, File arquivoRecibo, boolean visualizaImpressao)
  {
    String jasper = aRelatorioJasper;
    titulo = aTitulo;
    relatorioJasper = jasper;
    this.arquivoDeclaracao = arquivoDeclaracao;
    this.arquivoRecibo = arquivoRecibo;
    montarRelatorio ();
    if (visualizaImpressao)
      relatorio.visualizar ();
    else
      relatorio.imprimir ();
  }
  
  public void montarRelatorio ()
  {
    relatorio = new RelatorioMultiploXML (titulo, relatorioJasper, xmlDados, raiz);
    relatorio.addParametro ("icone", ClassLoader.getSystemClassLoader ().getResource ("icones/brasao.jpg").toString ());
    try
      {
	repositorioDeclaracao = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", arquivoDeclaracao);
	idDecl = repositorioDeclaracao.recuperarIdDeclaracaoNaoPersistido ();
	registroDeclaracao = repositorioDeclaracao.recuperarRegistroRecibo ();
	registroHeader = repositorioDeclaracao.recuperarRegistroHeader ();
	repositorioRecibo = new RepositorioDeclaracaoCentralTxt ("ARQ_COMPLRECIBO", arquivoRecibo);
	registroRecibo = repositorioRecibo.recuperarRegistroComplementoRecibo ();
	registroMulta = repositorioRecibo.recuperarRegistroComplementoReciboMulta ();
	repositorioRecibo.validarComplementoRecibo (idDecl);
	String templateDarfMulta = "";
	relatorio.addParametro ("nome", registroDeclaracao.fieldByName ("NM_NOME").asString ());
	relatorio.addParametro ("DDDTelefone", registroDeclaracao.fieldByName ("NR_DDD_TELEFONE").asString ());
	relatorio.addParametro ("Telefone", registroDeclaracao.fieldByName ("NR_TELEFONE").asString ());
	relatorio.addParametro ("nirf", UtilitariosString.formataCPF (registroDeclaracao.fieldByName ("NR_CPF").asString ()));
	String dtVenc = registroMulta.fieldByName ("DT_VENCIMENTO").asString ();
	relatorio.addParametro ("dataVencimento", dtVenc.substring (6, dtVenc.length ()) + "/" + dtVenc.substring (4, 6) + "/" + dtVenc.substring (0, 4));
	relatorio.addParametro ("ValorQuota", registroMulta.fieldByName ("VR_MULTA").asValor ().getConteudoFormatado ());
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  private void identificarQuota (int quota, int indexRelatorio)
  {
    switch (quota)
      {
      case 0:
	relatorio.addParametro ("observacao", "Primeira quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "30/04/2007", indexRelatorio);
	break;
      case 1:
	relatorio.addParametro ("observacao", "Segunda quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "31/05/2007", indexRelatorio);
	break;
      case 2:
	relatorio.addParametro ("observacao", "Terceira quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "29/06/2007", indexRelatorio);
	break;
      case 3:
	relatorio.addParametro ("observacao", "Quarta quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "31/07/2007", indexRelatorio);
	break;
      case 4:
	relatorio.addParametro ("observacao", "Quinta quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "31/08/2007", indexRelatorio);
	break;
      case 5:
	relatorio.addParametro ("observacao", "Sexta quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "28/09/2007", indexRelatorio);
	break;
      case 6:
	relatorio.addParametro ("observacao", "S\u00e9tima quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "31/10/2007", indexRelatorio);
	break;
      case 7:
	relatorio.addParametro ("observacao", "Oitava quota", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "30/11/2007", indexRelatorio);
	break;
      case 8:
	relatorio.addParametro ("observacao", "Quota \u00fanica", indexRelatorio);
	relatorio.addParametro ("dataVencimento", "30/04/2007", indexRelatorio);
	break;
      }
  }
}
