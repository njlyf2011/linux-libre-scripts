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
%global baserelease 201
%global fedora_build %{baserelease}

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 3.1-rc7-git1 starts with a 3.0 base,
# which yields a base_sublevel of 0.
%define base_sublevel 8

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
#define librev

%define baselibre -libre
%define basegnu -gnu%{?librev}

# To be inserted between "patch" and "-2.6.".
#define stablelibre -3.8%{?stablegnux}
#define rcrevlibre -3.8%{?rcrevgnux}
#define gitrevlibre -3.8%{?gitrevgnux}

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
%define stable_update 3
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
%define gitrev 100
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
# ARM OMAP (Beagle/Panda Board)
%define with_omap      %{?_without_omap:      0} %{?!_without_omap:      1}
# kernel-tegra (only valid for arm)
%define with_tegra       %{?_without_tegra:       0} %{?!_without_tegra:       1}
# kernel-kirkwood (only valid for arm)
%define with_kirkwood       %{?_without_kirkwood:       0} %{?!_without_kirkwood:       1}
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
# Vanilla kernels before 3.7 don't contain modsign support.  Remove this when
# we rebase to 3.7
%define signmodules 0
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

# kernel up (versatile express), tegra and  omap are only built on armv7 hfp/sfp
%ifnarch armv7hl armv7l
%define with_omap 0
%define with_tegra 0
%endif

# kernel-kirkwood is only built for armv5
%ifnarch armv5tel
%define with_kirkwood 0
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

# sparse blows up on ppc64
%ifarch ppc64 ppc ppc64p7
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
%define make_target bzImage
%define kernel_image arch/arm/boot/zImage
# we only build headers/perf/tools on the base arm arches
# just like we used to only build them on i386 for x86
%ifarch armv5tel
%define with_up 0
%endif
%ifnarch armv5tel armv7hl
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
BuildRequires: net-tools, hostname
BuildRequires: xmlto, asciidoc
%if %{with_sparse}
BuildRequires: sparse >= 0.4.1
%endif
%if %{with_perf}
BuildRequires: elfutils-devel zlib-devel binutils-devel newt-devel python-devel perl(ExtUtils::Embed) bison audit-libs-devel
%endif
%if %{with_tools}
BuildRequires: pciutils-devel gettext
%endif
BuildConflicts: rhbuildsys(DiskFree) < 500Mb
%if %{with_debuginfo}
# Fancy new debuginfo generation introduced in Fedora 8/RHEL 6.
# The -r flag to find-debuginfo.sh to invoke eu-strip --reloc-debug-sections
# reduces the number of relocations in kernel module .ko.debug files and was
# introduced with rpm 4.9 and elfutils 0.153.
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
Source18: mod-extra-sign.sh

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
Source100: config-armv7

# Legacy ARM kernels
Source105: config-arm-generic
Source110: config-arm-omap
Source111: config-arm-tegra
Source112: config-arm-kirkwood

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
Patch391: acpi-video-dos.patch
Patch394: acpi-debug-infinite-loop.patch
Patch396: acpi-sony-nonvs-blacklist.patch

Patch450: input-kill-stupid-messages.patch
Patch452: no-pcspkr-modalias.patch

Patch460: serial-460800.patch

Patch470: die-floppy-die.patch

Patch510: silence-noise.patch
Patch520: quiet-apm.patch
Patch530: silence-fbcon-logo.patch
Patch540: silence-empty-ipi-mask-warning.patch
Patch541: silence-tty-null.patch

Patch800: crash-driver.patch

# secure boot
Patch1000: secure-boot-20130219.patch

# virt + ksm patches

# DRM
#atch1700: drm-edid-try-harder-to-fix-up-broken-headers.patch
#Patch1800: drm-vgem.patch

# nouveau + drm fixes
# intel drm is all merged upstream
Patch1824: drm-intel-next.patch
Patch1825: drm-i915-dp-stfu.patch
# mustard patch to shut abrt up. please drop (and notify ajax) whenever it
# fails to apply
Patch1826: drm-i915-tv-detect-hush.patch

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

Patch13003: efi-dont-map-boot-services-on-32bit.patch

Patch14000: hibernate-freeze-filesystems.patch

Patch14010: lis3-improve-handling-of-null-rate.patch

Patch14011: team-net-next-update-20130307.patch


Patch20000: 0001-efifb-Skip-DMI-checks-if-the-bootloader-knows-what-i.patch
Patch20001: 0002-x86-EFI-Calculate-the-EFI-framebuffer-size-instead-o.patch

# ARM

# ARM tegra
Patch21004: arm-tegra-nvec-kconfig.patch
Patch21005: arm-tegra-usb-no-reset-linux33.patch
Patch21006: arm-tegra-sdhci-module-fix.patch

#rhbz 754518
Patch21235: scsi-sd_revalidate_disk-prevent-NULL-ptr-deref.patch

Patch22000: weird-root-dentry-name-debug.patch

#selinux ptrace child permissions
Patch22001: selinux-apply-different-permission-to-ptrace-child.patch

#rhbz 859485
Patch22226: vt-Drop-K_OFF-for-VC_MUTE.patch

#rhbz 799564
Patch22240: Input-increase-struct-ps2dev-cmdbuf-to-8-bytes.patch
Patch22241: Input-add-support-for-Cypress-PS2-Trackpads.patch

#rhbz 912166
Patch22243: Input-cypress_ps2-fix-trackpadi-found-in-Dell-XPS12.patch

#rhbz 892811
Patch22247: ath9k_rx_dma_stop_check.patch

#rhbz 903192
Patch22261: 0001-kmsg-Honor-dmesg_restrict-sysctl-on-dev-kmsg.patch

#rhbz 914737
Patch22262: x86-mm-Fix-vmalloc_fault-oops-during-lazy-MMU-updates.patch

#rhbz 916544
Patch22263: 0001-drivers-crypto-nx-fix-init-race-alignmasks-and-GCM-b.patch

#CVE-2013-1828 rhbz 919315 919316
Patch22269: net-sctp-Validate-parameter-size-for-SCTP_GET_ASSOC_.patch

#rhbz 812111
Patch24000: alps.patch

Patch24100: userns-avoid-recursion-in-put_user_ns.patch

#rhbz 859346
Patch24101: fix-destroy_conntrack-GPF.patch

#rhbz 917353
Patch24102: backlight_revert.patch

#rhbz 904182
Patch24103: TTY-do-not-reset-master-s-packet-mode.patch

#rhbz 857954
Patch24105: w1-fix-oops-when-w1_search-is-called-from.patch

#rhbz 911771
Patch24106: serial-8250-Keep-8250.-xxxx-module-options-functiona.patch

#rhbz 879462
Patch24107: uvcvideo-suspend-fix.patch

#CVE-2013-0914 rhbz 920499 920510
Patch24108: signal-always-clear-sa_restorer-on-execve.patch

#CVE-2013-0913 rhbz 920471 920529
Patch24109: drm-i915-bounds-check-execbuffer-relocation-count.patch

#rhbz 856863 892599
Patch24111: cfg80211-mac80211-disconnect-on-suspend.patch
Patch24112: mac80211_fixes_for_ieee80211_do_stop_while_suspend_v3.8.patch

#rhbz 859282
Patch24113: VMX-x86-handle-host-TSC-calibration-failure.patch

#rhbz 920586
Patch25000: amd64_edac_fix_rank_count.patch

#rhbz 921500
Patch25001: i7300_edac_single_mode_fixup.patch

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
Provides: python-perf-debuginfo = %{rpmversion}-%{pkg_release}
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
#	%%kernel_modules-extra_package <subpackage> <pretty-name>
#
%define kernel_modules-extra_package() \
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
Provides: kernel-modules-extra-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-modules-extra-uname-r = %{KVERREL}%{?1:.%{1}}\
Requires: kernel-uname-r = %{KVERREL}%{?1:.%{1}}\
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
%{expand:%%kernel_modules-extra_package %1 %{!?-n:%1}%{?-n:%{-n*}}}\
%{expand:%%kernel_debuginfo_package %1}\
%{nil}


# First the auxiliary packages of the main kernel package.
%kernel_devel_package
%kernel_modules-extra_package
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

%define variant_summary The Linux kernel compiled for marvell kirkwood boards
%kernel_variant_package kirkwood
%description kirkwood
This package includes a version of the Linux kernel with support for
marvell kirkwood based systems, i.e., guruplug, sheevaplug

%define variant_summary The Linux kernel compiled for TI-OMAP boards
%kernel_variant_package omap
%description omap
This package includes a version of the Linux kernel with support for
TI-OMAP based systems, i.e., BeagleBoard-xM.

%define variant_summary The Linux kernel compiled for tegra boards
%kernel_variant_package tegra
%description tegra
This package includes a version of the Linux kernel with support for
nvidia tegra based systems, i.e., trimslice, ac-100.


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
%if %{?all_arch_configs:1}%{!?all_arch_configs:0}
for i in %{all_arch_configs}
do
  mv $i $i.tmp
  ./merge.pl %{SOURCE1000} $i.tmp > $i
  rm $i.tmp
done
%endif

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

#ApplyPatch arm-tegra-nvec-kconfig.patch
ApplyPatch arm-tegra-usb-no-reset-linux33.patch
#ApplyPatch arm-tegra-sdhci-module-fix.patch

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
ApplyPatch acpi-video-dos.patch
ApplyPatch acpi-debug-infinite-loop.patch
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

# no-one cares about these warnings.
ApplyPatch silence-empty-ipi-mask-warning.patch
ApplyPatch silence-tty-null.patch

# Changes to upstream defaults.


# /dev/crash driver.
ApplyPatch crash-driver.patch

# secure boot
ApplyPatch secure-boot-20130219.patch

# Assorted Virt Fixes

# DRM core
#ApplyPatch drm-edid-try-harder-to-fix-up-broken-headers.patch
#ApplyPatch drm-vgem.patch

# Nouveau DRM

# Intel DRM
ApplyOptionalPatch drm-intel-next.patch
ApplyPatch drm-i915-dp-stfu.patch
ApplyPatch drm-i915-tv-detect-hush.patch

# silence the ACPI blacklist code
ApplyPatch silence-acpi-blacklist.patch
ApplyPatch quiet-apm.patch

# V4L/DVB updates/fixes/experimental drivers
#  apply if non-empty
ApplyOptionalPatch v4l-dvb-fixes.patch
ApplyOptionalPatch v4l-dvb-update.patch
ApplyOptionalPatch v4l-dvb-experimental.patch

# Patches headed upstream
ApplyPatch fs-proc-devtree-remove_proc_entry.patch

ApplyPatch disable-i8042-check-on-apple-mac.patch

ApplyPatch efi-dont-map-boot-services-on-32bit.patch

# FIXME: REBASE
#ApplyPatch hibernate-freeze-filesystems.patch

ApplyPatch lis3-improve-handling-of-null-rate.patch

#ApplyPatch 0001-efifb-Skip-DMI-checks-if-the-bootloader-knows-what-i.patch
#ApplyPatch 0002-x86-EFI-Calculate-the-EFI-framebuffer-size-instead-o.patch

#rhbz 754518
ApplyPatch scsi-sd_revalidate_disk-prevent-NULL-ptr-deref.patch

ApplyPatch weird-root-dentry-name-debug.patch

#selinux ptrace child permissions
ApplyPatch selinux-apply-different-permission-to-ptrace-child.patch

#rhbz 859485
ApplyPatch vt-Drop-K_OFF-for-VC_MUTE.patch

#rhbz 799564
ApplyPatch Input-increase-struct-ps2dev-cmdbuf-to-8-bytes.patch
ApplyPatch Input-add-support-for-Cypress-PS2-Trackpads.patch

#rhbz 912166
ApplyPatch Input-cypress_ps2-fix-trackpadi-found-in-Dell-XPS12.patch

#rhbz 892811
ApplyPatch ath9k_rx_dma_stop_check.patch

#rhbz 812111
ApplyPatch alps.patch

#rhbz 903192
ApplyPatch 0001-kmsg-Honor-dmesg_restrict-sysctl-on-dev-kmsg.patch

#rhbz 914737
ApplyPatch x86-mm-Fix-vmalloc_fault-oops-during-lazy-MMU-updates.patch

#rhbz 916544
ApplyPatch 0001-drivers-crypto-nx-fix-init-race-alignmasks-and-GCM-b.patch

ApplyPatch userns-avoid-recursion-in-put_user_ns.patch

#rhbz 859346
ApplyPatch fix-destroy_conntrack-GPF.patch

#CVE-2013-1828 rhbz 919315 919316
ApplyPatch net-sctp-Validate-parameter-size-for-SCTP_GET_ASSOC_.patch

#rhbz 917353
ApplyPatch backlight_revert.patch -R

#rhbz 920586
ApplyPatch amd64_edac_fix_rank_count.patch

#rhbz 921500
ApplyPatch i7300_edac_single_mode_fixup.patch

#Team Driver update
ApplyPatch team-net-next-update-20130307.patch

#rhbz 904182
ApplyPatch TTY-do-not-reset-master-s-packet-mode.patch

#rhbz 857954
ApplyPatch w1-fix-oops-when-w1_search-is-called-from.patch

#rhbz 911771
ApplyPatch serial-8250-Keep-8250.-xxxx-module-options-functiona.patch

#rhbz 879462
ApplyPatch uvcvideo-suspend-fix.patch

#CVE-2013-0914 rhbz 920499 920510
ApplyPatch signal-always-clear-sa_restorer-on-execve.patch

#CVE-2013-0913 rhbz 920471 920529
ApplyPatch drm-i915-bounds-check-execbuffer-relocation-count.patch

#rhbz 856863 892599
ApplyPatch cfg80211-mac80211-disconnect-on-suspend.patch
ApplyPatch mac80211_fixes_for_ieee80211_do_stop_while_suspend_v3.8.patch

#rhbz 859282
ApplyPatch VMX-x86-handle-host-TSC-calibration-failure.patch

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

    %if 0%{?stable_update}
    # make sure SUBLEVEL is incremented on a stable release.  Sigh 3.x.
    perl -p -i -e "s/^SUBLEVEL.*/SUBLEVEL = %{?stablerev}/" Makefile
    %endif

    # make sure EXTRAVERSION says what we want it to say
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = -libre.%{release}.%{_target_cpu}${Flavour:+.${Flavour}}/" Makefile

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
    chmod +x scripts/sign-file
    %endif

    Arch=`head -1 .config | cut -b 3-`
    echo USING ARCH=$Arch

    make -s ARCH=$Arch oldnoconfig >/dev/null
%ifarch %{arm}
    # http://lists.infradead.org/pipermail/linux-arm-kernel/2012-March/091404.html
    make -s ARCH=$Arch V=1 %{?_smp_mflags} $MakeTarget %{?sparse_mflags} KALLSYMS_EXTRA_PASS=1

    make -s ARCH=$Arch V=1 dtbs
    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}/dtb-$KernelVer
    install -m 644 arch/arm/boot/dts/*.dtb $RPM_BUILD_ROOT/boot/dtb-$KernelVer/
    rm -f arch/arm/boot/dts/*.dtb
%else
    make -s ARCH=$Arch V=1 %{?_smp_mflags} $MakeTarget %{?sparse_mflags}
%endif
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
    %if %{signmodules}
    # Sign the image if we're using EFI
    %pesign -s -i $KernelImage -o vmlinuz.signed
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
    			 'register_netdev|ieee80211_register_hw|usbnet_probe|phy_driver_register|rt(l_|2x00)(pci|usb)_probe'
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

    # Call the modules-extra script to move things around
    %{SOURCE17} $RPM_BUILD_ROOT/lib/modules/$KernelVer %{SOURCE16}

%if %{signmodules}
    # Save off the .tmp_versions/ directory.  We'll use it in the 
    # __debug_install_post macro below to sign the right things
    # Also save the signing keys so we actually sign the modules with the
    # right key.
    cp -r .tmp_versions .tmp_versions.sign${Flavour:+.${Flavour}}
    cp signing_key.priv signing_key.priv.sign${Flavour:+.${Flavour}}
    cp signing_key.x509 signing_key.x509.sign${Flavour:+.${Flavour}}
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
BuildKernel %make_target %kernel_image PAEdebug
%endif

%if %{with_pae}
BuildKernel %make_target %kernel_image PAE
%endif

%if %{with_kirkwood}
BuildKernel %make_target %kernel_image kirkwood
%endif

%if %{with_omap}
BuildKernel %make_target %kernel_image omap
%endif

%if %{with_tegra}
BuildKernel %make_target %kernel_image tegra
%endif

%if %{with_up}
BuildKernel %make_target %kernel_image
%endif

%if %{with_smp}
BuildKernel %make_target %kernel_image smp
%endif

%global perf_make \
  make %{?_smp_mflags} -C tools/perf -s V=1 WERROR=0 HAVE_CPLUS_DEMANGLE=1 prefix=%{_prefix}
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
    cd tools/power/cpupower/debug/i386
    make %{?_smp_mflags} centrino-decode powernow-k8-decode
    cd -
%endif
%ifarch x86_64
    cd tools/power/cpupower/debug/x86_64
    make %{?_smp_mflags} centrino-decode powernow-k8-decode
    cd -
%endif
%ifarch %{ix86} x86_64
   cd tools/power/x86/x86_energy_perf_policy/
   make
   cd -
   cd tools/power/x86/turbostat
   make
   cd -
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

###
### Special hacks for debuginfo subpackages.
###

# This macro is used by %%install, so we must redefine it before that.
%define debug_package %{nil}

# In the modsign case, we do 3 things.  1) We check the "flavour" and hard
# code the value in the following invocations.  This is somewhat sub-optimal
# but we're doing this inside of an RPM macro and it isn't as easy as it
# could be because of that.  2) We restore the .tmp_versions/ directory from
# the one we saved off in BuildKernel above.  This is to make sure we're
# signing the modules we actually built/installed in that flavour.  3) We
# grab the arch and invoke 'make modules_sign' and the mod-extra-sign.sh
# commands to actually sign the modules.
#
# We have to do all of those things _after_ find-debuginfo runs, otherwise
# that will strip the signature off of the modules.

%if %{with_debuginfo}
%define __debug_install_post \
  /usr/lib/rpm/find-debuginfo.sh %{debuginfo_args} %{_builddir}/%{?buildsubdir}\
  if [ "%{signmodules}" == "1" ]; \
  then \
    if [ "%{with_pae}" != "0" ]; \
    then \
      Arch=`head -1 configs/kernel-%{version}-%{_target_cpu}-PAE.config | cut -b 3-` \
      rm -rf .tmp_versions \
      mv .tmp_versions.sign.PAE .tmp_versions \
      mv signing_key.priv.sign.PAE signing_key.priv \
      mv signing_key.x509.sign.PAE signing_key.x509 \
      make -s ARCH=$Arch V=1 INSTALL_MOD_PATH=$RPM_BUILD_ROOT modules_sign KERNELRELEASE=%{KVERREL}.PAE \
      %{SOURCE18} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}.PAE/extra/ \
    fi \
    if [ "%{with_debug}" != "0" ]; \
    then \
      Arch=`head -1 configs/kernel-%{version}-%{_target_cpu}-debug.config | cut -b 3-` \
      rm -rf .tmp_versions \
      mv .tmp_versions.sign.debug .tmp_versions \
      mv signing_key.priv.sign.debug signing_key.priv \
      mv signing_key.x509.sign.debug signing_key.x509 \
      make -s ARCH=$Arch V=1 INSTALL_MOD_PATH=$RPM_BUILD_ROOT modules_sign KERNELRELEASE=%{KVERREL}.debug \
      %{SOURCE18} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}.debug/extra/ \
    fi \
    if [ "%{with_pae_debug}" != "0" ]; \
    then \
      Arch=`head -1 configs/kernel-%{version}-%{_target_cpu}-PAEdebug.config | cut -b 3-` \
      rm -rf .tmp_versions \
      mv .tmp_versions.sign.PAEdebug .tmp_versions \
      mv signing_key.priv.sign.PAEdebug signing_key.priv \
      mv signing_key.x509.sign.PAEdebug signing_key.x509 \
      make -s ARCH=$Arch V=1 INSTALL_MOD_PATH=$RPM_BUILD_ROOT modules_sign KERNELRELEASE=%{KVERREL}.PAEdebug \
      %{SOURCE18} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}.PAEdebug/extra/ \
    fi \
    if [ "%{with_up}" != "0" ]; \
    then \
      Arch=`head -1 configs/kernel-%{version}-%{_target_cpu}.config | cut -b 3-` \
      rm -rf .tmp_versions \
      mv .tmp_versions.sign .tmp_versions \
      mv signing_key.priv.sign signing_key.priv \
      mv signing_key.x509.sign signing_key.x509 \
      make -s ARCH=$Arch V=1 INSTALL_MOD_PATH=$RPM_BUILD_ROOT modules_sign KERNELRELEASE=%{KVERREL} \
      %{SOURCE18} $RPM_BUILD_ROOT/lib/modules/%{KVERREL}/extra/ \
    fi \
  fi \
%{nil}

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

# glibc provides scsi headers for itself, for now
rm -rf $RPM_BUILD_ROOT/usr/include/scsi
rm -f $RPM_BUILD_ROOT/usr/include/asm*/atomic.h
rm -f $RPM_BUILD_ROOT/usr/include/asm*/io.h
rm -f $RPM_BUILD_ROOT/usr/include/asm*/irq.h
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
    cd tools/power/cpupower/debug/i386
    install -m755 centrino-decode %{buildroot}%{_bindir}/centrino-decode
    install -m755 powernow-k8-decode %{buildroot}%{_bindir}/powernow-k8-decode
    cd -
%endif
%ifarch x86_64
    cd tools/power/cpupower/debug/x86_64
    install -m755 centrino-decode %{buildroot}%{_bindir}/centrino-decode
    install -m755 powernow-k8-decode %{buildroot}%{_bindir}/powernow-k8-decode
    cd -
%endif
chmod 0755 %{buildroot}%{_libdir}/libcpupower.so*
mkdir -p %{buildroot}%{_unitdir} %{buildroot}%{_sysconfdir}/sysconfig
install -m644 %{SOURCE2000} %{buildroot}%{_unitdir}/cpupower.service
install -m644 %{SOURCE2001} %{buildroot}%{_sysconfdir}/sysconfig/cpupower
%endif
%ifarch %{ix86} x86_64
   mkdir -p %{buildroot}%{_mandir}/man8
   cd tools/power/x86/x86_energy_perf_policy
   make DESTDIR=%{buildroot} install
   cd -
   cd tools/power/x86/turbostat
   make DESTDIR=%{buildroot} install
   cd -
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
#	%%kernel_modules-extra_post [<subpackage>]
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

%kernel_variant_preun PAE
%kernel_variant_post -v PAE -r (kernel|kernel-smp)

%kernel_variant_preun debug
%kernel_variant_post -v debug

%kernel_variant_post -v PAEdebug -r (kernel|kernel-smp)
%kernel_variant_preun PAEdebug

%kernel_variant_preun kirkwood
%kernel_variant_post -v kirkwood

%kernel_variant_preun omap
%kernel_variant_post -v omap

%kernel_variant_preun tegra
%kernel_variant_post -v tegra

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
%kernel_variant_files %{with_pae} PAE
%kernel_variant_files %{with_pae_debug} PAEdebug
%kernel_variant_files %{with_kirkwood} kirkwood
%kernel_variant_files %{with_omap} omap
%kernel_variant_files %{with_tegra} tegra

# plz don't put in a version string unless you're going to tag
# and build.

#  ___________________________________________________________
# / This branch is for Fedora 18. You probably want to commit \
# \ to the F-17 branch instead, or in addition to this one.   /
#  -----------------------------------------------------------
#         \   ^__^
#          \  (@@)\_______
#             (__)\       )\/\
#                 ||----w |
#                 ||     ||
%changelog
* Fri Mar 15 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.8.3-gnu.

* Thu Mar 14 2013 Justin M. Forbes <jforbes@redhat.com> 3.8.3-201
- Linux v3.8.3

* Thu Mar 14 2013 Josh Boyer <jwboyer@redhat.com>
- Fix divide by zero on host TSC calibration failure (rhbz 859282)

* Thu Mar 14 2013 Mauro Carvalho Chehab <mchehab@redhat.com>
- fix i7300_edac twice-mem-size-report via EDAC API (rhbz 921500)

* Tue Mar 12 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix ieee80211_do_stop (rhbz 892599)
- Add patches to fix cfg80211 issues with suspend (rhbz 856863)
- Add patch to fix Cypress trackpad on XPS 12 machines (rhbz 912166)
- CVE-2013-0913 drm/i915: head writing overflow (rhbz 920471 920529)
- CVE-2013-0914 sa_restorer information leak (rhbz 920499 920510)

* Mon Mar 11 2013 Mauro Carvalho Chehab <mchehab@redhat.com>
- fix amd64_edac twice-mem-size-report via EDAC API (rhbz 920586)

* Mon Mar 11 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix usb_submit_urb error in uvcvideo (rhbz 879462)
- Add patch to allow "8250." prefix to keep working (rhbz 911771)
- Add patch to fix w1_search oops (rhbz 857954)
- Add patch to fix broken tty handling (rhbz 904182)

* Fri Mar 08 2013 Josh Boyer <jwboyer@redhat.com>
- Add turbostat and x86_engery_perf_policy debuginfo to kernel-tools-debuginfo

* Fri Mar 08 2013 Justin M. Forbes <jforbes@redhat.com>
- Revert "write backlight harder" until better solution is found (rhbz 917353)
- Update team driver from net-next from Jiri Pirko

* Fri Mar 08 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-1828 sctp: SCTP_GET_ASSOC_STATS stack buffer overflow (rhbz 919315 919316)

* Fri Mar  8 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Have kernel provide kernel-highbank for upgrade to unified
- Update mvebu configs
- Drop unused ARM patches

* Thu Mar 07 2013 Josh Boyer <jwboyer@redhat.com>
- Fix DMI regression (rhbz 916444)
- Fix logitech-dj HID bug from Benjamin Tissoires (rhbz 840391)
- CVE-2013-1792 keys: race condition in install_user_keyrings (rhbz 916646 919021)

* Wed Mar 06 2013 Justin M. Forbes <jforbes@redhat.com>
- Remove Ricoh multifunction DMAR patch as it's no longer needed (rhbz 880051)
- Fix destroy_conntrack GPF (rhbz 859346)

* Wed Mar 06 2013 Josh Boyer <jwboyer@redhat.com>
- Fix regression in secure-boot acpi_rsdp patch (rhbz 906225)
- crypto: info leaks in report API (rhbz 918512 918521)

* Tue Mar  5 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Fix Beagle (omap), update vexpress

* Tue Mar 05 2013 Josh Boyer <jwboyer@redhat.com>
- Backport 4 fixes for efivarfs (rhbz 917984)
- Enable CONFIG_IP6_NF_TARGET_MASQUERADE

* Mon Mar 04 2013 Josh Boyer <jwboyer@redhat.com>
- Fix issues in nx crypto driver from Kent Yoder (rhbz 916544)

* Mon Mar  4 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
* GNU Linux-libre 3.8.2-gnu.

* Mon Mar 04 2013 Justin M. Forbes <jforbes@redhat.com> - 3.8.2-201
- Linux v3.8.2

* Mon Mar  4 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Fix DTB generation on ARM

* Fri Mar 01 2013 Dave Jones <davej@redhat.com>
- Silence "tty is NULL" trace.

* Fri Mar 01 2013 Josh Boyer <jwboyer@redhat.com>
- Add patches to fix sunrpc panic (rhbz 904870)

* Thu Feb 28 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM config for 3.8

* Thu Feb 28 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
* GNU Linux-libre 3.8.1-gnu.

* Thu Feb 28 2013 Dave Jones <davej@redhat.com>
- Remove no longer needed E1000 hack.

* Thu Feb 28 2013 Dave Jones <davej@redhat.com>
- Drop SPARC64 support.

* Thu Feb 28 2013 Dave Jones <davej@redhat.com>
- Linux 3.8.1
  Dropped (merged in 3.8.1)
  - drm-i915-lvds-reclock-fix.patch
  - usb-cypress-supertop.patch
  - perf-hists-Fix-period-symbol_conf.field_sep-display.patch
  - ipv6-dst-from-ptr-race.patch
  - sock_diag-Fix-out-of-bounds-access-to-sock_diag_handlers.patch
  - tmpfs-fix-use-after-free-of-mempolicy-object.patch

* Thu Feb 28 2013 Dave Jones <davej@redhat.com>
- Update usb-cypress-supertop.patch

* Wed Feb 27 2013 Dave Jones <davej@redhat.com>
- Update ALPS patch to what got merged in 3.9-rc

* Wed Feb 27 2013 Dave Jones <davej@redhat.com>
- 3.8.0
  Dropped (merged in 3.8)
  - arm-l2x0-only-set-set_debug-on-pl310-r3p0-and-earlier.patch
  - power-x86-destdir.patch
  - modsign-post-KS-jwb.patch
  - efivarfs-3.7.patch
  - handle-efi-roms.patch
  - drm-i915-Fix-up-mismerge-of-3490ea5d-in-3.7.y.patch
  - USB-report-submission-of-active-URBs.patch
  - exec-use-eloop-for-max-recursion-depth.patch
  - 8139cp-revert-set-ring-address-before-enabling-receiver.patch
  - 8139cp-set-ring-address-after-enabling-C-mode.patch
  - 8139cp-re-enable-interrupts-after-tx-timeout.patch
  - brcmsmac-updates-rhbz892428.patch
  - silence-brcmsmac-warning.patch
  - net-fix-infinite-loop-in-__skb_recv_datagram.patch
  - Bluetooth-Add-support-for-Foxconn-Hon-Hai-0489-e056.patch
  - 0001-bluetooth-Add-support-for-atheros-04ca-3004-device-t.patch
  Needs checking:
  - arm-tegra-nvec-kconfig.patch
  - arm-tegra-sdhci-module-fix.patch

* Tue Feb 26 2013 Justin M. Forbes <jforbes@redhat.com>
- Avoid recursion in put_user_ns, potential overflow

* Tue Feb 26 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-1767 tmpfs: fix use-after-free of mempolicy obj (rhbz 915592,915716)
- Fix vmalloc_fault oops during lazy MMU (rhbz 914737)

* Mon Feb 25 2013 Josh Boyer <jwboyer@redhat.com>
- Honor dmesg_restrict for /dev/kmsg (rhbz 903192)

* Sun Feb 24 2013 Josh Boyer <jwboyer@redhat.com> - 3.7.9-205
- CVE-2013-1763 sock_diag: out-of-bounds access to sock_diag_handlers (rhbz 915052,915057)

* Fri Feb 22 2013 Josh Boyer <jwboyer@redhat.com>
- Add support for bluetooth in Acer Aspire S7 (rhbz 879408)

* Thu Feb 21 2013 Neil Horman <nhorman@redhat.com>
- Fix crash from race in ipv6 dst entries (rhbz 892060)

* Wed Feb 20 2013 Josh Boyer <jwboyer@redhat.com>
- Fix perf report field separator issue (rhbz 906055)
- Fix oops from acpi_rsdp setup in secure-boot patchset (rhbz 906225)

* Tue Feb 19 2013 Josh Boyer <jwboyer@redhat.com>
- Add support for Atheros 04ca:3004 bluetooth devices (rhbz 844750)
- Backport support for newer ALPS touchpads (rhbz 812111)

* Tue Feb 19 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Fix OMAP thermal driver by building it in (seems it doesn't auto load when a module)

* Tue Feb 19 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.9-gnu.

* Mon Feb 18 2013 Justin M. Forbes <jforbes@redhat.com> - 3.7.9-201
- Linux v3.7.9

* Mon Feb 18 2013 Adam Jackson <ajax@redhat.com
- i915: Fix a mismerge in 3.7.y that leads to divide-by-zero in i915_update_wm

* Sat Feb 16 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.8-gnu.

* Fri Feb 15 2013 Josh Boyer <jwboyer@redhat.com>
- CVE-2013-0290 net: infinite loop in __skb_recv_datagram (rhbz 911479 911473)

* Thu Feb 14 2013 Justin M. Forbes <jforbes@redhat.com> - 3.7.8-201
- Linux v3.7.8

* Thu Feb 14 2013 Adam Jackson <ajax@redhat.com>
- i915: Hush asserts during TV detection, just useless noise
- i915: Fix LVDS downclock to not cripple performance (#901951)

* Thu Feb 14 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix corruption on newer M6116 SATA bridges (rhbz 909591)
- CVE-2013-0228 xen: xen_iret() invalid %ds local DoS (rhbz 910848 906309)

* Tue Feb 12 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.7-gnu.

* Tue Feb 12 2013 Dave Jones <davej@redhat.com>
- Add networking queue for next stable release.

* Tue Feb 12 2013 Dave Jones <davej@redhat.com>
- mm: Check if PUD is large when validating a kernel address

* Tue Feb 12 2013 Dave Jones <davej@redhat.com>
- Silence brcmsmac warnings. (Fixed in 3.8, but not backporting to 3.7)

* Tue Feb 12 2013 Justin M. Forbes <jforbes@redhat.com>
- Linux v3.7.7

* Mon Feb 11 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch from Kees Cook to restrict MSR writting in secure boot mode
- Add patch to honor MokSBState (rhbz 907406)

* Thu Feb  7 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Minor ARM build fixes

* Wed Feb 06 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix ath9k dma stop checks (rhbz 892811)

* Mon Feb  4 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.6-gnu.

* Mon Feb 04 2013 Josh Boyer <jwboyer@redhat.com>
- Linux v3.7.6
- Update secure-boot patchset
- Fix rtlwifi scheduling while atomic from Larry Finger (rhbz 903881)

* Tue Jan 29 2013 Josh Boyer <jwboyer@redhat.com>
- Backport driver for Cypress PS/2 trackpad (rhbz 799564)

* Mon Jan 28 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.5-gnu.

* Mon Jan 28 2013 Josh Boyer <jwboyer@redhat.com> - 3.7.5-201
- Linux v3.7.5
- Add patch to fix iwlwifi issues (rhbz 863424)

* Sun Jan 27 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Build and package dtbs on ARM
- Enable FB options for qemu vexpress on unified

* Fri Jan 25 2013 Justin M. Forbes <jforbes@redhat.com>
- Turn off THP for 32bit

* Wed Jan 23 2013 Justin M. Forbes <jforbes@redhat.com> - 3.7.4-204
- brcmsmac fixes from upstream (rhbz 892428)

* Wed Jan 23 2013 Dave Jones <davej@redhat.com>
- Remove warnings about empty IPI masks.

* Tue Jan 22 2013 Justin M. Forbes <jforbes@redhat.com> - 3.7.4-203
- Add i915 bugfix from airlied

* Tue Jan 22 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Apply ARM errata fix
- disable HVC_DCC and VIRTIO_CONSOLE on ARM

* Tue Jan 22 2013 Josh Boyer <jwboyer@redhat.com>
- Fix libata settings bug (rhbz 902523)

* Tue Jan 22 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.4-gnu

* Mon Jan 21 2013 Josh Boyer <jwboyer@redhat.com> - 3.7.4-201
- Linux v3.7.4

* Mon Jan 21 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.3-gnu

* Fri Jan 18 2013 Justin M. Forbes <jforbes@redhat.com> 3.7.3-201
- Linux v3.7.3

* Thu Jan 17 2013 Peter Robinson <pbrobinson@fedoraproject.org>
- Merge 3.7 ARM kernel including unified kernel
- Drop separate IMX and highbank kernels
- Disable ARM PL310 errata that crash highbank

* Wed Jan 16 2013 Josh Boyer <jwboyer@redhat.com>
- Fix power management sysfs on non-secure boot machines (rhbz 896243)

* Wed Jan 16 2013 Justin M. Forbes <jforbes@redhat.com>  3.7.2-204
- Fix for CVE-2013-0190 xen corruption with 32bit pvops (rhbz 896051 896038)

* Wed Jan 16 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch from Stanislaw Gruszka to fix iwlegacy IBSS cleanup (rhbz 886946)

* Tue Jan 15 2013 Justin M. Forbes <jforbes@redhat.com> 3.7.2-203
- Turn off Intel IOMMU by default
- Stable queue from 3.7.3 with many relevant fixes

* Tue Jan 15 2013 Josh Boyer <jwboyer@redhat.com>
- Enable CONFIG_DVB_USB_V2 (rhbz 895460)

* Mon Jan 14 2013 Josh Boyer <jwboyer@redhat.com>
- Enable Orinoco drivers in kernel-modules-extra (rhbz 894069)

* Sun Jan 13 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.2-gnu

* Fri Jan 11 2013 Justin M. Forbes <jforbes@redhat.com> 3.7.1-1
- Linux v3.7.2
- Enable Intel IOMMU by default

* Thu Jan 10 2013 Dave Jones <davej@redhat.com>
- Add audit-libs-devel to perf build-deps to enable trace command. (rhbz 892893)

* Tue Jan 08 2013 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix shutdown on some machines (rhbz 890547)

* Mon Jan 07 2013 Josh Boyer <jwboyer@redhat.com>
- Patch to fix efivarfs underflow from Lingzhu Xiang (rhbz 888163)

* Sun Jan 06 2013 Josh Boyer <jwboyer@redhat.com>
- Fix version.h include due to UAPI change in 3.7 (rhbz 892373)

* Fri Jan 04 2013 Josh Boyer <jwboyer@redhat.com>
- Fix oops on aoe module removal (rhbz 853064)

* Fri Jan  4 2013 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.7.1

* Thu Jan 03 2013 Josh Boyer <jwboyer@redhat.com> - 3.7.1-2
- Fixup secure boot patchset for 3.7 rebase
- Package bash completion script for perf

* Thu Jan 03 2013 Dave Jones <davej@redhat.com>
- Rebase to 3.7.1

* Wed Jan 02 2013 Josh Boyer <jwboyer@redhat.com>
- Fix autofs issue in 3.6 (rhbz 874372)
- BR the hostname package (rhbz 886113)

* Mon Dec 17 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.11-gnu

* Mon Dec 17 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.11-3
- Linux v3.6.11

* Mon Dec 17 2012 Dennis Gilmore <dennis@ausil.us>
- disable gpiolib on vexpress

* Mon Dec 17 2012 Josh Boyer <jwboyer@redhat.com>
- Fix oops in sony-laptop setup (rhbz 873107)

* Wed Dec 12 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.10-5
- Fix infinite loop in efi signature parser
- Don't error out if db doesn't exist

* Tue Dec 11 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.10-gnu

* Tue Dec 11 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.10-4
- Update secure boot patches to include MoK support
- Fix IBSS scanning in mac80211 (rhbz 883414)

* Tue Dec 11 2012 Justin M. Forbes <jforbes@redhat.com> 3.6.10-1
- Linux 3.6.10

* Wed Dec 05 2012 Dave Jones <davej@redhat.com>
- Team driver updates (Jiri Pirko)

* Mon Dec 03 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.9-2
- Backport 3 upstream fixes to resolve radeon schedule IB errors (rhbz 855275)

* Mon Dec  3 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre Tue Dec  4
- GNU Linux-libre 3.6.9-gnu

* Mon Dec 03 2012 Justin M. Forbes <jforbes@redhat.com> 3.6.9-1
- Linux 3.6.9

* Thu Nov 29 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Update some ARM GPIO and I2C configs

* Tue Nov 27 2012 Josh Boyer <jwboyer@redhat.com>
- Update patches for 8139cp issues from David Woodhouse (rhbz 851278)

* Tue Nov 27 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.8-gnu

* Mon Nov 26 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.8-1
- Linux v3.6.8

* Mon Nov 26 2012 Josh Boyer <jwboyer@redhat.com>
- Fix regression in 8139cp driver, debugged by William J. Eaton (rhbz 851278)
- Fix ACPI video after _DOD errors (rhbz 869383)
- Fix ata command timeout oops in mvsas (rhbz 869629)
- Enable CONFIG_UIO_PDRV on ppc64 (rhbz 878180)
- CVE-2012-4530: stack disclosure binfmt_script load_script (rhbz 868285 880147)

* Tue Nov 20 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.7-5
- CVE-2012-4461: kvm: invalid opcode oops on SET_SREGS with OSXSAVE bit set (rhbz 878518 862900)
- Add VC_MUTE ioctl (rhbz 859485)
- Add support for BCM20702A0 (rhbz 874791)

* Tue Nov 20 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Change the minimum mmap address back to 32768 on ARM systems (thanks to Jon Masters)

* Mon Nov 19 2012 Josh Boyer <jwboyer@redhat.com>
- Apply patches from Jeff Moyer to fix direct-io oops (rhbz 812129)

* Mon Nov 19 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.7-gnu.

* Sat Nov 17 2012 Justin M. Forbes <jforbes@linuxtx.org> - 3.6.7-1
- linux 3.6.7

* Fri Nov 16 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.6-9
- Fix oops causing typo in keyspan driver (rhbz 870562)
- Don't WARN_ON empty queues in iwlwifi (rhbz 873001)

* Thu Nov 15 2012 Justin M. Forbes <jforbes@redhat.com>
- Fix panic in  panic in smp_irq_move_cleanup_interrupt (rhbz 869341)

* Wed Nov 14 2012 Josh Boyer <jwboyer@redhat.com>
- Fix module signing of kernel flavours

* Mon Nov 12 2012 Justin M. Forbes <jforbes@redhat.com>
- fix list_del corruption warning on USB audio with twinkle (rhbz 871078)

* Fri Nov 09 2012 Josh Boyer <jwboyer@redhat.com>
- Fix vanilla kernel builds (reported by Thorsten Leemhuis)

* Wed Nov 07 2012 Josh Boyer <jwboyer@redhat.com>
- Add patch to not break modules_install for external module builds

* Mon Nov  5 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.6-gnu.

* Mon Nov 05 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.6-3
- Backport efivarfs from efi/next for moktools
- Fix build break without CONFIG_EFI set (reported by Peter W. Bowey)
- Linux v3.6.6

* Mon Nov 05 2012 David Woodhouse <David.Woodhouse@intel.com> - 3.6.5-3
- Fix EFI framebuffer handling

* Wed Oct 31 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.5-2
- Update modsign to what is currently in 3.7-rc2
- Update secure boot support for UEFI cert importing.

* Wed Oct 31 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.5-gnu.

* Wed Oct 31 2012 Josh Boyer <jwboyer@redhat.com> - 3.6.5-1
- Linux v3.6.5
- CVE-2012-4565 net: divide by zero in tcp algorithm illinois (rhbz 871848 871923)

* Tue Oct 30 2012 Josh Boyer <jwboyer@redhat.com>
- Move power-x86-destdir.patch to apply on vanilla kernels (thanks knurd)

* Mon Oct 29 2012 Justin M. Forbes <jforbes@redhat.com>
- Uprobes backports from upstream

* Mon Oct 29 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM alignment patch to upstream

* Mon Oct 29 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.4-gnu.

* Mon Oct 29 2012 Justin M. Forbes <jforbes@redhat.com> 3.6.4-1
- Linux 3.6.4

* Thu Oct 25 2012 Justin M. Forbes <jforbes@redhat.com>
- CVE-2012-4508: ext4: AIO vs fallocate stale data exposure (rhbz 869904 869909)

* Wed Oct 24 2012 Josh Boyer <jwboyer@redhat.com>
- Remove patch added for rhbz 856863
- Add patch to fix corrupted text with i915 (rhbz 852210)

* Tue Oct 23 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Update OMAP Video config options

* Mon Oct 22 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- VIFO fails on ARM at the moment so disable it for the time being

* Mon Oct 22 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre Tue Oct 23
- GNU Linux-libre 3.6.3-gnu.

* Mon Oct 22 2012 Josh Boyer <jwboyer@redhat.com>
- Add patch to fix CIFS oops from Jeff Layton (rhbz 867344)
- CVE-2012-0957: uts: stack memory leak in UNAME26 (rhbz 862877 864824)
- Fix rt2x00 usb reset resume (rhbz 856863)

* Mon Oct 22 2012 Justin M. Forbes <jforbes@linuxtx.org> - 3.6.3-1
- Linux 3.6.3

* Mon Oct 22 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Revert ARM misaligned access check to stop kernel OOPS
- Actually apply highbank sata patch

* Thu Oct 18 2012 Josh Boyer <jwboyer@redhat.com>
- Patch to have mac80211 connect with HT20 if HT40 is not allowed (rhbz 866013)
- Enable VFIO (rhbz 867152)
- Apply patch from Stanislaw Gruszka to fix mac80211 issue (rhbz 862168)
- Apply patch to fix iwlwifi crash (rhbz 770484)

* Tue Oct 16 2012 Mauro Carvalho Chehab <mchehab@redhat.com> - 3.6.2-2
- Fix i82975x_edac OOPS

* Tue Oct 16 2012 Justin M. Forbes <jforbes@redhat.com>
- Enable CONFIG_TCM_VHOST (rhbz 866981)

* Mon Oct 15 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.2-gnu.

* Mon Oct 15 2012 Justin M. Forbes <jforbes@redhat.com> 3.6.2-1
- Linux 3.6.2

* Thu Oct 11 2012 Peter Robinson <pbrobinson@fedoraproject.org> 3.6.1-2
- Update ARM config for missing newoption items

* Tue Oct 09 2012 Josh Boyer <jwboyer@redhat.com>
- Drop unhandled irq polling patch

* Tue Oct  9 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6.1-gnu.

* Mon Oct 08 2012 Justin M. Forbes <jforbes@redhat.com> 3.6.1-1
- Linux 3.6.1

* Sat Oct 06 2012 Josh Boyer <jwboyer@redhat.com>
- secure boot modsign depends on CONFIG_MODULE_SIG not CONFIG_MODULES

* Fri Oct 05 2012 Josh Boyer <jwboyer@redhat.com>
- Adjust secure boot modsign patch

* Fri Oct  5 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Build MMC in on OMAP and Tegra until we work out why modules don't work

* Wed Oct 03 2012 Adam Jackson <ajax@redhat.com>
- Drop vgem patches, not doing anything yet.

* Wed Oct 03 2012 Josh Boyer <jwboyer@redhat.com>
- Make sure kernel-tools-libs-devel provides kernel-tools-devel

* Tue Oct 02 2012 Justin M. Forbes <jforbes@redhat.com> - 3.6.0-2
- Power: Fix VMX fix for memcpy case (rhbz 862420)

* Tue Oct 02 2012 Josh Boyer <jwboyer@redhat.com>
- Patch from David Howells to fix overflow on 32-bit X.509 certs (rhbz 861322)

* Tue Oct  2 2012 Peter Robinson <pbrobinson@fedoraproject.org>
- Update ARM configs for 3.6 final
- Add highbank SATA driver for stability
- Build in OMAP MMC and DMA drivers to fix borkage for now

* Mon Oct  1 2012 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 3.6-gnu

* Mon Oct 01 2012 Justin M. Forbes <jforbes@redhat.com> - 3.6.0-1
- Linux 3.6.0
- Disable debugging options.

###
# The following Emacs magic makes C-c C-e use UTC dates.
# Local Variables:
# rpm-change-log-uses-utc: t
# End:
###
