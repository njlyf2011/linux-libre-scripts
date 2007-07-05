/* PainelAlimentandos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.alimentandos;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.layout.GroupLayout;

import serpro.ppgd.app.PgdIRPF;
import serpro.ppgd.gui.xbeans.JEditAlfa;
import serpro.ppgd.gui.xbeans.JNavegadorColecao;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoEvent;
import serpro.ppgd.infraestrutura.util.NavegadorColecaoListener;
import serpro.ppgd.irpf.alimentandos.Alimentando;
import serpro.ppgd.irpf.gui.PainelIRPFIf;
import serpro.ppgd.irpf.gui.util.IRPFGuiUtil;
import serpro.ppgd.irpf.util.IRPFUtil;

public class PainelAlimentandos extends JPanel implements PainelIRPFIf
{
  private static final String SUB_TIT = "Item n\u00ba";
  private static final String TIT = "Alimentandos";
  private JEditAlfa jEditAlfa1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JNavegadorColecao jNavegadorColecao1;
  private JTextField jTextField1;
  
  public String getTituloPainel ()
  {
    return "Alimentandos";
  }
  
  public PainelAlimentandos ()
  {
    PlataformaPPGD.getPlataforma ().setHelpID (this, "Pens\u00e3oAliment\u00edcia");
    initComponents ();
    Font f = PgdIRPF.FONTE_DEFAULT;
    f = f.deriveFont (1);
    f = f.deriveFont (f.getSize2D () + 2.0F);
    IRPFGuiUtil.setaFonteTodosLabelContainer (this, f);
    reinitEdits ();
    jNavegadorColecao1.primeiro ();
  }
  
  private void reinitEdits ()
  {
    jEditAlfa1.getInformacao ().setReadOnly (true);
    jLabel1.setText ("Item n\u00ba");
  }
  
  private void initComponents ()
  {
    jNavegadorColecao1 = new JNavegadorColecao ();
    jEditAlfa1 = new JEditAlfa ();
    jLabel1 = new JLabel ();
    jLabel2 = new JLabel ();
    jLabel3 = new JLabel ();
    jTextField1 = new JTextField ();
    jNavegadorColecao1.setInformacaoAssociada ("alimentandos");
    jNavegadorColecao1.addNavegadorColecaoListener (new NavegadorColecaoListener ()
    {
      public void exibeColecaoVazia (NavegadorColecaoEvent evt)
      {
	PainelAlimentandos.this.jNavegadorColecao1ExibeColecaoVazia (evt);
      }
      
      public void exibeOutro (NavegadorColecaoEvent evt)
      {
	PainelAlimentandos.this.jNavegadorColecao1ExibeOutro (evt);
      }
    });
    jLabel1.setHorizontalAlignment (0);
    jLabel1.setText ("Item n\u00ba");
    jLabel2.setText ("Nome");
    jLabel3.setText ("Consulta/Atualiza\u00e7\u00e3o");
    jTextField1.setBorder (null);
    jTextField1.addFocusListener (new FocusAdapter ()
    {
      public void focusGained (FocusEvent evt)
      {
	PainelAlimentandos.this.jTextField1FocusGained (evt);
      }
    });
    GroupLayout layout = new GroupLayout (this);
    setLayout (layout);
    layout.setHorizontalGroup (layout.createParallelGroup (1).add (jLabel1, -1, 406, 32767).add (layout.createSequentialGroup ().add (jLabel3).addContainerGap ()).add (layout.createSequentialGroup ().add (jNavegadorColecao1, -2, -1, -2).addContainerGap (234, 32767)).add (layout.createSequentialGroup ().addContainerGap ().add (jLabel2).addPreferredGap (0).add (layout.createParallelGroup (1).add (jTextField1, -2, -1, -2).add (jEditAlfa1, -1, 349, 32767)).addContainerGap ()));
    layout.setVerticalGroup (layout.createParallelGroup (1).add (layout.createSequentialGroup ().add (jLabel3).add (8, 8, 8).add (jNavegadorColecao1, -2, -1, -2).addPreferredGap (0).add (jLabel1).addPreferredGap (0).add (layout.createParallelGroup (2).add (jLabel2).add (jEditAlfa1, -2, -1, -2)).addPreferredGap (0).add (jTextField1, -2, -1, -2).addContainerGap (187, 32767)));
  }
  
  private void jTextField1FocusGained (FocusEvent evt)
  {
    if (jNavegadorColecao1.getIndiceAtual () + 1 >= jNavegadorColecao1.getColecao ().recuperarLista ().size ())
      {
	if (jNavegadorColecao1.getIndiceAtual () != -1)
	  jNavegadorColecao1.adiciona ();
      }
    else
      jNavegadorColecao1.proximo ();
  }
  
  private void jNavegadorColecao1ExibeOutro (NavegadorColecaoEvent evt)
  {
    int index = jNavegadorColecao1.getIndiceAtual () + 1;
    jLabel1.setText ("Item n\u00ba " + String.valueOf (index));
    Alimentando a = (Alimentando) evt.getObjetoNegocio ();
    jEditAlfa1.setInformacao (a.getNome ());
    jEditAlfa1.setaFoco (false);
  }
  
  private void jNavegadorColecao1ExibeColecaoVazia (NavegadorColecaoEvent evt)
  {
    reinitEdits ();
  }
  
  public void vaiExibir ()
  {
    if (IRPFUtil.getEstadoSistema () != 1)
      {
	if (jNavegadorColecao1.getColecao ().recuperarLista ().size () == 0)
	  jNavegadorColecao1.adiciona ();
	if (jNavegadorColecao1.getColecao ().recuperarLista ().size () > 1)
	  jNavegadorColecao1.getColecao ().excluirRegistrosEmBranco ();
	jNavegadorColecao1.primeiro ();
      }
  }
  
  public void painelExibido ()
  {
    /* empty */
  }
}
