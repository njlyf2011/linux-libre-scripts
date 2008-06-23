Summary: The Linux kernel (the core of the GNU/Linux operating system)

# For a stable, released kernel, released_kernel should be 1. For rawhide
# and/or a kernel built from an rc or git snapshot, released_kernel should
# be 0.
%define released_kernel 0

# Versions of various parts

# Polite request for people who spin their own kernel rpms:
# please modify the "buildid" define in a way that identifies
# that the kernel isn't the stock distribution kernel, for example,
# by setting the define to ".local" or ".bz123456"
#
#% define buildid .local

# fedora_build defines which build revision of this kernel version we're
# building. Rather than incrementing forever, as with the prior versioning
# setup, we set fedora_cvs_origin to the current cvs revision s/1.// of the
# kernel spec when the kernel is rebased, so fedora_build automatically
# works out to the offset from the rebase, so it doesn't get too ginormous.
#
%define fedora_cvs_origin 623
%define fedora_build %(R="$Revision: 1.704 $"; R="${R%% \$}"; R="${R##: 1.}"; expr $R - %{fedora_cvs_origin})

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 25

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
%define librev 1

# To be inserted between "patch" and "-2.6.".
#define stablelibre -libre
%define rcrevlibre -libre
#define gitrevlibre -libre

# libres (s for suffix) may be bumped for rebuilds in which patches
# change but fedora_build doesn't.  Make sure it starts with a dot.
# It is appended after dist.
#define libres

## If this is a released kernel ##
%if 0%{?released_kernel}
# Do we have a 2.6.21.y update to apply?
%define stable_update 3
# Set rpm version accordingly
%if 0%{?stable_update}
%define stablerev .%{stable_update}
%endif
%define rpmversion 2.6.%{base_sublevel}%{?stablerev}

## The not-released-kernel case ##
%else
# The next upstream release sublevel (base_sublevel+1)
%define upstream_sublevel %(expr %{base_sublevel} + 1)
# The rc snapshot level
%define rcrev 7
# The git snapshot level
%define gitrev 0
# Set rpm version accordingly
%define rpmversion 2.6.%{upstream_sublevel}
%endif
# Nb: The above rcrev and gitrev values automagically define Patch00 and Patch01 below.

# What parts do we want to build?  We must build at least one kernel.
# These are the kernels that are built IF the architecture allows it.
# All should default to 1 (enabled) and be flipped to 0 (disabled)
# by later arch-specific checks.

# The following build options are enabled by default.
# Use either --without <opt> in your rpmbuild command or force values
# to 0 in here to disable them.
#
# standard kernel
%define with_up        %{?_without_up:        0} %{?!_without_up:        1}
# kernel-smp (only valid for ppc 32-bit, sparc64)
%define with_smp       %{?_without_smp:       0} %{?!_without_smp:       1}
# kernel-PAE (only valid for i686)
%define with_pae       %{?_without_pae:       0} %{?!_without_pae:       1}
# kernel-xen
%define with_xen       %{?_without_xen:       0} %{?!_without_xen:       1}
# kernel-kdump
%define with_kdump     %{?_without_kdump:     0} %{?!_without_kdump:     1}
# kernel-debug
%define with_debug     %{?_without_debug:     0} %{?!_without_debug:     1}
# kernel-doc
%define with_doc       %{?_without_doc:       0} %{?!_without_doc:       1}
# kernel-headers
%define with_headers   %{?_without_headers:   0} %{?!_without_headers:   1}
# kernel-debuginfo
%define with_debuginfo %{?_without_debuginfo: 0} %{?!_without_debuginfo: 1}
# kernel-bootwrapper (for creating zImages from kernel + initrd)
%define with_bootwrapper %{?_without_bootwrapper: 0} %{?!_without_bootwrapper: 1}

# Additional options for user-friendly one-off kernel building:
#
# Only build the base kernel (--with baseonly):
%define with_baseonly  %{?_with_baseonly:     1} %{?!_with_baseonly:     0}
# Only build the smp kernel (--with smponly):
%define with_smponly   %{?_with_smponly:      1} %{?!_with_smponly:      0}
# Only build the pae kernel (--with paeonly):
%define with_paeonly   %{?_with_paeonly:      1} %{?!_with_paeonly:      0}
# Only build the xen kernel (--with xenonly):
%define with_xenonly   %{?_with_xenonly:      1} %{?!_with_xenonly:      0}

# should we do C=1 builds with sparse
%define with_sparse	%{?_with_sparse:      1} %{?!_with_sparse:      0}

# Whether or not to apply the Xen patches -- leave this enabled
%define includexen 0
# Xen doesn't work with current upstream kernel, shut it off
%define with_xen 0

# Set debugbuildsenabled to 1 for production (build separate debug kernels)
#  and 0 for rawhide (all kernels are debug kernels).
# See also 'make debug' and 'make release'.
%define debugbuildsenabled 0

# Want to build a vanilla kernel build without any non-upstream patches?
# (well, almost none, we need nonintconfig for build purposes). Default to 0 (off).
%define with_vanilla %{?_with_vanilla: 1} %{?!_with_vanilla: 0}

# pkg_release is what we'll fill in for the rpm Release: field
%if 0%{?released_kernel}
%define pkg_release %{fedora_build}%{?buildid}%{?dist}%{?libres}
%else
%if 0%{?rcrev}
%define rctag .rc%rcrev
%endif
%if 0%{?gitrev}
%define gittag .git%gitrev
%if !0%{?rcrev}
%define rctag .rc0
%endif
%endif
%define pkg_release 0.%{fedora_build}%{?rctag}%{?gittag}%{?buildid}%{?dist}%{?libres}
%endif

# The kernel tarball/base version
%define kversion 2.6.%{base_sublevel}

%define make_target bzImage

%define xen_hv_cset 11633
%define xen_flags verbose=y crash_debug=y
%define xen_target vmlinuz
%define xen_image vmlinuz

%define KVERREL %{PACKAGE_VERSION}-libre.%{PACKAGE_RELEASE}.%{_target_cpu}
%define hdrarch %_target_cpu

%if 0%{!?nopatches:1}
%define nopatches 0
%endif

%if %{with_vanilla}
%define nopatches 1
%endif

%if %{nopatches}
%define includexen 0
%define with_xen 0
%define with_bootwrapper 0
%define variant -vanilla
%else
%define variant -libre
%define variant_fedora -libre-fedora
%endif

%define using_upstream_branch 0
%if 0%{?upstream_branch:1}
%define stable_update 0
%define using_upstream_branch 1
%define variant -%{upstream_branch}%{?variant_fedora}
%define pkg_release 0.%{fedora_build}%{upstream_branch_tag}%{?buildid}%{?dist}%{?libres}
%endif

%if !%{debugbuildsenabled}
%define with_debug 0
%endif

%if !%{with_debuginfo}
%define _enable_debug_packages 0
%endif
%define debuginfodir /usr/lib/debug

# if requested, only build base kernel
%if %{with_baseonly}
%define with_smp 0
%define with_pae 0
%define with_xen 0
%define with_kdump 0
%define with_debug 0
%endif

# if requested, only build smp kernel
%if %{with_smponly}
%define with_up 0
%define with_pae 0
%define with_xen 0
%define with_kdump 0
%define with_debug 0
%endif

# if requested, only build pae kernel
%if %{with_paeonly}
%define with_up 0
%define with_smp 0
%define with_xen 0
%define with_kdump 0
%define with_debug 0
%endif

# if requested, only build xen kernel
%if %{with_xenonly}
%define with_up 0
%define with_smp 0
%define with_pae 0
%define with_kdump 0
%define with_debug 0
%endif

%define all_x86 i386 i586 i686

# These arches install vdso/ directories.
%define vdso_arches %{all_x86} x86_64 ppc ppc64

# Overrides for generic default options

# only ppc and sparc64 need separate smp kernels
%ifnarch ppc sparc64 alphaev56
%define with_smp 0
%endif

# pae is only valid on i686
%ifnarch i686
%define with_pae 0
%endif

# xen only builds on i686, x86_64 and ia64
%ifnarch i686 x86_64 ia64
%define with_xen 0
%endif

# only build kernel-kdump on ppc64
# (no relocatable kernel support upstream yet)
%ifnarch ppc64
%define with_kdump 0
%endif

# don't do debug builds on anything but i686 and x86_64
%ifnarch i686 x86_64
%define with_debug 0
%endif

# only package docs noarch
%ifnarch noarch
%define with_doc 0
%endif

# no need to build headers again for these arches,
# they can just use i386 and ppc64 headers
%ifarch i586 i686 ppc64iseries
%define with_headers 0
%endif

# don't build noarch kernels or headers (duh)
%ifarch noarch
%define with_up 0
%define with_headers 0
%define all_arch_configs kernel-%{version}-*.config
%endif

# bootwrapper is only on ppc
%ifnarch ppc ppc64
%define with_bootwrapper 0
%endif

# sparse blows up on ppc64 alpha and sparc64
%ifarch ppc64 ppc alpha sparc64
%define with_sparse 0
%endif

# Per-arch tweaks

%ifarch %{all_x86}
%define all_arch_configs kernel-%{version}-i?86*.config
%define image_install_path boot
%define hdrarch i386
# we build always xen i686 HV with pae
%define xen_flags verbose=y crash_debug=y pae=y
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch x86_64
%define all_arch_configs kernel-%{version}-x86_64*.config
%define image_install_path boot
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch ppc64
%define all_arch_configs kernel-%{version}-ppc64*.config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%define kernel_image_elf 1
%define hdrarch powerpc
%endif

%ifarch s390x
%define all_arch_configs kernel-%{version}-s390x.config
%define image_install_path boot
%define make_target image
%define kernel_image arch/s390/boot/image
%define hdrarch s390
%endif

%ifarch sparc
# We only build sparc headers since we dont support sparc32 hardware
%endif

%ifarch sparc64
%define all_arch_configs kernel-%{version}-sparc64*.config
%define make_target image
%define kernel_image arch/sparc64/boot/image
%define image_install_path boot
%endif

%ifarch ppc
%define all_arch_configs kernel-%{version}-ppc{-,.}*config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%define kernel_image_elf 1
%define hdrarch powerpc
%endif

%ifarch ia64
%define all_arch_configs kernel-%{version}-ia64*.config
%define image_install_path boot/efi/EFI/redhat
%define make_target compressed
%define kernel_image vmlinux.gz
# ia64 xen HV doesn't building with debug=y at the moment
%define xen_flags verbose=y crash_debug=y
%define xen_target compressed
%define xen_image vmlinux.gz
%endif

%ifarch alpha alphaev56
%define all_arch_configs kernel-%{version}-alpha*.config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%endif

%if %{nopatches}
# XXX temporary until last vdso patches are upstream
%define vdso_arches ppc ppc64
%endif

%if %{nopatches}%{using_upstream_branch}
# Ignore unknown options in our config-* files.
# Some options go with patches we're not applying.
%define oldconfig_target loose_nonint_oldconfig
%else
%define oldconfig_target nonint_oldconfig
%endif

# To temporarily exclude an architecture from being built, add it to
# %nobuildarches. Do _NOT_ use the ExclusiveArch: line, because if we
# don't build kernel-headers then the new build system will no longer let
# us use the previous build of that package -- it'll just be completely AWOL.
# Which is a BadThing(tm).

# We don't build a kernel on i386; we only do kernel-headers there,
# and we no longer build for 31bit S390. Same for 32bit sparc.
%define nobuildarches i386 s390 sparc

%ifarch %nobuildarches
%define with_up 0
%define with_smp 0
%define with_pae 0
%define with_xen 0
%define with_kdump 0
%define with_debuginfo 0
%define _enable_debug_packages 0
%endif

%define with_pae_debug 0
%if %with_pae
%define with_pae_debug %{with_debug}
%endif

#
# Three sets of minimum package version requirements in the form of Conflicts:
# to versions below the minimum
#

#
# First the general kernel 2.6 required versions as per
# Documentation/Changes
#
%define kernel_dot_org_conflicts  ppp < 2.4.3-3, isdn4k-utils < 3.2-32, nfs-utils < 1.0.7-12, e2fsprogs < 1.37-4, util-linux < 2.12, jfsutils < 1.1.7-2, reiserfs-utils < 3.6.19-2, xfsprogs < 2.6.13-4, procps < 3.2.5-6.3, oprofile < 0.9.1-2

#
# Then a series of requirements that are distribution specific, either
# because we add patches for something, or the older versions have
# problems with the newer kernel or lack certain things that make
# integration in the distro harder than needed.
#
%define package_conflicts initscripts < 7.23, udev < 063-6, iptables < 1.3.2-1, ipw2200-firmware < 2.4, selinux-policy-targeted < 1.25.3-14

#
# The ld.so.conf.d file we install uses syntax older ldconfig's don't grok.
#
%define kernel_xen_conflicts glibc < 2.3.5-1, xen < 3.0.1

# upto and including kernel 2.4.9 rpms, the 4Gb+ kernel was called kernel-enterprise
# now that the smp kernel offers this capability, obsolete the old kernel
%define kernel_smp_obsoletes kernel-enterprise < 2.4.10
%define kernel_PAE_obsoletes kernel-smp < 2.6.17

#
# Packages that need to be installed before the kernel is, because the %post
# scripts use them.
#
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, mkinitrd >= 6.0.39-1

#
# This macro does requires, provides, conflicts, obsoletes for a kernel package.
#	%%kernel_reqprovconf <subpackage>
# It uses any kernel_<subpackage>_conflicts and kernel_<subpackage>_obsoletes
# macros defined above.
#
%define kernel_reqprovconf \
Provides: kernel = %{rpmversion}-%{pkg_release}\
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{pkg_release}%{?1:.%{1}}\
Provides: kernel-drm = 4.3.0\
Provides: kernel-drm-nouveau = 10\
Provides: kernel-modeset = 1\
Provides: kernel-uname-r = %{KVERREL}%{?1:.%{1}}\
Requires(pre): %{kernel_prereq}\
Conflicts: %{kernel_dot_org_conflicts}\
Conflicts: %{package_conflicts}\
%{?1:%{expand:%%{?kernel_%{1}_conflicts:Conflicts: %%{kernel_%{1}_conflicts}}}}\
%{?1:%{expand:%%{?kernel_%{1}_obsoletes:Obsoletes: %%{kernel_%{1}_obsoletes}}}}\
%{?1:%{expand:%%{?kernel_%{1}_provides:Provides: %%{kernel_%{1}_provides}}}}\
# We can't let RPM do the dependencies automatic because it'll then pick up\
# a correct but undesirable perl dependency from the module headers which\
# isn't required for the kernel proper to function\
AutoReq: no\
AutoProv: yes\
%{nil}

Name: kernel%{?variant}
Group: System Environment/Kernel
License: GPLv2
URL: http://www.kernel.org/
Version: %{rpmversion}
Release: %{pkg_release}
# DO NOT CHANGE THE 'ExclusiveArch' LINE TO TEMPORARILY EXCLUDE AN ARCHITECTURE BUILD.
# SET %%nobuildarches (ABOVE) INSTEAD
ExclusiveArch: noarch %{all_x86} x86_64 ppc ppc64 ia64 sparc sparc64 s390x alpha alphaev56
ExclusiveOS: Linux

%kernel_reqprovconf


#
# List the packages used during the kernel build
#
BuildRequires: module-init-tools, patch >= 2.5.4, bash >= 2.03, sh-utils, tar
BuildRequires: bzip2, findutils, gzip, m4, perl, make >= 3.78, diffutils, gawk
BuildRequires: gcc >= 3.4.2, binutils >= 2.12, redhat-rpm-config
BuildRequires: net-tools
%if %{with_doc}
BuildRequires: xmlto
%endif
%if %{with_sparse}
BuildRequires: sparse >= 0.4.1
%endif
BuildConflicts: rhbuildsys(DiskFree) < 500Mb

%define fancy_debuginfo 0
%if %{with_debuginfo}
%if 0%{?fedora} >= 8
%define fancy_debuginfo 1
%endif
%endif

%if %{fancy_debuginfo}
# Fancy new debuginfo generation introduced in Fedora 8.
BuildRequires: rpm-build >= 4.4.2.1-4
%define debuginfo_args --strict-build-id
%endif

Source0: linux-%{kversion}-libre%{?librev}.tar.bz2
#Source1: xen-%{xen_hv_cset}.tar.bz2
Source2: Config.mk

# For documentation purposes only.
Source3: deblob-main
Source4: deblob-%{kversion}
Source5: deblob-check

Source10: COPYING.modules
Source11: genkey
Source14: find-provides
Source15: merge.pl

Source20: Makefile.config
Source21: config-debug
Source22: config-nodebug
Source23: config-generic
Source24: config-xen-generic
Source25: config-rhel-generic
Source26: config-rhel-x86-generic

Source30: config-x86-generic
Source31: config-i586
Source32: config-i686
Source33: config-i686-PAE
Source34: config-xen-x86

Source40: config-x86_64-generic
Source41: config-xen-x86_64

Source50: config-powerpc-generic
Source51: config-powerpc32-generic
Source52: config-powerpc32-smp
Source53: config-powerpc64
Source54: config-powerpc64-kdump

Source60: config-ia64-generic
Source61: config-ia64
Source62: config-xen-ia64

Source70: config-s390x

Source90: config-sparc64-generic
Source91: config-sparc64
Source92: config-sparc64-smp

# Here should be only the patches up to the upstream canonical Linus tree.

# For a stable release kernel
%if 0%{?stable_update}
Patch00: patch%{?stablelibre}-2.6.%{base_sublevel}.%{stable_update}.bz2

# non-released_kernel case
# These are automagically defined by the rcrev and gitrev values set up
# near the top of this spec file.
%else
%if 0%{?rcrev}
Patch00: patch%{?rcrevlibre}-2.6.%{upstream_sublevel}-rc%{rcrev}.bz2
%if 0%{?gitrev}
Patch01: patch%{?gitrevlibre}-2.6.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}.bz2
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
Patch00: patch%{?gitrevlibre}-2.6.%{base_sublevel}-git%{gitrev}.bz2
%endif
%endif
%endif

%if %{using_upstream_branch}
### BRANCH PATCH ###
%endif

# stable release candidate
# Patch03: patch-2.6.24.1-rc1.bz2

# we always need nonintconfig, even for -vanilla kernels
Patch06: linux-2.6-build-nonintconfig.patch

# we also need compile fixes for -vanilla
Patch07: linux-2.6-compile-fixes.patch
#Patch08: linux-2.6-compile-fix-gcc-43.patch

%if !%{nopatches}

Patch10: linux-2.6-hotfixes.patch

Patch21: linux-2.6-utrace.patch

Patch41: linux-2.6-sysrq-c.patch
Patch42: linux-2.6-x86-tune-generic.patch
Patch75: linux-2.6-x86-debug-boot.patch

Patch140: linux-2.6-ps3-ehci-iso.patch
Patch141: linux-2.6-ps3-storage-alias.patch
Patch142: linux-2.6-ps3-legacy-bootloader-hack.patch
Patch143: linux-2.6-g5-therm-shutdown.patch
Patch144: linux-2.6-vio-modalias.patch
Patch147: linux-2.6-imac-transparent-bridge.patch
Patch148: linux-2.6-powerpc-zImage-32MiB.patch
Patch149: linux-2.6-efika-not-chrp.patch

Patch160: linux-2.6-execshield.patch
Patch250: linux-2.6-debug-sizeof-structs.patch
Patch260: linux-2.6-debug-nmi-timeout.patch
Patch270: linux-2.6-debug-taint-vm.patch
Patch280: linux-2.6-debug-spinlock-taint.patch
Patch340: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch370: linux-2.6-crash-driver.patch
Patch380: linux-2.6-defaults-pci_no_msi.patch
Patch400: linux-2.6-scsi-cpqarray-set-master.patch
Patch402: linux-2.6-scsi-mpt-vmware-fix.patch
Patch410: linux-2.6-alsa-kill-annoying-messages.patch
Patch411: linux-2.6-hda-intel-fix-dma-position-inaccuracy.patch
Patch420: linux-2.6-squashfs.patch
Patch430: linux-2.6-net-silence-noisy-printks.patch
Patch440: linux-2.6-net-8139-pio-modparam.patch
Patch441: linux-2.6-net-8139-pio-oqo2.patch
Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch460: linux-2.6-serial-460800.patch
Patch510: linux-2.6-silence-noise.patch
Patch520: linux-2.6-silence-x86-decompressor.patch
Patch530: linux-2.6-silence-fbcon-logo.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sparc-selinux-mprotect-checks.patch
Patch610: linux-2.6-defaults-fat-utf8.patch
Patch670: linux-2.6-ata-quirk.patch
Patch671: linux-2.6-libata-force-hardreset-in-sleep-mode.patch

Patch680: linux-2.6-wireless.patch
Patch681: linux-2.6-wireless-pending.patch
Patch690: linux-2.6-at76.patch

Patch700: linux-2.6-nfs-client-mounts-hang.patch
Patch701: linux-2.6-nfs-stack-usage.patch

# SELinux patches, will go upstream in .27
Patch800: linux-2.6-selinux-deffered-context-mapping.patch
Patch801: linux-2.6-selinux-deffered-context-mapping-no-sleep.patch
Patch802: linux-2.6-selinux-generic-ioctl.patch
Patch803: linux-2.6-selinux-new-proc-checks.patch
Patch804: linux-2.6-selinux-get-invalid-xattrs.patch
Patch805: linux-2.6-selinux-ecryptfs-support.patch
#

Patch1101: linux-2.6-default-mmf_dump_elf_headers.patch
Patch1400: linux-2.6-smarter-relatime.patch
Patch1515: linux-2.6-lirc.patch

# nouveau + drm fixes
Patch1801: linux-2.6-drm-git-mm.patch
Patch1803: nouveau-drm.patch
Patch1804: nouveau-drm-update.patch
Patch1806: linux-2.6-drm-i915-modeset.patch
Patch1807: linux-2.6-drm-radeon-fix-oops.patch
Patch1808: linux-2.6-drm-radeon-fix-oops2.patch
Patch1809: linux-2.6-drm-modesetting-oops-fixes.patch
Patch1810: linux-2.6-drm-fix-master-perm.patch

# kludge to make ich9 e1000 work
Patch2000: linux-2.6-e1000-ich9.patch

# Make Eee disk faster.
Patch2010: linux-2.6-sata-eeepc-faster.patch

# atl2 network driver
Patch2020: linux-2.6-netdev-atl2.patch

# make USB EHCI driver respect "nousb" parameter
Patch2300: linux-2.6-usb-ehci-hcd-respect-nousb.patch
# Fix HID usage descriptor on MS wireless desktop receiver
Patch2301: linux-2.6-ms-wireless-receiver.patch

# usb video
Patch2400: linux-2.6-uvcvideo.patch

Patch2501: linux-2.6-ppc-use-libgcc.patch

# get rid of imacfb and make efifb work everywhere it was used
Patch2600: linux-2.6-merge-efifb-imacfb.patch

%endif

BuildRoot: %{_tmppath}/kernel-%{KVERREL}-root

%description
The kernel package contains the Linux kernel (vmlinuz), the core of any
GNU/Linux operating system.  The kernel handles the basic functions
of the operating system: memory allocation, process allocation, device
input and output, etc.

The kernel-libre package is the upstream kernel without the non-Free
blobs it includes by default.

%package doc
Summary: Various documentation bits found in the kernel source
Group: Documentation
Provides: kernel-doc = %{rpmversion}-%{pkgrelease}
%description doc
This package contains documentation files from the kernel
source. Various bits of information about the Linux kernel and the
device drivers shipped with it are documented in these files.

You'll want to install this package if you need a reference to the
options that can be passed to Linux kernel modules at load time.


%package headers
Summary: Header files for the Linux kernel for use by glibc
Group: Development/System
Obsoletes: glibc-kernheaders
Provides: glibc-kernheaders = 3.0-46
Provides: kernel-headers = %{rpmversion}-%{pkgrelease}
%description headers
Kernel-headers includes the C header files that specify the interface
between the Linux kernel and userspace libraries and programs.  The
header files define structures and constants that are needed for
building most standard programs and are also needed for rebuilding the
glibc package.

%package bootwrapper
Summary: Boot wrapper files for generating combined kernel + initrd images
Group: Development/System
%description bootwrapper
Kernel-bootwrapper contains the wrapper code which makes bootable "zImage"
files combining both kernel and initial ramdisk.

%package debuginfo-common
Summary: Kernel source files used by %{name}-debuginfo packages
Group: Development/Debug
Provides: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
%description debuginfo-common
This package is required by %{name}-debuginfo subpackages.
It provides the kernel source files common to all builds.


#
# This macro creates a kernel-<subpackage>-debuginfo package.
#	%%kernel_debuginfo_package <subpackage>
#
%define kernel_debuginfo_package() \
%package %{?1:%{1}-}debuginfo\
Summary: Debug information for package %{name}%{?1:-%{1}}\
Group: Development/Debug\
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}\
Provides: %{name}%{?1:-%{1}}-debuginfo-%{_target_cpu} = %{version}-%{release}\
AutoReqProv: no\
%description -n %{name}%{?1:-%{1}}-debuginfo\
This package provides debug information for package %{name}%{?1:-%{1}}.\
This is required to use SystemTap with %{name}%{?1:-%{1}}-%{KVERREL}.\
%{expand:%%global debuginfo_args %{?debuginfo_args} -p '/.*/%%{KVERREL}%{?1:\.%{1}}/.*|/.*%%{KVERREL}%{?1:\.%{1}}(\.debug)?' -o debuginfo%{?1}.list}\
%{nil}

#
# This macro creates a kernel-<subpackage>-devel package.
#	%%kernel_devel_package <subpackage> <pretty-name>
#
%define kernel_devel_package() \
%package %{?1:%{1}-}devel\
Summary: Development package for building kernel modules to match the %{?2:%{2} }kernel\
Group: System Environment/Kernel\
Provides: kernel%{?1:-%{1}}-devel-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-devel-%{_target_cpu} = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-devel = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-devel-uname-r = %{KVERREL}%{?1:.%{1}}\
AutoReqProv: no\
Requires(pre): /usr/bin/find\
%description -n kernel%{?variant}%{?1:-%{1}}-devel\
This package provides kernel headers and makefiles sufficient to build modules\
against the %{?2:%{2} }kernel package.\
%{nil}

#
# This macro creates a kernel-<subpackage> and its -devel and -debuginfo too.
#	%%define variant_summary The Linux kernel compiled for <configuration>
#	%%kernel_variant_package [-n <pretty-name>] <subpackage>
#
%define kernel_variant_package(n:) \
%package %1\
Summary: %{variant_summary}\
Group: System Environment/Kernel\
%kernel_reqprovconf\
%{expand:%%kernel_devel_package %1 %{!?-n:%1}%{?-n:%{-n*}}}\
%{expand:%%kernel_debuginfo_package %1}\
%{nil}


# First the auxiliary packages of the main kernel package.
%kernel_devel_package
%kernel_debuginfo_package


# Now, each variant package.

%define variant_summary The Linux kernel compiled for SMP machines
%kernel_variant_package -n SMP smp
%description smp
This package includes a SMP version of the Linux kernel. It is
required only on machines with two or more CPUs as well as machines with
hyperthreading technology.

The kernel-libre-smp package is the upstream kernel without the
non-Free blobs it includes by default.

Install the kernel-libre-smp package if your machine uses two or more
CPUs.


%define variant_summary The Linux kernel compiled for PAE capable machines
%kernel_variant_package PAE
%description PAE
This package includes a version of the Linux kernel with support for up to
64GB of high memory. It requires a CPU with Physical Address Extensions (PAE).
The non-PAE kernel can only address up to 4GB of memory.
Install the kernel-PAE package if your machine has more than 4GB of memory.

The kernel-libre-PAE package is the upstream kernel without the
non-Free blobs it includes by default.



%define variant_summary The Linux kernel compiled with extra debugging enabled for PAE capable machines
%kernel_variant_package PAEdebug
Obsoletes: kernel-PAE-debug
%description PAEdebug
This package includes a version of the Linux kernel with support for up to
64GB of high memory. It requires a CPU with Physical Address Extensions (PAE).
The non-PAE kernel can only address up to 4GB of memory.
Install the kernel-PAE package if your machine has more than 4GB of memory.

This variant of the kernel has numerous debugging options enabled.
It should only be installed when trying to gather additional information
on kernel bugs, as some of these options impact performance noticably.

The kernel-libre-PAEdebug package is the upstream kernel without the
non-Free blobs it includes by default.


%define variant_summary The Linux kernel compiled with extra debugging enabled
%kernel_variant_package debug
%description debug
The kernel package contains the Linux kernel (vmlinuz), the core of any
GNU/Linux operating system.  The kernel handles the basic functions
of the operating system:  memory allocation, process allocation, device
input and output, etc.

This variant of the kernel has numerous debugging options enabled.
It should only be installed when trying to gather additional information
on kernel bugs, as some of these options impact performance noticably.

The kernel-libre-debug package is the upstream kernel without the
non-Free blobs it includes by default.


%define variant_summary The Linux kernel compiled for Xen VM operations
%kernel_variant_package -n Xen xen
%description xen
This package includes a version of the Linux kernel which
runs in a Xen VM. It works for both privileged and unprivileged guests.

The kernel-libre-xen package is the upstream kernel without the
non-Free blobs it includes by default.


%define variant_summary A minimal Linux kernel compiled for crash dumps
%kernel_variant_package kdump
%description kdump
This package includes a kdump version of the Linux kernel. It is
required only on machines which will use the kexec-based kernel crash dump
mechanism.

The kernel-libre-kdump package is the upstream kernel without the
non-Free blobs it includes by default.


%prep
# do a few sanity-checks for --with *only builds
%if %{with_baseonly}
%if !%{with_up}
echo "Cannot build --with baseonly, up build is disabled"
exit 1
%endif
%endif

%if %{with_smponly}
%if !%{with_smp}
echo "Cannot build --with smponly, smp build is disabled"
exit 1
%endif
%endif

%if %{with_paeonly}
%if !%{with_pae}
echo "Cannot build --with paeonly, pae build is disabled"
exit 1
%endif
%endif

%if %{with_xenonly}
%if !%{with_xen}
echo "Cannot build --with xenonly, xen build is disabled"
exit 1
%endif
%endif

patch_command='patch -p1 -F1 -s'
ApplyPatch()
{
  local patch=$1
  shift
  if [ ! -f $RPM_SOURCE_DIR/$patch ]; then
    exit 1;
  fi
  $RPM_SOURCE_DIR/deblob-check $RPM_SOURCE_DIR/$patch || exit 1
  case "$patch" in
  *.bz2) bunzip2 < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *.gz) gunzip < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *) $patch_command ${1+"$@"} < "$RPM_SOURCE_DIR/$patch" ;;
  esac
}

# First we unpack the kernel tarball.
# If this isn't the first make prep, we use links to the existing clean tarball
# which speeds things up quite a bit.

# Update to latest upstream.
%if 0%{?released_kernel}
%define vanillaversion 2.6.%{base_sublevel}
# released_kernel with stable_update available case
%if 0%{?stable_update}
%define vanillaversion 2.6.%{base_sublevel}.%{stable_update}
%endif
# non-released_kernel case
%else
%if 0%{?rcrev}
%define vanillaversion 2.6.%{upstream_sublevel}-rc%{rcrev}
%if 0%{?gitrev}
%define vanillaversion 2.6.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
%define vanillaversion 2.6.%{base_sublevel}-git%{gitrev}
%endif
%endif
%endif

if [ ! -d kernel-%{kversion}/vanilla-%{vanillaversion} ]; then
  # Ok, first time we do a make prep.
  rm -f pax_global_header
%setup -q -n kernel-%{kversion} -c
  mv linux-%{kversion} vanilla-%{vanillaversion}
  cd vanilla-%{vanillaversion}

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =/" Makefile

# Update vanilla to the latest upstream.
# released_kernel with stable_update available case
%if 0%{?stable_update}
ApplyPatch patch%{?stablelibre}-2.6.%{base_sublevel}.%{stable_update}.bz2
# non-released_kernel case
%else
%if 0%{?rcrev}
ApplyPatch patch%{?rcrevlibre}-2.6.%{upstream_sublevel}-rc%{rcrev}.bz2
%if 0%{?gitrev}
ApplyPatch patch%{?gitrevlibre}-2.6.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}.bz2
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
ApplyPatch patch%{?gitrevlibre}-2.6.%{base_sublevel}-git%{gitrev}.bz2
%endif
%endif
%endif

 cd ..

else
  # We already have a vanilla dir.
  cd kernel-%{kversion}
  if [ -d linux-%{kversion}.%{_target_cpu} ]; then
     # Just in case we ctrl-c'd a prep already
     rm -rf deleteme.%{_target_cpu}
     # Move away the stale away, and delete in background.
     mv linux-%{kversion}.%{_target_cpu} deleteme.%{_target_cpu}
     rm -rf deleteme.%{_target_cpu} &
  fi
fi

cp -rl vanilla-%{vanillaversion} linux-%{kversion}.%{_target_cpu}

cd linux-%{kversion}.%{_target_cpu}

%if %{using_upstream_branch}
### BRANCH APPLY ###
%endif

# Drop some necessary files from the source dir into the buildroot
cp $RPM_SOURCE_DIR/config-* .
cp %{SOURCE15} .

# Dynamically generate kernel .config files from config-* files
make -f %{SOURCE20} VERSION=%{version} configs

#if a rhel kernel, apply the rhel config options
%if 0%{?rhel}
  for i in %{all_arch_configs}
  do
    mv $i $i.tmp
    ./merge.pl config-rhel-generic $i.tmp > $i
    rm $i.tmp
  done
  for i in kernel-%{version}-{i586,i686,i686-PAE,x86_64}*.config
  do
    echo i is this file  $i
    mv $i $i.tmp
    ./merge.pl config-rhel-x86-generic $i.tmp > $i
    rm $i.tmp
  done
%endif

# stable release candidate
# ApplyPatch patch-2.6.24.1-rc1.bz2

# This patch adds a "make nonint_oldconfig" which is non-interactive and
# also gives a list of missing options at the end. Useful for automated
# builds (as used in the buildsystem).
ApplyPatch linux-2.6-build-nonintconfig.patch

#
# misc small stuff to make things compile
#
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-compile-fixes.patch | awk '{print $1}')
if [ "$C" -gt 10 ]; then
ApplyPatch linux-2.6-compile-fixes.patch
fi

# build with gcc43
#ApplyPatch linux-2.6-compile-fix-gcc-43.patch

%if !%{nopatches}

ApplyPatch linux-2.6-hotfixes.patch

# Roland's utrace ptrace replacement.
%ifnarch ia64
#ApplyPatch linux-2.6-utrace.patch
%endif

# enable sysrq-c on all kernels, not only kexec
ApplyPatch linux-2.6-sysrq-c.patch

# Architecture patches
# x86(-64)
# Compile 686 kernels tuned for Pentium4.
ApplyPatch linux-2.6-x86-tune-generic.patch

#
# PowerPC
#
### NOT (YET) UPSTREAM:
# The EHCI ISO patch isn't yet upstream but is needed to fix reboot
#ApplyPatch linux-2.6-ps3-ehci-iso.patch
# Fixes some wireless optical mice
#ApplyPatch linux-2.6-ms-wireless-receiver.patch
# The storage alias patch is Fedora-local, and allows the old 'ps3_storage'
# module name to work on upgrades. Otherwise, I believe mkinitrd will fail
# to pull the module in,
ApplyPatch linux-2.6-ps3-storage-alias.patch
# Support booting from Sony's original released 2.6.16-based kboot
#ApplyPatch linux-2.6-ps3-legacy-bootloader-hack.patch
# Alleviate G5 thermal shutdown problems
ApplyPatch linux-2.6-g5-therm-shutdown.patch
# Provide modalias in sysfs for vio devices
ApplyPatch linux-2.6-vio-modalias.patch
# Work around PCIe bridge setup on iSight
ApplyPatch linux-2.6-imac-transparent-bridge.patch
# Link zImage at 32MiB (for POWER machines, Efika)
ApplyPatch linux-2.6-powerpc-zImage-32MiB.patch
# Don't show 'CHRP' in /proc/cpuinfo on Efika
#ApplyPatch linux-2.6-efika-not-chrp.patch

#
# SPARC64
#

#
# Exec shield
#
ApplyPatch linux-2.6-execshield.patch

#
# bugfixes to drivers and filesystems
#

# USB
ApplyPatch linux-2.6-usb-ehci-hcd-respect-nousb.patch

# ACPI

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
ApplyPatch linux-2.6-debug-nmi-timeout.patch
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-spinlock-taint.patch
ApplyPatch linux-2.6-debug-vm-would-have-oomkilled.patch

#
# /dev/crash driver for the crashdump analysis tool
#
ApplyPatch linux-2.6-crash-driver.patch

#
# PCI
#
# disable message signaled interrupts
ApplyPatch linux-2.6-defaults-pci_no_msi.patch

#
# SCSI Bits.
#
# fix cpqarray pci enable
ApplyPatch linux-2.6-scsi-cpqarray-set-master.patch
# fix vmware emulated scsi controller
#ApplyPatch linux-2.6-scsi-mpt-vmware-fix.patch

# ALSA
#
ApplyPatch linux-2.6-alsa-kill-annoying-messages.patch
# In upstream alsa. Resolves issues with glitch-free pulseaudio on hda-intel.
# See: https://tango.0pointer.de/pipermail/pulseaudio-discuss/2008-May/001837.html
#      http://mailman.alsa-project.org/pipermail/alsa-devel/2008-May/007856.html
ApplyPatch linux-2.6-hda-intel-fix-dma-position-inaccuracy.patch

# Filesystem patches.
# Squashfs
ApplyPatch linux-2.6-squashfs.patch

# Networking
# Disable easy to trigger printk's.
ApplyPatch linux-2.6-net-silence-noisy-printks.patch
# Make 8139too PIO/MMIO a module parameter
ApplyPatch linux-2.6-net-8139-pio-modparam.patch
# OQO2 needs PIO
ApplyPatch linux-2.6-net-8139-pio-oqo2.patch

# Misc fixes
# The input layer spews crap no-one cares about.
ApplyPatch linux-2.6-input-kill-stupid-messages.patch

# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch
# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch

# Make the real mode boot decompressor understand and honor 'quiet'
ApplyPatch linux-2.6-silence-x86-decompressor.patch

# Make fbcon not show the penguins with 'quiet'
ApplyPatch linux-2.6-silence-fbcon-logo.patch

# Fix the SELinux mprotect checks on executable mappings
ApplyPatch linux-2.6-selinux-mprotect-checks.patch
# Fix SELinux for sparc
ApplyPatch linux-2.6-sparc-selinux-mprotect-checks.patch

# Changes to upstream defaults.
# Use UTF-8 by default on VFAT.
ApplyPatch linux-2.6-defaults-fat-utf8.patch

# ia64 ata quirk
ApplyPatch linux-2.6-ata-quirk.patch
# wake up links that have been put to sleep by BIOS (#436099)
ApplyPatch linux-2.6-libata-force-hardreset-in-sleep-mode.patch

# Allow selinux to defer validation of contexts, aka: rpm can write illegal labels
ApplyPatch linux-2.6-selinux-deffered-context-mapping.patch
ApplyPatch linux-2.6-selinux-deffered-context-mapping-no-sleep.patch
ApplyPatch linux-2.6-selinux-generic-ioctl.patch
ApplyPatch linux-2.6-selinux-new-proc-checks.patch
ApplyPatch linux-2.6-selinux-get-invalid-xattrs.patch
ApplyPatch linux-2.6-selinux-ecryptfs-support.patch

# wireless patches headed for 2.6.26
#ApplyPatch linux-2.6-wireless.patch
# wireless patches headed for 2.6.27
#ApplyPatch linux-2.6-wireless-pending.patch

# Add misc wireless bits from upstream wireless tree
#ApplyPatch linux-2.6-at76.patch

# implement smarter atime updates support.
#ApplyPatch linux-2.6-smarter-relatime.patch

# NFS Client mounts hang when exported directory do not exist
ApplyPatch linux-2.6-nfs-client-mounts-hang.patch
# Fix stack overflow at mount time.
ApplyPatch linux-2.6-nfs-stack-usage.patch

# build id related enhancements
ApplyPatch linux-2.6-default-mmf_dump_elf_headers.patch

# http://www.lirc.org/
ApplyPatch linux-2.6-lirc.patch

ApplyPatch linux-2.6-e1000-ich9.patch

ApplyPatch linux-2.6-sata-eeepc-faster.patch

#ApplyPatch linux-2.6-netdev-atl2.patch

# Nouveau DRM + drm fixes
#ApplyPatch linux-2.6-drm-git-mm.patch
#ApplyPatch nouveau-drm.patch
#ApplyPatch nouveau-drm-update.patch
#ApplyPatch linux-2.6-drm-i915-modeset.patch
#ApplyPatch linux-2.6-drm-radeon-fix-oops.patch
#ApplyPatch linux-2.6-drm-radeon-fix-oops2.patch
#ApplyPatch linux-2.6-drm-modesetting-oops-fixes.patch
#ApplyPatch linux-2.6-drm-fix-master-perm.patch

# ext4dev stable patch queue, slated for 2.6.25
#ApplyPatch linux-2.6-ext4-stable-queue.patch

# linux1394 git patches
#C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-firewire-git-pending.patch | awk '{print $1}')
#if [ "$C" -gt 10 ]; then
#ApplyPatch linux-2.6-firewire-git-pending.patch
#fi

# usb video
#ApplyPatch linux-2.6-uvcvideo.patch

ApplyPatch linux-2.6-ppc-use-libgcc.patch

# get rid of imacfb and make efifb work everywhere it was used
#ApplyPatch linux-2.6-merge-efifb-imacfb.patch

# ---------- below all scheduled for 2.6.24 -----------------

# END OF PATCH APPLICATIONS

%endif

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl

cp %{SOURCE10} Documentation/

# only deal with configs if we are going to build for the arch
%ifnarch %nobuildarches

mkdir configs

# Remove configs not for the buildarch
for cfg in kernel-%{version}-*.config; do
  if [ `echo %{all_arch_configs} | grep -c $cfg` -eq 0 ]; then
    rm -f $cfg
  fi
done

%if !%{debugbuildsenabled}
rm -f kernel-%{version}-*debug.config
%endif

# now run oldconfig over all the config files
for i in *.config
do
  mv $i .config
  Arch=`head -1 .config | cut -b 3-`
  make ARCH=$Arch %{oldconfig_target} > /dev/null
  echo "# $Arch" > configs/$i
  cat .config >> configs/$i
done
# end of kernel config
%endif

# get rid of unwanted files resulting from patch fuzz
find . \( -name "*.orig" -o -name "*~" \) -exec rm -f {} \; >/dev/null

cd ..

# Unpack the Xen tarball.
%if %{includexen}
cp %{SOURCE2} .
if [ -d xen ]; then
  rm -rf xen
fi
%setup -D -T -q -n kernel-%{kversion} -a1
cd xen
# Any necessary hypervisor patches go here

%endif


###
### build
###
%build

%if %{with_sparse}
%define sparse_mflags	C=1
%endif

%if %{fancy_debuginfo}
# This override tweaks the kernel makefiles so that we run debugedit on an
# object before embedding it.  When we later run find-debuginfo.sh, it will
# run debugedit again.  The edits it does change the build ID bits embedded
# in the stripped object, but repeating debugedit is a no-op.  We do it
# beforehand to get the proper final build ID bits into the embedded image.
# This affects the vDSO images in vmlinux, and the vmlinux image in bzImage.
idhack='cmd_objcopy=$(if $(filter -S,$(OBJCOPYFLAGS)),'\
'sh -xc "/usr/lib/rpm/debugedit -b $$RPM_BUILD_DIR -d /usr/src/debug -i $<";)'\
'$(OBJCOPY) $(OBJCOPYFLAGS) $(OBJCOPYFLAGS_$(@F)) $< $@'
%endif

cp_vmlinux()
{
  eu-strip --remove-comment -o "$2" "$1"
}

BuildKernel() {
    MakeTarget=$1
    KernelImage=$2
    Flavour=$3
    InstallName=${4:-vmlinuz}

    # Pick the right config file for the kernel we're building
    Config=kernel-%{version}-%{_target_cpu}${Flavour:+-${Flavour}}.config
    DevelDir=/usr/src/kernels/%{KVERREL}${Flavour:+.${Flavour}}

    # When the bootable image is just the ELF kernel, strip it.
    # We already copy the unstripped file into the debuginfo package.
    if [ "$KernelImage" = vmlinux ]; then
      CopyKernel=cp_vmlinux
    else
      CopyKernel=cp
    fi

    KernelVer=%{version}-libre.%{release}.%{_target_cpu}${Flavour:+.${Flavour}}
    echo BUILDING A KERNEL FOR ${Flavour} %{_target_cpu}...

    # make sure EXTRAVERSION says what we want it to say
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = %{?stablerev}-libre.%{release}.%{_target_cpu}${Flavour:+.${Flavour}}/" Makefile

    # if pre-rc1 devel kernel, must fix up SUBLEVEL for our versioning scheme
    %if !0%{?rcrev}
    %if 0%{?gitrev}
    perl -p -i -e 's/^SUBLEVEL.*/SUBLEVEL = %{upstream_sublevel}/' Makefile
    %endif
    %endif

    # and now to start the build process

    make -s mrproper
    cp configs/$Config .config

    Arch=`head -1 .config | cut -b 3-`
    echo USING ARCH=$Arch

    make -s ARCH=$Arch %{oldconfig_target} > /dev/null
    make -s ARCH=$Arch V=1 %{?_smp_mflags} $MakeTarget %{?sparse_mflags} \
    	 ${idhack+"$idhack"}
    make -s ARCH=$Arch V=1 %{?_smp_mflags} modules %{?sparse_mflags} || exit 1

    # Start installing the results
%if %{with_debuginfo}
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/boot
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/%{image_install_path}
%endif
    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}
    install -m 644 .config $RPM_BUILD_ROOT/boot/config-$KernelVer
    install -m 644 System.map $RPM_BUILD_ROOT/boot/System.map-$KernelVer
    touch $RPM_BUILD_ROOT/boot/initrd-$KernelVer.img
    if [ -f arch/$Arch/boot/zImage.stub ]; then
      cp arch/$Arch/boot/zImage.stub $RPM_BUILD_ROOT/%{image_install_path}/zImage.stub-$KernelVer || :
    fi
    $CopyKernel $KernelImage \
    		$RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer
    chmod 755 $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer

    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer
    make -s ARCH=$Arch INSTALL_MOD_PATH=$RPM_BUILD_ROOT modules_install KERNELRELEASE=$KernelVer
%ifarch %{vdso_arches}
    make -s ARCH=$Arch INSTALL_MOD_PATH=$RPM_BUILD_ROOT vdso_install KERNELRELEASE=$KernelVer
%endif

    # And save the headers/makefiles etc for building modules against
    #
    # This all looks scary, but the end result is supposed to be:
    # * all arch relevant include/ files
    # * all Makefile/Kconfig files
    # * all script/ files

    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/source
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    (cd $RPM_BUILD_ROOT/lib/modules/$KernelVer ; ln -s build source)
    # dirs for additional modules per module-init-tools, kbuild/modules.txt
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/extra
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/updates
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/weak-updates
    # first copy everything
    cp --parents `find  -type f -name "Makefile*" -o -name "Kconfig*"` $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp Module.symvers $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp System.map $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    if [ -s Module.markers ]; then
      cp Module.markers $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    fi
    # then drop all but the needed Makefiles/Kconfig files
    rm -rf $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/Documentation
    rm -rf $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts
    rm -rf $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cp .config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp -a scripts $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    if [ -d arch/%{_arch}/scripts ]; then
      cp -a arch/%{_arch}/scripts $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/%{_arch} || :
    fi
    if [ -f arch/%{_arch}/*lds ]; then
      cp -a arch/%{_arch}/*lds $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/%{_arch}/ || :
    fi
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/*.o
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/*/*.o
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cd include
    cp -a acpi config keys linux math-emu media mtd net pcmcia rdma rxrpc scsi sound video asm asm-generic $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cp -a `readlink asm` $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    # While arch/powerpc/include/asm is still a symlink to the old
    # include/asm-ppc{64,} directory, include that in kernel-devel too.
    if [ "$Arch" = "powerpc" -a -r ../arch/powerpc/include/asm ]; then
      cp -a `readlink ../arch/powerpc/include/asm` $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
      mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/$Arch/include
      pushd $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/$Arch/include
      ln -sf ../../../include/asm-ppc* asm
      popd
    fi
%if %{includexen}
    cp -a xen $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
%endif

    # Make sure the Makefile and version.h have a matching timestamp so that
    # external modules can be built
    touch -r $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/Makefile $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/linux/version.h
    touch -r $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/.config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/linux/autoconf.h
    # Copy .config to include/config/auto.conf so "make prepare" is unnecessary.
    cp $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/.config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/config/auto.conf
    cd ..

    #
    # save the vmlinux file for kernel debugging into the kernel-debuginfo rpm
    #
%if %{with_debuginfo}
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/lib/modules/$KernelVer
    cp vmlinux $RPM_BUILD_ROOT%{debuginfodir}/lib/modules/$KernelVer
%endif

    find $RPM_BUILD_ROOT/lib/modules/$KernelVer -name "*.ko" -type f >modnames

    # mark modules executable so that strip-to-file can strip them
    xargs --no-run-if-empty chmod u+x < modnames

    # Generate a list of modules for block and networking.

    fgrep /drivers/ modnames | xargs --no-run-if-empty nm -upA |
    sed -n 's,^.*/\([^/]*\.ko\):  *U \(.*\)$,\1 \2,p' > drivers.undef

    collect_modules_list()
    {
      sed -r -n -e "s/^([^ ]+) \\.?($2)\$/\\1/p" drivers.undef |
      LC_ALL=C sort -u > $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.$1
    }

    collect_modules_list networking \
    			 'register_netdev|ieee80211_register_hw|usbnet_probe'
    collect_modules_list block \
    			 'ata_scsi_ioctl|scsi_add_host|blk_init_queue|register_mtd_blktrans'

    # detect missing or incorrect license tags
    rm -f modinfo
    while read i
    do
      echo -n "${i#$RPM_BUILD_ROOT/lib/modules/$KernelVer/} " >> modinfo
      /sbin/modinfo -l $i >> modinfo
    done < modnames

    egrep -v \
    	  'GPL( v2)?$|Dual BSD/GPL$|Dual MPL/GPL$|GPL and additional rights$' \
	  modinfo && exit 1

    rm -f modinfo modnames

    # remove files that will be auto generated by depmod at rpm -i time
    for i in alias ccwmap dep ieee1394map inputmap isapnpmap ofmap pcimap seriomap symbols usbmap
    do
      rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.$i
    done

    # Move the devel headers out of the root file system
    mkdir -p $RPM_BUILD_ROOT/usr/src/kernels
    mv $RPM_BUILD_ROOT/lib/modules/$KernelVer/build $RPM_BUILD_ROOT/$DevelDir
    ln -sf ../../..$DevelDir $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
}

###
# DO it...
###

# prepare directories
rm -rf $RPM_BUILD_ROOT
mkdir -p $RPM_BUILD_ROOT/boot

%if %{includexen}
%if %{with_xen}
  cd xen
  mkdir -p $RPM_BUILD_ROOT/%{image_install_path} $RPM_BUILD_ROOT/boot
  make %{?_smp_mflags} %{xen_flags}
  install -m 644 xen.gz $RPM_BUILD_ROOT/%{image_install_path}/xen.gz-%{KVERREL}.xen
  install -m 755 xen-syms $RPM_BUILD_ROOT/boot/xen-syms-%{KVERREL}.xen
  cd ..
%endif
%endif

cd linux-%{kversion}.%{_target_cpu}

%if %{with_debug}
BuildKernel %make_target %kernel_image debug
%if %{with_pae}
BuildKernel %make_target %kernel_image PAEdebug
%endif
%endif

%if %{with_pae}
BuildKernel %make_target %kernel_image PAE
%endif

%if %{with_up}
BuildKernel %make_target %kernel_image
%endif

%if %{with_smp}
BuildKernel %make_target %kernel_image smp
%endif

%if %{includexen}
%if %{with_xen}
BuildKernel %xen_target %xen_image xen
%endif
%endif

%if %{with_kdump}
BuildKernel vmlinux vmlinux kdump vmlinux
%endif

###
### Special hacks for debuginfo subpackages.
###

# This macro is used by %%install, so we must redefine it before that.
%define debug_package %{nil}

%if %{fancy_debuginfo}
%define __debug_install_post \
  /usr/lib/rpm/find-debuginfo.sh %{debuginfo_args} %{_builddir}/%{?buildsubdir}\
%{nil}
%endif

%if %{with_debuginfo}
%ifnarch noarch
%global __debug_package 1
%files -f debugfiles.list debuginfo-common
%defattr(-,root,root)
%endif
%endif

###
### install
###

%install

cd linux-%{kversion}.%{_target_cpu}

%if %{includexen}
%if %{with_xen}
mkdir -p $RPM_BUILD_ROOT/etc/ld.so.conf.d
rm -f $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf
cat > $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf <<\EOF
# This directive teaches ldconfig to search in nosegneg subdirectories
# and cache the DSOs there with extra bit 0 set in their hwcap match
# fields.  In Xen guest kernels, the vDSO tells the dynamic linker to
# search in nosegneg subdirectories and to match this extra hwcap bit
# in the ld.so.cache file.
hwcap 0 nosegneg
EOF
chmod 444 $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf
%endif
%endif

%if %{with_doc}
mkdir -p $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}/Documentation

# sometimes non-world-readable files sneak into the kernel source tree
chmod -R a+r *
# copy the source over
tar cf - Documentation | tar xf - -C $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}

# Make man pages for the kernel API.
make mandocs
mkdir -p $RPM_BUILD_ROOT/usr/share/man/man9
mv Documentation/DocBook/man/*.9.gz $RPM_BUILD_ROOT/usr/share/man/man9
%endif

%if %{with_headers}
# Install kernel headers
make ARCH=%{hdrarch} INSTALL_HDR_PATH=$RPM_BUILD_ROOT/usr headers_install

# Manually go through the 'headers_check' process for every file, but
# don't die if it fails
chmod +x scripts/hdrcheck.sh
echo -e '*****\n*****\nHEADER EXPORT WARNINGS:\n*****' > hdrwarnings.txt
for FILE in `find $RPM_BUILD_ROOT/usr/include` ; do
    scripts/hdrcheck.sh $RPM_BUILD_ROOT/usr/include $FILE /dev/null >> hdrwarnings.txt || :
done
echo -e '*****\n*****' >> hdrwarnings.txt
if grep -q exist hdrwarnings.txt; then
   sed s:^$RPM_BUILD_ROOT/usr/include/:: hdrwarnings.txt
   # Temporarily cause a build failure if header inconsistencies.
   # exit 1
fi

# glibc provides scsi headers for itself, for now
rm -rf $RPM_BUILD_ROOT/usr/include/scsi
rm -f $RPM_BUILD_ROOT/usr/include/asm*/atomic.h
rm -f $RPM_BUILD_ROOT/usr/include/asm*/io.h
rm -f $RPM_BUILD_ROOT/usr/include/asm*/irq.h
%endif

%if %{with_bootwrapper}
make DESTDIR=$RPM_BUILD_ROOT bootwrapper_install WRAPPER_OBJDIR=%{_libdir}/kernel-wrapper WRAPPER_DTSDIR=%{_libdir}/kernel-wrapper/dts
%endif

###
### clean
###

%clean
rm -rf $RPM_BUILD_ROOT

###
### scripts
###

#
# This macro defines a %%post script for a kernel*-devel package.
#	%%kernel_devel_post <subpackage>
#
%define kernel_devel_post() \
%{expand:%%post %{?1:%{1}-}devel}\
if [ -f /etc/sysconfig/kernel ]\
then\
    . /etc/sysconfig/kernel || exit $?\
fi\
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ]\
then\
    (cd /usr/src/kernels/%{KVERREL}%{?1:.%{1}} &&\
     /usr/bin/find . -type f | while read f; do\
       hardlink -c /usr/src/kernels/*.fc*.*/$f $f\
     done)\
fi\
%{nil}

# This macro defines a %%posttrans script for a kernel package.
#	%%kernel_variant_posttrans [-v <subpackage>] [-s <s> -r <r>] <mkinitrd-args>
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_posttrans(s:r:v:) \
%{expand:%%posttrans %{?-v*}}\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --rpmposttrans %{KVERREL}%{?-v:.%{-v*}} || exit $?\
%{nil}

#
# This macro defines a %%post script for a kernel package and its devel package.
#	%%kernel_variant_post [-v <subpackage>] [-s <s> -r <r>] <mkinitrd-args>
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_post(s:r:v:) \
%{expand:%%kernel_devel_post %{?-v*}}\
%{expand:%%kernel_variant_posttrans %{?-v*}}\
%{expand:%%post %{?-v*}}\
%{-s:\
if [ `uname -i` == "x86_64" -o `uname -i` == "i386" ] &&\
   [ -f /etc/sysconfig/kernel ]; then\
  /bin/sed -i -e 's/^DEFAULTKERNEL=%{-s*}$/DEFAULTKERNEL=%{-r*}/' /etc/sysconfig/kernel || exit $?\
fi}\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --mkinitrd --depmod --install %{*} %{KVERREL}%{?-v:.%{-v*}} || exit $?\
#if [ -x /sbin/weak-modules ]\
#then\
#    /sbin/weak-modules --add-kernel %{KVERREL}%{?-v*} || exit $?\
#fi\
%{nil}

#
# This macro defines a %%preun script for a kernel package.
#	%%kernel_variant_preun <subpackage>
#
%define kernel_variant_preun() \
%{expand:%%preun %{?1}}\
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}%{?1:.%{1}} || exit $?\
#if [ -x /sbin/weak-modules ]\
#then\
#    /sbin/weak-modules --remove-kernel %{KVERREL}%{?1} || exit $?\
#fi\
%{nil}

%kernel_variant_preun
%kernel_variant_post -s kernel-smp -r kernel

%kernel_variant_preun smp
%kernel_variant_post -v smp

%kernel_variant_preun PAE
%kernel_variant_post -v PAE -s kernel-smp -r kernel-PAE

%kernel_variant_preun debug
%kernel_variant_post -v debug

%kernel_variant_post -v PAEdebug -s kernel-smp -r kernel-PAEdebug
%kernel_variant_preun PAEdebug

%kernel_variant_preun xen
%kernel_variant_post -v xen -s kernel-xen[0U] -r kernel-xen -- `[ -d /proc/xen -a ! -e /proc/xen/xsd_kva ] || echo --multiboot=/%{image_install_path}/xen.gz-%{KVERREL}.xen`
if [ -x /sbin/ldconfig ]
then
    /sbin/ldconfig -X || exit $?
fi

###
### file lists
###

%if %{with_headers}
%files headers
%defattr(-,root,root)
/usr/include/*
%endif

%if %{with_bootwrapper}
%files bootwrapper
%defattr(-,root,root)
/usr/sbin/*
%{_libdir}/kernel-wrapper
%endif

# only some architecture builds need kernel-doc
%if %{with_doc}
%files doc
%defattr(-,root,root)
%{_datadir}/doc/kernel-doc-%{kversion}/Documentation/*
%dir %{_datadir}/doc/kernel-doc-%{kversion}/Documentation
%dir %{_datadir}/doc/kernel-doc-%{kversion}
%{_datadir}/man/man9/*
%endif

# This is %{image_install_path} on an arch where that includes ELF files,
# or empty otherwise.
%define elf_image_install_path %{?kernel_image_elf:%{image_install_path}}

#
# This macro defines the %%files sections for a kernel package
# and its devel and debuginfo packages.
#	%%kernel_variant_files [-k vmlinux] [-a <extra-files-glob>] [-e <extra-nonbinary>] <condition> <subpackage>
#
%define kernel_variant_files(a:e:k:) \
%if %{1}\
%{expand:%%files %{?2}}\
%defattr(-,root,root)\
/%{image_install_path}/%{?-k:%{-k*}}%{!?-k:vmlinuz}-%{KVERREL}%{?2:.%{2}}\
/boot/System.map-%{KVERREL}%{?2:.%{2}}\
#/boot/symvers-%{KVERREL}%{?2:.%{2}}.gz\
/boot/config-%{KVERREL}%{?2:.%{2}}\
%{?-a:%{-a*}}\
%dir /lib/modules/%{KVERREL}%{?2:.%{2}}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/kernel\
/lib/modules/%{KVERREL}%{?2:.%{2}}/build\
/lib/modules/%{KVERREL}%{?2:.%{2}}/source\
/lib/modules/%{KVERREL}%{?2:.%{2}}/extra\
/lib/modules/%{KVERREL}%{?2:.%{2}}/updates\
/lib/modules/%{KVERREL}%{?2:.%{2}}/weak-updates\
%ifarch %{vdso_arches}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/vdso\
%endif\
/lib/modules/%{KVERREL}%{?2:.%{2}}/modules.block\
/lib/modules/%{KVERREL}%{?2:.%{2}}/modules.networking\
/lib/modules/%{KVERREL}%{?2:.%{2}}/modules.order\
%ghost /boot/initrd-%{KVERREL}%{?2:.%{2}}.img\
%{?-e:%{-e*}}\
%{expand:%%files %{?2:%{2}-}devel}\
%defattr(-,root,root)\
%verify(not mtime) /usr/src/kernels/%{KVERREL}%{?2:.%{2}}\
/usr/src/kernels/%{KVERREL}%{?2:.%{2}}\
%if %{with_debuginfo}\
%ifnarch noarch\
%if %{fancy_debuginfo}\
%{expand:%%files -f debuginfo%{?2}.list %{?2:%{2}-}debuginfo}\
%else\
%{expand:%%files %{?2:%{2}-}debuginfo}\
%endif\
%defattr(-,root,root)\
%if !%{fancy_debuginfo}\
%if "%{elf_image_install_path}" != ""\
%{debuginfodir}/%{elf_image_install_path}/*-%{KVERREL}%{?2:.%{2}}.debug\
%endif\
%{debuginfodir}/lib/modules/%{KVERREL}%{?2:.%{2}}\
%{debuginfodir}/usr/src/kernels/%{KVERREL}%{?2:.%{2}}\
%endif\
%endif\
%endif\
%endif\
%{nil}


%kernel_variant_files %{with_up}
%kernel_variant_files %{with_smp} smp
%kernel_variant_files %{with_debug} debug
%kernel_variant_files %{with_pae} PAE
%kernel_variant_files %{with_pae_debug} PAEdebug
%kernel_variant_files -k vmlinux %{with_kdump} kdump
%kernel_variant_files -a /%{image_install_path}/xen*-%{KVERREL}.xen -e /etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf %{with_xen} xen

%changelog
* Sun Jun 22 2008 Alexandre Oliva <lxoliva@fsfla.org> 2.6.26-libre.0.81.rc7.fc10
- Deblobbed 2.6.26-rc7

* Fri Jun 20 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc7

* Fri Jun 20 2008 Dave Jones <davej@redhat.com>
- Reduce the NFS mount code stack usage.

* Thu Jun 19 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc6-git6

* Wed Jun 18 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc6-git5

* Wed Jun 18 2008 Eric Paris <eparis@redhat.com>
- Better selinux support for ecryptfs overlays (BZ 450867)

* Tue Jun 17 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc6-git4

* Sat Jun 14 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc6-git2

* Sat Jun 14 2008 Chuck Ebbert <cebbert@redhat.com>
- Enable Controller Area Networking (F8#451179)

* Fri Jun 13 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-06-13
  (http://marc.info/?l=linux-wireless&m=121339101523260&w=2)

* Fri Jun 13 2008 Chuck Ebbert <cebbert@redhat.com>
- Build in the KGDB serial interface so early kernel init can be debugged.

* Fri Jun 13 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc6-git1

* Thu Jun 12 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc5-git7

* Thu Jun 12 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc5-git6

* Wed Jun 11 2008 Peter Jones <pjones@redhat.com>
- Don't show the penguin in fbcon with "quiet"

* Wed Jun 11 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc5-git5

* Wed Jun 11 2008 Eric Paris <eparis@redhat.com>
- allow things like restorecon to read invalid labels from the disk

* Tue Jun 10 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-06-09
  (http://marc.info/?l=linux-kernel&m=121304710726632&w=2)

* Tue Jun 10 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc5-git4

* Fri Jun 06 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.26-rc5-git3
- Re-enable CONFIG_PCIEASPM (#447231)

* Fri Jun 06 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.26-rc5-git2

* Thu Jun 05 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc5

* Tue Jun 03 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc4-git5

* Tue Jun 03 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-06-03
  (http://marc.info/?l=linux-wireless&m=121252137324941&w=2)
- Upstream wireless updates from 2008-06-03
  (http://marc.info/?l=linux-wireless&m=121252503832192&w=2)

* Mon Jun 02 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc4-git4

* Mon Jun 02 2008 Jarod Wilson <jwilson@redhat.com>
- Updated lirc patch with latest upstream changes and 2.6.26 compat

* Mon Jun 02 2008 John W. Linville <linville@redhat.com>
- Revert misguided wireless.h "fix" from upstream

* Thu May 29 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-05-28
  (http://marc.info/?l=linux-wireless&m=121201250110162&w=2)

* Thu May 29 2008 Kristian Høgsberg <krh@redhat.com>
- Add linux-2.6-silence-x86-decompressor.patch to silence the
  decompressor spew when 'quiet' is passed.

* Wed May 28 2008 Dave Jones <davej@redhat.com>
- Make the OQO2 use polled IO for its ethernet.

* Wed May 28 2008 Chuck Ebbert <cebbert@redhat.com>
- Remove eeepc driver, now upstream as eeepc-laptop.

* Wed May 28 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc4-git2

* Wed May 28 2008 Dave Jones <davej@redhat.com>
- Make 8139too PIO/MMIO a module parameter

* Wed May 28 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc4-git1

* Wed May 28 2008 Dave Jones <davej@redhat.com>
- PPC gets sad with debug alloc (bz 448598)

* Tue May 27 2008 John W. Linville <linville@redhat.com>
- Missed some at76 bits from 2008-05-22...

* Tue May 27 2008 Tom "spot" Callaway <tcallawa@redhat.com>
- Apply patch to Resolve issues with glitch-free pulseaudio on hda-intel

* Tue May 27 2008 Kristian Høgsberg <krh@redhat.com>
- Drop a couple of stray references to CONFIG_DEBUG_IGNORE_QUIET.

* Tue May 27 2008 John W. Linville <linville@redhat.com>
- Upstream wireless updates from 2008-05-22
  (http://marc.info/?l=linux-wireless&m=121146112404515&w=2)

* Mon May 26 2008 Alexandre Oliva <lxoliva@fsfla.org> 2.6.26-libre.0.3.rc4.fc10
- Deblob 2.6.26-rc4.

* Mon May 26 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc4

* Mon May 26 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git8

* Sun May 25 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git7

* Fri May 23 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git6

* Fri May 23 2008 Kristian Høgsberg <krh@redhat.com>
- Drop linux-2.6-debug-no-quiet.patch.  As discussed with Jeremy and
  Dave, it's time to drop this patch.  Verbose output can still be
  enabled by specifying 'noisy' on the kernel command line instead of
  'quiet'.

* Fri May 23 2008 Dave Jones <davej@redhat.com>
- Experiment: Disable CONFIG_PCIEASPM. It might be the reason for #447231

* Fri May 23 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git5

* Thu May 22 2008 Dave Jones <davej@redhat.com>
- Disable CONFIG_DMAR. This is terminally broken in the presence of a broken BIOS

* Thu May 22 2008 Dave Jones <davej@redhat.com>
- Fix gianfar build.

* Thu May 22 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git4

* Wed May 21 2008 John W. Linville <linville@redhat.com>
- libertas: Fix ethtool statistics
- mac80211: fix NULL pointer dereference in ieee80211_compatible_rates
- mac80211: don't claim iwspy support
- rtl8187: resource leak in error case
- hostap_cs: add ID for Conceptronic CON11CPro
- orinoco_cs: add ID for SpeedStream wireless adapters

* Wed May 21 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git3

* Tue May 20 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git1

* Mon May 19 2008 Dave Jones <davej@redhat.com>
- Disable PATA_ISAPNP (it's busted).

* Mon May 19 2008 John W. Linville <linville@redhat.com>
- mac80211 : Association with 11n hidden ssid ap.
- libertas: fix command timeout after firmware failure
- mac80211: Add RTNL version of ieee80211_iterate_active_interfaces
- wireless: Create 'device' symlink in sysfs
- hostap: fix "registers" registration in procfs
- wireless, airo: waitbusy() won't delay
- iwlwifi : Set monitor mode for 4965
- iwlwifi : Set monitor mode for 3945
- make sta_rx_agg_session_timer_expired() static
- remove ieee80211_tx_frame()
- remove ieee80211_wx_{get,set}_auth()
- wireless: fix "iwlwifi: unify init driver flow"
- iwl3945: do not delay hardware scan if it is a direct scan
- ath5k: Fix loop variable initializations
- zd1211rw: initial IBSS support
- mac80211: use hardware flags for signal/noise units
- mac80211: make rx radiotap header more flexible
- iwlwifi: HW dependent run time calibration
- iwlwifi: HW crypto acceleration fixes
- iwlwifi: remove uneeded callback
- iwlwifi: CT-Kill configuration fix
- iwlwifi: HT IE in probe request clean up
- iwlwifi: clean up register names and defines
- iwlwifi: move Flow Handlers define to iwl-fh.h
- iwlwifi: move verify_ucode functions to iwl-core
- iwlwifi: move hw_rx_handler_setup to iwl-4965.c
- iwlwifi-5000: update the CT-Kill value for 5000 series
- iwlwifi-5000: add run time calibrations for 5000
- iwlwifi-5000: update the byte count in SCD
- iwlwifi: move iwl4965_init_alive_start to iwl-4965.c
- wireless: Add missing locking to cfg80211_dev_rename
- mac80211: correct skb allocation
- iwlwifi: move per driverdebug_level to per device
- iwlwifi: move debug_level to sysfs/bus/pci/devices
- iwlwifi: update levels of debug prints
- iwlwifi: adding parameter of fw_restart
- iwlwifi: remove support for Narrow Channel (10Mhz)
- iwlwifi: HT antenna/chains overhaul
- iwlwifi: TLC modifications
- iwlwifi: rate scale module cleanups
- iwlwifi: rate scale restructure toggle_antenna functions
- iwlwifi: rs fix wrong parenthesizing in rs_get_lower_rate function
- iwlwifi: rate sacaling fixes
- iwlwifi: more RS improvements
- mac80211: remove unnecessary byteshifts in frame control testing
- wireless: use get/put_unaligned_* helpers
- mac80211: tkip.c use kernel-provided infrastructure
- b43: replace limit_value macro with clamp_val
- b43legacy: replace limit_value macro with clamp_val
- b43: use the bitrev helpers rather than rolling a private one
- libertas: debug output tweaks for lbs_thread
- libertas: make some functions void
- libertas: allow removal of card at any time
- libertas: remove lbs_get_data_rate()
- b43: nphy.c remove duplicated include
- mac80211: Replace ieee80211_tx_control->key_idx with ieee80211_key_conf
- mac80211: Add IEEE80211_KEY_FLAG_PAIRWISE
- rt2x00: Support hardware RTS and CTS-to-self frames
- rt2x00: Remove DRIVER_SUPPORT_MIXED_INTERFACES
- rt2x00: Use rt2x00 queue numbering
- rt2x00: Add helper macros
- rt2x00: Fix kernel-doc
- rt2x00: Release rt2x00 2.1.5
- rt2x00: Clarify supported chipsets in Kconfig
- mac80211: Set IEEE80211_TXPD_REQ_TX_STATUS for all TX frames
- mac80211: a few code cleanups
- mac80211: clean up get_tx_stats callback
- mac80211: remove queue info from ieee80211_tx_status
- mac80211: QoS related cleanups
- mac80211: fix wme code
- mac80211: require four hardware queues for QoS/HT
- mac80211: proper STA info locking
- mac80211: fix queue constant confusion
- wireless: fix warning introduced by "mac80211: QoS related cleanups"
- ssb: Allow reading of 440-byte SPROM that is not rev 4
- b43: Rewrite LO calibration algorithm
- b43: Remove some dead code
- b43: Don't disable IRQs in mac_suspend
- iwlwifi: Add power level support
- airo: use netstats in net_device structure
- arlan: use netstats in net_device structure
- atmel: use netstats in net_device structure
- iwlwifi: arranging aggregation actions
- iwlwifi: expanding HW parameters control
- iwlwifi: support 64 bit DMA masks
- iwlwifi: handle shared memory
- iwlwifi: unify init driver flow
- iwlwifi: iwl-sta redundant includes clean up
- iwlwifi-5000: add iwl 5000 shared memory handlers
- iwlwifi: map A-MPDU HW queue to mac80211 A-MPDU SW queue
- iwlwifi-5000: rename iwl5000_init_nic to iwl5000_init_config
- iwlwifi: create disable SCD Tx FIFOs handler
- iwlwifi: move NIC init and Tx queues init to iwlcore
- iwlwifi: handle shared memory Rx index access
- iwlwifi: remove 4965 prefix from iwl4965_kw and iwl4965_tx_queue
- iwlwifi: fix spinlock used before initialized
- iwlwifi: move find station to iwl-sta.c
- iwlwifi: cleanup set_pwr_src
- iwlwifi: define ANA_PLL values in iwl-csr.h
- iwlwifi: export int iwl4965_set_pwr_src
- iwlwifi: changing EEPROM layout handling
- iwlwifi: remove includes to net/ieee80211.h
- iwlwifi: add apm init handler
- iwlwifi: add iwl_hw_detect function to iwl core
- iwlwifi: check eeprom version in pci probe time
- iwlwifi: reorganize TX RX constatns
- iwlwifi: 3945 remove unused SCD definitions
- iwlwifi: remove 49 prefix from general CSR values
- iwlwifi: remove unnecessary apmg settings
- iwlwifi: wrapping nic configuration in iwl core handler
- iwlwifi-5000: adding initial recognition for the 5000 family
- iwlwifi-5000: add ops infrastructure for 5000
- iwlwifi-5000: add apm_init handler for 5000 HW family
- iwlwifi-5000: use iwl4965_set_pwr_src in 5000
- iwlwifi-5000: EEPROM settings for 5000
- iwlwifi-5000: adding iwl5000 HW parameters
- iwlwifi-5000: adjust antennas names in 5000 HW family
- iwlwifi-5000: Add HW REV of 5000 HW family
- iwlwifi-5000: add eeprom check version handler
- iwlwifi-5000: add nic config handler for 5000 HW
- iwlwifi: rename iwl-4965-commands to iwl-commands.h
- iwlwifi: rename iwl-4965.h to iwl-dev.h
- iwlwifi: move RX code to iwl-rx.c
- iwlwifi: don't override association channel with control channel
- iwlwifi: remove 4965 from station_entry
- iwlwifi: debugfs EEPROM dump
- iwlwifi: remove 4965 from rx_packet
- iwlwifi: generalize iwl4965_send_add_station function
- iwlwifi-5000: add build_addsta_hcmd handler for 5000 HW
- iwlwifi: move iwl4965_set_rxon_ht into iwlcore
- iwlwifi: compile iwl-sta into iwlcore
- iwlwifi: add device sysfs version entry
- at76: use hardware flags for signal/noise units

* Mon May 19 2008 Alexandre Oliva <lxoliva@fsfla.org> 2.6.26-libre.0.17.rc3.fc10
- Deblob 2.6.26-rc3.

* Sun May 18 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3

* Sat May 17 2008 Eric Paris <eparis@redhat.com>
- SELinux: enable deffered context validation
- SELinux: don't sleep while holding locks in above patch
- SELinux: replace ioctl specific knowlege in the selinux code and follow generic permissions
- SELinux: not all reading in /proc needs ptrace, so make those things just use 'read' perms

* Sat May 17 2008 Dave Jones <davej@redhat.com>
- Enable PAGEALLOC debugging for a while. (Some things might get slow).

* Sat May 17 2008 Dave Jones <davej@redhat.com>
- Disable CONFIG_SND_PCSP (#447039)

* Sat May 17 2008 Alexandre Oliva <lxoliva@fsfla.org> 2.6.26-libre.0.13.rc2.git5.fc10 Sun May 18 2008
- Rebase to libre1.
- Deblob patch-2.6.26-rc2.

* Fri May 16 2008 Dave Jones <davej@redhat.com>
- Enable CONFIG_SND_SERIAL_U16550

* Fri May 16 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc2-git5

* Thu May 15 2008 Eric Sandeen <esandeen@redhat.com>
- ext3/4: fix uninitialized bs in ext3/4_xattr_set_handle()

* Wed May 14 2008 Eric Paris <eparis@redhat.com>
- fix may sleep in allocation for previous deffered context patch
- replace selinux specific knowledge of ioctls with a generic ioctl implementation

* Mon May 12 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25.3

* Fri May 09 2008 John W. Linville <linville@redhat.com> 2.6.25.2-7
- Regroup wireless patches as prep for 2.6.26 and F10 cycles

* Fri May 09 2008 Eric Paris <eparis@redhat.com>
- support deffered context validation in selinux.  aka rpm can lay down illegal labels. (won't upstream until .27 opens)

* Thu May  8 2008 Alexandre Oliva <lxoliva@fsfla.org> 2.6.25.2-libre.5
- Rebase to linux-2.6.25-libre.tar.bz2.

* Wed May 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.2-5
- Add the patches queued for 2.6.25.3
