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

import net.sf.jasperreports.engine.JasperCompileManager;

public class JRCompile {
    public static void main(String[] argv) {
	if (argv.length == 0) {
	    System.out.println ("Compila arquivos .jrxml listados como argumentos para .jasper");
	}
	for (int i = 0; i < argv.length; i++)
	    try {
		String out;
		int len = argv[i].length ();
		if (len > 6 &&
		    argv[i].substring (len - 6, len).equals (".jrxml"))
		    out = argv[i].substring (0, len - 6);
		else
		    out = argv[i];
		out += ".jasper";
		JasperCompileManager.compileReportToFile(argv[i], out);
	    } catch (Exception e) {
		e.printStackTrace ();
		System.out.println ("enquanto compilava " + argv[i]);
	    }
    }
}
