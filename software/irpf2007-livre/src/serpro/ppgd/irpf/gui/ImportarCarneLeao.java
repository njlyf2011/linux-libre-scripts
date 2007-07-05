/* ImportarCarneLeao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.PainelCacher;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.rendpf.PainelRendPFTitular;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.irpf.txt.importacao.carneleao.RepositorioTxtDadosCarneLeao;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class ImportarCarneLeao extends AbstractAction
{
  public static final String PADRAO_EXT_CARNE_LEAO = ".(LE" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + "|le" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + ")";
  
  public void actionPerformed (ActionEvent e)
  {
    importar ();
  }
  
  private void importar ()
  {
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    RendPF rendPF = IRPFFacade.getInstancia ().getRendPFTitular ();
    if (rendPF.isVazio () || JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("confirmacao_importacao"), "Confirma\u00e7\u00e3o", 0) == 0)
      {
	rendPF.clear ();
	final JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Importa\u00e7\u00e3o de dados do Carn\u00ea-Le\u00e3o", "Procurar em:", "Importar", "Importar dados do Carn\u00ea-Le\u00e3o");
	fc.setAcceptAllFileFilterUsed (false);
	fc.setFileFilter (new DECFilter ());
	Integer retorno = (Integer) ProcessoSwing.executa (new Tarefa ()
	{
	  public Object run ()
	  {
	    return new Integer (fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ()));
	  }
	});
	if (retorno.intValue () == 0)
	  {
	    final RepositorioTxtDadosCarneLeao repositorioTxtDadosCarneLeao = new RepositorioTxtDadosCarneLeao (fc.getSelectedFile ().getPath ());
	    Object retornoImport = ProcessoSwing.executa (new Tarefa ()
	    {
	      public Object run ()
	      {
		try
		  {
		    repositorioTxtDadosCarneLeao.importaDados ();
		  }
		catch (GeracaoTxtException e)
		  {
		    return "Arquivo corrompido.";
		  }
		catch (IOException e)
		  {
		    return "Ocorreu um erro de IO:" + e.getMessage ();
		  }
		return null;
	      }
	    });
	    if (retornoImport != null)
	      JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), (String) retornoImport, "Erro", 0);
	    else
	      {
		if (IRPFGuiUtil.painelAtualmenteExibido != null)
		  {
		    String string = IRPFGuiUtil.painelAtualmenteExibido.getClass ().getName ();
		    Class var_class = serpro.ppgd.irpf.gui.rendpf.PainelRendPFTitular.class;
		    if (string.equals (var_class.getName ()))
		      {
			PainelCacher painelcacher = PainelCacher.getInstance ();
			Class var_class_5_ = serpro.ppgd.irpf.gui.rendpf.PainelRendPFTitular.class;
			PainelRendPFTitular painelRendPFTitular = (PainelRendPFTitular) painelcacher.obtemUrgentemente (var_class_5_.getName ());
			((IRPFTableModel) painelRendPFTitular.tableRendPF1.getModel ()).fireTableDataChanged ();
		      }
		  }
		JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("importacao_carneleao_sucesso"), "Informa\u00e7\u00e3o", 1);
	      }
	  }
      }
  }
}
