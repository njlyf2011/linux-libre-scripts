/* ReceitaDespesa - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class ReceitaDespesa extends ObjetoNegocio
{
  private Codigo pais = new Codigo (this, "", CadastroTabelasIRPF.recuperarPaisesExterior ());
  private Alfa descricaoPais = new Alfa (this, "");
  private Valor receitaBruta = new Valor (this, "");
  private Valor despesaCusteio = new Valor (this, "");
  private Valor resultadoIMoedaOriginal = new Valor (this, "");
  private Valor resultadoI_EmDolar = new Valor (this, "");
  
  public ReceitaDespesa ()
  {
    resultadoIMoedaOriginal.setReadOnly (true);
    receitaBruta.addObservador (this);
    despesaCusteio.addObservador (this);
    getPais ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	descricaoPais.setConteudo (getPais ().getConteudoAtual (1));
      }
    });
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    resultadoIMoedaOriginal.setConteudo (receitaBruta.operacao ('-', despesaCusteio));
  }
  
  public Valor getDespesaCusteio ()
  {
    return despesaCusteio;
  }
  
  public Codigo getPais ()
  {
    return pais;
  }
  
  public Valor getReceitaBruta ()
  {
    return receitaBruta;
  }
  
  public Valor getResultadoIMoedaOriginal ()
  {
    return resultadoIMoedaOriginal;
  }
  
  public Valor getResultadoI_EmDolar ()
  {
    return resultadoI_EmDolar;
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarListaCamposPendencia ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio ())
	  return false;
      }
    return true;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    return retorno;
  }
}
