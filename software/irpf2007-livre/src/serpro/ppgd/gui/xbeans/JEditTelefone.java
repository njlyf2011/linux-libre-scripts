/* JEditTelefone - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;

public class JEditTelefone extends JEditMascara
{
  private static String maskaraBR = "****-****";
  private static String maskaraOutrosPaises = "**** ****-****";
  private static String caracteresValidos = "0123456789 ";
  private boolean isBrasileiro = true;
  
  public JEditTelefone ()
  {
    this (new Alfa ());
  }
  
  public JEditTelefone (Informacao campo)
  {
    super (campo);
    setaBrasil ();
  }
  
  public void setaBrasil ()
  {
    setMascara (maskaraBR);
    setCaracteresValidos (caracteresValidos);
  }
  
  public void setaOutrosPaises ()
  {
    setMascara (maskaraOutrosPaises);
    setCaracteresValidos (caracteresValidos);
  }
  
  public void setBrasileiro (boolean isBrasileiro)
  {
    this.isBrasileiro = isBrasileiro;
  }
  
  public boolean isBrasileiro ()
  {
    return isBrasileiro;
  }
}
