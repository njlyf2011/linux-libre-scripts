/* TableModelFundosInvestimentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendavariavel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import serpro.ppgd.irpf.gui.IRPFTableModel;
import serpro.ppgd.irpf.rendavariavel.FundosInvestimentos;
import serpro.ppgd.irpf.rendavariavel.MesFundosInvestimentos;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;

public class TableModelFundosInvestimentos extends IRPFTableModel
{
  private FundosInvestimentos fundosInvestimentos;
  
  public TableModelFundosInvestimentos (FundosInvestimentos aFundos)
  {
    super ((ObjetoNegocio) aFundos);
    fundosInvestimentos = aFundos;
  }
  
  public Informacao getInformacaoAt (int row, int col)
  {
    MesFundosInvestimentos mes = fundosInvestimentos.getMeses ()[col - 1];
    Informacao info = null;
    switch (row)
      {
      case 0:
	info = mes.getResultLiquidoMes ();
	break;
      case 1:
	info = mes.getResultNegativoAnterior ();
	break;
      case 2:
	info = mes.getBaseCalcImposto ();
	break;
      case 3:
	info = mes.getPrejuizoCompensar ();
	break;
      case 4:
	info = mes.getAliquotaImposto ();
	break;
      case 5:
	info = mes.getImpostoDevido ();
	break;
      case 6:
	info = mes.getImpostoPago ();
	break;
      }
    return info;
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    boolean result = false;
    if (columnIndex == 0)
      return result;
    switch (rowIndex)
      {
      case 0:
	result = true;
	break;
      case 1:
	result = columnIndex == 1;
	break;
      case 6:
	result = true;
	break;
      }
    return result;
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex)
  {
    Informacao val = getInformacaoAt (rowIndex, columnIndex);
    val.setConteudo ((String) aValue);
  }
  
  public String getColumnName (int column)
  {
    switch (column)
      {
      case 0:
	return "M\u00eas";
      case 1:
	return "Janeiro";
      case 2:
	return "Fevereiro";
      case 3:
	return "Mar\u00e7o";
      case 4:
	return "Abril";
      case 5:
	return "Maio";
      case 6:
	return "Junho";
      case 7:
	return "Julho";
      case 8:
	return "Agosto";
      case 9:
	return "Setembro";
      case 10:
	return "Outubro";
      case 11:
	return "Novembro";
      case 12:
	return "Dezembro";
      default:
	return null;
      }
  }
  
  public int getColumnCount ()
  {
    return 13;
  }
  
  public int getRowCount ()
  {
    return 7;
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    JLabel label = new JLabel ();
    JPanel panel = new JPanel (new BorderLayout ());
    panel.setBorder (BorderFactory.createEtchedBorder ());
    if (columnIndex == 0)
      {
	switch (rowIndex)
	  {
	  case 0:
	    label.setText ("<html><p>Resultado l\u00edquido do m\u00eas</p></html>");
	    break;
	  case 1:
	    label.setText ("<html><p>Resultado negativo at\u00e9 o m\u00eas anterior</p></html>");
	    break;
	  case 2:
	    label.setText ("<html><p>Base de c\u00e1lculo do imposto</p></html>");
	    break;
	  case 3:
	    label.setText ("<html><p>Preju\u00edzo a compensar</p></html>");
	    break;
	  case 4:
	    label.setText ("<html><p>Al\u00edquota do imposto</p></html>");
	    break;
	  case 5:
	    label.setText ("<html><p>Imposto devido</p></html>");
	    break;
	  case 6:
	    label.setText ("<html><p>Imposto pago</p></html>");
	    break;
	  }
	panel.add (label);
	Dimension dim = panel.getPreferredSize ();
	panel.setMinimumSize (new Dimension (dim.width, 50));
	panel.setPreferredSize (new Dimension (dim.width, 50));
	panel.setMaximumSize (new Dimension (dim.width, 50));
	panel.setSize (new Dimension (dim.width, 50));
	return panel;
      }
    return getInformacaoAt (rowIndex, columnIndex).getConteudoFormatado ();
  }
  
  public void setObjetoNegocio (ObjetoNegocio pObj)
  {
    setFundosInvestimentos ((FundosInvestimentos) pObj);
  }
  
  private void setFundosInvestimentos (FundosInvestimentos investimentos)
  {
    FundosInvestimentos fundosinvestimentos = fundosInvestimentos;
    Class[] var_classes = new Class[1];
    int i = 0;
    Class var_class = serpro.ppgd.gui.EditValor.class;
    var_classes[i] = var_class;
    fundosinvestimentos.removeObservadores (var_classes);
    fundosInvestimentos = investimentos;
    fireTableDataChanged ();
  }
}
