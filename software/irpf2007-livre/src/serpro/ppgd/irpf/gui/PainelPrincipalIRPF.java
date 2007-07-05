/* PainelPrincipalIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PainelPrincipalIRPF extends JPanel
{
  private JPanel jPanel1;
  public JLabel lblTitulo;
  public JPanel pnlAreaUtil;
  
  public PainelPrincipalIRPF ()
  {
    initComponents ();
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    lblTitulo = new JLabel ();
    lblTitulo.setFont (lblTitulo.getFont ().deriveFont (1));
    lblTitulo.setFont (lblTitulo.getFont ().deriveFont (lblTitulo.getFont ().getSize2D () + 3.0F));
    pnlAreaUtil = new JPanel ();
    setLayout (new BorderLayout ());
    jPanel1.setBackground (new Color (58, 110, 165));
    jPanel1.setBorder (BorderFactory.createEtchedBorder ());
    lblTitulo.setForeground (new Color (255, 255, 255));
    lblTitulo.setText ("jLabel1");
    jPanel1.add (lblTitulo);
    add (jPanel1, "North");
    pnlAreaUtil.setLayout (new BorderLayout ());
    pnlAreaUtil.setBorder (BorderFactory.createBevelBorder (1));
    add (pnlAreaUtil, "Center");
  }
  
  public void mudaCorpoPainelPrincipal (PainelIRPFIf painel)
  {
    pnlAreaUtil.removeAll ();
    pnlAreaUtil.add ((JPanel) painel);
    lblTitulo.setText (painel.getTituloPainel ());
    pnlAreaUtil.validate ();
    pnlAreaUtil.repaint ();
    pnlAreaUtil.revalidate ();
  }
}
