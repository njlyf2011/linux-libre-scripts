/* Dec - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.interpretador;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.ObjetoNegocio;

class Dec extends ObjetoNegocio
{
  Teste t = new Teste (getIdDeclaracao ());
  Teste2 t2 = new Teste2 (getIdDeclaracao ());
  
  public Dec (IdDeclaracao idDeclaracao)
  {
    super (idDeclaracao);
  }
}
