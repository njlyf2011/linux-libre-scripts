/* ObservadorCEPMunicipio - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.contribuinte;
import java.util.StringTokenizer;

import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.Observador;

public class ObservadorCEPMunicipio extends Observador
{
  Codigo codMunicipio;
  Codigo uf;
  Codigo codigoPais;
  Informacao cep;
  
  public ObservadorCEPMunicipio (Codigo codMun, Codigo pUf, Codigo pCodPais, Informacao pCep)
  {
    codMunicipio = codMun;
    uf = pUf;
    codigoPais = pCodPais;
    cep = pCep;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (! "Val-Property".equals (nomePropriedade))
      {
	String conteudoUf = uf.getConteudoAtual (0);
	if (! uf.isReadOnly () && conteudoUf != null && conteudoUf.trim ().length () > 0)
	  {
	    String conteudoMunicipio = codMunicipio.getConteudoAtual (1);
	    if (conteudoMunicipio != null && conteudoMunicipio.trim ().length () > 0)
	      {
		StringTokenizer tokensCep = new StringTokenizer (codMunicipio.getConteudoAtual (2), ",");
		if (tokensCep.countTokens () == 1)
		  {
		    String tokenAtual = tokensCep.nextToken ();
		    String[] ceps = tokenAtual.split ("-");
		    long cepFaixaInicial = Long.parseLong (ceps[0]);
		    long cepFaixaFinal = Long.parseLong (ceps[1]);
		    if (cepFaixaFinal == cepFaixaInicial)
		      cep.setConteudo (ceps[0]);
		    else
		      cep.clear ();
		  }
	      }
	  }
      }
  }
}
