/* HashtableIdDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.repositorioXML;
import java.util.Enumeration;
import java.util.Hashtable;

import serpro.ppgd.negocio.IdDeclaracao;

public class HashtableIdDeclaracao extends Hashtable
{
  public synchronized boolean containsKey (Object value)
  {
    Enumeration allKeys = keys ();
  while_29_:
    do
      {
	IdDeclaracao idDecl;
	do
	  {
	    if (! allKeys.hasMoreElements ())
	      break while_29_;
	    idDecl = (IdDeclaracao) allKeys.nextElement ();
	  }
	while (! idDecl.equals ((IdDeclaracao) value));
	return true;
      }
    while (false);
    return false;
  }
  
  public synchronized Object remove (Object value)
  {
    Object oldIdDecl = null;
    Enumeration allKeys = keys ();
    while (allKeys.hasMoreElements ())
      {
	IdDeclaracao idDecl = (IdDeclaracao) allKeys.nextElement ();
	if (idDecl.equals ((IdDeclaracao) value))
	  oldIdDecl = super.remove (idDecl);
      }
    return oldIdDecl;
  }
}
