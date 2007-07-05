/* RelatorioDB - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class RelatorioDB implements RelatorioIf
{
  private static String driver = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.driver");
  private static String url = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.url");
  private static String login = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.usuario");
  private static String pass = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.conexaoDB.senha");
  private String relatorioJasper;
  private boolean isHabilitado = true;
  private String titulo;
  private HashMap params;
  private JasperPrint jasperPrint;
  private boolean preparado = false;
  
  public RelatorioDB (String aTitulo, String aRelatorioJasper)
  {
    setTitulo (aTitulo);
    setRelatorioJasper (aRelatorioJasper);
    params = new HashMap ();
  }
  
  public void imprimir ()
  {
    try
      {
	prepara ();
	JasperPrintManager.printReport (jasperPrint, true);
      }
    catch (/*JR*/Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void visualizar ()
  {
    try
      {
	prepara ();
	JasperViewer viewer = new JasperViewer (jasperPrint, true);
	viewer.setVisible (true);
      }
    catch (/*JR*/Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void prepara ()
  {
    if (! isPreparado ())
      {
	try
	  {
	    Connection con = DriverManager.getConnection (url, login, pass);
	    InputStream streamRel = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (getRelatorioJasper ());
	    jasperPrint = JasperFillManager.fillReport (streamRel, params, con);
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	preparado = true;
      }
  }
  
  public String getTitulo ()
  {
    return titulo;
  }
  
  public void setTitulo (String aTitulo)
  {
    titulo = aTitulo;
  }
  
  private void setRelatorioJasper (String relatorioJasper)
  {
    this.relatorioJasper = relatorioJasper;
  }
  
  private String getRelatorioJasper ()
  {
    return relatorioJasper;
  }
  
  public boolean isHabilitado ()
  {
    return isHabilitado;
  }
  
  public void setHabilitado (boolean aHabilitado)
  {
    isHabilitado = aHabilitado;
  }
  
  public boolean isPreparado ()
  {
    return preparado;
  }
  
  static
  {
    try
      {
	Class.forName (driver);
      }
    catch (ClassNotFoundException e)
      {
	e.printStackTrace ();
      }
  }
}
