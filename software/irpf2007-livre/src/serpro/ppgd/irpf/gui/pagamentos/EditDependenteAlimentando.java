/* EditDependenteAlimentando - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.pagamentos;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;

import serpro.ppgd.gui.xbeans.JButtonMensagem;
import serpro.ppgd.gui.xbeans.JEditCampo;
import serpro.ppgd.gui.xbeans.PainelBotao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.alimentandos.Alimentando;
import serpro.ppgd.irpf.dependentes.Dependente;
import serpro.ppgd.irpf.pagamentos.Pagamento;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;

public class EditDependenteAlimentando extends JEditCampo implements ActionListener
{
  private JComboBox cmbNome;
  private Pagamento pagamento;
  private DeclaracaoIRPF declaracaoIRPF;
  
  public EditDependenteAlimentando ()
  {
    super (new Alfa ());
    try
      {
	Informacao informacao = getInformacao ();
	Class[] var_classes = new Class[1];
	int i = 0;
	Class var_class = serpro.ppgd.irpf.gui.pagamentos.EditDependenteAlimentando.class;
	var_classes[i] = var_class;
	informacao.removeObservadores (var_classes);
      }
    catch (ClassNotFoundException classnotfoundexception)
      {
	/* empty */
      }
    if (! PlataformaPPGD.isEmDesign ())
      declaracaoIRPF = IRPFFacade.getInstancia ().getDeclaracao ();
  }
  
  protected void buildComponente ()
  {
    removeAll ();
    setLayout (new BorderLayout ());
    add (cmbNome, "Center");
    JButtonMensagem btnMsg = getButtonMensagem ();
    if (btnMsg != null)
      add (new PainelBotao (btnMsg), "East");
  }
  
  public void setInformacao (Informacao campo)
  {
    if (campo != null && campo.getOwner () != null && campo.getOwner () instanceof Pagamento)
      {
	pagamento = (Pagamento) campo.getOwner ();
	try
	  {
	    pagamento.getCodigo ().removeObservadores (new Class[] { this.getClass () });
	  }
	catch (ClassNotFoundException classnotfoundexception)
	  {
	    /* empty */
	  }
	pagamento.getCodigo ().addObservador (new Observador ()
	{
	  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
	  {
	    implementacaoPropertyChange (null);
	  }
	});
	atualizaModel ();
      }
    super.setInformacao (campo);
  }
  
  protected void instanciaComponentes ()
  {
    cmbNome = new JComboBox ();
    cmbNome.setRenderer (new DefaultListCellRenderer ()
    {
      public Component getListCellRendererComponent (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
      {
	return super.getListCellRendererComponent (list, value, index, isSelected, cellHasFocus);
      }
    });
    cmbNome.setEditable (false);
    cmbNome.addActionListener (this);
  }
  
  public void atualizaModel ()
  {
    if (pagamento != null && declaracaoIRPF != null)
      {
	String codigoPagamento = pagamento.getCodigo ().getConteudoAtual (0);
	if (codigoPagamento.equals ("03") || codigoPagamento.equals ("04"))
	  {
	    cmbNome.setModel (new DefaultComboBoxModel (declaracaoIRPF.getDependentes ().recuperarLista ().toArray ()));
	    return;
	  }
	if (codigoPagamento.equals ("05") || codigoPagamento.equals ("06"))
	  {
	    cmbNome.setModel (new DefaultComboBoxModel (declaracaoIRPF.getAlimentandos ().recuperarLista ().toArray ()));
	    return;
	  }
      }
    cmbNome.setModel (new DefaultComboBoxModel ());
  }
  
  private ObjetoNegocio obtemBeneficiarioSelecionado ()
  {
    if (pagamento != null && declaracaoIRPF != null)
      {
	String codigoPagamento = pagamento.getCodigo ().getConteudoAtual (0);
	if (codigoPagamento.equals ("03") || codigoPagamento.equals ("04"))
	  {
	    Iterator it = declaracaoIRPF.getDependentes ().recuperarLista ().iterator ();
	    while (it.hasNext ())
	      {
		Dependente dependente = (Dependente) it.next ();
		if (getInformacao ().asString ().equals (dependente.getNome ().asString ()))
		  return dependente;
	      }
	  }
	else if (codigoPagamento.equals ("05") || codigoPagamento.equals ("06"))
	  {
	    Iterator it = declaracaoIRPF.getAlimentandos ().recuperarLista ().iterator ();
	    while (it.hasNext ())
	      {
		Alimentando alimentando = (Alimentando) it.next ();
		if (getInformacao ().asString ().equals (alimentando.getNome ().asString ()))
		  return alimentando;
	      }
	  }
      }
    return null;
  }
  
  protected void informacaoModificada ()
  {
    implementacaoPropertyChange (null);
  }
  
  protected void readOnlyPropertyChange (boolean readOnly)
  {
    cmbNome.setEnabled (! readOnly);
  }
  
  protected void habilitadoPropertyChange (boolean habilitado)
  {
    cmbNome.setEnabled (habilitado);
  }
  
  public void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    atualizaModel ();
    cmbNome.setSelectedItem (obtemBeneficiarioSelecionado ());
  }
  
  public JComponent getComponenteEditor ()
  {
    return cmbNome;
  }
  
  public JComponent getComponenteFoco ()
  {
    return cmbNome;
  }
  
  public void setEstiloFonte (int estilo)
  {
    Font f = cmbNome.getFont ();
    f = f.deriveFont (estilo);
    cmbNome.setFont (f);
  }
  
  public void setIncrementoTamanhoFonte (int incremento)
  {
    incrementoTamanhoFonte = incremento;
    Font f = cmbNome.getFont ();
    if (tamanhoOriginal == -1.0F)
      tamanhoOriginal = f.getSize2D ();
    f = f.deriveFont (tamanhoOriginal + (float) incremento);
    cmbNome.setFont (f);
  }
  
  public int getIncrementoTamanhoFonte ()
  {
    return incrementoTamanhoFonte;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    Object obj = cmbNome.getSelectedItem ();
    if (obj != null)
      getInformacao ().setConteudo (obj.toString ());
    else
      getInformacao ().clear ();
  }
}
