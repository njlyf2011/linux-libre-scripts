/* PPGDList - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.list;
import java.util.Vector;

import javax.swing.JList;

public class PPGDList extends JList
{
  private PPGDList ()
  {
    /* empty */
  }
  
  private PPGDList (Object[] listData)
  {
    super (listData);
  }
  
  private PPGDList (Vector listData)
  {
    super (listData);
  }
  
  public PPGDList (PPGDListModel ppgdListModel)
  {
    super ((javax.swing.ListModel) ppgdListModel);
  }
  
  public void atualiza ()
  {
    ((PPGDListModel) getModel ()).atualiza ();
  }
}
