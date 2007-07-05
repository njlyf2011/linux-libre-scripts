/* CampoTXT - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.Valor;

public class CampoTXT
{
  private String fNome;
  private String fTipo;
  private String fConteudo;
  private int fTamanho;
  private int fDecimais;
  private boolean fAutomatico;
  private String atributoObjetoNegocio = null;
  
  public CampoTXT (String nome, String tipoTamanho) throws GeracaoTxtException
  {
    int decimais = 0;
    String tipos = "CcAaNnIiDd";
    fNome = nome;
    fTamanho = 0;
    if (tipos.indexOf (tipoTamanho.charAt (0)) != -1)
      {
	/* empty */
      }
    fTipo = tipoTamanho.substring (0, 1);
    if (fTipo.equals ("I"))
      fTamanho = 1;
    else if (fTipo.equals ("C") || fTipo.equals ("A"))
      fTamanho = Integer.parseInt (tipoTamanho.substring (1, tipoTamanho.length ()));
    else if (fTipo.equals ("N"))
      {
	int tamanho = tipoTamanho.indexOf (",");
	if (tamanho < 0)
	  tamanho = tipoTamanho.length ();
	else
	  decimais = Integer.parseInt (tipoTamanho.substring (tamanho + 1, tipoTamanho.length ()));
	String aux = tipoTamanho.substring (1, tamanho);
	fTamanho = Integer.parseInt (aux);
	if (decimais > fTamanho)
	  throw new GeracaoTxtException ("Erro de layout - Campo " + fNome + " com n\u00famero de decimais maior que tamanho.");
	fDecimais = decimais;
      }
    else
      fTamanho = 8;
  }
  
  public CampoTXT (String nome, String tipoTamanho, boolean informacaoAutomatica) throws GeracaoTxtException
  {
    this (nome, tipoTamanho);
    fAutomatico = informacaoAutomatica;
  }
  
  private boolean getBoolean ()
  {
    if ("SsNn10".indexOf (fConteudo) != -1)
      {
	/* empty */
      }
    return fConteudo.equals ("S") || fConteudo.equals ("1");
  }
  
  private int getInteger ()
  {
    int aux = 0;
    if (fTipo.equals ("N"))
      aux = Integer.parseInt (fConteudo);
    else if (fTipo.equals ("C"))
      {
	if (fConteudo.trim ().length () != 0)
	  {
	    /* empty */
	  }
	aux = Integer.parseInt (fConteudo);
      }
    else if (fTipo.equals ("D"))
      {
	if (fConteudo.trim ().length () != 0)
	  {
	    /* empty */
	  }
	aux = Integer.parseInt (fConteudo);
      }
    else if (fTipo.equals ("I"))
      {
	if (fConteudo == "S")
	  aux = 1;
	else
	  aux = 0;
      }
    return aux;
  }
  
  private String getString ()
  {
    if (fConteudo == null)
      return "";
    return fConteudo;
  }
  
  private void setBoolean (boolean value)
  {
    if (fTipo.equals ("I") || fTipo.equals ("C"))
      {
	if (value)
	  fConteudo = "S";
	else
	  fConteudo = "N";
      }
    else if (value)
      fConteudo = "1";
    else
      fConteudo = "0";
  }
  
  private void setInteger (int value) throws GeracaoTxtException
  {
    if (fTipo.equals ("I"))
      {
	if (value == 1)
	  fConteudo = "S";
	else if (value == 0)
	  fConteudo = "N";
	else
	  throw new GeracaoTxtException ("Erro atribuindo conte\u00fado a campo do tipo Indicador " + fNome + ".");
      }
    String aux = String.valueOf (value);
    if (aux.length () > fTamanho)
      throw new GeracaoTxtException ("Erro atribuindo conte\u00fado a campo do tipo Indicador " + fNome + ".");
    fConteudo = aux;
  }
  
  protected void setString (String value) throws GeracaoTxtException
  {
    String aux = value.trim ();
    if (fTipo.equals ("D"))
      {
	for (/**/; aux.indexOf ("/") > 0; aux = aux.substring (0, aux.indexOf ("/")) + aux.substring (aux.indexOf ("/") + 1))
	  {
	    /* empty */
	  }
	if (aux.length () > fTamanho)
	  throw new GeracaoTxtException ("Erro atribuindo conte\u00fado maior que o tamanho do campo " + fNome + ".");
	fConteudo = aux;
      }
    else
      {
	if (aux.length () > fTamanho)
	  throw new GeracaoTxtException ("Erro atribuindo conte\u00fado maior que o tamanho do campo " + fNome + ".");
	if (fTipo.equals ("C") || fTipo.equals ("A"))
	  fConteudo = aux;
	else if (fTipo.equals ("N"))
	  {
	    if (aux.length () == 0)
	      {
		fConteudo = "0";
		aux = "0";
	      }
	    if (fDecimais == 0)
	      {
		try
		  {
		    Long.parseLong (aux);
		    fConteudo = aux;
		  }
		catch (Exception e)
		  {
		    throw new GeracaoTxtException ("Erro atribuindo conte\u00fado inv\u00e1lido ao campo " + fNome + ".");
		  }
	      }
	    else
	      {
		if (fDecimais > 0 & aux.indexOf (".") > 0 && fDecimais < aux.substring (aux.indexOf (".") + 1).length ())
		  throw new GeracaoTxtException ("Quantidade de decimais maior que o permitido no campo " + fNome + ".");
		Double.parseDouble (aux);
		fConteudo = aux;
	      }
	  }
	else if (fTipo.equals ("I"))
	  {
	    if (aux.length () > 1)
	      aux = aux.substring (0, 0);
	    if (aux.equalsIgnoreCase ("S") || aux.equalsIgnoreCase ("N"))
	      fConteudo = aux.toUpperCase ();
	    else if (aux.length () == 0)
	      fConteudo = " ";
	  }
	else
	  throw new GeracaoTxtException ("<html>Quantidade de decimais maior que o<br>definido para o campo " + fNome + ".</html>");
      }
  }
  
  private String getTxt () throws GeracaoTxtException
  {
    String texto = getString ().trim ();
    if (fTipo.equals ("C") | fTipo.equals ("I") | fTipo.equals ("D") | fTipo.equals ("A"))
      {
	if (texto.length () > fTamanho)
	  texto = texto.substring (1, fTamanho);
	for (/**/; texto.length () < fTamanho; texto = texto.concat (" "))
	  {
	    /* empty */
	  }
      }
    else if (fTipo.equals ("N"))
      {
	texto.replaceAll ("[,.]", "");
	int aux = texto.length ();
	if (aux > fTamanho + fDecimais)
	  throw new GeracaoTxtException ("Erro atribuindo conte\u00fado (" + texto + ") maior que o tamanho do campo " + fNome + ".");
	for (/**/; texto.length () < fTamanho; texto = "0".concat (texto))
	  {
	    /* empty */
	  }
	if (texto.indexOf ("-") >= 0)
	  {
	    texto = texto.replaceAll ("-", "");
	    texto = "-".concat (texto);
	  }
      }
    return texto;
  }
  
  private void setLogico (Logico value)
  {
    fConteudo = value.asString ();
  }
  
  public boolean estaPreenchido () throws NumberFormatException, GeracaoTxtException
  {
    if (fAutomatico)
      return false;
    if (getString ().length () == 0)
      return false;
    if (fTipo.equals ("N") && Double.parseDouble (getTxt ()) == 0.0)
      return false;
    return true;
  }
  
  public String getNome ()
  {
    return fNome;
  }
  
  public String getTipo ()
  {
    return fTipo;
  }
  
  public int getTamanho ()
  {
    return fTamanho;
  }
  
  public int getDecimais ()
  {
    return fDecimais;
  }
  
  public String asString ()
  {
    return getString ();
  }
  
  public boolean asBoolean ()
  {
    return getBoolean ();
  }
  
  public int asInteger ()
  {
    return getInteger ();
  }
  
  public Valor asValor ()
  {
    Valor val = new Valor ();
    val.setCasasDecimais (getDecimais ());
    val.setConteudo (new Long (asString ()));
    return val;
  }
  
  public String asTxt () throws GeracaoTxtException
  {
    return getTxt ();
  }
  
  public void set (String valor) throws GeracaoTxtException
  {
    setString (valor);
  }
  
  public void setLimitado (String valor) throws GeracaoTxtException
  {
    if (valor.length () > getTamanho ())
      valor = valor.substring (0, getTamanho ());
    setString (valor);
  }
  
  public void set (boolean valor)
  {
    setBoolean (valor);
  }
  
  public void set (int valor) throws GeracaoTxtException
  {
    setInteger (valor);
  }
  
  public void set (Valor pValor) throws GeracaoTxtException
  {
    Valor valArmazenado = pValor;
    if (pValor.getCasasDecimais () != getDecimais ())
      {
	valArmazenado = new Valor ();
	valArmazenado.setConteudo (pValor);
	valArmazenado.converteQtdCasasDecimais (getDecimais ());
      }
    String valTxt = valArmazenado.asTxt ();
    String acrescimo = "";
    int totalAcrescimos = getTamanho ();
    boolean ehNegativo = pValor.getConteudo ().longValue () < 0L;
    if (ehNegativo)
      {
	totalAcrescimos--;
	valTxt = valTxt.replaceAll ("-", "");
      }
    if (valTxt.length () < totalAcrescimos)
      {
	for (int i = 0; i < totalAcrescimos - valTxt.length (); i++)
	  acrescimo += "0";
      }
    if (ehNegativo)
      acrescimo = "-" + acrescimo;
    set (acrescimo + valTxt);
  }
  
  public void set (Logico valor)
  {
    setLogico (valor);
  }
  
  public String getAtributoObjetoNegocio ()
  {
    if (atributoObjetoNegocio == null)
      atributoObjetoNegocio = "";
    return atributoObjetoNegocio;
  }
  
  public void setAtributoObjetoNegocio (String pAcessoObjetoNegocio)
  {
    atributoObjetoNegocio = pAcessoObjetoNegocio;
  }
  
  public static void main (String[] args) throws GeracaoTxtException
  {
    /* empty */
  }
}
