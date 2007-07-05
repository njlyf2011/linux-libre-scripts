/* PainelResumoOutrasInformacoes - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.resumo;
import java.awt.Font;

import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JFlipComponentes;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.negocio.Observador;

public class PainelResumoOutrasInformacoes extends JPanel implements PainelIRPFIf
{
  private static final String TITULO = "Outras informa\u00e7\u00f5es";
  private JFlipComponentes jFlipComponentes1;
  
  public PainelResumoOutrasInformacoes ()
  {
    initComponents ();
    try
      {
	IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().removeObservadores (new Class[] { this.getClass () });
      }
    catch (Exception exception)
      {
	/* empty */
      }
    IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().addObservador (new Observador ()
    {
      public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
      {
	PainelResumoOutrasInformacoes.this.atualizaSubPainel ();
      }
    });
    atualizaSubPainel ();
    Font f = PgdIRPF.FONTE_DEFAULT.deriveFont (1);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f.deriveFont (f.getSize2D () + 2.0F));
  }
  
  private void atualizaSubPainel ()
  {
    if (IRPFFacade.getInstancia ().getIdDeclaracaoAberto ().getTipoDeclaracao ().asString ().equals ("1"))
      jFlipComponentes1.exibeComponenteB ();
    else
      jFlipComponentes1.exibeComponenteA ();
  }
  
  public String getTituloPainel ()
  {
    return "Outras informa\u00e7\u00f5es";
  }
  
  private void initComponents ()
  {
    jFlipComponentes1 = new JFlipComponentes ();
    jFlipComponentes1.setBorder (null);
    jFlipComponentes1.setComponenteA (new SubPainelOutrasInformacoesCompleta ());
    jFlipComponentes1.setComponenteB (new SubPainelOutrasInformacoesSimplificada ());
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jFlipComponentes1, -1, 503, 32767));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (jFlipComponentes1, -1, 435, 32767));
  }
  
  public void vaiExibir ()
  {
    if (IRPFFacade.getInstancia ().getDeclaracao ().getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("0"))
      {
	IRPFFacade.getInstancia ().getDeclaracao ().getModeloCompleta ().resumoOutrasInformacoes ();
	IRPFFacade.getInstancia ().getDeclaracao ().getModeloCompleta ().aplicaValoresNaDeclaracao ();
      }
    if (IRPFFacade.getInstancia ().getDeclaracao ().getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("1"))
      {
	IRPFFacade.getInstancia ().getDeclaracao ().getModeloSimplificada ().resumoOutrasInformacoes ();
	IRPFFacade.getInstancia ().getDeclaracao ().getModeloSimplificada ().aplicaValoresNaDeclaracao ();
      }
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
