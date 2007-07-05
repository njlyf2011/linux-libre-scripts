/* CalculosReceitaDespesaARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import java.util.Iterator;

import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.atividaderural.exterior.ReceitaDespesa;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosReceitaDespesaARExterior extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosReceitaDespesaARExterior (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  ((ReceitaDespesa) valorNovo).getResultadoI_EmDolar ().addObservador (this);
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  ((ReceitaDespesa) valorNovo).getResultadoI_EmDolar ().removeObservador (this);
	calculaTotais ();
      }
  }
  
  private void calculaTotais ()
  {
    Valor totais = new Valor ();
    Iterator it = declaracaoIRPF.getAtividadeRural ().getExterior ().getReceitasDespesas ().recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	ReceitaDespesa receitaDespesa = (ReceitaDespesa) it.next ();
	totais.append ('+', receitaDespesa.getResultadoI_EmDolar ());
      }
    declaracaoIRPF.getAtividadeRural ().getExterior ().getReceitasDespesas ().getTotais ().setConteudo (totais);
  }
}
