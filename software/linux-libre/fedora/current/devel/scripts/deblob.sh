#! /bin/sh

# by Alexandre Oliva <lxoliva@fsfla.org>, <aoliva@redhat.com>

kver=$1

if test ! -f linux-$kver.tar.bz2; then
  echo linux-$kver.tar.bz2 does not exist >&2
  exit 1
fi

if test ! -f deblob; then
  echo deblob does not exist >&2
  exit 1
fi

if test -f libre-linux-$kver.tar.bz2; then
  echo libre-linux-$kver.tar.bz2 already exists >&2
  exit 1
fi

if test -f linux-$kver.tar; then
  echo linux-$kver.tar already exists >&2
  exit 1
fi

if test -f libre-linux-$kver.tar; then
  echo libre-linux-$kver.tar already exists >&2
  exit 1
fi

if test -f libre-linux-$kver.patch; then
  echo libre-linux-$kver.patch already exists >&2
  exit 1
fi

if test -f libre-linux-$kver.xdelta; then
  echo libre-linux-$kver.xdelta already exists >&2
  exit 1
fi

if test -d linux-$kver; then
  echo linux-$kver already exists >&2
  exit 1
fi

if test -d libre-linux-$kver; then
  echo libre-linux-$kver already exists >&2
  exit 1
fi

if test -d orig-linux-$kver; then
  echo orig-linux-$kver already exists >&2
  exit 1
fi

trap "status=$?; echo cleaning up...; rm -rf orig-linux-$kver linux-$kver libre-linux-$kver linux-$kver.tar libre-linux-$kver.tar libre-linux-$kver.tar.bz2 libre-linux-$kver.patch libre-linux-$kver.xdelta; (exit $status); exit" 0 1 2 15

rm -rf linux-$kver linux-$kver.tar
bunzip2 < linux-$kver.tar.bz2 > linux-$kver.tar
tar -xf linux-$kver.tar
rm -rf libre-linux-$kver libre-linux-$kver.tar
cp linux-$kver.tar libre-linux-$kver.tar
cp -lR linux-$kver/. libre-linux-$kver
(cd libre-linux-$kver && /bin/sh ../deblob)
rm -f libre-linux-$kver.patch
diff -druN linux-$kver libre-linux-$kver > libre-linux-$kver.patch

diff -rq linux-$kver libre-linux-$kver |
sed -n "
  s,^Only in \(linux-$kver/.*\): \(.*\),\1/\2,p;
  s,^Files \(linux-$kver/.*\) and libre-\1 differ,\1,p;
" |
xargs tar --delete -f libre-linux-$kver.tar

rm -rf orig-linux-$kver
mv linux-$kver orig-linux-$kver
mv libre-linux-$kver linux-$kver
diff -rq orig-linux-$kver linux-$kver |
sed -n "
  s,^Files orig-\(linux-$kver/.*\) and \1 differ,\1,p;
  s,^Only in \(linux-$kver/.*\): \(.*\),\1/\2,p;
" |
xargs tar --append -f libre-linux-$kver.tar

rm -rf linux-$kver orig-linux-$kver

xdelta delta -0 linux-$kver.tar libre-linux-$kver.tar libre-linux-$kver.xdelta

rm -f linux-$kver.tar
bzip2 -9 libre-linux-$kver.tar

trap "status=$?; (exit $status); exit" 0 1 2 15

gpg -a --detach-sign libre-linux-$kver.tar.bz2
mv libre-linux-$kver.tar.bz2.asc libre-linux-$kver.tar.bz2.sign

echo Please review libre-linux-$kver.patch

exit 0
