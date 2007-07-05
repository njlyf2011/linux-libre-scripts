/* JEditMascara - Decompiled by JODE
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
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

import serpro.ppgd.gui.editors.PPGDFormattedTextField;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.util.UtilitariosString;

public class JEditMascara extends JEditCampo
{
  protected JFormattedTextField componente;
  private MaskFormatter formatador;
  private static Alfa vazio = new Alfa ("EditMascara");
  private String mascara;
  private boolean selecionaTextoOnFocusGained = true;
  private boolean sobrescreve = false;
  
  public JEditMascara ()
  {
    super (vazio);
  }
  
  public JEditMascara (Informacao aInfo)
  {
    super (aInfo);
  }
  
  public JEditMascara (String aMascara)
  {
    setMascara (aMascara);
  }
  
  public JEditMascara (Informacao aInfo, String aMascara)
  {
    super (aInfo);
    setMascara (aMascara);
  }
  
  public void setMascara (String aMascara)
  {
    mascara = aMascara;
    aplicaMascara ();
  }
  
  public String getMascara ()
  {
    return mascara;
  }
  
  private void aplicaMascara ()
  {
    try
      {
	if (formatador == null)
	  {
	    formatador = new MaskFormatter (mascara);
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
  
  protected void informacaoModificada ()
  {
    ((PPGDFormattedTextField) componente).setInformacao (getInformacao ());
    implementacaoPropertyChange (null);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (campo != null)
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
  
  public void setarCampo ()
  {
    String s = componente.getText ();
    String valorSemMascara = UtilitariosString.retiraMascara (s);
    int tamMask = getMascara ().length ();
    if (s.length () > tamMask)
      s = s.substring (0, tamMask);
    getInformacao ().setConteudo (valorSemMascara);
  }
  
  public JComponent getComponenteFoco ()
  {
    return componente;
  }
  
  public boolean isSelecionaTextoOnFocusGained ()
  {
    return selecionaTextoOnFocusGained;
  }
  
  public void setSelecionaTextoOnFocusGained (boolean selecionaTextoOnFocusGained)
  {
    this.selecionaTextoOnFocusGained = selecionaTextoOnFocusGained;
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
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (componente == null)
      {
	componente = new PPGDFormattedTextField ();
	if (getMascara () == null)
	  {
	    setMascara ("********");
	    aplicaMascara ();
	  }
	int tamMask = getMascara ().length ();
	componente.setHorizontalAlignment (0);
	componente.addFocusListener (new FocusAdapter ()
	{
	  public void focusGained (FocusEvent e)
	  {
	    if (isSelecionaTextoOnFocusGained ())
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
		JEditMascara.this.setIdentificacaoFoco (false);
		if (JEditMascara.this.getInformacao () != null)
		  {
		    if (JEditMascara.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || JEditMascara.this.verificaValidacoesImpeditivas (componente.getText ()))
		      {
			setarCampo ();
			JEditMascara.this.chamaValidacao ();
		      }
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
    add (componente, "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    getComponenteEditor ().setEnabled (habilitado);
    javax.swing.JLabel rotulo = getRotulo ();
    if (rotulo != null)
      getRotulo ().setEnabled (habilitado);
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
  
  public String getConteudoComMascara ()
  {
    String valorSemMascara = UtilitariosString.retiraMascara (getInformacao ().getConteudoFormatado ());
    return UtilitariosString.retornaComMascara (valorSemMascara, formatador.getMask ());
  }
  
  protected void trataEventoKeyTyped (KeyEvent e)
  {
    boolean manipulacaoCursor = e.getKeyChar () == '\t' || e.getKeyChar () == '\n' || e.getKeyChar () == '\010' || e.getKeyChar () == '\u007f' || e.getKeyChar () == '%' || e.getKeyChar () == '\'';
    if (getFormatador () != null && getFormatador ().getValidCharacters () != null && getFormatador ().getValidCharacters ().indexOf (" ") == -1 && e.getKeyChar () == ' ')
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
	if (sobrescreve && getMascara () != null && componente.getCaret ().getDot () <= getMascara ().length () && eventosEhInclusao && tamanhoTextoAtual >= getMascara ().length () && componente.getSelectedText () == null)
	  componente.select (componente.getCaret ().getDot (), componente.getCaret ().getDot () + 1);
	if (getMascara () != null && tamanhoTextoAtual >= getMascara ().length () && eventosEhInclusao && componente.getSelectedText () == null)
	  {
	    e.setKeyChar ('\uffff');
	    e.consume ();
	    UIManager.getLookAndFeel ().provideErrorFeedback (componente);
	  }
      }
  }
}
