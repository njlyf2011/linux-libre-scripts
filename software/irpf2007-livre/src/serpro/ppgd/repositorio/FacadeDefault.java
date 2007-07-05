/* FacadeDefault - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.PPGDFacade;
import serpro.ppgd.repositorio.util.FabricaRepositorio;

public class FacadeDefault implements PPGDFacade
{
  private static FacadeDefault instancia = new FacadeDefault ();
  
  private FacadeDefault ()
  {
    /* empty */
  }
  
  public static PPGDFacade getInstancia ()
  {
    return instancia;
  }
  
  public ObjetoNegocio getDeclaracao ()
  {
    ObjetoNegocio obj = null;
    try
      {
	obj = FabricaRepositorio.getCadastroId ().getUltimaDeclaracaoAberta ();
      }
    catch (RepositorioException e)
      {
	e.printStackTrace ();
      }
    return obj;
  }
  
  public void salvaUltimaDeclaracaoAberta ()
  {
    try
      {
	FabricaRepositorio.getCadastroId ().salvarUltimaDeclaracaoAberta ();
      }
    catch (RepositorioException repositorioexception)
      {
	/* empty */
      }
  }
  
  public boolean existeDeclaracoes ()
  {
    return FabricaRepositorio.getCadastroId ().recuperarIdDeclaracoes ().size () > 0;
  }
}
