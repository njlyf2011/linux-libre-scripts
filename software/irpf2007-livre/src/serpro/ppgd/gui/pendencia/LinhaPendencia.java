/* LinhaPendencia - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.pendencia;
import javax.swing.ImageIcon;

import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.negocio.Pendencia;

public class LinhaPendencia
{
  private byte severidade;
  private String campo;
  private ImageIcon tipo;
  private Pendencia pendencia;
  
  public LinhaPendencia (byte severidade, String campo, Pendencia pendencia)
  {
    setSeveridade (severidade);
    setCampo (campo);
    tipo = getIconeSeveridade (severidade);
    this.pendencia = pendencia;
  }
  
  public void setSeveridade (byte severidade)
  {
    this.severidade = severidade;
  }
  
  public ImageIcon getTipo ()
  {
    if (tipo != null)
      return tipo;
    return new ImageIcon ();
  }
  
  public byte getSeveridade ()
  {
    return severidade;
  }
  
  public String getSeveridadeAsString ()
  {
    switch (severidade)
      {
      case 2:
	return "AVISO";
      case 3:
	return "ERRO";
      case 1:
	return "ATEN\u00c7\u00c3O";
      default:
	return "";
      }
  }
  
  public void setCampo (String campo)
  {
    this.campo = campo;
  }
  
  public String getCampo ()
  {
    return campo;
  }
  
  public Pendencia getPendencia ()
  {
    return pendencia;
  }
  
  private ImageIcon getIconeSeveridade (byte severidade)
  {
    switch (severidade)
      {
      case 0:
	return ConstantesGlobaisGUI.ICO_VAZIO;
      case 2:
	return ConstantesGlobaisGUI.ICO_AVISO;
      case 3:
	return ConstantesGlobaisGUI.ICO_ERRO;
      case 1:
	return ConstantesGlobaisGUI.ICO_INFORMA;
      default:
	return null;
      }
  }
}
