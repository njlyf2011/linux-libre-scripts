/* JDropDownButtonBeanInfo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Image;
import java.beans.SimpleBeanInfo;

public class JDropDownButtonBeanInfo extends SimpleBeanInfo
{
  public Image getIcon (int iconKind)
  {
    if (iconKind == 1)
      return loadImage ("/beanicon/color/16/btn_dropdown.png");
    if (iconKind == 2)
      return loadImage ("/beanicon/color/32/btn_dropdown.png");
    return super.getIcon (iconKind);
  }
}
