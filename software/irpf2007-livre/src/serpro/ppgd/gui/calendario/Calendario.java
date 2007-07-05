/* Calendario - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.calendario;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.negocio.util.LogPPGD;

public class Calendario extends JPanel implements ItemListener, MouseListener, FocusListener, KeyListener, ActionListener
{
  private static final String[] MONTHS = { "Janeiro", "Fevereiro", "Mar\u00e7o", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
  private static final String[] DAYS = { "Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab" };
  private static Color WEEK_DAYS_FOREGROUND = UIManager.getColor ("TextArea.selectionForeground");
  private static Color WEEK_DAYS_BACKGROUND = Color.lightGray;
  private static Color ENDWEEK_DAYS_FOREGROUND = UIManager.getColor ("TextArea.foreground");
  private static Color ENDWEEK_DAYS_BACKGROUND = UIManager.getColor ("TextArea.background");
  private static Color DAYS_FOREGROUND = UIManager.getColor ("TextArea.foreground");
  private Color corTextoSelecao = UIManager.getColor ("TextArea.selectionForeground");
  private Color corSelecao = UIManager.getColor ("TextArea.selectionBackground");
  private Color corDiaDesabilitado = UIManager.getColor ("Menu.disabledForeground");
  private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder (1, 1, 1, 1);
  private static final Border FOCUSED_BORDER = BorderFactory.createLineBorder (Color.black, 1);
  private static final int FIRST_YEAR = 1900;
  private static final int LAST_YEAR = 2100;
  private GregorianCalendar calendar;
  private LabelDia[][] days;
  private FocusablePanel daysGrid;
  private JComboBox month;
  private JComboBox year;
  private JButton ok;
  private JButton cancel;
  private int offset;
  private int lastDay;
  private ArrayList selectedDays = new ArrayList ();
  private boolean okClicked;
  private JLabel monthAndYearTitle = new JLabel ();
  private JPanel monthYear;
  private JPanel daysPanel;
  private JPanel buttons;
  private boolean dataFixa = false;
  private boolean selecaoMultipla = false;
  private EventListenerList listenersCalendario = new EventListenerList ();
  private boolean anoFixo = false;
  private boolean selecaoHabilitada = true;
  private boolean eventosDesabilitados = false;
  
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
  
  private class LabelDia extends JLabel
  {
    private boolean habilitado = true;
    
    public LabelDia ()
    {
      /* empty */
    }
    
    public LabelDia (String desc, int align)
    {
      super (desc, align);
    }
    
    public void setHabilitado (boolean habilitado)
    {
      this.habilitado = habilitado;
    }
    
    public boolean isHabilitado ()
    {
      return habilitado;
    }
  }
  
  private void constructWithStaticLabel ()
  {
    setLayout (new BorderLayout ());
    calendar = new GregorianCalendar ();
    month = new JComboBox (MONTHS);
    month.addItemListener (this);
    year = new JComboBox ();
    for (int i = 1900; i <= 2100; i++)
      year.addItem (Integer.toString (i));
    year.addItemListener (this);
    days = new LabelDia[7][7];
    days[0][0] = new LabelDia (DAYS[0], 0);
    days[0][0].setForeground (WEEK_DAYS_FOREGROUND);
    days[0][0].setBackground (WEEK_DAYS_BACKGROUND);
    days[0][0].setOpaque (true);
    for (int i = 1; i < 6; i++)
      {
	days[0][i] = new LabelDia (DAYS[i], 0);
	days[0][i].setForeground (WEEK_DAYS_FOREGROUND);
	days[0][i].setBackground (WEEK_DAYS_BACKGROUND);
	days[0][i].setOpaque (true);
      }
    days[0][6] = new LabelDia (DAYS[6], 0);
    days[0][6].setForeground (WEEK_DAYS_FOREGROUND);
    days[0][6].setBackground (WEEK_DAYS_BACKGROUND);
    days[0][6].setOpaque (true);
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  {
	    days[i][j] = new LabelDia (" ", 0);
	    aplicaVisualLabelDias (i, j);
	    days[i][j].addMouseListener (this);
	  }
      }
    ok = new JButton ("Ok");
    ok.addActionListener (this);
    cancel = new JButton ("Cancelar");
    cancel.addActionListener (this);
    buildMonthYearStatic ();
    controiDaysPanel ();
    buttons = new JPanel ();
    buttons.add (ok);
    buttons.add (cancel);
    addComponents (monthYear, daysPanel, buttons);
    update ();
  }
  
  private void aplicaVisualLabelDias (int i, int j)
  {
    days[i][j].setForeground (DAYS_FOREGROUND);
    days[i][j].setBackground (getCorSelecao ());
    days[i][j].setBorder (EMPTY_BORDER);
  }
  
  private void buildMonthYearStatic ()
  {
    monthYear = new JPanel ();
    monthYear.setLayout (new FormLayout ("5dlu:grow,F:P,3dlu:grow,F:P:grow,3dlu", "P"));
    CellConstraints cc = new CellConstraints ();
    monthYear.add (monthAndYearTitle, cc.xywh (2, 1, 3, 1));
  }
  
  public void addCalendarioListener (CalendarioListener listener)
  {
    EventListenerList eventlistenerlist = listenersCalendario;
    Class var_class = serpro.ppgd.gui.calendario.CalendarioListener.class;
    eventlistenerlist.add (var_class, listener);
  }
  
  public void removeCalendarioListener (CalendarioListener listener)
  {
    EventListenerList eventlistenerlist = listenersCalendario;
    Class var_class = serpro.ppgd.gui.calendario.CalendarioListener.class;
    eventlistenerlist.remove (var_class, listener);
  }
  
  private void construct ()
  {
    setLayout (new BorderLayout ());
    calendar = new GregorianCalendar ();
    month = new JComboBox (MONTHS);
    month.addItemListener (this);
    year = new JComboBox ();
    for (int i = 1900; i <= 2100; i++)
      year.addItem (Integer.toString (i));
    year.addItemListener (this);
    days = new LabelDia[7][7];
    days[0][0] = new LabelDia (DAYS[0], 0);
    days[0][0].setForeground (WEEK_DAYS_FOREGROUND);
    days[0][0].setBackground (WEEK_DAYS_BACKGROUND);
    days[0][0].setOpaque (true);
    for (int i = 1; i < 6; i++)
      {
	days[0][i] = new LabelDia (DAYS[i], 0);
	days[0][i].setForeground (WEEK_DAYS_FOREGROUND);
	days[0][i].setBackground (WEEK_DAYS_BACKGROUND);
	days[0][i].setOpaque (true);
      }
    days[0][6] = new LabelDia (DAYS[6], 0);
    days[0][6].setForeground (WEEK_DAYS_FOREGROUND);
    days[0][6].setBackground (WEEK_DAYS_BACKGROUND);
    days[0][6].setOpaque (true);
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  {
	    days[i][j] = new LabelDia (" ", 0);
	    aplicaVisualLabelDias (i, j);
	    days[i][j].addMouseListener (this);
	  }
      }
    ok = new JButton ("Ok");
    ok.addActionListener (this);
    cancel = new JButton ("Cancel");
    cancel.addActionListener (this);
    buildMonthYearDynamic ();
    controiDaysPanel ();
    buttons = new JPanel ();
    buttons.add (ok);
    buttons.add (cancel);
    addComponents (monthYear, daysPanel, buttons);
    update ();
  }
  
  private void controiDaysPanel ()
  {
    JPanel daysTitles = new JPanel (new GridLayout (1, 7, 0, 0));
    daysTitles.setBorder (BorderFactory.createMatteBorder (0, 1, 1, 1, WEEK_DAYS_BACKGROUND));
    for (int i = 0; i < 7; i++)
      daysTitles.add (days[0][i]);
    daysGrid = new FocusablePanel (new GridLayout (6, 7, 0, 0));
    daysGrid.addFocusListener (this);
    daysGrid.addKeyListener (this);
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  daysGrid.add (days[i][j]);
      }
    daysGrid.setBackground (Color.white);
    daysPanel = new JPanel (new BorderLayout ());
    daysPanel.add (daysTitles, "North");
    daysPanel.add (daysGrid, "Center");
    daysPanel.setBorder (BorderFactory.createLoweredBevelBorder ());
  }
  
  private void buildMonthYearDynamic ()
  {
    monthYear = new JPanel ();
    monthYear.setLayout (new FormLayout ("F:P:grow,3dlu,F:P:grow", "P"));
    CellConstraints cc = new CellConstraints ();
    monthYear.add (month, cc.xywh (1, 1, 1, 1));
    monthYear.add (year, cc.xywh (3, 1, 1, 1));
  }
  
  private void addComponents (JPanel monthYear, JPanel daysPanel, JPanel buttons)
  {
    add (monthYear, "North");
    add (daysPanel, "Center");
  }
  
  private void readdComponents ()
  {
    removeAll ();
    addComponents (monthYear, daysPanel, buttons);
  }
  
  private int getSelectedDay ()
  {
    if (selectedDays.isEmpty ())
      return -1;
    try
      {
	return Integer.parseInt (((JLabel) selectedDays.get (0)).getText ());
      }
    catch (NumberFormatException numberformatexception)
      {
	return -1;
      }
  }
  
  private int[] getSelectedDays ()
  {
    if (selectedDays.isEmpty ())
      return null;
    try
      {
	int[] intDays = new int[selectedDays.size ()];
	Iterator it = selectedDays.iterator ();
	int i = 0;
	while (it.hasNext ())
	  {
	    intDays[i] = Integer.parseInt (((JLabel) it.next ()).getText ());
	    i++;
	  }
	return intDays;
      }
    catch (NumberFormatException e)
      {
	e.printStackTrace ();
	return null;
      }
  }
  
  private void setSelected (JLabel newDay)
  {
    newDay.setForeground (getCorTextoSelecao ());
    newDay.setOpaque (true);
    if (daysGrid.hasFocus ())
      newDay.setBorder (FOCUSED_BORDER);
  }
  
  private void setSelected (int newDay)
  {
    setSelected (getJLabelForDay (newDay));
  }
  
  private JLabel getJLabelForDay (int newDay)
  {
    return days[(newDay + offset - 1) / 7 + 1][(newDay + offset - 1) % 7];
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
    monthAndYearTitle.setText (month.getSelectedItem () + ", " + year.getSelectedItem ().toString ());
  }
  
  public void actionPerformed (ActionEvent e)
  {
    if (e.getSource () == ok)
      okClicked = true;
  }
  
  public void focusGained (FocusEvent e)
  {
    Iterator it = selectedDays.iterator ();
    while (it.hasNext ())
      setSelected ((JLabel) it.next ());
  }
  
  public void focusLost (FocusEvent e)
  {
    Iterator it = selectedDays.iterator ();
    while (it.hasNext ())
      setSelected ((JLabel) it.next ());
  }
  
  public void itemStateChanged (ItemEvent e)
  {
    deselecionaTodos ();
    update ();
    CalendarioEvent event = new CalendarioEvent (e.getSource ());
    event.setMes (month.getSelectedIndex ());
    event.setAno (year.getSelectedIndex () + 1900);
    fireMudouMesAno (event);
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
    if (isSelecaoHabilitada ())
      {
	JLabel day = (JLabel) e.getSource ();
	processaSelecao (day);
	CalendarioEvent event = new CalendarioEvent (e.getSource ());
	event.setLabelDia (day);
	if (selectedDays.contains (day))
	  fireSelecionouDia (event);
	else
	  fireDeselecionouDia (event);
      }
  }
  
  public void desabilitaDia (int aDia)
  {
    LabelDia label = (LabelDia) getJLabelForDay (aDia);
    unselectDay (label);
    selectedDays.remove (label);
    label.setHabilitado (false);
    label.setForeground (getCorDiaDesabilitado ());
  }
  
  public void desabilitaDia (int aDia, Color background, Color foreground)
  {
    LabelDia label = (LabelDia) getJLabelForDay (aDia);
    unselectDay (label);
    selectedDays.remove (label);
    label.setHabilitado (false);
    label.setOpaque (true);
    label.setBackground (background);
    label.setForeground (foreground);
  }
  
  public void habilitaDia (int aDia)
  {
    LabelDia label = (LabelDia) getJLabelForDay (aDia);
    label.setHabilitado (true);
    label.setForeground (DAYS_FOREGROUND);
    label.setBackground (getCorSelecao ());
    label.setOpaque (false);
  }
  
  private void processaSelecao (JLabel day)
  {
    LabelDia lblDia = (LabelDia) day;
    if (! day.getText ().equals (" ") && lblDia.isHabilitado ())
      {
	if (! isSelecaoMultipla ())
	  {
	    if (! selectedDays.isEmpty ())
	      {
		JLabel oldDay = (JLabel) selectedDays.get (0);
		selectedDays.remove (oldDay);
		unselectDay (oldDay);
	      }
	    selectedDays.add (day);
	    setSelected (day);
	  }
	else if (selectedDays.contains (day))
	  {
	    selectedDays.remove (day);
	    unselectDay (day);
	  }
	else
	  {
	    LogPPGD.debug ("adicionando dia:" + day);
	    selectedDays.add (day);
	    setSelected (day);
	  }
      }
    daysGrid.requestFocus ();
  }
  
  private void unselectDay (JLabel day)
  {
    day.setForeground (DAYS_FOREGROUND);
    day.setOpaque (false);
    day.setBorder (EMPTY_BORDER);
  }
  
  public void deselecionaTodos ()
  {
    Iterator it = selectedDays.iterator ();
    while (it.hasNext ())
      unselectDay ((JLabel) it.next ());
    selectedDays.clear ();
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  {
	    if (! days[i][j].isHabilitado ())
	      {
		unselectDay (days[i][j]);
		days[i][j].setHabilitado (true);
	      }
	  }
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
  
  public Calendario ()
  {
    construct ();
  }
  
  /**
   * @deprecated
   */
  public Date select (Date date)
  {
    calendar.setTime (date);
    int _day = calendar.get (5);
    int _month = calendar.get (2);
    int _year = calendar.get (1);
    setAno (_year);
    setMes (_month);
    setSelected (_day);
    okClicked = false;
    setVisible (true);
    if (! okClicked)
      return null;
    calendar.set (5, getSelectedDay ());
    calendar.set (2, month.getSelectedIndex () + 0);
    calendar.set (1, year.getSelectedIndex () + 1900);
    return calendar.getTime ();
  }
  
  public Collection getDatasSelecionadas ()
  {
    ArrayList lista = new ArrayList ();
    int[] selDays = getSelectedDays ();
    if (selDays != null)
      {
	for (int i = 0; i < selDays.length; i++)
	  {
	    calendar.set (5, selDays[i]);
	    calendar.set (2, month.getSelectedIndex () + 0);
	    calendar.set (1, year.getSelectedIndex () + 1900);
	    lista.add (calendar.getTime ());
	  }
      }
    return lista;
  }
  
  public void setDatasSelecionadas (Collection datas)
  {
    Iterator it = datas.iterator ();
    while (it.hasNext ())
      setData ((Date) it.next ());
  }
  
  public void setData (Date aDate)
  {
    eventosDesabilitados = true;
    calendar.setTime (aDate);
    int _day = calendar.get (5);
    int _month = calendar.get (2);
    int _year = calendar.get (1);
    setAno (_year);
    setMes (_month);
    processaSelecao (getJLabelForDay (_day));
    eventosDesabilitados = false;
  }
  
  public void setMes (int aMes)
  {
    eventosDesabilitados = true;
    if (aMes < 0 || aMes > 11)
      throw new IllegalArgumentException ("M\u00eas s\u00f3 vai de 0 a 11");
    month.setSelectedIndex (aMes - 0);
    eventosDesabilitados = false;
  }
  
  public int getMes ()
  {
    return month.getSelectedIndex () + 0;
  }
  
  public void setAno (int aAno)
  {
    eventosDesabilitados = true;
    year.setSelectedIndex (aAno - 1900);
    eventosDesabilitados = false;
  }
  
  public int getAno ()
  {
    return year.getSelectedIndex () + 1900;
  }
  
  public Date getData ()
  {
    return calendar.getTime ();
  }
  
  /**
   * @deprecated
   */
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
	setAno (_year);
	year.setEnabled (true);
      }
    if (pMonth != -1)
      {
	month.setSelectedIndex (pMonth);
	month.setEnabled (false);
      }
    else
      {
	setMes (_month);
	month.setEnabled (true);
      }
    setSelected (_day);
    okClicked = false;
    setVisible (true);
    if (! okClicked)
      return null;
    calendar.set (5, getSelectedDay ());
    calendar.set (2, month.getSelectedIndex () + 0);
    calendar.set (1, year.getSelectedIndex () + 1900);
    return calendar.getTime ();
  }
  
  /**
   * @deprecated
   */
  public Date select ()
  {
    return select (new Date ());
  }
  
  public JComboBox getSeletorMes ()
  {
    return month;
  }
  
  public JComboBox getSeletorAno ()
  {
    return year;
  }
  
  public JLabel getLabelMesAno ()
  {
    return monthAndYearTitle;
  }
  
  public void setDataFixa (boolean staticView)
  {
    dataFixa = staticView;
    if (isDataFixa ())
      {
	buildMonthYearStatic ();
	readdComponents ();
      }
    else
      {
	buildMonthYearDynamic ();
	readdComponents ();
      }
  }
  
  public void setAnoFixo (boolean staticView)
  {
    anoFixo = staticView;
    if (isDataFixa ())
      {
	buildMonthYearStatic ();
	readdComponents ();
      }
    else
      {
	buildMonthYearDynamic ();
	readdComponents ();
      }
  }
  
  public boolean isDataFixa ()
  {
    return dataFixa;
  }
  
  public void setSelecaoMultipla (boolean multiSelection)
  {
    selecaoMultipla = multiSelection;
    deselecionaTodos ();
  }
  
  public boolean isSelecaoMultipla ()
  {
    return selecaoMultipla;
  }
  
  public void setCorSelecao (Color corSelecao)
  {
    this.corSelecao = corSelecao;
    deselecionaTodos ();
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  aplicaVisualLabelDias (i, j);
      }
  }
  
  public Color getCorSelecao ()
  {
    return corSelecao;
  }
  
  private void fireSelecionouDia (CalendarioEvent evt)
  {
    if (! eventosDesabilitados)
      {
	Object[] listeners = listenersCalendario.getListenerList ();
	for (int i = 0; i < listeners.length; i += 2)
	  {
	    Object object = listeners[i];
	    Class var_class = serpro.ppgd.gui.calendario.CalendarioListener.class;
	    if (object == var_class)
	      ((CalendarioListener) listeners[i + 1]).marcouDia (evt);
	  }
      }
  }
  
  private void fireDeselecionouDia (CalendarioEvent evt)
  {
    if (! eventosDesabilitados)
      {
	Object[] listeners = listenersCalendario.getListenerList ();
	for (int i = 0; i < listeners.length; i += 2)
	  {
	    Object object = listeners[i];
	    Class var_class = serpro.ppgd.gui.calendario.CalendarioListener.class;
	    if (object == var_class)
	      ((CalendarioListener) listeners[i + 1]).desmarcouDia (evt);
	  }
      }
  }
  
  private void fireMudouMesAno (CalendarioEvent event)
  {
    if (! eventosDesabilitados)
      {
	Object[] listeners = listenersCalendario.getListenerList ();
	for (int i = 0; i < listeners.length; i += 2)
	  {
	    Object object = listeners[i];
	    Class var_class = serpro.ppgd.gui.calendario.CalendarioListener.class;
	    if (object == var_class)
	      ((CalendarioListener) listeners[i + 1]).mudouMesAno (event);
	  }
      }
  }
  
  public static void main (String[] args)
  {
    UIDefaults uiDefaults = UIManager.getDefaults ();
    Enumeration enum2 = uiDefaults.keys ();
    while (enum2.hasMoreElements ())
      {
	Object key = enum2.nextElement ();
	Object val = uiDefaults.get (key);
	System.out.println (key.toString ());
      }
  }
  
  public void setCorDiaDesabilitado (Color corDiaDesabilitado)
  {
    this.corDiaDesabilitado = corDiaDesabilitado;
  }
  
  public Color getCorDiaDesabilitado ()
  {
    return corDiaDesabilitado;
  }
  
  public void setCorTextoSelecao (Color corTextoSelecao)
  {
    this.corTextoSelecao = corTextoSelecao;
    deselecionaTodos ();
    for (int i = 1; i < 7; i++)
      {
	for (int j = 0; j < 7; j++)
	  aplicaVisualLabelDias (i, j);
      }
  }
  
  public Color getCorTextoSelecao ()
  {
    return corTextoSelecao;
  }
  
  public void setSelecaoHabilitada (boolean selecaoHabilitada)
  {
    this.selecaoHabilitada = selecaoHabilitada;
  }
  
  public boolean isSelecaoHabilitada ()
  {
    return selecaoHabilitada;
  }
}
