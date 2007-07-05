/* ReceitasDespesas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class ReceitasDespesas extends ObjetoNegocio
{
  private MesReceitaDespesa janeiro = new MesReceitaDespesa ();
  private MesReceitaDespesa fevereiro = new MesReceitaDespesa ();
  private MesReceitaDespesa marco = new MesReceitaDespesa ();
  private MesReceitaDespesa abril = new MesReceitaDespesa ();
  private MesReceitaDespesa maio = new MesReceitaDespesa ();
  private MesReceitaDespesa junho = new MesReceitaDespesa ();
  private MesReceitaDespesa julho = new MesReceitaDespesa ();
  private MesReceitaDespesa agosto = new MesReceitaDespesa ();
  private MesReceitaDespesa setembro = new MesReceitaDespesa ();
  private MesReceitaDespesa outubro = new MesReceitaDespesa ();
  private MesReceitaDespesa novembro = new MesReceitaDespesa ();
  private MesReceitaDespesa dezembro = new MesReceitaDespesa ();
  private Valor totalReceita = new Valor (this, "");
  private Valor totalDespesas = new Valor (this, "");
  
  public ReceitasDespesas ()
  {
    setFicha ("Receitas e Despesas - BRASIL");
  }
  
  public void addObservadorCalculosTotais (Observador obs)
  {
    janeiro.getReceitaBrutaMensal ().addObservador (obs);
    janeiro.getDespesaCusteioInvestimento ().addObservador (obs);
    fevereiro.getReceitaBrutaMensal ().addObservador (obs);
    fevereiro.getDespesaCusteioInvestimento ().addObservador (obs);
    marco.getReceitaBrutaMensal ().addObservador (obs);
    marco.getDespesaCusteioInvestimento ().addObservador (obs);
    abril.getReceitaBrutaMensal ().addObservador (obs);
    abril.getDespesaCusteioInvestimento ().addObservador (obs);
    maio.getReceitaBrutaMensal ().addObservador (obs);
    maio.getDespesaCusteioInvestimento ().addObservador (obs);
    junho.getReceitaBrutaMensal ().addObservador (obs);
    junho.getDespesaCusteioInvestimento ().addObservador (obs);
    julho.getReceitaBrutaMensal ().addObservador (obs);
    julho.getDespesaCusteioInvestimento ().addObservador (obs);
    agosto.getReceitaBrutaMensal ().addObservador (obs);
    agosto.getDespesaCusteioInvestimento ().addObservador (obs);
    setembro.getReceitaBrutaMensal ().addObservador (obs);
    setembro.getDespesaCusteioInvestimento ().addObservador (obs);
    outubro.getReceitaBrutaMensal ().addObservador (obs);
    outubro.getDespesaCusteioInvestimento ().addObservador (obs);
    novembro.getReceitaBrutaMensal ().addObservador (obs);
    novembro.getDespesaCusteioInvestimento ().addObservador (obs);
    dezembro.getReceitaBrutaMensal ().addObservador (obs);
    dezembro.getDespesaCusteioInvestimento ().addObservador (obs);
  }
  
  public MesReceitaDespesa getMesReceitaPorIndice (int mes)
  {
    if (mes == 0)
      return janeiro;
    if (mes == 1)
      return fevereiro;
    if (mes == 2)
      return marco;
    if (mes == 3)
      return abril;
    if (mes == 4)
      return maio;
    if (mes == 5)
      return junho;
    if (mes == 6)
      return julho;
    if (mes == 7)
      return agosto;
    if (mes == 8)
      return setembro;
    if (mes == 9)
      return outubro;
    if (mes == 10)
      return novembro;
    if (mes == 11)
      return dezembro;
    return null;
  }
  
  public String obterMesFormatoNumerico (MesReceitaDespesa mesReceita)
  {
    if (mesReceita.equals (janeiro))
      return "01";
    if (mesReceita.equals (fevereiro))
      return "02";
    if (mesReceita.equals (marco))
      return "03";
    if (mesReceita.equals (abril))
      return "04";
    if (mesReceita.equals (maio))
      return "05";
    if (mesReceita.equals (junho))
      return "06";
    if (mesReceita.equals (julho))
      return "07";
    if (mesReceita.equals (agosto))
      return "08";
    if (mesReceita.equals (setembro))
      return "09";
    if (mesReceita.equals (outubro))
      return "10";
    if (mesReceita.equals (novembro))
      return "11";
    if (mesReceita.equals (dezembro))
      return "12";
    return null;
  }
  
  public MesReceitaDespesa getAbril ()
  {
    return abril;
  }
  
  public MesReceitaDespesa getAgosto ()
  {
    return agosto;
  }
  
  public MesReceitaDespesa getDezembro ()
  {
    return dezembro;
  }
  
  public MesReceitaDespesa getFevereiro ()
  {
    return fevereiro;
  }
  
  public MesReceitaDespesa getJaneiro ()
  {
    return janeiro;
  }
  
  public MesReceitaDespesa getJulho ()
  {
    return julho;
  }
  
  public MesReceitaDespesa getJunho ()
  {
    return junho;
  }
  
  public MesReceitaDespesa getMaio ()
  {
    return maio;
  }
  
  public MesReceitaDespesa getMarco ()
  {
    return marco;
  }
  
  public MesReceitaDespesa getNovembro ()
  {
    return novembro;
  }
  
  public MesReceitaDespesa getOutubro ()
  {
    return outubro;
  }
  
  public MesReceitaDespesa getSetembro ()
  {
    return setembro;
  }
  
  public void setTotalReceita (Valor totalMes)
  {
    totalReceita = totalMes;
  }
  
  public Valor getTotalReceita ()
  {
    return totalReceita;
  }
  
  public void setTotalDespesas (Valor totalReceita)
  {
    totalDespesas = totalReceita;
  }
  
  public Valor getTotalDespesas ()
  {
    return totalDespesas;
  }
  
  public void clear ()
  {
    super.clear ();
    janeiro.clear ();
    fevereiro.clear ();
    marco.clear ();
    abril.clear ();
    maio.clear ();
    junho.clear ();
    julho.clear ();
    agosto.clear ();
    setembro.clear ();
    outubro.clear ();
    novembro.clear ();
    dezembro.clear ();
  }
}
