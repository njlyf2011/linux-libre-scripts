/* CalculosRendPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.calculos;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.negocio.Observador;
import serpro.ppgd.negocio.Valor;

public class CalculosRendPF extends Observador
{
  private RendPF rendPF = null;
  
  public CalculosRendPF (RendPF pRendpf)
  {
    rendPF = pRendpf;
  }
  
  public void notifica (Object observado, String nomePropriedade, Object valorAntigo, Object valorNovo)
  {
    if (nomePropriedade != null)
      {
	if (nomePropriedade.equals ("Pessoa F\u00edsica"))
	  calculaTotaisPessoaFisica ();
	else if (! nomePropriedade.equals ("Carn\u00ea-Le\u00e3o"))
	  {
	    if (nomePropriedade.equals ("Darf Pago c\u00f3d. 0190"))
	      calculaTotaisDarfPago ();
	    else if (nomePropriedade.equals ("Dependentes"))
	      calculaTotaisDependentes ();
	    else if (nomePropriedade.equals ("Exterior"))
	      calculaTotaisExterior ();
	    else if (nomePropriedade.equals ("Livro Caixa"))
	      calculaTotaisLivroCaixa ();
	    else if (nomePropriedade.equals ("Pens\u00e3o Aliment\u00edcia"))
	      calculaTotaisPensao ();
	    else if (nomePropriedade.equals ("Previd\u00eancia oficial"))
	      calculaTotaisPrevidenciaOficial ();
	  }
      }
  }
  
  public void calculaTotaisPessoaFisica ()
  {
    Valor total = new Valor ();
    total.append ('+', rendPF.getJaneiro ().getPessoaFisica ());
    total.append ('+', rendPF.getFevereiro ().getPessoaFisica ());
    total.append ('+', rendPF.getMarco ().getPessoaFisica ());
    total.append ('+', rendPF.getAbril ().getPessoaFisica ());
    total.append ('+', rendPF.getMaio ().getPessoaFisica ());
    total.append ('+', rendPF.getJunho ().getPessoaFisica ());
    total.append ('+', rendPF.getJulho ().getPessoaFisica ());
    total.append ('+', rendPF.getAgosto ().getPessoaFisica ());
    total.append ('+', rendPF.getSetembro ().getPessoaFisica ());
    total.append ('+', rendPF.getOutubro ().getPessoaFisica ());
    total.append ('+', rendPF.getNovembro ().getPessoaFisica ());
    total.append ('+', rendPF.getDezembro ().getPessoaFisica ());
    rendPF.getTotalPessoaFisica ().setConteudo (total);
  }
  
  public void calculaTotaisExterior ()
  {
    Valor total = new Valor ();
    total.append ('+', rendPF.getJaneiro ().getExterior ());
    total.append ('+', rendPF.getFevereiro ().getExterior ());
    total.append ('+', rendPF.getMarco ().getExterior ());
    total.append ('+', rendPF.getAbril ().getExterior ());
    total.append ('+', rendPF.getMaio ().getExterior ());
    total.append ('+', rendPF.getJunho ().getExterior ());
    total.append ('+', rendPF.getJulho ().getExterior ());
    total.append ('+', rendPF.getAgosto ().getExterior ());
    total.append ('+', rendPF.getSetembro ().getExterior ());
    total.append ('+', rendPF.getOutubro ().getExterior ());
    total.append ('+', rendPF.getNovembro ().getExterior ());
    total.append ('+', rendPF.getDezembro ().getExterior ());
    rendPF.getTotalExterior ().setConteudo (total);
  }
  
  public void calculaTotaisPrevidenciaOficial ()
  {
    Valor total = new Valor ();
    total.append ('+', rendPF.getJaneiro ().getPrevidencia ());
    total.append ('+', rendPF.getFevereiro ().getPrevidencia ());
    total.append ('+', rendPF.getMarco ().getPrevidencia ());
    total.append ('+', rendPF.getAbril ().getPrevidencia ());
    total.append ('+', rendPF.getMaio ().getPrevidencia ());
    total.append ('+', rendPF.getJunho ().getPrevidencia ());
    total.append ('+', rendPF.getJulho ().getPrevidencia ());
    total.append ('+', rendPF.getAgosto ().getPrevidencia ());
    total.append ('+', rendPF.getSetembro ().getPrevidencia ());
    total.append ('+', rendPF.getOutubro ().getPrevidencia ());
    total.append ('+', rendPF.getNovembro ().getPrevidencia ());
    total.append ('+', rendPF.getDezembro ().getPrevidencia ());
    rendPF.getTotalPrevidencia ().setConteudo (total);
  }
  
  public void calculaTotaisDependentes ()
  {
    Valor total = new Valor ();
    total.append ('+', rendPF.getJaneiro ().getDependentes ());
    total.append ('+', rendPF.getFevereiro ().getDependentes ());
    total.append ('+', rendPF.getMarco ().getDependentes ());
    total.append ('+', rendPF.getAbril ().getDependentes ());
    total.append ('+', rendPF.getMaio ().getDependentes ());
    total.append ('+', rendPF.getJunho ().getDependentes ());
    total.append ('+', rendPF.getJulho ().getDependentes ());
    total.append ('+', rendPF.getAgosto ().getDependentes ());
    total.append ('+', rendPF.getSetembro ().getDependentes ());
    total.append ('+', rendPF.getOutubro ().getDependentes ());
    total.append ('+', rendPF.getNovembro ().getDependentes ());
    total.append ('+', rendPF.getDezembro ().getDependentes ());
    rendPF.getTotalDependentes ().setConteudo (total);
  }
  
  public void calculaTotaisPensao ()
  {
    Valor total = new Valor ();
    total.append ('+', rendPF.getJaneiro ().getPensao ());
    total.append ('+', rendPF.getFevereiro ().getPensao ());
    total.append ('+', rendPF.getMarco ().getPensao ());
    total.append ('+', rendPF.getAbril ().getPensao ());
    total.append ('+', rendPF.getMaio ().getPensao ());
    total.append ('+', rendPF.getJunho ().getPensao ());
    total.append ('+', rendPF.getJulho ().getPensao ());
    total.append ('+', rendPF.getAgosto ().getPensao ());
    total.append ('+', rendPF.getSetembro ().getPensao ());
    total.append ('+', rendPF.getOutubro ().getPensao ());
    total.append ('+', rendPF.getNovembro ().getPensao ());
    total.append ('+', rendPF.getDezembro ().getPensao ());
    rendPF.getTotalPensao ().setConteudo (total);
  }
  
  public void calculaTotaisLivroCaixa ()
  {
    Valor total = new Valor ();
    total.append ('+', rendPF.getJaneiro ().getLivroCaixa ());
    total.append ('+', rendPF.getFevereiro ().getLivroCaixa ());
    total.append ('+', rendPF.getMarco ().getLivroCaixa ());
    total.append ('+', rendPF.getAbril ().getLivroCaixa ());
    total.append ('+', rendPF.getMaio ().getLivroCaixa ());
    total.append ('+', rendPF.getJunho ().getLivroCaixa ());
    total.append ('+', rendPF.getJulho ().getLivroCaixa ());
    total.append ('+', rendPF.getAgosto ().getLivroCaixa ());
    total.append ('+', rendPF.getSetembro ().getLivroCaixa ());
    total.append ('+', rendPF.getOutubro ().getLivroCaixa ());
    total.append ('+', rendPF.getNovembro ().getLivroCaixa ());
    total.append ('+', rendPF.getDezembro ().getLivroCaixa ());
    rendPF.getTotalLivroCaixa ().setConteudo (total);
  }
  
  public void calculaTotaisDarfPago ()
  {
    Valor total = new Valor ();
    total.append ('+', rendPF.getJaneiro ().getDarfPago ());
    total.append ('+', rendPF.getFevereiro ().getDarfPago ());
    total.append ('+', rendPF.getMarco ().getDarfPago ());
    total.append ('+', rendPF.getAbril ().getDarfPago ());
    total.append ('+', rendPF.getMaio ().getDarfPago ());
    total.append ('+', rendPF.getJunho ().getDarfPago ());
    total.append ('+', rendPF.getJulho ().getDarfPago ());
    total.append ('+', rendPF.getAgosto ().getDarfPago ());
    total.append ('+', rendPF.getSetembro ().getDarfPago ());
    total.append ('+', rendPF.getOutubro ().getDarfPago ());
    total.append ('+', rendPF.getNovembro ().getDarfPago ());
    total.append ('+', rendPF.getDezembro ().getDarfPago ());
    rendPF.getTotalDarfPago ().setConteudo (total);
  }
}
