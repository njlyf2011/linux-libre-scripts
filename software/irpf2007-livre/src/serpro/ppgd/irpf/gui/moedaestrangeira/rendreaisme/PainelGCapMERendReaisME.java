/* PainelGCapMERendReaisME - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.moedaestrangeira.rendreaisme;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.irpf.gui.PainelIRPFIf;

public class PainelGCapMERendReaisME extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Moeda Estrangeira - Bens, Direitos e Aplica\u00e7\u00f5es Financeiras";
  private JButton btnAbrir;
  private JButton btnAjuda;
  private JButton btnImportar;
  private JPanel jPanel1;
  private JScrollPane jScrollPane1;
  private TableGCapRendReaisME tableGCapRendReaisME1;
  
  public String getTituloPainel ()
  {
    return "Moeda Estrangeira - Bens, Direitos e Aplica\u00e7\u00f5es Financeiras";
  }
  
  public PainelGCapMERendReaisME ()
  {
    initComponents ();
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    btnAbrir = new JButton ();
    btnAjuda = new JButton ();
    btnImportar = new JButton ();
    jScrollPane1 = new JScrollPane ();
    tableGCapRendReaisME1 = new TableGCapRendReaisME ();
    btnAbrir.setMnemonic ('b');
    btnAbrir.setText ("<HTML><B>A<U>b</U>rir...</B></HTML>");
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnImportar.setMnemonic ('I');
    btnImportar.setText ("<HTML><B><U>I</U>mportar</B></HTML>");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (2, jPanel1Layout.createSequentialGroup ().addContainerGap (261, 32767).add (btnImportar).addPreferredGap (0).add (btnAbrir).addPreferredGap (0).add (btnAjuda).addContainerGap ()));
    jPanel1Layout.linkSize (new Component[] { btnAbrir, btnAjuda, btnImportar }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (3).add (btnAjuda).add (btnAbrir).add (btnImportar)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAbrir, btnAjuda, btnImportar }, 2);
    jScrollPane1.setBorder (BorderFactory.createBevelBorder (1));
    jScrollPane1.setViewportView (tableGCapRendReaisME1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (2, jScrollPane1, -1, 544, 32767).add (2, jPanel1, -1, -1, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (2, layout.createSequentialGroup ().addContainerGap ().add (jScrollPane1, -1, 361, 32767).addPreferredGap (0).add (jPanel1, -2, -1, -2)));
  }
  
  public void vaiExibir ()
  {
    /* empty */
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
