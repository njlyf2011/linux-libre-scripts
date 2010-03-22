# We have to override the new %%install behavior because, well... the kernel is special.
%global __spec_install_pre %{___build_pre}

Summary: The Linux kernel

# For a stable, released kernel, released_kernel should be 1. For rawhide
# and/or a kernel built from an rc or git snapshot, released_kernel should
# be 0.
%global released_kernel 0

# Versions of various parts

# Polite request for people who spin their own kernel rpms:
# please modify the "buildid" define in a way that identifies
# that the kernel isn't the stock distribution kernel, for example,
# by setting the define to ".local" or ".bz123456"
#
# % define buildid .local

# fedora_build defines which build revision of this kernel version we're
# building. Rather than incrementing forever, as with the prior versioning
# setup, we set fedora_cvs_origin to the current cvs revision s/1.// of the
# kernel spec when the kernel is rebased, so fedora_build automatically
# works out to the offset from the rebase, so it doesn't get too ginormous.
#
# If you're building on a branch, the RCS revision will be something like
# 1.1205.1.1.  In this case we drop the initial 1, subtract fedora_cvs_origin
# from the second number, and then append the rest of the RCS string as is.
# Don't stare at the awk too long, you'll go blind.
%define fedora_cvs_origin   1935
%define fedora_cvs_revision() %2
%global fedora_build %(echo %{fedora_cvs_origin}.%{fedora_cvs_revision $Revision: 1.1948 $} | awk -F . '{ OFS = "."; ORS = ""; print $3 - $1 ; i = 4 ; OFS = ""; while (i <= NF) { print ".", $i ; i++} }')

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 33

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
#define librev

# To be inserted between "patch" and "-2.6.".
#define stablelibre -libre
%define rcrevlibre -libre
#define gitrevlibre -libre

# libres (s for suffix) may be bumped for rebuilds in which patches
# change but fedora_build doesn't.  Make sure it starts with a dot.
# It is appended after dist.
#define libres .

## If this is a released kernel ##
%if 0%{?released_kernel}

# Do we have a -stable update to apply?
%define stable_update 0
# Is it a -stable RC?
%define stable_rc 0
# Set rpm version accordingly
%if 0%{?stable_update}
%define stablerev .%{stable_update}
%define stable_base %{stable_update}
%if 0%{?stable_rc}
# stable RCs are incremental patches, so we need the previous stable patch
%define stable_base %(echo $((%{stable_update} - 1)))
%endif
%endif
%define rpmversion 2.6.%{base_sublevel}%{?stablerev}

## The not-released-kernel case ##
%else
# The next upstream release sublevel (base_sublevel+1)
%define upstream_sublevel %(echo $((%{base_sublevel} + 1)))
# The rc snapshot level
%define rcrev 1
# The git snapshot level
%define gitrev 1
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
# kernel-smp (only valid for ppc 32-bit)
%define with_smp       %{?_without_smp:       0} %{?!_without_smp:       1}
# kernel-kdump
%define with_kdump     %{?_without_kdump:     0} %{?!_without_kdump:     1}
# kernel-debug
%define with_debug     %{?_without_debug:     0} %{?!_without_debug:     1}
# kernel-doc
%define with_doc       %{?_without_doc:       0} %{?!_without_doc:       1}
# kernel-headers
%define with_headers   %{?_without_headers:   0} %{?!_without_headers:   1}
# kernel-firmware
%define with_firmware  %{?_with_firmware:     1} %{?!_with_firmware:     0}
# tools/perf
%define with_perftool  %{?_without_perftool:  0} %{?!_without_perftool:  1}
# perf noarch subpkg
%define with_perf      %{?_without_perf:      0} %{?!_without_perf:      1}
# kernel-debuginfo
%define with_debuginfo %{?_without_debuginfo: 0} %{?!_without_debuginfo: 1}
# kernel-bootwrapper (for creating zImages from kernel + initrd)
%define with_bootwrapper %{?_without_bootwrapper: 0} %{?!_without_bootwrapper: 1}
# Want to build a the vsdo directories installed
%define with_vdso_install %{?_without_vdso_install: 0} %{?!_without_vdso_install: 1}
# Use dracut instead of mkinitrd for initrd image generation
%define with_dracut       %{?_without_dracut:       0} %{?!_without_dracut:       1}

# Build the kernel-doc package, but don't fail the build if it botches.
# Here "true" means "continue" and "false" means "fail the build".
%if 0%{?released_kernel}
%define doc_build_fail false
%else
%define doc_build_fail true
%endif

%define rawhide_skip_docs 1
%if 0%{?rawhide_skip_docs}
%define with_doc 0
%endif

# Additional options for user-friendly one-off kernel building:
#
# Only build the base kernel (--with baseonly):
%define with_baseonly  %{?_with_baseonly:     1} %{?!_with_baseonly:     0}
# Only build the smp kernel (--with smponly):
%define with_smponly   %{?_with_smponly:      1} %{?!_with_smponly:      0}
# Only build the debug kernel (--with dbgonly):
%define with_dbgonly   %{?_with_dbgonly:      1} %{?!_with_dbgonly:      0}

# should we do C=1 builds with sparse
%define with_sparse	%{?_with_sparse:      1} %{?!_with_sparse:      0}

# Set debugbuildsenabled to 1 for production (build separate debug kernels)
#  and 0 for rawhide (all kernels are debug kernels).
# See also 'make debug' and 'make release'.
%define debugbuildsenabled 0

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
%else
%define rctag .rc0
%endif
%if 0%{?gitrev}
%define gittag .git%gitrev
%else
%define gittag .git0
%endif
%define pkg_release 0.%{fedora_build}%{?rctag}%{?gittag}%{?buildid}%{?dist}%{?libres}

%endif

# The kernel tarball/base version
%define kversion 2.6.%{base_sublevel}

%define make_target bzImage

%define KVERREL %{version}-libre.%{release}.%{_target_cpu}
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

# kernel-PAE is only built on i686.
%ifarch i686
%define with_pae 1
%else
%define with_pae 0
%endif

# if requested, only build base kernel
%if %{with_baseonly}
%define with_smp 0
%define with_kdump 0
%define with_debug 0
%endif

# if requested, only build smp kernel
%if %{with_smponly}
%define with_up 0
%define with_kdump 0
%define with_debug 0
%endif

# if requested, only build debug kernel
%if %{with_dbgonly}
%if %{debugbuildsenabled}
%define with_up 0
%endif
%define with_smp 0
%define with_pae 0
%define with_xen 0
%define with_kdump 0
%define with_perftool 0
%endif

%define all_x86 i386 i686

%if %{with_vdso_install}
# These arches install vdso/ directories.
%define vdso_arches %{all_x86} x86_64 ppc ppc64
%endif

# Overrides for generic default options

# only ppc and alphav56 need separate smp kernels
%ifnarch ppc alphaev56
%define with_smp 0
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
%define with_perf 0
%endif

# don't build noarch kernels or headers (duh)
%ifarch noarch
%define with_up 0
%define with_headers 0
%define all_arch_configs kernel-%{version}-*.config
%define with_firmware  %{?_with_firmware:     1} %{?!_with_firmware:     0}
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
%define asmarch x86
%define hdrarch i386
%define all_arch_configs kernel-%{version}-i?86*.config
%define image_install_path boot
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch x86_64
%define asmarch x86
%define all_arch_configs kernel-%{version}-x86_64*.config
%define image_install_path boot
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch ppc64
%define asmarch powerpc
%define hdrarch powerpc
%define all_arch_configs kernel-%{version}-ppc64*.config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%define kernel_image_elf 1
%endif

%ifarch s390x
%define asmarch s390
%define hdrarch s390
%define all_arch_configs kernel-%{version}-s390x.config
%define image_install_path boot
%define make_target image
%define kernel_image arch/s390/boot/image
%endif

%ifarch sparc
# We only build sparc headers since we dont support sparc32 hardware
%endif

%ifarch sparc64
%define asmarch sparc
%define all_arch_configs kernel-%{version}-sparc64*.config
%define make_target image
%define kernel_image arch/sparc/boot/image
%define image_install_path boot
%define with_perftool 0
%endif

%ifarch ppc
%define asmarch powerpc
%define hdrarch powerpc
%define all_arch_configs kernel-%{version}-ppc{-,.}*config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%define kernel_image_elf 1
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

%ifarch %{arm}
%define all_arch_configs kernel-%{version}-arm*.config
%define image_install_path boot
%define hdrarch arm
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
# and we no longer build for 31bit S390. Same for 32bit sparc and arm.
%define nobuildarches i386 s390 sparc %{arm}

%ifarch %nobuildarches
%define with_up 0
%define with_smp 0
%define with_pae 0
%define with_kdump 0
%define with_debuginfo 0
%define with_perftool 0
%define _enable_debug_packages 0
%endif

%define with_pae_debug 0
%if %{with_pae}
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
%define package_conflicts initscripts < 7.23, udev < 063-6, iptables < 1.3.2-1, ipw2200-firmware < 2.4, iwl4965-firmware < 228.57.2, selinux-policy-targeted < 1.25.3-14, squashfs-tools < 4.0, wireless-tools < 29-3

#
# The ld.so.conf.d file we install uses syntax older ldconfig's don't grok.
#
%define kernel_xen_conflicts glibc < 2.3.5-1, xen < 3.0.1

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
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, grubby >= 7.0.10-1
%if %{with_dracut}
%define initrd_prereq  dracut >= 001-7
%else
%define initrd_prereq  mkinitrd >= 6.0.61-1
%endif

#
# This macro does requires, provides, conflicts, obsoletes for a kernel package.
#	%%kernel_reqprovconf <subpackage>
# It uses any kernel_<subpackage>_conflicts and kernel_<subpackage>_obsoletes
# macros defined above.
#
%define kernel_reqprovconf \
Provides: kernel = %{rpmversion}-%{pkg_release}\
Provides: kernel-libre = %{rpmversion}-%{pkg_release}\
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{pkg_release}%{?1:.%{1}}\
Provides: kernel-libre-%{_target_cpu} = %{rpmversion}-%{pkg_release}%{?1:.%{1}}\
Provides: kernel-drm = 4.3.0\
Provides: kernel-drm-nouveau = 16\
Provides: kernel-modeset = 1\
Provides: kernel-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-uname-r = %{KVERREL}%{?1:.%{1}}\
Requires(pre): %{kernel_prereq}\
Requires(pre): %{initrd_prereq}\
%if %{with_firmware}\
Requires(pre): kernel-libre-firmware >= %{rpmversion}-%{pkg_release}\
%else\
%if %{with_perftool}\
Requires(pre): libdwarf\
%endif\
%endif\
Requires(post): /sbin/new-kernel-pkg\
Requires(preun): /sbin/new-kernel-pkg\
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
URL: http://linux-libre.fsfla.org/
Version: %{rpmversion}
Release: %{pkg_release}
# DO NOT CHANGE THE 'ExclusiveArch' LINE TO TEMPORARILY EXCLUDE AN ARCHITECTURE BUILD.
# SET %%nobuildarches (ABOVE) INSTEAD
ExclusiveArch: noarch %{all_x86} x86_64 ppc ppc64 ia64 sparc sparc64 s390x alpha alphaev56 %{arm}
ExclusiveOS: Linux

%kernel_reqprovconf
%ifarch x86_64 sparc64
Obsoletes: kernel-smp
%endif


#
# List the packages used during the kernel build
#
BuildRequires: module-init-tools, patch >= 2.5.4, bash >= 2.03, sh-utils, tar
BuildRequires: bzip2, findutils, gzip, m4, perl, make >= 3.78, diffutils, gawk
BuildRequires: gcc >= 3.4.2, binutils >= 2.12, redhat-rpm-config
BuildRequires: net-tools
BuildRequires: xmlto, asciidoc
%if %{with_sparse}
BuildRequires: sparse >= 0.4.1
%endif
%if %{with_perftool}
BuildRequires: elfutils-libelf-devel zlib-devel binutils-devel libdwarf-devel
%endif
BuildConflicts: rhbuildsys(DiskFree) < 500Mb

%define fancy_debuginfo 0
%if %{with_debuginfo}
%if 0%{?fedora} >= 8 || 0%{?rhel} >= 6
%define fancy_debuginfo 1
%endif
%endif

%if %{fancy_debuginfo}
# Fancy new debuginfo generation introduced in Fedora 8.
BuildRequires: rpm-build >= 4.4.2.1-4
%define debuginfo_args --strict-build-id
%endif

Source0: http://linux-libre.fsfla.org/pub/linux-libre/freed-ora/src/linux-%{kversion}-libre%{?librev}.tar.bz2

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
Source31: config-i686-PAE

Source40: config-x86_64-generic

Source50: config-powerpc-generic
Source51: config-powerpc32-generic
Source52: config-powerpc32-smp
Source53: config-powerpc64

Source60: config-ia64-generic

Source70: config-s390x

Source90: config-sparc64-generic

Source100: config-arm

Source200: perf

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

Patch08: freedo.patch

%if !%{nopatches}

# revert upstream patches we get via other methods
Patch09: linux-2.6-upstream-reverts.patch
# Git trees.
Patch10: git-cpufreq.patch
Patch11: git-bluetooth.patch

# Standalone patches
Patch20: linux-2.6-hotfixes.patch

Patch21: linux-2.6-tracehook.patch
Patch22: linux-2.6-utrace.patch
Patch23: linux-2.6-utrace-ptrace.patch

Patch143: linux-2.6-g5-therm-shutdown.patch
Patch144: linux-2.6-vio-modalias.patch

Patch150: linux-2.6.29-sparc-IOC_TYPECHECK.patch

Patch160: linux-2.6-execshield.patch

Patch250: linux-2.6-debug-sizeof-structs.patch
Patch260: linux-2.6-debug-nmi-timeout.patch
Patch270: linux-2.6-debug-taint-vm.patch
Patch300: linux-2.6-driver-level-usb-autosuspend.diff
Patch303: linux-2.6-enable-btusb-autosuspend.patch
Patch304: linux-2.6-usb-uvc-autosuspend.diff
Patch305: linux-2.6-fix-btusb-autosuspend.patch

Patch340: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch360: linux-2.6-debug-always-inline-kzalloc.patch
Patch380: linux-2.6-defaults-pci_no_msi.patch
Patch381: linux-2.6-pciehp-update.patch
Patch382: linux-2.6-defaults-pciehp.patch
Patch383: linux-2.6-defaults-aspm.patch
Patch390: linux-2.6-defaults-acpi-video.patch
Patch391: linux-2.6-acpi-video-dos.patch
Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch451: linux-2.6-input-fix-toshiba-hotkeys.patch
Patch452: linux-2.6.30-no-pcspkr-modalias.patch

Patch460: linux-2.6-serial-460800.patch

Patch470: die-floppy-die.patch

Patch510: linux-2.6-silence-noise.patch
Patch520: linux-2.6.30-hush-rom-warning.patch
Patch530: linux-2.6-silence-fbcon-logo.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sparc-selinux-mprotect-checks.patch

Patch610: hda_intel-prealloc-4mb-dmabuffer.patch

Patch670: linux-2.6-ata-quirk.patch

Patch680: linux-2.6-rt2x00-asus-leds.patch
Patch681: linux-2.6-mac80211-age-scan-results-on-resume.patch

Patch700: linux-2.6.31-nx-data.patch
Patch701: linux-2.6.31-modules-ro-nx.patch

Patch800: linux-2.6-crash-driver.patch

Patch900: linux-2.6-cantiga-iommu-gfx.patch

Patch1515: lirc-2.6.33.patch
Patch1517: hdpvr-ir-enable.patch
Patch1520: crystalhd-2.6.34-staging.patch

# virt + ksm patches
Patch1551: linux-2.6-ksm-kvm.patch
Patch1552: linux-2.6-userspace_kvmclock_offset.patch
Patch1553: vhost_net-rollup.patch
Patch1554: virt_console-rollup.patch

# fbdev x86-64 primary fix
Patch1700: linux-2.6-x86-64-fbdev-primary.patch

# nouveau + drm fixes
Patch1815: drm_nouveau_ucode.patch
Patch1816: drm-nouveau-abi16.patch
Patch1819: drm-intel-big-hammer.patch
# intel drm is all merged upstream
Patch1824: drm-intel-next.patch

# linux1394 git patches
Patch2200: linux-2.6-firewire-git-update.patch
Patch2201: linux-2.6-firewire-git-pending.patch

# Quiet boot fixes
# silence the ACPI blacklist code
Patch2802: linux-2.6-silence-acpi-blacklist.patch

Patch2899: linux-2.6-v4l-dvb-fixes.patch
Patch2900: linux-2.6-v4l-dvb-update.patch
Patch2901: linux-2.6-v4l-dvb-experimental.patch
Patch2904: linux-2.6-v4l-dvb-rebase-gspca-to-latest.patch

# fs fixes

# NFSv4
Patch3051: linux-2.6-nfs4-callback-hidden.patch

# VIA Nano / VX8xx updates

# patches headed upstream
Patch12010: linux-2.6-dell-laptop-rfkill-fix.patch
Patch12011: linux-2.6-block-silently-error-unsupported-empty-barriers-too.patch
Patch12013: linux-2.6-rfkill-all.patch

Patch12015: add-appleir-usb-driver.patch

Patch12017: prevent-runtime-conntrack-changes.patch

Patch12018: neuter_intel_microcode_load.patch

Patch12019: linux-2.6-umh-refactor.patch
Patch12020: coredump-uid-pipe-check.patch

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

%package debuginfo-common-%{_target_cpu}
Summary: Kernel source files used by %{name}-debuginfo packages
Group: Development/Debug
%description debuginfo-common-%{_target_cpu}
This package is required by %{name}-debuginfo subpackages.
It provides the kernel source files common to all builds.

%package -n perf-libre
Provides: perf = %{rpmversion}-%{pkg_release}
Summary: Performance monitoring for the Linux kernel
Group: Development/System
License: GPLv2
%description -n perf-libre
This package provides the supporting documentation for the perf tool
shipped in each kernel image subpackage.

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
Provides: kernel-libre%{?1:-%{1}}-devel-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-devel-%{_target_cpu} = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-libre-devel-%{_target_cpu} = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-devel = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-libre-devel = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-devel-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-devel-uname-r = %{KVERREL}%{?1:.%{1}}\
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
%if !%{with_up}%{with_pae}
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

# more sanity checking; do it quietly
if [ "%{patches}" != "%%{patches}" ] ; then
  for patch in %{patches} ; do
    if [ ! -f $patch ] ; then
      echo "ERROR: Patch  ${patch##/*/}  listed in specfile but is missing"
      exit 1
    fi
  done
fi 2>/dev/null

patch_command='patch -p1 -F1 -s'
ApplyPatch()
{
  local patch=$1
  shift
  if [ ! -f $RPM_SOURCE_DIR/$patch ]; then
    exit 1
  fi
%if !%{using_upstream_branch}
  if ! egrep "^Patch[0-9]+: $patch\$" %{_specdir}/${RPM_PACKAGE_NAME%%%%%{?variant}}.spec ; then
    if [ "${patch:0:10}" != "patch-2.6." ] && 
       [ "${patch:0:16}" != "patch-libre-2.6." ] ; then
      echo "ERROR: Patch  $patch  not listed as a source patch in specfile"
      exit 1
    fi
  fi 2>/dev/null
  $RPM_SOURCE_DIR/deblob-check $RPM_SOURCE_DIR/$patch || exit 1
%endif
  case "$patch" in
  *.bz2) bunzip2 < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *.gz) gunzip < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *) $patch_command ${1+"$@"} < "$RPM_SOURCE_DIR/$patch" ;;
  esac
}

# don't apply patch if it's empty
ApplyOptionalPatch()
{
  local patch=$1
  shift
  if [ ! -f $RPM_SOURCE_DIR/$patch ]; then
    exit 1
  fi
  local C=$(wc -l $RPM_SOURCE_DIR/$patch | awk '{print $1}')
  if [ "$C" -gt 9 ]; then
    ApplyPatch $patch ${1+"$@"}
  fi
}

# we don't want a .config file when building firmware: it just confuses the build system
%define build_firmware \
   mv .config .config.firmware_save \
   make INSTALL_FW_PATH=$RPM_BUILD_ROOT/lib/firmware firmware_install \
   mv .config.firmware_save .config

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
%else
%define vanillaversion 2.6.%{base_sublevel}
%endif
%endif
%endif

# We can share hardlinked source trees by putting a list of
# directory names of the CVS checkouts that we want to share
# with in .shared-srctree. (Full pathnames are required.)
[ -f .shared-srctree ] && sharedirs=$(cat .shared-srctree)

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
    for sharedir in $sharedirs ; do
      if [[ ! -z $sharedir  &&  -d $sharedir/kernel-%{kversion}/vanilla-%{kversion} ]] ; then
        break
      fi
    done
    if [[ ! -z $sharedir  &&  -d $sharedir/kernel-%{kversion}/vanilla-%{kversion} ]] ; then
%setup -q -n kernel-%{kversion} -c -T
      cp -rl $sharedir/kernel-%{kversion}/vanilla-%{kversion} .
    else
%setup -q -n kernel-%{kversion} -c
      mv linux-%{kversion} vanilla-%{kversion}
    fi

  fi

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =/" vanilla-%{kversion}/Makefile

%if "%{kversion}" != "%{vanillaversion}"

  for sharedir in $sharedirs ; do
    if [[ ! -z $sharedir  &&  -d $sharedir/kernel-%{kversion}/vanilla-%{vanillaversion} ]] ; then
      break
    fi
  done
  if [[ ! -z $sharedir  &&  -d $sharedir/kernel-%{kversion}/vanilla-%{vanillaversion} ]] ; then

    cp -rl $sharedir/kernel-%{kversion}/vanilla-%{vanillaversion} .

  else

    cp -rl vanilla-%{kversion} vanilla-%{vanillaversion}
    cd vanilla-%{vanillaversion}

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

  fi

%endif

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

ApplyOptionalPatch git-linus.diff

# This patch adds a "make nonint_oldconfig" which is non-interactive and
# also gives a list of missing options at the end. Useful for automated
# builds (as used in the buildsystem).
ApplyPatch linux-2.6-build-nonintconfig.patch

ApplyPatch linux-2.6-makefile-after_link.patch

#
# misc small stuff to make things compile
#
ApplyOptionalPatch linux-2.6-compile-fixes.patch

# Freedo logo.
ApplyPatch freedo.patch

%if !%{nopatches}

# revert patches from upstream that conflict or that we get via other means
ApplyOptionalPatch linux-2.6-upstream-reverts.patch -R

#ApplyPatch git-cpufreq.patch
#ApplyPatch git-bluetooth.patch

ApplyPatch linux-2.6-hotfixes.patch

# Roland's utrace ptrace replacement.
ApplyPatch linux-2.6-tracehook.patch
ApplyPatch linux-2.6-utrace.patch
#ApplyPatch linux-2.6-utrace-ptrace.patch

# Architecture patches
# x86(-64)
#ApplyPatch linux-2.6-dell-laptop-rfkill-fix.patch

#
# Intel IOMMU
#

#
# PowerPC
#
### NOT (YET) UPSTREAM:
# Alleviate G5 thermal shutdown problems
ApplyPatch linux-2.6-g5-therm-shutdown.patch
# Provide modalias in sysfs for vio devices
ApplyPatch linux-2.6-vio-modalias.patch

#
# SPARC64
#
ApplyPatch linux-2.6.29-sparc-IOC_TYPECHECK.patch

#
# Exec shield
#
#ApplyPatch linux-2.6-execshield.patch

#
# bugfixes to drivers and filesystems
#

# ext4

# xfs

# btrfs

# eCryptfs

# NFSv4
ApplyPatch linux-2.6-nfs4-callback-hidden.patch

# USB
#ApplyPatch linux-2.6-driver-level-usb-autosuspend.diff
#ApplyPatch linux-2.6-enable-btusb-autosuspend.patch
#ApplyPatch linux-2.6-usb-uvc-autosuspend.diff
#ApplyPatch linux-2.6-fix-btusb-autosuspend.patch

# WMI

# ACPI
ApplyPatch linux-2.6-defaults-acpi-video.patch
ApplyPatch linux-2.6-acpi-video-dos.patch

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
#ApplyPatch linux-2.6-debug-nmi-timeout.patch
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-vm-would-have-oomkilled.patch
ApplyPatch linux-2.6-debug-always-inline-kzalloc.patch

#
# PCI
#
# disable message signaled interrupts
#ApplyPatch linux-2.6-defaults-pci_no_msi.patch
# update the pciehp driver
#ApplyPatch linux-2.6-pciehp-update.patch
# default to enabling passively listening for hotplug events
#ApplyPatch linux-2.6-defaults-pciehp.patch
# enable ASPM by default on hardware we expect to work
ApplyPatch linux-2.6-defaults-aspm.patch

#
# SCSI Bits.
#

# ALSA
#ApplyPatch hda_intel-prealloc-4mb-dmabuffer.patch

# Networking

# Misc fixes
# The input layer spews crap no-one cares about.
#ApplyPatch linux-2.6-input-kill-stupid-messages.patch

# stop floppy.ko from autoloading during udev...
ApplyPatch die-floppy-die.patch

# Get away from having to poll Toshibas
#ApplyPatch linux-2.6-input-fix-toshiba-hotkeys.patch

ApplyPatch linux-2.6.30-no-pcspkr-modalias.patch

# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch

# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch
ApplyPatch linux-2.6.30-hush-rom-warning.patch

# Make fbcon not show the penguins with 'quiet'
ApplyPatch linux-2.6-silence-fbcon-logo.patch

# Fix the SELinux mprotect checks on executable mappings
#ApplyPatch linux-2.6-selinux-mprotect-checks.patch
# Fix SELinux for sparc
#ApplyPatch linux-2.6-sparc-selinux-mprotect-checks.patch

# Changes to upstream defaults.


# ia64 ata quirk
ApplyPatch linux-2.6-ata-quirk.patch

# rt2x00: back-port activity LED init patches
#ApplyPatch linux-2.6-rt2x00-asus-leds.patch

# back-port scan result aging patches
#ApplyPatch linux-2.6-mac80211-age-scan-results-on-resume.patch

# Mark kernel data as NX
#ApplyPatch linux-2.6.31-nx-data.patch
# Apply NX/RO to modules
#ApplyPatch linux-2.6.31-modules-ro-nx.patch

# /dev/crash driver.
#ApplyPatch linux-2.6-crash-driver.patch

# Cantiga chipset b0rkage
ApplyPatch linux-2.6-cantiga-iommu-gfx.patch

# http://www.lirc.org/
ApplyPatch lirc-2.6.33.patch
# enable IR receiver on Hauppauge HD PVR (v4l-dvb merge pending)
ApplyPatch hdpvr-ir-enable.patch
# Broadcom Crystal HD video decoder
#ApplyPatch crystalhd-2.6.34-staging.patch

# Add kernel KSM support
# Optimize KVM for KSM support
#ApplyPatch linux-2.6-ksm-kvm.patch

# Assorted Virt Fixes
#ApplyPatch linux-2.6-userspace_kvmclock_offset.patch
#ApplyPatch vhost_net-rollup.patch
#ApplyPatch virt_console-rollup.patch

# Fix block I/O errors in KVM
#ApplyPatch linux-2.6-block-silently-error-unsupported-empty-barriers-too.patch

# fix x86-64 fbdev primary GPU selection
#ApplyPatch linux-2.6-x86-64-fbdev-primary.patch

# Nouveau DRM + drm fixes
# squash nouveau firmware into a single commit until it gets into linux-firmware
ApplyPatch drm_nouveau_ucode.patch
#ApplyPatch drm-nouveau-abi16.patch
# pm broken on my thinkpad t60p - airlied
ApplyPatch drm-intel-big-hammer.patch
ApplyOptionalPatch drm-intel-next.patch

# linux1394 git patches
#ApplyPatch linux-2.6-firewire-git-update.patch
#ApplyOptionalPatch linux-2.6-firewire-git-pending.patch

# silence the ACPI blacklist code
ApplyPatch linux-2.6-silence-acpi-blacklist.patch

# V4L/DVB updates/fixes/experimental drivers
#  apply if non-empty
ApplyOptionalPatch linux-2.6-v4l-dvb-fixes.patch
ApplyOptionalPatch linux-2.6-v4l-dvb-update.patch
ApplyOptionalPatch linux-2.6-v4l-dvb-experimental.patch

#ApplyPatch linux-2.6-v4l-dvb-rebase-gspca-to-latest.patch

# Patches headed upstream
ApplyPatch linux-2.6-rfkill-all.patch

#ApplyPatch add-appleir-usb-driver.patch

ApplyPatch neuter_intel_microcode_load.patch

# Refactor UserModeHelper code & satisfy abrt recursion check request
#ApplyPatch linux-2.6-umh-refactor.patch
#ApplyPatch coredump-uid-pipe-check.patch

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
'sh -xc "/usr/lib/rpm/debugedit -b $$RPM_BUILD_DIR -d /usr/src/debug \
    				-i $@ > $@.id"'
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

%if %{with_perftool}
    pushd tools/perf
# make sure the scripts are executable... won't be in tarball until 2.6.31 :/
    chmod +x util/generate-cmdlist.sh util/PERF-VERSION-GEN
    make -s V=1 %{?_smp_mflags} perf
    mkdir -p $RPM_BUILD_ROOT/usr/libexec/
    install -m 755 perf $RPM_BUILD_ROOT/usr/libexec/perf.$KernelVer
    popd
%endif

    # Start installing the results
%if %{with_debuginfo}
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/boot
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/%{image_install_path}
%endif
    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}
    install -m 644 .config $RPM_BUILD_ROOT/boot/config-$KernelVer
    install -m 644 System.map $RPM_BUILD_ROOT/boot/System.map-$KernelVer
%if %{with_dracut}
    # We estimate the size of the initramfs because rpm needs to take this size
    # into consideration when performing disk space calculations. (See bz #530778)
    dd if=/dev/zero of=$RPM_BUILD_ROOT/boot/initramfs-$KernelVer.img bs=1M count=20
%else
    dd if=/dev/zero of=$RPM_BUILD_ROOT/boot/initrd-$KernelVer.img bs=1M count=5
%endif
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
    if [ -d arch/%{asmarch}/include ]; then
      cp -a --parents arch/%{asmarch}/include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
    cp -a include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include

    # Make sure the Makefile and version.h have a matching timestamp so that
    # external modules can be built
    touch -r $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/Makefile $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/linux/version.h
    touch -r $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/.config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/linux/autoconf.h
    # Copy .config to include/config/auto.conf so "make prepare" is unnecessary.
    cp $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/.config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/config/auto.conf

    if test -s vmlinux.id; then
      cp vmlinux.id $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/vmlinux.id
    else
      echo >&2 "*** WARNING *** no vmlinux build ID! ***"
    fi

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
    			 'ata_scsi_ioctl|scsi_add_host|scsi_add_host_with_dma|blk_init_queue|register_mtd_blktrans|scsi_esp_register|scsi_register_device_handler'
    collect_modules_list drm \
    			 'drm_open|drm_init'
    collect_modules_list modesetting \
    			 'drm_crtc_init'

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
    for i in alias alias.bin ccwmap dep dep.bin ieee1394map inputmap isapnpmap ofmap pcimap seriomap symbols symbols.bin usbmap
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
%endif

%if %{with_pae_debug}
BuildKernel %make_target %kernel_image PAEdebug
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

%if %{with_perf}
pushd tools/perf
make %{?_smp_mflags} man || %{doc_build_fail}
popd
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
%files -f debugfiles.list debuginfo-common-%{_target_cpu}
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
%endif # with_doc

# perf docs
%if %{with_perf}
mandir=$RPM_BUILD_ROOT%{_datadir}/man
man1dir=$mandir/man1
pushd tools/perf/Documentation
make install-man mandir=$mandir
popd

pushd $man1dir
for d in *.1; do
 gzip $d;
done
popd
%endif # with_perf

# perf shell wrapper
%if %{with_perf}
mkdir -p $RPM_BUILD_ROOT/usr/sbin/
cp $RPM_SOURCE_DIR/perf $RPM_BUILD_ROOT/usr/sbin/perf
chmod 0755 $RPM_BUILD_ROOT/usr/sbin/perf
mkdir -p $RPM_BUILD_ROOT%{_datadir}/doc/perf
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
%{build_firmware}
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
%{expand:\
%if %{with_dracut}\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --mkinitrd --dracut --depmod --update %{KVERREL}%{?-v:.%{-v*}} || exit $?\
%else\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --mkinitrd --depmod --update %{KVERREL}%{?-v:.%{-v*}} || exit $?\
%endif}\
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
  /bin/sed -r -i -e 's/^DEFAULTKERNEL=%{-r*}$/DEFAULTKERNEL=kernel-libre%{?-v:-%{-v*}}/' /etc/sysconfig/kernel || exit $?\
fi}\
%{expand:\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --install %{KVERREL}%{?-v:.%{-v*}} || exit $?\
}\
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
%kernel_variant_post -v PAE -r (kernel|kernel-smp|kernel-xen)

%kernel_variant_preun debug
%kernel_variant_post -v debug

%kernel_variant_post -v PAEdebug -r (kernel|kernel-smp|kernel-xen)
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

%if %{with_perf}
%files -n perf-libre
%defattr(-,root,root)
%{_datadir}/doc/perf
/usr/sbin/perf
%{_datadir}/man/man1/*
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
%if %{with_perftool}\
/usr/libexec/perf.%{KVERREL}%{?2:.%{2}}\
%endif\
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
%if %{with_dracut}\
%ghost /boot/initramfs-%{KVERREL}%{?2:.%{2}}.img\
%else\
%ghost /boot/initrd-%{KVERREL}%{?2:.%{2}}.img\
%endif\
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

# plz don't put in a version string unless you're going to tag
# and build.

#  ___________________________________________________________ 
# / This branch is for Fedora 14. You probably want to commit \
# \ to the F-13 branch instead, or in addition to this one.   /
#  ----------------------------------------------------------- 
#         \   ^__^
#          \  (@@)\_______
#             (__)\       )\/\
#                 ||----w |
#                 ||     ||

%changelog
* Sun Mar 21 2010 Alexandre Oliva <lxoliva@fsfla.org>
- Deblobbed patch-libre-2.6.34-rc1.

* Fri Mar 19 2010 David Woodhouse <David.Woodhouse@intel.com>
- Apply fix for #538163 again (Cantiga shadow GTT chipset b0rkage).

* Thu Mar 18 2010 Neil Horman <nhorman@redhat.com>
- Disable TIPC in config

* Thu Mar 11 2010 Kyle McMartin <kyle@redhat.com> 2.6.34.0.11-rc1.git1
- 2.6.34-rc1-git1

* Mon Mar 08 2010 Kyle McMartin <kyle@redhat.com> 2.6.34.0.10-rc1.git0
- 2.6.34-rc1
- Flipped a bunch of stuff that went tristate => bool off, a bunch of
  MFD devices mostly. If any of it is actually useful, give us a holler.

* Sun Mar 07 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-0.8.rc0.git13
- 2.6.33-git13

* Sat Mar 06 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-0.8.rc0.git11
- Disable usb_autosuspend for now. (Needs rebasing.)

* Sat Mar 06 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-0.7.rc0.git11
- 2.6.33-git11
- config changes:
 - debug flavours:
  - PROVE_RCU
  - PM_ADVANCED_DEBUG
  - DMADEVICES_DEBUG (and VDEBUG)
 - arm:
  - PERF_EVENTS enabled
 - sparc:
  - GRETH module
 - generic:
  - IP_VS_PROTO_SCTP=y
  - BRIDGE_IGMP_SNOOPING=y
  - NETFILTER_XT_TARGET_CT=m
  - NF_CONNTRACK_ZONES=y
  - MACVTAP=m
  - KSZ884X_PCI=m
  - IXGBEVF=m
  - QLCNIC=m
  - LIBERTAS_MESH=y
  - RT2800USB, RT2800PCI added devices enabled
  - CAN_PLX_PCI=m (?)
  - VGA_ARB_MAX_GPUS=16
  - various DVB/VIDEO enables
  - OPTPROBES=y
  - REGULATOR framework disabled.
 - x86:
  - VGA_SWITCHEROO enabled.

* Sat Mar 06 2010 Kyle McMartin <kyle@redhat.com>
- Add requires on libdwarf if with_perftool.

* Mon Mar 01 2010 Dave Jones <davej@redhat.com>
- Don't own /usr/src/kernels any more, it's now owned by filesystem. (#569438)

* Fri Feb 26 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-0.4.rc0.git2
- Start of the 2.6.34 series in rawhide.
- Trim changelog.

* Wed Feb 24 2010 Chuck Ebbert <cebbert@redhat.com>
- Remove obsolete config options with scripts/sort-config -nfi
  (generated .configs are unchanged)

* Wed Feb 24 2010 Chuck Ebbert <cebbert@redhat.com>
- Drop/clear obsolete V4L patches, use ApplyOptionalPatch

* Wed Feb 24 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Make that 2.6.33-libre.

* Wed Feb 24 2010 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.33

* Wed Feb 24 2010 Dave Jones <davej@redhat.com>
- Remove unnecessary redefinition of KEY_RFKILL from linux-2.6-rfkill-all.patch

* Mon Feb 22 2010 Dave Jones <davej@redhat.com> 2.6.33-0.51.rc8.git6
- 2.6.33-rc8-git6

* Mon Feb 22 2010 Neil Horman <nhorman@redhat.com>
- Fix a problem with coredump path checking uids on files when coredump
  target is a pipe.

* Sun Feb 21 2010 Hans de Goede <hdegoede@redhat.com>
- Rebase gspca usb webcam driver + sub drivers to latest upstream, this
  adds support for the following webcam bridge chipsets: benq, cpia1, sn9c2028;
  and support for new devices and many bugfixes in other gspca-subdrivers

* Fri Feb 19 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.33-0.48.rc8.git1
- nouveau: update to new interface version

* Wed Feb 17 2010 Chuck Ebbert <cebbert@redhat.com>
- This branch is now for the F-14 release.

* Tue Feb 16 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.46.rc8.git1
- 2.6.33-rc8-git1
- virt_console-rollup.patch: fixes from linux-next from Amit.

* Mon Feb 15 2010 Neil Horman <nhorman@redhat.com>
- Refactor usermodehelper code and change recursion check for abrt
  with linux-2.6-umh-refactor.patch from -mm
  fixes bz 557386

* Sat Feb 13 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Deblobbed patch-libre-2.6.33-rc8.

* Fri Feb 12 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.33-0.44.rc8
- 2.6.33-rc8

* Fri Feb 12 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.33-0.43.rc7.git6
- 2.6.33-rc7-git6

* Thu Feb 11 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.33-0.42.rc7.git5
- 2.6.33-rc7-git5
- Drop merged patches:
  fix-conntrack-bug-with-namespaces.patch
  commit ad60a9154887bb6162e427b0969fefd2f34e94a6 from git-bluetooth.patch

* Mon Feb 08 2010 Josh Boyer <jwboyer@gmail.com>
- Drop ppc ps3_storage and imac-transparent bridge patches

* Sat Feb 06 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.40.rc7.git0
- Add libdwarf-devel to build deps so perf gets linked to it.

* Sat Feb 06 2010 Kyle McMartin <kyle@redhat.com>
- virt_console-rollup.patch, for feature F13/VirtioSerial, patches
  are all targetted at 2.6.34 (and in linux-next.)

* Sat Feb 06 2010 Kyle McMartin <kyle@redhat.com>
- git-bluetooth.patch: selection of backports from next for hadess.
  (rhbz#562245)

* Sat Feb 06 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.36.rc7.git0
- Linux 2.6.33-rc7 (oops, jumped the gun on -git6 I guess. :)

* Sat Feb 06 2010 Kyle McMartin <kyle@redhat.com>
- 2.6.33-rc6-git6

* Sat Feb 06 2010 Kyle McMartin <kyle@redhat.com>
- Hack around delay loading microcode.ko, on intel, we don't split out
  the firmware into cpuid specific versions (in fact, I don't know who does...)
  so just patch out the request_firmware calls in microcode_intel.c, and
  microcode_ctl.init will do the right thing. (fixes rhbz#560031)
  (side note: I'll fix microcode_ctl to do one better at some point.)

* Sat Feb 06 2010 Kyle McMartin <kyle@redhat.com>
- Don't want linux-firmware if %with_firmware, yet. (Think F-11/F-12 2.6.33)

* Fri Feb 05 2010 Peter Jones <pjones@redhat.com>
- Move initrd creation to %%posttrans
  Resolves: rhbz#557922

* Fri Feb 05 2010 Kyle McMartin <kyle@redhat.com>
- If %with_firmware, continue with kernel-firmware, otherwise prereq on the
  separate linux-firmware pkg. Thanks to dzickus for noticing.

* Thu Feb 04 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.29.rc6.git4
- 2.6.33-rc6-git4

* Wed Feb 03 2010 Kyle McMartin <kyle@redhat.com>
- prevent-runtime-conntrack-changes.patch: fix another conntrack issue
  identified by jcm.

* Wed Feb 03 2010 Kyle McMartin <kyle@redhat.com>
- fix-conntrack-bug-with-namespaces.patch: Patch for issue identified
  by jcm. (Ref: http://lkml.org/lkml/2010/2/3/112)

* Mon Feb 02 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.33-0.26.rc6.git1
- 2.6.33-rc6-git1

* Mon Feb  2 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre Thu Feb  4
- Use 100% gnu+freedo boot splash logo, with black background.
- Deblobbed patch-libre-2.6.33-rc6.
- Adjust lirc-2.6.33.patch.
- Deblobbed drm_nouveau_ucode.patch.

* Fri Jan 29 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.33-0.25.rc6.git0
- 2.6.33-rc6

* Wed Jan 27 2010 Roland McGrath <roland@redhat.com> 2.6.33-0.24.rc5.git1
- Fix include/ copying for kernel-devel.

* Mon Jan 25 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.23.rc5.git1
- 2.6.33-rc5-git1
- arm: MTD_PISMO is not set

* Mon Jan 25 2010 Dave Jones <davej@redhat.com>
- Disable CONFIG_X86_CPU_DEBUG

* Mon Jan 25 2010 Josh Boyer <jwboyer@gmail.com>
- Turn off CONFIG_USB_FHCI_HCD.  It doesn't build

* Fri Jan 22 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.20.rc5.git0
- 2.6.33-rc5

* Thu Jan 21 2010 Jarod Wilson <jarod@redhat.com>
- Merge crystalhd powerpc build fix from airlied

* Wed Jan 20 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.18.rc4.git7
- 2.6.32-rc4-git7
- dvb mantis drivers as modules

* Wed Jan 20 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.17.rc4.git6
- add appleir usb driver

* Mon Jan 18 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.16.rc4.git6
- 2.6.33-rc4-git6
- execshield: rebase for mm_types.h reject

* Mon Jan 18 2010 Kyle McMartin <kyle@redhat.com>
- vhost_net-rollup.patch: https://fedoraproject.org/wiki/Features/VHostNet
  from davem/net-next-2.6.git

* Sat Jan 16 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.14.rc4.git3
- DEBUG_STRICT_USER_COPY_CHECKS off for now, tickles issue in lirc_it87.c

* Sat Jan 16 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.13.rc4.git3
- 2.6.33-rc4-git3

* Thu Jan 14 2010 Steve Dickson <steved@redhat.com>
- Enabled the NFS4.1 (CONFIG_NFS_V4_1) kernel config

* Wed Jan 13 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.11.rc4
- Linux 2.6.33-rc4

* Wed Jan 13 2010 Kyle McMartin <kyle@redhat.com> 2.6.33-0.10.rc3.git5
- 2.6.33-rc3-git5

* Wed Jan 13 2010 Dave Airlie <airlied@redhat.com>
- Add fbdev fix for multi-card primary console on x86-64
- clean up all the drm- patches

* Tue Jan 12 2010 Jarod Wilson <jarod@redhat.com>
- Update lirc patch for 2.6.33 kfifo changes
- Add Broadcom Crystal HD video decoder driver from staging

* Mon Jan 11 2010 Kyle McMartin <kyle@redhat.com>
- include/asm is gone, kludge it for now.

* Mon Jan 11 2010 Dave Jones <davej@redhat.com>
- Rebase exec-shield.

* Mon Jan 11 2010 Kyle McMartin <kyle@redhat.com>
- drop e1000 patch.

* Mon Jan 11 2010 Kyle McMartin <kyle@redhat.com>
- lirc broken due to kfifo mess.

* Mon Jan 11 2010 Kyle McMartin <kyle@redhat.com>
- drm-intel-big-hammer: fix IS_I855 macro.

* Mon Jan 11 2010 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.33-rc3
- utrace: rebased from roland's people page.
- via-hwmon-temp-sensor.patch: upstream.
- linux-2.6-defaults-alsa-hda-beep-off.patch: new config option supercedes.
- readd nouveau ctxprogs as firmware/ like it should be.
- linux-2.6-pci-cacheline-sizing.patch: upstream.
- linux-2.6-intel-agp-clear-gtt.patch: upstream.
- linux-2.6-nfsd4-proots.patch: upstream?
- rebased the rest.

* Mon Jan 11 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Use gnu+freedo boot splash logo.

* Mon Jan 11 2010 Kyle McMartin <kyle@redhat.com> 2.6.32.3-21
- Linux 2.6.32.3
- drm-intel-no-tv-hotplug.patch: re-add lost patch from F-12
  2.6.31 (#522611, #544671)

* Mon Jan 11 2010 Kyle McMartin <kyle@redhat.com> 2.6.32.2-20
- Re-enable ATM_HE (#545289)

* Fri Jan 08 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.32.2-19
- Add another symbol to look for when generating modules.block

* Thu Jan 07 2010 David Woodhouse <David.Woodhouse@intel.com> 2.6.32.2-18
- Drop kernel-firmware package now that it's packaged separately.

* Mon Jan 04 2010 Dave Jones <davej@redhat.com>
- Drop some of the vm/spinlock taint patches. dump_stack() already does same.

* Thu Dec 24 2009 Kyle McMartin <kyle@redhat.com> 2.6.32.2-15
- Add patch from dri-devel to fix vblanks on r600.
  [http://marc.info/?l=dri-devel&m=126137027403059&w=2]

* Fri Dec 18 2009 Kyle McMartin <kyle@redhat.com> 2.6.32.2-14
- Linux 2.6.32.2
- dropped upstream patches.

* Fri Dec 18 2009 Roland McGrath <roland@redhat.com> - 2.6.32.1-13
- minor utrace update

* Thu Dec 17 2009 Matthew Garrett <mjg@redhat.com> 2.6.32.1-12
- linux-2.6-driver-level-usb-autosuspend.diff: fix so it works properly...
- linux-2.6-fix-btusb-autosuspend.patch: avoid bluetooth connection drops
- linux-2.6-enable-btusb-autosuspend.patch: and default it to on
- linux-2.6-autoload-wmi.patch: autoload WMI drivers

* Thu Dec 17 2009 Jarod Wilson <jarod@redhat.com> 2.6.32.1-11
- Split off onboard decode imon devices into pure input driver,
  leaving lirc_imon for the ancient imon devices only
- Fix NULL ptr deref in lirc_serial (#543886)
- Assorted lirc_mceusb fixups suggested by Mauro
- Dropped compat ioctls from lirc_dev, main ioctls should now be
  compatible between 32-bit and 64-bit (also at Mauro's suggestion)

* Wed Dec 16 2009 Roland McGrath <roland@redhat.com> 2.6.32.1-10
- utrace update, now testing the utrace-based ptrace!

* Tue Dec 15 2009 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Added freedo.patch, with 100% Free Software Freedo logo.

* Mon Dec 14 2009 Kyle McMartin <kyle@redhat.com> 2.6.32.1-9
- 2.6.32.1
- ext4 patches and more...

* Wed Dec 09 2009 Kyle McMartin <kyle@redhat.com> 2.6.32-8
- Add a patch off lkml from krh to fix perf when DEBUG_PERF_USE_VMALLOC
  (rhbz#542791)
- Re-enable CONFIG_DEBUG_PERF_USE_VMALLOC on debug kernels.

* Wed Dec 09 2009 Kyle McMartin <kyle@redhat.com> 2.6.32-7
- ext4-fix-insufficient-checks-in-EXT4_IOC_MOVE_EXT.patch: CVE-2009-4131
  fix insufficient permission checking which could result in arbitrary
  data corruption by a local unprivileged user.

* Tue Dec 08 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.32-6
- Copy fix for #540580 from F-12.

* Tue Dec 08 2009 Kyle McMartin <kyle@redhat.com> 2.6.32-5
- new rpm changes:
 - %{PACKAGE_VERSION} -> %{version}
 - %{PACKAGE_RELEASE} -> %{release}

* Tue Dec 08 2009 Kyle McMartin <kyle@redhat.com> 2.6.32-4
- Disable CONFIG_DEBUG_PERF_USE_VMALLOC for now, causes issues
  on x86_64. (rhbz#542791)

* Mon Dec  7 2009 Justin M. Forbes <jforbes@redhat.com> 2.6.32-3
- Allow userspace to adjust kvmclock offset (#530389)

* Mon Dec  7 2009 Steve Dickson <steved@redhat.com> 2.6.32-2
- Updated the NFS4 pseudo root code to the latest release.

* Thu Dec 03 2009 Alexandre Oliva <lxoliva@fsfla.org> -libre Tue Dec 08
- Deblobbed 2.6.32
- Rename subpackage perf to perf-libre; add provides.

* Thu Dec 03 2009 Kyle McMartin <kyle@redhat.com> 2.6.32-1
- Linux 2.6.32

###
# The following Emacs magic makes C-c C-e use UTC dates.
# Local Variables:
# rpm-change-log-uses-utc: t
# End:
###
