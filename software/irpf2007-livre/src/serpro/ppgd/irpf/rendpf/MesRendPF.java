/* MesRendPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpf;
import java.util.List;

import serpro.ppgd.irpf.ValidadorNaoNegativo;
import serpro.ppgd.negocio.Inteiro;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class MesRendPF extends ObjetoNegocio
{
  public static final String VALOR_DEPENDENTE_CL = "126,36";
  public static final String VALOR_DEPENDENTE_CL_JANEIRO = "117,00";
  public static final String NOME_PESSOA_FISICA = "Pessoa F\u00edsica";
  public static final String NOME_EXTERIOR = "Exterior";
  public static final String NOME_PREV_OFICIAL = "Previd\u00eancia oficial";
  public static final String NOME_DEPENDENTES = "Dependentes";
  public static final String NOME_PENSAOALIM = "Pens\u00e3o Aliment\u00edcia";
  public static final String NOME_LIVRO_CAIXA = "Livro Caixa";
  public static final String NOME_CARNE_LEAO = "Carn\u00ea-Le\u00e3o";
  public static final String NOME_DARF_PAGO = "Darf Pago c\u00f3d. 0190";
  private Inteiro mes = new Inteiro (this, "");
  private Valor pessoaFisica = new Valor (this, "Pessoa F\u00edsica");
  private Valor exterior = new Valor (this, "Exterior");
  private Valor previdencia = new Valor (this, "Previd\u00eancia oficial");
  private Valor dependentes = new Valor (this, "Dependentes");
  private Valor pensao = new Valor (this, "Pens\u00e3o Aliment\u00edcia");
  private Valor livroCaixa = new Valor (this, "Livro Caixa");
  private Valor carneLeao = new Valor (this, "Carn\u00ea-Le\u00e3o");
  private Valor darfPago = new Valor (this, "Darf Pago c\u00f3d. 0190");
  
  public MesRendPF ()
  {
    pessoaFisica.addValidador (new ValidadorNaoNegativo ((byte) 3));
    exterior.addValidador (new ValidadorNaoNegativo ((byte) 3));
    previdencia.addValidador (new ValidadorNaoNegativo ((byte) 3));
    dependentes.addValidador (new ValidadorNaoNegativo ((byte) 3));
    pensao.addValidador (new ValidadorNaoNegativo ((byte) 3));
    livroCaixa.addValidador (new ValidadorNaoNegativo ((byte) 3));
    carneLeao.addValidador (new ValidadorNaoNegativo ((byte) 3));
    darfPago.addValidador (new ValidadorNaoNegativo ((byte) 3));
  }
  
  public void addObservador (Observador obs)
  {
    pessoaFisica.addObservador (obs);
    exterior.addObservador (obs);
    previdencia.addObservador (obs);
    dependentes.addObservador (obs);
    pensao.addObservador (obs);
    livroCaixa.addObservador (obs);
    carneLeao.addObservador (obs);
    darfPago.addObservador (obs);
  }
  
  public Valor getCarneLeao ()
  {
    return carneLeao;
  }
  
  public Valor getDependentes ()
  {
    return dependentes;
  }
  
  public Valor getExterior ()
  {
    return exterior;
  }
  
  public Valor getLivroCaixa ()
  {
    return livroCaixa;
  }
  
  public Inteiro getMes ()
  {
    return mes;
  }
  
  public Valor getPensao ()
  {
    return pensao;
  }
  
  public Valor getPessoaFisica ()
  {
    return pessoaFisica;
  }
  
  public Valor getPrevidencia ()
  {
    return previdencia;
  }
  
  public Valor getDarfPago ()
  {
    return darfPago;
  }
  
  public boolean isVazio ()
  {
    if (pessoaFisica.isVazio () && exterior.isVazio () && previdencia.isVazio () && dependentes.isVazio () && pensao.isVazio () && livroCaixa.isVazio () && carneLeao.isVazio () && darfPago.isVazio ())
      return true;
    return false;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = super.recuperarListaCamposPendencia ();
    retorno.add (getDependentes ());
    retorno.add (getPessoaFisica ());
    retorno.add (getDarfPago ());
    retorno.add (getExterior ());
    retorno.add (getLivroCaixa ());
    retorno.add (getPensao ());
    retorno.add (getPrevidencia ());
    return retorno;
  }
}
