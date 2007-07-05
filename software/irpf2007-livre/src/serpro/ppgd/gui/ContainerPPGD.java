/* ContainerPPGD - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;

import serpro.ppgd.negocio.Informacao;

public class ContainerPPGD extends Box
{
  public static final byte NONE = 0;
  public static final byte ESQUERDA = 1;
  public static final byte ACIMA = 2;
  private float alinhamento;
  private int eixo;
  private byte posicaoRotulo;
  
  public ContainerPPGD ()
  {
    super (1);
    eixo = 1;
    alinhamento = 0.0F;
    posicaoRotulo = (byte) 1;
  }
  
  public ContainerPPGD (int eixo, float alinhamento, byte posicaoRotulo)
  {
    super (eixo);
    this.eixo = eixo;
    this.alinhamento = alinhamento;
    if (posicaoRotulo < 0 || posicaoRotulo > 2)
      throw new IllegalArgumentException ("Posi\u00e7\u00e3o do r\u00f3tulo inv\u00e1lida");
    this.posicaoRotulo = posicaoRotulo;
  }
  
  public void addTitulo (String titulo, float alinhamentoTitulo)
  {
    JLabel labelTitulo = new JLabel (titulo);
    labelTitulo.setText (titulo);
    configuraAlinhamento (labelTitulo, alinhamentoTitulo);
    add (labelTitulo);
  }
  
  public EditCampo addCampo (EditCampo editCampo)
  {
    return addCampo (editCampo, null);
  }
  
  public EditCampo addCampo (EditCampo editCampo, String idAjuda)
  {
    editCampo.setIdAjuda (idAjuda);
    adicionaCampo (editCampo);
    return editCampo;
  }
  
  public EditCampo addCampo (Informacao campo)
  {
    return addCampo (campo, (String) null);
  }
  
  public EditCampo addCampo (Informacao campo, String idAjuda)
  {
    Dimension[] d = new Dimension[1];
    d[0] = new Dimension (0, 0);
    return addCampo (campo, d, idAjuda);
  }
  
  public EditCampo addCampo (Informacao campo, int tamanho)
  {
    return addCampo (campo, tamanho, null);
  }
  
  public EditCampo addCampo (Informacao campo, int tamanho, String idAjuda)
  {
    Dimension[] d = new Dimension[1];
    d[0] = new Dimension (tamanho, 0);
    return addCampo (campo, d, idAjuda);
  }
  
  public EditCampo addCampo (Informacao campo, Dimension dimension, String idAjuda)
  {
    Dimension[] d = new Dimension[1];
    d[0] = dimension;
    return addCampo (campo, d, idAjuda);
  }
  
  public EditCampo addCampo (Informacao campo, Dimension[] dimension)
  {
    return addCampo (campo, dimension, null);
  }
  
  public EditCampo addCampo (Informacao campo, Dimension[] dimension, String idAjuda)
  {
    EditCampo editCampo = FabricaGUI.getEditorInformacao (campo, dimension, idAjuda);
    adicionaCampo (editCampo);
    return editCampo;
  }
  
  private void adicionaCampo (EditCampo editCampo)
  {
    configuraAlinhamento (this, alinhamento);
    JComponent boxCampo = FabricaGUI.criaJCampo (editCampo);
    configuraAlinhamento (boxCampo, alinhamento);
    if (eixo == 1)
      {
	switch (posicaoRotulo)
	  {
	  case 2:
	    add (Box.createRigidArea (ConstantesGlobaisGUI.VGAP5));
	    configuraAlinhamento (editCampo.getRotulo (), alinhamento);
	    add (editCampo.getRotulo ());
	    add (boxCampo);
	    break;
	  case 1:
	    {
	      add (Box.createRigidArea (ConstantesGlobaisGUI.VGAP5));
	      ContainerPPGD boxLabelCampo = new ContainerPPGD (0, 0.0F, (byte) 0);
	      boxLabelCampo.configuraAlinhamento (editCampo.getRotulo (), 0.5F);
	      configuraAlinhamento (boxLabelCampo, 0.0F);
	      boxLabelCampo.add (Box.createRigidArea (ConstantesGlobaisGUI.HGAP5));
	      boxLabelCampo.add (editCampo.getRotulo ());
	      boxLabelCampo.add (Box.createRigidArea (ConstantesGlobaisGUI.HGAP5));
	      boxLabelCampo.add (boxCampo);
	      add (boxLabelCampo);
	      break;
	    }
	  default:
	    add (Box.createRigidArea (ConstantesGlobaisGUI.VGAP5));
	    add (boxCampo);
	  }
      }
    else if (eixo == 0)
      {
	switch (posicaoRotulo)
	  {
	  case 2:
	    {
	      ContainerPPGD boxLabelCampo = new ContainerPPGD (1, 0.0F, (byte) 2);
	      boxLabelCampo.configuraAlinhamento (editCampo.getRotulo (), alinhamento);
	      boxLabelCampo.configuraAlinhamento (boxCampo, alinhamento);
	      boxLabelCampo.add (editCampo.getRotulo ());
	      boxLabelCampo.add (boxCampo);
	      add (boxLabelCampo);
	      add (Box.createRigidArea (ConstantesGlobaisGUI.HGAP10));
	      break;
	    }
	  case 1:
	    configuraAlinhamento (editCampo.getRotulo (), alinhamento);
	    configuraAlinhamento (boxCampo, alinhamento);
	    add (editCampo.getRotulo ());
	    add (boxCampo);
	    add (Box.createRigidArea (ConstantesGlobaisGUI.HGAP10));
	    break;
	  default:
	    add (boxCampo);
	  }
      }
  }
  
  public void addBox (ContainerPPGD box)
  {
    configuraAlinhamento (box, alinhamento);
    add (Box.createRigidArea (ConstantesGlobaisGUI.VGAP5));
    add (box);
  }
  
  private void configuraAlinhamento (JComponent componente, float alinhamento)
  {
    if (eixo == 1)
      {
	componente.setAlignmentX (alinhamento);
	componente.setAlignmentY (0.5F);
      }
    else if (eixo == 0)
      {
	componente.setAlignmentX (0.5F);
	componente.setAlignmentY (alinhamento);
      }
  }
}
