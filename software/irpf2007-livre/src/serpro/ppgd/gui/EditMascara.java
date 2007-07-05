/* EditMascara - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import serpro.ppgd.gui.editors.PPGDFormattedTextField;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.util.UtilitariosString;

public class EditMascara extends EditCampo
{
  protected boolean retornaConteudoAposTeclaEsc = true;
  private static Alfa vazio = new Alfa ("EditMascara");
  protected JFormattedTextField componente;
  private MaskFormatter formatador;
  private boolean sobrescreve = false;
  private String mascara;
  
  public EditMascara ()
  {
    super (vazio);
  }
  
  public EditMascara (Informacao campo)
  {
    super (campo);
  }
  
  public EditMascara (Informacao campo, int tamanho)
  {
    super (campo, tamanho);
  }
  
  public void setMascara (String mascara)
  {
    try
      {
	this.mascara = mascara;
	if (formatador == null)
	  {
	    formatador = new MaskFormatter (this.mascara);
	    componente.setFormatterFactory (new DefaultFormatterFactory (formatador));
	  }
	else
	  formatador.setMask (mascara);
	componente.setValue (null);
      }
    catch (ParseException e)
      {
	throw new IllegalArgumentException (e.getMessage ());
      }
    implementacaoPropertyChange (null);
  }
  
  public void setCaracteresValidos (String caracteresValidos)
  {
    formatador.setValidCharacters (caracteresValidos);
  }
  
  public void setCaracteresInvalidos (String charsInvalidos)
  {
    formatador.setInvalidCharacters (charsInvalidos);
  }
  
  public void setInformacao (Informacao campo)
  {
    if (componente == null)
      {
	componente = new PPGDFormattedTextField ();
	setMascara (UtilitariosString.expandeString (d[0].width, "*"));
	componente.setHorizontalAlignment (0);
	componente.addFocusListener (new FocusAdapter ()
	{
	  public void focusGained (FocusEvent e)
	  {
	    if (EditMascara.this.isSelecionaTextoOnFocusGained ())
	      SwingUtilities.invokeLater (new Runnable ()
	      {
		public void run ()
		{
		  componente.selectAll ();
		}
	      });
	  }
	  
	  public void focusLost (FocusEvent e)
	  {
	    if (e.getOppositeComponent () != null)
	      {
		EditMascara.this.setIdentificacaoFoco (false);
		if (EditMascara.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || EditMascara.this.verificaValidacoesImpeditivas (componente.getText ()))
		  {
		    setarCampo ();
		    EditMascara.this.chamaValidacao ();
		  }
	      }
	  }
	});
	componente.addKeyListener (new KeyAdapter ()
	{
	  public void keyTyped (KeyEvent e)
	  {
	    trataEventoKeyTyped (e);
	  }
	});
      }
    ((PPGDFormattedTextField) componente).setInformacao (getInformacao ());
    setMascara (mascara);
    setTamanho (mascara.length ());
    implementacaoPropertyChange (null);
  }
  
  public void removeTransfereFocoEnter ()
  {
    componente.getInputMap ().put (KeyStroke.getKeyStroke (10, 0, true), "Focus.nextComponent");
    componente.getActionMap ().put ("Focus.nextComponent", null);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    String valorSemMascara = campo.asString ();
    String valorComMascara = UtilitariosString.retornaComMascara (valorSemMascara, getFormatador ().getMask ());
    if (valorSemMascara.trim ().equals (""))
      {
	((PPGDFormattedTextField) componente).setText (valorComMascara);
	((PPGDFormattedTextField) componente).setValue (null);
      }
    else
      ((PPGDFormattedTextField) componente).setText (valorComMascara);
    componente.setCaretPosition (0);
  }
  
  public JComponent getComponenteEditor ()
  {
    return componente;
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
  
  public void setarCampo ()
  {
    String s = componente.getText ();
    String valorSemMascara = UtilitariosString.retiraMascara (s, formatador.getMask ());
    if (s.length () > d[0].width)
      s = s.substring (0, d[0].width);
    getInformacao ().setConteudo (valorSemMascara);
  }
  
  public String getConteudoComMascara ()
  {
    String valorSemMascara = UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ());
    return UtilitariosString.retornaComMascara (valorSemMascara, formatador.getMask ());
  }
  
  public JComponent getComponenteFoco ()
  {
    return componente;
  }
  
  public boolean isSobrescreve ()
  {
    return sobrescreve;
  }
  
  public void setSobrescreve (boolean sobrescreve)
  {
    this.sobrescreve = sobrescreve;
  }
  
  public MaskFormatter getFormatador ()
  {
    return formatador;
  }
  
  public void setPerdeFocoComEnter (boolean aPerdeFocoComEnter)
  {
    isPerdeFocoComEnter = aPerdeFocoComEnter;
    if (isPerdeFocoComEnter ())
      aplicaTransfereFocoEnter ();
    else
      removeTransfereFocoEnter ();
  }
  
  public boolean isRetornaConteudoAposTeclaEsc ()
  {
    return retornaConteudoAposTeclaEsc;
  }
  
  public void setRetornaConteudoAposTeclaEsc (boolean retornaConteudoAposTeclaEsc)
  {
    this.retornaConteudoAposTeclaEsc = retornaConteudoAposTeclaEsc;
  }
  
  public String getMascara ()
  {
    return mascara;
  }
  
  protected void trataEventoKeyTyped (KeyEvent e)
  {
    boolean manipulacaoCursor = e.getKeyChar () == '\t' || e.getKeyChar () == '\n' || e.getKeyChar () == '\010' || e.getKeyChar () == '\u007f' || e.getKeyChar () == '%' || e.getKeyChar () == '\'';
    if (getFormatador ().getValidCharacters () != null && getFormatador ().getValidCharacters ().indexOf (" ") == -1 && e.getKeyChar () == ' ')
      {
	e.setKeyChar ('\uffff');
	e.consume ();
	UIManager.getLookAndFeel ().provideErrorFeedback (componente);
      }
    else if (! manipulacaoCursor)
      {
	char ch = e.getKeyChar ();
	boolean eventosEhInclusao = ch != '\t' && ch != '\n' && ch != '\010' && ch != '\u007f' && ch != '%' && ch != '\'';
	int tamanhoTextoAtual = componente.getText ().trim ().length ();
	if (sobrescreve && componente.getCaret ().getDot () <= d[0].width && eventosEhInclusao && tamanhoTextoAtual >= d[0].width && componente.getSelectedText () == null)
	  componente.select (componente.getCaret ().getDot (), componente.getCaret ().getDot () + 1);
	if (tamanhoTextoAtual >= d[0].width && eventosEhInclusao && componente.getSelectedText () == null)
	  {
	    e.setKeyChar ('\uffff');
	    e.consume ();
	    UIManager.getLookAndFeel ().provideErrorFeedback (componente);
	  }
      }
  }
}
