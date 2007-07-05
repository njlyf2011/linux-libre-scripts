/* TableMovimentacoesRebanhoBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import serpro.ppgd.gui.table.GroupableTableColumnModel;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.atividaderural.MovimentacaoRebanho;
import serpro.ppgd.irpf.gui.IRPFTable;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;

public class TableMovimentacoesRebanhoBrasil extends IRPFTable
{
  private static final MovimentacaoRebanho vazio = new MovimentacaoRebanho ();
  
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelMovimentacaoRebanhoBrasil (vazio));
  }
  
  public void setObjetoNegocio (ObjetoNegocio obj)
  {
    ((TableModelMovimentacaoRebanhoBrasil) getModel ()).setMovimentacao ((MovimentacaoRebanho) obj);
    super.setObjetoNegocio (obj);
  }
  
  protected void executaValidacaoAposEdicao (final int col, final int row)
  {
    final Informacao info = ((TableModelMovimentacaoRebanhoBrasil) getModel ()).getInformacaoAt (row, col);
    info.validar ();
    if (! info.isValido ())
      SwingUtilities.invokeLater (new Runnable ()
      {
	public void run ()
	{
	  TableMovimentacoesRebanhoBrasil.this.selecionaCelula (col, row);
	  String msg = info.getRetornoTodasValidacoes ().getPrimeiroRetornoValidacaoMaisSevero ().getMensagemValidacao ();
	  info.clear ();
	  JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), msg, "Aten\u00e7\u00e3o", 1);
	  TableMovimentacoesRebanhoBrasil.this.editarCelula (row, col);
	}
      });
  }
  
  public void proximaCelula (int row, int col)
  {
    if ((col == getModel ().getColumnCount () - 2 || col == getModel ().getColumnCount () - 1) && row == getModel ().getRowCount () - 1)
      {
	selecionaCelula (2, 0);
	editarCelula (0, 2);
      }
    else if (col == getModel ().getColumnCount () - 2 || col == getModel ().getColumnCount () - 1)
      {
	selecionaCelula (2, row + 1);
	editarCelula (row + 1, 2);
      }
    else
      {
	selecionaCelula (col + 1, row);
	editarCelula (row, col + 1);
      }
  }
  
  protected TableCellEditor instanciaCellEditor (int col)
  {
    if (col == 0 || col == 1 || col == 7)
      return super.instanciaCellEditor (col);
    return getEditorDefault ();
  }
  
  protected void configuraLayout ()
  {
    super.configuraLayout ();
    GroupableTableColumnModel cm = (GroupableTableColumnModel) getColumnModel ();
    cm.getColumn (0).setPreferredWidth (35);
    cm.getColumn (1).setPreferredWidth (160);
  }
}
