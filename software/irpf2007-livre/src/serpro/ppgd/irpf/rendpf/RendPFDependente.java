/* RendPFDependente - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendpf;
import java.util.List;

public class RendPFDependente extends RendPF
{
  private ColecaoCPFDependentes colecaoCPFDependentes = new ColecaoCPFDependentes ();
  
  public ColecaoCPFDependentes getColecaoCPFDependentes ()
  {
    return colecaoCPFDependentes;
  }
  
  public void aplicaNomeFicha ()
  {
    setFicha ("Rendimentos Tribut\u00e1veis Recebidos de PF e do Exterior - Dependentes");
    for (int i = 0; i < 12; i++)
      meses[i].setFicha ("Rendimentos Tribut\u00e1veis Recebidos de PF e do Exterior - Dependentes");
  }
  
  protected List recuperarListaCamposPendencia ()
  {
    List lista = super.recuperarListaCamposPendencia ();
    for (int i = 0; i < 12; i++)
      {
	lista.add (meses[i].getCarneLeao ());
	lista.add (meses[i].getDarfPago ());
	lista.add (meses[i].getDependentes ());
	lista.add (meses[i].getExterior ());
	lista.add (meses[i].getLivroCaixa ());
	lista.add (meses[i].getPensao ());
	lista.add (meses[i].getPessoaFisica ());
	lista.add (meses[i].getPrevidencia ());
      }
    return lista;
  }
}
