/* PPGDFormPanel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.gui.EditCampo;
import serpro.ppgd.gui.PPGDFormBuilder;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class PPGDFormPanel extends FormDebugPanel
{
  protected PPGDFormBuilder builder;
  protected boolean layoutIniciado = false;
  protected boolean debugColor = false;
  protected TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
  protected String identificaoPainel = "";
  
  public PPGDFormPanel ()
  {
    super ((FormLayout) null);
    setDebugColor (debugColor);
  }
  
  public void setBackground (Color bg)
  {
    super.setBackground (bg);
    setDebugColor (debugColor);
  }
  
  public void setDebugColor (boolean b)
  {
    debugColor = b;
    if (! b)
      {
	setGridColor (getBackground ());
	setPaintInBackground (true);
      }
    else
      {
	setGridColor (Color.red);
	setPaintInBackground (false);
      }
  }
  
  public void setLayout (LayoutManager mgr)
  {
    if (! layoutIniciado)
      {
	layoutIniciado = true;
	builder = new PPGDFormBuilder (this, (FormLayout) mgr);
      }
    else
      super.setLayout (mgr);
  }
  
  public void add (Object comp, Object constraints)
  {
    if (constraints instanceof String)
      {
	try
	  {
	    StringTokenizer st = new StringTokenizer ((String) constraints, "#");
	    if (st.countTokens () == 2)
	      {
		Method m = comp.getClass ().getMethod (st.nextToken (), null);
		comp = m.invoke (comp, null);
		constraints = st.nextToken ();
	      }
	    Method[] methods = (serpro.ppgd.gui.PPGDFormBuilder.class).getMethods ();
	    if (((String) constraints).indexOf ("(") != -1)
	      {
		st = new StringTokenizer ((String) constraints, "()");
		String nome = st.nextToken ();
		for (int i = 0; i < methods.length; i++)
		  {
		    if (nome.equals (methods[i].getName ()))
		      {
			if (methods[i].getParameterTypes ().length == 1 && st.countTokens () == 1 && methods[i].getParameterTypes ()[0].isAssignableFrom (Integer.TYPE))
			  {
			    methods[i].invoke (builder, new Object[] { new Integer (Integer.parseInt (st.nextToken ().trim ())) });
			    break;
			  }
			if (methods[i].getParameterTypes ().length == 0 && st.countTokens () == 0)
			  {
			    methods[i].invoke (builder, null);
			    break;
			  }
		      }
		  }
	      }
	    else
	      {
		st = new StringTokenizer ((String) constraints, ",");
		String nome = st.nextToken ();
		for (int i = 0; i < methods.length; i++)
		  {
		    if (nome.equals (methods[i].getName ()))
		      {
			if (methods[i].getParameterTypes ().length == 1 && st.countTokens () == 0 && methods[i].getParameterTypes ()[0].isAssignableFrom (comp.getClass ()))
			  {
			    methods[i].invoke (builder, new Object[] { comp });
			    break;
			  }
			if (methods[i].getParameterTypes ().length == 2 && st.countTokens () == 1 && methods[i].getParameterTypes ()[0].isAssignableFrom (comp.getClass ()) && methods[i].getParameterTypes ()[1].isAssignableFrom (Integer.TYPE))
			  {
			    methods[i].invoke (builder, new Object[] { comp, new Integer (Integer.parseInt (st.nextToken ().trim ())) });
			    break;
			  }
		      }
		  }
	      }
	  }
	catch (Exception e)
	  {
	    e.printStackTrace ();
	  }
      }
    else
      {
	if ((serpro.ppgd.gui.EditCampo.class).isAssignableFrom (comp.getClass ()))
	  comp = ((EditCampo) comp).getComponenteEditor ();
	add ((Component) comp, (CellConstraints) constraints);
      }
  }
  
  public void add (Component c, Object cc)
  {
    if (cc instanceof CellConstraints)
      {
	if (((CellConstraints) cc).gridX == 0)
	  ((CellConstraints) cc).gridX = builder.getColumn ();
	if (((CellConstraints) cc).gridY == 0)
	  ((CellConstraints) cc).gridY = builder.getRow ();
	super.add (c, (CellConstraints) cc);
      }
    else
      add ((Object) c, cc);
  }
  
  public PPGDFormBuilder getBuilder ()
  {
    return builder;
  }
  
  public void setIdentificaoPainel (String pIdPainel)
  {
    identificaoPainel = pIdPainel;
    try
      {
	Iterator itFields = FabricaUtilitarios.getAllFields (this.getClass ()).iterator ();
	while (itFields.hasNext ())
	  {
	    Field f = (Field) itFields.next ();
	    if (f.getType ().equals (serpro.ppgd.gui.EditCampo.class))
	      {
		f.setAccessible (true);
		EditCampo edit = (EditCampo) f.get (this);
		if (edit != null)
		  {
		    edit.setIdentificacaoPainelAssociado (identificaoPainel);
		    String ajudaId = edit.getIdAjuda ();
		    if (ajudaId != null && ! ajudaId.equals (""))
		      PlataformaPPGD.getPlataforma ().setHelpID (edit.getComponenteEditor (), edit.getIdAjuda ());
		  }
		f.setAccessible (false);
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public String getIdentificaoPainel ()
  {
    return identificaoPainel;
  }
  
  public void selecionaPendencia (Pendencia pPendencia)
  {
    /* empty */
  }
  
}
