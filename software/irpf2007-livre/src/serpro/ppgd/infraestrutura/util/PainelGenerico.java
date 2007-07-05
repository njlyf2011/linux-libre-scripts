/* PainelGenerico - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.lang.reflect.Method;

import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.PPGDFacade;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public abstract class PainelGenerico extends PPGDFormPanel
{
  public PainelGenerico ()
  {
    iniciaComponentes ();
    adicionaComponentes ();
  }
  
  public abstract void iniciaComponentes ();
  
  public abstract void adicionaComponentes ();
  
  public ObjetoNegocio getDeclaracao ()
  {
    ObjetoNegocio objetonegocio;
    try
      {
	String classeFacade = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault");
	Class classe = Class.forName (classeFacade);
	Method methGetInstancia = classe.getMethod ("getInstancia", null);
	PPGDFacade facade = (PPGDFacade) methGetInstancia.invoke (null, null);
	Method methGetDeclaracao = classe.getMethod ("getDeclaracao", null);
	objetonegocio = (ObjetoNegocio) methGetDeclaracao.invoke (facade, null);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	return null;
      }
    return objetonegocio;
  }
}
