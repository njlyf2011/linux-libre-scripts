/* IdLink - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IdLink
{
  private String nome;
  private Object usrObject;
  private Icon icone;
  private Icon iconeSelecionado;
  private Icon iconePressionado;
  private Icon iconeDesabilitado;
  private Icon iconeToggled;
  private Action action;
  private String nomeAbreviado = null;
  private String tooltip = null;
  private Icon iconeExpandido;
  private Icon iconeContraido;
  
  public IdLink ()
  {
    /* empty */
  }
  
  public IdLink (String nome, Action action, Icon icone)
  {
    this (nome, action, icone, null, null, null, null);
  }
  
  public IdLink (String nome, Action action, Icon icone, Icon iconeSelecionado, Icon iconePressionado)
  {
    this (nome, action, icone, iconeSelecionado, iconePressionado, null, null);
  }
  
  public IdLink (String nome, Action action, Icon icone, Icon iconeSelecionado, Icon iconePressionado, Icon iconeDesabilitado, Icon iconeToggled)
  {
    setNome (nome);
    setAction (action);
    setIcone (icone);
    setIconeSelecionado (iconeSelecionado);
    setIconePressionado (iconePressionado);
    setIconeDesabilitado (iconeDesabilitado);
    setIconeToggled (iconeToggled);
  }
  
  public IdLink (String nome, Action action, String icone)
  {
    this (nome, action, icone, "", "", "", "");
  }
  
  public IdLink (String nome, Action action, String icone, String iconeSelecionado, String iconePressionado)
  {
    this (nome, action, icone, iconeSelecionado, iconePressionado, "", "");
  }
  
  public IdLink (String nome, Action action, String icone, String iconeSelecionado, String iconePressionado, String iconeDesabilitado, String iconeToggled)
  {
    setNome (nome);
    setAction (action);
    setIcone (icone);
    setIconeSelecionado (iconeSelecionado);
    setIconePressionado (iconePressionado);
    setIconeDesabilitado (iconeDesabilitado);
    setIconeToggled (iconeToggled);
  }
  
  public IdLink (String nome, Action action)
  {
    setNome (nome);
    setAction (action);
  }
  
  public IdLink (Icon icone, Action action)
  {
    setAction (action);
    setIcone (icone);
  }
  
  public String getNome ()
  {
    return nome;
  }
  
  public Icon getIcone ()
  {
    return icone;
  }
  
  public Icon getIconeSelecionado ()
  {
    return iconeSelecionado;
  }
  
  public Icon getIconePressionado ()
  {
    return iconePressionado;
  }
  
  public Icon getIconeDesabilitado ()
  {
    return iconeDesabilitado;
  }
  
  public Icon getIconeToggled ()
  {
    return iconeToggled;
  }
  
  public Action getAction ()
  {
    return action;
  }
  
  public void setAction (Action action)
  {
    this.action = action;
  }
  
  public void setIcone (Icon icone)
  {
    this.icone = icone;
  }
  
  public void setIconeSelecionado (Icon icone)
  {
    iconeSelecionado = icone;
  }
  
  public void setIconePressionado (Icon icone)
  {
    iconePressionado = icone;
  }
  
  public void setIconeDesabilitado (Icon icone)
  {
    iconeDesabilitado = icone;
  }
  
  public void setIconeToggled (Icon icone)
  {
    iconeToggled = icone;
  }
  
  public void setIcone (String icone)
  {
    try
      {
	this.icone = new ImageIcon (this.getClass ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	this.icone = null;
      }
  }
  
  public void setIconeSelecionado (String icone)
  {
    try
      {
	iconeSelecionado = new ImageIcon (this.getClass ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconeSelecionado = null;
      }
  }
  
  public void setIconePressionado (String icone)
  {
    try
      {
	iconePressionado = new ImageIcon (this.getClass ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconePressionado = null;
      }
  }
  
  public void setIconeDesabilitado (String icone)
  {
    try
      {
	iconeDesabilitado = new ImageIcon (this.getClass ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconeDesabilitado = null;
      }
  }
  
  public void setIconeToggled (String icone)
  {
    try
      {
	iconeToggled = new ImageIcon (this.getClass ().getResource ("/" + icone));
      }
    catch (Exception e)
      {
	iconeToggled = null;
      }
  }
  
  public void setNome (String nome)
  {
    this.nome = nome;
  }
  
  public String toString ()
  {
    return getNome ();
  }
  
  public Object getUsrObject ()
  {
    return usrObject;
  }
  
  public void setUsrObject (Object object)
  {
    usrObject = object;
  }
  
  public String getNomeAbreviado ()
  {
    return nomeAbreviado;
  }
  
  public void setNomeAbreviado (String nomeAbreviado)
  {
    this.nomeAbreviado = nomeAbreviado;
  }
  
  public Icon getIconeContraido ()
  {
    return iconeContraido;
  }
  
  public void setIconeContraido (Icon iconeContraido)
  {
    this.iconeContraido = iconeContraido;
  }
  
  public Icon getIconeExpandido ()
  {
    return iconeExpandido;
  }
  
  public void setIconeExpandido (Icon iconeExpandido)
  {
    this.iconeExpandido = iconeExpandido;
  }
  
  public void setIconeExpandido (String iconeExpandido)
  {
    try
      {
	this.iconeExpandido = new ImageIcon (this.getClass ().getResource ("/" + iconeExpandido));
      }
    catch (Exception e)
      {
	this.iconeExpandido = null;
      }
  }
  
  public void setIconeContraido (String iconeContraido)
  {
    try
      {
	this.iconeContraido = new ImageIcon (this.getClass ().getResource ("/" + iconeContraido));
      }
    catch (Exception e)
      {
	this.iconeContraido = null;
      }
  }
  
  public String getTooltip ()
  {
    return tooltip;
  }
  
  public void setTooltip (String tooltip)
  {
    this.tooltip = tooltip;
  }
}
