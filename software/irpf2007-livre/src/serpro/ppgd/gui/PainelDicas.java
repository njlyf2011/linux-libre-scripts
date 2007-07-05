/* PainelDicas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import serpro.ppgd.negocio.util.UtilitariosString;

public class PainelDicas extends JPanel
{
  private static final int DICAS_AVISO = 2;
  private static final int DICAS_ATENCAO = 1;
  private static final int DICAS_ERRO = 3;
  private static final int DICAS_ERRO_IMPEDITIVO = 5;
  private final int TAM_ICO_FECHARDICAS = 18;
  private final int ALT_ICO_FECHARDICAS = 16;
  private final int ALT_LB_TITULO = 24;
  private final int TAM_MARGEM = 5;
  private final ImageIcon ICO_FECHARDICAS = ConstantesGlobaisGUI.ICO_FECHARDICAS;
  private final ImageIcon ICO_FECHARDICASDESABILITADO = ConstantesGlobaisGUI.ICO_FECHARDICASDESABILITADO;
  private Box boxDicas;
  private Box boxTitulo;
  private JEditorPane editDicas;
  private JLabel lbTitulo;
  private JLabel lbIcone;
  
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
  
  class EsconderDicas extends MouseAdapter
  {
    public void mouseEntered (MouseEvent e)
    {
      lbIcone.setIcon (ICO_FECHARDICASDESABILITADO);
      FabricaGUI.mudaCursorNoComponente ((Component) e.getSource (), 12);
    }
    
    public void mouseExited (MouseEvent e)
    {
      lbIcone.setIcon (ICO_FECHARDICAS);
      FabricaGUI.mudaCursorNoComponente ((Component) e.getSource (), 0);
    }
    
    public void mouseClicked (MouseEvent e)
    {
      esconderPainelDicas ();
    }
  }
  
  public PainelDicas ()
  {
    setVisible (false);
    setLayout (new BorderLayout ());
    boxTitulo = new Box (0);
    lbTitulo = new JLabel ();
    lbTitulo.setOpaque (true);
    boxTitulo.add (lbTitulo);
    lbIcone = new JLabel (ICO_FECHARDICAS);
    lbIcone.addMouseListener (new EsconderDicas ());
    boxTitulo.add (lbIcone);
    editDicas = new JEditorPane ("text/html", " ");
    editDicas.setEditable (false);
    editDicas.addHyperlinkListener (new ExecutaHiperlinkDicas ());
    boxDicas = new Box (1);
    boxDicas.add (boxTitulo);
    add (editDicas, "Center");
    add (boxDicas, "North");
    addFocusListener (new FocusListener ()
    {
      public void focusGained (FocusEvent e)
      {
	/* empty */
      }
      
      public void focusLost (FocusEvent e)
      {
	esconderPainelDicas ();
      }
    });
  }
  
  public void mostrarPainelDicas (int tipo, String titulo, String texto, int x, int y, int tamMin)
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
      default:
	c = ConstantesGlobaisGUI.COR_BORDA_PAINEL_DICAS_DEFAULT;
	corTitulo = ConstantesGlobaisGUI.COR_TITULO_PAINEL_DICAS_DEFAULT;
      }
    fonteHTML = "<font size=\"3\">";
    lbTitulo.setText ("<HTML><B>" + titulo + "</B></HTML>");
    lbTitulo.setBackground (c);
    lbTitulo.setForeground (corTitulo);
    editDicas.setForeground (c);
    boxTitulo.setBorder (BorderFactory.createMatteBorder (0, 0, 1, 0, c));
    setBorder (BorderFactory.createMatteBorder (2, 2, 2, 2, c));
    texto = UtilitariosString.expandeStringHTML (texto, fonteHTML, 0);
    editDicas.setText (texto);
    FontMetrics fm = getFontMetrics (editDicas.getFont ());
    int tamTitulo = SwingUtilities.computeStringWidth (fm, titulo);
    tamMin = Math.max (tamMin, tamTitulo + 5) + 18;
    ParserCallbackLocal callback = new ParserCallbackLocal (fm, tamMin - 4, texto.length ());
    HtmlParser p = new HtmlParser (texto, callback);
    int nrLinhas = callback.getNrLinhas ();
    int ajuste = 0;
    if (nrLinhas <= 3)
      ajuste = 3;
    else if (nrLinhas > 35)
      {
	tamTitulo *= 3;
	nrLinhas = 35;
      }
    int alt = (nrLinhas + 1) * (fm.getHeight () + ajuste) + 24;
    if (alt > 500)
      {
	tamMin += 100;
	alt -= 50;
      }
    tamMin = Math.max (tamMin, tamTitulo + 5) + 18;
    if (nrLinhas >= 6)
      alt += 3;
    UtilitariosGUI.setParametrosGUI (lbTitulo, tamMin - 18, 16);
    UtilitariosGUI.setParametrosGUI (editDicas, tamMin, alt - 24);
    Rectangle r = getRootPane ().getBounds ();
    Insets i = getRootPane ().getInsets ();
    if ((double) (x + tamMin) > r.getWidth ())
      x = (int) r.getWidth () - tamMin - i.right - i.left - 10;
    if ((double) (y + alt) > r.getHeight ())
      y = (int) r.getHeight () - alt - i.top - i.bottom - 10;
    setBounds (x, y, tamMin, alt);
    setVisible (true);
  }
  
  public void esconderPainelDicas ()
  {
    setVisible (false);
  }
}
