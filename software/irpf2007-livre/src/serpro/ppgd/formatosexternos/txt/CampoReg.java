/* CampoReg - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.formatosexternos.txt;
import serpro.ppgd.formatosexternos.txt.excecao.GeracaoTxtException;

public class CampoReg
{
  private int posInicio;
  private CampoTXT campoTXT;
  private String atributoObjetoNegocio = null;
  private String conteudoEstatico = "";
  private String metodoGravacao = "";
  private String metodoRestauracao = "";
  private boolean participaImportacao = true;
  private boolean participaGravacao = true;
  
  public CampoReg (String nome, String tipoTamanho) throws GeracaoTxtException
  {
    campoTXT = new CampoTXT (nome, tipoTamanho);
    posInicio = 0;
  }
  
  public CampoReg (String nome, String tipoTamanho, boolean automatico) throws GeracaoTxtException
  {
    campoTXT = new CampoTXT (nome, tipoTamanho, automatico);
    posInicio = 0;
  }
  
  public CampoTXT getCampoTXT ()
  {
    return campoTXT;
  }
  
  public int getPosicaoInicial ()
  {
    return posInicio;
  }
  
  public int getPosicaoFinal ()
  {
    return posInicio + campoTXT.getTamanho () - 1;
  }
  
  public void setCampoTXT (CampoTXT campoTXT)
  {
    this.campoTXT = campoTXT;
  }
  
  public void setPosicaoInicial (int i)
  {
    posInicio = i;
  }
  
  public String getAtributoObjetoNegocio ()
  {
    return getCampoTXT ().getAtributoObjetoNegocio ();
  }
  
  public void setAtributoObjetoNegocio (String acessoObjetoNegocio)
  {
    getCampoTXT ().setAtributoObjetoNegocio (acessoObjetoNegocio);
  }
  
  public String getConteudoEstatico ()
  {
    return conteudoEstatico;
  }
  
  public void setConteudoEstatico (String conteudoConstante)
  {
    conteudoEstatico = conteudoConstante;
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
