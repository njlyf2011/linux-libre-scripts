/* Alimentando - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.alimentandos;
import serpro.ppgd.gui.xbeans.JEditObjetoNegocioItemIf;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNome;

public class Alimentando extends ObjetoNegocio implements JEditObjetoNegocioItemIf
{
  public static final String PROP_CAMPO_NOME = "Nome";
  private String chave = "";
  private Alfa nome = new Alfa (this, "Nome", 60);
  
  public Alimentando ()
  {
    getNome ().addValidador (new ValidadorNome ((byte) 3));
  }
  
  public Alfa getNome ()
  {
    return nome;
  }
  
  public String toString ()
  {
    return nome.asString ();
  }
  
  public String getConteudo (int i)
  {
    return getNome ().getConteudoFormatado ();
  }
  
  public int getTotalAtributos ()
  {
    return 1;
  }
  
  public int getColunaFiltro ()
  {
    return 0;
  }
  
  public String getChave ()
  {
    return chave;
  }
  
  public void setChave (String chave)
  {
    this.chave = chave;
  }
}
