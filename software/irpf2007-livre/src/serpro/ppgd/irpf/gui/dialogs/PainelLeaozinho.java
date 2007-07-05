/* PainelLeaozinho - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;

public class PainelLeaozinho extends JPanel
{
  private JButton jButton1;
  private JLabel lblLink;
  private JLabel lblTexto;
  private JLabel lblTitulo;
  
  public PainelLeaozinho ()
  {
    initComponents ();
  }
  
  private void initComponents ()
  {
    jButton1 = new JButton ();
    lblTexto = new JLabel ();
    lblTitulo = new JLabel ();
    lblLink = new JLabel ();
    jButton1.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/CANCELAR.PNG")));
    jButton1.setMnemonic ('F');
    jButton1.setText ("<HTML><B><U>F</U>echar</B></HTML>");
    jButton1.setActionCommand ("Fechar");
    jButton1.addActionListener (new ActionListener ()
    {
      public void actionPerformed (ActionEvent evt)
      {
	PainelLeaozinho.this.jButton1ActionPerformed (evt);
      }
    });
    lblTexto.setHorizontalAlignment (0);
    lblTexto.setText ("<html><p align=\"justify\"><b>A Receita Federal, no \u00e2mbito do Programa Nacional de Educa\u00e7\u00e3o Fiscal, desenvolveu uma p\u00e1gina na internet especial para crian\u00e7as e jovens: a p\u00e1gina do Le\u00e3ozinho. A turma do Le\u00e3ozinho quer ensinar, brincando, que ser cidad\u00e3o \u00e9 muito mais que pagar impostos: \u00e9 ter plena consci\u00eancia de seus direitos e deveres. Para visualizar o Programa Nacional de Educa\u00e7\u00e3o Fiscal Virtual da SRF, acesse: </b></p></html>");
    lblTitulo.setHorizontalAlignment (0);
    lblTitulo.setText ("<html><b>Le\u00e3ozinho</b></html>");
    lblLink.setHorizontalAlignment (0);
    lblLink.setText ("<html><b><a href=\"http://www.leaozinho.receita.fazenda.gov.br\">http://www.leaozinho.receita.fazenda.gov.br</a></b></html>");
    lblLink.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelLeaozinho.this.lblLinkMouseClicked (evt);
      }
      
      public void mouseEntered (MouseEvent evt)
      {
	PainelLeaozinho.this.lblLinkMouseEntered (evt);
      }
      
      public void mouseExited (MouseEvent evt)
      {
	PainelLeaozinho.this.lblLinkMouseExited (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (lblTexto, -1, 395, 32767).add (lblLink, -1, 395, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (lblTitulo, -1, 375, 32767).addContainerGap ()).add (2, layout.createSequentialGroup ().addContainerGap (153, 32767).add (jButton1).add (149, 149, 149)));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (lblTitulo, -2, 19, -2).addPreferredGap (0).add (lblTexto, -2, 115, -2).addPreferredGap (0).add (lblLink).addPreferredGap (0).add (jButton1).addContainerGap (-1, 32767)));
  }
  
  private void lblLinkMouseExited (MouseEvent evt)
  {
    Cursor defaultCursor = new Cursor (0);
    setCursor (defaultCursor);
  }
  
  private void lblLinkMouseEntered (MouseEvent evt)
  {
    Cursor handCursor = new Cursor (12);
    setCursor (handCursor);
  }
  
  private void lblLinkMouseClicked (MouseEvent evt)
  {
    IRPFGuiUtil.abreURL ("http://www.leaozinho.receita.fazenda.gov.br");
  }
  
  private void jButton1ActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
