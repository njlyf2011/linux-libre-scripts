/* PainelGCapBensMoveis - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.ganhosdecapital.bensmoveis;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.irpf.gui.PainelIRPFIf;

public class PainelGCapBensMoveis extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Ganhos de Capital - Bens M\u00f3veis ";
  private JButton btnAbrir;
  private JButton btnAjuda;
  private JButton btnImportar;
  private JPanel jPanel1;
  private JScrollPane jScrollPane1;
  private TableBensMoveis tableBensMoveis1;
  
  public String getTituloPainel ()
  {
    return "Ganhos de Capital - Bens M\u00f3veis ";
  }
  
  public PainelGCapBensMoveis ()
  {
    initComponents ();
  }
  
  private void initComponents ()
  {
    jScrollPane1 = new JScrollPane ();
    tableBensMoveis1 = new TableBensMoveis ();
    jPanel1 = new JPanel ();
    btnAjuda = new JButton ();
    btnAbrir = new JButton ();
    btnImportar = new JButton ();
    jScrollPane1.setBorder (BorderFactory.createBevelBorder (1));
    jScrollPane1.setViewportView (tableBensMoveis1);
    btnAjuda.setIcon (new ImageIcon (this.getClass ().getResource ("/icones/HELP.PNG")));
    btnAjuda.setMnemonic ('A');
    btnAjuda.setText ("<HTML><B><U>A</U>juda</B></HTML>");
    btnAbrir.setMnemonic ('b');
    btnAbrir.setText ("<HTML><B>A<U>b</U>rir...</B></HTML>");
    btnImportar.setMnemonic ('I');
    btnImportar.setText ("<HTML><B><U>I</U>mportar</B></HTML>");
    GroupLayout jPanel1Layout = new GroupLayout (jPanel1);
    jPanel1.setLayout (jPanel1Layout);
    jPanel1Layout.setHorizontalGroup (jPanel1Layout.createParallelGroup (1).add (2, jPanel1Layout.createSequentialGroup ().addContainerGap (261, 32767).add (btnImportar).addPreferredGap (0).add (btnAbrir).addPreferredGap (0).add (btnAjuda).addContainerGap ()));
    jPanel1Layout.linkSize (new Component[] { btnAbrir, btnAjuda, btnImportar }, 1);
    jPanel1Layout.setVerticalGroup (jPanel1Layout.createParallelGroup (1).add (jPanel1Layout.createSequentialGroup ().addContainerGap ().add (jPanel1Layout.createParallelGroup (3).add (btnAjuda).add (btnAbrir).add (btnImportar)).addContainerGap (-1, 32767)));
    jPanel1Layout.linkSize (new Component[] { btnAbrir, btnAjuda, btnImportar }, 2);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (layout.createParallelGroup (1).add (2, jPanel1, -1, -1, 32767).add (2, jScrollPane1, -1, 544, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jScrollPane1, -2, 364, -2).addPreferredGap (0).add (jPanel1, -1, -1, 32767)));
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
