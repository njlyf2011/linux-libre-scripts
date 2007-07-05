/* InformacaoConverter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.infraestrutura.converters;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.jdom.Attribute;
import org.swixml.Converter;
import org.swixml.Localizer;

import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.ObjetoNegocio;
import serpro.ppgd.negocio.PPGDFacade;
import serpro.ppgd.negocio.util.FabricaUtilitarios;

public class InformacaoConverter implements Converter
{
  
  public Object convert (Class type, Attribute attr, Localizer localizer) throws Exception
  {
    ObjetoNegocio declaracaoAberta = getDeclaracao ();
    StringTokenizer atributos = new StringTokenizer (attr.getValue (), ".");
    ObjetoNegocio ultimoObjetoNegocioAcessado = declaracaoAberta;
    while (atributos.hasMoreTokens ())
      {
	String atributoAtual = atributos.nextToken ();
	if (atributos.hasMoreTokens ())
	  ultimoObjetoNegocioAcessado = (ObjetoNegocio) FabricaUtilitarios.getValorField (atributoAtual, ultimoObjetoNegocioAcessado);
	else
	  {
	    Informacao informacaoObtida = (Informacao) FabricaUtilitarios.getValorField (atributoAtual, ultimoObjetoNegocioAcessado);
	    return informacaoObtida;
	  }
      }
    return null;
  }
  
  public Class convertsTo ()
  {
    return serpro.ppgd.negocio.Informacao.class;
  }
  
  public ObjetoNegocio getDeclaracao ()
  {
    ObjetoNegocio objetonegocio;
    try
      {
	String classeFacade = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault");
	Class classe = Class.forName (classeFacade);
	Method methGetInstancia = classe.getMethod ("getInstancia", null);
	PPGDFacade facade = (PPGDFacade) methGetInstancia.invoke (null, null);
	Method methGetDeclaracao = classe.getMethod ("getDeclaracao", null);
	objetonegocio = (ObjetoNegocio) methGetInstancia.invoke (facade, null);
      }
    catch (Exception e)
      {
	return null;
      }
    return objetonegocio;
  }
  
}
