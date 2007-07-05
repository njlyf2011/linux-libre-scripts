/* Inteiro - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import serpro.ppgd.negocio.util.LogPPGD;

public class Inteiro extends Informacao
{
  public static final String PROP_LIMITE_MIN_MAX = "LIMITES";
  private int conteudoInteiro = 0;
  private int limiteMinimo = -1;
  private int limiteMaximo = -1;
  
  public Inteiro ()
  {
    this (null, "");
  }
  
  public Inteiro (String nomeCampo)
  {
    this (null, nomeCampo);
  }
  
  public Inteiro (ObjetoNegocio owner, String nomeCampo)
  {
    super (owner, nomeCampo);
  }
  
  public int getLimiteMaximo ()
  {
    return limiteMaximo;
  }
  
  public void setLimiteMaximo (int limiteMaximo)
  {
    this.limiteMaximo = limiteMaximo;
    getObservadores ().firePropertyChange ("LIMITES", false, true);
  }
  
  public int getLimiteMinimo ()
  {
    return limiteMinimo;
  }
  
  public void setLimiteMinimo (int limiteMinimo)
  {
    this.limiteMinimo = limiteMinimo;
    getObservadores ().firePropertyChange ("LIMITES", false, true);
  }
  
  public void setConteudo (int pConteudo)
  {
    LogPPGD.debug ("Atribui\u00e7\u00e3o: " + getNomeCampo () + " = " + String.valueOf (pConteudo));
    String antigo = asString ();
    clearRetornosValidacoes ();
    conteudoInteiro = pConteudo;
    disparaObservadores (antigo);
  }
  
  public void setConteudo (String conteudo)
  {
    if (conteudo.trim ().equals (""))
      setConteudo (0);
    else
      {
	int val = 0;
	try
	  {
	    val = Integer.parseInt (conteudo);
	  }
	catch (Exception e)
	  {
	    val = 0;
	  }
	setConteudo (val);
      }
  }
  
  public String getConteudoFormatado ()
  {
    return asString ();
  }
  
  public boolean isVazio ()
  {
    return asInteger () == 0;
  }
  
  public int compareTo (Object o)
  {
    Inteiro outro = (Inteiro) o;
    if (conteudoInteiro < outro.asInteger ())
      return -1;
    if (conteudoInteiro == outro.asInteger ())
      return 0;
    return 1;
  }
  
  public String asString ()
  {
    return String.valueOf (conteudoInteiro);
  }
  
  public int asInteger ()
  {
    return conteudoInteiro;
  }
  
  public static void main (String[] argumentos)
  {
    System.out.println (new Inteiro ().conteudoInteiro);
  }
  
  public void clear ()
  {
    setConteudo (0);
  }
  
  public int getConteudoInteiro ()
  {
    return conteudoInteiro;
  }
  
  public void setConteudoInteiro (int conteudoInteiro)
  {
    this.conteudoInteiro = conteudoInteiro;
  }
}
