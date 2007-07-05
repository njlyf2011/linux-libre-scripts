/* DateButton - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.calendario;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;

public class DateButton extends JButton
{
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat ("dd-MM-yyyy");
  private static final DateChooser DATE_CHOOSER = new DateChooser ((java.awt.Frame) null, "Selecione uma data");
  private Date date;
  
  protected void fireActionPerformed (ActionEvent e)
  {
    Date newDate = DATE_CHOOSER.select (date);
    if (newDate != null)
      setDate (newDate);
  }
  
  public DateButton (Date date)
  {
    super (DATE_FORMAT.format (date));
    this.date = date;
  }
  
  public DateButton ()
  {
    this (new Date ());
  }
  
  public Date getDate ()
  {
    return date;
  }
  
  public void setDate (Date date)
  {
    Date old = this.date;
    this.date = date;
    setText (DATE_FORMAT.format (date));
    firePropertyChange ("date", old, date);
  }
}
