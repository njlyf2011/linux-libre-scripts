# We have to override the new %%install behavior because, well... the kernel is special.
%global __spec_install_pre %{___build_pre}

# this should go away soon
%define _legacy_common_support 1

# At the time of this writing (2019-03), RHEL8 packages use w2.xzdio
# compression for rpms (xz, level 2).
# Kernel has several large (hundreds of mbytes) rpms, they take ~5 mins
# to compress by single-threaded xz. Switch to threaded compression,
# and from level 2 to 3 to keep compressed sizes close to "w2" results.
#
# NB: if default compression in /usr/lib/rpm/redhat/macros ever changes,
# this one might need tweaking (e.g. if default changes to w3.xzdio,
# change below to w4T.xzdio):
#
# This is disabled on i686 as it triggers oom errors

%ifnarch i686
%define _binary_payload w3T.xzdio
%endif

Summary: The Linux kernel

# For a kernel released for public testing, released_kernel should be 1.
# For internal testing builds during development, it should be 0.
# For rawhide and/or a kernel built from an rc or git snapshot,
# released_kernel should be 0.
# For a stable, released kernel, released_kernel should be 1.
%global released_kernel 1

%if 0%{?fedora}
%define secure_boot_arch x86_64
%else
%define secure_boot_arch x86_64 aarch64 s390x ppc64le
%endif

# Signing for secure boot authentication
%ifarch %{secure_boot_arch}
%global signkernel 1
%else
%global signkernel 0
%endif

# Sign modules on all arches
%global signmodules 1

# Compress modules only for architectures that build modules
%ifarch noarch
%global zipmodules 0
%else
%global zipmodules 1
%endif

%if %{zipmodules}
%global zipsed -e 's/\.ko$/\.ko.xz/'
# for parallel xz processes, replace with 1 to go back to single process
%global zcpu `nproc --all`
%endif

# define buildid .local

%if 0%{?fedora}
%define primary_target fedora
%else
%define primary_target rhel
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
%global baserelease 100
%global fedora_build %{baserelease}

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 3.1-rc7-git1 starts with a 3.0 base,
# which yields a base_sublevel of 0.
%define base_sublevel 9

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
#define librev

%define baselibre -libre
%define basegnu -gnu%{?librev}

# To be inserted between "patch" and "-4.".
%define stablelibre -5.9%{?stablegnux}
#define rcrevlibre  -5.9%{?rcrevgnux}
#define gitrevlibre -5.9%{?gitrevgnux}

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
%define stable_update 16
# Set rpm version accordingly
%if 0%{?stable_update}
%define stablerev %{stable_update}
%define stable_base %{stable_update}
%endif
%define rpmversion 5.%{base_sublevel}.%{stable_update}

## The not-released-kernel case ##
%else
# The next upstream release sublevel (base_sublevel+1)
%define upstream_sublevel %(echo $((%{base_sublevel} + 1)))
# The rc snapshot level
%global rcrev 0
# The git snapshot level
%define gitrev 0
# Set rpm version accordingly
%define rpmversion 5.%{upstream_sublevel}.0
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
# kernel PAE (only valid for ARM (lpae))
%define with_pae       %{?_without_pae:       0} %{?!_without_pae:       1}
# kernel-debug
%define with_debug     %{?_without_debug:     0} %{?!_without_debug:     1}
# kernel-doc
%define with_doc       %{?_without_doc:       0} %{?!_without_doc:       1}
# kernel-headers
%define with_headers   %{?_without_headers:   0} %{?!_without_headers:   1}
%define with_cross_headers   %{?_without_cross_headers:   0} %{?!_without_cross_headers:   1}
# perf
%define with_perf      %{?_without_perf:      0} %{?!_without_perf:      1}
# tools
%define with_tools     %{?_without_tools:     0} %{?!_without_tools:     1}
# bpf tool
%define with_bpftool   %{?_without_bpftool:   0} %{?!_without_bpftool:   1}
# kernel-debuginfo
%define with_debuginfo %{?_without_debuginfo: 0} %{?!_without_debuginfo: 1}
# Want to build a the vsdo directories installed
%define with_vdso_install %{?_without_vdso_install: 0} %{?!_without_vdso_install: 1}
# kernel-zfcpdump (s390 specific kernel for zfcpdump)
%define with_zfcpdump  %{?_without_zfcpdump:  0} %{?!_without_zfcpdump:  1}
# kernel-abi-whitelists
%define with_kernel_abi_whitelists %{?_without_kernel_abi_whitelists: 0} %{?!_without_kernel_abi_whitelists: 1}
# internal samples and selftests
%define with_selftests %{?_without_selftests: 0} %{?!_without_selftests: 1}
#
# Additional options for user-friendly one-off kernel building:
#
# Only build the base kernel (--with baseonly):
%define with_baseonly  %{?_with_baseonly:     1} %{?!_with_baseonly:     0}
# Only build the pae kernel (--with paeonly):
%define with_paeonly   %{?_with_paeonly:      1} %{?!_with_paeonly:      0}
# Only build the debug kernel (--with dbgonly):
%define with_dbgonly   %{?_with_dbgonly:      1} %{?!_with_dbgonly:      0}
# Control whether we perform a compat. check against published ABI.
%define with_kabichk   %{?_without_kabichk:   0} %{?!_without_kabichk:   1}
# Temporarily disable kabi checks until RC.
%define with_kabichk 0
# Control whether we perform a compat. check against DUP ABI.
%define with_kabidupchk %{?_with_kabidupchk:  1} %{?!_with_kabidupchk:   0}
#
# Control whether to run an extensive DWARF based kABI check.
# Note that this option needs to have baseline setup in SOURCE300.
%define with_kabidwchk %{?_without_kabidwchk: 0} %{?!_without_kabidwchk: 1}
%define with_kabidw_base %{?_with_kabidw_base: 1} %{?!_with_kabidw_base: 0}
#
# should we do C=1 builds with sparse
%define with_sparse    %{?_with_sparse:       1} %{?!_with_sparse:       0}
#
# Cross compile requested?
%define with_cross    %{?_with_cross:         1} %{?!_with_cross:        0}
#
# build a release kernel on rawhide
%define with_release   %{?_with_release:      1} %{?!_with_release:      0}

# verbose build, i.e. no silent rules and V=1
%define with_verbose %{?_with_verbose:        1} %{?!_with_verbose:      0}

#
# check for mismatched config options
%define with_configchecks %{?_without_configchecks:        0} %{?!_without_configchecks:        1}

#
# gcov support
%define with_gcov %{?_with_gcov:1}%{?!_with_gcov:0}

#
# ipa_clone support
%define with_ipaclones %{?_without_ipaclones: 0} %{?!_without_ipaclones: 1}

# Want to build a vanilla kernel build without any non-upstream patches?
%define with_vanilla %{?_with_vanilla: 1} %{?!_with_vanilla: 0}

# Set debugbuildsenabled to 1 for production (build separate debug kernels)
#  and 0 for rawhide (all kernels are debug kernels).
# See also 'make debug' and 'make release'.
%define debugbuildsenabled 1

%if 0%{?fedora}
# Kernel headers are being split out into a separate package
%define with_headers 0
%define with_cross_headers 0
# no selftests for now
%define with_selftests 0
# no ipa_clone for now
%define with_ipaclones 0
# no whitelist
%define with_kernel_abi_whitelists 0
# Fedora builds these separately
%define with_perf 0
%define with_tools 0
%define with_bpftool 0
%endif

%if %{with_verbose}
%define make_opts V=1
%else
%define make_opts -s
%endif

# pkg_release is what we'll fill in for the rpm Release: field
%if 0%{?released_kernel}

%define pkg_release %{fedora_build}%{?buildid}%{?dist}%{?libres}

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
%define kversion 5.%{base_sublevel}


# turn off debug kernel and kabichk for gcov builds
%if %{with_gcov}
%define with_debug 0
%define with_kabichk 0
%define with_kabidupchk 0
%define with_kabidwchk 0
%endif

# turn off kABI DWARF-based check if we're generating the base dataset
%if %{with_kabidw_base}
%define with_kabidwchk 0
%endif

# kpatch_kcflags are extra compiler flags applied to base kernel
# -fdump-ipa-clones is enabled only for base kernels on selected arches
%if %{with_ipaclones}
%ifarch x86_64 ppc64le
%define kpatch_kcflags -fdump-ipa-clones
%else
%define with_ipaclones 0
%endif
%endif

%define make_target bzImage
%define image_install_path boot

%define KVERREL %{version}-libre.%{release}.%{_target_cpu}
%define KVERREL_RE %(echo %KVERREL | sed 's/+/[+]/g')
%define hdrarch %_target_cpu
%define asmarch %_target_cpu

%if 0%{!?nopatches:1}
%define nopatches 0
%endif

%if %{with_vanilla}
%define nopatches 1
%endif

%if %{nopatches}
%define variant -vanilla
%endif

%if !%{debugbuildsenabled}
%define with_debug 0
%endif

%if !%{with_debuginfo}
%define _enable_debug_packages 0
%endif
%define debuginfodir /usr/lib/debug
# Needed because we override almost everything involving build-ids
# and debuginfo generation. Currently we rely on the old alldebug setting.
%global _build_id_links alldebug

# kernel PAE is only built on ARMv7
%ifnarch armv7hl
%define with_pae 0
%endif

# if requested, only build base kernel
%if %{with_baseonly}
%define with_pae 0
%define with_debug 0
%endif

# if requested, only build pae kernel
%if %{with_paeonly}
%define with_up 0
%define with_debug 0
%endif

# if requested, only build debug kernel
%if %{with_dbgonly}
%if %{debugbuildsenabled}
%define with_up 0
%endif
%define with_pae 0
%define with_tools 0
%define with_perf 0
%define with_bpftool 0
%endif

# turn off kABI DUP check and DWARF-based check if kABI check is disabled
%if !%{with_kabichk}
%define with_kabidupchk 0
%define with_kabidwchk 0
%endif

%if %{with_vdso_install}
%define use_vdso 1
%endif


%ifnarch noarch
%define with_kernel_abi_whitelists 0
%endif

# Overrides for generic default options

# only package docs noarch
%ifnarch noarch
%define with_doc 0
%define doc_build_fail true
%endif

%if 0%{?fedora}
# don't do debug builds on anything but i686 and x86_64
%ifnarch i686 x86_64
%define with_debug 0
%endif
%endif

# don't build noarch kernels or headers (duh)
%ifarch noarch
%define with_up 0
%define with_headers 0
%define with_cross_headers 0
%define with_tools 0
%define with_perf 0
%define with_bpftool 0
%define with_selftests 0
%define with_debug 0
%define all_arch_configs kernel-%{version}-*.config
%endif

# sparse blows up on ppc
%ifnarch ppc64le
%define with_sparse 0
%endif

# zfcpdump mechanism is s390 only
%ifnarch s390x
%define with_zfcpdump 0
%endif

%if 0%{?fedora}
# This is not for Fedora
%define with_zfcpdump 0
%endif

# Per-arch tweaks

%ifarch i686
%define asmarch x86
%define hdrarch i386
%define all_arch_configs kernel-%{version}-i?86*.config
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch x86_64
%define asmarch x86
%define all_arch_configs kernel-%{version}-x86_64*.config
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch ppc64le
%define asmarch powerpc
%define hdrarch powerpc
%define make_target vmlinux
%define kernel_image vmlinux
%define kernel_image_elf 1
%define all_arch_configs kernel-%{version}-ppc64le*.config
%define kcflags -O3
%endif

%ifarch s390x
%define asmarch s390
%define hdrarch s390
%define all_arch_configs kernel-%{version}-s390x.config
%define kernel_image arch/s390/boot/bzImage
%endif

%ifarch %{arm}
%define all_arch_configs kernel-%{version}-arm*.config
%define skip_nonpae_vdso 1
%define asmarch arm
%define hdrarch arm
%define make_target bzImage
%define kernel_image arch/arm/boot/zImage
# http://lists.infradead.org/pipermail/linux-arm-kernel/2012-March/091404.html
%define kernel_mflags KALLSYMS_EXTRA_PASS=1
# we only build headers/perf/tools on the base arm arches
# just like we used to only build them on i386 for x86
%ifnarch armv7hl
%define with_headers 0
%define with_cross_headers 0
%endif
# These currently don't compile on armv7
%define with_selftests 0
%endif

%ifarch aarch64
%define all_arch_configs kernel-%{version}-aarch64*.config
%define asmarch arm64
%define hdrarch arm64
%define make_target Image.gz
%define kernel_image arch/arm64/boot/Image.gz
%endif

# Should make listnewconfig fail if there's config options
# printed out?
%if %{nopatches}
%define with_configchecks 0
%endif

# To temporarily exclude an architecture from being built, add it to
# %%nobuildarches. Do _NOT_ use the ExclusiveArch: line, because if we
# don't build kernel-headers then the new build system will no longer let
# us use the previous build of that package -- it'll just be completely AWOL.
# Which is a BadThing(tm).

# We only build kernel-headers on the following...
%if 0%{?fedora}
%define nobuildarches i386
%else
%define nobuildarches i386 i686
%endif

%ifarch %nobuildarches
%define with_up 0
%define with_debug 0
%define with_debuginfo 0
%define with_perf 0
%define with_tools 0
%define with_bpftool 0
%define with_selftests 0
%define with_pae 0
%define _enable_debug_packages 0
%endif

# Architectures we build tools/cpupower on
%if 0%{?fedora}
%define cpupowerarchs %{ix86} x86_64 ppc64le %{arm} aarch64
%else
%define cpupowerarchs i686 x86_64 ppc64le aarch64
%endif

%if %{use_vdso}

%if 0%{?skip_nonpae_vdso}
%define _use_vdso 0
%else
%define _use_vdso 1
%endif

%else
%define _use_vdso 0
%endif

#
# Packages that need to be installed before the kernel is, because the %%post
# scripts use them.
#
%define kernel_prereq  coreutils, systemd >= 203-2, /usr/bin/kernel-install
%define initrd_prereq  dracut >= 027


Name: kernel-libre%{?variant}
License: GPLv2
URL: http://linux-libre.fsfla.org/
Version: %{rpmversion}
Release: %{pkg_release}
# DO NOT CHANGE THE 'ExclusiveArch' LINE TO TEMPORARILY EXCLUDE AN ARCHITECTURE BUILD.
# SET %%nobuildarches (ABOVE) INSTEAD
%if 0%{?fedora}
ExclusiveArch: x86_64 s390x %{arm} aarch64 ppc64le
%else
ExclusiveArch: noarch i386 i686 x86_64 s390x %{arm} aarch64 ppc64le
%endif
ExclusiveOS: Linux
%ifnarch %{nobuildarches}
Requires: kernel-libre-core-uname-r = %{KVERREL}%{?variant}
Requires: kernel-libre-modules-uname-r = %{KVERREL}%{?variant}
%endif


#
# List the packages used during the kernel build
#
BuildRequires: kmod, patch, bash, tar, git-core
BuildRequires: bzip2, xz, findutils, gzip, m4, perl-interpreter, perl-Carp, perl-devel, perl-generators, make, diffutils, gawk
BuildRequires: gcc, binutils, redhat-rpm-config, hmaccalc, bison, flex
BuildRequires: net-tools, hostname, bc, elfutils-devel
%if 0%{?fedora}
BuildRequires: dwarves
%endif
# Used to mangle unversioned shebangs to be Python 3
BuildRequires: python3-devel
%if %{with_headers}
BuildRequires: rsync
%endif
%if %{with_doc}
BuildRequires: xmlto, asciidoc, python3-sphinx
%endif
%if %{with_sparse}
BuildRequires: sparse
%endif
%if %{with_perf}
BuildRequires: zlib-devel binutils-devel newt-devel perl(ExtUtils::Embed) bison flex xz-devel
BuildRequires: audit-libs-devel
BuildRequires: java-devel
%ifnarch %{arm} s390x
BuildRequires: numactl-devel
%endif
%endif
%if %{with_tools}
BuildRequires: gettext ncurses-devel
%ifnarch s390x
BuildRequires: pciutils-devel
%endif
%endif
%if %{with_bpftool}
BuildRequires: python3-docutils
BuildRequires: zlib-devel binutils-devel
%endif
%if %{with_selftests}
%if 0%{?fedora}
BuildRequires: clang llvm
%else
BuildRequires: llvm-toolset
%endif
%ifnarch %{arm}
BuildRequires: numactl-devel
%endif
BuildRequires: libcap-devel libcap-ng-devel rsync
%endif
BuildConflicts: rhbuildsys(DiskFree) < 500Mb
%if %{with_debuginfo}
BuildRequires: rpm-build, elfutils
BuildConflicts: rpm < 4.13.0.1-19
%if 0%{?fedora}
BuildConflicts: dwarves < 1.13
%endif
# Most of these should be enabled after more investigation
%undefine _include_minidebuginfo
%undefine _find_debuginfo_dwz_opts
%undefine _unique_build_ids
%undefine _unique_debug_names
%undefine _unique_debug_srcs
%undefine _debugsource_packages
%undefine _debuginfo_subpackages
%global _find_debuginfo_opts -r
%global _missing_build_ids_terminate_build 1
%global _no_recompute_build_ids 1
%endif
%if %{with_kabidwchk} || %{with_kabidw_base}
BuildRequires: kabi-dw
%endif

%if %{signkernel}%{signmodules}
BuildRequires: openssl openssl-devel
%if %{signkernel}
%ifarch x86_64 aarch64
BuildRequires: nss-tools
BuildRequires: pesign >= 0.10-4
%endif
%endif
%endif

%if %{with_cross}
BuildRequires: binutils-%{_build_arch}-linux-gnu, gcc-%{_build_arch}-linux-gnu
%define cross_opts CROSS_COMPILE=%{_build_arch}-linux-gnu-
%endif

# These below are required to build man pages
%if %{with_perf}
BuildRequires: xmlto
%endif
%if %{with_perf} || %{with_tools}
BuildRequires: asciidoc
%endif

Source0: http://linux-libre.fsfla.org/pub/linux-libre/freed-ora/src/linux%{?baselibre}-%{kversion}%{basegnu}.tar.xz

# For documentation purposes only.
Source4: deblob-check
Source5: deblob-%{kversion}
# Source6: deblob-5.%{upstream_sublevel}

# Name of the packaged file containing signing key
%ifarch ppc64le
%define signing_key_filename kernel-signing-ppc.cer
%endif
%ifarch s390x
%define signing_key_filename kernel-signing-s390.cer
%endif

Source10: x509.genkey.rhel
Source11: x509.genkey.fedora
%if %{?released_kernel}

Source12: redhatsecurebootca5.cer
Source13: redhatsecurebootca1.cer
Source14: redhatsecureboot501.cer
Source15: redhatsecureboot301.cer
Source16: secureboot_s390.cer
Source17: secureboot_ppc.cer

%define secureboot_ca_1 %{SOURCE12}
%define secureboot_ca_0 %{SOURCE13}
%ifarch x86_64 aarch64
%define secureboot_key_1 %{SOURCE14}
%define pesign_name_1 redhatsecureboot501
%define secureboot_key_0 %{SOURCE15}
%define pesign_name_0 redhatsecureboot301
%endif
%ifarch s390x
%define secureboot_key_0 %{SOURCE16}
%define pesign_name_0 redhatsecureboot302
%endif
%ifarch ppc64le
%define secureboot_key_0 %{SOURCE17}
%define pesign_name_0 redhatsecureboot303
%endif

# released_kernel
%else

Source12: redhatsecurebootca4.cer
Source13: redhatsecurebootca2.cer
Source14: redhatsecureboot401.cer
Source15: redhatsecureboot003.cer

%define secureboot_ca_1 %{SOURCE12}
%define secureboot_ca_0 %{SOURCE13}
%define secureboot_key_1 %{SOURCE14}
%define pesign_name_1 redhatsecureboot401
%define secureboot_key_0 %{SOURCE15}
%define pesign_name_0 redhatsecureboot003

# released_kernel
%endif

Source22: mod-extra.list.rhel
Source23: mod-extra.list.fedora
Source24: mod-extra.sh
Source18: mod-sign.sh
Source19: mod-extra-blacklist.sh
Source79: parallel_xz.sh

Source80: filter-x86_64.sh.fedora
Source81: filter-armv7hl.sh.fedora
Source82: filter-i686.sh.fedora
Source83: filter-aarch64.sh.fedora
Source86: filter-ppc64le.sh.fedora
Source87: filter-s390x.sh.fedora
Source89: filter-modules.sh.fedora

Source90: filter-x86_64.sh.rhel
Source91: filter-armv7hl.sh.rhel
Source92: filter-i686.sh.rhel
Source93: filter-aarch64.sh.rhel
Source96: filter-ppc64le.sh.rhel
Source97: filter-s390x.sh.rhel
Source99: filter-modules.sh.rhel
%define modsign_cmd %{SOURCE18}

Source20: kernel-aarch64-rhel.config
Source21: kernel-aarch64-debug-rhel.config
Source30: kernel-ppc64le-rhel.config
Source31: kernel-ppc64le-debug-rhel.config
Source32: kernel-s390x-rhel.config
Source33: kernel-s390x-debug-rhel.config
Source34: kernel-s390x-zfcpdump-rhel.config
Source35: kernel-x86_64-rhel.config
Source36: kernel-x86_64-debug-rhel.config

Source37: kernel-aarch64-fedora.config
Source38: kernel-aarch64-debug-fedora.config
Source39: kernel-armv7hl-fedora.config
Source40: kernel-armv7hl-debug-fedora.config
Source41: kernel-armv7hl-lpae-fedora.config
Source42: kernel-armv7hl-lpae-debug-fedora.config
Source43: kernel-i686-fedora.config
Source44: kernel-i686-debug-fedora.config
Source45: kernel-ppc64le-fedora.config
Source46: kernel-ppc64le-debug-fedora.config
Source47: kernel-s390x-fedora.config
Source48: kernel-s390x-debug-fedora.config
Source49: kernel-x86_64-fedora.config
Source50: kernel-x86_64-debug-fedora.config



Source51: generate_all_configs.sh

Source52: process_configs.sh
Source53: generate_bls_conf.sh
Source56: update_scripts.sh

Source54: mod-internal.list
Source55: merge.pl

Source200: check-kabi

Source201: Module.kabi_aarch64
Source202: Module.kabi_ppc64le
Source203: Module.kabi_s390x
Source204: Module.kabi_x86_64

Source210: Module.kabi_dup_aarch64
Source211: Module.kabi_dup_ppc64le
Source212: Module.kabi_dup_s390x
Source213: Module.kabi_dup_x86_64

# Source300: kernel-abi-whitelists-%{rpmversion}-%{distro_build}.tar.bz2
# Source301: kernel-kabi-dw-%{rpmversion}-%{distro_build}.tar.bz2

# Sources for kernel-tools
Source2000: cpupower.service
Source2001: cpupower.config

## Patches needed for building this package

# Patch1: patch-%{rpmversion}-redhat.patch

# empty final patch to facilitate testing of kernel patches
# Patch999999: linux-kernel-test.patch

# This file is intentionally left empty in the stock kernel. Its a nicety
# added for those wanting to do custom rebuilds with altered config opts.
Source1000: kernel-local

# Here should be only the patches up to the upstream canonical Linus tree.

# For a stable release kernel
%if 0%{?stable_update}
%if 0%{?stable_base}
%define    stable_patch_00  patch%{?stablelibre}-5.%{base_sublevel}.%{stable_base}%{?stablegnu}.xz
Source5000: %{stable_patch_00}
%endif

# non-released_kernel case
# These are automagically defined by the rcrev and gitrev values set up
# near the top of this spec file.
%else
%if 0%{?rcrev}
Source5000: patch%{?rcrevlibre}-5.%{upstream_sublevel}-rc%{rcrev}%{?rcrevgnu}.xz
%if 0%{?gitrev}
Source5001: patch%{?gitrevlibre}-5.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}%{?gitrevgnu}.xz
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
Source5000: patch%{?gitrevlibre}-5.%{base_sublevel}-git%{gitrev}%{?gitrevgnu}.xz
%endif
%endif
%endif

## Patches needed for building this package

## compile fixes

Patch07: freedo.patch

%if !%{nopatches}

Patch6: 0001-ACPI-APEI-arm64-Ignore-broken-HPE-moonshot-APEI-supp.patch
Patch8: 0001-ACPI-irq-Workaround-firmware-issue-on-X-Gene-based-m.patch
Patch9: 0001-aarch64-acpi-scan-Fix-regression-related-to-X-Gene-U.patch
Patch11: 0001-kdump-round-up-the-total-memory-size-to-128M-for-cra.patch
Patch12: 0001-kdump-add-support-for-crashkernel-auto.patch
Patch15: 0001-kdump-fix-a-grammar-issue-in-a-kernel-message.patch
Patch19: 0001-Vulcan-AHCI-PCI-bar-fix-for-Broadcom-Vulcan-early-si.patch
Patch20: 0001-ahci-thunderx2-Fix-for-errata-that-affects-stop-engi.patch
Patch24: 0001-scsi-smartpqi-add-inspur-advantech-ids.patch
Patch26: 0001-ipmi-do-not-configure-ipmi-for-HPE-m400.patch
Patch28: 0001-iommu-arm-smmu-workaround-DMA-mode-issues.patch
Patch29: 0001-arm-aarch64-Drop-the-EXPERT-setting-from-ARM64_FORCE.patch
Patch31: 0001-Add-efi_status_to_str-and-rework-efi_status_to_err.patch
Patch32: 0001-Make-get_cert_list-use-efi_status_to_str-to-print-er.patch
Patch33: 0001-security-lockdown-expose-a-hook-to-lock-the-kernel-d.patch
Patch34: 0001-efi-Add-an-EFI_SECURE_BOOT-flag-to-indicate-secure-b.patch
Patch35: 0001-efi-Lock-down-the-kernel-if-booted-in-secure-boot-mo.patch
Patch36: 0001-s390-Lock-down-the-kernel-when-the-IPL-secure-flag-i.patch
Patch37: 0001-Add-option-of-13-for-FORCE_MAX_ZONEORDER.patch
Patch58: 0001-arm-make-CONFIG_HIGHPTE-optional-without-CONFIG_EXPE.patch
Patch59: 0001-ARM-tegra-usb-no-reset.patch
Patch61: 0001-Input-rmi4-remove-the-need-for-artificial-IRQ-in-cas.patch
Patch62: 0001-Drop-that-for-now.patch
Patch63: 0001-KEYS-Make-use-of-platform-keyring-for-module-signatu.patch
Patch64: 0001-mm-kmemleak-skip-late_init-if-not-skip-disable.patch
Patch65: 0001-ARM-fix-__get_user_check-in-case-uaccess_-calls-are-.patch
Patch66: 0001-dt-bindings-panel-add-binding-for-Xingbangda-XBD599-.patch
Patch67: 0001-drm-panel-add-Xingbangda-XBD599-panel.patch
Patch68: 0001-drm-sun4i-sun6i_mipi_dsi-fix-horizontal-timing-calcu.patch
Patch70: 0001-e1000e-bump-up-timeout-to-wait-when-ME-un-configure-.patch
Patch72: 0001-Work-around-for-gcc-bug-https-gcc.gnu.org-bugzilla-s.patch

# https://patchwork.kernel.org/patch/11743769/
Patch100: mmc-sdhci-iproc-Enable-eMMC-DDR-3.3V-support-for-bcm2711.patch

# https://patchwork.kernel.org/patch/11745283/
Patch101: brcmfmac-BCM4329-Fixes-and-improvement.patch

# https://patchwork.kernel.org/patch/11796255/
Patch102: arm64-dts-rockchip-disable-USB-type-c-DisplayPort.patch

# Tegra fixes
Patch105: 0001-PCI-Add-MCFG-quirks-for-Tegra194-host-controllers.patch
Patch106: arm64-tegra-Use-valid-PWM-period-for-VDD_GPU-on-Tegra210.patch
# https://www.spinics.net/lists/linux-tegra/msg53605.html
Patch108: iommu-tegra-smmu-Fix-TLB-line-for-Tegra210.patch

# A patch to fix some undocumented things broke a bunch of Allwinner networks due to wrong assumptions
Patch124: 0001-update-phy-on-pine64-a64-devices.patch
# https://patchwork.kernel.org/project/linux-arm-kernel/patch/20201025140144.28693-1-ats@offog.org/
Patch126: ARM-dts-sun7i-pcduino3-nano-enable-RGMII-RX-TX-delay-on-PHY.patch

# rhbz 1897038
Patch132: bluetooth-fix-LL-privacy-BLE-device-fails-to-connect.patch

# END OF PATCH DEFINITIONS

%endif


%description
The kernel-libre package is the upstream kernel without the non-Free
blobs it includes by default.

#
# This macro does requires, provides, conflicts, obsoletes for a kernel package.
#	%%kernel_reqprovconf <subpackage>
# It uses any kernel_<subpackage>_conflicts and kernel_<subpackage>_obsoletes
# macros defined above.
#
%define kernel_reqprovconf \
Provides: kernel = %{rpmversion}-%{pkg_release}\
Provides: kernel-libre = %{rpmversion}-%{pkg_release}\
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{pkg_release}%{?1:+%{1}}\
Provides: kernel-libre-%{_target_cpu} = %{rpmversion}-%{pkg_release}%{?1:+%{1}}\
Provides: kernel-drm-nouveau = 16\
Provides: kernel-libre-drm-nouveau = 16\
Provides: kernel-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Provides: kernel-libre-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Requires(pre): %{kernel_prereq}\
Requires(pre): %{initrd_prereq}\
Requires(preun): systemd >= 200\
Conflicts: xfsprogs < 4.3.0-1\
Conflicts: xorg-x11-drv-vmmouse < 13.0.99\
%{expand:%%{?kernel%{?1:_%{1}}_conflicts:Conflicts: %%{kernel%{?1:_%{1}}_conflicts}}}\
%{expand:%%{?kernel%{?1:_%{1}}_obsoletes:Obsoletes: %%{kernel%{?1:_%{1}}_obsoletes}}}\
%{expand:%%{?kernel%{?1:_%{1}}_provides:Provides: %%{kernel%{?1:_%{1}}_provides}}}\
# We can't let RPM do the dependencies automatic because it'll then pick up\
# a correct but undesirable perl dependency from the module headers which\
# isn't required for the kernel proper to function\
AutoReq: no\
AutoProv: yes\
%{nil}


%package doc
Summary: Various documentation bits found in the kernel source
Group: Documentation
%description doc
This package contains documentation files from the kernel
source. Various bits of information about the Linux kernel and the
device drivers shipped with it are documented in these files.

You'll want to install this package if you need a reference to the
options that can be passed to Linux kernel modules at load time.


%package headers
Summary: Header files for the Linux kernel for use by glibc
Obsoletes: glibc-kernheaders < 3.0-46
Provides: glibc-kernheaders = 3.0-46
%if "0%{?variant}"
Obsoletes: kernel-libre-headers < %{rpmversion}-%{pkg_release}
Provides: kernel-headers = %{rpmversion}-%{pkg_release}
Provides: kernel-libre-headers = %{rpmversion}-%{pkg_release}
%endif
%description headers
Kernel-headers includes the C header files that specify the interface
between the Linux kernel and userspace libraries and programs.  The
header files define structures and constants that are needed for
building most standard programs and are also needed for rebuilding the
glibc package.

%package cross-headers
Provides: kernel-libre-cross-headers = %{rpmversion}-%{pkg_release}
Summary: Header files for the Linux kernel for use by cross-glibc
%description cross-headers
Kernel-cross-headers includes the C header files that specify the interface
between the Linux kernel and userspace libraries and programs.  The
header files define structures and constants that are needed for
building most standard programs and are also needed for rebuilding the
cross-glibc package.


%package debuginfo-common-%{_target_cpu}
Summary: Kernel source files used by %{name}-debuginfo packages
Provides: installonlypkg(kernel)
%description debuginfo-common-%{_target_cpu}
This package is required by %{name}-debuginfo subpackages.
It provides the kernel source files common to all builds.

%if %{with_perf}
%package -n perf
Summary: Performance monitoring for the Linux kernel
License: GPLv2
%description -n perf
This package contains the perf tool, which enables performance monitoring
of the Linux kernel.

%package -n perf-debuginfo
Summary: Debug information for package perf
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n perf-debuginfo
This package provides debug information for the perf package.

# Note that this pattern only works right to match the .build-id
# symlinks because of the trailing nonmatching alternation and
# the leading .*, because of find-debuginfo.sh's buggy handling
# of matching the pattern against the symlinks file.
%{expand:%%global _find_debuginfo_opts %{?_find_debuginfo_opts} -p '.*%%{_bindir}/perf(\.debug)?|.*%%{_libexecdir}/perf-core/.*|.*%%{_libdir}/traceevent/plugins/.*|.*%%{_libdir}/libperf-jvmti.so(\.debug)?|XXX' -o perf-debuginfo.list}

%package -n python3-perf
Summary: Python bindings for apps which will manipulate perf events
%description -n python3-perf
The python3-perf package contains a module that permits applications
written in the Python programming language to use the interface
to manipulate perf events.

%package -n python3-perf-debuginfo
Summary: Debug information for package perf python bindings
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n python3-perf-debuginfo
This package provides debug information for the perf python bindings.

# the python_sitearch macro should already be defined from above
%{expand:%%global _find_debuginfo_opts %{?_find_debuginfo_opts} -p '.*%%{python3_sitearch}/perf.*so(\.debug)?|XXX' -o python3-perf-debuginfo.list}

# with_perf
%endif

%if %{with_tools}
%package -n kernel-tools
Summary: Assortment of tools for the Linux kernel
License: GPLv2
%ifarch %{cpupowerarchs}
Provides:  cpupowerutils = 1:009-0.6.p1
Obsoletes: cpupowerutils < 1:009-0.6.p1
Provides:  cpufreq-utils = 1:009-0.6.p1
Provides:  cpufrequtils = 1:009-0.6.p1
Obsoletes: cpufreq-utils < 1:009-0.6.p1
Obsoletes: cpufrequtils < 1:009-0.6.p1
Obsoletes: cpuspeed < 1:1.5-16
Requires: kernel-tools-libs = %{version}-%{release}
%endif
%define __requires_exclude ^%{_bindir}/python
%description -n kernel-tools
This package contains the tools/ directory from the kernel source
and the supporting documentation.

%package -n kernel-tools-libs
Summary: Libraries for the kernels-tools
License: GPLv2
%description -n kernel-tools-libs
This package contains the libraries built from the tools/ directory
from the kernel source.

%package -n kernel-tools-libs-devel
Summary: Assortment of tools for the Linux kernel
License: GPLv2
Requires: kernel-tools = %{version}-%{release}
%ifarch %{cpupowerarchs}
Provides:  cpupowerutils-devel = 1:009-0.6.p1
Obsoletes: cpupowerutils-devel < 1:009-0.6.p1
%endif
Requires: kernel-tools-libs = %{version}-%{release}
Provides: kernel-tools-devel
%description -n kernel-tools-libs-devel
This package contains the development files for the tools/ directory from
the kernel source.

%package -n kernel-tools-debuginfo
Summary: Debug information for package kernel-tools
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n kernel-tools-debuginfo
This package provides debug information for package kernel-tools.

# Note that this pattern only works right to match the .build-id
# symlinks because of the trailing nonmatching alternation and
# the leading .*, because of find-debuginfo.sh's buggy handling
# of matching the pattern against the symlinks file.
%{expand:%%global _find_debuginfo_opts %{?_find_debuginfo_opts} -p '.*%%{_bindir}/centrino-decode(\.debug)?|.*%%{_bindir}/powernow-k8-decode(\.debug)?|.*%%{_bindir}/cpupower(\.debug)?|.*%%{_libdir}/libcpupower.*|.*%%{_bindir}/turbostat(\.debug)?|.*%%{_bindir}/x86_energy_perf_policy(\.debug)?|.*%%{_bindir}/tmon(\.debug)?|.*%%{_bindir}/lsgpio(\.debug)?|.*%%{_bindir}/gpio-hammer(\.debug)?|.*%%{_bindir}/gpio-event-mon(\.debug)?|.*%%{_bindir}/iio_event_monitor(\.debug)?|.*%%{_bindir}/iio_generic_buffer(\.debug)?|.*%%{_bindir}/lsiio(\.debug)?|XXX' -o kernel-tools-debuginfo.list}

# with_tools
%endif

%if %{with_bpftool}

%package -n bpftool
Summary: Inspection and simple manipulation of eBPF programs and maps
License: GPLv2
%description -n bpftool
This package contains the bpftool, which allows inspection and simple
manipulation of eBPF programs and maps.

%package -n bpftool-debuginfo
Summary: Debug information for package bpftool
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}
AutoReqProv: no
%description -n bpftool-debuginfo
This package provides debug information for the bpftool package.

%{expand:%%global _find_debuginfo_opts %{?_find_debuginfo_opts} -p '.*%%{_sbindir}/bpftool(\.debug)?|XXX' -o bpftool-debuginfo.list}

# with_bpftool
%endif

%if %{with_selftests}

%package selftests-internal
Summary: Kernel samples and selftests
License: GPLv2
Requires: binutils, bpftool, iproute-tc, nmap-ncat
Requires: kernel-modules-internal = %{version}-%{release}
%description selftests-internal
Kernel sample programs and selftests.

# Note that this pattern only works right to match the .build-id
# symlinks because of the trailing nonmatching alternation and
# the leading .*, because of find-debuginfo.sh's buggy handling
# of matching the pattern against the symlinks file.
%{expand:%%global _find_debuginfo_opts %{?_find_debuginfo_opts} -p '.*%%{_libexecdir}/(ksamples|kselftests)/.*|XXX' -o selftests-debuginfo.list}

# with_selftests
%endif

%if %{with_gcov}
%package gcov
Summary: gcov graph and source files for coverage data collection.
%description gcov
kernel-gcov includes the gcov graph and source files for gcov coverage collection.
%endif

%package -n kernel-abi-whitelists
Summary: The Red Hat Enterprise Linux kernel ABI symbol whitelists
AutoReqProv: no
%description -n kernel-abi-whitelists
The kABI package contains information pertaining to the Red Hat Enterprise
Linux kernel ABI, including lists of kernel symbols that are needed by
external Linux kernel modules, and a yum plugin to aid enforcement.

%if %{with_kabidw_base}
%package kabidw-base
Summary: The baseline dataset for kABI verification using DWARF data
Group: System Environment/Kernel
AutoReqProv: no
%description kabidw-base
The kabidw-base package contains data describing the current ABI of the Red Hat
Enterprise Linux kernel, suitable for the kabi-dw tool.
%endif

#
# This macro creates a kernel-<subpackage>-debuginfo package.
#	%%kernel_debuginfo_package <subpackage>
#
# Explanation of the find_debuginfo_opts: We build multiple kernels (debug
# pae etc.) so the regex filters those kernels appropriately. We also
# have to package several binaries as part of kernel-devel but getting
# unique build-ids is tricky for these userspace binaries. We don't really
# care about debugging those so we just filter those out and remove it.
%define kernel_debuginfo_package() \
%package %{?1:%{1}-}debuginfo\
Provides: kernel%{?1:-%{1}}-debuginfo = %{version}-%{release}\
Summary: Debug information for package %{name}%{?1:-%{1}}\
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{version}-%{release}\
Provides: %{name}%{?1:-%{1}}-debuginfo-%{_target_cpu} = %{version}-%{release}\
Provides: installonlypkg(kernel)\
AutoReqProv: no\
%description %{?1:%{1}-}debuginfo\
This package provides debug information for package %{name}%{?1:-%{1}}.\
This is required to use SystemTap with %{name}%{?1:-%{1}}-%{KVERREL}.\
%{expand:%%global _find_debuginfo_opts %{?_find_debuginfo_opts} -p '.*\/usr\/src\/kernels/.*|XXX' -o ignored-debuginfo.list -p '/.*/%%{KVERREL_RE}%{?1:[+]%{1}}/.*|/.*%%{KVERREL_RE}%{?1:\+%{1}}(\.debug)?' -o debuginfo%{?1}.list}\


%{nil}

#
# This macro creates a kernel-<subpackage>-devel package.
#	%%kernel_devel_package <subpackage> <pretty-name>
#
%define kernel_devel_package() \
%package %{?1:%{1}-}devel\
Provides: kernel%{?1:-%{1}}-devel = %{version}-%{release}\
Summary: Development package for building kernel modules to match the %{?2:%{2} }kernel\
Provides: kernel%{?1:-%{1}}-devel-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-libre%{?1:-%{1}}-devel-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-devel-%{_target_cpu} = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel-libre-devel-%{_target_cpu} = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel-devel-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Provides: kernel-libre-devel-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Provides: installonlypkg(kernel)\
Provides: installonlypkg(kernel-libre)\
AutoReqProv: no\
Requires(pre): findutils\
Requires: findutils\
Requires: perl-interpreter\
%description %{?1:%{1}-}devel\
This package provides kernel headers and makefiles sufficient to build modules\
against the %{?2:%{2} }kernel package.\
%{nil}

#
# kernel-<variant>-ipaclones-internal package
#
%define kernel_ipaclones_package() \
%package %{?1:%{1}-}ipaclones-internal\
Summary: *.ipa-clones files generated by -fdump-ipa-clones for kernel%{?1:-%{1}}\
Group: System Environment/Kernel\
AutoReqProv: no\
%description %{?1:%{1}-}ipaclones-internal\
This package provides *.ipa-clones files.\
%{nil}

#
# This macro creates a kernel-<subpackage>-modules-internal package.
#	%%kernel_modules_internal_package <subpackage> <pretty-name>
#
%define kernel_modules_internal_package() \
%package %{?1:%{1}-}modules-internal\
Summary: Extra kernel modules to match the %{?2:%{2} }kernel\
Group: System Environment/Kernel\
Provides: kernel%{?1:-%{1}}-modules-internal-%{_target_cpu} = %{version}-%{release}\
Provides: kernel%{?1:-%{1}}-modules-internal-%{_target_cpu} = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel%{?1:-%{1}}-modules-internal = %{version}-%{release}%{?1:+%{1}}\
Provides: installonlypkg(kernel-module)\
Provides: kernel%{?1:-%{1}}-modules-internal-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Requires: kernel-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Requires: kernel%{?1:-%{1}}-modules-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
AutoReq: no\
AutoProv: yes\
%description %{?1:%{1}-}modules-internal\
This package provides kernel modules for the %{?2:%{2} }kernel package for Red Hat internal usage.\
%{nil}

#
# This macro creates a kernel-<subpackage>-modules-extra package.
#	%%kernel_modules_extra_package <subpackage> <pretty-name>
#
%define kernel_modules_extra_package() \
%package %{?1:%{1}-}modules-extra\
Provides: kernel%{?1:-%{1}}-modules-extra = %{version}-%{release}\
Summary: Extra kernel modules to match the %{?2:%{2} }kernel\
Provides: kernel%{?1:-%{1}}-modules-extra-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-libre%{?1:-%{1}}-modules-extra-%{_target_cpu} = %{version}-%{release}\
Provides: kernel%{?1:-%{1}}-modules-extra-%{_target_cpu} = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel-libre%{?1:-%{1}}-modules-extra-%{_target_cpu} = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel%{?1:-%{1}}-modules-extra = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel-libre%{?1:-%{1}}-modules-extra = %{version}-%{release}%{?1:+%{1}}\
Provides: installonlypkg(kernel-module)\
Provides: installonlypkg(kernel-libre-module)\
Provides: kernel%{?1:-%{1}}-modules-extra-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Provides: kernel-libre%{?1:-%{1}}-modules-extra-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Requires: kernel-libre-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Requires: kernel-libre%{?1:-%{1}}-modules-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
AutoReq: no\
AutoProv: yes\
%description %{?1:%{1}-}modules-extra\
This package provides less commonly used kernel modules for the %{?2:%{2} }kernel package.\
%{nil}

#
# This macro creates a kernel-<subpackage>-modules package.
#	%%kernel_modules_package <subpackage> <pretty-name>
#
%define kernel_modules_package() \
%package %{?1:%{1}-}modules\
Provides: kernel%{?1:-%{1}}-modules = %{version}-%{release}\
Summary: kernel modules to match the %{?2:%{2}-}core kernel\
Provides: kernel%{?1:-%{1}}-modules-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-libre%{?1:-%{1}}-modules-%{_target_cpu} = %{version}-%{release}\
Provides: kernel-modules-%{_target_cpu} = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel-libre-modules-%{_target_cpu} = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel-modules = %{version}-%{release}%{?1:+%{1}}\
Provides: kernel-libre-modules = %{version}-%{release}%{?1:+%{1}}\
Provides: installonlypkg(kernel-module)\
Provides: installonlypkg(kernel-libre-module)\
Provides: kernel%{?1:-%{1}}-modules-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Provides: kernel-libre%{?1:-%{1}}-modules-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Requires: kernel-libre-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Recommends: alsa-sof-firmware\
AutoReq: no\
AutoProv: yes\
%description %{?1:%{1}-}modules\
This package provides commonly used kernel modules for the %{?2:%{2}-}core kernel package.\
%{nil}

#
# this macro creates a kernel-<subpackage> meta package.
#	%%kernel_meta_package <subpackage>
#
%define kernel_meta_package() \
%package %{1}\
Provides: kernel-%{1} = %{KVERREL}+%{1}\
summary: kernel meta-package for the %{1} kernel\
Requires: kernel-libre-%{1}-core-uname-r = %{KVERREL}%{?variant}+%{1}\
Requires: kernel-libre-%{1}-modules-uname-r = %{KVERREL}%{?variant}+%{1}\
Provides: installonlypkg(kernel-libre)\
%description %{1}\
The meta-package for the %{1} kernel\
%{nil}

#
# This macro creates a kernel-<subpackage> and its -devel and -debuginfo too.
#	%%define variant_summary The Linux kernel compiled for <configuration>
#	%%kernel_variant_package [-n <pretty-name>] <subpackage>
#
%define kernel_variant_package(n:) \
%package %{?1:%{1}-}core\
Provides: kernel-%{?1:%{1}-}core = %{KVERREL}%{?1:+%{1}}\
Summary: %{variant_summary}\
Provides: kernel-libre-%{?1:%{1}-}core-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Provides: kernel-%{?1:%{1}-}core-uname-r = %{KVERREL}%{?variant}%{?1:+%{1}}\
Provides: installonlypkg(kernel-libre)\
%ifarch ppc64le\
Obsoletes: kernel-libre-bootwrapper\
%endif\
%{expand:%%kernel_reqprovconf}\
%if %{?1:1} %{!?1:0} \
%{expand:%%kernel_meta_package %{?1:%{1}}}\
%endif\
%{expand:%%kernel_devel_package %{?1:%{1}} %{!?{-n}:%{1}}%{?{-n}:%{-n*}}}\
%{expand:%%kernel_modules_package %{?1:%{1}} %{!?{-n}:%{1}}%{?{-n}:%{-n*}}}\
%{expand:%%kernel_modules_extra_package %{?1:%{1}} %{!?{-n}:%{1}}%{?{-n}:%{-n*}}}\
%{expand:%%kernel_modules_internal_package %{?1:%{1}} %{!?{-n}:%{1}}%{?{-n}:%{-n*}}}\
%{expand:%%kernel_debuginfo_package %{?1:%{1}}}\
%{nil}

# Now, each variant package.

%if %{with_pae}
%define variant_summary The Linux kernel compiled for Cortex-A15
%kernel_variant_package lpae
%description lpae-core
This package includes a version of the Linux kernel with support for
Cortex-A15 devices with LPAE and HW virtualisation support
%endif

%if %{with_zfcpdump}
%define variant_summary The Linux kernel compiled for zfcpdump usage
%kernel_variant_package zfcpdump
%description zfcpdump-core
The kernel package contains the Linux kernel (vmlinuz) for use by the
zfcpdump infrastructure.
# with_zfcpdump
%endif

%define variant_summary The Linux kernel compiled with extra debugging enabled
%kernel_variant_package debug
%description debug-core
The kernel package contains the Linux kernel (vmlinuz), the core of any
GNU/Linux operating system.  The kernel handles the basic functions
of the operating system:  memory allocation, process allocation, device
input and output, etc.

This variant of the kernel has numerous debugging options enabled.
It should only be installed when trying to gather additional information
on kernel bugs, as some of these options impact performance noticably.

The kernel-libre-debug package is the upstream kernel without the
non-Free blobs it includes by default.

# And finally the main -core package

%define variant_summary The Linux kernel
%kernel_variant_package
%description core
The kernel package contains the Linux kernel (vmlinuz), the core of any
GNU/Linux operating system.  The kernel handles the basic functions
of the operating system: memory allocation, process allocation, device
input and output, etc.

The kernel-libre package is the upstream kernel without the non-Free
blobs it includes by default.

%if %{with_ipaclones}
%kernel_ipaclones_package
%endif

%prep
# do a few sanity-checks for --with *only builds
%if %{with_baseonly}
%if !%{with_up}%{with_pae}
echo "Cannot build --with baseonly, up build is disabled"
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
  if ! grep -E "^Patch[0-9]+: $patch\$" %{_specdir}/${RPM_PACKAGE_NAME%%%%%{?variant}}.spec ; then
    if [ "${patch:0:8}" != "patch-5." ] ; then
      echo "ERROR: Patch  $patch  not listed as a source patch in specfile"
      exit 1
    fi
  fi 2>/dev/null
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

# First we unpack the kernel tarball.
# If this isn't the first make prep, we use links to the existing clean tarball
# which speeds things up quite a bit.

# Update to latest upstream.
%if 0%{?released_kernel}
%define vanillaversion 5.%{base_sublevel}
# non-released_kernel case
%else
%if 0%{?rcrev}
%define vanillaversion 5.%{upstream_sublevel}-rc%{rcrev}
%if 0%{?gitrev}
%define vanillaversion 5.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
%define vanillaversion 5.%{base_sublevel}-git%{gitrev}
%else
%define vanillaversion 5.%{base_sublevel}
%endif
%endif
%endif

# %%{vanillaversion} : the full version name, e.g. 2.6.35-rc6-git3
# %%{kversion}       : the base version, e.g. 2.6.34

# Use kernel-%%{kversion}%%{?dist} as the top-level directory name
# so we can prep different trees within a single git directory.

# Build a list of the other top-level kernel tree directories.
# This will be used to hardlink identical vanilla subdirs.
sharedirs=$(find "$PWD" -maxdepth 1 -type d -name 'kernel-5.*' \
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
      cp -al $sharedir/vanilla-%{kversion} .
    else
%setup -q -n kernel-%{kversion}%{?dist} -c
      mv linux-%{kversion} vanilla-%{kversion}
    fi

  fi

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?stablelibre: }%{?stablegnux}/" vanilla-%{kversion}/Makefile

%if "%{kversion}" != "%{vanillaversion}"

  for sharedir in $sharedirs ; do
    if [[ ! -z $sharedir  &&  -d $sharedir/vanilla-%{vanillaversion} ]] ; then
      break
    fi
  done
  if [[ ! -z $sharedir  &&  -d $sharedir/vanilla-%{vanillaversion} ]] ; then

    cp -al $sharedir/vanilla-%{vanillaversion} .

  else

    # Need to apply patches to the base vanilla version.
    cp -al vanilla-%{kversion} vanilla-%{vanillaversion}
    cd vanilla-%{vanillaversion}

cp %{SOURCE12} .

# Update vanilla to the latest upstream.
# (non-released_kernel case only)
%if 0%{?rcrev}
%if "%{?stablelibre}" != "%{?rcrevlibre}"
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?rcrevlibre: }%{?rcrevgnux}/" Makefile
%endif
    xzcat %{SOURCE5000} | patch -p1 -F1 -s
%if 0%{?gitrev}
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = -rc%{rcrev}%{?gitrevgnux}/" Makefile
    xzcat %{SOURCE5001} | patch -p1 -F1 -s
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if "%{?stablelibre}" != "%{?gitrevlibre}"
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?gitrevlibre: }%{?gitrevgnux}/" Makefile
%endif
%if 0%{?gitrev}
    xzcat %{SOURCE5000} | patch -p1 -F1 -s
%endif
%endif
    git init
    git config user.email "kernel-team@fedoraproject.org"
    git config user.name "Fedora Kernel Team"
    git config gc.auto 0
    git add .
    git commit -a -q -m "baseline"

    cd ..

  fi

%endif

else

  # We already have all vanilla dirs, just change to the top-level directory.
  cd kernel-%{kversion}%{?dist}

fi

# Now build the fedora kernel tree.
cp -al vanilla-%{vanillaversion} linux-%{KVERREL}

cd linux-%{KVERREL}
if [ ! -d .git ]; then
    git init
    git config user.email "kernel-team@fedoraproject.org"
    git config user.name "Fedora Kernel Team"
    git config gc.auto 0
    git add .
    git commit -a -q -m "baseline"
fi


# released_kernel with possible stable updates
%if 0%{?stable_base}
# This is special because the kernel spec is hell and nothing is consistent
xzcat %{SOURCE5000} | patch -p1 -F1 -s
git commit -a -m "Stable update"
%endif

# Note: Even in the "nopatches" path some patches (build tweaks and compile
# fixes) will always get applied; see patch defition above for details

$RPM_SOURCE_DIR/deblob-check %{patches} || exit 1
git am %{patches}

# END OF PATCH APPLICATIONS

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl
mv COPYING COPYING-%{version}-%{release}

# This Prevents scripts/setlocalversion from mucking with our version numbers.
touch .scmversion

# Mangle /usr/bin/python shebangs to /usr/bin/python3
# Mangle all Python shebangs to be Python 3 explicitly
# -p preserves timestamps
# -n prevents creating ~backup files
# -i specifies the interpreter for the shebang
# This fixes errors such as
# *** ERROR: ambiguous python shebang in /usr/bin/kvm_stat: #!/usr/bin/python. Change it to python3 (or python2) explicitly.
# We patch all sources below for which we got a report/error.
pathfix.py -i "%{__python3} %{py3_shbang_opts}" -p -n \
	tools/kvm/kvm_stat/kvm_stat \
	scripts/show_delta \
	scripts/diffconfig \
	scripts/bloat-o-meter \
	scripts/jobserver-exec \
	tools/perf/tests/attr.py \
	tools/perf/scripts/python/stat-cpi.py \
	tools/perf/scripts/python/sched-migration.py \
	Documentation \
	scripts/gen_compile_commands.py

# only deal with configs if we are going to build for the arch
%ifnarch %nobuildarches

if [ -L configs ]; then
	rm -f configs
fi
# Deal with configs stuff
mkdir configs
cd configs

# Drop some necessary files from the source dir into the buildroot
cp $RPM_SOURCE_DIR/kernel-*.config .
cp %{SOURCE1000} .
cp %{SOURCE55} .
cp %{SOURCE51} .
VERSION=%{version} ./generate_all_configs.sh %{primary_target} %{debugbuildsenabled}


# Merge in any user-provided local config option changes
%ifnarch %nobuildarches
for i in %{all_arch_configs}
do
  mv $i $i.tmp
  ./merge.pl %{SOURCE1000} $i.tmp > $i
  rm $i.tmp
done
%endif

%if !%{debugbuildsenabled}
rm -f kernel-%{version}-*debug.config
%endif

# enable GCOV kernel config options if gcov is on
%if %{with_gcov}
for i in *.config
do
  sed -i 's/# CONFIG_GCOV_KERNEL is not set/CONFIG_GCOV_KERNEL=y\nCONFIG_GCOV_PROFILE_ALL=y\n/' $i
done
%endif

cp %{SOURCE52} .
OPTS=""
%if %{with_configchecks}
	OPTS="$OPTS -w -n -c"
%endif
./process_configs.sh $OPTS kernel %{rpmversion}

cp %{SOURCE56} .
RPM_SOURCE_DIR=$RPM_SOURCE_DIR ./update_scripts.sh %{primary_target}

# end of kernel config
%endif

cd ..
# # End of Configs stuff

# get rid of unwanted files resulting from patch fuzz
find . \( -name "*.orig" -o -name "*~" \) -delete >/dev/null

# remove unnecessary SCM files
find . -name .gitignore -delete >/dev/null

cd ..

###
### build
###
%build

%if %{with_sparse}
%define sparse_mflags	C=1
%endif

cp_vmlinux()
{
  eu-strip --remove-comment -o "$2" "$1"
}

# These are for host programs that get built as part of the kernel and
# are required to be packaged in kernel-devel for building external modules.
# Since they are userspace binaries, they are required to pickup the hardening
# flags defined in the macros. The --build-id=uuid is a trick to get around
# debuginfo limitations: Typically, find-debuginfo.sh will update the build
# id of all binaries to allow for parllel debuginfo installs. The kernel
# can't use this because it breaks debuginfo for the vDSO so we have to
# use a special mechanism for kernel and modules to be unique. Unfortunately,
# we still have userspace binaries which need unique debuginfo and because
# they come from the kernel package, we can't just use find-debuginfo.sh to
# rewrite only those binaries. The easiest option right now is just to have
# the build id be a uuid for the host programs.
#
# Note we need to disable these flags for cross builds because the flags
# from redhat-rpm-config assume that host == target so target arch
# flags cause issues with the host compiler.
%if !%{with_cross}
%define build_hostcflags  %{?build_cflags}
%define build_hostldflags %{?build_ldflags}
%endif

%define make make %{?cross_opts} %{?make_opts} HOSTCFLAGS="%{?build_hostcflags}" HOSTLDFLAGS="%{?build_hostldflags}"

BuildKernel() {
    MakeTarget=$1
    KernelImage=$2
    Flavour=$4
    DoVDSO=$3
    Flav=${Flavour:++${Flavour}}
    InstallName=${5:-vmlinuz}

    DoModules=1
    if [ "$Flavour" = "zfcpdump" ]; then
	    DoModules=0
    fi

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

    %{make} %{?_smp_mflags} mrproper
    cp configs/$Config .config

    %if %{signkernel}%{signmodules}
    cp $RPM_SOURCE_DIR/x509.genkey certs/.
    %endif

    Arch=`head -1 .config | cut -b 3-`
    echo USING ARCH=$Arch

    KCFLAGS="%{?kcflags}"

    # add kpatch flags for base kernel
    if [ "$Flavour" == "" ]; then
        KCFLAGS="$KCFLAGS %{?kpatch_kcflags}"
    fi

    %{make} ARCH=$Arch olddefconfig >/dev/null

    # This ensures build-ids are unique to allow parallel debuginfo
    perl -p -i -e "s/^CONFIG_BUILD_SALT.*/CONFIG_BUILD_SALT=\"%{KVERREL}\"/" .config
    %{make} ARCH=$Arch KCFLAGS="$KCFLAGS" WITH_GCOV="%{?with_gcov}" %{?_smp_mflags} $MakeTarget %{?sparse_mflags} %{?kernel_mflags}
    if [ $DoModules -eq 1 ]; then
	%{make} ARCH=$Arch KCFLAGS="$KCFLAGS" WITH_GCOV="%{?with_gcov}" %{?_smp_mflags} modules %{?sparse_mflags} || exit 1
    fi

    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer
%if %{with_debuginfo}
    mkdir -p $RPM_BUILD_ROOT%{debuginfodir}/%{image_install_path}
%endif

%ifarch %{arm} aarch64
    %{make} ARCH=$Arch dtbs INSTALL_DTBS_PATH=$RPM_BUILD_ROOT/%{image_install_path}/dtb-$KernelVer
    %{make} ARCH=$Arch dtbs_install INSTALL_DTBS_PATH=$RPM_BUILD_ROOT/%{image_install_path}/dtb-$KernelVer
    cp -r $RPM_BUILD_ROOT/%{image_install_path}/dtb-$KernelVer $RPM_BUILD_ROOT/lib/modules/$KernelVer/dtb
    find arch/$Arch/boot/dts -name '*.dtb' -type f -delete
%endif

    # Start installing the results
    install -m 644 .config $RPM_BUILD_ROOT/boot/config-$KernelVer
    install -m 644 .config $RPM_BUILD_ROOT/lib/modules/$KernelVer/config
    install -m 644 System.map $RPM_BUILD_ROOT/boot/System.map-$KernelVer
    install -m 644 System.map $RPM_BUILD_ROOT/lib/modules/$KernelVer/System.map

    # We estimate the size of the initramfs because rpm needs to take this size
    # into consideration when performing disk space calculations. (See bz #530778)
    dd if=/dev/zero of=$RPM_BUILD_ROOT/boot/initramfs-$KernelVer.img bs=1M count=20

    if [ -f arch/$Arch/boot/zImage.stub ]; then
      cp arch/$Arch/boot/zImage.stub $RPM_BUILD_ROOT/%{image_install_path}/zImage.stub-$KernelVer || :
      cp arch/$Arch/boot/zImage.stub $RPM_BUILD_ROOT/lib/modules/$KernelVer/zImage.stub-$KernelVer || :
    fi

    %if %{signkernel}
    if [ "$KernelImage" = vmlinux ]; then
        # We can't strip and sign $KernelImage in place, because
        # we need to preserve original vmlinux for debuginfo.
        # Use a copy for signing.
        $CopyKernel $KernelImage $KernelImage.tosign
        KernelImage=$KernelImage.tosign
        CopyKernel=cp
    fi

    # Sign the image if we're using EFI
    # aarch64 kernels are gziped EFI images
    KernelExtension=${KernelImage##*.}
    if [ "$KernelExtension" == "gz" ]; then
        SignImage=${KernelImage%.*}
    else
        SignImage=$KernelImage
    fi

    %ifarch x86_64 aarch64
    %pesign -s -i $SignImage -o vmlinuz.tmp -a %{secureboot_ca_0} -c %{secureboot_key_0} -n %{pesign_name_0}
    %pesign -s -i vmlinuz.tmp -o vmlinuz.signed -a %{secureboot_ca_1} -c %{secureboot_key_1} -n %{pesign_name_1}
    rm vmlinuz.tmp
    %endif
    %ifarch s390x ppc64le
    if [ -x /usr/bin/rpm-sign ]; then
	rpm-sign --key "%{pesign_name_0}" --lkmsign $SignImage --output vmlinuz.signed
    elif [ $DoModules -eq 1 ]; then
	chmod +x scripts/sign-file
	./scripts/sign-file -p sha256 certs/signing_key.pem certs/signing_key.x509 $SignImage vmlinuz.signed
    else
	mv $SignImage vmlinuz.signed
    fi
    %endif

    if [ ! -s vmlinuz.signed ]; then
        echo "pesigning failed"
        exit 1
    fi
    mv vmlinuz.signed $SignImage
    if [ "$KernelExtension" == "gz" ]; then
        gzip -f9 $SignImage
    fi
    # signkernel
    %endif

    $CopyKernel $KernelImage \
                $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer
    chmod 755 $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer
    cp $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer $RPM_BUILD_ROOT/lib/modules/$KernelVer/$InstallName

    # hmac sign the kernel for FIPS
    echo "Creating hmac file: $RPM_BUILD_ROOT/%{image_install_path}/.vmlinuz-$KernelVer.hmac"
    ls -l $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer
    sha512hmac $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer | sed -e "s,$RPM_BUILD_ROOT,," > $RPM_BUILD_ROOT/%{image_install_path}/.vmlinuz-$KernelVer.hmac;
    cp $RPM_BUILD_ROOT/%{image_install_path}/.vmlinuz-$KernelVer.hmac $RPM_BUILD_ROOT/lib/modules/$KernelVer/.vmlinuz.hmac

    if [ $DoModules -eq 1 ]; then
	# Override $(mod-fw) because we don't want it to install any firmware
	# we'll get it from the linux-firmware package and we don't want conflicts
	%{make} %{?_smp_mflags} ARCH=$Arch INSTALL_MOD_PATH=$RPM_BUILD_ROOT %{?_smp_mflags} modules_install KERNELRELEASE=$KernelVer mod-fw=
    fi

%if %{with_gcov}
    # install gcov-needed files to $BUILDROOT/$BUILD/...:
    #   gcov_info->filename is absolute path
    #   gcno references to sources can use absolute paths (e.g. in out-of-tree builds)
    #   sysfs symlink targets (set up at compile time) use absolute paths to BUILD dir
    find . \( -name '*.gcno' -o -name '*.[chS]' \) -exec install -D '{}' "$RPM_BUILD_ROOT/$(pwd)/{}" \;
%endif

    # add an a noop %%defattr statement 'cause rpm doesn't like empty file list files
    echo '%%defattr(-,-,-)' > ../kernel${Flavour:+-${Flavour}}-ldsoconf.list
    if [ $DoVDSO -ne 0 ]; then
        %{make} ARCH=$Arch INSTALL_MOD_PATH=$RPM_BUILD_ROOT vdso_install KERNELRELEASE=$KernelVer
        if [ -s ldconfig-kernel.conf ]; then
             install -D -m 444 ldconfig-kernel.conf \
                $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernel-$KernelVer.conf
	     echo /etc/ld.so.conf.d/kernel-$KernelVer.conf >> ../kernel${Flavour:+-${Flavour}}-ldsoconf.list
        fi

        rm -rf $RPM_BUILD_ROOT/lib/modules/$KernelVer/vdso/.build-id
    fi

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
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/internal
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/updates
%if 0%{!?fedora:1}
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/weak-updates
%endif
    # CONFIG_KERNEL_HEADER_TEST generates some extra files in the process of
    # testing so just delete
    find . -name *.h.s -delete
    # first copy everything
    cp --parents `find  -type f -name "Makefile*" -o -name "Kconfig*"` $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp Module.symvers $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp System.map $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    if [ -s Module.markers ]; then
      cp Module.markers $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    fi

    # create the kABI metadata for use in packaging
    # NOTENOTE: the name symvers is used by the rpm backend
    # NOTENOTE: to discover and run the /usr/lib/rpm/fileattrs/kabi.attr
    # NOTENOTE: script which dynamically adds exported kernel symbol
    # NOTENOTE: checksums to the rpm metadata provides list.
    # NOTENOTE: if you change the symvers name, update the backend too
    echo "**** GENERATING kernel ABI metadata ****"
    gzip -c9 < Module.symvers > $RPM_BUILD_ROOT/boot/symvers-$KernelVer.gz
    cp $RPM_BUILD_ROOT/boot/symvers-$KernelVer.gz $RPM_BUILD_ROOT/lib/modules/$KernelVer/symvers.gz

%if %{with_kabichk}
    echo "**** kABI checking is enabled in kernel SPEC file. ****"
    chmod 0755 $RPM_SOURCE_DIR/check-kabi
    if [ -e $RPM_SOURCE_DIR/Module.kabi_%{_target_cpu}$Flavour ]; then
        cp $RPM_SOURCE_DIR/Module.kabi_%{_target_cpu}$Flavour $RPM_BUILD_ROOT/Module.kabi
        $RPM_SOURCE_DIR/check-kabi -k $RPM_BUILD_ROOT/Module.kabi -s Module.symvers || exit 1
        # for now, don't keep it around.
        rm $RPM_BUILD_ROOT/Module.kabi
    else
        echo "**** NOTE: Cannot find reference Module.kabi file. ****"
    fi
%endif

%if %{with_kabidupchk}
    echo "**** kABI DUP checking is enabled in kernel SPEC file. ****"
    if [ -e $RPM_SOURCE_DIR/Module.kabi_dup_%{_target_cpu}$Flavour ]; then
        cp $RPM_SOURCE_DIR/Module.kabi_dup_%{_target_cpu}$Flavour $RPM_BUILD_ROOT/Module.kabi
        $RPM_SOURCE_DIR/check-kabi -k $RPM_BUILD_ROOT/Module.kabi -s Module.symvers || exit 1
        # for now, don't keep it around.
        rm $RPM_BUILD_ROOT/Module.kabi
    else
        echo "**** NOTE: Cannot find DUP reference Module.kabi file. ****"
    fi
%endif

%if %{with_kabidw_base}
    # Don't build kabi base for debug kernels
    if [ "$Flavour" != "kdump" -a "$Flavour" != "debug" ]; then
        mkdir -p $RPM_BUILD_ROOT/kabi-dwarf
        tar xjvf %{SOURCE301} -C $RPM_BUILD_ROOT/kabi-dwarf

        mkdir -p $RPM_BUILD_ROOT/kabi-dwarf/whitelists
        tar xjvf %{SOURCE300} -C $RPM_BUILD_ROOT/kabi-dwarf/whitelists

        echo "**** GENERATING DWARF-based kABI baseline dataset ****"
        chmod 0755 $RPM_BUILD_ROOT/kabi-dwarf/run_kabi-dw.sh
        $RPM_BUILD_ROOT/kabi-dwarf/run_kabi-dw.sh generate \
            "$RPM_BUILD_ROOT/kabi-dwarf/whitelists/kabi-current/kabi_whitelist_%{_target_cpu}" \
            "$(pwd)" \
            "$RPM_BUILD_ROOT/kabidw-base/%{_target_cpu}${Flavour:+.${Flavour}}" || :

        rm -rf $RPM_BUILD_ROOT/kabi-dwarf
    fi
%endif

%if %{with_kabidwchk}
    if [ "$Flavour" != "kdump" ]; then
        mkdir -p $RPM_BUILD_ROOT/kabi-dwarf
        tar xjvf %{SOURCE301} -C $RPM_BUILD_ROOT/kabi-dwarf
        if [ -d "$RPM_BUILD_ROOT/kabi-dwarf/base/%{_target_cpu}${Flavour:+.${Flavour}}" ]; then
            mkdir -p $RPM_BUILD_ROOT/kabi-dwarf/whitelists
            tar xjvf %{SOURCE300} -C $RPM_BUILD_ROOT/kabi-dwarf/whitelists

            echo "**** GENERATING DWARF-based kABI dataset ****"
            chmod 0755 $RPM_BUILD_ROOT/kabi-dwarf/run_kabi-dw.sh
            $RPM_BUILD_ROOT/kabi-dwarf/run_kabi-dw.sh generate \
                "$RPM_BUILD_ROOT/kabi-dwarf/whitelists/kabi-current/kabi_whitelist_%{_target_cpu}" \
                "$(pwd)" \
                "$RPM_BUILD_ROOT/kabi-dwarf/base/%{_target_cpu}${Flavour:+.${Flavour}}.tmp" || :

            echo "**** kABI DWARF-based comparison report ****"
            $RPM_BUILD_ROOT/kabi-dwarf/run_kabi-dw.sh compare \
                "$RPM_BUILD_ROOT/kabi-dwarf/base/%{_target_cpu}${Flavour:+.${Flavour}}" \
                "$RPM_BUILD_ROOT/kabi-dwarf/base/%{_target_cpu}${Flavour:+.${Flavour}}.tmp" || :
            echo "**** End of kABI DWARF-based comparison report ****"
        else
            echo "**** Baseline dataset for kABI DWARF-BASED comparison report not found ****"
        fi

        rm -rf $RPM_BUILD_ROOT/kabi-dwarf
    fi
%endif

    # then drop all but the needed Makefiles/Kconfig files
    rm -rf $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts
    rm -rf $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cp .config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp -a scripts $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    rm -rf $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/tracing
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/spdxcheck.py

    # Files for 'make scripts' to succeed with kernel-devel.
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/security/selinux/include
    cp -a --parents security/selinux/include/classmap.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp -a --parents security/selinux/include/initial_sid_to_string.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/tools/include/tools
    cp -a --parents tools/include/tools/be_byteshift.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    cp -a --parents tools/include/tools/le_byteshift.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build

    if [ -f tools/objtool/objtool ]; then
      cp -a tools/objtool/objtool $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/tools/objtool/ || :
    fi
    if [ -d arch/$Arch/scripts ]; then
      cp -a arch/$Arch/scripts $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/%{_arch} || :
    fi
    if [ -f arch/$Arch/*lds ]; then
      cp -a arch/$Arch/*lds $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/arch/%{_arch}/ || :
    fi
    if [ -f arch/%{asmarch}/kernel/module.lds ]; then
      cp -a --parents arch/%{asmarch}/kernel/module.lds $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/*.o
    rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/scripts/*/*.o
%ifarch ppc64le
    cp -a --parents arch/powerpc/lib/crtsavres.[So] $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
%endif
    if [ -d arch/%{asmarch}/include ]; then
      cp -a --parents arch/%{asmarch}/include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
%ifarch aarch64
    # arch/arm64/include/asm/xen references arch/arm
    cp -a --parents arch/arm/include/asm/xen $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    # arch/arm64/include/asm/opcodes.h references arch/arm
    cp -a --parents arch/arm/include/asm/opcodes.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
%endif
    # include the machine specific headers for ARM variants, if available.
%ifarch %{arm}
    if [ -d arch/%{asmarch}/mach-${Flavour}/include ]; then
      cp -a --parents arch/%{asmarch}/mach-${Flavour}/include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
    # include a few files for 'make prepare'
    cp -a --parents arch/arm/tools/gen-mach-types $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/arm/tools/mach-types $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/

%endif
    cp -a include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
%ifarch i686 x86_64
    # files for 'make prepare' to succeed with kernel-devel
    cp -a --parents arch/x86/entry/syscalls/syscall_32.tbl $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/entry/syscalls/syscalltbl.sh $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/entry/syscalls/syscallhdr.sh $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/entry/syscalls/syscall_64.tbl $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/tools/relocs_32.c $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/tools/relocs_64.c $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/tools/relocs.c $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/tools/relocs_common.c $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/tools/relocs.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents tools/include/tools/le_byteshift.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/purgatory/purgatory.c $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/purgatory/stack.S $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/purgatory/setup-x86_64.S $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/purgatory/entry64.S $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/boot/string.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/boot/string.c $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    cp -a --parents arch/x86/boot/ctype.h $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
%endif
    # Make sure the Makefile and version.h have a matching timestamp so that
    # external modules can be built
    touch -r $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/Makefile $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/generated/uapi/linux/version.h

    # Copy .config to include/config/auto.conf so "make prepare" is unnecessary.
    cp $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/.config $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include/config/auto.conf

%if %{with_debuginfo}
    eu-readelf -n vmlinux | grep "Build ID" | awk '{print $NF}' > vmlinux.id
    cp vmlinux.id $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/vmlinux.id

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
      if [ ! -z "$3" ]; then
        sed -r -e "/^($3)\$/d" -i $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.$1
      fi
    }

    collect_modules_list networking \
      'register_netdev|ieee80211_register_hw|usbnet_probe|phy_driver_register|rt(l_|2x00)(pci|usb)_probe|register_netdevice'
    collect_modules_list block \
      'ata_scsi_ioctl|scsi_add_host|scsi_add_host_with_dma|blk_alloc_queue|blk_init_queue|register_mtd_blktrans|scsi_esp_register|scsi_register_device_handler|blk_queue_physical_block_size' 'pktcdvd.ko|dm-mod.ko'
    collect_modules_list drm \
      'drm_open|drm_init'
    collect_modules_list modesetting \
      'drm_crtc_init'

    # detect missing or incorrect license tags
    ( find $RPM_BUILD_ROOT/lib/modules/$KernelVer -name '*.ko' | xargs /sbin/modinfo -l | \
        grep -E -v 'GPL( v2)?$|Dual BSD/GPL$|Dual MPL/GPL$|GPL and additional rights$' ) && exit 1

    # remove files that will be auto generated by depmod at rpm -i time
    pushd $RPM_BUILD_ROOT/lib/modules/$KernelVer/
        rm -f modules.{alias*,builtin.bin,dep*,*map,symbols*,devname,softdep}
    popd

    # Call the modules-extra script to move things around
    %{SOURCE24} $RPM_BUILD_ROOT/lib/modules/$KernelVer $RPM_SOURCE_DIR/mod-extra.list
    # Blacklist net autoloadable modules in modules-extra
    %{SOURCE19} $RPM_BUILD_ROOT lib/modules/$KernelVer
    # Call the modules-extra script for internal modules
    %{SOURCE24} $RPM_BUILD_ROOT/lib/modules/$KernelVer %{SOURCE54} internal

    #
    # Generate the kernel-core and kernel-modules files lists
    #

    # Copy the System.map file for depmod to use, and create a backup of the
    # full module tree so we can restore it after we're done filtering
    cp System.map $RPM_BUILD_ROOT/.
    pushd $RPM_BUILD_ROOT
    mkdir restore
    cp -r lib/modules/$KernelVer/* restore/.

    # don't include anything going into k-m-e and k-m-i in the file lists
    rm -rf lib/modules/$KernelVer/{extra,internal}

    if [ $DoModules -eq 1 ]; then
	# Find all the module files and filter them out into the core and
	# modules lists.  This actually removes anything going into -modules
	# from the dir.
	find lib/modules/$KernelVer/kernel -name *.ko | sort -n > modules.list
	cp $RPM_SOURCE_DIR/filter-*.sh .
	./filter-modules.sh modules.list %{_target_cpu}
	rm filter-*.sh

	# Run depmod on the resulting module tree and make sure it isn't broken
	depmod -b . -aeF ./System.map $KernelVer &> depmod.out
	if [ -s depmod.out ]; then
	    echo "Depmod failure"
	    cat depmod.out
	    exit 1
	else
	    rm depmod.out
	fi
    else
	# Ensure important files/directories exist to let the packaging succeed
	echo '%%defattr(-,-,-)' > modules.list
	echo '%%defattr(-,-,-)' > k-d.list
	mkdir -p lib/modules/$KernelVer/kernel
	# Add files usually created by make modules, needed to prevent errors
	# thrown by depmod during package installation
	touch lib/modules/$KernelVer/modules.order
	touch lib/modules/$KernelVer/modules.builtin
    fi

    # remove files that will be auto generated by depmod at rpm -i time
    pushd $RPM_BUILD_ROOT/lib/modules/$KernelVer/
        rm -f modules.{alias*,builtin.bin,dep*,*map,symbols*,devname,softdep}
    popd

    # Go back and find all of the various directories in the tree.  We use this
    # for the dir lists in kernel-core
    find lib/modules/$KernelVer/kernel -mindepth 1 -type d | sort -n > module-dirs.list

    # Cleanup
    rm System.map
    cp -r restore/* lib/modules/$KernelVer/.
    rm -rf restore
    popd

    # Make sure the files lists start with absolute paths or rpmbuild fails.
    # Also add in the dir entries
    sed -e 's/^lib*/\/lib/' %{?zipsed} $RPM_BUILD_ROOT/k-d.list > ../kernel${Flavour:+-${Flavour}}-modules.list
    sed -e 's/^lib*/%dir \/lib/' %{?zipsed} $RPM_BUILD_ROOT/module-dirs.list > ../kernel${Flavour:+-${Flavour}}-core.list
    sed -e 's/^lib*/\/lib/' %{?zipsed} $RPM_BUILD_ROOT/modules.list >> ../kernel${Flavour:+-${Flavour}}-core.list

    # Cleanup
    rm -f $RPM_BUILD_ROOT/k-d.list
    rm -f $RPM_BUILD_ROOT/modules.list
    rm -f $RPM_BUILD_ROOT/module-dirs.list

%if %{signmodules}
    if [ $DoModules -eq 1 ]; then
	# Save the signing keys so we can sign the modules in __modsign_install_post
	cp certs/signing_key.pem certs/signing_key.pem.sign${Flav}
	cp certs/signing_key.x509 certs/signing_key.x509.sign${Flav}
    fi
%endif

    # Move the devel headers out of the root file system
    mkdir -p $RPM_BUILD_ROOT/usr/src/kernels
    mv $RPM_BUILD_ROOT/lib/modules/$KernelVer/build $RPM_BUILD_ROOT/$DevelDir

    # This is going to create a broken link during the build, but we don't use
    # it after this point.  We need the link to actually point to something
    # when kernel-devel is installed, and a relative link doesn't work across
    # the F17 UsrMove feature.
    ln -sf $DevelDir $RPM_BUILD_ROOT/lib/modules/$KernelVer/build

    # prune junk from kernel-devel
    find $RPM_BUILD_ROOT/usr/src/kernels -name ".*.cmd" -delete

    # build a BLS config for this kernel
    %{SOURCE53} "$KernelVer" "$RPM_BUILD_ROOT" "%{?variant}"

    # Red Hat UEFI Secure Boot CA cert, which can be used to authenticate the kernel
    mkdir -p $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer
    %ifarch x86_64 aarch64
       install -m 0644 %{secureboot_ca_0} $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/kernel-signing-ca-20200609.cer
       install -m 0644 %{secureboot_ca_1} $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/kernel-signing-ca-20140212.cer
       ln -s kernel-signing-ca-20200609.cer $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/kernel-signing-ca.cer
    %else
       install -m 0644 %{secureboot_ca_0} $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/kernel-signing-ca.cer
    %endif
    %ifarch s390x ppc64le
    if [ $DoModules -eq 1 ]; then
	if [ -x /usr/bin/rpm-sign ]; then
	    install -m 0644 %{secureboot_key_0} $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/%{signing_key_filename}
	else
	    install -m 0644 certs/signing_key.x509.sign${Flav} $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/kernel-signing-ca.cer
	    openssl x509 -in certs/signing_key.pem.sign${Flav} -outform der -out $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/%{signing_key_filename}
	    chmod 0644 $RPM_BUILD_ROOT%{_datadir}/doc/kernel-keys/$KernelVer/%{signing_key_filename}
	fi
    fi
    %endif

%if %{with_ipaclones}
    MAXPROCS=$(echo %{?_smp_mflags} | sed -n 's/-j\s*\([0-9]\+\)/\1/p')
    if [ -z "$MAXPROCS" ]; then
        MAXPROCS=1
    fi
    if [ "$Flavour" == "" ]; then
        mkdir -p $RPM_BUILD_ROOT/$DevelDir-ipaclones
        find . -name '*.ipa-clones' | xargs -i{} -r -n 1 -P $MAXPROCS install -m 644 -D "{}" "$RPM_BUILD_ROOT/$DevelDir-ipaclones/{}"
    fi
%endif

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
BuildKernel %make_target %kernel_image %{_use_vdso} debug
%endif

%if %{with_zfcpdump}
BuildKernel %make_target %kernel_image %{_use_vdso} zfcpdump
%endif

%if %{with_pae}
BuildKernel %make_target %kernel_image %{use_vdso} lpae
%endif

%if %{with_up}
BuildKernel %make_target %kernel_image %{_use_vdso}
%endif

%global perf_make \
  make -s EXTRA_CFLAGS="${RPM_OPT_FLAGS}" LDFLAGS="%{__global_ldflags}" %{?cross_opts} -C tools/perf V=1 NO_PERF_READ_VDSO32=1 NO_PERF_READ_VDSOX32=1 WERROR=0 NO_LIBUNWIND=1 HAVE_CPLUS_DEMANGLE=1 NO_GTK2=1 NO_STRLCPY=1 NO_BIONIC=1 prefix=%{_prefix} PYTHON=%{__python3}
%if %{with_perf}
# perf
# make sure check-headers.sh is executable
chmod +x tools/perf/check-headers.sh
%{perf_make} DESTDIR=$RPM_BUILD_ROOT all
%endif

%global tools_make \
  %{make} CFLAGS="${RPM_OPT_FLAGS}" LDFLAGS="%{__global_ldflags}" V=1

%if %{with_tools}
%ifarch %{cpupowerarchs}
# cpupower
# make sure version-gen.sh is executable.
chmod +x tools/power/cpupower/utils/version-gen.sh
%{tools_make} %{?_smp_mflags} -C tools/power/cpupower CPUFREQ_BENCH=false
%ifarch x86_64
    pushd tools/power/cpupower/debug/x86_64
    %{tools_make} %{?_smp_mflags} centrino-decode powernow-k8-decode
    popd
%endif
%ifarch x86_64
   pushd tools/power/x86/x86_energy_perf_policy/
   %{tools_make}
   popd
   pushd tools/power/x86/turbostat
   %{tools_make}
   popd
   pushd tools/power/x86/intel-speed-select
   %{make}
   popd
%endif
%endif
pushd tools/thermal/tmon/
%{tools_make}
popd
pushd tools/iio/
%{make}
popd
pushd tools/gpio/
%{make}
popd
%endif

%global bpftool_make \
  make EXTRA_CFLAGS="${RPM_OPT_FLAGS}" EXTRA_LDFLAGS="%{__global_ldflags}" DESTDIR=$RPM_BUILD_ROOT V=1
%if %{with_bpftool}
pushd tools/bpf/bpftool
%{bpftool_make}
popd
%endif

%if %{with_selftests}
%{make} -s ARCH=$Arch V=1 samples/bpf/
pushd tools/testing/selftests
# We need to install here because we need to call make with ARCH set which
# doesn't seem possible to do in the install section.
%{make} -s ARCH=$Arch V=1 TARGETS="bpf livepatch net" INSTALL_PATH=%{buildroot}%{_libexecdir}/kselftests install
popd
%endif

%if %{with_doc}
# Make the HTML pages.
make PYTHON=/usr/bin/python3 htmldocs || %{doc_build_fail}

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
#
# Don't sign modules for the zfcpdump flavour as it is monolithic.

%define __modsign_install_post \
  if [ "%{signmodules}" -eq "1" ]; then \
    if [ "%{with_pae}" -ne "0" ]; then \
       %{modsign_cmd} certs/signing_key.pem.sign+lpae certs/signing_key.x509.sign+lpae $RPM_BUILD_ROOT/lib/modules/%{KVERREL}+lpae/ \
    fi \
    if [ "%{with_debug}" -ne "0" ]; then \
      %{modsign_cmd} certs/signing_key.pem.sign+debug certs/signing_key.x509.sign+debug $RPM_BUILD_ROOT/lib/modules/%{KVERREL}+debug/ \
    fi \
    if [ "%{with_up}" -ne "0" ]; then \
      %{modsign_cmd} certs/signing_key.pem.sign certs/signing_key.x509.sign $RPM_BUILD_ROOT/lib/modules/%{KVERREL}/ \
    fi \
  fi \
  if [ "%{zipmodules}" -eq "1" ]; then \
    find $RPM_BUILD_ROOT/lib/modules/ -type f -name '*.ko' | xargs -P%{zcpu} xz; \
  fi \
%{nil}

###
### Special hacks for debuginfo subpackages.
###

# This macro is used by %%install, so we must redefine it before that.
%define debug_package %{nil}

%if %{with_debuginfo}

%ifnarch noarch
%global __debug_package 1
%files -f debugfiles.list debuginfo-common-%{_target_cpu}
%endif

%endif

# We don't want to package debuginfo for self-tests and samples but
# we have to delete them to avoid an error messages about unpackaged
# files.
# Delete the debuginfo for for kernel-devel files
%define __remove_unwanted_dbginfo_install_post \
  if [ "%{with_selftests}" -ne "0" ]; then \
    rm -rf $RPM_BUILD_ROOT/usr/lib/debug/usr/libexec/ksamples; \
    rm -rf $RPM_BUILD_ROOT/usr/lib/debug/usr/libexec/kselftests; \
  fi \
  rm -rf $RPM_BUILD_ROOT/usr/lib/debug/usr/src; \
%{nil}

#
# Disgusting hack alert! We need to ensure we sign modules *after* all
# invocations of strip occur, which is in __debug_install_post if
# find-debuginfo.sh runs, and __os_install_post if not.
#
%define __spec_install_post \
  %{?__debug_package:%{__debug_install_post}}\
  %{__arch_install_post}\
  %{__os_install_post}\
  %{__remove_unwanted_dbginfo_install_post}\
  %{__modsign_install_post}

###
### install
###

%install

cd linux-%{KVERREL}

%if %{with_doc}
docdir=$RPM_BUILD_ROOT%{_datadir}/doc/kernel-doc-%{rpmversion}

# copy the source over
mkdir -p $docdir
tar -h -f - --exclude=man --exclude='.*' -c Documentation | tar xf - -C $docdir

# with_doc
%endif

# We have to do the headers install before the tools install because the
# kernel headers_install will remove any header files in /usr/include that
# it doesn't install itself.

%if %{with_headers}
# Install kernel headers
make ARCH=%{hdrarch} INSTALL_HDR_PATH=$RPM_BUILD_ROOT/usr headers_install

find $RPM_BUILD_ROOT/usr/include \
     \( -name .install -o -name .check -o \
        -name ..install.cmd -o -name ..check.cmd \) -delete

%endif

%if %{with_cross_headers}
%if 0%{?fedora}
HDR_ARCH_LIST='arm arm64 powerpc s390 x86'
%else
HDR_ARCH_LIST='arm64 powerpc s390 x86'
%endif
mkdir -p $RPM_BUILD_ROOT/usr/tmp-headers

for arch in $HDR_ARCH_LIST; do
	mkdir $RPM_BUILD_ROOT/usr/tmp-headers/arch-${arch}
	make ARCH=${arch} INSTALL_HDR_PATH=$RPM_BUILD_ROOT/usr/tmp-headers/arch-${arch} headers_install
done

find $RPM_BUILD_ROOT/usr/tmp-headers \
     \( -name .install -o -name .check -o \
        -name ..install.cmd -o -name ..check.cmd \) -delete

# Copy all the architectures we care about to their respective asm directories
for arch in $HDR_ARCH_LIST ; do
	mkdir -p $RPM_BUILD_ROOT/usr/${arch}-linux-gnu/include
	mv $RPM_BUILD_ROOT/usr/tmp-headers/arch-${arch}/include/* $RPM_BUILD_ROOT/usr/${arch}-linux-gnu/include/
done

rm -rf $RPM_BUILD_ROOT/usr/tmp-headers
%endif

%if %{with_kernel_abi_whitelists}
# kabi directory
INSTALL_KABI_PATH=$RPM_BUILD_ROOT/lib/modules/
mkdir -p $INSTALL_KABI_PATH

# install kabi releases directories
tar xjvf %{SOURCE300} -C $INSTALL_KABI_PATH
# with_kernel_abi_whitelists
%endif

%if %{with_perf}
# perf tool binary and supporting scripts/binaries
%{perf_make} DESTDIR=$RPM_BUILD_ROOT lib=%{_lib} install-bin install-traceevent-plugins
# remove the 'trace' symlink.
rm -f %{buildroot}%{_bindir}/trace

# For both of the below, yes, this should be using a macro but right now
# it's hard coded and we don't actually want it anyway right now.
# Whoever wants examples can fix it up!

# remove examples
rm -rf %{buildroot}/usr/lib/perf/examples
# remove the stray files that somehow got packaged
rm -rf %{buildroot}/usr/lib/perf/include/bpf/bpf.h
rm -rf %{buildroot}/usr/lib/perf/include/bpf/stdio.h
rm -rf %{buildroot}/usr/lib/perf/include/bpf/linux/socket.h
rm -rf %{buildroot}/usr/lib/perf/include/bpf/pid_filter.h
rm -rf %{buildroot}/usr/lib/perf/include/bpf/unistd.h

# python-perf extension
%{perf_make} DESTDIR=$RPM_BUILD_ROOT install-python_ext

# perf man pages (note: implicit rpm magic compresses them later)
mkdir -p %{buildroot}/%{_mandir}/man1
%{perf_make} DESTDIR=$RPM_BUILD_ROOT install-man
%endif

%if %{with_tools}
%ifarch %{cpupowerarchs}
%{make} -C tools/power/cpupower DESTDIR=$RPM_BUILD_ROOT libdir=%{_libdir} mandir=%{_mandir} CPUFREQ_BENCH=false install
rm -f %{buildroot}%{_libdir}/*.{a,la}
%find_lang cpupower
mv cpupower.lang ../
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
%ifarch x86_64
   mkdir -p %{buildroot}%{_mandir}/man8
   pushd tools/power/x86/x86_energy_perf_policy
   %{tools_make} DESTDIR=%{buildroot} install
   popd
   pushd tools/power/x86/turbostat
   %{tools_make} DESTDIR=%{buildroot} install
   popd
   pushd tools/power/x86/intel-speed-select
   %{tools_make} CFLAGS+="-D_GNU_SOURCE -Iinclude" DESTDIR=%{buildroot} install
   popd
%endif
pushd tools/thermal/tmon
%{tools_make} INSTALL_ROOT=%{buildroot} install
popd
pushd tools/iio
make DESTDIR=%{buildroot} install
popd
pushd tools/gpio
make DESTDIR=%{buildroot} install
popd
pushd tools/kvm/kvm_stat
make INSTALL_ROOT=%{buildroot} install-tools
make INSTALL_ROOT=%{buildroot} install-man
popd
%endif

%if %{with_bpftool}
pushd tools/bpf/bpftool
%{bpftool_make} prefix=%{_prefix} bash_compdir=%{_sysconfdir}/bash_completion.d/ mandir=%{_mandir} install doc-install
popd
%endif

%if %{with_selftests}
pushd samples
install -d %{buildroot}%{_libexecdir}/ksamples
# install bpf samples
pushd bpf
install -d %{buildroot}%{_libexecdir}/ksamples/bpf
find -type f -executable -exec install -m755 {} %{buildroot}%{_libexecdir}/ksamples/bpf \;
install -m755 *.sh %{buildroot}%{_libexecdir}/ksamples/bpf
# test_lwt_bpf.sh compiles test_lwt_bpf.c when run; this works only from the
# kernel tree. Just remove it.
rm %{buildroot}%{_libexecdir}/ksamples/bpf/test_lwt_bpf.sh
install -m644 tcp_bpf.readme %{buildroot}%{_libexecdir}/ksamples/bpf
popd
# install pktgen samples
pushd pktgen
install -d %{buildroot}%{_libexecdir}/ksamples/pktgen
find . -type f -executable -exec install -m755 {} %{buildroot}%{_libexecdir}/ksamples/pktgen/{} \;
find . -type f ! -executable -exec install -m644 {} %{buildroot}%{_libexecdir}/ksamples/pktgen/{} \;
popd
popd
# install drivers/net/mlxsw selftests
pushd tools/testing/selftests/drivers/net/mlxsw
find -type d -exec install -d %{buildroot}%{_libexecdir}/kselftests/drivers/net/mlxsw/{} \;
find -type f -executable -exec install -D -m755 {} %{buildroot}%{_libexecdir}/kselftests/drivers/net/mlxsw/{} \;
find -type f ! -executable -exec install -D -m644 {} %{buildroot}%{_libexecdir}/kselftests/drivers/net/mlxsw/{} \;
popd
# install net/forwarding selftests
pushd tools/testing/selftests/net/forwarding
find -type d -exec install -d %{buildroot}%{_libexecdir}/kselftests/net/forwarding/{} \;
find -type f -executable -exec install -D -m755 {} %{buildroot}%{_libexecdir}/kselftests/net/forwarding/{} \;
find -type f ! -executable -exec install -D -m644 {} %{buildroot}%{_libexecdir}/kselftests/net/forwarding/{} \;
popd
# install tc-testing selftests
pushd tools/testing/selftests/tc-testing
find -type d -exec install -d %{buildroot}%{_libexecdir}/kselftests/tc-testing/{} \;
find -type f -executable -exec install -D -m755 {} %{buildroot}%{_libexecdir}/kselftests/tc-testing/{} \;
find -type f ! -executable -exec install -D -m644 {} %{buildroot}%{_libexecdir}/kselftests/tc-testing/{} \;
popd
# install livepatch selftests
pushd tools/testing/selftests/livepatch
find -type d -exec install -d %{buildroot}%{_libexecdir}/kselftests/livepatch/{} \;
find -type f -executable -exec install -D -m755 {} %{buildroot}%{_libexecdir}/kselftests/livepatch/{} \;
find -type f ! -executable -exec install -D -m644 {} %{buildroot}%{_libexecdir}/kselftests/livepatch/{} \;
popd
%endif

# We have to do the headers checksum calculation after the tools install because
# these might end up installing their own set of headers on top of kernel's
%if %{with_headers}
# compute a content hash to export as Provides: kernel-headers-checksum
HEADERS_CHKSUM=$(export LC_ALL=C; find $RPM_BUILD_ROOT/usr/include -type f -name "*.h" \
			! -path $RPM_BUILD_ROOT/usr/include/linux/version.h | \
		 sort | xargs cat | sha1sum - | cut -f 1 -d ' ');
# export the checksum via usr/include/linux/version.h, so the dynamic
# find-provides can grab the hash to update it accordingly
echo "#define KERNEL_HEADERS_CHECKSUM \"$HEADERS_CHKSUM\"" >> $RPM_BUILD_ROOT/usr/include/linux/version.h
%endif

###
### clean
###

###
### scripts
###

%if %{with_tools}
%post -n kernel-tools-libs
/sbin/ldconfig

%postun -n kernel-tools-libs
/sbin/ldconfig
%endif

#
# This macro defines a %%post script for a kernel*-devel package.
#	%%kernel_devel_post [<subpackage>]
# Note we don't run hardlink if ostree is in use, as ostree is
# a far more sophisticated hardlink implementation.
# https://github.com/projectatomic/rpm-ostree/commit/58a79056a889be8814aa51f507b2c7a4dccee526
#
%define kernel_devel_post() \
%{expand:%%post %{?1:%{1}-}devel}\
if [ -f /etc/sysconfig/kernel ]\
then\
    . /etc/sysconfig/kernel || exit $?\
fi\
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink -a ! -e /run/ostree-booted ] \
then\
    (cd /usr/src/kernels/%{KVERREL}%{?1:+%{1}} &&\
     /usr/bin/find . -type f | while read f; do\
       hardlink -c /usr/src/kernels/*%{?dist}.*/$f $f\
     done)\
fi\
%{nil}

#
# This macro defines a %%post script for a kernel*-modules-extra package.
# It also defines a %%postun script that does the same thing.
#	%%kernel_modules_extra_post [<subpackage>]
#
%define kernel_modules_extra_post() \
%{expand:%%post %{?1:%{1}-}modules-extra}\
/sbin/depmod -a %{KVERREL}%{?1:+%{1}}\
%{nil}\
%{expand:%%postun %{?1:%{1}-}modules-extra}\
/sbin/depmod -a %{KVERREL}%{?1:+%{1}}\
%{nil}

#
# This macro defines a %%post script for a kernel*-modules-internal package.
# It also defines a %%postun script that does the same thing.
#	%%kernel_modules_internal_post [<subpackage>]
#
%define kernel_modules_internal_post() \
%{expand:%%post %{?1:%{1}-}modules-internal}\
/sbin/depmod -a %{KVERREL}%{?1:+%{1}}\
%{nil}\
%{expand:%%postun %{?1:%{1}-}modules-internal}\
/sbin/depmod -a %{KVERREL}%{?1:+%{1}}\
%{nil}

#
# This macro defines a %%post script for a kernel*-modules package.
# It also defines a %%postun script that does the same thing.
#	%%kernel_modules_post [<subpackage>]
#
%define kernel_modules_post() \
%{expand:%%post %{?1:%{1}-}modules}\
/sbin/depmod -a %{KVERREL}%{?1:+%{1}}\
%{nil}\
%{expand:%%postun %{?1:%{1}-}modules}\
/sbin/depmod -a %{KVERREL}%{?1:+%{1}}\
%{nil}

# This macro defines a %%posttrans script for a kernel package.
#	%%kernel_variant_posttrans [<subpackage>]
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_posttrans() \
%{expand:%%posttrans %{?1:%{1}-}core}\
%if 0%{!?fedora:1}\
if [ -x %{_sbindir}/weak-modules ]\
then\
    %{_sbindir}/weak-modules --add-kernel %{KVERREL}%{?1:+%{1}} || exit $?\
fi\
%endif\
/bin/kernel-install add %{KVERREL}%{?1:+%{1}} /lib/modules/%{KVERREL}%{?1:+%{1}}/vmlinuz || exit $?\
%{nil}

#
# This macro defines a %%post script for a kernel package and its devel package.
#	%%kernel_variant_post [-v <subpackage>] [-r <replace>]
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_post(v:r:) \
%{expand:%%kernel_devel_post %{?-v*}}\
%{expand:%%kernel_modules_post %{?-v*}}\
%{expand:%%kernel_modules_extra_post %{?-v*}}\
%{expand:%%kernel_modules_internal_post %{?-v*}}\
%{expand:%%kernel_variant_posttrans %{?-v*}}\
%{expand:%%post %{?-v*:%{-v*}-}core}\
%{-r:\
if [ `uname -i` == "x86_64" -o `uname -i` == "i386" ] &&\
   [ -f /etc/sysconfig/kernel ]; then\
  /bin/sed -r -i -e 's/^DEFAULTKERNEL=%{-r*}$/DEFAULTKERNEL=kernel-libre%{?-v:-%{-v*}}/' /etc/sysconfig/kernel || exit $?\
fi}\
%{nil}

#
# This macro defines a %%preun script for a kernel package.
#	%%kernel_variant_preun <subpackage>
#
%define kernel_variant_preun() \
%{expand:%%preun %{?1:%{1}-}core}\
/bin/kernel-install remove %{KVERREL}%{?1:+%{1}} /lib/modules/%{KVERREL}%{?1:+%{1}}/vmlinuz || exit $?\
%if 0%{!?fedora:1}\
if [ -x %{_sbindir}/weak-modules ]\
then\
    %{_sbindir}/weak-modules --remove-kernel %{KVERREL}%{?1:+%{1}} || exit $?\
fi\
%endif\
%{nil}

%kernel_variant_preun
%kernel_variant_post -r kernel-smp

%if %{with_pae}
%kernel_variant_preun lpae
%kernel_variant_post -v lpae -r (kernel|kernel-smp)
%endif

%kernel_variant_preun debug
%kernel_variant_post -v debug

%if %{with_zfcpdump}
%kernel_variant_preun zfcpdump
%kernel_variant_post -v zfcpdump
%endif

if [ -x /sbin/ldconfig ]
then
    /sbin/ldconfig -X || exit $?
fi

###
### file lists
###

%if %{with_headers}
%files headers
/usr/include/*
%endif

%if %{with_cross_headers}
%files cross-headers
/usr/*-linux-gnu/include/*
%endif

%if %{with_kernel_abi_whitelists}
%files -n kernel-abi-whitelists
/lib/modules/kabi-*
%endif

%if %{with_kabidw_base}
%ifarch x86_64 s390x ppc64 ppc64le aarch64
%files kabidw-base
%defattr(-,root,root)
/kabidw-base/%{_target_cpu}/*
%endif
%endif

# only some architecture builds need kernel-doc
%if %{with_doc}
%files doc
%defattr(-,root,root)
%{_datadir}/doc/kernel-doc-%{rpmversion}/Documentation/*
%dir %{_datadir}/doc/kernel-doc-%{rpmversion}/Documentation
%dir %{_datadir}/doc/kernel-doc-%{rpmversion}
%endif

%if %{with_perf}
%files -n perf
%{_bindir}/perf
%{_libdir}/libperf-jvmti.so
%dir %{_libdir}/traceevent/plugins
%{_libdir}/traceevent/plugins/*
%dir %{_libexecdir}/perf-core
%{_libexecdir}/perf-core/*
%{_datadir}/perf-core/*
%{_mandir}/man[1-8]/perf*
%{_sysconfdir}/bash_completion.d/perf
%doc linux-%{KVERREL}/tools/perf/Documentation/examples.txt
%{_docdir}/perf-tip/tips.txt

%files -n python3-perf
%{python3_sitearch}/*

%if %{with_debuginfo}
%files -f perf-debuginfo.list -n perf-debuginfo

%files -f python3-perf-debuginfo.list -n python3-perf-debuginfo
%endif
# with_perf
%endif

%if %{with_tools}
%ifnarch %{cpupowerarchs}
%files -n kernel-tools
%else
%files -n kernel-tools -f cpupower.lang
%{_bindir}/cpupower
%{_datadir}/bash-completion/completions/cpupower
%ifarch x86_64
%{_bindir}/centrino-decode
%{_bindir}/powernow-k8-decode
%endif
%{_unitdir}/cpupower.service
%{_mandir}/man[1-8]/cpupower*
%config(noreplace) %{_sysconfdir}/sysconfig/cpupower
%ifarch x86_64
%{_bindir}/x86_energy_perf_policy
%{_mandir}/man8/x86_energy_perf_policy*
%{_bindir}/turbostat
%{_mandir}/man8/turbostat*
%{_bindir}/intel-speed-select
%endif
# cpupowerarchs
%endif
%{_bindir}/tmon
%{_bindir}/iio_event_monitor
%{_bindir}/iio_generic_buffer
%{_bindir}/lsiio
%{_bindir}/lsgpio
%{_bindir}/gpio-hammer
%{_bindir}/gpio-event-mon
%{_mandir}/man1/kvm_stat*
%{_bindir}/kvm_stat

%if %{with_debuginfo}
%files -f kernel-tools-debuginfo.list -n kernel-tools-debuginfo
%endif

%ifarch %{cpupowerarchs}
%files -n kernel-tools-libs
%{_libdir}/libcpupower.so.0
%{_libdir}/libcpupower.so.0.0.1

%files -n kernel-tools-libs-devel
%{_libdir}/libcpupower.so
%{_includedir}/cpufreq.h
%endif
# with_tools
%endif

%if %{with_bpftool}
%files -n bpftool
%{_sbindir}/bpftool
%{_sysconfdir}/bash_completion.d/bpftool
%{_mandir}/man8/bpftool-cgroup.8.gz
%{_mandir}/man8/bpftool-gen.8.gz
%{_mandir}/man8/bpftool-map.8.gz
%{_mandir}/man8/bpftool-prog.8.gz
%{_mandir}/man8/bpftool-perf.8.gz
%{_mandir}/man8/bpftool.8.gz
%{_mandir}/man7/bpf-helpers.7.gz
%{_mandir}/man8/bpftool-net.8.gz
%{_mandir}/man8/bpftool-feature.8.gz
%{_mandir}/man8/bpftool-btf.8.gz

%if %{with_debuginfo}
%files -f bpftool-debuginfo.list -n bpftool-debuginfo
%defattr(-,root,root)
%endif
%endif

%if %{with_selftests}
%files selftests-internal
%{_libexecdir}/ksamples
%{_libexecdir}/kselftests
%endif

# empty meta-package
%ifnarch %nobuildarches noarch
%files
%endif

%if %{with_gcov}
%ifarch x86_64 s390x ppc64le aarch64
%files gcov
%{_builddir}
%endif
%endif

# This is %%{image_install_path} on an arch where that includes ELF files,
# or empty otherwise.
%define elf_image_install_path %{?kernel_image_elf:%{image_install_path}}

#
# This macro defines the %%files sections for a kernel package
# and its devel and debuginfo packages.
#	%%kernel_variant_files [-k vmlinux] <condition> <subpackage> <without_modules>
#
%define kernel_variant_files(k:) \
%if %{2}\
%{expand:%%files -f kernel-%{?3:%{3}-}core.list %{?1:-f kernel-%{?3:%{3}-}ldsoconf.list} %{?3:%{3}-}core}\
%{!?_licensedir:%global license %%doc}\
%license linux-%{KVERREL}/COPYING-%{version}-%{release}\
/lib/modules/%{KVERREL}%{?3:+%{3}}/%{?-k:%{-k*}}%{!?-k:vmlinuz}\
%ghost /%{image_install_path}/%{?-k:%{-k*}}%{!?-k:vmlinuz}-%{KVERREL}%{?3:+%{3}}\
/lib/modules/%{KVERREL}%{?3:+%{3}}/.vmlinuz.hmac \
%ghost /%{image_install_path}/.vmlinuz-%{KVERREL}%{?3:+%{3}}.hmac \
%ifarch %{arm} aarch64\
/lib/modules/%{KVERREL}%{?3:+%{3}}/dtb \
%ghost /%{image_install_path}/dtb-%{KVERREL}%{?3:+%{3}} \
%endif\
%attr(600,root,root) /lib/modules/%{KVERREL}%{?3:+%{3}}/System.map\
%ghost /boot/System.map-%{KVERREL}%{?3:+%{3}}\
/lib/modules/%{KVERREL}%{?3:+%{3}}/symvers.gz\
/lib/modules/%{KVERREL}%{?3:+%{3}}/config\
%ghost /boot/symvers-%{KVERREL}%{?3:+%{3}}.gz\
%ghost /boot/config-%{KVERREL}%{?3:+%{3}}\
%ghost /boot/initramfs-%{KVERREL}%{?3:+%{3}}.img\
%dir /lib/modules\
%dir /lib/modules/%{KVERREL}%{?3:+%{3}}\
%dir /lib/modules/%{KVERREL}%{?3:+%{3}}/kernel\
/lib/modules/%{KVERREL}%{?3:+%{3}}/build\
/lib/modules/%{KVERREL}%{?3:+%{3}}/source\
/lib/modules/%{KVERREL}%{?3:+%{3}}/updates\
/lib/modules/%{KVERREL}%{?3:+%{3}}/bls.conf\
%if 0%{!?fedora:1}\
/lib/modules/%{KVERREL}%{?3:+%{3}}/weak-updates\
%endif\
%{_datadir}/doc/kernel-keys/%{KVERREL}%{?3:+%{3}}/kernel-signing-ca*.cer\
%ifarch s390x ppc64le\
%if 0%{!?4:1}\
%{_datadir}/doc/kernel-keys/%{KVERREL}%{?3:+%{3}}/%{signing_key_filename} \
%endif\
%endif\
%if %{1}\
/lib/modules/%{KVERREL}%{?3:+%{3}}/vdso\
%endif\
/lib/modules/%{KVERREL}%{?3:+%{3}}/modules.*\
%{expand:%%files -f kernel-%{?3:%{3}-}modules.list %{?3:%{3}-}modules}\
%{expand:%%files %{?3:%{3}-}devel}\
%defverify(not mtime)\
/usr/src/kernels/%{KVERREL}%{?3:+%{3}}\
%{expand:%%files %{?3:%{3}-}modules-extra}\
%config(noreplace) /etc/modprobe.d/*-blacklist.conf\
/lib/modules/%{KVERREL}%{?3:+%{3}}/extra\
%{expand:%%files %{?3:%{3}-}modules-internal}\
/lib/modules/%{KVERREL}%{?3:+%{3}}/internal\
%if %{with_debuginfo}\
%ifnarch noarch\
%{expand:%%files -f debuginfo%{?3}.list %{?3:%{3}-}debuginfo}\
%endif\
%endif\
%if %{?3:1} %{!?3:0}\
%{expand:%%files %{3}}\
%endif\
%endif\
%{nil}

%kernel_variant_files %{_use_vdso} %{with_up}
%kernel_variant_files %{_use_vdso} %{with_debug} debug
%kernel_variant_files %{use_vdso} %{with_pae} lpae
%kernel_variant_files %{_use_vdso} %{with_zfcpdump} zfcpdump 1

%define kernel_variant_ipaclones(k:) \
%if %{1}\
%if %{with_ipaclones}\
%{expand:%%files %{?2:%{2}-}ipaclones-internal}\
%defattr(-,root,root)\
%defverify(not mtime)\
/usr/src/kernels/%{KVERREL}%{?2:+%{2}}-ipaclones\
%endif\
%endif\
%{nil}

%kernel_variant_ipaclones %{with_up}

# plz don't put in a version string unless you're going to tag
# and build.
#
#
%changelog
* Tue Dec 22 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.16-gnu.

* Mon Dec 21 07:41:26 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.16-100
- Linux v5.9.16

* Fri Dec 18 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.15-gnu.

* Wed Dec 16 08:06:25 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.15-100
- Linux v5.9.15

* Sun Dec 13 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.14-gnu.

* Fri Dec 11 07:15:03 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.14-100
- Linux v5.9.14
- Fixes CVE-2020-29660 (rhbz 1906522 1906523)
- Fixes CVE-2020-29661 (rhbz 1906525 1906526)

* Wed Dec  9 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.13-gnu.

* Tue Dec  8 08:04:36 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.13-100
- Linux v5.9.13

* Fri Dec  4 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.12-gnu.

* Wed Dec  2 07:55:45 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.12-100
- Linux v5.9.12

* Wed Nov 25 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.11-gnu.

* Tue Nov 24 11:22:42 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.11-100
- Linux v5.9.11

* Tue Nov 24 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.10-gnu.

* Mon Nov 23 09:59:05 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.10-100
- Linux v5.9.10
- Fix CVE-2020-28941 (rhbz 1899985 1899986)
- Fix CVE-2020-4788 (rhbz 1888433 1900437)

* Fri Nov 20 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.9-gnu.

* Thu Nov 19 07:12:19 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.9-100
- Linux v5.9.9

* Thu Nov 12 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Fix bluetooth device disconnect issues. (rhbz 1897038)

* Tue Nov 10 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.9.8-gnu.

* Tue Nov 10 15:34:54 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.9.8-100
- Linux v5.9.8
- Fixes CVE-2020-8694 (rhbz 1828580 1896525)

* Tue Nov 10 2020 <jforbes@fedoraproject.org> - 5.9.7-100
- Linux v5.9.7 rebase
- Fixes CVE-2020-25668 (rhbz 1893287 1893288)
- Fixes CVE-2020-27673 (rhbz 1891110 1891112)
- Fixes CVE-2020-25704 (rhbz 1895951 1895963)

* Mon Nov  2 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.18-gnu.

* Mon Nov  2 10:50:42 CST 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.18-200
- Linux v5.8.18

* Fri Oct 30 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.17-gnu.

* Thu Oct 29 07:55:22 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.17-200
- Linux v5.8.17
- Fix CVE-2020-27675 (rhbz 1891114 1891115)

* Wed Oct 28 2020 Peter Robinson <pbrobinson@fedoraproject.org>
- Fixes for AllWinner wired network issues due to Realtek PHY driver change (rhbz 1889090)

* Mon Oct 19 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.16-gnu.

* Mon Oct 19 07:15:06 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.16-200
- Linux v5.8.16

* Fri Oct 16 2020 Hans de Goede <hdegoede@redhat.com>
- Fix Micrsoft Surface Go series boot regression (rhbz 1886249)

* Thu Oct 15 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.15-gnu.

* Thu Oct 15 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.15-201
- Fix BleedingTooth CVE-2020-12351 CVE-2020-12352 (rhbz 1886521 1888439 1886529 1888440)

* Wed Oct 14 11:29:47 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.15-200
- Linux v5.8.15
- Fix CVE-2020-16119 (rhbz 1886374 1888083)

* Mon Oct 12 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.14-gnu.

* Wed Oct  7 07:21:23 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.14-200
- Linux v5.8.14

* Wed Oct  7 2020 Peter Robinson <pbrobinson@fedoraproject.org>
- Fix aarch64 boot crash on BTI capable systems
- Fix boot crash on aarch64 Ampere eMAG systems (rhbz #1874117)

* Thu Oct  1 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.13-gnu.

* Thu Oct  1 12:09:13 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.13-200
- Linux v5.8.13

* Mon Sep 28 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.12-gnu.

* Mon Sep 28 06:48:18 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.12-200
- Linux v5.8.12

* Wed Sep 23 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.11-gnu.

* Wed Sep 23 06:59:07 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.11-200
- Linux v5.8.11
- Fix (rhbz 1821946)

* Fri Sep 18 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.10-gnu.

* Thu Sep 17 08:48:27 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.10-200
- Linux v5.8.10
- Fix (rhbz 1873720 1876997)

* Mon Sep 14 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.9-gnu.

* Mon Sep 14 08:51:46 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.9-200
- Linux v5.8.9
- Fix error code in bdev_del_part (rhbz 1878858)

* Thu Sep 10 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Fix CVE-2020-25211 (rhbz 1877571 1877572)

* Wed Sep  9 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.8-gnu.

* Wed Sep  9 13:39:22 CDT 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.8-200
- Linux v5.8.8

* Tue Sep  8 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.7-gnu.

* Mon Sep 07 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.7-200
- Linux v5.8.7
- Fix CVE-2020-14386 (rhbz 1875699 1876349)

* Fri Sep  4 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.6-gnu.

* Thu Sep 03 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.6-201
- Linux v5.8.6
- Fix CVE-2020-14385 (rhbz 1874800 1874811)
- Move CONFIG_USB_XHCI_PCI_RENESAS to inline (rhbz 1874300)

* Thu Aug 27 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.5-gnu.

* Thu Aug 27 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.5-200
- Linux v5.8.5

* Thu Aug 27 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.8.4-gnu.

* Wed Aug 26 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.8.4-200
- Linux v5.8.4 rebase

* Sat Aug 22 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.17-gnu.

* Fri Aug 21 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.17-200
- Linux v5.7.17

* Wed Aug 19 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.16-gnu.

* Wed Aug 19 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.16-200
- Linux v5.7.16

* Wed Aug 12 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.15-gnu.

* Tue Aug 11 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.15-200
- Linux v5.7.15

* Mon Aug 10 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.14-gnu.

* Fri Aug 07 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.14-200
- Linux v5.7.14

* Wed Aug 05 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.13-200
- Linux v5.7.13
- Fix CVE-2020-16166 (rhbz 1865751 1865752)

* Sat Aug  1 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.12-gnu.

* Sat Aug 01 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.12-200
- Linux v5.7.12

* Fri Jul 31 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.11-gnu.

* Wed Jul 29 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.11-200
- Linux v5.7.11
- Fix rhbz 1857101

* Thu Jul 23 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.10-gnu.

* Wed Jul 22 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.10-201
- Linux v5.7.10

* Mon Jul 20 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Fix GDB regression (rhbz 1858645)

* Sun Jul 19 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.9-gnu.

* Fri Jul 17 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.9-100
- Linux v5.7.9

* Wed Jul 15 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Make some killer wireless ac 1550 cards work again

* Sun Jul 12 2020 Peter Robinson <pbrobinson@fedoraproject.org>
- selinux: allow reading labels before policy is loaded (rhbz 1845210)

* Sun Jul 12 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre Mon Jul 13
- GNU Linux-libre 5.7.8-gnu.

* Thu Jul 09 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.8-200
- Linux v5.7.8
- Fixes (rhbz 1852944 1852942 1852963 1852962)

* Fri Jul  3 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.7.7-gnu.

* Wed Jul 01 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.7-200
- Linux v5.7.7

* Wed Jul  1 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre Thu Jul  2
- GNU Linux-libre 5.7.6-gnu.

* Mon Jun 29 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.7.6-200
- Linux v5.7.6 rebase

* Thu Jun 18 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.19-gnu.

* Wed Jun 17 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.19-300
- Linux v5.6.19

* Mon Jun 15 2020 Stefan Assmann <sassmann@redhat.com>
- Add dual fan control for P50, P51, P52, P70, P71, P72, P1 gen1, P2 gen2,
  X1E gen1 and X1E gen2.

* Thu Jun 11 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.18-gnu.

* Wed Jun 10 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.18-300
- Linux v5.6.18

* Mon Jun  8 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.17-gnu.

* Mon Jun 08 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.17-300
- Linux v5.6.17

* Thu Jun 04 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.16-300
- Fix CVE-2020-10757 (rhbz 1842525 184388)

* Thu Jun  4 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.16-gnu.

* Wed Jun 03 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Linux v5.6.16

* Thu May 28 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.15-gnu.

* Thu May 28 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.15-300
- Linux v5.6.15

* Fri May 22 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.14-gnu.

* Wed May 20 2020 Hans de Goede <hdegoede@redhat.com> - 5.6.14-300
- Fix automatic guest resolution resizing of VirtualBox VMs (rhbz 1789545)
- Fix Sony laptop hang on resume from suspend (rhbz 1830150)

* Wed May 20 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Linux v5.6.14
- Fix CVE-2020-12888 (rhbz 1836245 1836244)

* Mon May 18 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Fix stability issue with the jetson-tk1 NIC

* Mon May 18 2020 Hans de Goede <hdegoede@redhat.com>
- Add patch fixing backlight control on Cherry Trail devices (rhbz 1828927)

* Thu May 14 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.13-gnu.

* Thu May 14 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.13-300
- Linux v5.6.13
- Fix boot hang caused by buggy TPM support (rhbz 1779611)
- Fix CVE-2020-12655 (rhbz 1832543 1832545)

* Thu May 14 2020 Peter Robinson <pbrobinson@fedoraproject.org>
- Fix for NIC issues on Jetson Xavier AGX

* Tue May 12 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Fix CVE-2020-10711 (rhbz 1825116 1834778)

* Mon May 11 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.12-gnu.

* Mon May 11 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.12-300
- Linux v5.6.12

* Thu May  7 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.11-gnu.

* Wed May 06 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.11-300
- Linux v5.6.11

* Tue May  5 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.10-gnu.

* Mon May 04 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.10-300
- Linux v5.6.10

* Wed Apr 29 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.8-gnu.

* Wed Apr 29 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.8-300
- Linux v5.6.8
- Fixes CVE-2020-11884 (rhbz 1828149 1829181)

* Tue Apr 28 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- MST Fix from Lyude Paul
- drm/i915/gem: Hold obj->vma.lock over for_each_ggtt_vma() (airlied request)

* Fri Apr 24 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.7-gnu.

* Thu Apr 23 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.7-300
- Linux v5.6.7

* Wed Apr 22 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.6-gnu.

* Tue Apr 21 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.6-300
- Linux v5.6.6

* Sat Apr 18 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.5-gnu.
- Drop nouveau-add-missing-MODULE_FIRMWARE.patch, nothing in it for us.

* Fri Apr 17 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.6.5-300
- Linux v5.6.5

* Thu Apr 16 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Nouveau Add missing MODULE_FIRMWARE() lines for initramfs generators (rhbz 1825046)

* Mon Apr 13 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.4-gnu.

* Mon Apr 13 2020 Jeremy Cline <jcline@redhat.com> - 5.6.4-300
- Linux v5.6.4

* Thu Apr  9 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.3-gnu.

* Wed Apr 08 2020 Jeremy Cline <jcline@redhat.com> - 5.6.3-300
- Linux v5.6.3

* Tue Apr 07 2020 Karol Herbst <kherbst@redhat.com> - 5.6.2-301
- Add patches to fix nouveau issues preventing booting the installer or system

* Fri Apr  3 2020 Peter Robinson <pbrobinson@fedoraproject.org>
- Raspberry Pi HDMI mode validation fix
- Raspberry Pi 4 rev 1.2 mmc fix

* Fri Apr  3 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.2-gnu.

* Thu Apr 02 2020 Jeremy Cline <jcline@redhat.com> - 5.6.2-300
- Linux v5.6.2

* Thu Apr 02 2020 Hans de Goede <hdegoede@redhat.com>
- Add patch fixing Lenovo X1 7th and 8th gen not suspending (rhbz 1816621)
- Add patch fixing Lenovo X1 8th gen speaker volume control (rhbz 1820196)

* Wed Apr  1 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6.1-gnu.

* Wed Apr 01 2020 Jeremy Cline <jcline@redhat.com> - 5.6.1-300
- Linux v5.6.1
- Fixes CVE-2020-8835 (rhbz 1818941 1817350)

* Wed Apr  1 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.6-gnu.
- Mark non-blobs in Add-support-for-PinePhone-LCD-panel.patch.

* Mon Mar 30 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-300
- Linux v5.6

* Fri Mar 27 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc7.git1.1
- Linux v5.6-rc7-227-gf3e69428b5e2

* Fri Mar 27 2020 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Mar 23 2020 Peter Robinson <pbrobinson@gmail.com> - 5.6.0-0.rc7.git0.1
- Linux v5.6-rc7

* Mon Mar 23 2020 Peter Robinson <pbrobinson@gmail.com>
- Disable debugging options.

* Fri Mar 20 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc6.git2.1
- Linux v5.6-rc6-115-g5ad0ec0b8652
- Switch Secure Boot to lock down to integrity mode (rhbz 1815571)

* Wed Mar 18 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc6.git1.1
- Linux v5.6-rc6-9-gac309e7744be

* Wed Mar 18 2020 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Tue Mar 17 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc6.git0.1
- Linux v5.6-rc6

* Tue Mar 10 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc5.git0.2
- A series of eDP backlight fixes for i915 (rhbz 1811850)

* Mon Mar 09 2020 Hans de Goede <hdegoede@redhat.com>
- Fix only 1 monitor working on DP-MST docking stations (rhbz 1809681)
- Fix backtraces on various buggy BIOS-es (rhbz 1564895, 1808874)
- Add /etc/modprobe.d/floppy-blacklist.conf to fix auto-loading of the
  legacy floppy driver (rhbz 1789155)

* Mon Mar 09 2020 Peter Robinson <pbrobinson@gmail.com> - 5.6.0-0.rc5.git0.1
- Linux v5.6-rc5

* Mon Mar 09 2020 Peter Robinson <pbrobinson@gmail.com>
- Disable debugging options.

* Fri Mar 06 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc4.git1.1
- Linux v5.6-rc4-135-gaeb542a1b5c5

* Fri Mar 06 2020 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Mar 02 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc4.git0.1
- Linux v5.6-rc4

* Mon Mar 02 2020 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Feb 28 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc3.git3.1
- Linux v5.6-rc3-195-gc60c04021353

* Thu Feb 27 2020 Peter Robinson <pbrobinson@fedoraproject.org>
- Fixes and enhancements to some AllWinner Pine64 devices
- Some fixes for Tegra devices
- Initial support for the Pinebook Pro

* Thu Feb 27 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc3.git2.1
- Linux v5.6-rc3-71-gbfdc6d91a25f

* Tue Feb 25 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc3.git1.1
- Linux v5.6-rc3-26-g63623fd44972

* Tue Feb 25 2020 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Feb 24 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc3.git0.1
- Linux v5.6-rc3

* Mon Feb 24 2020 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Feb 21 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc2.git3.1
- Linux v5.6-rc2-55-gca7e1fd1026c

* Wed Feb 19 2020 Jeremy Cline <jcline@redhat.com>
- Pick up a uapi fix for qemu (rhbz 1804330)

* Wed Feb 19 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc2.git2.1
- Linux v5.6-rc2-47-g4b205766d8fc

* Tue Feb 18 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc2.git1.1
- Linux v5.6-rc2-8-gb1da3acc781c
- Enable CONFIG_INET_ESPINTCP (rhbz 1804255)

* Tue Feb 18 2020 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Feb 17 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc2.git0.1
- Linux v5.6-rc2

* Mon Feb 17 2020 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Feb 14 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc1.git3.1
- Linux v5.6-rc1-44-gb19e8c684703

* Thu Feb 13 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc1.git2.1
- Linux v5.6-rc1-23-g0bf999f9c5e7

* Thu Feb 13 2020 Jeremy Cline <jcline@redhat.com>
- Pull in cdrom ioctl fix (rhbz 1801353)

* Tue Feb 11 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc1.git1.1
- Linux v5.6-rc1-5-g0a679e13ea30

* Tue Feb 11 2020 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Feb 10 2020 Jeremy Cline <jcline@redhat.com>
- Remove sysrq support to lift lockdown (rhbz 1800859)

* Mon Feb 10 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc1.git0.1
- Linux v5.6-rc1

* Mon Feb 10 2020 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Feb 07 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc0.git5.1
- Linux v5.5-9824-g90568ecf5615
- Enable DM_CLONE as a module (rhbz 1799060)
- Enable PCI Express devices on RockChip SoCs (rhbz 1792564)

* Thu Feb 06 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc0.git4.1
- Linux v5.5-9737-g4c46bef2e96a

* Wed Feb 05 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc0.git3.1
- Linux v5.5-9402-g6992ca0dd017

* Sat Feb 01 2020 Jeremy Cline <jcline@redhat.com> - 5.6.0-0.rc0.git2.1
- Linux v5.5-8686-g14cd0bd04907

* Wed Jan 29 2020 Jeremy Cline <jcline@redhat.com> - 5.5.0-1
- Linux v5.5-3996-gb3a608222336

* Wed Jan 29 2020 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Wed Jan 29 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre Sat Feb  1
- GNU Linux-libre 5.5-gnu.
- Deblobbed arm64-usb-host-xhci-tegra-set-MODULE_FIRMWARE-for-tegra186.patch.

* Mon Jan 27 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-1
- Linux v5.5

* Fri Jan 24 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc7.git2.1
- Linux v5.5-rc7-62-g6381b442836e

* Thu Jan 23 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc7.git1.1
- Linux v5.5-rc7-16-g131701c697e8

* Mon Jan 20 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc7.git0.1
- Linux v5.5-rc7

* Mon Jan 20 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Disable debugging options.

* Fri Jan 17 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc6.git3.1
- Linux v5.5-rc6-143-gab7541c3addd

* Wed Jan 15 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc6.git2.1
- Linux v5.5-rc6-45-g51d69817519f

* Tue Jan 14 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc6.git1.1
- Linux v5.5-rc6-27-g452424cdcbca
- Reenable debugging options.

* Mon Jan 13 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Add Documentation back to kernel-devel as it has Kconfig now (rhbz 1789641)

* Mon Jan 13 2020 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.5-rc6-gnu.

* Mon Jan 13 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc6.git0.1
- Linux v5.5-rc6

* Mon Jan 13 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Disable debugging options.

* Fri Jan 10 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc5.git3.1
- Linux v5.5-rc5-215-g4e4cd21c64da

* Thu Jan 09 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc5.git2.1
- Linux v5.5-rc5-134-ge69ec487b2c7

* Wed Jan 08 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc5.git1.1
- Linux v5.5-rc5-41-gb07f636fca1c
- Reenable debugging options.

* Mon Jan 06 2020 Hans de Goede <hdegoede@redhat.com>
- Make the MFD Intel LPSS driver builtin, some devices require this to be
  available early during boot (rhbz#1787997)

* Mon Jan 06 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc5.git0.1
- Linux v5.5-rc5

* Mon Jan 06 2020 Justin M. Forbes <jforbes@fedoraproject.org>
- Disable debugging options.

* Fri Jan 03 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc4.git2.1
- Linux v5.5-rc4-116-gbed723519a72

* Thu Jan 02 2020 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc4.git1.1
- Linux v5.5-rc4-66-g738d2902773e

* Mon Dec 30 2019 Peter Robinson <pbrobinson@gmail.com> - 5.5.0-0.rc4.git0.1
- Linux v5.5-rc4

* Mon Dec 30 2019 Peter Robinson <pbrobinson@gmail.com>
- Disable debugging options.

* Mon Dec 23 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc3.git0.1
- Linux v5.5-rc3

* Mon Dec 23 2019 Justin M. Forbes <jforbes@fedoraproject.org>
- Disable debugging options.

* Thu Dec 19 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc2.git3.1
- Linux v5.5-rc2-195-g4a94c4332334

* Wed Dec 18 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc2.git2.1
- Linux v5.5-rc2-157-g2187f215ebaa

* Tue Dec 17 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc2.git1.1
- Linux v5.5-rc2-56-gea200dec5128
- Enable NO_HZ_FULL for other arches too.
- Reenable debugging options.

* Mon Dec 16 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc2.git0.1
- Linux v5.5-rc2

* Mon Dec 16 2019 Justin M. Forbes <jforbes@fedoraproject.org>
- Disable debugging options.

* Thu Dec 12 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc1.git2.1
- Linux v5.5-rc1-27-gae4b064e2a61

* Tue Dec 10 2019 Peter Robinson <pbrobinson@fedoraproject.org>
- Updates for ARMv7/aarch64
- Enable newer TI ARMv7 platforms

* Tue Dec 10 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc1.git1.1
- Linux v5.5-rc1-12-g6794862a16ef
- Reenable debugging options.

* Mon Dec 09 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc1.git0.1
- Linux v5.5-rc1

* Mon Dec 09 2019 Justin M. Forbes <jforbes@fedoraproject.org>
- Disable debugging options.

* Fri Dec 06 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc0.git7.1
- Linux v5.4-12941-gb0d4beaa5a4b

* Thu Dec 05 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc0.git6.1
- Linux v5.4-11747-g2f13437b8917

* Wed Dec 04 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc0.git5.1
- Linux v5.4-11681-g63de37476ebd

* Tue Dec 03 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc0.git4.1
- Linux v5.4-11180-g76bb8b05960c

* Mon Dec 02 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc0.git3.1
- Linux v5.4-10271-g596cf45cbf6e

* Wed Nov 27 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc0.git2.1
- Linux v5.4-5280-g89d57dddd7d3

* Tue Nov 26 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.5.0-0.rc0.git1.1
- Linux v5.4-3619-gbe2eca94d144
- Reenable debugging options.

* Mon Nov 25 2019 Laura Abbott <labbott@redhat.com> - 5.4.0-2
- bump and build to pick up fixes

* Mon Nov 25 2019 Justin M. Forbes <jforbes@fedoraproject.org>
- Fix CVE-2019-14895 (rhbz 1774870 1776139)
- Fix CVE-2019-14896 (rhbz 1774875 1776143)
- Fix CVE-2019-14897 (rhbz 1774879 1776146)
- Fix CVE-2019-14901 (rhbz 1773519 1776184)
- Fix CVE-2019-19078 (rhbz 1776354 1776353)

* Mon Nov 25 2019 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.4-gnu.

* Mon Nov 25 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-1
- Linux v5.4.0

* Fri Nov 22 2019 Laura Abbott <labbott@redhat.com> - 5.4.0-0.rc8.git1.2
- bump and build to test new configs

* Fri Nov 22 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc8.git1.1
- Linux v5.4-rc8-15-g81429eb8d9ca

* Fri Nov 22 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Thu Nov 21 2019 Justin M. Forbes <jforbes@fedoraproject.org> - 5.3.12-300
- Fix CVE-2019-19071 (rhbz 1774949 1774950)
- Fix CVE-2019-19070 (rhbz 1774957 1774958)
- Fix CVE-2019-19068 (rhbz 1774963 1774965)
- Fix CVE-2019-19043 (rhbz 1774972 1774973)
- Fix CVE-2019-19066 (rhbz 1774976 1774978)
- Fix CVE-2019-19046 (rhbz 1774988 1774989)
- Fix CVE-2019-19050 (rhbz 1774998 1775002)
- Fix CVE-2019-19062 (rhbz 1775021 1775023)
- Fix CVE-2019-19064 (rhbz 1775010 1775011)
- Fix CVE-2019-19063 (rhbz 1775015 1775016)
- Fix CVE-2019-19057 (rhbz 1775050 1775051)
- Fix CVE-2019-19053 (rhbz 1775956 1775110)
- Fix CVE-2019-19056 (rhbz 1775097 1775115)
- Fix CVE-2019-19054 (rhbz 1775063 1775117)

* Wed Nov 20 2019 Laura Abbott <labbott@redhat.com> - 5.4.0-0.rc8.git0.2
- bump and build to check the pesign

* Mon Nov 18 2019 Alexandre Oliva <lxoliva@fsfla.org> -libre
- GNU Linux-libre 5.4-rc8-gnu.

* Mon Nov 18 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc8.git0.1
- Linux v5.4-rc8

* Mon Nov 18 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Nov 15 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc7.git2.1
- Linux v5.4-rc7-68-g96b95eff4a59

* Thu Nov 14 2019 Laura Abbott <labbott@redhat.com> - 5.4.0-0.rc7.git1.2
- bump and build

* Wed Nov 13 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc7.git1.1
- Linux v5.4-rc7-49-g0e3f1ad80fc8

* Wed Nov 13 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Tue Nov 12 2019 Alexandre Oliva <lxoliva@fsfla.org> -libre Sun Nov 17
- GNU Linux-libre 5.4-rc7-gnu.
- Deblobbed arm64-usb-host-xhci-tegra-set-MODULE_FIRMWARE-for-tegra186.patch.

* Mon Nov 11 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc7.git0.1
- Linux v5.4-rc7

* Mon Nov 11 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Nov 08 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc6.git3.1
- Linux v5.4-rc6-29-g847120f859cc

* Thu Nov 07 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc6.git2.1
- Linux v5.4-rc6-26-g4dd58158254c

* Tue Nov 05 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc6.git1.1
- Linux v5.4-rc6-8-g26bc67213424

* Tue Nov 05 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Nov 04 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc6.git0.1
- Linux v5.4-rc6

* Mon Nov 04 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Nov 01 2019 Laura Abbott <labbott@redhat.com> - 5.4.0-0.rc5.git1.3
- bump and build again

* Thu Oct 31 2019 Laura Abbott <labbott@redhat.com> - 5.4.0-0.rc5.git1.2
- bump and build to fix broken weak-updates

* Thu Oct 31 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc5.git1.1
- Linux v5.4-rc5-49-ge472c64aa4fa

* Thu Oct 31 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Wed Oct 30 2019 Laura Abbott <labbott@redhat.com> - 5.4.0-0.rc5.git0.2
- bump and build to make sure I haven't broken anything

* Mon Oct 28 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc5.git0.1
- Linux v5.4-rc5

* Mon Oct 28 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Thu Oct 24 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc4.git3.1
- Linux v5.4-rc4-85-gf116b96685a0

* Wed Oct 23 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc4.git2.1
- Linux v5.4-rc4-37-g13b86bc4cd64

* Tue Oct 22 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc4.git1.1
- Linux v5.4-rc4-18-g3b7c59a1950c

* Tue Oct 22 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Oct 21 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc4.git0.1
- Linux v5.4-rc4

* Mon Oct 21 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Fri Oct 18 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc3.git2.1
- Linux v5.4-rc3-99-g0e2adab6cf28

* Tue Oct 15 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc3.git1.1
- Linux v5.4-rc3-18-g5bc52f64e884

* Tue Oct 15 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Oct 14 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc3.git0.1
- Linux v5.4-rc3

* Mon Oct 14 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Thu Oct 10 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc2.git2.1
- Linux v5.4-rc2-96-gfb20da6af705

* Tue Oct 08 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc2.git1.1
- Linux v5.4-rc2-20-geda57a0e4299

* Tue Oct 08 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Mon Oct 07 2019 Laura Abbott <labbott@redhat.com>
- Enable a few NFT options (rhbz 1651813)

* Mon Oct 07 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc2.git0.1
- Linux v5.4-rc2

* Mon Oct 07 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Sun Oct  6 2019 Peter Robinson <pbrobinson@fedoraproject.org>
- Fixes for Jetson TX1/TX2 series of devices

* Fri Oct 04 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc1.git1.1
- Linux v5.4-rc1-14-gcc3a7bfe62b9

* Fri Oct 04 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.

* Wed Oct 02 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc1.git0.1
- Linux v5.4-rc1

* Wed Oct 02 2019 Jeremy Cline <jcline@redhat.com>
- Disable debugging options.

* Mon Sep 30 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git9.1
- Linux v5.3-13236-g97f9a3c4eee5

* Thu Sep 26 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git8.1
- Linux v5.3-12397-gf41def397161

* Wed Sep 25 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git7.1
- Linux v5.3-12289-g351c8a09b00b

* Tue Sep 24 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git6.1
- Linux v5.3-12025-g4c07e2ddab5b

* Mon Sep 23 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git5.1
- Linux v5.3-11768-g619e17cf75dd

* Fri Sep 20 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git4.1
- Linux v5.3-10169-g574cc4539762

* Thu Sep 19 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git3.1
- Linux v5.3-7639-gb41dae061bbd

* Wed Sep 18 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git2.1
- Linux v5.3-3839-g35f7a9526615

* Tue Sep 17 2019 Jeremy Cline <jcline@redhat.com> - 5.4.0-0.rc0.git1.1
- Linux v5.3-2061-gad062195731b

* Tue Sep 17 2019 Jeremy Cline <jcline@redhat.com>
- Reenable debugging options.
