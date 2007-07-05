/* CalculosRendIsentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.rendIsentos.RendIsentos;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosRendIsentos extends Observador
{
  private RendIsentos rendIsentos;
  
  public CalculosRendIsentos (RendIsentos aRendIsentos)
  {
    rendIsentos = aRendIsentos;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    calculaTotalRendIsentos ();
  }
  
  private void calculaTotalRendIsentos ()
  {
    Valor total = new Valor (rendIsentos.getBolsaEstudos ().asString ());
    total.append ('+', rendIsentos.getCapitalApolices ());
    total.append ('+', rendIsentos.getIndenizacoes ());
    calculaAlienacaoGC ();
    total.append ('+', rendIsentos.getLucroAlienacao ());
    total.append ('+', rendIsentos.getLucroRecebido ());
    total.append ('+', rendIsentos.getOutros ());
    total.append ('+', rendIsentos.getParcIsentaAposentadoria ());
    total.append ('+', rendIsentos.getParcIsentaAtivRural ());
    total.append ('+', rendIsentos.getPensao ());
    total.append ('+', rendIsentos.getPoupanca ());
    total.append ('+', rendIsentos.getRendDependentes ());
    total.append ('+', rendIsentos.getRendSocio ());
    total.append ('+', rendIsentos.getTransferencias ());
    rendIsentos.getTotal ().setConteudo (total);
  }
  
  private void calculaAlienacaoGC ()
  {
    Valor totalInf = new Valor ();
    Valor totalTransp = new Valor ();
    totalInf.append ('+', rendIsentos.getBensPequenoValorInformado ());
    totalInf.append ('+', rendIsentos.getUnicoImovelInformado ());
    totalInf.append ('+', rendIsentos.getOutrosBensImoveisInformado ());
    totalInf.append ('+', rendIsentos.getMoedaEstrangeiraEspecieInformado ());
    totalTransp.append ('+', rendIsentos.getBensPequenoValorTransportado ());
    totalTransp.append ('+', rendIsentos.getUnicoImovelInformado ());
    totalTransp.append ('+', rendIsentos.getOutrosBensImoveisTransportado ());
    totalTransp.append ('+', rendIsentos.getMoedaEstrangeiraEspecieTransportado ());
    rendIsentos.getTotalInformado ().setConteudo (totalInf);
    rendIsentos.getTotalTransportado ().setConteudo (totalTransp);
  }
}
