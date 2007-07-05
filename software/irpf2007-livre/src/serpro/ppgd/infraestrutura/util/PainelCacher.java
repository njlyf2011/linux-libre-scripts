/* PainelCacher - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.treeview.ArvoreGenerica;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class PainelCacher extends Thread
{
  private List listaNomeProximos = new LinkedList ();
  private List listaNomeTodos = new LinkedList ();
  private Hashtable cache = new Hashtable ();
  private Integer lockLista = new Integer (0);
  private static PainelCacher instance = new PainelCacher ();
  public static final int INICIADO = 0;
  public static final int PAUSADO = 1;
  public static final int FINALIZADO = 2;
  private long cacherSleep;
  private transient int estado = 2;
  private ArvoreGenerica arvorePlataforma = (ArvoreGenerica) PlataformaPPGD.getPlataforma ().getSwingEngine ().find ("ppgdarvore");
  
  private class ItemCache
  {
    private String nomeClasse;
    private Object param;
    
    public ItemCache (String nomeClasse)
    {
      setNomeClasse (nomeClasse);
    }
    
    public ItemCache (String nomeClasse, Object param)
    {
      setNomeClasse (nomeClasse);
      setParam (param);
    }
    
    public void setNomeClasse (String nomeClasse)
    {
      this.nomeClasse = nomeClasse;
    }
    
    public String getNomeClasse ()
    {
      return nomeClasse;
    }
    
    public void setParam (Object param)
    {
      this.param = param;
    }
    
    public Object getParam ()
    {
      return param;
    }
    
    public boolean equals (Object obj)
    {
      if (obj instanceof ItemCache)
	{
	  ItemCache outroItem = (ItemCache) obj;
	  return getNomeClasse ().equals (outroItem.getNomeClasse ());
	}
      return false;
    }
  }
  
  /**
   * @deprecated
   */
  public static PainelCacher getInstance ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  public static PainelCacher getInstancia ()
  {
    if (! instance.isAlive ())
      {
	instance.start ();
	instance.setPriority (1);
      }
    return instance;
  }
  
  private PainelCacher ()
  {
    cacherSleep = Long.parseLong (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.infraestrutura.painelcacher.intervalo", "3000"));
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
		    if (listaNomeProximos.size () > 0)
		      {
			ItemCache item = (ItemCache) listaNomeProximos.remove (0);
			String painelStr = item.getNomeClasse ();
			JPanel painelInstanciado = (JPanel) cache.get (painelStr);
			if (painelInstanciado == null)
			  painelInstanciado = PlataformaPPGD.getPlataforma ().instanciaPainel (painelStr);
			if (painelInstanciado != null)
			  {
			    aplicaIdentificacao (painelStr, painelInstanciado);
			    cache.put (painelStr, painelInstanciado);
			  }
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
    LogPPGD.debug ("############ Esvaziando PainelCacher ##############");
    estado = 2;
    Integer integer;
    synchronized (integer = lockLista)
      {
	cache.clear ();
	listaNomeProximos.clear ();
	listaNomeTodos.clear ();
      }
  }
  
  private void reiniciaCache ()
  {
    Integer integer;
    synchronized (integer = lockLista)
      {
	cache.clear ();
	listaNomeProximos.clear ();
	listaNomeProximos.addAll (listaNomeTodos);
      }
  }
  
  public void fazCacheDe (String aPainelStr)
  {
    Integer integer;
    synchronized (integer = lockLista)
      {
	ItemCache item = new ItemCache (aPainelStr);
	listaNomeProximos.add (item);
	listaNomeTodos.add (item);
      }
  }
  
  public void fazCacheDe (String aPainelStr, Object aParam)
  {
    Integer integer;
    synchronized (integer = lockLista)
      {
	ItemCache item = new ItemCache (aPainelStr, aParam);
	listaNomeProximos.add (item);
	listaNomeTodos.add (item);
      }
  }
  
  public JPanel obtemUrgentemente (String aPainelStr)
  {
    JPanel painel = (JPanel) cache.get (aPainelStr);
    int estadoAnterior = estado;
    if (painel == null)
      {
	Integer integer;
	synchronized (integer = lockLista)
	  {
	    JPanel painelInstanciado = null;
	    try
	      {
		painelInstanciado = PlataformaPPGD.getPlataforma ().instanciaPainel (aPainelStr);
		aplicaIdentificacao (aPainelStr, painelInstanciado);
		cache.put (aPainelStr, painelInstanciado);
		listaNomeProximos.remove (aPainelStr);
	      }
	    catch (Exception e)
	      {
		LogPPGD.erro ("O PainelCacher n\u00e3o conseguiu instanciar a classe " + aPainelStr + "!!");
		e.printStackTrace ();
	      }
	    painel = painelInstanciado;
	  }
      }
    estado = estadoAnterior;
    return painel;
  }
  
  public JPanel obtemUrgentemente (String aChave, String aPainelStr)
  {
    JPanel painel = (JPanel) cache.get (aChave);
    int estadoAnterior = estado;
    if (painel == null)
      {
	Integer integer;
	synchronized (integer = lockLista)
	  {
	    JPanel painelInstanciado = null;
	    try
	      {
		painelInstanciado = PlataformaPPGD.getPlataforma ().instanciaPainel (aPainelStr);
		aplicaIdentificacao (aChave, painelInstanciado);
		cache.put (aChave, painelInstanciado);
		listaNomeProximos.remove (aPainelStr);
	      }
	    catch (Exception e)
	      {
		e.printStackTrace ();
	      }
	    painel = painelInstanciado;
	  }
      }
    estado = estadoAnterior;
    return painel;
  }
  
  public boolean isEmCache (String aPainelStr)
  {
    return cache.get (aPainelStr) != null;
  }
  
  public void instanciaTodosUrgenteMente ()
  {
    try
      {
	int estadoAnterior = estado;
	pausa ();
	Integer integer;
	synchronized (integer = lockLista)
	  {
	    Iterator itProximos = listaNomeProximos.iterator ();
	    while (itProximos.hasNext ())
	      {
		ItemCache item = (ItemCache) itProximos.next ();
		String proximo = item.getNomeClasse ();
		JPanel jpanel = obtemUrgentemente (proximo);
	      }
	    listaNomeProximos.clear ();
	  }
	estado = estadoAnterior;
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  private void aplicaIdentificacao (String proximo, JPanel painelInstanciado)
  {
    if (painelInstanciado instanceof PPGDFormPanel)
      ((PPGDFormPanel) painelInstanciado).setIdentificaoPainel (arvorePlataforma.getRotuloNodo (proximo));
  }
}
