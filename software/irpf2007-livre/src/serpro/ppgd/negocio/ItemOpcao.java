/* ItemOpcao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

public class ItemOpcao
{
  private String codigo;
  private String descricao;
  private boolean isSelecionado;
  
  public ItemOpcao (String codigo, String descricao)
  {
    this.codigo = codigo;
    this.descricao = descricao;
  }
  
  public ItemOpcao (String codigo, String descricao, boolean selecionado)
  {
    this.codigo = codigo;
    this.descricao = descricao;
    isSelecionado = selecionado;
  }
  
  public boolean isSelecionado ()
  {
    return isSelecionado;
  }
  
  public void setSelecionado (boolean isSelecionado)
  {
    this.isSelecionado = isSelecionado;
  }
  
  public String getCodigo ()
  {
    return codigo;
  }
  
  public void setCodigo (String codigo)
  {
    this.codigo = codigo;
  }
  
  public String getDescricao ()
  {
    return descricao;
  }
  
  public void setDescricao (String descricao)
  {
    this.descricao = descricao;
  }
}
