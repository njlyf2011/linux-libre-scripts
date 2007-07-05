/* RendPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpf;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class RendPF extends ObjetoNegocio
{
  public static final String FICHA_TIT = "Rendimentos Tribut\u00e1veis Recebidos de PF e do Exterior - Titular";
  public static final String FICHA_DEP = "Rendimentos Tribut\u00e1veis Recebidos de PF e do Exterior - Dependentes";
  public static final String NOME_TOTAL_PF = "Total Pessoa F\u00edsica";
  public static final String NOME_TOTAL_EXTERIOR = "Total Exterior";
  public static final String NOME_TOTAL_PREVIDENCIA = "Total Previd\u00eancia";
  public static final String NOME_TOTAL_DEPENDENTES = "Total Dependentes";
  public static final String NOME_TOTAL_PENSAO = "Total Pens\u00e3o";
  public static final String NOME_TOTAL_LIVROCAIXA = "Total Livro Caixa";
  public static final String NOME_TOTAL_DARF = "Total DARF";
  private MesRendPF janeiro = new MesRendPF ();
  private MesRendPF fevereiro = new MesRendPF ();
  private MesRendPF marco = new MesRendPF ();
  private MesRendPF abril = new MesRendPF ();
  private MesRendPF maio = new MesRendPF ();
  private MesRendPF junho = new MesRendPF ();
  private MesRendPF julho = new MesRendPF ();
  private MesRendPF agosto = new MesRendPF ();
  private MesRendPF setembro = new MesRendPF ();
  private MesRendPF outubro = new MesRendPF ();
  private MesRendPF novembro = new MesRendPF ();
  private MesRendPF dezembro = new MesRendPF ();
  protected MesRendPF[] meses = { janeiro, fevereiro, marco, abril, maio, junho, julho, agosto, setembro, outubro, novembro, dezembro };
  private Valor totalPessoaFisica = new Valor (this, "Total Pessoa F\u00edsica");
  private Valor totalExterior = new Valor (this, "Total Exterior");
  private Valor totalPrevidencia = new Valor (this, "Total Previd\u00eancia");
  private Valor totalDependentes = new Valor (this, "Total Dependentes");
  private Valor totalPensao = new Valor (this, "Total Pens\u00e3o");
  private Valor totalLivroCaixa = new Valor (this, "Total Livro Caixa");
  private Valor totalDarfPago = new Valor (this, "Total DARF");
  
  public RendPF ()
  {
    aplicaNomeFicha ();
  }
  
  public void aplicaNomeFicha ()
  {
    setFicha ("Rendimentos Tribut\u00e1veis Recebidos de PF e do Exterior - Titular");
    for (int i = 0; i < 12; i++)
      meses[i].setFicha ("Rendimentos Tribut\u00e1veis Recebidos de PF e do Exterior - Titular");
  }
  
  public MesRendPF getMesRendPFPorIndice (int mes)
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
  
  public int obterMesFormatoNumerico (MesRendPF ganhos)
  {
    if (ganhos.equals (janeiro))
      return 0;
    if (ganhos.equals (fevereiro))
      return 1;
    if (ganhos.equals (marco))
      return 2;
    if (ganhos.equals (abril))
      return 3;
    if (ganhos.equals (maio))
      return 4;
    if (ganhos.equals (junho))
      return 5;
    if (ganhos.equals (julho))
      return 6;
    if (ganhos.equals (agosto))
      return 7;
    if (ganhos.equals (setembro))
      return 8;
    if (ganhos.equals (outubro))
      return 9;
    if (ganhos.equals (novembro))
      return 10;
    if (ganhos.equals (dezembro))
      return 11;
    return 0;
  }
  
  public void addObservador (Observador obs)
  {
    janeiro.addObservador (obs);
    fevereiro.addObservador (obs);
    marco.addObservador (obs);
    abril.addObservador (obs);
    maio.addObservador (obs);
    junho.addObservador (obs);
    julho.addObservador (obs);
    agosto.addObservador (obs);
    setembro.addObservador (obs);
    outubro.addObservador (obs);
    novembro.addObservador (obs);
    dezembro.addObservador (obs);
  }
  
  public MesRendPF getAbril ()
  {
    return abril;
  }
  
  public MesRendPF getAgosto ()
  {
    return agosto;
  }
  
  public MesRendPF getDezembro ()
  {
    return dezembro;
  }
  
  public MesRendPF getFevereiro ()
  {
    return fevereiro;
  }
  
  public MesRendPF getJaneiro ()
  {
    return janeiro;
  }
  
  public MesRendPF getJulho ()
  {
    return julho;
  }
  
  public MesRendPF getJunho ()
  {
    return junho;
  }
  
  public MesRendPF getMaio ()
  {
    return maio;
  }
  
  public MesRendPF getMarco ()
  {
    return marco;
  }
  
  public MesRendPF getNovembro ()
  {
    return novembro;
  }
  
  public MesRendPF getOutubro ()
  {
    return outubro;
  }
  
  public MesRendPF getSetembro ()
  {
    return setembro;
  }
  
  public Valor getTotalDarfPago ()
  {
    return totalDarfPago;
  }
  
  public Valor getTotalDependentes ()
  {
    return totalDependentes;
  }
  
  public Valor getTotalExterior ()
  {
    return totalExterior;
  }
  
  public Valor getTotalLivroCaixa ()
  {
    return totalLivroCaixa;
  }
  
  public Valor getTotalPensao ()
  {
    return totalPensao;
  }
  
  public Valor getTotalPessoaFisica ()
  {
    return totalPessoaFisica;
  }
  
  public Valor getTotalPrevidencia ()
  {
    return totalPrevidencia;
  }
  
  public boolean isVazio ()
  {
    if (janeiro.isVazio () && fevereiro.isVazio () && marco.isVazio () && abril.isVazio () && maio.isVazio () && junho.isVazio () && julho.isVazio () && agosto.isVazio () && setembro.isVazio () && outubro.isVazio () && novembro.isVazio () && dezembro.isVazio ())
      return true;
    return false;
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
