/* Copyright 2007 FSFLA
   Alexandre Oliva <lxoliva@fsfla.org>

   Este arquivo é parte de IRPF2007-Livre.

   IRPF2007-Livre é Software Livre; você pode redistribuí-lo e/ou
   modificá-lo de acordo com os termos da GNU Lesser General Public
   License publicada pela Free Software Foundation; tanto a versão 2.1
   dessa licença, quanto (à sua escolha) qualquer versão posterior.

   IRPF2007-Livre é distribuído na esperança de que ele seja útil, mas
   SEM QUALQUER GARANTIA; até mesmo sem qualquer garantia implícita de
   COMERCIABILIDADE OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte
   a GNU Lesser General Public License para mais detalhes.

   Você deve ter recebido uma cópia da GNU Lesser General Public
   License junto com IRPF2007-Livre; se não, escreva para a Free
   Software Foundation, Inc., no endereço 59 Temple Street, Suite 330,
   Boston, MA 02111-1307 USA.

   Veja uma tradução não oficial da licença em
   http://creativecommons.org/licenses/LGPL/2.1/legalcode.pt


   This file is part of IRPF2007-Livre.

   IRPF2007-Livre is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public License
   as published by the Free Software Foundation; either version 2.1 of
   the License, or (at your option) any later version.

   IRPF2007-Livre is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with IRPF2007-Livre; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
   02111-1307 USA.
*/

package org.fsfla.irpf2007;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import serpro.ppgd.formatosexternos.txt.RegistroTxt;
import serpro.ppgd.infraestrutura.PlataformaPPGD;
import serpro.ppgd.irpf.DeclaracaoIRPF;
import serpro.ppgd.irpf.IRPFFacade;
import serpro.ppgd.irpf.IdentificadorDeclaracao;
import serpro.ppgd.irpf.PreenchedorFichasImpressao;
import serpro.ppgd.irpf.impressao.ImpressaoDeclaracao;
import serpro.ppgd.irpf.txt.gravacaorestauracao.ConstantesRepositorio;
import serpro.ppgd.irpf.txt.gravacaorestauracao.GravadorTXT;
import serpro.ppgd.irpf.txt.gravacaorestauracao.ImportadorTxt;
import serpro.ppgd.irpf.txt.gravacaorestauracao.RepositorioDeclaracaoCentralTxt;
import serpro.ppgd.negocio.ConstantesGlobais;
import serpro.ppgd.negocio.Pendencia;
import serpro.ppgd.negocio.Valor;
import serpro.ppgd.negocio.util.FabricaUtilitarios;
import serpro.ppgd.negocio.util.UtilitariosString;
import serpro.ppgd.negocio.util.Validador;
import serpro.ppgd.persistenciagenerica.RepositorioXMLDefault;

public class Main {
    public static void ajuda() {
	System.out.println
	    ( "IRPF2007 Livre\n"
	     +"Copyright 2007 FSFLA\n"
	     +"\n"
	     +"leiame: mostra instruções mais longas de uso\n"
	     +"nova CPF: cria nova declaração\n"
	     +"importa CPF: importa declaração de 2006 do arquivo\n"
	     +"grava CPF: faz cópia de segurança da declaração\n"
	     +"restaura CPF: restaura cópia de segurança\n"
	     +"verifica CPF: verifica pendências\n"
	     +"prepara CPF: prepara declaração para envio\n"
	     +"resumo CPF: imprime resumo da declaração\n"
	     +"completa CPF: imprime declaração completa\n"
	     +"recibo CPF: imprime recibo da declaração\n"
	      );
    }

    public static void leiame()
    throws Exception {
	InputStream in = Main.class.getClassLoader ()
	    .getResource ("LEIAME").openStream ();
	byte buffer[] = new byte[8192];
	for (;;) {
	    int count = in.read (buffer);
	    if (count <= 0)
		break;
	    System.out.write (buffer, 0, count);
	}
    }

    protected static DeclaracaoIRPF leDeclXML (String cpf)
    throws Exception {
	return IRPFFacade.getInstancia().recuperarDeclaracaoIRPF (cpf);
    }

    public static void nova(String cpf)
    throws Exception {
	IdentificadorDeclaracao id = new IdentificadorDeclaracao ();
	id.getCpf ().setConteudo (cpf);
	IRPFFacade.criarDeclaracao (id);
    }

    public static void importa(String cpf)
    throws Exception {
	File arq = null;
	String nome = null;
	{
	    String nomes[] = new String[] {
		"-IRPF-" + ConstantesGlobais.EXERCICIO_ANTERIOR + "-"
		+ ConstantesGlobais.ANO_BASE_ANTERIOR + "-RETIF",
		"-IRPF-" + ConstantesGlobais.EXERCICIO_ANTERIOR + "-"
		+ ConstantesGlobais.ANO_BASE_ANTERIOR + "-ORIGI"
	    };
	    String exts[] = new String[] { ".DBK", ".DEC" };
	    _found_:
	    for (int i = 0; i < nomes.length; i++) {
		for (int j = 0; j < exts.length; j++) {
		    arq = new File (nome = cpf + nomes[i] + exts[j]);
		    if (arq.exists ())
			break _found_;
		}
	    }
	}

	boolean temrec = new File (nome.substring (0, nome.length () - 4)
				   + ".REC").exists ();
	System.out.println ("importando de " + nome
			    + (temrec ? " com recibo" : " sem recibo"));
	ImportadorTxt importador = new ImportadorTxt ();
	IdentificadorDeclaracao id = importador.restaurarIdDeclaracaoNaoPersistidoAnoAnterior (arq);
	IRPFFacade.criarDeclaracao (id);
	id = importador.importarDeclaracaoAnoAnterior (arq, temrec);
    }

    public static void grava(String cpf)
    throws Exception {
	DeclaracaoIRPF dec = leDeclXML (cpf);
	String nome = GravadorTXT.montaNomeArquivoTXT (ConstantesRepositorio.GRAV_COPIA, dec.getIdentificadorDeclaracao ());
	System.out.println ("gravando em " + nome);
	RepositorioDeclaracaoCentralTxt repositorio = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", new File (nome));
	repositorio.atualizarDeclaracao (dec, repositorio.FINALIDADE_BACKUP);
	String hash = repositorio.getArquivo ().calcularHash ();
	Vector vRegistros = repositorio.getConversor2Registros ()
	    .montarRegistroHeader (dec);
	repositorio.getArquivo ().atualizaHeader (vRegistros, hash);
	repositorio.getArquivo ().salvar ();
    }

    public static void restaura(String cpf)
    throws Exception {
	File arq = null;
	String nome = null;

	{
	    String nomes[] = new String[] {
		"-IRPF-" + ConstantesGlobais.EXERCICIO + "-"
		+ ConstantesGlobais.ANO_BASE + "-RETIF",
		"-IRPF-" + ConstantesGlobais.EXERCICIO + "-"
		+ ConstantesGlobais.ANO_BASE + "-ORIGI"
	    };
	    String exts[] = new String[] { ".DBK", ".DEC" };
	    _found_:
	    for (int i = 0; i < nomes.length; i++) {
		for (int j = 0; j < exts.length; j++) {
		    arq = new File (nome = cpf + nomes[i] + exts[j]);
		    if (arq.exists ())
			break _found_;
		}
	    }
	}
	
	System.out.println ("restaurando de " + nome);
	ImportadorTxt importador = new ImportadorTxt ();
	importador.restaurarDeclaracao (arq);
    }

    public static boolean verifica(String cpf)
    throws Exception {
	DeclaracaoIRPF dec = leDeclXML (cpf);
	List pendencias = FabricaUtilitarios.verificarPendencias (dec);

	if (pendencias.isEmpty ()) {
	    System.out.println ("Não há pendências.");
	    return false;
	}

	boolean erros = false;
	boolean avisos = false;

	for (Iterator iterator = pendencias.iterator ();
	     iterator.hasNext ();) {
	    try {
		Pendencia p = (Pendencia) iterator.next ();
		if (!p.isErro ()) {
		    avisos = true;
		    continue;
		}
		if (!erros) {
		    System.out.println ("Pendências graves:\n");
		    erros = true;
		}

		System.out.println (p.getMsg ());
		System.out.println ("no item " + p.getNumItem ());
		if (p.getCampo () != null)
		    System.out.println ("do campo " + p.getCampo ());
		if (p.getEntidade () != null)
		    System.out.println ("da entidade " + p.getEntidade ());
		System.out.println ("");
	    } catch (Exception e) {
		e.printStackTrace ();
	    }
	}

	if (!avisos)
	    return erros;

	System.out.println ("Avisos:\n");
	for (Iterator iterator = pendencias.iterator ();
	     iterator.hasNext ();) {
	    try {
		Pendencia p = (Pendencia) iterator.next ();
		if (p.isErro ())
		    continue;

		System.out.println (p.getMsg ());
		System.out.println ("no item " + p.getNumItem ());
		if (p.getCampo () != null)
		    System.out.println ("do campo " + p.getCampo ());
		if (p.getEntidade () != null)
		    System.out.println ("da entidade " + p.getEntidade ());
		System.out.println ("");
	    } catch (Exception e) {
		e.printStackTrace ();
	    }
	}

	return erros;
    }

    public static void prepara(String cpf)
    throws Exception {
	DeclaracaoIRPF dec = leDeclXML (cpf);
	String nome = GravadorTXT.montaNomeArquivoTXT (ConstantesRepositorio.GRAV_GERACAO, dec.getIdentificadorDeclaracao ());
	RepositorioDeclaracaoCentralTxt repositorio = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", new File (nome));
	repositorio.atualizarDeclaracao (dec, repositorio.FINALIDADE_ENTREGA);
	String hash = repositorio.getArquivo ().calcularHash ();
	Vector vRegistros = repositorio.getConversor2Registros ()
	    .montarRegistroHeader (dec);
	repositorio.getArquivo ().atualizaHeader (vRegistros, hash);
	vRegistros = repositorio.getConversor2Registros ()
	    .montarRecibo (dec);
	repositorio.getArquivo ().incluirRecibo (vRegistros, hash);
	repositorio.getArquivo ().salvar ();
	System.out.println ("gravada em " + nome);
    }

    public static void resumo(String cpf)
    throws Exception {
	DeclaracaoIRPF dec = leDeclXML (cpf);
	System.out.println ("não implementado!");
    }

    public static void completa(String cpf)
    throws Exception {
	DeclaracaoIRPF dec = leDeclXML (cpf);
/*
	String pathDados = dec.getIdentificadorDeclaracao ().getPathArquivo ();
	ImpressaoDeclaracao novaImpressao = new ImpressaoDeclaracao ();
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoContribuinte.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheContribuinte (novaImpressao, dec, true);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoConjuge.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheConjuge (novaImpressao, dec, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPJTitular.jasper", pathDados, "/classe/rendPJ/colecaoRendPJTitular/item");
	PreenchedorFichasImpressao.preencheRendPJTitular (novaImpressao, dec, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPJDependente.jasper", pathDados, "/classe/rendPJ/colecaoRendPJDependente/item");
	PreenchedorFichasImpressao.preencheRendPJDependente (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPFTitular.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheRendPFTitular (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosTributaveisPFDependente.jasper", pathDados, "/classe/rendPFDependente/colecaoCPFDependentes/item");
	PreenchedorFichasImpressao.preencheRendPFDependente (dec, novaImpressao, false);
	if (dec.getRendIsentos ().getLucroRecebidoQuadroAuxiliar ().isVazio ())
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosIsentosNaoTributaveis.jasper", pathDados, "/classe");
	else
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosIsentosNaoTributaveisLucros.jasper", pathDados, "/classe/rendIsentos/lucroRecebidoQuadroAuxiliar/item");
	PreenchedorFichasImpressao.preencheRendIsentos (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoRendimentosSujTribExclusiva.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheRendTribExcl (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoImpostoPago.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheImpostoPago (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDependentes.jasper", pathDados, "/classe/dependentes/item");
	PreenchedorFichasImpressao.preencheDependentes (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoPagamentosDoacoes.jasper", pathDados, "/classe/pagamentos/item");
	PreenchedorFichasImpressao.preenchePgtosDoacoes (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoBensDireitos.jasper", pathDados, "/classe/bens/item");
	PreenchedorFichasImpressao.preencheBensDireitos (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDividasOnusReais.jasper", pathDados, "/classe/dividas/item");
	PreenchedorFichasImpressao.preencheDividasOnus (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoInventariante.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheInventariante (dec, novaImpressao, false);
	novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoDoacoesEleitorais.jasper", pathDados, "/classe/doacoes/item");
	PreenchedorFichasImpressao.preencheDoacoesCampanha (dec, novaImpressao, false);
	if (dec.getIdentificadorDeclaracao ().getTipoDeclaracao ().asString ().equals ("0"))
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoResumo.jasper", pathDados, "/classe");
	else
	  novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relDeclaracaoResumoSimplificada.jasper", pathDados, "/classe");
	PreenchedorFichasImpressao.preencheResumo (dec, novaImpressao, false);
	if (! dec.getAtividadeRural ().getBrasil ().isVazio ()) {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARIdentificacao.jasper", pathDados, "/classe/atividadeRural/brasil/identificacaoImovel/item");
	    novaImpressao.addParametroUltimo ("ARLocal", "BRASIL");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARReceitasDespesasBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARApuracaoBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARApuracaoBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARRebanhoBrasil.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRural.jasper", pathDados, "/classe/atividadeRural/brasil/bens/item");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARDividas.jasper", pathDados, "/classe/atividadeRural/brasil/dividas/item");
	    PreenchedorFichasImpressao.preencheARBrasil (dec, novaImpressao, false);
	}
	if (! dec.getAtividadeRural ().getExterior ().isVazio ()) {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARIdentificacao.jasper", pathDados, "/classe/atividadeRural/exterior/identificacaoImovel/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARReceitasDespesasExterior.jasper", pathDados, "/classe/atividadeRural/exterior/receitasDespesas/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARApuracaoExterior.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARApuracaoExterior (dec, novaImpressao, false);
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARRebanhoExterior.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    if (dec.getAtividadeRural ().getExterior ().getBens ().isVazio ()) {
		novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRuralExterior-SemInfo.jasper", pathDados, "/classe");
		PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    } else {
		novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARBensDaAtividadeRuralExterior.jasper", pathDados, "/classe/atividadeRural/exterior/bens/item");
		PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	    }
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relARDividas.jasper", pathDados, "/classe/atividadeRural/exterior/dividas/item");
	    PreenchedorFichasImpressao.preencheARExterior (dec, novaImpressao, false);
	}
	if (! dec.getRendaVariavel ().isVazio ()) {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/janeiro");
	    novaImpressao.addParametroUltimo ("MES", "JANEIRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/fevereiro");
	    novaImpressao.addParametroUltimo ("MES", "FEVEREIRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/marco");
	    novaImpressao.addParametroUltimo ("MES", "MARCO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/abril");
	    novaImpressao.addParametroUltimo ("MES", "ABRIL");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/maio");
	    novaImpressao.addParametroUltimo ("MES", "MAIO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/junho");
	    novaImpressao.addParametroUltimo ("MES", "JUNHO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/julho");
	    novaImpressao.addParametroUltimo ("MES", "JULHO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/agosto");
	    novaImpressao.addParametroUltimo ("MES", "AGOSTO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/setembro");
	    novaImpressao.addParametroUltimo ("MES", "SETEMBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/outubro");
	    novaImpressao.addParametroUltimo ("MES", "OUTUBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/novembro");
	    novaImpressao.addParametroUltimo ("MES", "NOVEMBRO");
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavel.jasper", pathDados, "/classe/rendaVariavel/dezembro");
	    novaImpressao.addParametroUltimo ("MES", "DEZEMBRO");
	}
	if (! dec.getRendaVariavel ().getFundInvest ().isVazio ()) {
	    novaImpressao.addImpressaoDeclaracao ("Declara\u00e7\u00e3o", "relRendaVariavelFundoInvestimento.jasper", pathDados, "/classe");
	    PreenchedorFichasImpressao.preencheRelatorioGeral (novaImpressao, dec);
	}
	novaImpressao.imprimir ();
*/
	System.out.println ("ainda não funciona :-(");
    }

    public static void recibo(String cpf)
    throws Exception {
/*
	File arq = null;
	String nome = null;

	{
	    String nomes[] = new String[] {
		"-IRPF-" + ConstantesGlobais.EXERCICIO + "-"
		+ ConstantesGlobais.ANO_BASE + "-RETIF",
		"-IRPF-" + ConstantesGlobais.EXERCICIO + "-"
		+ ConstantesGlobais.ANO_BASE + "-ORIGI"
	    };
	    String exts[] = new String[] { ".REC" };
	    _found_:
	    for (int i = 0; i < nomes.length; i++) {
		for (int j = 0; j < exts.length; j++) {
		    arq = new File (nome = cpf + nomes[i] + exts[j]);
		    if (arq.exists ())
			break _found_;
		}
	    }
	}

	File arquivoDeclaracao = new File (nome.substring (0, nome.length () - 4) + ".DEC");
	File arquivoRecibo = arq;

	RepositorioDeclaracaoCentralTxt repositorioDeclaracao = null;
	RegistroTxt registroDeclaracao = null;
	RegistroTxt registroHeader = null;
	RepositorioDeclaracaoCentralTxt repositorioRecibo = null;
	RegistroTxt registroRecibo = null;
	RegistroTxt registroMulta = null;

	repositorioDeclaracao = new RepositorioDeclaracaoCentralTxt ("ARQ_IRPF", arquivoDeclaracao);
	registroDeclaracao = repositorioDeclaracao.recuperarRegistroRecibo ();
	IdentificadorDeclaracao idDeclaracao = repositorioDeclaracao.recuperarIdDeclaracaoNaoPersistido ();
	registroHeader = repositorioDeclaracao.recuperarRegistroHeader ();
	repositorioRecibo = new RepositorioDeclaracaoCentralTxt ("ARQ_COMPLRECIBO", arquivoRecibo);
	registroRecibo = repositorioRecibo.recuperarRegistroComplementoRecibo ();
	registroMulta = repositorioRecibo.recuperarRegistroComplementoReciboMulta ();
	System.out.println ("registro multa: " + registroMulta);
	ImpressaoDeclaracao impressao = new ImpressaoDeclaracao ();
	impressao.addImpressaoDeclaracao ("Recibo", "relRecibo.jasper", null, null);
	repositorioRecibo.validarComplementoRecibo (idDeclaracao);
	String RW001 = " ";
	System.out.println ("detalhe in aplic->" + registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ());
	System.out.println ("cod agente transmissor->" + registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ());
	System.out.println ("nome transmissor->" + registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString ());
	if (registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ().equals ("1") || registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString ().trim ().length () == 0)
	    RW001 = " ";
	else if (registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ().equals ("2") && registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ().equals ("999"))
	    RW001 = registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString ();
	else if (registroRecibo.fieldByName ("IN_APLIC_TRANSMISSAO").asString ().equals ("2") && ! registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ().equals ("999"))
	    RW001 = registroRecibo.fieldByName ("APLIC_TRANSMISSAO").asString () + registroRecibo.fieldByName ("COD_AG_TRANSMISSOR").asString ();
	impressao.addParametro ("exercicio", ConstantesGlobais.EXERCICIO);
	impressao.addParametro ("anobase", ConstantesGlobais.ANO_BASE);
	if (registroDeclaracao.fieldByName ("IN_COMPLETA").asBoolean ())
	    impressao.addParametro ("TITULO", "COMPLETA");
	else
	    impressao.addParametro ("TITULO", "SIMPLIFICADA");
	impressao.addParametro ("nomeContribuinte", registroDeclaracao.fieldByName ("NM_NOME").asString ());
	impressao.addParametro ("cpfContribuinte", UtilitariosString.formataCPF (registroDeclaracao.fieldByName ("NR_CPF").asString ()));
	if (registroDeclaracao.fieldByName ("SG_UF").asString ().equals ("EX"))
	    impressao.addParametro ("endereco", registroDeclaracao.fieldByName ("NM_LOGRA").asString ());
	else {
	    String end = registroDeclaracao.fieldByName ("TIP_LOGRA").asString () + " " + registroDeclaracao.fieldByName ("NM_LOGRA").asString ();
	    impressao.addParametro ("endereco", end);
	}
	impressao.addParametro ("numero", registroDeclaracao.fieldByName ("NR_NUMERO").asString ());
	impressao.addParametro ("complemento", registroDeclaracao.fieldByName ("NM_COMPLEM").asString ());
	impressao.addParametro ("bairro", registroDeclaracao.fieldByName ("NM_BAIRRO").asString ());
	impressao.addParametro ("cep", UtilitariosString.formataCEP (registroDeclaracao.fieldByName ("NR_CEP").asString ()));
	impressao.addParametro ("municipio", registroDeclaracao.fieldByName ("NM_MUNICIP").asString ());
	impressao.addParametro ("uf", registroDeclaracao.fieldByName ("SG_UF").asString ());
	String telefone = registroDeclaracao.fieldByName ("NR_TELEFONE").asString ();
	if (telefone.length () > 0)
	    telefone = "(" + registroDeclaracao.fieldByName ("NR_DDD_TELEFONE").asString () + ") " + telefone;
	impressao.addParametro ("telefone", telefone);
	if (registroDeclaracao.fieldByName ("IN_RETIFICADORA").asBoolean ())
	    impressao.addParametro ("retificadora", "SIM");
	else
	    impressao.addParametro ("retificadora", "N\u00c3O");
	impressao.addParametro ("Tributaveis", registroDeclaracao.fieldByName ("VR_TOTTRIB").asValor ().getConteudoFormatado ());
	impressao.addParametro ("devido", registroDeclaracao.fieldByName ("VR_IMPDEV").asValor ().getConteudoFormatado ());
	impressao.addParametro ("Restituir", registroDeclaracao.fieldByName ("VR_IMPREST").asValor ().getConteudoFormatado ());
	impressao.addParametro ("Especie", registroDeclaracao.fieldByName ("VR_GCIMPOSTOPAGO").asValor ().getConteudoFormatado ());
	impressao.addParametro ("Pagar", registroDeclaracao.fieldByName ("VR_IMPPAGAR").asValor ().getConteudoFormatado ());
	impressao.addParametro ("DATA", registroRecibo.fieldByName ("DIAREC").asString () + "/" + registroRecibo.fieldByName ("MESREC").asString () + "/" + registroRecibo.fieldByName ("ANOREC").asString ());
	impressao.addParametro ("HORA", registroRecibo.fieldByName ("HORAREC").asString () + ":" + registroRecibo.fieldByName ("MINREC").asString () + ":" + registroRecibo.fieldByName ("SEGREC").asString ());
	impressao.addParametro ("RW001", RW001);
	String nrRecibo = registroHeader.fieldByName ("NR_HASH").asString ();
	String dvNrRecibo = "" + Validador.calcularModulo11 (nrRecibo, null, 2);
	dvNrRecibo += Validador.calcularModulo11 (nrRecibo + dvNrRecibo, null, 2);
	String nrReciboForma = nrRecibo.substring (0, 2) + "." + nrRecibo.substring (2, 4) + "." + nrRecibo.substring (4, 6) + "." + nrRecibo.substring (6, 8) + "." + nrRecibo.substring (8, 10);
	impressao.addParametro ("HashSemDV", nrReciboForma);
	String numReciboComDV = nrReciboForma + " - " + dvNrRecibo;
	impressao.addParametro ("NUM_RECIBO_FORM_DV", numReciboComDV);
	impressao.addParametro ("ASS_RECIBO", registroRecibo.fieldByName ("ASSINATURA").asString ());
	if (registroDeclaracao.fieldByName ("VR_IMPPAGAR").asValor ().isVazio ()) {
	    if (! registroDeclaracao.fieldByName ("NR_BANCO").asString ().trim ().equals ("")) {
		impressao.addParametro ("lblRestitParcel", "RESTITUI\u00c7\u00c3O");
		impressao.addParametro ("lblBancoQuotas", "C\u00d3DIGO DO BANCO");
		impressao.addParametro ("BancoNumQuotas", registroDeclaracao.fieldByName ("NR_BANCO").asString ());
		impressao.addParametro ("AgenciaValor", "AG\u00caNCIA BANC\u00c1RIA");
		String dvAgencia = registroDeclaracao.fieldByName ("NR_DV_AGENCIA").asString ();
		String agencia = registroDeclaracao.fieldByName ("NR_AGENCIA").asString ();
		if (dvAgencia.trim ().length () > 0)
		    agencia += "-" + (String) dvAgencia;
		impressao.addParametro ("AgenciaValQuota", agencia);
		impressao.addParametro ("lblCodBancoOuCC", "CONTA PARA CR\u00c9DITO");
		String conta = registroDeclaracao.fieldByName ("NR_CONTA").asString ();
		conta += "-" + registroDeclaracao.fieldByName ("NR_DV_CONTA").asString ();
		impressao.addParametro ("codBancoOuCC", conta);
	    } else {
		impressao.addParametro ("lblCodBancoOuCC", "");
		impressao.addParametro ("codBancoOuCC", "");
	    }
	} else {
	    impressao.addParametro ("lblRestitParcel", "PARCELAMENTO (Vencimento da 1a quota em 30/04/" + ConstantesGlobais.EXERCICIO + ")");
	    impressao.addParametro ("lblBancoQuotas", "N\u00daMERO DE QUOTAS");
	    impressao.addParametro ("BancoNumQuotas", registroDeclaracao.fieldByName ("NR_QUOTAS").asString ());
	    impressao.addParametro ("AgenciaValor", "VALOR DA QUOTA");
	    impressao.addParametro ("AgenciaValQuota", registroDeclaracao.fieldByName ("VR_QUOTA").asValor ().getConteudoFormatado ());
	    if (! registroDeclaracao.fieldByName ("NR_BANCO").asString ().trim ().equals ("")) {
		impressao.addParametro ("comDebitoAutomatico", "true");
		impressao.addParametro ("lblCodBancoOuCC", "C\u00d3DIGO DO BANCO");
		impressao.addParametro ("codBancoOuCC", registroDeclaracao.fieldByName ("NR_BANCO").asString ());
		String dvAgencia = registroDeclaracao.fieldByName ("NR_DV_AGENCIA").asString ();
		String agencia = registroDeclaracao.fieldByName ("NR_AGENCIA").asString ();
		agencia += "-" + dvAgencia;
		impressao.addParametro ("agencia", agencia);
		String conta = registroDeclaracao.fieldByName ("NR_CONTA").asString ();
		conta += "-" + registroDeclaracao.fieldByName ("NR_DV_CONTA").asString ();
		impressao.addParametro ("conta", conta);
	    } else {
		impressao.addParametro ("lblCodBancoOuCC", "");
		impressao.addParametro ("codBancoOuCC", "");
	    }
	}
	String assCertDigital = registroRecibo.fieldByName ("NI_ASSINATURA_DECL").asString ();
	if (assCertDigital != null && (assCertDigital.trim ().length () == 11 || assCertDigital.trim ().length () == 14)) {
	    if (assCertDigital.trim ().length () == 11)
		assCertDigital = UtilitariosString.formataCPF (assCertDigital);
	    else
		assCertDigital = UtilitariosString.formataCNPJ (assCertDigital);
	    impressao.addParametro ("LBL_ASS_CERT_DIGITAL", "Esta declara\u00e7\u00e3o foi assinada com o certificado digital do NI " + assCertDigital);
	} else
	    impressao.addParametro ("LBL_ASS_CERT_DIGITAL", "");
	if (registroMulta != null && registroMulta.fieldByName ("IN_ACAO_FISCAL").asString ().equals ("1"))
	    impressao.addParametro ("contribSobFiscal", "Esta declara\u00e7\u00e3o est\u00e1 sendo apresentada ap\u00f3s o in\u00edcio de procedimento fiscal.\nSomente alimentar\u00e3o a base de dados da SRF as informa\u00e7\u00f5es das seguintes fichas:\n1 - IDENTIFICA\u00c7\u00c3O DO CONTRIBUINTE\n2 - BENS E DIREITOS\n3 - D\u00cdVIDAS E \u00d4NUS REAIS\nAs demais informa\u00e7\u00f5es que est\u00e3o sendo alteradas devem ser entregues \u00e0 fiscaliza\u00e7\u00e3o.");
	if (registroMulta != null && registroMulta.fieldByName ("NR_DISTRIBUICAO").asString ().trim ().length () > 0) {
	    String codNotificacao = registroMulta.fieldByName ("NR_DISTRIBUICAO").asString ();
	    impressao.addParametro ("Notificacao", codNotificacao.substring (0, codNotificacao.length () - 2) + "-" + codNotificacao.substring (codNotificacao.length () - 2, codNotificacao.length ()));
	    String lblCodNotificacao = "C\u00d3DIGO DA NOTIFICA\u00c7\u00c3O DE MULTA POR ATRASO NA ENTREGA DA DECLARA\u00c7\u00c3O";
	    impressao.addParametro ("lblNotificacao", lblCodNotificacao);

	    RegistroTxt pRegMulta = registroMulta;
	    RegistroTxt pRegDec = registroDeclaracao;

	    impressao.addImpressaoDeclaracao ("Notifica\u00e7\u00e3o de Lan\u00e7amento", "relNotificacao.jasper", null, null);
	    Valor tempoAtraso = pRegMulta.fieldByName ("QT_MESES").asValor ();
	    impressao.addParametroUltimo ("DtEntregaNot", registroRecibo.fieldByName ("DIAREC").asString () + "/" + registroRecibo.fieldByName ("MESREC").asString () + "/" + registroRecibo.fieldByName ("ANOREC").asString ());
	    impressao.addParametroUltimo ("HrEntregaNot", registroRecibo.fieldByName ("HORAREC").asString () + ":" + registroRecibo.fieldByName ("MINREC").asString () + ":" + registroRecibo.fieldByName ("SEGREC").asString ());
	    impressao.addParametroUltimo ("MesesNot", pRegMulta.fieldByName ("QT_MESES").asString ());
	    impressao.addParametroUltimo ("impDev", pRegDec.fieldByName ("VR_IMPDEV").asValor ().getConteudoFormatado ());
	    if (tempoAtraso.comparacao (">", "20"))
		tempoAtraso.setConteudo ("20");
	    impressao.addParametroUltimo ("tempoAtrasoMax", tempoAtraso.getConteudoFormatado ());
	    Valor impostoDevido = pRegDec.fieldByName ("VR_IMPDEV").asValor ();
	    Valor valorCalculado = impostoDevido.operacao ('*', tempoAtraso);
	    valorCalculado.append ('/', "100,00");
	    impressao.addParametroUltimo ("MultaCalc", valorCalculado.getConteudoFormatado ());
	    impressao.addParametro ("EXERCICIO", ConstantesGlobais.EXERCICIO);
	    impressao.addParametro ("ANO_CALENDARIO", ConstantesGlobais.ANO_BASE);
	    impressao.addParametro ("ReciboNot", numReciboComDV);
	    String strCodNot = pRegMulta.fieldByName ("NR_DISTRIBUICAO").asString ();
	    if (strCodNot != null && strCodNot.length () > 2)
		strCodNot = strCodNot.substring (0, strCodNot.length () - 2) + "-" + strCodNot.substring (strCodNot.length () - 2);
	    impressao.addParametro ("CodNot", strCodNot);
	    impressao.addParametro ("MunicNot", pRegDec.fieldByName ("NM_MUNICIP").asString ());
	    Valor valMultaFixa = new Valor ();
	    valMultaFixa.setConteudo (pRegMulta.fieldByName ("VR_MULTA").asValor ());
	    impressao.addParametroUltimo ("MultaFixa", valMultaFixa.getConteudoFormatado ());
	    impressao.addParametroUltimo ("Multa", valMultaFixa.getConteudoFormatado ());
	    impressao.addParametroUltimo ("CONSTANTE_VAL_MULTA", ConstantesGlobais.MULTA_POR_ATRASO_ENTREGA.getConteudoFormatado ());
	    impressao.addParametroUltimo ("NOME_AUDITOR_TECNICO", pRegMulta.fieldByName ("NM_DELEGADO").asString ());
	    int cargo = pRegMulta.fieldByName ("NR_CARGO").asInteger ();
	    if (cargo == 1)
		impressao.addParametroUltimo ("CARGO", "AUDITOR FISCAL DA RECEITA FEDERAL");
	    else if (cargo == 2)
		impressao.addParametroUltimo ("CARGO", "T\u00c9CNICO DA RECEITA FEDERAL");
	    impressao.addParametroUltimo ("MATRIC_AUDITOR_TECNICO", pRegMulta.fieldByName ("NR_MATRIC_DELEGADO").asString ());
	    String nomeDelegacia = "";
	    String codDelegacia = pRegMulta.fieldByName ("NR_UA").asString ();
	    nomeDelegacia = pRegMulta.fieldByName ("TP_DELEGACIA").asString ();
	    nomeDelegacia += " " + pRegMulta.fieldByName ("NM_UA").asString ();
	    impressao.addParametroUltimo ("NOME_DELEGACIA", nomeDelegacia);
	} else {
	    impressao.addParametro ("lblNotificacao", "");
	    impressao.addParametro ("Notificacao", "");
	}
	
	impressao.imprimir ();
*/
	System.out.println ("ainda não funciona :-(");
    }

    public static void main(String[] argv) {
	try {
	    PlataformaPPGD.setEmDesign(false);
	    int ret = 0;

	    if (argv.length == 1 && "leiame".equals (argv[0])) {
		leiame ();
	    } else if (argv.length != 2) {
		ajuda ();
	    } else if ("nova".equals (argv[0])) {
		nova (argv[1]);
	    } else if ("importa".equals (argv[0])) {
		importa (argv[1]);
	    } else if ("grava".equals (argv[0])) {
		grava (argv[1]);
	    } else if ("restaura".equals (argv[0])) {
		restaura (argv[1]);
	    } else if ("verifica".equals (argv[0])) {
		ret = verifica (argv[1]) ? 1 : 0;
	    } else if ("prepara".equals (argv[0])) {
		prepara (argv[1]);
	    } else if ("resumo".equals (argv[0])) {
		resumo (argv[1]);
	    } else if ("completa".equals (argv[0])) {
		completa (argv[1]);
	    } else if ("recibo".equals (argv[0])) {
		recibo (argv[1]);
	    } else {
		ajuda ();
	    }
	    System.exit (ret);
	} catch (Exception e) {
	    e.printStackTrace ();
	    System.exit (1);
	}
    }
}
