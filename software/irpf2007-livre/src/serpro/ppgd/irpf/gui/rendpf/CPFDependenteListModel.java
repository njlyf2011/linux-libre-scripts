/* CPFDependenteListModel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.rendpf;
import javax.swing.AbstractListModel;

import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.rendpf.CPFDependente;
import serpro.ppgd.irpf.rendpf.ColecaoCPFDependentes;

public class CPFDependenteListModel extends AbstractListModel
{
  private ColecaoCPFDependentes colecaoCPFDependentes = new ColecaoCPFDependentes ();
  
  public CPFDependenteListModel ()
  {
    if (! PlataformaPPGD.isEmDesign ())
      colecaoCPFDependentes.recuperarLista ().addAll (IRPFFacade.getInstancia ().getRendPFDependente ().getColecaoCPFDependentes ().recuperarLista ());
  }
  
  public int getSize ()
  {
    return colecaoCPFDependentes.recuperarLista ().size ();
  }
  
  public Object getElementAt (int index)
  {
    CPFDependente cpfDep = (CPFDependente) colecaoCPFDependentes.recuperarLista ().get (index);
    return cpfDep;
  }
  
  public ColecaoCPFDependentes getColecaoCPFDependentes ()
  {
    return colecaoCPFDependentes;
  }
  
  public void setColecaoCPFDependentes (ColecaoCPFDependentes pColecaoCPFDependentes)
  {
    colecaoCPFDependentes = pColecaoCPFDependentes;
  }
  
  public void addCPF (String cpf)
  {
    CPFDependente cpfDep = new CPFDependente ();
    cpfDep.getCpf ().setConteudo (cpf);
    addCPF (cpfDep);
  }
  
  public void addCPF (CPFDependente cpf)
  {
    colecaoCPFDependentes.recuperarLista ().add (cpf);
    fireContentsChanged (this, 0, getSize () - 1);
  }
  
  public void deletaItens (Object[] obj)
  {
    if (colecaoCPFDependentes.recuperarLista ().size () != 0)
      {
	for (int i = 0; i < obj.length; i++)
	  getColecaoCPFDependentes ().recuperarLista ().remove (obj[i]);
	fireContentsChanged (this, 0, getSize () - 1);
      }
  }
}
