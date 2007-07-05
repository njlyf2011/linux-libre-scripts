/* FormDarf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JPanel;

class FormDarf extends JPanel implements Printable
{
  protected BufferedImage m_bi = null;
  protected String nomedeclarante = null;
  protected String ddd = null;
  protected String nrofone = null;
  protected String mensagem = null;
  protected String periodoapuracao = null;
  protected String cpfdeclarante = null;
  protected String codigoreceita = null;
  protected String numeroreferencia = null;
  protected String datavencimento = null;
  protected String valorquota = null;
  protected String valormulta = null;
  protected String valorjuros = null;
  protected String valortotal = null;
  protected int numerovias = 0;
  public int m_maxNumPage = 1;
  
  public FormDarf ()
  {
    /* empty */
  }
  
  public void setBufferedImage (BufferedImage bi)
  {
    if (bi != null)
      {
	m_bi = bi;
	Dimension d = new Dimension (m_bi.getWidth (this), m_bi.getHeight (this));
	setPreferredSize (d);
	revalidate ();
	repaint ();
      }
  }
  
  public void setNomeDeclarante (String nomedeclarante1)
  {
    nomedeclarante = nomedeclarante1;
  }
  
  public void setDdd (String ddd1)
  {
    ddd = ddd1;
  }
  
  public void setNroFone (String nrofone1)
  {
    nrofone = nrofone1;
  }
  
  public void setMensagem (String mensagem1)
  {
    mensagem = mensagem1;
  }
  
  public void setPeriodoApuracao (String periodoapuracao1)
  {
    periodoapuracao = periodoapuracao1;
  }
  
  public void setCpfDeclarante (String cpfdeclarante1)
  {
    cpfdeclarante = cpfdeclarante1;
  }
  
  public void setCodigoReceita (String codigoreceita1)
  {
    codigoreceita = codigoreceita1;
  }
  
  public void setNumeroReferencia (String numeroreferencia1)
  {
    /* empty */
  }
  
  public void setDataVencimento (String datavencimento1)
  {
    datavencimento = datavencimento1;
  }
  
  public void setValorQuota (String valorquota1)
  {
    valorquota = valorquota1;
  }
  
  public void setValorMulta (String valormulta1)
  {
    valormulta = valormulta1;
  }
  
  public void setValorJuros (String valorjuros1)
  {
    valorjuros = valorjuros1;
  }
  
  public void setValorTotal (String valortotal1)
  {
    valortotal = valortotal1;
  }
  
  public void setNumeroVias (int numerovias1)
  {
    numerovias = numerovias1;
  }
  
  public int print (Graphics pg, PageFormat pageFormat, int pageIndex) throws PrinterException
  {
    if (pageIndex >= m_maxNumPage || m_bi == null)
      return 1;
    pg.translate ((int) pageFormat.getImageableX (), (int) pageFormat.getImageableY ());
    int wPage = (int) pageFormat.getImageableWidth ();
    int hPage = (int) pageFormat.getImageableHeight ();
    int w = m_bi.getWidth (this);
    int h = m_bi.getHeight (this);
    if (w == 0 || h == 0)
      return 1;
    pg.setColor (Color.black);
    pg.drawLine (2, 2, wPage - 2, 2);
    pg.drawLine (2, 2, 2, hPage / 2 - 25 - 20);
    pg.drawLine (wPage - 2, 2, wPage - 2, hPage / 2 - 25 - 20);
    pg.drawLine (2, hPage / 2 - 25 - 20, wPage - 2, hPage / 2 - 25 - 20);
    pg.drawString ("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -", 0, hPage / 2 + 2);
    pg.drawLine (2, hPage / 2 + 25 + 20 + 2, wPage - 2, hPage / 2 + 25 + 20 + 2);
    pg.drawLine (2, hPage / 2 + 25 + 20 + 2, 2, hPage);
    pg.drawLine (wPage - 2, hPage / 2 + 25 + 20 + 2, wPage - 2, hPage);
    pg.drawLine (2, hPage, wPage - 2, hPage);
    for (int i = 0; i < 2; i++)
      {
	int y;
	if (i == 0)
	  y = 0;
	else
	  y = hPage / 2 + 25 + 20;
	pg.drawImage (m_bi, 5, 10 + y, 57, 66 + y, 0, 0, w, h, this);
	pg.setFont (new Font ("sanserif", 1, 10));
	pg.drawString ("MINIST\u00c9RIO DA FAZENDA", 65, 20 + y);
	pg.setFont (new Font ("sanserif", 1, 8));
	pg.drawString ("SECRETARIA DA RECEITA FEDERAL", 65, 32 + y);
	pg.setFont (new Font ("sanserif", 1, 6));
	pg.drawString ("Documento de Arrecada\u00e7\u00e3o de Receitas Federais", 65, 42 + y);
	pg.setFont (new Font ("sanserif", 1, 13));
	pg.drawString ("DARF", 65, 63 + y);
	pg.drawLine (wPage / 2, 2 + y, wPage / 2, hPage / 2 - 25 - 20 + y);
	pg.drawLine (3 * (wPage / 4), 2 + y, 3 * (wPage / 4), 207 + y);
	pg.drawLine (2, 77 + y, wPage / 2, 77 + y);
	pg.drawLine (2, 127 + y, wPage / 2, 127 + y);
	pg.drawLine (2, 152 + y, wPage / 2, 152 + y);
	pg.drawLine (wPage / 2, 23 + y, wPage - 2, 23 + y);
	pg.drawLine (wPage / 2, 46 + y, wPage - 2, 46 + y);
	pg.drawLine (wPage / 2, 69 + y, wPage - 2, 69 + y);
	pg.drawLine (wPage / 2, 92 + y, wPage - 2, 92 + y);
	pg.drawLine (wPage / 2, 115 + y, wPage - 2, 115 + y);
	pg.drawLine (wPage / 2, 138 + y, wPage - 2, 138 + y);
	pg.drawLine (wPage / 2, 161 + y, wPage - 2, 161 + y);
	pg.drawLine (wPage / 2, 184 + y, wPage - 2, 184 + y);
	pg.drawLine (wPage / 2, 207 + y, wPage - 2, 207 + y);
	pg.setFont (new Font ("sanserif", 1, 11));
	pg.drawString ("01", 4, 89 + y);
	pg.drawString ("02", wPage / 2 + 4, 12 + y);
	pg.drawString ("03", wPage / 2 + 4, 35 + y);
	pg.drawString ("04", wPage / 2 + 4, 58 + y);
	pg.drawString ("05", wPage / 2 + 4, 81 + y);
	pg.drawString ("06", wPage / 2 + 4, 104 + y);
	pg.drawString ("07", wPage / 2 + 4, 127 + y);
	pg.drawString ("08", wPage / 2 + 4, 150 + y);
	pg.drawString ("09", wPage / 2 + 4, 173 + y);
	pg.drawString ("10", wPage / 2 + 4, 196 + y);
	pg.drawString ("11", wPage / 2 + 4, 219 + y);
	pg.setFont (new Font ("sanserif", 1, 6));
	pg.drawString ("NOME/TELEFONE", 20, 85 + y);
	pg.drawString ("PER\u00cdODO DE APURA\u00c7\u00c3O", wPage / 2 + 20, 8 + y);
	pg.drawString ("N\u00daMERO DO CPF OU CNPJ", wPage / 2 + 20, 30 + y);
	pg.drawString ("C\u00d3DIGO DA RECEITA", wPage / 2 + 20, 53 + y);
	pg.drawString ("N\u00daMERO DE REFER\u00caNCIA", wPage / 2 + 20, 76 + y);
	pg.drawString ("DATA DE VENCIMENTO", wPage / 2 + 20, 99 + y);
	pg.drawString ("VALOR DO PRINCIPAL", wPage / 2 + 20, 122 + y);
	pg.drawString ("VALOR DA MULTA", wPage / 2 + 20, 145 + y);
	pg.drawString ("VALOR DOS JUROS E/OU", wPage / 2 + 20, 168 + y);
	pg.drawString ("ENCARGOS DL-1.025/69", wPage / 2 + 20, 175 + y);
	pg.drawString ("VALOR TOTAL", wPage / 2 + 20, 191 + y);
	pg.drawString ("AUTENTICA\u00c7\u00c3O BANC\u00c1RIA (Somente nas 1  e 2  vias)", wPage / 2 + 20, 214 + y);
	pg.setFont (new Font ("monospaced", 0, 8));
	pg.drawString (nomedeclarante, 4, 100 + y);
	pg.drawString ("(" + ddd + ")", 4, 120 + y);
	pg.drawString (nrofone, 30, 120 + y);
	pg.drawString (periodoapuracao, 3 * (wPage / 4) + 35, 18 + y);
	pg.drawString (cpfdeclarante, 3 * (wPage / 4) + 25, 41 + y);
	pg.drawString (codigoreceita, 3 * (wPage / 4) + 50, 64 + y);
	pg.drawString (datavencimento, 3 * (wPage / 4) + 35, 110 + y);
	pg.drawString (valorquota, 3 * (wPage / 4) + 25, 133 + y);
	pg.setFont (new Font ("monospaced", 1, 13));
	pg.drawString (mensagem, 20, 147 + y);
	pg.setFont (new Font ("sanserif", 1, 9));
	pg.drawString ("ATEN\u00c7\u00c3O", wPage / 4 - 20, 171 + y);
	pg.setFont (new Font ("monospaced", 0, 8));
	pg.drawString ("\u00c9 vedado o  recolhimento  de  tributos e con-", 4, 185 + y);
	pg.drawString ("tribui\u00e7\u00f5es  administrados  pela Secretaria da", 4, 195 + y);
	pg.drawString ("Receita Federal  cujo valor  total seja infe-", 4, 205 + y);
	pg.drawString ("rior a R$10,00. Ocorrendo tal  situa\u00e7\u00e3o, adi-", 4, 215 + y);
	pg.drawString ("cione esse  valor ao  tributo/contribui\u00e7\u00e3o de", 4, 225 + y);
	pg.drawString ("mesmo  c\u00f3digo  de per\u00edodos  subseq\u00fcentes, at\u00e9", 4, 235 + y);
	pg.drawString ("que o total seja igual ou superior a R$10,00.", 4, 245 + y);
      }
    System.gc ();
    return 0;
  }
}
