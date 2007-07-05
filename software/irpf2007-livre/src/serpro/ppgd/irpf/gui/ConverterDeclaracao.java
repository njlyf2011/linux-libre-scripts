/* ConverterDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.util.IRPFUtil;
import serpro.ppgd.negocio.util.TabelaMensagens;

public class ConverterDeclaracao extends AbstractAction
{
  private DeclaracaoIRPF declaracaoIRPF = null;
  
  public ConverterDeclaracao (DeclaracaoIRPF dec)
  {
    declaracaoIRPF = dec;
  }
  
  public boolean executaConfirmacao ()
  {
    boolean temQueverificarPrejuizoAcompensarAR = false;
    if (declaracaoIRPF.getIdentificadorDeclaracao ().isCompleta ())
      {
	if (declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getResultadoTributavel ().comparacao ("<", "0,00"))
	  temQueverificarPrejuizoAcompensarAR = true;
	else if (declaracaoIRPF.getAtividadeRural ().getExterior ().getApuracaoResultado ().getResultadoTributavel ().comparacao ("<", "0,00"))
	  temQueverificarPrejuizoAcompensarAR = true;
      }
    if (! temQueverificarPrejuizoAcompensarAR)
      return true;
    TabelaMensagens tab = TabelaMensagens.getTabelaMensagens ();
    if (JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("conf_muda_para_dec_simplificada"), "Informa\u00e7\u00e3o", 0) == 0)
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("conf_muda_para_dec_simplificada_1"), "Informa\u00e7\u00e3o", 1);
	return false;
      }
    if (JOptionPane.showConfirmDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("painel_conf_muda_tipo_declaracao_1"), "Informa\u00e7\u00e3o", 2) == 0)
      {
	JOptionPane.showMessageDialog (PlataformaPPGD.getPlataforma ().getJanelaPrincipal (), tab.msg ("conf_muda_para_dec_simplificada_2"), "Informa\u00e7\u00e3o", 1);
	declaracaoIRPF.getAtividadeRural ().getBrasil ().getApuracaoResultado ().getPrejuizoCompensar ().clear ();
      }
    else
      return false;
    return true;
  }
  
  public void actionPerformed (ActionEvent e)
  {
    if (executaConfirmacao ())
      IRPFUtil.converterDeclaracao (declaracaoIRPF);
  }
}
