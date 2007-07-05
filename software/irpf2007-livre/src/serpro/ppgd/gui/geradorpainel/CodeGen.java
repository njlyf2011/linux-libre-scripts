/* CodeGen - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.geradorpainel;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class CodeGen
{
  public static void criaPainelColecao (Class c, boolean imprimeNoConsole) throws Exception
  {
    String classFullName = c.getName ();
    String className = classFullName.substring (c.getName ().lastIndexOf (".") + 1);
    if (! imprimeNoConsole)
      {
	String arquivo = FabricaUtilitarios.getPathCompletoDirAplicacao () + "Painel" + className + "s.java";
	FileOutputStream fOutStream = new FileOutputStream (arquivo);
	PrintStream outStream = new PrintStream (fOutStream);
	System.setOut (outStream);
      }
    System.out.println ("import com.jgoodies.forms.layout.FormLayout;");
    System.out.println ("");
    System.out.println ("import " + classFullName + ";");
    System.out.println ("import serpro.ppgd.negocio.ObjetoNegocio;");
    System.out.println ("import serpro.ppgd.negocio.Colecao;");
    System.out.println ("import serpro.ppgd.gui.EditCampo;");
    System.out.println ("import serpro.ppgd.gui.FabricaGUI;");
    System.out.println ("import serpro.ppgd.infraestrutura.util.PPGDFormPanel;");
    System.out.println ("import serpro.ppgd.infraestrutura.util.PainelGenericoColecao;");
    System.out.println ("import " + c.getName () + ";");
    System.out.println ("");
    System.out.println ("public class Painel" + className + "s extends PainelGenericoColecao {");
    Method[] theMethods = c.getMethods ();
    Vector vFields = new Vector ();
    for (int i = 0; i < theMethods.length; i++)
      {
	String methodString = theMethods[i].getName ();
	if (methodString.startsWith ("get"))
	  {
	    String fieldName = methodString.substring (3);
	    String returnType = theMethods[i].getReturnType ().getName ();
	    Class returnClass = Class.forName (returnType);
	    Class superClass = returnClass.getSuperclass ();
	    if (superClass.getName ().endsWith ("Informacao"))
	      {
		fieldName = methodString.substring (3);
		vFields.add (fieldName);
	      }
	  }
      }
    System.out.println ("  // Declaracao dos EditCampo");
    Iterator iEditCampo = vFields.iterator ();
    while (iEditCampo.hasNext ())
      System.out.println ("  private EditCampo edt" + iEditCampo.next () + ";");
    System.out.println ("");
    System.out.println ("  public Painel" + className + "s(){");
    System.out.println ("  }");
    System.out.println ("");
    System.out.println ("  // Inicializacao dos Componentes");
    System.out.println ("  public void iniciaComponentes() {");
    System.out.println ("");
    System.out.println ("    " + className + " " + className.toLowerCase () + " = ((" + c.getName () + ") getObjetoNegocio());");
    System.out.println ("");
    System.out.println ("    //TODO: INCLUIR PAR\u00c2METRO COM TAMANHO M\u00c1XIMO, SE HOUVER");
    Iterator iComponente = vFields.iterator ();
    while (iComponente.hasNext ())
      {
	String field = (String) iComponente.next ();
	System.out.println ("    edt" + field + " = " + "FabricaGUI.criaCampo(" + className.toLowerCase () + ".get" + field + "());");
      }
    System.out.println ("  }");
    System.out.println ("");
    System.out.println ("  public void adicionaComponentes(PPGDFormPanel painelPrincipal) {");
    System.out.println ("");
    System.out.println ("    FormLayout layout = new FormLayout(\"l:p, 2dlu, l:p:grow\",  //colunas");
    System.out.println ("                            \"\"); //linhas");
    System.out.println ("    painelPrincipal.setLayout(layout);");
    System.out.println ("    painelPrincipal.setDebugColor(true);");
    System.out.println ("    painelPrincipal.setPaintInBackground(false);");
    System.out.println ("");
    Iterator iGui = vFields.iterator ();
    while (iGui.hasNext ())
      {
	String field = (String) iGui.next ();
	System.out.println ("    painelPrincipal.getBuilder().append(edt" + field + ");");
	System.out.println ("    painelPrincipal.getBuilder().nextLine();");
      }
    System.out.println ("  }");
    System.out.println ("");
    System.out.println ("    protected Colecao getColecao() {");
    System.out.println ("     //TODO - Coloque aqui para retornar a colecao que da origem ao painel");
    System.out.println ("       return null;");
    System.out.println ("    }");
    System.out.println ("");
    System.out.println ("    public void mostraOutroObjetoNegocio(ObjetoNegocio obj){");
    System.out.println ("");
    System.out.println ("    " + className + " " + className.toLowerCase () + " = ((" + c.getName () + ") obj);");
    System.out.println ("");
    iComponente = vFields.iterator ();
    while (iComponente.hasNext ())
      {
	String field = (String) iComponente.next ();
	System.out.println ("      edt" + field + ".setAssociaInformacao(" + className.toLowerCase () + ".get" + field + "());");
      }
    System.out.println ("   }");
    System.out.println ("}");
  }
  
  public static void criaPainel (Class c, boolean imprimeNoConsole) throws Exception
  {
    String classFullName = c.getName ();
    String className = classFullName.substring (c.getName ().lastIndexOf (".") + 1);
    if (! imprimeNoConsole)
      {
	String arquivo = FabricaUtilitarios.getPathCompletoDirAplicacao () + "Painel" + className + ".java";
	FileOutputStream fOutStream = new FileOutputStream (arquivo);
	PrintStream outStream = new PrintStream (fOutStream);
	System.setOut (outStream);
      }
    System.out.println ("import com.jgoodies.forms.layout.FormLayout;");
    System.out.println ("");
    System.out.println ("import " + classFullName + ";");
    System.out.println ("import serpro.ppgd.gui.EditCampo;");
    System.out.println ("import serpro.ppgd.gui.FabricaGUI;");
    System.out.println ("import serpro.ppgd.infraestrutura.util.PainelGenerico;");
    System.out.println ("");
    System.out.println ("public class Painel" + className + " extends PainelGenerico {");
    Method[] theMethods = c.getMethods ();
    Vector vFields = new Vector ();
    for (int i = 0; i < theMethods.length; i++)
      {
	String methodString = theMethods[i].getName ();
	if (methodString.startsWith ("get"))
	  {
	    String fieldName = methodString.substring (3);
	    String returnType = theMethods[i].getReturnType ().getName ();
	    Class returnClass = Class.forName (returnType);
	    Class superClass = returnClass.getSuperclass ();
	    if (superClass.getName ().endsWith ("Informacao"))
	      {
		fieldName = methodString.substring (3);
		vFields.add (fieldName);
	      }
	  }
      }
    System.out.println ("  // Declaracao dos EditCampo");
    Iterator iEditCampo = vFields.iterator ();
    while (iEditCampo.hasNext ())
      System.out.println ("  private EditCampo edt" + iEditCampo.next () + ";");
    System.out.println ("");
    System.out.println ("  public Painel" + className + "(){");
    System.out.println ("    setDebugColor(true);");
    System.out.println ("    setPaintInBackground(false);");
    System.out.println ("  }");
    System.out.println ("");
    System.out.println ("  // Inicializacao dos Componentes");
    System.out.println ("  public void iniciaComponentes() {");
    System.out.println ("    FormLayout layout = new FormLayout(\"l:p, 2dlu, l:p:grow\",  //colunas");
    System.out.println ("                            \"\"); //linhas");
    System.out.println ("    setLayout(layout);");
    System.out.println ("    " + className + " " + className.toLowerCase () + " = ((" + FabricaUtilitarios.nomeClasseDeclaracao + ") getDeclaracao()).get" + className + "();");
    System.out.println ("");
    System.out.println ("    //TODO: INCLUIR PAR\u00c2METRO COM TAMANHO M\u00c1XIMO, SE HOUVER");
    Iterator iComponente = vFields.iterator ();
    while (iComponente.hasNext ())
      {
	String field = (String) iComponente.next ();
	System.out.println ("    edt" + field + " = " + "FabricaGUI.criaCampo(" + className.toLowerCase () + ".get" + field + "());");
      }
    System.out.println ("  }");
    System.out.println ("");
    System.out.println ("  public void adicionaComponentes() {");
    Iterator iGui = vFields.iterator ();
    while (iGui.hasNext ())
      {
	String field = (String) iGui.next ();
	System.out.println ("    builder.append(edt" + field + ");");
	System.out.println ("    builder.nextLine();");
      }
    System.out.println ("  }");
    System.out.println ("}");
  }
  
  public static void main (String[] args) throws Exception
  {
    /* empty */
  }
}
