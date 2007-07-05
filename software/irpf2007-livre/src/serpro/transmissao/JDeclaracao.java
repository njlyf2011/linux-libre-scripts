/* JDeclaracao - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.transmissao;
import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class JDeclaracao
{
  public String m_strNome;
  public String m_strNI;
  public String m_strlblNome;
  public String m_strlblNI;
  public String m_strPrograma;
  public String m_strNIFormatado;
  public String m_strControleSRF;
  public String m_strUserSenha;
  private String m_strErro;
  private Window m_pParentFrame;
  private JTipoDeclaracao[] m_TiposDeclaracao;
  private int m_nQtdeTiposDeclaracao;
  private String m_strRecibo;
  
  public JDeclaracao ()
  {
    InitTiposDeclaracao ();
  }
  
  public JDeclaracao (JFrame jframe)
  {
    m_pParentFrame = jframe;
    InitTiposDeclaracao ();
  }
  
  private void InitTiposDeclaracao ()
  {
    m_nQtdeTiposDeclaracao = 1;
    m_TiposDeclaracao = new JTipoDeclaracao[m_nQtdeTiposDeclaracao];
    m_TiposDeclaracao[0] = new JTipoDeclaracao ("IRPF    2005", 1500, "Nome do contribuinte", "CPF", "2004", "999.999.999-99");
  }
  
  public int Identificar (String string)
  {
    File file = new File (JUtil.NOME_ARQUIVO_CERTIFICADO);
    if (! file.exists ())
      {
	m_strErro = "O arquivo contendo o certificado digital utilizado pelo Receitanet\n n\u00e3o foi encontrado!\nPara funcionar adequadamente, o Receitanet deve ser reinstalado.";
	return 1;
      }
    System.setProperty ("javax.net.ssl.trustStore", JUtil.NOME_ARQUIVO_CERTIFICADO);
    System.setProperty ("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
    if (string.length () == 0)
      {
	m_strErro = "ERRO!\nO nome do arquivo declara\u00e7\u00e3o n\u00e3o foi informado.";
	return 1;
      }
    if (! JUtil.FileExists (string))
      {
	m_strErro = "ERRO!\nO arquivo declara\u00e7\u00e3o \"" + string + "\" n\u00e3o existe.";
	return 1;
      }
    if (! JUtil.IsFile (string))
      {
	m_strErro = "ERRO!\nO arquivo selecionado n\u00e3o \u00e9 um arquivo declara\u00e7\u00e3o reconhecido por esta vers\u00e3o do Receitanet.";
	return 1;
      }
    int i = string.length ();
    String string_0_ = string.substring (i - 3, i);
    string_0_ = string_0_.toUpperCase ();
    if (string_0_.compareTo ("REC") == 0)
      {
	m_strErro = "ERRO!\nO arquivo selecionado \u00e9 um arquivo de recibo de entrega.\nPor favor, selecione o arquivo declara\u00e7\u00e3o correspondente (extens\u00e3o \".DEC\").";
	return 1;
      }
    if (string_0_.compareTo ("DBK") == 0)
      {
	m_strErro = "ERRO!\nA DECLARA\u00c7\u00c3O N\u00c3O FOI TRANSMITIDA.\nO arquivo selecionado \u00e9 uma c\u00f3pia de seguran\u00e7a.\nPor favor, gere novamente sua declara\u00e7\u00e3o usando o programa gerador fornecido pela Secretaria da Receita Federal.";
	return 1;
      }
    if (string_0_.compareTo ("DEC") != 0)
      {
	m_strErro = "ERRO!\nO arquivo selecionado n\u00e3o \u00e9 um arquivo declara\u00e7\u00e3o reconhecido por esta vers\u00e3o do Receitanet.";
	return 1;
      }
    Object object = null;
    RandomAccessFile randomaccessfile;
    try
      {
	randomaccessfile = new RandomAccessFile (string, "r");
      }
    catch (FileNotFoundException filenotfoundexception)
      {
	m_strErro = "ERRO!\nO arquivo " + string + " n\u00e3o foi encontrado.";
	return 1;
      }
    String string_1_ = "";
    try
      {
	string_1_ = randomaccessfile.readLine ();
	randomaccessfile.close ();
      }
    catch (IOException ioexception)
      {
	m_strErro = "Ocorreu um erro durante a leitura do arquivo declara\u00e7\u00e3o " + string + "." + ioexception.getMessage ();
	return 1;
      }
    if (! SetValuesIdentificacao (string_1_))
      return 1;
    if (! ValidaNomeArquivo (string))
      {
	m_strErro = "ERRO!\nO arquivo selecionado n\u00e3o \u00e9 um arquivo de declara\u00e7\u00e3o v\u00e1lido.\nPor favor, gere novamente sua declara\u00e7\u00e3o usando o programa gerador fornecido pela Secretaria da Receita Federal.";
	return 1;
      }
    return 0;
  }
  
  public boolean Transmitir (String string)
  {
    JTransnetDlg jtransnetdlg;
    if (m_pParentFrame instanceof JDialog)
      jtransnetdlg = new JTransnetDlg ((JDialog) m_pParentFrame, string, m_strUserSenha);
    else
      jtransnetdlg = new JTransnetDlg ((JFrame) m_pParentFrame, string, m_strUserSenha);
    jtransnetdlg.setVisible (true);
    if (jtransnetdlg.getRetCode () == 0)
      {
	m_strErro = jtransnetdlg.getLastError ();
	return false;
      }
    m_strRecibo = jtransnetdlg.getRecibo ();
    if (! GravaRecibo (string, m_strRecibo))
      return false;
    return true;
  }
  
  public int EnviarDeclaracao (Window window, JParametrosNet jparametrosnet, String string)
  {
    JUtil.NOME_ARQUIVO_CERTIFICADO = string;
    return EnviarDeclaracao (window, jparametrosnet);
  }
  
  public int EnviarDeclaracao (Window window, JParametrosNet jparametrosnet)
  {
    m_pParentFrame = window;
    if (jparametrosnet.m_strNomeArqDeclaracao.length () == 0)
      {
	jparametrosnet.m_strMensagem = "ERRO!\nO nome do arquivo declara\u00e7\u00e3o a ser transmitido n\u00e3o foi passado corretamente para o m\u00f3dulo de transmiss\u00e3o.";
	return 1;
      }
    int i = Identificar (jparametrosnet.m_strNomeArqDeclaracao);
    if (i != 0)
      {
	jparametrosnet.m_strMensagem = m_strErro;
	return i;
      }
    if (! Transmitir (jparametrosnet.m_strNomeArqDeclaracao))
      {
	jparametrosnet.m_strMensagem = m_strErro;
	return 1;
      }
    if (m_strRecibo.indexOf ("\nNC") != -1 || m_strRecibo.indexOf ("\rNC") != -1)
      {
	jparametrosnet.m_strMensagem = "Esta declara\u00e7\u00e3o" + m_strNI + "foi transmitida com sucesso, no entanto foi entregue fora do prazo e ensejou aplica\u00e7\u00e3o de multa. Imprima o Recibo de Entrega e Notifica\u00e7\u00e3o de Lan\u00e7amento acionando a op\u00e7\u00e3o correspondente no programa gerador da declara\u00e7\u00e3o.";
	return 2;
      }
    jparametrosnet.m_strMensagem = "Declara\u00e7\u00e3o transmitida com sucesso.";
    return 0;
  }
  
  public String GetLastError ()
  {
    return m_strErro;
  }
  
  private int PesquisaDeclaracao (String string)
  {
    int i = -1;
    for (int i_2_ = 0; i_2_ < m_nQtdeTiposDeclaracao; i_2_++)
      {
	if (string.compareTo (m_TiposDeclaracao[i_2_].m_strIDDeclaracao) == 0)
	  {
	    i = i_2_;
	    break;
	  }
      }
    return i;
  }
  
  private boolean ValidaNomeArquivo (String string)
  {
    File file = new File (string);
    String string_3_ = file.getName ().substring (0, 8);
    for (int i = 0; i < 8 && string_3_.charAt (i) != '~'; i++)
      {
	if (string_3_.charAt (i) != m_strNI.charAt (i))
	  return false;
      }
    return true;
  }
  
  private boolean SetValuesIdentificacao (String string)
  {
    String string_4_ = string.substring (0, 12);
    int i = PesquisaDeclaracao (string_4_);
    if (i == -1)
      {
	m_strErro = "O arquivo selecionado n\u00e3o \u00e9 um arquivo de declara\u00e7\u00e3o v\u00e1lido ou o esta declara\u00e7\u00e3o n\u00e3o \u00e9 reconhecida por esta vers\u00e3o do programa Receitanet.";
	return false;
      }
    String string_5_ = string.substring (12, 16);
    if (m_TiposDeclaracao[i].m_iTipoFormulario != 9999 && string_5_.compareTo (m_TiposDeclaracao[i].m_strExercicio) != 0)
      {
	m_strErro = "O arquivo selecionado n\u00e3o \u00e9 um arquivo de declara\u00e7\u00e3o v\u00e1lido.\nPor favor, gere novamente sua declara\u00e7\u00e3o usando o programa gerador fornecido pela Secretaria da Receita Federal.";
	return false;
      }
    int i_6_ = Integer.parseInt (string.substring (16, 20));
    if (i_6_ != m_TiposDeclaracao[i].m_iTipoFormulario)
      {
	m_strErro = "O arquivo selecionado n\u00e3o \u00e9 um arquivo de declara\u00e7\u00e3o v\u00e1lido.\nPor favor, gere novamente sua declara\u00e7\u00e3o usando o programa gerador fornecido pela Secretaria da Receita Federal.";
	return false;
      }
    String string_7_ = string.substring (0, 7);
    string_7_ = string_7_.trim ();
    String string_8_ = string.substring (8, 12);
    string_8_ = string_8_.trim ();
    m_strNI = string.substring (21, 35);
    m_strNI = m_strNI.trim ();
    m_strNome = string.substring (39, 99);
    m_strNome = m_strNome.trim ();
    m_strControleSRF = string.substring (101, 111);
    m_strControleSRF = FormatNI (m_strControleSRF, "99.99.99.99.99");
    if (m_strControleSRF.length () == 0)
      {
	m_strErro = "O arquivo selecionado n\u00e3o \u00e9 um arquivo de declara\u00e7\u00e3o v\u00e1lido.\nPor favor, gere novamente sua declara\u00e7\u00e3o usando o programa gerador fornecido pela Secretaria da Receita Federal.";
	return false;
      }
    m_strNIFormatado = FormatNI (m_strNI, m_TiposDeclaracao[i].m_strFormatoNI);
    m_strlblNome = m_TiposDeclaracao[i].m_strLabelNome;
    m_strlblNI = m_TiposDeclaracao[i].m_strLabelNI;
    m_strPrograma = string_7_ + " " + string_8_ + " Ano " + string_5_;
    return true;
  }
  
  private String FormatNI (String string, String string_9_)
  {
    int i = 0;
    int i_10_ = 0;
    String string_11_ = "";
    for (/**/; i < string_9_.length (); i++)
      {
	if (string_9_.charAt (i) == '9')
	  string_11_ += string.charAt (i_10_++);
	else
	  string_11_ += string_9_.charAt (i);
      }
    return string_11_;
  }
  
  private boolean GravaRecibo (String string, String string_12_)
  {
    File file = new File (string);
    String string_13_ = file.getParent ();
    String string_14_;
    try
      {
	Class.forName ("receitanet.ReceitanetApp");
	if (string_13_.substring (string_13_.length () - 1).compareTo ("\\") != 0)
	  string_14_ = string_13_ + "\\" + m_strNI.substring (0, 8) + ".REC";
	else
	  string_14_ = file.getParent () + m_strNI.substring (0, 8) + ".REC";
      }
    catch (Exception exception)
      {
	string_14_ = string.substring (0, string.length () - 4) + ".REC";
      }
    try
      {
	Object object = null;
	RandomAccessFile randomaccessfile = new RandomAccessFile (string_14_, "rw");
	randomaccessfile.writeBytes (string_12_);
	randomaccessfile.close ();
      }
    catch (Exception exception)
      {
	m_strErro = "Ocorreu um erro na grava\u00e7\u00e3o do recibo de entrega da declara\u00e7\u00e3o.";
	StringBuffer stringbuffer = new StringBuffer ();
	JDeclaracao jdeclaracao_15_ = this;
	jdeclaracao_15_.m_strErro = stringbuffer.append (jdeclaracao_15_.m_strErro).append ("\n\n").toString ();
	StringBuffer stringbuffer_16_ = new StringBuffer ();
	JDeclaracao jdeclaracao_17_ = this;
	jdeclaracao_17_.m_strErro = stringbuffer_16_.append (jdeclaracao_17_.m_strErro).append (exception.getMessage ()).toString ();
	StringBuffer stringbuffer_18_ = new StringBuffer ();
	JDeclaracao jdeclaracao_19_ = this;
	jdeclaracao_19_.m_strErro = stringbuffer_18_.append (jdeclaracao_19_.m_strErro).append ("\n\n").toString ();
	StringBuffer stringbuffer_20_ = new StringBuffer ();
	JDeclaracao jdeclaracao_21_ = this;
	jdeclaracao_21_.m_strErro = stringbuffer_20_.append (jdeclaracao_21_.m_strErro).append ("Para obter o recibo de entrega desta declara\u00e7\u00e3o transmita-a novamente.").toString ();
	return false;
      }
    return true;
  }
}
