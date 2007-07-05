/* JButtonMensagemBeanInfo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Image;
import java.beans.SimpleBeanInfo;

public class JButtonMensagemBeanInfo extends SimpleBeanInfo
{
  public Image getIcon (int iconKind)
  {
    if (iconKind == 1)
      return loadImage ("/beanicon/color/16/btn_erro.png");
    if (iconKind == 2)
      return loadImage ("/beanicon/color/32/btn_erro.png");
    return super.getIcon (iconKind);
  }
}
