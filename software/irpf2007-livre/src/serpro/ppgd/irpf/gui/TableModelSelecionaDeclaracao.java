/* TableModelSelecionaDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.Color;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.infraestrutura.util.PPGDFormPanel;
import serpro.ppgd.irpf.ColecaoIdDeclaracao;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.txt.util.IRPFTxtUtil;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class TableModelSelecionaDeclaracao extends AbstractTableModel
{
  private ColecaoIdDeclaracao colecaoIdUsuario = IRPFFacade.getListaIdDeclaracoes ();
  
  public int getRowCount ()
  {
    return colecaoIdUsuario.recuperarLista ().size ();
  }
  
  public int getColumnCount ()
  {
    return 3;
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    switch (columnIndex)
      {
      case 0:
	return montaPainelNumRecibo (rowIndex);
      case 1:
      case 2:
	return obtemInformacao (rowIndex, columnIndex);
      default:
	return "";
      }
  }
  
  public IdentificadorDeclaracao getIdentificadorDeclaracao (int rowIndex)
  {
    IdentificadorDeclaracao id = (IdentificadorDeclaracao) colecaoIdUsuario.recuperarLista ().get (rowIndex);
    return id;
  }
  
  private Object obtemInformacao (int rowIndex, int columnIndex)
  {
    IdentificadorDeclaracao usuario = getIdentificadorDeclaracao (rowIndex);
    switch (columnIndex)
      {
      case 1:
	return usuario.getCpf ().getConteudoFormatado ();
      case 2:
	return usuario.getNome ().getConteudoFormatado ();
      default:
	return null;
      }
  }
  
  private JPanel montaPainelNumRecibo (int rowIndex)
  {
    IdentificadorDeclaracao id = getIdentificadorDeclaracao (rowIndex);
    PPGDFormPanel pnl = new PPGDFormPanel ();
    pnl.setLayout (new FormLayout ("12dlu,2dlu,12dlu,2dlu,12dlu,2dlu,12dlu:grow(.5)", "P"));
    pnl.setBackground (Color.WHITE);
    pnl.getBuilder ().setRow (1);
    pnl.getBuilder ().setColumn (1);
    if (id.isCompleta ())
      pnl.getBuilder ().append (new JLabel (new ImageIcon (this.getClass ().getResource ("/icones/irpr_completa.png"))));
    else
      pnl.getBuilder ().append (new JLabel (new ImageIcon (this.getClass ().getResource ("/icones/irpr_simplificada.png"))));
    File decGeradaEntrega = IRPFTxtUtil.montaPathTXTDeclaracao (FabricaUtilitarios.getPathCompletoDirGravadas (), id, true);
    boolean gravada = decGeradaEntrega.exists ();
    if (gravada)
      {
	pnl.getBuilder ().setRow (1);
	pnl.getBuilder ().setColumn (3);
	pnl.getBuilder ().append (new JLabel (new ImageIcon (this.getClass ().getResource ("/icones/irpr_gravada.png"))));
      }
    String foiTransmitida = IRPFTxtUtil.declaracaoFoiTransmitida (id);
    if (foiTransmitida != null && foiTransmitida.trim ().length () > 0)
      {
	pnl.getBuilder ().setRow (1);
	pnl.getBuilder ().setColumn (5);
	pnl.getBuilder ().append (new JLabel (new ImageIcon (this.getClass ().getResource ("/icones/irpr_transmitida.png"))));
	pnl.getBuilder ().setRow (1);
	pnl.getBuilder ().setColumn (7);
	pnl.getBuilder ().append (new JLabel (IRPFTxtUtil.formataNumeroRecibo (foiTransmitida)));
      }
    return pnl;
  }
  
  public String getColumnName (int column)
  {
    switch (column)
      {
      case 0:
	return "N\u00ba do Recibo";
      case 1:
	return "CPF";
      case 2:
	return "Nome";
      default:
	return "";
      }
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    return super.isCellEditable (rowIndex, columnIndex);
  }
  
  public void setColecaoIdDeclaracao (ColecaoIdDeclaracao col)
  {
    colecaoIdUsuario = col;
    fireTableDataChanged ();
  }
}
