/* DividaAR - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural;
import java.util.Iterator;
import java.util.List;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class DividaAR extends ObjetoNegocio
{
  private Alfa discriminacao = new Alfa (this, "Discrimina\u00e7\u00e3o", 150);
  private Valor contraidasAteExercicioAnterior = new Valor (this, "D\u00edvidas at\u00e9 " + ConstantesGlobais.EXERCICIO_ANTERIOR);
  private Valor contraidasAteExercicioAtual = new Valor (this, "D\u00edvidas at\u00e9 " + ConstantesGlobais.EXERCICIO);
  private Valor efetivamentePagas = new Valor (this, "D\u00edvidas Pagas ");
  
  public DividaAR ()
  {
    getDiscriminacao ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_divida_ar_discriminacao"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    });
  }
  
  public Valor getContraidasAteExercicioAnterior ()
  {
    return contraidasAteExercicioAnterior;
  }
  
  public Valor getContraidasAteExercicioAtual ()
  {
    return contraidasAteExercicioAtual;
  }
  
  public Alfa getDiscriminacao ()
  {
    return discriminacao;
  }
  
  public Valor getEfetivamentePagas ()
  {
    return efetivamentePagas;
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
  
  protected List recuperarListaCamposPendencia ()
  {
    List retorno = recuperarCamposInformacao ();
    return retorno;
  }
}
