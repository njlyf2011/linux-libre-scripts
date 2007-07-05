/* Teste - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.interpretador;
import serpro.ppgd.negocio.Alfa;
import serpro.ppgd.negocio.IdDeclaracao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

class Teste extends ObjetoNegocio
{
  Valor a = new Valor ("5,00");
  Valor b = new Valor ("4,00");
  Valor c = new Valor ("3,00");
  Valor d = new Valor ("2,18");
  Alfa nome = new Alfa ();
  
  public Teste (IdDeclaracao idDeclaracao)
  {
    super (idDeclaracao);
    nome.setConteudo ("Joselito");
  }
}
