/* TagBotao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.tags;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;

import serpro.ppgd.infraestrutura.acoes.AcaoMudaPainel;
import serpro.ppgd.negocio.util.LogPPGD;

public class TagBotao extends JButton implements ActionListener
{
  private String acaoPadrao = "";
  private String painelStr;
  private Action actionObj = null;
  
  public TagBotao ()
  {
    addActionListener (this);
  }
  
  public void setAction (String aAcao)
  {
    acaoPadrao = aAcao;
  }
  
  public void setPainel (String aPainel)
  {
    painelStr = aPainel;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    if (actionObj == null)
      {
	if (acaoPadrao.equals ("AcaoMudaPainel"))
	  actionObj = new AcaoMudaPainel (painelStr);
	else
	  {
	    LogPPGD.erro ("Erro: " + this.getClass ().getName () + ": nenhuma a\u00e7\u00e3o v\u00e1lida foi especificada!");
	    return;
	  }
      }
    actionObj.actionPerformed (e);
  }
}
