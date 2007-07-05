/* ObservadorConjuge - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.conjuge;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class ObservadorConjuge extends Observador
{
  private Conjuge conjuge = null;
  
  public ObservadorConjuge (Conjuge conj)
  {
    conjuge = conj;
  }
  
  public Valor recuperarResultadoConjuge ()
  {
    Valor result = new Valor ();
    if (conjuge.getDecSimplificada ().asString ().equals ("0"))
      {
	result.setConteudo (conjuge.getBaseCalculoImposto ());
	result.append ('-', conjuge.getImpRetidoFonte ());
	result.append ('+', conjuge.getRendIsentoNaoTributaveis ());
	result.append ('+', conjuge.getRendSujeitosTribExcl ());
      }
    else
      {
	result.setConteudo (conjuge.getBaseCalculoImposto ());
	result.append ('-', conjuge.getImpRetidoFonte ());
	result.append ('-', conjuge.getCarneComImpComplementar ());
	result.append ('+', conjuge.getRendIsentoNaoTributaveis ());
	result.append ('+', conjuge.getRendSujeitosTribExcl ());
      }
    if (result.comparacao ("<", "0,00"))
      result.clear ();
    conjuge.getResultado ().setConteudo (result);
    return conjuge.getResultado ();
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("CPF"))
	  {
	    conjuge.getCpfConjuge ().validar ();
	    if (conjuge.getCpfConjuge ().isVazio () || ! conjuge.getCpfConjuge ().isValido ())
	      {
		desabilitaValores ();
		limpaValores ();
		conjuge.getDecSimplificada ().setHabilitado (false);
	      }
	    else
	      conjuge.getDecSimplificada ().setHabilitado (true);
	  }
	else if (nomePropriedade.equals ("O c\u00f4njuge apresentou declara\u00e7\u00e3o de ajuste anual simplificada?"))
	  {
	    if (! conjuge.getDecSimplificada ().isVazio ())
	      habilitaValores ();
	    else
	      desabilitaValores ();
	    if (conjuge.getDecSimplificada ().asString ().equals ("1"))
	      {
		conjuge.getCarneComImpComplementar ().setReadOnly (false);
		if (! conjuge.getValAntigoCarneLeao ().isVazio ())
		  conjuge.getCarneComImpComplementar ().setConteudo (conjuge.getValAntigoCarneLeao ());
		if (! conjuge.getValAntigoImpostoRetidoFonte ().isVazio ())
		  conjuge.getImpRetidoFonte ().setConteudo (conjuge.getValAntigoImpostoRetidoFonte ());
	      }
	    else if (conjuge.getDecSimplificada ().asString ().equals ("0"))
	      {
		conjuge.getCarneComImpComplementar ().setReadOnly (true);
		conjuge.getValAntigoCarneLeao ().setConteudo (conjuge.getCarneComImpComplementar ());
		conjuge.getValAntigoImpostoRetidoFonte ().setConteudo (conjuge.getImpRetidoFonte ());
		conjuge.getImpRetidoFonte ().setConteudo (conjuge.getImpRetidoFonte ().operacao ('+', conjuge.getCarneComImpComplementar ()));
		conjuge.getCarneComImpComplementar ().clear ();
	      }
	  }
	else if (nomePropriedade.equals ("Base de c\u00e1lculo") || nomePropriedade.equals ("Base de c\u00e1lculo") || nomePropriedade.equals ("Imposto retido na fonte") || nomePropriedade.equals ("Carn\u00ea-Le\u00e3o e imposto complementar") || nomePropriedade.equals ("Rendimentos isentos e n\u00e3o-tribut\u00e1veis") || nomePropriedade.equals ("Rendimentos sujeitos \u00e0 tributa\u00e7\u00e3o exclusiva"))
	  recuperarResultadoConjuge ();
      }
  }
  
  private void limpaValores ()
  {
    conjuge.getDecSimplificada ().clear ();
    conjuge.getBaseCalculoImposto ().clear ();
    conjuge.getImpRetidoFonte ().clear ();
    conjuge.getCarneComImpComplementar ().clear ();
    conjuge.getRendIsentoNaoTributaveis ().clear ();
    conjuge.getRendSujeitosTribExcl ().clear ();
    conjuge.getResultado ().clear ();
    conjuge.getValAntigoCarneLeao ().clear ();
    conjuge.getValAntigoImpostoRetidoFonte ().clear ();
  }
  
  private void desabilitaValores ()
  {
    conjuge.getBaseCalculoImposto ().setHabilitado (false);
    conjuge.getImpRetidoFonte ().setHabilitado (false);
    conjuge.getCarneComImpComplementar ().setReadOnly (true);
    conjuge.getRendIsentoNaoTributaveis ().setHabilitado (false);
    conjuge.getRendSujeitosTribExcl ().setHabilitado (false);
  }
  
  private void habilitaValores ()
  {
    conjuge.getBaseCalculoImposto ().setHabilitado (true);
    conjuge.getImpRetidoFonte ().setHabilitado (true);
    conjuge.getCarneComImpComplementar ().setReadOnly (false);
    conjuge.getRendIsentoNaoTributaveis ().setHabilitado (true);
    conjuge.getRendSujeitosTribExcl ().setHabilitado (true);
  }
}
