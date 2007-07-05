/* PainelSobre - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.gui.dialogs;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import serpro.ppgd.infraestrutura.util.BotoesDialog;
import serpro.ppgd.infraestrutura.util.PPGDFormPanel;
import serpro.ppgd.negocio.ConstantesGlobais;

public class PainelSobre extends PPGDFormPanel
{
  private BotoesDialog botoes;
  private PPGDFormPanel painelDireita = new PPGDFormPanel ();
  private PPGDFormPanel painelEsquerda = new PPGDFormPanel ();
  private PPGDFormPanel painelBotao = new PPGDFormPanel ();
  private JLabel lblSobre = new JLabel ();
  private final JScrollPane scroll;
  private boolean bolClicado = false;
  private String versao = "Vers\u00e3o 1.0";
  
  public PainelSobre ()
  {
    setLayout (new FormLayout ("2dlu,fill:p,5dlu,fill:204dlu:grow,2dlu", "2dlu,p,p,2dlu"));
    FormLayout flDireita = new FormLayout ("fill:p", "p,p,p:grow,p");
    final String strTexto1 = "<HTML><STRONG><CENTER><P><FONT COLOR=BLUE>MINIST\u00c9RIO DA FAZENDA<BR><BR>SECRETARIA DA RECEITA FEDERAL</P><BR><BR><P>Programa IRPF " + ConstantesGlobais.EXERCICIO + "</p><br><hr align=center width='80%'>" + "<p>Este produto foi desenvolvido pelo Serpro sob a <br>supervis\u00e3o da Coordena\u00e7\u00e3o-Geral de Tecnologia e <br>Seguran\u00e7a da Informa\u00e7\u00e3o - Cotec</P>" + "<br><br></CENTER><P ALIGN=RIGHT>" + versao + " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &copy;Copyright SRF/" + ConstantesGlobais.EXERCICIO + "<br></font><font color=blue>www.receita.fazenda.gov.br</p></FONT></STRONG></HTML>";
    final String strTexto2 = "<HTML><P><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR><BR></p><STRONG><CENTER><P><FONT COLOR=BLUE>PROGRAMA IRPF " + ConstantesGlobais.EXERCICIO + "</P></CENTER><BR><BR>" + "<table border=0 CELLSPACING=0 CELLPADDING=0>" + "<tr>" + "<td><P><STRONG><font color=blue>Secret\u00e1rio da Receita Federal</font><br>" + "Jorge Antonio Deher Rachid</STRONG></p></td>" + "</tr></table>" + "<br><p><STRONG><font color=blue>Secret\u00e1rio Adjunto da Receita Federal</font><br>" + "Paulo Ricardo de Souza Cardoso<br>" + "</STRONG></p><br>" + "<p><STRONG><font color=blue>Coordenador-Geral de Tecnologia e Seguran\u00e7a da Informa\u00e7\u00e3o</font><br>" + "Vitor Marcos Almeida Machado<br>" + "</STRONG></p><br>" + "<P><STRONG><font color=blue>Coordenadora de Sistemas de Informa\u00e7\u00e3o</font><br>" + "Maria do Bel\u00e9m Ferraz</STRONG></p><br>" + "<P><STRONG><font color=blue>Chefe da Divis\u00e3o de Sistemas Corporativos Tribut\u00e1rios</font><br>" + "Marcio Regadas Nogueira</STRONG></p><br>" + "<br><STRONG><font color=blue>Supervisor Nacional do Programa Imposto de Renda </font><br>" + "Joaquim Adir Vinhas Figueiredo</STRONG><br><br>" + "<p><STRONG><font size='150%' color=red>Comando T\u00e9cnico dos Programas de IRPF" + ConstantesGlobais.EXERCICIO + "</font><br><br>" + "<font color=blue>Supervisoras de Defini\u00e7\u00e3o</font><br>" + "Helena Martha G.de Medeiros - Dicor<br>" + "Samyra Fernandes Habibe - Defic/RJ/7\u00aa RF<br><br>" + "<font color=blue>Supervisor de Desenvolvimento</font><br>" + "Lucio Passele Couto - DRF/Varginha/6\u00aa RF</STRONG><br>" + "<br>" + "<STRONG><font color=blue>Equipe de Elabora\u00e7\u00e3o do Ajuda - Cosit/Dirpf</font><br>" + "Ana Fl\u00e1via Juventino<br>" + "Gl\u00e1uber Vargas de Paula<br>" + "Jos\u00e9 Maur\u00edcio Pereira \u00c1guia<br>" + "Newton Raimundo Barbosa da Silva<br>" + "Raquel Ferreira de Souza e Silva<br>" + "Rubens Massaru Saito<br>" + "<br><table border=0 CELLSPACING=0 CELLPADDING=0><tr><td><P><STRONG><font color=blue>Equipe de Desenvolvimento</font><td><img src='" + ClassLoader.getSystemClassLoader ().getResource ("icones/serpro_novo.gif") + "'></td></tr>" + "<tr><td><P><STRONG><font color=blue>L\u00edder de Projeto</p></td><td></td></tr></table>" + "<table  border=0 CELLSPACING=0 CELLPADDING=0>" + "<tr><td><strong>Leonardo de Almeida Cunha</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td collspan=2><font color=blue><strong><strong>Desenvolvedores</font></td></tr>" + "<tr><td><strong>Ant\u00f4nio Carlos M. dos Santos</td><td><strong>ATCAD/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Celson Nonato Cruz de Aquino</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Daniel Pantoja Franco</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Eliza In\u00eas Amorim de Brito</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Jo\u00e3o Victor Correia Vital</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Joselito Messias Lobo</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Klauber Cunha Mac\u00eado</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Leonardo Thomas Torres Santos</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Maria Isabela de Oliveira Cardoso</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Paulo Penna Duarte</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "<tr><td><strong>Roberto Nascimento Barros</td><td><strong>ATSEP/ATSDR/SUNAT</td></tr>" + "</table>" + "<br><P><STRONG><font color=blue>Testadores e Homologadores</font><br>" + "Adino\u00e9l Sebasti\u00e3o - DRF Maring\u00e1/9\u00aa RF<br>" + "Alexandre Rego - SRRF/4\u00aa RF<br>" + "Andr\u00e9 Soares Brand\u00e3o - DRF Bras\u00edlia/1\u00aa RF<br>" + "Carlos Alberto F. Braga - SRRF/1\u00aa RF<br>" + "Claire Feliz Regina - SRRF/8\u00aa RF<br>" + "Eleonora S. Gomes - SRRF/2\u00aa RF<br>" + "H\u00e9lio Cal - SRRF/5\u00aa RF<br>" + "Ivanir F. Silva Henriques - SRRF/8\u00aa RF<br>" + "Jos\u00e9 Carlos Gomes - SRRF/4\u00aa RF<br>" + "Josias C\u00e9lio dos Santos - DRF/Divin\u00f3polis/6\u00aa RF<br>" + "Maria Aparecida Bahia Brand\u00e3o - SRRF/6\u00aa RF<br>" + "Martha Lib\u00e2nio - SRRF/8\u00aa RF<br>" + "Ros\u00e2ngela Caminha Mesquita - DRF/3\u00aa RF<br>" + "Sandra Maria C. Costa - SRRF/7\u00aa RF<br>" + "</p>" + "</STRONG></HTML>";
    painelDireita.setLayout (flDireita);
    painelDireita.getBuilder ().setRow (1);
    painelDireita.getBuilder ().setColumn (1);
    painelDireita.getBuilder ().append (new JLabel ("<html><strong><big><center><font color='black'>IRPF " + ConstantesGlobais.EXERCICIO + "</font></center>" + "</big></strong></html>"));
    painelDireita.getBuilder ().setColumn (1);
    painelDireita.getBuilder ().setRow (2);
    painelDireita.getBuilder ().setRow (4);
    painelDireita.getBuilder ().setColumn (2);
    JLabel imagem = new JLabel (new ImageIcon (ClassLoader.getSystemClassLoader ().getResource ("icones/srf48.gif")));
    imagem.addMouseListener (new MouseListener ()
    {
      private boolean exibiu = false;
      private Rolagem rolagem;
      
      public void mouseClicked (MouseEvent e)
      {
	if (! exibiu)
	  {
	    if (rolagem != null)
	      {
		if (! rolagem.isAlive ())
		  {
		    rolagem = new Rolagem (scroll);
		    rolagem.start ();
		  }
	      }
	    else
	      {
		rolagem = new Rolagem (scroll);
		rolagem.start ();
	      }
	    lblSobre.setText (strTexto2);
	    exibiu = true;
	  }
	else
	  {
	    lblSobre.setText (strTexto1);
	    exibiu = false;
	  }
      }
      
      public void mouseEntered (MouseEvent e)
      {
	/* empty */
      }
      
      public void mousePressed (MouseEvent e)
      {
	/* empty */
      }
      
      public void mouseExited (MouseEvent e)
      {
	/* empty */
      }
      
      public void mouseReleased (MouseEvent e)
      {
	/* empty */
      }
    });
    imagem.setCursor (new Cursor (1));
    painelDireita.getBuilder ().append (imagem);
    builder.setRow (2);
    builder.setColumn (2);
    builder.setVAlignment (CellConstraints.TOP);
    builder.append (painelDireita);
    painelEsquerda.removeAll ();
    FormLayout flEsquerda = new FormLayout ("fill:p:grow", "220dlu,p");
    painelEsquerda.setLayout (flEsquerda);
    painelEsquerda.getBuilder ().setRow (1);
    painelEsquerda.getBuilder ().setColumn (1);
    lblSobre.setText (strTexto1);
    lblSobre.setBorder (null);
    scroll = new JScrollPane (lblSobre);
    scroll.setHorizontalScrollBarPolicy (31);
    painelEsquerda.getBuilder ().append (scroll);
    builder.setRow (2);
    builder.setColumn (4);
    builder.append (painelEsquerda);
    botoes = new BotoesDialog (new String[] { "Ok" });
    botoes.setMouseEvents (this, new String[] { "btOk" });
    builder.setRow (3);
    builder.setColumn (4);
    FormLayout flBotao = new FormLayout ("50dlu:grow,p,50dlu:grow", "5dlu,p");
    painelBotao.setLayout (flBotao);
    painelBotao.getBuilder ().setRow (2);
    painelBotao.getBuilder ().setColumn (2);
    painelBotao.getBuilder ().append (botoes);
    builder.append (painelBotao);
  }
  
  public void btOk ()
  {
    fecharDialogo ();
  }
  
  private void fecharDialogo ()
  {
    ((JDialog) SwingUtilities.getRoot (this)).setVisible (false);
  }
}
