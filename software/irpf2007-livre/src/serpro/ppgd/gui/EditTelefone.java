/* EditTelefone - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;

public class EditTelefone extends EditMascara
{
  private static String maskaraBR = "** ****-****";
  private static String maskaraOutrosPaises = "**** ****-****";
  private static String caracteresValidos = "0123456789 ";
  
  public EditTelefone ()
  {
    this (new Alfa ("Telefone:"));
  }
  
  public EditTelefone (Informacao campo)
  {
    super (campo, maskaraOutrosPaises.length ());
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
}
