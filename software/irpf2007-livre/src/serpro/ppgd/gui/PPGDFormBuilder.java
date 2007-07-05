/* PPGDFormBuilder - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import com.jgoodies.forms.builder.I15dPanelBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public final class PPGDFormBuilder extends I15dPanelBuilder
{
  private RowSpec lineGapSpec = FormFactory.LINE_GAP_ROWSPEC;
  private RowSpec paragraphGapSpec = FormFactory.PARAGRAPH_GAP_ROWSPEC;
  private int leadingColumnOffset = 0;
  private boolean rowGroupingEnabled = false;
  
  public PPGDFormBuilder (FormLayout layout)
  {
    this (new JPanel (), layout);
  }
  
  public PPGDFormBuilder (JPanel panel, FormLayout layout)
  {
    this (panel, layout, null);
  }
  
  public PPGDFormBuilder (FormLayout layout, ResourceBundle bundle)
  {
    this (new JPanel (), layout, bundle);
  }
  
  public PPGDFormBuilder (JPanel panel, FormLayout layout, ResourceBundle bundle)
  {
    super (layout, bundle, panel);
    setComponentFactory (PPGDComponentFactory.getInstance ());
  }
  
  public void setLineGapSize (ConstantSize lineGapSize)
  {
    RowSpec rowSpec = FormFactory.createGapRowSpec (lineGapSize);
    lineGapSpec = rowSpec/*.asUnmodifyable ()*/;
  }
  
  public RowSpec getLineGapSpec ()
  {
    return lineGapSpec;
  }
  
  public void setParagraphGapSize (ConstantSize paragraphGapSize)
  {
    RowSpec rowSpec = FormFactory.createGapRowSpec (paragraphGapSize);
    paragraphGapSpec = rowSpec/*.asUnmodifyable ()*/;
  }
  
  public int getLeadingColumnOffset ()
  {
    return leadingColumnOffset;
  }
  
  public void setLeadingColumnOffset (int columnOffset)
  {
    leadingColumnOffset = columnOffset;
  }
  
  public boolean isRowGroupingEnabled ()
  {
    return rowGroupingEnabled;
  }
  
  public void setRowGroupingEnabled (boolean enabled)
  {
    rowGroupingEnabled = enabled;
  }
  
  public void append (Component component)
  {
    append (component, 1);
  }
  
  public void append (Component component, int columnSpan, boolean lastInLine)
  {
    ensureCursorColumnInGrid ();
    ensureHasGapRow (lineGapSpec);
    ensureHasComponentLine ();
    if (lastInLine)
      {
	int colAtual = getColumn ();
	int numCols = getColumnCount ();
	columnSpan = numCols - colAtual + 1;
      }
    add (component, createLeftAdjustedConstraints (columnSpan));
    nextColumn (columnSpan + 1);
  }
  
  public void append (Component component, int columnSpan)
  {
    append (component, columnSpan, false);
  }
  
  public void append (Component c1, Component c2)
  {
    append (c1);
    append (c2);
  }
  
  public void append (Component c1, Component c2, Component c3)
  {
    append (c1);
    append (c2);
    append (c3);
  }
  
  public JLabel append (String textWithMnemonic)
  {
    JLabel label = getComponentFactory ().createLabel (textWithMnemonic);
    append (label);
    return label;
  }
  
  public JLabel append (String textWithMnemonic, Component component)
  {
    return append (textWithMnemonic, component, 1);
  }
  
  public JLabel append (String textWithMnemonic, Component c, int columnSpan)
  {
    JLabel label = append (textWithMnemonic);
    label.setLabelFor (c);
    append (c, columnSpan);
    return label;
  }
  
  public JLabel append (JLabel l, Component c, int columnSpan)
  {
    append (l);
    l.setLabelFor (c);
    append (c, columnSpan);
    return l;
  }
  
  public void appendNoMessage (EditCampo eCampo)
  {
    append (eCampo.getRotulo (), eCampo.getComponenteEditor ());
  }
  
  public void appendNoMessage (EditCampo eCampo, int columnSpan)
  {
    append (eCampo.getRotulo (), eCampo.getComponenteEditor (), columnSpan);
  }
  
  public void append (EditCampo eCampo, int columnSpan)
  {
    JPanel panel = createMessagePanel (eCampo);
    append (eCampo.getRotulo (), panel, columnSpan);
  }
  
  public void appendLabel (EditCampo eCampo, int columnSpan)
  {
    CellConstraints cc = new CellConstraints ();
    JPanel pnlRotulo = new JPanel (new FormLayout ("p:grow,center:MAX(p;8dlu)", "fill:p"));
    pnlRotulo.add (eCampo.getRotulo (), cc.xy (1, 1));
    pnlRotulo.add (eCampo.getSimbolo (), cc.xy (2, 1));
    append (pnlRotulo, columnSpan);
  }
  
  public void appendEditor (EditCampo eCampo, int columnSpan)
  {
    JPanel panel = createMessagePanel (eCampo);
    append (panel, columnSpan);
  }
  
  public void appendEditorNoMessage (EditCampo eCampo)
  {
    append (eCampo.getComponenteEditor ());
  }
  
  public void appendEditorNoMessage (EditCampo eCampo, int columnSpan)
  {
    append (eCampo.getComponenteEditor (), columnSpan);
  }
  
  public void appendSingleCell (EditCampo eCampo, int columnSpan)
  {
    JPanel panel = createAllPanel (eCampo);
    append (panel, columnSpan);
  }
  
  public void appendSingleCellToEOL (EditCampo eCampo)
  {
    JPanel panel = createAllPanel (eCampo);
    append (panel, 0, true);
  }
  
  public JLabel appendTitulo (String titulo)
  {
    return appendTitulo (titulo, 0);
  }
  
  public JLabel appendTitulo (String titulo, boolean negrito)
  {
    return appendTitulo (titulo, 0, negrito);
  }
  
  public JLabel appendTitulo (String titulo, int colSpan)
  {
    return appendTitulo (titulo, colSpan, false);
  }
  
  public JLabel appendTitulo (String titulo, int colSpan, boolean negrito)
  {
    JLabel lbTit = new JLabel (titulo);
    if (colSpan != 0)
      append (lbTit, colSpan);
    else
      append (lbTit);
    return lbTit;
  }
  
  private JPanel createMessagePanel (EditCampo eCampo)
  {
    FormLayout layout = new FormLayout ("fill:default:grow, center:8dlu", "fill:p");
    JPanel p = new JPanel (layout);
    CellConstraints cc = new CellConstraints ();
    p.add (eCampo.getComponenteEditor (), cc.xy (1, 1));
    p.add (eCampo.getButtonMensagem (), cc.xy (2, 1));
    p.setOpaque (false);
    return p;
  }
  
  public JPanel createAllPanel (EditCampo eCampo)
  {
    FormLayout layout = new FormLayout ("left:pref, 2dlu, fill:default:grow, center:8dlu", "fill:p");
    JPanel p = new JPanel (layout);
    CellConstraints cc = new CellConstraints ();
    JPanel pnlRotulo = new JPanel (new FormLayout ("p:grow,center:8dlu", "fill:p"));
    pnlRotulo.add (eCampo.getRotulo (), cc.xy (1, 1));
    pnlRotulo.add (eCampo.getSimbolo (), cc.xy (2, 1));
    p.add (pnlRotulo, cc.xy (1, 1));
    p.add (eCampo.getComponenteEditor (), cc.xy (3, 1));
    p.add (eCampo.getButtonMensagem (), cc.xy (4, 1));
    p.setOpaque (false);
    return p;
  }
  
  public void append (EditCampo eCampo)
  {
    CellConstraints cc = new CellConstraints ();
    JPanel pnlRotulo = new JPanel (new FormLayout ("p:grow,center:MAX(8dlu;p)", "fill:p"));
    pnlRotulo.add (eCampo.getRotulo (), cc.xy (1, 1));
    pnlRotulo.add (eCampo.getSimbolo (), cc.xy (2, 1));
    append (pnlRotulo, createMessagePanel (eCampo));
  }
  
  public void appendToEOLNoMessage (EditCampo eCampo)
  {
    JLabel label = append (eCampo.getInformacao ().getNomeCampo ());
    Component c = eCampo.getComponenteEditor ();
    label.setLabelFor (c);
    eCampo.setRotulo (label);
    append (c, 0, true);
  }
  
  public void appendToEOL (EditCampo eCampo)
  {
    JLabel label = append (eCampo.getInformacao ().getNomeCampo ());
    Component c = createMessagePanel (eCampo);
    label.setLabelFor (eCampo.getComponenteEditor ());
    eCampo.setRotulo (label);
    append (c, 0, true);
  }
  
  public void appendCenter (JComponent c)
  {
    append (createCenterPanel (c), 0, true);
  }
  
  public JPanel createCenterPanel (JComponent c)
  {
    FormLayout layout = new FormLayout ("p:grow, center:p, p:grow", "p");
    JPanel p = new JPanel (layout);
    CellConstraints cc = new CellConstraints ();
    p.add (c, cc.xy (2, 1));
    return p;
  }
  
  public JLabel append (String textWithMnemonic, Component c1, Component c2)
  {
    JLabel label = append (textWithMnemonic, c1);
    append (c2);
    return label;
  }
  
  public void append (String textWithMnemonic, Component c1, Component c2, int colSpan)
  {
    append (textWithMnemonic, c1);
    append (c2, colSpan);
  }
  
  public JLabel append (String textWithMnemonic, Component c1, Component c2, Component c3)
  {
    JLabel label = append (textWithMnemonic, c1, c2);
    append (c3);
    return label;
  }
  
  public JLabel append (String textWithMnemonic, Component c1, Component c2, Component c3, Component c4)
  {
    JLabel label = append (textWithMnemonic, c1, c2, c3);
    append (c4);
    return label;
  }
  
  public JLabel appendI15d (String resourceKey)
  {
    return append (getI15dString (resourceKey));
  }
  
  public JLabel appendI15d (String resourceKey, Component c, int columnSpan)
  {
    JLabel label = appendI15d (resourceKey);
    append (c, columnSpan);
    return label;
  }
  
  public JLabel appendI15d (String resourceKey, Component component)
  {
    return appendI15d (resourceKey, component, 1);
  }
  
  public JLabel appendI15d (String resourceKey, Component component, boolean nextLine)
  {
    JLabel label = appendI15d (resourceKey, component, 1);
    if (nextLine)
      nextLine ();
    return label;
  }
  
  public JLabel appendI15d (String resourceKey, Component c1, Component c2)
  {
    JLabel label = appendI15d (resourceKey, c1);
    append (c2);
    return label;
  }
  
  public JLabel appendI15d (String resourceKey, Component c1, Component c2, int colSpan)
  {
    JLabel label = appendI15d (resourceKey, c1);
    append (c2, colSpan);
    return label;
  }
  
  public JLabel appendI15d (String resourceKey, Component c1, Component c2, Component c3)
  {
    JLabel label = appendI15d (resourceKey, c1, c2);
    append (c3);
    return label;
  }
  
  public JLabel appendI15d (String resourceKey, Component c1, Component c2, Component c3, Component c4)
  {
    JLabel label = appendI15d (resourceKey, c1, c2, c3);
    append (c4);
    return label;
  }
  
  public JLabel appendTitle (String textWithMnemonic)
  {
    JLabel titleLabel = getComponentFactory ().createTitle (textWithMnemonic);
    append (titleLabel);
    return titleLabel;
  }
  
  public JLabel appendI15dTitle (String resourceKey)
  {
    return appendTitle (getI15dString (resourceKey));
  }
  
  public JComponent appendSeparator ()
  {
    return appendSeparator ("");
  }
  
  public JComponent appendSeparator (String text)
  {
    ensureCursorColumnInGrid ();
    ensureHasGapRow (paragraphGapSpec);
    ensureHasComponentLine ();
    setColumn (super.getLeadingColumn ());
    int columnSpan = getColumnCount ();
    setColumnSpan (getColumnCount ());
    JComponent titledSeparator = addSeparator (text);
    setColumnSpan (1);
    nextColumn (columnSpan);
    return titledSeparator;
  }
  
  public void appendI15dSeparator (String resourceKey)
  {
    appendSeparator (getI15dString (resourceKey));
  }
  
  protected int getLeadingColumn ()
  {
    int column = super.getLeadingColumn ();
    return column + getLeadingColumnOffset () * getColumnIncrementSign ();
  }
  
  private void ensureCursorColumnInGrid ()
  {
    if (isLeftToRight () && getColumn () > getColumnCount () || ! isLeftToRight () && getColumn () < 1)
      nextLine ();
  }
  
  private void ensureHasGapRow (RowSpec gapRowSpec)
  {
    if (getRow () != 1 && getRow () > getRowCount ())
      {
	if (getRow () <= getRowCount ())
	  {
	    RowSpec rowSpec = getCursorRowSpec ();
	    if (rowSpec == gapRowSpec)
	      return;
	  }
	appendRow (gapRowSpec);
	nextLine ();
      }
  }
  
  private void ensureHasComponentLine ()
  {
    if (getRow () > getRowCount ())
      {
	appendRow (FormFactory.PREF_ROWSPEC);
	if (isRowGroupingEnabled ())
	  getLayout ().addGroupedRow (getRow ());
      }
  }
  
  private RowSpec getCursorRowSpec ()
  {
    return getLayout ().getRowSpec (getRow ());
  }
  
  public void appendWithBorder (Border borda, EditCampo c, int colSpan)
  {
    JPanel panel = createAllPanel (c);
    panel.setBorder (borda);
    append (panel, colSpan, false);
  }
  
  public void appendButtonGroup (Border borda, EditCampo c, int colSpan)
  {
    JPanel panel = createButtonGroupPanel (c);
    panel.setBorder (borda);
    append (panel, colSpan, false);
  }
  
  public JPanel createButtonGroupPanel (EditCampo c)
  {
    EditCampo edtCampo = c;
    JPanel p = new JPanel (new BorderLayout ());
    CellConstraints cc = new CellConstraints ();
    p.add (edtCampo.getComponenteEditor (), "Center");
    p.add (edtCampo.getButtonMensagem (), "East");
    p.setOpaque (false);
    return p;
  }
  
  public void appendButtonGroupFormLayout (Border borda, EditCampo c, int colSpan)
  {
    JPanel panel = createButtonGroupPanelFormLayout (c);
    panel.setBorder (borda);
    append (panel, colSpan, false);
  }
  
  public JPanel createButtonGroupPanelFormLayout (EditCampo c)
  {
    EditLogico edtLogico = (EditLogico) c;
    JPanel panel = new JPanel (new FormLayout ("left:pref, 2dlu, center:MAX(8dlu;M)", "fill:p,fill:p"));
    Box box = new Box (edtLogico.getOrientacaoTexto ());
    CellConstraints cc = new CellConstraints ();
    Iterator it = edtLogico.getListaRadiosOrdenada ().iterator ();
    while (it.hasNext ())
      {
	JRadioButton rb = (JRadioButton) it.next ();
	box.add (rb);
	if (edtLogico.getOrientacaoTexto () == 0)
	  box.add (new JLabel (" "));
      }
    panel.add (box, cc.xy (1, 1));
    panel.add (edtLogico.getButtonMensagem (), cc.xywh (3, 1, 1, 2));
    panel.setOpaque (false);
    return panel;
  }
}
