/* DefaultListaRelatorios - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos;
import java.util.ArrayList;
import java.util.Collection;

public class DefaultListaRelatorios implements ListaRelatoriosIf
{
  private Collection rels = new ArrayList ();
  
  public void addRelatorio (RelatorioIf aRel)
  {
    rels.add (aRel);
  }
  
  public Collection getRelatorios ()
  {
    return rels;
  }
}
