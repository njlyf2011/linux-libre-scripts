/* PainelEdicaoItemQuadroAuxiliar - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JEditValor;
import serpro.ppgd.irpf.ColecaoItemQuadroAuxiliar;
import serpro.ppgd.irpf.ItemQuadroAuxiliar;

public class PainelEdicaoItemQuadroAuxiliar extends JPanel
{
  private ItemQuadroAuxiliar item = null;
  private ColecaoItemQuadroAuxiliar col = null;
  private boolean isEdicao = false;
  private String valAntigoEspecificacao = null;
  private String valAntigoValor = null;
  private JButton btnCancelar;
  private JButton btnOk;
  private JEditAlfa jEditAlfa1;
  private JEditValor jEditValor1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JPanel jPanel1;
  
  public PainelEdicaoItemQuadroAuxiliar (ColecaoItemQuadroAuxiliar _col, ItemQuadroAuxiliar _item, boolean _isEdicao)
  {
    item = _item;
    col = _col;
    isEdicao = _isEdicao;
    valAntigoEspecificacao = item.getEspecificacao ().getConteudoFormatado ();
    valAntigoValor = item.getValor ().getConteudoFormatado ();
    initComponents ();
    jEditAlfa1.setInformacao (item.getEspecificacao ());
    jEditValor1.setInformacao (item.getValor ());
    ActionListener acaoOk = new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	jEditAlfa1.setarCampo ();
	jEditValor1.getInformacao ().setConteudo (((JTextField) jEditValor1.getComponenteFoco ()).getText ());
	btnOk.doClick ();
      }
    };
  }
  
  private void initComponents ()
  {
    jLabel1 = new JLabel ();
    jEditAlfa1 = new JEditAlfa ();
    jLabel2 = new JLabel ();
    jEditValor1 = new JEditValor ();
    jPanel1 = new JPanel ();
    btnOk = new JButton ();
    btnCancelar = new JButton ();
    jLabel1.setText ("Especifica\u00e7\u00e3o:");
    jEditAlfa1.setMaxChars (60);
    jLabel2.setText ("Valor:");
    btnOk.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/OK.PNG")));
    btnOk.setMnemonic ('O');
    btnOk.setText ("<HTML><B>OK</B></HTML>");
    btnOk.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelEdicaoItemQuadroAuxiliar.this.btnOkActionPerformed (evt);
      }
    });
    btnCancelar.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    btnCancelar.setMnemonic ('C');
    btnCancelar.setText ("<HTML><B>Cancelar</B></HTML>");
    btnCancelar.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelEdicaoItemQuadroAuxiliar.this.btnCancelarActionPerformed (evt);
      }
    });
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (btnOk).addPreferredGap (0).add (btnCancelar).addContainerGap (56, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnCancelar, btnOk }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (3).add (btnOk).add (btnCancelar)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnCancelar, btnOk }, 2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (jPanel1, -2, -1, -2).add (layout.createSequentialGroup ().add (layout.createParallelGroup (2, false).add (1, jLabel2).add (1, jLabel1, -2, 93, -2)).addPreferredGap (0).add (layout.createParallelGroup (1).add (jEditAlfa1, -1, 213, 32767).add (jEditValor1, -1, 213, 32767)))).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (2).add (jLabel1).add (jEditAlfa1, -2, -1, -2)).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel2).add (jEditValor1, -2, -1, -2)).addPreferredGap (0).add (jPanel1, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  private void btnOkActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
    if (! isEdicao && ! item.getEspecificacao ().isVazio () && ! item.getValor ().isVazio ())
      col.recuperarLista ().add (item);
  }
  
  private void btnCancelarActionPerformed (ActionEvent evt)
  {
    if (isEdicao)
      {
	item.getEspecificacao ().setConteudo (valAntigoEspecificacao);
	item.getValor ().setConteudo (valAntigoValor);
      }
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
