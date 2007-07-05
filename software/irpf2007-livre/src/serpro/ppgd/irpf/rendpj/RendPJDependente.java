/* RendPJDependente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpj;
import java.util.List;

import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.util.Validador;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCPF;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class RendPJDependente extends RendPJTitular
{
  protected CPF cpfDependente = new CPF (this, "CPF");
  
  public RendPJDependente (IdentificadorDeclaracao id)
  {
    super (id);
    ValidadorNaoNulo validadorNaoNulo = new ValidadorNaoNulo ((byte) 3);
    validadorNaoNulo.setMensagemValidacao (tab.msg ("rendpjdep_cpf_branco"));
    getCpfDependente ().addValidador (validadorNaoNulo);
    ValidadorCPF validadorCPF = new ValidadorCPF ((byte) 3);
    validadorCPF.setMensagemValidacao (tab.msg ("rendpjdep_cpf_invalido"));
    getCpfDependente ().addValidador (validadorCPF);
  }
  
  public void addValidadores ()
  {
    ValidadorNaoNulo validadorNaoNuloNomeFontePagadora = new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	RetornoValidacao retornoValidacao = Validador.validarNI (RendPJDependente.this.getNIFontePagadora ().asString ());
	if (retornoValidacao == null && RendPJDependente.this.getNomeFontePagadora ().isVazio ())
	  {
	    setSeveridade ((byte) 2);
	    return new RetornoValidacao (tab.msg ("nome_fonte_pagadora_ausente"), (byte) 2);
	  }
	if (retornoValidacao != null && RendPJDependente.this.getNomeFontePagadora ().isVazio ())
	  {
	    setSeveridade ((byte) 3);
	    return new RetornoValidacao (tab.msg ("nome_fonte_pagadora"), (byte) 3);
	  }
	return null;
      }
    };
    getNomeFontePagadora ().addValidador (validadorNaoNuloNomeFontePagadora);
    ValidadorNaoNulo validadorNaoNuloNI = new ValidadorNaoNulo ((byte) 3)
    {
      public RetornoValidacao validarImplementado ()
      {
	RetornoValidacao retornoValidacao = Validador.validarNI (RendPJDependente.this.getNIFontePagadora ().asString ());
	if (RendPJDependente.this.getNIFontePagadora ().asString ().equals (getCpfDependente ().asString ()))
	  {
	    setSeveridade ((byte) 3);
	    return new RetornoValidacao (tab.msg ("ni_fonte_pagadora_igual_dependente"), (byte) 3);
	  }
	if (retornoValidacao != null && ! RendPJDependente.this.getImpostoRetidoFonte ().isVazio ())
	  {
	    setSeveridade ((byte) 3);
	    return new RetornoValidacao (tab.msg ("ni_fonte_pagadora"), (byte) 3);
	  }
	if (retornoValidacao != null && RendPJDependente.this.getImpostoRetidoFonte ().isVazio ())
	  {
	    setSeveridade ((byte) 2);
	    return new RetornoValidacao (tab.msg ("ni_fonte_pagadora"), (byte) 2);
	  }
	return null;
      }
    };
    getNIFontePagadora ().addValidador (validadorNaoNuloNI);
  }
  
  public CPF getCpfDependente ()
  {
    return cpfDependente;
  }
  
  public List verificarPendencias (int numeroItem)
  {
    List retorno = super.verificarPendencias (numeroItem);
    return retorno;
  }
}
