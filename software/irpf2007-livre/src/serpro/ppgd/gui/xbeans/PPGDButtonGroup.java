/* PPGDButtonGroup - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.PPGDFacade;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class PPGDButtonGroup extends ButtonGroup
{
  private Alfa informacao;
  private String informacaoAssociada;
  private boolean associaComFacade = true;
  private Collection colRadios = new ArrayList ();
  private Collection colCheckbox = new ArrayList ();
  private boolean desabilitaAtualizaInterface = false;
  private boolean atualizandoInterface = false;
  
  public void add (final AbstractButton b)
  {
    if (b instanceof PPGDRadioButton)
      {
	super.add (b);
	colRadios.add (b);
      }
    else
      {
	((PPGDCheckBox) b).addItemListener (new ItemListener ()
	{
	  public void itemStateChanged (ItemEvent e)
	  {
	    if (! atualizandoInterface)
	      PPGDButtonGroup.this.clicouCheckBox (b.getModel (), e.getStateChange () == 1);
	  }
	});
	colCheckbox.add (b);
      }
    if (b instanceof PPGDRadioButton)
      ((PPGDRadioButton) b).setPPGDButtonGroup (this);
    else if (b instanceof PPGDCheckBox)
      ((PPGDCheckBox) b).setPPGDButtonGroup (this);
    atualizaInterface (b);
  }
  
  public void remove (AbstractButton b)
  {
    super.remove (b);
    colRadios.remove (b);
    colCheckbox.remove (b);
  }
  
  public void setSelected (ButtonModel m, boolean b)
  {
    selecionouRadioButton (m, b);
  }
  
  public void selecionouRadioButton (ButtonModel m, boolean selecionou)
  {
    super.setSelected (m, selecionou);
    desabilitaAtualizaInterface = true;
    String novoValor = null;
    Iterator elems = colRadios.iterator ();
    while (elems.hasNext ())
      {
	AbstractButton btn = (AbstractButton) elems.next ();
	if (btn.getModel ().isSelected () && m.isSelected ())
	  {
	    novoValor = ((PPGDRadioButton) btn).getValor ();
	    break;
	  }
      }
    if (informacao != null)
      {
	if (novoValor != null)
	  informacao.setConteudo (novoValor);
	else
	  informacao.clear ();
      }
    desabilitaAtualizaInterface = false;
  }
  
  private void clicouCheckBox (ButtonModel m, boolean selecionou)
  {
    desabilitaAtualizaInterface = true;
    String novoValor = "";
    Iterator elems = colCheckbox.iterator ();
    while (elems.hasNext ())
      {
	PPGDCheckBox btn = (PPGDCheckBox) elems.next ();
	if (btn.isSelected ())
	  novoValor += "#" + btn.getValor () + ";";
      }
    if (informacao != null)
      informacao.setConteudo (novoValor);
    desabilitaAtualizaInterface = false;
  }
  
  public void setInformacao (Alfa informacao)
  {
    this.informacao = informacao;
    informacao.addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	PPGDButtonGroup.this.mudouInformacao ();
      }
    });
    mudouInformacao ();
  }
  
  private void mudouInformacao ()
  {
    Iterator elems = colCheckbox.iterator ();
    while (elems.hasNext ())
      {
	AbstractButton btn = (AbstractButton) elems.next ();
	atualizaInterface (btn);
      }
    elems = colRadios.iterator ();
    while (elems.hasNext ())
      {
	AbstractButton btn = (AbstractButton) elems.next ();
	atualizaInterface (btn);
      }
  }
  
  protected boolean atualizaInterface (AbstractButton btn)
  {
    if (! desabilitaAtualizaInterface)
      {
	atualizandoInterface = true;
	if (informacao != null)
	  {
	    if (btn instanceof PPGDRadioButton)
	      {
		PPGDRadioButton radioButton = (PPGDRadioButton) btn;
		if (radioButton.getValor () != null && radioButton.getValor ().equals (informacao.asString ()))
		  btn.setSelected (true);
	      }
	    else if (btn instanceof PPGDCheckBox)
	      {
		PPGDCheckBox checkBox = (PPGDCheckBox) btn;
		String valorCheckBox = "#" + checkBox.getValor () + ";";
		if (informacao.asString ().indexOf (valorCheckBox) > -1)
		  btn.setSelected (true);
		else
		  btn.setSelected (false);
	      }
	  }
	atualizandoInterface = false;
      }
    return btn.isSelected ();
  }
  
  public Alfa getInformacao ()
  {
    return informacao;
  }
  
  public void setInformacaoAssociada (String informacaoAssociada)
  {
    this.informacaoAssociada = informacaoAssociada;
    if (isAssociaComFacade ())
      associaInformacao (informacaoAssociada);
  }
  
  private void associaInformacao (String aInfo)
  {
    try
      {
	String classeFacade = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault");
	Class classe = Class.forName (classeFacade);
	Method methGetInstancia = classe.getMethod ("getInstancia", null);
	PPGDFacade facade = (PPGDFacade) methGetInstancia.invoke (null, null);
	Object objInfo = facade;
	StringTokenizer tokens = new StringTokenizer (aInfo, ".");
	while (tokens.hasMoreTokens ())
	  {
	    Class clazz = objInfo.getClass ();
	    String nomeMetodo = tokens.nextToken ();
	    nomeMetodo = nomeMetodo.substring (0, 1).toUpperCase () + nomeMetodo.substring (1, nomeMetodo.length ());
	    Method mtd = clazz.getMethod ("get" + nomeMetodo, null);
	    objInfo = mtd.invoke (objInfo, null);
	  }
	if (objInfo != null && objInfo instanceof Informacao)
	  setInformacao ((Alfa) objInfo);
	else
	  LogPPGD.erro ("O retorno de " + aInfo + " veio nulo. Este atributo existe mesmo?");
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public String getInformacaoAssociada ()
  {
    return informacaoAssociada;
  }
  
  public void setAssociaComFacade (boolean associadoAutomaticamente)
  {
    associaComFacade = associadoAutomaticamente;
  }
  
  public boolean isAssociaComFacade ()
  {
    return associaComFacade;
  }
}
