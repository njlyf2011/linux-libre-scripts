/* RelatorioXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.InputStream;
import java.util.HashMap;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

import serpro.ppgd.negocio.util.LogPPGD;

public class RelatorioXML implements RelatorioIf
{
  private String xml;
  private String raiz;
  private String relatorioJasper;
  private String titulo;
  private boolean habilitado = true;
  private HashMap parametros;
  
  public RelatorioXML (String aTitulo, String aRelatorioJasper, String aXml, String aRaiz)
  {
    parametros = new HashMap ();
    setTitulo (aTitulo);
    setRelatorioJasper (aRelatorioJasper.replaceAll ("%20", " "));
    setXml (aXml);
    setRaiz (aRaiz);
  }
  
  public RelatorioXML (String aTitulo, String aRelatorioJasper)
  {
    parametros = new HashMap ();
    setTitulo (aTitulo);
    setRelatorioJasper (aRelatorioJasper.replaceAll ("%20", " "));
  }
  
  public void visualizar ()
  {
    try
      {
	JasperPrint print;
	if (getXml () != null && ! getXml ().equals (""))
	  {
	    JRXmlDataSourcePPGD xmlDataSource = new JRXmlDataSourcePPGD (getXml (), getRaiz ());
	    InputStream streamRel = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (getRelatorioJasper ());
	    LogPPGD.debug ("rel jasper: " + getRelatorioJasper ());
	    LogPPGD.debug ("streamrel: " + streamRel);
	    print = JasperFillManager.fillReport (streamRel, parametros, xmlDataSource);
	  }
	else
	  {
	    JREmptyDataSource xmlDataSource = new JREmptyDataSource ();
	    InputStream streamRel = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (getRelatorioJasper ());
	    LogPPGD.debug ("rel jasper: " + getRelatorioJasper ());
	    LogPPGD.debug ("streamrel: " + streamRel);
	    print = JasperFillManager.fillReport (streamRel, parametros, xmlDataSource);
	  }
	JasperViewer viewer = new JasperViewer (print, false);
	viewer.setTitle (getTitulo ());
	viewer.setIconImage (Frame.getFrames ()[0].getIconImage ());
	viewer.addWindowFocusListener (new WindowFocusListener ()
	{
	  private boolean firstLost = true;
	  
	  public void windowLostFocus (WindowEvent e)
	  {
	    if (firstLost)
	      {
		e.getWindow ().toFront ();
		firstLost = false;
	      }
	  }
	  
	  public void windowGainedFocus (WindowEvent e)
	  {
	    /* empty */
	  }
	});
	viewer.setVisible (true);
      }
    catch (JRException e)
      {
	e.printStackTrace ();
      }
  }
  
  public void addParametro (String nome, String valor)
  {
    parametros.put (nome, valor);
  }
  
  public HashMap getAllParametro ()
  {
    return parametros;
  }
  
  public void imprimir ()
  {
    try
      {
	JasperPrint print;
	if (getXml () != null && ! getXml ().equals (""))
	  {
	    JRXmlDataSourcePPGD xmlDataSource = new JRXmlDataSourcePPGD (getXml (), getRaiz ());
	    InputStream streamRel = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (getRelatorioJasper ());
	    print = JasperFillManager.fillReport (streamRel, parametros, xmlDataSource);
	  }
	else
	  {
	    JREmptyDataSource xmlDataSource = new JREmptyDataSource ();
	    InputStream streamRel = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (getRelatorioJasper ());
	    print = JasperFillManager.fillReport (streamRel, parametros, xmlDataSource);
	  }
	JasperPrintManager.printReport (print, false);
      }
    catch (JRException e)
      {
	e.printStackTrace ();
      }
  }
  
  public JasperPrint obterPaginas ()
  {
    JasperPrint jasperprint;
    try
      {
	JasperPrint print;
	if (getXml () != null && ! getXml ().equals (""))
	  {
	    JRXmlDataSourcePPGD xmlDataSource = new JRXmlDataSourcePPGD (getXml (), getRaiz ());
	    InputStream streamRel = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (getRelatorioJasper ());
	    LogPPGD.debug ("rel jasper: " + getRelatorioJasper ());
	    LogPPGD.debug ("streamrel: " + streamRel);
	    print = JasperFillManager.fillReport (streamRel, parametros, xmlDataSource);
	  }
	else
	  {
	    JREmptyDataSource xmlDataSource = new JREmptyDataSource ();
	    InputStream streamRel = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (getRelatorioJasper ());
	    print = JasperFillManager.fillReport (streamRel, parametros, xmlDataSource);
	  }
	jasperprint = print;
      }
    catch (JRException e)
      {
	e.printStackTrace ();
	return null;
      }
    return jasperprint;
  }
  
  public void setHabilitado (boolean habilitado)
  {
    this.habilitado = habilitado;
  }
  
  public boolean isHabilitado ()
  {
    return habilitado;
  }
  
  public void setXml (String xml)
  {
    this.xml = xml;
  }
  
  public String getXml ()
  {
    return xml;
  }
  
  private void setRaiz (String raiz)
  {
    this.raiz = raiz;
  }
  
  private String getRaiz ()
  {
    return raiz;
  }
  
  private void setRelatorioJasper (String relatorioJasper)
  {
    this.relatorioJasper = relatorioJasper;
  }
  
  private String getRelatorioJasper ()
  {
    return relatorioJasper;
  }
  
  public void setTitulo (String titulo)
  {
    this.titulo = titulo;
  }
  
  public String getTitulo ()
  {
    return titulo;
  }
  
  public void prepara ()
  {
    /* empty */
  }
  
  public boolean isPreparado ()
  {
    return false;
  }
}
