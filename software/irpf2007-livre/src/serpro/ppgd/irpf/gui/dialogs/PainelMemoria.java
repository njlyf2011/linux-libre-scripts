/* PainelMemoria - Decompiled by JODE
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

public class PainelMemoria extends JPanel
{
  private JButton jButton1;
  private JLabel lblLink;
  private JLabel lblTexto;
  private JLabel lblTitulo;
  
  public PainelMemoria ()
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
	PainelMemoria.this.jButton1ActionPerformed (evt);
      }
    });
    lblTexto.setHorizontalAlignment (0);
    lblTexto.setText ("<html><p align=\"justify\"><b>A Receita Federal desenvolveu uma p\u00e1gina na internet sobre a mem\u00f3ria dos tributos brasileiros. H\u00e1 uma sala exclusiva do imposto de renda pessoa f\u00edsica que apresenta a hist\u00f3ria desse imposto, as curiosidades, todos os formul\u00e1rios de 1924 a 2006, com o coment\u00e1rio de cada um, os programas e outras informa\u00e7\u00f5es sobre a trajet\u00f3ria do IRPF no Brasil. Para visualizar a Mem\u00f3ria da Receita Federal, acesse:</b></p></html>");
    lblTitulo.setHorizontalAlignment (0);
    lblTitulo.setText ("<html><b>Mem\u00f3ria da Receita Federal</b></html>");
    lblLink.setHorizontalAlignment (0);
    lblLink.setText ("<html><b><a href=\"http://www.receita.fazenda.gov.br/memoria\">http://www.receita.fazenda.gov.br/Memoria</a></b></html>");
    lblLink.addMouseListener (new MouseAdapter ()
    {
      public void mouseClicked (MouseEvent evt)
      {
	PainelMemoria.this.lblLinkMouseClicked (evt);
      }
      
      public void mouseEntered (MouseEvent evt)
      {
	PainelMemoria.this.lblLinkMouseEntered (evt);
      }
      
      public void mouseExited (MouseEvent evt)
      {
	PainelMemoria.this.lblLinkMouseExited (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (lblTexto, -1, 397, 32767).add (lblLink, -1, 397, 32767).add (layout.createSequentialGroup ().addContainerGap ().add (lblTitulo, -1, 377, 32767).addContainerGap ()).add (2, layout.createSequentialGroup ().addContainerGap (153, 32767).add (jButton1).add (149, 149, 149)));
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
    IRPFGuiUtil.abreURL ("http://www.receita.fazenda.gov.br/Memoria");
  }
  
  private void jButton1ActionPerformed (ActionEvent evt)
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
