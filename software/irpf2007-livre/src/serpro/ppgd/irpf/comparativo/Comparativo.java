/* Comparativo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.comparativo;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;

public class Comparativo extends ObjetoNegocio
{
  private Valor totalRendTribCompleta = new Valor (this, "");
  private Valor baseCalcCompleta = new Valor (this, "");
  private Valor saldoPagarCompleta = new Valor (this, "");
  private Valor impRestituirCompleta = new Valor (this, "");
  private Valor totalRendTribSimplificada = new Valor (this, "");
  private Valor baseCalcSimplificada = new Valor (this, "");
  private Valor saldoPagarSimplificada = new Valor (this, "");
  private Valor impRestituirSimplificada = new Valor (this, "");
  
  public Comparativo ()
  {
    totalRendTribCompleta.setReadOnly (true);
    baseCalcCompleta.setReadOnly (true);
    saldoPagarCompleta.setReadOnly (true);
    impRestituirCompleta.setReadOnly (true);
    totalRendTribSimplificada.setReadOnly (true);
    baseCalcSimplificada.setReadOnly (true);
    saldoPagarSimplificada.setReadOnly (true);
    impRestituirSimplificada.setReadOnly (true);
  }
  
  public void setTotalRendTribCompleta (Valor totRendTribCompleta)
  {
    totalRendTribCompleta = totRendTribCompleta;
  }
  
  public Valor getTotalRendTribCompleta ()
  {
    return totalRendTribCompleta;
  }
  
  public void setBaseCalcCompleta (Valor baseCalcCompleta)
  {
    this.baseCalcCompleta = baseCalcCompleta;
  }
  
  public Valor getBaseCalcCompleta ()
  {
    return baseCalcCompleta;
  }
  
  public void setSaldoPagarCompleta (Valor saldoPagarCompleta)
  {
    this.saldoPagarCompleta = saldoPagarCompleta;
  }
  
  public Valor getSaldoPagarCompleta ()
  {
    return saldoPagarCompleta;
  }
  
  public void setImpRestituirCompleta (Valor impRestituirCompleta)
  {
    this.impRestituirCompleta = impRestituirCompleta;
  }
  
  public Valor getImpRestituirCompleta ()
  {
    return impRestituirCompleta;
  }
  
  public void setTotalRendTribSimplificada (Valor totRendTribSimplificada)
  {
    totalRendTribSimplificada = totRendTribSimplificada;
  }
  
  public Valor getTotalRendTribSimplificada ()
  {
    return totalRendTribSimplificada;
  }
  
  public void setBaseCalcSimplificada (Valor baseCalcSimplificada)
  {
    this.baseCalcSimplificada = baseCalcSimplificada;
  }
  
  public Valor getBaseCalcSimplificada ()
  {
    return baseCalcSimplificada;
  }
  
  public void setSaldoPagarSimplificada (Valor saldoPagarSimplificada)
  {
    this.saldoPagarSimplificada = saldoPagarSimplificada;
  }
  
  public Valor getSaldoPagarSimplificada ()
  {
    return saldoPagarSimplificada;
  }
  
  public void setImpRestituirSimplificada (Valor impRestituirSimplificada)
  {
    this.impRestituirSimplificada = impRestituirSimplificada;
  }
  
  public Valor getImpRestituirSimplificada ()
  {
    return impRestituirSimplificada;
  }
}
