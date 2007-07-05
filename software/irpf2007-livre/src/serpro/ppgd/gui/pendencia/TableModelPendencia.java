/* TableModelPendencia - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.pendencia;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.util.UtilitariosString;

public class TableModelPendencia extends AbstractTableModel
{
  protected String[] nomeColunas;
  protected List lstPendencia;
  protected Vector lstPendenciaTable;
  protected int numMinLinhas;
  protected int numLinhasTitulo;
  protected int totalAvisos;
  protected int totalErros;
  protected int[] tamColunas;
  protected int posicaoQuebraDeLinhaMensagem = 90;
  
  public TableModelPendencia (String[] nomeColunas, int numMinLinhas, int[] pTamColunas, List lstPendencia)
  {
    this.nomeColunas = nomeColunas;
    this.lstPendencia = lstPendencia;
    lstPendenciaTable = montaTableListaPendencia (lstPendencia);
    this.numMinLinhas = numMinLinhas;
    tamColunas = pTamColunas;
  }
  
  public int getColumnCount ()
  {
    return nomeColunas.length;
  }
  
  public int getRowCount ()
  {
    if (numMinLinhas > lstPendenciaTable.size ())
      return numMinLinhas;
    return lstPendenciaTable.size ();
  }
  
  public String getColumnName (int col)
  {
    return nomeColunas[col];
  }
  
  public Object getValueAt (int row, int col)
  {
    LinhaPendencia linhaPendencia = getLinhaPendenciaAt (row);
    if (linhaPendencia != null)
      {
	switch (col)
	  {
	  case 0:
	    return linhaPendencia.getTipo ();
	  case 1:
	    {
	      if (linhaPendencia.getSeveridade () == 0)
		{
		  String texto = linhaPendencia.getCampo ();
		  texto = texto.replaceAll ("<BR>", " ");
		  texto = texto.replaceAll ("\n", " ");
		  texto = texto.replaceAll ("<HTML>", " ");
		  texto = texto.replaceAll ("</HTML>", " ");
		  texto = "<HTML><LEFT>" + UtilitariosString.insereQuebraDeLinha (texto, getPosicaoQuebraDeLinhaMensagem () - 15, "<BR>") + "</LEFT></HTML>";
		  return "<html><b>" + texto + "</b></html>";
		}
	      String texto = linhaPendencia.getCampo ();
	      texto = texto.replaceAll ("<BR>", " ");
	      texto = texto.replaceAll ("\n", " ");
	      texto = texto.replaceAll ("<HTML>", " ");
	      texto = texto.replaceAll ("</HTML>", " ");
	      texto = "<HTML><LEFT>" + UtilitariosString.insereQuebraDeLinha (texto, getPosicaoQuebraDeLinhaMensagem (), "<BR>") + "</LEFT></HTML>";
	      return texto;
	    }
	  }
      }
    return "";
  }
  
  public Class getColumnClass (int c)
  {
    return getValueAt (0, c).getClass ();
  }
  
  public Pendencia getPendenciaAt (int index)
  {
    LinhaPendencia linha = getLinhaPendenciaAt (index);
    if (linha == null)
      return null;
    return linha.getPendencia ();
  }
  
  public LinhaPendencia getLinhaPendenciaAt (int index)
  {
    if (index < lstPendenciaTable.size ())
      return (LinhaPendencia) lstPendenciaTable.get (index);
    return null;
  }
  
  public int getTotalAvisos ()
  {
    return totalAvisos;
  }
  
  public int getTotalErros ()
  {
    return totalErros;
  }
  
  public Vector getListaPendencias ()
  {
    return lstPendenciaTable;
  }
  
  protected Vector montaTableListaPendencia (List lstPendencia)
  {
    int contaLinhasTitulo = 0;
    Vector listaPendencia = new Vector (lstPendencia.size ());
    String tituloFichaAnterior = "";
    String item = "";
    String itemPendente = "";
    for (int i = 0; i < lstPendencia.size (); i++)
      {
	if (lstPendencia.get (i) != null)
	  {
	    Pendencia pendencia = (Pendencia) lstPendencia.get (i);
	    String tituloFichaAtual = pendencia.getCampoInformacao ().getFicha ();
	    byte severidade = pendencia.getSeveridade ();
	    if (! tituloFichaAtual.equals (tituloFichaAnterior))
	      {
		if (severidade == 2 || severidade == 3 || severidade == 5)
		  listaPendencia.add (new LinhaPendencia ((byte) 0, tituloFichaAtual, null));
		tituloFichaAnterior = tituloFichaAtual;
		contaLinhasTitulo++;
	      }
	    if ((i == 0 || tituloFichaAtual.equals (tituloFichaAnterior)) && (i <= 0 || pendencia.getCampoInformacao () != ((Pendencia) lstPendencia.get (i - 1)).getCampoInformacao ()))
	      {
		item = "";
		if (pendencia.getNumItem () >= 1)
		  item = getLabelNumeracaoItem (pendencia);
		itemPendente = pendencia.getMsg () + item;
		if (! itemPendente.equals (""))
		  {
		    switch (severidade)
		      {
		      case 2:
			totalAvisos++;
			break;
		      case 3:
			totalErros++;
			break;
		      case 5:
			totalErros++;
			break;
		      }
		    if (severidade == 2 || severidade == 3 || severidade == 5)
		      listaPendencia.add (new LinhaPendencia (severidade, itemPendente, pendencia));
		  }
	      }
	  }
      }
    numLinhasTitulo = contaLinhasTitulo;
    return listaPendencia;
  }
  
  public int[] getTamColunas ()
  {
    return tamColunas;
  }
  
  public void setTamColunas (int[] tamColunas)
  {
    this.tamColunas = tamColunas;
  }
  
  public int getNumMinLinhas ()
  {
    return numMinLinhas;
  }
  
  public void setNumMinLinhas (int numMinLinhas)
  {
    this.numMinLinhas = numMinLinhas;
  }
  
  public void atualizaPendencias (List pLstPendencia)
  {
    lstPendencia = pLstPendencia;
    totalAvisos = 0;
    totalErros = 0;
    lstPendenciaTable = montaTableListaPendencia (lstPendencia);
    fireTableDataChanged ();
  }
  
  public int getPosicaoQuebraDeLinhaMensagem ()
  {
    return posicaoQuebraDeLinhaMensagem;
  }
  
  public void setPosicaoQuebraDeLinhaMensagem (int posicaoQuebraDeLinhaMensagem)
  {
    this.posicaoQuebraDeLinhaMensagem = posicaoQuebraDeLinhaMensagem;
  }
  
  public String getLabelNumeracaoItem (Pendencia pendencia)
  {
    return " - item no. " + pendencia.getNumItem ();
  }
}
