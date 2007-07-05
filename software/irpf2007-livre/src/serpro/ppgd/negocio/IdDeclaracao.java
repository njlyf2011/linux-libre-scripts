/* IdDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public abstract class IdDeclaracao extends ObjetoNegocio
{
  public static final String ID_DECLARACAO_RETIFICADORA = "Declara\u00e7\u00e3o Retificadora?";
  public static final String ID_DECLARACAO_NR_RECIBO = "N\u00famero do Recibo da Declara\u00e7\u00e3o";
  public static final String ID_DECLARACAO_NR_RECIBO_DECLARACAO_ANTERIOR = "N\u00famero do Recibo da Declara\u00e7\u00e3o Anterior";
  private Alfa tipo;
  private Alfa exercicio;
  private Inteiro ordem;
  private Logico gerada;
  private Logico retificadora;
  private Alfa numeroRecibo;
  private Alfa numeroReciboDeclaracaoAnterior;
  private IdUsuario id;
  
  public abstract String getPathArquivo (String string);
  
  public boolean existeArquivoDeclaracao (String dirDados)
  {
    File fl = new File (getPathArquivo (dirDados));
    return fl.exists ();
  }
  
  protected abstract List getListaAtributosPKIdDec ();
  
  public List getListaAtributosPK ()
  {
    List retorno = getId ().getListaAtributosPK ();
    List attIdDec = getListaAtributosPKIdDec ();
    if (attIdDec != null)
      retorno.addAll (attIdDec);
    return retorno;
  }
  
  private IdDeclaracao ()
  {
    tipo = new Alfa ();
    exercicio = new Alfa ();
    ordem = new Inteiro ();
    gerada = new Logico ();
    retificadora = new Logico (null, "Declara\u00e7\u00e3o Retificadora?");
    numeroRecibo = new Alfa ("N\u00famero do Recibo da Declara\u00e7\u00e3o");
    numeroReciboDeclaracaoAnterior = new Alfa ("N\u00famero do Recibo da Declara\u00e7\u00e3o Anterior");
    id = null;
  }
  
  public IdDeclaracao (IdUsuario pId)
  {
    tipo = new Alfa ();
    exercicio = new Alfa ();
    ordem = new Inteiro ();
    gerada = new Logico ();
    retificadora = new Logico (null, "Declara\u00e7\u00e3o Retificadora?");
    numeroRecibo = new Alfa ("N\u00famero do Recibo da Declara\u00e7\u00e3o");
    numeroReciboDeclaracaoAnterior = new Alfa ("N\u00famero do Recibo da Declara\u00e7\u00e3o Anterior");
    id = null;
    id = pId;
  }
  
  public Logico getGerada ()
  {
    return gerada;
  }
  
  public void setGerada (boolean gerada)
  {
    if (gerada)
      this.gerada.setConteudo (Logico.SIM);
    else
      this.gerada.setConteudo (Logico.NAO);
  }
  
  public boolean isGerada ()
  {
    return gerada.getConteudoFormatado ().equals (Logico.SIM);
  }
  
  public Logico getRetificadora ()
  {
    return retificadora;
  }
  
  public void setRetificadora (boolean retificadora)
  {
    if (retificadora)
      gerada.setConteudo (Logico.SIM);
    else
      gerada.setConteudo (Logico.NAO);
  }
  
  public boolean isRetificadora ()
  {
    return retificadora.getConteudoFormatado ().equals (Logico.SIM);
  }
  
  public Alfa getNumeroRecibo ()
  {
    return numeroRecibo;
  }
  
  public Alfa getNumeroReciboDeclaracaoAnterior ()
  {
    return numeroReciboDeclaracaoAnterior;
  }
  
  public boolean isTransmitida ()
  {
    return ! numeroRecibo.isVazio ();
  }
  
  public Alfa getTipo ()
  {
    return tipo;
  }
  
  public Alfa getExercicio ()
  {
    return exercicio;
  }
  
  public Inteiro getOrdem ()
  {
    return ordem;
  }
  
  public Nome getNome ()
  {
    return id.getNome ();
  }
  
  public NI getNiContribuinte ()
  {
    return id.getNiContribuinte ();
  }
  
  public IdUsuario getId ()
  {
    return id;
  }
  
  public void setId (IdUsuario idContribuinte)
  {
    id = idContribuinte;
  }
  
  public boolean equals (Object idDeclaracao)
  {
    Iterator itAtributos = getListaAtributosPK ().iterator ();
    Iterator itParametro = ((IdDeclaracao) idDeclaracao).getListaAtributosPK ().iterator ();
    while (itAtributos.hasNext ())
      {
	Informacao atributoAtual = (Informacao) itAtributos.next ();
	Informacao atributoParametroAtual = (Informacao) itParametro.next ();
	if (! atributoAtual.asString ().equals (atributoParametroAtual.asString ()))
	  return false;
      }
    return true;
  }
}
