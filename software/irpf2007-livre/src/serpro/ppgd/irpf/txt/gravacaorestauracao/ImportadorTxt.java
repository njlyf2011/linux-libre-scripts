/* ImportadorTxt - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.gravacaorestauracao;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JOptionPane;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class ImportadorTxt
{
  private RepositorioDeclaracaoCentralTxt repositorioTxt;
  
  public IdentificadorDeclaracao importarDeclaracaoAnoAnterior (File file, boolean temRec) throws GeracaoTxtException, IOException
  {
    if (repositorioTxt == null)
      repositorioTxt = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPFANOANTERIOR", file);
    IdentificadorDeclaracao idDecl = repositorioTxt.recuperarIdDeclaracaoAnoAnterior ();
    repositorioTxt.importarDeclaracaoAnoAnterior (idDecl);
    return idDecl;
  }
  
  public IdentificadorDeclaracao restaurarIdDeclaracaoNaoPersistidoAnoAnterior (File file) throws GeracaoTxtException, IOException
  {
    if (repositorioTxt == null)
      repositorioTxt = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPFANOANTERIOR", file);
    IdentificadorDeclaracao IdentificadorDeclaracao = repositorioTxt.recuperarIdDeclaracaoNaoPersistido ();
    IdentificadorDeclaracao.getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
    return IdentificadorDeclaracao;
  }
  
  public void restaurarDeclaracao (File file) throws Exception
  {
    if (repositorioTxt == null)
      repositorioTxt = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", file);
    IdentificadorDeclaracao idDecl = repositorioTxt.recuperarIdDeclaracao ();
    repositorioTxt.recuperarDeclaracao (idDecl);
    Iterator it = repositorioTxt.getArquivo ().arquivo ().iterator ();
    while (it.hasNext ())
      {
	String reg = ((String) it.next ()).substring (0, 2);
	if (reg.equals ("67") || reg.equals ("78"))
	  {
	    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), TabelaMensagens.getTabelaMensagens ().msg ("recuperar_dados_windows"), "IRPF" + ConstantesGlobais.EXERCICIO, 1);
	    break;
	  }
      }
    repositorioTxt = null;
  }
  
  public IdentificadorDeclaracao restaurarIdDeclaracao (File file) throws GeracaoTxtException, IOException
  {
    if (repositorioTxt == null)
      repositorioTxt = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", file);
    return repositorioTxt.recuperarIdDeclaracaoNaoPersistido ();
  }
  
  public boolean existeDeclaracaoExercicioAtual (File file) throws GeracaoTxtException, IOException
  {
    if (repositorioTxt == null)
      repositorioTxt = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", file);
    IdentificadorDeclaracao idDecl = repositorioTxt.recuperarIdDeclaracaoNaoPersistido ();
    idDecl.getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
    return IRPFFacade.existeDeclaracao (idDecl.getCpf ().asString ());
  }
  
  public RepositorioDeclaracaoCentralTxt getRepositorioTxt ()
  {
    return repositorioTxt;
  }
}
