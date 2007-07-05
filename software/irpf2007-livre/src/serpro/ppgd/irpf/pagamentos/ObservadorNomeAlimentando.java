/* ObservadorNomeAlimentando - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.pagamentos;
import java.util.Iterator;

import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.alimentandos.Alimentando;
import serpro.ppgd.negocio.Observador;

public class ObservadorNomeAlimentando extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public ObservadorNomeAlimentando (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("ObjetoInserido"))
	  {
	    Alimentando alimentando = (Alimentando) valorNovo;
	    alimentando.getNome ().addObservador (this);
	  }
	else if (nomePropriedade.equals ("ObjetoRemovido"))
	  {
	    Alimentando alimentandos = (Alimentando) valorNovo;
	    alimentandos.getNome ().removeObservador (this);
	  }
	else if (nomePropriedade.equals ("Nome"))
	  atualizaPagamentos ((String) valorAntigo, (String) valorNovo);
      }
  }
  
  private void atualizaPagamentos (String nomeAntigo, String nomeNovo)
  {
    Iterator it = declaracaoIRPF.getPagamentos ().recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	Pagamento pagamento = (Pagamento) it.next ();
	String codPagamento = pagamento.getCodigo ().getConteudoAtual (0);
	if ((codPagamento.equals ("05") || codPagamento.equals ("06")) && pagamento.getDependenteOuAlimentando ().asString ().equals (nomeAntigo))
	  pagamento.getDependenteOuAlimentando ().setConteudo (nomeNovo);
      }
  }
}
