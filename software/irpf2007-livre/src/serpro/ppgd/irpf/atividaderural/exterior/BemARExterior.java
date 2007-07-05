/* BemARExterior - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.atividaderural.exterior;
import serpro.ppgd.irpf.atividaderural.BemAR;
import serpro.ppgd.irpf.tabelas.CadastroTabelasIRPF;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class BemARExterior extends BemAR
{
  protected Codigo pais = new Codigo (this, "Pa\u00eds", CadastroTabelasIRPF.recuperarPaisesExterior ());
  protected Alfa descricaoPais = new Alfa (this, "");
  
  public BemARExterior ()
  {
    getPais ().addValidador (new ValidadorNaoNulo ((byte) 3, tab.msg ("ficha_bem_ar_pais"))
    {
      public RetornoValidacao validarImplementado ()
      {
	if (BemARExterior.this.isVazio ())
	  return null;
	return super.validarImplementado ();
      }
    });
    getPais ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	descricaoPais.setConteudo (getPais ().getConteudoAtual (1));
      }
    });
  }
  
  public Codigo getPais ()
  {
    return pais;
  }
}
