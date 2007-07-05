/* PreenchedorFichasImpressao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf;
import serpro.ppgd.irpf.impressao.ImpressaoDeclaracao;
import serpro.ppgd.negocio.ConstantesGlobais;

public class PreenchedorFichasImpressao
{
  public static void preencheRelatorioGeral (ImpressaoDeclaracao novaImpressao, DeclaracaoIRPF dec)
  {
    novaImpressao.addParametro ("cpfContribuinte", dec.getIdentificadorDeclaracao ().getCpf ().getConteudoFormatado ());
    novaImpressao.addParametro ("nomeContribuinte", dec.getIdentificadorDeclaracao ().getNome ().getConteudoFormatado ());
  }
  
  public static void preencheContribuinte (ImpressaoDeclaracao novaImpressao, DeclaracaoIRPF dec, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
    if (dec.getContribuinte ().getExterior ().asString ().equals ("1"))
      novaImpressao.addParametroUltimo ("municipio", dec.getContribuinte ().getCidade ().getConteudoFormatado ());
    else
      novaImpressao.addParametroUltimo ("municipio", dec.getContribuinte ().getMunicipio ().getConteudoAtual (1));
    novaImpressao.addParametroUltimo ("descNaturezaOcupacao", dec.getContribuinte ().getNaturezaOcupacao ().getConteudoAtual (1));
    novaImpressao.addParametroUltimo ("descOcupacaoPrincipal", dec.getContribuinte ().getOcupacaoPrincipal ().getConteudoAtual (3));
    novaImpressao.addParametroUltimo ("retificadora", dec.getContribuinte ().getDeclaracaoRetificadora ().getConteudoFormatado ().equals ("0") ? "N\u00e3o" : "Sim");
    novaImpressao.addParametroUltimo ("endDiferente", dec.getContribuinte ().getEnderecoDiferente ().getConteudoFormatado ().equals ("0") ? "N\u00e3o" : "Sim");
    if (dec.getContribuinte ().getDeclaracaoRetificadora ().getConteudoFormatado ().equals ("0"))
      {
	novaImpressao.addParametroUltimo ("txtRecibo", "N\u00ba do recibo da \u00faltima declara\u00e7\u00e3o entregue do exerc\u00edcio de 2006:");
	novaImpressao.addParametroUltimo ("numeroRecibo", dec.getContribuinte ().getNumeroReciboDecAnterior ().getConteudoFormatado ());
      }
    else
      {
	novaImpressao.addParametro ("txtRecibo", "N\u00ba do recibo da declara\u00e7\u00e3o anterior do exerc\u00edcio de 2007:");
	novaImpressao.addParametro ("numeroRecibo", dec.getContribuinte ().getNumReciboDecRetif ().getConteudoFormatado ());
      }
  }
  
  public static void preencheConjuge (ImpressaoDeclaracao novaImpressao, DeclaracaoIRPF dec, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheRendPJTitular (ImpressaoDeclaracao novaImpressao, DeclaracaoIRPF dec, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheRendaVariavel (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheARBrasil (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
    novaImpressao.addParametroUltimo ("ARLocal", "BRASIL");
  }
  
  public static void preencheARApuracaoBrasil (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
    novaImpressao.addParametroUltimo ("ARLocal", "BRASIL");
    novaImpressao.addParametroUltimo ("ANO_RECEITA_RECEBIDA", ConstantesGlobais.ANO_BASE);
    novaImpressao.addParametroUltimo ("ANO_ADIANTAMENTO", ConstantesGlobais.ANO_BASE_ANTERIOR);
    novaImpressao.addParametroUltimo ("ANO_PRODUTOS_ENTREGUES", ConstantesGlobais.ANO_BASE);
  }
  
  public static void preencheARExterior (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
    novaImpressao.addParametroUltimo ("ARLocal", "EXTERIOR");
  }
  
  public static void preencheARApuracaoExterior (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
    novaImpressao.addParametroUltimo ("ARLocal", "EXTERIOR");
    novaImpressao.addParametroUltimo ("ANO_RECEITA_RECEBIDA", ConstantesGlobais.ANO_BASE);
    novaImpressao.addParametroUltimo ("ANO_ADIANTAMENTO", ConstantesGlobais.ANO_BASE_ANTERIOR);
    novaImpressao.addParametroUltimo ("ANO_PRODUTOS_ENTREGUES", ConstantesGlobais.ANO_BASE);
  }
  
  public static void preencheResumo (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
    novaImpressao.addParametroUltimo ("ANO_BASE_ANTERIOR", ConstantesGlobais.ANO_BASE_ANTERIOR);
    novaImpressao.addParametroUltimo ("ANO_BASE", ConstantesGlobais.ANO_BASE);
  }
  
  public static void preencheDividasOnus (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheInventariante (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheDoacoesCampanha (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheBensDireitos (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preenchePgtosDoacoes (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheDependentes (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
    novaImpressao.addParametroUltimo ("totDeducoesDependentes", dec.getDependentes ().getTotalDeducaoDependentes ().getConteudoFormatado ());
  }
  
  public static void preencheImpostoPago (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheRendTribExcl (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheRendIsentos (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheRendPFDependente (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheRendPFTitular (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
  
  public static void preencheRendPJDependente (DeclaracaoIRPF dec, ImpressaoDeclaracao novaImpressao, boolean primeiroRelatorio)
  {
    if (primeiroRelatorio)
      preencheRelatorioGeral (novaImpressao, dec);
  }
}
