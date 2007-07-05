/* CalculosReceitasDespesasARBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosReceitasDespesasARBrasil extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public CalculosReceitasDespesasARBrasil (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("RECEITA"))
	  calculaTotalReceitas ();
	else if (nomePropriedade.equals ("DESPESA"))
	  calculaTotalDespesas ();
      }
  }
  
  private void calculaTotalDespesas ()
  {
    Valor totalDespesas = new Valor ();
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJaneiro ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getFevereiro ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getMarco ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getAbril ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getMaio ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJunho ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJulho ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getAgosto ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getSetembro ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getOutubro ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getNovembro ().getDespesaCusteioInvestimento ());
    totalDespesas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getDezembro ().getDespesaCusteioInvestimento ());
    declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getTotalDespesas ().setConteudo (totalDespesas);
  }
  
  private void calculaTotalReceitas ()
  {
    Valor totalReceitas = new Valor ();
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJaneiro ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getFevereiro ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getMarco ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getAbril ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getMaio ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJunho ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJulho ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getAgosto ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getSetembro ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getOutubro ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getNovembro ().getReceitaBrutaMensal ());
    totalReceitas.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getDezembro ().getReceitaBrutaMensal ());
    declaracaoIRPF.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getTotalReceita ().setConteudo (totalReceitas);
  }
}
