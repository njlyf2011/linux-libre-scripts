/* TableModelMovimentacaoRebanhoBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.atividaderural;
import serpro.ppgd.irpf.atividaderural.ItemMovimentacaoRebanho;
import serpro.ppgd.irpf.atividaderural.MovimentacaoRebanho;
import serpro.ppgd.irpf.gui.IRPFTableModel;
import serpro.ppgd.negocio.Informacao;

public class TableModelMovimentacaoRebanhoBrasil extends IRPFTableModel
{
  private final String TIT_COD = "<html><center>Cod</center></html>";
  private final String TIT_ESPECIE = "<html><center>Esp\u00e9cie</center></html>";
  private final String TIT_ESTOQUE_INICIAL = "<html><center>Estoque <br>inicial</center></html>";
  private final String TIT_AQUISICOES_ANO = "<html><center>Aquisi\u00e7\u00f5es <br>no ano</center></html>";
  private final String TIT_NASCIDOS_ANO = "<html><center>Nascidos <br>no ano</center></html>";
  private final String TIT_CONSUMO_PERDAS = "<html><center>Consumo e <br>perdas</center></html>";
  private final String TIT_VENDAS_ANO = "<html><center>Vendas no <br>ano</center></html>";
  private final String TIT_ESTOQUE_FINAL = "<html><center>Estoque <br>final</center></html>";
  private final String[] titulos = { "<html><center>Cod</center></html>", "<html><center>Esp\u00e9cie</center></html>", "<html><center>Estoque <br>inicial</center></html>", "<html><center>Aquisi\u00e7\u00f5es <br>no ano</center></html>", "<html><center>Nascidos <br>no ano</center></html>", "<html><center>Consumo e <br>perdas</center></html>", "<html><center>Vendas no <br>ano</center></html>", "<html><center>Estoque <br>final</center></html>" };
  private MovimentacaoRebanho movimentacao = null;
  
  public TableModelMovimentacaoRebanhoBrasil (MovimentacaoRebanho pObj)
  {
    super ((serpro.ppgd.negocio.ObjetoNegocio) pObj);
    movimentacao = pObj;
    if (pObj == null)
      throw new RuntimeException ("Obj negocio nulo");
  }
  
  public MovimentacaoRebanho getMovimentacao ()
  {
    return movimentacao;
  }
  
  public void setMovimentacao (MovimentacaoRebanho pMovimentacao)
  {
    movimentacao = pMovimentacao;
  }
  
  public Informacao getInformacaoAt (int row, int col)
  {
    ItemMovimentacaoRebanho item = null;
    switch (row)
      {
      case 0:
	item = movimentacao.getBovinos ();
	break;
      case 1:
	item = movimentacao.getSuinos ();
	break;
      case 2:
	item = movimentacao.getCaprinos ();
	break;
      case 3:
	item = movimentacao.getAsininos ();
	break;
      case 4:
	item = movimentacao.getOutros ();
	break;
      }
    switch (col)
      {
      case 2:
	return item.getEstoqueInicial ();
      case 3:
	return item.getAquisicoesAno ();
      case 4:
	return item.getNascidosAno ();
      case 5:
	return item.getConsumo ();
      case 6:
	return item.getVendas ();
      case 7:
	return item.getEstoqueFinal ();
      default:
	return null;
      }
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    if (columnIndex > 1 && columnIndex < getColumnCount () - 1)
      return true;
    return false;
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex)
  {
    if (columnIndex > 1 && columnIndex < getColumnCount () - 1)
      {
	Informacao val = getInformacaoAt (rowIndex, columnIndex);
	val.setConteudo ((String) aValue);
      }
  }
  
  public String getColumnName (int column)
  {
    return titulos[column];
  }
  
  public int getColumnCount ()
  {
    return titulos.length;
  }
  
  public int getRowCount ()
  {
    return 5;
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    if (columnIndex == 0)
      return "" + rowIndex;
    if (columnIndex == 1)
      {
	switch (rowIndex)
	  {
	  case 0:
	    return "Bovinos e bufalinos";
	  case 1:
	    return "Su\u00ednos";
	  case 2:
	    return "Caprinos e ovinos";
	  case 3:
	    return "Asininos, equinos e muares";
	  case 4:
	    return "Outros";
	  default:
	    break;
	  }
      }
    else
      {
	Informacao valor = getInformacaoAt (rowIndex, columnIndex);
	return valor.getConteudoFormatado ();
      }
    return null;
  }
}
