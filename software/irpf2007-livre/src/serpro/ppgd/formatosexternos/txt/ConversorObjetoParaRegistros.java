/* ConversorObjetoParaRegistros - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import java.util.Iterator;
import java.util.Vector;

import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;
import serpro.ppgd.negocio.Codigo;
import serpro.ppgd.negocio.Colecao;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class ConversorObjetoParaRegistros
{
  
  public void preencheDocumentoTXT (ObjetoNegocio pObj, DocumentoTXT pDoc) throws GeracaoTxtException
  {
    pDoc.clear ();
    Iterator itTipoRegsArquivo = MapeamentoTXT.getInstance ().getTiposDeRegistrosArquivo (pDoc.getTipoArquivo ()).iterator ();
    while (itTipoRegsArquivo.hasNext ())
      {
	String tipoRegAtual = (String) itTipoRegsArquivo.next ();
	String colecaoRegistros = MapeamentoTXT.getInstance ().getColecaoRegistroMultipo (pDoc.getTipoArquivo (), tipoRegAtual);
	Colecao col = null;
	if (colecaoRegistros.trim ().length () != 0)
	  {
	    if (colecaoRegistros.toUpperCase ().equals ("THIS"))
	      col = (Colecao) pObj;
	    else
	      {
		int posicaoLasanha = colecaoRegistros.indexOf ("#");
		if (posicaoLasanha == -1)
		  col = (Colecao) FabricaUtilitarios.obtemAtributo (pObj, colecaoRegistros);
		else
		  {
		    ObjetoNegocio obj = null;
		    if (posicaoLasanha == 0)
		      obj = pObj;
		    else
		      obj = (ObjetoNegocio) FabricaUtilitarios.obtemAtributo (pObj, colecaoRegistros.substring (0, posicaoLasanha));
		    String nomeMetodo = colecaoRegistros.substring (posicaoLasanha + 1, colecaoRegistros.length ());
		    col = (Colecao) FabricaUtilitarios.invocaMetodo (nomeMetodo, obj, new Class[0], new Object[0]);
		  }
	      }
	    col.excluirRegistrosEmBranco ();
	    setaRegistroMultiplo (col, tipoRegAtual, pObj, pDoc);
	  }
	else
	  setaRegistroComum (tipoRegAtual, pObj, pDoc);
      }
  }
  
  protected boolean antesDeSetarRegistro (ObjetoNegocio pObj, RegistroTxt pReg, DocumentoTXT pDoc) throws GeracaoTxtException
  {
    return true;
  }
  
  private void setaRegistroComum (String pTipoReg, ObjetoNegocio pObj, DocumentoTXT pDoc) throws GeracaoTxtException
  {
    RegistroTxt reg = new RegistroTxt (pDoc.getTipoArquivo (), pTipoReg);
    Vector listaReg = new Vector ();
    if (reg.isParticipaGravacao () && antesDeSetarRegistro (pObj, reg, pDoc))
      {
	Iterator itCampos = reg.objListaCampos.iterator ();
	while (itCampos.hasNext ())
	  {
	    CampoReg campoAtual = (CampoReg) itCampos.next ();
	    if (campoAtual.isParticipaGravacao ())
	      {
		if (campoAtual.getConteudoEstatico () != null && campoAtual.getConteudoEstatico ().trim ().length () != 0)
		  campoAtual.getCampoTXT ().set (campoAtual.getConteudoEstatico ());
		else if (campoAtual.getAtributoObjetoNegocio ().trim ().length () != 0)
		  {
		    String atributoObjetoNegocio = campoAtual.getAtributoObjetoNegocio ();
		    int posicaoLasanha = atributoObjetoNegocio.indexOf ("#");
		    if (posicaoLasanha == -1)
		      {
			Informacao info = (Informacao) FabricaUtilitarios.obtemAtributo (pObj, atributoObjetoNegocio);
			setaCampoTXT (campoAtual.getCampoTXT (), info);
		      }
		    else
		      {
			ObjetoNegocio obj = null;
			if (posicaoLasanha == 0)
			  obj = pObj;
			else
			  obj = (ObjetoNegocio) FabricaUtilitarios.obtemAtributo (pObj, atributoObjetoNegocio.substring (0, posicaoLasanha));
			String nomeMetodo = atributoObjetoNegocio.substring (posicaoLasanha + 1, atributoObjetoNegocio.length ());
			Object retornoMetodo = FabricaUtilitarios.invocaMetodo (nomeMetodo, obj, new Class[] { java.lang.String.class }, new Object[] { null });
			if (retornoMetodo != null)
			  campoAtual.getCampoTXT ().set (retornoMetodo.toString ());
		      }
		  }
		else
		  setaOutrosCampos (pObj, campoAtual.getCampoTXT (), reg);
	      }
	  }
	listaReg.add (reg);
	pDoc.setFicha (listaReg);
      }
  }
  
  protected void setaOutrosCampos (ObjetoNegocio pDec, CampoTXT pCampoTxt, RegistroTxt pReg) throws GeracaoTxtException
  {
    /* empty */
  }
  
  private void setaRegistroMultiplo (Colecao pCol, String pTipoReg, ObjetoNegocio pDec, DocumentoTXT pDoc) throws GeracaoTxtException
  {
    Vector listaReg = new Vector ();
    Iterator itItems = pCol.recuperarLista ().iterator ();
    while (itItems.hasNext ())
      {
	ObjetoNegocio objAtual = (ObjetoNegocio) itItems.next ();
	RegistroTxt reg = new RegistroTxt (pDoc.getTipoArquivo (), pTipoReg);
	if (reg.isParticipaGravacao () && antesDeSetarRegistro (pDec, reg, pDoc))
	  {
	    Iterator itCampos = reg.objListaCampos.iterator ();
	    while (itCampos.hasNext ())
	      {
		CampoReg campoAtual = (CampoReg) itCampos.next ();
		if (campoAtual.isParticipaGravacao ())
		  {
		    if (campoAtual.getConteudoEstatico () != null && campoAtual.getConteudoEstatico ().trim ().length () != 0)
		      campoAtual.getCampoTXT ().set (campoAtual.getConteudoEstatico ());
		    else if (campoAtual.getAtributoObjetoNegocio ().trim ().length () != 0)
		      {
			String atributoObjetoNegocio = campoAtual.getAtributoObjetoNegocio ();
			int posicaoLasanha = atributoObjetoNegocio.indexOf ("#");
			if (posicaoLasanha == -1)
			  {
			    Informacao info = (Informacao) FabricaUtilitarios.obtemAtributo (objAtual, atributoObjetoNegocio);
			    setaCampoTXT (campoAtual.getCampoTXT (), info);
			  }
			else
			  {
			    ObjetoNegocio obj = null;
			    if (posicaoLasanha == 0)
			      obj = objAtual;
			    else
			      obj = (ObjetoNegocio) FabricaUtilitarios.obtemAtributo (objAtual, atributoObjetoNegocio.substring (0, posicaoLasanha));
			    String nomeMetodo = atributoObjetoNegocio.substring (posicaoLasanha + 1, atributoObjetoNegocio.length ());
			    Object retornoMetodo = FabricaUtilitarios.invocaMetodo (nomeMetodo, obj, new Class[] { java.lang.String.class }, new Object[] { null });
			    if (retornoMetodo != null)
			      campoAtual.getCampoTXT ().set (retornoMetodo.toString ());
			  }
		      }
		    else
		      setaOutrosCampos (pDec, campoAtual.getCampoTXT (), reg);
		  }
	      }
	    listaReg.add (reg);
	  }
      }
    pDoc.setFicha (listaReg);
  }
  
  protected void setaCampoTXT (CampoTXT pCampoTxt, Informacao pInfo) throws GeracaoTxtException
  {
    if (! pInfo.isVazio ())
      {
	if (pInfo instanceof Valor)
	  pCampoTxt.set (((Valor) pInfo).asTxt ());
	else if (pInfo instanceof Codigo)
	  pCampoTxt.set (((Codigo) pInfo).getConteudoAtual (0));
	else
	  pCampoTxt.set (pInfo.asString ());
      }
  }
  
}
