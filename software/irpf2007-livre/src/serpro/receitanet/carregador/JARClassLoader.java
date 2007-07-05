/* JARClassLoader - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.receitanet.carregador;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JARClassLoader extends URLClassLoader
{
  private File _arquivo;
  private JarFile _jar;
  
  private JARClassLoader (File arquivo) throws MalformedURLException
  {
    super (new URL[] { arquivo.toURL () });
    if (! arquivo.exists ())
      throw new ExcecaoArquivoNaoEncontrado ("O arquivo " + arquivo + " n\u00e3o foi encontrado.");
    _arquivo = arquivo;
    try
      {
	_jar = new JarFile (arquivo);
      }
    catch (IOException e)
      {
	throw new ExcecaoErroDeLeitura (e);
      }
  }
  
  public static JARClassLoader criar (String arquivo)
  {
    JARClassLoader jarclassloader;
    try
      {
	jarclassloader = new JARClassLoader (new File (arquivo));
      }
    catch (MalformedURLException e)
      {
	throw new ExcecaoArquivoNaoEncontrado (e);
      }
    return jarclassloader;
  }
  
  public static JARClassLoader criar (File arquivo)
  {
    JARClassLoader jarclassloader;
    try
      {
	jarclassloader = new JARClassLoader (arquivo);
      }
    catch (MalformedURLException e)
      {
	throw new ExcecaoArquivoNaoEncontrado (e);
      }
    return jarclassloader;
  }
  
  public Class obterClasse (String nome)
  {
    if (nome == null)
      return null;
    Class var_class;
    try
      {
	var_class = loadClass (nome);
      }
    catch (ClassNotFoundException cnfe)
      {
	throw new ExcecaoErroDeLeitura ("A classe " + nome + " n\u00e3o foi encontrada.", cnfe);
      }
    return var_class;
  }
  
  public String completarNomeDaClasse (String nomeIncompleto)
  {
    if (nomeIncompleto == null)
      return null;
    if (nomeIncompleto.indexOf ('.') >= 0)
      throw new IllegalArgumentException ("Nome de classe informado provavelmente cont\u00e9m um pacote.");
    Enumeration e = _jar.entries ();
    while (e.hasMoreElements ())
      {
	JarEntry entry = (JarEntry) e.nextElement ();
	if (eEntradaArquivoDeClasse (entry))
	  {
	    String nomeCompleto = extrairNomeDaClasse (entry);
	    if (nomeCompleto.endsWith (nomeIncompleto))
	      return nomeCompleto;
	  }
      }
    return null;
  }
  
  public Class[] obterTodasAsClasses ()
  {
    ArrayList nomesDasClasses = new ArrayList ();
    Enumeration e = _jar.entries ();
    while (e.hasMoreElements ())
      {
	JarEntry entry = (JarEntry) e.nextElement ();
	if (eEntradaArquivoDeClasse (entry))
	  nomesDasClasses.add (extrairNomeDaClasse (entry));
      }
    Class[] classes = new Class[nomesDasClasses.size ()];
    for (int i = 0; i < nomesDasClasses.size (); i++)
      classes[i] = obterClasse ((String) nomesDasClasses.get (i));
    return classes;
  }
  
  private String extrairNomeDaClasse (JarEntry entry)
  {
    return entry.getName ().replaceAll ("/", ".").substring (0, entry.getName ().length () - 6);
  }
  
  private boolean eEntradaArquivoDeClasse (JarEntry entry)
  {
    return ! entry.isDirectory () && entry.getName ().endsWith (".class");
  }
  
  public void invocarClasse (String nomeDaClasse, String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException
  {
    Class c = loadClass (nomeDaClasse);
    Method m = c.getMethod ("main", new Class[] { args.getClass () });
    m.setAccessible (true);
    int mods = m.getModifiers ();
    if (m.getReturnType () != Void.TYPE || ! Modifier.isStatic (mods) || ! Modifier.isPublic (mods))
      throw new NoSuchMethodException ("main");
    try
      {
	m.invoke (null, new Object[] { args });
      }
    catch (IllegalAccessException e)
      {
	e.printStackTrace ();
	throw new ExcecaoCarregador ("Este erro nunca deveria acontecer.", e);
      }
  }
  
  public String obterNomeDaClassePrincipal ()
  {
    String string;
    try
      {
	URL u = new URL ("jar", "", _arquivo.toURL () + "!/");
	JarURLConnection uc = (JarURLConnection) u.openConnection ();
	Attributes attr = uc.getMainAttributes ();
	string = attr != null ? attr.getValue (Attributes.Name.MAIN_CLASS) : null;
      }
    catch (IOException e)
      {
	throw new ExcecaoErroDeLeitura (e);
      }
    return string;
  }
}
