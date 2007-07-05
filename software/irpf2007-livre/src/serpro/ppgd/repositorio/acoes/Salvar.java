/* Salvar - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.acoes;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.util.FabricaTratamentoErro;
import serpro.ppgd.repositorio.CadastroId;
import serpro.ppgd.repositorio.RepositorioException;
import serpro.ppgd.repositorio.util.FabricaRepositorio;

public class Salvar extends AbstractAction
{
  protected IdDeclaracao idDeclaracao;
  protected CadastroId cadastroId;
  
  private Salvar ()
  {
    /* empty */
  }
  
  public Salvar (IdDeclaracao pIdDeclaracao)
  {
    idDeclaracao = pIdDeclaracao;
  }
  
  protected void preparaAplicacao ()
  {
    /* empty */
  }
  
  protected void aposSalvar ()
  {
    /* empty */
  }
  
  public void actionPerformed (ActionEvent e)
  {
    preparaAplicacao ();
    try
      {
	FabricaRepositorio.getCadastroId ().salvarIdDeclaracao (idDeclaracao);
      }
    catch (RepositorioException exc)
      {
	FabricaTratamentoErro.getTrataErroSistemico ().trataErroSistemico (exc);
      }
    aposSalvar ();
  }
}
