/* DECFilter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.io.File;

import javax.swing.filechooser.FileFilter;

import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.Validador;

class DECFilter extends FileFilter
{
  IdentificadorDeclaracao idDeclaracao = IRPFFacade.getInstancia ().getIdDeclaracaoAberto ();
  private String padraoNomeArqCarneLeao = idDeclaracao.getCpf ().asString ().substring (0, 8) + ImportarCarneLeao.PADRAO_EXT_CARNE_LEAO;
  
  public boolean accept (File f)
  {
    if (f.isDirectory ())
      return true;
    return Validador.validarString (f.getName (), padraoNomeArqCarneLeao);
  }
  
  public String getDescription ()
  {
    return "Arquivos Carn\u00ea Le\u00e3o " + ConstantesGlobais.ANO_BASE;
  }
}
