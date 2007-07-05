/* PainelImprimirDARF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.formatosexternos.barcode.util.DarfAdaptor;
import serpro.ppgd.gui.DialogGenerico;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.TableSelecionaDeclaracao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Valor;

public class PainelImprimirDARF extends JPanel
{
  private JButton btnAjuda;
  private JButton btnCancelar;
  private JButton btnOk;
  private TableSelecionaDeclaracao edtTableDecs;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JScrollPane jScrollPane1;
  
  public PainelImprimirDARF ()
  {
    initComponents ();
    PlataformaPPGD.getPlataforma ().setHelpID (this, "CDARF");
    PlataformaPPGD.getPlataforma ().setHelpID (btnAjuda, "CDARF");
    jScrollPane1.getViewport ().setBackground (Color.WHITE);
    edtTableDecs.addDeclaracaoSelectionListener (new ListSelectionListener ()
    {
      public void valueChanged (ListSelectionEvent e)
      {
	if (edtTableDecs.getSelectedRowCount () > 0)
	  btnOk.setEnabled (true);
	else
	  btnOk.setEnabled (false);
      }
    });
    edtTableDecs.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent e)
      {
	if (e.getClickCount () == 2 && edtTableDecs.getSelectedRowCount () > 0)
	  PainelImprimirDARF.this.btnOkActionPerformed (null);
      }
    });
    edtTableDecs.setSelectionMode (2);
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jPanel2 = new JPanel ();
    btnOk = new JButton ();
    btnCancelar = new JButton ();
    btnAjuda = new JButton ();
    jPanel3 = new JPanel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jLabel4 = new JLabel ();
    jLabel5 = new JLabel ();
    jScrollPane1 = new JScrollPane ();
    edtTableDecs = new TableSelecionaDeclaracao ();
    jLabel1.setForeground (new Color (0, 0, 128));
    jLabel1.setText ("<HTML><B>Selecione os contribuintes cujos dados deseja imprimir:</B></HTML>");
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setMnemonic ('O');
    btnOk.setText ("<HTML><B><U>O</U>k</B></HTML>");
    btnOk.setEnabled (false);
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelImprimirDARF.this.btnOkActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B><U>C</U>ancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelImprimirDARF.this.btnCancelarActionPerformed (evt);
      }
    });
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    GroupLayout jPanel2Layout = new GroupLayout (jPanel2);
    jPanel2.setLayout (jPanel2Layout);
    jPanel2Layout.setHorizontalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().addContainerGap ().add (jPanel2Layout.createParallelGroup (1).add (btnOk).add (btnCancelar).add (btnAjuda)).addContainerGap (34, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnOk }, 1);
    jPanel2Layout.setVerticalGroup (jPanel2Layout.createParallelGroup (1).add (jPanel2Layout.createSequentialGroup ().add (btnOk).addPreferredGap (0).add (btnCancelar).addPreferredGap (0).add (btnAjuda).addContainerGap (213, 32767)));
    jPanel2Layout.linkSize (new Component[] { btnAjuda, btnCancelar, btnOk }, 2);
    jLabel2.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_gravada.png")));
    jLabel2.setText ("<HTML><B>Gravada</B.</HTML>");
    jLabel3.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_transmitida.png")));
    jLabel3.setText ("<HTML><B>Transmitida</B.</HTML>");
    jLabel4.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_completa.png")));
    jLabel4.setText ("<HTML><B>Completa</B></HTML>");
    jLabel5.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/irpr_simplificada.png")));
    jLabel5.setText ("<HTML><B>Simplificada</B></HTML>");
    GroupLayout jPanel3Layout = new GroupLayout (jPanel3);
    jPanel3.setLayout (jPanel3Layout);
    jPanel3Layout.setHorizontalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jLabel2, -2, 89, -2).addPreferredGap (0).add (jLabel3, -2, 105, -2).addPreferredGap (0).add (jLabel4, -2, 91, -2).addPreferredGap (0).add (jLabel5).addContainerGap (260, 32767)));
    jPanel3Layout.setVerticalGroup (jPanel3Layout.createParallelGroup (1).add (jPanel3Layout.createSequentialGroup ().addContainerGap ().add (jPanel3Layout.createParallelGroup (3).add (jLabel2).add (jLabel3).add (jLabel5).add (jLabel4)).addContainerGap (16, 32767)));
    edtTableDecs.setAutoResizeMode (0);
    jScrollPane1.setViewportView (edtTableDecs);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (1, jPanel3, -1, -1, 32767).add (layout.createSequentialGroup ().add (jScrollPane1, -1, 502, 32767).addPreferredGap (0).add (jPanel2, -2, -1, -2)).add (1, jLabel1)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (1).add (jScrollPane1, -1, 303, 32767).add (jPanel2, -1, -1, 32767)).addPreferredGap (0).add (jPanel3, -2, -1, -2)));
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    int[] rows = edtTableDecs.getSelectedRows ();
    if (rows.length > 0)
      {
	IdentificadorDeclaracao id = edtTableDecs.getIdentificadorDeclaracao (rows[0]);
	IRPFFacade.abreDeclaracao (id);
	DeclaracaoIRPF dec = IRPFFacade.getInstancia ().getDeclaracao ();
	String pathDados = id.getPathArquivo ();
	String cpf = id.getCpf ().getConteudoFormatado ();
	String nome = id.getNome ().getConteudoFormatado ();
	int qtdeQuota = dec.getResumo ().getCalculoImposto ().getNumQuotas ().getConteudoInteiro ();
	boolean debitoAutomatico = dec.getResumo ().getCalculoImposto ().getDebitoAutomatico ().asString ().equals ("autorizado");
	DarfAdaptor dados = new DarfAdaptor ();
	try
	  {
	    dados.setCodigoEmpresa ("0064");
	    dados.setIndicadorManualCalculado ("1");
	    dados.setNome (id.getNome ().toString ());
	    dados.setTelefone (dec.getContribuinte ().getTelefone ().toString ());
	    Data data = new Data ();
	    data.setConteudo ("31/12/" + ConstantesGlobais.ANO_BASE);
	    dados.setPeriodoApuracao (data);
	    dados.setCpfCnpj (id.getCpf ());
	    dados.setCodigoReceita ("0211");
	    dados.setNumeroReferencia ("856");
	    dados.setValorPrincipal (dec.getResumo ().getCalculoImposto ().getValorQuota ().getConteudoFormatado ());
	    Data dataVencimento = new Data ();
	    dataVencimento.setConteudo ("30/04/2007");
	    dados.setDataVencimento (dataVencimento);
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
	Valor minimoQuota = new Valor ();
	minimoQuota.append ('+', "10,00");
	if (dec.getResumo ().getCalculoImposto ().getValorQuota ().comparacao (">=", minimoQuota))
	  {
	    JFrame j = PlataformaPPGD.getPlataforma ().getJanelaPrincipal ();
	    DialogGenerico dialog = new DialogGenerico (j, new PainelImprimirDarfQuota (pathDados, cpf, qtdeQuota, true, nome, debitoAutomatico, dados), "Imprimir Darf do CPF: " + cpf, null, true);
	    dialog.dispose ();
	  }
	else
	  JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "N\u00e3o existe Darf a ser Impresso", "IRPF" + ConstantesGlobais.EXERCICIO, 1);
      }
  }
}
