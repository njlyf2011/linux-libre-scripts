/* CalendarioEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.calendario;
import java.util.EventObject;

import javax.swing.JLabel;

public class CalendarioEvent extends EventObject
{
  private JLabel labelDia;
  private int mes;
  private int ano;
  
  public CalendarioEvent (Object source)
  {
    super (source);
  }
  
  public void setLabelDia (JLabel labelDia)
  {
    this.labelDia = labelDia;
  }
  
  public JLabel getLabelDia ()
  {
    return labelDia;
  }
  
  public int getDia ()
  {
    return Integer.parseInt (getLabelDia ().getText ());
  }
  
  public void setMes (int mes)
  {
    this.mes = mes;
  }
  
  public int getMes ()
  {
    return mes;
  }
  
  public void setAno (int ano)
  {
    this.ano = ano;
  }
  
  public int getAno ()
  {
    return ano;
  }
}
