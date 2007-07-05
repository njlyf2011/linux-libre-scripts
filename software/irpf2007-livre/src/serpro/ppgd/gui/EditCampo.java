/* EditCampo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package serpro.ppgd.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import serpro.ppgd.gui.pendencia.MapeamentoInformacaoEditCampo;
import serpro.ppgd.negocio.Informacao;
import serpro.ppgd.negocio.RetornoValidacao;
import serpro.ppgd.negocio.RetornosValidacoes;
import serpro.ppgd.negocio.ValidadorImpeditivoDefault;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.LogPPGD;

public abstract class EditCampo implements PropertyChangeListener
{
  protected Informacao campo = null;
  protected JLabel labelCampo = null;
  protected JLabel simbolo;
  private BlinkLabel bLabel;
  protected Dimension[] d;
  private boolean observadorAtivo;
  private JButton buttonMensagem;
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
  protected boolean isPerdeFocoComEnter;
  private static boolean prevalidacaoAtiva = false;
  private static boolean escondeErroAoEditar = false;
  private static boolean validaNaoNuloAposEdicao = true;
  private static boolean validaAposAcoplarInformacao = false;
  private static boolean editCampoTransfereFocoEnter = false;
  private boolean readOnlyFocusAble;
  protected boolean selecionaTextoOnFocusGained;
  
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
      int tamMax = getInformacao ().getNomeCampoCurto ().length () * EditCampo.CONST_CALC_TAMX_PAINELDICAS;
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
      int tamMax = getInformacao ().getNomeCampoCurto ().length () * EditCampo.CONST_CALC_TAMX_PAINELDICAS;
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
  
  static
  {
    try
      {
	prevalidacaoAtiva = Boolean.valueOf (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.prevalidacaoAtiva", "false")).booleanValue ();
	escondeErroAoEditar = Boolean.valueOf (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.escondeErroAoEditar", "false")).booleanValue ();
	validaNaoNuloAposEdicao = Boolean.valueOf (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.validaNaoNuloAposEdicao", "true")).booleanValue ();
	validaAposAcoplarInformacao = Boolean.valueOf (FabricaUtilitarios.getProperties ().getProperty ("aplicacao.gui.validaAposAcoplarInformacao", "false")).booleanValue ();
	editCampoTransfereFocoEnter = Boolean.valueOf (FabricaUtilitarios.getProperties ().getProperty ("editCampo.transfereFocoEnter", "false")).booleanValue ();
      }
    catch (Exception e)
      {
	e.printStackTrace ();
      }
  }
  
  public EditCampo ()
  {
    simbolo = new JLabel ("");
    observadorAtivo = true;
    validar = true;
    identificacaoPainelAssociado = "";
    mostraDicas = null;
    focusLostAtivo = true;
    corComponenteReadOnly = null;
    corBackgroundComponenteReadOnly = null;
    corComponenteNormal = null;
    corBackgroundComponenteNormal = null;
    disableButtonMensagem = false;
    isPerdeFocoComEnter = editCampoTransfereFocoEnter;
    readOnlyFocusAble = true;
    selecionaTextoOnFocusGained = true;
  }
  
  public EditCampo (Informacao campo, Dimension[] arDim)
  {
    this (campo, arDim, null);
  }
  
  public EditCampo (Informacao campo, Dimension[] arDim, String idAjuda)
  {
    simbolo = new JLabel ("");
    observadorAtivo = true;
    validar = true;
    identificacaoPainelAssociado = "";
    mostraDicas = null;
    focusLostAtivo = true;
    corComponenteReadOnly = null;
    corBackgroundComponenteReadOnly = null;
    corComponenteNormal = null;
    corBackgroundComponenteNormal = null;
    disableButtonMensagem = false;
    isPerdeFocoComEnter = editCampoTransfereFocoEnter;
    readOnlyFocusAble = true;
    selecionaTextoOnFocusGained = true;
    init (campo, arDim, idAjuda);
  }
  
  public EditCampo (Informacao campo, Dimension d)
  {
    this (campo, d, null);
  }
  
  public EditCampo (Informacao campo, Dimension d, String idAjuda)
  {
    simbolo = new JLabel ("");
    observadorAtivo = true;
    validar = true;
    identificacaoPainelAssociado = "";
    mostraDicas = null;
    focusLostAtivo = true;
    corComponenteReadOnly = null;
    corBackgroundComponenteReadOnly = null;
    corComponenteNormal = null;
    corBackgroundComponenteNormal = null;
    disableButtonMensagem = false;
    isPerdeFocoComEnter = editCampoTransfereFocoEnter;
    readOnlyFocusAble = true;
    selecionaTextoOnFocusGained = true;
    Dimension[] arDim = new Dimension[1];
    arDim[0] = d;
    init (campo, arDim, idAjuda);
  }
  
  public EditCampo (Informacao campo, int tamanho)
  {
    this (campo, tamanho, null);
  }
  
  public EditCampo (Informacao campo, int tamanho, String idAjuda)
  {
    simbolo = new JLabel ("");
    observadorAtivo = true;
    validar = true;
    identificacaoPainelAssociado = "";
    mostraDicas = null;
    focusLostAtivo = true;
    corComponenteReadOnly = null;
    corBackgroundComponenteReadOnly = null;
    corComponenteNormal = null;
    corBackgroundComponenteNormal = null;
    disableButtonMensagem = false;
    isPerdeFocoComEnter = editCampoTransfereFocoEnter;
    readOnlyFocusAble = true;
    selecionaTextoOnFocusGained = true;
    Dimension[] arDim = new Dimension[1];
    arDim[0] = new Dimension (tamanho, 0);
    init (campo, arDim, idAjuda);
  }
  
  public EditCampo (Informacao campo)
  {
    this (campo, 0, null);
  }
  
  public EditCampo (Informacao campo, String idAjuda)
  {
    this (campo, 0, idAjuda);
  }
  
  public boolean isSelecionaTextoOnFocusGained ()
  {
    return selecionaTextoOnFocusGained;
  }
  
  public void setSelecionaTextoOnFocusGained (boolean selecionaTextoOnFocusGained)
  {
    this.selecionaTextoOnFocusGained = selecionaTextoOnFocusGained;
  }
  
  protected void desfazModificacao ()
  {
    getInformacao ().disparaObservadores ();
    getComponenteFoco ().grabFocus ();
  }
  
  protected boolean continuaValidacaoImpeditiva (Object proxConteudo)
  {
    if (proxConteudo != null && proxConteudo.equals (getInformacao ().getConteudoFormatado ()) && ! getInformacao ().isVazio ())
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
		int tipoMensagem = validador.getTipoMensagem ();
		if (validador.getSeveridade () == 5)
		  {
		    Object[] options = { stringOkCustom };
		    JOptionPane.showOptionDialog (UtilitariosGUI.tentaObterJanelaPrincipal (), retornoValidacao.getMensagemValidacaoExtendida (), validador.getTituloPopup (), 0, tipoMensagem != -1 ? tipoMensagem : 1, null, options, options[0]);
		    validador.acaoCancelar ();
		    setObservadorAtivo (true);
		    if (validador.isDesfazModificacaoAoCancelar ())
		      desfazModificacao ();
		    return false;
		  }
		Object[] options = { stringOkCustom, stringCancelCustom };
		int opt = JOptionPane.showOptionDialog (UtilitariosGUI.tentaObterJanelaPrincipal (), retornoValidacao.getMensagemValidacaoExtendida (), validador.getTituloPopup (), 2, tipoMensagem != -1 ? tipoMensagem : 3, null, options, options[0]);
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
  
  private void init (Informacao campo, Dimension[] arDim, String idAjuda)
  {
    setIdAjuda (idAjuda);
    d = arDim;
    for (int i = 0; i < d.length; i++)
      {
	if (d[i].width < 2)
	  d[i].width = 1;
	if (d[i].height < 2)
	  d[i].height = 1;
      }
    if (campo != null)
      {
	setAssociaInformacao (campo);
	getComponenteEditor ().setPreferredSize (new Dimension (d[0].width * 10, 20));
	if (escondeErroAoEditar)
	  getComponenteEditor ().addFocusListener (new FocusAdapter ()
	  {
	    public void focusGained (FocusEvent e)
	    {
	      if (getButtonMensagem ().isVisible ())
		{
		  getButtonMensagem ().setVisible (false);
		  FabricaGUI.esconderPainelDicas ();
		}
	    }
	  });
	if (prevalidacaoAtiva)
	  getComponenteEditor ().addHierarchyListener (new HierarchyListener ()
	  {
	    public void hierarchyChanged (HierarchyEvent e)
	    {
	      if (getInformacao ().asString () != null && ! getInformacao ().asString ().trim ().equals (""))
		chamaValidacao ();
	    }
	  });
	LogPPGD.debug (getComponenteEditor ().getPreferredSize ().toString ());
	LogPPGD.debug (getComponenteEditor ().getMaximumSize ().toString ());
      }
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
  
  public void setDim (Dimension[] d)
  {
    this.d = d;
    getComponenteEditor ().setPreferredSize (new Dimension (d[0].width * 10, 20));
    LogPPGD.debug (getComponenteEditor ().getPreferredSize ().toString ());
    LogPPGD.debug (getComponenteEditor ().getMaximumSize ().toString ());
  }
  
  public final void setAssociaInformacao (Informacao novoCampo)
  {
    if (labelCampo == null)
      labelCampo = new JLabel (novoCampo.getNomeCampo ());
    else
      labelCampo.setText (novoCampo.getNomeCampo ());
    if (buttonMensagem == null)
      buttonMensagem = new JButton ();
    buttonMensagem.setVisible (false);
    buttonMensagem.setContentAreaFilled (false);
    buttonMensagem.setFocusable (false);
    buttonMensagem.setSize (20, 20);
    buttonMensagem.setMaximumSize (buttonMensagem.getSize ());
    buttonMensagem.setBorder (null);
    if (campo != null)
      campo.getObservadores ().removePropertyChangeListener (this);
    campo = novoCampo;
    if (novoCampo != null)
      novoCampo.getObservadores ().addPropertyChangeListener (this);
    setInformacao (novoCampo);
    setPerdeFocoComEnter (isPerdeFocoComEnter);
    MapeamentoInformacaoEditCampo.associaInformacao (this, novoCampo);
    readOnlyPropertyChange (novoCampo.isReadOnly ());
    if (! novoCampo.isHabilitado ())
      habilitadoPropertyChange (novoCampo.isHabilitado ());
    if (validaAposAcoplarInformacao)
      campo.validar ();
    if (! campo.isValido ())
      chamaValidacao ();
  }
  
  public abstract void setInformacao (Informacao informacao);
  
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
    jlabel.setEnabled (labelCampo.isEnabled ());
    labelCampo = jlabel;
  }
  
  public JButton getButtonMensagem ()
  {
    return buttonMensagem;
  }
  
  public String getIdAjuda ()
  {
    return idAjuda;
  }
  
  public void setIdAjuda (String string)
  {
    idAjuda = string;
  }
  
  /**
   * @deprecated
   */
  public boolean isValidar ()
  {
    return validar;
  }
  
  public boolean isValidarHabilitado ()
  {
    return validar;
  }
  
  /**
   * @deprecated
   */
  public void setValidar (boolean b)
  {
    validar = b;
  }
  
  public void setValidarHabilitado (boolean b)
  {
    validar = b;
  }
  
  public synchronized boolean chamaValidacao ()
  {
    if (isValidandoImpeditivo ())
      return false;
    if (! validar && ! getInformacao ().isValidadoresAtivos ())
      {
	getButtonMensagem ().setVisible (false);
	return true;
      }
    if (! validaNaoNuloAposEdicao && getInformacao ().isVazio ())
      {
	getButtonMensagem ().setVisible (false);
	return true;
      }
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
	return false;
      }
    getButtonMensagem ().setVisible (false);
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
	    else if (evt.getPropertyName ().equals ("Label_Modificado"))
	      getRotulo ().setText (getInformacao ().getNomeCampo ());
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
	bLabel = new BlinkLabel (labelCampo);
	bLabel.start ();
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
  
  public Dimension[] getD ()
  {
    return d;
  }
  
  public void setTamanho (int tamanho)
  {
    d[0].width = tamanho;
    getComponenteEditor ().setPreferredSize (new Dimension (d[0].width * 10, 20));
    LogPPGD.debug (getComponenteEditor ().getPreferredSize ().toString ());
    LogPPGD.debug (getComponenteEditor ().getMaximumSize ().toString ());
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
  
  public void disparaOptionPaneValidacaoImpeditiva (String tit, int tipo)
  {
    RetornosValidacoes validacoes = getInformacao ().validar ();
    RetornoValidacao ret = validacoes.getPrimeiroRetornoValidacaoMaisSevero ();
    JOptionPane.showMessageDialog (UtilitariosGUI.tentaObterJanelaPrincipal (), ret.getMensagemValidacaoExtendida (), tit, tipo);
    SwingUtilities.invokeLater (new Runnable ()
    {
      public void run ()
      {
	getComponenteFoco ().grabFocus ();
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
  
  public abstract void setPerdeFocoComEnter (boolean bool);
  
  public boolean isPerdeFocoComEnter ()
  {
    return isPerdeFocoComEnter;
  }
  
  protected void aplicaTransfereFocoEnter ()
  {
    getComponenteFoco ().getInputMap ().put (KeyStroke.getKeyStroke (10, 0, true), "Focus.nextComponent");
    getComponenteFoco ().getActionMap ().put ("Focus.nextComponent", FabricaGUI.criaActionTransfereFoco ());
  }
  
  protected void removeTransfereFocoEnter ()
  {
    getComponenteFoco ().getInputMap ().put (KeyStroke.getKeyStroke (10, 0, true), "Focus.nextComponent");
    getComponenteFoco ().getActionMap ().put ("Focus.nextComponent", null);
  }
}
