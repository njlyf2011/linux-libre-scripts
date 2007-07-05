/* RelatorioIf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos;

public interface RelatorioIf
{
  public void imprimir ();
  
  public void visualizar ();
  
  public String getTitulo ();
  
  public void setTitulo (String string);
  
  public boolean isHabilitado ();
  
  public void setHabilitado (boolean bool);
  
  public void prepara ();
  
  public boolean isPreparado ();
}
