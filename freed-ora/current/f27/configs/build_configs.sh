#!/bin/bash
#
# This script merges together the hierarchy of CONFIG_* files under generic
# and debug to form the necessary $PACKAGE_NAME<version>-<arch>-<variant>.config
# files for building RHEL kernels, based on the contents of a control file

PACKAGE_NAME="${1:-kernel}" # defines the package name used
KVERREL="${2:-}"
SUBARCH="${3:-}" # defines a specific arch
SCRIPT="$(readlink -f $0)"
OUTPUT_DIR="$PWD"
SCRIPT_DIR="$(dirname $SCRIPT)"

LANG=en_US.UTF-8

# to handle this script being a symlink
cd $SCRIPT_DIR

set errexit
set nounset

control_file="config_generation"

cleanup()
{
	rm -f config-*
}

die()
{
	echo "$1"
	cleanup
	exit 1
}

function combine_config_layer()
{
	dir=$1
	file="config-$(echo $dir | sed -e 's|/|-|g')"

	if [ $(ls $dir/ | grep -c "^CONFIG_") -eq 0 ]; then
		touch $file
		return
	fi

	cat $dir/CONFIG_* > $file
}

function merge_configs()
{
	archvar=$1
	arch=$(echo "$archvar" | cut -f1 -d"-")
	configs=$2
	order=$3
	name=$OUTPUT_DIR/$PACKAGE_NAME-$archvar.config
	echo -n "Building $name ... "
	touch config-merging config-merged

	# apply based on order
	skip_if_missing=""
	for o in $order
	do
		for config in $(echo $configs | sed -e 's/:/ /g')
		do
			cfile="config-$o-$config"

			test -n "$skip_if_missing" && test ! -e $cfile && continue

			perl merge.pl $cfile config-merging > config-merged
			if [ ! $? -eq 0 ]; then
				die "Failed to merge $cfile"
			fi
			mv config-merged config-merging
		done

		# first configs in $order is baseline, all files should be
		# there.  second pass is overrides and can be missing.
		skip_if_missing="1"
	done
	if [ "x$arch" == "xaarch64" ]; then
		echo "# arm64" > $name
	elif [ "x$arch" == "xppc64" ]; then
		echo "# powerpc" > $name
	elif [ "x$arch" == "xppc64le" ]; then
		echo "# powerpc" > $name
	elif [ "x$arch" == "xs390x" ]; then
		echo "# s390" > $name
	elif [ "x$arch" == "xarmv7hl" ]; then
		echo "# arm" > $name
	elif [ "x$arch" == "xi686" ]; then
		echo "# i386" > $name
	else
		echo "# $arch" > $name
	fi
	sort config-merging >> $name
	rm -f config-merged config-merging
	echo "done"
}

while read line
do
	if [ $(echo "$line" | grep -c "^#") -ne 0 ]; then
		continue
	elif [ $(echo "$line" | grep -c "^$") -ne 0 ]; then
		continue
	elif [ $(echo "$line" | grep -c "^ORDER") -ne 0 ]; then
		order=$(echo "$line" | cut -f2 -d"=")
		for o in $order
		do
			glist=$(find $o -type d)
			for d in $glist
			do
				combine_config_layer $d
			done
		done
	else
		arch=$(echo "$line" | cut -f1 -d"=")
		configs=$(echo "$line" | cut -f2 -d"=")

		if [ -n "$SUBARCH" -a "$SUBARCH" != "$arch" ]; then
			continue
		fi

		merge_configs $arch $configs "$order"
	fi
done < $control_file

# A passed in kernel version implies copy to final location
# otherwise defer to another script
if test -n "$KVERREL"
then
	for i in kernel-*.config
	do
		NEW="$(echo $i | sed "s/$PACKAGE_NAME-$SUBARCH/$PACKAGE_NAME-$KVERREL-$SUBARCH/")"
		mv $i $NEW
	done
fi

cleanup
