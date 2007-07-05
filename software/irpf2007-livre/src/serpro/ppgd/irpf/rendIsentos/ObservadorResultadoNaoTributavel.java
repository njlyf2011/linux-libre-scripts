/* ObservadorResultadoNaoTributavel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendIsentos;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class ObservadorResultadoNaoTributavel extends Observador
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public ObservadorResultadoNaoTributavel (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    Valor parcIsentAtivRural = new Valor ();
    parcIsentAtivRural.append ('+', declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoNaoTributavel ());
    parcIsentAtivRural.append ('+', declaracaoIRPF.getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoNaoTributavel ());
    declaracaoIRPF.getRendIsentos ().getParcIsentaAtivRural ().setConteudo (parcIsentAtivRural);
  }
}
