/* PreenchedorCodigo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.util.ArrayList;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ElementoTabela;

public class PreenchedorCodigo
{
  private Codigo codigo;
  private int colAtual = 0;
  private ElementoTabela elem = new ElementoTabela ();
  private ArrayList lista = new ArrayList ();
  
  public PreenchedorCodigo ()
  {
    this (new Codigo ());
  }
  
  public PreenchedorCodigo (Codigo aCodigo)
  {
    codigo = aCodigo;
  }
  
  public PreenchedorCodigo add (String aStr)
  {
    elem.setConteudo (colAtual++, aStr);
    return this;
  }
  
  /**
   * @deprecated
   */
  public PreenchedorCodigo proximaLinha ()
  {
    lista.add (elem);
    elem = new ElementoTabela ();
    colAtual = 0;
    return this;
  }
  
  public PreenchedorCodigo EOL ()
  {
    lista.add (elem);
    elem = new ElementoTabela ();
    colAtual = 0;
    return this;
  }
  
  public Codigo getCodigoPreenchido ()
  {
    aplicaAlteracoes ();
    return codigo;
  }
  
  public void aplicaAlteracoes ()
  {
    codigo.getColecaoElementoTabela ().addAll (lista);
  }
}
