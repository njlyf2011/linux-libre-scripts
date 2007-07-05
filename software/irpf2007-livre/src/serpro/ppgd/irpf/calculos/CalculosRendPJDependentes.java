/* CalculosRendPJDependentes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import java.util.Hashtable;
import java.util.Iterator;

import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.rendpj.ColecaoRendPJTitular;
import serpro.ppgd.irpf.rendpj.RendPJTitular;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosRendPJDependentes extends Observador
{
  private ColecaoRendPJTitular colecaoRendPJ = null;
  private DeclaracaoIRPF declaracaoIRPF;
  
  public CalculosRendPJDependentes (ColecaoRendPJTitular colecao, DeclaracaoIRPF dec)
  {
    colecaoRendPJ = colecao;
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    RendPJTitular rendPJ = (RendPJTitular) valorNovo;
	    rendPJ.addObservador (this);
	    calculaTotaisRendRecebidoPJ ();
	    calculaTotaisContribPrev ();
	    calculaTotaisDecimoTerceiro ();
	    calculaTotaisImpostoRetido ();
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    RendPJTitular rendPJ = (RendPJTitular) valorNovo;
	    rendPJ.removeObservador (this);
	    calculaTotaisRendRecebidoPJ ();
	    calculaTotaisContribPrev ();
	    calculaTotaisDecimoTerceiro ();
	    calculaTotaisImpostoRetido ();
	  }
	else if (nomePropriedade.equals ("Contr. Prev. Oficial"))
	  calculaTotaisContribPrev ();
	else if (nomePropriedade.equals ("13\u00ba Sal\u00e1rio"))
	  calculaTotaisDecimoTerceiro ();
	else if (nomePropriedade.equals ("IR Retido na Fonte"))
	  calculaTotaisImpostoRetido ();
	else if (nomePropriedade.equals ("Rend. Recebid") || nomePropriedade.equals ("NI da Fonte Pagadora"))
	  calculaTotaisRendRecebidoPJ ();
      }
  }
  
  private void calculaTotaisImpostoRetido ()
  {
    Valor total = new Valor ();
    Iterator itRend = colecaoRendPJ.recuperarLista ().iterator ();
    while (itRend.hasNext ())
      {
	RendPJTitular rendAtual = (RendPJTitular) itRend.next ();
	total.append ('+', rendAtual.getImpostoRetidoFonte ());
      }
    colecaoRendPJ.getTotaisImpostoRetidoFonte ().setConteudo (total);
  }
  
  private void calculaTotaisDecimoTerceiro ()
  {
    Valor total = new Valor ();
    Iterator itRend = colecaoRendPJ.recuperarLista ().iterator ();
    while (itRend.hasNext ())
      {
	RendPJTitular rendAtual = (RendPJTitular) itRend.next ();
	total.append ('+', rendAtual.getDecimoTerceiro ());
      }
    colecaoRendPJ.getTotaisDecimoTerceiro ().setConteudo (total);
    declaracaoIRPF.getRendTributacaoExclusiva ().getDecimoTerceiroDependentes ().setConteudo (colecaoRendPJ.getTotaisDecimoTerceiro ().getConteudo ());
  }
  
  private void calculaTotaisContribPrev ()
  {
    Valor total = new Valor ();
    Iterator itRend = colecaoRendPJ.recuperarLista ().iterator ();
    while (itRend.hasNext ())
      {
	RendPJTitular rendAtual = (RendPJTitular) itRend.next ();
	total.append ('+', rendAtual.getContribuicaoPrevOficial ());
      }
    colecaoRendPJ.getTotaisContribuicaoPrevOficial ().setConteudo (total);
  }
  
  private void calculaTotaisRendRecebidoPJ ()
  {
    RendPJTitular rendMaiorFonte = null;
    java.util.Map niRendimentos = new Hashtable ();
    colecaoRendPJ.getNiMaiorFontePagadora ().clear ();
    Valor totaisRendRecebidoPJ = new Valor ();
    Iterator itRend = colecaoRendPJ.recuperarLista ().iterator ();
    colecaoRendPJ.getNiMaiorFontePagadora ().clear ();
    colecaoRendPJ.getTotaisRendRecebidoPJ ().clear ();
    while (itRend.hasNext ())
      {
	RendPJTitular rendAtual = (RendPJTitular) itRend.next ();
	totaisRendRecebidoPJ.append ('+', rendAtual.getRendRecebidoPJ ());
	if (! niRendimentos.containsKey (rendAtual.getNIFontePagadora ().asString ()))
	  {
	    Valor rend = new Valor ();
	    rend.setConteudo (rendAtual.getRendRecebidoPJ ().getConteudoFormatado ());
	    niRendimentos.put (rendAtual.getNIFontePagadora ().asString (), rend);
	  }
	else
	  {
	    Valor rend = (Valor) niRendimentos.get (rendAtual.getNIFontePagadora ().asString ());
	    rend.append ('+', rend.getConteudoFormatado ());
	  }
	if (rendMaiorFonte == null)
	  rendMaiorFonte = rendAtual;
	else
	  {
	    Valor rend = (Valor) niRendimentos.get (rendAtual.getNIFontePagadora ().asString ());
	    Valor rendMaior = (Valor) niRendimentos.get (rendMaiorFonte.getNIFontePagadora ().asString ());
	    if (rend.comparacao (">", rendMaior.getConteudoFormatado ()))
	      rendMaiorFonte = rendAtual;
	  }
	colecaoRendPJ.getNiMaiorFontePagadora ().setConteudo (rendMaiorFonte.getNIFontePagadora ().getConteudoFormatado ());
	colecaoRendPJ.getTotaisRendRecebidoPJ ().setConteudo (totaisRendRecebidoPJ);
      }
  }
}
