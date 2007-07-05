/* CalculosDoacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import java.util.Iterator;

import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.eleicoes.Doacao;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosDoacoes extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosDoacoes (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    Doacao doacao = (Doacao) valorNovo;
	    doacao.getValor ().addObservador (this);
	    calculoTotalDoacoes ();
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    Doacao doacao = (Doacao) valorNovo;
	    doacao.getValor ().removeObservador (this);
	    calculoTotalDoacoes ();
	  }
	else if (nomePropriedade.equals ("Valor da doa\u00e7\u00e3o"))
	  calculoTotalDoacoes ();
      }
  }
  
  public void calculoTotalDoacoes ()
  {
    Valor total = new Valor ();
    Iterator it = declaracaoIRPF.getDoacoes ().recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Valor valorDoacao = ((Doacao) it.next ()).getValor ();
	total.append ('+', valorDoacao);
      }
    declaracaoIRPF.getDoacoes ().getTotalDoacoes ().setConteudo (total);
  }
}
