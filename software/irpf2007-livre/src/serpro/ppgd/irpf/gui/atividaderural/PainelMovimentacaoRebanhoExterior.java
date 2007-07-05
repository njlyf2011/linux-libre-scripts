/* PainelMovimentacaoRebanhoExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;

public class PainelMovimentacaoRebanhoExterior extends JPanel implements PainelIRPFIf
{
  private JScrollPane jScrollPane1;
  private TableMovimentacoesRebanhoBrasil tableMovimentacoesRebanhoBrasil1;
  
  public PainelMovimentacaoRebanhoExterior ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "ARRebanho");
    initComponents ();
    tableMovimentacoesRebanhoBrasil1.setObjetoNegocio (IRPFFacade.getInstancia ().getAtividadeRural ().getExterior ().getMovimentacaoRebanho ());
  }
  
  private void initComponents ()
  {
    jScrollPane1 = new JScrollPane ();
    tableMovimentacoesRebanhoBrasil1 = new TableMovimentacoesRebanhoBrasil ();
    jScrollPane1.setViewportView (tableMovimentacoesRebanhoBrasil1);
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jScrollPane1, -1, 542, 32767).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().addContainerGap ().add (jScrollPane1, -2, 156, -2).addContainerGap (-1, 32767)));
  }
  
  public String getTituloPainel ()
  {
    return "Movimenta\u00e7\u00f5es do Rebanho - Exterior";
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
