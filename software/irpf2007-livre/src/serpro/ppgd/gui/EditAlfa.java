/* EditAlfa - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import serpro.ppgd.gui.editors.PPGDTextField;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;

public class EditAlfa extends EditCampo
{
  private static Alfa vazio = new Alfa ("EditAlfa");
  private JTextField componente;
  
  public EditAlfa ()
  {
    super (vazio);
  }
  
  public EditAlfa (Informacao campo, int tamanho)
  {
    super (campo, tamanho);
  }
  
  public void setInformacao (Informacao campo)
  {
    if (componente == null)
      {
	componente = new PPGDTextField ();
	componente.addFocusListener (new FocusAdapter ()
	{
	  public void focusLost (FocusEvent e)
	  {
	    if (e.getOppositeComponent () != null)
	      {
		EditAlfa.this.setIdentificacaoFoco (false);
		if (EditAlfa.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || EditAlfa.this.verificaValidacoesImpeditivas (componente.getText ()))
		  setarCampo ();
	      }
	  }
	  
	  public void focusGained (FocusEvent e)
	  {
	    if (EditAlfa.this.isSelecionaTextoOnFocusGained ())
	      SwingUtilities.invokeLater (new Runnable ()
	      {
		public void run ()
		{
		  componente.selectAll ();
		}
	      });
	  }
	});
	componente.addActionListener (new ActionListener ()
	{
	  public void actionPerformed (ActionEvent e)
	  {
	    setarCampo ();
	  }
	});
	componente.addKeyListener (new KeyAdapter ()
	{
	  public void keyTyped (KeyEvent e)
	  {
	    char ch = e.getKeyChar ();
	    boolean eventosEhInclusao = ch != '\t' && ch != '\n' && ch != '\010' && ch != '\u007f' && ch != '%' && ch != '\'';
	    if (ch == '\'')
	      eventosEhInclusao = true;
	    int tamanhoTextoAtual = componente.getText ().length ();
	    if (tamanhoTextoAtual >= d[0].width && eventosEhInclusao && componente.getSelectedText () == null)
	      {
		e.setKeyChar ('\uffff');
		e.consume ();
		UIManager.getLookAndFeel ().provideErrorFeedback (componente);
	      }
	  }
	});
      }
    ((PPGDTextField) componente).setInformacao (campo);
    setTamanho (((Alfa) campo).getMaximoCaracteres ());
    implementacaoPropertyChange (null);
  }
  
  public void setarCampo ()
  {
    String s = componente.getText ();
    if (s.length () > d[0].width)
      s = s.substring (0, d[0].width);
    getInformacao ().setConteudo (s);
    chamaValidacao ();
  }
  
  public JComponent getComponenteEditor ()
  {
    return componente;
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    componente.setText (getInformacao ().getConteudoFormatado ());
    componente.setCaretPosition (0);
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
  
  public void setPerdeFocoComEnter (boolean aPerdeFocoComEnter)
  {
    isPerdeFocoComEnter = aPerdeFocoComEnter;
    if (isPerdeFocoComEnter ())
      aplicaTransfereFocoEnter ();
    else
      removeTransfereFocoEnter ();
  }
}
