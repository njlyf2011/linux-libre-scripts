#! /bin/sh

# by Alexandre Oliva <lxoliva@fsfla.org>, <aoliva@redhat.com>

kver=$1

if test ! -f linux-$kver.tar.bz2; then
  echo linux-$kver.tar.bz2 does not exist >&2
  exit 1
fi

if test ! -f deblob-$kver; then
  echo deblob-$kver does not exist >&2
  exit 1
fi

if test -f linux-libre-$kver.tar.bz2; then
  echo linux-libre-$kver.tar.bz2 already exists >&2
  exit 1
fi

if test -f linux-libre-$kver.patch; then
  echo linux-libre-$kver.patch already exists >&2
  exit 1
fi

if test -d linux-$kver; then
  echo linux-$kver already exists >&2
  exit 1
fi

if test -d linux-libre-$kver; then
  echo linux-libre-$kver already exists >&2
  exit 1
fi

trap "status=$?; rm -rf linux-$kver linux-libre-$kver linux-libre-$kver.tar.bz2 linux-libre-$kver.patch; (exit $status); exit" 0 1 2 15

rm -rf linux-$kver
tar --use=bzip2 -xf linux-$kver.tar.bz2
rm -rf linux-libre-$kver
cp -lR linux-$kver/. linux-libre-$kver
(cd linux-libre-$kver && /bin/sh ../deblob-$kver)
rm -f linux-libre-$kver.patch
diff -druN linux-$kver linux-libre-$kver > linux-libre-$kver.patch
tar --use=bzip2 -cf linux-libre-$kver.tar.bz2 linux-libre-$kver
rm -rf linux-$kver linux-libre-$kver

trap "status=$?; (exit $status); exit" 0 1 2 15

gpg -a --detach-sign linux-libre-$kver.tar.bz2
mv linux-libre-$kver.tar.bz2.asc linux-libre-$kver.tar.bz2.sign

echo Please review linux-libre-$kver.patch

exit 0
