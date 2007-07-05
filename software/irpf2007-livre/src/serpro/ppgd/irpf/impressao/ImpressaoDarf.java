/* ImpressaoDarf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.impressao;
import java.util.HashMap;

import serpro.ppgd.formatosexternos.RelatorioMultiploXML;
import serpro.ppgd.formatosexternos.barcode.Barcode;
import serpro.ppgd.formatosexternos.barcode.BarcodeEncoder;
import serpro.ppgd.formatosexternos.barcode.util.DarfAdaptor;
import serpro.ppgd.irpf.util.IRPFUtil;

public class ImpressaoDarf
{
  private static final int MAX_QUOTAS = 8;
  public String titulo;
  public String relatorioJasper;
  public String xml;
  public String raiz;
  public RelatorioMultiploXML relatorio;
  public String xmlDados;
  private HashMap parametros;
  private String valorMulta;
  private String valorJuros;
  private String nirf;
  private String nome;
  private boolean[] quotas;
  private DarfAdaptor dados;
  
  public ImpressaoDarf (String aTitulo, String aRelatorioJasper, String aXml, String aRaiz, String Nirf, boolean[] quotas, boolean visualizaImpressao, String Nome, DarfAdaptor dados)
  {
    parametros = new HashMap ();
    valorMulta = "";
    valorJuros = "";
    nirf = "";
    nome = "";
    this.quotas = new boolean[8];
    String jasper = aRelatorioJasper;
    nirf = Nirf;
    nome = Nome;
    xmlDados = aXml;
    titulo = aTitulo;
    relatorioJasper = jasper;
    raiz = aRaiz;
    this.quotas = quotas;
    this.dados = dados;
    relatorio = new RelatorioMultiploXML (titulo, relatorioJasper, xmlDados, raiz);
    montarRelatorio ();
    if (visualizaImpressao)
      relatorio.visualizar ();
    else
      relatorio.imprimir ();
  }
  
  public ImpressaoDarf (String aTitulo, String aRelatorioJasper, String aXml, String aRaiz, String Nirf, boolean[] quotas, String Nome, DarfAdaptor dados)
  {
    parametros = new HashMap ();
    valorMulta = "";
    valorJuros = "";
    nirf = "";
    nome = "";
    this.quotas = new boolean[8];
    String jasper = aRelatorioJasper;
    nirf = Nirf;
    nome = Nome;
    xmlDados = aXml;
    titulo = aTitulo;
    relatorioJasper = jasper;
    raiz = aRaiz;
    this.quotas = quotas;
    this.dados = dados;
    relatorio = new RelatorioMultiploXML (titulo, relatorioJasper, xmlDados, raiz);
  }
  
  private void montarRelatorioCodBarra ()
  {
    String strPath = IRPFUtil.DIR_DADOS + "/img" + dados.getCpfCnpj ().asString () + ".jpg";
    BarcodeEncoder encoder = BarcodeEncoder.gerarCodigo (dados);
    Barcode.gerarBarcode (encoder.getNumero (), strPath);
    relatorio.addParametro ("imgCodBarra", strPath);
    relatorio.addParametro ("municipio", dados.getMunicipio ());
    relatorio.addParametro ("grupoCodBarra1", encoder.getGrupos ()[0]);
    relatorio.addParametro ("grupoCodBarra2", encoder.getGrupos ()[1]);
    relatorio.addParametro ("grupoCodBarra3", encoder.getGrupos ()[2]);
    relatorio.addParametro ("grupoCodBarra4", encoder.getGrupos ()[3]);
    adicionarParametros ();
    if (quotas.length == 1)
      identificarQuota (8, 0);
    else
      identificarQuota (0, 0);
  }
  
  public void vizualizarDarfCodBarra ()
  {
    montarRelatorioCodBarra ();
    relatorio.visualizar ();
  }
  
  private void adicionarParametros ()
  {
    relatorio.addParametro ("nirf", nirf);
    relatorio.addParametro ("icone", ClassLoader.getSystemClassLoader ().getResource ("icones/brasao.jpg").toString ());
    relatorio.addParametro ("nome", nome);
  }
  
  private void montarRelatorio ()
  {
    adicionarParametros ();
    int contRelatorio = 0;
    if (quotas.length == 1)
      identificarQuota (8, 0);
    else
      {
	for (int i = 0; i < quotas.length; i++)
	  {
	    if (quotas[i])
	      {
		if (contRelatorio != 0)
		  relatorio.addRelatorioXML (titulo, relatorioJasper, xmlDados, raiz);
		identificarQuota (i, contRelatorio);
		contRelatorio++;
	      }
	  }
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
