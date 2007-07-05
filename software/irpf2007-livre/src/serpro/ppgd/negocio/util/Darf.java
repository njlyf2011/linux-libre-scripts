/* Darf - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio.util;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import serpro.ppgd.negocio.ConstantesGlobais;

public class Darf
{
  protected FormDarf m_panel;
  protected BufferedImage m_bi1;
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
    numeroreferencia = numeroreferencia1;
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
  
  public void imprime ()
  {
    FormDarf m_panel = new FormDarf ();
    try
      {
	String pathBrasao = this.getClass ().getResource ("/imagens/brasao.jpg").getFile ();
	FileInputStream in = new FileInputStream (pathBrasao);
	JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder (in);
	BufferedImage m_bi1 = decoder.decodeAsBufferedImage ();
	in.close ();
	m_panel.setBufferedImage (m_bi1);
      }
    catch (Exception ex)
      {
	ex.printStackTrace ();
	LogPPGD.erro ("openFile: " + ex.toString ());
      }
    m_panel.setNomeDeclarante (nomedeclarante);
    m_panel.setDdd (ddd);
    m_panel.setNroFone (nrofone);
    m_panel.setMensagem (mensagem);
    m_panel.setPeriodoApuracao (periodoapuracao);
    m_panel.setCpfDeclarante (cpfdeclarante);
    m_panel.setCodigoReceita (codigoreceita);
    m_panel.setNumeroReferencia (numeroreferencia);
    m_panel.setDataVencimento (datavencimento);
    m_panel.setValorQuota (valorquota);
    m_panel.setValorMulta (valormulta);
    m_panel.setValorJuros (valorjuros);
    m_panel.setValorTotal (valortotal);
    m_panel.setNumeroVias (numerovias);
    try
      {
	PrinterJob prnJob = PrinterJob.getPrinterJob ();
	prnJob.setPrintable (m_panel);
	prnJob.print ();
      }
    catch (PrinterException e)
      {
	e.printStackTrace ();
	LogPPGD.erro ("Printing error: " + e.toString ());
      }
  }
  
  public static void main (String[] args)
  {
    Darf darf = new Darf ();
    darf.setNomeDeclarante ("Nome Declarante");
    darf.setDdd ("001");
    darf.setNroFone ("3333-4444");
    darf.setMensagem ("Isto \u00e9 uma mensagem");
    darf.setPeriodoApuracao ("31/12/" + ConstantesGlobais.ANO_BASE);
    darf.setCpfDeclarante ("999.999.999-99");
    darf.setCodigoReceita ("9999");
    darf.setNumeroReferencia ("");
    darf.setDataVencimento ("30/04/" + ConstantesGlobais.EXERCICIO);
    darf.setValorQuota ("1.200,00");
    darf.setValorMulta ("");
    darf.setValorJuros ("");
    darf.setValorTotal ("");
    darf.setNumeroVias (2);
    darf.imprime ();
  }
}
