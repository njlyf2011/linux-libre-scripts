/* ObjetoNegocio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.TabelaMensagens;

public abstract class ObjetoNegocio extends Observador
{
  /**
   * @deprecated
   */
  protected transient IdDeclaracao idDeclaracao;
  private boolean observadorAtivo = true;
  protected TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  protected String ficha = "";
  protected boolean persistente = true;
  
  /**
   * @deprecated
   */
  public ObjetoNegocio (IdDeclaracao idDeclaracao)
  {
    this.idDeclaracao = idDeclaracao;
  }
  
  public ObjetoNegocio ()
  {
    idDeclaracao = null;
  }
  
  /**
   * @deprecated
   */
  public IdDeclaracao getIdDeclaracao ()
  {
    return idDeclaracao;
  }
  
  protected boolean testaVazio (Informacao pInfo)
  {
    return true;
  }
  
  public boolean isVazio ()
  {
    try
      {
	Iterator itFields = FabricaUtilitarios.getAllFields (this.getClass ()).iterator ();
	while (itFields.hasNext ())
	  {
	    Field f = (Field) itFields.next ();
	    boolean acessible = f.isAccessible ();
	    f.setAccessible (true);
	    if ((serpro.ppgd.negocio.Informacao.class).isAssignableFrom (f.getType ()))
	      {
		Informacao info = (Informacao) f.get (this);
		if (testaVazio (info) && ! info.isVazio ())
		  return false;
		break;
	      }
	    if ((serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (f.getType ()))
	      {
		ObjetoNegocio obj = (ObjetoNegocio) f.get (this);
		if (obj != null && ! (obj instanceof IdDeclaracao) && ! obj.isVazio ())
		  return false;
	      }
	    f.setAccessible (acessible);
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
    return true;
  }
  
  public List recuperarCamposInformacao ()
  {
    List retorno = new Vector ();
    try
      {
	Iterator itFields = FabricaUtilitarios.getAllFields (this.getClass ()).iterator ();
	while (itFields.hasNext ())
	  {
	    Field f = (Field) itFields.next ();
	    boolean acessible = f.isAccessible ();
	    f.setAccessible (true);
	    if ((serpro.ppgd.negocio.Informacao.class).isAssignableFrom (f.getType ()))
	      {
		Informacao info = (Informacao) f.get (this);
		retorno.add (info);
	      }
	    f.setAccessible (acessible);
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
    return retorno;
  }
  
  public void clear ()
  {
    Iterator iterator = recuperarCamposInformacao ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	informacao.clear ();
      }
  }
  
  public final void propertyChange (PropertyChangeEvent evt)
  {
    if (isObservadorAtivo ())
      {
	LogPPGD.debug ("Disparando observador " + this + ".");
	implementacaoPropertyChange (evt);
      }
    super.propertyChange (evt);
  }
  
  /**
   * @deprecated
   */
  protected void implementacaoPropertyChange (PropertyChangeEvent evt)
  {
    /* empty */
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    /* empty */
  }
  
  public boolean isObservadorAtivo ()
  {
    return observadorAtivo;
  }
  
  public void setObservadorAtivo (boolean b)
  {
    observadorAtivo = b;
  }
  
  public void setValidadoresAtivos (boolean opcao)
  {
    try
      {
	Iterator itFields = FabricaUtilitarios.getAllFields (this.getClass ()).iterator ();
	while (itFields.hasNext ())
	  {
	    Field f = (Field) itFields.next ();
	    boolean acessible = f.isAccessible ();
	    f.setAccessible (true);
	    if ((serpro.ppgd.negocio.Informacao.class).isAssignableFrom (f.getType ()))
	      {
		Informacao info = (Informacao) f.get (this);
		if (info != null)
		  info.setValidadoresAtivos (opcao);
	      }
	    else if (! (serpro.ppgd.negocio.IdDeclaracao.class).isAssignableFrom (f.getType ()) && (serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (f.getType ()))
	      {
		ObjetoNegocio info = (ObjetoNegocio) f.get (this);
		if (info != null)
		  info.setValidadoresAtivos (opcao);
	      }
	    f.setAccessible (acessible);
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (e);
      }
  }
  
  public List verificarPendencias (int numeroItem)
  {
    List listaPendencias = new Vector ();
    Iterator iterator = recuperarListaCamposPendencia ().iterator ();
    while (iterator.hasNext ())
      {
	Object object = iterator.next ();
	Informacao informacao;
	if (object instanceof Informacao)
	  informacao = (Informacao) object;
	else
	  throw new IllegalArgumentException ("Par\u00e2metro " + object.getClass () + " n\u00e3o \u00e9 Informacao.");
	Enumeration retornosValidacoes = informacao.validar ().elements ();
	while (retornosValidacoes.hasMoreElements ())
	  {
	    Object objectRetornoValidacao = retornosValidacoes.nextElement ();
	    RetornoValidacao retornoValidacao;
	    if (objectRetornoValidacao instanceof RetornoValidacao)
	      retornoValidacao = (RetornoValidacao) objectRetornoValidacao;
	    else
	      {
		String msg = "Elemento " + objectRetornoValidacao.getClass () + " da lista de RetornosValidacoes da classe " + this.getClass () + " n\u00e3o \u00e9 um RetornoValidacao";
		LogPPGD.erro (msg);
		throw new IllegalArgumentException (msg);
	      }
	    if (retornoValidacao.getSeveridade () > 0)
	      {
		Pendencia pendencia = new Pendencia (retornoValidacao.getSeveridade (), informacao, informacao.getNomeCampo (), retornoValidacao.getMensagemValidacao (), numeroItem);
		listaPendencias.add (pendencia);
	      }
	  }
      }
    return listaPendencias;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List listaCamposPendencia = new Vector ();
    return listaCamposPendencia;
  }
  
  public void setFicha (String pFicha)
  {
    ficha = pFicha;
    Iterator iterator = recuperarListaCamposPendencia ().iterator ();
    while (iterator.hasNext ())
      {
	Object object = iterator.next ();
	Informacao informacao;
	if (object instanceof Informacao)
	  informacao = (Informacao) object;
	else
	  throw new IllegalArgumentException ("Par\u00e2metro " + object.getClass () + " n\u00e3o \u00e9 Informacao.");
	informacao.setFicha (ficha);
      }
  }
  
  public String getFicha ()
  {
    return ficha;
  }
  
  public void removeObservadoresAnonimosDesnecessarios (Class pClasse)
  {
    try
      {
	Iterator itFields = FabricaUtilitarios.getAllFields (this.getClass ()).iterator ();
	while (itFields.hasNext ())
	  {
	    Field f = (Field) itFields.next ();
	    if (! Modifier.isTransient (f.getModifiers ()))
	      {
		if ((serpro.ppgd.negocio.Informacao.class).isAssignableFrom (f.getType ()))
		  {
		    boolean acessivel = f.isAccessible ();
		    if (! f.isAccessible ())
		      f.setAccessible (true);
		    Informacao info = (Informacao) f.get (this);
		    if (info != null)
		      info.removeObservadoresAnonimosDesnecessarios (pClasse);
		    f.setAccessible (acessivel);
		  }
		else if (! (serpro.ppgd.negocio.IdDeclaracao.class).isAssignableFrom (f.getType ()) && (serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (f.getType ()))
		  {
		    boolean acessivel = f.isAccessible ();
		    if (! f.isAccessible ())
		      f.setAccessible (true);
		    ObjetoNegocio obj = (ObjetoNegocio) f.get (this);
		    if (obj != null)
		      obj.removeObservadoresAnonimosDesnecessarios (pClasse);
		    f.setAccessible (acessivel);
		  }
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public void removeObservadores (Class[] pClasse)
  {
    try
      {
	Iterator itFields = FabricaUtilitarios.getAllFields (this.getClass ()).iterator ();
	while (itFields.hasNext ())
	  {
	    Field f = (Field) itFields.next ();
	    if (! Modifier.isTransient (f.getModifiers ()))
	      {
		if ((serpro.ppgd.negocio.Informacao.class).isAssignableFrom (f.getType ()))
		  {
		    boolean acessivel = f.isAccessible ();
		    if (! f.isAccessible ())
		      f.setAccessible (true);
		    Informacao info = (Informacao) f.get (this);
		    if (info != null)
		      info.removeObservadores (pClasse);
		    f.setAccessible (acessivel);
		  }
		else if (! (serpro.ppgd.negocio.IdDeclaracao.class).isAssignableFrom (f.getType ()) && (serpro.ppgd.negocio.ObjetoNegocio.class).isAssignableFrom (f.getType ()))
		  {
		    boolean acessivel = f.isAccessible ();
		    if (! f.isAccessible ())
		      f.setAccessible (true);
		    ObjetoNegocio obj = (ObjetoNegocio) f.get (this);
		    if (obj != null)
		      obj.removeObservadores (pClasse);
		    f.setAccessible (acessivel);
		  }
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public boolean isPersistente ()
  {
    return persistente;
  }
  
  public void setPersistente (boolean persistente)
  {
    this.persistente = persistente;
  }
  
}
