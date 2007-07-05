/* PainelReceitasDespesasBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.ImportarAtividadeRural;
import serpro.ppgd.irpf.gui.PainelIRPFIf;

public class PainelReceitasDespesasBrasil extends JPanel implements PainelIRPFIf
{
  private JButton jButton1;
  private JPanel jPanel1;
  private JScrollPane jScrollPane1;
  public TableReceitasDespesasBrasil tableReceitasDespesasBrasil1;
  
  public PainelReceitasDespesasBrasil ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ARReceitas");
    initComponents ();
    tableReceitasDespesasBrasil1.setObjetoNegocio (IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ());
    jButton1.addActionListener (new ImportarAtividadeRural ()
    {
      public boolean importaExterior ()
      {
	return false;
      }
    });
  }
  
  private void initComponents ()
  {
    jPanel1 = new JPanel ();
    jButton1 = new JButton ();
    jScrollPane1 = new JScrollPane ();
    tableReceitasDespesasBrasil1 = new TableReceitasDespesasBrasil ();
    jButton1.setText ("Importar");
    jPanel1.add (jButton1);
    jScrollPane1.setViewportView (tableReceitasDespesasBrasil1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jPanel1, -1, 536, 32767).add (layout.createSequentialGroup ().add (12, 12, 12).add (jScrollPane1, -1, 512, 32767).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jScrollPane1, -2, 313, -2).addPreferredGap (0).add (jPanel1, -2, -1, -2).addContainerGap (-1, 32767)));
  }
  
  public String getTituloPainel ()
  {
    return "Receitas e Despesas - Brasil";
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
