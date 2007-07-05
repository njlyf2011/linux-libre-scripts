/* JEditInteiro - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import serpro.ppgd.gui.editors.PPGDSpinner;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Inteiro;

public class JEditInteiro extends JEditCampo
{
  private JSpinner componente;
  private SpinnerNumberModel model;
  
  public JEditInteiro ()
  {
    super (new Inteiro ("Inteiro"));
  }
  
  public JEditInteiro (Informacao campo)
  {
    super (campo);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (evt != null && evt.getPropertyName () != null && evt.getPropertyName ().equals ("LIMITES"))
      {
	if (getInformacao () instanceof Inteiro)
	  {
	    int limiteMax = ((Inteiro) getInformacao ()).getLimiteMaximo ();
	    int limiteMin = ((Inteiro) getInformacao ()).getLimiteMinimo ();
	    if (limiteMax != -1)
	      setValorMaximo (limiteMax);
	    if (limiteMin != -1)
	      setValorMinimo (limiteMin);
	  }
      }
    else
      {
	getInformacao ().setObservadoresAtivos (false);
	setObservadorAtivo (false);
	Integer newValue = new Integer (((Inteiro) getInformacao ()).asInteger ());
	Integer maxValue = (Integer) model.getMaximum ();
	Integer minValue = (Integer) model.getMinimum ();
	if (newValue.intValue () > maxValue.intValue ())
	  newValue = maxValue;
	else if (newValue.intValue () < minValue.intValue ())
	  newValue = minValue;
	componente.setValue (newValue);
	getInformacao ().setObservadoresAtivos (true);
	setObservadorAtivo (true);
      }
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
  }
  
  public JComponent getComponenteEditor ()
  {
    return componente;
  }
  
  public void setValorMinimo (int aValorMinimo)
  {
    model.setMinimum (new Integer (aValorMinimo));
    implementacaoPropertyChange (null);
  }
  
  public int getValorMinimo ()
  {
    return Integer.parseInt (model.getMinimum ().toString ());
  }
  
  public void setValorMaximo (int aValorMax)
  {
    model.setMaximum (new Integer (aValorMax));
    implementacaoPropertyChange (null);
  }
  
  public int getValorMaximo ()
  {
    return Integer.parseInt (model.getMaximum ().toString ());
  }
  
  public void setValorOffset (int aOffset)
  {
    model.setStepSize (new Integer (aOffset));
  }
  
  public int getValorOffset ()
  {
    return model.getStepSize ().intValue ();
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
	model = new SpinnerNumberModel (0, 0, 10000, 1);
	componente = new PPGDSpinner (model);
	model.addChangeListener (new ChangeListener ()
	{
	  public void stateChanged (ChangeEvent event)
	  {
	    JEditInteiro.this.setIdentificacaoFoco (false);
	    Integer value = (Integer) componente.getValue ();
	    Integer newValue = value;
	    Integer maxValue = (Integer) model.getMaximum ();
	    Integer minValue = (Integer) model.getMinimum ();
	    if (newValue.intValue () > maxValue.intValue ())
	      newValue = maxValue;
	    else if (newValue.intValue () < minValue.intValue ())
	      newValue = minValue;
	    JEditInteiro.this.getInformacao ().setConteudo (newValue.toString ());
	  }
	});
      }
    add (componente, "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  protected void informacaoModificada ()
  {
    Inteiro inteiro = (Inteiro) getInformacao ();
    int valor = inteiro.asInteger ();
    componente.setValue (new Integer (valor));
    ((PPGDSpinner) componente).setInformacao (campo);
    if (getInformacao () instanceof Inteiro)
      {
	int limiteMax = ((Inteiro) getInformacao ()).getLimiteMaximo ();
	int limiteMin = ((Inteiro) getInformacao ()).getLimiteMinimo ();
	if (limiteMax != -1)
	  setValorMaximo (limiteMax);
	if (limiteMin != -1)
	  setValorMinimo (limiteMin);
      }
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
