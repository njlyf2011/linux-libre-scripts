/* FabricaGUI - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CEP;
import serpro.ppgd.negocio.CNPJ;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Inteiro;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.NIRF;
import serpro.ppgd.negocio.Nome;
import serpro.ppgd.negocio.Opcao;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.LogPPGD;

public abstract class FabricaGUI
{
  private float alinhamento;
  private int eixo;
  private byte posicaoRotulo;
  public static final PainelDicas painelDicas = new PainelDicas ();
  private static JPanel glassPane = null;
  public static MouseAdapter mouseAdapterBloqueadorEventos = new MouseAdapter ()
  {
  };
  public static KeyAdapter keyAdapterBloqueadorEventos = new KeyAdapter ()
  {
  };
  
  static
  {
    glassPane = new JPanel ();
    glassPane.setLayout (null);
    glassPane.setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    glassPane.setOpaque (false);
    glassPane.add (painelDicas);
    JFrame framePrincipal = UtilitariosGUI.tentaObterJanelaPrincipal ();
    if (framePrincipal != null)
      {
	framePrincipal.getRootPane ().setGlassPane (glassPane);
	framePrincipal.getRootPane ().getGlassPane ().setVisible (true);
      }
  }
  
  public FabricaGUI (int eixo, float alinhamento, byte posicaoRotulo)
  {
    this.eixo = eixo;
    this.alinhamento = alinhamento;
    if (posicaoRotulo < 0 || posicaoRotulo > 2)
      throw new IllegalArgumentException ("Posi\u00e7\u00e3o do r\u00f3tulo inv\u00e1lida");
    this.posicaoRotulo = posicaoRotulo;
  }
  
  public ContainerPPGD construirBoxIRPF ()
  {
    return new ContainerPPGD (eixo, alinhamento, posicaoRotulo);
  }
  
  public static EditCampo getEditorInformacao (Informacao campo, Dimension[] d, String idAjuda)
  {
    EditCampo resultado;
    if (campo instanceof Nome)
      resultado = new EditNome (campo, 60);
    else if (campo instanceof Data)
      resultado = new EditData (campo);
    else if (campo instanceof Alfa)
      {
	if (((Alfa) campo).getMaximoCaracteres () == 1)
	  resultado = new EditAlfa (campo, d[0].width);
	else
	  resultado = new EditAlfa (campo, ((Alfa) campo).getMaximoCaracteres ());
      }
    else if (campo instanceof CEP)
      resultado = new EditCEP (campo);
    else if (campo instanceof Valor)
      resultado = new EditValor (campo);
    else if (campo instanceof Inteiro)
      resultado = new EditInteiro (campo);
    else if (campo instanceof Codigo)
      resultado = new EditCodigo (campo, d, idAjuda);
    else if (campo instanceof CPF)
      resultado = new EditCPF (campo);
    else if (campo instanceof Logico)
      resultado = new EditLogico (campo, d[0].width);
    else if (campo instanceof Opcao)
      resultado = new EditOpcao (campo);
    else if (campo instanceof NI)
      resultado = new EditNI (campo);
    else if (campo instanceof NIRF)
      resultado = new EditNIRF (campo);
    else if (campo instanceof CNPJ)
      resultado = new EditCNPJ (campo);
    else
      resultado = null;
    if (resultado != null)
      resultado.setIdAjuda (idAjuda);
    return resultado;
  }
  
  public static EditCampo getEditorInformacao (Informacao campo, int tamanho, String idAjuda)
  {
    return getEditorInformacao (campo, new Dimension[] { new Dimension (tamanho, 1), new Dimension (0, 0) }, idAjuda);
  }
  
  public static JComponent criaJCampo (EditCampo editCampo)
  {
    JPanel boxCampo = new JPanel (new BorderLayout ());
    boxCampo.add (editCampo.getComponenteEditor (), "Center");
    boxCampo.add (editCampo.getButtonMensagem (), "East");
    return boxCampo;
  }
  
  public static JComponent criaJCampo (EditCampo editCampo, String idAjuda)
  {
    editCampo.setIdAjuda (idAjuda);
    return criaJCampo (editCampo);
  }
  
  public static JComponent criaJCampo (Informacao campo)
  {
    return criaJCampo (campo, (String) null);
  }
  
  public static JComponent criaJCampo (Informacao campo, String idAjuda)
  {
    Dimension[] d = new Dimension[1];
    d[0] = new Dimension (0, 0);
    return criaJCampo (campo, d, idAjuda);
  }
  
  public static JComponent criaJCampo (Informacao campo, int tamanho)
  {
    return criaJCampo (campo, tamanho, null);
  }
  
  public static JComponent criaJCampo (Informacao campo, int tamanho, String idAjuda)
  {
    Dimension[] d = new Dimension[1];
    d[0] = new Dimension (tamanho, 0);
    return criaJCampo (campo, d, idAjuda);
  }
  
  public static JComponent criaJCampo (Informacao campo, Dimension dimension, String idAjuda)
  {
    Dimension[] d = new Dimension[1];
    d[0] = dimension;
    return criaJCampo (campo, d, idAjuda);
  }
  
  public static JComponent criaJCampo (Informacao campo, Dimension[] dimension)
  {
    return criaJCampo (campo, dimension, null);
  }
  
  public static JComponent criaJCampo (Informacao campo, Dimension[] dimension, String idAjuda)
  {
    EditCampo editCampo = getEditorInformacao (campo, dimension, idAjuda);
    return criaJCampo (editCampo);
  }
  
  public static EditCampo criaCampo (Informacao campo, int tamanho)
  {
    return criaCampo (campo, tamanho, null);
  }
  
  public static EditCampo criaCampo (EditCampo campo, String idAjuda)
  {
    campo.setIdAjuda (idAjuda);
    return campo;
  }
  
  public static EditCampo criaCampo (Informacao campo, String idAjuda)
  {
    return criaCampo (campo, 0, idAjuda);
  }
  
  public static EditCampo criaCampo (Informacao campo)
  {
    return criaCampo (campo, 0, null);
  }
  
  public static EditCampo criaCampo (Informacao campo, int tamanho, String idAjuda)
  {
    EditCampo editCampo = getEditorInformacao (campo, tamanho, idAjuda);
    return editCampo;
  }
  
  public static void exibirPainelDicas (int tipo, String titulo, String texto, Component ref, int tam)
  {
    Point p = obtemLocalizacaoPainelDicas (ref);
    if (p != null)
      {
	SwingUtilities.convertPointFromScreen (p, glassPane);
	painelDicas.mostrarPainelDicas (tipo, titulo, texto, p.x + 20, p.y + 8, tam);
      }
  }
  
  public static void exibirPainelDicas (Window janela, int tipo, String titulo, String texto, Component ref, int tam)
  {
    PainelDicas pnlDicasTemp = new PainelDicas ();
    JPanel novoGlassPane = new JPanel ();
    novoGlassPane.setLayout (null);
    novoGlassPane.setBackground (ConstantesGlobaisGUI.COR_BRANCO);
    novoGlassPane.setOpaque (false);
    novoGlassPane.add (pnlDicasTemp);
    Point p = obtemLocalizacaoPainelDicas (ref);
    if (p != null)
      {
	if (janela instanceof JDialog)
	  {
	    ((JDialog) janela).getRootPane ().setGlassPane (novoGlassPane);
	    ((JDialog) janela).getRootPane ().getGlassPane ().setVisible (true);
	    SwingUtilities.convertPointFromScreen (p, ((JDialog) janela).getGlassPane ());
	  }
	else
	  {
	    ((JFrame) janela).getRootPane ().setGlassPane (novoGlassPane);
	    ((JFrame) janela).getRootPane ().getGlassPane ().setVisible (true);
	    SwingUtilities.convertPointFromScreen (p, ((JFrame) janela).getGlassPane ());
	  }
	pnlDicasTemp.mostrarPainelDicas (tipo, titulo, texto, p.x + 20, p.y + 8, tam);
      }
  }
  
  private static Point obtemLocalizacaoPainelDicas (Component ref)
  {
    Point p = null;
    try
      {
	p = ref.getLocationOnScreen ();
      }
    catch (IllegalComponentStateException e)
      {
	LogPPGD.erro ("O painel de dicas n\u00e3o pode ser exibido porq o botao ainda n\u00e3o est\u00e1 visivel!!");
      }
    return p;
  }
  
  public static JPanel getGlassPane ()
  {
    return glassPane;
  }
  
  public static void exibirPainelDicasModal (Window parent, int tipo, String titulo, String texto, Component ref, int tam)
  {
    Point p = obtemLocalizacaoPainelDicas (ref);
    if (p == null)
      {
	Dimension tela = Toolkit.getDefaultToolkit ().getScreenSize ();
	Point centralizado = new Point ((tela.width - ref.getWidth ()) / 2, (tela.height - ref.getHeight ()) / 2);
	new PainelDicasModal (tipo, titulo, texto, centralizado.x + 20, centralizado.y + 8, tam).exibe ();
      }
    else
      {
	p.x += ref.getWidth ();
	if (parent instanceof Dialog)
	  new PainelDicasModal ((Dialog) parent, tipo, titulo, texto, p.x + 20, p.y + 8, tam).exibe ();
	else if (parent instanceof Frame)
	  new PainelDicasModal ((Frame) parent, tipo, titulo, texto, p.x + 20, p.y + 8, tam).exibe ();
      }
  }
  
  public static void exibirPainelDicasModal (Window parent, int tipo, String titulo, String texto, JComponent ref, int tam, ActionListener preActionOk)
  {
    Point p = obtemLocalizacaoPainelDicas (ref);
    if (p != null)
      {
	if (p == null)
	  {
	    Dimension tela = Toolkit.getDefaultToolkit ().getScreenSize ();
	    Point centralizado = new Point ((tela.width - ref.getWidth ()) / 2, (tela.height - ref.getHeight ()) / 2);
	    PainelDicasModal painel = new PainelDicasModal (tipo, titulo, texto, centralizado.x + 20, centralizado.y + 8, tam);
	    preparaModalOk (preActionOk, painel);
	    painel.exibe ();
	  }
	else
	  {
	    p.x += ref.getWidth ();
	    if (parent instanceof Dialog)
	      {
		PainelDicasModal painel = new PainelDicasModal ((Dialog) parent, tipo, titulo, texto, p.x + 20, p.y + 8, tam);
		preparaModalOk (preActionOk, painel);
		painel.exibe ();
	      }
	    else if (parent instanceof Frame)
	      {
		PainelDicasModal painel = new PainelDicasModal ((Frame) parent, tipo, titulo, texto, p.x + 20, p.y + 8, tam);
		preparaModalOk (preActionOk, painel);
		painel.exibe ();
	      }
	    else
	      LogPPGD.erro ("O parent passado para o PainelDicas n\u00e3o \u00e9 nem Dialog nem Frame!");
	  }
      }
  }
  
  private static void preparaModalOk (final ActionListener preActionOk, PainelDicasModal painel)
  {
    painel.getPainelConteudo ().getBtnOk ().addActionListener (preActionOk);
    painel.getPainelConteudo ().getBtnFechar ().addActionListener (preActionOk);
    painel.addWindowListener (new WindowAdapter ()
    {
      public void windowClosing (WindowEvent e)
      {
	preActionOk.actionPerformed (null);
      }
    });
  }
  
  public static void exibirPainelDicasModal (Window parent, int tipo, String titulo, String texto, Component ref, int tam, ActionListener preActionOk, ActionListener preActionCancel)
  {
    Point p = obtemLocalizacaoPainelDicas (ref);
    if (p == null)
      {
	Dimension tela = Toolkit.getDefaultToolkit ().getScreenSize ();
	Point centralizado = new Point ((tela.width - ref.getWidth ()) / 2, (tela.height - ref.getHeight ()) / 2);
	PainelDicasModal painel = new PainelDicasModal (tipo, titulo, texto, centralizado.x + 20, centralizado.y + 8, tam);
	preparaModalOk (preActionOk, painel);
	painel.exibe ();
      }
    else
      {
	p.x += ref.getWidth ();
	if (parent instanceof Dialog)
	  {
	    PainelDicasModal painel = new PainelDicasModal ((Dialog) parent, tipo, titulo, texto, p.x + 20, p.y + 8, tam);
	    preparaModalOkCancel (preActionOk, preActionCancel, painel);
	    painel.exibe ();
	  }
	else if (parent instanceof Frame)
	  {
	    PainelDicasModal painel = new PainelDicasModal ((Frame) parent, tipo, titulo, texto, p.x + 20, p.y + 8, tam);
	    preparaModalOkCancel (preActionOk, preActionCancel, painel);
	    painel.exibe ();
	  }
	else
	  LogPPGD.erro ("O parent passado para o PainelDicas n\u00e3o \u00e9 nem Dialog nem Frame!");
      }
  }
  
  private static void preparaModalOkCancel (ActionListener preActionOk, final ActionListener preActionCancel, PainelDicasModal painel)
  {
    painel.getPainelConteudo ().getBtnOk ().addActionListener (preActionOk);
    painel.getPainelConteudo ().getBtnFechar ().addActionListener (preActionCancel);
    painel.getPainelConteudo ().getBtnCancel ().addActionListener (preActionCancel);
    painel.addWindowListener (new WindowAdapter ()
    {
      public void windowClosing (WindowEvent e)
      {
	preActionCancel.actionPerformed (null);
      }
    });
  }
  
  public static void esconderPainelDicas ()
  {
    painelDicas.esconderPainelDicas ();
  }
  
  public static void mudaCursor (Component ref, int pTipoCursor)
  {
    try
      {
	((JFrame) SwingUtilities.getRoot (ref)).setCursor (Cursor.getPredefinedCursor (pTipoCursor));
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
  
  public static void mudaCursorNoComponente (Component pComp, int pTipoCursor)
  {
    try
      {
	pComp.setCursor (Cursor.getPredefinedCursor (pTipoCursor));
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
  
  public static void setaTitledBorderMultiLinha (JComponent c, String pTexto, Color pCor, Font pFont)
  {
    javax.swing.border.Border bordaGeral = null;
    StringTokenizer tokenizer = new StringTokenizer (pTexto, "\n");
    while (tokenizer.hasMoreTokens ())
      {
	String tokenAtual = tokenizer.nextToken ();
	if (bordaGeral == null)
	  {
	    TitledBorder titledBordaAtual = BorderFactory.createTitledBorder (tokenAtual);
	    if (pCor != null)
	      titledBordaAtual.setTitleColor (pCor);
	    if (pFont != null)
	      titledBordaAtual.setTitleFont (pFont);
	    bordaGeral = titledBordaAtual;
	  }
	else
	  {
	    TitledBorder titledBordaAtual = BorderFactory.createTitledBorder (BorderFactory.createEmptyBorder (), tokenAtual);
	    if (pCor != null)
	      titledBordaAtual.setTitleColor (pCor);
	    if (pFont != null)
	      titledBordaAtual.setTitleFont (pFont);
	    bordaGeral = BorderFactory.createCompoundBorder (bordaGeral, titledBordaAtual);
	  }
      }
    if (bordaGeral != null)
      c.setBorder (bordaGeral);
  }
  
  public static Action criaActionTransfereFoco ()
  {
    return new ActionTransfereFoco ();
  }
}
