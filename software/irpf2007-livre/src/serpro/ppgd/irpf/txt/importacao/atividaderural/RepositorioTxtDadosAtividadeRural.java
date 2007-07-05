/* RepositorioTxtDadosAtividadeRural - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.txt.importacao.atividaderural;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import serpro.hash.Crc32;
import serpro.ppgd.formatosexternos.txt.DocumentoTXT;
import serpro.ppgd.formatosexternos.txt.DocumentoTXTDefault;
import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.atividaderural.brasil.ApuracaoResultadoBrasil;
import serpro.ppgd.irpf.atividaderural.brasil.MesReceitaDespesa;
import serpro.ppgd.irpf.atividaderural.exterior.ApuracaoResultadoExterior;
import serpro.ppgd.irpf.atividaderural.exterior.ColecaoReceitasDespesas;
import serpro.ppgd.irpf.atividaderural.exterior.ReceitaDespesa;
import serpro.ppgd.negocio.Valor;
import serpro.util.PLong;

public class RepositorioTxtDadosAtividadeRural
{
  private String pathArquivo = null;
  private DocumentoTXT documentoTXT = null;
  private Map hashTablePaises = new Hashtable ();
  private boolean importaBrasil = true;
  private boolean importaExterior = true;
  
  public RepositorioTxtDadosAtividadeRural (String _pathArquivo, boolean _importaBrasil, boolean _importaExterior)
  {
    pathArquivo = _pathArquivo;
    documentoTXT = new DocumentoTXTDefault ("ARQ_IMPORTACAO_ATIV_RURAL", pathArquivo);
    importaBrasil = _importaBrasil;
    importaExterior = _importaExterior;
  }
  
  public void importaDados () throws GeracaoTxtException, IOException
  {
    documentoTXT.ler ();
    verificaCRC ();
    Iterator it = null;
    it = documentoTXT.getRegistrosTxt ("04").iterator ();
    while (it.hasNext ())
      {
	RegistroTxt reg = (RegistroTxt) it.next ();
	String flagMes = reg.fieldByName ("Flag Mes").asString ();
	String cdPais = reg.fieldByName ("CD_PAIS").asString ().toString ();
	Valor receitas = reg.fieldByName ("receitas").asValor ();
	Valor despesas = reg.fieldByName ("despesas").asValor ();
	Valor produtosEntregueAdiantamento = reg.fieldByName ("ProdutosEntregueAdiantamento").asValor ();
	Valor adiantamentoRecebido = reg.fieldByName ("AdiantamentoRecebido").asValor ();
	if (cdPais.equals ("105"))
	  {
	    DadoAtividadeRuralBrasil dadoAtividadeRuralBrasil = null;
	    if (hashTablePaises.containsKey (cdPais))
	      dadoAtividadeRuralBrasil = (DadoAtividadeRuralBrasil) hashTablePaises.get (cdPais);
	    else
	      {
		dadoAtividadeRuralBrasil = new DadoAtividadeRuralBrasil ();
		hashTablePaises.put (cdPais, dadoAtividadeRuralBrasil);
	      }
	    ItemMensalARBrasil itemMensalARBrasil = new ItemMensalARBrasil ();
	    itemMensalARBrasil.receita.setConteudo (receitas);
	    itemMensalARBrasil.despesa.setConteudo (despesas);
	    itemMensalARBrasil.produtoEntregueAdiantamento.setConteudo (produtosEntregueAdiantamento);
	    itemMensalARBrasil.adiantamentoRecebido.setConteudo (adiantamentoRecebido);
	    dadoAtividadeRuralBrasil.add (itemMensalARBrasil);
	    dadoAtividadeRuralBrasil.totalAdiantamentoRecebido.append ('+', itemMensalARBrasil.adiantamentoRecebido);
	    dadoAtividadeRuralBrasil.totalProdutoEntregueAdiantamento.append ('+', itemMensalARBrasil.produtoEntregueAdiantamento);
	  }
	else
	  {
	    DadoAtividadeRuralExterior dadoAtividadeRuralExterior = null;
	    if (hashTablePaises.containsKey (cdPais))
	      dadoAtividadeRuralExterior = (DadoAtividadeRuralExterior) hashTablePaises.get (cdPais);
	    else
	      {
		dadoAtividadeRuralExterior = new DadoAtividadeRuralExterior (cdPais);
		hashTablePaises.put (cdPais, dadoAtividadeRuralExterior);
	      }
	    dadoAtividadeRuralExterior.totalReceitas.append ('+', receitas);
	    dadoAtividadeRuralExterior.totalDespesas.append ('+', despesas);
	    dadoAtividadeRuralExterior.totalAdiantamentoRecebido.append ('+', adiantamentoRecebido);
	    dadoAtividadeRuralExterior.totalProdutoEntregueAdiantamento.append ('+', produtosEntregueAdiantamento);
	  }
      }
    if (importaBrasil)
      importaBrasil ();
    if (importaExterior)
      importaExterior ();
  }
  
  private void importaExterior ()
  {
    if (hashTablePaises.containsKey ("105"))
      hashTablePaises.remove ("105");
    Iterator itPaises = hashTablePaises.values ().iterator ();
    while (itPaises.hasNext ())
      {
	DadoAtividadeRuralExterior dadoAtividadeRuralExterior = (DadoAtividadeRuralExterior) itPaises.next ();
	ColecaoReceitasDespesas receitasDespesasExterior = IRPFFacade.getInstancia ().getAtividadeRural ().getExterior ().getReceitasDespesas ();
	ReceitaDespesa receita = new ReceitaDespesa ();
	receita.getPais ().setConteudo (dadoAtividadeRuralExterior.codPais);
	receita.getReceitaBruta ().setConteudo (dadoAtividadeRuralExterior.totalReceitas);
	receita.getDespesaCusteio ().setConteudo (dadoAtividadeRuralExterior.totalDespesas);
	receitasDespesasExterior.recuperarLista ().add (receita);
	ApuracaoResultadoExterior apuracaoResultadoExterior = IRPFFacade.getInstancia ().getAtividadeRural ().getExterior ().getApuracaoResultado ();
	apuracaoResultadoExterior.getReceitaRecebidaContaVenda ().setConteudo (dadoAtividadeRuralExterior.totalAdiantamentoRecebido);
	apuracaoResultadoExterior.getValorAdiantamento ().setConteudo (dadoAtividadeRuralExterior.totalProdutoEntregueAdiantamento);
      }
  }
  
  private void importaBrasil ()
  {
    DadoAtividadeRuralBrasil dadoAtividadeRuralBrasil = (DadoAtividadeRuralBrasil) hashTablePaises.remove ("105");
    for (int i = 0; i < dadoAtividadeRuralBrasil.size (); i++)
      preencheItemBrasil (i, dadoAtividadeRuralBrasil);
    ApuracaoResultadoBrasil apuracaoResultadoBrasil = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getApuracaoResultado ();
    apuracaoResultadoBrasil.getReceitaRecebidaContaVenda ().setConteudo (dadoAtividadeRuralBrasil.totalAdiantamentoRecebido);
    apuracaoResultadoBrasil.getValorAdiantamento ().setConteudo (dadoAtividadeRuralBrasil.totalProdutoEntregueAdiantamento);
  }
  
  private void preencheItemBrasil (int i, DadoAtividadeRuralBrasil dadoAtividadeRuralBrasil)
  {
    ItemMensalARBrasil itemMensalARBrasil = (ItemMensalARBrasil) dadoAtividadeRuralBrasil.get (i);
    MesReceitaDespesa mesReceitaDespesa = null;
    switch (i)
      {
      case 0:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJaneiro ();
	break;
      case 1:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getFevereiro ();
	break;
      case 2:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getMarco ();
	break;
      case 3:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getAbril ();
	break;
      case 4:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getMaio ();
	break;
      case 5:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJunho ();
	break;
      case 6:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getJulho ();
	break;
      case 7:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getAgosto ();
	break;
      case 8:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getSetembro ();
	break;
      case 9:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getOutubro ();
	break;
      case 10:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getNovembro ();
	break;
      case 11:
	mesReceitaDespesa = IRPFFacade.getInstancia ().getAtividadeRural ().getBrasil ().getReceitasDespesas ().getDezembro ();
	break;
      }
    mesReceitaDespesa.getReceitaBrutaMensal ().setConteudo (itemMensalARBrasil.receita);
    mesReceitaDespesa.getDespesaCusteioInvestimento ().setConteudo (itemMensalARBrasil.despesa);
  }
  
  public void verificaCRC () throws GeracaoTxtException
  {
    PLong pLongAcumulado = new PLong ();
    Crc32 crc32Acumulado = new Crc32 ();
    long hashCalculadoLinhaAnterior = 0L;
    for (int i = 0; i < documentoTXT.arquivo ().size () - 2; i++)
      {
	String linha = (String) documentoTXT.arquivo ().elementAt (i);
	if (hashCalculadoLinhaAnterior != 0L)
	  pLongAcumulado.setValue (hashCalculadoLinhaAnterior);
	hashCalculadoLinhaAnterior = crc32Acumulado.CalcCrc32 (linha, linha.length (), pLongAcumulado);
      }
    String crcAcumuladoFinal = String.valueOf (hashCalculadoLinhaAnterior);
    RegistroTxt reg = (RegistroTxt) documentoTXT.getRegistrosTxt ("99").get (0);
    String crcLido = reg.fieldByName ("HASHCODE").asString ();
    if (! crcLido.equals (crcAcumuladoFinal))
      throw new GeracaoTxtException ("Arquivo corrompido");
  }
}
