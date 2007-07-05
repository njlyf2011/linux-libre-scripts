/* Alfa - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

public class Alfa extends Informacao
{
  protected int maximoCaracteres = 1;
  
  public Alfa ()
  {
    /* empty */
  }
  
  public Alfa (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public Alfa (ObjetoNegocio owner, String nomeCampo, int maximoCaracteres)
  {
    super (owner, nomeCampo);
    setMaximoCaracteres (maximoCaracteres);
  }
  
  public Alfa (String nomeCampo)
  {
    super (nomeCampo);
  }
  
  public String getConteudoFormatado ()
  {
    return asString ();
  }
  
  public int getMaximoCaracteres ()
  {
    return maximoCaracteres;
  }
  
  public void setMaximoCaracteres (int maximoCaracteres)
  {
    this.maximoCaracteres = maximoCaracteres;
  }
}
