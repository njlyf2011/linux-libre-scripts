/* SalvarDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.repositorio.util.FabricaRepositorio;

public class SalvarDeclaracao extends Thread
{
  private Integer lockSalvamento = new Integer (0);
  private static SalvarDeclaracao instance = new SalvarDeclaracao ();
  public static final int INICIADO = 0;
  public static final int SALVANDO = 1;
  private transient int estado = 0;
  
  /**
   * @deprecated
   */
  public static SalvarDeclaracao getInstance ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  public static SalvarDeclaracao getInstancia ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  private SalvarDeclaracao ()
  {
    /* empty */
  }
  
  public void run ()
  {
    for (;;)
      {
	try
	  {
	    if (estado == 1)
	      {
		Integer integer;
		synchronized (integer = lockSalvamento)
		  {
		    try
		      {
			FabricaRepositorio.getCadastroId ().salvarUltimaDeclaracaoAberta ();
		      }
		    catch (RepositorioException e)
		      {
			FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
		      }
		    estado = 0;
		  }
	      }
	    aguarda ();
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
      }
  }
  
  public synchronized void salva ()
  {
    estado = 1;
    this.notify ();
  }
  
  private synchronized void aguarda ()
  {
    try
      {
	this.wait ();
      }
    catch (InterruptedException e)
      {
	e.printStackTrace ();
      }
  }
}
