/* TableModelReceitasDespesasBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import serpro.ppgd.irpf.atividaderural.brasil.MesReceitaDespesa;
import serpro.ppgd.irpf.atividaderural.brasil.ReceitasDespesas;
import serpro.ppgd.irpf.gui.IRPFTableModel;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;

public class TableModelReceitasDespesasBrasil extends IRPFTableModel
{
  private static final String TIT_MES = "<html><center>M\u00eas</center></html>";
  private static final String TIT_RECEITA_BRUTA = "<html><center>Receita bruta mensal</center></html>";
  private static final String TIT_DESPESAS = "<html><center>Despesas de custeio <br>e investimento</center></html>";
  private ReceitasDespesas receitasDespesas;
  
  public TableModelReceitasDespesasBrasil (ReceitasDespesas aReceitasDespesas)
  {
    super ((ObjetoNegocio) aReceitasDespesas);
    receitasDespesas = aReceitasDespesas;
  }
  
  public void setObjetoNegocio (ObjetoNegocio pObj)
  {
    super.setObjetoNegocio (pObj);
    receitasDespesas = (ReceitasDespesas) pObj;
  }
  
  public Informacao getInformacaoAt (int row, int col)
  {
    MesReceitaDespesa mes = null;
    switch (row)
      {
      case 0:
	mes = receitasDespesas.getJaneiro ();
	break;
      case 1:
	mes = receitasDespesas.getFevereiro ();
	break;
      case 2:
	mes = receitasDespesas.getMarco ();
	break;
      case 3:
	mes = receitasDespesas.getAbril ();
	break;
      case 4:
	mes = receitasDespesas.getMaio ();
	break;
      case 5:
	mes = receitasDespesas.getJunho ();
	break;
      case 6:
	mes = receitasDespesas.getJulho ();
	break;
      case 7:
	mes = receitasDespesas.getAgosto ();
	break;
      case 8:
	mes = receitasDespesas.getSetembro ();
	break;
      case 9:
	mes = receitasDespesas.getOutubro ();
	break;
      case 10:
	mes = receitasDespesas.getNovembro ();
	break;
      case 11:
	mes = receitasDespesas.getDezembro ();
	break;
      case 12:
	if (col == 1)
	  return receitasDespesas.getTotalReceita ();
	return receitasDespesas.getTotalDespesas ();
      }
    switch (col)
      {
      case 1:
	return mes.getReceitaBrutaMensal ();
      case 2:
	return mes.getDespesaCusteioInvestimento ();
      default:
	return null;
      }
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    if (rowIndex < getRowCount () - 1 && columnIndex > 0)
      return true;
    return false;
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex)
  {
    if (rowIndex < getRowCount () - 1 && columnIndex > 0)
      {
	Informacao val = getInformacaoAt (rowIndex, columnIndex);
	val.setConteudo ((String) aValue);
      }
  }
  
  public String getColumnName (int column)
  {
    switch (column)
      {
      case 0:
	return "<html><center>M\u00eas</center></html>";
      case 1:
	return "<html><center>Receita bruta mensal</center></html>";
      case 2:
	return "<html><center>Despesas de custeio <br>e investimento</center></html>";
      default:
	return "";
      }
  }
  
  public int getColumnCount ()
  {
    return 3;
  }
  
  public int getRowCount ()
  {
    return 13;
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    if (columnIndex == 0)
      return getCelulaMes (rowIndex);
    Informacao valor = getInformacaoAt (rowIndex, columnIndex);
    return valor.getConteudoFormatado ();
  }
}
