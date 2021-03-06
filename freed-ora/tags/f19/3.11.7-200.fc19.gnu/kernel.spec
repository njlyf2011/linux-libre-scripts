# We have to override the new %%install behavior because, well... the kernel is special.
%global __spec_install_pre %{___build_pre}

Summary: The Linux kernel

# For a stable, released kernel, released_kernel should be 1. For rawhide
# and/or a kernel built from an rc or git snapshot, released_kernel should
# be 0.
%global released_kernel 1

# Sign modules on x86.  Make sure the config files match this setting if more
# architectures are added.
%ifarch %{ix86} x86_64
%global signmodules 1
%else
%global signmodules 0
%endif

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
# NOTE: baserelease must be > 0 or bad things will happen if you switch
#       to a released kernel (released version will be < rc version)
#
# For non-released -rc kernels, this will be appended after the rcX and
# gitX tags, so a 3 here would become part of release "0.rcX.gitX.3"
#
%global baserelease 200
%global fedora_build %{baserelease}

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 3.1-rc7-git1 starts with a 3.0 base,
# which yields a base_sublevel of 0.
%define base_sublevel 11

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
#define librev

%define baselibre -libre
%define basegnu -gnu%{?librev}

# To be inserted between "patch" and "-2.6.".
#define stablelibre -3.11%{?stablegnux}
#define rcrevlibre -3.11%{?rcrevgnux}
#define gitrevlibre -3.11%{?gitrevgnux}

%if 0%{?stablelibre:1}
%define stablegnu -gnu%{?librev}
%endif
%if 0%{?rcrevlibre:1}
%define rcrevgnu -gnu%{?librev}
%endif
%if 0%{?gitrevlibre:1}
%define gitrevgnu -gnu%{?librev}
%endif

%if !0%{?stablegnux:1}
%define stablegnux %{?stablegnu}
%endif
%if !0%{?rcrevgnux:1}
%define rcrevgnux %{?rcrevgnu}
%endif
%if !0%{?gitrevgnux:1}
%define gitrevgnux %{?gitrevgnu}
%endif

# libres (s for suffix) may be bumped for rebuilds in which patches
# change but fedora_build doesn't.  Make sure there's a dot after
# librev if a libre build count is to follow.  It is appended after
# dist.
%define libres .gnu%{?librev}

## If this is a released kernel ##
%if 0%{?released_kernel}

# Do we have a -stable update to apply?
%define stable_update 7
# Is it a -stable RC?
%define stable_rc 0
# Set rpm version accordingly
%if 0%{?stable_update}
%define stablerev %{stable_update}
%define stable_base %{stable_update}
%if 0%{?stable_rc}
# stable RCs are incremental patches, so we need the previous stable patch
%define stable_base %(echo $((%{stable_update} - 1)))
%endif
%endif
%define rpmversion 3.%{base_sublevel}.%{stable_update}

## The not-released-kernel case ##
%else
# The next upstream release sublevel (base_sublevel+1)
%define upstream_sublevel %(echo $((%{base_sublevel} + 1)))
# The rc snapshot level
%define rcrev 0
# The git snapshot level
%define gitrev 0
# Set rpm version accordingly
%define rpmversion 3.%{upstream_sublevel}.0
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
# kernel PAE (only valid for i686 (PAE) and ARM (lpae))
%define with_pae       %{?_without_pae:       0} %{?!_without_pae:       1}
# kernel-debug
%define with_debug     %{?_without_debug:     0} %{?!_without_debug:     1}
# kernel-doc
%define with_doc       %{?_without_doc:       0} %{?!_without_doc:       1}
# kernel-headers
%define with_headers   %{?_without_headers:   0} %{?!_without_headers:   1}
# kernel-firmware
%define with_firmware  %{?_with_firmware:     1} %{?!_with_firmware:     0}
# perf
%define with_perf      %{?_without_perf:      0} %{?!_without_perf:      1}
# tools
%define with_tools     %{?_without_tools:     0} %{?!_without_tools:     1}
# kernel-debuginfo
%define with_debuginfo %{?_without_debuginfo: 0} %{?!_without_debuginfo: 1}
# kernel-bootwrapper (for creating zImages from kernel + initrd)
%define with_bootwrapper %{?_without_bootwrapper: 0} %{?!_without_bootwrapper: 1}
# Want to build a the vsdo directories installed
%define with_vdso_install %{?_without_vdso_install: 0} %{?!_without_vdso_install: 1}
#
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
#
# should we do C=1 builds with sparse
%define with_sparse    %{?_with_sparse:       1} %{?!_with_sparse:       0}
#
# build a release kernel on rawhide
%define with_release   %{?_with_release:      1} %{?!_with_release:      0}

# Set debugbuildsenabled to 1 for production (build separate debug kernels)
#  and 0 for rawhide (all kernels are debug kernels).
# See also 'make debug' and 'make release'.
%define debugbuildsenabled 1

# Want to build a vanilla kernel build without any non-upstream patches?
%define with_vanilla %{?_with_vanilla: 1} %{?!_with_vanilla: 0}

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

# pkg_release is what we'll fill in for the rpm Release: field
%if 0%{?released_kernel}

%if 0%{?stable_rc}
%define stable_rctag .rc%{stable_rc}
%define pkg_release 0%{stable_rctag}.%{fedora_build}%{?buildid}%{?dist}%{?libres}
%else
%define pkg_release %{fedora_build}%{?buildid}%{?dist}%{?libres}
%endif

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
%define kversion 3.%{base_sublevel}

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

# kernel PAE is only built on i686 and ARMv7.
%ifnarch i686 armv7hl
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
%define with_tools 0
%define with_perf 0
%endif

%define all_x86 i386 i686

%if %{with_vdso_install}
# These arches install vdso/ directories.
%define vdso_arches %{all_x86} x86_64 ppc ppc64 ppc64p7 s390 s390x
%endif

# Overrides for generic default options

# only ppc needs a separate smp kernel
%ifnarch ppc 
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
%define with_tools 0
%define with_perf 0
%define all_arch_configs kernel-%{version}-*.config
%define with_firmware  %{?_without_firmware:     0} %{?!_without_firmware:     1}
%endif

# bootwrapper is only on ppc
%ifnarch ppc ppc64 ppc64p7
%define with_bootwrapper 0
%endif

# sparse blows up on ppc64 and sparc64
%ifarch ppc64 ppc ppc64p7
%define with_sparse 0
%endif

# Per-arch tweaks

%ifarch %{all_x86}
%define asmarch x86
%define hdrarch i386
%define pae PAE
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

%ifarch ppc64 ppc64p7
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
%define with_tools 0
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

%ifarch %{arm}
%define all_arch_configs kernel-%{version}-arm*.config
%define image_install_path boot
%define asmarch arm
%define hdrarch arm
%define pae lpae
%define make_target bzImage
%define kernel_image arch/arm/boot/zImage
# http://lists.infradead.org/pipermail/linux-arm-kernel/2012-March/091404.html
%define kernel_mflags KALLSYMS_EXTRA_PASS=1
# we only build headers/perf/tools on the base arm arches
# just like we used to only build them on i386 for x86
%ifnarch armv7hl
%define with_headers 0
%define with_perf 0
%define with_tools 0
%endif
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
%define nobuildarches i386 s390

%ifarch %nobuildarches
%define with_up 0
%define with_smp 0
%define with_pae 0
%define with_debuginfo 0
%define with_perf 0
%define with_tools 0
%define _enable_debug_packages 0
%endif

%define with_pae_debug 0
%if %{with_pae}
%define with_pae_debug %{with_debug}
%endif

# Architectures we build tools/cpupower on
%define cpupowerarchs %{ix86} x86_64 ppc ppc64 ppc64p7 %{arm}

#
# Three sets of minimum package version requirements in the form of Conflicts:
# to versions below the minimum
#

#
# First the general kernel 2.6 required versions as per
# Documentation/Changes
#
%define kernel_dot_org_conflicts  ppp < 2.4.3-3, isdn4k-utils < 3.2-32, nfs-utils < 1.2.5-7.fc17, e2fsprogs < 1.37-4, util-linux < 2.12, jfsutils < 1.1.7-2, reiserfs-utils < 3.6.19-2, xfsprogs < 2.6.13-4, procps < 3.2.5-6.3, oprofile < 0.9.1-2, device-mapper-libs < 1.02.63-2, mdadm < 3.2.1-5

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
%define kernel_prereq  fileutils, module-init-tools >= 3.16-4, initscripts >= 8.11.1-1, grubby >= 8.3-1
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
Provides: kernel-libre-drm = 4.3.0\
Provides: kernel-drm-nouveau = 16\
Provides: kernel-libre-drm-nouveau = 16\
Provides: kernel-modeset = 1\
Provides: kernel-libre-modeset = 1\
Provides: kernel-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-highbank\
Provides: kernel-libre-highbank\
Provides: kernel-highbank-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-highbank-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-omap\
Provides: kernel-libre-omap\
Provides: kernel-omap-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-omap-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-tegra\
Provides: kernel-libre-tegra\
Provides: kernel-tegra-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-tegra-uname-r = %{KVERREL}%{?1:.%{1}}\
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
ExclusiveArch: noarch %{all_x86} x86_64 ppc ppc64 ppc64p7 s390 s390x %{arm}
ExclusiveOS: Linux

%kernel_reqprovconf

#
# List the packages used during the kernel build
#
BuildRequires: module-init-tools, patch >= 2.5.4, bash >= 2.03, sh-utils, tar
BuildRequires: bzip2, xz, findutils, gzip, m4, perl, make >= 3.78, diffutils, gawk
BuildRequires: gcc >= 3.4.2, binutils >= 2.12, redhat-rpm-config, hmaccalc
BuildRequires: net-tools, hostname, bc
BuildRequires: xmlto, asciidoc
%if %{with_sparse}
BuildRequires: sparse >= 0.4.1
%endif
%if %{with_perf}
BuildRequires: elfutils-devel zlib-devel binutils-devel newt-devel python-devel perl(ExtUtils::Embed) bison
BuildRequires: audit-libs-devel
%endif
%if %{with_tools}
BuildRequires: pciutils-devel gettext
%endif
BuildConflicts: rhbuildsys(DiskFree) < 500Mb
%if %{with_debuginfo}
# Fancy new debuginfo generation introduced in Fedora 8/RHEL 6.
# The -r flag to find-debuginfo.sh invokes eu-strip --reloc-debug-sections
# which reduces the number of relocations in kernel module .ko.debug files and
# was introduced with rpm 4.9 and elfutils 0.153.
BuildRequires: rpm-build >= 4.9.0-1, elfutils >= elfutils-0.153-1
%define debuginfo_args --strict-build-id -r
%endif

%if %{signmodules}
BuildRequires: openssl
BuildRequires: pesign >= 0.10-4
%endif

Source0: http://linux-libre.fsfla.org/pub/linux-libre/freed-ora/src/linux%{?baselibre}-%{kversion}%{basegnu}.tar.xz

# For documentation purposes only.
Source3: deblob-main
Source4: deblob-check
Source5: deblob-%{kversion}
#Source6: deblob-3.%{upstream_sublevel}

%if %{signmodules}
Source11: x509.genkey
%endif

Source15: merge.pl
Source16: mod-extra.list
Source17: mod-extra.sh
Source18: mod-sign.sh
%define modsign_cmd %{SOURCE18}

Source19: Makefile.release
Source20: Makefile.config
Source21: config-debug
Source22: config-nodebug
Source23: config-generic

Source30: config-x86-generic
Source31: config-i686-PAE
Source32: config-x86-32-generic

Source40: config-x86_64-generic

Source50: config-powerpc-generic
Source51: config-powerpc32-generic
Source52: config-powerpc32-smp
Source53: config-powerpc64
Source54: config-powerpc64p7

Source70: config-s390x

# Unified ARM kernels
Source100: config-arm-generic
Source101: config-armv7-generic
Source102: config-armv7
Source103: config-armv7-lpae

# This file is intentionally left empty in the stock kernel. Its a nicety
# added for those wanting to do custom rebuilds with altered config opts.
Source1000: config-local

# Sources for kernel-libre-tools
Source2000: cpupower.service
Source2001: cpupower.config

# Here should be only the patches up to the upstream canonical Linus tree.

# For a stable release kernel
%if 0%{?stable_update}
%if 0%{?stable_base}
%define    stable_patch_00  patch%{?stablelibre}-3.%{base_sublevel}.%{stable_base}%{?stablegnu}.xz
Patch00: %{stable_patch_00}
%endif
%if 0%{?stable_rc}
%define    stable_patch_01  patch%{?rcrevlibre}-3.%{base_sublevel}.%{stable_update}-rc%{stable_rc}%{?rcrevgnu}.xz
Patch01: %{stable_patch_01}
%endif

# non-released_kernel case
# These are automagically defined by the rcrev and gitrev values set up
# near the top of this spec file.
%else
%if 0%{?rcrev}
Patch00: patch%{?rcrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}%{?rcrevgnu}.xz
%if 0%{?gitrev}
Patch01: patch%{?gitrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}%{?gitrevgnu}.xz
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
Patch00: patch%{?gitrevlibre}-3.%{base_sublevel}-git%{gitrev}%{?gitrevgnu}.xz
%endif
%endif
%endif

%if %{using_upstream_branch}
### BRANCH PATCH ###
%endif

# we also need compile fixes for -vanilla
Patch04: compile-fixes.patch

# build tweak for build ID magic, even for -vanilla
Patch05: makefile-after_link.patch

Patch07: freedo.patch

%if !%{nopatches}


# revert upstream patches we get via other methods
Patch09: upstream-reverts.patch
# Git trees.

# Standalone patches

Patch100: taint-vbox.patch

Patch110: vmbugon-warnon.patch

Patch390: defaults-acpi-video.patch
Patch396: acpi-sony-nonvs-blacklist.patch

Patch450: input-kill-stupid-messages.patch
Patch452: no-pcspkr-modalias.patch

Patch460: serial-460800.patch

Patch470: die-floppy-die.patch

Patch510: silence-noise.patch
Patch530: silence-fbcon-logo.patch

Patch800: crash-driver.patch

# crypto/

# secure boot
Patch1000: devel-pekey-secure-boot-20130502.patch

# virt + ksm patches

# DRM
#atch1700: drm-edid-try-harder-to-fix-up-broken-headers.patch
#Patch1800: drm-vgem.patch
# nouveau + drm fixes
# intel drm is all merged upstream
Patch1824: drm-intel-next.patch
Patch1825: drm-i915-dp-stfu.patch
# radeon drm fix

# Quiet boot fixes
# silence the ACPI blacklist code
Patch2802: silence-acpi-blacklist.patch

# media patches
Patch2899: v4l-dvb-fixes.patch
Patch2900: v4l-dvb-update.patch
Patch2901: v4l-dvb-experimental.patch

# fs fixes

# NFSv4

# patches headed upstream
Patch10000: fs-proc-devtree-remove_proc_entry.patch

Patch12016: disable-i8042-check-on-apple-mac.patch

Patch14000: hibernate-freeze-filesystems.patch

Patch14010: lis3-improve-handling-of-null-rate.patch

Patch15000: nowatchdog-on-virt.patch

# ARM
Patch21000: arm-export-read_current_timer.patch

# lpae
Patch21001: arm-lpae-ax88796.patch

# ARM omap
Patch21003: arm-omap-load-tfp410.patch

# ARM tegra
Patch21005: arm-tegra-usb-no-reset-linux33.patch

#rhbz 754518
Patch21235: scsi-sd_revalidate_disk-prevent-NULL-ptr-deref.patch

# https://fedoraproject.org/wiki/Features/Checkpoint_Restore
Patch21242: criu-no-expert.patch

#rhbz 892811
Patch21247: ath9k_rx_dma_stop_check.patch

Patch22000: weird-root-dentry-name-debug.patch
Patch22010: debug-idle-sched-warn-once.patch

#rhbz 927469
Patch23006: fix-child-thread-introspection.patch

Patch25047: drm-radeon-Disable-writeback-by-default-on-ppc.patch

#rhbz 977040
Patch25056: iwl3945-better-skb-management-in-rx-path.patch
Patch25057: iwl4965-better-skb-management-in-rx-path.patch

#rhbz 963715
Patch25077: media-cx23885-Fix-TeVii-S471-regression-since-introduction-of-ts2020.patch

#CVE-2013-4345 rhbz 1007690 1009136
Patch25104: ansi_cprng-Fix-off-by-one-error-in-non-block-size-request.patch

#rhbz 985522
Patch25107: ntp-Make-periodic-RTC-update-more-reliable.patch

#rhbz 1010431
Patch25108: Revert-rt2x00pci-Use-PCI-MSIs-whenever-possible.patch

#rhbz 971893
Patch25109: bonding-driver-alb-learning.patch

#rhbz 902012
Patch25114: elevator-Fix-a-race-in-elevator-switching-and-md.patch
Patch25115: elevator-acquire-q-sysfs_lock-in-elevator_change.patch

#rhbz 974072
Patch25117: rt2800-add-support-for-rf3070.patch

#rhbz 1015989
Patch25122: netfilter-nf_conntrack-use-RCU-safe-kfree-for-conntr.patch

#rhbz 982153
Patch25123: iommu-Remove-stack-trace-from-broken-irq-remapping-warning.patch

#rhbz 998732
Patch25125: vfio-iommu-Fixed-interaction-of-VFIO_IOMMU_MAP_DMA.patch

#rhbz 896695
Patch25126: 0001-iwlwifi-don-t-WARN-on-host-commands-sent-when-firmwa.patch
Patch25127: 0002-iwlwifi-don-t-WARN-on-bad-firmware-state.patch

#rhbz 993744
Patch25128: dm-cache-policy-mq_fix-large-scale-table-allocation-bug.patch

#rhbz 1000439
Patch25129: cpupower-Fix-segfault-due-to-incorrect-getopt_long-a.patch

#rhbz 1010679
Patch25130: fix-radeon-sound.patch

#rhbz 1011714
Patch25131: btrfs-relocate-csums-properly-with-prealloc-ext.patch

#rhbz 984696
Patch25132: rt2800usb-slow-down-TX-status-polling.patch

#rhbz 1015558
Patch25133: fix-buslogic.patch

#rhbz 1023413
Patch25135: alps-Support-for-Dell-XT2-model.patch

#rhbz 1011621
Patch25137: cifs-Allow-LANMAN-auth-for-unencapsulated-auth-methods.patch

#rhbz 995782
Patch25138: intel-3.12-stable-fixes.patch

#CVE-2013-4348 rhbz 1007939 1025647
Patch25139: net-flow_dissector-fail-on-evil-iph-ihl.patch

#rhbz 1010603
Patch25140: 0001-Revert-epoll-use-freezable-blocking-call.patch
Patch25141: 0001-Revert-select-use-freezable-blocking-call.patch

#rhbz 1025769
Patch25142: iwlwifi-dvm-dont-override-mac80211-queue-setting.patch

# END OF PATCH DEFINITIONS

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
Requires: gzip binutils
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
This package contains the perf tool, which enables performance monitoring
of the Linux kernel.

%package -n perf-libre-debuginfo
Provides: perf-debuginfo = %{rpmversion}-%{pkg_release}
Summary: Debug information for package perf-libre
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n perf-libre-debuginfo
This package provides debug information for the perf package.

# Note that this pattern only works right to match the .build-id
# symlinks because of the trailing nonmatching alternation and
# the leading .*, because of find-debuginfo.sh's buggy handling
# of matching the pattern against the symlinks file.
%{expand:%%global debuginfo_args %{?debuginfo_args} -p '.*%%{_bindir}/perf(\.debug)?|.*%%{_libexecdir}/perf-core/.*|XXX' -o perf-debuginfo.list}

%package -n python-perf-libre
Provides: python-perf = %{rpmversion}-%{pkg_release}
Summary: Python bindings for apps which will manipulate perf events
Group: Development/Libraries
%description -n python-perf-libre
The python-perf-libre package contains a module that permits applications
written in the Python programming language to use the interface
to manipulate perf events.

%{!?python_sitearch: %global python_sitearch %(%{__python} -c "from distutils.sysconfig import get_python_lib; print get_python_lib(1)")}

%package -n python-perf-libre-debuginfo
Provides: python-perf = %{rpmversion}-%{pkg_release}
Summary: Debug information for package perf python bindings
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n python-perf-libre-debuginfo
This package provides debug information for the perf python bindings.

# the python_sitearch macro should already be defined from above
%{expand:%%global debuginfo_args %{?debuginfo_args} -p '.*%%{python_sitearch}/perf.so(\.debug)?|XXX' -o python-perf-debuginfo.list}


%endif # with_perf

%if %{with_tools}
%package -n kernel-libre-tools
Provides: kernel-tools = %{rpmversion}-%{pkg_release}
Summary: Assortment of tools for the Linux kernel
Group: Development/System
License: GPLv2
Provides:  cpupowerutils = 1:009-0.6.p1
Obsoletes: cpupowerutils < 1:009-0.6.p1
Provides:  cpufreq-utils = 1:009-0.6.p1
Provides:  cpufrequtils = 1:009-0.6.p1
Obsoletes: cpufreq-utils < 1:009-0.6.p1
Obsoletes: cpufrequtils < 1:009-0.6.p1
Obsoletes: cpuspeed < 1:1.5-16
Requires: kernel-libre-tools-libs = %{version}-%{release}
%description -n kernel-libre-tools
This package contains the tools/ directory from the kernel source
and the supporting documentation.

%package -n kernel-libre-tools-libs
Provides: kernel-tools-libs = %{rpmversion}-%{pkg_release}
Summary: Libraries for the kernels-tools
Group: Development/System
License: GPLv2
%description -n kernel-libre-tools-libs
This package contains the libraries built from the tools/ directory
from the kernel source.

%package -n kernel-libre-tools-libs-devel
Provides: kernel-tools-libs-devel = %{rpmversion}-%{pkg_release}
Summary: Assortment of tools for the Linux kernel
Group: Development/System
License: GPLv2
Requires: kernel-libre-tools = %{version}-%{release}
Provides:  cpupowerutils-devel = 1:009-0.6.p1
Obsoletes: cpupowerutils-devel < 1:009-0.6.p1
Requires: kernel-libre-tools-libs = %{version}-%{release}
Provides: kernel-libre-tools-devel
Provides: kernel-tools-devel
%description -n kernel-libre-tools-libs-devel
This package contains the development files for the tools/ directory from
the kernel source.

%package -n kernel-libre-tools-debuginfo
Provides: kernel-tools-debuginfo = %{rpmversion}-%{pkg_release}
Summary: Debug information for package kernel-libre-tools
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n kernel-libre-tools-debuginfo
This package provides debug information for package kernel-libre-tools.

# Note that this pattern only works right to match the .build-id
# symlinks because of the trailing nonmatching alternation and
# the leading .*, because of find-debuginfo.sh's buggy handling
# of matching the pattern against the symlinks file.
%{expand:%%global debuginfo_args %{?debuginfo_args} -p '.*%%{_bindir}/centrino-decode(\.debug)?|.*%%{_bindir}/powernow-k8-decode(\.debug)?|.*%%{_bindir}/cpupower(\.debug)?|.*%%{_libdir}/libcpupower.*|.*%%{_bindir}/turbostat(\.debug)?|.*%%{_bindir}/x86_energy_perf_policy(\.debug)?|XXX' -o kernel-tools-debuginfo.list}

%endif # with_tools


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
# This macro creates a kernel-<subpackage>-modules-extra package.
#	%%kernel_modules_extra_package <subpackage> <pretty-name>
#
%define kernel_modules_extra_package() \
%package %{?1:%{1}-}modules-extra\
Summary: Extra kernel modules to match the %{?2:%{2} }kernel\
Group: System Environment/Kernel\
Provides: kernel%{?1:-%{1}}-modules-extra-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-libre%{?1:-%{1}}-modules-extra-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-modules-extra-%{_target_cpu} = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-libre-modules-extra-%{_target_cpu} = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-modules-extra = %{version}-%{release}%{?1:.%{1}}\
Provides: kernel-libre-modules-extra = %{version}-%{release}%{?1:.%{1}}\
Provides: installonlypkg(kernel-module)\
Provides: installonlypkg(kernel-libre-module)\
Provides: kernel-modules-extra-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-modules-extra-uname-r = %{KVERREL}%{?1:.%{1}}\
Requires: kernel-libre-uname-r = %{KVERREL}%{?1:.%{1}}\
AutoReqProv: no\
%description -n kernel%{?variant}%{?1:-%{1}}-modules-extra\
This package provides less commonly used kernel modules for the %{?2:%{2} }kernel package.\
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
%{expand:%%kernel_modules_extra_package %1 %{!?-n:%1}%{?-n:%{-n*}}}\
%{expand:%%kernel_debuginfo_package %1}\
%{nil}


# First the auxiliary packages of the main kernel package.
%kernel_devel_package
%kernel_modules_extra_package
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


%ifnarch armv7hl
%define variant_summary The Linux kernel compiled for PAE capable machines
%kernel_variant_package %{pae}
%description %{pae}
This package includes a version of the Linux kernel with support for up to
64GB of high memory. It requires a CPU with Physical Address Extensions (PAE).
The non-PAE kernel can only address up to 4GB of memory.
Install the kernel-PAE package if your machine has more than 4GB of memory.
%else
%define variant_summary The Linux kernel compiled for Cortex-A15
%kernel_variant_package %{pae}
%description %{pae}
This package includes a version of the Linux kernel with support for
Cortex-A15 devices with LPAE and HW virtualisation support
%endif

The kernel-libre-PAE package is the upstream kernel without the
non-Free blobs it includes by default.



%define variant_summary The Linux kernel compiled with extra debugging enabled for PAE capable machines
%kernel_variant_package %{pae}debug
Obsoletes: kernel-PAE-debug
%description %{pae}debug
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

%if "%{baserelease}" == "0"
echo "baserelease must be greater than zero"
exit 1
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
    if [ "${patch:0:8}" != "patch-3." ] &&
       [ "${patch:0:14}" != "patch-libre-3." ] ; then
      echo "ERROR: Patch  $patch  not listed as a source patch in specfile"
      exit 1
    fi
  fi 2>/dev/null
  $RPM_SOURCE_DIR/deblob-check $RPM_SOURCE_DIR/$patch || exit 1
%endif
  case "$patch" in
  *.bz2) bunzip2 < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *.gz)  gunzip  < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *.xz)  unxz    < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
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
   make INSTALL_FW_PATH=$RPM_BUILD_ROOT/lib/firmware firmware_install \

# First we unpack the kernel tarball.
# If this isn't the first make prep, we use links to the existing clean tarball
# which speeds things up quite a bit.

# Update to latest upstream.
%if 0%{?released_kernel}
%define vanillaversion 3.%{base_sublevel}
# non-released_kernel case
%else
%if 0%{?rcrev}
%define vanillaversion 3.%{upstream_sublevel}-rc%{rcrev}
%if 0%{?gitrev}
%define vanillaversion 3.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
%define vanillaversion 3.%{base_sublevel}-git%{gitrev}
%else
%define vanillaversion 3.%{base_sublevel}
%endif
%endif
%endif

# %%{vanillaversion} : the full version name, e.g. 2.6.35-rc6-git3
# %%{kversion}       : the base version, e.g. 2.6.34

# Use kernel-%%{kversion}%%{?dist} as the top-level directory name
# so we can prep different trees within a single git directory.

# Build a list of the other top-level kernel tree directories.
# This will be used to hardlink identical vanilla subdirs.
sharedirs=$(find "$PWD" -maxdepth 1 -type d -name 'kernel-3.*' \
            | grep -x -v "$PWD"/kernel-%{kversion}%{?dist}) ||:

# Delete all old stale trees.
if [ -d kernel-%{kversion}%{?dist} ]; then
  cd kernel-%{kversion}%{?dist}
  for i in linux-*
  do
     if [ -d $i ]; then
       # Just in case we ctrl-c'd a prep already
       rm -rf deleteme.%{_target_cpu}
       # Move away the stale away, and delete in background.
       mv $i deleteme-$i
       rm -rf deleteme* &
     fi
  done
  cd ..
fi

# Generate new tree
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

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?stablegnux}/" vanilla-%{kversion}/Makefile

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
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?rcrevgnux}/" Makefile
%endif
    ApplyPatch patch%{?rcrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}%{?rcrevgnu}.xz
%if 0%{?gitrev}
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = -rc%{rcrev}%{?gitrevgnux}/" Makefile
    ApplyPatch patch%{?gitrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}%{?gitrevgnu}.xz
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if "%{?stablelibre}" != "%{?gitrevlibre}"
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?gitrevgnux}/" Makefile
%endif
%if 0%{?gitrev}
    ApplyPatch patch%{?gitrevlibre}-3.%{base_sublevel}-git%{gitrev}%{?gitrevgnu}.xz
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
cp -rl vanilla-%{vanillaversion} linux-%{KVERREL}

cd linux-%{KVERREL}

# released_kernel with possible stable updates
%if 0%{?stable_base}
ApplyPatch %{stable_patch_00}
%endif
%if 0%{?stable_rc}
perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?rcrevgnux}/" Makefile
ApplyPatch %{stable_patch_01}
%endif

%if %{using_upstream_branch}
### BRANCH APPLY ###
%endif

# Drop some necessary files from the source dir into the buildroot
cp $RPM_SOURCE_DIR/config-* .
cp %{SOURCE15} .

%if !%{debugbuildsenabled}
%if %{with_release}
# The normal build is a really debug build and the user has explicitly requested
# a release kernel. Change the config files into non-debug versions.
make -f %{SOURCE19} config-release
%endif
%endif

# Dynamically generate kernel .config files from config-* files
make -f %{SOURCE20} VERSION=%{version} configs

# Merge in any user-provided local config option changes
for i in kernel-%{version}-*.config
do
  mv $i $i.tmp
  ./merge.pl %{SOURCE1000} $i.tmp > $i
  rm $i.tmp
done

ApplyPatch makefile-after_link.patch

#
# misc small stuff to make things compile
#
ApplyOptionalPatch compile-fixes.patch

# Freedo logo.
ApplyPatch freedo.patch

%if !%{nopatches}

# revert patches from upstream that conflict or that we get via other means
ApplyOptionalPatch upstream-reverts.patch -R

ApplyPatch taint-vbox.patch

ApplyPatch vmbugon-warnon.patch

# Architecture patches
# x86(-64)

#
# ARM
#
ApplyPatch arm-export-read_current_timer.patch
ApplyPatch arm-lpae-ax88796.patch
ApplyPatch arm-omap-load-tfp410.patch
ApplyPatch arm-tegra-usb-no-reset-linux33.patch

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
ApplyPatch defaults-acpi-video.patch
ApplyPatch acpi-sony-nonvs-blacklist.patch

#
# PCI
#

#
# SCSI Bits.
#

# ACPI

# ALSA

# Networking

# Misc fixes
# The input layer spews crap no-one cares about.
ApplyPatch input-kill-stupid-messages.patch

# stop floppy.ko from autoloading during udev...
ApplyPatch die-floppy-die.patch

ApplyPatch no-pcspkr-modalias.patch

# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch serial-460800.patch

# Silence some useless messages that still get printed with 'quiet'
ApplyPatch silence-noise.patch

# Make fbcon not show the penguins with 'quiet'
ApplyPatch silence-fbcon-logo.patch

# Changes to upstream defaults.


# /dev/crash driver.
ApplyPatch crash-driver.patch

# crypto/

# secure boot
ApplyPatch devel-pekey-secure-boot-20130502.patch

# Assorted Virt Fixes

# DRM core
#ApplyPatch drm-edid-try-harder-to-fix-up-broken-headers.patch
#ApplyPatch drm-vgem.patch

# Nouveau DRM

# Intel DRM
ApplyOptionalPatch drm-intel-next.patch
ApplyPatch drm-i915-dp-stfu.patch

# silence the ACPI blacklist code
ApplyPatch silence-acpi-blacklist.patch

# V4L/DVB updates/fixes/experimental drivers
#  apply if non-empty
ApplyOptionalPatch v4l-dvb-fixes.patch
ApplyOptionalPatch v4l-dvb-update.patch
ApplyOptionalPatch v4l-dvb-experimental.patch

# Patches headed upstream
ApplyPatch fs-proc-devtree-remove_proc_entry.patch

ApplyPatch disable-i8042-check-on-apple-mac.patch

# FIXME: REBASE
#ApplyPatch hibernate-freeze-filesystems.patch

ApplyPatch lis3-improve-handling-of-null-rate.patch

# Disable watchdog on virtual machines.
ApplyPatch nowatchdog-on-virt.patch

#rhbz 754518
ApplyPatch scsi-sd_revalidate_disk-prevent-NULL-ptr-deref.patch

ApplyPatch weird-root-dentry-name-debug.patch
ApplyPatch debug-idle-sched-warn-once.patch

# https://fedoraproject.org/wiki/Features/Checkpoint_Restore
ApplyPatch criu-no-expert.patch

#rhbz 892811
ApplyPatch ath9k_rx_dma_stop_check.patch

#rhbz 927469
ApplyPatch fix-child-thread-introspection.patch

ApplyPatch drm-radeon-Disable-writeback-by-default-on-ppc.patch

#rhbz 977040
ApplyPatch iwl3945-better-skb-management-in-rx-path.patch
ApplyPatch iwl4965-better-skb-management-in-rx-path.patch

#rhbz 963715
ApplyPatch media-cx23885-Fix-TeVii-S471-regression-since-introduction-of-ts2020.patch

#CVE-2013-4345 rhbz 1007690 1009136
ApplyPatch ansi_cprng-Fix-off-by-one-error-in-non-block-size-request.patch

#rhbz 985522
ApplyPatch ntp-Make-periodic-RTC-update-more-reliable.patch

#rhbz 1010431
ApplyPatch Revert-rt2x00pci-Use-PCI-MSIs-whenever-possible.patch

#rhbz 971893
ApplyPatch bonding-driver-alb-learning.patch

#rhbz 902012
ApplyPatch elevator-Fix-a-race-in-elevator-switching-and-md.patch
ApplyPatch elevator-acquire-q-sysfs_lock-in-elevator_change.patch

#rhbz 974072
ApplyPatch rt2800-add-support-for-rf3070.patch

#rhbz 1015989
ApplyPatch netfilter-nf_conntrack-use-RCU-safe-kfree-for-conntr.patch

#rhbz 982153
ApplyPatch iommu-Remove-stack-trace-from-broken-irq-remapping-warning.patch

#rhbz 998732
ApplyPatch vfio-iommu-Fixed-interaction-of-VFIO_IOMMU_MAP_DMA.patch

#rhbz 896695
ApplyPatch 0001-iwlwifi-don-t-WARN-on-host-commands-sent-when-firmwa.patch
ApplyPatch 0002-iwlwifi-don-t-WARN-on-bad-firmware-state.patch

#rhbz 993744
ApplyPatch dm-cache-policy-mq_fix-large-scale-table-allocation-bug.patch

#rhbz 1000439
ApplyPatch cpupower-Fix-segfault-due-to-incorrect-getopt_long-a.patch

#rhbz 1010679
ApplyPatch fix-radeon-sound.patch

#rhbz 1011714
ApplyPatch btrfs-relocate-csums-properly-with-prealloc-ext.patch

#rhbz 984696
ApplyPatch rt2800usb-slow-down-TX-status-polling.patch

#rhbz 1015558
ApplyPatch fix-buslogic.patch

#rhbz 1023413
ApplyPatch alps-Support-for-Dell-XT2-model.patch

#rhbz 1011621
ApplyPatch cifs-Allow-LANMAN-auth-for-unencapsulated-auth-methods.patch

#rhbz 995782
ApplyPatch intel-3.12-stable-fixes.patch

#CVE-2013-4348 rhbz 1007939 1025647
ApplyPatch net-flow_dissector-fail-on-evil-iph-ihl.patch

#rhbz 1010603
ApplyPatch 0001-Revert-epoll-use-freezable-blocking-call.patch
ApplyPatch 0001-Revert-select-use-freezable-blocking-call.patch

#rhbz 1025769
ApplyPatch iwlwifi-dvm-dont-override-mac80211-queue-setting.patch

# END OF PATCH APPLICATIONS

%endif

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl

# This Prevents scripts/setlocalversion from mucking with our version numbers.
touch .scmversion

# only deal with configs if we are going to build for the arch
%ifnarch %nobuildarches

mkdir configs

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

%if %{with_debuginfo}
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
    Flav=${Flavour:+.${Flavour}}
    InstallName=${4:-vmlinuz}

    # Pick the right config file for the kernel we're building
    Config=kernel-%{version}-%{_target_cpu}${Flavour:+-${Flavour}}.config
    DevelDir=/usr/src/kernels/%{KVERREL}${Flav}

    # When the bootable image is just the ELF kernel, strip it.
    # We already copy the unstripped file into the debuginfo package.
    if [ "$KernelImage" = vmlinux ]; then
      CopyKernel=cp_vmlinux
    else
      CopyKernel=cp
    fi

    KernelVer=%{version}-libre.%{release}.%{_target_cpu}${Flav}
    echo BUILDING A KERNEL FOR ${Flavour} %{_target_cpu}...

    %if 0%{?stable_update}
    # make sure SUBLEVEL is incremented on a stable release.  Sigh 3.x.
    perl -p -i -e "s/^SUBLEVEL.*/SUBLEVEL = %{?stablerev}/" Makefile
    %endif

    # make sure EXTRAVERSION says what we want it to say
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = -libre.%{release}.%{_target_cpu}${Flav}/" Makefile

    # if pre-rc1 devel kernel, must fix up PATCHLEVEL for our versioning scheme
    %if !0%{?rcrev}
    %if 0%{?gitrev}
    perl -p -i -e 's/^PATCHLEVEL.*/PATCHLEVEL = %{upstream_sublevel}/' Makefile
    %endif
    %endif

    # and now to start the build process

    make -s mrproper
    cp configs/$Config .config

    %if %{signmodules}
    cp %{SOURCE11} .
    %endif

    chmod +x scripts/sign-file

    Arch=`head -1 .config | cut -b 3-`
    echo USING ARCH=$Arch

    make -s ARCH=$Arch oldnoconfig >/dev/null
    make -s ARCH=$Arch V=1 %{?_smp_mflags} $MakeTarget %{?sparse_mflags} %{?kernel_mflags}
    make -s ARCH=$Arch V=1 %{?_smp_mflags} modules %{?sparse_mflags} || exit 1

%ifarch %{arm}
    make -s ARCH=$Arch V=1 dtbs
    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}/dtb-$KernelVer
    install -m 644 arch/arm/boot/dts/*.dtb $RPM_BUILD_ROOT/boot/dtb-$KernelVer/
    rm -f arch/arm/boot/dts/*.dtb
%endif

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
    %if %{signmodules}
    # Sign the image if we're using EFI
    %pesign -s -i $KernelImage -o vmlinuz.signed
    if [ ! -s vmlinuz.signed ]; then
        echo "pesigning failed"
        exit 1
    fi
    mv vmlinuz.signed $KernelImage
    %endif
    $CopyKernel $KernelImage \
    		$RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer
    chmod 755 $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer

    # hmac sign the kernel for FIPS
    echo "Creating hmac file: $RPM_BUILD_ROOT/%{image_install_path}/.vmlinuz-$KernelVer.hmac"
    ls -l $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer
    sha512hmac $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer | sed -e "s,$RPM_BUILD_ROOT,," > $RPM_BUILD_ROOT/%{image_install_path}/.vmlinuz-$KernelVer.hmac;

    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer
    # Override $(mod-fw) because we don't want it to install any firmware
    # we'll get it from the linux-firmware package and we don't want conflicts
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
%ifarch ppc ppc64 ppc64p7
    cp -a --parents arch/powerpc/lib/crtsavres.[So] $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
%endif
    if [ -d arch/%{asmarch}/include ]; then
      cp -a --parents arch/%{asmarch}/include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
    # include the machine specific headers for ARM variants, if available.
%ifarch %{arm}
    if [ -d arch/%{asmarch}/mach-${Flavour}/include ]; then
      cp -a --parents arch/%{asmarch}/mach-${Flavour}/include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
%endif
    cp -a include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include

    # Make sure the Makefile and version.h have a matching timestamp so that
    # external modules can be built
    touch -r $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/Makefile $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/generated/uapi/linux/version.h

    # Copy .config to include/config/auto.conf so "make prepare" is unnecessary.
    cp $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/.config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/config/auto.conf

%if %{with_debuginfo}
    if test -s vmlinux.id; then
      cp vmlinux.id $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/vmlinux.id
    else
      echo >&2 "*** ERROR *** no vmlinux build ID! ***"
      exit 1
    fi

    #
    # save the vmlinux file for kernel debugging into the kernel-debuginfo rpm
    #
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
    			 'register_netdev|ieee80211_register_hw|usbnet_probe|phy_driver_register|rt(l_|2x00)(pci|usb)_probe|register_netdevice'
    collect_modules_list block \
    			 'ata_scsi_ioctl|scsi_add_host|scsi_add_host_with_dma|blk_init_queue|register_mtd_blktrans|scsi_esp_register|scsi_register_device_handler|blk_queue_physical_block_size'
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

    # Call the modules-extra script to move things around
    %{SOURCE17} $RPM_BUILD_ROOT/lib/modules/$KernelVer %{SOURCE16}

%if %{signmodules}
    # Save the signing keys so we can sign the modules in __modsign_install_post
    cp signing_key.priv signing_key.priv.sign${Flav}
    cp signing_key.x509 signing_key.x509.sign${Flav}
%endif

    # remove files that will be auto generated by depmod at rpm -i time
    for i in alias alias.bin builtin.bin ccwmap dep dep.bin ieee1394map inputmap isapnpmap ofmap pcimap seriomap symbols symbols.bin usbmap devname softdep
    do
      rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.$i
    done

    # Move the devel headers out of the root file system
    mkdir -p $RPM_BUILD_ROOT/usr/src/kernels
    mv $RPM_BUILD_ROOT/lib/modules/$KernelVer/build $RPM_BUILD_ROOT/$DevelDir

    # This is going to create a broken link during the build, but we don't use
    # it after this point.  We need the link to actually point to something
    # when kernel-devel is installed, and a relative link doesn't work across
    # the F17 UsrMove feature.
    ln -sf $DevelDir $RPM_BUILD_ROOT/lib/modules/$KernelVer/build

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

cd linux-%{KVERREL}

%if %{with_debug}
BuildKernel %make_target %kernel_image debug
%endif

%if %{with_pae_debug}
BuildKernel %make_target %kernel_image %{pae}debug
%endif

%if %{with_pae}
BuildKernel %make_target %kernel_image %{pae}
%endif

%if %{with_up}
BuildKernel %make_target %kernel_image
%endif

%if %{with_smp}
BuildKernel %make_target %kernel_image smp
%endif

%global perf_make \
  make %{?_smp_mflags} -C tools/perf -s V=1 WERROR=0 NO_LIBUNWIND=1 HAVE_CPLUS_DEMANGLE=1 NO_GTK2=1 NO_LIBNUMA=1 NO_STRLCPY=1 prefix=%{_prefix}
%if %{with_perf}
# perf
%{perf_make} all
%{perf_make} man || %{doc_build_fail}
%endif

%if %{with_tools}
%ifarch %{cpupowerarchs}
# cpupower
# make sure version-gen.sh is executable.
chmod +x tools/power/cpupower/utils/version-gen.sh
make %{?_smp_mflags} -C tools/power/cpupower CPUFREQ_BENCH=false
%ifarch %{ix86}
    pushd tools/power/cpupower/debug/i386
    make %{?_smp_mflags} centrino-decode powernow-k8-decode
    popd
%endif
%ifarch x86_64
    pushd tools/power/cpupower/debug/x86_64
    make %{?_smp_mflags} centrino-decode powernow-k8-decode
    popd
%endif
%ifarch %{ix86} x86_64
   pushd tools/power/x86/x86_energy_perf_policy/
   make
   popd
   pushd tools/power/x86/turbostat
   make
   popd
%endif #turbostat/x86_energy_perf_policy
%endif
%endif

%if %{with_doc}
# Make the HTML and man pages.
make htmldocs mandocs || %{doc_build_fail}

# sometimes non-world-readable files sneak into the kernel source tree
chmod -R a=rX Documentation
find Documentation -type d | xargs chmod u+w
%endif

# In the modsign case, we do 3 things.  1) We check the "flavour" and hard
# code the value in the following invocations.  This is somewhat sub-optimal
# but we're doing this inside of an RPM macro and it isn't as easy as it
# could be because of that.  2) We restore the .tmp_versions/ directory from
# the one we saved off in BuildKernel above.  This is to make sure we're
# signing the modules we actually built/installed in that flavour.  3) We
# grab the arch and invoke mod-sign.sh command to actually sign the modules.
#
# We have to do all of those things _after_ find-debuginfo runs, otherwise
# that will strip the signature off of the modules.

%define __modsign_install_post \
  if [ "%{signmodules}" == "1" ]; then \
    if [ "%{with_pae}" -ne "0" ]; then \
      mv signing_key.priv.sign.%{pae} signing_key.priv \
      mv signing_key.x509.sign.%{pae} signing_key.x509 \
      %{modsign_cmd} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}.%{pae}/ \
    fi \
    if [ "%{with_debug}" -ne "0" ]; then \
      mv signing_key.priv.sign.debug signing_key.priv \
      mv signing_key.x509.sign.debug signing_key.x509 \
      %{modsign_cmd} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}.debug/ \
    fi \
    if [ "%{with_pae_debug}" -ne "0" ]; then \
      mv signing_key.priv.sign.%{pae}debug signing_key.priv \
      mv signing_key.x509.sign.%{pae}debug signing_key.x509 \
      %{modsign_cmd} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}.%{pae}debug/ \
    fi \
    if [ "%{with_up}" -ne "0" ]; then \
      mv signing_key.priv.sign signing_key.priv \
      mv signing_key.x509.sign signing_key.x509 \
      %{modsign_cmd} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}/ \
    fi \
  fi \
%{nil}

###
### Special hacks for debuginfo subpackages.
###

# This macro is used by %%install, so we must redefine it before that.
%define debug_package %{nil}

%if %{with_debuginfo}

%define __debug_install_post \
  /usr/lib/rpm/find-debuginfo.sh %{debuginfo_args} %{_builddir}/%{?buildsubdir}\
%{nil}

%ifnarch noarch
%global __debug_package 1
%files -f debugfiles.list debuginfo-common-%{_target_cpu}
%defattr(-,root,root)
%endif

%endif

#
# Disgusting hack alert! We need to ensure we sign modules *after* all
# invocations of strip occur, which is in __debug_install_post if
# find-debuginfo.sh runs, and __os_install_post if not.
#
%define __spec_install_post \
  %{?__debug_package:%{__debug_install_post}}\
  %{__arch_install_post}\
  %{__os_install_post}\
  %{__modsign_install_post}

###
### install
###

%install

cd linux-%{KVERREL}

%if %{with_doc}
docdir=$RPM_BUILD_ROOT%{_datadir}/doc/kernel-doc-%{rpmversion}
man9dir=$RPM_BUILD_ROOT%{_datadir}/man/man9

# copy the source over
mkdir -p $docdir
tar -h -f - --exclude=man --exclude='.*' -c Documentation | tar xf - -C $docdir

# Install man pages for the kernel API.
mkdir -p $man9dir
find Documentation/DocBook/man -name '*.9.gz' -print0 |
xargs -0 --no-run-if-empty %{__install} -m 444 -t $man9dir $m
ls $man9dir | grep -q '' || > $man9dir/BROKEN
%endif # with_doc

# We have to do the headers install before the tools install because the
# kernel headers_install will remove any header files in /usr/include that
# it doesn't install itself.

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

%endif

%if %{with_perf}
# perf tool binary and supporting scripts/binaries
%{perf_make} DESTDIR=$RPM_BUILD_ROOT install

# python-perf extension
%{perf_make} DESTDIR=$RPM_BUILD_ROOT install-python_ext

# perf man pages (note: implicit rpm magic compresses them later)
%{perf_make} DESTDIR=$RPM_BUILD_ROOT install-man || %{doc_build_fail}
%endif

%if %{with_tools}
%ifarch %{cpupowerarchs}
make -C tools/power/cpupower DESTDIR=$RPM_BUILD_ROOT libdir=%{_libdir} mandir=%{_mandir} CPUFREQ_BENCH=false install
rm -f %{buildroot}%{_libdir}/*.{a,la}
%find_lang cpupower
mv cpupower.lang ../
%ifarch %{ix86}
    pushd tools/power/cpupower/debug/i386
    install -m755 centrino-decode %{buildroot}%{_bindir}/centrino-decode
    install -m755 powernow-k8-decode %{buildroot}%{_bindir}/powernow-k8-decode
    popd
%endif
%ifarch x86_64
    pushd tools/power/cpupower/debug/x86_64
    install -m755 centrino-decode %{buildroot}%{_bindir}/centrino-decode
    install -m755 powernow-k8-decode %{buildroot}%{_bindir}/powernow-k8-decode
    popd
%endif
chmod 0755 %{buildroot}%{_libdir}/libcpupower.so*
mkdir -p %{buildroot}%{_unitdir} %{buildroot}%{_sysconfdir}/sysconfig
install -m644 %{SOURCE2000} %{buildroot}%{_unitdir}/cpupower.service
install -m644 %{SOURCE2001} %{buildroot}%{_sysconfdir}/sysconfig/cpupower
%endif
%ifarch %{ix86} x86_64
   mkdir -p %{buildroot}%{_mandir}/man8
   pushd tools/power/x86/x86_energy_perf_policy
   make DESTDIR=%{buildroot} install
   popd
   pushd tools/power/x86/turbostat
   make DESTDIR=%{buildroot} install
   popd
%endif #turbostat/x86_energy_perf_policy
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

%if %{with_tools}
%post -n kernel-libre-tools
/sbin/ldconfig

%postun -n kernel-libre-tools
/sbin/ldconfig
%endif

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

#
# This macro defines a %%post script for a kernel*-modules-extra package.
#	%%kernel_modules_extra_post [<subpackage>]
#
%define kernel_modules_extra_post() \
%{expand:%%post %{?1:%{1}-}modules-extra}\
/sbin/depmod -a %{KVERREL}%{?1:.%{1}}\
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
%{expand:%%kernel_modules_extra_post %{?-v*}}\
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

%kernel_variant_preun %{pae}
%kernel_variant_post -v %{pae} -r (kernel|kernel-smp)

%kernel_variant_post -v %{pae}debug -r (kernel|kernel-smp)
%kernel_variant_preun %{pae}debug

%kernel_variant_preun debug
%kernel_variant_post -v debug

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
%doc linux-%{KVERREL}/firmware/WHENCE
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
%{_mandir}/man[1-8]/perf*
%{_sysconfdir}/bash_completion.d/perf
%doc linux-%{KVERREL}/tools/perf/Documentation/examples.txt

%files -n python-perf-libre
%defattr(-,root,root)
%{python_sitearch}

%if %{with_debuginfo}
%files -f perf-debuginfo.list -n perf-libre-debuginfo
%defattr(-,root,root)

%files -f python-perf-debuginfo.list -n python-perf-libre-debuginfo
%defattr(-,root,root)
%endif
%endif # with_perf

%if %{with_tools}
%files -n kernel-libre-tools -f cpupower.lang
%defattr(-,root,root)
%ifarch %{cpupowerarchs}
%{_bindir}/cpupower
%ifarch %{ix86} x86_64
%{_bindir}/centrino-decode
%{_bindir}/powernow-k8-decode
%endif
%{_unitdir}/cpupower.service
%{_mandir}/man[1-8]/cpupower*
%config(noreplace) %{_sysconfdir}/sysconfig/cpupower
%ifarch %{ix86} x86_64
%{_bindir}/x86_energy_perf_policy
%{_mandir}/man8/x86_energy_perf_policy*
%{_bindir}/turbostat
%{_mandir}/man8/turbostat*
%endif
%endif

%if %{with_debuginfo}
%files -f kernel-tools-debuginfo.list -n kernel-libre-tools-debuginfo
%defattr(-,root,root)
%endif

%ifarch %{cpupowerarchs}
%files -n kernel-libre-tools-libs
%{_libdir}/libcpupower.so.0
%{_libdir}/libcpupower.so.0.0.0

%files -n kernel-libre-tools-libs-devel
%{_libdir}/libcpupower.so
%{_includedir}/cpufreq.h
%endif
%endif # with_perf

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
/%{image_install_path}/.vmlinuz-%{KVERREL}%{?2:.%{2}}.hmac \
%ifarch %{arm}\
/%{image_install_path}/dtb-%{KVERREL}%{?2:.%{2}} \
%endif\
%attr(600,root,root) /boot/System.map-%{KVERREL}%{?2:.%{2}}\
/boot/config-%{KVERREL}%{?2:.%{2}}\
%dir /lib/modules/%{KVERREL}%{?2:.%{2}}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/kernel\
/lib/modules/%{KVERREL}%{?2:.%{2}}/build\
/lib/modules/%{KVERREL}%{?2:.%{2}}/source\
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
%{expand:%%files %{?2:%{2}-}modules-extra}\
%defattr(-,root,root)\
/lib/modules/%{KVERREL}%{?2:.%{2}}/extra\
%if %{with_debuginfo}\
%ifnarch noarch\
%{expand:%%files -f debuginfo%{?2}.list %{?2:%{2}-}debuginfo}\
%defattr(-,root,root)\
%endif\
%endif\
%endif\
%{nil}


%kernel_variant_files %{with_up}
%kernel_variant_files %{with_smp} smp
%kernel_variant_files %{with_debug} debug
%kernel_variant_files %{with_pae} %{pae}
%kernel_variant_files %{with_pae_debug} %{pae}debug

# plz don't put in a version string unless you're going to tag
# and build.

%changelog
* Mon Nov 04 2013 Josh Boyer <jwboyer@fedoraproject.org> - 3.11.7-200
- Add patch to fix iwlwifi queue settings backtrace (rhbz 1025769)

* Mon Nov 04 2013 Justin M. Forbes <jforbes@fedoraproject.org>
- Linux v3.11.7

* Fri Nov 01 2013 Josh Boyer <jwboyer@fedoraproject.org> - 3.11.6-201
- Revert blocking patches causing systemd to crash on resume (rhbz 1010603)
- CVE-2013-4348 net: deadloop path in skb_flow_dissect (rhbz 1007939 1025647)

* Thu Oct 31 2013 Josh Boyer <jwboyer@fedoraprorject.org>
- Fix display regression on Dell XPS 13 machines (rhbz 995782)

* Tue Oct 29 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Fix plaintext auth regression in cifs (rhbz 1011621)

* Fri Oct 25 2013 Josh Boyer <jwboyer@fedoraproject.org>
- CVE-2013-4470 net: memory corruption with UDP_CORK and UFO (rhbz 1023477 1023495)
- Add touchpad support for Dell XT2 (rhbz 1023413)

* Tue Oct 22 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add patch to fix warning in tcp_fastretrans_alert (rhbz 989251)

* Fri Oct 18 2013 Justin M. Forbes <jforbes@fedoraproject.org> - 3.11.6-200
- Linux v3.11.6

* Thu Oct 17 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add patch to fix BusLogic error (rhbz 1015558)
- Fix rt2800usb polling timeouts and throughput issues (rhbz 984696)

* Wed Oct 16 2013 Josh Boyer <jwboyer@fedoraproject.org> 
- Fix btrfs balance/scrub issue (rhbz 1011714)

* Tue Oct 15 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Fix regression in radeon sound (rhbz 1010679)

* Mon Oct 14 2013 Justin M. Forbes <jforbes@fedoraproject.org> - 3.11.5-200
- Linux v3.11.5

* Fri Oct 11 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Fix segfault in cpupower set (rhbz 1000439)

* Thu Oct 10 2013 Justin M. Forbes <jforbes@fedoraproject.org> - 3.11.4-201
- Tag for build

* Thu Oct 10 2013 Josh Boyer <jwboyer@fedoraproject.org>
- USB OHCI accept very late isochronous URBs (in 3.11.4) (rhbz 975158)
- Fix large order allocation in dm mq policy (rhbz 993744)

* Wed Oct 09 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Don't trigger a stack trace on crashing iwlwifi firmware (rhbz 896695)
- Add patch to fix VFIO IOMMU crash (rhbz 998732)

* Tue Oct 08 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add patch to fix nouveau crash (rhbz 1015920)
- Quiet irq remapping stack trace (rhbz 982153)
- Use RCU safe kfree for conntrack (rhbz 1015989)

* Mon Oct 7 2013 Justin M. Forbes <jforbes@fedoraproject.org>
- Linux v3.11.4

* Thu Oct 3 2013 Josh Boyer <jwboyer@fedoraproject.org>
- CVE-2013-4387 ipv6: panic when UFO=On for an interface (rhbz 1011927 1015166)

* Wed Oct 2 2013 Justin M. Forbes <jforbes@fedoraproject.org> 
- drm/radeon: don't set default clocks for SI when DPM is disabled (rhbz 1013814)

* Wed Oct 2 2013 Justin M. Forbes <jforbes@fedoraproject.org> - 3.11.3-200
- Linux v3.11.3

* Wed Oct 02 2013 Neil Horman <nhorman@redhat.com>
- Add promiscuity fix for vlans plus bonding (rhbz 1005567)

* Mon Sep 30 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add support for rf3070 devices from Stanislaw Gruszka (rhbz 974072)
- Drop VC_MUTE patch (rhbz 859485)

* Fri Sep 27 2013 Justin M. Forbes <jforbes@fedoraproject.org> - 3.11.2-201
- Bump and tag for build

* Fri Sep 27 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add patch to fix oops from applesmc (rhbz 1011719)
- Add patches to fix soft lockup from elevator changes (rhbz 902012)

* Fri Sep 27 2013 Justin M. Forbes <jforbes@fedoraproject.org> - 3.11.2-200
- Linux v3.11.2

* Wed Sep 25 2013 Justin M. Forbes <jforbes@fedoraproject.org>
- Bump baserelease for test build

* Wed Sep 25 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add another fix for skge (rhbz 1008323)

* Mon Sep 23 2013 Neil Horman <nhorman@redhat.com>
- Add alb learning packet config knob (rhbz 971893)

* Mon Sep 23 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Revert rt2x00 commit that breaks connectivity (rhbz 1010431)

* Fri Sep 20 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Fix RTC updates from ntp (rhbz 985522)
- Fix broken skge driver (rhbz 1008328)
- Fix large order rpc allocations (rhbz 997705)
- Fix multimedia keys on Genius GX keyboard (rhbz 928561)

* Tue Sep 17 2013 Josh Boyer <jwboyer@fedoraproject.org>
- CVE-2013-4345 ansi_cprng: off by one error in non-block size request (rhbz 1007690 1009136)

* Sat Sep 14 2013 Josh Boyer <jwboyer@fedoraproject.org> - 3.11.1-200
- Linux v3.11.1

* Fri Sep 13 2013 Kyle McMartin <kyle@redhat.com>
- Fix crash-driver.patch to properly use page_is_ram. 

* Fri Sep 13 2013 Josh Boyer <jwboyer@fedoraproject.org>
- CVE-2013-4350 net: sctp: ipv6 ipsec encryption bug in sctp_v6_xmit (rhbz 1007872 1007903)
- CVE-2013-4343 net: use-after-free TUNSETIFF (rhbz 1007733 1007741)

* Thu Sep 12 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Update HID CVE fixes to fix crash from lenovo-tpkbd driver (rhbz 1003998)

* Wed Sep 11 2013 Neil Horman <nhorman@redhat.com>
- Fix pcie/acpi hotplug conflict (rhbz 963991)
- Fix race in crypto larval lookup

* Wed Sep 11 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre Thu Sep 12
- GNU Linux-libre 3.11-gnu.

* Wed Sep 11 2013 Justin M. Forbes <jforbes@fedoraproject.org>
- Linux v3.11 rebase

* Tue Sep 10 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.11-gnu.

* Mon Sep 09 2013 Josh Boyer <jwboyer@fedoraproject.org> 3.10.11-200
- Fix system freeze due to incorrect rt2800 initialization (rhbz 1000679)

* Mon Sep 09 2013 Justin M. Forbes <jforbes@fedoraproject.org>
- Linux v3.10.11

* Fri Aug 30 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Fix HID CVEs.  Absurd.
- CVE-2013-2888 rhbz 1000451 1002543 CVE-2013-2889 rhbz 999890 1002548
- CVE-2013-2891 rhbz 999960 1002555  CVE-2013-2892 rhbz 1000429 1002570
- CVE-2013-2893 rhbz 1000414 1002575 CVE-2013-2894 rhbz 1000137 1002579
- CVE-2013-2895 rhbz 1000360 1002581 CVE-2013-2896 rhbz 1000494 1002594
- CVE-2013-2897 rhbz 1000536 1002600 CVE-2013-2899 rhbz 1000373 1002604

* Thu Aug 29 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.10-gnu.

* Thu Aug 29 2013 Justin M. Forbes <jforbes@fedoraproject.org> 3.10.10-200
- Linux v3.10.10

* Wed Aug 28 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add mei patches that fix various s/r issues (rhbz 994824 989373)

* Wed Aug 21 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.9-gnu.

* Wed Aug 21 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add patch to fix brcmsmac oops (rhbz 989269)
- CVE-2013-0343 handling of IPv6 temporary addresses (rhbz 914664 999380)

* Tue Aug 20 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Linux v3.10.9

* Tue Aug 20 2013 Josh Boyer <jwboyer@fedoraproject.org> - 3.10.8-200
- Linux v3.10.8
- CVE-2013-4254 ARM: perf: NULL pointer dereference in validate_event (rhbz 998878 998881)

* Fri Aug 16 2013 Josh Boyer <jwboyer@fedoraproject.org>
- Add patch from Nathanael Noblet to fix mic on Gateway LT27 (rhbz 845699)

* Thu Aug 15 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.7-gnu.

* Thu Aug 15 2013 Josh Boyer <jwboyer@redhat.com> - 3.6.10.7-200
- Enable memory cgroup swap accounting (rhbz 982808)
- Add patch to fix regression on TeVII S471 devices (rhbz 963715)
- Linux v3.10.7

* Mon Aug 12 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.6-gnu.

* Mon Aug 12 2013 Justin M. Forbes <jforbes@redhat.com> 3.10.6-200
- Linux v3.10.6

* Wed Aug 07 2013 Justin M. Forbes <jforbes@redhat.com> 3.10.5-201
- Bump for rebuild after koji hiccup

* Wed Aug 07 2013 Josh Boyer <jwboyer@redhat.com>
- Add zero file length check to make sure pesign didn't fail (rhbz 991808)

* Tue Aug  6 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.5-gnu.

* Tue Aug 06 2013 Justin M. Forbes <jforbes@redhat.com> 3.10.5-200
- update s390x config [Dan Hor??k]

* Mon Aug 05 2013 Justin M. Forbes <jforbes@redhat.com>
- Linux v3.10.5

* Thu Aug 01 2013 Josh Boyer <jwboyer@redhat.com>
- Fix mac80211 connection issues (rhbz 981445)
- Fix firmware issues with iwl4965 and rfkill (rhbz 977053)
- Drop hid-logitech-dj patch that was breaking enumeration (rhbz 989138)

* Wed Jul 31 2013 Josh Boyer <jwboyer@redhat.com>
- update s390x config [Dan Hor??k]

* Wed Jul 31 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.4-gnu.

* Tue Jul 30 2013 Josh Boyer <jwboyer@redhat.com> - 3.10.4-300
- Revert some changes to make Logitech devices function properly (rhbz 989138)

* Mon Jul 29 2013 Josh Boyer <jwboyer@redhat.com>
- Fix i915 suspend/resume regression in 3.10 (rhbz 989093)
- Linux v3.10.4
- Add support for elantech v7 devices (rhbz 969473)

* Fri Jul 26 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix NULL deref in iwlwifi (rhbz 979581)

* Thu Jul 25 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.3-gnu.

* Thu Jul 25 2013 Justin M. Forbes <jforbes@redhat.com> 3.10.3-300
- Linux v3.10.3

* Wed Jul 24 2013 Justin M. Forbes <jforbes@redhat.com>
- Net stable queue from davem (rhbz 987639 987656)

* Mon Jul 22 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.10.2-gnu.

* Mon Jul 22 2013 Justin M. Forbes <jforbes@redhat.com> 3.10.2-301
- Update secureboot patch for 3.10

* Mon Jul 22 2013 Josh Boyer <jwboyer@redhat.com>
- Fix timer issue in bridge code (rhbz 980254)

* Mon Jul 22 2013 Justin M. Forbes <jforbes@redhat.com> 3.10.2-300
- Linux v3.10.2

* Fri Jul 19 2013 Dave Jones <davej@redhat.com>
- CVE-2013-4125  ipv6: BUG_ON in fib6_add_rt2node() (rhbz 984664)

* Wed Jul 17 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Re-enable ARM
- Drop tegra subkernel as it's now multi-platform
- Enable i.MX SoC support
- Drop old ARM patches

* Wed Jul 17 2013 Dave Jones <davej@redhat.com>
- Rebase to 3.10.1
  dropped:
   debug-bad-pte-dmi.patch
   debug-bad-pte-modules.patch
   arm-omap-ehci-fix.patch
   arm-omap-fixdrm.patch
   drm-ttm-exports-for-qxl.patch
   drm-qxl-driver.patch
   drm-qxl-3.10-rc7-diff.patch
   drm-qxl-access-fix.patch
   VMX-x86-handle-host-TSC-calibration-failure.patch
   forcedeth-dma-error-check.patch
   block-do-not-pass-disk-names-as-format-strings.patch
   cdrom-use-kzalloc-for-failing-hardware.patch
   vfio-Set-container-device-mode.patch
   vfio-fix-crash-on-rmmod.patch
   tulip-dma-debug-error.patch
   af_key-fix-info-leaks-in-notify-messages.patch
   ipv6-ip6_sk_dst_check-must-not-assume-ipv6-dst.patch
   arm-tegra-fixclk.patch
   cfg80211-mac80211-disconnect-on-suspend.patch
   mac80211_fixes_for_ieee80211_do_stop_while_suspend_v3.9.patch
   gssproxy-backport.patch
   ceph-fix.patch

* Fri Jul 12 2013 Dave Jones <davej@redhat.com> - 3.9.9-304
- Disable LATENCYTOP/SCHEDSTATS in non-debug builds.

* Fri Jul 12 2013 Josh Boyer <jwboyer@redhat.com>
- Fix various overflow issues in ext4 (rhbz 976837)
- Add iwlwifi fix for connection issue (rhbz 885407)

* Thu Jul 11 2013 Kyle McMartin <kyle@redhat.com>
- Enable USB on i.MX based boards, patch from Niels de Vos.

* Fri Jul 05 2013 Josh Boyer <jwboyer@redhat.com>
- Add report fixup for Genius Gila mouse from Benjamin Tissoires (rhbz 959721)
- Add vhost-net use-after-free fix (rhbz 976789 980643)
- Add fix for timer issue in bridge code (rhbz 980254)
- CVE-2013-2232 ipv6: using ipv4 vs ipv6 structure during routing lookup in sendmsg (rhbz 981552 981564)

* Thu Jul 04 2013 Dave Airlie <airlied@redhat.com>
- qxl: add suspend/resume and hibernate support

* Wed Jul 03 2013 Josh Boyer <jwboyer@redhat.com> 3.9.9-301
- CVE-2013-1059 libceph: Fix NULL pointer dereference in auth client code (rhbz 977356 980341)
- CVE-2013-2234 net: information leak in AF_KEY notify (rhbz 980995 981007)

* Wed Jul  3 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre Sun Jul  7
- GNU Linux-libre 3.9.9-gnu.

* Wed Jul 03 2013 Justin M. Forbes <jforbes@redhat.com> 3.9.9-300
- Linux v3.9.9

* Wed Jul 03 2013 Josh Boyer <jwboyer@redhat.com>
- Add patches to fix iwl skb managment (rhbz 977040)

* Wed Jul 03 2013 Dave Airlie <airlied@redhat.com>
- fixup QXL driver patches to make it easier to rebase
- add qxl driver dynamic resize + multiple heads support

* Mon Jul 01 2013 Dave Airlie <airlied@redhat.com>
- kernel portion of qxl cursor and dynamic resize fixes.

* Fri Jun 28 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Only enable ARM A15 errata on the LPAE kernel as it breaks A8

* Fri Jun 28 2013 Dave Airlie <airlied@redhat.com> 
- add qxl fix for missing access ok macro.

* Fri Jun 28 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9.8-gnu.

* Thu Jun 27 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.8-300
- Linux v3.9.8

* Thu Jun 27 2013 Josh Boyer <jwboyer@redhat.com>
- Fix stack memory usage for DMA in ath3k (rhbz 977558)

* Wed Jun 26 2013 Josh Boyer <jwboyer@redhat.com>
- Add two patches to fix bridge networking issues (rhbz 880035)

* Tue Jun 25 2013 Kyle McMartin <kyle@redhat.com>
- Cherry pick fix out of rawhide for %{with_*} tests in module
  signing from Jan Stancek.

* Mon Jun 24 2013 Josh Boyer <jwboyer@redhat.com>
- Fix battery issue with bluetooth keyboards (rhbz 903741)

* Fri Jun 21 2013 Josh Boyer <jwboyer@redhat.com>
- Add two patches to fix iwlwifi issues in unmapping
- Add patch to fix carl9170 oops (rhbz 967271)

* Thu Jun 20 2013 Justin M. Forbes <jforbes@redhat.com>
- Linux v3.9.7

* Tue Jun 18 2013 Neil Horman <nhorman@redhat.com>
- Fix dma debug error in tulip driver (rhbz 956732)

* Tue Jun 18 2013 Dave Jones <davej@redhat.com>
- Disable MTRR sanitizer by default.

* Mon Jun 17 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.6-301
- Add patch to fix radeon issues on powerpc

* Sat Jun 15 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9.6-gnu.

* Thu Jun 13 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.6-300
- Linux v3.9.6
- Drop a bunch of powerpc patches that were includes in 3.9.6.  Yay!

* Wed Jun 12 2013 Kyle McMartin <kmcmarti@redhat.com>
- Merge %{with_pae} and %{with_lpae} so both ARM and i686 use the same
  flavours. Set %{pae} to the flavour name {lpae, PAE}. Merging
  the descriptions would be nice, but is somewhat irrelevant...

* Wed Jun 12 2013 Josh Boyer <jwboyer@redhat.com>
- Update gssproxy patches
- Fix KVM divide by zero error (rhbz 969644)
- Add fix for rt5390/rt3290 regression (rhbz 950735)

* Tue Jun 11 2013 Dave Jones <davej@redhat.com>
- Disable soft lockup detector on virtual machines. (rhbz 971139)

* Tue Jun 11 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.5-301
- Temporarily disable gssproxy patches
- Add two patches to fix vfio device permissions (rhbz 967230)
- Add patches to fix MTRR issues in 3.9.5 (rhbz 973185)
- Add two patches to fix issues with vhost_net and macvlan (rhbz 954181)
- CVE-2013-2164 information leak in cdrom driver (rhbz 973100 973109)

* Mon Jun 10 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9.5-gnu.

* Mon Jun 10 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.5-300
- Apply scsi lockdep patch for powerpc IPR issues (rhbz 954252)
- Linux v3.9.5

* Fri Jun 07 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-2851 block: passing disk names as format strings (rhbz 969515 971662)
- CVE-2013-2852 b43: format string leaking into error msgs (rhbz 969518 971665)

* Thu Jun 06 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-2148 fanotify: info leak in copy_event_to_user (rhbz 971258 971261)
- CVE-2013-2147 cpqarray/cciss: information leak via ioctl (rhbz 971242 971249)

* Wed Jun 05 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-2140 xen: blkback: insufficient permission checks for BLKIF_OP_DISCARD (rhbz 971146 971148)

* Tue Jun 04 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.4-302
- Add gssproxy backport from J. Bruce Fields
- Fix build issue with PowerPC MSI patches (rhbz 962496)

* Mon Jun 03 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.4-301
- Fix UEFI anti-bricking code (rhbz 964335)
- Add patches to fix PowerPC MSI handling (rhbz 962496)

* Sat Jun  1 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Add patch to fix DRM/X on omap (panda)
- Enable Cortex-A8 errata on multiplatform kernels (omap3)

* Fri May 31 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-2850 iscsi-target: heap buffer overflow on large key error (rhbz 968036 969272)

* Thu May 30 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM tegra config

* Sat May 25 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9.4-gnu.

* Fri May 24 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.3-300
- Linux v3.9.4

* Fri May 24 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to quiet irq remapping failures (rhbz 948262)

* Thu May 23 2013 Josh Boyer <jwboyer@redhat.com>
- Fix oops from incorrect rfkill set in hp-wmi (rhbz 964367)

* Wed May 22 2013 Josh Boyer <jwboyer@redhat.com>
- Fix memcmp error in iwlwifi

* Tue May 21 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Enable OMAP5 on ARM multiplatform

* Mon May 20 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9.3-gnu.

* Mon May 20 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.3-301
- Linux v3.9.3

* Thu May 16 2013 Josh Boyer <jwboyer@redhat.com>
- Fix config-local usage (rhbz 950841)

* Wed May 15 2013 Dave Airlie <airlied@redhat.com>
- fix nomodeset on radeon (rhbz 924507)

* Tue May 14 2013 Dave Airlie <airlied@redhat.com>
- backport upstream qxl fixes, fixes VM crash on X exit or randr.

* Mon May 13 2013 Josh Boyer <jwboyer@redhat.com>
- Add radeon fixes for PCI-e gen2 speed issues (rhbz 961527)

* Mon May 13 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9.2-gnu.

* Mon May 13 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.2-301
- Linux v3.9.2

* Thu May  9 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Disable PL330 on ARM as it's broken on highbank

* Thu May  9 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9.1-gnu.

* Wed May 08 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.1-301
- Linux v3.9.1

* Tue May 07 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-303
- Fix dmesg_restrict patch to avoid regression (rhbz 952655)

* Mon May  6 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Enable TPS65217 (am33xx) on ARM

* Mon May 06 2013 Josh Boyer <jwboyer@redhat.com>
- Don't remove headers explicitly exported via UAPI (rhbz 959467)

* Fri May 03 2013 Josh Boyer <jwboyer@redhat.com>
- Add two more patches for POWER

* Wed May 01 2013 Josh Boyer <jwboyer@redhat.com>
- Add some powerpc fixes for POWER8

* Tue Apr 30 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Enable CONFIG_SERIAL_8250_DW on ARM

* Mon Apr 29 2013 Neil Horman <nhorman@redhat.com>
- Enabled CONFIG_PACKET_DIAG (rhbz 956870)
 
* Mon Apr 29 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.9-gnu.

* Mon Apr 29 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-301
- Linux v3.9

* Fri Apr 26 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to prevent scheduling while atomic error in blkcg

* Fri Apr 26 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre Sun Apr 28
- GNU Linux-libre 3.9-rc8-gnu.

* Wed Apr 24 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc8.git0.2
- Add patch to fix EFI boot on Macs (rhbz 953447)

* Mon Apr 22 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.0-0.rc8.git0.1
- Linux v3.9-rc8

* Mon Apr 22 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Minor ARM updates

* Fri Apr 19 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix RCU splat from perf events

* Fri Apr 19 2013 Peter Robinson <pbrobinson@fedoraproject.org> 
- Temporaily disable cpu idle on ARM as it appears to be causing stability issues
- Minor ARM config updates
- Add patch for DT DMA issues that affect at least highbank/tegra ARM devices

* Fri Apr 19 2013 Josh Boyer <jwboyer@redhat.com>
- Disable Intel HDA and enable RSXX block dev on ppc64/ppc64p7

* Thu Apr 18 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.0-0.rc7.git3.1
- Linux v3.9-rc7-70-gd202f05
- Disable debugging options.

* Tue Apr 16 2013 Josh Boyer <jwboyer@redhat.com>
- Fix uninitialized variable free in iwlwifi (rhbz 951241)
- Fix race in regulatory code (rhbz 919176)

* Mon Apr 15 2013 Josh Boyer <jwboyer@redhat.com>
- Fix debug patches to build on s390x/ppc

* Mon Apr 15 2013 Josh Boyer <jwboyer@redhat.com>
- Grab fixes for UEFI space issues (rhbz 947142)

* Fri Apr 12 2013 Josh Boyer <jwboyer@redhat.com>
- Enable CONFIG_LDM_PARTITION (rhbz 948636)

* Thu Apr 11 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.0-0.rc6.git2
- Linux v3.9-rc6-115-g7ee32a6
- libsas: use right function to alloc smp response (rhbz 949875)
- Reenable debugging options.

* Thu Apr 11 2013 Dave Jones <davej@redhat.com>
- Print out some extra debug information when we hit bad page tables.

* Tue Apr  9 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Add patch to fix building some ARM tegra modules
- Some minor ARM OMAP updates

* Mon Apr 08 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.0-0.rc6.git0.1
- Disable debugging options.
- Linux v3.9-rc6

* Fri Apr 05 2013 Justin M. Forbes <jforbes@redhat.com>
- Move cpufreq drivers to be modular (rhbz 746372)

* Wed Apr 03 2013 Dave Jones <davej@redhat.com>
- Enable MTD_CHAR/MTD_BLOCK (Needed for SFC)
  Enable 10gigE on 64-bit only.

* Wed Apr 03 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.0-0.rc5.git2.1
- Linux v3.9-rc5-146-gda241ef
- Drop basrelease back to 1 until 3.9 is out of rc

* Wed Apr  3 2013 Peter Robinson <pbrobinson@fedoraproject.org> 
- Add upstream usb-next OMAP patch to fix usb on omap/mvebu

* Tue Apr 02 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.0-0.rc5.git1.301
- Linux v3.9-rc5-108-g118c9a4
- Reenable debugging options.

* Tue Apr 02 2013 Josh Boyer <jwboyer@redhat.com>
- Enable CONFIG_FB_MATROX_G on powerpc

* Tue Apr 02 2013 Neil Horman <nhorman@redhat.com>
- Fix dma debug error on unmap (rhbz 928024)

* Tue Apr 02 2013 Josh Boyer <jwboyer@redhat.com>
- Enable CONFIG_SCSI_DMX3191D (rhbz 919874)

* Tue Apr 02 2013 Justin M. Forbes <jforbes@redhat.com> - 3.9.0-0.rc5.git0.1
- Linux v3.9-rc5

* Mon Apr 01 2013 Josh Boyer <jwboyer@redhat.com>
- Enable CONFIG_MCE_INJECT (rhbz 927353)

* Mon Apr  1 2013 Peter Robinson <pbrobinson@fedoraproject.org> 
- Minor ARM LPAE updates

* Sun Mar 31 2013 Peter Robinson <pbrobinson@fedoraproject.org> 
- Make tegra inherit armv7-generic, fix and re-enable tegra
- Enable SPI on ARM
- Drop config-arm-generic
- ARM config updates

* Thu Mar 28 2013 Peter Robinson <pbrobinson@fedoraproject.org> 
- Update ARM unified config for OMAP

* Tue Mar 26 2013 Justin M. Forbes <jforbes@redhat.com>
- Fix child thread introspection of of /proc/self/exe (rhbz 927469)

* Tue Mar 26 2013 Dave Jones <davej@redhat.com>
- Enable CONFIG_DM_CACHE (rhbz 924325)

* Tue Mar 26 2013 Josh Boyer <jwboyer@redhat.com>
- Add quirk for Realtek card reader to avoid 10 sec boot delay (rhbz 806587)
- Add quirk for MSI keyboard backlight to avoid 10 sec boot delay (rhbz 907221)

* Mon Mar 25 2013 Justin M. Forbes <jforbes@redhat.com>
- disable whci-hcd since it doesnt seem to have users (rhbz 919289)

* Sun Mar 24 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc4.git0.1
- Linux 3.9-rc4
  merged: drm-i915-bounds-check-execbuffer-relocation-count.patch

* Sun Mar 24 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM config for OMAP/mvebu/lpae

* Fri Mar 22 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc3.git1.4
- Fix calculation of current frequency in intel_pstate driver. (rhbz 923942)

* Thu Mar 21 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc3.git1.2
- Only print "bad: scheduling from the idle thread" warning once.

* Thu Mar 21 2013 Josh Boyer <jwboyer@redhat.com>
- Fix workqueue crash in mac80211 (rhbz 920218)

* Thu Mar 21 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc3.git1.1
- Linux v3.9-rc3-148-g2ffdd7e
- Fixes CVE-2013-1796, CVE-2013-1797, CVE-2013-1798 in kvm.

* Wed Mar 20 2013 Dave Jones <davej@redhat.com>
- Enable CONFIG_DM_DELAY (rhbz 923721)

* Tue Mar 19 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc3.git0.4
- Reenable debugging options.

* Tue Mar 19 2013 Dave Jones <davej@redhat.com>
- cpufreq/intel_pstate: Add function to check that all MSR's are valid (rhbz 922923)

* Mon Mar 18 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc3.git0.3
- s390x config option changes from Dan Hor??k <dan@danny.cz>
  - enable PCI
  - disable few useless drivers
  - disable drivers conflicting with s390x

* Mon Mar 18 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc3.git0.2
- Linux 3.9-rc3
  merged: w1-fix-oops-when-w1_search-is-called-from.patch
- Disable debugging options.

* Sun Mar 17 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Merge OMAP support into ARM unified kernel
- Add ARM LPAE kernel for Cortex A-15 devices that support LPAE and HW virtualisation
- Unified ARM kernel provides highbank and OMAP support
- Drop remantents of ARM softfp kernels

* Fri Mar 15 2013 Josh Boyer <jwboyer@redhat.com>
- Fix divide by zero on host TSC calibration failure (rhbz 859282)

* Fri Mar 15 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc2.git1.1
- Linux v3.9-rc2-292-ga2362d2
- Fixes CVE-2013-1860 kernel: usb: cdc-wdm buffer overflow triggered by device

* Wed Mar 13 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc2.git0.3
- Reenable debugging options.

* Tue Mar 12 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix ieee80211_do_stop (rhbz 892599)
- Add patches to fix cfg80211 issues with suspend (rhbz 856863)
- CVE-2013-0913 drm/i915: head writing overflow (rhbz 920471 920529)
- CVE-2013-0914 sa_restorer information leak (rhbz 920499 920510)

* Tue Mar 12 2013 Dave Airlie <airlied@redhat.com>
- add QXL driver (f19 only)

* Mon Mar 11 2013 Dave Jones <davej@redhat.com> - 3.9.0-0.rc2.git0.2
- Disable debugging options.

* Mon Mar 11 2013 Dave Jones <davej@redhat.com>
- Linux 3.9-rc2

* Mon Mar 11 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to allow "8250." prefix to keep working (rhbz 911771)
- Add patch to fix w1_search oops (rhbz 857954)

* Sun Mar 10 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc1.git2.1
- Linux v3.9-rc1-278-g8343bce

* Sun Mar 10 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Enable Xilinx Zynq
- Enable highbank cpufreq driver

* Fri Mar 08 2013 Josh Boyer <jwboyer@redhat.com>
- Add turbostat and x86_engery_perf_policy debuginfo to kernel-tools-debuginfo

* Fri Mar 08 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc1.git1.1
- Linux v3.9-rc1-211-g47b3bc9
- Reenable debugging options.
- CVE-2013-1828 sctp: SCTP_GET_ASSOC_STATS stack buffer overflow (rhbz 919315 919316)

* Thu Mar 07 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-1792 keys: race condition in install_user_keyrings (rhbz 916646 919021)

* Wed Mar 06 2013 Josh Boyer <jwboyer@redhat.com>
- Adjust secure-boot patchset to work with boot_params sanitizing
- Don't clear efi_info in boot_params (rhbz 918408)

* Wed Mar 06 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM mvebu config

* Wed Mar 06 2013 Dave Jones <davej@redhat.com>
- drop acpi debugging patch.

* Wed Mar 06 2013 Justin M. Forbes <jforbes@redhat.com>
- Remove Ricoh multifunction DMAR patch as it's no longer needed (rhbz 880051)

* Tue Mar 05 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc1.git0.3
- Fix intel_pstate init error path (rhbz 916833)

* Tue Mar  5 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Temporarily disable tegra until we get a fix from upstream

* Tue Mar 05 2013 Josh Boyer <jwboyer@redhat.com>
- Add 3 fixes for efi issues (rhbz 917984)
- Enable CONFIG_IP6_NF_TARGET_MASQUERADE

* Mon Mar 04 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc1.git0.1
- Linux v3.9-rc1
- Add patch from Dirk Brandewie to fix intel pstate divide error (rhbz 916833)
- Disable debugging options.

* Mon Mar  4 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Update vexpress and omap options (fix MMC on qemu, hopefully fix OMAP3)

* Sun Mar 03 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git15.1
- Linux v3.8-10734-ga7c1120

* Fri Mar 01 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git14.1
- Linux v3.8-10206-gb0af9cd

* Fri Mar 01 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git13.1
- Linux v3.8-9761-gde1a226

* Thu Feb 28 2013 Kyle McMartin <kmcmarti@redhat.com>
- Make iso9660 a module.

* Thu Feb 28 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git12.1
- Linux v3.8-9633-g2a7d2b9

* Wed Feb 27 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Drop ARM kirkwood kernel
- Enable SPI on ARM
- General 3.9 updates

* Wed Feb 27 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git11.1
- Linux v3.8-9456-g309667e

* Wed Feb 27 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git10.1
- Linux v3.8-9405-gd895cb1

* Tue Feb 26 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git9.1
- Linux v3.8-9165-g1cef935

* Tue Feb 26 2013 Kyle McMartin <kmcmarti@redhat.com>
- Move VMXNET3 to config-x86-generic from config-generic, it's VMware
  virtual ethernet.

* Tue Feb 26 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git8.1
- Linux v3.8-8664-gc41b381

* Tue Feb 26 2013 Kyle McMartin <kmcmarti@redhat.com>
- Add blk_queue_physical_block_size and register_netdevice to the symbols
  used for initrd generation (synched from .el6)
- ipr.ko driven SAS VRAID cards found on x86_64 machines these days, and not
  just on ppc64

* Tue Feb 26 2013 Josh Boyer <jwboyer@redhat.com>
- Fix vmalloc_fault oops during lazy MMU (rhbz 914737)

* Mon Feb 25 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git7.1
- Honor dmesg_restrict for /dev/kmsg (rhbz 903192)
- Linux v3.8-7888-gab78265

* Sun Feb 24 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git6.1
- Linux v3.8-6988-g9e2d59a

* Sun Feb 24 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-1763 sock_diag: out-of-bounds access to sock_diag_handlers (rhbz 915052,915057)

* Fri Feb 22 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git5.1
- Linux v3.8-6071-g8b5628a

* Fri Feb 22 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git4.1
- Linux v3.8-6071-g8b5628a
- Enable the rtl8192e driver (rhbz 913753)

* Thu Feb 21 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git3.1
- Linux v3.8-3195-g024e4ec
- Shut up perf about missing build things we don't care about
- Drop the old aic7xxx driver, from Paul Bolle

* Thu Feb 21 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git2.1
- Linux v3.8-3040-ga0b1c42

* Thu Feb 21 2013 Josh Boyer <jwboyer@redhat.com> - 3.9.0-0.rc0.git1.1
- Linux v3.8-523-gece8e0b
- Reenable debugging options.

* Tue Feb 19 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-2
- Add pekey support from David Howells and rework secure-boot patchset on top
- Add support for Atheros 04ca:3004 bluetooth devices (rhbz 844750)
- Backport support for newer ALPS touchpads (rhbz 812111)
- Enable CONFIG_AUDIT_LOGINUID_IMMUTABLE

* Tue Feb 19 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.8-gnu.

* Tue Feb 19 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-1
- Linux v3.8
- Fix build with CONFIG_EFI disabled, reported by Peter Bowey (rhbz 911833)
- Disable debugging options.

* Mon Feb 18 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc7.git4.1
- Linux v3.8-rc7-93-gf741656

* Thu Feb 14 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc7.git3.1
- Linux v3.8-rc7-73-g323a72d

* Thu Feb 14 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix corruption on newer M6116 SATA bridges (rhbz 909591)
- CVE-2013-0228 xen: xen_iret() invalid %ds local DoS (rhbz 910848 906309)

* Wed Feb 13 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Disable tegra30

* Wed Feb 13 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc7.git2.1
- Linux v3.8-rc7-32-gecf223f

* Tue Feb 12 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to create a convenient mount point for pstore (rhbz 910126)

* Tue Feb 12 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc7.git1.1
- Linux v3.8-rc7-6-g211b0cd
- Reenable debugging options.

* Mon Feb 11 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch from Kees Cook to restrict MSR writting in secure boot mode
- Build PATA_MACIO in on powerpc (rhbz 831361)

* Sat Feb  9 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.8-rc7-gnu.

* Fri Feb 08 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc7.git0.1
- Linux v3.8-rc7
- Add patch to fix atomic sleep issue on alloc_pid failure (rhbz 894623)
- Disable debugging options.

* Thu Feb  7 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Minor ARM build fixes

* Wed Feb 06 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc6.git3.3
- Enable CONFIG_NAMESPACES everywhere (rhbz 907576)
- Add patch to fix ath9k dma stop checks (rhbz 892811)

* Wed Feb 06 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc6.git3.1
- Linux v3.8-rc6-98-g1589a3e
- Add patch to honor MokSBState (rhbz 907406)

* Wed Feb  6 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.8-rc6-gnu.

* Tue Feb 05 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc6.git2.1
- Linux v3.8-rc6-62-gfe547d7
- Enable CONFIG_DRM_VMWGFX_FBCON (rhbz 907620)
- Enable CONFIG_DETECT_HUNG_TASK

* Mon Feb 04 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc6.git1.1
- Linux v3.8-rc6-22-g6edacf0
- Enable CONFIG_EXT4_DEBUG
- Fix rtlwifi scheduling while atomic from Larry Finger (rhbz 903881)

* Fri Feb 01 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc6.git0.1
- Linux v3.8-rc6
- Enable CONFIG_DMA_API_DEBUG
- Add patches to improve mac80211 latency and throughput (rhbz 830151)

* Thu Jan 31 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc5.git3.1
- Linux v3.8-rc5-245-g04c2eee
- Enable CONFIG_DEBUG_STACK_USAGE

* Wed Jan 30 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc5.git2.1
- Linux v3.8-rc5-218-ga56e160
- Enable NAMESPACES and CHECKPOINT_RESTORE on x86_64 for F19 CRIU feature
- Enable CONFIG_DEBUG_ATOMIC_SLEEP

* Tue Jan 29 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc5.git1.1
- Linux v3.8-rc5-150-g6abb7c2

* Tue Jan 29 2013 Josh Boyer <jwboyer@redhat.com>
- Backport driver for Cypress PS/2 trackpad (rhbz 799564)

* Mon Jan 28 2013 Josh Boyer <jwboyer@redhat.com> - 3.8.0-0.rc5.git0.1
- Linux v3.8-rc5
- Add patches to fix issues with iwlwifi (rhbz 863424)
- Enable CONFIG_PROVE_RCU

* Sun Jan 27 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Reenable perf on ARM (was suppose to be temporary)
- Build and package dtbs on ARM
- Enable FB options for qemu vexpress on unified

* Fri Jan 25 2013 Kyle McMartin <kmcmarti@redhat.com>
- Sign all modules with the mod-extra-sign.sh script, ensures nothing gets
  missed because of .config differences between invocations of BuildKernel.

* Fri Jan 25 2013 Justin M. Forbes <jforbes@redhat.com>
- Turn off THP for 32bit

* Fri Jan 25 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc4.git5.1
- Linux v3.8-rc4-277-g66e2d3e
- Enable slub debug

* Thu Jan 24 2013 Josh Boyer <jwboyer@redhat.com>
- Update secure-boot patchset

* Thu Jan 24 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc4.git4.1
- Linux v3.8-rc4-183-gff7532c
- Enable lockdep

* Wed Jan 23 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc4.git3.1
- Linux v3.8-rc4-139-g1d85490
- Enable debug spinlocks

* Wed Jan 23 2013 Dave Jones <davej@redhat.com>
- Remove warnings about empty IPI masks.

* Sun Jan 20 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Remove obsolete ARM configs
- Update OMAP config for TI AM35XX SoCs
- Add patch to fix versatile build failure

* Sat Jan 19 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc4.git1.1
- Linux v3.8-rc4-42-g5da1f88

* Fri Jan 18 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc4.git0.1
- Linux v3.8-rc4
- Disable debugging options.

* Fri Jan 18 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Disable problematic PL310 ARM errata
- Minor ARM config tweaks
- OMAP DRM driver to fix OMAP kernel build

* Wed Jan 16 2013 Josh Boyer <jwboyer@redhat.com>
- Fix power management sysfs on non-secure boot machines (rhbz 896243)

* Wed Jan 16 2013 Dave Jones <davej@redhat.com>
- Experiment: Double the length of the brcmsmac transmit timeout.

* Wed Jan 16 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch from Stanislaw Gruszka to fix iwlegacy IBSS cleanup (rhbz 886946)

* Tue Jan 15 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc3.git2.1
- Linux v3.8-rc3-293-g406089d

* Tue Jan 15 2013 Josh Boyer <jwboyer@redhat.com>
- Enable CONFIG_DVB_USB_V2 (rhbz 895460)

* Mon Jan 14 2013 Josh Boyer <jwboyer@redhat.com>
- Enable Orinoco drivers in kernel-modules-extra (rhbz 894069)

* Mon Jan 14 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc3.git1.1
- Linux v3.8-rc3-74-gb719f43

* Fri Jan 11 2013 Josh Boyer <jwboyer@redhat.com>
- Update secure-boot patchset

* Thu Jan 10 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc3.git0.2
- Reenable debugging options.

* Thu Jan 10 2013 Dave Jones <davej@redhat.com>
- Drop old Montevina era E1000 workaround.

* Thu Jan 10 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc3.git0.1
- Linux v3.8-rc3
- Disable debugging options.

* Wed Jan 09 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc2.git4.1
- Linux v3.8-rc2-370-g57a0c1e

* Wed Jan  9 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM mvebu config

* Wed Jan 09 2013 Josh Boyer <jwboyer@redhat.com>
- Enable CONFIG_CIFS_DEBUG as it was on before it was split out

* Tue Jan 08 2013 Kyle McMartin <kmcmarti@redhat.com>
- Ensure modules are signed even if *-debuginfo rpms are not produced by
  re-defining __spec_install_post and adding a hook after all strip
  invocations. Ideally, in the future, we could patch the rpm macro and
  remove the re-define from kernel.spec, but that's another windmill to tilt
  at.

* Tue Jan 08 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc2.git3.1
- Linux v3.8-rc2-222-g2a893f9

* Mon Jan 07 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc2.git2.1
- Linux v3.8-rc2-191-gd287b87
- remove the namei-include.patch, it's upstream now

* Mon Jan 07 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc2.git1.2
- Reenable debugging options.

* Mon Jan  7 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Further ARM config updates
- Add patch to fix building omapdrm

* Mon Jan 07 2013 Justin M. Forbes <jforbes@redhat.com>
- Bye sparc

* Mon Jan 07 2013 Justin M. Forbes <jforbes@redhat.com>
- Fix up configs for build

* Mon Jan 07 2013 Josh Boyer <jwboyer@redhat.com>
- Patch to fix efivarfs underflow from Lingzhu Xiang (rhbz 888163)

* Sat Jan  5 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Initial update of ARM configs for 3.8
- Enable DRM driver for tegra
- Drop separate imx kernel. Will be reintroduced soon in unified

* Fri Jan 04 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.0-0.rc2.git1.1
- Linux v3.8-rc2-116-g5f243b9

* Thu Jan 03 2013 Justin M. Forbes <jforbes@redhat.com>
- Initial 3.8-rc2 rebase

* Wed Jan 02 2013 Josh Boyer <jwboyer@redhat.com>
- BR the hostname package (rhbz 886113)

* Tue Dec 18 2012 Dave Jones <davej@redhat.com>
- On rebases, list new config options.
  (Revert to pre-18 behaviour)

* Mon Dec 17 2012 Josh Boyer <jwboyer@redhat.com>
- Fix oops in sony-laptop setup (rhbz 873107)

* Fri Dec 14 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Add patch to fix arm imx drm driver build

* Wed Dec 12 2012 Josh Boyer <jwboyer@redhat.com>
- Fix infinite loop in efi signature parser
- Don't error out if db doesn't exist

* Tue Dec 11 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM configs for latest 3.7
- Drop highbank kernel build variant as its in unified kernel

* Tue Dec 11 2012 Josh Boyer <jwboyer@redhat.com>
- Update secure boot patches to include MoK support
- Fix IBSS scanning in mac80211 (rhbz 883414)

* Tue Dec 11 2012 Dave Jones <davej@redhat.com> - 3.7.0-2
- Reenable debugging options.

* Tue Dec 11 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7-gnu

* Tue Dec 11 2012 Dave Jones <davej@redhat.com> - 3.7.0-1
- Linux v3.7

###
# The following Emacs magic makes C-c C-e use UTC dates.
# Local Variables:
# rpm-change-log-uses-utc: t
# End:
###
