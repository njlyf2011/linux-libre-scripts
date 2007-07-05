/* GravadorTXT - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.gravacaorestauracao;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.ConstantesGlobais;

public class GravadorTXT
{
  private static final Logger logger = Logger.getLogger ("srf.irpf.importacaoGravacao.ImportarDeclaracao.ImportadorTxt");
  private static IdentificadorDeclaracao objIdDeclGerar;
  
  public static void gravarDeclaracao (IdentificadorDeclaracao idDecl, String path) throws GeracaoTxtException, IOException
  {
    objIdDeclGerar = idDecl;
    RepositorioDeclaracaoCentralTxt repositorio = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", montaNome (ConstantesRepositorio.GRAV_GERACAO, path));
    repositorio.gravarDeclaracao (idDecl);
  }
  
  public static void copiarDeclaracao (IdentificadorDeclaracao idDecl, String path) throws GeracaoTxtException, IOException
  {
    objIdDeclGerar = idDecl;
    RepositorioDeclaracaoCentralTxt repositorio = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", montaNome (ConstantesRepositorio.GRAV_COPIA, path));
    repositorio.salvarDeclaracao (idDecl);
  }
  
  private static File montaNome (byte tipo, String path)
  {
    StringBuffer nomeArqGerado = new StringBuffer (new File (path).getAbsolutePath ());
    nomeArqGerado.append (System.getProperty ("file.separator"));
    nomeArqGerado.append (objIdDeclGerar.getCpf ().asString () + "-" + "IRPF-" + ConstantesGlobais.EXERCICIO + "-" + ConstantesGlobais.ANO_BASE + "-");
    nomeArqGerado.append (objIdDeclGerar.isRetificadora () ? "RETIF." : "ORIGI.");
    nomeArqGerado.append (tipo == ConstantesRepositorio.GRAV_GERACAO ? "DEC" : "DBK");
    String extensao = nomeArqGerado.toString ();
    return new File (nomeArqGerado.toString ());
  }
  
  public static String montaNome (byte tipo, String path, IdentificadorDeclaracao idDec)
  {
    StringBuffer nomeArqGerado = new StringBuffer (new File (path).getAbsolutePath ());
    nomeArqGerado.append (System.getProperty ("file.separator"));
    nomeArqGerado.append (idDec.getCpf ().asString () + "-" + "IRPF-" + ConstantesGlobais.EXERCICIO + "-" + ConstantesGlobais.ANO_BASE + "-");
    nomeArqGerado.append (idDec.isRetificadora () ? "RETIF." : "ORIGI.");
    nomeArqGerado.append (tipo == ConstantesRepositorio.GRAV_GERACAO ? "DEC" : "DBK");
    return nomeArqGerado.toString ();
  }
  
  public static String montaNomeArquivoTXT (byte tipo, IdentificadorDeclaracao id)
  {
    StringBuffer nomeArqGerado = new StringBuffer ();
    nomeArqGerado.append (id.getCpf ().asString () + "-" + "IRPF-" + ConstantesGlobais.EXERCICIO + "-" + ConstantesGlobais.ANO_BASE + "-");
    nomeArqGerado.append (id.isRetificadora () ? "RETIF." : "ORIGI.");
    nomeArqGerado.append (tipo == ConstantesRepositorio.GRAV_GERACAO ? "DEC" : "DBK");
    return nomeArqGerado.toString ();
  }
  
  public static boolean fileExists (IdentificadorDeclaracao idDecl, String path, byte tipo)
  {
    objIdDeclGerar = idDecl;
    return montaNome (tipo, path).exists ();
  }
}
