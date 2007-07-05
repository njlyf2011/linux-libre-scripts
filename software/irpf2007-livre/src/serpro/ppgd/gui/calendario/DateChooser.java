/* DateChooser - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.calendario;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DateChooser extends JDialog implements ItemListener, MouseListener, FocusListener, KeyListener, ActionListener
{
  private static final String[] MONTHS = { "Janeiro", "Fevereiro", "Mar\u00e7o", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
  private static final String[] DAYS = { "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" };
  private static final Color WEEK_DAYS_FOREGROUND = Color.black;
  private static final Color DAYS_FOREGROUND = Color.blue;
  private static final Color SELECTED_DAY_FOREGROUND = Color.white;
  private static final Color SELECTED_DAY_BACKGROUND = Color.blue;
  private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder (1, 1, 1, 1);
  private static final Border FOCUSED_BORDER = BorderFactory.createLineBorder (Color.yellow, 1);
  private static final int FIRST_YEAR = 1900;
  private static final int LAST_YEAR = 2100;
  private GregorianCalendar calendar;
  private JLabel[][] days;
  private FocusablePanel daysGrid;
  private JComboBox month;
  private JComboBox year;
  private JButton ok;
  private JButton cancel;
  private int offset;
  private int lastDay;
  private JLabel day;
  private boolean okClicked;
  private JLabel monthAndYear;
  
  private static class FocusablePanel extends JPanel
  {
    public FocusablePanel (LayoutManager layout)
    {
      super (layout);
    }
    
    public boolean isFocusTraversable ()
    {
      return true;
    }
  }
  
  private void constructWithStaticLabel ()
  {
    calendar = new GregorianCalendar ();
    month = new JComboBox (MONTHS);
    month.addItemListener (this);
    year = new JComboBox ();
    for (int i = 1900; i <= 2100; i++)
      year.addItem (Integer.toString (i));
    year.addItemListener (this);
    days = new JLabel[7][7];
    for (int i = 0; i < 7; i++)
      {
	days[0][i] = new JLabel (DAYS[i], 4);
	days[0][i].setForeground (WEEK_DAYS_FOREGROUND);
      }
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  {
	    days[i][j] = new JLabel (" ", 4);
	    days[i][j].setForeground (DAYS_FOREGROUND);
	    days[i][j].setBackground (SELECTED_DAY_BACKGROUND);
	    days[i][j].setBorder (EMPTY_BORDER);
	    days[i][j].addMouseListener (this);
	  }
      }
    ok = new JButton ("Ok");
    ok.addActionListener (this);
    cancel = new JButton ("Cancelar");
    cancel.addActionListener (this);
    JPanel monthYear = new JPanel ();
    monthYear.setLayout (new FormLayout ("5dlu:grow,F:P,3dlu:grow,F:P:grow,3dlu", "P"));
    CellConstraints cc = new CellConstraints ();
    monthYear.add (monthAndYear, cc.xywh (2, 1, 3, 1));
    daysGrid = new FocusablePanel (new GridLayout (7, 7, 5, 0));
    daysGrid.addFocusListener (this);
    daysGrid.addKeyListener (this);
    for (int i = 0; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  daysGrid.add (days[i][j]);
      }
    daysGrid.setBackground (Color.white);
    daysGrid.setBorder (BorderFactory.createLoweredBevelBorder ());
    JPanel daysPanel = new JPanel ();
    daysPanel.add (daysGrid);
    JPanel buttons = new JPanel ();
    buttons.add (ok);
    buttons.add (cancel);
    Container dialog = getContentPane ();
    dialog.add ("North", monthYear);
    dialog.add ("Center", daysPanel);
    dialog.add ("South", buttons);
    pack ();
    setResizable (false);
  }
  
  private void construct ()
  {
    calendar = new GregorianCalendar ();
    month = new JComboBox (MONTHS);
    month.addItemListener (this);
    year = new JComboBox ();
    for (int i = 1900; i <= 2100; i++)
      year.addItem (Integer.toString (i));
    year.addItemListener (this);
    days = new JLabel[7][7];
    for (int i = 0; i < 7; i++)
      {
	days[0][i] = new JLabel (DAYS[i], 4);
	days[0][i].setForeground (WEEK_DAYS_FOREGROUND);
      }
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  {
	    days[i][j] = new JLabel (" ", 4);
	    days[i][j].setForeground (DAYS_FOREGROUND);
	    days[i][j].setBackground (SELECTED_DAY_BACKGROUND);
	    days[i][j].setBorder (EMPTY_BORDER);
	    days[i][j].addMouseListener (this);
	  }
      }
    ok = new JButton ("Ok");
    ok.addActionListener (this);
    cancel = new JButton ("Cancel");
    cancel.addActionListener (this);
    JPanel monthYear = new JPanel ();
    monthYear.setLayout (new FormLayout ("5dlu,F:P:grow,3dlu,F:P:grow,3dlu", "P"));
    CellConstraints cc = new CellConstraints ();
    monthYear.add (month, cc.xywh (2, 1, 1, 1));
    monthYear.add (year, cc.xywh (4, 1, 1, 1));
    daysGrid = new FocusablePanel (new GridLayout (7, 7, 5, 0));
    daysGrid.addFocusListener (this);
    daysGrid.addKeyListener (this);
    for (int i = 0; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  daysGrid.add (days[i][j]);
      }
    daysGrid.setBackground (Color.white);
    daysGrid.setBorder (BorderFactory.createLoweredBevelBorder ());
    JPanel daysPanel = new JPanel ();
    daysPanel.add (daysGrid);
    JPanel buttons = new JPanel ();
    buttons.add (ok);
    buttons.add (cancel);
    Container dialog = getContentPane ();
    dialog.add ("North", monthYear);
    dialog.add ("Center", daysPanel);
    dialog.add ("South", buttons);
    pack ();
    setResizable (false);
  }
  
  private int getSelectedDay ()
  {
    if (day == null)
      return -1;
    try
      {
	return Integer.parseInt (day.getText ());
      }
    catch (NumberFormatException numberformatexception)
      {
	return -1;
      }
  }
  
  private void setSelected (JLabel newDay)
  {
    if (day != null)
      {
	day.setForeground (DAYS_FOREGROUND);
	day.setOpaque (false);
	day.setBorder (EMPTY_BORDER);
      }
    day = newDay;
    day.setForeground (SELECTED_DAY_FOREGROUND);
    day.setOpaque (true);
    if (daysGrid.hasFocus ())
      day.setBorder (FOCUSED_BORDER);
  }
  
  private void setSelected (int newDay)
  {
    setSelected (days[(newDay + offset - 1) / 7 + 1][(newDay + offset - 1) % 7]);
  }
  
  private void update ()
  {
    int iday = getSelectedDay ();
    for (int i = 0; i < 7; i++)
      {
	days[1][i].setText (" ");
	days[5][i].setText (" ");
	days[6][i].setText (" ");
      }
    calendar.set (5, 1);
    calendar.set (2, month.getSelectedIndex () + 0);
    calendar.set (1, year.getSelectedIndex () + 1900);
    offset = calendar.get (7) - 1;
    lastDay = calendar.getActualMaximum (5);
    for (int i = 0; i < lastDay; i++)
      days[(i + offset) / 7 + 1][(i + offset) % 7].setText (String.valueOf (i + 1));
    if (iday != -1)
      {
	if (iday > lastDay)
	  iday = lastDay;
	setSelected (iday);
      }
  }
  
  public void actionPerformed (ActionEvent e)
  {
    if (e.getSource () == ok)
      okClicked = true;
    hide ();
  }
  
  public void focusGained (FocusEvent e)
  {
    setSelected (day);
  }
  
  public void focusLost (FocusEvent e)
  {
    setSelected (day);
  }
  
  public void itemStateChanged (ItemEvent e)
  {
    update ();
    monthAndYear.setText (month.getSelectedItem () + ", " + year.getSelectedItem ().toString ());
  }
  
  public void keyPressed (KeyEvent e)
  {
    int iday = getSelectedDay ();
    switch (e.getKeyCode ())
      {
      case 37:
	if (iday > 1)
	  setSelected (iday - 1);
	break;
      case 39:
	if (iday < lastDay)
	  setSelected (iday + 1);
	break;
      case 38:
	if (iday > 7)
	  setSelected (iday - 7);
	break;
      case 40:
	if (iday <= lastDay - 7)
	  setSelected (iday + 7);
	break;
      }
  }
  
  public void mouseClicked (MouseEvent e)
  {
    JLabel day = (JLabel) e.getSource ();
    if (! day.getText ().equals (" "))
      setSelected (day);
    daysGrid.requestFocus ();
    if (e.getClickCount () == 2)
      {
	okClicked = true;
	hide ();
      }
  }
  
  public void keyReleased (KeyEvent e)
  {
    /* empty */
  }
  
  public void keyTyped (KeyEvent e)
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
  
  public void mousePressed (MouseEvent e)
  {
    /* empty */
  }
  
  public void mouseReleased (MouseEvent e)
  {
    /* empty */
  }
  
  public DateChooser (Dialog owner, String title)
  {
    super (owner, title, true);
    monthAndYear = new JLabel ();
    construct ();
  }
  
  public DateChooser (JFrame owner, String title, boolean withLabel)
  {
    super ((Frame) owner, title, true);
    monthAndYear = new JLabel ();
    constructWithStaticLabel ();
  }
  
  public DateChooser (Dialog owner)
  {
    super (owner, true);
    monthAndYear = new JLabel ();
    construct ();
  }
  
  public DateChooser (Frame owner, String title)
  {
    super (owner, title, true);
    monthAndYear = new JLabel ();
    construct ();
  }
  
  public DateChooser (Frame owner)
  {
    super (owner, true);
    monthAndYear = new JLabel ();
    construct ();
  }
  
  public Date select (Date date)
  {
    calendar.setTime (date);
    int _day = calendar.get (5);
    int _month = calendar.get (2);
    int _year = calendar.get (1);
    year.setSelectedIndex (_year - 1900);
    month.setSelectedIndex (_month - 0);
    setSelected (_day);
    okClicked = false;
    setLocationRelativeTo (null);
    setVisible (true);
    if (! okClicked)
      return null;
    calendar.set (5, getSelectedDay ());
    calendar.set (2, month.getSelectedIndex () + 0);
    calendar.set (1, year.getSelectedIndex () + 1900);
    return calendar.getTime ();
  }
  
  public Date select (Date date, int pMonth, String pYear)
  {
    if (date != null)
      calendar.setTime (date);
    else
      {
	calendar.set (5, 1);
	calendar.set (2, pMonth != -1 ? pMonth - 1 : 0);
	calendar.set (1, Integer.parseInt (pYear));
      }
    int _day = calendar.get (5);
    int _month = calendar.get (2);
    int _year = calendar.get (1);
    if (pYear != null && pYear.trim ().length () > 0)
      {
	year.setSelectedItem (pYear);
	year.setEnabled (false);
      }
    else
      {
	year.setSelectedIndex (_year - 1900);
	year.setEnabled (true);
      }
    if (pMonth != -1)
      {
	month.setSelectedIndex (pMonth);
	month.setEnabled (false);
      }
    else
      {
	month.setSelectedIndex (_month - 0);
	month.setEnabled (true);
      }
    setSelected (_day);
    okClicked = false;
    setLocationRelativeTo (null);
    setVisible (true);
    if (! okClicked)
      return null;
    calendar.set (5, getSelectedDay ());
    calendar.set (2, month.getSelectedIndex () + 0);
    calendar.set (1, year.getSelectedIndex () + 1900);
    return calendar.getTime ();
  }
  
  public Date select ()
  {
    return select (new Date ());
  }
  
  public JComboBox getMonth ()
  {
    return month;
  }
  
  public JComboBox getYear ()
  {
    return year;
  }
  
  public JLabel getMonthAndYear ()
  {
    return monthAndYear;
  }
}
