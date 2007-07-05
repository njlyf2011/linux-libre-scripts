/* JTransnetDlg - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class JTransnetDlg extends JDialog implements ControleProgressoEnvio
{
  protected JTransnetDlgEvent dlgQueue;
  private JButton jBtnCancelar;
  private JProgressBar jProgBarConect;
  private JProgressBar jProgBarTransf;
  private Label lblConectar;
  private Label lblTransfer;
  private JLabel jLabelIcoReceita;
  private JLabel jLabelAnimaWait;
  private JLabel jLabelAnimaTransmissao;
  private ImageIcon imgAnimacaoTransmitindo;
  private ImageIcon imgAnimacaoAmpulheta;
  private ImageIcon imageIcoReceita;
  private String m_strNomeArquivoDecl;
  public String m_strProxyPwd;
  public boolean m_bCancelar;
  ThreadEnvio m_threadEnvio;
  
  public JTransnetDlg (JDialog jdialog, String string, String string_0_)
  {
    super (jdialog, "Transmiss\u00e3o de Declara\u00e7\u00e3o", true);
    jBtnCancelar = new JButton ();
    jProgBarConect = new JProgressBar ();
    jProgBarTransf = new JProgressBar ();
    lblConectar = new Label ();
    lblTransfer = new Label ();
    jLabelIcoReceita = new JLabel ();
    jLabelAnimaWait = new JLabel ();
    jLabelAnimaTransmissao = new JLabel ();
    m_bCancelar = false;
    constroi (string, string_0_);
  }
  
  public JTransnetDlg (JFrame jframe, String string, String string_1_)
  {
    super (jframe, "Transmiss\u00e3o de Declara\u00e7\u00e3o", true);
    jBtnCancelar = new JButton ();
    jProgBarConect = new JProgressBar ();
    jProgBarTransf = new JProgressBar ();
    lblConectar = new Label ();
    lblTransfer = new Label ();
    jLabelIcoReceita = new JLabel ();
    jLabelAnimaWait = new JLabel ();
    jLabelAnimaTransmissao = new JLabel ();
    m_bCancelar = false;
    constroi (string, string_1_);
  }
  
  void constroi (String string, String string_2_)
  {
    m_strNomeArquivoDecl = string;
    m_strProxyPwd = string_2_;
    try
      {
	jbInit ();
      }
    catch (Exception exception)
      {
	exception.printStackTrace ();
      }
  }
  
  private void jbInit () throws Exception
  {
    dlgQueue = new JTransnetDlgEvent (this);
    jBtnCancelar.setBounds (new Rectangle (177, 189, 95, 30));
    jBtnCancelar.setText ("Cancelar");
    jBtnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent actionevent)
      {
	jBtnCancelar_actionPerformed (actionevent);
      }
    });
    jBtnCancelar.setMnemonic ('C');
    imgAnimacaoTransmitindo = new ImageIcon ("imagens/transmitindo.gif");
    imgAnimacaoAmpulheta = new ImageIcon ("imagens/wait.gif");
    imageIcoReceita = new ImageIcon ("imagens/logotxt_srf.gif");
    jLabelIcoReceita.setIcon (imageIcoReceita);
    jLabelAnimaWait.setIcon (imgAnimacaoAmpulheta);
    jLabelAnimaTransmissao.setIcon (imgAnimacaoTransmitindo);
    setResizable (true);
    getContentPane ().setLayout (null);
    getContentPane ().setSize (new Dimension (400, 400));
    setSize (new Dimension (439, 260));
    getContentPane ().setBackground (SystemColor.control);
    JUtil.centerWindow (this);
    jProgBarConect.setBounds (new Rectangle (143, 84, 276, 22));
    jProgBarConect.setMinimum (0);
    jProgBarConect.setMaximum (3);
    jProgBarTransf.setEnabled (false);
    jProgBarTransf.setBounds (new Rectangle (144, 145, 275, 24));
    lblConectar.setText ("Conectando-se \u00e0 Receita Federal");
    lblConectar.setBounds (new Rectangle (142, 60, 278, 18));
    lblTransfer.setEnabled (false);
    lblTransfer.setText ("Transferindo o arquivo");
    lblTransfer.setBounds (new Rectangle (146, 122, 272, 17));
    jLabelIcoReceita.setBounds (new Rectangle (16, 13, 173, 40));
    jLabelAnimaWait.setBounds (new Rectangle (10, 96, 117, 62));
    jLabelAnimaWait.setVisible (false);
    jLabelAnimaTransmissao.setBounds (new Rectangle (10, 108, 117, 62));
    jLabelAnimaTransmissao.setVisible (false);
    getContentPane ().add (jLabelAnimaWait, (Object) null);
    getContentPane ().add (jLabelIcoReceita, (Object) null);
    getContentPane ().add (jProgBarTransf, (Object) null);
    getContentPane ().add (lblTransfer, (Object) null);
    getContentPane ().add (jProgBarConect, (Object) null);
    getContentPane ().add (lblConectar, (Object) null);
    getContentPane ().add (jBtnCancelar, (Object) null);
    getContentPane ().add (jLabelAnimaTransmissao, (Object) null);
    validate ();
    KeyAdapter keyadapter = new KeyAdapter ()
    {
      public void keyPressed (KeyEvent keyevent)
      {
	if (keyevent.getKeyCode () == 27)
	  cancel ();
      }
    };
    addKeyListener (keyadapter);
    addWindowListener (new WindowAdapter ()
    {
      public void windowClosing (WindowEvent windowevent)
      {
	windowevent.getWindow ().dispose ();
      }
      
      public void windowOpened (WindowEvent windowevent)
      {
	m_threadEnvio = new ThreadEnvio ((JTransnetDlg) windowevent.getWindow (), m_strNomeArquivoDecl);
	m_threadEnvio.start ();
      }
    });
    jBtnCancelar.requestFocus ();
    getRootPane ().setDefaultButton (jBtnCancelar);
  }
  
  void jBtnCancelar_actionPerformed (ActionEvent actionevent)
  {
    cancel ();
  }
  
  void cancel ()
  {
    m_bCancelar = true;
    m_threadEnvio.setError ("E R R O!\nTransmiss\u00e3o cancelada.\nA declara\u00e7\u00e3o N\u00c3O foi transmitida.\nVoc\u00ea deve reiniciar o processo de transmiss\u00e3o.");
    m_threadEnvio.setRetCode (0);
    dispose ();
  }
  
  public String getLastError ()
  {
    return m_threadEnvio.getLastError ();
  }
  
  public String getRecibo ()
  {
    return m_threadEnvio.getRecibo ();
  }
  
  public int getRetCode ()
  {
    return m_threadEnvio.getRetCode ();
  }
  
  public void conectando ()
  {
    jProgBarConect.setValue (1);
    lblConectar.setText ("Conectando-se \u00e0 Receita Federal...");
    jLabelAnimaWait.setVisible (true);
  }
  
  public void iniciandoEnvio (int i)
  {
    lblConectar.setText ("Conectado \u00e0 Receita Federal");
    jProgBarConect.setValue (3);
    jProgBarTransf.setEnabled (true);
    jProgBarTransf.setMaximum (i);
    lblTransfer.setEnabled (true);
    jLabelAnimaWait.setVisible (false);
    jLabelAnimaTransmissao.setVisible (true);
  }
  
  public void atualizaProgresso (int i)
  {
    jProgBarTransf.setValue (i);
  }
  
  public void fimTransmissao ()
  {
    jLabelAnimaTransmissao.setVisible (false);
    jBtnCancelar.setEnabled (false);
    dispose ();
  }
  
  public boolean cancelado ()
  {
    return m_bCancelar;
  }
}
