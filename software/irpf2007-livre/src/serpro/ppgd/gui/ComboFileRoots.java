/* ComboFileRoots - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

public class ComboFileRoots extends JComboBox
{
  private Color selectedForeGround = getForeground ();
  public static ImageIcon iconeDisquete = new ImageIcon (Thread.currentThread ().getContextClassLoader ().getResource ("imagens/disk.png"));
  private boolean bloqueiaActionListener = false;
  
  public ComboFileRoots ()
  {
    adicionaFileRoots ();
    addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	if (! bloqueiaActionListener)
	  {
	    File file = (File) ComboFileRoots.this.getSelectedItem ();
	    boolean acessivel = true;
	    try
	      {
		acessivel = file.canRead ();
	      }
	    catch (Exception ioe)
	      {
		acessivel = false;
	      }
	    if (acessivel)
	      selecionou (file);
	    else
	      selecionou (null);
	  }
      }
    });
    setRenderer (new CustomRenderer (this));
  }
  
  public void atualizaRoots ()
  {
    bloqueiaActionListener = true;
    removeAllItems ();
    adicionaFileRoots ();
    bloqueiaActionListener = false;
  }
  
  public boolean isBloqueiaActionListener ()
  {
    return bloqueiaActionListener;
  }
  
  public void setBloqueiaActionListener (boolean bloqueiaActionListener)
  {
    this.bloqueiaActionListener = bloqueiaActionListener;
  }
  
  private void adicionaFileRoots ()
  {
    boolean isWindows = System.getProperty ("os.name").toUpperCase ().indexOf ("WIND") != -1;
    boolean isLinux = System.getProperty ("os.name").toUpperCase ().indexOf ("LINUX") != -1;
    boolean isMac = System.getProperty ("os.name").toUpperCase ().indexOf ("MAC") != -1;
    File[] fileRoots = File.listRoots ();
    File fileHD = null;
    if (isWindows)
      {
	for (int i = 0; i < fileRoots.length; i++)
	  {
	    boolean isHD = fileRoots[i].toString ().toUpperCase ().indexOf ("C:") != -1;
	    if (isWindows && isHD)
	      fileHD = fileRoots[i];
	    else
	      addItem (fileRoots[i]);
	  }
	if (fileHD != null)
	  {
	    ((DefaultComboBoxModel) getModel ()).insertElementAt (fileHD, 0);
	    getModel ().setSelectedItem (fileHD);
	  }
      }
    else
      {
	addItem (new File (System.getProperty ("user.home")));
	addItem (new File ("/"));
      }
  }
  
  public void atualizaCombo ()
  {
    bloqueiaActionListener = true;
    removeAllItems ();
    adicionaFileRoots ();
    bloqueiaActionListener = false;
  }
  
  protected void selecionou (File pFile)
  {
    /* empty */
  }
  
  public Color getSelectedForeGround ()
  {
    return selectedForeGround;
  }
  
  public void setSelectedForeGround (Color selectedForeGround)
  {
    this.selectedForeGround = selectedForeGround;
  }
}
