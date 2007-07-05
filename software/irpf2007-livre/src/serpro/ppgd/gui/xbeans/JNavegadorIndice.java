/* JNavegadorIndice - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import javax.swing.JOptionPane;

import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.negocio.Observador;

public class JNavegadorIndice extends JEditNumero implements NavegadorColecaoListener
{
  private JNavegadorColecao navegadorColecao;
  
  public JNavegadorIndice ()
  {
    getInformacao ().setConteudo ("1");
    getInformacao ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	JNavegadorIndice.this.digitouValor (valorNovo);
      }
    });
  }
  
  private void atualizaIndice ()
  {
    if (navegadorColecao != null && navegadorColecao.getIndiceAtual () > -1)
      {
	getInformacao ().setHabilitado (true);
	getInformacao ().setConteudo (String.valueOf (navegadorColecao.getIndiceAtual () + 1));
      }
    else
      exibeColecaoVazia (null);
  }
  
  private void digitouValor (Object valorNovo)
  {
    try
      {
	if (getNavegadorColecao () != null)
	  {
	    int novoValor = Integer.parseInt ((String) valorNovo) - 1;
	    if (novoValor < getNavegadorColecao ().getColecao ().recuperarLista ().size ())
	      getNavegadorColecao ().exibe (novoValor);
	    else
	      {
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), "N\u00e3o h\u00e1 item n\u00b0 " + valorNovo + " na lista.", "Busca por item", 1);
		getInformacao ().setConteudo (getInformacao ().getConteudoAntigo ());
	      }
	  }
      }
    catch (Exception exception)
      {
	/* empty */
      }
  }
  
  public void setNavegadorColecao (JNavegadorColecao navegadorColecao)
  {
    if (navegadorColecao != null)
      navegadorColecao.removeNavegadorColecaoListener (this);
    this.navegadorColecao = navegadorColecao;
    navegadorColecao.addNavegadorColecaoListener (this);
    atualizaIndice ();
  }
  
  public JNavegadorColecao getNavegadorColecao ()
  {
    return navegadorColecao;
  }
  
  public void exibeOutro (NavegadorColecaoEvent evt)
  {
    atualizaIndice ();
  }
  
  public void exibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    getInformacao ().setConteudo ("0");
    getInformacao ().setHabilitado (false);
  }
}
