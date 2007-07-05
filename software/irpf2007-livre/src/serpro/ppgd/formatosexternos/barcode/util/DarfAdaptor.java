/* DarfAdaptor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.barcode.util;
import java.text.SimpleDateFormat;
import java.util.Date;

import serpro.ppgd.negocio.CNPJ;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.util.LogPPGD;

public class DarfAdaptor
{
  private String nome = "";
  private String telefone = "";
  private Data periodoApuracao = new Data (null, "");
  private String periodoApuracaoJuliana = "";
  private Informacao cpfCnpj = new NI (null, "");
  private String codigoReceita = "";
  private String numeroReferencia = "";
  private Data dataVencimento = new Data (null, "");
  private String dataVencimentoJuliana = "";
  private String valorPrincipal = "";
  private String valorMulta = "";
  private String valorJuros = "";
  private String valorTotal = "";
  private String nomePrograma = "";
  private String observacoes = "";
  private Data dataValidade = new Data (null, "");
  private String mensagem = "";
  private String municipio = "";
  private String indicadorManualCalculado = "";
  private String codigoEmpresa = "";
  private String tipoDocumento = "0";
  
  public String montaData ()
  {
    SimpleDateFormat sd = new SimpleDateFormat ("dd/MM/yyyy '\u00e0s' HH:mm");
    return sd.format (new Date ());
  }
  
  public String getCodigoReceita ()
  {
    return codigoReceita;
  }
  
  public Informacao getCpfCnpj ()
  {
    return cpfCnpj;
  }
  
  public String getCpfCnpjCalculado ()
  {
    if (getCpfCnpj ().asString ().length () == 11)
      {
	StringBuffer sb = new StringBuffer (cpfCnpj.asString ());
	sb.append ('0');
	return sb.toString ();
      }
    return cpfCnpj.asString ().substring (0, 12);
  }
  
  public Data getDataValidade ()
  {
    return dataValidade;
  }
  
  public Data getDataVencimento ()
  {
    return dataVencimento;
  }
  
  public String getIndicadorManualCalculado ()
  {
    return indicadorManualCalculado;
  }
  
  public String getMensagem ()
  {
    return mensagem;
  }
  
  public String getMunicipio ()
  {
    return municipio;
  }
  
  public String getNome ()
  {
    return nome;
  }
  
  public String getNomePrograma ()
  {
    return nomePrograma;
  }
  
  public String getNumeroReferencia ()
  {
    return numeroReferencia;
  }
  
  public String getObservacoes ()
  {
    return observacoes;
  }
  
  public Data getPeriodoApuracao ()
  {
    return periodoApuracao;
  }
  
  public String getTelefone ()
  {
    return telefone;
  }
  
  public String getValorJuros ()
  {
    return valorJuros;
  }
  
  public String getValorMulta ()
  {
    return valorMulta;
  }
  
  public String getValorPrincipal ()
  {
    return valorPrincipal;
  }
  
  public String getValorTotal ()
  {
    return valorTotal;
  }
  
  public void setCodigoReceita (String codigoReceita) throws Exception
  {
    if (CalculosGenericos.testaCampo (codigoReceita) != 4)
      throw new Exception ("Atributo \"codigoReceita\" inv\u00e1lido. Tamanho inconsistente.");
    this.codigoReceita = codigoReceita;
  }
  
  public void setCpfCnpj (CPF cpfCnpj) throws Exception
  {
    if (CalculosGenericos.testaCampo (cpfCnpj.asString ()) != 11)
      throw new Exception ("Atributo \"cpfCnpj\" inv\u00e1lido. Tamanho inconsistente.");
    setTipoDocumento ("0");
    this.cpfCnpj = cpfCnpj;
  }
  
  public void setCpfCnpj (CNPJ cpfCnpj) throws Exception
  {
    if (CalculosGenericos.testaCampo (cpfCnpj.asString ()) != 14)
      throw new Exception ("Atributo \"cpfCnpj\" inv\u00e1lido. Tamanho inconsistente.");
    setTipoDocumento ("1");
    this.cpfCnpj = cpfCnpj;
  }
  
  public void setCpfCnpj (NI cpfCnpj) throws Exception
  {
    if (CalculosGenericos.testaCampo (cpfCnpj.asString ()) != 14 && CalculosGenericos.testaCampo (cpfCnpj.asString ()) != 11)
      throw new Exception ("Atributo \"cpfCnpj\" inv\u00e1lido. Tamanho inconsistente.");
    setTipoDocumento ("1");
    this.cpfCnpj = cpfCnpj;
  }
  
  public void setDataValidade (Data dataValidade) throws Exception
  {
    if (CalculosGenericos.testaCampo (dataValidade.asString ()) != 8)
      throw new Exception ("Atributo \"dataValidade\" inv\u00e1lido. Tamanho inconsistente.");
    this.dataValidade = dataValidade;
  }
  
  public void setDataVencimento (Data dataVencimento) throws Exception
  {
    if (CalculosGenericos.testaCampo (dataVencimento.asString ()) != 8)
      throw new Exception ("Atributo \"dataVencimento\" inv\u00e1lido. Tamanho inconsistente.");
    this.dataVencimento = dataVencimento;
    setDataVencimentoJuliana ();
  }
  
  public void setIndicadorManualCalculado (String indicadorManualCalculado) throws Exception
  {
    if (CalculosGenericos.testaCampo (indicadorManualCalculado) != 1)
      throw new Exception ("Atributo \"indicadorManualCalculado\" inv\u00e1lido. Tamanho inconsistente.");
    this.indicadorManualCalculado = indicadorManualCalculado;
  }
  
  public void setMensagem (String mensagem)
  {
    this.mensagem = mensagem;
  }
  
  public void setMunicipio (String municipio)
  {
    this.municipio = municipio;
  }
  
  public void setNome (String nome)
  {
    this.nome = nome;
  }
  
  public void setNomePrograma (String nomePrograma)
  {
    this.nomePrograma = nomePrograma;
  }
  
  public void setNumeroReferencia (String numeroReferencia)
  {
    this.numeroReferencia = numeroReferencia;
  }
  
  public void setObservacoes (String observacoes)
  {
    this.observacoes = observacoes;
  }
  
  public void setPeriodoApuracao (Data periodoApuracao) throws Exception
  {
    if (CalculosGenericos.testaCampo (periodoApuracao.asString ()) != 8)
      throw new Exception ("Atributo \"periodoApuracao\" inv\u00e1lido. Tamanho inconsistente.");
    this.periodoApuracao = periodoApuracao;
    setPeriodoApuracaoJuliana ();
  }
  
  public void setTelefone (String telefone)
  {
    this.telefone = telefone;
  }
  
  public void setValorJuros (String valorJuros) throws Exception
  {
    if (valorJuros.trim ().length () == 0)
      throw new Exception ("Atributo \"valorJuros\" inv\u00e1lido. Valor n\u00e3o foi atribu\u00eddo.");
    this.valorJuros = valorJuros;
  }
  
  public void setValorMulta (String valorMulta) throws Exception
  {
    if (valorMulta.trim ().length () == 0)
      throw new Exception ("Atributo \"valorMulta\" inv\u00e1lido. Valor n\u00e3o foi atribu\u00eddo.");
    this.valorMulta = valorMulta;
  }
  
  public void setValorPrincipal (String valorPrincipal) throws Exception
  {
    if (valorPrincipal.trim ().length () == 0)
      throw new Exception ("Atributo \"valorPrincipal\" inv\u00e1lido. Valor n\u00e3o foi atribu\u00eddo.");
    this.valorPrincipal = valorPrincipal;
  }
  
  public void setValorTotal (String valorTotal) throws Exception
  {
    if (valorTotal.trim ().length () == 0)
      throw new Exception ("Atributo \"valorTotal\" inv\u00e1lido. Valor n\u00e3o foi atribu\u00eddo.");
    this.valorTotal = valorTotal;
  }
  
  public String getCodigoEmpresa ()
  {
    return codigoEmpresa;
  }
  
  public void setCodigoEmpresa (String codigoEmpresa)
  {
    this.codigoEmpresa = codigoEmpresa;
  }
  
  public String getDataVencimentoJuliana ()
  {
    return dataVencimentoJuliana;
  }
  
  private void setDataVencimentoJuliana ()
  {
    try
      {
	JulianDate data = new JulianDate (dataVencimento.asString ());
	dataVencimentoJuliana = data.getJulianDate4Digits ();
      }
    catch (Exception e)
      {
	LogPPGD.erro (e.getMessage ());
      }
  }
  
  public String getPeriodoApuracaoJuliana ()
  {
    return periodoApuracaoJuliana;
  }
  
  private void setPeriodoApuracaoJuliana ()
  {
    try
      {
	JulianDate data = new JulianDate (periodoApuracao.asString ());
	periodoApuracaoJuliana = data.getJulianDate4Digits ();
      }
    catch (Exception e)
      {
	LogPPGD.erro (e.getMessage ());
      }
  }
  
  public String getTipoDocumento ()
  {
    return tipoDocumento;
  }
  
  private void setTipoDocumento (String tipoDocumento)
  {
    this.tipoDocumento = tipoDocumento;
  }
}
