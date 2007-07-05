/* ImovelARBrasil - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.brasil;
import serpro.ppgd.irpf.atividaderural.ImovelAR;
import serpro.ppgd.negocio.NIRF;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNIRF;

public class ImovelARBrasil extends ImovelAR
{
  protected NIRF nirf = new NIRF (this, "NIRF");
  
  public ImovelARBrasil ()
  {
    getNirf ().addValidador (new ValidadorNIRF ((byte) 2)
    {
      public RetornoValidacao validarImplementado ()
      {
	if (ImovelARBrasil.this.getCodigo ().isVazio ())
	  return null;
	String codigo = ImovelARBrasil.this.getCodigo ().getConteudoAtual (0);
	if (! codigo.trim ().equals ("10") && ! codigo.trim ().equals ("11") && ! codigo.trim ().equals ("12") && ! codigo.trim ().equals ("13") && ! codigo.trim ().equals ("14") && ! codigo.trim ().equals ("99"))
	  return null;
	return super.validarImplementado ();
      }
    });
  }
  
  public NIRF getNirf ()
  {
    return nirf;
  }
}
