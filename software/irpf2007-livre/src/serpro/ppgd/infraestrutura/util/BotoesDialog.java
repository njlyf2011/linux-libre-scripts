/* BotoesDialog - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.util;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.gui.ButtonPPGD;

public class BotoesDialog extends PPGDFormPanel
{
  private JPanel painel;
  private Vector botoes = new Vector ();
  private String strLayout = "";
  
  private class BotaoAdapter implements ActionListener
  {
    private Method m = null;
    
    public BotaoAdapter (String m)
    {
      try
	{
	  if (m != null)
	    this.m = painel.getClass ().getMethod (m, null);
	}
      catch (SecurityException e)
	{
	  e.printStackTrace ();
	}
      catch (NoSuchMethodException e)
	{
	  e.printStackTrace ();
	}
    }
    
    public void actionPerformed (ActionEvent e)
    {
      try
	{
	  if (m == null)
	    fecharDialogo ();
	  else
	    m.invoke (painel, null);
	}
      catch (Exception ex)
	{
	  ex.printStackTrace ();
	}
    }
  }
  
  public BotoesDialog ()
  {
    this (new String[] { "Ok", "Cancelar", "Ajuda" });
  }
  
  public BotoesDialog (String[] labels)
  {
    for (int i = 0; i < labels.length; i++)
      {
	botoes.add (new ButtonPPGD (labels[i]));
	strLayout = strLayout.concat ("f:p,");
      }
    strLayout = strLayout.substring (0, strLayout.length () - 1);
    FormLayout layout = new FormLayout (strLayout);
    setLayout (layout);
    setBorder (new BevelBorder (1));
    for (int i = 0; i < labels.length; i++)
      {
	builder.setColumn (i + 1);
	builder.append ((ButtonPPGD) botoes.elementAt (i));
      }
  }
  
  public ButtonPPGD getBotao (int i)
  {
    return (ButtonPPGD) botoes.elementAt (i);
  }
  
  public void fecharDialogo ()
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
  
  public void setMouseEvents (JPanel painel, String[] metodos)
  {
    this.painel = painel;
    Iterator i = botoes.iterator ();
    int j = 1;
    int qtdMetodos = metodos != null ? metodos.length : 0;
    while (i.hasNext ())
      {
	((ButtonPPGD) i.next ()).addActionListener (new BotaoAdapter (j <= qtdMetodos ? metodos[j - 1] : null));
	j++;
      }
  }
}
