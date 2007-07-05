/* TableRendPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpf;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import serpro.ppgd.gui.table.ColumnGroup;
import serpro.ppgd.gui.table.GroupableTableColumnModel;
import serpro.ppgd.irpf.gui.DefaultIRPFHeaderCellRenderer;
import serpro.ppgd.irpf.gui.IRPFTable;
import serpro.ppgd.irpf.gui.IRPFTableModel;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.negocio.Valor;

public class TableRendPF extends IRPFTable
{
  private static final RendPF vazio = new RendPF ();
  
  protected void iniciaModelVazio ()
  {
    setModel (new TableModelRendPF (vazio));
  }
  
  protected TableCellEditor instanciaCellEditor (int col)
  {
    if (col == 0)
      return super.instanciaCellEditor (col);
    return getEditorDefault ();
  }
  
  protected void configuraLayout ()
  {
    super.configuraLayout ();
    GroupableTableColumnModel cm = (GroupableTableColumnModel) getColumnModel ();
    cm.getColumn (0).setPreferredWidth (45);
    cm.getColumn (1).setPreferredWidth (85);
    cm.getColumn (2).setPreferredWidth (85);
    cm.getColumn (3).setPreferredWidth (85);
    cm.getColumn (4).setPreferredWidth (85);
    cm.getColumn (5).setPreferredWidth (85);
    cm.getColumn (6).setPreferredWidth (85);
    cm.getColumn (7).setPreferredWidth (85);
    ColumnGroup g_vazio = new ColumnGroup (new DefaultIRPFHeaderCellRenderer (), "    ");
    g_vazio.add (cm.getColumn (0));
    ColumnGroup g_rend = new ColumnGroup (new DefaultIRPFHeaderCellRenderer (), "<HTML><CENTER>Rendimentos</CENTER></HTML>");
    g_rend.add (cm.getColumn (1));
    g_rend.add (cm.getColumn (2));
    ColumnGroup g_deducoes = new ColumnGroup (new DefaultIRPFHeaderCellRenderer (), "<HTML><CENTER>Dedu\u00e7\u00f5es</CENTER></HTML>");
    g_deducoes.add (cm.getColumn (3));
    g_deducoes.add (cm.getColumn (4));
    g_deducoes.add (cm.getColumn (5));
    g_deducoes.add (cm.getColumn (6));
    ColumnGroup g_carneleao = new ColumnGroup (new DefaultIRPFHeaderCellRenderer (), "<HTML><CENTER>Carn\u00ea-Le\u00e3o</CENTER></HTML>");
    g_carneleao.add (cm.getColumn (7));
    cm.addColumnGroup (g_vazio);
    cm.addColumnGroup (g_rend);
    cm.addColumnGroup (g_deducoes);
    cm.addColumnGroup (g_carneleao);
  }
  
  protected void executaValidacaoAposEdicao (int col, int row)
  {
    if (col == 4)
      {
	Valor dependentes = (Valor) ((IRPFTableModel) getModel ()).getInformacaoAt (row, col);
	if (! dependentes.isVazio ())
	  {
	    if (row == 0)
	      {
		if (! dependentes.operacao ('%', "117,00").isVazio ())
		  {
		    dependentes.setConteudo (dependentes.getConteudoAntigo ());
		    editarCelula (row, col);
		    JOptionPane.showMessageDialog (null, "O valor mensal para cada dependente \u00e9 R$117,00. Corrija o valor informado.", "Erro", 0);
		  }
	      }
	    else if (! dependentes.operacao ('%', "126,36").isVazio ())
	      {
		dependentes.setConteudo (dependentes.getConteudoAntigo ());
		editarCelula (row, col);
		SwingUtilities.invokeLater (new Runnable ()
		{
		  public void run ()
		  {
		    JOptionPane.showMessageDialog (null, "O valor mensal para cada dependente \u00e9 R$126,36. Corrija o valor informado.", "Erro", 0);
		  }
		});
	      }
	  }
      }
  }
  
  public void proximaCelula (int row, int col)
  {
    if (col == 7 && row == 11)
      {
	selecionaCelula (1, 0);
	editarCelula (0, 1);
      }
    else if (col == 7)
      {
	selecionaCelula (1, row + 1);
	editarCelula (row + 1, 1);
      }
    else
      {
	selecionaCelula (col + 1, row);
	editarCelula (row, col + 1);
      }
  }
  
  public static void main (String[] args)
  {
    JDialog dlg = new JDialog ();
    dlg.setDefaultCloseOperation (2);
    JScrollPane scroll = new JScrollPane (new TableRendPF ());
    dlg.getContentPane ().add (scroll);
    dlg.pack ();
    dlg.setVisible (true);
  }
}
