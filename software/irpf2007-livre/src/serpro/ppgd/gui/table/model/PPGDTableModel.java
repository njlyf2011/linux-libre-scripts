/* PPGDTableModel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.table.model;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.ElementoTabela;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class PPGDTableModel extends AbstractTableModel
{
  protected Colecao colecao;
  protected int tamanhoMaximoGrid;
  protected LinkedList atributos = new LinkedList ();
  private ObjetoNegocio objetoNegocio;
  
  public void setColecao (Colecao pCol)
  {
    colecao = pCol;
    Iterator itCampos = FabricaUtilitarios.getFieldsCamposInformacao (colecao.getTipoItens ()).iterator ();
    while (itCampos.hasNext ())
      {
	Field field = (Field) itCampos.next ();
	Class var_class = field.getType ();
	Class var_class_0_ = serpro.ppgd.negocio.Logico.class;
	if (var_class != var_class_0_)
	  {
	    Class var_class_2_ = field.getType ();
	    Class var_class_3_ = serpro.ppgd.negocio.Opcao.class;
	    if (var_class_2_ != var_class_3_)
	      atributos.addLast (field);
	  }
      }
  }
  
  protected ObjetoNegocio instanciaItem ()
  {
    ObjetoNegocio retorno = null;
    try
      {
	Class[] var_classes = new Class[1];
	int i = 0;
	Class var_class = serpro.ppgd.negocio.IdDeclaracao.class;
	var_classes[i] = var_class;
	Class[] argumentosFormais = var_classes;
	Object[] argumentosReais = { colecao.getIdDeclaracao () };
	Constructor construtor = null;
	try
	  {
	    construtor = colecao.getTipoItens ().getConstructor (argumentosFormais);
	    retorno = (ObjetoNegocio) construtor.newInstance (argumentosReais);
	  }
	catch (Exception e)
	  {
	    retorno = (ObjetoNegocio) colecao.getTipoItens ().newInstance ();
	  }
	retorno.setFicha (colecao.getFicha ());
      }
    catch (IllegalAccessException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
    catch (InstantiationException e)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
    return retorno;
  }
  
  public int getColumnCount ()
  {
    int cols = atributos.size ();
    return cols;
  }
  
  public int getRowCount ()
  {
    return tamanhoMaximoGrid;
  }
  
  public Object getValueAt (int rowIndex, int columnIndex)
  {
    Informacao info = getAtributoInformacao (rowIndex, columnIndex);
    if (info.isVazio ())
      return "";
    return info.getConteudoFormatado ();
  }
  
  public Informacao getAtributoInformacao (int pRow, int pCol)
  {
    ObjetoNegocio item = null;
    try
      {
	item = (ObjetoNegocio) colecao.recuperarLista ().get (pRow);
      }
    catch (Exception e)
      {
	item = instanciaItem ();
	colecao.recuperarLista ().add (item);
      }
    Informacao info = (Informacao) FabricaUtilitarios.getValorField ((Field) atributos.get (pCol), item);
    return info;
  }
  
  public boolean isCellEditable (int rowIndex, int columnIndex)
  {
    boolean editavel = ! getAtributoInformacao (rowIndex, columnIndex).isReadOnly ();
    return editavel;
  }
  
  public String getColumnName (int column)
  {
    if (objetoNegocio == null)
      objetoNegocio = instanciaItem ();
    Informacao info = (Informacao) FabricaUtilitarios.getValorField ((Field) atributos.get (column), objetoNegocio);
    return info.getNomeCampo ();
  }
  
  public void setValueAt (Object aValue, int rowIndex, int columnIndex)
  {
    if (aValue instanceof ElementoTabela)
      getAtributoInformacao (rowIndex, columnIndex).setConteudo (((ElementoTabela) aValue).getConteudo (0));
    else
      getAtributoInformacao (rowIndex, columnIndex).setConteudo ((String) aValue);
  }
  
  public int getTamanhoMaximoGrid ()
  {
    return tamanhoMaximoGrid;
  }
  
  public void setTamanhoMaximoGrid (int tamanhoMaximoGrid)
  {
    this.tamanhoMaximoGrid = tamanhoMaximoGrid;
  }
  
  public LinkedList getAtributos ()
  {
    return atributos;
  }
  
  public void setAtributos (LinkedList atributos)
  {
    this.atributos = atributos;
  }
  
  public void limparLinha (int pRow)
  {
    if (pRow != -1)
      {
	for (int i = 0; i < atributos.size (); i++)
	  setValueAt ("", pRow, i);
      }
  }
  
  public void preencheComItemsVazios ()
  {
    for (int i = 0; i < tamanhoMaximoGrid; i++)
      getAtributoInformacao (i, 0);
  }
  
  public Colecao getColecao ()
  {
    return colecao;
  }
}
