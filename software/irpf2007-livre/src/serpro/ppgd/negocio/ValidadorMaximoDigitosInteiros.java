/* ValidadorMaximoDigitosInteiros - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.negocio;

class ValidadorMaximoDigitosInteiros extends ValidadorDefault
{
  public ValidadorMaximoDigitosInteiros (byte severidade)
  {
    super (severidade);
  }
  
  public RetornoValidacao validarImplementado ()
  {
    Valor val = (Valor) getInformacao ();
    if (val.getMaximoDigitosParteInteira () < val.getParteInteira ().length ())
      {
	setSeveridade (val.getSeveridadeValidacaoMaximoDigitos ());
	setMensagemValidacao (val.getMsgErroEstourodigitos ());
	RetornoValidacao retorno = new RetornoValidacao (val.getMsgErroEstourodigitos (), val.getSeveridadeValidacaoMaximoDigitos ());
	return retorno;
      }
    return null;
  }
}
