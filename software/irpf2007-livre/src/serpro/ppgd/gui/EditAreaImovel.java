/* EditAreaImovel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.event.KeyEvent;

import javax.swing.UIManager;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.util.UtilitariosString;

public class EditAreaImovel extends EditValor
{
  private static String maskara = "###.###.##0,0";
  private int TAM_MAX = maskara.length () - 3;
  private int parteDecimal = 1;
  
  public EditAreaImovel ()
  {
    /* empty */
  }
  
  public EditAreaImovel (Informacao campo)
  {
    super (campo, maskara.length ());
  }
  
  protected void trataEventoKeyTyped (KeyEvent e)
  {
    String text = componente.getText ();
    int tamanhoTextoAtual = UtilitariosString.retiraMascara (text).length ();
    boolean fracaoGrande = false;
    int pos = text.indexOf (',');
    int tamanhoMaximo;
    if (pos != -1)
      {
	tamanhoMaximo = TAM_MAX;
	String s = text.substring (pos + 1);
	if (s.length () > 1 && componente.getCaretPosition () > pos && componente.getSelectedText () == null)
	  fracaoGrande = true;
      }
    else
      tamanhoMaximo = TAM_MAX - parteDecimal;
    if (e.getKeyChar () == ',')
      tamanhoMaximo = TAM_MAX;
    char ch = e.getKeyChar ();
    boolean eventosEhInclusao = ch != '\t' && ch != '\n' && ch != '\010' && ch != '\u007f' && ch != '%' && ch != '\'';
    boolean jaExisteUmSinalNeg = false;
    if (componente.getText ().trim ().indexOf ("-") != -1)
      jaExisteUmSinalNeg = true;
    if (tamanhoTextoAtual >= tamanhoMaximo && eventosEhInclusao && componente.getSelectedText () == null || e.getKeyChar () == '-' && ! aceitaNumerosNegativos || Character.isLetter (e.getKeyChar ()) || fracaoGrande || e.getKeyChar () == '-' && jaExisteUmSinalNeg)
      {
	e.setKeyChar ('\uffff');
	e.consume ();
	UIManager.getLookAndFeel ().provideErrorFeedback (componente);
      }
  }
}
