/* ItemQuadroLucrosDividendos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.util.ArrayList;

import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.CNPJ;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.PreenchedorCodigo;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorCNPJ;
import serpro.ppgd.negocio.validadoresBasicos.ValidadorNaoNulo;

public class ItemQuadroLucrosDividendos extends ObjetoNegocio
{
  private Codigo tipo = new Codigo (this, "Benefici\u00e1rio", new ArrayList ());
  private CNPJ cnpjEmpresa = new CNPJ (this, "CNPJ da Empresa");
  private Alfa nomeFonte = new Alfa (this, "Nome da fonte pagadora", 100);
  private Valor valor = new Valor (this, "Valor");
  
  public ItemQuadroLucrosDividendos ()
  {
    PreenchedorCodigo preencheCodigo = new PreenchedorCodigo (tipo);
    preencheCodigo.add ("Titular").add ("Titular").EOL ();
    preencheCodigo.add ("Dependente").add ("Dependente").EOL ();
    preencheCodigo.aplicaAlteracoes ();
    getTipo ().addValidador (new ValidadorNaoNulo ((byte) 3));
    getCnpjEmpresa ().addValidador (new ValidadorNaoNulo ((byte) 3));
    getCnpjEmpresa ().addValidador (new ValidadorCNPJ ((byte) 3));
    getValor ().addValidador (new ValidadorNaoNulo ((byte) 3));
  }
  
  public Alfa getNomeFonte ()
  {
    return nomeFonte;
  }
  
  public Valor getValor ()
  {
    return valor;
  }
  
  public CNPJ getCnpjEmpresa ()
  {
    return cnpjEmpresa;
  }
  
  public void setTipo (Codigo tipo)
  {
    this.tipo = tipo;
  }
  
  public Codigo getTipo ()
  {
    return tipo;
  }
}
