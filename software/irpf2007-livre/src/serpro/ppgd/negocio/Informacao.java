/* Informacao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosString;

public abstract class Informacao implements Comparable
{
  public static final String VALIDO_PROPERTY = "Val-Property";
  public static final String READ_ONLY_PROPERTY = "ReadOnly";
  public static final String HABILITADO_PROPERTY = "habilitado";
  public static final String ROTULO_CHANGE_PROPERTY = "Label_Modificado";
  private String nomeCampo = "";
  private String nomeCampoCurto = "";
  private ObjetoNegocio owner;
  private String conteudo = "";
  private String ficha = "";
  private boolean readOnly = false;
  private boolean habilitado = true;
  private boolean transportado = false;
  protected String conteudoAntigo = "";
  protected boolean inicializouUltimoConteudoValido = false;
  private String ultimoConteudoValido = "";
  private Vector listaValidadores;
  private Vector listaValidadoresImpeditivos = new Vector ();
  private RetornosValidacoes retornoTodasValidacoes = new RetornosValidacoes ();
  private PropertyChangeSupport observadores = new PropertyChangeSupport (this);
  private boolean observadoresAtivos = true;
  private boolean validadoresAtivos = true;
  private boolean atributoPersistente = true;
  
  public Informacao ()
  {
    /* empty */
  }
  
  public Informacao (ObjetoNegocio owner, String nomeCampo)
  {
    this ();
    setOwner (owner);
    setNomeCampo (nomeCampo);
    setNomeCampoAlternativo (nomeCampo);
    LogPPGD.debug ("Campo [" + nomeCampo + "] criado como objeto " + this.getClass ().getName () + ".");
  }
  
  public Informacao (String nomeCampo)
  {
    this ();
    setNomeCampo (nomeCampo);
    setNomeCampoAlternativo (nomeCampo);
    LogPPGD.debug ("Campo [" + nomeCampo + "] criado como objeto " + this.getClass ().getName () + ".");
  }
  
  public void setConteudo (String conteudo)
  {
    if (! inicializouUltimoConteudoValido)
      {
	inicializouUltimoConteudoValido = true;
	ultimoConteudoValido = conteudo;
      }
    LogPPGD.debug ("Atribui\u00e7\u00e3o: " + getNomeCampo () + " = " + conteudo);
    String antigo = asString ();
    clearRetornosValidacoes ();
    this.conteudo = conteudo;
    if (isVazio ())
      ultimoConteudoValido = "";
    disparaObservadores (antigo);
  }
  
  public abstract String getConteudoFormatado ();
  
  public String toString ()
  {
    return getConteudoFormatado ();
  }
  
  public String asString ()
  {
    return conteudo;
  }
  
  public boolean isVazio ()
  {
    return asString () == null || UtilitariosString.retiraMascara (asString ().trim ()).length () == 0;
  }
  
  public int compareTo (Object o)
  {
    return asString ().compareTo (o.toString ());
  }
  
  public void clear ()
  {
    setConteudo ("");
  }
  
  public String getNomeCampo ()
  {
    return nomeCampo;
  }
  
  public void setNomeCampo (String nomeCampo)
  {
    String nomeAntigo = getNomeCampo ();
    this.nomeCampo = nomeCampo;
    getObservadores ().firePropertyChange ("Label_Modificado", nomeAntigo, getNomeCampo ());
  }
  
  public void addValidador (ValidadorIf validador)
  {
    if (listaValidadores == null)
      listaValidadores = new Vector ();
    if (validador instanceof ValidadorImpeditivoDefault)
      listaValidadoresImpeditivos.add (validador);
    else
      listaValidadores.add (validador);
    validador.setInformacao (this);
    validador.setRetornosValidacoes (retornoTodasValidacoes);
  }
  
  public boolean isValido ()
  {
    return retornoTodasValidacoes.isTodosValidos ();
  }
  
  public RetornoValidacao getPrimeiroRetornoValidacaoMaisSevero ()
  {
    return retornoTodasValidacoes.getPrimeiroRetornoValidacaoMaisSevero ();
  }
  
  public void clearRetornosValidacoes ()
  {
    retornoTodasValidacoes.clear ();
  }
  
  public RetornosValidacoes validar ()
  {
    LogPPGD.debug ("Valida\u00e7\u00e3o de " + getNomeCampo () + " [=" + toString () + "]");
    retornoTodasValidacoes.clear ();
    if (listaValidadores != null && isValidadoresAtivos ())
      {
	Iterator validadores = listaValidadores.iterator ();
	while (validadores.hasNext ())
	  ((ValidadorIf) validadores.next ()).validar ();
      }
    if (isValido () && getRetornoTodasValidacoes ().getPrimeiroRetornoValidacaoMaisSevero ().getSeveridade () != 4)
      setUltimoConteudoValido (asString ());
    return retornoTodasValidacoes;
  }
  
  public ObjetoNegocio getOwner ()
  {
    return owner;
  }
  
  public void setOwner (ObjetoNegocio owner)
  {
    this.owner = owner;
  }
  
  public PropertyChangeSupport getObservadores ()
  {
    return observadores;
  }
  
  public void addObservador (Observador observador)
  {
    observadores.addPropertyChangeListener (observador);
  }
  
  public void addObservador (String nomePropriedade, Observador observador)
  {
    observadores.addPropertyChangeListener (nomePropriedade, observador);
  }
  
  public void removeObservador (Observador observador)
  {
    observadores.removePropertyChangeListener (observador);
  }
  
  public void removeObservador (String nomePropriedade, Observador observador)
  {
    observadores.removePropertyChangeListener (nomePropriedade, observador);
  }
  
  public void disparaObservadores ()
  {
    if (isVazio ())
      disparaObservadores ("");
    else
      disparaObservadores ("_");
  }
  
  protected void disparaObservadores (Object aConteudoAntigo)
  {
    if (aConteudoAntigo != null && ! aConteudoAntigo.equals ("-"))
      setConteudoAntigo ((String) aConteudoAntigo);
    LogPPGD.debug ("Chamando observadores de " + getNomeCampo () + " [=" + toString () + "]");
    if (isObservadoresAtivos ())
      getObservadores ().firePropertyChange (getNomeCampo (), aConteudoAntigo, asString ());
  }
  
  public boolean isObservadoresAtivos ()
  {
    return observadoresAtivos;
  }
  
  public void setObservadoresAtivos (boolean b)
  {
    observadoresAtivos = b;
  }
  
  public boolean isReadOnly ()
  {
    return readOnly;
  }
  
  public void setReadOnly (boolean b)
  {
    LogPPGD.debug (getNomeCampo () + "." + "readOnly" + " = " + String.valueOf (b));
    boolean readOnlyAntigo = readOnly;
    readOnly = b;
    if (isObservadoresAtivos () && readOnlyAntigo != readOnly)
      getObservadores ().firePropertyChange ("ReadOnly", ! b, b);
  }
  
  public String getFicha ()
  {
    return ficha;
  }
  
  public void setFicha (String string)
  {
    ficha = string;
  }
  
  public RetornosValidacoes getRetornoTodasValidacoes ()
  {
    return retornoTodasValidacoes;
  }
  
  public void setRetornoTodasValidacoes (RetornosValidacoes retornoTodasValidacoes)
  {
    this.retornoTodasValidacoes = retornoTodasValidacoes;
  }
  
  public Vector getListaValidadores ()
  {
    return listaValidadores;
  }
  
  public void setNomeCampoAlternativo (String nomeCampoCurto)
  {
    this.nomeCampoCurto = nomeCampoCurto;
  }
  
  public String getNomeCampoCurto ()
  {
    return nomeCampoCurto;
  }
  
  public void setHabilitado (boolean aHabilitado)
  {
    LogPPGD.debug (getNomeCampo () + "." + "isHabilitado" + " = " + String.valueOf (aHabilitado));
    habilitado = aHabilitado;
    if (isObservadoresAtivos ())
      getObservadores ().firePropertyChange ("habilitado", ! aHabilitado, aHabilitado);
    habilitado = aHabilitado;
  }
  
  public void sinalizaValidoEdit ()
  {
    getObservadores ().firePropertyChange ("Val-Property", "Invalido", "Valido");
  }
  
  public boolean isHabilitado ()
  {
    return habilitado;
  }
  
  public boolean isTransportado ()
  {
    return transportado;
  }
  
  public void setTransportado (boolean transportado)
  {
    this.transportado = transportado;
  }
  
  public String getConteudoAntigo ()
  {
    return conteudoAntigo;
  }
  
  public void setConteudoAntigo (String valorAntigo)
  {
    if (! valorAntigo.equals (asString ()))
      conteudoAntigo = valorAntigo;
  }
  
  public void removeObservadores (Class[] pTipoClasses) throws ClassNotFoundException
  {
    PropertyChangeListener[] observadores = getObservadores ().getPropertyChangeListeners ();
    for (int i = 0; i < observadores.length; i++)
      {
	String nomeClasse = observadores[i].getClass ().getName ();
	Class classeBase = null;
	if (nomeClasse.indexOf ("$") != -1)
	  classeBase = Class.forName (nomeClasse.substring (0, nomeClasse.indexOf ("$")));
	else
	  classeBase = observadores[i].getClass ();
	if (comparaClass (pTipoClasses, classeBase))
	  getObservadores ().removePropertyChangeListener (observadores[i]);
      }
  }
  
  private boolean comparaClass (Class[] pTipoClasses, Class tipoProcurado)
  {
    for (int tipoClasse = 0; tipoClasse < pTipoClasses.length; tipoClasse++)
      {
	if (pTipoClasses[tipoClasse].isAssignableFrom (tipoProcurado))
	  return true;
      }
    return false;
  }
  
  public void removeObservadoresAnonimosDesnecessarios (Class pClasse) throws ClassNotFoundException
  {
    PropertyChangeListener[] observadores = getObservadores ().getPropertyChangeListeners ();
    for (int i = 0; i < observadores.length; i++)
      {
	String nomeClasse = observadores[i].getClass ().getName ();
	if (nomeClasse.indexOf ("$") != -1)
	  {
	    Class classeBase = Class.forName (nomeClasse.substring (0, nomeClasse.indexOf ("$")));
	    if (pClasse.isAssignableFrom (classeBase))
	      getObservadores ().removePropertyChangeListener (observadores[i]);
	  }
      }
  }
  
  public boolean isValidadoresAtivos ()
  {
    return validadoresAtivos;
  }
  
  public void setValidadoresAtivos (boolean validadoresAtivos)
  {
    this.validadoresAtivos = validadoresAtivos;
  }
  
  public void setUltimoConteudoValido (String ultimoConteudoValido)
  {
    this.ultimoConteudoValido = ultimoConteudoValido;
  }
  
  public String getUltimoConteudoValido ()
  {
    return ultimoConteudoValido;
  }
  
  public Vector getListaValidadoresImpeditivos ()
  {
    return listaValidadoresImpeditivos;
  }
  
  public void ordenaListaValidadoreImpeditivos ()
  {
    LinkedList auxListaImpeditivos = new LinkedList ();
    LinkedList auxListaOkCancelar = new LinkedList ();
    Iterator itValidadores = getListaValidadoresImpeditivos ().iterator ();
    while (itValidadores.hasNext ())
      {
	ValidadorImpeditivoDefault validador = (ValidadorImpeditivoDefault) itValidadores.next ();
	if (validador.getSeveridade () == 5)
	  auxListaImpeditivos.add (validador);
	else if (validador.getSeveridade () == 4)
	  auxListaOkCancelar.add (validador);
      }
    auxListaImpeditivos.addAll (auxListaOkCancelar);
    listaValidadoresImpeditivos.clear ();
    listaValidadoresImpeditivos.addAll (auxListaImpeditivos);
  }
  
  public boolean isAtributoPersistente ()
  {
    return atributoPersistente;
  }
  
  public void setAtributoPersistente (boolean atributoPersistente)
  {
    this.atributoPersistente = atributoPersistente;
  }
}
