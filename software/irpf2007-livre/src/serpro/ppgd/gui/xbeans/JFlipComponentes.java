/* JFlipComponentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import serpro.ppgd.infraestrutura.PlataformaPPGD;

public class JFlipComponentes extends JPanel
{
  private JComponent componenteA = new LabelFlip ("JFlipComponentes");
  private JComponent componenteB = new LabelFlip ("JFlipComponentes");
  private JComponent componenteC = new LabelFlip ("JFlipComponentes");
  private JComponent componenteD = new LabelFlip ("JFlipComponentes");
  
  public JFlipComponentes ()
  {
    super (new BorderLayout ());
    exibeComponenteA ();
    setPreferredSize (new Dimension (100, 100));
    if (PlataformaPPGD.isEmDesign ())
      setBorder (BorderFactory.createLineBorder (Color.gray));
  }
  
  public void exibeComponenteA ()
  {
    super.removeAll ();
    add (componenteA, "Center");
    validate ();
    repaint ();
  }
  
  public void exibeComponenteB ()
  {
    super.removeAll ();
    add (componenteB, "Center");
    validate ();
    repaint ();
  }
  
  public void exibeComponenteC ()
  {
    super.removeAll ();
    add (componenteC, "Center");
    validate ();
    repaint ();
  }
  
  public void exibeComponenteD ()
  {
    super.removeAll ();
    add (componenteD, "Center");
    validate ();
    repaint ();
  }
  
  public void setComponenteA (JComponent componentA)
  {
    componenteA = componentA;
    exibeComponenteA ();
  }
  
  public JComponent getComponenteA ()
  {
    return componenteA;
  }
  
  public void setComponenteB (JComponent componentB)
  {
    componenteB = componentB;
  }
  
  public JComponent getComponenteB ()
  {
    return componenteB;
  }
  
  public void setComponenteC (JComponent componentC)
  {
    componenteC = componentC;
  }
  
  public JComponent getComponenteC ()
  {
    return componenteC;
  }
  
  public void setComponenteD (JComponent componentD)
  {
    componenteD = componentD;
  }
  
  public JComponent getComponenteD ()
  {
    return componenteD;
  }
}
