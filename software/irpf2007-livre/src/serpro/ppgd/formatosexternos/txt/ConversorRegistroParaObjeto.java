/* ConversorRegistroParaObjeto - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import java.util.Iterator;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.negocio.CNPJ;
import serpro.ppgd.negocio.CPF;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Data;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.NI;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class ConversorRegistroParaObjeto
{
  
  public void preencheObjetoNegocio (ObjetoNegocio pObj, DocumentoTXT pDoc) throws GeracaoTxtException
  {
    Iterator itTipoRegsArquivo = MapeamentoTXT.getInstance ().getTiposDeRegistrosArquivo (pDoc.getTipoArquivo ()).iterator ();
    while (itTipoRegsArquivo.hasNext ())
      {
	String tipoRegAtual = (String) itTipoRegsArquivo.next ();
	Iterator itRegistrosAtuais = pDoc.getRegistrosTxt (tipoRegAtual).iterator ();
	String colecaoRegistros = MapeamentoTXT.getInstance ().getColecaoRegistroMultipo (pDoc.getTipoArquivo (), tipoRegAtual);
	Colecao col = null;
	if (colecaoRegistros.trim ().length () != 0)
	  {
	    if (colecaoRegistros.toUpperCase ().equals ("THIS"))
	      col = (Colecao) pObj;
	    else
	      col = (Colecao) FabricaUtilitarios.obtemAtributo (pObj, colecaoRegistros);
	    col.recuperarLista ().clear ();
	  }
	while (itRegistrosAtuais.hasNext ())
	  {
	    RegistroTxt regAtual = (RegistroTxt) itRegistrosAtuais.next ();
	    if (regAtual.isParticipaImportacao ())
	      {
		antesDeCarregarRegistro (pObj, regAtual);
		if (colecaoRegistros.trim ().length () != 0)
		  preencheRegistroMultiplo (col, regAtual, pObj);
		else
		  preencheRegistroComum (regAtual, pObj);
	      }
	  }
      }
  }
  
  protected void antesDeCarregarRegistro (ObjetoNegocio pDec, RegistroTxt pReg) throws GeracaoTxtException
  {
    /* empty */
  }
  
  protected void preencheRegistroMultiplo (Colecao pCol, RegistroTxt pReg, ObjetoNegocio pObj) throws GeracaoTxtException
  {
    try
      {
	ObjetoNegocio itemColecao = FabricaUtilitarios.instanciaObjetoNegocio (pCol.getTipoItens (), pObj.getIdDeclaracao ());
	Iterator itCampos = pReg.objListaCampos.iterator ();
	while (itCampos.hasNext ())
	  {
	    CampoReg campoAtual = (CampoReg) itCampos.next ();
	    if (campoAtual.isParticipaImportacao ())
	      {
		if (campoAtual.getAtributoObjetoNegocio ().trim ().length () != 0)
		  {
		    String atributoObjetoNegocio = campoAtual.getAtributoObjetoNegocio ().trim ();
		    int posicaoLasanha = atributoObjetoNegocio.indexOf ("#");
		    if (posicaoLasanha == -1)
		      {
			Informacao info = (Informacao) FabricaUtilitarios.obtemAtributo (itemColecao, campoAtual.getAtributoObjetoNegocio ());
			preencheCampoInformacao (campoAtual.getCampoTXT (), info);
		      }
		    else
		      {
			ObjetoNegocio obj = null;
			if (posicaoLasanha == 0)
			  obj = itemColecao;
			else
			  obj = (ObjetoNegocio) FabricaUtilitarios.obtemAtributo (itemColecao, atributoObjetoNegocio.substring (0, posicaoLasanha));
			String nomeMetodo = atributoObjetoNegocio.substring (posicaoLasanha + 1, atributoObjetoNegocio.length ());
			FabricaUtilitarios.invocaMetodo (nomeMetodo, obj, new Class[] { java.lang.String.class }, new Object[] { campoAtual.getCampoTXT ().asString () });
		      }
		  }
		else
		  preencheOutrosCampos (pObj, pReg, campoAtual.getCampoTXT ());
	      }
	  }
	pCol.recuperarLista ().add (itemColecao);
      }
    catch (Exception e)
      {
	throw new GeracaoTxtException (e);
      }
  }
  
  protected void preencheRegistroComum (RegistroTxt pReg, ObjetoNegocio pObj) throws GeracaoTxtException
  {
    Iterator itCampos = pReg.objListaCampos.iterator ();
    while (itCampos.hasNext ())
      {
	CampoReg campoAtual = (CampoReg) itCampos.next ();
	if (! campoAtual.getCampoTXT ().getNome ().equals ("CPF CONTRIBUINTE"))
	  {
	    /* empty */
	  }
	if (campoAtual.isParticipaImportacao ())
	  {
	    if (campoAtual.getAtributoObjetoNegocio ().trim ().length () != 0)
	      {
		String atributoObjetoNegocio = campoAtual.getAtributoObjetoNegocio ().trim ();
		int posicaoLasanha = atributoObjetoNegocio.indexOf ("#");
		if (posicaoLasanha == -1)
		  {
		    Informacao info = (Informacao) FabricaUtilitarios.obtemAtributo (pObj, campoAtual.getAtributoObjetoNegocio ());
		    preencheCampoInformacao (campoAtual.getCampoTXT (), info);
		  }
		else
		  {
		    ObjetoNegocio obj = null;
		    if (posicaoLasanha == 0)
		      obj = pObj;
		    else
		      obj = (ObjetoNegocio) FabricaUtilitarios.obtemAtributo (pObj, atributoObjetoNegocio.substring (0, posicaoLasanha));
		    String nomeMetodo = atributoObjetoNegocio.substring (posicaoLasanha + 1, atributoObjetoNegocio.length ());
		    FabricaUtilitarios.invocaMetodo (nomeMetodo, obj, new Class[] { java.lang.String.class }, new Object[] { campoAtual.getCampoTXT ().asString () });
		  }
	      }
	    else
	      preencheOutrosCampos (pObj, pReg, campoAtual.getCampoTXT ());
	  }
      }
  }
  
  protected void preencheOutrosCampos (ObjetoNegocio pDec, RegistroTxt pReg, CampoTXT pCampoTxt) throws GeracaoTxtException
  {
    /* empty */
  }
  
  protected void preencheCampoInformacao (CampoTXT pCampoTxt, Informacao pInfo)
  {
    if (! pCampoTxt.getNome ().equals ("TIPO LOGRADOURO IMOVEL"))
      {
	/* empty */
      }
    if ((pCampoTxt.getTipo ().equals ("N") || pInfo instanceof NI || pInfo instanceof CPF || pInfo instanceof CNPJ || pInfo instanceof Data) && pCampoTxt.asString ().trim ().length () > 0)
      {
	do
	  {
	    try
	      {
		if (Long.parseLong (pCampoTxt.asString ()) != 0L)
		  break;
	      }
	    catch (Exception e)
	      {
		/* empty */
	      }
	    return;
	  }
	while (false);
      }
    if (pInfo instanceof Valor)
      {
	Valor val = pCampoTxt.asValor ();
	pInfo.setConteudo (val.getConteudoFormatado ());
      }
    else
      pInfo.setConteudo (pCampoTxt.asString ().trim ());
  }
  
}
