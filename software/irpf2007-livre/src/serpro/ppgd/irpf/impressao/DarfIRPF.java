/* DarfIRPF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.irpf.impressao;
import serpro.ppgd.formatosexternos.barcode.BarcodeEncoder;
import serpro.ppgd.formatosexternos.barcode.util.DarfAdaptor;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Data;

public class DarfIRPF
{
  public static String gerarBarcode (DeclaracaoIRPF declaracao, Data pDataVencimento)
  {
    DarfAdaptor dadosDarf = new DarfAdaptor ();
    try
      {
	dadosDarf.setCodigoEmpresa ("0064");
	dadosDarf.setIndicadorManualCalculado ("1");
	dadosDarf.setNome (declaracao.getIdDeclaracao ().getNome ().toString ());
	dadosDarf.setTelefone (declaracao.getContribuinte ().getTelefone ().asString ());
	Data d = new Data ();
	d.setConteudo ("31/12/" + ConstantesGlobais.EXERCICIO);
	dadosDarf.setPeriodoApuracao (d);
	dadosDarf.setCodigoReceita ("0211");
	dadosDarf.setNumeroReferencia ("856");
	Data dataVencimento = new Data ();
	dataVencimento.setConteudo (pDataVencimento.getConteudoFormatado ());
	dadosDarf.setDataVencimento (dataVencimento);
	dadosDarf.setValorPrincipal (declaracao.getResumo ().getCalculoImposto ().getValorQuota ().asString ());
	dadosDarf.setValorMulta ("0,00");
	dadosDarf.setValorJuros ("0,00");
	dadosDarf.setValorTotal (declaracao.getResumo ().getCalculoImposto ().getValorQuota ().asString ());
	dadosDarf.setObservacoes ("OBSERVAC\u00d5ES");
	dadosDarf.setDataValidade (d);
	dadosDarf.setMensagem ("MENSAGEM");
	dadosDarf.setMunicipio ("MUNIC\u00cdPIO - " + declaracao.getContribuinte ().getMunicipio ().getConteudoFormatado ());
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
    BarcodeEncoder encoder = BarcodeEncoder.gerarCodigo (dadosDarf);
    return encoder.getNumero ();
  }
}
