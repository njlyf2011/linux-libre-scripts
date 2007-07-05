/* ItemRelatorio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import serpro.ppgd.formatosexternos.RelatorioIf;

class ItemRelatorio
{
  private RelatorioIf relatorio;
  
  public ItemRelatorio (RelatorioIf aRelatorio)
  {
    setRelatorio (aRelatorio);
  }
  
  public void setRelatorio (RelatorioIf relatorio)
  {
    this.relatorio = relatorio;
  }
  
  public RelatorioIf getRelatorio ()
  {
    return relatorio;
  }
  
  public String toString ()
  {
    String ret;
    if (relatorio.isHabilitado ())
      ret = getRelatorio ().getTitulo ();
    else
      ret = "<html><font color=gray>" + getRelatorio ().getTitulo () + "</font></html>";
    return ret;
  }
}
