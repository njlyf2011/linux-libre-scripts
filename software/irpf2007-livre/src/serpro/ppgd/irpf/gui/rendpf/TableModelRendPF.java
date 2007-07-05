/* TableModelRendPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpf;
import serpro.ppgd.irpf.gui.IRPFTableModel;
import serpro.ppgd.irpf.rendpf.MesRendPF;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;

public class TableModelRendPF extends IRPFTableModel
{
  private RendPF rendPF = null;
  private static final String TIT_MES = "<html><center>M\u00eas</center></html>";
  private static final String TIT_PESSOA_FISICA = "<html><center>Pessoa<BR>F\u00edsica</center></html>";
  private static final String TIT_EXTERIOR = "<html><center>Exterior</center></html>";
  private static final String TIT_PREVIDENCIA_OFICIAL = "<html><center>Previd\u00eancia<BR>oficial</center></html>";
  private static final String TIT_DEPENDENTES = "<html><center>Dependentes</center></html>";
  private static final String TIT_PENSAO = "<html><center>Pens\u00e3o<BR>Aliment\u00edcia</center></html>";
  private static final String TIT_LIVRO_CAIXA = "<html><center>Livro Caixa</center></html>";
  private static final String TIT_DARF = "<html><center>Darf pago<BR>c\u00f3d. 0190</center></html>";
  
  public TableModelRendPF (RendPF pRend)
  {
    super ((ObjetoNegocio) pRend);
    rendPF = pRend;
  }
  
  public int getRowCount ()
  {
    return 13;
  }
  
  public int getColumnCount ()
  {
    return 8;
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    if (columnIndex == 0)
      return getCelulaMes (rowIndex);
    return getInformacaoAt (rowIndex, columnIndex).getConteudoFormatado ();
  }
  
  public String getColumnName (int column)
  {
    switch (column)
      {
      case 1:
	return "<html><center>Pessoa<BR>F\u00edsica</center></html>";
      case 2:
	return "<html><center>Exterior</center></html>";
      case 3:
	return "<html><center>Previd\u00eancia<BR>oficial</center></html>";
      case 4:
	return "<html><center>Dependentes</center></html>";
      case 5:
	return "<html><center>Pens\u00e3o<BR>Aliment\u00edcia</center></html>";
      case 6:
	return "<html><center>Livro Caixa</center></html>";
      case 7:
	return "<html><center>Darf pago<BR>c\u00f3d. 0190</center></html>";
      default:
	return "";
      }
  }
  
  public Informacao getInformacaoAt (int row, int col)
  {
    MesRendPF mes = null;
    switch (row)
      {
      case 0:
	mes = rendPF.getJaneiro ();
	break;
      case 1:
	mes = rendPF.getFevereiro ();
	break;
      case 2:
	mes = rendPF.getMarco ();
	break;
      case 3:
	mes = rendPF.getAbril ();
	break;
      case 4:
	mes = rendPF.getMaio ();
	break;
      case 5:
	mes = rendPF.getJunho ();
	break;
      case 6:
	mes = rendPF.getJulho ();
	break;
      case 7:
	mes = rendPF.getAgosto ();
	break;
      case 8:
	mes = rendPF.getSetembro ();
	break;
      case 9:
	mes = rendPF.getOutubro ();
	break;
      case 10:
	mes = rendPF.getNovembro ();
	break;
      case 11:
	mes = rendPF.getDezembro ();
	break;
      case 12:
	switch (col)
	  {
	  case 1:
	    return rendPF.getTotalPessoaFisica ();
	  case 2:
	    return rendPF.getTotalExterior ();
	  case 3:
	    return rendPF.getTotalPrevidencia ();
	  case 4:
	    return rendPF.getTotalDependentes ();
	  case 5:
	    return rendPF.getTotalPensao ();
	  case 6:
	    return rendPF.getTotalLivroCaixa ();
	  case 7:
	    return rendPF.getTotalDarfPago ();
	  default:
	    break;
	  }
	break;
      }
    switch (col)
      {
      case 1:
	return mes.getPessoaFisica ();
      case 2:
	return mes.getExterior ();
      case 3:
	return mes.getPrevidencia ();
      case 4:
	return mes.getDependentes ();
      case 5:
	return mes.getPensao ();
      case 6:
	return mes.getLivroCaixa ();
      case 7:
	return mes.getDarfPago ();
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
  
  public void setRendPF (RendPF pRendPF)
  {
    RendPF rendpf = rendPF;
    Class[] var_classes = new Class[1];
    int i = 0;
    Class var_class = serpro.ppgd.gui.EditValor.class;
    var_classes[i] = var_class;
    rendpf.removeObservadores (var_classes);
    rendPF = pRendPF;
    fireTableDataChanged ();
  }
  
  public void setObjetoNegocio (ObjetoNegocio pObj)
  {
    setRendPF ((RendPF) pObj);
  }
  
  public ObjetoNegocio getObjetoNegocio ()
  {
    return rendPF;
  }
}
