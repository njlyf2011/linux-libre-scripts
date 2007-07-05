/* ConteudoPainelDicas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosString;

public class ConteudoPainelDicas extends JPanel
{
  private static final int DICAS_AVISO = 2;
  private static final int DICAS_ATENCAO = 1;
  private static final int DICAS_ERRO = 3;
  private static final int DICAS_ERRO_IMPEDITIVO = 5;
  private static final int DICAS_ERRO_OK_CANCELAR = 4;
  public static final int TAM_ICO_FECHARDICAS = 18;
  public static final int ALT_ICO_FECHARDICAS = 16;
  public static final int ALT_LB_TITULO = 24;
  public static final int TAM_MARGEM = 5;
  public final ImageIcon ICO_FECHARDICAS = ConstantesGlobaisGUI.ICO_FECHARDICAS;
  public final ImageIcon ICO_FECHARDICASDESABILITADO = ConstantesGlobaisGUI.ICO_FECHARDICAS;
  private Box boxDicas;
  private Box boxTitulo;
  private JEditorPane editDicas;
  private JLabel lbTitulo;
  private JButton btnFechar;
  private JPanel painelBotoes = new JPanel ();
  private ButtonPPGD btnOk = new ButtonPPGD ("Ok");
  private ButtonPPGD btnCancel = new ButtonPPGD ("Cancelar");
  private int altura;
  
  class ParserCallbackLocal extends HTMLEditorKit.ParserCallback
  {
    private FontMetrics fm;
    private int tamMax;
    private int posFinal;
    private String htmlTexto;
    private int nrLinhasHtml;
    
    public ParserCallbackLocal (FontMetrics fm, int tamMax, int posFinal)
    {
      this.fm = fm;
      this.tamMax = tamMax;
      this.posFinal = posFinal;
      nrLinhasHtml = 0;
    }
    
    public void handleSimpleTag (HTML.Tag t, MutableAttributeSet a, int pos)
    {
      if (t.breaksFlow () || pos >= posFinal)
	{
	  nrLinhasHtml += calculaNrLinhas (htmlTexto);
	  htmlTexto = null;
	}
    }
    
    public void handleEndTag (HTML.Tag t, int pos)
    {
      if (t.toString ().equals ("html"))
	nrLinhasHtml += calculaNrLinhas (htmlTexto);
    }
    
    public void handleText (char[] data, int pos)
    {
      if (htmlTexto == null)
	htmlTexto = String.copyValueOf (data);
      else
	{
	  ParserCallbackLocal parsercallbacklocal = this;
	  String string = parsercallbacklocal.htmlTexto;
	  StringBuffer stringbuffer = new StringBuffer (string);
	  parsercallbacklocal.htmlTexto = stringbuffer.append (String.copyValueOf (data)).toString ();
	}
    }
    
    public int getNrLinhas ()
    {
      return nrLinhasHtml;
    }
    
    private int calculaNrLinhas (String texto)
    {
      String s = "";
      String lin = "";
      int count = 0;
      if (texto == null)
	return 0;
      StringTokenizer sToken = new StringTokenizer (texto);
      if (sToken.countTokens () == 0)
	return 0;
      if (sToken.countTokens () == 1)
	return 1;
      while (sToken.hasMoreTokens ())
	{
	  String token = sToken.nextToken ();
	  s = lin + token + " ";
	  int tam = SwingUtilities.computeStringWidth (fm, s);
	  if (tam >= tamMax)
	    {
	      lin = "";
	      count++;
	    }
	  lin += (String) token + " ";
	}
      if (s != "")
	count++;
      return count;
    }
  }
  
  public ConteudoPainelDicas (int tipo, String titulo, String texto, int x, int y, int tamMin)
  {
    buildPainel (tipo, titulo, texto, x, y, tamMin);
  }
  
  private void buildPainel (int tipo, String titulo, String texto, int x, int y, int tamMin)
  {
    aplicaBorda ();
    String fonteHTML = aplicaFormatacao (tipo, titulo);
    setAltura (aplicaTexto (fonteHTML, texto, titulo, tamMin));
    if (5 == tipo)
      aplicaBotaoOkSomente ();
    else if (4 == tipo)
      aplicaBotoesOkCancelar ();
  }
  
  private void aplicaBorda ()
  {
    setLayout (new BorderLayout ());
    boxTitulo = new Box (0);
    lbTitulo = new JLabel ();
    lbTitulo.setOpaque (true);
    boxTitulo.setBorder (BorderFactory.createEmptyBorder ());
    boxTitulo.add (lbTitulo);
    setBtnFechar (new JButton (ICO_FECHARDICAS));
    getBtnFechar ().setBorderPainted (false);
    getBtnFechar ().setToolTipText ("Fechar");
    getBtnFechar ().setBackground (Color.white);
    int wFechar = ICO_FECHARDICAS.getIconWidth () + 4;
    int hFechar = ICO_FECHARDICAS.getIconHeight () + 4;
    getBtnFechar ().setSize (wFechar, hFechar);
    getBtnFechar ().setPreferredSize (new Dimension (wFechar, hFechar));
    getBtnFechar ().setMinimumSize (new Dimension (wFechar, hFechar));
    getBtnFechar ().setMaximumSize (new Dimension (wFechar, hFechar));
    getBtnFechar ().addMouseListener (new MouseAdapter ()
    {
      public void mouseEntered (MouseEvent e)
      {
	getBtnFechar ().setIcon (ICO_FECHARDICASDESABILITADO);
      }
      
      public void mouseExited (MouseEvent e)
      {
	getBtnFechar ().setIcon (ICO_FECHARDICAS);
      }
    });
    boxTitulo.add (getBtnFechar ());
    editDicas = new JEditorPane ("text/html", " ");
    editDicas.setEditable (false);
    editDicas.addHyperlinkListener (new ExecutaHiperlinkDicas ());
    boxDicas = new Box (1);
    boxDicas.add (boxTitulo);
    add (editDicas, "Center");
    add (boxDicas, "North");
  }
  
  private void aplicaBotaoOkSomente ()
  {
    JPanel painelBotoes = new JPanel ();
    btnOk.setBorderPainted (false);
    btnOk.setBackground (Color.white);
    painelBotoes.setBackground (Color.white);
    painelBotoes.add (btnOk);
    add (painelBotoes, "South");
  }
  
  private void aplicaBotoesOkCancelar ()
  {
    btnOk.setBorderPainted (false);
    btnOk.setBackground (Color.white);
    btnCancel.setBorderPainted (false);
    btnCancel.setBackground (Color.white);
    painelBotoes.setBackground (Color.white);
    if (System.getProperty ("os.name").startsWith ("Mac"))
      {
	painelBotoes.add (btnCancel);
	painelBotoes.add (btnOk);
      }
    else
      {
	painelBotoes.add (btnOk);
	painelBotoes.add (btnCancel);
      }
    add (painelBotoes, "South");
  }
  
  private int aplicaTexto (String fonteHTML, String texto, String titulo, int tamMin)
  {
    texto = UtilitariosString.expandeStringHTML (texto, fonteHTML, 0);
    editDicas.setText (texto);
    FontMetrics fm = getFontMetrics (editDicas.getFont ());
    int tamTitulo = SwingUtilities.computeStringWidth (fm, titulo);
    tamMin = Math.max (tamMin, tamTitulo + 5) + 18;
    ParserCallbackLocal callback = new ParserCallbackLocal (fm, tamMin - 4, texto.length ());
    HtmlParser p = new HtmlParser (texto, callback);
    int nrLinhas = callback.getNrLinhas ();
    int ajuste = 0;
    if (nrLinhas < 3)
      ajuste = 3;
    else if (nrLinhas > 35)
      {
	tamTitulo *= 3;
	nrLinhas = 35;
      }
    tamMin = Math.max (tamMin, tamTitulo + 5) + 18;
    if (tamTitulo <= 0)
      {
	LogPPGD.erro ("Tamanho do t\u00edtulo igual ou menor que zero!");
	LogPPGD.erro ("Isso significa que a informa\u00e7\u00e3o n\u00e3o teve um nome setado.");
	LogPPGD.erro ("Deve ser setado usando o Informacao.setNomeCampo ou Informacao.setNomeCampoAlternativo");
	tamTitulo = 10;
      }
    int aumentoProporcionalLargura = 4000 / tamTitulo;
    int alt = (nrLinhas + 1) * (fm.getHeight () + ajuste) + 24 + painelBotoes.getPreferredSize ().height + 10 + aumentoProporcionalLargura;
    UtilitariosGUI.setParametrosGUI (lbTitulo, tamMin - 18, 16);
    UtilitariosGUI.setParametrosGUI (editDicas, tamMin, alt - painelBotoes.getPreferredSize ().height);
    return alt;
  }
  
  private String aplicaFormatacao (int tipo, String titulo)
  {
    Color c = null;
    Color corTitulo = null;
    String fonteHTML = null;
    switch (tipo)
      {
      case 2:
	c = ConstantesGlobaisGUI.COR_BORDA_PAINEL_DICAS_AVISO;
	corTitulo = ConstantesGlobaisGUI.COR_TITULO_PAINEL_DICAS_AVISO;
	break;
      case 1:
	c = ConstantesGlobaisGUI.COR_BORDA_PAINEL_DICAS_ATENCAO;
	corTitulo = ConstantesGlobaisGUI.COR_TITULO_PAINEL_DICAS_ATENCAO;
	break;
      case 3:
	c = ConstantesGlobaisGUI.COR_BORDA_PAINEL_DICAS_ERRO;
	corTitulo = ConstantesGlobaisGUI.COR_TITULO_PAINEL_DICAS_ERRO;
	break;
      case 5:
	c = ConstantesGlobaisGUI.COR_BORDA_PAINEL_DICAS_ERRO_IMPEDITIVO;
	corTitulo = ConstantesGlobaisGUI.COR_TITULO_PAINEL_DICAS_ERRO_IMPEDITIVO;
	break;
      case 4:
	c = ConstantesGlobaisGUI.COR_BORDA_PAINEL_DICAS_ERRO_OK_CANCELAR;
	corTitulo = ConstantesGlobaisGUI.COR_TITULO_PAINEL_DICAS_ERRO_OK_CANCELAR;
	break;
      default:
	c = ConstantesGlobaisGUI.COR_BORDA_PAINEL_DICAS_DEFAULT;
	corTitulo = ConstantesGlobaisGUI.COR_TITULO_PAINEL_DICAS_DEFAULT;
      }
    fonteHTML = "<font size=\"3\">";
    lbTitulo.setText ("<HTML><B>" + titulo + "</B></HTML>");
    lbTitulo.setBackground (c);
    boxTitulo.setBackground (c);
    setBackground (c);
    lbTitulo.setForeground (corTitulo);
    if (btnOk != null)
      btnOk.setForeground (ConstantesGlobaisGUI.COR_PRETO);
    if (btnCancel != null)
      btnCancel.setForeground (ConstantesGlobaisGUI.COR_PRETO);
    editDicas.setForeground (c);
    boxTitulo.setBorder (BorderFactory.createMatteBorder (0, 0, 1, 0, c));
    setBorder (BorderFactory.createMatteBorder (2, 2, 2, 2, c));
    return fonteHTML;
  }
  
  public void setBtnOk (ButtonPPGD btnOk)
  {
    this.btnOk = btnOk;
  }
  
  public ButtonPPGD getBtnOk ()
  {
    return btnOk;
  }
  
  public void setBtnCancel (ButtonPPGD btnCancel)
  {
    this.btnCancel = btnCancel;
  }
  
  public ButtonPPGD getBtnCancel ()
  {
    return btnCancel;
  }
  
  public void setBtnFechar (JButton btnFechar)
  {
    this.btnFechar = btnFechar;
  }
  
  public JButton getBtnFechar ()
  {
    return btnFechar;
  }
  
  public void setAltura (int altura)
  {
    this.altura = altura;
  }
  
  public int getAltura ()
  {
    return altura;
  }
}
