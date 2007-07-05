/* RecuperarExercicioAnterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.ImportadorTxt;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.PreferenciasGlobais;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;

public class RecuperarExercicioAnterior extends AbstractAction
{
  private String dirInicial = PreferenciasGlobais.get ("IRPF2006_PATH_GRAVADAS");
  
  class DECFilter extends FileFilter
  {
    private final String padraoNomeCopiaSeguranca = "\\d{11}-IRPF-" + ConstantesGlobais.ANO_BASE + "-" + ConstantesGlobais.ANO_BASE_ANTERIOR + "-(ORIGI|RETIF|origi|retif).(DEC|DBK|dbk|dec)";
    
    public boolean accept (File f)
    {
      if (f.isDirectory ())
	return true;
      return Validador.validarString (f.getName (), padraoNomeCopiaSeguranca);
    }
    
    public String getDescription ()
    {
      return "Arquivos IRPF-" + ConstantesGlobais.ANO_BASE + "-" + ConstantesGlobais.ANO_BASE_ANTERIOR;
    }
  }
  
  public RecuperarExercicioAnterior ()
  {
    if (dirInicial == null)
      {
	String os = System.getProperty ("os.name").toLowerCase ();
	if (os.startsWith ("windows"))
	  {
	    String arqProgramas;
	    try
	      {
		Properties env = getEnvVars (os);
		arqProgramas = env.getProperty ("ProgramFiles");
	      }
	    catch (IOException e)
	      {
		arqProgramas = "C:\\Arquivos de Programas";
		e.printStackTrace ();
	      }
	    dirInicial = arqProgramas + File.separator + "Programas SRF" + File.separator + "IRPFJava" + ConstantesGlobais.EXERCICIO_ANTERIOR + File.separator + "transmitidas";
	  }
	else
	  dirInicial = System.getProperty ("user.home") + File.separator + "ProgramasSRF" + File.separator + "IRPFJava" + ConstantesGlobais.EXERCICIO_ANTERIOR + File.separator + "transmitidas";
      }
  }
  
  public void actionPerformed (ActionEvent e)
  {
    ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	importar ();
	IRPFUtil.habilitaComponentesTemDeclaracao ();
	return null;
      }
    });
  }
  
  public IdentificadorDeclaracao importar ()
  {
    boolean concluiuImportacao = false;
    IdentificadorDeclaracao idDeclaracao = null;
    IdentificadorDeclaracao idDeclAberta = null;
    try
      {
	JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Importa\u00e7\u00e3o de dados da Declara\u00e7\u00e3o de IRPF " + ConstantesGlobais.EXERCICIO_ANTERIOR, "Procurar em:", "Importar", "Importa dados da declara\u00e7\u00e3o de IRPF " + ConstantesGlobais.EXERCICIO_ANTERIOR);
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileFilter (new DECFilter ());
	fc.setMultiSelectionEnabled (true);
	fc.setCurrentDirectory (new File (dirInicial));
	int ret = fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ());
	if (ret == 0)
	  {
	    File[] selectedFiles = fc.getSelectedFiles ();
	    int retorno = 1;
	    String msgConfirmacao = null;
	    int indexDecRepetida = contemDeclaracaoRepetida (selectedFiles);
	    if (indexDecRepetida > -1)
	      {
		msgConfirmacao = "<html>Voc\u00ea selecionou duas declara\u00e7\u00f5es para o mesmo CPF ( " + UtilitariosString.formataCPF (selectedFiles[indexDecRepetida].getName ().substring (0, 11)) + " )" + ",<br>" + "uma original e outra retificadora, e tem duas alternativas: <br><br>" + "1) Retornar, desmarcar a declara\u00e7\u00e3o original e manter a retificadora, por ser a \u00faltima <br>entregue (aconselh\u00e1vel); OU<br><br>" + "2) Continuar. Nesse caso, o programa importar\u00e1 a 1\u00aa declara\u00e7\u00e3o e logo em seguida, <br>" + "na 2\u00aa importa\u00e7\u00e3o para o mesmo CPF, verificar\u00e1 que j\u00e1 existe declara\u00e7\u00e3o na base. <br>" + "O aplicativo perguntar\u00e1 se deseja sobrepor os dados. O risco dessa op\u00e7\u00e3o \u00e9 <br>" + "a declara\u00e7\u00e3o original ser a segunda e n\u00e3o ser recuperado o dado mais atualizado." + "</html>";
		int opt = JOptionPane.showOptionDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), msgConfirmacao, "Aten\u00e7\u00e3o", 0, 3, null, new Object[] { "Continuar", "Retornar" }, "Continuar");
		if (opt != 0)
		  retorno = -1;
	      }
	    if (retorno == 1)
	      {
		for (int i = 0; i < selectedFiles.length; i++)
		  {
		    ImportadorTxt importador = new ImportadorTxt ();
		    idDeclaracao = importador.restaurarIdDeclaracaoNaoPersistidoAnoAnterior (selectedFiles[i]);
		    boolean confirmacao = true;
		    if (importador.existeDeclaracaoExercicioAtual (selectedFiles[i]))
		      {
			msgConfirmacao = "<html>J\u00e1 existe declara\u00e7\u00e3o do exerc\u00edcio de " + ConstantesGlobais.EXERCICIO + " para o CPF " + idDeclaracao.getCpf ().getConteudoFormatado () + " em sua base de<br>dados. Se importar os dados de " + ConstantesGlobais.EXERCICIO_ANTERIOR + ", as informa\u00e7\u00f5es existentes ser\u00e3o substitu\u00eddas.<br>Deseja importar os dados da declara\u00e7\u00e3o de " + ConstantesGlobais.EXERCICIO_ANTERIOR + "?</html>";
			if (JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), msgConfirmacao, "Confirma\u00e7\u00e3o", 0, 3) != 0)
			  confirmacao = false;
			else
			  IRPFFacade.excluirDeclaracao (idDeclaracao.getCpf ().asString ());
		      }
		    if (confirmacao)
		      {
			boolean temRec = false;
			String nomeDec = selectedFiles[i].toString ();
			nomeDec = nomeDec.substring (0, nomeDec.length () - ".DEC".length ());
			temRec = new File (nomeDec + ".REC").exists () || new File (nomeDec + ".rec").exists () || new File (nomeDec.substring (0, 8) + ".REC").exists () || new File (nomeDec.substring (0, 8) + ".rec").exists ();
			IRPFFacade.criarDeclaracao (idDeclaracao);
			idDeclaracao = importador.importarDeclaracaoAnoAnterior (selectedFiles[i], temRec);
			msgConfirmacao = "<html>Importa\u00e7\u00e3o da declara\u00e7\u00e3o de " + idDeclaracao.getNome () + ",<br>CPF: " + idDeclaracao.getCpf ().getConteudoFormatado () + " realizada com sucesso.</br>";
			JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), msgConfirmacao, "Importa\u00e7\u00e3o de dados da Declara\u00e7\u00e3o de IRPF " + ConstantesGlobais.EXERCICIO_ANTERIOR, 1);
			concluiuImportacao = true;
		      }
		  }
	      }
	  }
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), e.getMessage (), "Erro", 0);
      }
    if (concluiuImportacao)
      return idDeclaracao;
    return null;
  }
  
  private int contemDeclaracaoRepetida (File[] selectedFiles)
  {
    HashSet set = new HashSet ();
    boolean semDuplicatas = true;
    int i;
    for (i = 0; i < selectedFiles.length && semDuplicatas; i++)
      {
	class ItemDec implements Comparable
	{
	  private String ni;
	  
	  public ItemDec (String aNi)
	  {
	    ni = aNi;
	  }
	  
	  public int compareTo (Object o)
	  {
	    int result;
	    if (o == null || ! (o instanceof ItemDec))
	      result = 1;
	    else
	      {
		ItemDec outro = (ItemDec) o;
		if (Integer.parseInt (toString ()) > Integer.parseInt (outro.toString ()))
		  result = 1;
		else if (Integer.parseInt (toString ()) < Integer.parseInt (outro.toString ()))
		  result = -1;
		else
		  result = 0;
	      }
	    return result;
	  }
	  
	  public boolean equals (Object o)
	  {
	    boolean result;
	    if (o == null || ! (o instanceof ItemDec))
	      result = false;
	    else
	      {
		ItemDec outro = (ItemDec) o;
		result = toString ().equals (outro.toString ());
	      }
	    return result;
	  }
	  
	  public String toString ()
	  {
	    return ni;
	  }
	  
	  public int hashCode ()
	  {
	    return toString ().hashCode ();
	  }
	};
	semDuplicatas = set.add (new ItemDec (selectedFiles[i].getName ().substring (0, 8)));
      }
    int result = -1;
    if (! semDuplicatas)
      result = --i;
    return result;
  }
  
  public static Properties getEnvVars (String os) throws IOException
  {
    Process p = null;
    Properties envVars = new Properties ();
    Runtime r = Runtime.getRuntime ();
    if (os.indexOf ("windows 9") > -1)
      p = r.exec ("command.com /c set");
    else
      p = r.exec ("cmd.exe /c set");
    BufferedReader br = new BufferedReader (new InputStreamReader (p.getInputStream ()));
    String line;
    while ((line = br.readLine ()) != null)
      {
	int idx = line.indexOf ('=');
	String key = line.substring (0, idx);
	String value = line.substring (idx + 1);
	envVars.setProperty (key, value);
      }
    return envVars;
  }
}
