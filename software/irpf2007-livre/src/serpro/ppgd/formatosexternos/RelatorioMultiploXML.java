/* RelatorioMultiploXML - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioMultiploXML implements RelatorioIf
{
  private ArrayList rels;
  private ArrayList parametros;
  private String titulo;
  private JasperPrint jasperPrint;
  private boolean preparado;
  private String PARAM_PAG_INICIAL;
  
  private class duplaParam
  {
    String nome;
    String valor;
    
    public String getNome ()
    {
      return nome;
    }
    
    public void setNome (String nome)
    {
      this.nome = nome;
    }
    
    public String getValor ()
    {
      return valor;
    }
    
    public void setValor (String valor)
    {
      this.valor = valor;
    }
    
    public duplaParam (String nome, String valor)
    {
      this.nome = nome;
      this.valor = valor;
    }
  }
  
  public RelatorioMultiploXML (String aTitulo, String aRelatorioJasper, String aXml, String aRaiz)
  {
    rels = new ArrayList ();
    parametros = new ArrayList ();
    preparado = false;
    PARAM_PAG_INICIAL = "pagInicial";
    titulo = aTitulo;
    rels.add (new RelatorioXML (aTitulo, aRelatorioJasper, aXml, aRaiz));
  }
  
  public RelatorioMultiploXML (String aTitulo, String aRelatorioJasper)
  {
    rels = new ArrayList ();
    parametros = new ArrayList ();
    preparado = false;
    PARAM_PAG_INICIAL = "pagInicial";
    titulo = aTitulo;
    rels.add (new RelatorioXML (aTitulo, aRelatorioJasper));
  }
  
  public void addRelatorioXML (String aTitulo, String aRelatorioJasper, String aXml, String aRaiz)
  {
    RelatorioXML novoRelatorio = new RelatorioXML (aTitulo, aRelatorioJasper, aXml, aRaiz);
    for (int x = 0; x < parametros.size (); x++)
      {
	String nome = ((duplaParam) parametros.get (x)).getNome ();
	String valor = ((duplaParam) parametros.get (x)).getValor ();
	novoRelatorio.addParametro (nome, valor);
      }
    rels.add (novoRelatorio);
  }
  
  public void addRelatorioXML (String aTitulo, String aRelatorioJasper)
  {
    RelatorioXML novoRelatorio = new RelatorioXML (aTitulo, aRelatorioJasper);
    for (int x = 0; x < parametros.size (); x++)
      {
	String nome = ((duplaParam) parametros.get (x)).getNome ();
	String valor = ((duplaParam) parametros.get (x)).getValor ();
	novoRelatorio.addParametro (nome, valor);
      }
    rels.add (novoRelatorio);
  }
  
  public void imprimir ()
  {
    try
      {
	prepara ();
	JasperPrintManager.printReport (jasperPrint, false);
      }
    catch (JRException e)
      {
	e.printStackTrace ();
      }
  }
  
  public void prepara ()
  {
    if (! isPreparado ())
      {
	int pageCount = 1;
	jasperPrint = new JasperPrint ();
	int y = rels.size ();
	for (int x = 0; x < y; x++)
	  {
	    addParametro (PARAM_PAG_INICIAL, String.valueOf (pageCount), x);
	    JasperPrint printTmp = ((RelatorioXML) rels.get (x)).obterPaginas ();
	    List lista = printTmp.getPages ();
	    pageCount += lista.size ();
	    if (x == 0)
	      jasperPrint = printTmp;
	    else
	      {
		for (int i = 0; i < lista.size (); i++)
		  jasperPrint.addPage ((JRPrintPage) lista.get (i));
	      }
	  }
	preparado = true;
      }
  }
  
  public void visualizar ()
  {
    try
      {
	prepara ();
	JasperViewer viewer = new JasperViewer (jasperPrint, false);
	viewer.setTitle (getTitulo ());
	viewer.setIconImage (Frame.getFrames ()[0].getIconImage ());
	viewer.setVisible (true);
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
      }
    catch (/*JR*/Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void addParametro (String nome, String valor)
  {
    for (int x = 0; x < rels.size (); x++)
      ((RelatorioXML) rels.get (x)).addParametro (nome, valor);
    parametros.add (new duplaParam (nome, valor));
  }
  
  public void addParametro (String nome, String valor, int indice)
  {
    ((RelatorioXML) rels.get (indice)).addParametro (nome, valor);
  }
  
  public void addParametroUltimo (String nome, String valor)
  {
    ((RelatorioXML) rels.get (rels.size () - 1)).addParametro (nome, valor);
  }
  
  public ArrayList getAllParametro ()
  {
    return parametros;
  }
  
  public String getTitulo ()
  {
    return titulo;
  }
  
  public void setTitulo (String aTitulo)
  {
    titulo = aTitulo;
  }
  
  public boolean isHabilitado ()
  {
    return false;
  }
  
  public void setHabilitado (boolean aHabilitado)
  {
    /* empty */
  }
  
  public boolean isPreparado ()
  {
    return preparado;
  }
  
  public int getQtdPaginas ()
  {
    int qtd = -1;
    if (isPreparado ())
      qtd = jasperPrint.getPages ().size ();
    return qtd;
  }
}
