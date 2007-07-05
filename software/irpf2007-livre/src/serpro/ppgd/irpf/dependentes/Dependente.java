/* Dependente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.dependentes;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.gui.xbeans.JEditObjetoNegocioItemIf;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCPF;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorData;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNome;

public class Dependente extends ObjetoNegocio implements JEditObjetoNegocioItemIf
{
  public static final String PROP_CAMPO_NOME = "Nome";
  public static final String PROP_CAMPO_CPF = "CPF";
  private Codigo codigo = new Codigo (this, "C\u00f3digo", CadastroTabelasIRPF.recuperarDependencias ());
  private Alfa nome = new Alfa (this, "Nome", 60);
  private CPF cpfDependente = new CPF (this, "CPF");
  private Data dataNascimento = new Data (this, "Data de Nascimento");
  private transient Contribuinte contribuinte;
  private transient IdentificadorDeclaracao identificadorDeclaracao = null;
  private String chave = "";
  
  public Dependente ()
  {
    setFicha ("Dependentes");
    getCodigo ().setColunaFiltro (1);
    getCodigo ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("dependente_codigo")));
    getNome ().addValidador (new ValidadorNome ((byte) 3));
    getNome ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("dependente_nome"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if ((! getCodigo ().isVazio () || ! getDataNascimento ().isVazio ()) && getNome ().isVazio ())
	  return super.validarImplementado ();
	return null;
      }
    });
    String ANO_01_01_1984 = "01/01/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 22);
    final Data data22Anos = new Data ();
    data22Anos.setConteudo (ANO_01_01_1984);
    String ANO_01_01_1986 = "01/01/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 20);
    final Data data20Anos = new Data ();
    data20Anos.setConteudo (ANO_01_01_1986);
    String ANO_01_01_1981 = "01/01/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 25);
    final Data data25Anos = new Data ();
    data25Anos.setConteudo (ANO_01_01_1981);
    String ANO_31_12_1985 = "31/12/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 21);
    final Data data21Anos = new Data ();
    data21Anos.setConteudo (ANO_31_12_1985);
    getCpfDependente ().addValidador (new ValidadorCPF ((byte) 3));
    getCpfDependente ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if ((getCodigo ().asString ().equals ("11") || getCodigo ().asString ().equals ("21") || getCodigo ().asString ().equals ("24") || getCodigo ().asString ().equals ("41") || getCodigo ().asString ().equals ("31")) && getCpfDependente ().isVazio () && ! getDataNascimento ().isVazio () && getDataNascimento ().maisAntiga (data20Anos))
	  return new RetornoValidacao (tab.msg ("dependente_cpf_brancoinvalido"), (byte) 3);
	return null;
      }
    });
    getCpfDependente ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if ((getCodigo ().asString ().equals ("22") || getCodigo ().asString ().equals ("25")) && getCpfDependente ().isVazio ())
	  return new RetornoValidacao (tab.msg ("dependente_cpf_brancoinvalido"), (byte) 3);
	return null;
      }
    });
    getCpfDependente ().addValidador (new ValidadorDefault ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCpfDependente ().asString ().equals (identificadorDeclaracao.getCpf ().asString ()))
	  return new RetornoValidacao (tab.msg ("add_cpf_dep"), (byte) 3);
	return null;
      }
    });
    getDataNascimento ().addValidador (new ValidadorData ((byte) 3));
    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("dependente_data"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if ((! getCodigo ().isVazio () || ! getNome ().isVazio () || ! getCpfDependente ().isVazio ()) && getDataNascimento ().isVazio ())
	  return super.validarImplementado ();
	return null;
      }
    });
    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if ((getCodigo ().asString ().equals ("22") || getCodigo ().asString ().equals ("25")) && ! getDataNascimento ().isVazio () && (getDataNascimento ().maisNova (data21Anos) || getDataNascimento ().maisAntiga (data25Anos)))
	  return new RetornoValidacao (tab.msg ("dependente_data_incompativel"), (byte) 3);
	return null;
      }
    });
    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if ((getCodigo ().asString ().equals ("21") || getCodigo ().asString ().equals ("24") || getCodigo ().asString ().equals ("41")) && ! getDataNascimento ().isVazio () && getDataNascimento ().maisAntiga (data22Anos))
	  return new RetornoValidacao (tab.msg ("dependente_data_incompativel"), (byte) 3);
	return null;
      }
    });
    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 2)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (getCodigo ().asString ().equals ("31"))
	  {
	    Data dtNascContribuinte = getContribuinte ().getDataNascimento ();
	    if (! dtNascContribuinte.isVazio ())
	      {
		int diaMesLength = "**/**/".length ();
		String dezAnosAntes = "" + (Integer.parseInt (dtNascContribuinte.asString ().substring (diaMesLength)) - 10);
		Data dtDezAnosAntes = new Data ();
		dtDezAnosAntes.setConteudo (dtNascContribuinte.asString ().substring (0, diaMesLength) + "/" + dezAnosAntes);
		if (! getDataNascimento ().maisAntiga (dtDezAnosAntes) && ! getDataNascimento ().igual (dtDezAnosAntes))
		  return new RetornoValidacao (tab.msg ("dependente_data_incompativel"), (byte) 2);
	      }
	  }
	return null;
      }
    });
  }
  
  public String toString ()
  {
    return nome.asString ();
  }
  
  public Codigo getCodigo ()
  {
    return codigo;
  }
  
  public CPF getCpfDependente ()
  {
    return cpfDependente;
  }
  
  public Data getDataNascimento ()
  {
    return dataNascimento;
  }
  
  public Alfa getNome ()
  {
    return nome;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List lista = super.recuperarListaCamposPendencia ();
    lista.add (getCodigo ());
    lista.add (getCpfDependente ());
    lista.add (getDataNascimento ());
    lista.add (getNome ());
    return lista;
  }
  
  public String getConteudo (int i)
  {
    return getNome ().getConteudoFormatado ();
  }
  
  public int getTotalAtributos ()
  {
    return 1;
  }
  
  public int getColunaFiltro ()
  {
    return 0;
  }
  
  public String getChave ()
  {
    return chave;
  }
  
  public void setChave (String chave)
  {
    this.chave = chave;
  }
  
  public IdentificadorDeclaracao getIdentificadorDeclaracao ()
  {
    return identificadorDeclaracao;
  }
  
  public void setIdentificadorDeclaracao (IdentificadorDeclaracao identificadorDeclaracao)
  {
    this.identificadorDeclaracao = identificadorDeclaracao;
  }
  
  public boolean isVazio ()
  {
    Iterator iterator = recuperarCamposInformacao ().iterator ();
    while (iterator.hasNext ())
      {
	Informacao informacao = (Informacao) iterator.next ();
	if (! informacao.isVazio ())
	  return false;
      }
    return true;
  }
  
  public void setContribuinte (Contribuinte contribuinte)
  {
    this.contribuinte = contribuinte;
  }
  
  public Contribuinte getContribuinte ()
  {
    return contribuinte;
  }
  
  public void addObservador (Observador obsTotalizaDep)
  {
    getCodigo ().addObservador (obsTotalizaDep);
    getNome ().addObservador (obsTotalizaDep);
  }
  
  public void removeObservador (Observador obsTotalizaDep)
  {
    getCodigo ().removeObservador (obsTotalizaDep);
    getNome ().removeObservador (obsTotalizaDep);
  }
}
