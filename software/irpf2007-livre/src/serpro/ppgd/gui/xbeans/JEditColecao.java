/* JEditColecao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.gui.EditMascara;
import serpro.ppgd.gui.table.JTableEx;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.PPGDFormPanel;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.util.UtilitariosArquivo;
import serpro.ppgd.negocio.util.UtilitariosString;

public class JEditColecao extends JEditCampo implements ActionListener
{
  protected JFormattedTextField txtCodigo;
  protected JLabel lblTexto;
  protected JButton btnPopup;
  private EditMascara edtMsk;
  protected int colunaExibidaLabel;
  protected int colunaExibidaCodigo;
  protected JDialog dlg;
  protected JTable tabelaPopup;
  protected JScrollPane scroll;
  protected String tituloDialogo = "";
  protected int delayMaxFiltroTeclado = 1000;
  protected int maxCaracteresNaColunaDaListaPopup = 40;
  protected String definicaoFormsColunas;
  protected String definicaoFormsLinha;
  private boolean debugging = false;
  private double pesoTxtCodigo;
  private double pesoLabel;
  private boolean quebraAutomaticaTexto = false;
  private boolean podeDigitarValoresForaDoDominio = false;
  
  class ListPopupModel extends AbstractTableModel
  {
    public int getRowCount ()
    {
      Codigo info = (Codigo) JEditColecao.this.getInformacao ();
      return info.getColecaoElementoTabela ().size ();
    }
    
    public Object getValueAt (int row, int column)
    {
      ElementoTabela elemento = ((Codigo) JEditColecao.this.getInformacao ()).getElementoTabela (row);
      return elemento.getConteudo (column);
    }
    
    public boolean isCellEditable (int row, int column)
    {
      return false;
    }
    
    public int getColumnCount ()
    {
      if (((Codigo) JEditColecao.this.getInformacao ()).getColecaoElementoTabela ().size () == 0)
	return 0;
      ElementoTabela elemento = ((Codigo) JEditColecao.this.getInformacao ()).getElementoTabela (0);
      return elemento.size ();
    }
  }
  
  class ListPopupRenderer extends DefaultTableCellRenderer
  {
    private JTable tbl;
    
    public ListPopupRenderer (JTable pTbl)
    {
      tbl = pTbl;
    }
    
    public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
      boolean ehPar = row % 2 == 0;
      String val = (String) value;
      super.getTableCellRendererComponent (table, val, isSelected, hasFocus, row, column);
      if (! isSelected)
	{
	  if (! ehPar)
	    {
	      Color c = new Color (tbl.getBackground ().getRed () - 35, tbl.getBackground ().getGreen () - 35, tbl.getBackground ().getBlue () - 35);
	      setBackground (c);
	    }
	  else
	    setBackground (tbl.getBackground ());
	  return this;
	}
      return super.getTableCellRendererComponent (table, val, isSelected, hasFocus, row, column);
    }
  }
  
  class KeyListenerPopup extends KeyAdapter
  {
    long ultimaVez = 0L;
    String filtro = "";
    int colunaFiltro;
    
    public KeyListenerPopup (int aColunaFiltro)
    {
      colunaFiltro = aColunaFiltro;
    }
    
    public void keyPressed (KeyEvent e)
    {
      if (Character.isDigit (e.getKeyChar ()) || Character.isLetter (e.getKeyChar ()))
	{
	  int selecao = selectionForKey (e.getKeyChar ());
	  tabelaPopup.getSelectionModel ().setSelectionInterval (selecao, selecao);
	  if (selecao != -1)
	    {
	      if (selecao == 0)
		scroll.getVerticalScrollBar ().setValue (scroll.getVerticalScrollBar ().getMinimum ());
	      else if (selecao == tabelaPopup.getRowCount () - 1)
		scroll.getVerticalScrollBar ().setValue (scroll.getVerticalScrollBar ().getMaximum ());
	      else
		{
		  int max = scroll.getVerticalScrollBar ().getMaximum ();
		  int pos = max / (tabelaPopup.getRowCount () - 1) * selecao;
		  scroll.getVerticalScrollBar ().setValue (pos);
		}
	    }
	}
      else if (e.getKeyCode () == 10 && tabelaPopup.getSelectedRow () != -1)
	{
	  dlg.setVisible (false);
	  String cod = (String) tabelaPopup.getModel ().getValueAt (tabelaPopup.getSelectedRow (), 0);
	  ((Codigo) JEditColecao.this.getInformacao ()).setConteudo (cod);
	}
    }
    
    public int selectionForKey (char aKey)
    {
      int itemSelecionado = -1;
      if (colunaFiltro >= 0)
	{
	  long atual = System.currentTimeMillis ();
	  long diferenca = atual - ultimaVez;
	  if (diferenca < (long) getDelayMaxFiltroTeclado ())
	    {
	      KeyListenerPopup keylistenerpopup = this;
	      String string = keylistenerpopup.filtro;
	      StringBuffer stringbuffer = new StringBuffer (string);
	      keylistenerpopup.filtro = stringbuffer.append (aKey).toString ();
	      itemSelecionado = tentaSelecionar ();
	    }
	  else
	    {
	      filtro = "";
	      KeyListenerPopup keylistenerpopup = this;
	      String string = keylistenerpopup.filtro;
	      StringBuffer stringbuffer = new StringBuffer (string);
	      keylistenerpopup.filtro = stringbuffer.append (aKey).toString ();
	      itemSelecionado = tentaSelecionar ();
	    }
	  ultimaVez = atual;
	  if (filtro.length () > 100)
	    filtro = "";
	}
      return itemSelecionado;
    }
    
    private synchronized int tentaSelecionar ()
    {
      int total = ((Codigo) JEditColecao.this.getInformacao ()).getColecaoElementoTabela ().size ();
      int itemSelecionado = -1;
      String upperFiltro = filtro.toUpperCase ();
      for (int i = 0; i < total; i++)
	{
	  ElementoTabela elem = (ElementoTabela) ((Codigo) JEditColecao.this.getInformacao ()).getColecaoElementoTabela ().get (i);
	  String item = UtilitariosString.removeAcentos (elem.getConteudo (colunaFiltro));
	  if (item.toUpperCase ().startsWith (upperFiltro))
	    {
	      itemSelecionado = i;
	      break;
	    }
	}
      return itemSelecionado;
    }
  }
  
  public JEditColecao ()
  {
    super (new Codigo (null, "Codigo", new Vector ()));
    setMascaraTxtCodigo ("**");
  }
  
  protected void instanciaComponentes ()
  {
    Icon icon = new ImageIcon (UtilitariosArquivo.localizaArquivoEmClasspath ("/icones/down_narrow.png"));
    btnPopup = new JButton (icon);
    btnPopup.setFocusable (false);
    btnPopup.setPreferredSize (new Dimension (26, btnPopup.getPreferredSize ().height + 3));
    btnPopup.addActionListener (this);
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (txtCodigo == null)
      {
	edtMsk = new EditMascara ();
	edtMsk.removeTransfereFocoEnter ();
	edtMsk.getComponenteFoco ().addFocusListener (new FocusListener ()
	{
	  public void focusGained (FocusEvent e)
	  {
	    SwingUtilities.invokeLater (new Runnable ()
	    {
	      public void run ()
	      {
		((JTextField) edtMsk.getComponenteFoco ()).selectAll ();
	      }
	    });
	  }
	  
	  public void focusLost (FocusEvent e)
	  {
	    if (e.getOppositeComponent () != null)
	      {
		JEditColecao.this.setIdentificacaoFoco (false);
		if (JEditColecao.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || JEditColecao.this.verificaValidacoesImpeditivas (((JTextField) edtMsk.getComponenteFoco ()).getText ()))
		  {
		    setarCampo ();
		    implementacaoPropertyChange (null);
		    JEditColecao.this.chamaValidacao ();
		  }
	      }
	  }
	});
	edtMsk.getComponenteFoco ().addKeyListener (new KeyListener ()
	{
	  public void keyPressed (KeyEvent e)
	  {
	    if (e.getKeyCode () == 40)
	      btnPopup.doClick ();
	  }
	  
	  public void keyReleased (KeyEvent e)
	  {
	    /* empty */
	  }
	  
	  public void keyTyped (KeyEvent e)
	  {
	    /* empty */
	  }
	});
	edtMsk.setSobrescreve (true);
	edtMsk.setMascara ("*************");
	edtMsk.setTamanho (10);
	edtMsk.getInformacao ().setObservadoresAtivos (false);
	definicaoFormsColunas = "1px,MIN(12dlu;P):grow(.1),2dlu,P,2dlu,MIN(1px;P):grow(.9)";
	definicaoFormsLinha = "1px,P,1px,MIN(P;10dlu):grow,1px";
	pesoTxtCodigo = 0.1;
	pesoLabel = 0.9;
	txtCodigo = (JFormattedTextField) edtMsk.getComponenteFoco ();
	txtCodigo.setEditable (false);
	txtCodigo.setBorder (BorderFactory.createLoweredBevelBorder ());
	lblTexto = new JLabel (" ");
	lblTexto.setBorder (BorderFactory.createLoweredBevelBorder ());
	lblTexto.setVerticalTextPosition (1);
	lblTexto.setHorizontalTextPosition (2);
	colunaExibidaLabel = 1;
	colunaExibidaCodigo = 0;
      }
    add (montaPainelCombo (), "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  public JButton getBtnPopup ()
  {
    return btnPopup;
  }
  
  public boolean isTxtCodigoEditavel ()
  {
    return txtCodigo.isEditable ();
  }
  
  public void setTxtCodigoEditavel (boolean opt)
  {
    txtCodigo.setEditable (opt);
  }
  
  public boolean isTxtCodigoHabilitado ()
  {
    return txtCodigo.isEnabled ();
  }
  
  public void setTxtCodigoHabilitado (boolean opt)
  {
    txtCodigo.setEnabled (opt);
  }
  
  protected JPanel montaPainelCombo ()
  {
    PPGDFormPanel retorno = new PPGDFormPanel ();
    if (isDebugging ())
      retorno.setDebugColor (true);
    retorno.setLayout (new FormLayout (definicaoFormsColunas, definicaoFormsLinha));
    retorno.getBuilder ().setRow (2);
    retorno.getBuilder ().setColumn (2);
    retorno.getBuilder ().append (txtCodigo);
    retorno.getBuilder ().setColumn (4);
    retorno.getBuilder ().append (btnPopup);
    CellConstraints cc = new CellConstraints ();
    retorno.add (lblTexto, cc.xywh (6, 2, 1, 3));
    return retorno;
  }
  
  public int getColunaExibidaLabel ()
  {
    return colunaExibidaLabel;
  }
  
  public void setColunaExibidaLabel (int pColunaExibidaLabel)
  {
    colunaExibidaLabel = pColunaExibidaLabel;
  }
  
  public int getColunaExibidaCodigo ()
  {
    return colunaExibidaCodigo;
  }
  
  public void setColunaExibidaCodigo (int colunaExibidaCodigo)
  {
    this.colunaExibidaCodigo = colunaExibidaCodigo;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    SwingUtilities.invokeLater (new Runnable ()
    {
      public void run ()
      {
	if (dlg == null)
	  JEditColecao.this.instanciaPopUp ();
	JEditColecao.this.getInformacao ().setConteudo (((JTextField) edtMsk.getComponenteFoco ()).getText ());
	JEditColecao.this.exibePopUp ();
      }
    });
  }
  
  private void instanciaPopUp ()
  {
    dlg = new JDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), false);
    dlg.setDefaultCloseOperation (2);
    tabelaPopup = getTabelaPopup ();
    tabelaPopup.setModel (new ListPopupModel ());
    tabelaPopup.setTableHeader (null);
    tabelaPopup.setSelectionMode (0);
    tabelaPopup.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	if (e.getClickCount () == 2)
	  {
	    dlg.setVisible (false);
	    int indiceSelecionado = tabelaPopup.getSelectedRow ();
	    if (indiceSelecionado != -1)
	      {
		String cod = (String) tabelaPopup.getModel ().getValueAt (indiceSelecionado, 0);
		((Codigo) JEditColecao.this.getInformacao ()).setConteudo (cod);
	      }
	  }
      }
    });
    setListRenderer (new ListPopupRenderer (tabelaPopup));
    PPGDFormPanel pnl = new PPGDFormPanel ();
    pnl.setLayout (new FormLayout ("P:grow", "P"));
    pnl.getBuilder ().append (tabelaPopup);
    scroll = new JScrollPane (pnl);
    scroll.setAutoscrolls (true);
    scroll.setHorizontalScrollBarPolicy (32);
    scroll.setVerticalScrollBarPolicy (20);
    tabelaPopup.addKeyListener (new KeyListenerPopup (((Codigo) getInformacao ()).getColunaFiltro ()));
    tabelaPopup.addKeyListener (new KeyAdapter ()
    {
      public void keyPressed (KeyEvent e)
      {
	if (e.getKeyChar () == '\033')
	  dlg.setVisible (false);
      }
    });
    dlg.getContentPane ().add (scroll);
    dlg.addWindowFocusListener (new WindowFocusListener ()
    {
      public void windowGainedFocus (WindowEvent e)
      {
	/* empty */
      }
      
      public void windowLostFocus (WindowEvent e)
      {
	if (e.getOppositeWindow () != null && ! e.getOppositeWindow ().equals (dlg))
	  dlg.dispose ();
      }
    });
    dlg.setUndecorated (true);
    ((JPanel) dlg.getContentPane ()).setBorder (BorderFactory.createRaisedBevelBorder ());
    getTabelaPopup ().setAutoResizeMode (0);
    ajustaLarguraColunas ();
  }
  
  private void exibePopUp ()
  {
    int linhaPreSelecionada = ((Codigo) getInformacao ()).getIndiceElementoTabela ();
    if (! getInformacao ().isVazio ())
      tabelaPopup.getSelectionModel ().setSelectionInterval (linhaPreSelecionada, linhaPreSelecionada);
    else
      {
	tabelaPopup.getSelectionModel ().clearSelection ();
	tabelaPopup.getSelectionModel ().setSelectionInterval (0, 0);
      }
    if (tabelaPopup.getPreferredSize ().height < 160)
      dlg.setSize (new Dimension (350, tabelaPopup.getPreferredSize ().height + 5));
    else
      dlg.setSize (new Dimension (350, 160));
    dlg.setTitle (tituloDialogo);
    dlg.setLocationRelativeTo (null);
    Point ponto = btnPopup.getLocationOnScreen ();
    ponto.y = ponto.y + btnPopup.getSize ().height;
    dlg.setLocation (ponto);
    dlg.setVisible (true);
    try
      {
	ajustaScroll ();
      }
    catch (Exception excp)
      {
	excp.printStackTrace ();
      }
    scroll.getViewport ().setViewPosition (new Point (0, 0));
  }
  
  private void ajustaScroll ()
  {
    int totalLinhas = ((Codigo) getInformacao ()).getColecaoElementoTabela ().size ();
    int indiceSelecionado = ((Codigo) getInformacao ()).getIndiceElementoTabela ();
    if (indiceSelecionado == -1)
      scroll.getVerticalScrollBar ().setValue (scroll.getVerticalScrollBar ().getMinimum ());
    else
      {
	int valor = 0;
	int maximoScroll = scroll.getVerticalScrollBar ().getMaximum ();
	int larguraLinha = tabelaPopup.getRowHeight ();
	scroll.getVerticalScrollBar ().setBlockIncrement (maximoScroll / larguraLinha * 2);
	scroll.getVerticalScrollBar ().setUnitIncrement (maximoScroll / larguraLinha);
	valor = maximoScroll / totalLinhas * indiceSelecionado;
	scroll.getVerticalScrollBar ().setValue (valor);
      }
  }
  
  private void ajustaLarguraColunas ()
  {
    List elementosTabela = ((Codigo) getInformacao ()).getColecaoElementoTabela ();
    for (int i = elementosTabela.size () - 1; i >= 0; i--)
      calculaLarguraColuna ((ElementoTabela) elementosTabela.get (i));
  }
  
  private void calculaLarguraColuna (ElementoTabela elem)
  {
    JLabel lbl = new JLabel ();
    for (int i = 0; i < elem.size (); i++)
      {
	lbl.setText (elem.getConteudo (i));
	if (lbl.getPreferredSize ().width > getLarguraColunaTabela (i))
	  setLarguraColunaTabela (i, lbl.getPreferredSize ().width);
      }
  }
  
  public void setTxtCodigoVisivel (boolean pOpt)
  {
    txtCodigo.setVisible (pOpt);
    if (! txtCodigo.isVisible ())
      definicaoFormsColunas = "1px,P,P,P,2dlu,MIN(1px;P):grow";
    else
      definicaoFormsColunas = "1px,MIN(12dlu;P):grow(" + String.valueOf (pesoTxtCodigo) + "),2dlu,P,2dlu,MIN(1px;P):grow(" + String.valueOf (pesoLabel) + ")";
    buildComponente ();
  }
  
  public void setLblTextoVisivel (boolean pOpt)
  {
    lblTexto.setVisible (pOpt);
  }
  
  public boolean isLblTextoVisivel ()
  {
    return lblTexto.isVisible ();
  }
  
  public boolean getTxtCodigoVisivel ()
  {
    return txtCodigo.isVisible ();
  }
  
  public void setLarguraColunaTabela (int col, int largura)
  {
    getTabelaPopup ().getColumnModel ().getColumn (col).setWidth (largura);
    getTabelaPopup ().getColumnModel ().getColumn (col).setPreferredWidth (largura);
  }
  
  public int getLarguraColunaTabela (int col)
  {
    return getTabelaPopup ().getColumnModel ().getColumn (col).getWidth ();
  }
  
  public String getTituloDialogo ()
  {
    return tituloDialogo;
  }
  
  public void setTituloDialogo (String tituloDialogo)
  {
    this.tituloDialogo = tituloDialogo;
  }
  
  public JLabel getLblTexto ()
  {
    return lblTexto;
  }
  
  protected void informacaoModificada ()
  {
    instanciaPopUp ();
    implementacaoPropertyChange (null);
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    txtCodigo.setEditable (! readOnly);
    btnPopup.setEnabled (! readOnly);
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    txtCodigo.setEnabled (habilitado);
    if (habilitado)
      txtCodigo.setBackground (Color.WHITE);
    else
      txtCodigo.setBackground (Color.getColor ("TextField.disabledBackground"));
    btnPopup.setEnabled (habilitado);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (evt != null && evt.getPropertyName () != null && evt.getPropertyName ().equals ("ComboReiniciado"))
      setInformacao (getInformacao ());
    if (getInformacao ().isVazio ())
      {
	setaConteudoTxtCodigo (" ");
	lblTexto.setText (" ");
      }
    else
      {
	ElementoTabela elem = ((Codigo) getInformacao ()).getElementoTabela ();
	if (elem == null)
	  lblTexto.setText (" ");
	else
	  {
	    setaConteudoTxtCodigo (elem.getConteudo (colunaExibidaCodigo));
	    if (quebraAutomaticaTexto)
	      lblTexto.setText ("<HTML>" + elem.getConteudo (colunaExibidaLabel) + "</HTML>");
	    else
	      lblTexto.setText (elem.getConteudo (colunaExibidaLabel));
	  }
      }
  }
  
  private void setaConteudoTxtCodigo (String texto)
  {
    String valorSemMascara = UtilitariosString.retiraMascara (campo.getConteudoFormatado ());
    String valorComMascara = UtilitariosString.retornaComMascara (valorSemMascara, edtMsk.getFormatador ().getMask ());
    if (valorSemMascara.trim ().equals (""))
      {
	txtCodigo.setText (valorComMascara);
	txtCodigo.setValue (null);
      }
    else
      {
	txtCodigo.setText (valorComMascara);
	txtCodigo.setValue (valorComMascara);
      }
    txtCodigo.setCaretPosition (0);
  }
  
  public JComponent getComponenteEditor ()
  {
    return txtCodigo;
  }
  
  public JComponent getComponenteFoco ()
  {
    return txtCodigo;
  }
  
  public void setEstiloFonte (int estilo)
  {
    Font f = txtCodigo.getFont ();
    f = f.deriveFont (estilo);
    txtCodigo.setFont (f);
    f = lblTexto.getFont ();
    f = f.deriveFont (estilo);
    lblTexto.setFont (f);
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    Font f = txtCodigo.getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    txtCodigo.setFont (f);
    f = lblTexto.getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    lblTexto.setFont (f);
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
  
  public Color getBackGroundListaPopup ()
  {
    return getTabelaPopup ().getBackground ();
  }
  
  public void setBackGroundListaPopup (Color cl)
  {
    getTabelaPopup ().setBackground (cl);
  }
  
  public void setListRenderer (TableCellRenderer renderer)
  {
    JTable jtable = tabelaPopup;
    Class var_class = java.lang.Object.class;
    jtable.setDefaultRenderer (var_class, renderer);
  }
  
  public JTable getTabelaPopup ()
  {
    if (tabelaPopup == null)
      tabelaPopup = new JTableEx ()
      {
	public boolean getScrollableTracksViewportHeight ()
	{
	  return true;
	}
      };
    return tabelaPopup;
  }
  
  public int getDelayMaxFiltroTeclado ()
  {
    return delayMaxFiltroTeclado;
  }
  
  public void setDelayMaxFiltroTeclado (int delayMaxFiltroTeclado)
  {
    this.delayMaxFiltroTeclado = delayMaxFiltroTeclado;
  }
  
  public int getMaxCaracteresNaColunaDaListaPopup ()
  {
    return maxCaracteresNaColunaDaListaPopup;
  }
  
  public void setMaxCaracteresNaColunaDaListaPopup (int pMaxCaracteresNaColunaDaListaPopup)
  {
    maxCaracteresNaColunaDaListaPopup = pMaxCaracteresNaColunaDaListaPopup;
  }
  
  public boolean isDebugging ()
  {
    return debugging;
  }
  
  public void setDebugging (boolean debugging)
  {
    this.debugging = debugging;
    buildComponente ();
  }
  
  public String getMascaraTxtCodigo ()
  {
    return edtMsk.getMascara ();
  }
  
  public void setMascaraTxtCodigo (String pMascaraTxtCodigo)
  {
    String textoAntigo = ((JTextField) edtMsk.getComponenteFoco ()).getText ();
    edtMsk.setMascara (pMascaraTxtCodigo);
    edtMsk.setTamanho (pMascaraTxtCodigo.length ());
    setaConteudoTxtCodigo (textoAntigo);
    buildComponente ();
  }
  
  public String getCaracteresInvalidosTxtCodigo ()
  {
    return edtMsk.getFormatador ().getInvalidCharacters ();
  }
  
  public void setCaracteresInvalidosTxtCodigo (String pCaracteresInvalidosTxtCodigo)
  {
    edtMsk.setCaracteresInvalidos (pCaracteresInvalidosTxtCodigo);
  }
  
  public String getCaracteresValidosTxtCodigo ()
  {
    return edtMsk.getFormatador ().getValidCharacters ();
  }
  
  public void setCaracteresValidosTxtCodigo (String pCaracteresValidosTxtCodigo)
  {
    edtMsk.setCaracteresValidos (pCaracteresValidosTxtCodigo);
  }
  
  public double getPesoLabel ()
  {
    return pesoLabel;
  }
  
  public void setPesoLabel (double pPesoLabel)
  {
    if (! (pPesoLabel <= 0.0) && ! (pPesoLabel >= 1.0) && txtCodigo.isVisible ())
      {
	pesoLabel = pPesoLabel;
	pesoTxtCodigo = 1.0 - pesoLabel;
	definicaoFormsColunas = "1px,MIN(12dlu;P):grow(" + String.valueOf (pesoTxtCodigo) + "),2dlu,P,2dlu,MIN(40dlu;P):grow(" + String.valueOf (pesoLabel) + ")";
	buildComponente ();
      }
  }
  
  public double getPesoTxtCodigo ()
  {
    return pesoTxtCodigo;
  }
  
  public void setPesoTxtCodigo (double pPesoTxtCodigo)
  {
    if (! (pPesoTxtCodigo <= 0.0) && ! (pPesoTxtCodigo >= 1.0) && txtCodigo.isVisible ())
      {
	pesoTxtCodigo = pPesoTxtCodigo;
	pesoLabel = 1.0 - pesoTxtCodigo;
	definicaoFormsColunas = "1px,MIN(12dlu;P):grow(" + String.valueOf (pesoTxtCodigo) + "),2dlu,P,2dlu,MIN(40dlu;P):grow(" + String.valueOf (pesoLabel) + ")";
	buildComponente ();
      }
  }
  
  public boolean isQuebraAutomaticaTexto ()
  {
    return quebraAutomaticaTexto;
  }
  
  public void setQuebraAutomaticaTexto (boolean quebraAutomaticaTexto)
  {
    this.quebraAutomaticaTexto = quebraAutomaticaTexto;
  }
  
  public Icon getIconBtnPopup ()
  {
    return btnPopup.getIcon ();
  }
  
  public void setIconBtnPopup (Icon icon)
  {
    btnPopup.setIcon (icon);
  }
  
  public boolean isPodeDigitarValoresForaDoDominio ()
  {
    return podeDigitarValoresForaDoDominio;
  }
  
  public void setPodeDigitarValoresForaDoDominio (boolean podeDigitarValoresForaDoDominio)
  {
    this.podeDigitarValoresForaDoDominio = podeDigitarValoresForaDoDominio;
  }
  
  public void setarCampo ()
  {
    int total = ((Codigo) getInformacao ()).getColecaoElementoTabela ().size ();
    int itemSelecionado = -1;
    String upperFiltro = ((JTextField) edtMsk.getComponenteFoco ()).getText ().trim ().toUpperCase ();
    boolean achouOpcaoValida = false;
    for (int i = 0; i < total; i++)
      {
	ElementoTabela elem = (ElementoTabela) ((Codigo) getInformacao ()).getColecaoElementoTabela ().get (i);
	String item = UtilitariosString.removeAcentos (elem.getConteudo (colunaExibidaCodigo)).trim ();
	if (item.toUpperCase ().equals (upperFiltro))
	  {
	    itemSelecionado = i;
	    achouOpcaoValida = true;
	  }
      }
    if (! isPodeDigitarValoresForaDoDominio () && ! achouOpcaoValida)
      {
	getInformacao ().clear ();
	((JFormattedTextField) edtMsk.getComponenteFoco ()).setValue (null);
      }
    else
      getInformacao ().setConteudo (((JTextField) edtMsk.getComponenteFoco ()).getText ());
  }
  
  public static void main (String[] args)
  {
    /* empty */
  }
}
