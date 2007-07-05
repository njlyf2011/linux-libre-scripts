/* EditCodigoAntigo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JTextField;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Informacao;

public class EditCodigoAntigo extends EditCampo
{
  private static Codigo vazio = new Codigo ("EditCodigo");
  private BoxPPGDComponente box;
  private EditComboElementoTabela editCombo;
  private EditAlfa editAlfa;
  
  public EditCodigoAntigo ()
  {
    super (vazio);
    getInformacao ().getObservadores ().addPropertyChangeListener ("ComboReiniciado", this);
  }
  
  public EditCodigoAntigo (Informacao campo, Dimension[] d, String idAjuda)
  {
    super (campo, d, idAjuda);
    if (d.length < 1)
      throw new IllegalStateException ("O array d[] deve ter pelo menos um elemento");
    getInformacao ().getObservadores ().addPropertyChangeListener ("ComboReiniciado", this);
  }
  
  public void setInformacao (Informacao campo)
  {
    boolean isSimples = ((Codigo) campo).isSimples ();
    if (editCombo == null || editCombo.getInformacao ().equals (vazio))
      {
	box = new BoxPPGDComponente (0, 0.0F, (byte) 0);
	editCombo = new EditComboElementoTabela (campo, d[0].width, getIdAjuda ());
	editCombo.setEditCodigo (this);
	editCombo.setValidar (false);
	box.addCampo (editCombo, getIdAjuda ());
	if (d.length > 1 && ! isSimples)
	  {
	    editAlfa = new EditAlfa (new Alfa (), d[1].width);
	    editAlfa.getComponenteEditor ().setFocusable (false);
	    editCombo.setEditAlfa (editAlfa);
	    ((JTextField) editAlfa.getComponenteEditor ()).setEditable (false);
	    box.addCampo (editAlfa, getIdAjuda ());
	    d[0].width = d[0].width + d[1].width;
	  }
      }
    editCombo.setInformacao (campo);
  }
  
  public JComponent getComponenteEditor ()
  {
    return box;
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (evt.getPropertyName () != null && evt.getPropertyName ().equals ("ComboReiniciado"))
      setInformacao (getInformacao ());
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    if (readOnly)
      {
	editCombo.setReadOnly (true);
	labelCampo.setEnabled (false);
	if (editAlfa != null)
	  editAlfa.getComponenteEditor ().setEnabled (false);
      }
    else
      {
	editCombo.setReadOnly (false);
	labelCampo.setEnabled (true);
	if (editAlfa != null)
	  editAlfa.getComponenteEditor ().setEnabled (true);
      }
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    editCombo.setReadOnly (! habilitado);
    labelCampo.setEnabled (habilitado);
    if (editAlfa != null)
      editAlfa.getComponenteEditor ().setEnabled (habilitado);
  }
  
  public EditComboElementoTabela getEditComboElementoTabela ()
  {
    return editCombo;
  }
  
  public JComponent getComponenteFoco ()
  {
    return editCombo.getComponenteFoco ();
  }
  
  public void setPerdeFocoComEnter (boolean isPerdeFocoComEnter)
  {
    /* empty */
  }
}
