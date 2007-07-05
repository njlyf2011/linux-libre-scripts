/* ObjetoPersistente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.embeddedDB;

class ObjetoPersistente
{
  public Object instancia = null;
  public boolean persistido = false;
  
  public ObjetoPersistente (Object pObj)
  {
    instancia = pObj;
  }
  
  public ObjetoPersistente (Object pObj, boolean pPersistido)
  {
    instancia = pObj;
    persistido = pPersistido;
  }
}
