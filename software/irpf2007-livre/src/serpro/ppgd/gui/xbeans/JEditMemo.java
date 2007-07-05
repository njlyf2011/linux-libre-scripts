/* JEditMemo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import serpro.ppgd.gui.editors.PPGDTextArea;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;

public class JEditMemo extends JEditCampo
{
  private JTextArea componente;
  private JScrollPane scrollPane;
  private int nrMaxCaracteres = 3000;
  
  public JEditMemo ()
  {
    this (new Alfa ("Memo"));
  }
  
  public JEditMemo (Informacao campo)
  {
    super (campo);
  }
  
  public JEditMemo (Informacao campo, int nrMaxCaracteres)
  {
    super (campo);
    setMaxChars (nrMaxCaracteres);
  }
  
  public JComponent getComponenteEditor ()
  {
    return scrollPane;
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    componente.setText (getInformacao ().getConteudoFormatado ());
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    if (readOnly)
      componente.setEditable (false);
    else
      componente.setEditable (true);
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    componente.setEnabled (habilitado);
  }
  
  public JComponent getComponenteFoco ()
  {
    return componente;
  }
  
  public void setMaxChars (int nrMaxCaracteres)
  {
    this.nrMaxCaracteres = nrMaxCaracteres;
  }
  
  public int getMaxChars ()
  {
    return nrMaxCaracteres;
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (componente == null)
      {
	componente = new PPGDTextArea ();
	scrollPane = new JScrollPane (componente);
	componente.setLineWrap (true);
	componente.setWrapStyleWord (true);
	componente.setTabSize (1);
	componente.addFocusListener (new FocusAdapter ()
	{
	  public void focusLost (FocusEvent e)
	  {
	    if (e.getOppositeComponent () != null)
	      {
		JEditMemo.this.setIdentificacaoFoco (false);
		if (JEditMemo.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || JEditMemo.this.verificaValidacoesImpeditivas (componente.getText ()))
		  {
		    JEditMemo.this.setObservadorAtivo (false);
		    String s = componente.getText ();
		    if (s.length () > nrMaxCaracteres)
		      s = s.substring (0, nrMaxCaracteres);
		    JEditMemo.this.getInformacao ().setConteudo (s);
		    JEditMemo.this.chamaValidacao ();
		    JEditMemo.this.setObservadorAtivo (true);
		  }
	      }
	  }
	});
	componente.addKeyListener (new KeyAdapter ()
	{
	  public void keyPressed (KeyEvent e)
	  {
	    char ch = e.getKeyChar ();
	    boolean eventosEhInclusao = ch != '\uffff' && ch != '\010' && ch != '\u007f' && ch != '%' && ch != '\'';
	    int tamanhoTextoAtual = componente.getText ().length ();
	    if (tamanhoTextoAtual >= nrMaxCaracteres && eventosEhInclusao && componente.getSelectedText () == null)
	      {
		e.setKeyChar ('\uffff');
		e.consume ();
		UIManager.getLookAndFeel ().provideErrorFeedback (componente);
	      }
	  }
	  
	  public void keyTyped (KeyEvent e)
	  {
	    char ch = e.getKeyChar ();
	    boolean eventosEhInclusao = ch != '\t' && ch != '\n' && ch != '\010' && ch != '\u007f' && ch != '%' && ch != '\'';
	    int tamanhoTextoAtual = componente.getText ().length ();
	    if (tamanhoTextoAtual >= nrMaxCaracteres && eventosEhInclusao && componente.getSelectedText () == null)
	      {
		e.setKeyChar ('\uffff');
		e.consume ();
		UIManager.getLookAndFeel ().provideErrorFeedback (componente);
	      }
	  }
	});
      }
    add (getComponenteEditor (), "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  protected void informacaoModificada ()
  {
    ((PPGDTextArea) componente).setInformacao (getInformacao ());
    implementacaoPropertyChange (null);
  }
  
  public void setEstiloFonte (int estilo)
  {
    Font f = componente.getFont ();
    f = f.deriveFont (estilo);
    componente.setFont (f);
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    Font f = componente.getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    componente.setFont (f);
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
}
