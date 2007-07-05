/* FabricaRepositorio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.repositorio.util;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.repositorio.CadastroId;
import serpro.ppgd.repositorio.RepositorioObjetoNegocioIf;
import serpro.ppgd.repositorio.RepositorioTabelasBasicasIf;
import serpro.ppgd.repositorio.embeddedDB.RepositorioIdDB;
import serpro.ppgd.repositorio.embeddedDB.RepositorioIdDeclaracaoDB;
import serpro.ppgd.repositorio.embeddedDB.RepositorioObjetoNegocioDB;
import serpro.ppgd.repositorio.embeddedDB.RepositorioTabelasBasicasDB;
import serpro.ppgd.repositorio.repositorioXML.RepositorioIdDeclaracaoXML;
import serpro.ppgd.repositorio.repositorioXML.RepositorioIdXML;
import serpro.ppgd.repositorio.repositorioXML.RepositorioObjetoNegocioXML;
import serpro.ppgd.repositorio.repositorioXML.RepositorioTabelasBasicasXML;

public abstract class FabricaRepositorio
{
  private static CadastroId cadastroIdXML = null;
  private static CadastroId cadastroIdDB = null;
  private static RepositorioTabelasBasicasIf repositorioTabelasBasicasXML = new RepositorioTabelasBasicasXML ();
  private static RepositorioTabelasBasicasIf repositorioTabelasBasicasDB = new RepositorioTabelasBasicasDB ();
  
  public static String getPathArquivoXMLMapeamento ()
  {
    String retorno = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.mapeamentoXML");
    if (retorno == null || retorno.trim ().length () == 0)
      retorno = "/mapeamentoObjetos.xml";
    return retorno;
  }
  
  private static CadastroId getCadastroIdXML ()
  {
    if (cadastroIdXML == null)
      {
	serpro.ppgd.repositorio.RepositorioIdIf repositorioId = new RepositorioIdXML (FabricaUtilitarios.nomeClasseId);
	RepositorioObjetoNegocioIf repObjNegocio = null;
	String classeRepObj = FabricaUtilitarios.getProperties ().getProperty ("repositoriodeclaracao");
	if (classeRepObj != null && ! classeRepObj.trim ().equals (""))
	  {
	    try
	      {
		repObjNegocio = (RepositorioObjetoNegocioIf) Class.forName (classeRepObj).newInstance ();
	      }
	    catch (Exception e)
	      {
		LogPPGD.erro ("Nao foimpossivel instanciar o repositorio: " + classeRepObj);
	      }
	  }
	else
	  repObjNegocio = new RepositorioObjetoNegocioXML (FabricaUtilitarios.nomeClasseDeclaracao);
	cadastroIdXML = new CadastroId (repositorioId, new RepositorioIdDeclaracaoXML (FabricaUtilitarios.nomeClasseIdDeclaracao, repositorioId), repObjNegocio);
      }
    return cadastroIdXML;
  }
  
  private static CadastroId getCadastroIdDB ()
  {
    if (cadastroIdDB == null)
      {
	serpro.ppgd.repositorio.RepositorioIdIf repositorioId = new RepositorioIdDB (FabricaUtilitarios.nomeClasseId);
	RepositorioObjetoNegocioIf repObjNegocio = null;
	String classeRepObj = FabricaUtilitarios.getProperties ().getProperty ("repositoriodeclaracao");
	if (classeRepObj != null && ! classeRepObj.trim ().equals (""))
	  {
	    try
	      {
		repObjNegocio = (RepositorioObjetoNegocioIf) Class.forName (classeRepObj).newInstance ();
	      }
	    catch (Exception e)
	      {
		LogPPGD.erro ("Nao foimpossivel instanciar o repositorio: " + classeRepObj);
	      }
	  }
	else
	  repObjNegocio = new RepositorioObjetoNegocioDB (FabricaUtilitarios.nomeClasseDeclaracao);
	cadastroIdDB = new CadastroId (repositorioId, new RepositorioIdDeclaracaoDB (FabricaUtilitarios.nomeClasseIdDeclaracao), repObjNegocio);
      }
    return cadastroIdDB;
  }
  
  public static CadastroId getCadastroId ()
  {
    String tipoCadastro = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.persistencia.tipo", "");
    if (tipoCadastro.trim ().length () != 0)
      {
	if (tipoCadastro.equalsIgnoreCase ("XML"))
	  {
	    if (cadastroIdXML == null)
	      cadastroIdXML = getCadastroIdXML ();
	    return cadastroIdXML;
	  }
	if (cadastroIdDB == null)
	  cadastroIdDB = getCadastroIdDB ();
	return cadastroIdDB;
      }
    if (cadastroIdXML == null)
      cadastroIdXML = getCadastroIdXML ();
    return cadastroIdXML;
  }
  
  public static RepositorioTabelasBasicasIf getRepositorioTabelasBasicasDB ()
  {
    return repositorioTabelasBasicasDB;
  }
  
  public static RepositorioTabelasBasicasIf getRepositorioTabelasBasicasXML ()
  {
    return repositorioTabelasBasicasXML;
  }
}
