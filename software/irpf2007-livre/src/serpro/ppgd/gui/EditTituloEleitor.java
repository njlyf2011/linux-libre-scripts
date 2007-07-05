/* EditTituloEleitor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Informacao;

public class EditTituloEleitor extends EditMascara
{
  public EditTituloEleitor ()
  {
    this (new Alfa ("T\u00edtulo Eleitor:"));
  }
  
  public EditTituloEleitor (Informacao campo)
  {
    super (campo, 13);
    setMascara ("*************");
    setCaracteresValidos ("0123456789. /-");
  }
}
