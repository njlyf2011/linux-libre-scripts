/* VisualizadorHelp - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import javax.swing.ImageIcon;

import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class VisualizadorHelp
{
  private HelpUtil help;
  
  public VisualizadorHelp ()
  {
    carregaHelp ();
  }
  
  private void carregaHelp ()
  {
    String helpSetName = FabricaUtilitarios.getProperties ().getProperty ("help", "");
    String defaultHelpID = FabricaUtilitarios.getProperties ().getProperty ("help.default.id", "");
    if (! helpSetName.equals (""))
      {
	setHelp (new HelpUtil (null, defaultHelpID));
	String appPath = FabricaUtilitarios.getPathCompletoDirAplicacao ();
	getHelp ().setHelpSet (helpSetName, appPath);
	String iconPath = appPath + FabricaUtilitarios.getProperties ().getProperty ("help.icon", "");
	ImageIcon icon = new ImageIcon (iconPath);
	getHelp ().setIcon (icon);
	getHelp ().setNavigatorVisible (false);
      }
  }
  
  public void exibe ()
  {
    getHelp ().exibeAjuda ();
  }
  
  public static void main (String[] args)
  {
    new VisualizadorHelp ().exibe ();
  }
  
  public void setHelp (HelpUtil help)
  {
    this.help = help;
  }
  
  public HelpUtil getHelp ()
  {
    return help;
  }
}
