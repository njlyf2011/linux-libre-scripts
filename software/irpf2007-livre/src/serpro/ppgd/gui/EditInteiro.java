/* EditInteiro - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import serpro.ppgd.gui.editors.PPGDSpinner;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Inteiro;

public class EditInteiro extends EditCampo
{
  private static Inteiro vazio = new Inteiro ("EditInteiro");
  private JSpinner componente;
  private SpinnerNumberModel model;
  int valorInicial;
  int valorMinimo;
  int valorMaximo;
  int valorStep;
  
  public EditInteiro ()
  {
    super (vazio);
  }
  
  public EditInteiro (Informacao campo)
  {
    super (campo);
  }
  
  public void setInformacao (Informacao campo)
  {
    if (componente == null)
      {
	model = new SpinnerNumberModel (0, 0, 10000, 1);
	componente = new PPGDSpinner (model);
	model.addChangeListener (new ChangeListener ()
	{
	  public void stateChanged (ChangeEvent event)
	  {
	    EditInteiro.this.setIdentificacaoFoco (false);
	    Integer value = (Integer) componente.getValue ();
	    EditInteiro.this.getInformacao ().setConteudo (value.toString ());
	  }
	});
	((JSpinner.DefaultEditor) componente.getEditor ()).getTextField ().addFocusListener (new FocusListener ()
	{
	  public void focusGained (FocusEvent e)
	  {
	    /* empty */
	  }
	  
	  public void focusLost (FocusEvent e)
	  {
	    EditInteiro.this.setIdentificacaoFoco (false);
	    String val = ((JSpinner.DefaultEditor) componente.getEditor ()).getTextField ().getText ();
	    EditInteiro.this.getInformacao ().setConteudo (val);
	  }
	});
      }
    Inteiro inteiro = (Inteiro) getInformacao ();
    int valor = inteiro.asInteger ();
    componente.setValue (new Integer (valor));
    ((PPGDSpinner) componente).setInformacao (campo);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    getInformacao ().setObservadoresAtivos (false);
    setObservadorAtivo (false);
    componente.setValue (new Integer (((Inteiro) getInformacao ()).asInteger ()));
    getInformacao ().setObservadoresAtivos (true);
    setObservadorAtivo (true);
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    if (readOnly)
      componente.setEnabled (false);
    else
      componente.setEnabled (true);
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    componente.setEnabled (habilitado);
    labelCampo.setEnabled (habilitado);
  }
  
  public JComponent getComponenteEditor ()
  {
    return componente;
  }
  
  public void configurarLimites (int valorMinimo, int valorMaximo, int valorStep)
  {
    model.setMaximum (new Integer (valorMaximo));
    model.setMinimum (new Integer (valorMinimo));
    model.setStepSize (new Integer (valorStep));
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
