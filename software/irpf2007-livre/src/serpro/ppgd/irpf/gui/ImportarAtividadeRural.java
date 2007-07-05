/* ImportarAtividadeRural - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.PainelCacher;
import serpro.ppgd.infraestrutura.util.ProcessoSwing;
import serpro.ppgd.infraestrutura.util.Tarefa;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasBrasil;
import serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasExterior;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.txt.importacao.atividaderural.RepositorioTxtDadosAtividadeRural;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.util.TabelaMensagens;
import serpro.ppgd.negocio.util.Validador;

public class ImportarAtividadeRural extends AbstractAction
{
  public static final String PADRAO_EXT_ATIV_RURAL = ".(AE" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + "|ae" + ConstantesGlobais.EXERCICIO_ANTERIOR.charAt (3) + ")";
  
  class DECFilter extends FileFilter
  {
    IdentificadorDeclaracao idDeclaracao = IRPFFacade.getInstancia ().getIdDeclaracaoAberto ();
    private String padraoNomeArqAtividadeRural = idDeclaracao.getCpf ().asString ().substring (0, 8) + ImportarAtividadeRural.PADRAO_EXT_ATIV_RURAL;
    
    public boolean accept (File f)
    {
      if (f.isDirectory ())
	return true;
      return Validador.validarString (f.getName (), padraoNomeArqAtividadeRural);
    }
    
    public String getDescription ()
    {
      return "Arquivos do Livro Caixa de Atividade Rural " + ConstantesGlobais.ANO_BASE;
    }
  }
  
  public void actionPerformed (ActionEvent e)
  {
    importar ();
  }
  
  public boolean importaBrasil ()
  {
    return true;
  }
  
  public boolean importaExterior ()
  {
    return true;
  }
  
  private void importar ()
  {
    final JFileChooser fc = UtilitariosGUI.setFileChooserProperties ("Importa\u00e7\u00e3o de dados do Livro Caixa da Atividade Rural", "Procurar em:", "Importar", "Importa\u00e7\u00e3o de dados do Livro Caixa da Atividade Rural");
    fc.setAcceptAllFileFilterUsed (false);
    fc.setFileFilter (new DECFilter ());
    Integer retorno = (Integer) ProcessoSwing.executa (new Tarefa ()
    {
      public Object run ()
      {
	return new Integer (fc.showOpenDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal ()));
      }
    });
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    if (retorno.intValue () == 0)
      {
	final RepositorioTxtDadosAtividadeRural repositorioTxtDadosAtividadeRural = new RepositorioTxtDadosAtividadeRural (fc.getSelectedFile ().getPath (), importaBrasil (), importaExterior ());
	Object retornoImport = ProcessoSwing.executa (new Tarefa ()
	{
	  public Object run ()
	  {
	    try
	      {
		repositorioTxtDadosAtividadeRural.importaDados ();
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
		Class var_class = serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasBrasil.class;
		if (string.equals (var_class.getName ()))
		  {
		    PainelCacher painelcacher = PainelCacher.getInstance ();
		    Class var_class_5_ = serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasBrasil.class;
		    PainelReceitasDespesasBrasil painelReceitasDespesasBrasil = (PainelReceitasDespesasBrasil) painelcacher.obtemUrgentemente (var_class_5_.getName ());
		    ((IRPFTableModel) painelReceitasDespesasBrasil.tableReceitasDespesasBrasil1.getModel ()).fireTableDataChanged ();
		  }
		else
		  {
		    String string_7_ = IRPFGuiUtil.painelAtualmenteExibido.getClass ().getName ();
		    Class var_class_8_ = serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasExterior.class;
		    if (string_7_.equals (var_class_8_.getName ()))
		      {
			PainelCacher painelcacher = PainelCacher.getInstance ();
			Class var_class_10_ = serpro.ppgd.irpf.gui.atividaderural.PainelReceitasDespesasExterior.class;
			PainelReceitasDespesasExterior painelReceitasDespesasExterior = (PainelReceitasDespesasExterior) painelcacher.obtemUrgentemente (var_class_10_.getName ());
			painelReceitasDespesasExterior.getNavegador ().primeiro ();
		      }
		  }
	      }
	    JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("importacao_ar_sucesso"), "Informa\u00e7\u00e3o", 1);
	  }
      }
  }
}
