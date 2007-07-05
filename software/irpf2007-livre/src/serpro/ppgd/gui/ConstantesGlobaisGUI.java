/* ConstantesGlobaisGUI - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public class ConstantesGlobaisGUI
{
  public static final ImageIcon ICO_AVANCAR;
  public static final ImageIcon ICO_AVANCAR_PRES;
  public static final ImageIcon ICO_AVANCAR_SEL;
  public static final ImageIcon ICO_INFORMA;
  public static final ImageIcon ICO_INFORMA_PRES;
  public static final ImageIcon ICO_INFORMA_SEL;
  public static final ImageIcon ICO_PLAY;
  public static final ImageIcon ICO_PLAY_PRES;
  public static final ImageIcon ICO_PLAY_SEL;
  public static final ImageIcon ICO_STOP;
  public static final ImageIcon ICO_STOP_PRES;
  public static final ImageIcon ICO_STOP_SEL;
  public static final ImageIcon ICO_VAZIO;
  public static final ImageIcon ICO_ERRO;
  public static final ImageIcon ICO_AVISO;
  public static final ImageIcon ICO_ATENCAO;
  public static final ImageIcon ICO_VISUALIZAR;
  public static final ImageIcon ICO_VISUALIZAR_SEL;
  public static final ImageIcon ICO_VISUALIZAR_PRES;
  public static final int TAM_TELA = 800;
  public static final int ALT_TELA = 600;
  public static final int ALT_BARRA_TITULO = 31;
  public static final int AJUSTE_TAM_TELA = 12;
  public static final int ALT_AREA_CABECALHO = 85;
  public static final int ALT_AREA_RODAPE = 85;
  public static final int TAM_AREA_FERRAMENTAS = 120;
  public static final int ALT_AREA_NAVEGADOR = 569;
  public static final int ALT_AREA_APLICACAO = 399;
  public static final int ALT_AREA_FERRAMENTAS = 284;
  public static final int TAM_AREA_UTILITARIOS = 120;
  public static final int ALT_AREA_UTILITARIOS = 284;
  public static final String NOME_JANELA_PRINCIPAL = "PPGD_JANELA_PRINCIPAL";
  public static final Dimension HGAP2;
  public static final Dimension VGAP2;
  public static final Dimension HGAP5;
  public static final Dimension VGAP5;
  public static final Dimension HGAP10;
  public static final Dimension VGAP10;
  public static final Dimension HGAP15;
  public static final Dimension VGAP15;
  public static final Dimension HGAP20;
  public static final Dimension VGAP20;
  public static final Dimension HGAP25;
  public static final Dimension VGAP25;
  public static final Dimension HGAP30;
  public static final Dimension HGAP35;
  public static final Dimension VGAP30;
  public static final Dimension HGAP55;
  public static final Dimension VGAP80;
  public static final Dimension HGAP100;
  public static final Dimension VGAP100;
  public static final Dimension HGAP200;
  public static final Dimension VGAP200;
  public static final Dimension HGAP230;
  public static final Dimension HGAP250;
  public static final int MULTIPLICADOR_COMPRIMENTO = 10;
  public static final int ALTURA_CONTROLES = 20;
  public static final Color COR_BRANCO;
  public static final Color COR_PRETO;
  public static final Color COR_VERMELHO;
  public static final Color COR_AMARELO;
  public static final Color COR_CINZA;
  public static final Color COR_CINZA_LINHAS;
  public static final Color COR_AZUL_CLARO;
  public static final Color COR_AZUL_MEDIO;
  public static final Color COR_AZUL_ESCURO;
  public static final Color COR_VERDE;
  public static final Color COR_LARANJA;
  public static final Color COR_CINZA_CLARO;
  public static final String COR_HTML_AZUL_SUBLINHADO = "<FONT COLOR=#336699><u>";
  public static final String COR_HTML_BLACK = "<FONT COLOR=black>";
  public static final Font FONTE_8_NORMAL;
  public static final Font FONTE_9_NORMAL;
  public static final Font FONTE_10_NORMAL;
  public static final Font FONTE_9_BOLD;
  public static final Font FONTE_10_BOLD;
  public static final Font FONTE_11_NORMAL;
  public static final Font FONTE_11_BOLD;
  public static final Font FONTE_15_BOLD;
  public static final Font FONTE_18_BOLD;
  public static final String FONTE_HTML_AZUL_PEQUENA = "<FONT FACE=Verdana COLOR=#336699 SIZE=1>";
  public static final String FONTE_HTML_AZUL = "<FONT FACE=Verdana COLOR=#336699 SIZE=2>";
  public static final String FONTE_HTML_LARANJA = "<FONT FACE=Verdana COLOR=#FFFF63 SIZE=2>";
  public static final String FONTE_HTML_VERDE = "<FONT FACE=Verdana COLOR=#008080 SIZE=2>";
  public static final String FONTE_HTML_AZUL_GRANDE = "<FONT FACE=Verdana COLOR=#336699 SIZE=4>";
  public static final String FONTE_HTML_BLACK = "<FONT FACE=Verdana COLOR=black SIZE=2>";
  public static final Border BORDA_VAZIA;
  public static final Border BORDA_BOTAO;
  public static final Border BORDA_COMUM;
  public static final Border BORDA_GROSSA;
  public static final Border BORDA_EDIT_CAMPO;
  public static final Border BORDA_LINHA_RODAPE;
  public static final Border BORDA_LINHA_TOPO;
  public static final Border BORDA_LINHA_VERTICAL;
  public static final String LABEL_ERRO_ABRIR_DECL = "N\u00e3o foi poss\u00edvel realizar a opera\u00e7\u00e3o requerida (arquivo da declara\u00e7\u00e3o corrompido).\nFavor restaurar a copia de seguran\u00e7a da declara\u00e7\u00e3o.";
  public static final ImageIcon ICO_FECHARDICAS;
  public static final ImageIcon ICO_FECHARDICASDESABILITADO;
  public static Color COR_BORDA_PAINEL_DICAS_AVISO;
  public static Color COR_TITULO_PAINEL_DICAS_AVISO;
  public static Color COR_BORDA_PAINEL_DICAS_ERRO;
  public static Color COR_TITULO_PAINEL_DICAS_ERRO;
  public static Color COR_BORDA_PAINEL_DICAS_ATENCAO;
  public static Color COR_TITULO_PAINEL_DICAS_ATENCAO;
  public static Color COR_BORDA_PAINEL_DICAS_DEFAULT;
  public static Color COR_TITULO_PAINEL_DICAS_DEFAULT;
  public static Color COR_BORDA_PAINEL_DICAS_ERRO_IMPEDITIVO;
  public static Color COR_TITULO_PAINEL_DICAS_ERRO_IMPEDITIVO;
  public static Color COR_BORDA_PAINEL_DICAS_ERRO_OK_CANCELAR;
  public static Color COR_TITULO_PAINEL_DICAS_ERRO_OK_CANCELAR;
  
  static
  {
    Class var_class = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon = new ImageIcon (var_class.getResource ("/imagens/bot_avancar.gif"));
    ICO_AVANCAR = imageicon;
    Class var_class_2_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_1_ = new ImageIcon (var_class_2_.getResource ("/imagens/bot_avancarpressionado.gif"));
    ICO_AVANCAR_PRES = imageicon_1_;
    Class var_class_5_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_4_ = new ImageIcon (var_class_5_.getResource ("/imagens/bot_avancarselecionado.gif"));
    ICO_AVANCAR_SEL = imageicon_4_;
    Class var_class_8_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_7_ = new ImageIcon (var_class_8_.getResource ("/imagens/bot_informa.gif"));
    ICO_INFORMA = imageicon_7_;
    Class var_class_11_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_10_ = new ImageIcon (var_class_11_.getResource ("/imagens/bot_informapressionado.gif"));
    ICO_INFORMA_PRES = imageicon_10_;
    Class var_class_14_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_13_ = new ImageIcon (var_class_14_.getResource ("/imagens/bot_informaselecionado.gif"));
    ICO_INFORMA_SEL = imageicon_13_;
    Class var_class_17_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_16_ = new ImageIcon (var_class_17_.getResource ("/imagens/bot_play.gif"));
    ICO_PLAY = imageicon_16_;
    Class var_class_20_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_19_ = new ImageIcon (var_class_20_.getResource ("/imagens/bot_playpressionado.gif"));
    ICO_PLAY_PRES = imageicon_19_;
    Class var_class_23_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_22_ = new ImageIcon (var_class_23_.getResource ("/imagens/bot_playselecionado.gif"));
    ICO_PLAY_SEL = imageicon_22_;
    Class var_class_26_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_25_ = new ImageIcon (var_class_26_.getResource ("/imagens/bot_stop.gif"));
    ICO_STOP = imageicon_25_;
    Class var_class_29_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_28_ = new ImageIcon (var_class_29_.getResource ("/imagens/bot_stoppressionado.gif"));
    ICO_STOP_PRES = imageicon_28_;
    Class var_class_32_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_31_ = new ImageIcon (var_class_32_.getResource ("/imagens/bot_stopselecionado.gif"));
    ICO_STOP_SEL = imageicon_31_;
    Class var_class_35_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_34_ = new ImageIcon (var_class_35_.getResource ("/imagens/ico_vazio.gif"));
    ICO_VAZIO = imageicon_34_;
    Class var_class_38_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_37_ = new ImageIcon (var_class_38_.getResource (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.iconeerro", "/imagens/ico_erro.gif")));
    ICO_ERRO = imageicon_37_;
    Class var_class_41_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_40_ = new ImageIcon (var_class_41_.getResource (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.iconeaviso", "/imagens/ico_aviso.gif")));
    ICO_AVISO = imageicon_40_;
    Class var_class_44_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_43_ = new ImageIcon (var_class_44_.getResource (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.iconeatencao", "/imagens/ico_atencao.png")));
    ICO_ATENCAO = imageicon_43_;
    Class var_class_47_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_46_ = new ImageIcon (var_class_47_.getResource ("/imagens/bot_view.gif"));
    ICO_VISUALIZAR = imageicon_46_;
    Class var_class_50_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_49_ = new ImageIcon (var_class_50_.getResource ("/imagens/bot_viewselecionado.gif"));
    ICO_VISUALIZAR_SEL = imageicon_49_;
    Class var_class_53_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_52_ = new ImageIcon (var_class_53_.getResource ("/imagens/bot_viewpressionado.gif"));
    ICO_VISUALIZAR_PRES = imageicon_52_;
    HGAP2 = new Dimension (2, 1);
    VGAP2 = new Dimension (1, 2);
    HGAP5 = new Dimension (5, 1);
    VGAP5 = new Dimension (1, 5);
    HGAP10 = new Dimension (10, 1);
    VGAP10 = new Dimension (1, 10);
    HGAP15 = new Dimension (15, 1);
    VGAP15 = new Dimension (1, 15);
    HGAP20 = new Dimension (20, 1);
    VGAP20 = new Dimension (1, 20);
    HGAP25 = new Dimension (25, 1);
    VGAP25 = new Dimension (1, 25);
    HGAP30 = new Dimension (30, 1);
    HGAP35 = new Dimension (35, 1);
    VGAP30 = new Dimension (1, 30);
    HGAP55 = new Dimension (55, 1);
    VGAP80 = new Dimension (1, 80);
    HGAP100 = new Dimension (100, 1);
    VGAP100 = new Dimension (1, 100);
    HGAP200 = new Dimension (200, 1);
    VGAP200 = new Dimension (1, 200);
    HGAP230 = new Dimension (230, 1);
    HGAP250 = new Dimension (250, 1);
    COR_BRANCO = Color.white;
    COR_PRETO = Color.black;
    COR_VERMELHO = new Color (16724787);
    COR_AMARELO = Color.yellow;
    COR_CINZA = new Color (11184810);
    COR_CINZA_LINHAS = new Color (13421772);
    COR_AZUL_CLARO = new Color (39372);
    COR_AZUL_MEDIO = new Color (3368601);
    COR_AZUL_ESCURO = new Color (0.0F, 0.0F, 0.6F);
    COR_VERDE = new Color (32896);
    COR_LARANJA = new Color (16777059);
    COR_CINZA_CLARO = new Color (12303291);
    FONTE_8_NORMAL = new Font ("Verdana", 0, 8);
    FONTE_9_NORMAL = new Font ("Verdana", 0, 9);
    FONTE_10_NORMAL = new Font ("Verdana", 0, 10);
    FONTE_9_BOLD = new Font ("Verdana", 1, 9);
    FONTE_10_BOLD = new Font ("Verdana", 1, 10);
    FONTE_11_NORMAL = new Font ("Verdana", 0, 11);
    FONTE_11_BOLD = new Font ("Verdana", 1, 11);
    FONTE_15_BOLD = new Font ("Verdana", 1, 15);
    FONTE_18_BOLD = new Font ("Verdana", 1, 18);
    BORDA_VAZIA = BorderFactory.createEmptyBorder ();
    BORDA_BOTAO = BorderFactory.createRaisedBevelBorder ();
    BORDA_COMUM = BorderFactory.createEtchedBorder ();
    BORDA_GROSSA = BorderFactory.createMatteBorder (2, 2, 2, 2, COR_PRETO);
    BORDA_EDIT_CAMPO = BorderFactory.createBevelBorder (0, Color.GRAY, Color.LIGHT_GRAY);
    BORDA_LINHA_RODAPE = BorderFactory.createMatteBorder (0, 0, 1, 0, COR_CINZA_LINHAS);
    BORDA_LINHA_TOPO = BorderFactory.createMatteBorder (1, 0, 0, 0, COR_CINZA_LINHAS);
    BORDA_LINHA_VERTICAL = BorderFactory.createMatteBorder (0, 0, 0, 1, COR_CINZA_LINHAS);
    Class var_class_56_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_55_ = new ImageIcon (var_class_56_.getResource (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.icone_fechardicas", "/imagens/ico_fechardicas.gif")));
    ICO_FECHARDICAS = imageicon_55_;
    Class var_class_59_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
    ImageIcon imageicon_58_ = new ImageIcon (var_class_59_.getResource (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.icone_fechardicas_desabilitado", "/imagens/ico_fechardicasdesabilitado.gif")));
    ICO_FECHARDICASDESABILITADO = imageicon_58_;
    COR_BORDA_PAINEL_DICAS_AVISO = COR_AMARELO;
    COR_TITULO_PAINEL_DICAS_AVISO = COR_PRETO;
    COR_BORDA_PAINEL_DICAS_ERRO = COR_VERMELHO;
    COR_TITULO_PAINEL_DICAS_ERRO = COR_BRANCO;
    COR_BORDA_PAINEL_DICAS_ATENCAO = COR_VERDE;
    COR_TITULO_PAINEL_DICAS_ATENCAO = COR_BRANCO;
    COR_BORDA_PAINEL_DICAS_DEFAULT = COR_AZUL_CLARO;
    COR_TITULO_PAINEL_DICAS_DEFAULT = COR_BRANCO;
    COR_BORDA_PAINEL_DICAS_ERRO_IMPEDITIVO = COR_VERMELHO;
    COR_TITULO_PAINEL_DICAS_ERRO_IMPEDITIVO = COR_BRANCO;
    COR_BORDA_PAINEL_DICAS_ERRO_OK_CANCELAR = COR_AMARELO;
    COR_TITULO_PAINEL_DICAS_ERRO_OK_CANCELAR = COR_PRETO;
    Color corBordaAviso = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corBordaAviso"));
    Color corTituloAviso = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corTituloAviso"));
    Color corBordaErro = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corBordaErro"));
    Color corTituloErro = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corTituloErro"));
    Color corBordaAtencao = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corBordaAtencao"));
    Color corTituloAtencao = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corTituloAtencao"));
    Color corBordaDefault = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corBordaDefault"));
    Color corTituloDefault = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corTituloDefault"));
    Color corBordaErroImpeditivo = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corBordaErroImpeditivo"));
    Color corTituloErroImpeditivo = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corTituloErroImpeditivo"));
    Color corBordaErroOkCancelar = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corBordaErroOkCancelar"));
    Color corTituloErroOkCancelar = getColor (FabricaUtilitarios.getProperties ().getProperty ("ppgd.gui.paineldicas.corTituloErroOkCancelar"));
    COR_BORDA_PAINEL_DICAS_AVISO = corBordaAviso == null ? COR_AMARELO : corBordaAviso;
    COR_TITULO_PAINEL_DICAS_AVISO = corTituloAviso == null ? COR_PRETO : corTituloAviso;
    COR_BORDA_PAINEL_DICAS_ERRO = corBordaErro == null ? COR_VERMELHO : corBordaErro;
    COR_TITULO_PAINEL_DICAS_ERRO = corTituloErro == null ? COR_BRANCO : corTituloErro;
    COR_BORDA_PAINEL_DICAS_ATENCAO = corBordaAtencao == null ? COR_VERDE : corBordaAtencao;
    COR_TITULO_PAINEL_DICAS_ATENCAO = corTituloAtencao == null ? COR_BRANCO : corTituloAtencao;
    COR_BORDA_PAINEL_DICAS_DEFAULT = corBordaDefault == null ? COR_AZUL_CLARO : corBordaDefault;
    COR_TITULO_PAINEL_DICAS_DEFAULT = corTituloDefault == null ? COR_BRANCO : corTituloDefault;
    COR_BORDA_PAINEL_DICAS_ERRO_IMPEDITIVO = corBordaErroImpeditivo == null ? COR_VERMELHO : corBordaErroImpeditivo;
    COR_TITULO_PAINEL_DICAS_ERRO_IMPEDITIVO = corTituloErroImpeditivo == null ? COR_BRANCO : corTituloErroImpeditivo;
    COR_BORDA_PAINEL_DICAS_ERRO_OK_CANCELAR = corBordaErroOkCancelar == null ? COR_AMARELO : corBordaErroOkCancelar;
    COR_TITULO_PAINEL_DICAS_ERRO_OK_CANCELAR = corTituloErroOkCancelar == null ? COR_PRETO : corTituloErroOkCancelar;
  }
  
  public static Color getColor (String def)
  {
    if (def == null || def.trim ().equals (""))
      return null;
    try
      {
	Class var_class = java.awt.Color.class;
	Field field = var_class.getField (def.trim ());
	Class var_class_62_ = java.awt.Color.class;
	if (var_class_62_.equals (field.getType ()) && Modifier.isStatic (field.getModifiers ()))
	  {
	    Field field_64_ = field;
	    Class var_class_65_ = java.awt.Color.class;
	    return (Color) field_64_.get (var_class_65_);
	  }
      }
    catch (NoSuchFieldException nosuchfieldexception)
      {
	/* empty */
      }
    catch (SecurityException securityexception)
      {
	/* empty */
      }
    catch (IllegalAccessException illegalaccessexception)
      {
	/* empty */
      }
    try
      {
	Class var_class = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
	Field field = var_class.getField (def.trim ());
	Class var_class_68_ = java.awt.Color.class;
	if (var_class_68_.equals (field.getType ()) && Modifier.isStatic (field.getModifiers ()))
	  {
	    Field field_70_ = field;
	    Class var_class_71_ = serpro.ppgd.gui.ConstantesGlobaisGUI.class;
	    return (Color) field_70_.get (var_class_71_);
	  }
      }
    catch (NoSuchFieldException nosuchfieldexception)
      {
	/* empty */
      }
    catch (SecurityException securityexception)
      {
	/* empty */
      }
    catch (IllegalAccessException illegalaccessexception)
      {
	/* empty */
      }
    try
      {
	StringTokenizer st = new StringTokenizer (def, ",");
	if (1 == st.countTokens ())
	  {
	    try
	      {
		return new Color (Integer.parseInt (st.nextToken ().trim (), 16));
	      }
	    catch (NumberFormatException e)
	      {
		LogPPGD.erro ("N\u00e3o foi poss\u00edvel decodificar a cor:" + def);
		return null;
	      }
	  }
	int[] para = ia (st);
	if (4 <= para.length)
	  return new Color (para[0], para[1], para[2], para[3]);
	if (3 <= para.length)
	  return new Color (para[0], para[1], para[2]);
	if (1 <= para.length)
	  return new Color (para[0]);
      }
    catch (Exception e)
      {
	LogPPGD.erro ("N\u00e3o foi poss\u00edvel decodificar a cor:" + def);
      }
    return null;
  }
  
  static int[] ia (StringTokenizer st)
  {
    int size = st != null ? st.countTokens () : 0;
    int[] a = new int[size];
    int i = 0;
    while (st != null && st.hasMoreTokens ())
      {
	try
	  {
	    a[i] = Integer.parseInt (st.nextToken ().trim ());
	    i++;
	  }
	catch (NumberFormatException numberformatexception)
	  {
	    /* empty */
	  }
      }
    int[] b = new int[i];
    System.arraycopy (a, 0, b, 0, i);
    return b;
  }
}
