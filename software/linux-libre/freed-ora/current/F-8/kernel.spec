Summary: The Linux kernel

# For a stable, released kernel, released_kernel should be 1. For rawhide
# and/or a kernel built from an rc or git snapshot, released_kernel should
# be 0.
%define released_kernel 1

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
# Bah. Have to set this to a negative for the moment to fix rpm ordering after
# moving the spec file. cvs sucks. Should be sure to fix this once 2.6.23 is out.
%define fedora_cvs_origin 440
%define fedora_build %(R="$Revision: 1.509 $"; R="${R%% \$}"; R="${R##: 1.}"; expr $R - %{fedora_cvs_origin})

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 25

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
%define librev 3

# To be inserted between "patch" and "-2.6.".
#define stablelibre -libre
#define rcrevlibre -libre
#define gitrevlibre -libre

# libres (s for suffix) may be bumped for rebuilds in which patches
# change but fedora_build doesn't.  Make sure it starts with a dot.
# It is appended after dist.
#define libres .

## If this is a released kernel ##
%if 0%{?released_kernel}
# Do we have a 2.6.21.y update to apply?
%define stable_update 14
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
%define rcrev 0
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

# Whether or not to gpg sign modules
%define with_modsign   %{?_without_modsign:   0} %{?!_without_modsign:   1}

# Whether or not to do C=1 builds with sparse
%define usesparse 0
%if "%fedora" > "8"
%define usesparse 1
%endif

# Whether or not to apply the Xen patches -- leave this enabled
%define includexen 0
# Xen doesn't work with current upstream kernel, shut it off
%define with_xen 0

# Set debugbuildsenabled to 1 for production (build separate debug kernels)
#  and 0 for rawhide (all kernels are debug kernels).
# See also 'make debug' and 'make release'.
%define debugbuildsenabled 1

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
%define kernel_image x86

%define xen_hv_cset 11633
%define xen_flags verbose=y crash_debug=y
%define xen_target vmlinuz
%define xen_image vmlinuz

%define KVERREL %{PACKAGE_VERSION}-libre.%{PACKAGE_RELEASE}
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
%define variant -vanilla
%else
%define variant -libre
%define variant_fedora -libre-fedora
%endif

%define using_upstream_branch 0
%if 0%{?upstream_branch:1}
%define using_upstream_branch 1
%define variant -%{upstream_branch}%{?variant_fedora}
%define pkg_release %{upstream_branch_release}.%{pkg_release}
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
# they can just use i386 and ppc64 and sparc headers
%ifarch i586 i686 ppc64iseries sparc64
%define with_headers 0
%endif

# don't build noarch kernels or headers (duh)
%ifarch noarch
%define with_up 0
%define with_headers 0
%define all_arch_configs kernel-%{version}-*.config
%endif

# don't sign modules on these platforms
%ifarch s390x sparc64 ppc alpha
%define with_modsign 0
%endif

# sparse blows up on ppc64
%ifarch ppc64 ppc alpha sparc64
%define usesparse 0
%endif

# Per-arch tweaks

%ifarch %{all_x86}
%define all_arch_configs kernel-%{version}-i?86*.config
%define image_install_path boot
%define hdrarch i386
# we build always xen i686 HV with pae
%define xen_flags verbose=y crash_debug=y pae=y
%endif

%ifarch x86_64
%define all_arch_configs kernel-%{version}-x86_64*.config
%define image_install_path boot
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
# Yes, this is a hack. We want both sets of headers in the sparc.rpm
%define hdrarch sparc64
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
%define with_modsign 0
# XXX temporary until last vdso patches are upstream
%define vdso_arches %{nil}
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
%define package_conflicts initscripts < 7.23, udev < 063-6, iptables < 1.3.2-1, ipw2200-firmware < 2.4, iwl4965-firmware < 228.57.2, selinux-policy-targeted < 1.25.3-14

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
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, mkinitrd >= 6.0.9-7

#
# This macro does requires, provides, conflicts, obsoletes for a kernel package.
#	%%kernel_reqprovconf <subpackage>
# It uses any kernel_<subpackage>_conflicts and kernel_<subpackage>_obsoletes
# macros defined above.
#
%define kernel_reqprovconf \
Provides: kernel = %{rpmversion}-%{pkg_release}\
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{pkg_release}%{?1}\
Provides: kernel-drm = 4.3.0\
Provides: kernel-drm-nouveau = 10\
Requires(pre): %{kernel_prereq}\
Conflicts: %{kernel_dot_org_conflicts}\
Conflicts: %{package_conflicts}\
%{?1:%{expand:%%{?kernel_%{1}_conflicts:Conflicts: %%{kernel_%{1}_conflicts}}}}\
%{?1:%{expand:%%{?kernel_%{1}_obsoletes:Obsoletes: %%{kernel_%{1}_obsoletes}}}}\
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
%if %{with_modsign}
BuildRequires: gnupg
%endif
BuildRequires: gcc >= 3.4.2, binutils >= 2.12, redhat-rpm-config
%if %{usesparse}
BuildRequires: sparse >= 0.3
%endif
BuildConflicts: rhbuildsys(DiskFree) < 500Mb

%define fancy_debuginfo 0
%if %{with_debuginfo}
%if "%fedora" > "7"
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

%if %{using_upstream_branch}
### BRANCH PATCH ###
%else
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

%endif

# -stable RC
# Patch02: patch-2.6.23.15-rc1.bz2

# we also need compile fixes for -vanilla
Patch07: linux-2.6-compile-fixes.patch
Patch08: linux-2.6-compile-fix-gcc-43.patch

%if !%{nopatches}

# revert upstream changes we get from elsewhere
Patch05: linux-2.6-upstream-reverts.patch
# patches queued for the next -stable release
Patch11: linux-2.6-stable-queue.patch

Patch21: linux-2.6-utrace.patch
Patch22: linux-2.6.25-utrace-bugon.patch
Patch23: linux-2.6.25-utrace-i386-syscall-trace.patch

Patch41: linux-2.6-sysrq-c.patch
Patch60: linux-2.6-x86-tune-generic.patch
Patch75: linux-2.6-x86-debug-boot.patch
Patch85: linux-2.6-x86-dont-map-vdso-when-disabled.patch
Patch86: linux-2.6-x86-dont-use-disabled-vdso-for-signals.patch
Patch87: linux-2.6-x86-apic-dump-all-regs-v3.patch
Patch88: linux-2.6-x86-mm-ioremap-64-bit-resource-on-32-bit-kernel.patch

Patch90: linux-2.6-alsa-hda-codec-add-AD1884A-mobile.patch
Patch91: linux-2.6-alsa-hda-codec-add-AD1884A-x300.patch
Patch92: linux-2.6-alsa-hda-codec-add-AD1884A.patch
Patch93: linux-2.6-alsa-kill-annoying-messages.patch

Patch123: linux-2.6-ppc-rtc.patch
Patch130: linux-2.6-ppc-use-libgcc.patch
Patch140: linux-2.6-ps3-ehci-iso.patch
Patch141: linux-2.6-ps3-storage-alias.patch
Patch142: linux-2.6-ps3-legacy-bootloader-hack.patch
Patch143: linux-2.6-g5-therm-shutdown.patch
Patch144: linux-2.6-vio-modalias.patch
Patch145: linux-2.6-windfarm-pm121.patch
Patch146: linux-2.6-windfarm-pm121-fix.patch
Patch147: linux-2.6-imac-transparent-bridge.patch
Patch148: linux-2.6-powerpc-zImage-32MiB.patch
Patch149: linux-2.6-efika-not-chrp.patch

Patch150: linux-2.6-build-nonintconfig.patch
Patch160: linux-2.6-execshield.patch
Patch170: linux-2.6-modsign-mpilib.patch
Patch180: linux-2.6-modsign-crypto.patch
Patch190: linux-2.6-modsign-include.patch
Patch200: linux-2.6-modsign-verify.patch
Patch210: linux-2.6-modsign-ksign.patch
Patch220: linux-2.6-modsign-core.patch
Patch230: linux-2.6-modsign-script.patch
Patch250: linux-2.6-debug-sizeof-structs.patch
Patch260: linux-2.6-debug-nmi-timeout.patch
Patch270: linux-2.6-debug-taint-vm.patch
Patch280: linux-2.6-debug-spinlock-taint.patch
Patch330: linux-2.6-debug-no-quiet.patch
Patch340: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch350: linux-2.6-devmem.patch
Patch370: linux-2.6-crash-driver.patch

Patch400: linux-2.6-scsi-cpqarray-set-master.patch
Patch402: linux-2.6-scsi-mpt-vmware-fix.patch

# filesystem patches
Patch421: linux-2.6-squashfs.patch
Patch424: linux-2.6-gfs-locking-exports.patch
Patch425: linux-2.6-nfs-client-mounts-hang.patch
Patch426: linux-2.6-fs-fat-cleanup-code.patch
Patch427: linux-2.6-fs-fat-fix-setattr.patch
Patch428: linux-2.6-fs-fat-relax-permission-check-of-fat_setattr.patch

Patch430: linux-2.6-net-silence-noisy-printks.patch

Patch440: linux-2.6-sha_alignment.patch
Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch460: linux-2.6-serial-460800.patch
Patch510: linux-2.6-silence-noise.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sysrq-add-show-backtrace-on-all-cpus-function.patch
Patch600: linux-2.6-vm-silence-atomic-alloc-failures.patch

Patch610: linux-2.6-defaults-fat-utf8.patch
Patch640: linux-2.6-defaults-pci_no_msi.patch

Patch670: linux-2.6-ata-quirk.patch
Patch672: linux-2.6-libata-acpi-hotplug-fixups.patch
Patch673: linux-2.6-libata-be-a-bit-more-slack-about-early-devices.patch
Patch674: linux-2.6-sata-eeepc-faster.patch
Patch675: linux-2.6-libata-acpi-handle-bay-devices-in-dock-stations.patch
Patch678: linux-2.6-libata-ata_piix-dont-attach-to-ich6m-in-ahci-mode.patch
Patch679: linux-2.6-libata-acpi-fix-invalid-context-acpi.patch

Patch680: linux-2.6-wireless.patch
Patch681: linux-2.6-wireless-pending.patch
Patch682: linux-2.6-wireless-fixups.patch
Patch683: linux-2.6-rt2500usb-fix.patch
Patch690: linux-2.6-at76.patch
Patch691: linux-2.6-zd1211rw-module-alias.patch
Patch692: linux-2.6-cfg80211-extras.patch

Patch720: linux-2.6-e1000-corrupt-eeprom-checksum.patch
Patch721: linux-2.6-netdev-e1000-disable-alpm.patch
Patch725: linux-2.6-netdev-atl2.patch
Patch726: linux-2.6-netdev-atl1e.patch
Patch727: linux-2.6-e1000-ich9.patch
Patch728: linux-2.6-bluetooth-signal-userspace-for-socket-errors.patch

Patch768: linux-2.6-acpi-fix-sizeof.patch
Patch769: linux-2.6-acpi-fix-error-with-external-methods.patch
Patch784: linux-2.6-acpi-eeepc-hotkey.patch

Patch1101: linux-2.6-default-mmf_dump_elf_headers.patch

Patch1308: linux-2.6-usb-ehci-hcd-respect-nousb.patch

Patch1400: linux-2.6-smarter-relatime.patch

Patch1515: linux-2.6-lirc.patch

# nouveau + drm fixes
Patch1802: nouveau-drm.patch

# Updated firewire stack from linux1394 git
Patch1910: linux-2.6-firewire-git-update.patch
Patch1911: linux-2.6-firewire-git-pending.patch

# usb video
Patch2400: linux-2.6-uvcvideo.patch

%endif

BuildRoot: %{_tmppath}/kernel-%{KVERREL}-root-%{_target_cpu}

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
Provides: kernel-doc = %{rpmversion}-%{pkg_release}
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
Provides: kernel-headers = %{rpmversion}-%{pkg_release}
%description headers
Kernel-headers includes the C header files that specify the interface
between the Linux kernel and userspace libraries and programs.  The
header files define structures and constants that are needed for
building most standard programs and are also needed for rebuilding the
glibc package.


%package debuginfo-common
Summary: Kernel source files used by %{name}-debuginfo packages
Group: Development/Debug
Provides: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
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
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}\
Provides: %{name}%{?1:-%{1}}-debuginfo-%{_target_cpu} = %{KVERREL}\
AutoReqProv: no\
%description -n %{name}%{?1:-%{1}}-debuginfo\
This package provides debug information for package %{name}%{?1:-%{1}}.\
This is required to use SystemTap with %{name}%{?1:-%{1}}-%{KVERREL}.\
%{expand:%%global debuginfo_args %{?debuginfo_args} -p '/.*/%%{KVERREL}%{?1:-?%{1}}(-%%{_target_cpu})?/.*|/.*%%{KVERREL}%{?1}' -o debuginfo%{?1}.list}\
%{nil}

#
# This macro creates a kernel-<subpackage>-devel package.
#	%%kernel_devel_package <subpackage> <pretty-name>
#
%define kernel_devel_package() \
%package %{?1:%{1}-}devel\
Summary: Development package for building kernel modules to match the %{?2:%{2} }kernel\
Group: System Environment/Kernel\
Provides: kernel%{?1:-%{1}}-devel-%{_target_cpu} = %{rpmversion}-%{release}\
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}%{?1}\
Provides: kernel-devel = %{rpmversion}-%{release}%{?1}\
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
%kernel_variant_package PAE-debug
%description PAE-debug
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

# First we unpack the kernel tarball.
# If this isn't the first make prep, we use links to the existing clean tarball
# which speeds things up quite a bit.
if [ ! -d kernel-%{kversion}/vanilla ]; then
  # Ok, first time we do a make prep.
  rm -f pax_global_header
%setup -q -n kernel-%{kversion} -c
  mv linux-%{kversion} vanilla
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

cp -rl vanilla linux-%{kversion}.%{_target_cpu}

cd linux-%{kversion}.%{_target_cpu}

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

%if %{using_upstream_branch}
### BRANCH APPLY ###
%else

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =/" Makefile

# Update to latest upstream.
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

%endif

# -stable RC
# ApplyPatch patch-2.6.23.15-rc1.bz2

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
ApplyPatch linux-2.6-compile-fix-gcc-43.patch

%if !%{nopatches}

# Revert -stable pieces we get from elsewhere here
ApplyPatch linux-2.6-upstream-reverts.patch -R
ApplyPatch linux-2.6-stable-queue.patch

# Roland's utrace ptrace replacement.
ApplyPatch linux-2.6-utrace.patch
ApplyPatch linux-2.6.25-utrace-bugon.patch
ApplyPatch linux-2.6.25-utrace-i386-syscall-trace.patch

# ALSA Thinkpad X300 support
ApplyPatch linux-2.6-alsa-hda-codec-add-AD1884A.patch
ApplyPatch linux-2.6-alsa-hda-codec-add-AD1884A-mobile.patch
ApplyPatch linux-2.6-alsa-hda-codec-add-AD1884A-x300.patch
# kill annoying messages
ApplyPatch linux-2.6-alsa-kill-annoying-messages.patch

# Nouveau DRM + drm fixes
ApplyPatch nouveau-drm.patch

# enable sysrq-c on all kernels, not only kexec
ApplyPatch linux-2.6-sysrq-c.patch

# Architecture patches
# IA64
# x86(-64)
# Compile 686 kernels tuned for Pentium4.
ApplyPatch linux-2.6-x86-tune-generic.patch
# debug early boot
#ApplyPatch linux-2.6-x86-debug-boot.patch
# don't map or use disabled x86 vdso
ApplyPatch linux-2.6-x86-dont-map-vdso-when-disabled.patch
ApplyPatch linux-2.6-x86-dont-use-disabled-vdso-for-signals.patch
# dump *PIC state at boot with apic=debug
ApplyPatch linux-2.6-x86-apic-dump-all-regs-v3.patch
# fix 64-bit resource on 32-bit kernels
ApplyPatch linux-2.6-x86-mm-ioremap-64-bit-resource-on-32-bit-kernel.patch

#
# PowerPC
#
###  UPSTREAM PATCHES:
###  NOT (YET) UPSTREAM:
# RTC class driver for ppc_md rtc functions
ApplyPatch linux-2.6-ppc-rtc.patch
# use libgcc
ApplyPatch linux-2.6-ppc-use-libgcc.patch
# The EHCI ISO patch isn't yet upstream but is needed to fix reboot
ApplyPatch linux-2.6-ps3-ehci-iso.patch
# The storage alias patch is Fedora-local, and allows the old 'ps3_storage'
# module name to work on upgrades. Otherwise, I believe mkinitrd will fail
# to pull the module in,
ApplyPatch linux-2.6-ps3-storage-alias.patch
# Support booting from Sony's original released 2.6.16-based kboot
ApplyPatch linux-2.6-ps3-legacy-bootloader-hack.patch
# Alleviate G5 thermal shutdown problems
ApplyPatch linux-2.6-g5-therm-shutdown.patch
# Provide modalias in sysfs for vio devices
ApplyPatch linux-2.6-vio-modalias.patch
# Fan support on iMac G5 iSight
ApplyPatch linux-2.6-windfarm-pm121.patch
ApplyPatch linux-2.6-windfarm-pm121-fix.patch
# Work around PCIe bridge setup on iSight
ApplyPatch linux-2.6-imac-transparent-bridge.patch
# Link zImage at 32MiB (for POWER machines, Efika)
ApplyPatch linux-2.6-powerpc-zImage-32MiB.patch
# Don't show 'CHRP' in /proc/cpuinfo on Efika
ApplyPatch linux-2.6-efika-not-chrp.patch

#
# Exec shield
#
ApplyPatch linux-2.6-execshield.patch

#
# GPG signed kernel modules
#
ApplyPatch linux-2.6-modsign-mpilib.patch
ApplyPatch linux-2.6-modsign-crypto.patch
ApplyPatch linux-2.6-modsign-include.patch
ApplyPatch linux-2.6-modsign-verify.patch
ApplyPatch linux-2.6-modsign-ksign.patch
ApplyPatch linux-2.6-modsign-core.patch
ApplyPatch linux-2.6-modsign-script.patch

#
# bugfixes to drivers and filesystems
#
# pc speaker autoload

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
ApplyPatch linux-2.6-debug-nmi-timeout.patch
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-spinlock-taint.patch
%if !%{debugbuildsenabled}
ApplyPatch linux-2.6-debug-no-quiet.patch
%endif
ApplyPatch linux-2.6-debug-vm-would-have-oomkilled.patch

#
# Make /dev/mem a need-to-know function
#
ApplyPatch linux-2.6-devmem.patch

#
# /dev/crash driver for the crashdump analysis tool
#
ApplyPatch linux-2.6-crash-driver.patch

#
# driver core
#

#
# SCSI Bits.
#
# fix cpqarray pci enable
ApplyPatch linux-2.6-scsi-cpqarray-set-master.patch
# Fix async scanning double-add problems
# fix vmware emulated scsi controller
ApplyPatch linux-2.6-scsi-mpt-vmware-fix.patch
# fin initio driver

# Filesystem patches.
# Squashfs
ApplyPatch linux-2.6-squashfs.patch
# export symbols for gfs2 locking modules
ApplyPatch linux-2.6-gfs-locking-exports.patch
# fix nfs mount hang
ApplyPatch linux-2.6-nfs-client-mounts-hang.patch
# fix rsync inability to write to vfat partitions
ApplyPatch linux-2.6-fs-fat-cleanup-code.patch
ApplyPatch linux-2.6-fs-fat-fix-setattr.patch
ApplyPatch linux-2.6-fs-fat-relax-permission-check-of-fat_setattr.patch

# Networking
# Disable easy to trigger printk's.
ApplyPatch linux-2.6-net-silence-noisy-printks.patch

# Misc fixes
# Fix SHA1 alignment problem on ia64
ApplyPatch linux-2.6-sha_alignment.patch
# The input layer spews crap no-one cares about.
ApplyPatch linux-2.6-input-kill-stupid-messages.patch
# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch
# add ids for new wacom tablets

# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch

# Fix the SELinux mprotect checks on executable mappings
ApplyPatch linux-2.6-selinux-mprotect-checks.patch

# add "show backtrace on all cpus" (sysrq-l)
ApplyPatch linux-2.6-sysrq-add-show-backtrace-on-all-cpus-function.patch

#
# VM related fixes.
#
# Silence GFP_ATOMIC failures.
ApplyPatch linux-2.6-vm-silence-atomic-alloc-failures.patch

# Changes to upstream defaults.
# Use UTF-8 by default on VFAT.
ApplyPatch linux-2.6-defaults-fat-utf8.patch
# Disable PCI MMCONFIG by default.
ApplyPatch linux-2.6-defaults-pci_no_msi.patch

# libata
#
# ia64 ata quirk
ApplyPatch linux-2.6-ata-quirk.patch
# fix hangs on undock (#439197)
ApplyPatch linux-2.6-libata-acpi-hotplug-fixups.patch
# fix problems with some old/broken CF hardware (F8 #224005)
ApplyPatch linux-2.6-libata-be-a-bit-more-slack-about-early-devices.patch
# make eeepc ata go faster
ApplyPatch linux-2.6-sata-eeepc-faster.patch
# fix docking on stations that have a bay device
ApplyPatch linux-2.6-libata-acpi-handle-bay-devices-in-dock-stations.patch
# fix ahci / ICH6 conflict
ApplyPatch linux-2.6-libata-ata_piix-dont-attach-to-ich6m-in-ahci-mode.patch
# fix invalid irq context in libata-acpi (#451896)
ApplyPatch linux-2.6-libata-acpi-fix-invalid-context-acpi.patch

# wireless
#
# wireless patches headed for 2.6.26
ApplyPatch linux-2.6-wireless.patch
# wireless patches headed for 2.6.27
ApplyPatch linux-2.6-wireless-pending.patch
# Add misc wireless bits from upstream wireless tree
ApplyPatch linux-2.6-at76.patch
# fixups to make current wireless patches build on this kernel
ApplyPatch linux-2.6-wireless-fixups.patch
# fix for long-standing rt2500usb issues
ApplyPatch linux-2.6-rt2500usb-fix.patch
# module alias for zd1211rw module
ApplyPatch linux-2.6-zd1211rw-module-alias.patch
# Restore ability to add/remove virtual i/fs to mac80211 devices
ApplyPatch linux-2.6-cfg80211-extras.patch

# Workaround for flaky e1000 EEPROMs
ApplyPatch linux-2.6-e1000-corrupt-eeprom-checksum.patch
# disable link power savings, should fix bad eeprom checksum too
ApplyPatch linux-2.6-netdev-e1000-disable-alpm.patch
# make new ich9 e1000 work
ApplyPatch linux-2.6-e1000-ich9.patch
# add atl2 network driver for eeepc
ApplyPatch linux-2.6-netdev-atl2.patch
# add atl1e network driver for eeepc 901
ApplyPatch linux-2.6-netdev-atl1e.patch
# fix bluetooth kbd disconnect
ApplyPatch linux-2.6-bluetooth-signal-userspace-for-socket-errors.patch

# ACPI/PM patches
# acpi has a bug in the sizeof function causing thermal panics (from 2.6.26)
ApplyPatch linux-2.6-acpi-fix-sizeof.patch
ApplyPatch linux-2.6-acpi-fix-error-with-external-methods.patch
# properly disable stray interrupts in acpi (??? in 2.6.25)
#ApplyPatch linux-2.6-acpi-disable-stray-interrupt-1.patch
#ApplyPatch linux-2.6-acpi-disable-stray-interrupt-2.patch
# Eeepc hotkey driver
ApplyPatch linux-2.6-acpi-eeepc-hotkey.patch

# dm / md

# ACPI

# USB
# respect the 'nousb' boot option
ApplyPatch linux-2.6-usb-ehci-hcd-respect-nousb.patch

# ISDN

# implement smarter atime updates support.
ApplyPatch linux-2.6-smarter-relatime.patch

# build id related enhancements
ApplyPatch linux-2.6-default-mmf_dump_elf_headers.patch

# http://www.lirc.org/
ApplyPatch linux-2.6-lirc.patch

# FireWire updates and fixes
# snap from http://me.in-berlin.de/~s5r6/linux1394/updates/
ApplyPatch linux-2.6-firewire-git-update.patch
ApplyPatch linux-2.6-firewire-git-pending.patch

# usb video
ApplyPatch linux-2.6-uvcvideo.patch

# ---------- below all scheduled for 2.6.25 -----------------

# SELinux perf patches

# END OF PATCH APPLICATIONS

%endif

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl

cp %{SOURCE10} Documentation/

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

# get rid of unwanted files resulting from patch fuzz
find . \( -name "*.orig" -o -name "*~" \) -exec rm -f {} \; >/dev/null

cd ..

# Unpack the Xen tarball.
%if %{includexen}
cp %{SOURCE2} .
if [ -d xen ]; then
  rm -rf xen
fi
%setup -D -T -q -n kernel-%{version} -a1
cd xen
# Any necessary hypervisor patches go here

%endif


###
### build
###
%build

%if %{usesparse}
%define sparse_mflags	C=1
%endif

#
# Create gpg keys for signing the modules
#
%if %{with_modsign}
gpg --homedir . --batch --gen-key %{SOURCE11}
gpg --homedir . --export --keyring ./kernel.pub Red > extract.pub
make linux-%{kversion}.%{_target_cpu}/scripts/bin2c
linux-%{kversion}.%{_target_cpu}/scripts/bin2c ksign_def_public_key __initdata < extract.pub > linux-%{kversion}.%{_target_cpu}/crypto/signature/key.h
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

BuildKernel() {
    MakeTarget=$1
    KernelImage=$2
    Flavour=$3

    # Pick the right config file for the kernel we're building
    if [ -n "$Flavour" ] ; then
      Config=kernel-%{version}-%{_target_cpu}-$Flavour.config
      DevelDir=/usr/src/kernels/%{KVERREL}-$Flavour-%{_target_cpu}
      DevelLink=/usr/src/kernels/%{KVERREL}$Flavour-%{_target_cpu}
    else
      Config=kernel-%{version}-%{_target_cpu}.config
      DevelDir=/usr/src/kernels/%{KVERREL}-%{_target_cpu}
      DevelLink=
    fi

    KernelVer=%{version}-libre.%{release}$Flavour
    echo BUILDING A KERNEL FOR $Flavour %{_target_cpu}...

    # make sure EXTRAVERSION says what we want it to say
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = %{?stablerev}-libre.%{release}$Flavour/" Makefile

    # if pre-rc1 devel kernel, must fix up SUBLEVEL for our versioning scheme
    %if !0%{?rcrev}
    %if 0%{?gitrev}
    perl -p -i -e 's/^SUBLEVEL.*/SUBLEVEL = %{upstream_sublevel}/' Makefile
    %endif
    %endif

    # and now to start the build process

    make -s mrproper
    cp configs/$Config .config

    %if !%{with_debuginfo}
    perl -p -i -e 's/^CONFIG_DEBUG_INFO=y$/# CONFIG_DEBUG_INFO is not set/' .config
    %endif

    Arch=`head -1 .config | cut -b 3-`
    echo USING ARCH=$Arch

    if [ "$KernelImage" == "x86" ]; then
       KernelImage=arch/$Arch/boot/bzImage
    fi

    make -s ARCH=$Arch %{oldconfig_target} > /dev/null
    make -s ARCH=$Arch %{?_smp_mflags} $MakeTarget %{?sparse_mflags} \
    	 ${idhack+"$idhack"}
    make -s ARCH=$Arch %{?_smp_mflags} modules %{?sparse_mflags} || exit 1

    # Start installing the results
%if %{with_debuginfo}
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/boot
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/%{image_install_path}
%endif
    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}
    install -m 644 .config $RPM_BUILD_ROOT/boot/config-$KernelVer
    install -m 644 System.map $RPM_BUILD_ROOT/boot/System.map-$KernelVer
    touch $RPM_BUILD_ROOT/boot/initrd-$KernelVer.img
    cp $KernelImage $RPM_BUILD_ROOT/%{image_install_path}/vmlinuz-$KernelVer
    if [ -f arch/$Arch/boot/zImage.stub ]; then
      cp arch/$Arch/boot/zImage.stub $RPM_BUILD_ROOT/%{image_install_path}/zImage.stub-$KernelVer || :
    fi

    if [ "$Flavour" == "kdump" ]; then
        cp vmlinux $RPM_BUILD_ROOT/%{image_install_path}/vmlinux-$KernelVer
        rm -f $RPM_BUILD_ROOT/%{image_install_path}/vmlinuz-$KernelVer
    fi

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
    			 'ata_scsi_ioctl|scsi_add_host|blk_init_queue'

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
    [ -z "$DevelLink" ] || ln -sf `basename $DevelDir` $RPM_BUILD_ROOT/$DevelLink
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
  install -m 644 xen.gz $RPM_BUILD_ROOT/%{image_install_path}/xen.gz-%{KVERREL}
  install -m 755 xen-syms $RPM_BUILD_ROOT/boot/xen-syms-%{KVERREL}
  cd ..
%endif
%endif

cd linux-%{kversion}.%{_target_cpu}

%if %{with_debug}
BuildKernel %make_target %kernel_image debug
%if %{with_pae}
BuildKernel %make_target %kernel_image PAE-debug
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
BuildKernel %make_target %kernel_image kdump
%endif

%if %{with_modsign}
# gpg sign the modules
gcc $RPM_OPT_FLAGS -o scripts/modsign/mod-extract scripts/modsign/mod-extract.c

# We do this on the installed, stripped .ko files in $RPM_BUILD_ROOT
# rather than as we are building them.  The __arch_install_post macro
# comes after __debug_install_post, which is what runs find-debuginfo.sh.
# This is necessary because the debugedit changes to the build ID bits
# change the contents of the .ko that go into the signature.  A signature
# made before debugedit is no longer correct for the .ko contents we'll
# have in the end.
%define __arch_install_post \
find $RPM_BUILD_ROOT/lib/modules -name '*.ko' |\
(cd %{_builddir}/%{buildsubdir}/linux-%{kversion}.%{_target_cpu}\
while read i\
do\
  GNUPGHOME=.. sh ./scripts/modsign/modsign.sh $i Red\
  mv -f $i.signed $i\
done)\
%{nil}
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
rm -f $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf
cat > $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf <<\EOF
# This directive teaches ldconfig to search in nosegneg subdirectories
# and cache the DSOs there with extra bit 0 set in their hwcap match
# fields.  In Xen guest kernels, the vDSO tells the dynamic linker to
# search in nosegneg subdirectories and to match this extra hwcap bit
# in the ld.so.cache file.
hwcap 0 nosegneg
EOF
chmod 444 $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf
%endif
%endif

%if %{with_doc}
mkdir -p $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}/Documentation

# sometimes non-world-readable files sneak into the kernel source tree
chmod -R a+r *
# copy the source over
tar cf - Documentation | tar xf - -C $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}
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
    (cd /usr/src/kernels/%{KVERREL}-%{?1:%{1}-}%{_target_cpu} &&\
     /usr/bin/find . -type f | while read f; do\
       hardlink -c /usr/src/kernels/*.fc*-*/$f $f\
     done)\
fi\
%{nil}

#
# This macro defines a %%post script for a kernel package and its devel package.
#	%%kernel_variant_post [-v <subpackage>] [-s <s> -r <r>] <mkinitrd-args>
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_post(s:r:v:) \
%{expand:%%kernel_devel_post %{?-v*}}\
%{expand:%%post %{?-v*}}\
%{-s:\
if [ `uname -i` == "x86_64" -o `uname -i` == "i386" ] &&\
   [ -f /etc/sysconfig/kernel ]; then\
  /bin/sed -i -e 's/^DEFAULTKERNEL=%{-s*}$/DEFAULTKERNEL=%{-r*}/' /etc/sysconfig/kernel || exit $?\
fi}\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --mkinitrd --depmod --install %{?1} %{KVERREL}%{?-v*} || exit $?\
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
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}%{?1} || exit $?\
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

%kernel_variant_post -v PAE-debug -s kernel-smp -r kernel-PAE-debug
%kernel_variant_preun PAE-debug

%kernel_variant_preun xen
%kernel_variant_post xen -v xen -s kernel-xen[0U] -r kernel-xen -- `[ -d /proc/xen -a ! -e /proc/xen/xsd_kva ] || echo --multiboot=/%{image_install_path}/xen.gz-%{KVERREL}`
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

# only some architecture builds need kernel-doc
%if %{with_doc}
%files doc
%defattr(-,root,root)
%{_datadir}/doc/kernel-doc-%{kversion}/Documentation/*
%dir %{_datadir}/doc/kernel-doc-%{kversion}/Documentation
%dir %{_datadir}/doc/kernel-doc-%{kversion}
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
/%{image_install_path}/%{?-k:%{-k*}}%{!?-k:vmlinuz}-%{KVERREL}%{?2}\
/boot/System.map-%{KVERREL}%{?2}\
#/boot/symvers-%{KVERREL}%{?2}.gz\
/boot/config-%{KVERREL}%{?2}\
%{?-a:%{-a*}}\
%dir /lib/modules/%{KVERREL}%{?2}\
/lib/modules/%{KVERREL}%{?2}/kernel\
/lib/modules/%{KVERREL}%{?2}/build\
/lib/modules/%{KVERREL}%{?2}/source\
/lib/modules/%{KVERREL}%{?2}/extra\
/lib/modules/%{KVERREL}%{?2}/updates\
/lib/modules/%{KVERREL}%{?2}/weak-updates\
%ifarch %{vdso_arches}\
/lib/modules/%{KVERREL}%{?2}/vdso\
%endif\
/lib/modules/%{KVERREL}%{?2}/modules.block\
/lib/modules/%{KVERREL}%{?2}/modules.networking\
/lib/modules/%{KVERREL}%{?2}/modules.order\
%ghost /boot/initrd-%{KVERREL}%{?2}.img\
%{?-e:%{-e*}}\
%{expand:%%files %{?2:%{2}-}devel}\
%defattr(-,root,root)\
%verify(not mtime) /usr/src/kernels/%{KVERREL}%{?2:-%{2}}-%{_target_cpu}\
/usr/src/kernels/%{KVERREL}%{?2}-%{_target_cpu}\
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
%{debuginfodir}/%{elf_image_install_path}/*-%{KVERREL}%{?2}.debug\
%endif\
%{debuginfodir}/lib/modules/%{KVERREL}%{?2}\
%{debuginfodir}/usr/src/kernels/%{KVERREL}%{?2:-%{2}}-%{_target_cpu}\
%endif\
%endif\
%endif\
%endif\
%{nil}


%kernel_variant_files %{with_up}
%kernel_variant_files %{with_smp} smp
%kernel_variant_files %{with_debug} debug
%kernel_variant_files %{with_pae} PAE
%kernel_variant_files %{with_pae_debug} PAE-debug
%kernel_variant_files -k vmlinux %{with_kdump} kdump
%kernel_variant_files -a /%{image_install_path}/xen*-%{KVERREL} -e /etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf %{with_xen} xen


%changelog
* Mon Aug 04 2008 John W. Linville <linville@redhat.com> 2.6.25.14-69
- fix for long-standing rt2500usb issues (#411481)

* Sun Aug 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.14-68
- Add patches queued for 2.6.25.15.
- Add conflict against older iwl4965 firmware.

* Fri Aug 01 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.14-67
- Linux 2.6.25.14
  Dropped patches:
    linux-2.6-alsa-trident-spdif.patch
    linux-2.6-libata-retry-enabling-ahci.patch
    linux-2.6-libata-pata_atiixp-dont-disable.patch
  Reverted from 2.6.25.14:
    ath5k-don-t-enable-msi-we-cannot-handle-it-yet.patch
    b43legacy-release-mutex-in-error-handling-code.patch

* Fri Aug 01 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.66.fc8
- Deblob linux-2.6-wireless-pending.patch.

* Fri Aug 01 2008 John W. Linville <linville@redhat.com> 2.6.25.13-66
- Upstream wireless updates from 2008-07-14
  (http://marc.info/?l=linux-wireless&m=121606436000705&w=2)
- Upstream wireless fixes from 2008-07-29
  (http://marc.info/?l=linux-wireless&m=121737750023195&w=2)
- Revert at76_usb to version from before attempted mac80211 port

* Sun Jul 28 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.13-65
- Linux 2.6.25.13

* Fri Jul 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.12-64
- Fix 64-bit resource checking on 32-bit kernels. (F9#447143)

* Fri Jul 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.12-63
- Set default powersave timeout to 0 for the AC97 driver. (F9#450395)

* Thu Jul 24 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.12-62
- Linux 2.6.25.12

* Tue Jul 22 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.11-61
- libata-acpi: fix calling sleeping function in irq context (#451896, #454954)

* Mon Jul 21 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.60
- Fix provides from pkgrelease to pkg_release.

* Sun Jul 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.11-60
- x86: dump APIC/IOAPIC/PIC state at boot time

* Sun Jul 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.11-59
- USB: fix timer regression

* Sun Jul 20 2008 Kyle McMartin <kmcmartin@redhat.com>
- Add atl1e driver for eee 901.

* Fri Jul 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.11-57
- Add ALSA Trident driver fix from F9 kernel. (F9#453464)

* Fri Jul 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.11-56
- Add "show backtrace on all CPUs" (SysRq-l).

* Mon Jul 14 2008 Kyle McMartin <kmcmartin@redhat.com>
- Disable CONFIG_ACPI_SYSFS_POWER, which still seems to be confusing hal.

* Mon Jul 14 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.54.1 Jul 16
- Updated deblobbing to -libre3.

* Mon Jul 14 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.54
- Updated deblobbing to -libre2.

* Sun Jul 13 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.11-54
- Linux 2.6.25.11

* Fri Jul 11 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.53
- Deblobbed rtl8187b_reg_table in linux-2.6-wireless-pending.patch.

* Thu Jul 10 2008 John W. Linville <linville@redhat.com>  2.6.25.10-53
- Upstream wireless fixes from 2008-07-09
  (http://marc.info/?l=linux-netdev&m=121563769208664&w=2)
- Upstream wireless updates from 2008-07-08
  (http://marc.info/?l=linux-wireless&m=121554411325041&w=2)

* Wed Jul 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-52
- libata: don't let ata_piix driver attach to ICH6M in ahci mode (F9#430916)

* Wed Jul 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-51
- Fix reiserfs list corruption (F9#453699)

* Wed Jul 09 2008 Jarod Wilson <jwilson@redhat.com> 2.6.25.10-50
- Actually fix lirc_i2c oops and add new MCE receiver support this
  time, never actually made it in here... (#453348)

* Wed Jul 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-49
- hdaps: support new Lenovo notebook models (F9#449888)

* Tue Jul 08 2008 John W. Linville <linville@redhat.com> 2.6.25.10-48
- Upstream wireless fixes from 2008-07-07
  (http://marc.info/?l=linux-wireless&m=121546143025524&w=2)

* Mon Jul 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-47
- Disable file capabilities.
- Fix USB interrupt handling with shared interrupts.

* Fri Jul 04 2008 John W. Linville <linville@redhat.com> 2.6.25.10-46
- Upstream wireless fixes from 2008-07-02
  (http://marc.info/?l=linux-netdev&m=121503163124089&w=2)
- Upstream wireless updates from 2008-06-30
  (http://marc.info/?l=linux-wireless&m=121486432315033&w=2)
- Apply Stefan Becker's fix for bad hunk of wireless build fixups for 2.6.25
  (https://bugzilla.redhat.com/show_bug.cgi?id=453390#c36)

* Thu Jul 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-42
- Linux 2.6.25.10
- Reverted stable patch, not needed with utrace:
	x86_64-ptrace-fix-sys32_ptrace-task_struct-leak.patch
- Reverted part of this stable patch against drivers/net/wireless/strip.c
  (the driver eventually gets removed as part of the wireless updates):
	tty-fix-for-tty-operations-bugs.patch

* Wed Jul 02 2008 John W. Linville <linville@redhat.com> 2.6.25.9-41
- Upstream wireless fixes from 2008-06-30
  (http://marc.info/?l=linux-wireless&m=121485709702728&w=2)
- Upstream wireless updates from 2008-06-27
  (http://marc.info/?l=linux-wireless&m=121458164930953&w=2)

* Tue Jul 01 2008 Dave Jones <davej@redhat.com>
- Shorten summary in specfile.

* Fri Jun 27 2008 John W. Linville <linville@redhat.com> 2.6.25.9-39
- Upstream wireless fixes from 2008-06-27
  (http://marc.info/?l=linux-wireless&m=121459423021061&w=2)

* Fri Jun 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.9-39
- Fix bluetooth keyboard disconnect (F9#449872)

* Wed Jun 25 2008 John W. Linville <linville@redhat.com> 2.6.25.9-38
- Upstream wireless fixes from 2008-06-25
  (http://marc.info/?l=linux-wireless&m=121440912502527&w=2)
- Upstream wireless updates from 2008-06-14
  (http://marc.info/?l=linux-netdev&m=121346686508160&w=2)

* Tue Jun 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.9-37
- Linux 2.6.25.9

* Tue Jun 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.8-36
- pppolt2p: fix heap overflow (CVE-2008-2750) (#452110)

* Mon Jun 23 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.8-35
- libata: retry enable of AHCI mode before reporting an error (F9#452595)

* Mon Jun 23 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.8-34
- Linux 2.6.25.8
- Patches reverted from 2.6.25.8, already in Fedora:
    b43-fix-noise-calculation-warn_on.patch
    b43-fix-possible-null-pointer-dereference-in-dma-code.patch

* Fri Jun 20 2008 Dave Jones <davej@redhat.com>
- Fix hpwdt driver to not oops on init. (452183)

* Fri Jun 20 2008 Jarod Wilson <jwilson@redhat.com> 2.6.25.7-32
- firewire: add phy config packet send timeout, prevents deadlock
  with flaky ALi controllers (#446763, #444694)

* Mon Jun 16 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.7-31
- Linux 2.6.25.7
- Apply compile-fixes patches to -vanilla kernels.
- Dropped patches:
    linux-2.6-alsa-emu10k1-fix-audigy2.patch
    linux-2.6-netlink-fix-parse-of-nested-attributes.patch
    linux-2.6-af_key-fix-selector-family-initialization.patch
    linux-2.6-mmc-wbsd-fix-request_irq.patch
- Reverted wireless patches from 2.6.25.7, already in Fedora:
    b43-fix-controller-restart-crash.patch
    mac80211-send-association-event-on-ibss-create.patch
    ssb-fix-context-assertion-in-ssb_pcicore_dev_irqvecs_enable.patch

* Sun Jun 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-30
- Make rsync able to write to VFAT partitions again. (#450493)

* Sat Jun 14 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-29
- Enable Controller Area Networking (#451179)

* Fri Jun 13 2008 John W. Linville <linville@redhat.com> 2.6.25.6-28
- Upstream wireless fixes from 2008-06-13
  (http://marc.info/?l=linux-wireless&m=121339101523260&w=2)

* Thu Jun 12 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-26
- Fix UML breakage (#450501)

* Tue Jun 10 2008 John W. Linville <linville@redhat.com> 2.6.25.6-25
- Upstream wireless fixes from 2008-06-09
  (http://marc.info/?l=linux-kernel&m=121304710726632&w=2)
- Upstream wireless updates from 2008-06-09
  (http://marc.info/?l=linux-netdev&m=121304710526613&w=2)

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-24
- Copy utrace and mmc driver bug fixes from F-9.

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-23
- Fix init of af_key sockets (#450499)

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-22
- Copy netlink message parsing bug fix from Fedora 9.

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-21
- Fix hang with audigy2 sound card (#326411)

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-20
- Linux 2.6.25.6
- Dropped patches:
    linux-2.6-x86-fix-asm-constraint-in-do_IRQ.patch
    linux-2.6-x86-pci-revert-remove-default-rom-allocation.patch
    linux-2.6-x86-dont-read-maxlvt-if-apic-unmapped.patch
    linux-2.6-cifs-fix-unc-path-prefix.patch
    linux-2.6-ext34-xattr-fix.patch
    linux-2.6-xfs-small-buffer-reads.patch
    linux-2.6-net-iptables-add-xt_iprange-aliases.patch
    linux-2.6-libata-force-hardreset-in-sleep-mode.patch
    
* Sat Jun 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.5-19
- Linux 2.6.25.5

* Thu Jun 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-18
- Make DMA work again on atiixp PATA devices (#437163)

* Thu Jun 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-17
- Fix docking when docking station has a bay device (bug reported on IRC.)

* Tue Jun 03 2008 John W. Linville <linville@redhat.com> 2.6.25.4-16
- Upstream wireless fixes from 2008-06-03
  (http://marc.info/?l=linux-wireless&m=121252137324941&w=2)

* Mon Jun 02 2008 Jarod Wilson <jwilson@redhat.com> 2.6.25.4-15
- Fix oops in lirc_i2c module
- Add lirc support for additional MCE receivers

* Thu May 29 2008 John W. Linville <linville@redhat.com> 2.6.25.4-14
- Upstream wireless updates from 2008-05-22
  (http://marc.info/?l=linux-wireless&m=121146112404515&w=2)
- Upstream wireless fixes from 2008-05-28
  (http://marc.info/?l=linux-wireless&m=121201250110162&w=2)

* Tue May 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-13
- Remove obsolete unicode patch.

* Tue May 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-12
- libata: fix hangs on undock (#439197)
- libata: fix problems with some old/broken CF hardware (F8 #224005)

* Tue May 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-11
- Remove already-merged libata pata_ali DMA disable patch.
- Add missing libata patch from the F9 kernel.

* Thu May 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-10
- Revert to the "old" RTC driver for F8.

* Thu May 22 2008 Dave Jones <davej@redhat.com> 2.6.25.4-9
- Disable CONFIG_DMAR. This is terminally broken in the presence of a broken BIOS

* Thu May 22 2008 John W. Linville <linville@redhat.com> 2.6.25.4-8
- Restore ability to add/remove virtual i/fs to mac80211 devices via sysfs

* Wed May 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-7
- Disable CONFIG_PADLOCK_SHA on i686.
- Package the file modules.order.
- Disable sound debugging.

* Wed May 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-5
- Sync wireless with last F9 update.
- Re-add module alias for zd1211rw.

* Wed May 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-4
- Actually apply the firewire-git-pending and utrace patches.
- Add the wireless-fixups patch to the patch list.
- Fix ACPI error in the SizeOf() operator. (#436959)
- Add missing netfilter and NFS patches from F9.
- Set CONFIG_SYSFS_DEPRECATED[_V2]

* Wed May 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-3
- Initial cut of 2.6.25.4 kernel -- passes 'make prep'.

* Wed May 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-2
- eeepc, wireless, acpi, lirc, and uvcvideo updates for 2.6.25.

* Wed May 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-1
- Initial commit of kernel 2.6.25.4; does not build yet.

* Mon May 19 2008 Dave Jones <davej@redhat.com>
- Disable PATA_ISAPNP (it's busted).

* Mon May 19 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.93.1
- Rebase to libre1.

* Fri May 09 2008 John W. Linville <linville@redhat.com> 2.6.24.7-93
- make sta_rx_agg_session_timer_expired() static
- remove ieee80211_tx_frame()
- remove ieee80211_wx_{get,set}_auth()
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
- mac80211: correct skb allocation
- iwlwifi: remove support for Narrow Channel (10Mhz)
- iwlwifi: HT antenna/chains overhaul
- iwlwifi: TLC modifications
- iwlwifi: rate scale module cleanups
- iwlwifi: rate scale restructure toggle_antenna functions
- iwlwifi: rs fix wrong parenthesizing in rs_get_lower_rate function
- iwlwifi: rate sacaling fixes
- iwlwifi: more RS improvements
- libertas: debug output tweaks for lbs_thread
- libertas: make some functions void
- libertas: allow removal of card at any time
- mac80211: Replace ieee80211_tx_control->key_idx with ieee80211_key_conf
- mac80211: Add IEEE80211_KEY_FLAG_PAIRWISE
- rt2x00: Support hardware RTS and CTS-to-self frames
- rt2x00: Remove DRIVER_SUPPORT_MIXED_INTERFACES
- rt2x00: Use rt2x00 queue numbering
- rt2x00: Add helper macros
- rt2x00: Fix kernel-doc
- rt2x00: Release rt2x00 2.1.5
- rt2x00: Clarify supported chipsets in Kconfig
- mac80211: a few code cleanups
- mac80211: clean up get_tx_stats callback
- mac80211: remove queue info from ieee80211_tx_status
- mac80211: QoS related cleanups
- mac80211: fix wme code
- wireless: fix warning introduced by "mac80211: QoS related cleanups"
- ssb: Allow reading of 440-byte SPROM that is not rev 4
- b43: Rewrite LO calibration algorithm
- b43: Remove some dead code
- b43: Don't disable IRQs in mac_suspend
- iwlwifi: Add power level support
- iwlwifi: arranging aggregation actions
- iwlwifi: expanding HW parameters control
- iwlwifi: support 64 bit DMA masks
- iwlwifi: handle shared memory
- iwlwifi: unify init driver flow
- iwlwifi: iwl-sta redundant includes clean up
- iwlwifi-5000: add iwl 5000 shared memory handlers
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

* Thu May  8 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.92
- Rebase to linux-2.6.25-libre.tar.bz2.

* Wed May 07 2008 Neil Horman <nhorman@redhat.com> 2.6.24.7-92
- Return kcore access policy to upstream behavior (bz 241362)

* Tue May 06 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.24.7-91
- Linux 2.6.24.7

* Fri May 02 2008 John W. Linville <linville@redhat.com> 2.6.24.5-90
- iwlwifi: fix debug messages during scanning
- iwlwifi: fix current channel is not scanned
- rt2x00: Don't enable short preamble for 1MBs
- rt2x00: Fix quality/activity led handling
- Make linux/wireless.h be able to compile
- b43: Fix some TX/RX locking issues

* Thu May 01 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.6-89
- Linux 2.6.24.6

* Thu May 01 2008 John W. Linville <linville@redhat.com> 2.6.24.5-88
- mac80211: incorrect shift direction
- libertas: fix use-before-check violation
- mac80211: assign conf.beacon_control for mesh
- mac80211: don't allow invalid WDS peer addresses
- mac80211: insert WDS peer after adding interface
- mac80211: use 4-byte mesh sequence number
- b43: Fix dual-PHY devices

* Mon Apr 28 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.87
- Provide kernel-headers from kernel-libre-headers.
- Provide kernel-doc from kernel-libre-doc.
- Deblobbed nouveau-drm.patch.

* Thu Apr 24 2008 John W. Linville <linville@redhat.com> 2.6.24.5-87
- mac80211: Fix n-band association problem
- net/mac80211/rx.c: fix off-by-one
- mac80211: MAINTAINERS update
- ssb: Fix all-ones boardflags
- mac80211: update mesh EID values
- b43: Workaround invalid bluetooth settings
- b43: Fix HostFlags data types
- b43: Add more btcoexist workarounds
- b43: Workaround DMA quirks
- ath5k: Fix radio identification on AR5424/2424
- mac80211: Fix race between ieee80211_rx_bss_put and lookup routines.
- prism54: prism54_get_encode() test below 0 on unsigned index
- wireless: rndis_wlan: modparam_workaround_interval is never below 0.
- iwlwifi: Don't unlock priv->mutex if it isn't locked
- mac80211: fix use before check of Qdisc length

* Tue Apr 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.5-86
- Enable machine check exception handling on x86_64.

* Tue Apr 22 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.85 Apr 24
- Deblob patch-2.6.24.5.bz2.

* Sat Apr 19 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.24.5-85
- Linux 2.6.24.5
- linux-2.6-pci-keep-SMBus-hidden-on-nx6110.patch merged
- linux-2.6-usb-serial-ti_usb-fix-endpoint-requirements.patch merged
- linux-2.6-usb-serial-visor-fix-regression.patch merged

* Wed Apr 16 2008 John W. Linville <linville@redhat.com> 2.6.24.4-84
- iwlwifi: replace sprintf with scnprintf for debugfs output
- proc: switch /proc/driver/ray_cs/ray_cs to seq_file interface
- iwlwifi: add default WEP key host command
- iwlwifi: default WEP HW encryption
- iwlwifi: add 1X HW WEP support
- iwlwifi: maintain uCode key table state
- iwlwifi: moves security functions to iwl-sta.c
- iwlwifi: remove the statistics work
- iwlwifi: Fix TKIP update key and get_free_ucode_key
- iwlwifi: Use HW acceleration decryption by default
- libertas: convert libertas driver to use an event/cmdresp queue
- libertas: un-garbage various command structs
- rt2x00: Only free skb when beacon_update fails
- mac80211: fix key hwaccel race
- mac80211: further RCU fixes
- mac80211: fix spinlock recursion
- mac80211: fix key todo list order
- mac80211: allow WDS mode
- mac80211: rework scanning to account for probe response/beacon difference
- mlme.c: fixup some merge damage
- ssb-pcicore: Remove b44 TPS flag workaround
- b43: Add fastpath to b43_mac_suspend()
- iwlwifi: fix unload warning and error
- ath5k: Add RF2425 initvals
- ath5k: Misc fixes/cleanups
- mac80211: no BSS changes to driver from beacons processed during scanning
- iwl4965: make iwl4965_send_rxon_assoc asynchronous
- iwlwifi: make Makefile more concise
- iwlwifi: perform bss_info_changed post association work right away
- iwlwifi: move HW device registration
- iwlwifi: arrange max number of Tx queues
- b43legacy: fix TBTT and PU timings
- iwlwifi: generalize iwlwifi init flow
- iwlwifi: Fix byte count table for fragmented packets
- iwlwifi: move shared pointers to iwl_priv
- iwlwifi: hw_setting cleanup
- iwlwifi: support different num of tx and rx antennas
- iwlwifi: move the creation of LQ host command to iwlcore
- iwlwifi: introduce host commands callbacks
- iwlwifi: move rxon associated command to hcmd

* Wed Apr 16 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.4-83
- Back out FireWire patch requiring successive selfID complete
  events, needs more work to keep from causing sbp2 issues (#435550)

* Tue Apr 15 2008 John W. Linville <linville@redhat.com> 2.6.24.4-82
- rfkill: Fix device type check when toggling states
- rtl8187: Add missing priv->vif assignments
- Add rfkill to MAINTAINERS file
- Update rt2x00 MAINTAINERS entry
- mac80211: remove message on receiving unexpected unencrypted frames
- PS3: gelic: fix the oops on the broken IE returned from the hypervisor
- ssb: Fix usage of struct device used for DMAing
- b43legacy: Fix usage of struct device used for DMAing
- MAINTAINERS: move to generic repository for iwlwifi
- b43legacy: fix initvals loading on bcm4303
- b43legacy: fix DMA mapping leakage
- rndis_host: fix transfer size negotiation
- rndis_host: fix oops when query for OID_GEN_PHYSICAL_MEDIUM fails

* Tue Apr 15 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.4-81
- Resync FireWire drivers with latest upstream git tree:
  * Fix dvgrab on buggy TI chipsets (#243081). May fix #435550 too.
  * Work-around for buggy 1st-gen JMicron JMB38x controllers

* Fri Apr 11 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-80
- Prevent sys_io_getevents syscall from clobbering the esi register on x86.
  (F9#427707)

* Wed Apr 09 2008 John W. Linville <linville@redhat.com> 2.6.24.4-79
- rt61pci: rt61pci_beacon_update do not free skb twice
- ssb-pcicore: Fix IRQ TPS flag handling
- ssb-mipscore: Fix interrupt vectors
- mac80211: use short_preamble mode from capability if ERP IE not present
- mac80211: add station aid into ieee80211_tx_control
- p54: move to separate directory
- drivers/net/wireless/p54/net2280.h: silence checkpatch.pl
- wavelan_cs: stop inlining largish static functions
- libertas: move association code from join.c into scan.c
- libertas: move association code from scan.c into assoc.c
- libertas: move lbs_update_channel out of assoc.c
- libertas: remove lbs_get_fwversion()
- rt2x00: Use lib->config_filter() during scheduled packet filter config
- mac80211: fix defined but not used
- iwlwifi: fix some warnings
- mac80211: fix possible sta-debugfs work lockup
- mac80211: clean up IEEE80211_FC use
- iwlwifi: honour regulatory restrictions in scan code
- mac80211: make debugfs files root-only
- mac80211: fix ieee80211_ioctl_giwrate
- mac80211: fix sta-info pinning
- mac80211: fix key vs. sta locking problems
- mac80211: rename files
- mac80211: fix key debugfs default_key link
- Revert "mac80211: use a struct for bss->mesh_config"
- drivers/net/wireless/iwlwifi/iwl-debugfs.c: fix another '%llu' warning
- iwlwifi/Kconfg: make IWLWIFI_LEDS invisible
- drivers/net/wireless/iwlwifi/iwl-3945.h: correct CONFIG_IWL4965_LEDS typo
- cfg80211: default to regulatory max power for channel
- prism54: set carrier flags correctly
- ssb-pcmcia: IRQ and DMA related fixes
- b43: Add PIO support for PCMCIA devices
- ssb: Turn suspend/resume upside down
- ssb: Fix build for non-PCIhost
- ssb: Add support for block-I/O
- b43: Use SSB block-I/O to do PIO
- b43: Add more N-PHY stuff
- b43: Fix TBTT and PU timings
- b43: Beaconing fixes
- b43: Fix beacon BH update
- b43: Fix PHY TX control words in SHM
- b43: use b43_is_mode() call
- iwlwifi: fix rfkill memory error
- mac80211: notify mac from low level driver (iwlwifi)
- adm8211: remove commented-out code
- iwl4965: use IWLWIFI_LEDS config variable
- iwlwifi: ensure led registration complete as part of initialization
- mac80211: notify upper layers after lower
- mac80211: BA session debug prints changes
- mac80211: eliminate conf_ht
- iwlwifi: eliminate conf_ht
- mac80211: add association capabilty and timing info into bss_conf
- iwlwifi: Eliminate association from beacon
- iwlwifi: hw names cleanup
- iwlwifi: move driver status inliners into iwl-core.h
- iwlwifi: use ieee80211_frequency_to_channel

* Tue Apr 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-78
- [from F9] Leave debug config files alone when building noarch.

* Mon Apr 07 2008 John W. Linville <linville@redhat.com> 2.6.24.4-77
- iwlwifi: fix n-band association problem
- ipw2200: set MAC address on radiotap interface
- libertas: fix mode initialization problem
- nl80211: fix STA AID bug
- b43legacy: fix bcm4303 crash

* Mon Apr 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-76
- Fix Palm Treo/Visor devices not being recognized as serial ports. (#436950)
- Fix ti_usb_3410_5052 serial driver. (#439134)

* Mon Apr 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-75
- Enable the 1-wire drivers (except for the Matrox driver which conflicts
  with the Matrox framebuffer driver.) (#441047)

* Mon Apr 07 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.74
- Enable CONFIG_EEPRO100.

* Wed Apr 02 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-74
- Disable sound card debugging messages. (#439592)

* Wed Apr 02 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-73
- Fix locking in tun/tap driver when device address changes (#439715)

* Wed Apr 02 2008 Eric Sandeen <sandeen@redhat.com> 2.6.24.4-72
- Fix mis-read of xfs attr2 superblock flag which was causing
  corruption in some cases. (#437968)

* Wed Apr 02 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-71
- Disable the VIA Padlock SHA crypto hardware driver
  because it prevents module loading. (#438322)

* Wed Apr 02 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-70
- PNP: disable PNP motherboard resources that overlap PCI BARs (#439978)

* Tue Apr 01 2008 John W. Linville <linville@redhat.com> 2.6.24.4-69
- mac80211: trigger ieee80211_sta_work after opening interface
- b43: Add DMA mapping failure messages
- b43: Fix PCMCIA IRQ routing
- mac80211: correct use_short_preamble handling
- endianness annotations: drivers/net/wireless/rtl8180_dev.c
- net/mac80211/debugfs_netdev.c: use of bool triggers a gcc bug
- libertas: convert CMD_802_11_MAC_ADDRESS to a direct command
- libertas: convert CMD_802_11_EEPROM_ACCESS to a direct command
- libertas: convert sleep/wake config direct commands
- libertas: don't depend on IEEE80211
- rt2x00: Invert scheduled packet_filter check
- rt2x00: TO_DS filter depends on intf_ap_count
- rt2x00: Remove MAC80211_LEDS dependency
- mac80211 ibss: flush only stations belonging to current interface
- mac80211: fix sta_info_destroy(NULL)
- mac80211: automatically free sta struct when insertion fails
- mac80211: clean up sta_info_destroy() users wrt. RCU/locking
- mac80211: sta_info_flush() fixes
- mac80211: fix sparse complaint in ieee80211_sta_def_wmm_params
- rt2x00: fixup some non-functional merge errors
- wireless: fix various printk warnings on ia64 (and others)
- mac80211: fix deadlocks in debugfs_netdev.c
- mac80211: fix spinlock recursion on sta expiration
- mac80211: use recent multicast table for all mesh multicast frames
- mac80211: check for mesh_config length on incoming management frames
- mac80211: use a struct for bss->mesh_config
- iwlwifi: add notification infrastructure to iwlcore
- iwlwifi: hook iwlwifi with Linux rfkill
- iwlwifi: fix race condition during driver unload
- iwlwifi: move rate registration to module load
- iwlwifi: unregister to upper stack before releasing resources
- iwlwifi: LED initialize before registering
- iwlwifi: Fix synchronous host command

* Tue Apr 01 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-68
- libata: clear simplex DMA before probing pata_atiixp devices (#437163)

* Tue Apr 01 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-67
- Fix ISAPnP device resource limits so they match the spec.
- Extend the PnP memory resource limit to 24.

* Mon Mar 31 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.4-66
- Patch up paranoid iret cs reg corruption crasher on x86_64 (#431314)

* Sat Mar 29 2008 Dave Jones <davej@redhat.com> 2.6.24.4-65
- powerpc: Fix missed hardware breakpoints across multiple threads. (#439619)

* Fri Mar 28 2008 John W. Linville <linville@redhat.com> 2.6.24.4-64
- libertas: fix spinlock recursion bug
- rt2x00: Ignore set_state(STATE_SLEEP) failure
- iwlwifi: allow a default callback for ASYNC host commands
- libertas: kill useless #define LBS_MONITOR_OFF 0
- libertas: remove CMD_802_11_PWR_CFG
- libertas: the compact flash driver is no longer experimental
- libertas: reduce debug output
- mac80211: reorder fields to make some structures smaller
- iwlwifi: Add led support
- mac80211: fix wrong Rx A-MPDU control via debugfs
- mac80211: A-MPDU MLME use dynamic allocation
- iwlwifi: rename iwl-4965-io.h to iwl-io.h
- iwlwifi: improve NIC i/o debug prints information
- iwlwifi: iwl_priv - clean up in types of members

* Thu Mar 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-63
- Disable PID namespaces, hopefully fixing bug #438414

* Thu Mar 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-62
- Fix broken PCI resource allocation.

* Thu Mar 27 2008 Dave Jones <davej@redhat.com>
- Backport lots of MTRR fixes from 2.6.25.
  Amongst others, this fixes bz 438960

* Thu Mar 27 2008 Dave Jones <davej@redhat.com>
- Enable USB debug in debug kernels.

* Thu Mar 27 2008 John W. Linville <linville@redhat.com> 2.6.24.4-59
- cfg80211: don't export ieee80211_get_channel

* Wed Mar 26 2008 John W. Linville <linville@redhat.com> 2.6.24.4-58
- ipw2200 annotations and fixes
- iwlwifi: Re-ordering probe flow (4965)
- iwlwifi: Packing all 4965 parameters
- iwlwifi: Probe Flow - Performing allocation in a separate function
- iwlwifi: Probe Flow - Extracting hw and priv init
- iwlwifi: rename iwl4965_get_channel_info to iwl_get_channel_info
- iwlwifi: Completing the parameter packaging
- iwlwifi-2.6: Cleans up set_key flow
- iwlwifi-2.6: RX status translation to old scheme
- mac80211: get a TKIP phase key from skb
- mac80211: allows driver to request a Phase 1 RX key
- iwlwifi-2.6: enables HW TKIP encryption
- iwlwifi-2.6: enables RX TKIP decryption in HW
- libertas: convert CMD_MAC_CONTROL to a direct command
- libertas: rename packetfilter to mac_control
- libertas: remove some unused commands
- libertas: make a handy lbs_cmd_async() command
- libertas: fix scheduling while atomic bug in CMD_MAC_CONTROL
- libertas: convert GET_LOG to a direct command
- libertas: misc power saving adjusts
- libertas: remove lots of unused stuff
- libertas: store rssi as an u32
- rt2x00: Add dev_flags to rx descriptor
- rt2x00: Fix rate detection for invalid signals
- rt2x00: Fix in_atomic() usage
- wireless: add wiphy channel freq to channel struct lookup helper
- mac80211: use ieee80211_get_channel
- mac80211: filter scan results on unusable channels
- PS3: gelic: Add support for separate cipher selection
- iwlwifi: Bug fix, CCMP with HW encryption with AGG
- b43: Don't compile N-PHY code when N-PHY is disabled
- mac80211: prevent tuning during scanning
- iwlwifi: remove macros containing offsets from eeprom struct
- mac80211: fixing delba debug print
- mac80211: fixing debug prints for AddBA request
- mac80211: tear down of block ack sessions
- iwlwifi: rename iwl-4965-debug.h back to iwl-debug.h
- iwlwifi: rename struct iwl4965_priv to struct iwl_priv
- iwlwifi: Add TX/RX statistcs to driver
- iwlwifi: Add debugfs to iwl core
- iwlwifi: iwl3945 remove 4965 commands
- iwlwifi: move host command sending functions to core module
- mac80211: configure default wmm params correctly
- mac80211: silently accept deletion of non-existant key
- prism54: correct thinko in "prism54: Convert stats_sem in a mutex"

* Wed Mar 26 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-57
- Fix spurious thermal trips on Compaq notebook. (#437466)

* Wed Mar 26 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.56
- Deblob linux tarball.
- Deblob patch-2.6.24.4.bz2.
- Disable linux-2.6-drm-radeon-update.patch, patches removed files.
- Disable linux-2.6-git-initial-r500-drm.patch, patches removed files.
- Disable CONFIG_TIGON3.
- Disable CONFIG_VIDEO_CX23885.
- Disable CONFIG_DVB_TUNER_MT2131.
- Enable CONFIG_DVB_TDA10023.
- Enable CONFIG_DVB_TUA6100.

* Tue Mar 25 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.4-56
- Plug DMA memory leak in firewire async receive handler

* Tue Mar 25 2008 John W. Linville <linville@redhat.com> 2.6.24.4-55
- wavelan_cs arm fix
- arlan: fix warning when PROC_FS=n
- rt2x00: Add id for Corega CG-WLUSB2GPX
- b43: Fix DMA mapping leakage
- b43: Remove irqs_disabled() sanity checks
- iwlwifi: fix a typo in Kconfig message
- MAINTAINERS: update iwlwifi git url
- iwlwifi: fix __devexit_p points to __devexit functions
- iwlwifi: mac start synchronization issue

* Mon Mar 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.4-53
- Linux 2.6.24.4

* Mon Mar 24 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.3-52
- firewire: make sure phy config packets get sent before initiating bus
  reset. Fixes bugzilla.kernel.org #10128.

* Fri Mar 21 2008 Dave Jones <davej@redhat.com> 2.6.24.3-51
- Enable PIIX4 I2C driver on x86-64.

* Thu Mar 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-50
- Reduce the severity of the PnP resource overflow message.

* Thu Mar 20 2008 John W. Linville <linville@redhat.com> 2.6.24.3-49
- Prevent iwlwifi drivers from registering bands with no channels (#438273)

* Wed Mar 19 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-48
- Revert the ACPI sizeof patch that fixes BZ 437466 because it breaks acpi-cpufreq.

* Wed Mar 19 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-47
- Add support for newer Apple keyboards (#426576)

* Wed Mar 19 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-46
- Stop endless stream of ACPI interrupt messages (#251744)

* Wed Mar 19 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-45
- Add Penryn CPU support to the hwmon coretemp driver (#438073)

* Tue Mar 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-44
- Fix spurious ACPI thermal trips (#437466)

* Tue Mar 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-43
- Revert the ACPI multiple-busses patch that causes problems for some people.
- Set CONFIG_SYSFS_DEPRECATED, fixing network device naming bugs. (#436583)

* Tue Mar 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-42
- Fix broken it821x adapter drive detection (#434864)

* Tue Mar 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-41
- Make the i686 kernel work on compatible processors, take 2. (#435609)

* Mon Mar 17 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.3-40
- Actually add the coherent DMA fix that was supposed to be added in -37.

* Mon Mar 17 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.3-39
- firewire: fix panic in handle_at_packet (bz.kernel.org #9617)
- firewire-sbp2: add workaround for busted LSI/Symbios bridges (#436879)

* Fri Mar 14 2008 John W. Linville <linville@redhat.com> 2.6.24.3-38
- b43: phy.c fix typo in register write
- prism54: support for 124a:4025 - another version of IOGear GWU513 802.11g
- PS3: gelic: change the prefix of the net interface for wireless
- ath5k: disable irq handling in ath5k_hw_detach()
- revert "tkip: remove unused function, other cleanups"
- revert "mac80211: remove Hi16, Lo16 helpers"
- revert "mac80211: remove Hi8/Lo8 helpers, add initialization vector helpers"

* Fri Mar 14 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.3-37
- Resync firewire patches w/linux1394-2.6.git
- Add firewire selfID/AT/AR debug support via optional
  module parameters
- firewire: fix DMA coherence on x86_64 systems w/memory mapped
  over the 4GB boundry (#434830)

* Thu Mar 13 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-36
- Add support for another Dell wireless modem (#437396)

* Wed Mar 12 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-35
- Revert 2.6.24 ACPI change that broke some notebooks. (#432477)

* Tue Mar 11 2008 John W. Linville <linville@redhat.com> 2.6.24.3-34
- rt2x00:correct rx packet length for USB devices
- make b43_mac_{enable,suspend}() static
- the scheduled bcm43xx removal
- the scheduled ieee80211 softmac removal
- the scheduled rc80211-simple.c removal
- iwlwifi: Use eeprom form iwlcore
- tkip: remove unused function, other cleanups
- mac80211: remove Hi16, Lo16 helpers
- mac80211: remove Hi8/Lo8 helpers, add initialization vector helpers
- b43: pull out helpers for writing noise table
- libertas: implement SSID scanning for SIOCSIWSCAN
- rt2x00: Align RX descriptor to 4 bytes
- rt2x00: Don't use uninitialized desc_len
- rt2x00: Use skbdesc fields for descriptor initialization
- rt2x00: Only disable beaconing just before beacon update
- rt2x00: Upgrade queue->lock to use irqsave
- rt2x00: Move firmware checksumming to driver
- rt2x00: Start bugging when rt2x00lib doesn't filter SW diversity
- rt2x00: Check IEEE80211_TXCTL_SEND_AFTER_DTIM flag
- rt2x00: Rename config_preamble() to config_erp()
- rt2x00: Add suspend/resume handlers to rt2x00rfkill
- rt2x00: Make rt2x00leds_register return void
- rt2x00: Always enable TSF ticking
- rt2x00: Fix basic rate initialization
- rt2x00: Fix compile error when rfkill is disabled
- rt2x00: Fix RX DMA ring initialization
- rt2x00: Fix rt2400pci signal
- rt2x00: Release rt2x00 2.1.4
- rt2x00: Only strip preamble bit in rt2400pci
- prism54: support for 124a:4025 - another version of IOGear GWU513 802.11g
- drivers/net/wireless/ath5k - convert == (true|false) to simple logical tests
- include/net/ieee80211.h - remove duplicate include
- rndis_wlan: cleanup, rename and reorder enums and structures
- rndis_wlan: cleanup, rename structure members
- rt2x00: Fix trivial log message
- PS3: gelic: ignore scan info from zero SSID beacons
- rt2x00: Initialize TX control field in data entries
- rt2x00: Use the correct size when copying the control info in txdone
- rt2x00: Don't use unitialized rxdesc->size
- ssb: Add SPROM/invariants support for PCMCIA devices
- iwlwifi: update copyright year
- iwlwifi: fix bug to show hidden APs during scan
- iwlwifi: Use sta_bcast_id variable instead of BROADCAST_ID constant
- iwlwifi: Fix endianity in debug print
- iwlwifi: change rate number to a constant

* Tue Mar 11 2008 John W. Linville <linville@redhat.com> 2.6.24.3-33
- rt2x00: never disable multicast because it disables broadcast too
- rt2x00: Add new D-Link USB ID
- drivers/net/Kconfig: fix whitespace for GELIC_WIRELESS entry
- libertas: fix the 'compare command with itself' properly

* Tue Mar 11 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-32
- Add missing debug patch.
- Remove the fix for stray GPEs because it breaks ACPI (#436959)

* Tue Mar 11 2008 Dave Jones <davej@redhat.com> 2.6.24.3-31
- Print values when we overflow resource allocation.

* Mon Mar 10 2008 John W. Linville <linville@redhat.com> 2.6.24.3-30
- Learn to read preceding changelogs...

* Mon Mar 10 2008 John W. Linville <linville@redhat.com> 2.6.24.3-29
- Use correct "Dual BSD/GPL" license tag for iwlcore.ko

* Mon Mar 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-28
- Fix license in iwlcore driver.

* Mon Mar 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-27
- usb-serial: fix deadlock (#431379)

* Mon Mar 10 2008 John W. Linville <linville@redhat.com> 2.6.24.3-26
- iwlwifi: Moving EEPROM handling in iwlcore module
- ath5k: struct ath5k_desc cleanups
- ath5k: move rx and tx status structures out of hardware descriptor
- ath5k: add notes about rx timestamp
- ath5k: work around wrong beacon rx timestamp in IBSS mode
- libertas: convert KEY_MATERIAL to a direct command
- libertas: add LED control TLV to types.h
- libertas: convert 802_11_SCAN to a direct command
- libertas: clean up scan.c, remove zeromac and bcastmac
- iwlwifi: Cancel scanning upon association
- iwlwifi: 802.11n spec removes AUTO offset for FAT channel
- WEXT: add mesh interface type
- mac80211: add mesh interface type
- mac80211: clean up mesh code
- mac80211: mesh hwmp locking fixes
- mac80211: enable mesh in Kconfig
- mac80211: add missing "break" statement in mesh code
- mac80211: clarify mesh Kconfig
- mac80211: export mesh_plink_broken
- mac80211: clean up mesh RX path a bit more
- mac80211: fix kernel-doc comment for mesh_plink_deactivate
- mac80211: reorder a few fields in sta_info
- mac80211: split ieee80211_txrx_data
- mac80211: RCU-ify STA info structure access
- mac80211: split sta_info_add
- mac80211: clean up sta_info and document locking
- mac80211: remove STA entries when taking down interface
- mac80211: don't clear next_hop in path reclaim
- mac80211: add documentation book
- mac80211: fix sta_info mesh timer bug
- b43: verify sta_notify mac80211 callback
- mac80211: always insert key into list
- mac80211: fix hardware scan completion
- mac80211: don't call conf_tx under RCU lock
- wireless: correct warnings from using '%llx' for type 'u64'
- wireless: various definitions for mesh networking
- nl80211/cfg80211: support for mesh, sta dumping
- mac80211: mesh function and data structures definitions
- mac80211: support functions for mesh
- mac80211: support for mesh interfaces in mac80211 data path
- mac80211: mesh data structures and first mesh changes
- mac80211: mesh changes to the MLME
- mac80211: mesh peer link implementation
- mac80211: mesh path table implementation
- mac80211: code for on-demand Hybrid Wireless Mesh Protocol
- mac80211: mesh statistics and config through debugfs
- mac80211: mesh path and mesh peer configuration
- mac80211: complete the mesh (interface handling) code
- mac80211: fix mesh endianness sparse warnings and unmark it as broken
- mac80211: fix incorrect parenthesis
- mac80211: move comment to better location
- mac80211: breakdown mesh network attributes in different extra fields for wext
- mac80211: clean up use of endianness conversion functions
- mac80211: delete mesh_path timer on mesh_path removal
- mac80211: always force mesh_path deletions
- mac80211: add PLINK_ prefix and kernel doc to enum plink_state
- mac80211: path IE fields macros, fix alignment problems and clean up
- mac80211: fix mesh_path and sta_info get_by_idx functions
- zd1211rw: support for mesh interface and beaconing
- ssb: Add Gigabit Ethernet driver
- b43: Add QOS support
- b43: Rename the DMA ring pointers
- b43: Add TX statistics debugging counters
- b43: Fix failed frames status report typo
- ath5k: Add RF2413 srev values
- ath5k: Add RF2413 initial settings
- ath5k: Identify RF2413 and deal with PHY_SPENDING
- ath5k: more RF2413 stuff
- ath5k: Remove RF5413 from rf gain optimization functions
- ath5k: Fixes for PCI-E cards
- ath5k: Make some changes to follow register dumps.
- ath5k: Add 2413 to srev_names so that it shows up during module load
- iwlwifi: fix potential lock inversion deadlock
- mac80211: adding mac80211_tx_control_flags and HT flags
- iwlwifi: use mac80211_tx_control_flags
- mac80211: document IEEE80211_TXCTL_OFDM_HT
- iwlwifi: grab NIC access when disabling aggregations
- iwlwifi: removing unused priv->config
- iwlwifi: refactor init geos function
- iwlwifi: Fix 52 rate report in rx status
- iwlwifi: extract iwl-csr.h
- iwlwifi: Move HBUS address to iwl-csr.h
- iwlwifi: add struct iwl_cfg
- iwlwifi: Take the fw file name from the iwl_cfg.
- iwlwifi: fix locking unbalance in 4965 rate scale
- iwlwifi: add iwl-core module
- iwlwifi: queue functions cleanup
- iwlwifi: Fix 3945 rate scaling
- iwlwifi: 3945 split tx_complete to command and packet function

* Mon Mar 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-25
- Use better fix for clearing the direction flag for x86 signal handlers.

* Mon Mar 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-24
- ACPI: really disable stray GPEs (#251744)
- x86: clear direction flag before calling signal handlers

* Mon Mar 10 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.3-23
- firewire-sbp2: improved ability to reconnect to devices
  following a bus reset
- firewire-sbp2: set proper I/O retry limits in SBP-2 devices

* Thu Mar 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-22
- Fix libata DMA masking for HPT and SVW (possible fix for #433557)

* Thu Mar 06 2008 John W. Linville <linville@redhat.com> 2.6.24.3-21
- correct patch name typo in spec file

* Thu Mar 06 2008 John W. Linville <linville@redhat.com> 2.6.24.3-20
- add ps3_gelic_wirless driver

* Thu Mar 06 2008 Dave Airlie <airlied@redhat.com> 2.6.24.3-19
- fixup agp/drm patches for F8 kernel

* Tue Mar 04 2008 John W. Linville <linville@redhat.com> 2.6.24.3-18
- libertas: fix sanity check on sequence number in command response
- p54: fix EEPROM structure endianness
- p54: fix eeprom parser length sanity checks
- rndis_wlan: fix broken data copy
- b43legacy: Fix module init message
- libertas: compare the current command with response
- rc80211-pid: fix rate adjustment
- ssb: Add pcibios_enable_device() return value check
- mac80211: always insert key into list (temporary backport)
- mac80211: fix hardware scan completion (temporary backport)

* Mon Mar 03 2008 Jarod Wilson <jwilson@redhat.com> 2.6.24.3-17
- firewire-sbp2: permit drives to suspend (#243210)
- firewire: fix suspend/resume on older PowerPC Macs (#312871)
- firewire: restore bus power on resume on older PowerPC Macs
- firewire: support for first-gen Apple UniNorth controller
- firewire: fix crashes in workqueue jobs

* Mon Mar 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-16
- ISDN: don't oops on driver load (#362621)

* Mon Mar 03 2008 John W. Linville <linville@redhat.com> 2.6.24.3-15
- ssb: Add CHIPCO IRQ access functions
- p54: print unknown eeprom fields
- rt2x00: Check for 5GHz band in link tuner
- rt2x00: Release rt2x00 2.1.3
- mac80211: rework TX filtered frame code
- mac80211: atomically check whether STA exists already
- mac80211: Disallow concurrent IBSS/STA mode interfaces
- mac80211: fix debugfs_sta print_mac() warning
- mac80211: fix IBSS code
- adm8211: fix cfg80211 band API conversion
- mac80211: clarify use of TX status/RX callbacks
- mac80211: safely free beacon in ieee80211_if_reinit
- mac80211: remove STA infos last_ack stuff
- mac80211: split ieee80211_key_alloc/free
- mac80211: fix key replacing, hw accel
- b43legacy: Fix nondebug build
- ath5k: fix all endian issues reported by sparse

* Mon Mar 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-14
- Allow i686 kernel to boot on Via C3/C7 processors (#435609)

* Wed Feb 27 2008 John W. Linville <linville@redhat.com> 2.6.24.3-13
- rt2x00: correct address calc for queue private data
- mac80211: better definition of mactime
- mac80211: move function ieee80211_sta_join_ibss()
- mac80211: enable IBSS merging
- p54: use IEEE 802.11e defaults for initialization
- ipw2100/ipw2200: note firmware loading caveat in Kconfig help text
- iwlwifi-2.6: Adds and fixes defines about security
- rt2x00: Fix hw mode registration with mac80211.
- rt2x00: Fix invalid DMA free
- rt2x00: Make rt2x00 less verbose
- rt2x00: Remove MGMT ring initialization
- rt2x00: Select CONFIG_NEW_LEDS
- rt2x00: make csr_cache and csr_addr an union
- rt2x00: Fix scheduling while atomic errors in usb drivers
- rt2x00: Add queue statistics to debugfs
- rt2x00: Fix typo in debug statement
- rt2x00: Fix skbdesc->data_len initialization
- rt2x00: Fix queue->qid initialization
- rt2x00: Cleanup Makefile
- rt2x00: Kill guardian urb during disable_radio
- rt2x00: Release rt2x00 2.1.1
- rt2x00: Send frames out with configured TX power
- rt2x00: Don't report driver generated frames to tx_status()
- rt2x00: Filter ACK_CTS based on FIF_CONTROL
- rt2x00: Fix Descriptor DMA initialization
- rt2x00: Remove reset_tsf()
- rt2x00: Rename dscape -> mac80211
- rt2x00: Cleanup mode registration
- rt2x00: Remove async vendor request calls from rt2x00usb
- rt2x00: Fix MAC address defines in rt61pci
- rt2x00: Release rt2x00 2.1.2
- zd1211rw: Fixed incorrect constant name.
- WDEV: ath5k, typecheck on nonDEBUG
- mac80211: defer master netdev allocation to ieee80211_register_hw
- mac80211: give burst time in txop rather than 0.1msec units
- mac80211: fix ecw2cw brain-damage
- rtl818x: fix RTS/CTS-less transmit
- b43(legacy): include full timestamp in beacon frames
- mac80211: convert sta_info.pspoll to a flag
- mac80211: invoke set_tim() callback after setting own TIM info
- mac80211: remove sta TIM flag, fix expiry TIM handling
- mac80211: consolidate TIM handling code
- adm8211: fix sparse warnings
- p54: fix sparse warnings
- ipw2200: le*_add_cpu conversion
- prism54: Convert acl->sem in a mutex
- prism54: Convert stats_sem in a mutex
- prism54: Convert wpa_sem in a mutex
- b43: Fix bandswitch
- mac80211: Extend filter flag documentation about unsupported flags
- b43: Add HostFlags HI support
- zd1211rw: Fix beacon filter flags thinko
- ssb: Add support for 8bit register access
- mac80211: fix incorrect use of CONFIG_MAC80211_IBSS_DEBUG
- wireless: rt2x00: fix driver menu indenting
- iwlwifi: Update iwlwifi version stamp to 1.2.26
- iwlwifi: fix name of function in comment (_rx_card_state_notif)
- wireless: Convert to list_for_each_entry_rcu()
- mac80211: adjustable number of bits for qdisc pool
- iwlwifi: remove IWL{4965,3945}_QOS
- net/mac80211/: Use time_* macros
- drivers/net/wireless/atmel.c: Use time_* macros
- b43legacy: add definitions for MAC control register
- b43legacy: fix upload of beacon packets to the hardware
- b43legacy: fix B43legacy_WARN_ON macro
- iwlwifi: change iwl->priv iwl_priv * type in iwl-YYY-io.h
- iwlwifi: Add tx_ant_num hw setting variable
- iwlwifi: remove twice defined CSR register
- wireless: update US regulatory domain
- Use a separate config option for the b43 pci to ssb bridge.
- Don't build bcm43xx if SSB is static and b43 PCI-SSB bridge is enabled.
- Fix b43 driver build for arm
- rt2x00: Fix antenna diversity
- rt2x00: Add link tuner safe RX toggle states
- rt2x00: Don't switch to antenna with low rssi
- rt2x00: Fix rt2x00lib_reset_link_tuner()
- rndis_wlan: fix sparse warnings
- mac80211: fix kmalloc vs. net_ratelimit
- libertas: Remove unused exports
- at76_usb: Add at76_dbg_dump() macro
- at76_usb: Convert DBG_TX levels to use at76_dbg_dump()
- at76_usb: Add DBG_CMD for debugging firmware commands
- at76_usb: add mac80211 support
- at76_usb: Add support for monitor mode
- at76_usb: Add support for WEP
- at76_usb: Remove support the legacy stack
- at76_usb: Use wiphy_name everywhere where needed
- at76_usb: Allocate struct at76_priv using ieee80211_alloc_hw()
- at76_usb: Prepare for struct net_device removal
- at76_usb: Remove struct net_device
- at76_usb: Use net/mac80211.h instead of net/ieee80211.h
- at76_usb: fix missing newlines in printk, improve some messages
- at76_usb: remove unneeded code
- at76_usb: add more MODULE_AUTHOR entries
- at76_usb: reindent, reorder initializers for readability
- at76_usb: make the driver depend on MAC80211

* Tue Feb 26 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.3-12
- Linux 2.6.24.3

* Fri Feb 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.2-11
- Restore RTC drivers on powerpc kernels (#433262)

* Thu Feb 21 2008 John W. Linville <linville@redhat.com> 2.6.24.2-10
- wireless: Fix WARN_ON() with ieee802.11b
- rt2x00: Rate structure overhaul
- rt2x00: Remove HWMODE_{A,B,G}
- rt2x00: Use ieee80211_channel_to_frequency()
- rt2x00: Make use of MAC80211_LED_TRIGGERS
- rt2x00: Enable LED class support for rt2500usb/rt73usb
- rt2x00: Fix rate initialization
- rt2x00: Release rt2x00 2.1.0
- cfg80211 API for channels/bitrates, mac80211 and driver conversion
- nl80211: export hardware bitrate/channel capabilities
- mac80211: fix scan band off-by-one error
- mac80211: remove port control enable switch, clean up sta flags
- wireless: fix ERP rate flags
- mac80211: split ieee80211_txrx_result
- mac80211: split RX_DROP
- mac80211: clean up some things in the RX path
- mac80211: remove "dynamic" RX/TX handlers
- mac80211: move some code into ieee80211_invoke_rx_handlers
- ath5k: Port to new bitrate/channel API
- ath5k: Cleanup after API changes
- ath5k: ath5k_copy_channels() was not setting the channel band
- ath5k: Use our own Kconfig file, we'll be expanding this shortly
- ath5k: Port debug.c over to the new band API and enable as build option
- ath5k: Use software encryption for now
- ath5k/phy.c: fix negative array index
- nl80211: Add monitor interface configuration flags
- mac80211: Use monitor configuration flags
- mac80211: Add cooked monitor mode support
- iwlwifi: initialize ieee80211_channel->hw_value
- iwlwifi: set rate_idx correctly from plcp
- rc80211-pid: fix rate adjustment
- iwlwifi: Fix HT compilation breakage caused by cfg80211 API for channels/bitrates patch
- ath5k: Fix build warnings on some 64-bit platforms.
- p54usb: add USB ID for Phillips CPWUA054
- WDEV: ath5k, fix lock imbalance
- WDEV, ath5k, don't return int from bool function
- rtl818x: fix sparse warnings
- zd1211rw: fix sparse warnings
- p54usb: add USB ID for Linksys WUSB54G ver 2
- ssb: Fix serial console on new bcm47xx devices
- ssb: Fix watchdog access for devices without a chipcommon
- ssb: Fix the GPIO API
- ssb: Make the GPIO API reentrancy safe
- ssb: Fix pcicore cardbus mode
- ssb: Fix support for PCI devices behind a SSB->PCI bridge

* Mon Feb 18 2008 John W. Linville <linville@redhat.com> 2.6.24.2-7
- ath5k: correct padding in tx descriptors
- ipw2200: fix ucode assertion for RX queue overrun
- iwlwifi: Don't send host commands on rfkill
- rt2x00: Add new USB ID to rt2500usb
- wavelan: mark hardware interfacing structures as packed
- rndis_wlan: enable stall workaround by link quality instead of link speed
- b43: Add driver load messages
- b43: Add firmware information to modinfo
- b43: Fix firmware load message level
- mac80211: Fix initial hardware configuration
- iwlwifi: earlier rx allocation
- iwlwifi: do not clear GEO_CONFIGURED bit when calling _down
- iwlwifi: only check for association id when associating with AP
- b43legacy: fix DMA for 30/32-bit DMA engines
- b43legacy: add firmware information to modinfo
- b43legacy: fix firmware load message level
- b43legacy: Add driver load messages
- iwlwifi: reverting 'misc wireless annotations' patch for iwlwifi

* Mon Feb 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.2-6
- Bump version.

* Sun Feb 17 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.2-5
- Disable ACPI power information in sysfs (ACPI_SYSFS_POWER).
- Kill annoying audio driver messages.

* Fri Feb 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.2-4
- Bump version.

* Fri Feb 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.2-3
- Restore missing ppc32 patch.
- Add USB video camera (UVC) drivers.

* Fri Feb 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.24.2-2
- Linux 2.6.24.2

* Sun Feb 10 2008 Dave Airlie <airlied@redhat.com> 2.6.23.15-137
- CVE-2008-0600 - local root vulnerability in vmsplice

* Fri Feb 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.15-136
- Linux 2.6.23.15
- Fix Megahertz PCMCIA Ethernet adapter (#233255)

* Wed Feb 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-135
- Bump version.

* Wed Feb 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-134
- Use the upstream fix for futex locking.
- Fix oops in netfilter (#430663)

* Tue Feb 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-133
- ACPI: fix early init of EC (#426480)

* Tue Feb 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-132
- ACPI: fix multiple problems with brightness controls (#427518)

* Tue Feb 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-131
- ALSA: fix audio on some systems with STAC codec (#431360)

* Tue Feb 05 2008 Jarod Wilson <jwilson@redhat.com> 2.6.23.14-130
- Pull in additional firewire fixes from upstream. Should resolve
  most 'giving up on config rom' problems (#429598).

* Tue Feb 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-129
- ASUS Eeepc hotkey ACPI driver.

* Thu Jan 31 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-128
- Strip extra leading slashes from path names in selinux.

* Thu Jan 31 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-127
- Added Atheros L2 fast Ethernet driver (atl2).

* Wed Jan 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-125
- Fix segfaults from using vdso=2 (#427641)

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-124
- Unset CONFIG_USB_DEVICE_CLASS (#362221)

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-123
- Fix the initio driver broken in 2.6.23. (#390531)

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-122
- Drop obsolete ptrace patch.

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-121
- Add support for new Macbook touchpads (#426574)

* Wed Jan 23 2008 John W. Linville <linville@redhat.com> 2.6.23.14-120
- bump release to get around Koji wierdness

* Wed Jan 23 2008 John W. Linville <linville@redhat.com> 2.6.23.14-119
- Latest wireless updates from upstream
- Remove obsolete ath5k and rtl8180 patches
- Add rndis_wext driver

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-118
- Fix futex oops on uniprocessor machine. (#429412)

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-117
- Work around broken Seagate LBA48 disks (#429364)

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-116
- Fix memory leak in netlabel code (F7#352281)

* Mon Jan 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-115
- No change, just increment release.

* Sat Jan 19 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.14-114
- Revert CONFIG_PHYSICAL_START on x86_64.

* Fri Jan 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-113
- Fix loss of mouse sync on some systems (#427697)
- Revert "libata: allow short SCSI commands for ATAPI devices" (F7#429353)

* Thu Jan 17 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-112
- Set x86 CONFIG_PHYSICAL_START=0x400000

* Thu Jan 17 2008 John W. Linville <linville@redhat.com> 2.6.23.14-111
- More wireless fixes headed for 2.6.24
- More wireless updates headed for 2.6.25

* Thu Jan 17 2008 Dave Airlie <airlied@redhat.com> 2.6.23.14-108
- update r500 patch to not have duplicate pciids.

* Mon Jan 14 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.14-107
- Linux 2.6.23.14

* Fri Jan 11 2008 Jarod Wilson <jwilson@redhat.com> 2.6.23.13-106
- FireWire update, should enable iidc reception on all controllers
- Update lirc bits to latest upstream

* Thu Jan 10 2008 John W. Linville <linville@redhat.com> 2.6.23.13-105
- rt2500usb thinko fix
- b43 N phy pre-support updates
- ath5k cleanups and beacon fixes

* Wed Jan 09 2008 John W. Linville <linville@redhat.com> 2.6.23.13-104
- More wireless fixes for 2.6.24
- More wireless update for 2.6.25
- Enable CONFIG_NL80211

* Wed Jan 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.13-103
- Linux 2.6.23.13

* Tue Jan 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.12-102
- Restore /proc/slabinfo (#396041)

* Fri Jan 04 2008 John W. Linville <linville@redhat.com> 2.6.23.12-101
- Another round of wireless fixes headed for 2.6.24
- Another round of wireless updates headed for 2.6.25

* Fri Dec 21 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.12-100
- USB: Use upstream version of the Huawei USB modem fix.

* Wed Dec 19 2007 John W. Linville <linville@redhat.com> 2.6.23.12-99
- Some wireless fixes headed for 2.6.24
- Some wireless updates headed for 2.6.25

* Tue Dec 18 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.12-98
- Linux 2.6.23.12
- Add fixed version of APM emulation patch removed in 2.6.23.10

* Sat Dec 15 2007 David Woodhouse <dwmw2@redhat.com> 2.6.23.10-97
- Fix IPv6 checksums for pasemi-mac

* Fri Dec 14 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.10-96
- Linux 2.6.23.10

* Fri Dec 14 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-95
- Update utrace to latest.

* Fri Dec 14 2007 David Woodhouse <dwmw2@redhat.com> 2.6.23.9-94
- Re-enable and fix pasemi-mac (and gpio-mdio)

* Fri Dec 14 2007 David Woodhouse <dwmw2@redhat.com> 2.6.23.9-91
- PA Semi platform fixes
- Fix OProfile on non-Cell ppc64

* Wed Dec 12 2007 Dave Airlie <airlied@redhat.com> 2.6.23.9-90
- fixup radeon r500 patch to apply to proper function

* Wed Dec 12 2007 Dave Airlie <airlied@redhat.com> 2.6.23.9-89
- Add support for r500 DRM for making 2D accel go faster

* Tue Dec 11 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-88
- Enable the USB IO-Warrior driver. (#419661)
- ALSA: snd-hda-intel: don't go into polling mode. (#417141)

* Mon Dec 10 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-87
- highres-timers: update to -hrt4 (#394981); includes hang fix

* Mon Dec 10 2007 John W. Linville <linville@redhat.com> 2.6.23.9-86
- add module alias for "zd1211rw-mac80211"

* Fri Dec 07 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-84
- highres-timers: fix possible hang

* Thu Dec 06 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-82
- libata: fix AHCI controller reset (#411171)
- ACPI: don't init EC early if it has no _INI method (#334781)

* Wed Dec 05 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-81
- Fix some cpuidle bugs, should fix hangs on startup.

* Wed Dec 05 2007 John W. Linville <linville@redhat.com> 2.6.23.9-80
- Some wireless driver bits headed for 2.6.25

* Tue Dec 04 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-79
- libata: fix ATAPI tape drives (#394961)
- libata: allow short SCSI commands for ATAPI devices

* Mon Dec 03 2007 Jarod Wilson <jwilson@redhat.com> 2.6.23.9-78
- Fix FireWire OHCI 1.1 regression introduced by 1.0 support

* Sat Dec 01 2007 John W. Linville <linville@redhat.com> 2.6.23.9-77
- Some wireless bits headed for 2.6.25
- Make ath5k use software WEP

* Fri Nov 30 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-76
- ALSA: fix missing controls on some drivers (#370821)
- ACPI: send initial button state on startup (#275651)

* Fri Nov 30 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-75
- Disable e1000 link power management (#400561)

* Fri Nov 30 2007 Jarod Wilson <jwilson@redhat.com> 2.6.23.9-74
- Improved FireWire OHCI 1.0 Isochronous Receive support (#344851)

* Fri Nov 30 2007 John W. Linville <linville@redhat.com> 2.6.23.9-73
- Some more wireless bits headed for 2.6.24

* Thu Nov 29 2007 John W. Linville <linville@redhat.com> 2.6.23.9-72
- Resync wireless bits headed for 2.6.24
- Resync wireless bits headed for 2.6.25

* Wed Nov 28 2007 David Woodhouse <dwmw2@redhat.com> 2.6.23.9-71
- Add support for MPC52xx FEC (again)

* Wed Nov 28 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-70
- Fix further bugs in init of Huawei USB modem (#253096)
- Fix libata handling of IO ready test (#389971)

* Wed Nov 28 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-69
- Add support for SiS 7019 audio for K12LTSP project

* Tue Nov 27 2007 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.9-68
- Some USB disks spin themselves down automatically and need
  scsi_device.allow_restart enabled so they'll spin back up.

* Tue Nov 27 2007 John W. Linville <linville@redhat.com> 2.6.23.9-67
- Fix NULL ptr reference in iwlwifi (CVE-2007-5938)

* Tue Nov 27 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-66
- ALSA 1.0.15 20071120

* Mon Nov 26 2007 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.9-65
- Linux 2.6.23.9

* Mon Nov 26 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.8-64
- Set CONFIG_USB_DEVICE_CLASS (#397571)

* Wed Nov 21 2007 John W. Linville <linville@redhat.com> 2.6.23.8-63
- Revise b43 rev D support (new upstream patch)
- Restore ability to add/remove virtual i/fs to mac80211 devices

* Tue Nov 20 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.8-62
- Linux 2.6.23.9-rc1

* Mon Nov 19 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.8-61
- Fix oops in netfilter NAT module (#259501)

* Mon Nov 19 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.8-60
- libata: fix resume on some systems
- libata: fix pata_serverworks with some drive combinations

* Mon Nov 19 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.8-59
- Linux 2.6.23.8

* Thu Nov 15 2007 John W. Linville <linville@redhat.com> 2.6.23.1-56
- wireless fixes from 2.6.24
- wireless updates destined for 2.6.25
- ath5k driver updates
- add rtl8180 driver
- enable libertas driver
- add experimental b43 rev D support

* Thu Nov 15 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-55
- Add DMI based autoloading for the Dell dcdbas driver (#248257)

* Wed Nov 14 2007 Jarod Wilson <jwilson@redhat.com> 2.6.23.1-54
- Initial FireWire OHCI 1.0 Isochronous Receive support (#344851)

* Tue Nov 13 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-53
- Disable precise CPU time accounting, fixing a divide-by-zero bug.
- Disable transparent PCI bridge resizing.

* Tue Nov 13 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-52
- Add touchpad support for Dell Vostro 1400 and Thinkpad R61 (#375471)

* Tue Nov 13 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-51
- Fix completely broken sata_sis libata driver (#365331)

* Fri Nov  9 2007 Eric Paris <eparis@redhat.com> 2.6.23.1-50
- Fix loop iteration problem in selinux ebitmap code

* Thu Nov  8 2007 John W. Linville <linville@redhat.com> 2.6.23.1-49
- Resync wireless bits from current upstream

* Wed Nov  7 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-48
- md/raid5: fix misapplication of previous patch
- net: fix panic removing devices from teql secheduler
- net: fix oops in l2tp transmit and receive
- nfs: fix writeback race causing data corruption
- x86 setup: fix boot on 486DX4 processor

* Tue Nov  6 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-47
- update utrace

* Tue Nov  6 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-46
- ALSA updates:
   hda: revert STAC92XX volume control changes (#354981)
   hda: add STAC92XX DMIC support
   hda: disable shared stream on AD1986A
   cmipci: fix wrong definitions
- CIFS: fix corruption when server returns EAGAIN (#357001)
- ACPI: suspend/resume fixes
- drivers: restore platform driver modaliases
- x86: fix tsc clocksource calibration
- x86_64: fix global tlb flushing bug
- hidinput: add powerbook driver to x86_64 config (#358721)
- spider_net: fix hang
- mm: fix invalid ptrace access causing kernel hang
- direct-io: fix return of stale data after DIO write
- md/raid5: fix data corruption in some failure cases
- serial: add IDs for some new Wacom tablets (#352811)

* Tue Nov  6 2007 David Airlie <airlied@redhat.com> 2.6.23.1-44
- Fix bug 228414 - X hangs at startup with Radeon X800 GTO PCIe with DRI

* Sat Nov  3 2007 David Woodhouse <dwmw2@redhat.com> 2.6.23.1-43
- Apply PS3 EHCI workaround to make rebooting work when hci_usb is loaded

* Tue Oct 30 2007 Dave Jones <davej@redhat.com> 2.6.23.1-42
- Disable PCI MMCONFIG by default. (Boot with pci=msi if you need it)
  Works around bz 329241 amongst others.

* Mon Oct 29 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-41
- Enable hamradio drivers.

* Mon Oct 29 2007 David Woodhouse <dwmw2@redhat.com> 2.6.23.1-40
- Disable PS3 wireless for now (#343901)

* Mon Oct 29 2007 Dave Jones <davej@redhat.com> 2.6.23.1-39
- Revert: x86_64: allocate sparsemem memmap above 4G (fixes bz #249174)
  (Much thanks to Martin Ebourne for tracking this down).

* Mon Oct 29 2007 Dave Jones <davej@redhat.com> 2.6.23.1-38
- Change CRYPTO_CRC32 to built-in for F8 (bz #208607)

* Fri Oct 26 2007 John W. Linville <linville@redhat.com> 2.6.23.1-37
- iwlwifi: clear irqs before enabling them

* Thu Oct 25 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-36
- ALSA 1.0.15
- Fix ppc64 DSO unwinder bug (#350291).
- Fix linker script bug preventing Fedora bringup on IA64.
- Kill not-always-relevant debug message in ath5k driver.

* Wed Oct 24 2007 Dave Jones <davej@redhat.com> 2.6.23.1-35
- Disable early boot debugging for release.

* Wed Oct 24 2007 Roland McGrath <roland@redhat.com> 2.6.23.1-34
- Install System.map in kernel-devel packages.

* Tue Oct 23 2007 John W. Linville <linville@redhat.com> 2.6.23.1-33
- quiet mac80211 decryption noise when frames may not be for us

* Tue Oct 23 2007 Eric Paris <eparis@redhat.com> 2.6.23.1-32
- check sigchld when waiting on a task (gdb/selinux interaction)

* Tue Oct 23 2007 John W. Linville <linville@redhat.com> 2.6.23.1-31
- remove problematic hunk from ath5k fixes patch

* Mon Oct 22 2007 Adam Jackson <ajax@redhat.com> 2.6.23.1-30
- Add e1000.eeprom_bad_csum_allow

* Sun Oct 21 2007 Jarod Wilson <jwilson@redhat.com> 2.6.23.1-29
- Log warning about unimplemented isochronous I/O on
  firewire ohci 1.0 controllers (bz #344851)

* Fri Oct 19 2007 John W. Linville <linville@redhat.com> 2.6.23.1-28
- iwl4965-base.c: fix off-by-one errors
- ipw2100: send WEXT scan events
- rt2x00: Add new rt73usb USB ID
- zd1211rw, fix oops when ejecting install media
- rt2x00: Fix residual check in PLCP calculations.
- rtl8187: Fix more frag bit checking, rts duration calc
- iwlwifi: set correct base rate for A band in rs_dbgfs_set_mcs
- iwlwifi: Fix rate setting in probe request for HW scan

* Thu Oct 18 2007 Dave Jones <davej@redhat.com> 2.6.23.1-26
- Silence i8042 'error' on imacs and such.

* Thu Oct 18 2007 Dave Jones <davej@redhat.com> 2.6.23.1-25
- Disable polling before registering netdevice in e100.

* Thu Oct 18 2007 John W. Linville <linville@redhat.com> 2.6.23.1-24
- avoid null ptr dereference in rx path of zd1201 driver

* Wed Oct 17 2007 John W. Linville <linville@redhat.com> 2.6.23.1-23
- iee80211: fix an endian bug
- mac80211: change BSSID list key to (BSSID, SSID, frequency) tuple
- mac80211: make honor IW_SCAN_THIS_ESSID
- mac80211: don't fail IE parse if junk at end of frame

* Wed Oct 17 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-22
- Revert to less-accurate but stable 2.6.22 cputime accounting.

* Wed Oct 17 2007 John W. Linville <linville@redhat.com> 2.6.23.1-21
- ath5k fix to avoid oops on driver load w/ unknown/unsupported cards

* Wed Oct 17 2007 Dave Jones <davej@redhat.com> 2.6.23.1-20
- Update highres timers patch to 2.6.23-hrt3.

* Wed Oct 17 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-19
- Use upstream libata DMA disable patch
  (libata.dma parameter replaces libata.pata_dma)

* Tue Oct 16 2007 Eric Paris <eparis@redhat.com> 2.6.23.1-18
- SELinux: performance: do not revalite perms on read/write
- SELinux: performance: use ebitmaps to speed up AVC misses
- SELinux: performance: fix warnings in ebitmaps to speed up AVC misses

* Tue Oct 16 2007 Dave Jones <davej@redhat.com> 2.6.23.1-17
- Disable sparse builds.

* Tue Oct 16 2007 John W. Linville <linville@redhat.com> 2.6.23.1-16
- ath5k updates

* Tue Oct 16 2007 Dave Airlie <airlied@redhat.com> 2.6.23.1-15
- fix i915 drm memory allocation issue

* Mon Oct 15 2007 Jeremy Katz <katzj@redhat.com> 2.6.23.1-13
- fix thinkpad key events for volume/brightness

* Mon Oct 15 2007 Dave Jones <davej@redhat.com> 2.6.23.1-14
- Update highres timers patch to 2.6.23-hrt2.

* Mon Oct 15 2007 Dave Jones <davej@redhat.com> 2.6.23.1-12
- Reenable 'quiet' mode.

* Mon Oct 15 2007 Dave Jones <davej@redhat.com> 2.6.23.1-11
- Work around E1000 corrupt EEPROM problem.

* Fri Oct 12 2007 Dave Jones <davej@redhat.com> 2.6.23.1-10
- Disable debug, start doing -debug builds again.

* Fri Oct 12 2007 Dave Jones <davej@redhat.com> 2.6.23.1-8
- 2.6.23.1

* Fri Oct 12 2007 Jarod Wilson <jwilson@redhat.com> 2.6.23-7
- Fix virtual kernel-arch Provides for flavored kernels (bz #327961)
- Add paeonly build flag

* Thu Oct 11 2007 Dave Jones <davej@redhat.com> 2.6.23-6
- Fix race in e100 driver.

* Wed Oct 10 2007 John W. Linville <linville@redhat.com> 2.6.23-5
- fix scanning for hidden SSID w/ NetworkManager

* Wed Oct 10 2007 Dave Jones <davej@redhat.com> 2.6.23-4
- Update highres timers patch to latest upstream (2.6.23-hrt1)

* Wed Oct 10 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23-3
- Fix date/time printed during boot.
- Port ACPI suspend/resume fixes to cpuidle.

* Wed Oct 10 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23-2
- Don't use incremental patches for -stable updates.
- Add USB modem fix from Fedora 7 kernel to Fedora 8.

* Tue Oct 09 2007 Dave Jones <davej@redhat.com> 2.6.23-1
- 2.6.23

* Tue Oct 09 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc9-git7

* Tue Oct 09 2007 Dave Jones <davej@redhat.com>
- Fix lockdep bug in firewire.

* Mon Oct 08 2007 Dave Jones <davej@redhat.com>
- Add a bunch of modules to the 586 kernel for the livecd.

* Mon Oct 08 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc9-git6

* Sat Oct 06 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc9-git4

* Fri Oct 05 2007 Chuck Ebbert <cebbert@redhat.com>
- Enable HID debugging in all kernels

* Fri Oct 05 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc9-git2

* Fri Oct 05 2007 John W. Linville <linville@redhat.com>
- Back-out last round of wireless updates

* Wed Oct 03 2007 John W. Linville <linville@redhat.com>
- Update wireless bits from upstream

* Tue Oct 02 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc9-git1

* Tue Oct 02 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc9

* Sun Sep 30 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc8-git4

* Fri Sep 28 2007 Chuck Ebbert <cebbert@redhat.com>
- fix X86 memory detection (bz #311491)

* Fri Sep 28 2007 Dave Airlie <airlied@redhat.com>
- Add i965 drm patch to fix vblank interrupts - should be upstream soon

* Thu Sep 27 2007 John W. Linville <linville@redhat.com>
- A few iwlwifi and ath5k fixes

* Thu Sep 27 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc8-git2
- Re-add AMD timer fix removed from upstream
- Fix hotplug CPU (broken by AMD timer patch)

* Thu Sep 27 2007 John W. Linville <linville@redhat.com>
- Fix-up botched wireless patch restructuring...

* Wed Sep 26 2007 John W. Linville <linville@redhat.com>
- Update and restructure wireless patches

* Wed Sep 26 2007 Chuck Ebbert <cebbert@redhat.com>
- x86 e820 bugfix

* Wed Sep 26 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc8-git1

* Wed Sep 26 2007 Dave Jones <davej@redhat.com>
- Switch back to ye olde RTC driver. At least it worked.

* Wed Sep 26 2007 Chuck Ebbert <cebbert@redhat.com>
- Remove extended EDD boot debugging.
- Shorten i386 oops reports by three lines.

* Tue Sep 25 2007 Dave Jones <davej@redhat.com>
- Disable RTC_HCTOSYS. The initscripts already do this for us.

* Tue Sep 25 2007 Dave Jones <davej@redhat.com>
- x86-64: Disable local APIC timer use on AMD systems with C1E.

* Mon Sep 24 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc8

* Mon Sep 24 2007 Roland McGrath <roland@redhat.com>
- Fix powerpc oops on ptrace after exec. (#301791)

* Mon Sep 24 2007 Dave Jones <davej@redhat.com>
- Disable generic RTC.

* Mon Sep 24 2007 Chuck Ebbert <cebbert@redhat.com>
- Fix IDE on ppc Pegasos platform (F7 bz #247602)

* Mon Sep 24 2007 Dave Jones <davej@redhat.com>
- Update hi-res timers patch to .23rc7-hrt1.

* Mon Sep 24 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc7-git5

* Sun Sep 23 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc7-git4

* Sat Sep 22 2007 Chuck Ebbert <cebbert@redhat.com>
- 2.6.23-rc7-git3

* Fri Sep 21 2007 Chuck Ebbert <cebbert@redhat.com>
- Build dcdbas and dell_rbu modules on i586 (#216304)

* Thu Sep 20 2007 Chuck Ebbert <cebbert@redhat.com>
- 2.6.23-rc7-git1

* Thu Sep 20 2007 Dave Jones <davej@redhat.com>
- Enable tcrypt module for crypto testing.

* Thu Sep 20 2007 Dave Jones <davej@redhat.com>
- Enable SECMARK by default.

* Thu Sep 20 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc7

* Thu Sep 20 2007 Dave Airlie <airlied@redhat.com>
- drm-mm-git.patch - pull in DRM git queue
- nouveau-drm.patch - update nouveau patch on top of drm git queue

* Wed Sep 19 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc6-git8
- Enable Secure Computing (CONFIG_SECCOMP) (#295841)

* Tue Sep 18 2007 Eric Sandeen <sandeen@redhat.com>
- ext3 bugfixes: fix potential corruption in do_split dx leaf split (#28650),
  handle dx directory corruption gracefully, w/o BUG (#236464).
- xfs bugfixes: setfattr/getfattr/getversion 32-bit compat fixes (#291981),
  log replay vs. filesize update fixes from sgi.

* Tue Sep 18 2007 John W. Linville <linville@redhat.com>
- Update bits from wireless-2.6 and wireless-dev

* Mon Sep 17 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc6-git7

* Fri Sep 14 2007 Chuck Ebbert <cebbert@redhat.com>
- leave boot option as "libata.pata_dma" for now

* Fri Sep 14 2007 Chuck Ebbert <cebbert@redhat.com>
- x86 setup: limit number of EDD devices scanned
- x86 setup: print number of EDD device during scan
- libata: change boot option name to "libata.dma"
  (still only affects PATA drives)

* Thu Sep 13 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc6-git4

* Thu Sep 13 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc6-git3
- disable stack overflow debugging on i386

* Wed Sep 12 2007 Dave Jones <davej@redhat.com>
- Change ACPI dock drivers to be built-in.

* Wed Sep 12 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc6-git2

* Wed Sep 12 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc6-git1

* Wed Sep 12 2007 Chuck Ebbert <cebbert@redhat.com>
- better debug message for SSB driver
- acpi: fix EC initialization (from linux-acpi.git)

* Tue Sep 11 2007 Roland McGrath <roland@redhat.com>
- utrace update (#248532, #267161, #284311)

* Tue Sep 11 2007 Chuck Ebbert <cebbert@redhat.com>
- fix emulated vmware SCSI disks
- add option to disable libata PATA DMA

* Tue Sep 11 2007 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.23-rc6

* Mon Sep 10 2007 Chuck Ebbert <cebbert@redhat.com>
- fix clock warping on x86_64

* Mon Sep 10 2007 Chuck Ebbert <cebbert@redhat.com>
- x86: fix debug early boot

* Thu Sep 06 2007 Chuck Ebbert <cebbert@redhat.com>
- x86: debug early boot

* Thu Sep 06 2007 Chuck Ebbert <cebbert@redhat.com>
- fix imbalance in scheduler on multicore systems

* Thu Sep 06 2007 Chuck Ebbert <cebbert@redhat.com>
- fix Xen boot problem
- fix boot hang on Via C7 CPU
- Fix oops in networking code

* Thu Sep 06 2007 Chuck Ebbert <cebbert@redhat.com>
- exec-shield: enable interrupts in do_iret_error()

* Wed Sep 05 2007 Chuck Ebbert <cebbert@redhat.com>
- 2.6.23-rc5-git1

* Tue Sep 04 2007 Chuck Ebbert <cebbert@redhat.com>
- fix DMA mode on VIA 6421

* Tue Sep 04 2007 Chuck Ebbert <cebbert@redhat.com>
- Fix oops in cpuidle under QEMU

* Tue Sep  4 2007 Roland McGrath <roland@redhat.com>
- utrace update (#232837, #248532)

* Sun Sep 02 2007 Dave Jones <davej@redhat.com>
- Fix oops in IPv4.

* Sat Sep 01 2007 Dave Jones <davej@redhat.com>
- Terminate list in ata-piix

* Sat Sep 01 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc5

* Fri Aug 31 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc4-git3

* Fri Aug 31 2007 Dave Jones <davej@redhat.com>
- Fix booting on Dell I5150.
  Apparently some VESA BIOS implementations clobber registers when called.

* Fri Aug 31 2007 Dave Jones <davej@redhat.com>
- Reenable CONFIG_ACPI_PROC_EVENT for now, acpid isn't ready.

* Fri Aug 31 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc4-git2

* Thu Aug 30 2007 John W. Linville <linville@redhat.com>
- Update bits from wireless-2.6 and wireless-dev

* Wed Aug 29 2007 Roland McGrath <roland@redhat.com>
- Add ppc64 back to vdso_arches.

* Wed Aug 29 2007 Chuck Ebbert <cebbert@redhat.com>
- enable the i82365 PCMCIA driver
- add debug code to acpi_os_write_port for bug 258641

* Wed Aug 29 2007 David Woodhouse <dwmw2@infradead.org>
- Re-enable ppc32 build.

* Tue Aug 28 2007 Dave Jones <davej@redhat.com>
- Enable ACPI ATA objects.

* Tue Aug 28 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc4

* Tue Aug 28 2007 Peter Jones <pjones@redhat.com>
- Fix collect_modules_list to handle ppc64's dot-prefixed function symbols.

* Mon Aug 27 2007 Jarod Wilson <jwilson@redhat.com>
- Add lirc.org drivers

* Mon Aug 27 2007 Kristian Høgsberg <krh@redhat.com>
- Add patch from Stefan Richter to support multi-lun SBP-2 devices (#242254).

* Sun Aug 26 2007 Dave Jones <davej@redhat.com>
- Resurrect 586 support.

* Sun Aug 26 2007 Dave Jones <davej@redhat.com>
- Fix async scanning double-add problems.

* Sun Aug 26 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3-git10

* Fri Aug 24 2007 Kristian Høgsberg <krh@redhat.com>
- Add patch for the "status write for unknown orb" firewire-sbp2 error.

* Fri Aug 24 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3-git7

* Thu Aug 23 2007 Eric Sandeen <sandeen@redhat.com>
- Update xfs filesystem for bug fixes & stack reduction

* Thu Aug 23 2007 John W. Linville <linville@redhat.com>
- Update wireless-dev bits (mac80211, rt2x00, b43, ssb)
- Add b43legacy driver
- Add ath5k driver
- Add at76_usb driver

* Thu Aug 23 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3-git6

* Wed Aug 22 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3-git5

* Wed Aug 22 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3-git4

* Tue Aug 21 2007 Dave Jones <davej@redhat.com>
- Rebase utrace.

* Mon Aug 20 2007 Dave Jones <davej@redhat.com>
- Add new variant of lparmap ppc buildfix

* Mon Aug 20 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3-git2

* Mon Aug 20 2007 Jarod Wilson <jwilson@redhat.com>
- Better fix for variant kernel %%post scriptlet generation

* Mon Aug 20 2007 Tom "spot" Callaway <tcallawa@redhat.com>
- fix post -smp scriplet

* Sun Aug 19 2007 Dave Jones <davej@redhat.com>
- Remove unneeded compile fix.

* Fri Aug 17 2007 John W. Linville <linville@redhat.com>
- Update wireless-dev bits (upstream fixes, b43, ssb)
- mac80211: rate limit WEP bad keyidx message
- ssb: fix auto-load

* Fri Aug 17 2007 Tom "spot" Callaway <tcallawa@redhat.com>
- sparc64 changes

* Fri Aug 17 2007 Dave Jones <davej@redhat.com>
- Remove dead tux config symbols.

* Thu Aug 16 2007 Roland McGrath <roland@redhat.com>
- commit missed powerpc vdso install update
- fix vdso spec hacks for --with vanilla

* Thu Aug 16 2007 Chuck Ebbert <cebbert@redhat.com>
- export GFS2 symbols for lock modules

* Wed Aug 15 2007 Chuck Ebbert <cebbert@redhat.com>
- enable ACPI debugging in -debug builds

* Wed Aug 15 2007 Chuck Ebbert <cebbert@redhat.com>
- e1000e updates

* Wed Aug 15 2007 Dave Jones <davej@redhat.com>
- Enable BSG.

* Tue Aug 14 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3-git1

* Tue Aug 14 2007 Chuck Ebbert <cebbert@redhat.com>
- set CONFIG_NET_RADIO (#251094)

* Tue Aug 14 2007 John W. Linville <linville@redhat.com>
- Update wireless-dev bits (mac80211, bcm43xx -> b43, ssb)

* Mon Aug 13 2007 Dave Jones <davej@redhat.com>
- Add patch-2.6.23-rc3-hrt2.patch, bringing X86-64 tickless back.

* Mon Aug 13 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc3

* Sun Aug 12 2007 Roland McGrath <roland@redhat.com>
- Rearrange module signing to avoid conflicts with debugedit build-id changes.
- debugedit embedded objects before stripping to avoid later build-id change.
- Use -g for C files in x86_64 vDSO.

* Sun Aug 12 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc2-git5

* Sun Aug 12 2007 Dave Jones <davej@redhat.com>
- Speed up builds by disabling modversions.

* Sun Aug 12 2007 Dave Jones <davej@redhat.com>
- implement smarter atime updates support.

* Sun Aug 12 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc2-git3

* Fri Aug 10 2007 Roland McGrath <roland@redhat.com>
- comment out weak-modules script from %%post and %%preun (RHBZ#251699)

* Thu Aug  9 2007 Roland McGrath <roland@redhat.com>
- disable new e1000e driver for powerpc, still broken
- support rpmbuild --without modsign for manual test builds
- fix powerpc vdso install patch
- fix i386 vdso install patch
- fix ppc64 build breakage from "enable -g for assembly"
- disable ppc64 vdso install pending eu-strip bug
- clean up spec file sh scripting
- spec fix for kdump kernel

* Thu Aug 09 2007 Chuck Ebbert <cebbert@redhat.com>
- update e1000e driver

* Thu Aug 09 2007 John W. Linville <linville@redhat.com>
- remove obsolete linux-2.6-wireless.patch

* Thu Aug  9 2007 Roland McGrath <roland@redhat.com>
- fix vdso install patch
- enable -g for assembly

* Thu Aug 09 2007 Dave Jones <davej@redhat.com>
- Do USB suspend only on certain classes of device.

* Thu Aug 09 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc2-git2

* Thu Aug 09 2007 Chuck Ebbert <cebbert@redhat.com>
- enable CONFIG_CBE_CPUFREQ* on ppc64 (bz #251517)

* Thu Aug 09 2007 Chuck Ebbert <cebbert@redhat.com>
- update e1000e driver

* Thu Aug  9 2007 Roland McGrath <roland@redhat.com>
- macroized spec file, use new find-debuginfo.sh features
- update build-id related patches: core dump support,
  /sys/module/name/notes, installed vdso binaries
- temporarily disable e1000e %%ifarch ppc ppc64 to work around build problems

* Wed Aug 08 2007 John W. Linville <linville@redhat.com>
- Update wireless bits from wireless-2.6 and wireless-dev

* Wed Aug 08 2007 Chuck Ebbert <cebbert@redhat.com>
- new e1000e (ICH9) driver from the netdev tree

* Tue Aug 07 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc2-git1

* Mon Aug 06 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch (bcm43xx-mac80211 & zd1211rw-mac80211 updates)
- mac80211: Filter locally-originated multicast frames echoed by AP (2nd try)
- mac80211: probe for hidden SSIDs when scanning for association

* Mon Aug 06 2007 Dave Jones <davej@redhat.com>
- Make CONFIG_DEBUG_STACK_USAGE a 'make debug' option.

* Sat Aug 04 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc2

* Fri Aug 03 2007 Dave Jones <davej@redhat.com>
- Reenable CONFIG_NETLABEL.

* Fri Aug 03 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc1-git13

* Fri Aug 03 2007 John W. Linville <linville@redhat.com>
- Disable busted mac80211 local multicast filter patch

* Thu Aug 02 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch (iwlwifi & rt2x00 updates)
- mac80211: Filter locally-originated multicast frames echoed by AP

* Thu Aug 02 2007 Dave Jones <davej@redhat.com>
- Add back PIE randomisation.

* Thu Aug 02 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc1-git12

* Tue Jul 31 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch (iwlwifi, rt2x00, & zd1211rw updates)

* Tue Jul 31 2007 Chuck Ebbert <cebbert@redhat.com>
- fix boot from CD and remove debug patch

* Tue Jul 31 2007 David Woodhouse <dwmw2@infradead.org>
- Fix PS3 booting (spu init breakage)

* Tue Jul 31 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc1-git9

* Tue Jul 31 2007 David Woodhouse <dwmw2@infradead.org>
- Fix bcm43xx transmission problems for mac80211 driver too
- Fix softmac deadlock
- Fix bcm43xx regression
- Restore PowerMac suspend via /sys/power/state
- PS3 fixes
- Restore ibmvscsi support on iSeries
- Re-enable CONFIG_IDE_PROC_FS to fix ybin

* Sun Jul 29 2007 Dave Jones <davej@redhat.com>
- 2.6.23-rc1-git6

* Fri Jul 27 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch (iwlwifi & bcm43xx updates)

* Fri Jul 27 2007 Chuck Ebbert <cebbert@redhat.com>
- temporarily added debug messages for x86 bootup

* Fri Jul 27 2007 Chuck Ebbert <cebbert@redhat.com>
- 2.6.23-rc1-git4

* Thu Jul 26 2007 Chuck Ebbert <cebbert@redhat.com>
- 2.6.23-rc1-git3

* Wed Jul 25 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch (iwlwifi & rt2x00 updates)
- Drop git-iwlwifi.patch

* Wed Jul 25 2007 Chuck Ebbert <cebbert@redhat.com>
- 2.6.23-rc1-git1

* Mon Jul 23 2007 John W. Linville <linville@redhat.com>
- Rediff git-wireless-dev.patch

* Sat Jul 21 2007 Roland McGrath <roland@redhat.com>
- Fix biarch issue in linux-2.6-elf-core-sysctl.patch

* Sat Jul 21 2007 Dave Jones <davej@redhat.com>
- 2.6.22-git16

* Sat Jul 21 2007 Roland McGrath <roland@redhat.com>
- Update linux-2.6-elf-core-sysctl.patch

* Fri Jul 20 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch (rt2x00 update)

* Fri Jul 20 2007 Dave Jones <davej@redhat.com>
- 2.6.22-git14

* Thu Jul 19 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch

* Wed Jul 18 2007 John W. Linville <linville@redhat.com>
- mac80211: avoid deadlock during device shutdown

* Mon Jul 16 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch

* Mon Jul 16 2007 Dave Jones <davej@redhat.com>
- Disable ppc64 build until upstream merge (Broken PS3 drivers).

* Mon Jul 16 2007 Dave Jones <davej@redhat.com>
- 2.6.22-git6

* Sat Jul 14 2007 Dave Jones <davej@redhat.com>
- 2.6.22-git5

* Fri Jul 13 2007 Chuck Ebbert <cebbert@redhat.com>
- more ATI SB700 ahci device ids

* Fri Jul 13 2007 Dave Jones <davej@redhat.com>
- 2.6.22-git4

* Fri Jul 13 2007 Dave Jones <davej@redhat.com>
- Disable CONFIG_NETLABEL, it's broken right now.

* Thu Jul 12 2007 Jarod Wilson <jwilson@redhat.com>
- Switch from using kernel-*.config files to using config-*
  files and dynamically building kernel-*.config files

* Thu Jul 12 2007 Jarod Wilson <jwilson@redhat.com>
- Fix up some uname -r issues in certain kernel version
  cases (due to new versioning scheme)

* Thu Jul 12 2007 Dave Jones <davej@redhat.com>
- 2.6.22-git2

* Wed Jul 11 2007 Roland McGrath <roland@redhat.com>
- core dump enhancement: include first page of ELF files, with sysctl control

* Wed Jul 11 2007 John W. Linville <linville@redhat.com>
- Reinstate git-wireless-dev.patch
- Add updated iwlwifi driver from intellinuxwireless.org

* Tue Jul 10 2007 Dave Jones <davej@redhat.com>
- Fix issue with PIE randomization (#246623).

* Tue Jul 10 2007 Dave Jones <davej@redhat.com>
- 2.6.22-git1

* Tue Jul 10 2007 John W. Linville <linville@redhat.com>
- Update git-wireless-dev.patch
- Add upstream version of rtl8187 patch

* Tue Jul 10 2007 Dave Jones <davej@redhat.com>
- Split utrace up into multiple patches.

* Mon Jul 09 2007 Chuck Ebbert <cebbert@redhat.com>
- sky2: restore workarounds for lost interrupts

* Mon Jul 09 2007 Dave Jones <davej@redhat.com>
- Disable forced HPET on ESB2 too

* Mon Jul 09 2007 Chuck Ebbert <cebbert@redhat.com>
- changed default TCP/IPV4 congestion control to CUBIC

* Mon Jul 09 2007 Dave Jones <davej@redhat.com>
- 2.6.22

* Fri Jul 06 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc7-git6

* Fri Jul 06 2007 Dave Jones <davej@redhat.com>
- Remove another common wakeup (cursor blinking).

* Fri Jul 06 2007 Dave Jones <davej@redhat.com>
- Update CFS to v19.

* Fri Jul  6 2007 Jeremy Katz <katzj@redhat.com>
- Add minimal patch from markmc for ICH9 support in e1000 while the new
  driver works itself out upstream

* Fri Jul 06 2007 Chuck Ebbert <cebbert@redhat.com>
- add Intel ICH8M (Santa Rosa) PCI ID to ata_piix driver

* Fri Jul 06 2007 Dave Jones <davej@redhat.com>
- Don't force enable HPET on ICH7 & ICH8
  This seems to be broken.

* Fri Jul 06 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc7-git5

* Thu Jul 05 2007 Dave Jones <davej@redhat.com>
- Remove out-of-date Xen patches (Xen now builds from separate package).

* Thu Jul 05 2007 Dave Jones <davej@redhat.com>
- Further tickless improvements to ondemand cpufreq governor.

* Thu Jul 05 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc7-git4

* Thu Jul 05 2007 Dave Jones <davej@redhat.com>
- Lower mkinitrd requires: to match what's in rawhide.

* Wed Jul 04 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc7-git3

* Wed Jul 04 2007 Dave Jones <davej@redhat.com>
- x86-64 tickless support.

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Remove a redundant if in the specfile.

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Update to latest utrace. (just diff changes, no code changes)

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc7-git2

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc7-git1.

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Remove bogus XFS umount change.

* Tue Jul 03 2007 Jarod Wilson <jwilson@redhat.com>
- New kernel versioning scheme, intended to more closely
  match with that of the upstream Linus kernel
- Add --with/without debuginfo flag
- Redundancy reduction wrt kernel-debug flags
- Make one-off kernel-vanilla build correctly again

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Drop warning about OSS usage. The API will probably never die.

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Remove an unnecessary NFS fix now that the sleep_on change has been dropped.

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Drop a bunch of unneeded warning fixes.

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Remove ancient sleep_on patch. Fix it upstream instead.

* Tue Jul 03 2007 Dave Jones <davej@redhat.com>
- Drop the stale olpc bits, it's now built from a separate branch.

* Mon Jul 02 2007 Dave Jones <davej@redhat.com>
- Bump minimum required mkinitrd to match F-7.

* Mon Jul 02 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc7.

* Sat Jun 30 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc6-git4

* Fri Jun 29 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc6-git3

* Mon Jun 25 2007 Chuck Ebbert <cebbert@redhat.com>
- 2.6.22-rc6
- cfs update for -rc6

* Mon Jun 25 2007 John W. Linville <linville@redhat.com>
- Re-enable wireless-dev patch (updated for current kernel)

* Mon Jun 25 2007 Roland McGrath <roland@redhat.com>
- Let spec-file ApplyPatch function pass extra args to patch.
- Re-enable utrace patch with -F2, needed after nearby CFS change.
- utrace update (06c303cccb93e7ca3a95923e69b4d82733c1cf00)

* Sun Jun 24 2007 Dave Jones <davej@redhat.com>
- Fix 64 bit overflow in CFS.

* Sat Jun 23 2007 Dave Jones <davej@redhat.com>
- DMI based module autoloading.

* Sat Jun 23 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc5-git8.
- Update to latest upstream cfs scheduler.

* Fri Jun 22 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc5-git6.

* Thu Jun 21 2007 Dave Jones <davej@redhat.com>
- Remove some unnecessary parts of execshield.

* Thu Jun 21 2007 Dave Jones <davej@redhat.com>
- Add vDSO for x86-64 with gettimeofday/clock_gettime/getcpu.

* Thu Jun 21 2007 Dave Jones <davej@redhat.com>
- Remove unnecessary patch 'optimising' spinlock debug.

* Wed Jun 20 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc5-git4.

* Mon Jun 19 2007 Chuck Ebbert <cebbert@redhat.com>
- enable sound system debugging in -debug kernels

* Mon Jun 18 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc5-git1.

* Mon Jun 18 2007 Jeremy Katz <katzj@redhat.com>
- add patch from upstream kvm to fix suspend/resume with kvm
  loaded (and guests running)

* Sun Jun 17 2007 Dave Jones <davej@redhat.com>
- Make the 686 kernel bootable on 586s.

* Sun Jun 17 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc5.

* Sun Jun 17 2007 Dave Jones <davej@redhat.com>
- Add Ingo's CFS scheduler.

* Sat Jun 16 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc4-git8. (utrace broke, temporarily disabled).

* Fri Jun 15 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc4-git7.

* Wed Jun 13 2007 John W. Linville <linville@redhat.com>
- Refresh wireless-dev patch
- Drop iwlwifi patch (0.0.25 now in wireless-dev!)

* Tue Jun 12 2007 Dave Jones <davej@redhat.com>
- Disable libusual.

* Tue Jun 12 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc4-git4

* Sat Jun 09 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc4-git3

* Fri Jun  8 2007 Roland McGrath <roland@redhat.com>
- Add spec hacks to enable building alternate vanilla and git-based rpms.
- Enable sparse checking in build for F7 and later.

* Thu Jun 07 2007 John W. Linville <linville@redhat.com>
- Re-introduce iwlwifi patch w/ update to version 0.0.24

* Thu Jun 07 2007 John W. Linville <linville@redhat.com>
- Refresh wireless-dev patch to work w/ 2.6.22 base.

* Wed Jun 06 2007 Dave Jones <davej@redhat.com>
- RTC driver needs to be built-in.

* Wed Jun 06 2007 Dave Jones <davej@redhat.com>
- Fix bug with MAP_FIXED (#242612).

* Wed Jun 06 2007 Dave Jones <davej@redhat.com>
- Build with -Wpointer-arith for a while.
  See http://bugzilla.kernel.org/show_bug.cgi?id=7561 for info.

* Tue Jun 05 2007 Dave Jones <davej@redhat.com>
- Re-add a bunch of multimedia drivers. (#242503)

* Tue Jun 05 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc4

* Mon Jun 04 2007 Dave Jones <davej@redhat.com>
- Allow kdump to read /proc/kcore. (#241362)

* Mon Jun 04 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc3-git7

* Mon Jun 04 2007 Dave Jones <davej@redhat.com>
- Remove some warning switches.

* Sun Jun 03 2007 Dave Jones <davej@redhat.com>
- Un-inline page_is_ram on x86-64.  Rediff crash driver.

* Sat Jun 02 2007 David Woodhouse <dwmw2@infradead.org>
- Re-enable ppc64 builds.

* Fri Jun 01 2007 Dave Jones <davej@redhat.com>
- Disable ppc64 builds.
  http://koji.fedoraproject.org/koji/getfile?taskID=21950&name=build.log

* Fri Jun 01 2007 Dave Jones <davej@redhat.com>
- Update utrace to latest.

* Fri Jun 01 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc3-git6

* Thu May 31 2007 Dave Jones <davej@redhat.com>
- 2.6.22-rc3-git3

* Tue May 29 2007 Dave Jones <davej@redhat.com>
- Anaconda now uses mdadm, remove the raid autorun ioctl.

* Mon May 28 2007 Dave Jones <davej@redhat.com>
- Remove empty config-x86_64 template.

* Mon May 28 2007 Dave Jones <davej@redhat.com>
- With iseries gone, theres no need for a powerpc64-generic config template.

* Mon May 28 2007 Dave Jones <davej@redhat.com>
- Disable sparse for now, until things are buildable again.

* Mon May 28 2007 Dave Jones <davej@redhat.com>
- Reenable debugging.

* Sun May 27 2007 Dave Jones <davej@redhat.com>
- Fix up compat futexes.

* Sun May 27 2007 Dave Jones <davej@redhat.com>
- Apply all patches using -F1, rediff where necessary.

* Sun May 27 2007 Dave Jones <davej@redhat.com>
- Switch to using slub as allocator by default.

* Sun May 27 2007 Dave Jones <davej@redhat.com>
- As the x86-64 kernel is now relocatable, -kdump can go.

* Sun May 27 2007 Dave Jones <davej@redhat.com>
- Remove last remains of 31bit s390 support.

* Sun May 27 2007 Dave Jones <davej@redhat.com>
- Start F8 branch. Rebase to 2.6.22rc3
