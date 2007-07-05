/* CalculosTotaisRendaVariavel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.rendavariavel.GanhosLiquidosOuPerdas;
import serpro.ppgd.irpf.rendavariavel.RendaVariavel;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosTotaisRendaVariavel extends Observador
{
  private RendaVariavel rendaVariavel = null;
  
  public CalculosTotaisRendaVariavel (DeclaracaoIRPF dec)
  {
    rendaVariavel = dec.getRendaVariavel ();
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    setAtivo (false);
    calculaTotaisRendaVariavel (nomePropriedade);
    setAtivo (true);
  }
  
  public void calculaTotaisRendaVariavel (String nomePropriedade)
  {
    Valor totalBaseCalculo = new Valor ();
    Valor totalIRFonteDayTrade = new Valor ();
    Valor totalImpostoPago = new Valor ();
    Valor totalImpRetidoLei11033 = new Valor ();
    if ("BASE CALCULO".equals (nomePropriedade))
      {
	for (int i = 0; i <= 11; i++)
	  {
	    GanhosLiquidosOuPerdas ganhosMesAtual = rendaVariavel.getGanhosPorIndice (i);
	    totalBaseCalculo.append ('+', ganhosMesAtual.getOperacoesComuns ().getBaseCalculoImposto ());
	    totalBaseCalculo.append ('+', ganhosMesAtual.getOperacoesDayTrade ().getBaseCalculoImposto ());
	  }
	rendaVariavel.getTotalBaseCalculo ().setConteudo (totalBaseCalculo);
      }
    else if ("IRFONTEDAYTRADE".equals (nomePropriedade))
      {
	for (int i = 0; i <= 11; i++)
	  {
	    GanhosLiquidosOuPerdas ganhosMesAtual = rendaVariavel.getGanhosPorIndice (i);
	    totalIRFonteDayTrade.append ('+', ganhosMesAtual.getIrFonteDayTradeMesAtual ());
	  }
	rendaVariavel.getTotalIRFonteDayTrade ().setConteudo (totalIRFonteDayTrade);
      }
    else if ("IMP PAGO".equals (nomePropriedade))
      {
	for (int i = 0; i <= 11; i++)
	  {
	    GanhosLiquidosOuPerdas ganhosMesAtual = rendaVariavel.getGanhosPorIndice (i);
	    totalImpostoPago.append ('+', ganhosMesAtual.getImpostoPago ());
	  }
	rendaVariavel.getTotalImpostoPago ().setConteudo (totalImpostoPago);
      }
    else if ("IR LEI 11033".equals (nomePropriedade))
      {
	for (int i = 0; i <= 11; i++)
	  {
	    GanhosLiquidosOuPerdas ganhosMesAtual = rendaVariavel.getGanhosPorIndice (i);
	    totalImpRetidoLei11033.append ('+', ganhosMesAtual.getImpostoRetidoFonteLei11033 ());
	  }
	rendaVariavel.getTotalImpostoRetidoFonteLei11033 ().setConteudo (totalImpRetidoLei11033);
      }
  }
}
