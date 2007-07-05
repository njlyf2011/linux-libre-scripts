/* FundosInvestimentos - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.rendavariavel;
import java.util.Hashtable;

import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class FundosInvestimentos extends ObjetoNegocio
{
  private MesFundosInvestimentos jan = new MesFundosInvestimentos (0);
  private MesFundosInvestimentos fev = new MesFundosInvestimentos (1);
  private MesFundosInvestimentos mar = new MesFundosInvestimentos (2);
  private MesFundosInvestimentos abr = new MesFundosInvestimentos (3);
  private MesFundosInvestimentos mai = new MesFundosInvestimentos (4);
  private MesFundosInvestimentos jun = new MesFundosInvestimentos (5);
  private MesFundosInvestimentos jul = new MesFundosInvestimentos (6);
  private MesFundosInvestimentos ago = new MesFundosInvestimentos (7);
  private MesFundosInvestimentos set = new MesFundosInvestimentos (8);
  private MesFundosInvestimentos out = new MesFundosInvestimentos (9);
  private MesFundosInvestimentos nov = new MesFundosInvestimentos (10);
  private MesFundosInvestimentos dez = new MesFundosInvestimentos (11);
  private MesFundosInvestimentos[] meses = { jan, fev, mar, abr, mai, jun, jul, ago, set, out, nov, dez };
  private transient Valor totalBaseCalcImposto = new Valor ();
  private transient Valor totalImpostoPago = new Valor ();
  
  public void adicionarObservGanhosFundInvest (Observador obs)
  {
    totalBaseCalcImposto.addObservador (obs);
    totalImpostoPago.addObservador (obs);
  }
  
  public void adicionarCalculosTotaisFundInvest (Observador obs)
  {
    for (int i = 0; i < 12; i++)
      {
	meses[i].getBaseCalcImposto ().addObservador (obs);
	meses[i].getImpostoPago ().addObservador (obs);
      }
  }
  
  public Valor getTotalBaseCalcImposto ()
  {
    return totalBaseCalcImposto;
  }
  
  public Valor getTotalImpostoPago ()
  {
    return totalImpostoPago;
  }
  
  public MesFundosInvestimentos getAbr ()
  {
    return abr;
  }
  
  public MesFundosInvestimentos getAgo ()
  {
    return ago;
  }
  
  public MesFundosInvestimentos getDez ()
  {
    return dez;
  }
  
  public MesFundosInvestimentos getFev ()
  {
    return fev;
  }
  
  public MesFundosInvestimentos getJan ()
  {
    return jan;
  }
  
  public MesFundosInvestimentos getJul ()
  {
    return jul;
  }
  
  public MesFundosInvestimentos getJun ()
  {
    return jun;
  }
  
  public MesFundosInvestimentos getMar ()
  {
    return mar;
  }
  
  public MesFundosInvestimentos getNov ()
  {
    return nov;
  }
  
  public MesFundosInvestimentos getOut ()
  {
    return out;
  }
  
  public MesFundosInvestimentos getSet ()
  {
    return set;
  }
  
  public MesFundosInvestimentos[] getMeses ()
  {
    return meses;
  }
  
  public MesFundosInvestimentos getMai ()
  {
    return mai;
  }
  
  public Hashtable obterTotalAnual ()
  {
    Valor totalResultLiquido = new Valor ();
    Valor totalResultNegativoAnterior = new Valor ();
    Valor totalBaseCalcImposto = new Valor ();
    Valor totalPrejuizoCompensar = new Valor ();
    Valor totalImpostoDevido = new Valor ();
    Valor totalImpostoPago = new Valor ();
    for (int i = 0; i < 12; i++)
      {
	totalResultLiquido.append ('+', meses[i].getResultLiquidoMes ());
	totalBaseCalcImposto.append ('+', meses[i].getBaseCalcImposto ());
	totalImpostoDevido.append ('+', meses[i].getImpostoDevido ());
	totalImpostoPago.append ('+', meses[i].getImpostoPago ());
      }
    totalResultNegativoAnterior.setConteudo (meses[11].getResultNegativoAnterior ());
    totalPrejuizoCompensar.setConteudo (meses[11].getPrejuizoCompensar ());
    Hashtable hash = new Hashtable ();
    hash.put ("VR_TOTALANUALRESULTADOLIQUIDOSRENDAVARIAVEL_FII", totalResultLiquido);
    hash.put ("VR_TOTALANUALRESULTADONEGATIVOMESANTERIOR_FII", totalResultNegativoAnterior);
    hash.put ("VR_TOTALANUALBASECALCULOIMPOSTO_FII", totalBaseCalcImposto);
    hash.put ("VR_TOTALANUALPREJUIZOCOMPENSAR_FII", totalPrejuizoCompensar);
    hash.put ("VR_TOTALANUALIMPOSTODEVIDO_FII", totalImpostoDevido);
    hash.put ("VR_TOTALANUALIMPOSTOPAGAR_FII", totalImpostoPago);
    return hash;
  }
}
