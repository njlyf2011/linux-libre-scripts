/* RegistroTxt - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import java.util.Enumeration;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import serpro.hash.Crc32;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.negocio.util.LogPPGD;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.util.PLong;

public class RegistroTxt
{
  protected boolean fOpcional;
  protected Vector objListaCampos;
  protected String fTipo;
  protected String tipoArquivo;
  protected int fPosicaoAtual = 0;
  protected boolean participaImportacao = true;
  protected boolean participaGravacao = true;
  
  public RegistroTxt ()
  {
    objListaCampos = new Vector ();
    fOpcional = false;
    fPosicaoAtual = 0;
  }
  
  public RegistroTxt (String tipoArq, String tipoReg) throws GeracaoTxtException
  {
    this ();
    tipoArquivo = tipoArq;
    fTipo = tipoReg;
    inicializaLista (tipoArq, MapeamentoTXT.getInstance ().getRelacaoCamposRegistro (tipoArq, tipoReg));
  }
  
  public RegistroTxt (String tipoArq, String tipoReg, boolean opcional) throws GeracaoTxtException
  {
    this (tipoArq, tipoReg);
    fOpcional = opcional;
  }
  
  protected void inicializaLista (String tipo, NodeList node) throws GeracaoTxtException
  {
    setParticipaGravacao (MapeamentoTXT.getInstance ().participaGravacao (tipoArquivo, getTipo ()));
    setParticipaImportacao (MapeamentoTXT.getInstance ().participaImportacao (tipoArquivo, getTipo ()));
    if (node != null)
      {
	for (int i = 0; i < node.getLength (); i++)
	  {
	    Node nodeAtual = node.item (i);
	    if (nodeAtual instanceof Element && nodeAtual.getNodeName ().equals ("Campo"))
	      {
		Element nodeFilho = (Element) node.item (i);
		String decimais = nodeFilho.getAttribute ("Decimais");
		String casasDecimais = nodeFilho.getAttribute ("CasasDecimais");
		if (casasDecimais.trim ().length () > 0)
		  decimais = casasDecimais;
		adicionaCampo (nodeFilho.getAttribute ("Nome"), nodeFilho.getAttribute ("Tipo"), nodeFilho.getAttribute ("Tamanho"), decimais, nodeFilho.getAttribute ("atributoObjetoNegocio"), nodeFilho.getAttribute ("Conteudo"), nodeFilho.getAttribute ("ParticipaImportacao"), nodeFilho.getAttribute ("ParticipaGravacao"));
	      }
	  }
      }
  }
  
  protected void adicionaCampo (String nome, String tipoSize) throws GeracaoTxtException
  {
    CampoReg campoReg = new CampoReg (nome, tipoSize);
    objListaCampos.add (campoReg);
    campoReg.setPosicaoInicial (fPosicaoAtual);
    fPosicaoAtual += campoReg.getCampoTXT ().getTamanho ();
  }
  
  protected void adicionaCampo (String nome, String tipo, String size) throws GeracaoTxtException
  {
    CampoReg campoReg = new CampoReg (nome, tipo.concat (size));
    objListaCampos.add (campoReg);
    campoReg.setPosicaoInicial (fPosicaoAtual);
    fPosicaoAtual += campoReg.getCampoTXT ().getTamanho ();
  }
  
  protected void adicionaCampo (String nome, String tipo, String size, String decimais, String pAtributoObjetoNegocio, String pConteudoEstatico, String pParticipaImportacao, String pParticipaGravacao) throws GeracaoTxtException
  {
    CampoReg campoReg;
    if (decimais.equals (""))
      campoReg = new CampoReg (nome, tipo.concat (size));
    else
      campoReg = new CampoReg (nome, tipo.concat (size).concat (",").concat (decimais));
    if (pAtributoObjetoNegocio != null && pAtributoObjetoNegocio.trim ().length () != 0)
      campoReg.setAtributoObjetoNegocio (pAtributoObjetoNegocio);
    if (pConteudoEstatico != null && pConteudoEstatico.trim ().length () != 0)
      campoReg.setConteudoEstatico (pConteudoEstatico);
    if (pParticipaImportacao != null && pParticipaImportacao.trim ().equals ("false"))
      campoReg.setParticipaImportacao (false);
    if (pParticipaGravacao != null && pParticipaGravacao.trim ().equals ("false"))
      campoReg.setParticipaGravacao (false);
    objListaCampos.add (campoReg);
    campoReg.setPosicaoInicial (fPosicaoAtual);
    fPosicaoAtual += campoReg.getCampoTXT ().getTamanho ();
  }
  
  public int getTamanho ()
  {
    int tamanho = 0;
    Enumeration enumTemp = objListaCampos.elements ();
    while (enumTemp.hasMoreElements ())
      tamanho += ((CampoReg) enumTemp.nextElement ()).getCampoTXT ().getTamanho ();
    return tamanho;
  }
  
  public String getLinha () throws GeracaoTxtException
  {
    StringBuffer linha = new StringBuffer ("");
    Enumeration enumTemp = objListaCampos.elements ();
    while (enumTemp.hasMoreElements ())
      linha.append (((CampoReg) enumTemp.nextElement ()).getCampoTXT ().asTxt ());
    return linha.toString ();
  }
  
  public void setLinha (String linha) throws GeracaoTxtException
  {
    if (linha.length () != getTamanho ())
      {
	LogPPGD.debug ("tamanho limite->" + getTamanho ());
	LogPPGD.debug ("Linha->" + linha);
	LogPPGD.debug ("tamanho linha->" + linha.length ());
	throw new GeracaoTxtException ("Tamanho da linha diferente ( Registro ) " + getTipo () + ".");
      }
    Enumeration enumTemp = objListaCampos.elements ();
    while (enumTemp.hasMoreElements ())
      {
	CampoReg campoReg = (CampoReg) enumTemp.nextElement ();
	int inicio = campoReg.getPosicaoInicial ();
	int fim = campoReg.getPosicaoFinal ();
	campoReg.getCampoTXT ().set (linha.substring (inicio, inicio + (fim - inicio) + 1));
      }
  }
  
  public String getTipo ()
  {
    return fTipo;
  }
  
  public boolean estaPreenchido () throws NumberFormatException, GeracaoTxtException
  {
    boolean preenchido = true;
    Enumeration enumTemp = objListaCampos.elements ();
  while_175_:
    do
      {
	do
	  {
	    if (! enumTemp.hasMoreElements ())
	      break while_175_;
	  }
	while (! ((CampoReg) enumTemp.nextElement ()).getCampoTXT ().estaPreenchido ());
	return true;
      }
    while (false);
    return false;
  }
  
  public boolean opcional ()
  {
    return fOpcional;
  }
  
  public CampoTXT fieldByName (String nomeCampo) throws GeracaoTxtException
  {
    CampoReg campoReg = null;
    Enumeration enumTemp = objListaCampos.elements ();
  while_176_:
    do
      {
	do
	  {
	    if (! enumTemp.hasMoreElements ())
	      break while_176_;
	    campoReg = (CampoReg) enumTemp.nextElement ();
	  }
	while (! campoReg.getCampoTXT ().getNome ().equalsIgnoreCase (nomeCampo));
	return campoReg.getCampoTXT ();
      }
    while (false);
    if (campoReg == null)
      throw new GeracaoTxtException ("Campo n\u00e3o encontrado: " + nomeCampo + " no Registro tipo " + fTipo + ".");
    return null;
  }
  
  public void calculaCRCRegistro () throws GeracaoTxtException
  {
    PLong pLong = new PLong ();
    Crc32 crc32 = new Crc32 ();
    StringBuffer linha = new StringBuffer ("");
    linha.append (UtilitariosString.retiraCaracteresEspeciais (getLinha ().substring (0, getLinha ().length () - 10)));
    long hash = crc32.CalcCrc32 (linha.toString (), linha.toString ().length (), pLong);
    fieldByName ("NR_CONTROLE").set (crc32.getStrCrc32 ());
  }
  
  public String calculaCRCHeader (String filename) throws GeracaoTxtException
  {
    PLong pLong = new PLong ();
    Crc32 crc32 = new Crc32 ();
    StringBuffer linha = new StringBuffer (filename);
    linha.append (UtilitariosString.retiraCaracteresEspeciais (getLinha ().substring (0, getLinha ().length () - 10)));
    long hash = crc32.CalcCrc32 (linha.toString (), linha.length (), pLong);
    return crc32.getStrCrc32 ();
  }
  
  public void validar () throws GeracaoTxtException
  {
    String hashLido = fieldByName ("NR_CONTROLE").asString ();
    calculaCRCRegistro ();
    if (! hashLido.equals (fieldByName ("NR_CONTROLE").asString ()))
      throw new GeracaoTxtException ("N\u00famero de controle inv\u00e1lido no registro " + getTipo ());
  }
  
  public static void main (String[] args)
  {
    /* empty */
  }
  
  public boolean isParticipaGravacao ()
  {
    return participaGravacao;
  }
  
  public void setParticipaGravacao (boolean participaGravacao)
  {
    this.participaGravacao = participaGravacao;
  }
  
  public boolean isParticipaImportacao ()
  {
    return participaImportacao;
  }
  
  public void setParticipaImportacao (boolean participaImportacao)
  {
    this.participaImportacao = participaImportacao;
  }
}
