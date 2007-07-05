/* ProcessosAssincronos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.util.LinkedList;
import java.util.List;

public class ProcessosAssincronos extends Thread
{
  private List listaProximos = new LinkedList ();
  private Integer lockLista = new Integer (0);
  private static ProcessosAssincronos instance = new ProcessosAssincronos ();
  public static final int INICIADO = 0;
  public static final int PAUSADO = 1;
  public static final int FINALIZADO = 2;
  private long cacherSleep;
  private transient int estado = 2;
  
  /**
   * @deprecated
   */
  public static ProcessosAssincronos getInstance ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  public static ProcessosAssincronos getInstancia ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  private ProcessosAssincronos ()
  {
    cacherSleep = Long.parseLong (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.infraestrutura.processosassincronos.intervalo", "500"));
  }
  
  public void run ()
  {
    for (;;)
      {
	try
	  {
	    if (estado == 0)
	      {
		Integer integer;
		synchronized (integer = lockLista)
		  {
		    if (listaProximos.size () > 0)
		      {
			ProcessoAssincronoIf processo = (ProcessoAssincronoIf) listaProximos.remove (0);
			processo.executa ();
		      }
		  }
	      }
	    sleep (cacherSleep);
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
      }
  }
  
  public void inicia ()
  {
    LogPPGD.debug ("############ Iniciando PainelCacher ##############");
    estado = 0;
  }
  
  public void pausa ()
  {
    estado = 1;
  }
  
  public void encerra ()
  {
    LogPPGD.debug ("############ Encerrando PainelCacher ##############");
    estado = 2;
    reiniciaCache ();
  }
  
  public void esvaziaCache ()
  {
    estado = 2;
    Integer integer;
    synchronized (integer = lockLista)
      {
	listaProximos.clear ();
      }
  }
  
  private void reiniciaCache ()
  {
    Integer integer;
    synchronized (integer = lockLista)
      {
	listaProximos.clear ();
      }
  }
  
  public void adicionaTarefa (ProcessoAssincronoIf aParam)
  {
    Integer integer;
    synchronized (integer = lockLista)
      {
	listaProximos.add (aParam);
      }
  }
}
