/* Valor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;

public class Valor extends Informacao
{
  protected int maximoDigitosParteInteira = ConstantesGlobais.TAMANHO_VALOR - ConstantesGlobais.TAMANHO_VALOR_PARTE_DECIMAL;
  protected String msgErroEstourodigitos = "O valor m\u00e1ximo para o tipo do campo foi excedido.";
  protected byte severidadeValidacaoMaximoDigitos = 3;
  protected Long conteudo;
  protected int casasDecimais;
  protected boolean porcentagem;
  private DecimalFormat df;
  private boolean campoCalculado;
  public static int ARREDONDA = 1;
  public static int TRUNCA = 0;
  private int tratamentocasasDecimais;
  private int pisoArredondamento;
  
  public Valor ()
  {
    conteudo = new Long (0L);
    casasDecimais = ConstantesGlobais.TAMANHO_VALOR_PARTE_DECIMAL;
    porcentagem = false;
    df = (DecimalFormat) DecimalFormat.getNumberInstance (ConstantesGlobais.LOCALIDADE);
    campoCalculado = false;
    tratamentocasasDecimais = TRUNCA;
    pisoArredondamento = 5;
    addValidador (new ValidadorMaximoDigitosInteiros (severidadeValidacaoMaximoDigitos));
  }
  
  public Valor (Long pVal)
  {
    this (pVal.toString ());
  }
  
  public Valor (String pVal)
  {
    conteudo = new Long (0L);
    casasDecimais = ConstantesGlobais.TAMANHO_VALOR_PARTE_DECIMAL;
    porcentagem = false;
    df = (DecimalFormat) DecimalFormat.getNumberInstance (ConstantesGlobais.LOCALIDADE);
    campoCalculado = false;
    tratamentocasasDecimais = TRUNCA;
    pisoArredondamento = 5;
    String parteInteira = "";
    String parteDecimal = "";
    if (pVal.lastIndexOf (',') != -1)
      parteDecimal = pVal.substring (pVal.lastIndexOf (',') + 1, pVal.length ());
    else if (pVal.lastIndexOf ('.') != -1 && pVal.indexOf ('.') == pVal.lastIndexOf ('.'))
      {
	pVal = pVal.replaceAll ("[.]", ",");
	parteDecimal = pVal.substring (pVal.lastIndexOf (',') + 1, pVal.length ());
      }
    if (parteDecimal.length () > 0)
      {
	setCasasDecimais (parteDecimal.length ());
	setConteudo (pVal);
      }
    else
      {
	setCasasDecimais (0);
	setConteudo (pVal);
      }
    addValidador (new ValidadorMaximoDigitosInteiros (severidadeValidacaoMaximoDigitos));
  }
  
  public Valor (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
    conteudo = new Long (0L);
    casasDecimais = ConstantesGlobais.TAMANHO_VALOR_PARTE_DECIMAL;
    porcentagem = false;
    df = (DecimalFormat) DecimalFormat.getNumberInstance (ConstantesGlobais.LOCALIDADE);
    campoCalculado = false;
    tratamentocasasDecimais = TRUNCA;
    pisoArredondamento = 5;
    addValidador (new ValidadorMaximoDigitosInteiros (severidadeValidacaoMaximoDigitos));
  }
  
  public Valor (ObjetoNegocio owner, String nomeCampo, int pMaximoDigitosInteiros, int pQtdCasasDecimais)
  {
    super (owner, nomeCampo);
    conteudo = new Long (0L);
    casasDecimais = ConstantesGlobais.TAMANHO_VALOR_PARTE_DECIMAL;
    porcentagem = false;
    df = (DecimalFormat) DecimalFormat.getNumberInstance (ConstantesGlobais.LOCALIDADE);
    campoCalculado = false;
    tratamentocasasDecimais = TRUNCA;
    pisoArredondamento = 5;
    addValidador (new ValidadorMaximoDigitosInteiros (severidadeValidacaoMaximoDigitos));
    setMaximoDigitosParteInteira (pMaximoDigitosInteiros);
    setCasasDecimais (pQtdCasasDecimais);
  }
  
  public Valor (ObjetoNegocio owner, String nomeCampo, boolean readOnly)
  {
    super (owner, nomeCampo);
    conteudo = new Long (0L);
    casasDecimais = ConstantesGlobais.TAMANHO_VALOR_PARTE_DECIMAL;
    porcentagem = false;
    df = (DecimalFormat) DecimalFormat.getNumberInstance (ConstantesGlobais.LOCALIDADE);
    campoCalculado = false;
    tratamentocasasDecimais = TRUNCA;
    pisoArredondamento = 5;
    setReadOnly (readOnly);
    addValidador (new ValidadorMaximoDigitosInteiros (severidadeValidacaoMaximoDigitos));
  }
  
  public void converte (Valor pVal)
  {
    converteQtdCasasDecimais (pVal.getCasasDecimais ());
    setMaximoDigitosParteInteira (pVal.getMaximoDigitosParteInteira ());
    setTratamentocasasDecimais (pVal.getTratamentocasasDecimais ());
  }
  
  public String getParteInteira ()
  {
    long conteudoAtual = conteudo.longValue ();
    long fatorCasaDecimal = 1L;
    for (int i = 0; i < casasDecimais; i++)
      fatorCasaDecimal *= 10L;
    return String.valueOf (Math.abs (conteudoAtual / fatorCasaDecimal));
  }
  
  public Long getParteInteiraAsLong ()
  {
    return new Long (getParteInteira ());
  }
  
  public String getParteDecimal ()
  {
    if (casasDecimais == 0)
      return "";
    long conteudoAtual = conteudo.longValue ();
    long fatorCasaDecimal = 1L;
    String padraoCasasDecimais = "";
    for (int i = 0; i < casasDecimais; i++)
      {
	fatorCasaDecimal *= 10L;
	padraoCasasDecimais += "0";
      }
    df.applyLocalizedPattern (padraoCasasDecimais);
    return df.format (Math.abs (conteudoAtual % fatorCasaDecimal));
  }
  
  public String getConteudoFormatado ()
  {
    df.applyLocalizedPattern ("###.###.##0");
    String negativo = "";
    String parteInteiraFormatada = df.format (new Long (getParteInteira ()));
    String parteDecimalFormatada = getParteDecimal ();
    if (conteudo.longValue () < 0L)
      negativo += "-";
    if (parteDecimalFormatada.trim ().length () == 0)
      return negativo + parteInteiraFormatada;
    return negativo + parteInteiraFormatada + "," + parteDecimalFormatada;
  }
  
  public int getCasasDecimais ()
  {
    return casasDecimais;
  }
  
  public void setCasasDecimais (int casasDecimais)
  {
    this.casasDecimais = casasDecimais;
  }
  
  public Long getConteudo ()
  {
    return conteudo;
  }
  
  public void setConteudo (Long pConteudo)
  {
    String antigo = asString ();
    clearRetornosValidacoes ();
    conteudo = pConteudo;
    if (! inicializouUltimoConteudoValido)
      {
	inicializouUltimoConteudoValido = true;
	setUltimoConteudoValido (asString ());
      }
    if (isVazio ())
      setUltimoConteudoValido ("");
    disparaObservadores (antigo);
  }
  
  public Long getConteudoAbsoluto ()
  {
    return new Long (Math.abs (conteudo.longValue ()));
  }
  
  public boolean isPorcentagem ()
  {
    return porcentagem;
  }
  
  public void setPorcentagem (boolean porcentagem)
  {
    this.porcentagem = porcentagem;
  }
  
  public void setConteudo (String pConteudo)
  {
    String parteDecimal = "";
    if (pConteudo.lastIndexOf (',') == -1)
      {
	for (int i = 0; i < getCasasDecimais (); i++)
	  parteDecimal += "0";
      }
    else
      {
	String parteDecimalPassada = pConteudo.substring (pConteudo.lastIndexOf (',') + 1, pConteudo.length ());
	for (int i = parteDecimalPassada.length (); i < getCasasDecimais (); i++)
	  parteDecimal += "0";
      }
    String conteudoLimpo = pConteudo.replaceAll ("[.,]", "");
    Long valConteudo = null;
    try
      {
	valConteudo = new Long (conteudoLimpo + parteDecimal);
      }
    catch (Exception e)
      {
	valConteudo = new Long (0L);
      }
    setConteudo (valConteudo);
  }
  
  public void setConteudo (Valor pConteudo)
  {
    Valor copia = new Valor (pConteudo.asString ());
    copia.converte (this);
    setConteudo (copia.getConteudo ());
  }
  
  public void clear ()
  {
    setConteudo (new Long (0L));
  }
  
  public boolean isVazio ()
  {
    return conteudo.longValue () == 0L;
  }
  
  public void converteQtdCasasDecimais (int pCasasDecimais)
  {
    if (getCasasDecimais () != pCasasDecimais)
      {
	long conteudoAtual = conteudo.longValue ();
	long fatorCasaDecimal = 1L;
	int diferenca = Math.abs (getCasasDecimais () - pCasasDecimais);
	for (int i = 0; i < diferenca; i++)
	  fatorCasaDecimal *= 10L;
	if (getCasasDecimais () < pCasasDecimais)
	  conteudoAtual *= fatorCasaDecimal;
	else
	  conteudoAtual /= fatorCasaDecimal;
	setCasasDecimais (pCasasDecimais);
	setConteudo (new Long (conteudoAtual));
      }
  }
  
  public int compareTo (Object o)
  {
    if (! (o instanceof Valor))
      return -1;
    int retorno = 0;
    Valor parametro = (Valor) o;
    int qtdCasasDecimaisParametroAnterior = parametro.getCasasDecimais ();
    int qtdCasasDecimaisAnterior = getCasasDecimais ();
    if (parametro.getCasasDecimais () > getCasasDecimais ())
      converteQtdCasasDecimais (parametro.getCasasDecimais ());
    else
      parametro.converteQtdCasasDecimais (getCasasDecimais ());
    if (conteudo.longValue () > parametro.getConteudo ().longValue ())
      retorno = 1;
    else if (conteudo.longValue () < parametro.getConteudo ().longValue ())
      retorno = -1;
    converteQtdCasasDecimais (qtdCasasDecimaisAnterior);
    parametro.converteQtdCasasDecimais (qtdCasasDecimaisParametroAnterior);
    return retorno;
  }
  
  public void arredonda (int pQtdCasas, int piso)
  {
    String parteInteira = getParteInteira ();
    String parteDecimal = "";
    if (getParteDecimal ().length () > pQtdCasas)
      {
	if (pQtdCasas > 0)
	  parteDecimal = getParteDecimal ().substring (0, pQtdCasas - 1);
	int fatorTruncamento = Integer.parseInt (getParteDecimal ().substring (pQtdCasas, pQtdCasas + 1));
	if (fatorTruncamento >= piso && fatorTruncamento <= 9)
	  {
	    Valor acrescimo = new Valor ();
	    acrescimo.setCasasDecimais (pQtdCasas);
	    acrescimo.setConteudo (new Long (1L));
	    setConteudo (soma (acrescimo));
	  }
	converteQtdCasasDecimais (pQtdCasas);
      }
  }
  
  private Valor soma (Valor parametro)
  {
    Valor retorno = new Valor ();
    int qtdCasasDecimaisParametroAnterior = parametro.getCasasDecimais ();
    int qtdCasasDecimaisAnterior = getCasasDecimais ();
    if (parametro.getCasasDecimais () > getCasasDecimais ())
      converteQtdCasasDecimais (parametro.getCasasDecimais ());
    else
      parametro.converteQtdCasasDecimais (getCasasDecimais ());
    retorno.setCasasDecimais (getCasasDecimais ());
    long resultado = getConteudo ().longValue () + parametro.getConteudo ().longValue ();
    retorno.setConteudo (new Long (resultado));
    converteQtdCasasDecimais (qtdCasasDecimaisAnterior);
    parametro.converteQtdCasasDecimais (qtdCasasDecimaisParametroAnterior);
    if (getTratamentocasasDecimais () == ARREDONDA)
      retorno.arredonda (getCasasDecimais (), getPisoArredondamento ());
    return retorno;
  }
  
  private Valor soma (Long val)
  {
    Valor param = new Valor ();
    param.converte (this);
    param.setConteudo (val);
    return soma (param);
  }
  
  private Valor soma (String val)
  {
    Valor param = new Valor ();
    param.converte (this);
    param.setConteudo (val);
    return soma (param);
  }
  
  private Valor subtrai (Valor parametro)
  {
    Valor retorno = new Valor ();
    int qtdCasasDecimaisParametroAnterior = parametro.getCasasDecimais ();
    int qtdCasasDecimaisAnterior = getCasasDecimais ();
    if (parametro.getCasasDecimais () > getCasasDecimais ())
      converteQtdCasasDecimais (parametro.getCasasDecimais ());
    else
      parametro.converteQtdCasasDecimais (getCasasDecimais ());
    retorno.setCasasDecimais (getCasasDecimais ());
    long resultado = getConteudo ().longValue () - parametro.getConteudo ().longValue ();
    retorno.setConteudo (new Long (resultado));
    converteQtdCasasDecimais (qtdCasasDecimaisAnterior);
    parametro.converteQtdCasasDecimais (qtdCasasDecimaisParametroAnterior);
    if (getTratamentocasasDecimais () == ARREDONDA)
      retorno.arredonda (getCasasDecimais (), getPisoArredondamento ());
    return retorno;
  }
  
  private Valor subtrai (Long val)
  {
    Valor param = new Valor ();
    param.converte (this);
    param.setConteudo (val);
    return subtrai (param);
  }
  
  private Valor subtrai (String val)
  {
    Valor param = new Valor ();
    param.converte (this);
    param.setConteudo (val);
    return subtrai (param);
  }
  
  private Valor multiplica (Valor parametro)
  {
    Valor retorno = new Valor ();
    long resultado = getConteudo ().longValue () * parametro.getConteudo ().longValue ();
    retorno.setCasasDecimais (getCasasDecimais () + parametro.getCasasDecimais ());
    retorno.setConteudo (new Long (resultado));
    if (getTratamentocasasDecimais () == ARREDONDA)
      retorno.arredonda (getCasasDecimais (), getPisoArredondamento ());
    else
      retorno.converteQtdCasasDecimais (getCasasDecimais ());
    return retorno;
  }
  
  private Valor multiplica (Long val)
  {
    Valor param = new Valor ();
    param.converte (this);
    param.setConteudo (val);
    return multiplica (param);
  }
  
  private Valor divide (Valor parametro)
  {
    Valor retorno = new Valor ();
    int qtdCasasDecimaisParametroAnterior = parametro.getCasasDecimais ();
    int qtdCasasDecimaisAnterior = getCasasDecimais ();
    if (parametro.getCasasDecimais () > getCasasDecimais ())
      converteQtdCasasDecimais (parametro.getCasasDecimais ());
    else
      parametro.converteQtdCasasDecimais (getCasasDecimais ());
    long resultado = 0L;
    if (parametro.getConteudo ().longValue () != 0L)
      {
	long divisor = getConteudo ().longValue ();
	long dividendo = parametro.getConteudo ().longValue ();
	if (divisor < dividendo)
	  {
	    retorno.converteQtdCasasDecimais (getCasasDecimais () + 2);
	    resultado = efetuaDivisaoComDividendoMaior (divisor, dividendo, getCasasDecimais () + 2);
	  }
	else
	  {
	    retorno.converteQtdCasasDecimais (getCasasDecimais ());
	    resultado = efetuaDivisaoComDividendoMenor (divisor, dividendo);
	  }
      }
    retorno.setConteudo (new Long (resultado));
    converteQtdCasasDecimais (qtdCasasDecimaisAnterior);
    parametro.converteQtdCasasDecimais (qtdCasasDecimaisParametroAnterior);
    if (getTratamentocasasDecimais () == ARREDONDA)
      retorno.arredonda (getCasasDecimais (), getPisoArredondamento ());
    else
      retorno.converteQtdCasasDecimais (getCasasDecimais ());
    return retorno;
  }
  
  protected long efetuaDivisaoComDividendoMenor (long divisor, long dividendo)
  {
    long fatorCasaDecimal = 1L;
    for (int i = 0; i < getCasasDecimais (); i++)
      fatorCasaDecimal *= 10L;
    divisor *= fatorCasaDecimal;
    return divisor / dividendo;
  }
  
  protected long efetuaDivisaoComDividendoMaior (long divisor, long dividendo, int qtdCasas)
  {
    long retorno = 0L;
    long novoDivisor = divisor;
    int fatorResultado = qtdCasas - 1;
    for (int i = 1; i <= qtdCasas; i++)
      {
	novoDivisor = divisor * elevaPotencia (10L, i);
	retorno = novoDivisor / dividendo * elevaPotencia (10L, fatorResultado);
	novoDivisor %= dividendo;
	fatorResultado--;
      }
    return retorno;
  }
  
  protected long elevaPotencia (long val, int potencia)
  {
    long retorno = 1L;
    for (int i = 1; i <= potencia; i++)
      retorno *= val;
    return retorno;
  }
  
  private Valor divide (Long val)
  {
    Valor param = new Valor ();
    param.converte (this);
    param.setConteudo (val);
    return divide (param);
  }
  
  private Valor resto (Valor parametro)
  {
    Valor retorno = new Valor ();
    int qtdCasasDecimaisParametroAnterior = parametro.getCasasDecimais ();
    int qtdCasasDecimaisAnterior = getCasasDecimais ();
    if (parametro.getCasasDecimais () > getCasasDecimais ())
      converteQtdCasasDecimais (parametro.getCasasDecimais ());
    else
      parametro.converteQtdCasasDecimais (getCasasDecimais ());
    retorno.setCasasDecimais (getCasasDecimais ());
    long resultado = 0L;
    if (parametro.getConteudo ().longValue () != 0L)
      {
	resultado = getConteudo ().longValue ();
	resultado %= parametro.getConteudo ().longValue ();
      }
    retorno.setConteudo (new Long (resultado));
    converteQtdCasasDecimais (qtdCasasDecimaisAnterior);
    parametro.converteQtdCasasDecimais (qtdCasasDecimaisParametroAnterior);
    if (getTratamentocasasDecimais () == ARREDONDA)
      retorno.arredonda (getCasasDecimais (), getPisoArredondamento ());
    return retorno;
  }
  
  private boolean isMaior (Valor pObj)
  {
    return compareTo (pObj) == 1;
  }
  
  private boolean isMaiorOuIgual (Valor pObj)
  {
    return compareTo (pObj) == 1 || compareTo (pObj) == 0;
  }
  
  private boolean isMenor (Valor pObj)
  {
    return compareTo (pObj) == -1;
  }
  
  public String asString ()
  {
    String negativo = "";
    if (conteudo.longValue () < 0L)
      negativo += "-";
    String parteInteira = getParteInteira ();
    String parteDecimal = getParteDecimal ();
    if (! parteDecimal.trim ().equals (""))
      parteDecimal = "," + parteDecimal;
    return negativo + parteInteira + parteDecimal;
  }
  
  public boolean equals (Object obj)
  {
    return compareTo (obj) == 0;
  }
  
  private boolean isMenorOuIgual (Valor pObj)
  {
    return compareTo (pObj) == -1 || compareTo (pObj) == 0;
  }
  
  public Valor operacao (char pOperacao, String pVal)
  {
    Valor m = new Valor (pVal);
    m.converte (this);
    return operacao (pOperacao, m);
  }
  
  public Valor operacao (char pOperacao, Valor pVal)
  {
    Valor retorno = null;
    pVal.setObservadoresAtivos (false);
    pVal.setValidadoresAtivos (false);
    switch (pOperacao)
      {
      case '+':
	retorno = soma (pVal);
	break;
      case '-':
	retorno = subtrai (pVal);
	break;
      case '*':
	retorno = multiplica (pVal);
	break;
      case '/':
	retorno = divide (pVal);
	break;
      case '%':
	retorno = resto (pVal);
	break;
      default:
	throw new IllegalArgumentException ("Sinal de Opera\u00e7\u00e3o" + pOperacao + " inv\u00e1lido!!!");
      }
    pVal.setObservadoresAtivos (true);
    pVal.setValidadoresAtivos (true);
    return retorno;
  }
  
  public boolean comparacao (String pComp, Valor pVal)
  {
    boolean retorno = false;
    setObservadoresAtivos (false);
    setValidadoresAtivos (false);
    pVal.setObservadoresAtivos (false);
    pVal.setValidadoresAtivos (false);
    if (pComp.equals (">"))
      retorno = isMaior (pVal);
    else if (pComp.equals (">="))
      retorno = isMaiorOuIgual (pVal);
    else if (pComp.equals ("<"))
      retorno = isMenor (pVal);
    else if (pComp.equals ("<="))
      retorno = isMenorOuIgual (pVal);
    else if (pComp.equals ("="))
      retorno = equals (pVal);
    else if (pComp.equals ("!=") || pComp.equals ("<>"))
      retorno = ! equals (pVal);
    setObservadoresAtivos (true);
    setValidadoresAtivos (true);
    setValidadoresAtivos (true);
    pVal.setObservadoresAtivos (true);
    pVal.setValidadoresAtivos (true);
    return retorno;
  }
  
  public boolean comparacao (String pComp, String pVal)
  {
    Valor m = new Valor (pVal);
    return comparacao (pComp, m);
  }
  
  public void append (char pOperacao, Valor pVal)
  {
    Valor operado = operacao (pOperacao, pVal);
    setConteudo (operado);
  }
  
  public void append (char pOperacao, String pVal)
  {
    setConteudo (operacao (pOperacao, pVal));
  }
  
  public String asTxt ()
  {
    String negativo = "";
    if (conteudo.longValue () < 0L)
      negativo += "-";
    return negativo + getParteInteira () + getParteDecimal ();
  }
  
  public Valor getValorAbsoluto ()
  {
    Valor result = new Valor ();
    result.setCasasDecimais (getCasasDecimais ());
    result.setConteudo (new Long (Math.abs (conteudo.longValue ())));
    return result;
  }
  
  public static void main (String[] args)
  {
    Valor m = new Valor ("23,55");
    Valor n = new Valor ("100,00");
    m.setTratamentocasasDecimais (ARREDONDA);
    System.out.println ("r->" + m.operacao ('/', n).getConteudoFormatado ());
  }
  
  public static Valor soma (Collection pCol, int pCasasDecimais)
  {
    Valor retorno = new Valor ();
    retorno.setCasasDecimais (pCasasDecimais);
    Iterator itMoedaes = pCol.iterator ();
    while (itMoedaes.hasNext ())
      {
	Valor m = (Valor) itMoedaes.next ();
	retorno.setConteudo (retorno.soma (m));
      }
    return retorno;
  }
  
  public boolean isCampoCalculado ()
  {
    return campoCalculado;
  }
  
  public void setCampoCalculado (boolean campoCalculado)
  {
    this.campoCalculado = campoCalculado;
  }
  
  public int getMaximoDigitosParteInteira ()
  {
    return maximoDigitosParteInteira;
  }
  
  public void setMaximoDigitosParteInteira (int maximoParteInteira)
  {
    maximoDigitosParteInteira = maximoParteInteira;
  }
  
  public String getMsgErroEstourodigitos ()
  {
    return msgErroEstourodigitos;
  }
  
  public void setMsgErroEstourodigitos (String msgErroEstourodigitos)
  {
    this.msgErroEstourodigitos = msgErroEstourodigitos;
  }
  
  public byte getSeveridadeValidacaoMaximoDigitos ()
  {
    return severidadeValidacaoMaximoDigitos;
  }
  
  public void setSeveridadeValidacaoMaximoDigitos (byte severidadeValidacaoMaximoDigitos)
  {
    this.severidadeValidacaoMaximoDigitos = severidadeValidacaoMaximoDigitos;
  }
  
  public int getTratamentocasasDecimais ()
  {
    return tratamentocasasDecimais;
  }
  
  public void setTratamentocasasDecimais (int tratamentocasasDecimais)
  {
    this.tratamentocasasDecimais = tratamentocasasDecimais;
  }
  
  public int getPisoArredondamento ()
  {
    return pisoArredondamento;
  }
  
  public void setPisoArredondamento (int pisoArredondamento)
  {
    this.pisoArredondamento = pisoArredondamento;
  }
}
