/* AcaoMudaPainel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.acoes;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import org.swixml.SwingEngine;

import serpro.ppgd.gui.FabricaGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.treeview.NoGenerico;
import serpro.ppgd.infraestrutura.util.PainelCacher;
import serpro.ppgd.negocio.util.LogPPGD;

public class AcaoMudaPainel extends AbstractAction
{
  protected JPanel painelAcionado;
  protected String painelStr = null;
  protected SwingEngine engine;
  
  public AcaoMudaPainel ()
  {
    /* empty */
  }
  
  public AcaoMudaPainel (String aPainelStr)
  {
    setPainelStr (aPainelStr);
  }
  
  protected void acionaPainel (String pRotuloNodo)
  {
    try
      {
	painelAcionado = PainelCacher.getInstance ().obtemUrgentemente (painelStr);
	PlataformaPPGD.getPlataforma ().mudaPainelExibido (painelAcionado);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void setPainelStr (String painelStr)
  {
    this.painelStr = painelStr;
    PainelCacher.getInstance ().fazCacheDe (this.painelStr);
  }
  
  public String getPainelStr ()
  {
    return painelStr;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    FabricaGUI.esconderPainelDicas ();
    if (e != null && e.getActionCommand () != null && e.getActionCommand ().length () > 0 && getPainelStr () == null)
      setPainelStr (e.getActionCommand ());
    if (painelStr != null && painelStr.trim ().length () > 0)
      {
	if (e.getSource () instanceof NoGenerico)
	  {
	    NoGenerico nodoSource = (NoGenerico) e.getSource ();
	    acionaPainel (nodoSource.getNome ());
	  }
	else
	  acionaPainel (e.getActionCommand ());
      }
    else
      LogPPGD.debug ("Erro: " + this.getClass ().getName () + ": faltou especificar o painel a ser acionado!");
  }
}
