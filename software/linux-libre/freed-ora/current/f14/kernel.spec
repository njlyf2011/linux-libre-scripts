# We have to override the new %%install behavior because, well... the kernel is special.
%global __spec_install_pre %{___build_pre}

Summary: The Linux kernel

# For a stable, released kernel, released_kernel should be 1. For rawhide
# and/or a kernel built from an rc or git snapshot, released_kernel should
# be 0.
%global released_kernel 1

# Save original buildid for later if it's defined
%if 0%{?buildid:1}
%global orig_buildid %{buildid}
%undefine buildid
%endif

###################################################################
# Polite request for people who spin their own kernel rpms:
# please modify the "buildid" define in a way that identifies
# that the kernel isn't the stock distribution kernel, for example,
# by setting the define to ".local" or ".bz123456". This will be
# appended to the full kernel version.
#
# (Uncomment the '#' and both spaces below to set the buildid.)
#
# % define buildid .local
###################################################################

# The buildid can also be specified on the rpmbuild command line
# by adding --define="buildid .whatever". If both the specfile and
# the environment define a buildid they will be concatenated together.
%if 0%{?orig_buildid:1}
%if 0%{?buildid:1}
%global srpm_buildid %{buildid}
%define buildid %{srpm_buildid}%{orig_buildid}
%else
%define buildid %{orig_buildid}
%endif
%endif

# baserelease defines which build revision of this kernel version we're
# building.  We used to call this fedora_build, but the magical name
# baserelease is matched by the rpmdev-bumpspec tool, which you should use.
#
# We used to have some extra magic weirdness to bump this automatically,
# but now we don't.  Just use: rpmdev-bumpspec -c 'comment for changelog'
# When changing base_sublevel below or going from rc to a final kernel,
# reset this by hand to 1 (or to 0 and then use rpmdev-bumpspec).
# scripts/rebase.sh should be made to do that for you, actually.
#
%global baserelease 44
%global fedora_build %{baserelease}

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 35

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
%define librev 2

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
%define stable_update 6
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
# kernel-smp (only valid for ppc 32-bit)
%define with_smp       %{?_without_smp:       0} %{?!_without_smp:       1}
# kernel-debug
%define with_debug     %{?_without_debug:     0} %{?!_without_debug:     1}
# kernel-doc
%define with_doc       %{?_without_doc:       0} %{?!_without_doc:       1}
# kernel-headers
%define with_headers   %{?_without_headers:   0} %{?!_without_headers:   1}
# kernel-firmware
%define with_firmware  %{?_with_firmware:     1} %{?!_with_firmware:     0}
# tools/perf
%define with_perf      %{?_without_perf:      0} %{?!_without_perf:      1}
# kernel-debuginfo
%define with_debuginfo %{?_without_debuginfo: 0} %{?!_without_debuginfo: 1}
# kernel-bootwrapper (for creating zImages from kernel + initrd)
%define with_bootwrapper %{?_without_bootwrapper: 0} %{?!_without_bootwrapper: 1}
# Want to build a the vsdo directories installed
%define with_vdso_install %{?_without_vdso_install: 0} %{?!_without_vdso_install: 1}

# Build the kernel-doc package, but don't fail the build if it botches.
# Here "true" means "continue" and "false" means "fail the build".
%if 0%{?released_kernel}
%define doc_build_fail false
%else
%define doc_build_fail true
%endif

%define rawhide_skip_docs 0
%if 0%{?rawhide_skip_docs}
%define with_doc 0
%define doc_build_fail true
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
%define with_debug 0
%endif

# if requested, only build smp kernel
%if %{with_smponly}
%define with_up 0
%define with_debug 0
%endif

# if requested, only build debug kernel
%if %{with_dbgonly}
%if %{debugbuildsenabled}
%define with_up 0
%endif
%define with_smp 0
%define with_pae 0
%define with_perf 0
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

# don't do debug builds on anything but i686 and x86_64
%ifnarch i686 x86_64
%define with_debug 0
%endif

# only package docs noarch
%ifnarch noarch
%define with_doc 0
%endif

# don't build noarch kernels or headers (duh)
%ifarch noarch
%define with_up 0
%define with_headers 0
%define with_perf 0
%define all_arch_configs kernel-%{version}-*.config
%define with_firmware  %{?_without_firmware:     0} %{?!_without_firmware:     1}
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

%ifarch sparcv9
%define hdrarch sparc
%endif

%ifarch sparc64
%define asmarch sparc
%define all_arch_configs kernel-%{version}-sparc64*.config
%define make_target image
%define kernel_image arch/sparc/boot/image
%define image_install_path boot
%define with_perf 0
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

# We only build kernel-headers on the following...
%define nobuildarches i386 s390 sparc sparcv9 %{arm}

%ifarch %nobuildarches
%define with_up 0
%define with_smp 0
%define with_pae 0
%define with_debuginfo 0
%define with_perf 0
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

# We moved the drm include files into kernel-headers, make sure there's
# a recent enough libdrm-devel on the system that doesn't have those.
%define kernel_headers_conflicts libdrm-devel < 2.4.0-0.15

#
# Packages that need to be installed before the kernel is, because the %post
# scripts use them.
#
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, grubby >= 7.0.10-1
%define initrd_prereq  dracut >= 001-7

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
ExclusiveArch: noarch %{all_x86} x86_64 ppc ppc64 ia64 sparc sparcv9 sparc64 s390 s390x alpha alphaev56 %{arm}
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
# python-devel and perl(ExtUtils::Embed) are required for perf scripting
%if %{with_perf}
BuildRequires: elfutils-devel zlib-devel binutils-devel python-devel perl(ExtUtils::Embed)
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

# Standalone patches
Patch20: linux-2.6-hotfixes.patch

Patch30: git-utrace.patch
Patch31: utrace-ptrace-fix-build.patch
Patch32: utrace-remove-use-of-kref_set.patch

Patch150: linux-2.6.29-sparc-IOC_TYPECHECK.patch

Patch160: linux-2.6-32bit-mmap-exec-randomization.patch
Patch161: linux-2.6-i386-nx-emulation.patch

Patch200: linux-2.6-debug-sizeof-structs.patch
Patch201: linux-2.6-debug-nmi-timeout.patch
Patch202: linux-2.6-debug-taint-vm.patch
Patch203: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch204: linux-2.6-debug-always-inline-kzalloc.patch

Patch300: create-sys-fs-cgroup-to-mount-cgroupfs-on.patch

Patch360: disable-xhci-by-default.patch

Patch380: linux-2.6-defaults-pci_no_msi.patch
Patch381: linux-2.6-defaults-pci_use_crs.patch
Patch382: linux-2.6-defaults-no-pm-async.patch
Patch383: linux-2.6-defaults-aspm.patch
Patch384: pci-acpi-disable-aspm-if-no-osc.patch
Patch385: pci-aspm-dont-enable-too-early.patch

Patch390: linux-2.6-defaults-acpi-video.patch
Patch391: linux-2.6-acpi-video-dos.patch
Patch393: acpi-ec-add-delay-before-write.patch
Patch394: linux-2.6-acpi-debug-infinite-loop.patch

Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch452: linux-2.6.30-no-pcspkr-modalias.patch
Patch454: thinkpad-acpi-fix-backlight.patch

Patch460: linux-2.6-serial-460800.patch

Patch470: die-floppy-die.patch

Patch510: linux-2.6-silence-noise.patch
Patch530: linux-2.6-silence-fbcon-logo.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sparc-selinux-mprotect-checks.patch

Patch610: hda_intel-prealloc-4mb-dmabuffer.patch

Patch700: linux-2.6-e1000-ich9-montevina.patch

Patch800: linux-2.6-crash-driver.patch

# crypto/

# virt + ksm patches
Patch1555: fix_xen_guest_on_old_EC2.patch

# DRM
Patch1801: drm-revert-drm-fbdev-rework-output-polling-to-be-back-in-core.patch
Patch1802: revert-drm-kms-toggle-poll-around-switcheroo.patch
# drm fixes nouveau depends on
Patch1805: drm-simplify-i2c-config.patch
Patch1806: drm-sil164-module.patch
Patch1807: drm-i2c-ch7006-fix.patch
Patch1808: drm-ttm-fix.patch
# nouveau + drm fixes
Patch1810: drm-nouveau-updates.patch
Patch1811: drm-nouveau-race-fix.patch
Patch1812: drm-nouveau-nva3-noaccel.patch
Patch1819: drm-intel-big-hammer.patch
# intel drm is all merged upstream
Patch1824: drm-intel-next.patch
# make sure the lvds comes back on lid open
Patch1825: drm-intel-make-lvds-work.patch
Patch1900: linux-2.6-intel-iommu-igfx.patch

Patch2000: efifb-add-more-models.patch
Patch2001: efifb-check-that-the-base-addr-is-plausible-on-pci-systems.patch

# linux1394 git patches
Patch2200: linux-2.6-firewire-git-update.patch
Patch2201: linux-2.6-firewire-git-pending.patch

# Quiet boot fixes
# silence the ACPI blacklist code
Patch2802: linux-2.6-silence-acpi-blacklist.patch

# media patches
Patch2899: linux-2.6-v4l-dvb-fixes.patch
Patch2900: linux-2.6-v4l-dvb-update.patch
Patch2901: linux-2.6-v4l-dvb-experimental.patch
Patch2902: linux-2.6-v4l-dvb-uvcvideo-update.patch

Patch2910: linux-2.6-v4l-dvb-add-lgdt3304-support.patch
Patch2911: linux-2.6-v4l-dvb-add-kworld-a340-support.patch
Patch2912: linux-2.6-v4l-dvb-ir-core-update.patch
Patch2913: linux-2.6-v4l-dvb-ir-core-memleak-fixes.patch
Patch2914: linux-2.6-v4l-dvb-ir-core-streamzap.patch

Patch2915: lirc-staging-2.6.36.patch
#Patch2916: lirc-staging-2.6.36-fixes.patch
Patch2917: hdpvr-ir-enable.patch
Patch2918: linux-2.6-v4l-dvb-ir-core-update-2.patch
Patch2919: linux-2.6-v4l-dvb-ir-core-update-3.patch
Patch2920: linux-2.6-lirc-ioctl-compat-fixups.patch

Patch2950: linux-2.6-via-velocity-dma-fix.patch

Patch3000: linux-2.6-rcu-sched-warning.patch
Patch3010: linux-2.6-rcu-netpoll.patch

# NFSv4

# patches headed upstream

Patch12016: disable-i8042-check-on-apple-mac.patch

Patch12017: prevent-runtime-conntrack-changes.patch

Patch12018: neuter_intel_microcode_load.patch

Patch12019: add-appleir-usb-driver.patch

Patch12020: hid-support-tivo-slide-remote.patch

Patch12040: only-use-alpha2-regulatory-information-from-country-IE.patch

Patch12080: kprobes-x86-fix-kprobes-to-skip-prefixes-correctly.patch

# rhbz #622149
Patch12085: fix-rcu_deref_check-warning.patch
Patch12086: linux-2.6-cgroups-rcu.patch

Patch12517: flexcop-fix-xlate_proc_name-warning.patch

# mitigate DOS attack with large argument lists
Patch12520: execve-improve-interactivity-with-large-arguments.patch
Patch12521: execve-make-responsive-to-sigkill-with-large-arguments.patch
Patch12522: setup_arg_pages-diagnose-excessive-argument-size.patch

Patch12565: sched-05-avoid-side-effect-of-tickless-idle-on-update_cpu_load.patch
Patch12570: sched-10-change-nohz-idle-load-balancing-logic-to-push-model.patch
Patch12575: sched-15-update-rq-clock-for-nohz-balanced-cpus.patch
Patch12580: sched-20-fix-rq-clock-synchronization-when-migrating-tasks.patch
Patch12585: sched-25-move-sched_avg_update-to-update_cpu_load.patch
Patch12590: sched-30-sched-fix-nohz-balance-kick.patch
Patch12595: sched-35-increment-cache_nice_tries-only-on-periodic-lb.patch

Patch13600: btusb-macbookpro-6-2.patch
Patch13601: btusb-macbookpro-7-1.patch

Patch13610: libata-it821x-dump-stack-on-cache-flush.patch
Patch13620: xen-fix-typo-in-xen-irq-fix.patch

Patch13630: dm-allow-setting-of-uuid-via-rename-if-not-already-set.patch

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

%if %{with_perf}
%package -n perf-libre
Provides: perf = %{rpmversion}-%{pkg_release}
Summary: Performance monitoring for the Linux kernel
Group: Development/System
License: GPLv2
%description -n perf-libre
This package provides the perf tool and the supporting documentation.
%endif


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
Requires: perl\
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

# %{vanillaversion} : the full version name, e.g. 2.6.35-rc6-git3
# %{kversion}       : the base version, e.g. 2.6.34

# Use kernel-%{kversion}%{?dist} as the top-level directory name
# so we can prep different trees within a single git directory.

# Build a list of the other top-level kernel tree directories.
# This will be used to hardlink identical vanilla subdirs.
sharedirs=$(find "$PWD" -maxdepth 1 -type d -name 'kernel-2.6.*' \
            | grep -x -v "$PWD"/kernel-%{kversion}%{?dist}) ||:

if [ ! -d kernel-%{kversion}%{?dist}/vanilla-%{vanillaversion} ]; then

  if [ -d kernel-%{kversion}%{?dist}/vanilla-%{kversion} ]; then

    # The base vanilla version already exists.
    cd kernel-%{kversion}%{?dist}

    # Any vanilla-* directories other than the base one are stale.
    for dir in vanilla-*; do
      [ "$dir" = vanilla-%{kversion} ] || rm -rf $dir &
    done

  else

    rm -f pax_global_header
    # Look for an identical base vanilla dir that can be hardlinked.
    for sharedir in $sharedirs ; do
      if [[ ! -z $sharedir  &&  -d $sharedir/vanilla-%{kversion} ]] ; then
        break
      fi
    done
    if [[ ! -z $sharedir  &&  -d $sharedir/vanilla-%{kversion} ]] ; then
%setup -q -n kernel-%{kversion}%{?dist} -c -T
      cp -rl $sharedir/vanilla-%{kversion} .
    else
%setup -q -n kernel-%{kversion}%{?dist} -c
      mv linux-%{kversion} vanilla-%{kversion}
    fi

  fi

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =/" vanilla-%{kversion}/Makefile

%if "%{kversion}" != "%{vanillaversion}"

  for sharedir in $sharedirs ; do
    if [[ ! -z $sharedir  &&  -d $sharedir/vanilla-%{vanillaversion} ]] ; then
      break
    fi
  done
  if [[ ! -z $sharedir  &&  -d $sharedir/vanilla-%{vanillaversion} ]] ; then

    cp -rl $sharedir/vanilla-%{vanillaversion} .

  else

    # Need to apply patches to the base vanilla version.
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

  # We already have all vanilla dirs, just change to the top-level directory.
  cd kernel-%{kversion}%{?dist}

fi

# Now build the fedora kernel tree.
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

ApplyPatch linux-2.6-hotfixes.patch

# Roland's utrace ptrace replacement.
ApplyPatch git-utrace.patch
ApplyPatch utrace-ptrace-fix-build.patch
ApplyPatch utrace-remove-use-of-kref_set.patch

# Architecture patches
# x86(-64)

#
# Intel IOMMU
#

#
# PowerPC
#

#
# SPARC64
#
ApplyPatch linux-2.6.29-sparc-IOC_TYPECHECK.patch

#
# Exec shield
#
ApplyPatch linux-2.6-i386-nx-emulation.patch
ApplyPatch linux-2.6-32bit-mmap-exec-randomization.patch

#
# bugfixes to drivers and filesystems
#

# ext4

# xfs

# btrfs


# eCryptfs

# NFSv4

# USB
ApplyPatch disable-xhci-by-default.patch

# WMI

# ACPI
ApplyPatch linux-2.6-defaults-acpi-video.patch
ApplyPatch linux-2.6-acpi-video-dos.patch
ApplyPatch acpi-ec-add-delay-before-write.patch
ApplyPatch linux-2.6-acpi-debug-infinite-loop.patch
ApplyPatch linux-2.6-defaults-no-pm-async.patch

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
ApplyPatch linux-2.6-debug-nmi-timeout.patch
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-vm-would-have-oomkilled.patch
ApplyPatch linux-2.6-debug-always-inline-kzalloc.patch

#
# PCI
#
# make default state of PCI MSI a config option
ApplyPatch linux-2.6-defaults-pci_no_msi.patch
ApplyPatch linux-2.6-defaults-pci_use_crs.patch
# enable ASPM by default on hardware we expect to work
ApplyPatch linux-2.6-defaults-aspm.patch
# disable aspm if acpi doesn't provide an _OSC method
ApplyPatch pci-acpi-disable-aspm-if-no-osc.patch
# allow drivers to disable aspm at load time
ApplyPatch pci-aspm-dont-enable-too-early.patch

#
# SCSI Bits.
#

# ACPI

# ALSA
ApplyPatch hda_intel-prealloc-4mb-dmabuffer.patch

# Networking

# Misc fixes
# The input layer spews crap no-one cares about.
ApplyPatch linux-2.6-input-kill-stupid-messages.patch

# stop floppy.ko from autoloading during udev...
ApplyPatch die-floppy-die.patch

ApplyPatch linux-2.6.30-no-pcspkr-modalias.patch

#ApplyPatch thinkpad-acpi-fix-backlight.patch

# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch

# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch

# Make fbcon not show the penguins with 'quiet'
ApplyPatch linux-2.6-silence-fbcon-logo.patch

# Fix the SELinux mprotect checks on executable mappings
#ApplyPatch linux-2.6-selinux-mprotect-checks.patch
# Fix SELinux for sparc
# FIXME: Can we drop this now? See updated linux-2.6-selinux-mprotect-checks.patch
#ApplyPatch linux-2.6-sparc-selinux-mprotect-checks.patch

# Changes to upstream defaults.
ApplyPatch create-sys-fs-cgroup-to-mount-cgroupfs-on.patch


# /dev/crash driver.
ApplyPatch linux-2.6-crash-driver.patch

# Hack e1000e to work on Montevina SDV
ApplyPatch linux-2.6-e1000-ich9-montevina.patch

# crypto/

# Assorted Virt Fixes
ApplyPatch fix_xen_guest_on_old_EC2.patch

#ApplyPatch drm-revert-drm-fbdev-rework-output-polling-to-be-back-in-core.patch
#ApplyPatch revert-drm-kms-toggle-poll-around-switcheroo.patch

ApplyPatch drm-simplify-i2c-config.patch
ApplyPatch drm-sil164-module.patch
ApplyPatch drm-i2c-ch7006-fix.patch
ApplyPatch drm-ttm-fix.patch
# Nouveau DRM + drm fixes
ApplyPatch drm-nouveau-updates.patch
ApplyPatch drm-nouveau-race-fix.patch
ApplyPatch drm-nouveau-nva3-noaccel.patch

ApplyPatch drm-intel-big-hammer.patch
ApplyOptionalPatch drm-intel-next.patch
ApplyPatch drm-intel-make-lvds-work.patch
ApplyPatch linux-2.6-intel-iommu-igfx.patch

ApplyPatch efifb-add-more-models.patch
ApplyPatch efifb-check-that-the-base-addr-is-plausible-on-pci-systems.patch

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
ApplyPatch linux-2.6-v4l-dvb-uvcvideo-update.patch

ApplyPatch linux-2.6-v4l-dvb-ir-core-update.patch
ApplyPatch linux-2.6-v4l-dvb-ir-core-memleak-fixes.patch
ApplyPatch linux-2.6-v4l-dvb-ir-core-streamzap.patch
ApplyPatch linux-2.6-v4l-dvb-add-lgdt3304-support.patch
ApplyPatch linux-2.6-v4l-dvb-add-kworld-a340-support.patch

# http://www.lirc.org/
ApplyPatch lirc-staging-2.6.36.patch
#ApplyOptionalPatch lirc-staging-2.6.36-fixes.patch
# enable IR receiver on Hauppauge HD PVR (v4l-dvb merge pending)
ApplyPatch hdpvr-ir-enable.patch
ApplyPatch linux-2.6-v4l-dvb-ir-core-update-2.patch
ApplyPatch linux-2.6-v4l-dvb-ir-core-update-3.patch
ApplyPatch linux-2.6-lirc-ioctl-compat-fixups.patch

# Fix DMA bug on via-velocity
ApplyPatch linux-2.6-via-velocity-dma-fix.patch

# silence another rcu_reference warning
ApplyPatch linux-2.6-rcu-sched-warning.patch
ApplyPatch linux-2.6-rcu-netpoll.patch

# Patches headed upstream
ApplyPatch disable-i8042-check-on-apple-mac.patch

ApplyPatch add-appleir-usb-driver.patch

ApplyPatch hid-support-tivo-slide-remote.patch

ApplyPatch neuter_intel_microcode_load.patch

ApplyPatch only-use-alpha2-regulatory-information-from-country-IE.patch

# bz 610941
ApplyPatch kprobes-x86-fix-kprobes-to-skip-prefixes-correctly.patch

# bz 622149
ApplyPatch fix-rcu_deref_check-warning.patch
ApplyPatch linux-2.6-cgroups-rcu.patch

# bz #575873
ApplyPatch flexcop-fix-xlate_proc_name-warning.patch

# mitigate DOS attack with large argument lists
ApplyPatch execve-improve-interactivity-with-large-arguments.patch
ApplyPatch execve-make-responsive-to-sigkill-with-large-arguments.patch
ApplyPatch setup_arg_pages-diagnose-excessive-argument-size.patch

# Scheduler fixes (#635813 and #633037)
ApplyPatch sched-05-avoid-side-effect-of-tickless-idle-on-update_cpu_load.patch
ApplyPatch sched-10-change-nohz-idle-load-balancing-logic-to-push-model.patch
ApplyPatch sched-15-update-rq-clock-for-nohz-balanced-cpus.patch
ApplyPatch sched-20-fix-rq-clock-synchronization-when-migrating-tasks.patch
ApplyPatch sched-25-move-sched_avg_update-to-update_cpu_load.patch
ApplyPatch sched-30-sched-fix-nohz-balance-kick.patch
ApplyPatch sched-35-increment-cache_nice_tries-only-on-periodic-lb.patch

ApplyPatch btusb-macbookpro-7-1.patch
ApplyPatch btusb-macbookpro-6-2.patch

# temporary patch, dump stack on failed it821x commands
ApplyPatch libata-it821x-dump-stack-on-cache-flush.patch

# temporary fix for typo in Xen -stable patch
ApplyPatch xen-fix-typo-in-xen-irq-fix.patch


# rhbz#641476
ApplyPatch dm-allow-setting-of-uuid-via-rename-if-not-already-set.patch

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

touch .scmversion

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

    # Start installing the results
%if %{with_debuginfo}
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/boot
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/%{image_install_path}
%endif
    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}
    install -m 644 .config $RPM_BUILD_ROOT/boot/config-$KernelVer
    install -m 644 System.map $RPM_BUILD_ROOT/boot/System.map-$KernelVer

    # We estimate the size of the initramfs because rpm needs to take this size
    # into consideration when performing disk space calculations. (See bz #530778)
    dd if=/dev/zero of=$RPM_BUILD_ROOT/boot/initramfs-$KernelVer.img bs=1M count=20

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
    			 'register_netdev|ieee80211_register_hw|usbnet_probe|phy_driver_register'
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
mkdir -p $RPM_BUILD_ROOT%{_libexecdir}

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

%global perf_make \
  make %{?_smp_mflags} -C tools/perf -s V=1 NO_DEMANGLE=1 prefix=%{_prefix}
%if %{with_perf}
%{perf_make} all
%{perf_make} man || %{doc_build_fail}
%endif

%if %{with_doc}
# Make the HTML and man pages.
make htmldocs mandocs || %{doc_build_fail}

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

%if %{with_perf}
# perf tool binary and supporting scripts/binaries
%{perf_make} DESTDIR=$RPM_BUILD_ROOT install

# perf man pages (note: implicit rpm magic compresses them later)
%{perf_make} DESTDIR=$RPM_BUILD_ROOT install-man || %{doc_build_fail}
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
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --mkinitrd --dracut --depmod --update %{KVERREL}%{?-v:.%{-v*}} || exit $?\
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
%{nil}

#
# This macro defines a %%preun script for a kernel package.
#	%%kernel_variant_preun <subpackage>
#
%define kernel_variant_preun() \
%{expand:%%preun %{?1}}\
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}%{?1:.%{1}} || exit $?\
%{nil}

%kernel_variant_preun
%kernel_variant_post -r kernel-smp

%kernel_variant_preun smp
%kernel_variant_post -v smp

%kernel_variant_preun PAE
%kernel_variant_post -v PAE -r (kernel|kernel-smp)

%kernel_variant_preun debug
%kernel_variant_post -v debug

%kernel_variant_post -v PAEdebug -r (kernel|kernel-smp)
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
%{_bindir}/perf
%dir %{_libexecdir}/perf-core
%{_libexecdir}/perf-core/*
%{_mandir}/man[1-8]/*
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
/boot/config-%{KVERREL}%{?2:.%{2}}\
%dir /lib/modules/%{KVERREL}%{?2:.%{2}}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/kernel\
/lib/modules/%{KVERREL}%{?2:.%{2}}/build\
/lib/modules/%{KVERREL}%{?2:.%{2}}/source\
/lib/modules/%{KVERREL}%{?2:.%{2}}/extra\
/lib/modules/%{KVERREL}%{?2:.%{2}}/updates\
%ifarch %{vdso_arches}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/vdso\
/etc/ld.so.conf.d/kernel-%{KVERREL}%{?2:.%{2}}.conf\
%endif\
/lib/modules/%{KVERREL}%{?2:.%{2}}/modules.*\
%ghost /boot/initramfs-%{KVERREL}%{?2:.%{2}}.img\
%{expand:%%files %{?2:%{2}-}devel}\
%defattr(-,root,root)\
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

# plz don't put in a version string unless you're going to tag
# and build.

%changelog
* Fri Oct 15 2010 Peter Jones <pjones@redhat.com> 2.6.35.6-44
- Add a missing dm_put in previous device mapper patch.

* Wed Oct 13 2010 Dave Jones <davej@redhat.com> 2.6.35.6-43
- bump build.

* Wed Oct 13 2010 Kyle McMartin <kyle@redhat.com>
- Disable XHCI registration by default. Passing xhci.enable=1 to the
  kernel will enable it, as will
  echo "options xhci-hcd enable=1" >/etc/modprobe.d/xhci.conf
  This is necessary, because it is beginning to turn up on more and
  more boards, and prevents suspend if the device is probed (since it
  does not implement suspend handlers.)
  Simply removing the module alias would work (and require you to
  manually load the driver, like for floppy) however, there's a chance
  people would like to install onto usb3 drives, so let's provide them
  with an easy means to enable it (the grub cmdline.)

* Tue Oct 12 2010 Kyle McMartin <kyle@redhat.com> 2.6.35.6-42
- Fix  devicemapper UUID field cannot be assigned after map creation
  (rhbz#641476) thanks pjones@.

* Mon Oct 11 2010 Jarod Wilson <jarod@redhat.com> 2.6.35.6-40
- update imon driver to fix issues with key releases and properly
  auto-configure another 0xffdc device (VFD + MCE IR)
- add new nuvoton-cir driver (for integrated IR in ASRock ION 330HT)
- add lirc compat ioctl portability fixups

* Mon Oct 11 2010 Ben Skeggs <bskeggs@redhat.com>
- fix ttm bug that can cause nouveau to crash

* Fri Oct 08 2010 Kyle McMartin <kyle@redhat.com> 2.6.35.6-39
- Push to F-14.

* Wed Oct 06 2010 Dave Jones <davej@redhat.com>
- Another day, another rcu_dereference warning. (#640673)

* Mon Oct 04 2010 Kyle McMartin <kyle@redhat.com>
- Build intel_idle into the kernel, so they get loaded by default. In
  later kernels, it is no longer modular, so it isn't an issue. Noticed
  by mjg59.

* Mon Oct 04 2010 Kyle McMartin <kyle@redhat.com>
- Make printk.time=1 the default, use printk.time=0 to turn it off.
  Results in much more useful traces for us, which is a big win.

* Sun Oct 03 2010 Kyle McMartin <kyle@redhat.com> 2.6.35.6-38
- Add more mac models supported by efifb. (#528232)
- Sanity check base address in efifb on pci systems.

* Fri Oct 01 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.35.6-37
- nouveau: DP fixes, nv50+ corruption fix, display fixes
- drm-nouveau-ibdma-race.patch: removed, in updates now

* Thu Sep 30 2010 Dave Jones <davej@redhat.com>
- silence another rcu_reference warning

* Thu Sep 30 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.35.6-36
- nouveau: fix theoretical race condition which may be the cause of some
  random hangs people reported.

* Wed Sep 29 2010 Dave Jones <davej@redhat.com> 2.6.35.6-35
- Add back an old hack to make an SDV e1000e variant work.

* Wed Sep 29 2010 Dave Jones <davej@redhat.com>
- Enable IB700 watchdog (used by qemu/kvm). (#637152)

* Mon Sep 27 2010 Kyle McMartin <kyle@redhat.com> 2.6.35.6-34
- Linux 2.6.35.6
- bdi-fix-warnings-in-__mark_inode_dirty-for-dev-zero-and-friends.patch was
  dropped, should fix the inode_to_bdi WARN_ON triggering for a bunch of
  people.

* Sat Sep 25 2010 Chuck Ebbert <kyle@redhat.com> 2.6.35.6-33.rc1
- Linux 2.6.35.6-rc1
- Comment out merged patches:
   aio-check-for-multiplication-overflow-in-do_io_submit.patch
   linux-2.6.35.4-virtio_console-fix-poll.patch
   fix-unprotected-access-to-task-credentials-in-whatid.patch
   dell-wmi-add-support-for-eject-key-studio-1555.patch
   irda-correctly-clean-up-self-ias_obj-on-irda_bind-failure.patch
   keys-fix-bug-in-keyctl_session_to_parent-if-parent-has-no-session-keyring.patch
   keys-fix-rcu-no-lock-warning-in-keyctl_session_to_parent.patch
   sched-00-fix-user-time-incorrectly-accounted-as-system-time-on-32-bit.patch
- Revert: "drm/nv50: initialize ramht_refs list for faked 0 channel"
  (our DRM update removes ramht_refs entirely.)
- Add sched-35-increment-cache_nice_tries-only-on-periodic-lb.patch, another
  fix for excessive scheduler load balancing.
- Add xen-fix-typo-in-xen-irq-fix.patch, fixes typo in 2.6.35.5 patch.

* Thu Sep 23 2010 Kyle McMartin <kyle@redhat.com> 2.6.35.5-32
- Serialize mandocs/htmldocs build, since otherwise it will constantly
  fail on the koji builders.

* Thu Sep 23 2010 Kyle McMartin <kyle@redhat.com> 2.6.35.5-31
- Slay another rcu_dereference_check warning pointed out by
  rmcgrath@.

* Thu Sep 23 2010 Kyle McMartin <kyle@redhat.com>
- Enable -debug flavours and switch default image to release builds.
- Bump NR_CPUS on i686 to 64.

* Thu Sep 23 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.35.5-30
- nouveau: fix IGP chipsets (rhbz#636326), and some DP boards
- drm-nouveau-acpi-edid-fix.patch: drop, merged into updates

* Wed Sep 22 2010 Chuck Ebbert <cebbert@redhat.com>
- Fix possible lockup with new scheduler idle balance code.
- Dump stack on failed ATA commands in pata_it821x driver (#632753)

* Tue Sep 21 2010 Kyle McMartin <kyle@redhat.com>
- Add new btusb ids for MacBookPro from wwoods@.

* Tue Sep 21 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.35.5-29
- Scheduler fixes for Bugzilla #635813 and #633037

* Mon Sep 20 2010 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.35.5
- Drop merged patches:
  01-compat-make-compat_alloc_user_space-incorporate-the-access_ok-check.patch
  02-compat-test-rax-for-the-system-call-number-not-eax.patch
  03-compat-retruncate-rax-after-ia32-syscall-entry-tracing.patch
  direct-io-move-aio_complete-into-end_io.patch
  ext4-move-aio-completion-after-unwritten-extent-conversion.patch
  xfs-move-aio-completion-after-unwritten-extent-conversion.patch
  alsa-seq-oss-fix-double-free-at-error-path-of-snd_seq_oss_open.patch

* Thu Sep 16 2010 Dennis Gilmore <dennis@ausil.us>
- build sparc headers on sparcv9
- disable some modules to enable the kernel to build on sparc

* Thu Sep 16 2010 Hans de Goede <hdegoede@redhat.com>
- Small fix to virtio_console poll fix from upstream review

* Wed Sep 15 2010 Dave Jones <davej@redhat.com>
- Fix another RCU lockdep warning (cgroups).

* Wed Sep 15 2010 Hans de Goede <hdegoede@redhat.com>
- virtio_console: Fix poll/select blocking even though there is data to read

* Tue Sep 14 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.35.4-28
- Fix 3 CVEs:
  /dev/sequencer open failure is not handled correctly (CVE-2010-3080)
  NULL deref and panic in irda (CVE-2010-2954)
  keyctl_session_to_parent NULL deref system crash (CVE-2010-2960)

* Tue Sep 14 2010 Chuck Ebbert <cebbert@redhat.com>
- Fix DOS with large argument lists.

* Tue Sep 14 2010 Kyle McMartin <kyle@redhat.com>
- x86_64: plug compat syscalls holes. (CVE-2010-3081, CVE-2010-3301)
  upgrading is highly recommended.
- aio: check for multiplication overflow in do_io_submit. (CVE-2010-3067)

* Mon Sep 13 2010 Chuck Ebbert <cebbert@redhat.com>
- Add support for perl and python scripting to perf (#632942)

* Mon Sep 13 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.35.4-27
- nouveau: fix oops in acpi edid support

* Fri Sep 10 2010 Jarod Wilson <jarod@redhat.com> 2.6.35.4-26
- ir-core rebase to current upstream

* Fri Sep 10 2010 Bastien Nocera <bnocera@redhat.com> - 2.6.35.4-25
- Update AppleIR patch to work, and support the enter key on
  newer remotes

* Fri Sep 10 2010 Chuck Ebbert <cebbert@redhat.com>
- Disable asynchronous suspend by default.

* Fri Sep 10 2010 Ben Skeggs <bskeggs@redhat.com> - 2.6.35.4-23
- nouveau: disable acceleration on nva3/nva5/nva8

* Wed Sep 08 2010 Kyle McMartin <kyle@redhat.com>
- Enable GPIO_SYSFS. (#631958)

* Wed Sep 08 2010 Kyle McMartin <kyle@redhat.com>
- Make pci=use_crs a config option.

* Wed Sep 08 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.35.4-23
- nouveau: handle certain GPU errors better, AGP + misc fixes

* Tue Sep 07 2010 Dave Jones <davej@redhat.com> 2.6.35.4-22
- Disable hung task checker, it only ever causes false positives. (#630777)

* Tue Sep 07 2010 Jarod Wilson <jarod@redhat.com> 2.6.35.4-21
- Enhance HID layer to fully support TiVo Slide remote and dongle

* Mon Sep 06 2010 Kyle McMartin <kyle@redhat.com>
- Suck in patch from F-13 to add support for the eject key on the
  Dell Studio 1555. (#513530)
- flexcop: fix registering braindead stupid names (#575873)

* Mon Sep 06 2010 Kyle McMartin <kyle@redhat.com>
- Patch from paulmck to fix rcu_dereference_check warning
  (http://lkml.org/lkml/2010/8/16/258)

* Mon Sep 06 2010 Jarod Wilson <jarod@redhat.com> 2.6.35.4-20
- Restore the rest of the appleir driver patch

* Mon Sep 06 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.35.4-19
- nouveau: misc fixes from upstream + NVAF support

* Fri Sep 03 2010 Kyle McMartin <kyle@redhat.com>
- Restore appleir driver that got lost in the 2.6.35 rebase.

* Thu Sep 02 2010 Dave Jones <davej@redhat.com> 2.6.35.4-18
- Scatter-gather on via-velocity is hopelessly broken.
  Just switch it off for now.

* Thu Sep 02 2010 Dave Jones <davej@redhat.com> 2.6.35.4-17
- Simplify the VIA Velocity changes. The last round of
  fixes introduced some bugs.

* Wed Sep 01 2010 Dave Jones <davej@redhat.com> 2.6.35.4-16
- Another VIA Velocity fix. This time in ifdown path.

* Wed Sep 01 2010 Dave Jones <davej@redhat.com> 2.6.35.4-15
- Improved version of the VIA Velocity DMA fix.

* Tue Aug 31 2010 Kyle McMartin <kyle@redhat.com> 2.6.35.4-14
- efifb-add-more-models.patch: Add patch from Luke Macken to
  support more Mac models (rhbz#528232)

* Tue Aug 31 2010 Dave Jones <davej@redhat.com> 2.6.35.4-13
- Fix incorrect DMA size freeing error in via-velocity.

* Fri Aug 27 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.4-12
- Linux 2.6.35.4
- kprobes-x86-fix-kprobes-to-skip-prefixes-correctly.patch (#610941)

* Wed Aug 25 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.4-11.rc1
- Linux 2.6.35.4-rc1
- Fix up linux-2.6-i386-nx-emulation.patch for 2.6.35.4

* Sat Aug 21 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.3-10
- Linux 2.6.35.3
- Drop merged patches:
   mm-fix-page-table-unmap-for-stack-guard-page-properly.patch
   mm-fix-up-some-user-visible-effects-of-the-stack-guard-page.patch

* Wed Aug 18 2010 Dave Jones <davej@redhat.com>
- systemd is dependant upon autofs, so build it in instead of modular.

* Tue Aug 17 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.2-9
- Fix fallout from the stack guard page fixes.
  (mm-fix-page-table-unmap-for-stack-guard-page-properly.patch,
   mm-fix-up-some-user-visible-effects-of-the-stack-guard-page.patch)

* Tue Aug 17 2010 Kyle McMartin <kyle@redhat.com>
- Move cgroup fs to /sys/fs/cgroup instead of /cgroup in accordance with
  upstream change post 2.6.35.

* Tue Aug 17 2010 Kyle McMartin <kyle@redhat.com>
- Touch .scmversion in the kernel top level to prevent scripts/setlocalversion
  from recursing into our fedpkg git tree and trying to decide whether the
  kernel git is modified (obviously not, since it's a tarball.) Fixes make
  local.

* Mon Aug 16 2010 Jarod Wilson <jarod@redhat.com>
- Add ir-core streamzap driver, nuke lirc_streamzap

* Sun Aug 15 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.2-8
- Linux 2.6.35.2

* Fri Aug 13 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.2-7.rc1
- Linux 2.6.35.2-rc1
- Comment out patches merged in -stable:
    linux-2.6-tip.git-396e894d289d69bacf5acd983c97cd6e21a14c08.patch
    linux-2.6-ext4-fix-freeze-deadlock.patch
- New config option:
    CONFIG_CRYPTO_MANAGER_TESTS=y

* Tue Aug 10 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.1-6
- Linux 2.6.35.1

* Tue Aug 10 2010 Ben Skeggs <bskeggs@redhat.com> 2.6.35.1-5.rc1
- nouveau: bring in patches up to what will be in 2.6.36

* Sat Aug 07 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35.1-4.rc1
- Linux 2.6.35.1-rc1

* Sat Aug 07 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre Sat Aug 28
- Deblobbed 2.6.35-libre2.
- Deblobbed lirc-staging-2.6.36.patch.
- Deblobbed linux-2.6-v4l-dvb-ir-core-update.patch.
- Adjusted drm-nouveau-updates.patch.

* Fri Aug 06 2010 Chuck Ebbert <cebbert@redhat.com>  2.6.35-3
- Copy fix for bug #617699 ("ext4 and xfs wrong data returned on read
  after write if file size was changed with ftruncate") from F-13
- Disable CONFIG_MULTICORE_RAID456

* Wed Aug 04 2010 Dave Jones <davej@redhat.com>
- sched: Revert nohz_ratelimit() which causes a lot of
  extra wakeups burning CPU, and my legs.

* Sun Aug 01 2010 Dave Jones <davej@redhat.com>
- Linux 2.6.35

* Sun Aug 01 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc6-git6

* Fri Jul 30 2010 Jarod Wilson <jarod@redhat.com>
- lirc staging update
- update kworld patch to one committed upstream
- can't believe how much nicer dist-git is than dist-cvs
- patch memory leaks in mceusb and imon drivers

* Fri Jul 30 2010 Dave Jones <davej@redhat.com>
- Enable PPS (#619392)

* Thu Jul 29 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc6-git5

* Mon Jul 27 2010 Kyle McMartin <kyle@redhat.com>
- Patch from linville to only use the country code to set band limits.
  (Fixes Apple Airport base stations from limiting you from associating
   with other channels.)

* Fri Jul 23 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.35-0.56.rc6.git1
- Linux 2.6.35-rc6-git1

* Thu Jul 22 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc6

* Thu Jul 22 2010 Ben Skeggsb <bskeggs@redhat.com>
- drm-nouveau-updates: bring back, most patches *weren't* upstream yet,
  they're queued for 2.6.36.

* Wed Jul 21 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.35-0.53.rc5.git7
- Linux 2.6.35-rc5-git7

* Wed Jul 21 2010 Dave Jones <davej@redhat.com>
- Remove the %verify (no mtime) on kernel-devel's files.
  If they got modified, they should fail rpm verify.

* Wed Jul 21 2010 Dave Jones <davej@redhat.com>
- Linux 2.6.35-rc5-git6
- Removed drm-nouveau-updates.patch (upstreamed)

* Mon Jul 19 2010 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.35-rc5-git4

* Mon Jul 19 2010 Jarod Wilson <jarod@redhat.com> 2.6.35-0.49.rc5.git2
- Fix from Kyle for input_set_key oops introduced by ir-core patches (#615707)
- Update lirc-staging patches to match what's about to be submitted upstream,
  complete with updated copy_from_user overflow check fixages

* Sun Jul 18 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.35-0.47.rc5.git2
- Linux 2.6.35-rc5-git2

* Sun Jul 18 2010 Chuck Ebbert <cebbert@redhat.com>
- lirc-staging-2.6.36-fixes.patch: Fix up buffer size checking in
  lirc code, found by CONFIG_DEBUG_STRICT_USER_COPY_CHECKS

* Sun Jul 18 2010 Hans de Goede <hdegoede@redhat.com>
- Add support for dynamic menu controls to the uvcvideo driver (#576023)

* Sun Jul 18 2010 Jarod Wilson <jarod@redhat.com> 2.6.35-0.44.rc5.git1
- Oops, minor oversight when moving lirc bits into staging resulted
  in the modules themselves not getting built. Fix that.

* Fri Jul 16 2010 Dave Jones <davej@redhat.com>
- Limit some alsa spew that the nuforce udac makes happen a lot.

* Fri Jul 16 2010 Jarod Wilson <jarod@redhat.com> 2.6.35-0.41.rc5.git1
- Pull in ir-core update from v4l/dvb staging for 2.6.36, which includes
  new in-kernel lirc_dev and ir-core mceusb driver
- Move not-yet-merged/ported lirc bits to drivers/staging/, patch to be
  sent upstream for 2.6.36 staging tree RSN

* Thu Jul 15 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.35-0.40.rc5.git1
- Replace pci-acpi-disable-aspm-if-no-osc.patch with
  updated version from F-13

* Thu Jul 15 2010 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.35-rc5-git1

* Tue Jul 13 2010 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.35-rc5

* Tue Jul 13 2010 Ben Skeggs <bskeggs@redhat.com>
- nouveau: miscellanous fixes

* Mon Jul 12 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc4-git5

* Mon Jul 12 2010 Dave Jones <davej@redhat.com>
- Remove a bunch of x86 options from config files that get set
  automatically, and can't be overridden.

* Fri Jul  9 2010 Roland McGrath <roland@redhat.com>
- Split execshield into two patches.

* Thu Jul 08 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.35-0.31.rc4.git4
- Linux 2.6.35-rc4-git4

* Thu Jul  8 2010 Roland McGrath <roland@redhat.com>
- Remove exec-shield=2 setting, some other cruft from execshield.

* Wed Jul  7 2010 Roland McGrath <roland@redhat.com> 2.6.35-0.29.rc4.git0.fc14
- Revamp perf packaging.

* Wed Jul 07 2010 Chuck Ebbert <cebbert@redhat.com>
- pci-acpi-disable-aspm-if-no-osc.patch, pci-aspm-dont-enable-too-early.patch
  PCI layer fixes for problems with hardware that doesn't support ASPM.

* Wed Jul 07 2010 Ben Skeggs <bskeggs@redhat.com>
- nouveau: bring in nouveau upstream

* Mon Jul 05 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc4

* Fri Jul 02 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc3-git7
  dropped: i915-fix-crt-hotplug-regression.patch (upstream)
  dropped: drm-i915-fix-edp-panels.patch (upstream)

* Fri Jul 02 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc3-git6

* Thu Jul 01 2010 Dave Jones <davej@redhat.com>
- Add a patch to debug an infinite loop warning from acpi.

* Thu Jul 01 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc3-git5

* Thu Jul 01 2010 Chuck Ebbert <cebbert@redhat.com>
- Copy fix for BZ#220892 from F-13.

* Wed Jun 30 2010 Kyle McMartin <kyle@redhat.com> 2.6.35-0.19.rc3.git4
- 2.6.35-rc3-git4

* Tue Jun 29 2010 Chuck Ebbert <cebbert@redhat.com>
- Disable Intel Moorestown support -- it breaks PC keyboard controllers.

* Tue Jun 29 2010 Dave Jones <davej@redhat.com>
- Building external modules requires perl. (#608525)

* Tue Jun 29 2010 Kyle McMartin <kyle@redhat.com> 2.6.35-0.15.rc3.git3
- 2.6.35-rc3-git3
- i915-fix-crt-hotplug-regression.patch: attempt to solve the gm45 hotplug
  irq storm.

* Mon Jun 28 2010 Chuck Ebbert <cebbert@redhat.com>
- ppc64: enable active memory sharing and DLPAR memory remove (#607175)

* Mon Jun 28 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc3-git2

* Mon Jun 28 2010 Dave Jones <davej@redhat.com>
- 2.6.35-rc3-git1

* Mon Jun 28 2010 Dave Airlie <airlied@redhat.com>
- drm-i915-fix-edp-panels.patch: update to newer version

* Fri Jun 25 2010 Kyle McMartin <kyle@redhat.com>
- drm-i915-fix-edp-panels.patch: copy from rawhide.

* Wed Jun 23 2010 Eric Sandeen <sandeen@redhat.com>
- Fix ext4 freeze deadlock (#607245)

* Wed Jun 23 2010 Kyle McMartin <kyle@redhat.com>
- Override generic's CONFIG_HZ_1000 on s390.

* Tue Jun 22 2010 Dave Jones <davej@redhat.com>
- Fix localhost networking.

* Tue Jun 22 2010 Kyle McMartin <kyle@redhat.com> 2.6.35-0.1.rc3.git0
- Putting the raw back into rawhide... Yeehaw.
- 2.6.35-rc3
- Drop a tonne of patches that were merged upstream, or were backports.
- Rebase execshield, utrace.
- Fix up a bunch of rejects, build failures.
- Fix up lirc to build with strict copy_from_user checking.

* Mon Jun 21 2010 Dave Jones <davej@redhat.com>
- Disable workaround for obscure SMP pentium pro errata.
  I miss the 1990s too, but it's time to move on.
  If anyone actually needs this it would be better done using
  the apply_alternatives infrastructure.

* Mon Jun 21 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-45
- drm-revert-drm-fbdev-rework-output-polling-to-be-back-in-core.patch
  Revert eb1f8e4f, bisected by Nicolas Kaiser. Thanks! (rhbz#599190)
  (If this works, will try to root-cause.)
- rebase previous patch on top of above reversion

* Mon Jun 21 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-44
- revert-drm-kms-toggle-poll-around-switcheroo.patch (rhbz#599190)

* Thu Jun 17 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-43
- Suck in patch from Dave Miller in 2.6.35 to add async hash testing,
  hopefully fixes error from previous commit. (But making it modular
  is still a good idea.)

* Thu Jun 17 2010 Kyle McMartin <kyle@redhat.com>
- make ghash-clmulni modular to get rid of early boot noise (rhbz#586954)
  (not a /fix/ but it should at least quiet boot down a bit if you have
   the cpu support)

* Wed Jun 16 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-40
- Snag some more DRM commits into drm-next.patch that I missed the first
  time.
- Fix up radeon_pm toggle to work with the upstream code.

* Tue Jun 15 2010 Prarit Bhargava <prarit@redhat.com>
- Turn off CONFIG_I2O on x86.
  It is broken on 64-bit address spaces (i686/PAE, x86_64), and frankly, I'm
  having trouble finding anyone who actually uses it.

* Tue Jun 15 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Re-enable kernel-libre-firmware.

* Tue Jun 15 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-38
- Fix build by nuking superfluous "%{expand" which was missing a
  trailing '}'. You may now reward me with an array of alcoholic
  beverages, I so richly deserve for spending roughly a full
  day staring at the diff of the spec.

* Mon Jun 14 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-37
- btrfs ACL fixes from CVE-2010-2071.

* Sun Jun 13 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-36
- remunge and reapply hdpvr-ir-enable

* Sun Jun 13 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-35
- mac80211/iwlwifi fix connections to some APs (rhbz#558002)
  patches from sgruszka@.

* Sun Jun 13 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-34
- Provide a knob to enable radeon_pm to allow users to test
  that functionality. Add radeon.pm=1 to your kernel cmdline
  in order to enable it. (It still defaults to off though.)

* Sun Jun 13 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-33
- Update drm-next to include fixes since 2.6.35-rc1.

* Fri Jun 11 2010 Justin M. Forbes <jforbes@redhat.com>
- Disable xsave for so that kernel will boot on ancient EC2 hosts.

* Wed Jun 09 2010 John W. Linville <linville@redhat.com>
- Disable rt20xx and rt35xx chipset support in rt2800 drivers (#570869)

* Wed Jun 09 2010 David Woodhouse <David.Woodhouse@intel.com>
- Include PHY modules in modules.networking (#602155)

* Tue Jun 08 2010 Dave Jones <davej@redhat.com>
- Remove useless -kdump kernel support

* Tue Jun 08 2010 Dave Jones <davej@redhat.com>
- Remove ia64 ata quirk which had no explanation, and still
  isn't upstream. No-one cares.

* Tue Jun 08 2010 Dave Jones <davej@redhat.com>
- Drop linux-2.6-vio-modalias.patch
  Two years should have been long enough to get upstream if this is important.

* Tue Jun 08 2010 Dave Jones <davej@redhat.com>
- Remove crufty Xen remnants from specfile.

* Tue Jun 08 2010 Dave Jones <davej@redhat.com>
- Remove mkinitrd ifdefs. Dracut or GTFO.

* Thu Jun 03 2010 Kyle McMartin <kyle@redhat.com>
- Build kernel headers on s390.

* Wed Jun 02 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-20
- Disable doc_build_fail because xmlto et al. are crud.

* Wed Jun 02 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-19
- Enable -debug flavour builds, until we branch for 2.6.35-rcX.

* Wed Jun 02 2010 Kyle McMartin <kyle@redhat.com>
- revert writeback fixes for now, there appear to be dragons
  lurking there still.

* Tue Jun 01 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-18
- fix mismerge in i915_gem.c, drm_gem_object_alloc is now
  i915_gem_alloc_object.
- add a hunk to rcu_read{,un}lock in sched_fair too.

* Tue Jun 01 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-17
- backport writeback fixes from Jens until stable@ picks them up.

* Tue Jun 01 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-16
- quiet-prove_RCU-in-cgroups.patch: shut RCU lockdep up
  as in 8b08ca52f5942c21564bbb90ccfb61053f2c26a1.

* Tue Jun 01 2010 Kyle McMartin <kyle@redhat.com>
- disable radeon_pm for now.

* Mon May 31 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Adjust iwlwifi-Recover-TX-flow-stall-due-to-stuck-queue.patch.
- Adjust drm-next.patch.

* Mon May 31 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-14
- re-add drm-next.patch, should be in sync with 2.6.35 and what
  was backported to Fedora 13.
- drop patches merged in drm-next to 2.6.35
- rebase relevant iwl fixes on top of 2.6.34 from the ones committed
  to F-13 by linville.

* Wed May 26 2010 Adam Jackson <ajax@redhat.com>
- config-generic: Stop building i830.ko

* Wed May 26 2010 Kyle McMartin <kyle@redhat.com>
- iwlwifi-recover_from_tx_stall.patch: copy from F-13.

* Fri May 21 2010 Roland McGrath <roland@redhat.com> 2.6.34-11
- utrace update

* Fri May 21 2010 Dave Jones <davej@redhat.com>
- Update the SELinux mprotect patch with a newer version from Stephen

* Fri May 21 2010 Roland McGrath <roland@redhat.com>
- perf requires libdw now, not libdwarf

* Fri May 21 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-6
- Fixups for virt_console from Amit Shah, thanks!

* Thu May 20 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-5
- disable intel sdvo fixes until dependent code is backported.

* Thu May 20 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-4
- resync a lot of stuff with F-13...
- linux-2.6-acpi-video-export-edid.patch: rebase & copy from F-13
- acpi-ec-add-delay-before-write.patch: copy from F-13
- ... and a whole lot more that I can't be bothered typing.

* Mon May 17 2010 Matthew Garrett <mjg@redhat.com>
- thinkpad-acpi-fix-backlight.patch: Fix backlight support on some recent
   Thinkpads

* Mon May 17 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Deblobbed 2.6.34-libre.

* Sun May 16 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-2
- Disable strict copy_from_user checking until lirc is fixed.

* Sun May 16 2010 Kyle McMartin <kyle@redhat.com> 2.6.34-1
- Linux 2.6.34


###
# The following Emacs magic makes C-c C-e use UTC dates.
# Local Variables:
# rpm-change-log-uses-utc: t
# End:
###
