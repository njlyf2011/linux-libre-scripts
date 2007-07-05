/* JEditValor - Decompiled by JODE
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
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.editors.PPGDFormattedTextField;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.UtilitariosString;

public class JEditValor extends JEditCampo
{
  private static Valor vazio = new Valor (null, "EditValor");
  private static String maskara = "###.###.##0,00";
  protected JFormattedTextField componente;
  protected boolean aceitaNumerosNegativos = false;
  
  public JEditValor ()
  {
    super (new Valor ());
  }
  
  public JEditValor (Informacao campo)
  {
    super (campo);
  }
  
  public boolean isAceitaNumerosNegativos ()
  {
    return aceitaNumerosNegativos;
  }
  
  public void setAceitaNumerosNegativos (boolean aceitaNumerosNegativos)
  {
    this.aceitaNumerosNegativos = aceitaNumerosNegativos;
  }
  
  protected void informacaoModificada ()
  {
    ((PPGDFormattedTextField) componente).setInformacao (getInformacao ());
    implementacaoPropertyChange (null);
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (componente == null)
      {
	componente = new PPGDFormattedTextField ();
	componente.setHorizontalAlignment (11);
	componente.setFont (ConstantesGlobaisGUI.FONTE_9_NORMAL);
	componente.setHorizontalAlignment (4);
	componente.addFocusListener (new FocusAdapter ()
	{
	  public void focusGained (FocusEvent e)
	  {
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
		JEditValor.this.setIdentificacaoFoco (false);
		if (JEditValor.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || JEditValor.this.verificaValidacoesImpeditivas (componente.getText ()))
		  {
		    JEditValor.this.getInformacao ().setConteudo (componente.getText ());
		    componente.setText (JEditValor.this.getInformacao ().getConteudoFormatado ());
		    JEditValor.this.chamaValidacao ();
		  }
	      }
	  }
	});
	componente.addKeyListener (new KeyAdapter ()
	{
	  public void keyTyped (KeyEvent ev)
	  {
	    trataEventoKeyTyped (ev);
	  }
	});
      }
    add (getComponenteEditor (), "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  protected void trataEventoKeyTyped (KeyEvent e)
  {
    boolean manipulacaoCursor = (e.getKeyChar () == '\t' || e.getKeyChar () == '\n' || e.getKeyChar () == '\010' || e.getKeyChar () == '\u007f' || e.getKeyChar () == '%' || e.getKeyChar () == '\'') && ! String.valueOf (e.getKeyChar ()).equals ("%") && ! String.valueOf (e.getKeyChar ()).equals ("'");
    if (! manipulacaoCursor)
      {
	String text = componente.getText ();
	int tamanhoTextoAtual = UtilitariosString.retiraMascara (text).length ();
	boolean fracaoGrande = false;
	Valor info = (Valor) getInformacao ();
	int pos = text.indexOf (',');
	int tamanhoMaximo;
	if (pos != -1)
	  {
	    tamanhoMaximo = info.getMaximoDigitosParteInteira () + info.getCasasDecimais ();
	    String s = text.substring (pos + 1);
	    if (s.length () == info.getCasasDecimais () && componente.getCaretPosition () > pos && componente.getSelectedText () == null)
	      fracaoGrande = true;
	  }
	else
	  tamanhoMaximo = info.getMaximoDigitosParteInteira ();
	if (e.getKeyChar () == ',')
	  tamanhoMaximo = info.getMaximoDigitosParteInteira () + info.getCasasDecimais ();
	char ch = e.getKeyChar ();
	boolean eventosEhInclusao = ch != '\t' && ch != '\n' && ch != '\010' && ch != '\u007f' && ch != '%' && ch != '\'';
	boolean jaExisteUmSinalNeg = false;
	if (componente.getText ().trim ().indexOf ("-") != -1)
	  jaExisteUmSinalNeg = true;
	boolean jaExisteVirgula = false;
	if (componente.getText ().trim ().indexOf (",") != -1)
	  jaExisteVirgula = true;
	boolean naoEhDigitoVirgulaPontoOuSinalNegativo = ! Character.isDigit (e.getKeyChar ()) && e.getKeyChar () != '-' && e.getKeyChar () != '.' && e.getKeyChar () != ',' && e.getKeyChar () != ' ';
	if (tamanhoTextoAtual >= tamanhoMaximo && eventosEhInclusao && componente.getSelectedText () == null || e.getKeyChar () == '-' && ! aceitaNumerosNegativos || Character.isLetter (e.getKeyChar ()) || fracaoGrande || e.getKeyChar () == '-' && jaExisteUmSinalNeg || jaExisteUmSinalNeg && tamanhoTextoAtual + 1 >= tamanhoMaximo || e.getKeyChar () == '\'' || e.getKeyChar () == '\"' || String.valueOf (e.getKeyChar ()).equals ("\u00b4") || String.valueOf (e.getKeyChar ()).equals ("`") || String.valueOf (e.getKeyChar ()).equals ("^") || String.valueOf (e.getKeyChar ()).equals ("@") || String.valueOf (e.getKeyChar ()).equals ("~") || String.valueOf (e.getKeyChar ()).equals ("'") || String.valueOf (e.getKeyChar ()).equals ("%") || e.getKeyChar () == ',' && jaExisteVirgula || e.getKeyChar () == ',' && info.getCasasDecimais () == 0 || naoEhDigitoVirgulaPontoOuSinalNegativo)
	  {
	    e.setKeyChar ('\uffff');
	    e.consume ();
	    UIManager.getLookAndFeel ().provideErrorFeedback (componente);
	  }
      }
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    componente.setText (getInformacao ().getConteudoFormatado ());
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
    getComponenteEditor ().setEnabled (habilitado);
    javax.swing.JLabel rotulo = getRotulo ();
    if (rotulo != null)
      getRotulo ().setEnabled (habilitado);
  }
  
  public JComponent getComponenteFoco ()
  {
    return componente;
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
  
  public void setAceitaFoco (boolean aAceita)
  {
    getComponenteEditor ().setFocusable (aAceita);
  }
  
  public boolean isAceitaFoco ()
  {
    return getComponenteEditor ().isFocusable ();
  }
}
