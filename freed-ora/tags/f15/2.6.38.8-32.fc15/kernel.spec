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
# For non-released -rc kernels, this will be prepended with "0.", so
# for example a 3 here will become 0.3
#
%global baserelease 32
%global fedora_build %{baserelease}

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 38

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
%define stable_update 8
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
# kernel-PAE (only valid for i686)
%define with_pae       %{?_without_pae:       0} %{?!_without_pae:       1}
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
# Only build the pae kernel (--with paeonly):
%define with_paeonly   %{?_with_paeonly:      1} %{?!_with_paeonly:      0}
# Only build the debug kernel (--with dbgonly):
%define with_dbgonly   %{?_with_dbgonly:      1} %{?!_with_dbgonly:      0}

# should we do C=1 builds with sparse
%define with_sparse    %{?_with_sparse:       1} %{?!_with_sparse:       0}

# Set debugbuildsenabled to 1 for production (build separate debug kernels)
#  and 0 for rawhide (all kernels are debug kernels).
# See also 'make debug' and 'make release'.
%define debugbuildsenabled 1

# Want to build a vanilla kernel build without any non-upstream patches?
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
%define pkg_release 0%{?rctag}%{?gittag}.%{fedora_build}%{?buildid}%{?dist}%{?libres}

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
%ifnarch i686
%define with_pae 0
%endif

# if requested, only build base kernel
%if %{with_baseonly}
%define with_smp 0
%define with_pae 0
%define with_debug 0
%endif

# if requested, only build smp kernel
%if %{with_smponly}
%define with_up 0
%define with_pae 0
%define with_debug 0
%endif

# if requested, only build pae kernel
%if %{with_paeonly}
%define with_up 0
%define with_smp 0
%define with_debug 0
%endif

# if requested, only build debug kernel
%if %{with_dbgonly}
%if %{debugbuildsenabled}
%define with_up 0
%define with_pae 0
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
%define with_perf 0
%endif

%ifarch sparc64
%define asmarch sparc
%define all_arch_configs kernel-%{version}-sparc64*.config
%define make_target image
%define kernel_image vmlinux
%define image_install_path boot
%define with_perf 0
%endif

%ifarch sparcv9
%define hdrarch sparc
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

# Should make listnewconfig fail if there's config options
# printed out?
%if %{nopatches}%{using_upstream_branch}
%define listnewconfig_fail 0
%else
%define listnewconfig_fail 1
%endif

# To temporarily exclude an architecture from being built, add it to
# %%nobuildarches. Do _NOT_ use the ExclusiveArch: line, because if we
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
# Packages that need to be installed before the kernel is, because the %%post
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
ExclusiveArch: noarch %{all_x86} x86_64 ppc ppc64 ia64 %{sparc} s390 s390x alpha alphaev56 %{arm}
ExclusiveOS: Linux

%kernel_reqprovconf

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
%if %{with_perf}
BuildRequires: elfutils-devel zlib-devel binutils-devel newt-devel python-devel perl(ExtUtils::Embed)
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

# This file is intentionally left empty in the stock kernel. Its a nicety
# added for those wanting to do custom rebuilds with altered config opts.
Source1000: config-local

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

# we also need compile fixes for -vanilla
Patch04: linux-2.6-compile-fixes.patch

# build tweak for build ID magic, even for -vanilla
Patch05: linux-2.6-makefile-after_link.patch

Patch07: freedo.patch

%if !%{nopatches}


# revert upstream patches we get via other methods
Patch09: linux-2.6-upstream-reverts.patch
# Git trees.

# Standalone patches
Patch20: linux-2.6-hotfixes.patch

Patch29: linux-2.6-utrace-revert-make-ptrace-functions-static.patch
Patch30: linux-2.6-tracehook.patch
Patch31: linux-2.6-utrace.patch
Patch32: linux-2.6-utrace-ptrace.patch

Patch150: linux-2.6.29-sparc-IOC_TYPECHECK.patch

Patch160: linux-2.6-32bit-mmap-exec-randomization.patch
Patch161: linux-2.6-i386-nx-emulation.patch

Patch200: linux-2.6-debug-sizeof-structs.patch
Patch202: linux-2.6-debug-taint-vm.patch
Patch203: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch204: linux-2.6-debug-always-inline-kzalloc.patch

Patch380: linux-2.6-defaults-pci_no_msi.patch
Patch381: linux-2.6-defaults-pci_use_crs.patch

# ASPM: enable powersave by default
Patch383: linux-2.6-defaults-aspm.patch
Patch384: pci-pcie-links-may-not-get-configured-for-aspm-under-powersave-mode.patch
Patch385: pci-enable-aspm-state-clearing-regardless-of-policy.patch

Patch389: ima-allow-it-to-be-completely-disabled-and-default-off.patch

Patch390: linux-2.6-defaults-acpi-video.patch
Patch391: linux-2.6-acpi-video-dos.patch
Patch393: acpi-ec-add-delay-before-write.patch
Patch394: linux-2.6-acpi-debug-infinite-loop.patch

Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch452: linux-2.6.30-no-pcspkr-modalias.patch

Patch460: linux-2.6-serial-460800.patch

Patch470: die-floppy-die.patch

Patch510: linux-2.6-silence-noise.patch
Patch530: linux-2.6-silence-fbcon-logo.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sparc-selinux-mprotect-checks.patch

Patch600: block-queue-refcount.patch

Patch610: hda_intel-prealloc-4mb-dmabuffer.patch

Patch700: linux-2.6-e1000-ich9-montevina.patch

Patch800: linux-2.6-crash-driver.patch

# crypto/

# virt + ksm patches
Patch1555: fix_xen_guest_on_old_EC2.patch

# DRM

# nouveau + drm fixes
Patch1809: drm-nouveau-fixes.patch
Patch1810: drm-nouveau-updates.patch
Patch1811: drm-ttm-move-notify.patch
Patch1819: drm-intel-big-hammer.patch
# intel drm is all merged upstream
# fix for 945G corruption will hit stable eventually
Patch1821: drm-i915-fix-pipelined-fencing.patch
Patch1824: drm-intel-next.patch
# make sure the lvds comes back on lid open
Patch1825: drm-intel-make-lvds-work.patch
Patch1826: drm-intel-edp-fixes.patch
Patch1828: drm-intel-eeebox-eb1007-quirk.patch
Patch1829: drm-intel-restore-mode.patch
# radeon - new hw + fixes for fusion and t500 regression
Patch1839: drm-radeon-fix-regression-on-atom-cards-with-hardcoded-EDID-record.patch
Patch1840: drm-radeon-update.patch
Patch1841: drm-radeon-update2.patch
Patch1842: drm-radeon-pageflip-oops-fix.patch

Patch1900: linux-2.6-intel-iommu-igfx.patch

# linux1394 git patches
Patch2200: linux-2.6-firewire-git-update.patch
Patch2201: linux-2.6-firewire-git-pending.patch

# Quiet boot fixes
# silence the ACPI blacklist code
Patch2802: linux-2.6-silence-acpi-blacklist.patch

# media patches
Patch2898: cx88-Fix-HVR4000-IR-keymap.patch
Patch2899: linux-2.6-v4l-dvb-fixes.patch
Patch2900: linux-2.6-v4l-dvb-update.patch
Patch2901: linux-2.6-v4l-dvb-experimental.patch

# fs fixes

# NFSv4

# patches headed upstream

Patch12010: add-appleir-usb-driver.patch

Patch12016: disable-i8042-check-on-apple-mac.patch

Patch12018: neuter_intel_microcode_load.patch

Patch12101: apple_backlight.patch
Patch12102: efifb_update.patch
Patch12200: acpi_reboot.patch

# Runtime power management
Patch12203: linux-2.6-usb-pci-autosuspend.patch
Patch12204: linux-2.6-enable-more-pci-autosuspend.patch

Patch12303: dmar-disable-when-ricoh-multifunction.patch

Patch12305: printk-do-not-mangle-valid-userspace-syslog-prefixes.patch
Patch12306: scsi-sd-downgrade-caching-printk-from-error-to-notice.patch

#netconsole fixes
Patch12400: linux-2.6-netconsole-deadlock.patch

# CVE-2011-1581
Patch12402: bonding-incorrect-tx-queue-offset.patch

# Restore reliable stack backtraces, and hopefully fix RHBZ #700718
Patch12403: x86-dumpstack-correct-stack-dump-info-when-frame-pointer-is-available.patch

# Fix breakage of PCI network adapter names on older Dell systems
Patch12404: x86-pci-preserve-existing-pci-bfsort-whitelist-for-dell-systems.patch

Patch12407: scsi_dh_hp_sw-fix-deadlock-in-start_stop_endio.patch

Patch12415: hid-multitouch-add-support-for-elo-touchsystems.patch
Patch12416: bluetooth-device-ids-for-ath3k-on-pegatron-lucid-tablets.patch

Patch12418: ath5k-disable-fast-channel-switching-by-default.patch

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
Obsoletes: glibc-kernheaders < 3.0-46
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

%package -n perf-libre-debuginfo
Summary: Debug information for package perf
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n perf-libre-debuginfo
This package provides debug information for package perf.

# Note that this pattern only works right to match the .build-id
# symlinks because of the trailing nonmatching alternation and
# the leading .*, because of find-debuginfo.sh's buggy handling
# of matching the pattern against the symlinks file.
%{expand:%%global debuginfo_args %{?debuginfo_args} -p '.*%%{_bindir}/perf(\.debug)?|.*%%{_libexecdir}/perf-core/.*|XXX' -o perf-debuginfo.list}
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
  if ! grep -E "^Patch[0-9]+: $patch\$" %{_specdir}/${RPM_PACKAGE_NAME%%%%%{?variant}}.spec ; then
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

# %%{vanillaversion} : the full version name, e.g. 2.6.35-rc6-git3
# %%{kversion}       : the base version, e.g. 2.6.34

# Use kernel-%%{kversion}%%{?dist} as the top-level directory name
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

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?stablelibre:-libre%{?librev}}/" vanilla-%{kversion}/Makefile

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
%if "%{?stablelibre}" != "%{?rcrevlibre}"
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?rcrevlibre:-libre%{?librev}}/" Makefile
%endif
    ApplyPatch patch%{?rcrevlibre}-2.6.%{upstream_sublevel}-rc%{rcrev}.bz2
%if 0%{?gitrev}
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = -rc%{rcrev}%{?gitrevlibre:-libre%{?librev}}/" Makefile
    ApplyPatch patch%{?gitrevlibre}-2.6.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}.bz2
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if "%{?stablelibre}" != "%{?gitrevlibre}"
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?rcrevlibre:-libre%{?librev}}/" Makefile
%endif
    ApplyPatch patch%{?gitrevlibre}-2.6.%{base_sublevel}-git%{gitrev}.bz2
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

%if %{?all_arch_configs:1}%{!?all_arch_configs:0}
#if a rhel kernel, apply the rhel config options
%if 0%{?rhel}
  for i in %{all_arch_configs}
  do
    mv $i $i.tmp
    ./merge.pl config-rhel-generic $i.tmp > $i
    rm $i.tmp
  done
%endif

# Merge in any user-provided local config option changes
for i in %{all_arch_configs}
do
  mv $i $i.tmp
  ./merge.pl %{SOURCE1000} $i.tmp > $i
  rm $i.tmp
done
%endif

ApplyOptionalPatch git-linus.diff

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
ApplyPatch linux-2.6-utrace-revert-make-ptrace-functions-static.patch
ApplyPatch linux-2.6-tracehook.patch
ApplyPatch linux-2.6-utrace.patch
ApplyPatch linux-2.6-utrace-ptrace.patch

# Architecture patches
# x86(-64)
# Restore reliable stack backtraces, and hopefully
# fix RHBZ #700718
ApplyPatch x86-dumpstack-correct-stack-dump-info-when-frame-pointer-is-available.patch

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

# WMI

# ACPI
ApplyPatch linux-2.6-defaults-acpi-video.patch
ApplyPatch linux-2.6-acpi-video-dos.patch
ApplyPatch acpi-ec-add-delay-before-write.patch
ApplyPatch linux-2.6-acpi-debug-infinite-loop.patch

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
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
# fixes for ASPM powersave mode
ApplyPatch pci-pcie-links-may-not-get-configured-for-aspm-under-powersave-mode.patch
ApplyPatch pci-enable-aspm-state-clearing-regardless-of-policy.patch
# Fix breakage of PCI network adapter names on older Dell systems
ApplyPatch x86-pci-preserve-existing-pci-bfsort-whitelist-for-dell-systems.patch

#ApplyPatch ima-allow-it-to-be-completely-disabled-and-default-off.patch

#
# SCSI Bits.
#
ApplyPatch block-queue-refcount.patch

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


# /dev/crash driver.
ApplyPatch linux-2.6-crash-driver.patch

# Hack e1000e to work on Montevina SDV
ApplyPatch linux-2.6-e1000-ich9-montevina.patch

# crypto/

# Assorted Virt Fixes
ApplyPatch fix_xen_guest_on_old_EC2.patch

# DRM core

# Nouveau DRM
ApplyPatch drm-ttm-move-notify.patch
ApplyOptionalPatch drm-nouveau-fixes.patch
ApplyOptionalPatch drm-nouveau-updates.patch

# Intel DRM
ApplyOptionalPatch drm-intel-next.patch
ApplyPatch drm-intel-big-hammer.patch
ApplyPatch drm-intel-make-lvds-work.patch
ApplyPatch linux-2.6-intel-iommu-igfx.patch
ApplyPatch drm-intel-edp-fixes.patch
ApplyPatch drm-i915-fix-pipelined-fencing.patch
ApplyPatch drm-intel-eeebox-eb1007-quirk.patch
ApplyPatch drm-intel-restore-mode.patch

# radeon DRM (add cayman support)
ApplyPatch drm-radeon-fix-regression-on-atom-cards-with-hardcoded-EDID-record.patch -R
ApplyPatch drm-radeon-update.patch
ApplyPatch drm-radeon-update2.patch
ApplyPatch drm-radeon-pageflip-oops-fix.patch

# linux1394 git patches
#ApplyPatch linux-2.6-firewire-git-update.patch
#ApplyOptionalPatch linux-2.6-firewire-git-pending.patch

# silence the ACPI blacklist code
ApplyPatch linux-2.6-silence-acpi-blacklist.patch

# V4L/DVB updates/fixes/experimental drivers
#  apply if non-empty
ApplyPatch cx88-Fix-HVR4000-IR-keymap.patch -R
ApplyOptionalPatch linux-2.6-v4l-dvb-fixes.patch
ApplyOptionalPatch linux-2.6-v4l-dvb-update.patch
ApplyOptionalPatch linux-2.6-v4l-dvb-experimental.patch

# Patches headed upstream

ApplyPatch disable-i8042-check-on-apple-mac.patch

ApplyPatch add-appleir-usb-driver.patch

ApplyPatch neuter_intel_microcode_load.patch

# various fixes for Apple and EFI
ApplyPatch apple_backlight.patch
ApplyPatch efifb_update.patch
ApplyPatch acpi_reboot.patch

# Runtime PM
#ApplyPatch linux-2.6-usb-pci-autosuspend.patch
### Broken by implicit notify support & ACPICA rebase
###ApplyPatch linux-2.6-enable-more-pci-autosuspend.patch

# rhbz#605888
ApplyPatch dmar-disable-when-ricoh-multifunction.patch

# rhbz#691888
ApplyPatch printk-do-not-mangle-valid-userspace-syslog-prefixes.patch

ApplyPatch scsi-sd-downgrade-caching-printk-from-error-to-notice.patch

#rhbz 668231
ApplyPatch linux-2.6-netconsole-deadlock.patch

# CVE-2011-1581
ApplyPatch bonding-incorrect-tx-queue-offset.patch

ApplyPatch scsi_dh_hp_sw-fix-deadlock-in-start_stop_endio.patch

ApplyPatch hid-multitouch-add-support-for-elo-touchsystems.patch
ApplyPatch bluetooth-device-ids-for-ath3k-on-pegatron-lucid-tablets.patch

# rhbz#709122
ApplyPatch ath5k-disable-fast-channel-switching-by-default.patch

# END OF PATCH APPLICATIONS

%endif

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl

touch .scmversion

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
  make ARCH=$Arch listnewconfig | grep -E '^CONFIG_' >.newoptions || true
%if %{listnewconfig_fail}
  if [ -s .newoptions ]; then
    cat .newoptions
    exit 1
  fi
%endif
  rm -f .newoptions
  make ARCH=$Arch oldnoconfig
  echo "# $Arch" > configs/$i
  cat .config >> configs/$i
done
# end of kernel config
%endif

# get rid of unwanted files resulting from patch fuzz
find . \( -name "*.orig" -o -name "*~" \) -exec rm -f {} \; >/dev/null

# remove unnecessary SCM files
find . -name .gitignore -exec rm -f {} \; >/dev/null

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

    make -s ARCH=$Arch oldnoconfig >/dev/null
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

%if %{fancy_debuginfo}
    if test -s vmlinux.id; then
      cp vmlinux.id $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/vmlinux.id
    else
      echo >&2 "*** ERROR *** no vmlinux build ID! ***"
      exit 1
    fi
%endif

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

    grep -F /drivers/ modnames | xargs --no-run-if-empty nm -upA |
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

    grep -E -v \
    	  'GPL( v2)?$|Dual BSD/GPL$|Dual MPL/GPL$|GPL and additional rights$' \
	  modinfo && exit 1

    rm -f modinfo modnames

    # remove files that will be auto generated by depmod at rpm -i time
    for i in alias alias.bin builtin.bin ccwmap dep dep.bin ieee1394map inputmap isapnpmap ofmap pcimap seriomap symbols symbols.bin usbmap
    do
      rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.$i
    done

    # Move the devel headers out of the root file system
    mkdir -p $RPM_BUILD_ROOT/usr/src/kernels
    mv $RPM_BUILD_ROOT/lib/modules/$KernelVer/build $RPM_BUILD_ROOT/$DevelDir
    ln -sf ../../..$DevelDir $RPM_BUILD_ROOT/lib/modules/$KernelVer/build

    # prune junk from kernel-devel
    find $RPM_BUILD_ROOT/usr/src/kernels -name ".*.cmd" -exec rm -f {} \;
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
  make %{?_smp_mflags} -C tools/perf -s V=1 HAVE_CPLUS_DEMANGLE=1 prefix=%{_prefix}
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

%if %{with_debuginfo}
%files -f perf-debuginfo.list -n perf-libre-debuginfo
%defattr(-,root,root)
%endif
%endif

# This is %%{image_install_path} on an arch where that includes ELF files,
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
%attr(600,root,root) /boot/System.map-%{KVERREL}%{?2:.%{2}}\
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
* Thu Jun 09 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.8-32
- ath5k-disable-fast-channel-switching-by-default.patch (rhbz#709122)
  (korgbz#34992) [a99168ee in wireless-next]

* Tue Jun 07 2011 Dave Jones <davej@redhat.com>
- [SCSI] Fix oops caused by queue refcounting failure.

* Sat Jun 04 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.8-31
- Linux 2.6.38.8
- Revert radeon patches we already have:
   drm/radeon/kms: add wait idle ioctl for eg->cayman
   drm/radeon/evergreen/btc/fusion: setup hdp to invalidate and flush when asked
- Drop individual patches we have:
   ips-use-interruptible-waits-in-ips-monitor.patch
   drm-vblank-events-fix-hangs.patch
   mm-vmscan-correct-use-of-pgdat_balanced-in-sleeping_prematurely.patch
   mm-vmscan-correctly-check-if-reclaimer-should-schedule-during-shrink_slab.patch
- Drop x86-amd-arat-bug-on-sempron-workaround.patch; the proper fix is in 2.6.38.8

* Sun May 29 2011 Dave Airlie <airlied@redhat.com>
- fix oops on pageflipping sometimes (#680651)

* Fri May 27 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38.7-30
- nouveau: minor fixes for various issues from upstream
- nv40 modesetting fix (rhbz#708235)
- nv50+ support for LVDS panels using SPWG spec (blank/corrupt screen fixes)
- nva3+ pm clock get/set fixes

* Wed May 25 2011 Dave Airlie <airlied@redhat.com>
- drm-radeon-update2.patch: more radeon updates + cayman accel support

* Tue May 24 2011 Kyle McMartin <kmcmartin@redhat.com>
- hid-multitouch: add support for elo touchsystems panels (requested
  by hadess, backported from hid-next)
- bluetooth: add support for more ath3k devices (Ditto.)

* Mon May 23 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.7-29
- Linux 2.6.38.7
- Eliminate hangs when using frequent high-order allocations

* Fri May 20 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.7-28.rc1
- Linux 2.6.38.7-rc1
- Fix up context in utrace-ptrace.patch
- Revert radeon patches already in our radeon update:
  drm-radeon-kms-fix-gart-setup-on-fusion-parts-v2-backport.patch
- Drop merged patches:
  iwlwifi-add-_ack_plpc_check-module-parameters.patch
- Fix stalls on AMD Sempron notebooks (#704059)

* Fri May 13 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.6-27
- [fabbione@] Fix a deadlock when using hp_sw with an HP san.
  (7a1e9d82 upstream)

* Wed May 11 2011 Chuck Ebbert <cebbert@redhat.com>
- Fix Intel IPS driver so it doesn't run continuously (#703511)

* Tue May 10 2011 Kyle McMartin <kmcmartin@redhat.com>
- [sgruszka@] iwlwifi: add {ack,plpc}_check module parameters (#666646)

* Tue May 10 2011 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.38.6 (no functional changes from 2.6.38.6-26.rc1)
- Drop merged patches:
    can-add-missing-socket-check-in-can_raw_release.patch
    scsi-fix-oops-in-scsi_run_queue.patch
    vm-skip-the-stack-guard-page-lookup-in-get_user_pages-only-for-mlock.patch

* Mon May 09 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.6-26.rc1
- Fix a VM bug introduced in 2.6.38.4

* Mon May 09 2011 Kyle McMartin <kmcmartin@redhat.com>
- Update to stable review 2.6.38.6-rc1
- Revert DRM patch duplicated in drm-radeon-update rollup.
- Revert cx88 fix in stable which has been fixed differently in the
  v4l-dvb-update backport.
- Add two patches which should make it into the final 2.6.38.6 release.
 - can-add-missing-socket-check-in-can_raw_release.patch
 - scsi-fix-oops-in-scsi_run_queue.patch

* Mon May 09 2011 Chuck Ebbert <cebbert@redhat.com>
- Enable CONFIG_FB_UDL (#634636)

* Mon May 09 2011 Dave Airlie <airlied@redhat.com>
- fix dual-gpu intel/radeon laptops where intel would cause radeon crash.

* Sat May 07 2011 Chuck Ebbert <cebbert@redhat.com>
- Fix breakage of network device names on Dell systems (#702740)

* Fri May 06 2011 Dave Airlie <airlied@redhat.com> 2.6.38.5-24
- forgot the cayman PCI IDs.

* Wed May  4 2011  <lxoliva@fsfla.org> -libre
- Deblobbed drm-radeon-update.patch.

* Tue May 03 2011 Dave Airlie <airlied@redhat.com> 2.6.38.5-23
- radeon updates from 2.6.39 with cayman + fixes for lots of things including Fusion.
- vblank fix for core drm

* Mon May 02 2011 Chuck Ebbert <cebbert@redhat.com>
- [SCSI] mpt2sas: prevent heap overflows and unchecked reads
  (CVE-2011-1494, CVE-2011-1495)
- bonding: Incorrect TX queue offset (CVE-2011-1581)
- Restore reliable stack backtraces, and hopefully fix RHBZ #700718

* Mon May 02 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.5-22
- And to the released 2.6.38.5

* Sun May 01 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.5-21.rc1
- Update to stable release candidate 2.6.38.5-rc1

* Thu Apr 28 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.4-20
- [sgruszka@] Upstream fixes for iwl3945 bugs (#683571, #688252, #671366)

* Tue Apr 26 2011 Chuck Ebbert <cebbert@redhat.com>
- Another fix for ASPM powersave mode

* Mon Apr 25 2011 Jarod Wilson <jarod@redhat.com>
- ite-cir: fix modular build on powerpc (#698378)
- mceusb: add Dell-branded transceiver device ID
- nuvoton-cir: improve compatibility with lirc raw IR mode

* Mon Apr 25 2011 Neil Horman <nhorman@redhat.com>
- netconsole: fix deadlock in netdev notifier handler

* Sun Apr 24 2011 Kyle McMartin <kmcmartin@redhat.com>
- ppc64: disable TUNE_CELL, which causes problems with illegal instuctions
  being generated on non-Cell PPC machines. (#698256)

* Fri Apr 22 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.4-19
- Update to 2.6.38.4

* Fri Apr 22 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Deblobbed linux-2.6-v4l-dvb-update.patch.

* Fri Apr 22 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.3-18
- iwlwifi: fix scanning when channel changing (#688252)

* Tue Apr 19 2011 Jarod Wilson <jarod@redhat.com>
- Add basic support for full 32-bit NEC IR scancodes
- Add latest patches sent upstream for hid layer expansion and full
  support for the TiVo Slide bluetooth/hid remote
- Add a TiVo IR remote keymap, use it by default with TiVo mceusb device
- Add ite-cir driver, nuke crappy old lirc_it* drivers
- Add an initial Apple remote keymap
- Add support for more Nuvoton IR hardware variants
- Overhaul lirc_zilog refcounting so it doesn't suck so badly anymore
- Clean up myriad of Hauppauge keymaps
- Make ir-kbd-i2c pass full rc5 scancodes when it can
- Misc minor v4l/dvb fixes

* Fri Apr 15 2011 Kyle McMartin <kmcmartin@redhat.com>
- Drop x86-hibernate-initialize-mmu_cr4_features-during-boot.patch, 
  e5f15b45 was reverted in stable.

* Thu Apr 14 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.3-16
- Linux 2.6.38.3
- Drop merged patches:
    linux-2.6-x86-fix-mtrr-resume.patch
    pci-acpi-report-aspm-support-to-bios-if-not-disabled-from-command-line.patch
- Drop some obsolete patches:
    runtime_pm_fixups.patch
    drm-i915-gen4-has-non-power-of-two-strides.patch'

* Wed Apr 13 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.3-15.rc1
- Linux 2.6.38.3-rc1

* Tue Apr 12 2011 Kyle McMartin <kmcmartin@redhat.com>
- fix hibernate which was broken by 2.6.38.y (korg#32222)

* Tue Apr 12 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-2.14
- nouveau: correct lock ordering problem

* Mon Apr 11 2011 Dave Airlie <airlied@redhat.com>
- x86: add upstream patch to fix MTRR on resume - will come via stable later.

* Fri Apr 08 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-2.13
- nouveau: fix pcie nv3x (rhbz#692588)

* Thu Apr 07 2011 Hans de Goede <hdegoede@redhat.com>
- i915: Add a no lvds quirk for the Asus EB1007, this fixes gnome-shell

* Thu Apr 07 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-2.12
- nouveau: switch nv4x back to DMA32 only (rhbz#689825)

* Mon Apr 04 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-2.11
- ttm: add patch from upstream to fix a recent nouveau issue

* Thu Mar 31 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-2.10
- nouveau: nva3+ stability improvements
- nouveau: nvc0 "stutter" fixes
- nouveau: nv50/nvc0 page flipping
- nouveau: nv50 z compression

* Wed Mar 29 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38.2-9
- Downgrade SCSI sd printk's about disk caching from KERN_ERR to KERN_NOTICE
  so they don't show up in our pretty quiet boot. Ray noticed them when
  booting from a USB stick which doesn't have a cache page returned in the
  sense buffer.

* Tue Mar 29 2011 Kyle McMartin <kmcmartin@redhat.com>
- Disable CONFIG_IMA, CONFIG_TCG_TPM on powerpc (#689468)

* Tue Mar 29 2011 Kyle McMartin <kmcmartin@redhat.com>
- printk: do not mangle valid userspace syslog prefixes with
  /dev/kmsg (#691888)
  - The patch is upstream in 2.6.39, and Lennart tells me the patch has been
    backported for the next Suse release as well.
- Disable qla4xxx (CONFIG_SCSI_QLA_ISCSI) driver on powerpc32 (#686199)

* Sun Mar 27 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.2-8
- Linux 2.6.38.2
- Drop patches merged in 2.6.38.2:
   dcdbas-force-smi-to-happen-when-expected.patch
   fs-call-security_d_instantiate-in-d_obtain_alias.patch
   prevent-rt_sigqueueinfo-and-rt_tgsigqueueinfo-from-spoofing-the-signal-code.patch
- Fix more PCIe ASPM bugs:
   kworker task over 65% after resume (#683156)
   ASPM powersave mode does not get enabled

* Sat Mar 26 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.2-7.rc1
- Linux 2.6.38.2-rc1

* Fri Mar 25 2011 Chuck Ebbert <cebbert@redhat.com>
- CVE-2011-1182: kernel signal spoofing issue
- Drop unused patches already applied upstream:
  hdpvr-ir-enable.patch
  thinkpad-acpi-fix-backlight.patch

* Wed Mar 23 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38.1-6
- Linux 2.6.38.1
- Drop linux-2.6-ehci-check-port-status.patch, merged in .38.1
- Add dcdbas-force-smi-to-happen-when-expected.patch

* Wed Mar 23 2011 Kyle McMartin <kmcmartin@redhat.com>
- Re-create ACPI battery sysfs files on resume from suspend, fixes the
  upstream changes to the dropped
  acpi-update-battery-information-on-notification-0x81.patch.

* Wed Mar 23 2011 Dave Airlie <airlied@redhat.com> 2.6.38-5
- i915: add fix for 945G misrendering terminal

* Tue Mar 22 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-4
- nouveau: implement missing bios opcode 0x5c (rhbz#688569)
- nouveau: a couple of minor fixes from nouveau git

* Mon Mar 21 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-3
- Add contents of 2.6.38.1 patch queue (in git-linus.diff)

* Thu Mar 17 2011 Matthew Garrett <mjg@redhat.com> 2.6.38-2
- drop efi_default_physical.patch - it's actually setting up something that's
  neither physical nor virtual, and it's probably breaking EFI boots

* Wed Mar 16 2011 Dennis Gilmore <dennis@ausil.us>
- build sparc imagae as vmlinux
- fixes buildid conflicts since the sparc kernel is just a elf image

* Wed Mar 16 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Deblobbed to 2.6.38-libre.

* Tue Mar 15 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-1
- Linux 2.6.38

* Mon Mar 14 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Deblobbed patch-libre-2.6.38-rc8-git4 by hand.

* Mon Mar 13 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc8.git4.1
- Linux 2.6.38-rc8-git4

* Thu Mar 10 2011 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.38-rc8-git3

* Thu Mar 10 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Created patch-libre-2.6.38-rc8 by diffing deblobbed trees.

* Thu Mar 10 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc8.git2.1
- Linux 2.6.38-rc8-git2

* Wed Mar 09 2011 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.38-rc8-git1

* Wed Mar 09 2011 Dennis Gilmore <dennis@ausil.us>
- apply sparc64 gcc-4.6.0 buildfix patch

* Wed Mar 09 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-0.rc8.git0.2
- nouveau: allow max clients on nv4x (679629), better error reporting

* Tue Mar 08 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc8.git0.1
- Linux 2.6.38-rc8

* Mon Mar  7 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Created patch-libre-2.6.38-rc7 by diffing deblobbed trees.

* Sat Mar 05 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc7.git4.1
- Linux 2.6.38-rc7-git4
- Revert upstream commit e3e89cc535223433a619d0969db3fa05cdd946b8
  for now to fix utrace build.

* Fri Mar 04 2011 Roland McGrath <roland@redhat.com> - 2.6.38-0.rc7.git2.3
- Split out perf-debuginfo subpackage.

* Fri Mar 04 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc7.git2.2
- Disable drm-i915-gen4-has-non-power-of-two-strides.patch for now, breaks
  my mutter.

* Fri Mar 04 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc7.git2.1
- Linux 2.6.38-rc7-git2
- drm-i915-gen4-has-non-power-of-two-strides.patch (#681285)

* Thu Mar 03 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc7.git1.1
- Linux 2.6.38-rc7-git1

* Tue Mar 01 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc7.git0.1
- Linux 2.6.38-rc7

* Fri Feb 25 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc6.git6.1
- Linux 2.6.38-rc6-git6
- Build in virtio_pci driver so virtio_console will be built-in (#677713)

* Thu Feb 24 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc6.git4.1
- Linux 2.6.38-rc6-git4

* Thu Feb 24 2011 Matthew Garrett <mjg@redhat.com> 2.6.38-0.rc6.git2.2
- linux-2.6-acpi-fix-implicit-notify.patch: Fix implicit notify when there's
  more than one device per GPE

* Wed Feb 23 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc6.git2.1
- Linux 2.6.38-rc6-git2

* Wed Feb 23 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre Thu Feb 24
- Created patch-libre-2.6.38-rc6 by diffing deblobbed trees.

* Wed Feb 23 2011 Ben Skeggs <bskeggs@redhat.com> 2.6.38-0.rc6.git0.2
- nouveau: nv4x pciegart fixes, nvc0 accel improvements

* Tue Feb 22 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc6.git0.1
- Linux 2.6.38-rc6

* Tue Feb 22 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc5.git7.1
- Linux 2.6.38-rc5-git7

* Mon Feb 21 2011 Dave Jones <davej@redhat.com> 2.6.38-0.rc5.git6.1
- Linux 2.6.38-rc5-git6

* Sat Feb 19 2011 Chuck Ebbert <cebbert@redhat.com>  2.6.38-0.rc5.git5.1
- Linux 2.6.38-rc5-git5

* Wed Feb 16 2011 Chuck Ebbert <cebbert@redhat.com>  2.6.38-0.rc5.git1.1
- Linux 2.6.38-rc5-git1
- Add support for Airprime/Sierra USB IP modem (#676860)
- Make virtio_console built-in on x86_64 (#677713)
- Revert check for read-only block device added in .38 (#672265)

* Tue Feb 15 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc5.git0.1
- Linux 2.6.38-rc5 (81 minutes later...)

* Sun Feb 13 2011 Chuck Ebbert <cebbert@redhat.com>  2.6.38-0.rc4.git7.1
- Linux 2.6.38-rc4-git7

* Sat Feb 12 2011 Chuck Ebbert <cebbert@redhat.com>  2.6.38-0.rc4.git6.1
- Linux 2.6.38-rc4-git6
- Fix memory corruption caused by bug in bridge code.

* Thu Feb 10 2011 Chuck Ebbert <cebbert@redhat.com>  2.6.38-0.rc4.git3.1
- Linux 2.6.38-rc4-git3

* Mon Feb 07 2011 Fedora Release Engineering <rel-eng@lists.fedoraproject.org> - 2.6.38-0.rc4.git0.2
- Rebuilt for https://fedoraproject.org/wiki/Fedora_15_Mass_Rebuild

* Mon Feb 07 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc4.git0.1
- Linux 2.6.38-rc4

* Fri Feb 04 2011 Chuck Ebbert <cebbert@redhat.com>  2.6.38-0.rc3.git4.1
- Linux 2.6.38-rc3-git4

* Thu Feb 03 2011 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.38-rc3-git3
- Enable Advansys SCSI driver on x86_64 (#589115)

* Thu Feb 03 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc3.git2.1
- Linux 2.6.38-rc3-git2 snapshot
- [sgruszka] ath5k: fix fast channel change (#672778)

* Wed Feb 02 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc3.git1.1
- Linux 2.6.38-rc3-git1 snapshot.

* Wed Feb 02 2011 Chuck Ebbert <cebbert@redhat.com>
- Fix autoload of atl1c driver for latest hardware (#607499)

* Tue Feb 01 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc3.git0.1
- Linux 2.6.38-rc3
- Try to fix some obvious bugs in hfsplus mount failure handling (#673857)

* Mon Jan 31 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc2.git9.1
- Linux 2.6.38-rc2-git9

* Mon Jan 31 2011 Kyle McMartin <kmcmartin@redhat.com>
- disable CONFIG_SERIAL_8250_DETECT_IRQ (from mschmidt@redhat.com)

* Mon Jan 31 2011 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.38-rc2-git8
- Add Trond's NFS bugfixes branch from git.linux-nfs.org

* Mon Jan 31 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc2.git7.2
- Fix build failure on s390.

* Fri Jan 28 2011 Chuck Ebbert <cebbert@redhat.com> 2.6.38-0.rc2.git7.1
- Linux 2.6.38-rc2-git7

* Wed Jan 26 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc2.git5.1
- Linux 2.6.38-rc2-git5
- [x86] Re-enable TRANSPARENT_HUGEPAGE, should be fixed by cacf061c.

* Tue Jan 25 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc2.git3.2
- [x86] Disable TRANSPARENT_HUGEPAGE for now, there be dragons there.

* Tue Jan 25 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc2.git3.1
- Linux 2.6.38-rc2-git3
- perf-gcc460-build-fixes.patch: fix context from [9486aa38]

* Mon Jan 24 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc2.git1.3
- Disable usb/pci/acpi autosuspend goo until it can be checked.

* Mon Jan 24 2011 Kyle McMartin <kmcmartin@redhat.com>
- debug-tty-print-dev-name.patch: drop, haven't seen any warnings recently.
- runtime_pm_fixups.patch: rebase and re-enable, make acpi_power_transition
   in pci_bind actually do the right thing instead of (likely) always
   trying to transition to D0.

* Mon Jan 24 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc2.git1.1
- Linux 2.6.38-rc2-git1
- [e5cce6c1] tpm: fix panic caused by "tpm: Autodetect itpm devices"
  may fix some boot issues people were having.
- tpm-fix-stall-on-boot.patch: upstream.
- perf-gcc460-build-fixes.patch: fix build issues with warn-unused-but-set
  in gcc 4.6.0

* Sat Jan 22 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc2.git0.1
- Linux 2.6.38-rc2
- linux-2.6-serial-460800.patch, drivers/serial => drivers/tty/serial

* Thu Jan 20 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc1.git1.1
- Linux 2.6.38-rc1-git1, should fix boot failure in -rc1.

* Wed Jan 19 2011 Roland McGrath <roland@redhat.com>
- utrace update

* Wed Jan 19 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc1.git0.1
- Linux 2.6.38-rc1

* Tue Jan 18 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc0.git18.1
- Linux 2.6.37-git18

* Mon Jan 17 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc0.git16.1
- Linux 2.6.37-git16
- config changes:
 - CONFIG_SQUASHFS_XZ=y [generic]
 - CONFIG_SPARSE_IRQ=y [arm]

* Sat Jan 15 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc0.git13.1
- Linux 2.6.37-git13
- Drop xen_export-arbitrary_virt_to_machine.patch, upstream.

* Fri Jan 14 2011 Kyle McMartin <kmcmartin@redhat.com>
- xen_export-arbitrary_virt_to_machine.patch: pull patch from upstream
  to fix build error.

* Fri Jan 14 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc0.git12.1
- Linux 2.6.37-git12
- 0001-use-__devexit-not-__exit-in-n2_unregister_algs-fixes.patch: drop
  upstream patch.
- acpi-update-battery-information-on-notification-0x81.patch: drop upstream
  patch.
- mm-*.patch: drop upstream patches.
- important config changes:
  ACPI_IPMI=m
  CRYPTO_AES_NI_INTEL=m [i386]
  TRANSPARENT_HUGEPAGE=y

* Wed Jan 12 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc0.git9.1
- Linux 2.6.37-git9
- Re-enable DEBUG_SET_MODULE_RONX since commit 94462ad3 fixed it.
- Enable some more new random HID and sensor junk as modules.

* Tue Jan 11 2011 Matthew Garrett <mjg@redhat.com>
- linux-2.6-ehci-check-port-status.patch - fix USB resume on some AMD systems

* Mon Jan 10 2011 Jarod Wilson <jarod@redhat.com>
- Add support for local rebuild config option overrides
- Add missing --with/--without pae build flag support

* Mon Jan 10 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc0.git4.2
- Disable DEBUG_SET_MODULE_RONX for now, it causes boot-up to fail since
  dynamic ftrace is trying to rewrite instructions on module load.

* Mon Jan 10 2011 Kyle McMartin <kmcmartin@redhat.com>
- Drop obsolete linux-2.6-debug-nmi-timeout.patch

* Mon Jan 10 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.38-0.rc0.git4.1
- Branch for 2.6.38
- Rebase trivial patches.
- Switch debug configs back on.
- config changes:
  DEBUG_SET_MODULE_RONX=y
  B43_PHY_N=y
  RT2800USB_RT33XX=y |
  RT2800PCI_RT33XX=y | experimental
  WL12XX=m
  RTL8192CE=m
  CAN_SLCAN=m
  SCHED_AUTOGROUP=n

* Fri Jan 07 2011 Kyle McMartin <kmcmartin@redhat.com> 2.6.37-2
- drm_i915-check-eDP-encoder-correctly-when-setting-modes.patch reported to
  fix HP/Sony eDP issues by adamw and airlied.

* Wed Jan 05 2011 Dennis Gilmore <dennis@ausil.us>
- build sparc headers on sparcv9

* Tue Jan 04 2011 Dennis Gilmore <dennis@ausil.us>
- add patch for sparc build failure

* Tue Jan 04 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre Wed Jan 05
- Deblobbed to 2.6.37-libre.

* Tue Jan 04 2011 Kyle McMartin <kyle@redhat.com> 2.6.37-1
- Track release of 2.6.37

* Mon Jan 03 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Deblobbed patch-libre-2.6.37-rc8.

* Mon Jan 03 2011 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc8.git3.1
- Linux 2.6.37-rc8-git3
- Merged acpi battery notification patch and -rc8.

* Thu Dec 23 2010 Kyle McMartin <kyle@redhat.com>
- Pull in flexcop procfs rename patch since it's still not upstream. (#664852)

* Tue Dec 21 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre Tue Dec 28
- Deblobbed patch-libre-2.6.37-rc7.

* Tue Dec 21 2010 Kyle McMartin <kyle@redhat.com> 2.6.37.0.rc7.git0.2
- Linux 2.6.37-rc7
- CONFIG_USB_OTG=n (seems unnecessary?)
- Enable release builds until .37 releases in rawhide.

* Sun Dec 19 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc6.git5.1
- Linux 2.6.37-rc6-git5
- sched-cure-more-NO_HZ-load-average-woes.patch: upstream.

* Sat Dec 18 2010 Kyle McMartin <kyle@redhat.com>
- Patch from nhorman against f13:
  Enhance AF_PACKET to allow non-contiguous buffer alloc (#637619)

* Sat Dec 18 2010 Kyle McMartin <kyle@redhat.com>
- Fix SELinux issues with NFS/btrfs and/or xfsdump. (#662344)

* Fri Dec 17 2010 Matthew Garrett <mjg@redhat.com> 2.6.37-0.rc6.git0.3
- efi_default_physical.patch: Revert hunk that breaks boot
- linux-next-macbook-air-input.patch: Add input support for new Macbook Airs

* Thu Dec 16 2010 Matthew Garrett <mjg@redhat.com> 2.6.37-0.rc6.git0.2
- applesmc_update.patch: Make the driver more generic. Should help Apples.
- apple_backlight.patch: Make sure that this loads on all hardware.
- efifb_update.patch: Fixes for the 11 inch Macbook Air
- acpi_reboot.patch: Should make reboot work better on most hardware
- efi_default_physical.patch: Some machines dislike EFI virtual mode

* Wed Dec 15 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc6.git0.1
- Linux 2.6.37-rc6
- Re-activate acpi patch which rejected on the previous commit.

* Wed Dec 15 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc5.git5.1
- 2.6.37-rc5-git5

* Fri Dec 10 2010 Kyle McMartin <kyle@redhat.com>
- Another patch from mjg59: Set _OSC supported field correctly (#638912)

* Fri Dec 10 2010 Kyle McMartin <kyle@redhat.com>
- pci-disable-aspm-if-bios-asks-us-to.patch: Patch from mjg59 to disable
  ASPM if the BIOS has disabled it, but enabled it already on some devices.

* Thu Dec 09 2010 Kyle McMartin <kyle@redhat.com>
- Snarf patch from wireless-next to fix mdomsch's orinico wifi.
  (orinoco: initialise priv->hw before assigning the interrupt)
  [229bd792] (#657864)

* Thu Dec 09 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre Mon Dec 27
- Deblobbed patch-libre-2.6.37-rc5.

* Wed Dec 08 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc5.git2.1
- Linux 2.6.37-rc5-git2
- sched-cure-more-NO_HZ-load-average-woes.patch: fix some of the complaints
  in 2.6.35+ about load average with dynticks. (rhbz#650934)

* Tue Dec 07 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc5.git0.1
- Linux 2.6.37-rc5

* Sat Dec 04 2010 Kyle McMartin <kyle@redhat.com>
- Enable C++ symbol demangling with perf by linking against libiberty.a,
  which is LGPL2.

* Fri Dec 03 2010 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.37-rc4-git3
- Enable HP ILO on x86_64 for (#571329)
- Drop merged drm-fixes.patch, split out edp-fixes.
- tty-dont-allow-reopen-when-ldisc-is-changing.patch: upstream.
- tty-ldisc-fix-open-flag-handling.patch: upstream.
- Enable CIFS_ACL.

* Thu Dec 02 2010 Kyle McMartin <kyle@redhat.com>
- Grab some of Mel's fixes from -mmotm to hopefully sort out #649694.

* Wed Dec 01 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc4.git1.1
- Linux 2.6.37-rc4-git1
- Pull in DRM fixes that are queued for -rc5 [3074adc8]
  + edp-fixes on top

* Tue Nov 30 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc4.git0.1
- Linux 2.6.37-rc4

* Mon Nov 29 2010 Kyle McMartin <kyle@redhat.com>
- Update debug-vm-would_have_oomkilled patch.

* Mon Nov 29 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc3.git6.1
- Linux 2.6.37-rc3-git6
- TTY: open/hangup race fixup (rhbz#630464)

* Fri Nov 26 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc3.git3.1
- Linux 2.6.37-rc3-git3
- Print tty->flags as well in debugging patch...

* Fri Nov 26 2010 Kyle McMartin <kyle@redhat.com>
- Copy tty_open WARN_ON debugging patch from rawhide.

* Fri Nov 26 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc3.git2.1
- Linux 2.6.37-rc3-git2
- CGROUP_MEM_RES_CTLR_SWAP_ENABLED is not set, so the cgroup memory
  resource controller swap accounting is disabled by default. You can
  enable it with 'swapaccount' if desired.
- TTY: don't allow reopen when ldisc is changing (rhbz#630464)

* Wed Nov 24 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc3.git1.1
- Linux 2.6.37-rc3-git1

* Mon Nov 22 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc3.git0.1
- Linux 2.6.37-rc3

* Sat Nov 20 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc2.git7.1
- Linux 2.6.37-rc2-git7

* Fri Nov 19 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.rc2.git5.1
- Linux 2.6.37-rc2-git5

* Thu Nov 18 2010 Kyle McMartin <kyle@redhat.com>
- Move %{fedora_build} in the un-released (ie: -git/-rc) kernel case for
  a variety of reasons, principally so that:
  1: Bumping %baserelease isn't needed if we're just updating snapshots.
  2: %buildid will sort as newer so we don't need to bump baserelease when
     building bugzilla fixes.

* Wed Nov 17 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc2.git2
- Linux 2.6.37-rc2-git2
- enable STRICT_DEVMEM on s390x.

* Wed Nov 17 2010 Kyle McMartin <kyle@redhat.com>
- Make vmlinuz/System.map root read-write only by default. You can just
  chmod 644 them later if you (unlikely) need them without root.

* Mon Nov 15 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc2.git0
- Linux 2.6.37-rc2

* Sat Nov 13 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc1.git10
- Linux 2.6.37-rc1-git10
- SECURITY_DMESG_RESTRICT added, the principle of least surprise dictates
  we should probably have it off. If you want to restrict dmesg access
  you may use the kernel.dmesg_restrict sysctl.
- linux-2.6-bluetooth-autosuspend.patch: merged upstream.

* Tue Nov 09 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc1.git7
- Linux 2.6.37-rc1-git7

* Mon Nov 08 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc1.git5
- Linux 2.6.37-rc1-git5

* Mon Nov 08 2010 Kyle McMartin <kyle@redhat.com>
- Cherry-pick utrace-ptrace fixes from mayoung. Thanks!

* Tue Nov 02 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc1.git0
- Linux 2.6.37-rc1

* Tue Oct 26 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc0.git8
- Linux 2.6.36-git8

* Fri Oct 22 2010 Kyle McMartin <kyle@redhat.com> 2.6.37-0.1.rc0.git2
- Switch to tracking git snapshots of what will become 2.6.37.
- Fix context rejects in utrace and a few other patches.

* Thu Oct 21 2010 Alexandre Oliva <lxoliva@fsfla.org> -libre
* Deblobbed 2.6.36-libre.

* Wed Oct 20 2010 Chuck Ebbert <cebbert@redhat.com> 2.6.36-1
- Linux 2.6.36

###
# The following Emacs magic makes C-c C-e use UTC dates.
# Local Variables:
# rpm-change-log-uses-utc: t
# End:
###
