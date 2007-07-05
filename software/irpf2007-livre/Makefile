all:

JAVAC = gcj -C -w -I src
GCJ = gcj
GCJFLAGS = -O2 -fnem-tente-por-enquanto

JAVA = gij
JAVACP = :/usr/share/java/libgcj-4.1.2.jar

CORE-SOURCES = $(shell cat list/core-sources)
MORE-SOURCES = $(shell cat list/more-sources)
SRC-RESOURCES = $(shell cat list/src-resources)
RES-RESOURCES = $(shell sed 's, ,\\ ,g' list/res-resources)
LICENSES = $(shell cat list/licenses)

.PHONY: all-gui
all: irpf2007-cl.jar # all-gui
all-gui: list/extra-classes irpf2007.jar

.PHONY: clean
clean:
	-test ! -f list/classes || (cd src && xargs rm -f) < list/classes
	-find src -name \*.class -print | xargs rm -f list/classes
	-rm -f *.T list/*.T Log.txt TryLock.txt
	-rm -rf jar

.PHONY: distclean
distclean: clean
	-rm -f irpf2007.jar irpf2007-cl.jar list/extra-classes

src/org/fsfla/irpf2007/Main.class: \
  src/org/fsfla/irpf2007/Main.java
	$(JAVAC) src/org/fsfla/irpf2007/Main.java

irpf2007-cl.jar: src/org/fsfla/irpf2007/Main.class \
		 list/rcl-resources list/licenses LEIAME
	rm -rf jar
	mkdir jar
	{ find src -name \*.class -print; echo src/META-INF/MANIFEST.MF; \
	  cat list/licenses; } | sed 's,^src/,,' | \
	  (cd src && cpio -o) | (cd jar && cpio -i --make-dir)
	sed 's,^res/,,' list/rcl-resources | \
	  (cd res && cpio -o) | (cd jar && cpio -i --make-dir)
	cp -p LEIAME jar
	(cd jar && zip -9qmr - .) > $@.T
	rmdir jar
	mv $@.T $@

list/classes: $(CORE-SOURCES) $(MORE-SOURCES) \
	      list/core-sources list/more-sources
	-find src -name \*.class -print | \
	  if test -f list/classes; then fgrep -v -f list/classes; \
	  else cat; fi | \
	  xargs rm -f list/classes
	$(JAVAC) @list/core-sources @list/more-sources
	-rm -f $@.T
	find src -name \*.class -print > $@.T
	-rm -f $@
	mv $@.T $@

irpf2007.jar: $(SRC-RESOURCES) $(LICENSES) $(RES-RESOURCES) list/extra-classes \
	      list/classes list/src-resources list/licenses list/res-resources \
	      LEIAME
	-rm -rf jar
	mkdir jar
	sed 's,^src/,,' list/classes list/src-resources list/licenses | \
	  (cd src && cpio -o) | (cd jar && cpio -i --make-dir)
	sed 's,^res/,,' list/res-resources | \
	  (cd res && cpio -o) | (cd jar && cpio -i --make-dir)
	cp -p LEIAME jar
	(cd jar && zip -9qmr - .) > $@.T
	rmdir jar
	mv $@.T $@

# Isso devia funcionar, mas deixei o GCJ rodando por horas numa
# máquina com um tanto bem razoável de RAM, ela fazendo swap que nem
# louca, e quase nada de progresso na compilação.  Depois que o uso de
# swap passou de 4GB, desisti.  Se quiser experimentar, troque
# GCJFLAGS por algo que o gcj não rejeite ;-)
%: %.jar
	$(GCJ) $(GCJFLAGS) $< -o $@

# Verifica se não estamos trazendo classes indesejadas.
list/excluded-sources: list/core-sources list/more-sources
	-find src -name \*.java | \
	  fgrep -v -f list/core-sources | \
	  fgrep -v -f list/more-sources > $@.T
	test ! -s $@.T
	mv $@.T $@

list/extra-classes: list/excluded-sources list/classes
	-sed 's,\.class,\.java,' list/classes | \
	  fgrep -f list/excluded-sources > $@.T
	test ! -s $@.T
	mv $@.T $@

# Use isto depois de fazer um upgrade do jasperreports.
jasper-rebuild: list/classes
	rm -f res/*.jasper
	$(JAVA) -cp src:res$(JAVACP) org.fsfla.irpf2007.JRCompile res/*.jrxml
