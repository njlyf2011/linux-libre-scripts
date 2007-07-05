/* ValidadorImpostoRetidoLei11033 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorImpeditivoDefault;
import serpro.ppgd.negocio.Valor;

public class ValidadorImpostoRetidoLei11033 extends ValidadorImpeditivoDefault
{
  private GanhosLiquidosOuPerdas ganhos = null;
  
  public ValidadorImpostoRetidoLei11033 (GanhosLiquidosOuPerdas _ganhos)
  {
    super (tab.msg ("rendavariavel_irretido_lei10033"));
    setSeveridade ((byte) 5);
    ganhos = _ganhos;
  }
  
  public void acaoOk ()
  {
    /* empty */
  }
  
  public void acaoCancelar ()
  {
    /* empty */
  }
  
  public String getTituloPopup ()
  {
    return "Informa\u00e7\u00e3o";
  }
  
  public RetornoValidacao validarImplementado ()
  {
    getInformacao ().setConteudo ((String) getProximoConteudo ());
    Valor param2 = new Valor ();
    param2.append ('+', ganhos.getTotalImpostoDevido ());
    param2.append ('-', ganhos.getIrFonteDayTradeMesAtual ());
    param2.append ('-', ganhos.getIrFonteDayTradeMesesAnteriores ());
    if (ganhos.getImpostoRetidoFonteLei11033 ().comparacao (">", param2))
      {
	getInformacao ().clear ();
	return new RetornoValidacao (getMensagemValidacao (), getSeveridade ());
      }
    return null;
  }
}
