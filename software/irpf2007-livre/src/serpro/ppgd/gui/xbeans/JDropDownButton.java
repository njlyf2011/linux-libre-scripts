/* JDropDownButton - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class JDropDownButton extends JButton implements MouseListener, MouseMotionListener
{
  private Point pontoDropDown = new Point ();
  private JPopupMenu popupMenu = new JPopupMenu ();
  private static Point posicaoMouse = new Point ();
  
  public JDropDownButton ()
  {
    addMouseListener (this);
    addMouseMotionListener (this);
  }
  
  public Component add (JPopupMenu aPopup)
  {
    popupMenu = aPopup;
    return popupMenu;
  }
  
  public Component add (Component c)
  {
    if (c instanceof JPopupMenu)
      return add ((JPopupMenu) c);
    return super.add (c);
  }
  
  public void addPopupItem (JMenuItem item)
  {
    popupMenu.add (item);
  }
  
  public Point getPontoDropDown ()
  {
    return pontoDropDown;
  }
  
  public void setPontoDropDown (Point pontoDropDown)
  {
    this.pontoDropDown = pontoDropDown;
  }
  
  public JPopupMenu getPopupMenu ()
  {
    return popupMenu;
  }
  
  public void setPopupMenu (JPopupMenu popupMenu)
  {
    this.popupMenu = popupMenu;
  }
  
  protected void fireActionPerformed (ActionEvent event)
  {
    if (popupMenu.getSubElements ().length <= 0 || ! (posicaoMouse.getX () > (double) pontoDropDown.x) || ! (posicaoMouse.getY () > (double) pontoDropDown.y))
      super.fireActionPerformed (event);
  }
  
  public void mouseClicked (MouseEvent e)
  {
    if (popupMenu.getSubElements ().length > 0 && e.getX () > pontoDropDown.x && e.getY () > pontoDropDown.y && isEnabled ())
      popupMenu.show (this, 0, (int) getBounds ().getMaxY ());
  }
  
  public void mousePressed (MouseEvent e)
  {
    /* empty */
  }
  
  public void mouseReleased (MouseEvent e)
  {
    /* empty */
  }
  
  public void mouseEntered (MouseEvent e)
  {
    /* empty */
  }
  
  public void mouseExited (MouseEvent e)
  {
    /* empty */
  }
  
  public void mouseDragged (MouseEvent e)
  {
    /* empty */
  }
  
  public void mouseMoved (MouseEvent e)
  {
    posicaoMouse.x = e.getX ();
    posicaoMouse.y = e.getY ();
  }
}
