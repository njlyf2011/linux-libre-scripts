/* EditComboElementoTabela - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.util.LogPPGD;

public class EditComboElementoTabela extends EditCampo
{
  private static Codigo vazio = new Codigo ("EditCombo");
  private ComboElementoTabela combo;
  private EditCodigoAntigo editCodigo;
  private EditAlfa editAlfa;
  private String lastCodigo;
  
  public EditComboElementoTabela ()
  {
    super (vazio);
  }
  
  public EditComboElementoTabela (Informacao campo, int tamanho, String idAjuda)
  {
    super (campo, tamanho, idAjuda);
  }
  
  private String getConteudoAtual (int coluna)
  {
    int i = combo.getSelectedIndex ();
    if (i != -1)
      {
	List colecao = ((Codigo) getInformacao ()).getColecaoElementoTabela ();
	ElementoTabela elementoTabela = (ElementoTabela) colecao.get (i);
	return elementoTabela.getConteudo (coluna);
      }
    return "";
  }
  
  public void setInformacao (Informacao campo)
  {
    this.campo = campo;
    boolean isSimples = ((Codigo) campo).isSimples ();
    int colunaFiltro = ((Codigo) campo).getColunaFiltro ();
    if (combo == null)
      {
	Vector colecao = (Vector) ((Codigo) getInformacao ()).getColecaoElementoTabela ();
	if (isSimples)
	  combo = new ComboElementoSimples (colecao, colunaFiltro);
	else
	  combo = new ComboElementoTabela (colecao, colunaFiltro);
	combo.setSizeCol (0, d[0].width);
	combo.addPopupMenuListener (new PopupMenuListener ()
	{
	  public void popupMenuWillBecomeInvisible (PopupMenuEvent e)
	  {
	    EditComboElementoTabela.this.setarConteudoEditado ();
	  }
	  
	  public void popupMenuWillBecomeVisible (PopupMenuEvent e)
	  {
	    lastCodigo = EditComboElementoTabela.this.getInformacao ().getConteudoFormatado ();
	  }
	  
	  public void popupMenuCanceled (PopupMenuEvent e)
	  {
	    /* empty */
	  }
	});
	combo.addFocusListener (new FocusAdapter ()
	{
	  public void focusLost (FocusEvent e)
	  {
	    setIdentificacaoFoco (false);
	  }
	});
	combo.addKeyListener (new KeyAdapter ()
	{
	  public void keyReleased (KeyEvent e)
	  {
	    super.keyReleased (e);
	    EditComboElementoTabela.this.setarConteudoEditado ();
	    if (e.getKeyCode () == 27 && (lastCodigo == null || lastCodigo.length () == 0))
	      {
		combo.setSelectedIndex (-1);
		EditComboElementoTabela.this.setObservadorAtivo (false);
		if (editAlfa != null)
		  {
		    ((JTextField) editAlfa.getComponenteEditor ()).setText (EditComboElementoTabela.this.getConteudoAtual (1));
		    ((JTextField) editAlfa.getComponenteEditor ()).setCaretPosition (0);
		  }
		EditComboElementoTabela.this.getInformacao ().setConteudo (EditComboElementoTabela.this.getConteudoAtual (0));
		EditComboElementoTabela.this.setObservadorAtivo (true);
	      }
	  }
	});
      }
    Vector colecao = (Vector) ((Codigo) getInformacao ()).getColecaoElementoTabela ();
    if (colecao != combo.getColecao ())
      combo.setColecao (colecao);
    combo.setSelectedIndex (((Codigo) campo).getIndiceElementoTabela ());
    if (editAlfa != null)
      {
	((JTextField) editAlfa.getComponenteEditor ()).setText (getConteudoAtual (1));
	((JTextField) editAlfa.getComponenteEditor ()).setCaretPosition (0);
      }
  }
  
  private void setarConteudoEditado ()
  {
    setObservadorAtivo (false);
    getInformacao ().setConteudo (getConteudoAtual (0));
    if (editAlfa != null)
      {
	((JTextField) editAlfa.getComponenteEditor ()).setText (getConteudoAtual (1));
	((JTextField) editAlfa.getComponenteEditor ()).setCaretPosition (0);
      }
    chamaValidacao ();
    setObservadorAtivo (true);
  }
  
  public void setEditAlfa (EditAlfa editAlfa)
  {
    this.editAlfa = editAlfa;
    ((JTextField) editAlfa.getComponenteEditor ()).setText (getConteudoAtual (1));
    ((JTextField) editAlfa.getComponenteEditor ()).setCaretPosition (0);
  }
  
  public void setEditCodigo (EditCodigoAntigo edit)
  {
    editCodigo = edit;
  }
  
  public JComponent getComponenteEditor ()
  {
    return combo;
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    if (((Codigo) campo).isVazio () && editAlfa != null)
      {
	combo.setSelectedIndex (-1);
	((JTextField) editAlfa.getComponenteEditor ()).setText ("");
	((JTextField) editAlfa.getComponenteEditor ()).setCaretPosition (0);
      }
    else if (((Codigo) campo).isVazio () && editAlfa == null)
      combo.setSelectedIndex (-1);
    else
      {
	try
	  {
	    if (combo.getItemCount () > ((Codigo) campo).getIndiceElementoTabela ())
	      {
		combo.setSelectedIndex (((Codigo) campo).getIndiceElementoTabela ());
		if (editAlfa != null)
		  {
		    ((JTextField) editAlfa.getComponenteEditor ()).setText (getConteudoAtual (1));
		    ((JTextField) editAlfa.getComponenteEditor ()).setCaretPosition (0);
		  }
	      }
	  }
	catch (Exception e)
	  {
	    LogPPGD.erro ("---Erro no implementacao property change do combo----");
	    e.printStackTrace ();
	    combo.setSelectedIndex (-1);
	  }
      }
  }
  
  public void setReadOnly (boolean b)
  {
    getComponenteEditor ().setEnabled (! b);
  }
  
  public JComponent getComponenteFoco ()
  {
    return combo;
  }
  
  public void setIdentificacaoFoco (boolean status)
  {
    super.setIdentificacaoFoco (status);
    if (editCodigo != null)
      editCodigo.setIdentificacaoFoco (status);
  }
  
  public ComboElementoTabela getCombo ()
  {
    return combo;
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    /* empty */
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    /* empty */
  }
  
  public void setPerdeFocoComEnter (boolean isPerdeFocoComEnter)
  {
    /* empty */
  }
}
