/* PPGDComponentFactory - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import com.jgoodies.forms.factories.ComponentFactory;

public class PPGDComponentFactory implements ComponentFactory
{
  private static final PPGDComponentFactory INSTANCE = new PPGDComponentFactory ();
  private static final boolean IS_BEFORE_14 = isBefore14 ();
  private static final char MNEMONIC_MARKER = '&';
  
  private static class TitleLabel extends JLabel
  {
    TitleLabel ()
    {
      /* empty */
    }
    
    TitleLabel (String text)
    {
      super (text);
    }
    
    public void updateUI ()
    {
      super.updateUI ();
      Color foreground = UIManager.getColor ("TitledBorder.titleColor");
      if (foreground != null)
	setForeground (foreground);
      setFont (UIManager.getFont ("TitledBorder.font"));
    }
  }
  
  private PPGDComponentFactory ()
  {
    /* empty */
  }
  
  /**
   * @deprecated
   */
  public static PPGDComponentFactory getInstance ()
  {
    return INSTANCE;
  }
  
  public static PPGDComponentFactory getInstancia ()
  {
    return INSTANCE;
  }
  
  public JLabel createLabel (String textWithMnemonic)
  {
    JLabel label = new JLabel ();
    setTextAndMnemonic (label, textWithMnemonic);
    return label;
  }
  
  public JLabel createTitle (String textWithMnemonic)
  {
    return createTitle (textWithMnemonic, 0);
  }
  
  private JLabel createTitle (String textWithMnemonic, int gap)
  {
    JLabel label = new TitleLabel ();
    setTextAndMnemonic (label, textWithMnemonic);
    label.setVerticalAlignment (0);
    label.setBorder (BorderFactory.createEmptyBorder (1, 0, 1, gap));
    return label;
  }
  
  public JComponent createSeparator (String text)
  {
    return createSeparator (text, 2);
  }
  
  public JComponent createSeparator (String text, int alignment)
  {
    JPanel header = new JPanel (new GridBagLayout ());
    GridBagConstraints gbc = new GridBagConstraints ();
    gbc.weightx = 0.0;
    gbc.weighty = 1.0;
    gbc.anchor = 16;
    gbc.fill = 1;
    gbc.gridwidth = 1;
    gbc.gridheight = 3;
    if (text != null && text.length () > 0)
      header.add (createTitle (text, 4), gbc);
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.gridwidth = 0;
    gbc.gridheight = 1;
    JSeparator separator = new JSeparator ();
    header.add (Box.createGlue (), gbc);
    gbc.weighty = 0.0;
    header.add (separator, gbc);
    gbc.weighty = 1.0;
    header.add (Box.createGlue (), gbc);
    return header;
  }
  
  public static void setTextAndMnemonic (JLabel label, String textWithMnemonic)
  {
    int markerIndex = textWithMnemonic.indexOf ('&');
    if (markerIndex == -1)
      label.setText (textWithMnemonic);
    else
      {
	int mnemonicIndex = -1;
	int begin = 0;
	int length = textWithMnemonic.length ();
	StringBuffer buffer = new StringBuffer ();
	do
	  {
	    int end;
	    if (markerIndex + 1 < length && textWithMnemonic.charAt (markerIndex + 1) == '&')
	      end = markerIndex + 1;
	    else
	      {
		end = markerIndex;
		if (mnemonicIndex == -1)
		  mnemonicIndex = markerIndex;
	      }
	    buffer.append (textWithMnemonic.substring (begin, end));
	    begin = end + 1;
	    markerIndex = begin < length ? textWithMnemonic.indexOf ('&', begin) : -1;
	  }
	while (markerIndex != -1);
	buffer.append (textWithMnemonic.substring (begin));
	label.setText (buffer.toString ());
	if (mnemonicIndex != -1 && mnemonicIndex + 1 < length)
	  {
	    label.setDisplayedMnemonic (textWithMnemonic.charAt (mnemonicIndex + 1));
	    setDisplayedMnemonicIndex (label, mnemonicIndex);
	  }
      }
  }
  
  private static void setDisplayedMnemonicIndex (JLabel label, int displayedMnemonicIndex)
  {
    Integer index = new Integer (displayedMnemonicIndex);
    if (IS_BEFORE_14)
      label.putClientProperty ("displayedMnemonicIndex", index);
    else
      {
	try
	  {
	    Class var_class = javax.swing.AbstractButton.class;
	    Method method = var_class.getMethod ("setDisplayedMnemonicIndex", new Class[0]);
	    method.invoke (label, new Object[] { index });
	  }
	catch (NoSuchMethodException nosuchmethodexception)
	  {
	    /* empty */
	  }
	catch (InvocationTargetException invocationtargetexception)
	  {
	    /* empty */
	  }
	catch (IllegalAccessException illegalaccessexception)
	  {
	    /* empty */
	  }
      }
  }
  
  private static boolean isBefore14 ()
  {
    String version = System.getProperty ("java.version");
    if (! version.startsWith ("1.2") && ! version.startsWith ("1.3"))
      return false;
    return true;
  }
}
