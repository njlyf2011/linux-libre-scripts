#! /bin/sh

# Copyright 2007 Alexandre Oliva, FSFLA

# This file is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

case $# in
0) set fnord `cat srclist`; shift ;;
esac

# Use .class notation instead of class$ method.  Also handle array classes.
# Delete class$ and array$ Class variables.
sed -i 's,\(class\$.*\) == null ? \1 = class\$ ("\([^"]*\)") : \1,\2.class,g; s,\(array\$.*\) == null ? \1 = class\$ ("\[L\([^"]*\);") : \1,\2[].class,g; /\/\*synthetic\*\/ static Class \(class\|array\)\$[^ 0-9]/d; /^  \/\*synthetic\*\/ static Class class\$ (/,/^  }/d' "$@"

# Replace class$<number> if/try/catch initializer blocks with .class notation.
sed -i '/Class \([^ =;]*\) = class\$[0-9]*/ { : rep; N; s,Class \([^ =;]*\) = class\$[0-9]*;.*if.*{.*try.*{.* = Class.forName ("\([^"]*\)").*}.*catch.*{.*}.*=.*}$,Class \1 = \2.class;,; t end; b rep; } ; : end; /\/\*synthetic\*\/ static Class class\$[0-9]*/d' "$@"

# Adjust Class var = new Class; Other vars; ((UNCONSTRUCTED)var).Class (...);
# to     Other vars; Class var = new Class (...);
sed -i -n '/\([A-Za-z_][A-Za-z_0-9]*\) [A-Za-z_][A-Za-z_0-9]* = new \1;/{ h; : rep; n; /UNCONSTRUCTED/ ! { p; b rep; }; H; x; s,\([A-Za-z_][A-Za-z_0-9]*\) \([A-Za-z_][A-Za-z_0-9]*\) = new \1;.*((UNCONSTRUCTED)\2)\.\1,\1 \2 = new \1,; }; p' "$@"

# Turn (UNCONSTRUCTED) call in the beginning of a constructor
# into a this() or super() call.
sed -i '/\(public \)\?[A-Za-z_][A-Za-z_0-9]* (/{ N;N;N;N; s,\(\(public \)\?\([A-Za-z_][A-Za-z_0-9]*\) (.*).*{.*\)\3 \([A-Za-z_][A-Za-z_0-9]*\) = this;.*Class var_class = \(.*\.class\);.*((UNCONSTRUCTED)\4)\.\3 (var_class,\1this (\5,; s,\(\(public \)\?\([A-Za-z_][A-Za-z_0-9]*\) (.*).*{.*\)\3 \([A-Za-z_][A-Za-z_0-9]*\) = this;.*Class var_class = \(.*\.class\);.*((UNCONSTRUCTED)\4)\.[A-Za-z_][A-Za-z_0-9]* (var_class,\1super (\5,; }' "$@"

# Fix MISSING MONITORENTER
sed -i '/MONITORENTER/{ N;N; s,MONITORENTER (\(.*\) = \(.*\));.*MISSING MONITORENTER.*synchronized (\1),synchronized (\1 = \2),; }' "$@"

# Fix anonymous inner classes with references to local variables: make
# such local variables final, remove them from constructor arguments,
# remove null-named const ructor and synthetic fields used to
# reference them, and adjust references to the fields to refer to the
# local variables directly.

file=serpro/ppgd/irpf/dependentes/Dependente.java
case " $@ " in
*" $file "*)
  grep " null (" $file > /dev/null &&
  patch -p0 $file <<\EOF
Index: serpro/ppgd/irpf/dependentes/Dependente.java
===================================================================
--- serpro/ppgd/irpf/dependentes/Dependente.java	(revision 915)
+++ serpro/ppgd/irpf/dependentes/Dependente.java	(revision 916)
@@ -52,31 +52,23 @@
       }
     });
     String ANO_01_01_1984 = "01/01/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 22);
-    Data data22Anos = new Data ();
+    final Data data22Anos = new Data ();
     data22Anos.setConteudo (ANO_01_01_1984);
     String ANO_01_01_1986 = "01/01/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 20);
-    Data data20Anos = new Data ();
+    final Data data20Anos = new Data ();
     data20Anos.setConteudo (ANO_01_01_1986);
     String ANO_01_01_1981 = "01/01/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 25);
-    Data data25Anos = new Data ();
+    final Data data25Anos = new Data ();
     data25Anos.setConteudo (ANO_01_01_1981);
     String ANO_31_12_1985 = "31/12/" + (Integer.parseInt (ConstantesGlobais.EXERCICIO_ANTERIOR) - 21);
-    Data data21Anos = new Data ();
+    final Data data21Anos = new Data ();
     data21Anos.setConteudo (ANO_31_12_1985);
     getCpfDependente ().addValidador (new ValidadorCPF ((byte) 3));
-    getCpfDependente ().addValidador (new ValidadorNaoNulo ((byte) 3, data20Anos)
+    getCpfDependente ().addValidador (new ValidadorNaoNulo ((byte) 3)
     {
-      /*synthetic*/ private final Data val$data20Anos;
-      
-      null (byte $anonymous0, Data data)
-      {
-	super ($anonymous0);
-	val$data20Anos = data;
-      }
-      
       public RetornoValidacao validarImplementado ()
       {
-	if ((getCodigo ().asString ().equals ("11") || getCodigo ().asString ().equals ("21") || getCodigo ().asString ().equals ("24") || getCodigo ().asString ().equals ("41") || getCodigo ().asString ().equals ("31")) && getCpfDependente ().isVazio () && ! getDataNascimento ().isVazio () && getDataNascimento ().maisAntiga (val$data20Anos))
+	if ((getCodigo ().asString ().equals ("11") || getCodigo ().asString ().equals ("21") || getCodigo ().asString ().equals ("24") || getCodigo ().asString ().equals ("41") || getCodigo ().asString ().equals ("31")) && getCpfDependente ().isVazio () && ! getDataNascimento ().isVazio () && getDataNascimento ().maisAntiga (data20Anos))
 	  return new RetornoValidacao (tab.msg ("dependente_cpf_brancoinvalido"), (byte) 3);
 	return null;
       }
@@ -109,38 +101,20 @@
 	return null;
       }
     });
-    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3, data21Anos, data25Anos)
+    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3)
     {
-      /*synthetic*/ private final Data val$data21Anos;
-      /*synthetic*/ private final Data val$data25Anos;
-      
-      null (byte $anonymous0, Data data, Data data_11_)
-      {
-	super ($anonymous0);
-	val$data21Anos = data;
-	val$data25Anos = data_11_;
-      }
-      
       public RetornoValidacao validarImplementado ()
       {
-	if ((getCodigo ().asString ().equals ("22") || getCodigo ().asString ().equals ("25")) && ! getDataNascimento ().isVazio () && (getDataNascimento ().maisNova (val$data21Anos) || getDataNascimento ().maisAntiga (val$data25Anos)))
+	if ((getCodigo ().asString ().equals ("22") || getCodigo ().asString ().equals ("25")) && ! getDataNascimento ().isVazio () && (getDataNascimento ().maisNova (data21Anos) || getDataNascimento ().maisAntiga (data25Anos)))
 	  return new RetornoValidacao (tab.msg ("dependente_data_incompativel"), (byte) 3);
 	return null;
       }
     });
-    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3, data22Anos)
+    getDataNascimento ().addValidador (new ValidadorNaoNulo ((byte) 3)
     {
-      /*synthetic*/ private final Data val$data22Anos;
-      
-      null (byte $anonymous0, Data data)
-      {
-	super ($anonymous0);
-	val$data22Anos = data;
-      }
-      
       public RetornoValidacao validarImplementado ()
       {
-	if ((getCodigo ().asString ().equals ("21") || getCodigo ().asString ().equals ("24") || getCodigo ().asString ().equals ("41")) && ! getDataNascimento ().isVazio () && getDataNascimento ().maisAntiga (val$data22Anos))
+	if ((getCodigo ().asString ().equals ("21") || getCodigo ().asString ().equals ("24") || getCodigo ().asString ().equals ("41")) && ! getDataNascimento ().isVazio () && getDataNascimento ().maisAntiga (data22Anos))
 	  return new RetornoValidacao (tab.msg ("dependente_data_incompativel"), (byte) 3);
 	return null;
       }
EOF
;;
esac

# We don't use the patches below any longer

# Use ImageIO interfaces to read and write jpeg files (untested).

file=serpro/ppgd/formatosexternos/barcodedesigners/DefaultBarCode2DRenderer.java
case "" in # this patch is disabled: " $@ " in
*" $file "*)
  fgrep javax.imageio.ImageIo $file > /dev/null ||
  patch -p0 $file <<\EOF
Index: serpro/ppgd/formatosexternos/barcodedesigners/DefaultBarCode2DRenderer.java
===================================================================
--- serpro/ppgd/formatosexternos/barcodedesigners/DefaultBarCode2DRenderer.java	(revision 916)
+++ serpro/ppgd/formatosexternos/barcodedesigners/DefaultBarCode2DRenderer.java	(revision 918)
@@ -7,8 +7,7 @@
 import java.awt.image.BufferedImage;
 import java.io.FileOutputStream;
 
-import com.sun.image.codec.jpeg.JPEGCodec;
-import com.sun.image.codec.jpeg.JPEGImageEncoder;
+import javax.imageio.ImageIO;
 
 import serpro.ppgd.negocio.util.LogPPGD;
 
@@ -64,9 +63,9 @@
     g1 = g;
     try
       {
-	JPEGImageEncoder ImgEnc = JPEGCodec.createJPEGEncoder (new FileOutputStream ("e:\\testeElizier.jpg"));
-	ImgEnc.encode (Bi);
-	ImgEnc.getOutputStream ().close ();
+	FileOutputStream os = new FileOutputStream ("e:\\testeElizier.jpg");
+	ImageIO.write (Bi, "jpeg", os);
+	os.close ();
       }
     catch (Exception e)
       {
@@ -108,9 +107,9 @@
       }
     try
       {
-	JPEGImageEncoder ImgEnc = JPEGCodec.createJPEGEncoder (new FileOutputStream (arquivo));
-	ImgEnc.encode (Bi);
-	ImgEnc.getOutputStream ().close ();
+	FileOutputStream os = new FileOutputStream (arquivo);
+	ImageIO.write (Bi, "jpeg", os);
+	os.close ();
       }
     catch (Exception e)
       {
EOF
;;
esac

file=serpro/ppgd/negocio/util/Darf.java
case "" in # this patch is disabled: " $@ " in
*" $file "*)
  fgrep javax.imageio.ImageIo $file > /dev/null ||
  patch -p0 <<\EOF
Index: serpro/ppgd/negocio/util/Darf.java
===================================================================
--- serpro/ppgd/negocio/util/Darf.java	(revision 916)
+++ serpro/ppgd/negocio/util/Darf.java	(revision 918)
@@ -7,8 +7,7 @@
 import java.awt.print.PrinterJob;
 import java.io.FileInputStream;
 
-import com.sun.image.codec.jpeg.JPEGCodec;
-import com.sun.image.codec.jpeg.JPEGImageDecoder;
+import javax.imageio.ImageIO;
 
 import serpro.ppgd.negocio.ConstantesGlobais;
 
@@ -108,8 +107,7 @@
       {
 	String pathBrasao = this.getClass ().getResource ("/imagens/brasao.jpg").getFile ();
 	FileInputStream in = new FileInputStream (pathBrasao);
-	JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder (in);
-	BufferedImage m_bi1 = decoder.decodeAsBufferedImage ();
+	BufferedImage m_bi1 = ImageIO.read (in);
 	in.close ();
 	m_panel.setBufferedImage (m_bi1);
       }
EOF
;;
esac
