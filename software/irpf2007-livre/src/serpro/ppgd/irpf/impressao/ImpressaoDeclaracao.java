/* ImpressaoDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.impressao;
import serpro.ppgd.formatosexternos.RelatorioMultiploXML;

public class ImpressaoDeclaracao
{
  public RelatorioMultiploXML relatorio;
  public String xmlDados;
  private boolean relatorioVazio;
  
  public ImpressaoDeclaracao (String aTitulo, String aRelatorioJasper, String aXml, String aRaiz)
  {
    String jasper = aRelatorioJasper;
    relatorio = new RelatorioMultiploXML (aTitulo, jasper, aXml, aRaiz);
    relatorioVazio = false;
  }
  
  public ImpressaoDeclaracao ()
  {
    relatorioVazio = true;
  }
  
  public void addImpressaoDeclaracao (String aTitulo, String aRelatorioJasper, String aXml, String aRaiz)
  {
    if (relatorioVazio)
      {
	String jasper = aRelatorioJasper;
	relatorio = new RelatorioMultiploXML (aTitulo, jasper, aXml, aRaiz);
	relatorioVazio = false;
      }
    else
      {
	String jasper = aRelatorioJasper;
	relatorio.addRelatorioXML (aTitulo, jasper, aXml, aRaiz);
      }
  }
  
  public void addParametro (String nome, String valor, int indice)
  {
    relatorio.addParametro (nome, valor, indice);
  }
  
  public void addParametro (String nome, String valor)
  {
    relatorio.addParametro (nome, valor);
  }
  
  public void addParametroUltimo (String nome, String valor)
  {
    relatorio.addParametroUltimo (nome, valor);
  }
  
  public void visualizar ()
  {
    relatorio.visualizar ();
  }
  
  public void imprimir ()
  {
    relatorio.imprimir ();
  }
}
