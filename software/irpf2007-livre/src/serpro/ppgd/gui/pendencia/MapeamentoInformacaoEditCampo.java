/* MapeamentoInformacaoEditCampo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.pendencia;
import java.util.Hashtable;
import java.util.Map;

import serpro.ppgd.negocio.Informacao;

public class MapeamentoInformacaoEditCampo
{
  private static Map mapeamentoEdits = new Hashtable ();
  
  private MapeamentoInformacaoEditCampo ()
  {
    /* empty */
  }
  
  public static Object getEditAssociado (Informacao pInfo)
  {
    if (! mapeamentoEdits.containsKey (pInfo))
      return null;
    return mapeamentoEdits.get (pInfo);
  }
  
  public static void associaInformacao (Object pEdit, Informacao pInfo)
  {
    mapeamentoEdits.put (pInfo, pEdit);
  }
  
  public static void limpaAssociacoes ()
  {
    mapeamentoEdits.clear ();
  }
}
