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
%global baserelease 3
%global fedora_build %{baserelease}

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 0

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
%define rpmversion 3.%{base_sublevel}.%{stable_update}

## The not-released-kernel case ##
%else
# The next upstream release sublevel (base_sublevel+1)
# % define upstream_sublevel %(echo $((%{base_sublevel} + 1)))
%define upstream_sublevel 0
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

%define rawhide_skip_docs 1
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
%define debugbuildsenabled 0

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
%define kernel_image arch/sparc/boot/image
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
%define kernel_dot_org_conflicts  ppp < 2.4.3-3, isdn4k-utils < 3.2-32, nfs-utils < 1.0.7-12, e2fsprogs < 1.37-4, util-linux < 2.12, jfsutils < 1.1.7-2, reiserfs-utils < 3.6.19-2, xfsprogs < 2.6.13-4, procps < 3.2.5-6.3, oprofile < 0.9.1-2, device-mapper-libs < 1.02.63-2, mdadm < 3.2.1-5

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
%define kernel_prereq  fileutils, module-init-tools >= 3.16-2, initscripts >= 8.11.1-1, grubby >= 7.0.10-1
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
Source4: deblob-check
Source5: deblob-%{kversion}
#Source6: deblob-%{rpmversion}

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
%define    stable_patch_00  patch%{?stablelibre}-3.%{base_sublevel}.%{stable_base}.bz2
Patch00: %{stable_patch_00}
%endif
%if 0%{?stable_rc}
%define    stable_patch_01  patch%{?rcrevlibre}-3.%{base_sublevel}.%{stable_update}-rc%{stable_rc}.bz2
Patch01: %{stable_patch_01}
%endif

# non-released_kernel case
# These are automagically defined by the rcrev and gitrev values set up
# near the top of this spec file.
%else
%if 0%{?rcrev}
Patch00: patch%{?rcrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}.bz2
%if 0%{?gitrev}
Patch01: patch%{?gitrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}.bz2
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if 0%{?gitrev}
Patch00: patch%{?gitrevlibre}-3.%{base_sublevel}-git%{gitrev}.bz2
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

Patch150: linux-2.6.29-sparc-IOC_TYPECHECK.patch

Patch160: linux-2.6-32bit-mmap-exec-randomization.patch
Patch161: linux-2.6-i386-nx-emulation.patch

Patch202: linux-2.6-debug-taint-vm.patch
Patch203: linux-2.6-debug-vm-would-have-oomkilled.patch

Patch383: linux-2.6-defaults-aspm.patch

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

Patch610: hda_intel-prealloc-4mb-dmabuffer.patch

Patch700: linux-2.6-e1000-ich9-montevina.patch

Patch800: linux-2.6-crash-driver.patch

# crypto/

# virt + ksm patches
Patch1555: fix_xen_guest_on_old_EC2.patch

# DRM

# nouveau + drm fixes
Patch1810: drm-nouveau-updates.patch
# intel drm is all merged upstream
Patch1824: drm-intel-next.patch
# make sure the lvds comes back on lid open
Patch1825: drm-intel-make-lvds-work.patch

Patch1900: linux-2.6-intel-iommu-igfx.patch

# Quiet boot fixes
# silence the ACPI blacklist code
Patch2802: linux-2.6-silence-acpi-blacklist.patch

# media patches
Patch2899: linux-2.6-v4l-dvb-fixes.patch
Patch2900: linux-2.6-v4l-dvb-update.patch
Patch2901: linux-2.6-v4l-dvb-experimental.patch
Patch2902: linux-2.6-v4l-dvb-uvcvideo-update.patch

# fs fixes

# NFSv4

# patches headed upstream
Patch12010: add-appleir-usb-driver.patch

Patch12016: disable-i8042-check-on-apple-mac.patch

Patch12018: neuter_intel_microcode_load.patch

Patch12019: linux-2.6-rt2x00-Add-device-ID-for-RT539F-device.patch

Patch12020: linux-2.6-zd1211rw-fix-invalid-signal-values-from-device.patch

Patch12021: udlfb-bind-framebuffer-to-interface.patch

# Runtime power management
Patch12203: linux-2.6-usb-pci-autosuspend.patch
Patch12204: linux-2.6-enable-more-pci-autosuspend.patch
Patch12205: runtime_pm_fixups.patch

Patch12303: dmar-disable-when-ricoh-multifunction.patch

Patch13000: fix-scsi_dispatch_cmd.patch
Patch13001: epoll-fix-spurious-lockdep-warnings.patch

Patch20000: utrace.patch

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
    ApplyPatch patch%{?rcrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}.bz2
%if 0%{?gitrev}
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = -rc%{rcrev}%{?gitrevlibre:-libre%{?librev}}/" Makefile
    ApplyPatch patch%{?gitrevlibre}-3.%{upstream_sublevel}-rc%{rcrev}-git%{gitrev}.bz2
%endif
%else
# pre-{base_sublevel+1}-rc1 case
%if "%{?stablelibre}" != "%{?gitrevlibre}"
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =%{?rcrevlibre:-libre%{?librev}}/" Makefile
%endif
%if 0%{?gitrev}
    ApplyPatch patch%{?gitrevlibre}-3.%{base_sublevel}-git%{gitrev}.bz2
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

# WMI

# ACPI
ApplyPatch linux-2.6-defaults-acpi-video.patch
ApplyPatch linux-2.6-acpi-video-dos.patch
ApplyPatch acpi-ec-add-delay-before-write.patch
ApplyPatch linux-2.6-acpi-debug-infinite-loop.patch

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-vm-would-have-oomkilled.patch

#
# PCI
#
# enable ASPM by default on hardware we expect to work
ApplyPatch linux-2.6-defaults-aspm.patch

#
# SCSI Bits.
#

# ACPI

# ALSA
ApplyPatch hda_intel-prealloc-4mb-dmabuffer.patch

# Networking

ApplyPatch linux-2.6-zd1211rw-fix-invalid-signal-values-from-device.patch

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
ApplyOptionalPatch drm-nouveau-updates.patch

# Intel DRM
ApplyOptionalPatch drm-intel-next.patch
ApplyPatch drm-intel-make-lvds-work.patch
ApplyPatch linux-2.6-intel-iommu-igfx.patch

# silence the ACPI blacklist code
ApplyPatch linux-2.6-silence-acpi-blacklist.patch

# V4L/DVB updates/fixes/experimental drivers
#  apply if non-empty
ApplyOptionalPatch linux-2.6-v4l-dvb-fixes.patch
ApplyOptionalPatch linux-2.6-v4l-dvb-update.patch
ApplyOptionalPatch linux-2.6-v4l-dvb-experimental.patch
#ApplyPatch linux-2.6-v4l-dvb-uvcvideo-update.patch

# Patches headed upstream
ApplyPatch disable-i8042-check-on-apple-mac.patch

ApplyPatch add-appleir-usb-driver.patch

ApplyPatch neuter_intel_microcode_load.patch

ApplyPatch linux-2.6-rt2x00-Add-device-ID-for-RT539F-device.patch

ApplyPatch udlfb-bind-framebuffer-to-interface.patch

# Runtime PM
#ApplyPatch linux-2.6-usb-pci-autosuspend.patch
### Broken by implicit notify support & ACPICA rebase
###ApplyPatch linux-2.6-enable-more-pci-autosuspend.patch
#ApplyPatch runtime_pm_fixups.patch

# rhbz#605888
ApplyPatch dmar-disable-when-ricoh-multifunction.patch

ApplyPatch fix-scsi_dispatch_cmd.patch
ApplyPatch epoll-fix-spurious-lockdep-warnings.patch

# utrace.
ApplyPatch utrace.patch

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
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = %{?stablerev}-libre-%{release}.%{_target_cpu}${Flavour:+.${Flavour}}/" Makefile

    # if pre-rc1 devel kernel, must fix up SUBLEVEL for our versioning scheme
    ### XXX this will probably be dead code in 3.0 --kyle
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
    			 'register_netdev|ieee80211_register_hw|usbnet_probe|phy_driver_register|rt2x00(pci|usb)_probe'
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
* Tue Aug 02 2011 Josh Boyer <jwboyer@redhat.com>
- Fix epoll recursive lockdep warnings (rhbz 722472)
- Turn debug builds back on

* Tue Aug 02 2011 Josh Boyer <jwboyer@redhat.com>
- Add change from Yanko Kaneti to get the rt2x00 drivers in modules.networking
  (rhbz 708314)

* Fri Jul 29 2011 Dave Jones <davej@redhat.com>
- Fix scsi_dispatch_cmd oops (USB eject problems, etc).

* Thu Jul 28 2011 Dave Jones <davej@redhat.com>
- module-init-tools needs to be a prereq, not a conflict.

* Thu Jul 28 2011 Josh Boyer <jwboyer@redhat.com>
- Backport patch to correct udlfb removal events (rhbz 726163)

* Wed Jul 27 2011 Dave Jones <davej@redhat.com>
- Turn off debug builds.

* Fri Jul 22 2011 Dave Jones <davej@redhat.com>
- bootwrapper needs objcopy. Add it to requires: (wwoods)

* Fri Jul 22 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Deblobbed 3.0-libre.

* Fri Jul 22 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0.0-1
- Linux 3.0, but really 3.0.0 (sigh)

* Thu Jul 21 2011 Chuck Ebbert <cebbert@redhat.com>  3.0-0.rc7.git10.1
- 3.0-rc7-git10
- Use ext4 for ext2 and ext3 filesystems (CONFIG_EXT4_USE_FOR_EXT23=y)

* Thu Jul 21 2011 Dave Jones <davej@redhat.com>
- Switch BLK_DEV_RAM to be modular (rhbz 720833)

* Wed Jul 20 2011 Chuck Ebbert <cebbert@redhat.com> 3.0-0.rc7.git8.1
- 3.0-rc7-git8

* Wed Jul 20 2011 Alexandre Oliva <lxoliva@fsfla.org> -libre Thu Jul 21
- Deblobbed 3.0-rc7-libre.

* Fri Jul 15 2011 Dave Jones <davej@redhat.com> 3.0-0.rc7.git3.1
- 3.0-rc7-git3

* Fri Jul 15 2011 Dave Jones <davej@redhat.com>
- Bring back utrace until uprobes gets merged upstream.

* Wed Jul 13 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc7.git1.1
- Update to snapshot 3.0-rc7-git1 for intel drm fixes.

* Tue Jul 12 2011 John W. Linville <linville@redhat.com>
- zd1211rw: fix invalid signal values from device (rhbz 720093)

* Tue Jul 12 2011 John W. Linville <linville@redhat.com>
- rt2x00: Add device ID for RT539F device. (rhbz 720594)

* Tue Jul 12 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc7.git0.1
- Linux 3.0-rc7, hopefully the last before the Great Renumbering becomes
  official.

* Mon Jul 11 2011 Dave Jones <davej@redhat.com>
- Change BINFMT_MISC to be modular. (rhbz 695415)

* Sun Jul 10 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc6.git6.1
- Linux 3.0-rc6-git6
- iwlagn-fix-dma-direction.patch: drop.
- Revert CONFIG_X86_RESERVE_LOW=640, it breaks booting on x86_64.

* Thu Jul 07 2011 Dave Jones <davej@redhat.com>
- Centralise CPU_FREQ options into config-generic.
  Switch to using ondemand by default. (rhbz 713572)

* Wed Jul 06 2011 Chuck Ebbert <cebbert@redhat.com>
- Set CONFIG_X86_RESERVE_LOW=640 as requested by mjg

* Mon Jul 04 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc6.git0.1
- Linux 3.0-rc6
- [generic] SCSI_ISCI=m, because why not

* Sat Jul 02 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc5.git5.1
- Linux 3.0-rc5-git5

* Mon Jun 27 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc5.git0.1
- Linux 3.0-rc5

* Mon Jun 27 2011 Dave Jones <davej@redhat.com>
- Disable CONFIG_CRYPTO_MANAGER_DISABLE_TESTS, as this also disables FIPS (rhbz 716942)

* Thu Jun 23 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc4.git3.1
- Linux 3.0-rc4-git3
- Drop linux-3.0-fix-uts-release.patch, and instead just perl the Makefile
- linux-2.6-silence-noise.patch: fix context
- iwlagn-fix-dma-direction.patch: fix DMAR errors (for me at least)

* Wed Jun 22 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc4.git0.2
- Re-enable debuginfo generation. Thanks to Richard Jones for noticing... no
  wonder builds had been so quick lately.

* Tue Jun 21 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc4.git0.1
- Linux 3.0-rc4 (getting closer...)

* Fri Jun 17 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc3.git6.1
- Update to 3.0-rc3-git6

* Fri Jun 17 2011 Dave Jones <davej@redhat.com>
- drop qcserial 'compile fix' that was just duplicating an include.
- drop struct sizeof debug patch. (no real value. not upstreamable)
- drop linux-2.6-debug-always-inline-kzalloc.patch.
  Can't recall why this was added. Can easily re-add if deemed necessary.

* Fri Jun 17 2011 Kyle McMartin <kmcmartin@redhat.com>
- linux-2.6-defaults-pci_no_msi.patch: drop, haven't toggled the default
  in many moons.
- linux-2.6-defaults-pci_use_crs.patch: ditto.
- linux-2.6-selinux-mprotect-checks.patch: upstream a while ago.
- drm-i915-gen4-has-non-power-of-two-strides.patch: drop buggy bugfix
- drop some more unapplied crud.
- We haven't applied firewire patches in a dogs age.

* Fri Jun 17 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc3.git5.1
- Try updating to a git snapshot for the first time in 3.0-rc,
  update to 3.0-rc3-git5
- Fix a subtle bug I introduced in 3.0-rc1, "patch-3." is 9 letters, not 10.

* Thu Jun 16 2011 Kyle McMartin <kmcmartin@redhat.com>
- Disable mm patches which had been submitted against 2.6.39, as Rik reports
  they seem to aggravate a VM_BUG_ON. More investigation is necessary.

* Wed Jun 15 2011 Kyle McMartin <kmcmartin@redhat.com>
- Conflict with pre-3.2.1-5 versions of mdadm. (#710646)

* Wed Jun 15 2011 Kyle McMartin <kmcmartin@redhat.com>
- Build in aesni-intel on i686 for symmetry with 64-bit.

* Tue Jun 14 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc3.git0.3
- Fix libdm conflict (whose bright idea was it to give subpackages differing
  version numbers?)

* Tue Jun 14 2011 Kyle McMartin <kmcmartin@redhat.com>
- Update to 3.0-rc3, add another conflicts to deal with 2 digit
  versions (libdm.)
- Simplify linux-3.0-fix-uts-release.patch now that SUBLEVEL is optional.
- revert-ftrace-remove-unnecessary-disabling-of-irqs.patch: drop upstreamed
  patch.
- drm-intel-eeebox-eb1007-quirk.patch: ditto.
- ath5k-disable-fast-channel-switching-by-default.patch: ditto.

* Thu Jun 09 2011 Kyle McMartin <kmcmartin@redhat.com>
- ath5k-disable-fast-channel-switching-by-default.patch (rhbz#709122)
  (korgbz#34992) [a99168ee in wireless-next]

* Thu Jun 09 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc2.git0.2
- rhbz#710921: revert-ftrace-remove-unnecessary-disabling-of-irqs.patch

* Wed Jun 08 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc2.git0.1
- Update to 3.0-rc2, rebase utsname fix.
- Build IPv6 into the kernel for a variety of reasons
  (http://lists.fedoraproject.org/pipermail/kernel/2011-June/003105.html)

* Mon Jun 06 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc1.git0.3
- Conflict with module-init-tools older than 3.13 to ensure the
  3.0 transition is handled correctly.

* Wed Jun 01 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc1.git0.2
- Fix utsname for 3.0-rc1

* Mon May 30 2011 Kyle McMartin <kmcmartin@redhat.com> 3.0-0.rc1.git0.1
- Linux 3.0-rc1 (won't build until module-init-tools gets an update.)

* Mon May 30 2011 Kyle McMartin <kyle@redhat.com>
- Trimmed changelog, see fedpkg git for earlier history.

###
# The following Emacs magic makes C-c C-e use UTC dates.
# Local Variables:
# rpm-change-log-uses-utc: t
# End:
###
