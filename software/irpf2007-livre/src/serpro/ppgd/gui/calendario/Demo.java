/* Demo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.calendario;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;

import serpro.ppgd.negocio.util.LogPPGD;

public class Demo extends JFrame implements PropertyChangeListener
{
  private DateButton startDateButton;
  private DateButton endDateButton;
  private Date startDate;
  private Date endDate;
  
  private Demo ()
  {
    super ("DateChooser demo");
    addWindowListener (new WindowAdapter ()
    {
      public void windowClosing (WindowEvent e)
      {
	System.exit (0);
      }
    });
    startDate = new Date ();
    endDate = new Date ();
    startDateButton = new DateButton ();
    startDateButton.addPropertyChangeListener ("date", this);
    endDateButton = new DateButton ();
    endDateButton.addPropertyChangeListener ("date", this);
    getContentPane ().setLayout (new GridLayout (2, 2));
    getContentPane ().add (new JLabel ("Start date"));
    getContentPane ().add (startDateButton);
    getContentPane ().add (new JLabel ("End date"));
    getContentPane ().add (endDateButton);
    pack ();
    setResizable (false);
  }
  
  public void propertyChange (PropertyChangeEvent e)
  {
    DateButton db = (DateButton) e.getSource ();
    if (db == startDateButton)
      LogPPGD.debug ("Start date changed: ");
    else
      LogPPGD.debug ("End date changed: ");
    LogPPGD.debug (db.getDate ().toString ());
  }
  
  public static void main (String[] args)
  {
    new Demo ().show ();
  }
}
