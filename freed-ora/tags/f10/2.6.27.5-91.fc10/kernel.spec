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
%define fedora_cvs_origin   1036
%define fedora_build_string %(R="$Revision: 1.1127 $"; R="${R%% \$}"; R="${R#: 1.}"; echo $R)
%define fedora_build_origin %(R=%{fedora_build_string}; R="${R%%%%.*}"; echo $R)
%define fedora_build_prefix %(expr %{fedora_build_origin} - %{fedora_cvs_origin})
%define fedora_build_suffix %(R=%{fedora_build_string}; R="${R#%{fedora_build_origin}}"; echo $R)
%define fedora_build        %{fedora_build_prefix}%{?fedora_build_suffix}

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 27

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
#define librev

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

# Do we have a -stable update to apply?
%define stable_update 5
# Is it a -stable RC?
%define stable_rc 0
# Set rpm version accordingly
%if 0%{?stable_update}
%define stablerev .%{stable_update}
%define stable_base %{stable_update}
%if 0%{?stable_rc}
# stable RCs are incremental patches, so we need the previous stable patch
%define stable_base %(expr %{stable_update} - 1)
%endif
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
# kernel-kdump
%define with_kdump     %{?_without_kdump:     0} %{?!_without_kdump:     1}
# kernel-debug
%define with_debug     %{?_without_debug:     0} %{?!_without_debug:     1}
# kernel-doc
%define with_doc       %{?_without_doc:       0} %{?!_without_doc:       1}
# kernel-headers
%define with_headers   %{?_without_headers:   0} %{?!_without_headers:   1}
# kernel-firmware
%define with_firmware  %{?_with_firmware:  1} %{?!_with_firmware:  0}
# kernel-debuginfo
%define with_debuginfo %{?_without_debuginfo: 0} %{?!_without_debuginfo: 1}
# kernel-bootwrapper (for creating zImages from kernel + initrd)
%define with_bootwrapper %{?_without_bootwrapper: 0} %{?!_without_bootwrapper: 1}
# Want to build a the vsdo directories installed
%define with_vdso_install %{?_without_vdso_install: 0} %{?!_without_vdso_install: 1}

# Build the kernel-doc package, but don't fail the build if it botches.
# Here "true" means "continue" and "false" means "fail the build".
#% define with_doc 0
%if 0%{?released_kernel}
%define doc_build_fail false
%else
%define doc_build_fail true
%endif

# Additional options for user-friendly one-off kernel building:
#
# Only build the base kernel (--with baseonly):
%define with_baseonly  %{?_with_baseonly:     1} %{?!_with_baseonly:     0}
# Only build the smp kernel (--with smponly):
%define with_smponly   %{?_with_smponly:      1} %{?!_with_smponly:      0}
# Only build the pae kernel (--with paeonly):
%define with_paeonly   %{?_with_paeonly:      1} %{?!_with_paeonly:      0}

# should we do C=1 builds with sparse
%define with_sparse	%{?_with_sparse:      1} %{?!_with_sparse:      0}

# Set debugbuildsenabled to 1 for production (build separate debug kernels)
#  and 0 for rawhide (all kernels are debug kernels).
# See also 'make debug' and 'make release'.
%define debugbuildsenabled 1

# Want to build a vanilla kernel build without any non-upstream patches?
# (well, almost none, we need nonintconfig for build purposes). Default to 0 (off).
%define with_vanilla %{?_with_vanilla: 1} %{?!_with_vanilla: 0}

# pkg_release is what we'll fill in for the rpm Release: field
%if 0%{?released_kernel}

%if 0%{?stable_rc}
%define stable_rctag .rc%{stable_rc}
%endif
%define pkg_release %{fedora_build}%{?stable_rctag}%{?buildid}%{?dist}%{?libres}

%else

# non-released_kernel
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

%define KVERREL %{PACKAGE_VERSION}-libre.%{PACKAGE_RELEASE}.%{_target_cpu}
%define hdrarch %_target_cpu
%define asmarch %_target_cpu

%if 0%{!?nopatches:1}
%define nopatches 0
%endif

%if %{with_vanilla}
%define nopatches 1
%endif

%if %{nopatches}
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
%define with_kdump 0
%define with_debug 0
%endif

# if requested, only build smp kernel
%if %{with_smponly}
%define with_up 0
%define with_pae 0
%define with_kdump 0
%define with_debug 0
%endif

# if requested, only build pae kernel
%if %{with_paeonly}
%define with_up 0
%define with_smp 0
%define with_kdump 0
%define with_debug 0
%endif

%define all_x86 i386 i586 i686

%if %{with_vdso_install}
# These arches install vdso/ directories.
%define vdso_arches %{all_x86} x86_64 ppc ppc64
%endif

# Overrides for generic default options

# only ppc and sparc64 need separate smp kernels
%ifnarch ppc sparc64 alphaev56
%define with_smp 0
%endif

# pae is only valid on i686
%ifnarch i686
%define with_pae 0
%endif

# only build kernel-kdump on ppc64
# (no relocatable kernel support upstream yet)
#FIXME: Temporarily disabled to speed up builds.
#ifnarch ppc64
%define with_kdump 0
#endif

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
%define with_firmware  %{?_without_firmware:  0} %{?!_without_firmware:  1}
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
%define asmarch x86
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch x86_64
%define asmarch x86
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
%define kernel_PAE_obsoletes kernel-smp < 2.6.17, kernel-xen <= 2.6.27-0.2.rc0.git6.fc10
%define kernel_PAE_provides kernel-xen = %{rpmversion}-%{pkg_release}

%ifarch x86_64
%define kernel_obsoletes kernel-xen <= 2.6.27-0.2.rc0.git6.fc10
%define kernel_provides kernel-xen = %{rpmversion}-%{pkg_release}
%endif

# We moved the drm include files into kernel-headers, make sure there's
# a recent enough libdrm-devel on the system that doesn't have those.
%define kernel_headers_conflicts libdrm-devel < 2.4.0-0.15

#
# Packages that need to be installed before the kernel is, because the %post
# scripts use them.
#
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, mkinitrd >= 6.0.61-1, kernel-libre-firmware >= %{rpmversion}-%{pkg_release}

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
Provides: kernel-drm-nouveau = 11\
Provides: kernel-modeset = 1\
Provides: kernel-uname-r = %{KVERREL}%{?1:.%{1}}\
Requires(pre): %{kernel_prereq}\
Conflicts: %{kernel_dot_org_conflicts}\
Conflicts: %{package_conflicts}\
%{expand:%%{?kernel%{?1:_%{1}}_conflicts:Conflicts: %%{kernel%{?1:_%{1}}_conflicts}}}\
%{expand:%%{?kernel%{?1:_%{1}}_obsoletes:Obsoletes: %%{kernel%{?1:_%{1}}_obsoletes}}}\
%{expand:%%{?kernel%{?1:_%{1}}_provides:Provides: %%{kernel%{?1:_%{1}}_provides}}}\
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

Source0: http://fsfla.org/selibre/linux-libre/download/freed-ora/src/linux-%{kversion}-libre%{?librev}.tar.bz2

# For documentation purposes only.
Source3: deblob-main
Source4: deblob-%{kversion}
Source5: deblob-check

Source11: genkey
Source14: find-provides
Source15: merge.pl

Source20: Makefile.config
Source21: config-debug
Source22: config-nodebug
Source23: config-generic
Source24: config-rhel-generic

Source30: config-x86-generic
Source31: config-i586
Source32: config-i686
Source33: config-i686-PAE

Source40: config-x86_64-generic

Source50: config-powerpc-generic
Source51: config-powerpc32-generic
Source52: config-powerpc32-smp
Source53: config-powerpc64
Source54: config-powerpc64-kdump

Source60: config-ia64-generic

Source70: config-s390x

Source90: config-sparc64-generic
Source91: config-sparc64-smp

# Here should be only the patches up to the upstream canonical Linus tree.

# For a stable release kernel
%if 0%{?stable_update}
%if 0%{?stable_base}
%define    stable_patch_00  patch%{?stablelibre}-2.6.%{base_sublevel}.%{stable_base}.bz2
Patch00: %{stable_patch_00}
%endif
%if 0%{?stable_rc}
%define    stable_patch_01  patch%{?rcrevlibre}-2.6.%{base_sublevel}.%{stable_update}-rc%{stable_rc}.bz2
Patch01: %{stable_patch_01}
%endif

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

Patch02: git-linus.diff

# we always need nonintconfig, even for -vanilla kernels
Patch03: linux-2.6-build-nonintconfig.patch

# we also need compile fixes for -vanilla
Patch04: linux-2.6-compile-fixes.patch

# build tweak for build ID magic, even for -vanilla
Patch05: linux-2.6-makefile-after_link.patch

%if !%{nopatches}

# revert upstream patches we get via other methods
Patch09: linux-2.6-upstream-reverts.patch
# Git trees.
Patch10: git-cpufreq.patch

# Standalone patches
Patch20: linux-2.6-hotfixes.patch

Patch21: linux-2.6-utrace.patch
Patch22: linux-2.6-x86-tracehook.patch
Patch23: linux-2.6.27-x86-tracehook-syscall-arg-order.patch

Patch30: linux-2.6-x86-mtrr-kill-bogus-warning.patch

Patch41: linux-2.6-sysrq-c.patch

Patch140: linux-2.6-ps3-ehci-iso.patch
Patch141: linux-2.6-ps3-storage-alias.patch
Patch142: linux-2.6-ps3-legacy-bootloader-hack.patch
Patch143: linux-2.6-g5-therm-shutdown.patch
Patch144: linux-2.6-vio-modalias.patch
Patch147: linux-2.6-imac-transparent-bridge.patch
Patch149: linux-2.6-efika-not-chrp.patch

Patch160: linux-2.6-execshield.patch
Patch161: linux-2.6-xen-execshield-add-xen-specific-load_user_cs_desc.patch
Patch163: linux-2.6-xen-execshield-only-define-load_user_cs_desc-on-32-bit.patch
Patch250: linux-2.6-debug-sizeof-structs.patch
Patch260: linux-2.6-debug-nmi-timeout.patch
Patch270: linux-2.6-debug-taint-vm.patch
Patch280: linux-2.6-debug-spinlock-taint.patch
Patch340: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch341: linux-2.6-mm-pagefault-enable-ints.patch
Patch360: linux-2.6-debug-always-inline-kzalloc.patch
Patch370: linux-2.6-crash-driver.patch
Patch380: linux-2.6-defaults-pci_no_msi.patch
Patch381: linux-2.6-pciehp-update.patch
Patch382: linux-2.6-defaults-pciehp.patch
Patch390: linux-2.6-defaults-acpi-video.patch

Patch391: linux-2.6-acpi-video-dos.patch
Patch394: linux-2.6-acpi-handle-ec-init-failure.patch
Patch395: linux-2.6-acpi-dock-avoid-check-sta-method.patch
Patch396: linux-2.6-acpi-dock-fix-eject-request-process.patch

Patch400: linux-2.6-scsi-cpqarray-set-master.patch
Patch420: linux-2.6-squashfs.patch
Patch430: linux-2.6-net-silence-noisy-printks.patch
Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch452: linux-2.6.27-hwmon-applesmc-2.6.28.patch
Patch460: linux-2.6-serial-460800.patch
Patch510: linux-2.6-silence-noise.patch
Patch530: linux-2.6-silence-fbcon-logo.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sparc-selinux-mprotect-checks.patch

Patch670: linux-2.6-ata-quirk.patch
Patch672: linux-2.6-libata-avoid-overflow-with-large-disks.patch

#Patch680: linux-2.6-iwlwifi-use-dma_alloc_coherent.patch
Patch681: linux-2.6-iwlagn-downgrade-BUG_ON-in-interrupt.patch
Patch682: linux-2.6-iwl3945-ibss-tsf-fix.patch
Patch683: linux-2.6-hostap-skb-cb-hack.patch
Patch690: linux-2.6-at76.patch

Patch700: linux-2.6-nfs-client-mounts-hang.patch

Patch800: linux-2.6-alsa-ac97-whitelist.patch
Patch801: linux-2.6-alsa-ac97-whitelist-AD1981B.patch

Patch1101: linux-2.6-default-mmf_dump_elf_headers.patch
Patch1515: linux-2.6.27-lirc.patch
Patch1520: linux-2.6-hdpvr.patch

# Fix the return code CD accesses when the CDROM drive door is closed
# but the drive isn't yet ready.
Patch1550: linux-2.6-cdrom-door-status.patch

# nouveau + drm fixes
Patch1800: nvidia-agp.patch
Patch1810: drm-next.patch
Patch1811: drm-intel-gem-x86-64-faster.patch
Patch1812: drm-modesetting-radeon.patch
Patch1813: drm-modesetting-i915.patch
Patch1814: drm-nouveau.patch

# kludge to make ich9 e1000 work
Patch2000: linux-2.6-e1000-ich9.patch

# new e1000e hardware support from net-next-2.6 (e.g. ich10)
Patch2001: linux-2.6-e1000e-add-support-for-the-82567LM-4-device.patch
Patch2002: linux-2.6-e1000e-add-support-for-82567LM-3-and-82567LF-3-ICH10D-parts.patch
Patch2003: linux-2.6-e1000e-add-support-for-new-82574L-part.patch

# r8169 fixes
Patch2005: linux-2.6-netdev-r8169-2.6.28.patch

# Make Eee laptop driver suck less
Patch2011: linux-2.6-eeepc-laptop-update.patch

# Backport Toshiba updates so Bluetooth can be enabled (#437091)
Patch2012: linux-2.6-toshiba-acpi-update.patch

# atl2 network driver
Patch2020: linux-2.6-netdev-atl2.patch

# Fix DEBUG_SHIRQ problem in tulip driver.  (454575)
Patch2030: linux-2.6-net-tulip-interrupt.patch

# olpc fixes
Patch2040: linux-2.6-olpc-speaker-out.patch
Patch2041: linux-2.6-olpc-touchpad.patch

# linux1394 git patches
Patch2200: linux-2.6-firewire-git-update.patch
Patch2201: linux-2.6-firewire-git-pending.patch

# make USB EHCI driver respect "nousb" parameter
Patch2300: linux-2.6-usb-ehci-hcd-respect-nousb.patch

# Add fips_enable flag
Patch2400: linux-2.6-crypto-fips_enable.patch

# get rid of imacfb and make efifb work everywhere it was used
Patch2600: linux-2.6-merge-efifb-imacfb.patch

# Quiet boot fixes
# silence piix3 in quiet boot (ie, qemu)
Patch2800: linux-2.6-piix3-silence-quirk.patch
# Hush IOMMU warnings, you typically can't fix them anyway
Patch2801: linux-2.6-quiet-iommu.patch
# silence the ACPI blacklist code
Patch2802: linux-2.6-silence-acpi-blacklist.patch
# it's... it's ALIVE!
Patch2803: linux-2.6-amd64-yes-i-know-you-live.patch
# hush pci bar allocation failures
Patch2804: linux-2.6.27-pci-hush-allocation-failures.patch

# ext4 fun - new & improved, now with less dev!
Patch2900: linux-2.6.27-ext4-2.6.28-rc3-git6.patch
Patch2901: linux-2.6.27-ext4-2.6.28-backport-fixups.patch

# cciss sysfs links are broken
Patch3000: linux-2.6-blk-cciss-fix-regression-sysfs-symlink-missing.patch

# Sony Vaio suspend fix
Patch3100: linux-2.6.27-sony-laptop-suspend-fix.patch

# Add better support for DMI-based autoloading
Patch3110: linux-2.6-dmi-autoload.patch

# SELinux: Fix handling of empty tty_files. 37dd0bd04a3240d2922786d501e2f12cec858fbf BZ469079
Patch3120: linux-2.6-selinux-empty-tty-files.patch

# Provide P4 clock modulation in-kernel for thermal reasons, but don't expose
# ui
Patch3130: disable-p4-cpufreq-ui.patch

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

%package firmware
Summary: Firmware files used by the Linux kernel
Group: Development/System
License: GPLv2+
Provides: kernel-firmware = %{rpmversion}-%{pkg_release}
%description firmware
Kernel-firmware includes firmware files required for some devices to
operate.

%package bootwrapper
Summary: Boot wrapper files for generating combined kernel + initrd images
Group: Development/System
Requires: gzip
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

  if [ -d kernel-%{kversion}/vanilla-%{kversion} ]; then

    cd kernel-%{kversion}

    # Any vanilla-* directories other than the base one are stale.
    for dir in vanilla-*; do
      [ "$dir" = vanilla-%{kversion} ] || rm -rf $dir &
    done

  else

    # Ok, first time we do a make prep.
    rm -f pax_global_header
%setup -q -n kernel-%{kversion} -c
    mv linux-%{kversion} vanilla-%{kversion}

  fi

%if "%{kversion}" != "%{vanillaversion}"
  cp -rl vanilla-%{kversion} vanilla-%{vanillaversion}
%endif
  cd vanilla-%{vanillaversion}

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =/" Makefile

# Update vanilla to the latest upstream.
# (non-released_kernel case only)
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

  cd ..

else
  # We already have a vanilla dir.
  cd kernel-%{kversion}
fi

if [ -d linux-%{kversion}.%{_target_cpu} ]; then
  # Just in case we ctrl-c'd a prep already
  rm -rf deleteme.%{_target_cpu}
  # Move away the stale away, and delete in background.
  mv linux-%{kversion}.%{_target_cpu} deleteme.%{_target_cpu}
  rm -rf deleteme.%{_target_cpu} &
fi

cp -rl vanilla-%{vanillaversion} linux-%{kversion}.%{_target_cpu}

cd linux-%{kversion}.%{_target_cpu}

# released_kernel with possible stable updates
%if 0%{?stable_base}
ApplyPatch %{stable_patch_00}
%endif
%if 0%{?stable_rc}
ApplyPatch %{stable_patch_01}
%endif

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
%endif

#ApplyPatch git-linus.diff

# This patch adds a "make nonint_oldconfig" which is non-interactive and
# also gives a list of missing options at the end. Useful for automated
# builds (as used in the buildsystem).
ApplyPatch linux-2.6-build-nonintconfig.patch

ApplyPatch linux-2.6-makefile-after_link.patch

#
# misc small stuff to make things compile
#
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-compile-fixes.patch | awk '{print $1}')
if [ "$C" -gt 10 ]; then
ApplyPatch linux-2.6-compile-fixes.patch
fi

%if !%{nopatches}

# revert patches from upstream that conflict or that we get via other means
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-upstream-reverts.patch | awk '{print $1}')
if [ "$C" -gt 10 ]; then
ApplyPatch linux-2.6-upstream-reverts.patch -R
fi

ApplyPatch git-cpufreq.patch

ApplyPatch linux-2.6-hotfixes.patch

# Roland's utrace ptrace replacement.
ApplyPatch linux-2.6-utrace.patch
ApplyPatch linux-2.6-x86-tracehook.patch
ApplyPatch linux-2.6.27-x86-tracehook-syscall-arg-order.patch

ApplyPatch linux-2.6-x86-mtrr-kill-bogus-warning.patch

# enable sysrq-c on all kernels, not only kexec
ApplyPatch linux-2.6-sysrq-c.patch

# scheduler

# Architecture patches
# x86(-64)

#
# PowerPC
#
### NOT (YET) UPSTREAM:
# The EHCI ISO patch isn't yet upstream but is needed to fix reboot
#ApplyPatch linux-2.6-ps3-ehci-iso.patch
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
# Don't show 'CHRP' in /proc/cpuinfo on Efika
#ApplyPatch linux-2.6-efika-not-chrp.patch

#
# SPARC64
#

#
# Exec shield
#
ApplyPatch linux-2.6-execshield.patch
ApplyPatch linux-2.6-xen-execshield-add-xen-specific-load_user_cs_desc.patch

ApplyPatch linux-2.6-xen-execshield-only-define-load_user_cs_desc-on-32-bit.patch

#
# bugfixes to drivers and filesystems
#

# Pending ext4 patch queue, minus fiemap, includes s/ext4dev/ext4
# ext4/jbd changes up to 2.6.28-rc3-git6
ApplyPatch linux-2.6.27-ext4-2.6.28-rc3-git6.patch
# Fixups for the upstream ext4 code to build cleanly in 2.6.27.
ApplyPatch linux-2.6.27-ext4-2.6.28-backport-fixups.patch

# xfs

# USB
ApplyPatch linux-2.6-usb-ehci-hcd-respect-nousb.patch

# Add the ability to turn FIPS-compliant mode on or off at boot
ApplyPatch linux-2.6-crypto-fips_enable.patch

# ACPI
ApplyPatch linux-2.6-defaults-acpi-video.patch
ApplyPatch linux-2.6-acpi-video-dos.patch
ApplyPatch linux-2.6-acpi-handle-ec-init-failure.patch
# fix dock bugs
ApplyPatch linux-2.6-acpi-dock-avoid-check-sta-method.patch
ApplyPatch linux-2.6-acpi-dock-fix-eject-request-process.patch

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
ApplyPatch linux-2.6-debug-nmi-timeout.patch
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-spinlock-taint.patch
ApplyPatch linux-2.6-debug-vm-would-have-oomkilled.patch
ApplyPatch linux-2.6-mm-pagefault-enable-ints.patch
ApplyPatch linux-2.6-debug-always-inline-kzalloc.patch

#
# /dev/crash driver for the crashdump analysis tool
#
ApplyPatch linux-2.6-crash-driver.patch

#
# PCI
#
# disable message signaled interrupts
ApplyPatch linux-2.6-defaults-pci_no_msi.patch

# update the pciehp driver
ApplyPatch linux-2.6-pciehp-update.patch

# default to enabling passively listening for hotplug events
ApplyPatch linux-2.6-defaults-pciehp.patch

#
# SCSI Bits.
#
# fix cpqarray pci enable
ApplyPatch linux-2.6-scsi-cpqarray-set-master.patch

# ALSA

# Filesystem patches.
# Squashfs
ApplyPatch linux-2.6-squashfs.patch

# Networking
# Disable easy to trigger printk's.
ApplyPatch linux-2.6-net-silence-noisy-printks.patch

# Misc fixes
# The input layer spews crap no-one cares about.
ApplyPatch linux-2.6-input-kill-stupid-messages.patch
# kill annoying applesmc debug messages
ApplyPatch linux-2.6.27-hwmon-applesmc-2.6.28.patch

# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch
# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch

# Make fbcon not show the penguins with 'quiet'
ApplyPatch linux-2.6-silence-fbcon-logo.patch

# Fix the SELinux mprotect checks on executable mappings
ApplyPatch linux-2.6-selinux-mprotect-checks.patch
# Fix SELinux for sparc
ApplyPatch linux-2.6-sparc-selinux-mprotect-checks.patch

# Changes to upstream defaults.

# ia64 ata quirk
ApplyPatch linux-2.6-ata-quirk.patch
# fix overlow with large disk
ApplyPatch linux-2.6-libata-avoid-overflow-with-large-disks.patch

# fix spot's iwlwifi, hopefully...
#ApplyPatch linux-2.6-iwlwifi-use-dma_alloc_coherent.patch
# make jarod's iwl4965 not panic near N APs, hopefully
ApplyPatch linux-2.6-iwlagn-downgrade-BUG_ON-in-interrupt.patch
# iwl3945 fix for stable ad-hoc mode connections (#459401)
ApplyPatch linux-2.6-iwl3945-ibss-tsf-fix.patch
# hostap hack to still work w/ quetionable skb->cb usage
ApplyPatch linux-2.6-hostap-skb-cb-hack.patch

# Add misc wireless bits from upstream wireless tree
ApplyPatch linux-2.6-at76.patch

# NFS Client mounts hang when exported directory do not exist
ApplyPatch linux-2.6-nfs-client-mounts-hang.patch

# implement whitelist for ac97
ApplyPatch linux-2.6-alsa-ac97-whitelist.patch
ApplyPatch linux-2.6-alsa-ac97-whitelist-AD1981B.patch

# build id related enhancements
ApplyPatch linux-2.6-default-mmf_dump_elf_headers.patch

# http://www.lirc.org/
ApplyPatch linux-2.6.27-lirc.patch
# http://hg.jannau.net/hdpvr/
ApplyPatch linux-2.6-hdpvr.patch

# Fix the return code CD accesses when the CDROM drive door is closed
# but the drive isn't yet ready.
ApplyPatch linux-2.6-cdrom-door-status.patch

# fix sysfs links for the cciss driver
ApplyPatch linux-2.6-blk-cciss-fix-regression-sysfs-symlink-missing.patch

ApplyPatch linux-2.6-e1000-ich9.patch

ApplyPatch linux-2.6-e1000e-add-support-for-the-82567LM-4-device.patch
ApplyPatch linux-2.6-e1000e-add-support-for-82567LM-3-and-82567LF-3-ICH10D-parts.patch
ApplyPatch linux-2.6-e1000e-add-support-for-new-82574L-part.patch

ApplyPatch linux-2.6-netdev-r8169-2.6.28.patch

ApplyPatch linux-2.6-eeepc-laptop-update.patch
ApplyPatch linux-2.6-toshiba-acpi-update.patch

# atl2 network driver
ApplyPatch linux-2.6-netdev-atl2.patch

ApplyPatch linux-2.6-net-tulip-interrupt.patch

ApplyPatch linux-2.6-olpc-speaker-out.patch
ApplyPatch linux-2.6-olpc-touchpad.patch

# Nouveau DRM + drm fixes
ApplyPatch nvidia-agp.patch
ApplyPatch drm-next.patch
ApplyPatch drm-intel-gem-x86-64-faster.patch
ApplyPatch drm-modesetting-radeon.patch
#ApplyPatch drm-modesetting-i915.patch
ApplyPatch drm-nouveau.patch

# linux1394 git patches
ApplyPatch linux-2.6-firewire-git-update.patch
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-firewire-git-pending.patch | awk '{print $1}')
if [ "$C" -gt 10 ]; then
ApplyPatch linux-2.6-firewire-git-pending.patch
fi

# get rid of imacfb and make efifb work everywhere it was used
ApplyPatch linux-2.6-merge-efifb-imacfb.patch

# Sony Vaio suspend fix
ApplyPatch linux-2.6.27-sony-laptop-suspend-fix.patch

ApplyPatch linux-2.6-dmi-autoload.patch

# silence piix3 in quiet boot (ie, qemu)
ApplyPatch linux-2.6-piix3-silence-quirk.patch
# Hush IOMMU warnings, you typically can't fix them anyway
ApplyPatch linux-2.6-quiet-iommu.patch
# silence the ACPI blacklist code
ApplyPatch linux-2.6-silence-acpi-blacklist.patch
# it's... it's ALIVE!
ApplyPatch linux-2.6-amd64-yes-i-know-you-live.patch
# hush pci bar allocation failures
ApplyPatch linux-2.6.27-pci-hush-allocation-failures.patch

# SELinux on ppc64 without plymouth can't boot
ApplyPatch linux-2.6-selinux-empty-tty-files.patch

ApplyPatch disable-p4-cpufreq-ui.patch

# END OF PATCH APPLICATIONS

%endif

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl

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
export AFTER_LINK=\
'sh -xc "/usr/lib/rpm/debugedit -b $$RPM_BUILD_DIR -d /usr/src/debug -i $@"'
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
    make -s ARCH=$Arch V=1 %{?_smp_mflags} $MakeTarget %{?sparse_mflags}
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
    # Override $(mod-fw) because we don't want it to install any firmware
    # We'll do that ourselves with 'make firmware_install'
    make -s ARCH=$Arch INSTALL_MOD_PATH=$RPM_BUILD_ROOT modules_install KERNELRELEASE=$KernelVer mod-fw=
%ifarch %{vdso_arches}
    make -s ARCH=$Arch INSTALL_MOD_PATH=$RPM_BUILD_ROOT vdso_install KERNELRELEASE=$KernelVer
    if grep '^CONFIG_XEN=y$' .config >/dev/null; then
      echo > ldconfig-kernel.conf "\
# This directive teaches ldconfig to search in nosegneg subdirectories
# and cache the DSOs there with extra bit 0 set in their hwcap match
# fields.  In Xen guest kernels, the vDSO tells the dynamic linker to
# search in nosegneg subdirectories and to match this extra hwcap bit
# in the ld.so.cache file.
hwcap 0 nosegneg"
    fi
    if [ ! -s ldconfig-kernel.conf ]; then
      echo > ldconfig-kernel.conf "\
# Placeholder file, no vDSO hwcap entries used in this kernel."
    fi
    %{__install} -D -m 444 ldconfig-kernel.conf \
        $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernel-$KernelVer.conf
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
    if [ -d arch/$Arch/scripts ]; then
      cp -a arch/$Arch/scripts $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/%{_arch} || :
    fi
    if [ -f arch/$Arch/*lds ]; then
      cp -a arch/$Arch/*lds $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/%{_arch}/ || :
    fi
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/*.o
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/*/*.o
%ifarch ppc
    cp -a --parents arch/powerpc/lib/crtsavres.[So] $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
%endif
    if [ -d arch/$Arch/include ]; then
      cp -a --parents arch/$Arch/include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cd include
    cp -a acpi config keys linux math-emu media mtd net pcmcia rdma rxrpc scsi sound video drm asm-generic $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    asmdir=$(readlink asm)
    cp -a $asmdir $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/
    pushd $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    ln -s $asmdir asm
    popd
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

%if %{with_kdump}
BuildKernel vmlinux vmlinux kdump vmlinux
%endif

%if %{with_doc}
# Make the HTML and man pages.
make %{?_smp_mflags} htmldocs mandocs || %{doc_build_fail}

# sometimes non-world-readable files sneak into the kernel source tree
chmod -R a=rX Documentation
find Documentation -type d | xargs chmod u+w
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

%if %{with_doc}
docdir=$RPM_BUILD_ROOT%{_datadir}/doc/kernel-doc-%{rpmversion}
man9dir=$RPM_BUILD_ROOT%{_datadir}/man/man9

# copy the source over
mkdir -p $docdir
tar -f - --exclude=man --exclude='.*' -c Documentation | tar xf - -C $docdir

# Install man pages for the kernel API.
mkdir -p $man9dir
find Documentation/DocBook/man -name '*.9.gz' -print0 |
xargs -0 --no-run-if-empty %{__install} -m 444 -t $man9dir $m
ls $man9dir | grep -q '' || > $man9dir/BROKEN
%endif

%if %{with_headers}
# Install kernel headers
make ARCH=%{hdrarch} INSTALL_HDR_PATH=$RPM_BUILD_ROOT/usr headers_install

# Do headers_check but don't die if it fails.
make ARCH=%{hdrarch} INSTALL_HDR_PATH=$RPM_BUILD_ROOT/usr headers_check \
     > hdrwarnings.txt || :
if grep -q exist hdrwarnings.txt; then
   sed s:^$RPM_BUILD_ROOT/usr/include/:: hdrwarnings.txt
   # Temporarily cause a build failure if header inconsistencies.
   # exit 1
fi

find $RPM_BUILD_ROOT/usr/include \
     \( -name .install -o -name .check -o \
     	-name ..install.cmd -o -name ..check.cmd \) | xargs rm -f

# glibc provides scsi headers for itself, for now
rm -rf $RPM_BUILD_ROOT/usr/include/scsi
rm -f $RPM_BUILD_ROOT/usr/include/asm*/atomic.h
rm -f $RPM_BUILD_ROOT/usr/include/asm*/io.h
rm -f $RPM_BUILD_ROOT/usr/include/asm*/irq.h
%endif

%if %{with_firmware}
make INSTALL_FW_PATH=$RPM_BUILD_ROOT/lib/firmware firmware_install
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
#	%%kernel_devel_post [<subpackage>]
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
#	%%kernel_variant_posttrans [<subpackage>]
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_posttrans() \
%{expand:%%posttrans %{?1}}\
/sbin/new-kernel-pkg --package kernel-libre%{?1:-%{1}} --rpmposttrans %{KVERREL}%{?1:.%{1}} || exit $?\
%{nil}

#
# This macro defines a %%post script for a kernel package and its devel package.
#	%%kernel_variant_post [-v <subpackage>] [-r <replace>]
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_post(v:r:) \
%{expand:%%kernel_devel_post %{?-v*}}\
%{expand:%%kernel_variant_posttrans %{?-v*}}\
%{expand:%%post %{?-v*}}\
%{-r:\
if [ `uname -i` == "x86_64" -o `uname -i` == "i386" ] &&\
   [ -f /etc/sysconfig/kernel ]; then\
  /bin/sed -r -i -e 's/^DEFAULTKERNEL=%{-r*}$/DEFAULTKERNEL=kernel%{?-v:-%{-v*}}/' /etc/sysconfig/kernel || exit $?\
fi}\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --mkinitrd --depmod --install %{KVERREL}%{?-v:.%{-v*}} || exit $?\
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
%ifarch x86_64
%kernel_variant_post -r (kernel-smp|kernel-xen)
%else
%kernel_variant_post -r kernel-smp
%endif

%kernel_variant_preun smp
%kernel_variant_post -v smp

%kernel_variant_preun PAE
%kernel_variant_post -v PAE -r (kernel-smp|kernel-xen)

%kernel_variant_preun debug
%kernel_variant_post -v debug

%kernel_variant_post -v PAEdebug -r (kernel-smp|kernel-xen)
%kernel_variant_preun PAEdebug

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

%if %{with_firmware}
%files firmware
%defattr(-,root,root)
/lib/firmware/*
%doc linux-%{kversion}.%{_target_cpu}/firmware/WHENCE
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
%{_datadir}/doc/kernel-doc-%{rpmversion}/Documentation/*
%dir %{_datadir}/doc/kernel-doc-%{rpmversion}/Documentation
%dir %{_datadir}/doc/kernel-doc-%{rpmversion}
%{_datadir}/man/man9/*
%endif

# This is %{image_install_path} on an arch where that includes ELF files,
# or empty otherwise.
%define elf_image_install_path %{?kernel_image_elf:%{image_install_path}}

#
# This macro defines the %%files sections for a kernel package
# and its devel and debuginfo packages.
#	%%kernel_variant_files [-k vmlinux] <condition> <subpackage>
#
%define kernel_variant_files(k:) \
%if %{1}\
%{expand:%%files %{?2}}\
%defattr(-,root,root)\
/%{image_install_path}/%{?-k:%{-k*}}%{!?-k:vmlinuz}-%{KVERREL}%{?2:.%{2}}\
/boot/System.map-%{KVERREL}%{?2:.%{2}}\
#/boot/symvers-%{KVERREL}%{?2:.%{2}}.gz\
/boot/config-%{KVERREL}%{?2:.%{2}}\
%dir /lib/modules/%{KVERREL}%{?2:.%{2}}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/kernel\
/lib/modules/%{KVERREL}%{?2:.%{2}}/build\
/lib/modules/%{KVERREL}%{?2:.%{2}}/source\
/lib/modules/%{KVERREL}%{?2:.%{2}}/extra\
/lib/modules/%{KVERREL}%{?2:.%{2}}/updates\
/lib/modules/%{KVERREL}%{?2:.%{2}}/weak-updates\
%ifarch %{vdso_arches}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/vdso\
/etc/ld.so.conf.d/kernel-%{KVERREL}%{?2:.%{2}}.conf\
%endif\
/lib/modules/%{KVERREL}%{?2:.%{2}}/modules.*\
%ghost /boot/initrd-%{KVERREL}%{?2:.%{2}}.img\
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

%changelog
* Sun Nov 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.5-91
- Fix up the CVE-2008-3528 patch so we get it from -stable.

* Sun Nov 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.5-90
- ext4 updates to 2.6.28-rc3

* Sun Nov 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.5-89
- Fix ACPI dock bugs (#470321)

* Sat Nov 08 2008 Dave Airlie <airlied@redhat.com> 2.6.27.5-88
- radeon modesetting - fix mouse on second head and 3 second hangs hopefully

* Fri Nov 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.5-87
- Linux 2.6.27.5
  Dropped Patches:
    linux-2.6-sched-features-disable-hrtick.patch
    linux-2.6-sched_clock-prevent-scd-clock-from-moving-backwards
    linux-2.6-x86-avoid-dereferencing-beyond-stack-THREAD_SIZE.patch
    linux-2.6-usb-storage-unusual-devs-jmicron-ata-bridge.patch
    linux-2.6-acpi-clear-wake-status.patch
    linux-2.6-acpi-ignore-reset_reg_sup.patch
    linux-2.6-net-tcp-option-ordering.patch
    linux-2.6-input-dell-keyboard-keyup.patch
    linux-2.6.27-acpi-ec-drizzle.patch
    linux-2.6-libata-pata_it821x-fix-lba48-on-raid-volumes.patch
    linux-2.6-rtc-cmos-look-for-pnp-rtc-first.patch
    linux-2.6-x86-register-platform-rtc-if-pnp-doesnt-describe-it.patch
    linux-2.6-agp-intel-cantiga-fix.patch
  Reverted from upstream:
    firewire-fix-ioctl-return-code.patch
    firewire-fix-setting-tag-and-sy-in-iso-transmission.patch
    firewire-fix-struct-fw_node-memory-leak.patch
    firewire-fw-sbp2-delay-first-login-to-avoid-retries.patch
    firewire-fw-sbp2-fix-races.patch
    firewire-survive-more-than-256-bus-resets.patch

* Fri Nov 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-86
- Update the r8169 network driver to the 2.6.28 version.

* Fri Nov 07 2008 John W. Linville <linville@redhat.com> 2.6.27.4-85
- Re-modularize ieee80211 component
- Cleanup ieee80211-related config stuff

* Thu Nov 06 2008 Dave Jones <davej@redhat.com>
- alsa: implement ac97_clock whitelist (#441087)

* Wed Nov 05 2008 John W. Linville <linville@redhat.com> 2.6.27.4-83
- Re-modularize mac80211 component (#470143)

* Wed Nov 05 2008 Dave Jones <davej@redhat.com> 2.6.27.4-82
- x86/mm: do not trigger a kernel warning if user-space disables interrupts and generates a page fault

* Wed Nov 05 2008 Dave Jones <davej@redhat.com> 2.6.27.4-81
- ACPI: Ignore AE_NOT_FOUND error of EC _REG method and continue to initialize EC (#435653)

* Wed Nov  5 2008 Jeremy Katz <katzj@redhat.com> 2.6.27.4-80
- linux-2.6-olpc-touchpad.patch: backport from 2.6.28
  * Adds support for quirks of the OLPC touchpad

* Mon Nov 03 2008 Matthew Garrett <mjg@redhat.com> 2.6.27.4-79
- linux-2.6-toshiba-acpi-update.patch: backport from 2.6.28
  * Adds support for rfkill control of Bluetooth (#437091)
- linux-2.6-dmi-autoload.patch: backport DMI autoloading from 2.6.28
  * Fixes autoloading of Macbook Pro Nvidia backlight driver (#462409)

* Mon Nov 03 2008 Eric Paris <eparis@redhat.com> 2.6.27.4-76
- Fix selinux oops on ppc64 due to empty tty_files list (#469079)

* Mon Nov 03 2008 Matthew Garrett <mjg@redhat.com> 2.6.27.4-75
- linux-2.6-pciehp-update.patch
  * Update pciehp driver to support autoloading and listening for events
- linux-2.6-defaults-pciehp.patch
  * Enable passive mode by default
- Build acpiphp in statically
- disable-p4-cpufreq-ui.patch
  * Remove the UI from the p4-clockmod code, but allow it to be used in-kernel

* Mon Nov 03 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-73
- drm-modesetting-radeon.patch: fix modeset reporting for pm-utils

* Mon Nov 03 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-72
- backport upstream fixes to make 64-bit Intel GEM useable (#469584)

* Fri Oct 31 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-71
- Fix overflow in libata when using large disks.

* Fri Oct 31 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-70
- Silence bogus MTRR warning when running in vmware (#468845)
- Remove xen dependencies patch, now upstream and not needed in Fedora.

* Fri Oct 31 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-69
- radeon: fix out of bounds VRAM access - hopefully fixes the corruption

* Thu Oct 30 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-68
- radeon: try and workaround AGP badness with kms + enable VRAM mtrr

* Wed Oct 29 2008 Dave Jones <davej@redhat.com> 2.6.27.4-67
- Reduce maximum supported CPUs on x86-64 to 64.

* Wed Oct 29 2008 Jarod Wilson <jarod@redhat.com> 2.6.27.4-66
- Update to latest firewire git code:
  * Resolve spb2/ohci module load race causing delayed sbp2 logins (#466679)
  * Prevent >256 bus resets from crashing the system (improves #244576)
  * Fix assorted memory leaks
  * Include timestamps in iso packet headers

* Wed Oct 29 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-65
- radeon modesetting : misc fixes - rs690, agp unload, module unload warning

* Tue Oct 28 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-64
- Drop Fedora patch that changed the default FAT charset to UTF-8 (F9#454013)

* Tue Oct 28 2008 John W. Linville <linville@redhat.com> 2.6.27.4-63
- hostap hack to still work w/ quetionable skb->cb usage (#468613)

* Tue Oct 28 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-61
- modesetting add some debugging in /proc and pad ring writes

* Tue Oct 28 2008 Jeremy Katz <katzj@redhat.com> 2.6.27.4-60
- add fix for speaker output on OLPC (#466038)

* Tue Oct 28 2008 John W. Linville <linville@redhat.com> 2.6.27.4-59
- iwl3945 fix for stable ad-hoc mode connections (#459401)

* Tue Oct 28 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-58
- add support for wait rendering API

* Tue Oct 28 2008 Dave Airlie <airlied@redhat.com>  2.6.27.4-57
- fix rs4xx bus mastering.

* Mon Oct 27 2008 Jeremy Katz <katzj@redhat.com>  2.6.27.4-56
- Make olpc-battery built in so that its usable (#467759)

* Mon Oct 27 2008 Eric Sandeen <sandeen@redhat.com> 2.6.27.4-55
- Delay capable() checks in ext4 until necessary. (#467216)

* Mon Oct 27 2008 Dave Jones <davej@redhat.com>  2.6.27.4-54
- ACPI: Ignore the RESET_REG_SUP bit when using ACPI reset mechanism. (461228)

* Mon Oct 27 2008 Dave Airlie <airlied@redhat.com> 2.6.27.4-52
- drm-modesetting-radeon.patch - fix some kms issues + add better CS scheme

* Sun Oct 26 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-51
- Linux 2.6.27.4

* Sat Oct 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-50.rc3
- Two patches for the r8169 network driver that should fix bug #460747

* Sat Oct 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-49.rc3
- Scheduler fixes from 2.6.28, fixing performance problems:
    Disable HRTICK scheduler feature.
    Keep the scheduler clock from going backwards.

* Sat Oct 25 2008 Dave Jones <davej@redhat.com> 2.6.27.4-48.rc3
- tcp: Restore ordering of TCP options for the sake of inter-operability

* Fri Oct 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-47.rc3
- Fix LBA48 on pata_it821x RAID volumes.

* Fri Oct 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-46.rc3
- Fix I/O errors on jmicron USB/ATA bridges (#465539)

* Fri Oct 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.4-45.rc3
- 2.6.27.4-rc3
  Dropped patches:
    linux-2.6-x86-acpi-fix-resume-on-64-bit-UP-systems.patch
  Upstream reverts:
    ext-avoid-printk-floods-in-the-face-of-directory-corruption.patch

* Thu Oct 23 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.3-44
- Disable the snd-aw2 driver until upstream comes up with a fix.

* Thu Oct 23 2008 Adam Jackson <ajax@redhat.com> 2.6.27.3-43
- Hush more PCI BAR allocation failures

* Thu Oct 23 2008 Jarod Wilson <jarod@redhat.com> 2.6.27.3-42
- Disable r8169 2.6.28 update patch, causes much bustification

* Thu Oct 23 2008 Hans de Goede <hdegoede.redhat.com> 2.6.27.3-39
- Disable the obsolete zc0301 driver (superseeded by gspca_zc3xx)

* Wed Oct 22 2008 Kyle McMartin <kyle@redhat.com> 2.6.27.3-38
- Linux 2.6.27.3

* Wed Oct 22 2008 Eric Sandeen <sandeen@redhat.com>
- Patch for CVE-2008-3528, ext-fs dir corruption.

* Tue Oct 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.3-37.rc1
- Support building from CVS branches.

* Tue Oct 21 2008 Adam Jackson <ajax@redhat.com> 2.6.27.3-36.rc1
- Quieten the EC storm printk

* Tue Oct 21 2008 Jarod Wilson <jarod@redhat.com> 2.6.27.3-35.rc1
- improved lirc support for iMon LCD/IR devices
- lirc support for additional MCE transceivers
- nuke lirc commandir kernel drivers, lirc 0.8.4 talks to the via userspace
- nuke lirc atiusb kernel drivers, conflicts with ati_remote (#462212), and
  one can keep using lirc w/these via the atilibusb userspace driver

* Tue Oct 21 2008 Dave Airlie <airlied@redhat.com> 2.6.27.3-34.rc1
- rebase to drm-next from upstream for GEM fixes.
- drop intel modesetting for now - broken by rebase

* Mon Oct 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.3-33.rc1
- Update applesmc hwmon driver to what is upstream for 2.6.28.

* Mon Oct 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.3-32.rc1
- Update r8169 network driver to what is upstream for 2.6.28.

* Mon Oct 20 2008 David Woodhouse <David.Woodhouse@intel.com>
- Fix %%{_arch} vs. $Arch confusion in fix for #465486

* Mon Oct 20 2008 Dave Airlie <airlied@redhat.com>
- radeon: fix VRAM sizing issue

* Mon Oct 20 2008 Dave Airlie <airlied@redhat.com>
- radeon: fix writeback + some warning fixes

* Sun Oct 19 2008 Dave Jones <davej@redhat.com>
- Disable debug printks in the memstick drivers.

* Fri Oct 17 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.3-27.rc1
- Linux 2.6.27.3-rc1
  Dropped patches:
    linux-2.6.27-xfs-remount-fix.patch

* Fri Oct 17 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.2-26.rc1
- Fix resume on x86_64 UP systems with SMP kernel.

* Fri Oct 17 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.2-25.rc1
- DRM: fix ioctl security issue (CVE-2008-3831).

* Fri Oct 17 2008 Adam Jackson <ajax@redhat.com> 2.6.27.2-24.rc1
- Fix suspend on newer Vaios

* Thu Oct 16 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.2-23.rc1
- Linux 2.6.27.2-rc1
  Dropped patches:
    linux-2.6-x86-improve-up-kernel-when-cpu-hotplug-and-smp.patch
    linux-2.6.27-xfs-barrier-fix.patch
    linux-2.6-mac80211-debugfs-stable-fix.patch

* Thu Oct 16 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.1-22
- Fix the cciss sysfs links fix.

* Thu Oct 16 2008 Adam Jackson <ajax@redhat.com> 2.6.27.1-21
- Don't carp about PCI BAR allocation failures in quiet boot

* Thu Oct 16 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27.1-20
- Fix RTC on systems that don't expose it via PnP (F9#451188)

* Thu Oct 16 2008 Kyle McMartin <kyle@redhat.com> 2.6.27.1-19
- Linux 2.6.27.1

* Thu Oct 16 2008 Eric Sandeen <sandeen@redhat.com> 2.6.27-18
- ext4 updates from stable patch queue

* Wed Oct 15 2008 Dave Airlie <airlied@redhat.com> 2.6.27-17
- radeon-modesetting - fix rs48x

* Wed Oct 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27-16
- Disable FTRACE; DYNAMIC_FTRACE will be marked broken in 2.6.27.1
  (without dynamic ftrace the overhead is noticeable.)

* Wed Oct 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27-15
- Fix cciss sysfs links. (#466181)

* Wed Oct 15 2008 Roland McGrath <roland@redhat.com> 2.6.27-14
- fix x86 syscall_get_arguments() order

* Wed Oct 15 2008 Dave Airlie <airlied@redhat.com>
- radeon modesetting agp support

* Wed Oct 15 2008 Dave Airlie <airlied@redhat.com>
- fix cantiga hopefully.

* Tue Oct 14 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Tue Oct 14 2008 Kyle McMartin <kyle@redhat.com>
- nuke iwlwifi-use-dma_alloc_coherent.patch, should be fixed properly now.

* Mon Oct 13 2008 John W. Linville <linville@redhat.com>
- -stable fix for mac80211 debugf-related panics

* Mon Oct 13 2008 Eric Sandeen <sandeen@redhat.com>
- Add fix for xfs root mount failure when some options are used.
- Update to upstream ext4 code destined for 2.6.28.

* Fri Oct 10 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.3
- Adjusted intel modesetting patch for deblobbed kernel.

* Fri Oct 10 2008 Dave Airlie <airlied@redhat.com>
- rebase drm patches onto drm-next.patch which is going upstream
- intel modesetting make not work properly due to rebase

* Thu Oct 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.27-2
- Fix possible oops in get_wchan()

* Thu Oct 09 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.1
- Deblobbed 2.6.27.

* Thu Oct 09 2008 Dave Jones <davej@redhat.com>
- 2.6.27

* Thu Oct 09 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc9-git2

* Thu Oct 09 2008 Chuck Ebbert <cebbert@redhat.com>
- x86: switch to UP mode when only one CPU is present at boot time

* Thu Oct 09 2008 Peter Jones <pjones@redhat.com>
- Fix the return code CD accesses when the CDROM drive door is closed
  but the drive isn't yet ready.

* Thu Oct 09 2008 Eric Sandeen <sandeen@redhat.com>
- Fix for xfs wrongly disabling barriers while running.

* Wed Oct 08 2008 Dave Jones <davej@redhat.com>
- The mptfusion/vmware patch isn't needed after all. (#466071)

* Wed Oct 08 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.408.rc9.git1 Oct 09
- Updated to 2.6.26-libre2 baseline, and 2.6.27-rc9 patch to match.

* Wed Oct 08 2008 Eric Sandeen <sandeen@redhat.com>
- Add in latest ext4 patch queue - rename to ext4 as well.

* Wed Oct 08 2008 Dave Jones <davej@redhat.com>
- Reenable a bunch of PPC config options that broke earlier.

* Wed Oct 08 2008 Dave Jones <davej@redhat.com>
- Fix DEBUG_SHIRQ problem in tulip driver.  (#454575)

* Wed Oct 08 2008 Dave Jones <davej@redhat.com>
- Work around VMWares busted mptfusion emulation again. (#466071)

* Wed Oct 08 2008 Dave Airlie <airlied@redhat.com>
- radeon - hopefully fix suspend/resume - reenable HW migration

* Tue Oct 07 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc9-git1

* Tue Oct  7 2008 Roland McGrath <roland@redhat.com>
- Fix build ID fiddling magic. (#465873)

* Mon Oct 06 2008 Eric Sandeen <sandeen@redhat.com>
- Turn stack overflow debugging back on for x86.

* Mon Oct 06 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.398.rc9
- Deblobbed 2.6.27-rc9.

* Mon Oct 06 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc9

* Mon Oct 06 2008 Dave Jones <davej@redhat.com>
- Add cpufreq.git bits queued for 2.6.28.

* Mon Oct 06 2008 Jarod Wilson <jarod@redhat.com>
- Add driver for Hauppauge HD PVR

* Mon Oct 06 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc8-git8

* Mon Oct 06 2008 Jarod Wilson <jarod@redhat.com>
- Don't BUG_ON when iwl4695 gets packets in the wrong queue,
  just WARN. Slight leak w/kerneloops report is better than
  simply panicking, Intel working on root-cause (#457154).

* Mon Oct 06 2008 Dave Airlie <airlied@redhat.com>
- drm-modesetting-radeon.patch - fix drm mode header + Xv alignment issue

* Sun Oct 05 2008 Dave Airlie <airlied@redhat.com>
- drm-fix-drm-mode-h.patch - fix drm mode .h header

* Sat Oct 04 2008 Chuck Ebbert <cebbert@redhat.com>
- Update to the latest git (2 patches.)

* Sat Oct 04 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.27-rc8-git7

* Sat Oct 04 2008 Chuck Ebbert <cebbert@redhat.com>
- Make applesmc driver stop spewing messages. (#463756)

* Sat Oct 04 2008 Chuck Ebbert <cebbert@redhat.com>
- Support building -stable RC kernels.

* Sat Oct 04 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.27-rc8-git6

* Fri Oct 03 2008 Dave Jones <davej@redhat.com>
- Demodularise some of the devicemapper modules that always get loaded.

* Fri Oct 03 2008 David Woodhouse <David.Woodhouse@intel.com>
- Include arch/$ARCH/include/ directories in kernel-devel (#465486)
- Include arch/powerpc/lib/crtsavres.[So] too (#464613)

* Fri Oct 03 2008 Chuck Ebbert <cebbert@redhat.com>
- specfile: don't use the latest stable update for the vanilla directory.

* Fri Oct 03 2008 Kyle McMartin <kyle@redhat.com>
- 2.6.27-rc8-git5

* Thu Oct 02 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc8-git4

* Thu Oct 02 2008 Dave Jones <davej@redhat.com>
- Add the ability to turn FIPS-compliant mode on or off at boot.

* Thu Oct 02 2008 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.27-rc8-git3
- Re-enable e1000e driver, corruption prevention fix is upstream

* Wed Oct 01 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc8-git2

* Wed Oct 01 2008 Jarod Wilson <jarod@redhat.com>
- Build pcie hotplug driver in, so expresscards Just Work(tm)

* Wed Oct 01 2008 Dave Airlie <airlied@redhat.com>
- nvidia-agp support for TTM

* Tue Sep 30 2008 Dave Jones <davej@redhat.com>
- Disable debugging options in default builds. Enable kernel-debug.

* Tue Sep 30 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc8-git1

* Tue Sep 30 2008 Dave Airlie <airlied@redhat.com>
- drm modesetting - radeon add some fixes

* Tue Sep 30 2008 Jeremy Katz <katzj@redhat.com>
- update to squashfs 3.4

* Mon Sep 29 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.370.rc8
- Deblobbed 2.6.27-rc8.

* Mon Sep 29 2008 Roland McGrath <roland@redhat.com>
- 2.6.27-rc8
- utrace update
  - fix CLONE_PTRACE (#461552)
  - fix PTRACE_O_TRACEVFORK (#464520)

* Mon Sep 29 2008 Dave Jones <davej@redhat.com>
- Turn off CONFIG_USB_DEBUG. It's noisy, and of no real value right now.

* Mon Sep 29 2008 Dave Jones <davej@redhat.com>
- Kill of config-ia64. for real this time.

* Mon Sep 29 2008 Adam Jackson <ajax@redhat.com>
- Kill the useless "Kernel alive" early_printk()'s

* Sun Sep 28 2008 Chuck Ebbert <cebbert@redhat.com>
- make XEN__SAVE_RESTORE denpend on XEN

* Fri Sep 26 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc7-git5

* Thu Sep 25 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc7-git4

* Thu Sep 25 2008 Dave Jones <davej@redhat.com>
- Disable the BT_HCIUSB driver.
  It sucks more power than BT_HCIBTUSB which has the same functionality.

* Thu Sep 25 2008 Dave Jones <davej@redhat.com>
- Change some more netfilter bits we always load to be built-ins.

* Thu Sep 25 2008 Peter Jones <pjones@redhat.com>
- Remove i8042 "No controller found." noise.

* Thu Sep 25 2008 Dave Jones <davej@redhat.com>
- Disable noisy ALSA debug spew.

* Thu Sep 25 2008 Dave Jones <davej@redhat.com>
- Disable building kdump kernels for ppc64 for now.

* Thu Sep 25 2008 Dave Jones <davej@redhat.com>
- Drop a bunch of unused/old patches.
  Rediff & Reapply PPC32/Sparc SELinux mprotect patches.

* Thu Sep 25 2008 Dave Jones <davej@redhat.com>
- Drop duplicated eeepc sata patch.

* Wed Sep 24 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc7-git3

* Wed Sep 24 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc7-git2

* Tue Sep 23 2008 Dave Jones <davej@redhat.com>
- x86 compile fix.

* Tue Sep 23 2008 Dave Jones <davej@redhat.com>
- Merge Linux-2.6 up to commit fb478da5ba69ecf40729ae8ab37ca406b1e5be48

* Tue Sep 23 2008 Dave Jones <davej@redhat.com>
- Disable E1000E driver until bz 459202 is solved.

* Tue Sep 23 2008 Dave Airlie <airlied@redhat.com>
- fix radeon cursor disappearing bug.

* Tue Sep 23 2008 Dave Airlie <airlied@redhat.com>
- rebase drm patches with latest upstream GEM bits

* Mon Sep 22 2008 Jeremy Katz <katzj@redhat.com>
- Enable Geode framebuffer so that we can have a console on the XO

* Mon Sep 22 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.344.rc7
- Deblobbed 2.6.27-rc7.

* Mon Sep 22 2008 Roland McGrath <roland@redhat.com>
- 2.6.27-rc7-git1

* Mon Sep 22 2008 Kristian H??gsberg <krh@redhat.com>
- Fix check for radeon external TMDS.

* Mon Sep 22 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Mon Sep 22 2008 Dave Jones <davej@redhat.com>
- Readd CONFIG_IWLCORE that disappeared for no apparent reason.

* Mon Sep 22 2008 Dave Jones <davej@redhat.com>
- Commit Bills demodularisation patch.

* Mon Sep 22 2008 Kristian H??gsberg <krh@redhat.com>
- Add patch to allow userspace to get a handle for the buffer object
  backing the drm fbdev buffer.
- Update intel modesetting for the buffer object change.

* Mon Sep 22 2008 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.27-rc7

* Fri Sep 19 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc6-git6

* Fri Sep 19 2008 Dave Airlie <airlied@redhat.com>
- more fixes from AMD upstream for LVDS

* Thu Sep 18 2008 Dave Airlie <airlied@redhat.com>
- Merge krh's patches + new patches from AMD

* Thu Sep 18 2008 Kristian H??gsberg <krh@redhat.com>
- Fix precedence in PLL value computation.
- Allow R300_DST_PIPE_CONFIG register write use by X.
- Add DRM_ERROR() for EINVAL returns from DRM_RADEON_CS that will crash X.

* Thu Sep 18 2008 Dave Airlie <airlied@redhat.com>
- update radeon LVDS bits from AMD

* Wed Sep 17 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc6-git5

* Mon Sep 15 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc6-git4

* Mon Sep 15 2008 Dave Airlie <airlied@redhat.com>
- removed previous attempted fix

* Mon Sep 15 2008 Dave Airlie <airlied@redhat.com>
- properly fix issues with Intel interrupts

* Mon Sep 15 2008 Dave Airlie <airlied@redhat.com>
- fix from Intel for irqs hopefully

* Sat Sep 13 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc6-git3

* Sat Sep 13 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix hang in installer on PS3. (#458910)

* Fri Sep 12 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc6-git2

* Fri Sep 12 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.27-rc6-git1

* Thu Sep 11 2008 Dave Airlie <airlied@redhat.com>
- drm - fix some minor annoyance with radeon for beta

* Wed Sep 10 2008 Mark McLoughlin <markmc@redhat.com>
- Pull in new e1000e hardware support (e.g. ich10) from net-next-2.6

* Tue Sep 09 2008 Dave Airlie <airlied@redhat.com>
- Update radeon modesetting - memory setup + ref count fail

* Tue Sep 09 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.320.rc6
- Deblobbed 2.6.27-rc6.

* Tue Sep 09 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc6

* Tue Sep 09 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc5-git11

* Tue Sep 09 2008 Jarod Wilson <jarod@redhat.com>
- turn ibmveth back on for ppc64, it builds now (#461637)

* Tue Sep 09 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.317.rc5.git10
- Adjusted i915 Kconfig patch to compensate for deblobbing elsewhere.

* Tue Sep 09 2008 Dave Airlie <airlied@redhat.com>
- radeon - update modesetting bits - should fix r400
- add i915 modesetting bits - don't enable these by default yet

* Mon Sep 08 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc5-git10

* Mon Sep 08 2008 Jarod Wilson <jarod@redhat.com>
- Dave's lirc patch merged for upstream submission

* Mon Sep 08 2008 Dave Airlie <airlied@redhat.com>
- disable VGA bashing in radeon - make text reserve larger.

* Sun Sep 07 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.27-rc5-git9

* Sun Sep 07 2008 Dave Airlie <airlied@redhat.com>
- disable radeon verbose debugging. doh.

* Sat Sep 06 2008 Dave Airlie <airlied@redhat.com>
- fix lirc on powerpc64 - its Saturday goddamit.

* Sat Sep 06 2008 Dave Airlie <airlied@redhat.com>
- powerpc build broken disable FSL_UPM

* Fri Sep  5 2008 Roland McGrath <roland@redhat.com>
- 2.6.27-rc5-git7
- utrace kerneldoc fixups

* Fri Sep 05 2008 Dave Airlie <airlied@redhat.com>
- introduce radeon suspend/resume + change pin api

* Fri Sep 05 2008 Chuck Ebbert <cebbert@redhat.com>
- Restore most of the dropped powerpc32 drivers.

* Fri Sep 05 2008 Jarod Wilson <jarod@redhat.com>
- More lirc updates:
  * convert single-holder semaphores to mutexes
  * actually build lirc_sasem driver
  * assorted compile-time warning cleanups
  * add lirc_ite8709 driver
  * sync with latest lirc cvs

* Fri Sep 05 2008 Dave Airlie <airlied@redhat.com>
- modesetting updates - fix AMD rs690 - roll in krh dri2 patch

* Thu Sep  4 2008 David Woodhouse <David.Woodhouse@intel.com>
- 2.6.27-rc5-git6

* Wed Sep  3 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Wed Sep 03 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.27-rc5-git5

* Wed Sep 03 2008 Jarod Wilson <jarod@redhat.com>
- Another series of checkpatch cleanups to lirc code,
  courtesy of Janne Grunau. This stuff might actually
  finally get upstream soon...

* Tue Sep 02 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc5-git3

* Tue Sep 02 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix selinux memory leak (#460848)

* Tue Sep 02 2008 Jarod Wilson <jarod@redhat.com>
- Rename lirc_pvr150 to more appropriate lirc_zilog

* Tue Sep 02 2008 Kyle McMartin <kyle@redhat.com>
- hopefully fix the iwlwifi issues.
- add an include <linux/pagemap.h> to drm-nouveau to hopefully fix
  the build on powerpc64.

* Tue Sep 02 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.296.rc5.git2
- Deblobbed patch-2.6.27-rc5.
- Deblobbed drm-nouveau.patch.

* Tue Sep 02 2008 Dave Airlie <airlied@redhat.com>
- bring back nouveau yet again

* Mon Sep 01 2008 Dave Jones <davej@redhat.com>
- Always inline kzalloc.  And drop busted rcu debug patch

* Sat Aug 30 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc5-git2

* Fri Aug 29 2008 Chuck Ebbert <cebbert@redhat.com>
- x86: add Presario F700 to io_delay quirk list. (F9#459546)

* Fri Aug 29 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc5-git1

* Fri Aug 29 2008 Kristian H??gsberg <krh@redhat.com>
- Fix a couple of merge bugs between DRI2 and KMS.

* Thu Aug 28 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc5

* Thu Aug 28 2008 Dave Airlie <airlied@redhat.com>
- change boot option from text to nomodeset and make drivers debuggable later

* Thu Aug 28 2008 Dave Airlie <airlied@redhat.com>
- rebase i915 GEM support to avoid oopsing.

* Thu Aug 28 2008 Dave Airlie <airlied@redhat.com>
- hopefully fix r300 lvds

* Thu Aug 28 2008 Dave Airlie <airlied@redhat.com>
- rebase modesetting patches - add DRI2 for intel patches from krh
- no intel modesetting yet didn't have time

* Wed Aug 27 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc4-git7

* Wed Aug 27 2008 Dave Airlie <airlied@redhat.com>
- drm update - add opregion support - fix some bugs in radeon modesetting

* Tue Aug 26 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc4-git6

* Tue Aug 26 2008 Adam Jackson <ajax@redhat.com>
- Silence the IOMMU warnings, since pretty much no BIOS has the options to
  fix them anyway.

* Tue Aug 26 2008 Jarod Wilson <jarod@redhat.com>
- Temporarily turn on CONFIG_SYSFS_DEPRECATED{,_V2} for ia64

* Tue Aug 26 2008 Dave Airlie <airlied@redhat.com>
- radeon improved memory manager/modesetting fixups

* Mon Aug 25 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Mon Aug 25 2008 Dave Jones <davej@redhat.com>
- Merge Linux-2.6 up to commit b8e6c91c74e9f0279b7c51048779b3d62da60b88

* Mon Aug 25 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc4-git4 (Drop NR_CPUS on x86-64 to 512)

* Sun Aug 24 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc4-git3

* Sat Aug 23 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc4-git2

* Sat Aug 23 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Fri Aug 22 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc4-git1

* Thu Aug 21 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc4

* Wed Aug 20 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3-git7

* Wed Aug 20 2008 Roland McGrath <roland@redhat.com>
- utrace update, fix irqs-disabled lockdep warning on i386

* Wed Aug 20 2008 Dave Jones <davej@redhat.com>
- include/asm needs to be a symlink, not a dir.

* Tue Aug 19 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3-git6

* Mon Aug 18 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3-git5

* Sun Aug 17 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3-git4

* Fri Aug 15 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3-git3

* Fri Aug 15 2008 Dave Airlie <airlied@redhat.com>
- Add i915 sarea fixups

* Thu Aug 14 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.266.rc3.git2
- Deblobbed patch-2.6.27-rc3-git2.

* Thu Aug 14 2008 Dave Jones <davej@redhat.com>
- Bump max cpus supported on x86-64 to 4096. Just to see what happens.

* Thu Aug 14 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3-git2

* Thu Aug 14 2008 Matthew Garrett <mjg@redhat.com>
- Force keyup events on broken Dell hotkeys

* Thu Aug 14 2008 Dave Airlie <airlied@redhat.com>
- Update intel headers so DDX can build

* Thu Aug 14 2008 Dave Airlie <airlied@redhat.com>
- Update radeon modesetting code - include text cli option
- support r300 cards and up - could in theory support r100/r200
- reserve some memory for text mode on radeons

* Wed Aug 13 2008 Roland McGrath <roland@redhat.com>
- enable kernel-doc, include htmldocs
- utrace kerneldoc typo fixes

* Wed Aug 13 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3-git1

* Wed Aug 13 2008 Chuck Ebbert <cebbert@redhat.com>
- Enable CONFIG_MODULE_FORCE_UNLOAD for -debug kernels.

* Wed Aug 13 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.254.rc3
- Deblobbed patch-2.6.27-rc3.

* Tue Aug 12 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc3

* Tue Aug 12 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc2-git6

* Tue Aug 12 2008 Adam Jackson <ajax@redhat.com>
- Silence some more useless bootup spew in quiet mode.

* Mon Aug 11 2008 Adam Jackson <ajax@redhat.com>
- Require a mkinitrd that can handle built-in USB and requires plymouth.

* Mon Aug 11 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc2-git5

* Mon Aug 11 2008 Dave Jones <davej@redhat.com>
- Change USB controllers to be built-in instead of modular.

* Mon Aug 11 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Sat Aug 09 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc2-git4
- Temporarily disable utrace.

* Fri Aug 08 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc2-git3

* Fri Aug 08 2008 Jarod Wilson <jwilson@redhat.com>
- Rebase and re-enable firewire git tree patch

* Fri Aug  8 2008 Kristian H??gsberg <krh@redhat.com>
- Export module device table for radeon DRM driver.

* Fri Aug 08 2008 Adam Jackson <ajax@redhat.com>
- Silence the PIIX3 PCI quirk message in quiet boot.

* Fri Aug 08 2008 Dave Airlie <airlied@redhat.com>
- attempt to fix oops in drm_open

* Thu Aug 07 2008 Dave Jones <davej@redhat.com>
- include/asm needs to be called such, not asm-x86.

* Thu Aug 07 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc2-git1

* Thu Aug  7 2008 Roland McGrath <roland@redhat.com>
- utrace update
- Clean up %%prep old vanilla-* source purging.

* Thu Aug 07 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.237.rc2
- Deblobbed patch-2.6.27-rc2.

* Wed Aug 06 2008 Dave Jones <davej@redhat.com>
- Enable USB_PERSIST

* Wed Aug 06 2008 Dave Jones <davej@redhat.com>
- If include/asm exists, it's a symlink. Fix test. (#458201)

* Wed Aug 06 2008 Dave Jones <davej@redhat.com>
- Own all the modules.* files in /lib/modules. (#456857)

* Wed Aug 06 2008 Matthew Garrett <mjg@redhat.com>
- Fix reboot after resume on various AMI-bios machines

* Wed Aug 06 2008 Jarod Wilson <jwilson@redhat.com>
- Have kernel-%%{variant}-firmware Provides: kernel-firmware

* Wed Aug 06 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc2

* Wed Aug 06 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.231.rc1.git6
- Deblobbed patch-2.6.27-rc1-git6.

* Wed Aug 06 2008 Dave Airlie <airlied@redhat.com>
- fix modesetting introduced bugs on PCI radeon cards

* Tue Aug 05 2008 Chuck Ebbert <cebbert@redhat.com>
- Allow building firmware during arch build (--with firmware).

* Tue Aug 05 2008 Tom "spot" Callaway <tcallawa@redhat.com>
- fix license tag of kernel-firmware

* Tue Aug 05 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc1-git6

* Tue Aug 05 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.226.rc1.git5
- Deblobbed patch-2.6.27-rc1-git5.

* Tue Aug 05 2008 Dave Airlie <airlied@redhat.com>
- more drm regressions squashed

* Mon Aug 04 2008 Dave Jones <davej@redhat.com>
- Fix bogus printk in execshield noticed by Brendan Lynch.

* Mon Aug 04 2008 Dave Jones <davej@redhat.com>
- Merge Linux-2.6 up to commit 2e1e9212ed8c532c6b324de77d3cafef5d2bc846

* Mon Aug 04 2008 Matthew Garrett <mjg@redhat.com>
- disable ACPI video display switching by default

* Mon Aug 04 2008 Jarod Wilson <jwilson@redhat.com>
- add latest firewire goodies: actual iso timestamps ftw

* Mon Aug 04 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc1-git5

* Mon Aug 04 2008 Hans de Goede <j.w.r.degoede@hhs.nl>
- fix broken /usr/include/linux/videodev2.h (bz 457688)

* Mon Aug 04 2008 Matthew Garrett <mjg@redhat.com>
- update eeepc-laptop driver (#441454)

* Mon Aug 04 2008 Jarod Wilson <jwilson@redhat.com>
- lirc updates:
  * make it build again
  * add philips omaura transceiver support to mceusb2 driver
  * add lcd-based device support to imon driver

* Mon Aug 04 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.215.rc1.git4
- Deblobbed patch-2.6.27-rc1-git4.

* Mon Aug 04 2008 Dave Airlie <airlied@redhat.com>
- rebase modesetting patch + fixes

* Sat Aug 02 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc1-git4

* Sat Aug 02 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.211.rc1.git3
- Deblobbed patch-2.6.27-rc1-git3.

* Sat Aug 02 2008 Dave Airlie <airlied@redhat.com>
- Fix locking in drm patches

* Fri Aug 01 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Fri Aug 01 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc1-git3

* Fri Aug 01 2008 John W. Linville <linville@redhat.com>
- Revert at76_usb to version from before attempted mac80211 port

* Fri Aug 01 2008 Dave Airlie <airlied@redhat.com>
- Fix ppc build again with drm changes

* Fri Aug 01 2008 Dave Airlie <airlied@redhat.com>
- Fix ppc build with drm changes

* Fri Aug 01 2008 Dave Airlie <airlied@redhat.com>
- Add initial radeon kernel modesetting jumbo patch

* Thu Jul 31 2008 Dave Jones <davej@redhat.com>
- Fix PPC64 build.

* Thu Jul 31 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc1-git2

* Wed Jul 30 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc1-git1

* Wed Jul 30 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-07-29
  (http://marc.info/?l=linux-wireless&m=121737750023195&w=2)

* Wed Jul 30 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.201.rc1
- Deblobbed patch-2.6.27-rc1.

* Wed Jul 30 2008 Mark McLoughlin <markmc@redhat.com>
- Replace kernel-xen in DEFAULTKERNEL (#456558)

* Tue Jul 29 2008 Dave Jones <davej@redhat.com>
- Disable CONFIG_VIDEO_ADV_DEBUG (#456751)

* Tue Jul 29 2008 Dave Jones <davej@redhat.com>
- 2.6.27-rc1

* Mon Jul 28 2008 Josh Boyer <jwboyer@gmail.com>
- Add gzip requires for kernel-bootwrapper (#456947)

* Mon Jul 28 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git18

* Mon Jul 28 2008 Roland McGrath <roland@redhat.com>
- Disable hfcmulti driver on big-endian.
- 2.6.26-git17

* Sun Jul 27 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.191.rc0.git16
- Deblobbed patch-2.6.26-git16.

* Sun Jul 27 2008 Roland McGrath <roland@redhat.com>
- 2.6.26-git16
- Fix up linux-2.6-build-nonintconfig.patch after kconfig changes.
- Fix up .spec for headers check.

* Sun Jul 27 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.186.rc0.git15
- Deblobbed patch-2.6.26-git15.

* Sun Jul 27 2008 Roland McGrath <roland@redhat.com>
- 2.6.26-git15
- Disable powerpc64 ibmveth driver, not compiling.

* Sat Jul 26 2008 Dave Jones <davej@redhat.com>
- Enable CONFIG_VIDEO_ADV_DEBUG

* Sat Jul 26 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.183.rc0.git14
- Deblobbed patch-2.6.26-git14.

* Sat Jul 26 2008 Roland McGrath <roland@redhat.com>
- 2.6.26-git14
- Disable lirc patch, not building.

* Fri Jul 25 2008 Roland McGrath <roland@redhat.com>
- 2.6.26-git13
- utrace update

* Fri Jul 25 2008 Roland McGrath <roland@redhat.com>
- 2.6.26-git13

* Fri Jul 25 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git12

* Thu Jul 24 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.180.rc0.git11
- Deblobbed patch-2.6.26-git11.

* Thu Jul 24 2008 Mark McLoughlin <markmc@redhat.com>
- Enable Xen guest support in kernel-PAE.i686 and kernel.x86_64
- Obsolete kernel-xen
- Remove the hypervisor (xen.gz) - moved to xen-hypervisor package

* Thu Jul 24 2008 Roland McGrath <roland@redhat.com>
- Disable sfc module, not compiling.
- Disable kernel-doc package.

* Thu Jul 24 2008 Roland McGrath <roland@redhat.com>
- 2.6.26-git11
- kconfig updates for 2.6.26-git11
- utrace update

* Wed Jul 23 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git10

* Tue Jul 22 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git9

* Tue Jul 22 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.166.rc0.git8
- Deblobbed patch-2.6.26-git8.

* Mon Jul 21 2008 Dave Jones <davej@redhat.com>
- Merge Linux-2.6 up to commit 93ded9b8fd42abe2c3607097963d8de6ad9117eb

* Mon Jul 21 2008 Dave Jones <davej@redhat.com>
- Change yenta to modular instead of built-in. (#456173)

* Mon Jul 21 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git8

* Mon Jul 21 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git7

* Sun Jul 20 2008 Kyle McMartin <kmcmartin@redhat.com>
- Re-enable atl2, and add atl1e driver (found in eee901.)

* Sat Jul 19 2008 Chuck Ebbert <cebbert@redhat.com>
- Start building the kernel-doc package again.

* Sat Jul 19 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.159.rc0.git6.2
- Fix provides from pkgrelease to pkg_release.

* Fri Jul 18 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.159.rc0.git6.1
- Depend on kernel-libre-firmware.
- Provide kernel-firmware in kernel-libre-firmware.  Change its
license to GPLv2.

* Fri Jul 18 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.159.rc0.git6
- Deblobbed 2.6.26-git6.

* Thu Jul 17 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git6

* Thu Jul 17 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git5

* Thu Jul 17 2008 Kristian H??gsberg <krh@redhat.com>
- Add conflicts for older libdrm-devel to kernel-headers.

* Thu Jul 17 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.156.rc0.git4
- Deblobbed 2.6.26-git4.
- Re-enabled kernel-firmware.

* Wed Jul 16 2008 Dave Jones <davej@redhat.com>
- Merge Linux-2.6 up to commit 8a0ca91e1db5de5eb5b18cfa919d52ff8be375af

* Wed Jul 16 2008 Kristian H??gsberg <krh@redhat.com>
- Also copy new include/drm directory.

* Wed Jul 16 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git4

* Wed Jul 16 2008 Dave Jones <davej@redhat.com>
- Remove extraneous sparc64 config file.

* Wed Jul 16 2008 Dave Jones <davej@redhat.com>
- Remove sparc32 config files.

* Wed Jul 16 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git3

* Wed Jul 16 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.148.rc0.git2 Jul 17
- Rebased to 2.6.26-libre1.
- Deblobbed 2.6.26-git2.
- Temporarily disabled the kernel-firmware package, until I can figure
  out how to deblob this stuff in a way that it builds and doesn't require
  too much maintenance.  This may break the few devices that require Free
  firmware.

* Tue Jul 15 2008 Dave Jones <davej@redhat.com>
- Merge Linux-2.6 up to commit 45158894d4d6704afbb4cefe55e5f6ca279fe12a

* Tue Jul 15 2008 Dave Jones <davej@redhat.com>
- Further improvements to 8139 patchset.

* Tue Jul 15 2008 John W. Linville <linville@redhat.com>
- Upstream wireless updates from 2008-07-14
  (http://marc.info/?l=linux-wireless&m=121606436000705&w=2)

* Tue Jul 15 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git2

* Tue Jul 15 2008 Dave Jones <davej@redhat.com>
- 2.6.26-git1

* Mon Jul 14 2008 David Woodhouse <David.Woodhouse@intel.com>
- Add kernel-firmware subpackage

* Mon Jul 14 2008 Eric Sandeen <sandeen@redhat.com>
- Add pending ext4 patch queue; adds fiemap interface

* Mon Jul 14 2008 Roland McGrath <roland@redhat.com>
- Fix ia64 build nit, properly disable utrace for ia64.

* Mon Jul 14 2008 Dave Jones <davej@redhat.com>
- Improve 8139too PIO patch with jgarziks comments.

* Mon Jul 14 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Mon Jul 14 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.136
- Deblobbed 2.6.26

* Sun Jul 13 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.26

* Sun Jul 13 2008 Kyle McMartin <kmcmartin@redhat.com>
- Enable CONFIG_NETDEVICES_MULTIQUEUE (and CONFIG_MAC80211_QOS.)

* Sun Jul 13 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git12

* Sun Jul 13 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git11

* Sat Jul 12 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git10

* Fri Jul 11 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Fri Jul 11 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git9

* Fri Jul 11 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git8

* Thu Jul 10 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git7

* Thu Jul 10 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-07-09
  (http://marc.info/?l=linux-netdev&m=121563769208664&w=2)

* Thu Jul 10 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git6

* Wed Jul 09 2008 Chuck Ebbert <cebbert@redhat.com>
- Enable the i2c-tiny-usb driver. (#451451)

* Wed Jul 09 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.124.rc9.git5
- Deblobbed rtl8187b_reg_table in linux-2.6-wireless-pending.patch.

* Wed Jul 09 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git5

* Wed Jul 09 2008 Dave Jones <davej@redhat.com>
- Reenable paravirt on x86-64.

* Tue Jul  8 2008 Roland McGrath <roland@redhat.com>
- new bleeding-edge utrace code

* Tue Jul 08 2008 Eric Sandeen <sandeen@redhat.com>
- Fix reiserfs list corruption (#453699)

* Tue Jul 08 2008 John W. Linville <linville@redhat.com>
- Upstream wireless updates from 2008-07-08
  (http://marc.info/?l=linux-wireless&m=121554411325041&w=2)

* Tue Jul 08 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git4

* Tue Jul 08 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-07-07
  (http://marc.info/?l=linux-wireless&m=121546143025524&w=2)

* Tue Jul 08 2008 Kyle McMartin <kmcmartin@redhat.com>
- nuke linux-2.6-uvcvideo.patch, merged upstream

* Tue Jul 08 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git3

* Mon Jul 07 2008 Chuck Ebbert <cebbert@redhat.com>
- Skip building the kernel-doc package due to breakage somewhere in rawhide XML land.

* Mon Jul 07 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git2

* Mon Jul 07 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9-git1

* Sun Jul 06 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.113.rc9
- Deblobbed 2.6.26-rc9

* Sun Jul 06 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc9

* Sat Jul 05 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc8-git5

* Fri Jul 04 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc8-git4

* Thu Jul  3 2008 Kristian H??gsberg <krh@redhat.com>
- Add linux-2.6-export-shmem-bits-for-gem.patch to export
  shmmem_getpage and shmem_file_setup for GEM.  For now this will let
  use GEM from the drm tree against the system kernel, but eventually
  we'll pull in the GEM patches here.
- Drop account_system_vtime export patch, now upstream.

* Thu Jul 03 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-07-02
  (http://marc.info/?l=linux-netdev&m=121503163124089&w=2)

* Thu Jul 03 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc8-git3

* Thu Jul 03 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix EFI boot.
- Export account_system_vtime on IA64.

* Thu Jul  3 2008 Jeremy Katz <katzj@redhat.com>
- Disable the garmin_gps driver; programs are using libusb for this now
- Make padlock warnings quieter

* Wed Jul 02 2008 Chuck Ebbert <cebbert@redhat.com>
- Re-enable the snd-pcsp driver (#452748)

* Tue Jul 01 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-06-30
  (http://marc.info/?l=linux-wireless&m=121485709702728&w=2)
- Upstream wireless updates from 2008-06-30
  (http://marc.info/?l=linux-wireless&m=121486432315033&w=2)

* Tue Jul 01 2008 Dave Jones <davej@redhat.com>
- Shorten summary in specfile.

* Tue Jul 01 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc8-git2

* Mon Jun 30 2008 Chuck Ebbert <cebbert@redhat.com>
- Disable the selinux ecryptfs support patch; it broke ntfs-3g (#452438)

* Mon Jun 30 2008 Dave Jones <davej@redhat.com>
- Disable rio500 driver (bz 451567)

* Mon Jun 30 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc8-git1

* Fri Jun 27 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-06-27
  (http://marc.info/?l=linux-wireless&m=121459423021061&w=2)

* Fri Jun 27 2008 John W. Linville <linville@redhat.com>
- Upstream wireless updates from 2008-06-27
  (http://marc.info/?l=linux-wireless&m=121458164930953&w=2)

* Thu Jun 26 2008 Dave Jones <davej@redhat.com>
- Print out modules list when we hit soft lockup.

* Wed Jun 25 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.93.rc8
- Deblobbed 2.6.26-rc8

* Wed Jun 25 2008 John W. Linville <linville@redhat.com>
- Upstream wireless fixes from 2008-06-25
  (http://marc.info/?l=linux-wireless&m=121440912502527&w=2)

* Wed Jun 25 2008 Dave Jones <davej@redhat.com>
- Reenable a few ppc32 modules.

* Wed Jun 25 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc8

* Tue Jun 24 2008 Dave Jones <davej@redhat.com>
- Disable a bunch of modules in the ppc32 kernel.

* Tue Jun 24 2008 John W. Linville <linville@redhat.com>
- Upstream wireless updates from 2008-06-14
  (http://marc.info/?l=linux-netdev&m=121346686508160&w=2)

* Tue Jun 24 2008 John W. Linville <linville@redhat.com>
- Restore wireless patches disabled during recent updates

* Tue Jun 24 2008 Dave Jones <davej@redhat.com>
- Disable the RCU linked list debug routines for now.

* Tue Jun 24 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc7-git2

* Mon Jun 23 2008 Dave Jones <davej@redhat.com>
- Add debug variants of the RCU linked list routines.

* Mon Jun 23 2008 Dave Jones <davej@redhat.com>
- Build LIBATA & the SCSI bits non-modular.

* Mon Jun 23 2008 Dave Jones <davej@redhat.com>
- Change ACPI button driver to non-modular.

* Sun Jun 22 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc7-git1

* Sun Jun 22 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.81.rc7
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

* Thu May 29 2008 Kristian H??gsberg <krh@redhat.com>
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

* Tue May 27 2008 Kristian H??gsberg <krh@redhat.com>
- Drop a couple of stray references to CONFIG_DEBUG_IGNORE_QUIET.

* Tue May 27 2008 John W. Linville <linville@redhat.com>
- Upstream wireless updates from 2008-05-22
  (http://marc.info/?l=linux-wireless&m=121146112404515&w=2)

* Mon May 26 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.3.rc4
- Deblob 2.6.26-rc4.

* Mon May 26 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc4

* Mon May 26 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git8

* Sun May 25 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git7

* Fri May 23 2008 Dave Jones <davej@redhat.com>
- 2.6.26-rc3-git6

* Fri May 23 2008 Kristian H??gsberg <krh@redhat.com>
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

* Mon May 19 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.17.rc3
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

* Sat May 17 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.13.rc2.git5 May 18
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

* Thu May  8 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.5
- Rebase to linux-2.6.25-libre.tar.bz2.

* Wed May 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.2-5
- Add the patches queued for 2.6.25.3
