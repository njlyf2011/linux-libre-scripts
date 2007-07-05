/* PainelEdicaoItemLucrosDividendos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditCNPJ;
import serpro.ppgd.gui.xbeans.JEditCodigo;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.irpf.ColecaoItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.ItemQuadroLucrosDividendos;
import serpro.ppgd.negocio.ConstantesGlobais;

public class PainelEdicaoItemLucrosDividendos extends JPanel
{
  private ItemQuadroLucrosDividendos item = null;
  private ColecaoItemQuadroLucrosDividendos col = null;
  private boolean isEdicao = false;
  private String valAntigoEspecificacao = null;
  private String valAntigoValor = null;
  private JButton btnCancelar;
  private JButton btnOk;
  private JEditCNPJ edtCnpj;
  private JEditAlfa edtNome;
  private JEditCodigo edtTipo;
  private JEditValor edtValor;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JPanel jPanel1;
  
  public PainelEdicaoItemLucrosDividendos (ColecaoItemQuadroLucrosDividendos _col, ItemQuadroLucrosDividendos _item, boolean _isEdicao)
  {
    item = _item;
    col = _col;
    isEdicao = _isEdicao;
    valAntigoEspecificacao = item.getNomeFonte ().getConteudoFormatado ();
    valAntigoValor = item.getValor ().getConteudoFormatado ();
    initComponents ();
    edtTipo.setInformacao (item.getTipo ());
    edtCnpj.setInformacao (item.getCnpjEmpresa ());
    edtNome.setInformacao (item.getNomeFonte ());
    edtValor.setInformacao (item.getValor ());
    ActionListener acaoOk = new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	edtNome.setarCampo ();
	edtValor.getInformacao ().setConteudo (((JTextField) edtValor.getComponenteFoco ()).getText ());
	btnOk.doClick ();
      }
    };
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    edtNome = new JEditAlfa ();
    jLabel2 = new JLabel ();
    edtValor = new JEditValor ();
    jPanel1 = new JPanel ();
    btnOk = new JButton ();
    btnCancelar = new JButton ();
    jLabel3 = new JLabel ();
    edtCnpj = new JEditCNPJ ();
    jLabel4 = new JLabel ();
    edtTipo = new JEditCodigo ();
    jLabel1.setText ("Nome da empresa");
    edtNome.addKeyListener (new KeyAdapter ()
    {
      public void keyPressed (KeyEvent evt)
      {
	PainelEdicaoItemLucrosDividendos.this.edtNomeKeyPressed (evt);
      }
    });
    jLabel2.setText ("Valor");
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setMnemonic ('O');
    btnOk.setText ("<HTML><B>OK</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelEdicaoItemLucrosDividendos.this.btnOkActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B>Cancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelEdicaoItemLucrosDividendos.this.btnCancelarActionPerformed (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (btnOk).addPreferredGap (0).add (btnCancelar).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnCancelar, btnOk }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (1).add (btnCancelar).add (btnOk)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnCancelar, btnOk }, 2);
    jLabel3.setText ("CNPJ");
    jLabel4.setText ("Benefici\u00e1rio");
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (layout.createParallelGroup (1).add (jLabel1, -1, 139, 32767).add (jLabel3, -1, 139, 32767).add (jLabel2).add (jLabel4, -1, 139, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1).add (edtValor, -1, 169, 32767).add (layout.createSequentialGroup ().add (edtNome, -1, 168, 32767).add (1, 1, 1)).add (layout.createParallelGroup (2, false).add (1, edtTipo, -1, -1, 32767).add (1, edtCnpj, -1, 132, 32767)))).add (2, jPanel1, -2, -1, -2)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jLabel4, -1, 22, 32767).add (edtTipo, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jLabel3).add (edtCnpj, -2, 21, -2)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (jLabel1, -1, -1, 32767).add (edtNome, -1, -1, 32767)).addPreferredGap (0).add (layout.createParallelGroup (1, false).add (jLabel2, -1, -1, 32767).add (edtValor, -1, -1, 32767)).addPreferredGap (0).add (jPanel1, -1, -1, 32767)));
  }
  
  private void edtNomeKeyPressed (KeyEvent evt)
  {
    /* empty */
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    edtTipo.getInformacao ().validar ();
    edtCnpj.getInformacao ().validar ();
    edtValor.getInformacao ().validar ();
    if (! edtTipo.getInformacao ().isValido ())
      {
	JOptionPane.showMessageDialog (this, "\u00c9 necess\u00e1rio preencher o campo Tipo", "IRPF" + ConstantesGlobais.EXERCICIO, 1);
	edtTipo.setaFoco (false);
      }
    else if (! edtCnpj.getInformacao ().isValido ())
      {
	JOptionPane.showMessageDialog (this, "CNPJ da fonte pagadora em branco ou inv\u00e1lido", "IRPF" + ConstantesGlobais.EXERCICIO, 1);
	edtCnpj.setaFoco (false);
      }
    else if (! edtValor.getInformacao ().isValido ())
      {
	JOptionPane.showMessageDialog (this, "Falta valor no campo Lucros e dividendos recebidos", "IRPF" + ConstantesGlobais.EXERCICIO, 1);
	edtValor.setaFoco (false);
      }
    else
      {
	((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
	if (! isEdicao && ! item.getValor ().isVazio ())
	  col.recuperarLista ().add (item);
      }
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    if (isEdicao)
      {
	item.getNomeFonte ().setConteudo (valAntigoEspecificacao);
	item.getValor ().setConteudo (valAntigoValor);
      }
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
