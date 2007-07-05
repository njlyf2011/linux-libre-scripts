/* JEditCampo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui.xbeans;
import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import serpro.ppgd.gui.BlinkLabel;
import serpro.ppgd.gui.ConstantesGlobaisGUI;
import serpro.ppgd.gui.FabricaGUI;
import serpro.ppgd.gui.PainelDicasModal;
import serpro.ppgd.gui.UtilitariosGUI;
import serpro.ppgd.gui.pendencia.MapeamentoInformacaoEditCampo;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.PPGDFacade;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.RetornosValidacoes;
import serpro.ppgd.negocio.ValidadorImpeditivoDefault;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public abstract class JEditCampo extends JComponent implements PropertyChangeListener
{
  protected Informacao campo = null;
  private String informacaoAssociada;
  private boolean associaComFacade = true;
  protected JLabel simbolo;
  private BlinkLabel bLabel;
  private boolean observadorAtivo;
  private JLabel labelCampo;
  private JButtonMensagem buttonMensagem;
  private boolean validar;
  private String idAjuda;
  private String identificacaoPainelAssociado;
  private MostrarDicas mostraDicas;
  private static boolean validandoImpeditivo;
  private ActionListener preActionOkSomente;
  private ActionListener preActionOk;
  private ActionListener preActionCancel;
  private static int CONST_CALC_TAMX_PAINELDICAS = 7;
  private boolean focusLostAtivo;
  private Color corComponenteReadOnly;
  private Color corBackgroundComponenteReadOnly;
  private Color corComponenteNormal;
  private Color corBackgroundComponenteNormal;
  private boolean disableButtonMensagem;
  private boolean readOnlyFocusAble;
  protected int incrementoTamanhoFonte;
  protected float tamanhoOriginal;
  
  class Run implements Runnable
  {
    private boolean foco;
    
    public Run (boolean mudarIdentificacaoFoco)
    {
      foco = mudarIdentificacaoFoco;
    }
    
    public void run ()
    {
      setIdentificacaoFoco (foco);
      getComponenteFoco ().requestFocusInWindow ();
    }
  }
  
  class MostrarDicas implements ActionListener
  {
    private String msg;
    private String titulo;
    private int tipo;
    private Window parent = null;
    
    public MostrarDicas (int tipo, String titulo, String msg)
    {
      this.msg = msg;
      this.titulo = titulo;
      this.tipo = tipo;
    }
    
    public MostrarDicas (Window parent, byte severidade, String nomeCampoCurto, String mensagem)
    {
      this (severidade, nomeCampoCurto, mensagem);
      this.parent = parent;
    }
    
    public void disparaValidacaoImpeditiva ()
    {
      int tamMax = getInformacao ().getNomeCampoCurto ().length () * JEditCampo.CONST_CALC_TAMX_PAINELDICAS;
      tamMax = tamMax > 90 ? tamMax : 90;
      if (tipo == 5)
	{
	  FabricaGUI.exibirPainelDicasModal (parent, tipo, titulo, msg, getComponenteEditor (), tamMax);
	  setValidandoImpeditivo (false);
	  getButtonMensagem ().setVisible (false);
	}
      else if (tipo == 4)
	{
	  FabricaGUI.exibirPainelDicasModal (parent, tipo, titulo, msg, getComponenteEditor (), tamMax);
	  setValidandoImpeditivo (false);
	  getButtonMensagem ().setVisible (false);
	}
    }
    
    public void dispara ()
    {
      int tamMax = getInformacao ().getNomeCampoCurto ().length () * JEditCampo.CONST_CALC_TAMX_PAINELDICAS;
      tamMax = tamMax > 90 ? tamMax : 90;
      if (tipo == 5)
	{
	  FabricaGUI.exibirPainelDicasModal (parent, tipo, titulo, msg, getComponenteEditor (), tamMax, getPreActionOkSomente ());
	  setValidandoImpeditivo (false);
	  getButtonMensagem ().setVisible (false);
	}
      else if (tipo == 4)
	{
	  FabricaGUI.exibirPainelDicasModal (parent, tipo, titulo, msg, getComponenteEditor (), tamMax, getPreActionOk (), getPreActionCancel ());
	  setValidandoImpeditivo (false);
	  getButtonMensagem ().setVisible (false);
	}
      else if (SwingUtilities.getRoot (getButtonMensagem ()) instanceof JDialog)
	FabricaGUI.exibirPainelDicas ((Window) SwingUtilities.getRoot (getButtonMensagem ()), tipo, titulo, msg, getButtonMensagem (), tamMax);
      else
	FabricaGUI.exibirPainelDicas (tipo, titulo, msg, getButtonMensagem (), tamMax);
    }
    
    public void actionPerformed (ActionEvent e)
    {
      dispara ();
    }
  }
  
  public JEditCampo ()
  {
    simbolo = new JLabel ("");
    observadorAtivo = true;
    buttonMensagem = new JButtonMensagem ();
    validar = true;
    identificacaoPainelAssociado = "";
    mostraDicas = null;
    focusLostAtivo = true;
    corComponenteReadOnly = null;
    corBackgroundComponenteReadOnly = null;
    corComponenteNormal = null;
    corBackgroundComponenteNormal = null;
    disableButtonMensagem = false;
    readOnlyFocusAble = true;
    incrementoTamanhoFonte = 0;
    tamanhoOriginal = -1.0F;
    instanciaComponentes ();
    String str = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.guibeans.associaComFacade", "true");
    setAssociaComFacade (Boolean.valueOf (str).booleanValue ());
    buildComponente ();
    setOpaque (false);
  }
  
  protected void instanciaComponentes ()
  {
    /* empty */
  }
  
  public JEditCampo (Informacao campo)
  {
    this (campo, null);
  }
  
  public JEditCampo (Informacao campo, String idAjuda)
  {
    simbolo = new JLabel ("");
    observadorAtivo = true;
    buttonMensagem = new JButtonMensagem ();
    validar = true;
    identificacaoPainelAssociado = "";
    mostraDicas = null;
    focusLostAtivo = true;
    corComponenteReadOnly = null;
    corBackgroundComponenteReadOnly = null;
    corComponenteNormal = null;
    corBackgroundComponenteNormal = null;
    disableButtonMensagem = false;
    readOnlyFocusAble = true;
    incrementoTamanhoFonte = 0;
    tamanhoOriginal = -1.0F;
    instanciaComponentes ();
    init (idAjuda);
    buildComponente ();
    setInformacao (campo);
  }
  
  protected void desfazModificacao ()
  {
    getInformacao ().disparaObservadores ();
    getComponenteFoco ().grabFocus ();
  }
  
  protected boolean continuaValidacaoImpeditiva (Object proxConteudo)
  {
    if (proxConteudo != null && proxConteudo.equals (getInformacao ().getConteudoFormatado ()))
      return false;
    return true;
  }
  
  protected boolean verificaValidacoesImpeditivas (Object proxConteudo)
  {
    if (! continuaValidacaoImpeditiva (proxConteudo))
      return true;
    setObservadorAtivo (false);
    getInformacao ().ordenaListaValidadoreImpeditivos ();
    Iterator itValidacoesImpeditivas = getInformacao ().getListaValidadoresImpeditivos ().iterator ();
    while (itValidacoesImpeditivas.hasNext ())
      {
	ValidadorImpeditivoDefault validador = (ValidadorImpeditivoDefault) itValidacoesImpeditivas.next ();
	validador.setProximoConteudo (proxConteudo);
	RetornoValidacao retornoValidacao = validador.validarImplementado ();
	if (retornoValidacao != null)
	  {
	    if (validador.getTipoExibicao () == 0)
	      {
		String stringOkCustom = validador.getStringOkCustomizada ();
		String stringCancelCustom = validador.getStringCancelarCustomizada ();
		if (stringOkCustom == null)
		  stringOkCustom = "Ok";
		if (stringCancelCustom == null)
		  stringCancelCustom = "Cancelar";
		if (validador.getSeveridade () == 5)
		  {
		    Object[] options = { stringOkCustom };
		    JOptionPane.showOptionDialog (UtilitariosGUI.tentaObterJanelaPrincipal (), retornoValidacao.getMensagemValidacaoExtendida (), validador.getTituloPopup (), 0, 1, null, options, options[0]);
		    validador.acaoCancelar ();
		    setObservadorAtivo (true);
		    if (validador.isDesfazModificacaoAoCancelar ())
		      desfazModificacao ();
		    return false;
		  }
		Object[] options = { stringOkCustom, stringCancelCustom };
		int opt = JOptionPane.showOptionDialog (UtilitariosGUI.tentaObterJanelaPrincipal (), retornoValidacao.getMensagemValidacaoExtendida (), validador.getTituloPopup (), 2, 3, null, options, options[0]);
		if (opt == 0)
		  {
		    validador.acaoOk ();
		    setObservadorAtivo (true);
		  }
		else
		  {
		    validador.acaoCancelar ();
		    setObservadorAtivo (true);
		    if (validador.isDesfazModificacaoAoCancelar ())
		      desfazModificacao ();
		    return false;
		  }
	      }
	    else if (validador.getTipoExibicao () == 1)
	      {
		setValidandoImpeditivo (true);
		Window parent = SwingUtilities.getWindowAncestor (getButtonMensagem ());
		if (validador.getSeveridade () == 5)
		  getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_ERRO);
		else
		  getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_AVISO);
		getButtonMensagem ().setVisible (true);
		mostraDicas = new MostrarDicas (parent, validador.getSeveridade (), campo.getNomeCampoCurto (), retornoValidacao.getMensagemValidacaoExtendida ());
		mostraDicas.disparaValidacaoImpeditiva ();
		if (PainelDicasModal.getOpcaoSelecionada () == 1 && validador.getSeveridade () == 4)
		  {
		    validador.acaoOk ();
		    setObservadorAtivo (true);
		  }
		else
		  {
		    validador.acaoCancelar ();
		    setObservadorAtivo (true);
		    if (validador.isDesfazModificacaoAoCancelar ())
		      desfazModificacao ();
		    return false;
		  }
	      }
	  }
      }
    setObservadorAtivo (true);
    return true;
  }
  
  private void init (String idAjuda)
  {
    setIdAjuda (idAjuda);
    setPreActionOkSomente (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	Informacao info = getInformacao ();
	info.setConteudo (info.getUltimoConteudoValido ());
	getComponenteEditor ().grabFocus ();
      }
    });
    setPreActionOk (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	Informacao info = getInformacao ();
	info.setUltimoConteudoValido (info.asString ());
      }
    });
    setPreActionCancel (new ActionListener ()
    {
      public void actionPerformed (ActionEvent e)
      {
	Informacao info = getInformacao ();
	String conteudoAtual = info.asString ();
	info.setConteudo (info.getUltimoConteudoValido ());
	getComponenteEditor ().grabFocus ();
      }
    });
  }
  
  protected abstract void buildComponente ();
  
  private final void setAssociaInformacao (Informacao novoCampo)
  {
    if (campo != null)
      campo.getObservadores ().removePropertyChangeListener (this);
    campo = novoCampo;
    if (novoCampo != null)
      novoCampo.getObservadores ().addPropertyChangeListener (this);
    MapeamentoInformacaoEditCampo.associaInformacao (this, novoCampo);
    readOnlyPropertyChange (novoCampo.isReadOnly ());
    habilitadoPropertyChange (novoCampo.isHabilitado ());
    if (novoCampo.isReadOnly () || ! novoCampo.isHabilitado ())
      setDisableButtonMensagem (true);
    else
      setDisableButtonMensagem (false);
  }
  
  public void setInformacao (Informacao campo)
  {
    if (campo == null)
      LogPPGD.erro ("Tentando setar null em uma informa\u00e7\u00e3o? Edits podem n\u00e3o funcionar bem com uma informa\u00e7\u00e3o nula.");
    setAssociaInformacao (campo);
    informacaoModificada ();
    getButtonMensagem ().setVisible (false);
  }
  
  protected abstract void informacaoModificada ();
  
  public Informacao getInformacao ()
  {
    return campo;
  }
  
  public JLabel getRotulo ()
  {
    return labelCampo;
  }
  
  public void setRotulo (JLabel jlabel)
  {
    if (getRotulo () != null)
      jlabel.setEnabled (getRotulo ().isEnabled ());
    labelCampo = jlabel;
  }
  
  public JButtonMensagem getButtonMensagem ()
  {
    return buttonMensagem;
  }
  
  public void setButtonMensagem (JButtonMensagem aBtn)
  {
    buttonMensagem = aBtn;
    buildComponente ();
  }
  
  public String getIdAjuda ()
  {
    return idAjuda;
  }
  
  public void setIdAjuda (String string)
  {
    idAjuda = string;
  }
  
  public boolean isValidar ()
  {
    return validar;
  }
  
  public void setValidar (boolean b)
  {
    validar = b;
  }
  
  public synchronized boolean chamaValidacao ()
  {
    if (isValidandoImpeditivo ())
      return false;
    if (! validar || getButtonMensagem () == null)
      return true;
    Informacao campo = getInformacao ();
    RetornosValidacoes retornosValidacoes = campo.validar ();
    RetornoValidacao ret = retornosValidacoes.getPrimeiroRetornoValidacaoMaisSevero ();
    if (ret.getSeveridade () > 0)
      {
	String mensagem = ret.getMensagemValidacaoExtendida ();
	if (mostraDicas != null)
	  getButtonMensagem ().removeActionListener (mostraDicas);
	if (ret.getSeveridade () == 1)
	  {
	    getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_ATENCAO);
	    mostraDicas = new MostrarDicas (ret.getSeveridade (), campo.getNomeCampoCurto (), mensagem);
	    getButtonMensagem ().addActionListener (mostraDicas);
	  }
	else if (ret.getSeveridade () == 3)
	  {
	    getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_ERRO);
	    mostraDicas = new MostrarDicas (ret.getSeveridade (), campo.getNomeCampoCurto (), mensagem);
	    getButtonMensagem ().addActionListener (mostraDicas);
	  }
	else if (ret.getSeveridade () == 5)
	  {
	    setValidandoImpeditivo (true);
	    Window parent = SwingUtilities.getWindowAncestor (getButtonMensagem ());
	    getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_ERRO);
	    mostraDicas = new MostrarDicas (parent, ret.getSeveridade (), campo.getNomeCampoCurto (), mensagem);
	    SwingUtilities.invokeLater (new Runnable ()
	    {
	      public void run ()
	      {
		mostraDicas.dispara ();
	      }
	    });
	  }
	else if (ret.getSeveridade () == 4)
	  {
	    setValidandoImpeditivo (true);
	    Window parent = SwingUtilities.getWindowAncestor (getButtonMensagem ());
	    LogPPGD.debug ("colocando icone atencao");
	    getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_ATENCAO);
	    mostraDicas = new MostrarDicas (parent, ret.getSeveridade (), campo.getNomeCampoCurto (), mensagem);
	    SwingUtilities.invokeLater (new Runnable ()
	    {
	      public void run ()
	      {
		mostraDicas.dispara ();
	      }
	    });
	  }
	else
	  {
	    getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_AVISO);
	    mostraDicas = new MostrarDicas (ret.getSeveridade (), campo.getNomeCampoCurto (), mensagem);
	    getButtonMensagem ().addActionListener (mostraDicas);
	    getButtonMensagem ().setVisible (! isDisableButtonMensagem ());
	  }
	getButtonMensagem ().setVisible (! isDisableButtonMensagem ());
	if (SwingUtilities.getRoot (getButtonMensagem ()) instanceof Window)
	  {
	    getButtonMensagem ().getParent ().repaint ();
	    repaint ();
	    ((Window) SwingUtilities.getRoot (getButtonMensagem ())).repaint ();
	  }
	return false;
      }
    getButtonMensagem ().setVisible (false);
    if (SwingUtilities.getRoot (getButtonMensagem ()) instanceof Window)
      {
	getButtonMensagem ().getParent ().repaint ();
	repaint ();
	((Window) SwingUtilities.getRoot (getButtonMensagem ())).repaint ();
      }
    FabricaGUI.esconderPainelDicas ();
    return true;
  }
  
  public final void propertyChange (PropertyChangeEvent evt)
  {
    if (isObservadorAtivo ())
      {
	LogPPGD.debug ("Disparando observador EditCampo de " + getInformacao ().getNomeCampo () + ".");
	if (evt.getPropertyName () != null)
	  {
	    if (evt.getPropertyName ().equals ("ReadOnly"))
	      {
		if (! isReadOnlyFocusAble ())
		  {
		    if (getInformacao ().isReadOnly ())
		      getComponenteFoco ().setFocusable (false);
		    else
		      getComponenteFoco ().setFocusable (true);
		  }
		else
		  getComponenteFoco ().setFocusable (true);
		if (getInformacao ().isReadOnly ())
		  {
		    setDisableButtonMensagem (true);
		    getComponenteFoco ().setForeground (getCorComponenteReadOnly ());
		    getComponenteFoco ().setBackground (getCorBackgroundComponenteReadOnly ());
		  }
		else
		  {
		    setDisableButtonMensagem (false);
		    getComponenteFoco ().setForeground (getCorComponenteNormal ());
		    getComponenteFoco ().setBackground (getCorBackgroundComponenteNormal ());
		  }
		if (getInformacao ().isTransportado ())
		  getSimbolo ().setText (">>");
		else
		  getSimbolo ().setText ("=");
		getSimbolo ().setVisible (getInformacao ().isReadOnly ());
		readOnlyPropertyChange (getInformacao ().isReadOnly ());
	      }
	    else if (evt.getPropertyName ().equals ("habilitado"))
	      {
		if (getInformacao ().isHabilitado ())
		  setDisableButtonMensagem (false);
		else
		  setDisableButtonMensagem (true);
		habilitadoPropertyChange (getInformacao ().isHabilitado ());
	      }
	    else if (evt.getPropertyName ().equals ("Val-Property"))
	      {
		getButtonMensagem ().setVisible (false);
		FabricaGUI.esconderPainelDicas ();
		return;
	      }
	  }
	implementacaoPropertyChange (evt);
	chamaValidacao ();
      }
  }
  
  protected abstract void readOnlyPropertyChange (boolean bool);
  
  protected abstract void habilitadoPropertyChange (boolean bool);
  
  public boolean isObservadorAtivo ()
  {
    return observadorAtivo;
  }
  
  public void setObservadorAtivo (boolean b)
  {
    observadorAtivo = b;
  }
  
  public abstract void implementacaoPropertyChange (PropertyChangeEvent propertychangeevent);
  
  public abstract JComponent getComponenteEditor ();
  
  public abstract JComponent getComponenteFoco ();
  
  public void setIdentificacaoFoco (boolean status)
  {
    if (status)
      {
	JLabel rotulo = getRotulo ();
	if (rotulo != null)
	  {
	    bLabel = new BlinkLabel (rotulo);
	    bLabel.start ();
	  }
      }
    else if (bLabel != null)
      {
	bLabel.parar ();
	bLabel = null;
      }
  }
  
  public void setaFoco (boolean status)
  {
    SwingUtilities.invokeLater (new Run (status));
  }
  
  public void setaVisivel (boolean pOpt)
  {
    getComponenteEditor ().setVisible (pOpt);
    if (getRotulo () != null)
      getRotulo ().setVisible (pOpt);
  }
  
  public String getIdentificacaoPainelAssociado ()
  {
    return identificacaoPainelAssociado;
  }
  
  public void setIdentificacaoPainelAssociado (String aIdentificacao)
  {
    identificacaoPainelAssociado = aIdentificacao;
  }
  
  public JLabel getSimbolo ()
  {
    return simbolo;
  }
  
  public void setSimbolo (JLabel simbolo)
  {
    this.simbolo = simbolo;
  }
  
  public void setPreActionOkSomente (ActionListener preActionOkSomente)
  {
    this.preActionOkSomente = preActionOkSomente;
  }
  
  public ActionListener getPreActionOkSomente ()
  {
    return preActionOkSomente;
  }
  
  public void setPreActionOk (ActionListener preActionOk)
  {
    this.preActionOk = preActionOk;
  }
  
  public ActionListener getPreActionOk ()
  {
    return preActionOk;
  }
  
  public void setPreActionCancel (ActionListener preActionCancel)
  {
    this.preActionCancel = preActionCancel;
  }
  
  public ActionListener getPreActionCancel ()
  {
    return preActionCancel;
  }
  
  protected synchronized void setValidandoImpeditivo (boolean pValidandoImpeditivo)
  {
    validandoImpeditivo = pValidandoImpeditivo;
  }
  
  protected boolean isValidandoImpeditivo ()
  {
    return validandoImpeditivo;
  }
  
  public void disparaPainelDicasModal ()
  {
    getButtonMensagem ().setVisible (true);
    RetornosValidacoes validacoes = getInformacao ().validar ();
    RetornoValidacao ret = validacoes.getPrimeiroRetornoValidacaoMaisSevero ();
    Window parent = SwingUtilities.getWindowAncestor (getButtonMensagem ());
    getButtonMensagem ().setIcon (ConstantesGlobaisGUI.ICO_ERRO);
    mostraDicas = new MostrarDicas (parent, ret.getSeveridade (), campo.getNomeCampoCurto (), ret.getMensagemValidacaoExtendida ());
    SwingUtilities.invokeLater (new Runnable ()
    {
      public void run ()
      {
	mostraDicas.dispara ();
      }
    });
  }
  
  public boolean isFocusLostAtivo ()
  {
    return focusLostAtivo;
  }
  
  public void setFocusLostAtivo (boolean focusListenersAtivos)
  {
    focusLostAtivo = focusListenersAtivos;
  }
  
  public Color getCorComponenteReadOnly ()
  {
    if (corComponenteReadOnly == null)
      corComponenteReadOnly = getComponenteFoco ().getForeground ();
    return corComponenteReadOnly;
  }
  
  public void setCorComponenteReadOnly (Color corComponenteReadOnly)
  {
    this.corComponenteReadOnly = corComponenteReadOnly;
  }
  
  public Color getCorComponenteNormal ()
  {
    if (corComponenteNormal == null)
      corComponenteNormal = getComponenteFoco ().getForeground ();
    return corComponenteNormal;
  }
  
  public void setCorComponenteNormal (Color corComponenteNormal)
  {
    this.corComponenteNormal = corComponenteNormal;
  }
  
  public Color getCorBackgroundComponenteNormal ()
  {
    if (corBackgroundComponenteNormal == null)
      corBackgroundComponenteNormal = getComponenteFoco ().getBackground ();
    return corBackgroundComponenteNormal;
  }
  
  public void setCorBackgroundComponenteNormal (Color corBackgroundComponenteNormal)
  {
    this.corBackgroundComponenteNormal = corBackgroundComponenteNormal;
  }
  
  public Color getCorBackgroundComponenteReadOnly ()
  {
    if (corBackgroundComponenteReadOnly == null)
      corBackgroundComponenteReadOnly = getComponenteFoco ().getBackground ();
    return corBackgroundComponenteReadOnly;
  }
  
  public void setCorBackgroundComponenteReadOnly (Color corBackgroundComponenteReadOnly)
  {
    this.corBackgroundComponenteReadOnly = corBackgroundComponenteReadOnly;
  }
  
  public boolean isReadOnlyFocusAble ()
  {
    return readOnlyFocusAble;
  }
  
  public void setReadOnlyFocusAble (boolean isReadOnlyFocusAble)
  {
    readOnlyFocusAble = isReadOnlyFocusAble;
  }
  
  public boolean isDisableButtonMensagem ()
  {
    return disableButtonMensagem;
  }
  
  public void setDisableButtonMensagem (boolean disableButtonMensagem)
  {
    this.disableButtonMensagem = disableButtonMensagem;
  }
  
  public void setInformacaoAssociada (String informacaoAssociada)
  {
    this.informacaoAssociada = informacaoAssociada;
    if (isAssociaComFacade () && ! PlataformaPPGD.isEmDesign ())
      associaInformacao (informacaoAssociada);
  }
  
  protected void associaInformacao (Object objPai, String aInfo)
  {
    Object objInfo = objPai;
    try
      {
	StringTokenizer tokens = new StringTokenizer (aInfo, ".");
	while (tokens.hasMoreTokens ())
	  {
	    Class clazz = objInfo.getClass ();
	    String nomeMetodo = tokens.nextToken ();
	    nomeMetodo = nomeMetodo.substring (0, 1).toUpperCase () + nomeMetodo.substring (1, nomeMetodo.length ());
	    Method mtd = clazz.getMethod ("get" + nomeMetodo, null);
	    objInfo = mtd.invoke (objInfo, null);
	    if (objInfo == null)
	      break;
	  }
	if (objInfo != null)
	  setInformacao ((Informacao) objInfo);
	else
	  getInformacao ().setHabilitado (false);
      }
    catch (Exception e)
      {
	LogPPGD.erro ("N\u00e3o foi poss\u00edvel encontrar '" + aInfo + "'. Este atributo existe mesmo?");
	e.printStackTrace ();
      }
  }
  
  protected void associaInformacao (String aInfo)
  {
    try
      {
	String classeFacade = FabricaUtilitarios.getProperties ().getProperty ("aplicacao.classes.facade", "serpro.ppgd.repositorio.FacadeDefault");
	Class classe = Class.forName (classeFacade);
	Method methGetInstancia = classe.getMethod ("getInstancia", null);
	PPGDFacade facade = (PPGDFacade) methGetInstancia.invoke (null, null);
	Object objInfo = facade;
	associaInformacao (objInfo, aInfo);
      }
    catch (Exception e)
      {
	LogPPGD.erro ("N\u00e3o foi poss\u00edvel encontrar '" + aInfo + "'. Este atributo existe mesmo?");
	e.printStackTrace ();
      }
  }
  
  public String getInformacaoAssociada ()
  {
    return informacaoAssociada;
  }
  
  public void setAssociaComFacade (boolean associadoAutomaticamente)
  {
    associaComFacade = associadoAutomaticamente;
  }
  
  public boolean isAssociaComFacade ()
  {
    return associaComFacade;
  }
  
  public abstract void setEstiloFonte (int i);
  
  public abstract void setIncrementoTamanhoFonte (int i);
  
  public abstract int getIncrementoTamanhoFonte ();
}
