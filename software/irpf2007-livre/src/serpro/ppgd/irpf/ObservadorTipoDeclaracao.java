/* ObservadorTipoDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.negocio.Observador;

public class ObservadorTipoDeclaracao extends Observador
{
  private DeclaracaoIRPF declaracao = null;
  
  public ObservadorTipoDeclaracao (DeclaracaoIRPF dec)
  {
    declaracao = dec;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    IdentificadorDeclaracao idDeclaracao = declaracao.getIdentificadorDeclaracao ();
    if (observado != null && observado.equals (idDeclaracao.getTipoDeclaracao ()))
      {
	if (idDeclaracao.getTipoDeclaracao ().asString ().equals ("0"))
	  atualizaCompleta ();
	else
	  atualizaSimplificada ();
      }
  }
  
  private void atualizaSimplificada ()
  {
    declaracao.setModeloSimplificada ();
    declaracao.getImpostoPago ().getImpostoPagoExterior ().clear ();
    declaracao.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().setHabilitado (false);
    declaracao.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().clear ();
    declaracao.getAtividadeRural ().getExterior ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().setHabilitado (false);
    declaracao.getAtividadeRural ().getExterior ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().clear ();
  }
  
  private void atualizaCompleta ()
  {
    declaracao.setModeloCompleta ();
    declaracao.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().setHabilitado (true);
    declaracao.getAtividadeRural ().getExterior ().getApuracaoResultado ().getPrejuizoExercicioAnterior ().setHabilitado (true);
    declaracao.getAtividadeRural ().getBrasil ().getReceitasDespesas ().getTotalReceita ().disparaObservadores ();
    declaracao.getAtividadeRural ().getExterior ().getReceitasDespesas ().getTotais ().disparaObservadores ();
  }
}
