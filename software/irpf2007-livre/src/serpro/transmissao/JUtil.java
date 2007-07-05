/* JUtil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class JUtil
{
  private static final int MAX_LINE_CHAR = 85;
  public static boolean VERSAO_DESENVOLVIMENTO = true;
  public static String NOME_ARQUIVO_CERTIFICADO = "cacerts";
  public static String HTTPS_SERVER = "https://hom.receita.fazenda.gov.br/receitanetweb/RecnetWEB.dll";
  
  public static void centerWindow (JFrame jframe)
  {
    java.awt.Container container = jframe.getParent ();
    if (container != null)
      {
	Dimension dimension = jframe.getParent ().getSize ();
	Dimension dimension_0_ = jframe.getSize ();
	int i = jframe.getParent ().getX ();
	int i_1_ = jframe.getParent ().getY ();
	if (dimension_0_.height > dimension.height)
	  dimension_0_.height = dimension.height;
	if (dimension_0_.width > dimension.width)
	  dimension_0_.width = dimension.width;
	jframe.setLocation (i + (dimension.width - dimension_0_.width) / 2, i_1_ + (dimension.height - dimension_0_.height) / 2);
      }
  }
  
  public static void centerWindow (JDialog jdialog)
  {
    java.awt.Container container = jdialog.getParent ();
    if (container != null)
      {
	Dimension dimension = jdialog.getParent ().getSize ();
	Dimension dimension_2_ = jdialog.getSize ();
	int i = jdialog.getParent ().getX ();
	int i_3_ = jdialog.getParent ().getY ();
	if (dimension_2_.height > dimension.height)
	  dimension_2_.height = dimension.height;
	if (dimension_2_.width > dimension.width)
	  dimension_2_.width = dimension.width;
	jdialog.setLocation (i + (dimension.width - dimension_2_.width) / 2, i_3_ + (dimension.height - dimension_2_.height) / 2);
      }
  }
  
  public static String BreakLines (String string)
  {
    int i = string.length ();
    if (i <= 85)
      return string;
    int i_4_ = 0;
    int i_5_ = 0;
    String string_6_ = new String ("");
    String string_7_ = new String ("");
    String string_8_ = new String ("");
    for (int i_9_ = 0; i_9_ < i; i_9_++)
      {
	if (string.charAt (i_9_) == '\n')
	  {
	    i_4_ = 0;
	    string_7_ = string_7_.trim ();
	    string_7_ += string.charAt (i_9_);
	    string_6_ += string_7_;
	    string_7_ = "";
	    i_5_ = 0;
	  }
	else
	  {
	    if (string.charAt (i_9_) == ' ')
	      i_5_ = i_4_;
	    string_7_ += string.charAt (i_9_);
	    if (++i_4_ % 85 == 0 && i_5_ > 0)
	      {
		string_8_ = string_7_.substring (0, i_5_);
		string_8_ = string_8_.trim ();
		string_6_ += string_8_;
		string_6_ += '\n';
		string_7_ = string_7_.substring (i_5_ + 1);
		i_4_ = string_7_.length ();
		i_5_ = 0;
	      }
	  }
      }
    string_6_ += (String) string_7_;
    return string_6_;
  }
  
  public static void MsgBox (Component component, String string, int i)
  {
    JOptionPane.showMessageDialog (component, BreakLines (string), "Receitanet", i);
  }
  
  public static void MsgBox (JDialog jdialog, String string, int i)
  {
    JOptionPane.showMessageDialog (jdialog, BreakLines (string), "Receitanet", i);
  }
  
  public static void MsgBox (JFrame jframe, String string, int i)
  {
    JOptionPane.showMessageDialog (jframe, BreakLines (string), "Receitanet", i);
  }
  
  public static void MsgBox (String string, int i)
  {
    JOptionPane.showMessageDialog (null, BreakLines (string), "Receitanet", i);
  }
  
  public static boolean IsFile (String string)
  {
    if (string.length () == 0)
      return false;
    File file = new File (string);
    if (file.isFile ())
      return true;
    return false;
  }
  
  public static boolean FileExists (String string)
  {
    File file = new File (string);
    if (file.exists ())
      return true;
    return false;
  }
}
