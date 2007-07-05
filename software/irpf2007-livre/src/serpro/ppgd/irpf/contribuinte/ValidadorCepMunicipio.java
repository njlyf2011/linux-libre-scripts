/* ValidadorCepMunicipio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.contribuinte;
import java.util.StringTokenizer;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;

public class ValidadorCepMunicipio extends ValidadorDefault
{
  private Codigo pais;
  private Codigo municipio;
  private Informacao cep;
  
  public ValidadorCepMunicipio (byte pSeveridade, Codigo pPais, Codigo pMunicipio, Informacao pCep, String pMsg)
  {
    super (pSeveridade);
    setMensagemValidacao (pMsg);
    pais = pPais;
    municipio = pMunicipio;
    cep = pCep;
  }
  
  public RetornoValidacao validarImplementado ()
  {
    if (pais.isVazio () || municipio.isVazio () || cep.isVazio () || cep.getConteudoFormatado ().replaceAll ("-", "").replaceAll (" ", "").length () != 8 || ! pais.getConteudoFormatado ().equals ("105"))
      {
	cep.sinalizaValidoEdit ();
	municipio.sinalizaValidoEdit ();
	return null;
      }
    StringTokenizer tokensCep = new StringTokenizer (municipio.getConteudoAtual (2), ",");
    while (tokensCep.hasMoreTokens ())
      {
	String tokenAtual = tokensCep.nextToken ();
	String[] ceps = tokenAtual.split ("-");
	long cepFaixaInicial = Long.parseLong (ceps[0]);
	long cepFaixaFinal = Long.parseLong (ceps[1]);
	long cepDigitado = Long.parseLong (cep.getConteudoFormatado ().replaceAll ("-", ""));
	if (cepDigitado >= cepFaixaInicial && cepDigitado <= cepFaixaFinal)
	  {
	    cep.sinalizaValidoEdit ();
	    municipio.sinalizaValidoEdit ();
	    return null;
	  }
      }
    return new RetornoValidacao (getMensagemValidacao (), getSeveridade ());
  }
}
