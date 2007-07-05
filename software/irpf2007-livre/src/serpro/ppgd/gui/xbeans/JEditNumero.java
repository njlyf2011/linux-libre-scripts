/* JEditNumero - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.Font;
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
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Inteiro;

public class JEditNumero extends JEditCampo
{
  private JTextField componente;
  
  public JEditNumero ()
  {
    super (new Inteiro ());
  }
  
  public JEditNumero (Informacao campo)
  {
    super (campo);
  }
  
  protected void informacaoModificada ()
  {
    ((PPGDTextField) componente).setInformacao (campo);
    implementacaoPropertyChange (null);
  }
  
  public void setarCampo ()
  {
    String s = componente.getText ();
    int limiteMax = ((Inteiro) getInformacao ()).getLimiteMaximo ();
    if (limiteMax != -1 && Integer.parseInt (s) > limiteMax)
      s = "" + limiteMax;
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
    getComponenteEditor ().setEnabled (habilitado);
    javax.swing.JLabel rotulo = getRotulo ();
    if (rotulo != null)
      getRotulo ().setEnabled (habilitado);
  }
  
  public JComponent getComponenteFoco ()
  {
    return componente;
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    if (componente == null)
      {
	componente = new PPGDTextField ();
	componente.setColumns (6);
	componente.setHorizontalAlignment (4);
	componente.addFocusListener (new FocusAdapter ()
	{
	  public void focusLost (FocusEvent e)
	  {
	    if (e.getOppositeComponent () != null)
	      {
		JEditNumero.this.setIdentificacaoFoco (false);
		if (JEditNumero.this.getInformacao ().getListaValidadoresImpeditivos ().isEmpty () || JEditNumero.this.verificaValidacoesImpeditivas (componente.getText ()))
		  setarCampo ();
	      }
	  }
	  
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
	    boolean ehCharValido = Character.isDigit (ch) || ch == '\t' || ch == '\n' || ch == '\010' || ch == '\u007f' || ch == '%' || ch == '\'';
	    if (! ehCharValido)
	      {
		e.setKeyChar ('\uffff');
		e.consume ();
		UIManager.getLookAndFeel ().provideErrorFeedback (componente);
	      }
	  }
	});
      }
    add (componente, "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
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
