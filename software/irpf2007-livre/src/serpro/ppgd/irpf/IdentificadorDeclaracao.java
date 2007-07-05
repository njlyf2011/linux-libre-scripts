/* IdentificadorDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.io.File;
import java.util.StringTokenizer;

import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Logico;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.util.Validador;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCPF;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNome;

public class IdentificadorDeclaracao extends ObjetoNegocio
{
  public static final String DEC_COMPLETA = "0";
  public static final String DEC_SIMPLIFICADA = "1";
  private CPF cpf = new CPF (this, "CPF");
  private Alfa nome = new Alfa (this, "Nome", 60);
  private Alfa exercicio = new Alfa (this, "Exerc\u00edcio", 4);
  private Logico transmitida = new Logico (this, "");
  private Alfa tipoDeclaracao = new Alfa (this, "Tipo da Declara\u00e7\u00e3o", 5);
  private Alfa declaracaoRetificadora = new Alfa (this, "\u00c9 Retificadora?", 2);
  private Alfa numReciboDecRetif = new Alfa (this, "N\u00ba do Recibo Dec. Anterior", 12);
  private Alfa numeroReciboDecAnterior = new Alfa (this, "N\u00ba do Recibo Dec. Exerc\u00edcio Anterior", 12);
  private Alfa enderecoDiferente = new Alfa (this, "Endere\u00e7o Novo?", 2);
  
  public IdentificadorDeclaracao ()
  {
    transmitida.converteEmTipoSimNao (Logico.NAO);
    cpf.addValidador (new ValidadorCPF ((byte) 3));
    nome.addValidador (new ValidadorNaoNulo ((byte) 3));
    nome.addValidador (new ValidadorNome ((byte) 3));
    nome.addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	StringTokenizer str = new StringTokenizer (getNome ().asString (), " ");
	StringBuffer strBuff = new StringBuffer ();
	while (str.hasMoreTokens ())
	  strBuff.append (str.nextToken () + " ");
	getNome ().setConteudo (strBuff.toString ().trim ());
      }
    });
    ValidadorNaoNulo validadorNaoNuloRetif = new ValidadorNaoNulo ((byte) 3);
    validadorNaoNuloRetif.setMensagemValidacao (tab.msg ("dec_retificadora"));
    declaracaoRetificadora.addValidador (validadorNaoNuloRetif);
    ValidadorNaoNulo validadorNaoNuloNumRecibo = new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (declaracaoRetificadora.asString ().equals (Logico.NAO) || declaracaoRetificadora.isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    };
    validadorNaoNuloNumRecibo.setMensagemValidacao (tab.msg ("num_recibo_dec_retif"));
    numReciboDecRetif.addValidador (validadorNaoNuloNumRecibo);
    numReciboDecRetif.addValidador (new ValidadorDefault ((byte) 2)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (declaracaoRetificadora.asString ().equals (Logico.NAO))
	  return null;
	if (Validador.validarNrRecibo (getInformacao ().asString ()))
	  return null;
	return new RetornoValidacao (tab.msg ("num_recibo_invalido"), getSeveridade ());
      }
    });
    tipoDeclaracao.setConteudo ("0");
    ValidadorNaoNulo validadorNaoNuloEnderDif = new ValidadorNaoNulo ((byte) 3);
    validadorNaoNuloEnderDif.setMensagemValidacao (tab.msg ("endereco_diferente"));
    getEnderecoDiferente ().addValidador (validadorNaoNuloEnderDif);
    getExercicio ().setConteudo (ConstantesGlobais.EXERCICIO);
  }
  
  public boolean isDeclaracaoGerada ()
  {
    return true;
  }
  
  public String getPathArquivo ()
  {
    String diretorioDadosApp = IRPFUtil.DIR_DADOS;
    String path = diretorioDadosApp + "/" + cpf.asString ();
    File flDados = new File (path);
    if (! flDados.exists ())
      flDados.mkdirs ();
    StringBuffer nomeArquivoDec = new StringBuffer ();
    nomeArquivoDec.append (path);
    nomeArquivoDec.append ("/" + cpf.asString ());
    nomeArquivoDec.append (".xml");
    return nomeArquivoDec.toString ();
  }
  
  public CPF getCpf ()
  {
    return cpf;
  }
  
  public Alfa getExercicio ()
  {
    return exercicio;
  }
  
  public Alfa getNome ()
  {
    return nome;
  }
  
  public Alfa getDeclaracaoRetificadora ()
  {
    return declaracaoRetificadora;
  }
  
  public Alfa getNumReciboDecRetif ()
  {
    return numReciboDecRetif;
  }
  
  public Logico getTransmitida ()
  {
    return transmitida;
  }
  
  public boolean equals (Object obj)
  {
    if (obj instanceof IdentificadorDeclaracao && ((IdentificadorDeclaracao) obj).getCpf ().asString ().equals (getCpf ().asString ()))
      return true;
    return false;
  }
  
  public Alfa getTipoDeclaracao ()
  {
    return tipoDeclaracao;
  }
  
  public boolean isCompleta ()
  {
    return getTipoDeclaracao ().asString ().equals ("0");
  }
  
  public boolean isRetificadora ()
  {
    return getDeclaracaoRetificadora ().asString ().equals (Logico.SIM);
  }
  
  public Alfa getEnderecoDiferente ()
  {
    return enderecoDiferente;
  }
  
  public void setNumeroReciboDecAnterior (Alfa numeroReciboDecAnterior)
  {
    this.numeroReciboDecAnterior = numeroReciboDecAnterior;
  }
  
  public Alfa getNumeroReciboDecAnterior ()
  {
    return numeroReciboDecAnterior;
  }
}
