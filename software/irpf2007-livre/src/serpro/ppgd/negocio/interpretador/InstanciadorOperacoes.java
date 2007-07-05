/* InstanciadorOperacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.interpretador;
import java.util.ArrayList;

import serpro.ppgd.negocio.util.LogPPGD;

public class InstanciadorOperacoes extends Thread
{
  private ArrayList listaProximos = new ArrayList ();
  private Integer lockLista = new Integer (0);
  private static InstanciadorOperacoes instance = new InstanciadorOperacoes ();
  public static final int INICIADO = 0;
  public static final int PAUSADO = 1;
  public static final int FINALIZADO = 2;
  private long cacherSleep = 200L;
  private transient int estado = 2;
  
  /**
   * @deprecated
   */
  public static InstanciadorOperacoes getInstance ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  public static InstanciadorOperacoes getInstancia ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  public void setIntervaloInstanciacao (int milissegundos)
  {
    cacherSleep = (long) milissegundos;
  }
  
  private InstanciadorOperacoes ()
  {
    /* empty */
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
			InterpretadorOperacoes interpretadorOperacoes = (InterpretadorOperacoes) listaProximos.remove (0);
			interpretadorOperacoes.traduzXML ();
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
    LogPPGD.debug ("############ Iniciando Intanciador de opera\u00e7\u00f5es ##############");
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
  
  private void reiniciaCache ()
  {
    Integer integer;
    synchronized (integer = lockLista)
      {
	listaProximos.clear ();
      }
  }
  
  public void instanciaOperacao (InterpretadorOperacoes op)
  {
    Integer integer;
    synchronized (integer = lockLista)
      {
	listaProximos.add (op);
      }
  }
}
