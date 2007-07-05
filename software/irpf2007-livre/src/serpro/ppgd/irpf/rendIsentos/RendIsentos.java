/* RendIsentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendIsentos;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.irpf.ColecaoItemQuadroAuxiliar;
import serpro.ppgd.irpf.ColecaoItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.ItemQuadroLucrosDividendos;
import serpro.ppgd.irpf.calculos.CalculosRendIsentos;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.ValidadorDefault;
import serpro.ppgd.negocio.ValidadorImpeditivoDefault;
import serpro.ppgd.negocio.Valor;

public class RendIsentos extends ObjetoNegocio
{
  public static final String LIMITE_ISENCAO_APOSENTADORIA = "16.249,44";
  private Valor bolsaEstudos = new Valor ();
  private Valor capitalApolices = new Valor ();
  private Valor indenizacoes = new Valor ();
  private Valor lucroAlienacao = new Valor ();
  private Valor lucroRecebido = new Valor ();
  private ColecaoItemQuadroLucrosDividendos lucroRecebidoQuadroAuxiliar = new ColecaoItemQuadroLucrosDividendos ();
  private Valor parcIsentaAtivRural = new Valor ();
  private Valor parcIsentaAposentadoria = new Valor (this, "Parcela isenta de proventos");
  private Valor pensao = new Valor ();
  private Valor poupanca = new Valor ();
  private ColecaoItemQuadroAuxiliar poupancaQuadroAuxiliar = new ColecaoItemQuadroAuxiliar ();
  private Valor rendSocio = new Valor ();
  private Valor transferencias = new Valor ();
  private Valor outros = new Valor ();
  private ColecaoItemQuadroAuxiliar outrosQuadroAuxiliar = new ColecaoItemQuadroAuxiliar ();
  private Valor rendDependentes = new Valor ();
  private ColecaoItemQuadroAuxiliar rendDependentesQuadroAuxiliar = new ColecaoItemQuadroAuxiliar ();
  private Valor total = new Valor ();
  private Alfa descOutros = new Alfa ();
  private Valor bensPequenoValorInformado = new Valor (this, "");
  private Valor unicoImovelInformado = new Valor (this, "");
  private Valor outrosBensImoveisInformado = new Valor (this, "");
  private Valor moedaEstrangeiraEspecieInformado = new Valor (this, "");
  private Valor totalInformado = new Valor (this, "");
  private Valor bensPequenoValorTransportado = new Valor (this, "");
  private Valor unicoImovelTransportado = new Valor (this, "");
  private Valor outrosBensImoveisTransportado = new Valor (this, "");
  private Valor moedaEstrangeiraEspecieTransportado = new Valor (this, "");
  private Valor totalTransportado = new Valor (this, "");
  
  public RendIsentos ()
  {
    getTotal ().setReadOnly (true);
    getParcIsentaAtivRural ().setReadOnly (true);
    getLucroRecebido ().setReadOnly (true);
    getBensPequenoValorTransportado ().setHabilitado (false);
    getUnicoImovelTransportado ().setHabilitado (false);
    getOutrosBensImoveisTransportado ().setHabilitado (false);
    getMoedaEstrangeiraEspecieTransportado ().setHabilitado (false);
    getTotalInformado ().setHabilitado (false);
    getTotalTransportado ().setHabilitado (false);
    getParcIsentaAposentadoria ().addValidador (new ValidadorImpeditivoDefault (tab.msg ("rendisentos_aposentadoria_limite", new String[] { "16.249,44" }))
    {
      public RetornoValidacao validarImplementado ()
      {
	Valor proxConteudo = new Valor ((String) getProximoConteudo ());
	if (proxConteudo.comparacao (">", "16.249,44"))
	  {
	    setTipoExibicao (0);
	    setSeveridade ((byte) 5);
	    return new RetornoValidacao (tab.msg ("rendisentos_aposentadoria_limite", new String[] { "16.249,44" }));
	  }
	return null;
      }
      
      public void acaoOk ()
      {
	/* empty */
      }
      
      public void acaoCancelar ()
      {
	/* empty */
      }
      
      public String getTituloPopup ()
      {
	return getParcIsentaAposentadoria ().getNomeCampo ();
      }
    });
    lucroRecebido.addValidador (new ValidadorDefault ((byte) 2)
    {
      public RetornoValidacao validarImplementado ()
      {
	Iterator it = lucroRecebidoQuadroAuxiliar.recuperarLista ().iterator ();
	while (it.hasNext ())
	  {
	    ItemQuadroLucrosDividendos item = (ItemQuadroLucrosDividendos) it.next ();
	    if (item.getNomeFonte ().isVazio ())
	      return new RetornoValidacao (tab.msg ("nome_fonte_pagadora_ausente"), (byte) 2);
	  }
	return null;
      }
    });
    setFicha ("Rendimentos Isentos e N\u00e3o-Tribut\u00e1veis");
  }
  
  public void addObservador (CalculosRendIsentos calculosRendIsentos)
  {
    bolsaEstudos.addObservador (calculosRendIsentos);
    capitalApolices.addObservador (calculosRendIsentos);
    indenizacoes.addObservador (calculosRendIsentos);
    bensPequenoValorInformado.addObservador (calculosRendIsentos);
    unicoImovelInformado.addObservador (calculosRendIsentos);
    outrosBensImoveisInformado.addObservador (calculosRendIsentos);
    moedaEstrangeiraEspecieInformado.addObservador (calculosRendIsentos);
    bensPequenoValorTransportado.addObservador (calculosRendIsentos);
    unicoImovelTransportado.addObservador (calculosRendIsentos);
    outrosBensImoveisTransportado.addObservador (calculosRendIsentos);
    moedaEstrangeiraEspecieTransportado.addObservador (calculosRendIsentos);
    lucroAlienacao.addObservador (calculosRendIsentos);
    lucroRecebido.addObservador (calculosRendIsentos);
    parcIsentaAtivRural.addObservador (calculosRendIsentos);
    parcIsentaAposentadoria.addObservador (calculosRendIsentos);
    pensao.addObservador (calculosRendIsentos);
    poupanca.addObservador (calculosRendIsentos);
    rendSocio.addObservador (calculosRendIsentos);
    transferencias.addObservador (calculosRendIsentos);
    outros.addObservador (calculosRendIsentos);
    rendDependentes.addObservador (calculosRendIsentos);
  }
  
  public Valor getBolsaEstudos ()
  {
    return bolsaEstudos;
  }
  
  public void setBolsaEstudos (Valor bolsaEstudos)
  {
    this.bolsaEstudos = bolsaEstudos;
  }
  
  public Valor getCapitalApolices ()
  {
    return capitalApolices;
  }
  
  public void setCapitalApolices (Valor capitalApolices)
  {
    this.capitalApolices = capitalApolices;
  }
  
  public Alfa getDescOutros ()
  {
    return descOutros;
  }
  
  public void setDescOutros (Alfa descOutros)
  {
    this.descOutros = descOutros;
  }
  
  public Valor getIndenizacoes ()
  {
    return indenizacoes;
  }
  
  public void setIndenizacoes (Valor idenizacoes)
  {
    indenizacoes = idenizacoes;
  }
  
  public Valor getLucroAlienacao ()
  {
    return lucroAlienacao;
  }
  
  public void setLucroAlienacao (Valor lucroAlienacao)
  {
    this.lucroAlienacao = lucroAlienacao;
  }
  
  public Valor getLucroRecebido ()
  {
    return lucroRecebido;
  }
  
  public void setLucroRecebido (Valor lucroRecebido)
  {
    this.lucroRecebido = lucroRecebido;
  }
  
  public Valor getOutros ()
  {
    return outros;
  }
  
  public void setOutros (Valor outros)
  {
    this.outros = outros;
  }
  
  public Valor getParcIsentaAposentadoria ()
  {
    return parcIsentaAposentadoria;
  }
  
  public void setParcIsentaAposentadoria (Valor parcIsentaAposentadoria)
  {
    this.parcIsentaAposentadoria = parcIsentaAposentadoria;
  }
  
  public Valor getParcIsentaAtivRural ()
  {
    return parcIsentaAtivRural;
  }
  
  public void setParcIsentaAtivRural (Valor parcIsentaAtivRural)
  {
    this.parcIsentaAtivRural = parcIsentaAtivRural;
  }
  
  public Valor getPensao ()
  {
    return pensao;
  }
  
  public void setPensao (Valor pensao)
  {
    this.pensao = pensao;
  }
  
  public Valor getPoupanca ()
  {
    return poupanca;
  }
  
  public void setPoupanca (Valor poupanca)
  {
    this.poupanca = poupanca;
  }
  
  public Valor getRendDependentes ()
  {
    return rendDependentes;
  }
  
  public void setRendDependentes (Valor rendDependentes)
  {
    this.rendDependentes = rendDependentes;
  }
  
  public Valor getRendSocio ()
  {
    return rendSocio;
  }
  
  public void setRendSocio (Valor rendSocio)
  {
    this.rendSocio = rendSocio;
  }
  
  public Valor getTotal ()
  {
    return total;
  }
  
  public void setTotal (Valor total)
  {
    this.total = total;
  }
  
  public Valor getTransferencias ()
  {
    return transferencias;
  }
  
  public void setTransferencias (Valor transferencias)
  {
    this.transferencias = transferencias;
  }
  
  public ColecaoItemQuadroLucrosDividendos getLucroRecebidoQuadroAuxiliar ()
  {
    return lucroRecebidoQuadroAuxiliar;
  }
  
  public ColecaoItemQuadroAuxiliar getOutrosQuadroAuxiliar ()
  {
    return outrosQuadroAuxiliar;
  }
  
  public ColecaoItemQuadroAuxiliar getPoupancaQuadroAuxiliar ()
  {
    return poupancaQuadroAuxiliar;
  }
  
  public ColecaoItemQuadroAuxiliar getRendDependentesQuadroAuxiliar ()
  {
    return rendDependentesQuadroAuxiliar;
  }
  
  public Valor getBensPequenoValorInformado ()
  {
    return bensPequenoValorInformado;
  }
  
  public Valor getBensPequenoValorTransportado ()
  {
    return bensPequenoValorTransportado;
  }
  
  public Valor getMoedaEstrangeiraEspecieInformado ()
  {
    return moedaEstrangeiraEspecieInformado;
  }
  
  public Valor getMoedaEstrangeiraEspecieTransportado ()
  {
    return moedaEstrangeiraEspecieTransportado;
  }
  
  public Valor getOutrosBensImoveisInformado ()
  {
    return outrosBensImoveisInformado;
  }
  
  public Valor getOutrosBensImoveisTransportado ()
  {
    return outrosBensImoveisTransportado;
  }
  
  public Valor getUnicoImovelInformado ()
  {
    return unicoImovelInformado;
  }
  
  public Valor getUnicoImovelTransportado ()
  {
    return unicoImovelTransportado;
  }
  
  public Valor getTotalInformado ()
  {
    return totalInformado;
  }
  
  public Valor getTotalTransportado ()
  {
    return totalTransportado;
  }
  
  public Valor recuperarTotalTitularExcetoAtividadeRuraleGC ()
  {
    Valor result = new Valor ();
    result.append ('+', getIndenizacoes ());
    result.append ('+', getLucroAlienacao ());
    result.append ('+', getParcIsentaAposentadoria ());
    result.append ('+', getCapitalApolices ());
    result.append ('+', getPoupanca ());
    result.append ('+', getPensao ());
    result.append ('+', getTransferencias ());
    result.append ('+', getOutros ());
    result.append ('+', getBolsaEstudos ());
    result.append ('+', getRendSocio ());
    return result;
  }
  
  public Valor recuperarTotalTitular ()
  {
    Valor result = new Valor ();
    result.append ('+', getIndenizacoes ());
    result.append ('+', getLucroAlienacao ());
    result.append ('+', recuperarTotalLucrosDividendosTit ());
    result.append ('+', getParcIsentaAtivRural ());
    result.append ('+', getParcIsentaAposentadoria ());
    result.append ('+', getCapitalApolices ());
    result.append ('+', getPoupanca ());
    result.append ('+', getPensao ());
    result.append ('+', getTransferencias ());
    result.append ('+', getOutros ());
    result.append ('+', getBolsaEstudos ());
    result.append ('+', getRendSocio ());
    return result;
  }
  
  public Valor recuperarSubTotalRendIsentoTransportado ()
  {
    Valor result = new Valor ();
    result.append ('+', getParcIsentaAtivRural ());
    result.append ('+', getLucroAlienacao ());
    return result;
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List lista = super.recuperarListaCamposPendencia ();
    lista.add (lucroRecebido);
    return lista;
  }
  
  public Valor recuperarTotalLucrosDividendosTit ()
  {
    Valor total = new Valor ();
    Iterator it = lucroRecebidoQuadroAuxiliar.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	ItemQuadroLucrosDividendos item = (ItemQuadroLucrosDividendos) it.next ();
	if (item.getTipo ().asString ().equals ("Titular"))
	  total.append ('+', item.getValor ());
      }
    return total;
  }
  
  public Valor recuperarTotalLucrosDividendosDep ()
  {
    Valor total = new Valor ();
    Iterator it = lucroRecebidoQuadroAuxiliar.recuperarLista ().iterator ();
    while (it.hasNext ())
      {
	ItemQuadroLucrosDividendos item = (ItemQuadroLucrosDividendos) it.next ();
	if (item.getTipo ().asString ().equals ("Dependente"))
	  total.append ('+', item.getValor ());
      }
    return total;
  }
}
