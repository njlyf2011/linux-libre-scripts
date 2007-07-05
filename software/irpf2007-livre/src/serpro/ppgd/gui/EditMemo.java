/* EditMemo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Dimension;
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

public class EditMemo extends EditCampo
{
  private static Alfa vazio = new Alfa ("EditMemo");
  private JTextArea componente;
  private JScrollPane sp;
  private int nrMaxCaracteres;
  
  public EditMemo ()
  {
    super (vazio);
  }
  
  public EditMemo (Informacao campo, Dimension d, int nrMaxCaracteres)
  {
    super (campo, d);
    this.nrMaxCaracteres = nrMaxCaracteres;
  }
  
  public void setInformacao (Informacao campo)
  {
    if (componente == null)
      {
	componente = new PPGDTextArea (d[0].height, d[0].width);
	sp = new JScrollPane (componente);
	componente.setLineWrap (true);
	componente.setWrapStyleWord (true);
	componente.setTabSize (1);
	componente.addFocusListener (new FocusAdapter ()
	{
	  public void focusLost (FocusEvent e)
	  {
	    if (e.getOppositeComponent () != null)
	      {
		EditMemo.this.setIdentificacaoFoco (false);
		if (EditMemo.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || EditMemo.this.verificaValidacoesImpeditivas (componente.getText ()))
		  {
		    EditMemo.this.setObservadorAtivo (false);
		    String s = componente.getText ();
		    if (s.length () > nrMaxCaracteres)
		      s = s.substring (0, nrMaxCaracteres);
		    EditMemo.this.getInformacao ().setConteudo (s);
		    EditMemo.this.chamaValidacao ();
		    EditMemo.this.setObservadorAtivo (true);
		  }
	      }
	  }
	});
	componente.addKeyListener (new KeyAdapter ()
	{
	  public void keyPressed (KeyEvent e)
	  {
	    char ch = e.getKeyChar ();
	    boolean eventosEhInclusao = ch != '\uffff' && ch != '\010' && ch != '\u007f' && ch != '%' && ch != '\'' || ch == '\021';
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
    ((PPGDTextArea) componente).setInformacao (getInformacao ());
    setMaxCaracteres (((Alfa) campo).getMaximoCaracteres ());
    implementacaoPropertyChange (null);
  }
  
  public JComponent getComponenteEditor ()
  {
    return sp;
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
    labelCampo.setEnabled (habilitado);
  }
  
  public JComponent getComponenteFoco ()
  {
    return componente;
  }
  
  public void setMaxCaracteres (int nrMaxCaracteres)
  {
    this.nrMaxCaracteres = nrMaxCaracteres;
  }
  
  public void setPerdeFocoComEnter (boolean isPerdeFocoComEnter)
  {
    /* empty */
  }
}
