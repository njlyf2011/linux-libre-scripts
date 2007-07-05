/* IRPFFacade - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import java.util.List;

import serpro.ppgd.irpf.alimentandos.Alimentandos;
import serpro.ppgd.irpf.atividaderural.AtividadeRural;
import serpro.ppgd.irpf.bens.Bens;
import serpro.ppgd.irpf.comparativo.Comparativo;
import serpro.ppgd.irpf.conjuge.Conjuge;
import serpro.ppgd.irpf.contribuinte.Contribuinte;
import serpro.ppgd.irpf.dependentes.Dependentes;
import serpro.ppgd.irpf.dividas.Dividas;
import serpro.ppgd.irpf.eleicoes.Doacoes;
import serpro.ppgd.irpf.espolio.Espolio;
import serpro.ppgd.irpf.ganhosdecapital.GanhosDeCapital;
import serpro.ppgd.irpf.impostopago.ImpostoPago;
import serpro.ppgd.irpf.moedaestrangeira.MoedaEstrangeira;
import serpro.ppgd.irpf.pagamentos.Pagamentos;
import serpro.ppgd.irpf.rendIsentos.RendIsentos;
import serpro.ppgd.irpf.rendTributacaoExclusiva.RendTributacaoExclusiva;
import serpro.ppgd.irpf.rendavariavel.RendaVariavel;
import serpro.ppgd.irpf.rendpf.RendPF;
import serpro.ppgd.irpf.rendpf.RendPFDependente;
import serpro.ppgd.irpf.rendpj.ColecaoRendPJDependente;
import serpro.ppgd.irpf.rendpj.ColecaoRendPJTitular;
import serpro.ppgd.irpf.resumo.Resumo;
import serpro.ppgd.negocio.PPGDFacade;

public class IRPFFacade implements PPGDFacade
{
  private static IRPFFacade instancia = null;
  private RepositorioXMLIRPF repositorioXMLIRPF = new RepositorioXMLIRPF ();
  
  private IRPFFacade ()
  {
    /* empty */
  }
  
  public static IRPFFacade getInstancia ()
  {
    if (instancia == null)
      instancia = new IRPFFacade ();
    return instancia;
  }
  
  public static void abreDeclaracao (IdentificadorDeclaracao id)
  {
    try
      {
	getInstancia ().repositorioXMLIRPF.abreDeclaracao (id);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public static boolean existeDeclaracao (String cpf)
  {
    return getInstancia ().repositorioXMLIRPF.getListaIdDeclaracoes ().existeCPFCadastrado (cpf);
  }
  
  public static ColecaoIdDeclaracao getListaIdDeclaracoes ()
  {
    return getInstancia ().repositorioXMLIRPF.getListaIdDeclaracoes ();
  }
  
  public static void criarDeclaracao (IdentificadorDeclaracao id)
  {
    getInstancia ().repositorioXMLIRPF.criarDeclaracao (id);
  }
  
  public static void excluirDeclaracao (String cpf)
  {
    IdentificadorDeclaracao id = getInstancia ().recuperarIdDeclaracao (cpf);
    excluirDeclaracao (id);
  }
  
  public static void excluirDeclaracao (IdentificadorDeclaracao id)
  {
    getInstancia ().repositorioXMLIRPF.excluirDeclaracao (id);
  }
  
  public static void excluirDeclaracao (List ids)
  {
    getInstancia ().repositorioXMLIRPF.excluirDeclaracao (ids);
  }
  
  public static void salvaDeclaracaoAberta ()
  {
    getInstancia ().repositorioXMLIRPF.salvaDeclaracaoAberta ();
  }
  
  public static void fechaDeclaracao ()
  {
    getInstancia ().repositorioXMLIRPF.fechaDeclaracao ();
  }
  
  public Contribuinte getContribuinte ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getContribuinte ();
  }
  
  public Conjuge getConjuge ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getConjuge ();
  }
  
  public Espolio getEspolio ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getEspolio ();
  }
  
  public Resumo getResumo ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getResumo ();
  }
  
  public ImpostoPago getImpostoPago ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getImpostoPago ();
  }
  
  public Dependentes getDependentes ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getDependentes ();
  }
  
  public RendaVariavel getRendaVariavel ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getRendaVariavel ();
  }
  
  public ColecaoRendPJDependente getColecaoRendPJDependente ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getColecaoRendPJDependente ();
  }
  
  public ColecaoRendPJTitular getColecaoRendPJTitular ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getColecaoRendPJTitular ();
  }
  
  public Alimentandos getAlimentandos ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getAlimentandos ();
  }
  
  public Pagamentos getPagamentos ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getPagamentos ();
  }
  
  public Bens getBens ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getBens ();
  }
  
  public Dividas getDividas ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getDividas ();
  }
  
  public AtividadeRural getAtividadeRural ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getAtividadeRural ();
  }
  
  public GanhosDeCapital getGanhosDeCapital ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getGanhosDeCapital ();
  }
  
  public MoedaEstrangeira getMoedaEstrangeira ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getMoedaEstrangeira ();
  }
  
  public RendPF getRendPFTitular ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getRendPFTitular ();
  }
  
  public Comparativo getComparativo ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getComparativo ();
  }
  
  public RendPFDependente getRendPFDependente ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getRendPFDependente ();
  }
  
  public RendIsentos getRendIsentos ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getRendIsentos ();
  }
  
  public RendTributacaoExclusiva getRendTributacaoExclusiva ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getRendTributacaoExclusiva ();
  }
  
  public Doacoes getDoacoes ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ().getDoacoes ();
  }
  
  public ModeloDeclaracao getModelo ()
  {
    return getDeclaracao ().getModelo ();
  }
  
  public IdentificadorDeclaracao getIdDeclaracaoAberto ()
  {
    return repositorioXMLIRPF.getIdDeclaracaoAberto ();
  }
  
  public IdentificadorDeclaracao recuperarIdDeclaracao (String cpf)
  {
    return repositorioXMLIRPF.getIdDeclaracao (cpf);
  }
  
  public DeclaracaoIRPF recuperarDeclaracaoIRPF (String cpf)
  {
    try
      {
	return repositorioXMLIRPF.recuperarDeclaracaoIRPF (cpf);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
	return null;
      }
  }
  
  public void salvarDeclaracao (String cpf)
  {
    try
      {
	repositorioXMLIRPF.salvarDeclaracao (cpf);
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public boolean existeDeclaracoes ()
  {
    if (getListaIdDeclaracoes ().recuperarLista ().size () > 0)
      return true;
    return false;
  }
  
  public static DeclaracaoIRPF getDeclaracao ()
  {
    return getInstancia ().repositorioXMLIRPF.getDeclaracaoAberta ();
  }
}
