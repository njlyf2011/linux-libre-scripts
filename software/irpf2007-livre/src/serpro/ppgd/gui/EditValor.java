/* EditValor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import serpro.ppgd.gui.editors.PPGDFormattedTextField;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.UtilitariosString;

public class EditValor extends EditCampo
{
  private static Valor vazio = new Valor (null, "EditValor");
  private static String maskara = "###.###.##0,00";
  protected JFormattedTextField componente;
  protected boolean aceitaNumerosNegativos = false;
  
  public EditValor ()
  {
    this (vazio);
  }
  
  public EditValor (Informacao campo)
  {
    super (campo, maskara.length ());
  }
  
  protected EditValor (Informacao pInfo, int pLength)
  {
    super (pInfo, pLength);
  }
  
  public boolean isAceitaNumerosNegativos ()
  {
    return aceitaNumerosNegativos;
  }
  
  public void setAceitaNumerosNegativos (boolean aceitaNumerosNegativos)
  {
    this.aceitaNumerosNegativos = aceitaNumerosNegativos;
  }
  
  public void setInformacao (Informacao campo)
  {
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
	    if (EditValor.this.isSelecionaTextoOnFocusGained ())
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
		EditValor.this.setIdentificacaoFoco (false);
		if (EditValor.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || EditValor.this.verificaValidacoesImpeditivas (componente.getText ()))
		  {
		    EditValor.this.getInformacao ().setConteudo (componente.getText ());
		    componente.setText (EditValor.this.getInformacao ().getConteudoFormatado ());
		    EditValor.this.chamaValidacao ();
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
    ((PPGDFormattedTextField) componente).setInformacao (getInformacao ());
    implementacaoPropertyChange (null);
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
