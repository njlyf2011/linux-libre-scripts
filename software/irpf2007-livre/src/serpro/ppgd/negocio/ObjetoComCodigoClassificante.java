/* ObjetoComCodigoClassificante - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.util.List;

public abstract class ObjetoComCodigoClassificante extends ObjetoNegocio implements Comparable
{
  protected Codigo codigo;
  
  public ObjetoComCodigoClassificante (IdDeclaracao idDeclaracao, String nomeCampo, List colecao)
  {
    super (idDeclaracao);
    codigo = new Codigo (this, nomeCampo, colecao);
  }
  
  public Codigo getCodigo ()
  {
    return codigo;
  }
  
  public int compareTo (Object outro)
  {
    return 0;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List listaCamposPendencia = super.recuperarListaCamposPendencia ();
    listaCamposPendencia.add (codigo);
    return listaCamposPendencia;
  }
}
