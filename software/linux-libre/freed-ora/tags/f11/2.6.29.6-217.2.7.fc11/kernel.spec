# We have to override the new %%install behavior because, well... the kernel is special.
%global __spec_install_pre %{___build_pre}

Summary: The Linux kernel

# For a stable, released kernel, released_kernel should be 1. For rawhide
# and/or a kernel built from an rc or git snapshot, released_kernel should
# be 0.
%global released_kernel 1

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
%define fedora_cvs_origin   1462
%define fedora_cvs_revision() %2
%global fedora_build %(echo %{fedora_cvs_origin}.%{fedora_cvs_revision $Revision: 1.1679.2.7 $} | awk -F . '{ OFS = "."; ORS = ""; print $3 - $1 ; i = 4 ; OFS = ""; while (i <= NF) { print ".", $i ; i++} }')

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 29

# librev starts empty, then 1, etc, as the linux-libre tarball
# changes.  This is only used to determine which tarball to use.
%define librev 1

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
# kernel-smp (only valid for ppc 32-bit, sparc64)
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
%define with_firmware  %{?_with_firmware:  1} %{?!_with_firmware:  0}
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
%endif

# Additional options for user-friendly one-off kernel building:
#
# Only build the base kernel (--with baseonly):
%define with_baseonly  %{?_with_baseonly:     1} %{?!_with_baseonly:     0}
# Only build the smp kernel (--with smponly):
%define with_smponly   %{?_with_smponly:      1} %{?!_with_smponly:      0}

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

# We only build -PAE for 686 as of Fedora 11.
%ifarch i686
%define with_up 0
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
# they can just use i586 and ppc64 headers
%ifarch i686 ppc64iseries
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
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, mkinitrd >= 6.0.61-1, kernel-libre-firmware >= %{rpmversion}-%{pkg_release}, /sbin/new-kernel-pkg

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
Provides: kernel-drm-nouveau = 12\
Provides: kernel-modeset = 1\
Provides: kernel-uname-r = %{KVERREL}%{?1:.%{1}}\
Provides: kernel-libre-uname-r = %{KVERREL}%{?1:.%{1}}\
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
URL: http://linux-libre.fsfla.org/
Version: %{rpmversion}
Release: %{pkg_release}
# DO NOT CHANGE THE 'ExclusiveArch' LINE TO TEMPORARILY EXCLUDE AN ARCHITECTURE BUILD.
# SET %%nobuildarches (ABOVE) INSTEAD
ExclusiveArch: noarch %{all_x86} x86_64 ppc ppc64 ia64 sparc sparc64 s390x alpha alphaev56 %{arm}
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
Source31: config-i586
Source32: config-i686-PAE

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

%if !%{nopatches}

# revert upstream patches we get via other methods
Patch09: linux-2.6-upstream-reverts.patch
# Git trees.
Patch10: git-cpufreq.patch
Patch11: git-bluetooth.patch
Patch12: git-bluetooth-fixes.patch

# Standalone patches
Patch20: linux-2.6-hotfixes.patch

Patch21: linux-2.6-tracehook.patch
Patch22: linux-2.6-utrace.patch
Patch23: linux-2.6-utrace-ftrace.patch

# vm patches
Patch24: linux-2.6-defaults-saner-vm-settings.patch
Patch25: linux-2.6-mm-lru-evict-streaming-io-pages-first.patch
Patch26: linux-2.6-mm-lru-report-vm-flags-in-page-referenced.patch
Patch27: linux-2.6-mm-lru-dont-evict-mapped-executable-pages.patch

# Support suspend/resume, other crash fixes
Patch30: linux-2.6-iommu-fixes.patch

Patch41: linux-2.6-sysrq-c.patch

Patch50: make-sock_sendpage-use-kernel_sendpage.patch

#Patch101: linux-2.6-e820-save-restore-edi-ebp.patch
#Patch102: linux-2.6-e820-acpi3-bios-workaround.patch
#Patch103: linux-2.6-e820-guard-against-pre-acpi3.patch

Patch141: linux-2.6-ps3-storage-alias.patch
Patch143: linux-2.6-g5-therm-shutdown.patch
Patch144: linux-2.6-vio-modalias.patch
Patch147: linux-2.6-imac-transparent-bridge.patch

Patch150: linux-2.6.29-sparc-IOC_TYPECHECK.patch

Patch160: linux-2.6-execshield.patch
Patch250: linux-2.6-debug-sizeof-structs.patch
Patch260: linux-2.6-debug-nmi-timeout.patch
Patch270: linux-2.6-debug-taint-vm.patch
Patch280: linux-2.6-debug-spinlock-taint.patch
Patch340: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch360: linux-2.6-debug-always-inline-kzalloc.patch
Patch380: linux-2.6-defaults-pci_no_msi.patch
Patch381: linux-2.6-pciehp-update.patch
Patch382: linux-2.6-defaults-pciehp.patch
Patch383: linux-2.6-pci-sysfs-remove-id.patch
Patch390: linux-2.6-defaults-acpi-video.patch
Patch391: linux-2.6-acpi-video-dos.patch
Patch392: linux-2.6-acpi-strict-resources.patch
Patch393: linux-2.6-hwmon-atk0110.patch
Patch394: linux-2.6-acpi-video-didl-intel-outputs.patch
Patch395: linux-2.6-sony-laptop-rfkill.patch
Patch400: linux-2.6-scsi-cpqarray-set-master.patch
Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch451: linux-2.6-input-fix-toshiba-hotkeys.patch
Patch452: linux-2.6-input-hid-extra-gamepad.patch
Patch453: linux-2.6-input-wacom-bluetooth.patch
Patch455: linux-2.6-input-bcm5974-new-header-type.patch
Patch456: linux-2.6-input-bcm5974-add-quad-finger.patch
Patch457: linux-2.6-input-bcm5974-add-macbook-unibody.patch
Patch458: linux-2.6-hid-apple-mini-keyboard.patch

Patch470: linux-2.6-serial-460800.patch
Patch471: linux-2.6-serial-add-txen-test-param.patch
Patch472: linux-2.6-drivers-char-low-latency-removal.patch

# 8192 too low
Patch480: increase-MAX_LOCKDEP_ENTRIES.patch

Patch510: linux-2.6-silence-noise.patch
Patch511: linux-2.6-shut-up-efifb.patch
Patch530: linux-2.6-silence-fbcon-logo.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sparc-selinux-mprotect-checks.patch
Patch600: linux-2.6-defaults-alsa-hda-beep-off.patch
Patch601: alsa-rewrite-hw_ptr-updaters.patch
Patch602: alsa-pcm-always-reset-invalid-position.patch
Patch603: alsa-pcm-fix-delta-calc-at-overlap.patch
Patch605: alsa-hda-dont-reset-BDL-unnecessarily.patch
Patch606: alsa-dont-reset-stream-at-each-prepare-callb.patch
Patch607: alsa-hda_intel-fix-unexpected-ring-buffer-positio.patch
Patch608: alsa-pcm-safer-boundary-checks.patch
Patch610: hda_intel-prealloc-4mb-dmabuffer.patch
Patch611: linux-2.6.29-alsa-update-quirks.patch
Patch612: alsa-hda-add-debugging.patch

Patch630: net-revert-forcedeth-power-down-phy-when-interface-is.patch
Patch642: linux-2.6-netdev-r8169-use-different-family-defaults.patch

Patch670: linux-2.6-ata-quirk.patch

Patch680: linux-2.6-rt2x00-asus-leds.patch
Patch681: linux-2.6-mac80211-age-scan-results-on-resume.patch
Patch682: linux-2.6-ipw2x00-age-scan-results-on-resume.patch
Patch683: linux-2.6-iwl3945-report-killswitch-changes-even-if-the-interface-is-down.patch
Patch684: linux-2.6-iwlagn-fix-hw-rfkill-while-the-interface-is-down.patch
Patch685: linux-2.6-mac80211-fix-beacon-loss-detection-after-scan.patch
Patch686: linux-2.6-iwl3945-use-cancel_delayed_work_sync-to-cancel-rfkill_poll.patch
Patch687: mac80211-don-t-drop-nullfunc-frames-during-software.patch
Patch690: iwl3945-release-resources-before-shutting-down.patch
Patch691: iwl3945-add-debugging-for-wrong-command-queue.patch
Patch692: iwl3945-fix-rfkill-sw-and-hw-mishmash.patch
Patch693: linux-2.6-iwlwifi_-fix-TX-queue-race.patch

Patch700: linux-2.6-dma-debug-fixes.patch

Patch800: linux-2.6-crash-driver.patch

Patch1000: linux-2.6-neigh_-fix-state-transition-INCOMPLETE-_FAILED-via-Netlink-request.patch

Patch1515: linux-2.6.29-lirc.patch
Patch1517: hdpvr-ir-enable.patch
Patch1518: hid-ignore-all-recent-imon-devices.patch

# Fix the return code CD accesses when the CDROM drive door is closed
# but the drive isn't yet ready.
Patch1550: linux-2.6-cdrom-door-status.patch

Patch1700: agp-set_memory_ucwb.patch
# nouveau + drm fixes
Patch1811: drm-next.patch
Patch1812: drm-modesetting-radeon.patch
Patch1813: drm-radeon-pm.patch
Patch1814: drm-nouveau.patch
Patch1816: drm-no-gem-on-i8xx.patch
Patch1818: drm-i915-resume-force-mode.patch
Patch1819: drm-intel-big-hammer.patch
Patch1821: drm-intel-lying-systems-without-lvds.patch
Patch1822: drm-intel-gen3-fb-hack.patch
Patch1824: drm-intel-hdmi-edid-fix.patch
Patch1825: drm-intel-tiling-transition.patch
Patch1826: drm-intel-next.patch
Patch1828: drm-intel-debugfs-ringbuffer.patch
Patch1829: drm-edid-ignore-tiny-modes.patch
Patch1830: linux-2.6.29.3-boot-vga.patch
Patch1831: drm-intel-include-965gme-pci-id.patch
Patch1832: drm-intel-gem-use-dma32-on-pae.patch
Patch1833: drm-intel-i8xx-cursors.patch
Patch1834: drm-intel-vmalloc.patch
Patch1835: drm-copyback-ioctl-data-to-userspace-regardless-of-retcode.patch
Patch1836: drm-intel-disable-kms-i8xx.patch
Patch1837: drm-i915-apply-a-big-hammer-to-865-gem-object.patch
Patch1838: drm-i915-fix-tiling-pitch.patch
Patch1839: drm-intel-set-domain-on-fault.patch
Patch1840: drm-modesetting-radeon-fixes.patch
Patch1841: drm-radeon-fix-ring-commit.patch
Patch1842: drm-radeon-new-pciids.patch
Patch1843: drm-dont-frob-i2c.patch
Patch1844: drm-connector-dpms-fix.patch
Patch1845: drm-intel-tv-fix.patch
Patch1846: drm-radeon-cs-oops-fix.patch
Patch1847: drm-intel-a17-fix.patch
Patch1848: drm-pnp-add-resource-range-checker.patch
Patch1849: drm-i915-enable-mchbar.patch
Patch1850: drm-i915-gem-bad-bug-on.patch

# kludge to make ich9 e1000 work
Patch2000: linux-2.6-e1000-ich9.patch
# BZ #498854
Patch2010: linux-2.6-netdev-ehea-fix-circular-locking.patch
Patch2012: linux-2.6-netdev-ehea-fix-page-alignment.patch
Patch2013: linux-2.6-netdev-ehea-remove-from-list.patch

# linux1394 git patches
Patch2200: linux-2.6-firewire-git-update.patch
Patch2201: linux-2.6-firewire-git-pending.patch

# Quiet boot fixes
# silence the ACPI blacklist code
Patch2802: linux-2.6-silence-acpi-blacklist.patch

Patch2900: linux-2.6-v4l-dvb-fixes.patch
Patch2901: linux-2.6-v4l-dvb-experimental.patch
Patch2902: linux-2.6-v4l-dvb-fix-uint16_t-audio-h.patch
Patch2903: linux-2.6-revert-dvb-net-kabi-change.patch

# fs fixes
# ext4 fixes

Patch3000: linux-2.6-btrfs-unstable-update.patch
Patch3010: linux-2.6-relatime-by-default.patch
Patch3020: linux-2.6-fiemap-header-install.patch
Patch3030: linux-2.6-ecryptfs-overflow-fixes.patch

Patch5000: linux-2.6-add-qcserial.patch

# patches headed for -stable
# fix squashfs on systems where pagesize > blocksize (ia64, ppc64 w/64k pages)
Patch6010: squashfs-broken-when-pagesize-greater-than-blocksize.patch

# CIFS
Patch6100: linux-2.6-fs-cifs-fix-port-numbers.patch

Patch9001: revert-fix-modules_install-via-nfs.patch
Patch9010: linux-2.6-nfsd-report-short-writes.patch
Patch9020: linux-2.6-nfsd-report-short-writes-fix.patch
Patch9030: linux-2.6-nfsd-cred-refcount-fix.patch

Patch9100: cpufreq-add-atom-to-p4-clockmod.patch
# VIA processors: enable pstates
Patch9110: linux-2.6-cpufreq-enable-acpi-pstates-on-via.patch

#Adding dropwatch into rawhide until we get to 2.6.30
Patch9200: linux-2.6-dropwatch-protocol.patch

# kvm fixes
Patch9303: linux-2.6-kvm-skip-pit-check.patch
Patch9304: linux-2.6-xen-check-for-nx-support.patch
Patch9305: linux-2.6-xen-fix_warning_when_deleting_gendisk.patch
Patch9307: linux-2.6.29-xen-disable-gbpages.patch
Patch9308: linux-2.6-virtio_blk-dont-bounce-highmem-requests.patch

Patch11000: linux-2.6-parport-quickfix-the-proc-registration-bug.patch
Patch11010: linux-2.6-dev-zero-avoid-oom-lockup.patch
Patch11020: linux-2.6-usb-remove-low-latency-hack.patch
Patch11030: linux-2.6-x86-delay-tsc-barrier.patch

# via: enable 64-bit padlock support on nano, add CPU temp sensor,
#  add via-sdmmc driver
Patch11100: via-centaur-merge-32-64-bit-init.patch
Patch11105: via-sdmmc.patch
Patch11106: via-rng-64-bit-enable.patch
Patch11107: via-padlock-nano-workarounds-ecb.patch
Patch11108: via-padlock-nano-workarounds-cbc.patch
Patch11109: via-padlock-cryptodev-1-64bit-enable.patch
Patch11110: via-padlock-cryptodev-2-64bit-enable.patch
Patch11120: via-padlock-fix-might-sleep.patch
Patch11130: via-hwmon-temp-sensor.patch

# CVE-2009-1895
Patch12000: security-use-mmap_min_addr-indepedently-of-security-models.patch
Patch12010: personality-fix-per_clear_on_setid.patch

# make gcc stop optimizing away null pointer tests
Patch13000: add-fno-delete-null-pointer-checks-to-gcc-cflags.patch

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
  if ! egrep "^Patch[0-9]+: $patch\$" %{_specdir}/${RPM_PACKAGE_NAME%%%%%{?variant}}.spec ; then
    if [ "${patch:0:10}" != "patch-2.6." ] ; then
      echo "ERROR: Patch  $patch  not listed as a source patch in specfile"
      exit 1
    fi
  fi 2>/dev/null
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

#ApplyPatch git-cpufreq.patch
ApplyPatch git-bluetooth.patch
ApplyPatch git-bluetooth-fixes.patch

ApplyPatch linux-2.6-hotfixes.patch

# Roland's utrace ptrace replacement.
ApplyPatch linux-2.6-tracehook.patch
ApplyPatch linux-2.6-utrace.patch
ApplyPatch linux-2.6-utrace-ftrace.patch

# vm patches
ApplyPatch linux-2.6-defaults-saner-vm-settings.patch
ApplyPatch linux-2.6-mm-lru-evict-streaming-io-pages-first.patch
ApplyPatch linux-2.6-mm-lru-report-vm-flags-in-page-referenced.patch
ApplyPatch linux-2.6-mm-lru-dont-evict-mapped-executable-pages.patch

# IOMMU fixes backported to 2.6.29
ApplyPatch linux-2.6-iommu-fixes.patch

# enable sysrq-c on all kernels, not only kexec
ApplyPatch linux-2.6-sysrq-c.patch


# Architecture patches
# x86(-64)
#ApplyPatch linux-2.6-e820-save-restore-edi-ebp.patch
#ApplyPatch linux-2.6-e820-acpi3-bios-workaround.patch
#ApplyPatch linux-2.6-e820-guard-against-pre-acpi3.patch

#
# PowerPC
#
### NOT (YET) UPSTREAM:
# The storage alias patch is Fedora-local, and allows the old 'ps3_storage'
# module name to work on upgrades. Otherwise, I believe mkinitrd will fail
# to pull the module in,
ApplyPatch linux-2.6-ps3-storage-alias.patch
# Alleviate G5 thermal shutdown problems
ApplyPatch linux-2.6-g5-therm-shutdown.patch
# Provide modalias in sysfs for vio devices
ApplyPatch linux-2.6-vio-modalias.patch
# Work around PCIe bridge setup on iSight
ApplyPatch linux-2.6-imac-transparent-bridge.patch

#
# SPARC64
#

ApplyPatch linux-2.6.29-sparc-IOC_TYPECHECK.patch
#
# Exec shield
#
ApplyPatch linux-2.6-execshield.patch

#
# bugfixes to drivers and filesystems
#

# ext4

# xfs

# btrfs
ApplyPatch linux-2.6-btrfs-unstable-update.patch

# eCryptfs
ApplyPatch linux-2.6-ecryptfs-overflow-fixes.patch

# relatime
ApplyPatch linux-2.6-relatime-by-default.patch

# put fiemap.h into kernel-headers
ApplyPatch linux-2.6-fiemap-header-install.patch

# USB
ApplyPatch linux-2.6-add-qcserial.patch

# ACPI
ApplyPatch linux-2.6-defaults-acpi-video.patch
ApplyPatch linux-2.6-acpi-video-dos.patch
ApplyPatch linux-2.6-acpi-strict-resources.patch
ApplyPatch linux-2.6-hwmon-atk0110.patch
ApplyPatch linux-2.6-acpi-video-didl-intel-outputs.patch
ApplyPatch linux-2.6-sony-laptop-rfkill.patch

# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
ApplyPatch linux-2.6-debug-nmi-timeout.patch
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-spinlock-taint.patch
ApplyPatch linux-2.6-debug-vm-would-have-oomkilled.patch
ApplyPatch linux-2.6-debug-always-inline-kzalloc.patch

#
# PCI
#
# disable message signaled interrupts
ApplyPatch linux-2.6-defaults-pci_no_msi.patch
# update the pciehp driver
#ApplyPatch linux-2.6-pciehp-update.patch
# default to enabling passively listening for hotplug events
#ApplyPatch linux-2.6-defaults-pciehp.patch
# Add /sys/bus/pci/devices/*/remove_id
ApplyPatch linux-2.6-pci-sysfs-remove-id.patch

#
# SCSI Bits.
#
# fix cpqarray pci enable
ApplyPatch linux-2.6-scsi-cpqarray-set-master.patch

# ALSA
# squelch hda_beep by default
ApplyPatch linux-2.6-defaults-alsa-hda-beep-off.patch
ApplyPatch linux-2.6.29-alsa-update-quirks.patch

# fix alsa for pulseaudio
ApplyPatch alsa-rewrite-hw_ptr-updaters.patch
ApplyPatch alsa-pcm-always-reset-invalid-position.patch
ApplyPatch alsa-pcm-fix-delta-calc-at-overlap.patch
ApplyPatch alsa-hda-dont-reset-BDL-unnecessarily.patch
ApplyPatch alsa-dont-reset-stream-at-each-prepare-callb.patch
ApplyPatch alsa-hda_intel-fix-unexpected-ring-buffer-positio.patch
ApplyPatch alsa-pcm-safer-boundary-checks.patch
ApplyPatch hda_intel-prealloc-4mb-dmabuffer.patch
ApplyPatch alsa-hda-add-debugging.patch

# Networking
ApplyPatch net-revert-forcedeth-power-down-phy-when-interface-is.patch

# r8169 fixes from 2.6.30
ApplyPatch linux-2.6-netdev-r8169-use-different-family-defaults.patch

# Misc fixes
# The input layer spews crap no-one cares about.
ApplyPatch linux-2.6-input-kill-stupid-messages.patch

# Get away from having to poll Toshibas
ApplyPatch linux-2.6-input-fix-toshiba-hotkeys.patch

# HID: add support for another version of 0e8f:0003 device in hid-pl
ApplyPatch linux-2.6-input-hid-extra-gamepad.patch

# HID: add support for Bluetooth Wacom pads
ApplyPatch linux-2.6-input-wacom-bluetooth.patch

# bcm5974: macbook 5 (unibody) support
ApplyPatch linux-2.6-input-bcm5974-new-header-type.patch
ApplyPatch linux-2.6-input-bcm5974-add-quad-finger.patch
ApplyPatch linux-2.6-input-bcm5974-add-macbook-unibody.patch

# support apple mini keyboard (#507517)
ApplyPatch linux-2.6-hid-apple-mini-keyboard.patch

# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch
# let users skip the TXEN bug test
ApplyPatch linux-2.6-serial-add-txen-test-param.patch
# fix oops in nozomi drver (#507005) plus two others
ApplyPatch linux-2.6-drivers-char-low-latency-removal.patch

ApplyPatch increase-MAX_LOCKDEP_ENTRIES.patch

# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch

# Avoid efifb spew
ApplyPatch linux-2.6-shut-up-efifb.patch

# Make fbcon not show the penguins with 'quiet'
ApplyPatch linux-2.6-silence-fbcon-logo.patch

# Fix the SELinux mprotect checks on executable mappings
ApplyPatch linux-2.6-selinux-mprotect-checks.patch
# Fix SELinux for sparc
ApplyPatch linux-2.6-sparc-selinux-mprotect-checks.patch

# Changes to upstream defaults.


# ia64 ata quirk
ApplyPatch linux-2.6-ata-quirk.patch

# rt2x00: back-port activity LED init patches
ApplyPatch linux-2.6-rt2x00-asus-leds.patch

# back-port scan result aging patches
ApplyPatch linux-2.6-mac80211-age-scan-results-on-resume.patch
ApplyPatch linux-2.6-ipw2x00-age-scan-results-on-resume.patch

# back-port iwlwifi rfkill while device down patches
ApplyPatch linux-2.6-iwl3945-report-killswitch-changes-even-if-the-interface-is-down.patch
ApplyPatch linux-2.6-iwlagn-fix-hw-rfkill-while-the-interface-is-down.patch
ApplyPatch linux-2.6-iwl3945-use-cancel_delayed_work_sync-to-cancel-rfkill_poll.patch

# back-port mac80211: fix beacon loss detection after scan
ApplyPatch linux-2.6-mac80211-fix-beacon-loss-detection-after-scan.patch

ApplyPatch mac80211-don-t-drop-nullfunc-frames-during-software.patch

ApplyPatch iwl3945-release-resources-before-shutting-down.patch
ApplyPatch iwl3945-add-debugging-for-wrong-command-queue.patch
ApplyPatch iwl3945-fix-rfkill-sw-and-hw-mishmash.patch

# iwlwifi: fix TX queue race
ApplyPatch linux-2.6-iwlwifi_-fix-TX-queue-race.patch

# Fix up DMA debug code
ApplyPatch linux-2.6-dma-debug-fixes.patch

# /dev/crash driver.
ApplyPatch linux-2.6-crash-driver.patch

# neigh: fix state transition INCOMPLETE->FAILED via Netlink request
ApplyPatch linux-2.6-neigh_-fix-state-transition-INCOMPLETE-_FAILED-via-Netlink-request.patch

# http://www.lirc.org/
ApplyPatch linux-2.6.29-lirc.patch
ApplyPatch hid-ignore-all-recent-imon-devices.patch

# Fix the return code CD accesses when the CDROM drive door is closed
# but the drive isn't yet ready.
ApplyPatch linux-2.6-cdrom-door-status.patch

ApplyPatch linux-2.6-e1000-ich9.patch
# bz 498854
ApplyPatch linux-2.6-netdev-ehea-fix-circular-locking.patch
ApplyPatch linux-2.6-netdev-ehea-fix-page-alignment.patch
ApplyPatch linux-2.6-netdev-ehea-remove-from-list.patch

ApplyPatch agp-set_memory_ucwb.patch
# Nouveau DRM + drm fixes
ApplyPatch drm-next.patch
ApplyPatch drm-modesetting-radeon.patch
ApplyPatch drm-nouveau.patch
# pm broken on my thinkpad t60p - airlied
#ApplyPatch drm-radeon-pm.patch
ApplyPatch drm-no-gem-on-i8xx.patch
ApplyPatch drm-i915-resume-force-mode.patch
ApplyPatch drm-intel-big-hammer.patch
ApplyPatch drm-intel-lying-systems-without-lvds.patch
ApplyPatch drm-intel-gen3-fb-hack.patch
ApplyPatch drm-intel-hdmi-edid-fix.patch
ApplyPatch drm-intel-tiling-transition.patch
ApplyPatch drm-intel-next.patch
ApplyPatch drm-intel-debugfs-ringbuffer.patch
ApplyPatch drm-edid-ignore-tiny-modes.patch
ApplyPatch drm-intel-include-965gme-pci-id.patch
ApplyPatch linux-2.6.29.3-boot-vga.patch
ApplyPatch drm-intel-gem-use-dma32-on-pae.patch
ApplyPatch drm-intel-i8xx-cursors.patch
ApplyPatch drm-intel-vmalloc.patch
ApplyPatch drm-copyback-ioctl-data-to-userspace-regardless-of-retcode.patch
# These should be fixed with the fix-tiling patch
# ApplyPatch drm-intel-disable-kms-i8xx.patch
ApplyPatch drm-i915-apply-a-big-hammer-to-865-gem-object.patch
ApplyPatch drm-i915-fix-tiling-pitch.patch
ApplyPatch drm-intel-set-domain-on-fault.patch
ApplyPatch drm-modesetting-radeon-fixes.patch
ApplyPatch drm-radeon-fix-ring-commit.patch
ApplyPatch drm-radeon-new-pciids.patch
ApplyPatch drm-dont-frob-i2c.patch
ApplyPatch drm-connector-dpms-fix.patch
ApplyPatch drm-intel-tv-fix.patch
ApplyPatch drm-radeon-cs-oops-fix.patch
ApplyPatch drm-intel-a17-fix.patch
ApplyPatch drm-pnp-add-resource-range-checker.patch
ApplyPatch drm-i915-enable-mchbar.patch
ApplyPatch drm-i915-gem-bad-bug-on.patch

# linux1394 git patches
ApplyPatch linux-2.6-firewire-git-update.patch
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-firewire-git-pending.patch | awk '{print $1}')
#if [ "$C" -gt 10 ]; then
#ApplyPatch linux-2.6-firewire-git-pending.patch
#fi

# silence the ACPI blacklist code
ApplyPatch linux-2.6-silence-acpi-blacklist.patch

# V4L/DVB updates/fixes/experimental drivers
ApplyPatch linux-2.6-v4l-dvb-fixes.patch
ApplyPatch linux-2.6-v4l-dvb-experimental.patch
ApplyPatch linux-2.6-v4l-dvb-fix-uint16_t-audio-h.patch
ApplyPatch linux-2.6-revert-dvb-net-kabi-change.patch
ApplyPatch hdpvr-ir-enable.patch

# revert 8b249b6856f16f09b0e5b79ce5f4d435e439b9d6
ApplyPatch revert-fix-modules_install-via-nfs.patch

ApplyPatch linux-2.6-dropwatch-protocol.patch

# patches headed for -stable
ApplyPatch squashfs-broken-when-pagesize-greater-than-blocksize.patch

# fix nfs reporting of short writes (#493500)
ApplyPatch linux-2.6-nfsd-report-short-writes.patch
# fix the short write fix (#508174)
ApplyPatch linux-2.6-nfsd-report-short-writes-fix.patch
# Fix null credential bug (#494067)
ApplyPatch linux-2.6-nfsd-cred-refcount-fix.patch

# fix cifs mount option "port=" (#506574)
ApplyPatch linux-2.6-fs-cifs-fix-port-numbers.patch

# cpufreq
ApplyPatch cpufreq-add-atom-to-p4-clockmod.patch
ApplyPatch linux-2.6-cpufreq-enable-acpi-pstates-on-via.patch
# kvm fixes
ApplyPatch linux-2.6-kvm-skip-pit-check.patch
ApplyPatch linux-2.6-xen-check-for-nx-support.patch
ApplyPatch linux-2.6-xen-fix_warning_when_deleting_gendisk.patch
ApplyPatch linux-2.6.29-xen-disable-gbpages.patch
# http://git.kernel.org/?p=linux/kernel/git/torvalds/linux-2.6.git;a=commitdiff;h=4eff3cae9c9809720c636e64bc72f212258e0bd5 (#510304)
ApplyPatch linux-2.6-virtio_blk-dont-bounce-highmem-requests.patch
# finally fix the proc registration bug (F11#503773 and others)
ApplyPatch linux-2.6-parport-quickfix-the-proc-registration-bug.patch
#
ApplyPatch linux-2.6-dev-zero-avoid-oom-lockup.patch
# fix oopses in usb serial devices (#500954)
ApplyPatch linux-2.6-usb-remove-low-latency-hack.patch

ApplyPatch linux-2.6-x86-delay-tsc-barrier.patch

# CVE-2009-1895
ApplyPatch security-use-mmap_min_addr-indepedently-of-security-models.patch
ApplyPatch personality-fix-per_clear_on_setid.patch

# don't optimize out null pointer tests
ApplyPatch add-fno-delete-null-pointer-checks-to-gcc-cflags.patch

ApplyPatch make-sock_sendpage-use-kernel_sendpage.patch

# VIA: add 64-bit padlock support, sdmmc driver, temp sensor driver
ApplyPatch via-centaur-merge-32-64-bit-init.patch
ApplyPatch via-padlock-fix-might-sleep.patch
ApplyPatch via-padlock-cryptodev-1-64bit-enable.patch
ApplyPatch via-padlock-cryptodev-2-64bit-enable.patch
ApplyPatch via-padlock-nano-workarounds-ecb.patch
ApplyPatch via-padlock-nano-workarounds-cbc.patch
ApplyPatch via-sdmmc.patch
ApplyPatch via-rng-64-bit-enable.patch
ApplyPatch via-hwmon-temp-sensor.patch

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
    if [ -d arch/%{asmarch}/include ]; then
      cp -a --parents arch/%{asmarch}/include $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cd include
    cp -a acpi config crypto keys linux math-emu media mtd net pcmcia rdma rxrpc scsi sound trace video drm asm-generic $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
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
    			 'ata_scsi_ioctl|scsi_add_host|blk_init_queue|register_mtd_blktrans|scsi_esp_register'
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
%if %{with_up}
BuildKernel %make_target %kernel_image debug
%endif
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
make htmldocs || %{doc_build_fail}
make %{?_smp_mflags} mandocs || %{doc_build_fail}

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
%dir /usr/src/kernels\
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
%if %{with_up}
%kernel_variant_files %{with_debug} debug
%endif
%kernel_variant_files %{with_pae} PAE
%kernel_variant_files %{with_pae_debug} PAEdebug
%kernel_variant_files -k vmlinux %{with_kdump} kdump

# plz don't put in a version string unless you're going to tag
# and build.

%changelog
* Fri Aug 14 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.6-217.2.7
- CVE-2009-2692: Fix sock sendpage NULL ptr deref.

* Thu Aug 13 2009 Kristian Høgsberg <krh@redhat.com> - 2.6.29.6-217.2.6
- Backport 0e7ddf7e to fix bad BUG_ON() in i915 gem fence management
  code.  Adds drm-i915-gem-bad-bug-on.patch, fixes #514091.

* Wed Aug 12 2009 John W. Linville <linville@redhat.com> 2.6.29.6-217.2.5
- iwlwifi: fix TX queue race

* Mon Aug 10 2009 Jarod Wilson <jarod@redhat.com> 2.6.29.6-217.2.4
- Add tunable pad threshold support to lirc_imon
- Blacklist all iMON devices in usbhid driver so lirc_imon can bind
- Add new device ID to lirc_mceusb (#512483)
- Enable IR transceiver on the HD PVR

* Wed Jul 29 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-217.2.3
- Don't optimize away NULL pointer tests where pointer is used before the test.
  (CVE-2009-1897)

* Wed Jul 29 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-217.2.2
- Fix mmap_min_addr security bugs (CVE-2009-1895)

* Wed Jul 29 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-217.2.1
- Fix eCryptfs overflow issues (CVE-2009-2406, CVE-2009-2407)

* Thu Jul 23 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.6-217
- Apply three patches requested by sgruszka@redhat.com:
 - iwl3945-release-resources-before-shutting-down.patch
 - iwl3945-add-debugging-for-wrong-command-queue.patch
 - iwl3945-fix-rfkill-sw-and-hw-mishmash.patch
 
* Thu Jul 23 2009 Jarod Wilson <jarod@redhat.com>
- virtio_blk: don't bounce highmem requests, works around a frequent
  oops in kvm guests using virtio block devices (#510304)

* Wed Jul 22 2009 Tom "spot" Callaway <tcallawa@redhat.com>
- We have to override the new %%install behavior because, well... the kernel is
special.

* Wed Jul 22 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: Fix DPMS off for DAC outputs, NV4x PFIFO typo

* Tue Jul 07 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-213
- Drop the correct patch to fix bug #498858

* Mon Jul 06 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-212
- Additional fixes for bug #498854

* Thu Jul 02 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-211
- Fix NFSD null credentials bug (#494067)
- Remove null credentials debugging patch.

* Thu Jul 02 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-210
- Linux 2.6.29.6

* Wed Jul 01 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.6-209.rc1
- Linux 2.6.29.6-rc1
- Enable CONFIG_DEBUG_CREDENTIALS in debug kernels only.
- Dropped patches merged upstream:
    linux-2.6-netdev-r8169-fix-lg-pkt-crash.patch
    linux-2.6-input-atkbd-forced-release.patch

* Wed Jul 01 2009 Dave Airlie <airlied@redhat.com> 2.6.29.5-208
- drm-intel-a17-fix.patch, drm-pnp-add-resource-range-checker.patch,
  drm-i915-enable-mchbar.patch:
    backport upstream fixes for 915/945 tiling slowness.

* Tue Jun 30 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-207
- Fix stalled NFS writes (#508174)
- Fix broken TSC-based delay.

* Tue Jun 30 2009 Jarod Wilson <jarod@redhat.com> 2.6.29.5-206
- Fix busticated lirc_serial (#504402)

* Tue Jun 30 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.5-205
- nouveau: Forcibly DPMS on DAC/SORs during modeset

* Mon Jun 29 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-204
- Fix "port=" option in CIFS mount calls. (#506574)

* Mon Jun 29 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-203
- Add support for Apple mini keyboard (#507517)

* Mon Jun 29 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-202
- New debug patch for null selinux credentials (for bug #494067)

* Mon Jun 26 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.5-201
- nouveau: bump timeout up a bit, some people hitting false hangs

* Mon Jun 26 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.5-200
- nouveau: backport nv50 output script fixes from upstream

* Mon Jun 26 2009 Ben Skeggs <bskeggs@redhat.com>
- nouveau: fix GT200 context control, will allow use of 3D engine now

* Wed Jun 24 2009 Jarod Wilson <jarod@redhat.com> 2.6.29.5-198
- Fix lirc_i2c functionality (#507047)
- Add ability to disable lirc_imon mouse mode

* Wed Jun 24 2009 Kyle McMartin <kyle@redhat.com>
- config changes:
 - generic:
  - CONFIG_SCSI_DEBUG=m (was off, requested by davidz.)

* Mon Jun 22 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-196
- Fix oopses in a bunch of USB serial devices (#500954)

* Sat Jun 20 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-195
- Add linux-2.6-drivers-char-low-latency-removal.patch
  to fix oops in nozomi driver (#507005)

* Thu Jun 18 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.5-194
- drm-nouveau.patch: un-break DPMS after DRM changes

* Thu Jun 18 2009 Dave Airlie <airlied@redhat.com> 2.6.29.5-193
- drm-radeon-cs-oops-fix.patch: fix oops if CS path called from non-kms

* Wed Jun 17 2009 Jarod Wilson <jarod@redhat.com>
- New lirc_imon hotness:
  * support dual-interface devices with a single lirc device
  * directional pad functions as an input device mouse
  * touchscreen devices finally properly supported
  * support for using MCE/RC-6 protocol remotes
  * fix oops in RF remote association code (F10 bug #475496)
  * fix re-enabling case/panel buttons and/or knobs
- Add some misc additional lirc_mceusb2 transceiver IDs
- Add missing unregister_chrdev_region() call to lirc_dev exit
- Add it8720 support to lirc_it87

* Tue Jun 16 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-191
- Copy latest version of the -mm streaming IO and executable pages patches from F-10
- Copy the saner-vm-settings patch from F-10:
    change writeback interval from 5,30 seconds to 3,10 seconds
- Comment out the null credentials debugging patch (bug #494067)

* Tue Jun 16 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-190
- Two r8169 driver updates from 2.6.30
- Update via-sdmmc driver

* Tue Jun 16 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-189
- New debug patch for bug #494067, now enabled for non-debug kernels too.

* Tue Jun 16 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-188
- Avoid lockup on OOM with /dev/zero

* Tue Jun 16 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.5-187
- Drop the disable of mwait on VIA Nano processor. The lockup bug is
  fixed by BIOS updates.

* Tue Jun 16 2009 Alexandre Oliva <lxoliva@fsfla.org> -libre
- Add -libre provides for kernel and devel packages.
- Adjusted drm-modesetting-radeon-fixes.patch for deblobbed sources.
- Adjusted drm-radeon-new-pciids.patch for deblobbed sources.

* Tue Jun 16 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.5-186
- nouveau: Use VBIOS image from PRAMIN in preference to PROM (#492658)

* Tue Jun 16 2009 Dave Airlie <airlied@redhat.com> 2.6.29.5-185
- drm-connector-dpms-fix.patch - allow hw to dpms off
- drm-dont-frob-i2c.patch - don't play with i2c bits just do EDID
- drm-intel-tv-fix.patch - fixed intel tv after connector dpms
- drm-modesetting-radeon-fixes.patch - fix AGP issues (go faster) (otaylor)
- drm-radeon-fix-ring-commit.patch - fix stability on some radeons
- drm-radeon-new-pciids.patch - add rv770/790 support
- drm-intel-vmalloc.patch - fix vmalloc patch

* Mon Jun 15 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.5-184
- Get rid of the annoying parport sysctl registration warning (#503773)
  (linux-2.6-parport-quickfix-the-proc-registration-bug.patch)

* Mon Jun 15 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.5-183
- Linux 2.6.29.5

* Mon Jun 15 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.5-182.rc1
- Add support for touchpad on MacBook 5 (Unibody) (#504197)

* Mon Jun 15 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.5-181.rc1
- Fix reporting of short writes to the NFS client (#493500)

* Mon Jun 15 2009 John W. Linville <linville@redhat.com>
- neigh: fix state transition INCOMPLETE->FAILED via Netlink request

* Fri Jun 12 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.5-179.rc1
- VIA Nano / VX800 fixes
    Padlock 64-bit fixes
    Disable mwait on the Nano
    Add via-sdmmc driver
    Enable the VIA random number generator on 64-bit
- Enable the userspace ARP daemon (#502844)

* Wed Jun 10 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: fill in modes derived from VBIOS tables better

* Tue Jun  9 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.5-177.rc1
- 2.6.29.5-rc1
- Reverted from stable, patch already in drm-next:
    drm-r128-fix-r128-ioremaps-to-use-ioremap_wc.patch
- Dropped patches, merged in -stable:
    hpet-fixes.patch
    keys-Handle-there-being-no-fallback-destination-key.patch
    kvm-Fix-PDPTR-reloading-on-CR4-writes.patch
    kvm-Make-paravirt-tlb-flush-also-reload-the-PAE-PDP.patch
    linux-2.6-ptrace-fix-possible-zombie-leak.patch
    linux-2.6-usb-cdc-acm-remove-low-latency-flag.patch
    linux-2.6-xen-xenbus_state_transition_when_not_connected.patch
    linux-2.6.29.5-ext4-stable-fixes.patch

* Tue Jun 09 2009 John W. Linville <linville@tuxdriver.com>
- Clean-up some wireless bits in config-generic

* Tue Jun  9 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.4-175
- Add ext4 stable patch queue, 18 patches submitted for 2.6.29.5
  (adds 10 patches that weren't already in F-11.)

* Tue Jun  9 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.4-174
- Add support for ACPI P-states on VIA processors.
- Disable the e_powersaver driver.

* Mon Jun  8 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.4-173
- Add linux-2.6-ptrace-fix-possible-zombie-leak.patch
  Fixes bug #481753, ptraced processes fail to deliver exit notification to parent

* Mon Jun  8 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.4-172
- Add linux-2.6-netdev-ehea-fix-circular-locking.patch (#498854)

* Mon Jun  8 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.4-171
- Add AT keyboard forced key release quirks for four more notebooks.
  (Fixes Samsung NC20/Q45, Fujitsu PA1510/Xi3650)

* Mon Jun  8 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.4-170
- Drop ALSA jiffies-based PCM boundary checking (#498858)

* Mon Jun  8 2009 Chuck Ebbert <cebbert@redhat.com> - 2.6.29.4-169
- Add debug patch for finding null security credentials. (494067)

* Tue Jun  2 2009 Alexandre Oliva <lxoliva@fsfla.org> -libre...1 Tue Jun  9 2009
- Switched to 2.6.29-libre1, fixes e100, disables again mga, r128 and radeon.
- Adjust drm-modesetting-radeon.patch.

* Tue Jun  2 2009 Roland McGrath <roland@redhat.com> - 2.6.29.4-168
- utrace update (fixes stap PR10185)

* Wed May 27 2009 Kristian Høgsberg <krh@redhat.com> - 2.6.29.4-167
- Actually disable drm-intel-disable-kms-i8xx.patch.

* Wed May 27 2009 Kristian Høgsberg <krh@redhat.com> - 2.6.29.4-166
- Add drm-intel-set-domain-on-fault.patch to fix random gem object
  corruption when swapping (495323 and probably others).
- Enable kms on 845 and 855 as well, Erics tiling patch should fix
  those too.

* Wed May 27 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.4-165
- Enable KMS/gem on I865.
- drm-no-gem-on-i8xx.patch: Remove I865 so GEM will be enabled.
- drm-intel-disable-kms-i8xx.patch: Enable KMS on I865.
- Two fixes from Eric Anholt to fix i8x5:
   drm-i915-apply-a-big-hammer-to-865-gem-object.patch
   drm-i915-fix-tiling-pitch.patch

* Wed May 27 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.4-164
- drm-intel-disable-kms-i8xx.patch: disable KMS by default on 845, 855,
  and 865. It can be forced on with i915.modeset=1 boot parameter.

* Tue May 26 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.4-163
- drm-nouveau.patch: fix sor dpms (rh#501877)

* Mon May 25 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.4-162
- keys-Handle-there-being-no-fallback-destination-key.patch:
  fix oops at boot with autofs/krb/cifs rhbz#501588.

* Mon May 25 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.4-161
- Linux 2.6.29.4
- dropped patches:
  linux-2.6-i2c-fix-bit-algorithm-timeout.patch
  linux-2.6-ftdi-oops.patch
  linux-2.6-btrfs-fix-page-mkwrite.patch
- rebased patches:
  linux-2.6-btrfs-unstable-update.patch, page_mkwrite fixes.

* Mon May 25 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-160
- kvm fixes destined for 2.6.30, rhbz#492838:
   kvm-Fix-PDPTR-reloading-on-CR4-writes.patch
   kvm-Make-paravirt-tlb-flush-also-reload-the-PAE-PDP.patch

* Fri May 22 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-159
- drm-copyback-ioctl-data-to-userspace-regardless-of-retcode.patch:
  Fix possible hang in drmWaitVblank.
   upstream 9b6fe313bfce27d4a261257da70196be0ac2bef5.

* Fri May 22 2009 John W. Linville <linville@redhat.com> - 2.6.29.3-158
- back-port "iwl3945: use cancel_delayed_work_sync to cancel rfkill_poll"
- modify changelog entry from Apr 13 2009 to reference correct patch

* Thu May 21 2009 Kyle McMartin <kyle@redhat.com> - 2.6.29.3-157
- mac80211-don-t-drop-nullfunc-frames-during-software.patch:
   upstream a9a6ffffd05f97e6acbdeafc595e269855829751.

* Wed May 20 2009  Chuck Ebbert <cebbert@redhat.com> - 2.6.29.3-156
- Enable Divas (formerly Eicon) ISDN drivers on x86_64. (#480837)

* Wed May 20 2009  <krh@redhat.com> - 2.6.29.3-155
- Add drm-intel-i8xx-cursors.patch to fix cursors on i8xx desktop
  chipsets (#488980).
- Add drm-intel-vmalloc.patch as part of the fix for #498131.

* Wed May 20 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-154
- disable e820 backports, causes problems in some places, bz#499396.
  linux-2.6-e820-save-restore-edi-ebp.patch
  linux-2.6-e820-acpi3-bios-workaround.patch
  linux-2.6-e820-guard-against-pre-acpi3.patch

* Tue May 19 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-153
- drm-intel-gem-use-dma32-on-pae.patch: Force GEM allocations to be DMA32
  when using PAE. This should fix bz#493526. Leave the gfp flags for every
  other chipset (radeon, really...) unset so we don't fribble the flags.
- agp-set_memory_ucwb.patch: comment out rejecting hunk that's no longer
  necessary (forcing gem on with highmem64g.)

* Tue May 19 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-151
- net-revert-forcedeth-power-down-phy-when-interface-is.patch: revert only
  hunks that powered down the phy. fixes rhbz#501249.

* Mon May 18 2009 Kyle McMartin <kyle@redhat.com>
- drm-intel-include-965gme-pci-id.patch: add patch patch from git head to
  treat 965GME like 965GM.

* Mon May 18 2009 Adam Jackson <ajax@redhat.com>
- Expose whether VGA devices were active at boot or not in sysfs.

* Mon May 18 2009 Justin M. Forbes <jforbes@redhat.com>
- Disable GB pages for Xen guests BZ# 499592

* Mon May 18 2009 Kyle McMartin <kyle@redhat.com>
- increase-MAX_LOCKDEP_ENTRIES.patch: suck in upstream fix
  d80c19df5fcceb8c741e96f09f275c2da719efef

* Mon May 18 2009 Justin M. Forbes <jforbes@redhat.com>
- xen/blkfront: fix warning when deleting gendisk on unplug/shutdown. BZ# 499621
- xen/blkfront: allow xenbus state transition to closing->closed when
  not connected

* Mon May 18 2009 Josef Bacik <josef@toxicpanda.com>
- fix page_mkwrite in btrfs

* Mon May 18 2009 David Woodhouse <David.Woodhouse@intel.com>
- Fix oops on FTDI removal.

* Fri May 15 2009 Eric Sandeen <sandeen@redhat.com>
- ext4: corruption fixes from upstream.

* Fri May 15 2009 Adam Jackson <ajax@redhat.com>
- drm: ignore tiny modes from EDID.

* Tue May 12 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-142
- linux-2.6-iommu-fixes.patch: intel-iommu: fix PCI device detach
    from virtual machine
  3199aa6bc8766e17b8f60820c4f78d59c25fce0e upstream.

* Tue May 12 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-141
- linux-2.6-iommu-fixes.patch: intel-iommu: PAE memory corruption fix
  fd18de50b9e7965f93d231e7390436fb8900c0e6 upstream. Also, re-cherry-pick
  patchset and fix up merge conflicts against 2.6.29.3.

* Tue May 12 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-140
- git-bluetooth-fixes.patch: fix build error in backport from previous
  commit.

* Tue May 12 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-139
- git-bluetooth.patch: suck in three more fixes from 2.6.30.

* Tue May 12 2009 Josef Bacik <josef@toxicpanda.com>
- bring btrfs uptodate with mainline

* Mon May 11 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-137
- net-revert-forcedeth-power-down-phy-when-interface-is.patch:
   Attempt to fix forcedeth failures, (#484505)

* Sat May 09 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.3-136
- Add more verbose debug messages for bug #498401

* Fri May 08 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.3-135
- Linux 2.6.29.3
- Merged patches:
  linux-2.6-x86-64-fix-fpu-corruption-with-signals-and-preemption.patch
  linux-2.6-ath9k-Fix-FIF_BCN_PRBRESP_PROMISC-handling.patch
  drm-intel-g41.patch
  drm-intel-tiled-front.patch
- Rebased patches:
  drm-next.patch - pci ids rejects
  linux-2.6-utrace.patch - fs/proc/array.c rejects
  linux-2.6-execshield.patch - fs/proc/array.c, drop useless hunk, dropped
    already in rawhide.
  linux-2.6-iommu-fixes.patch - single merged patch from series.

* Fri May 08 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.2-134
- linux-2.6-mm-lru-dont-evict-mapped-executable-pages.patch
  linux-2.6-mm-lru-evict-streaming-io-pages-first.patch
    Add VM patches from Rik to fix responsiveness, tested on the rawhide
    kernel.

* Thu May 07 2009 Adam Jackson <ajax@redhat.com>
- drm-intel-debugfs-ringbuffer.patch: Add debugfs support for
  intel_gpu_dump utility.

* Thu May  7 2009 Kristian Høgsberg <krh@redhat.com> 2.6.29.2-132
- drm intel fixes: Add PCI IDs for G41, fix tiling after resume.

* Thu May 07 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.2-131
- drm-nouveau.patch: fix bar1 mtrr size

* Wed May 06 2009 John W. Linville <linville@redhat.com> 2.6.29.2-130
- back-port ath9k: Fix FIF_BCN_PRBRESP_PROMISC handling

* Wed May 06 2009 Dave Airlie <airlied@redhat.com> 2.6.29.2-129
- drm-next: update with latest patch queue for upstream + intel fixes
- drm-modesetting-radeon.patch: rebase onto drm-next
- drm-nouveau.patch: rebase onto drm-next

* Tue May 05 2009 Dave Airlie <airlied@redhat.com> 2.6.29.2-128
- radeon kms: backport some fixes - put back old internal interface

* Tue May 05 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.2-127
- drm-nouveau.patch: bump dac load detection delay to match ddx

* Mon May 04 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.2-126
- Explicitly enable CONFIG_FB_MODE_HELPERS to fix build..

* Mon May 04 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.2-125
- Disable CONFIG_FB_NVIDIA due to very bad interactions with nouveau

* Sun May 03 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.2-124
- drm-nouveau.patch: ignore unsupported dcb encoder types completely
- nv50: module option to run output scripts, too raw to be by default yet,
  but will fix a number of issues in the places where they work.

* Sat May 02 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.2-123
- Build htmldocs single threaded.

* Sat May 02 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.2-122
- drm-nouveau.patch: nv50 connector grouping fixes
- bios parser updates from ddx (nv50 panel mode from VBIOS table)
- pre-nv50 modesetting updates from ddx (in disabled codepath)

* Fri May 01 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.2-121
- More bluetooth fixes from 2.6.30-rc.
- linux-2.6-v4l-dvb-fixes.patch: restore, accidently nuked.
- linux-2.6-v4l-dvb-experimental.patch: restore to previous version
   to fix rejects against restored -fixes.

* Fri May 01 2009 Eric Sandeen <sandeen@redhat.com>
- Fix ext4 corruption on partial write into prealloc block

* Thu Apr 30 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.2-119
- Update to 2.6.29.2
- Patches rebased:
  linux-2.6-v4l-dvb-experimental.patch
  drm-next.patch
- Patches merged upstream:
  linux-2.6-acer-wmi-bail-on-aao.patch
  linux-2.6-e820-mark-esi-clobbered.patch
  linux-2.6-kvm-kconfig-irqchip.patch
  linux-2.6-kvm-mask-notifiers.patch
  linux-2.6-kvm-reset-pit-irq-on-unmask.patch
  linux-2.6-md-raid1-dont-assume-new-bvecs-are-init.patch
  linux-2.6-mm-define-unique-value-for-as_unevictable.patch
  linux-2.6-net-fix-another-gro-bug.patch
  linux-2.6-posix-timers-fix-clock-monotonicity.patch
  linux-2.6-posix-timers-fix-rlimit_cpu-fork-2.patch
  linux-2.6-posix-timers-fix-rlimit_cpu-setitimer.patch
  linux-2.6-v4l-dvb-fixes.patch
  linux-2.6-v4l-dvb-update.patch
  linux-2.6.29.1-sparc-regression.patch
  pat-remove-page-granularity-tracking-for-vm_insert_pfn_maps.patch

* Thu Apr 30 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-118
- drm-radeon-kms-fixes.patch: revert rs480 snoop break

* Thu Apr 30 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-117
- drm-radeon-kms-fixes.patch: add r300 clip regs

* Thu Apr 30 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-116
- linux-2.6-drm-r128-ioremap.patch: fix r128 DRI fail
- drm-radeon-kms-fixes.patch: hopefully fix AGP corruption

* Tue Apr 28 2009 Adam Jackson <ajax@redhat.com> 2.6.29.1-115
- drm-intel-tiling-transition.patch: Fix transitions to linear mode.

* Tue Apr 28 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.1-114
- Make the kernel-vanilla package buildable again.

* Mon Apr 27 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.1-113
- Fix possible FPU context corruption on x86-64.

* Mon Apr 27 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.1-112
- 7a6f9cbb: x86: hpet: fix periodic mode programming on AMD 81xx

* Fri Apr 24 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.1-111
- backport hpet fixes from 2.6.30-rc3.

* Fri Apr 24 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-109
- linux-2.6-i2c-fix-bit-algorithm-timeout.patch - fix i2c EDID timeout

* Fri Apr 24 2009 Ben Skeggs <bskeggs@redhat.com> 2.6.29.1-108
- nouveau/nv50: don't clobber 0x001700 during instmem init, can confuse ddx

* Thu Apr 23 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.1-107
- Drop POSIX timer patch accidentally committed in 2.6.30.

* Wed Apr 22 2009 John W. Linville <linville@redhat.com> 2.6.29.1-106
- back-port mac80211: fix beacon loss detection after scan

* Tue Apr 21 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.1-105
- Don't include the modules.*.bin files in the RPM package.

* Tue Apr 21 2009 Adam Jackson <ajax@redhat.com> 2.6.29.1-104
- drm-intel-hdmi-edid-fix.patch: Fix EDID fetch on SDVO HDMI.

* Tue Apr 21 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-103
- radeon kms: fix lcd edid detection + fix legacy crtc setup

* Mon Apr 20 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.1-102
- git-bluetooth2.patch: Bluetooth fixes from 2.6.30-rc2.

* Sun Apr 19 2009 Mark McLoughlin <markmc@redhat.com> - 2.6.29.1-101
- Fix xen boot on machines without NX support (#492523)

* Sat Apr 18 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.1-100
- pat-remove-page-granularity-tracking-for-vm_insert_pfn_maps.patch:
   Fix the spew of "Xorg:3254 freeing invalid memtype" messages.

* Sat Apr 18 2009 Chuck Ebbert <cebbert@redhat.com>
- Build in the rfkill and rfkill-input modules (F10#485322)

* Sat Apr 18 2009 Chuck Ebbert <cebbert@redhat.com>
- Set CONFIG_UEVENT_HELPER_PATH to the empty string (#496296)

* Fri Apr 17 2009 Adam Jackson <ajax@redhat.com> 2.6.29.1-97
- drm-intel-tiled-front.patch: Enable tiled front buffer on gen4

* Fri Apr 17 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-95
- nouveau: fix powerpc build

* Fri Apr 17 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-94
- drm-next.patch: backport fix to drm-next

* Fri Apr 17 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-93
- drm modesetting: friday patch, missed a git add

* Fri Apr 17 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-92
- drm modesetting: force mode switch when connectors change

* Fri Apr 17 2009 Dave Airlie <airlied@redhat.com>
- radeon drm: fix oops in LUT loading

* Fri Apr 17 2009 Dave Airlie <airlied@redhat.com>
- drop ajax patch I rolled it in

* Fri Apr 17 2009 Dave Airlie <airlied@redhat.com>
- radeon drm: fix rv410 ddc + add another agp test hook

* Thu Apr 16 2009 Adam Jackson <ajax@redhat.com>
- radeon drm: Be sure to set a name for LVDS panel modes.

* Thu Apr 16 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: use less vmalloc space (rh#495843)

* Wed Apr 15 2009 Marcelo Tosatti <mtosatti@redhat.com> 2.6.29.1-85
- Skip PIT-through-IOAPIC routing check on KVM guests.

* Wed Apr 15 2009 Chuck Ebbert <cebbert@redhat.com>
- Add serial driver option to skip testing for the TXEN bug. (#495762)

* Wed Apr 15 2009 Dave Airlie <airlied@redhat.com>
- drm-modesetting-radeon: fix rs690 video (#492685) + add bandwidth calcs

* Thu Apr 14 2009 Marcelo Tosatti <mtosatti@redhat.com>
- kvm fixes for bz#491625

* Tue Apr 14 2009 Jarod Wilson <jarod@redhat.com>
- Make squashfs behave on systems where pagesize > blocksize (Doug Chapman)

* Tue Apr 14 2009 Chuck Ebbert <cebbert@redhat.com>
- Add missing patch for broken RLIMIT_CPU

* Tue Apr 14 2009 Chuck Ebbert <cebbert@redhat.com>
- Fix warnings/errors in USB cdc-acm modem driver (#495446)

* Tue Apr 14 2009 Chuck Ebbert <cebbert@redhat.com>
- Timer fixes headed for -stable

* Tue Apr 14 2009 Chuck Ebbert <cebbert@redhat.com>
- Fix duplicated flag value in pagemap.h (-stable patch)

* Tue Apr 14 2009 Chuck Ebbert <cebbert@redhat.com>
- acer-wmi: use upstream code to blacklist an additional model
- Trivial fix to drm-modesetting-radeon to fix failure to apply

* Tue Apr 14 2009 Dave Airlie <airlied@redhat.com> 2.6.29.1-73
- drm-modesetting-radeon.patch: more bug fixes

* Mon Apr 13 2009 Chuck Ebbert <cebbert@redhat.com>
- Fix oops in md raid1 resync (#495550)

* Mon Apr 13 2009 Eric Sandeen <sandeen@redhat.com>
- Turn of CONFIG_SND_HDA_POWER_SAVE_DEFAULT again (#493972)

* Mon Apr 13 2009 Kyle McMartin <kyle@redhat.com> 2.6.29.1-70
- merge alsa fixes from wwoods:
  alsa-hda-dont-reset-BDL-unnecessarily.patch
  alsa-dont-reset-stream-at-each-prepare-callb.patch
  alsa-hda_intel-fix-unexpected-ring-buffer-positio.patch
  alsa-pcm-midlevel-add-more-strict-buffer-position.patch
    bbf6ad13, fa00e046 from alsa-kernel/master (and deps)

* Mon Apr 13 2009 John W. Linville <linville@redhat.com>
- Remove "iwl3945: rely on priv->lock to protect priv access" (#495003)

* Fri Apr 10 2009 David Woodhouse <David.Woodhouse@intel.com>
- Fix suspend/resume with Intel IOMMU, handle devices behind PCI-PCI
  bridges, cope with BIOS claiming IOMMU is at address zero.

* Thu Apr 09 2009 Chuck Ebbert <cebbert@redhat.com>
- Only print ext4 allocator fallback warning once.

* Thu Apr 09 2009 Dennis Gilmore <dennis@ausil.us> 2.6.29.1-59
- add patch to fix regression on sparc

* Thu Apr 09 2009 Adam Jackson <ajax@redhat.com>
- drm-intel-gen3-fb-hack.patch: Allow up to 4k framebuffers on 9[14]5.  3D
  will be broken if you do that, but at least dualhead will be less broken.

* Thu Apr 09 2009 Dave Jones <davej@redhat.com>
- Bring back the /dev/crash driver. (#492803)

* Thu Apr 09 2009 Dave Airlie <airlied@redhat.com>
- radeon: fix some kms bugs, dac detect + screen resize

* Wed Apr 08 2009 Adam Jackson <ajax@redhat.com>
- Drop the PAT patch, sufficiently upstreamed now.

* Wed Apr 08 2009 Dave Jones <davej@redhat.com> 2.6.29.1-58
- disable MMIOTRACE in non-debug builds (#494584)

* Wed Apr 08 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: nv50 kms fixes (PROM access, i2c, clean some warnings)

* Tue Apr 07 2009 Kyle McMartin <kyle@redhat.com>
- linux-2.6-v4l-dvb-fix-uint16_t-audio-h.patch (#493053)

* Tue Apr 07 2009 Dave Jones <davej@redhat.com>
- Enable CONFIG_CIFS_STATS (#494545)

* Tue Apr 07 2009 Dave Airlie <airlied@redhat.com>
- drm-modesetting-radeon: repair vt switch

* Mon Apr 06 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: rebase on drm-f11

* Mon Apr 06 2009 Dave Airlie <airlied@redhat.com>
- radeon: bust APIs and move to what we want in the end.

* Sun Apr 05 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: big update.  mostly cleanups, few functional changes
    Big code cleanup (the scary looking, but hopefully harmless part)
    Now using "full-featured" VBIOS parser from DDX
    Remove custom i2c code in favour of i2c_algo_bit
    Refuse to suspend, we can't possibly resume just yet
    Fix ramht insertions when a collision happens (rh#492427)
    No kms warning on pre-nv50 when kms not acutally enabled (rh#493222)

* Sat Apr 04 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-add-qcserial.patch: Add the qcserial driver for Qualcomm modems

* Fri Apr 03 2009 Jarod Wilson <jarod@redhat.com>
- Don't set up non-existent LVDS on systems with mobile Intel graphics chips
  that lie about having LVDS (like my Dell Studio Hybrid). Makes plymouth
  graphical boot function properly.
- Don't let acer-wmi do stupid things on unsupported systems (like, create
  a bogus rfkill entry in sysfs that effectively neuters wireless in
  NetworkManager on the Aspire One)

* Fri Apr 03 2009 Chuck Ebbert <cebbert@redhat.com>
- x86 E820 fixes from 2.6.30

* Fri Apr 03 2009 Dave Jones <davej@redhat.com>
- x86/dma: unify definition of pci_unmap_addr* and pci_unmap_len macros

* Thu Apr 02 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29.1-46
- Enable debug builds and turn of debugging in the regular kernel.
- Remove dma-debug patches.
- Leave CONFIG_PCI_MSI_DEFAULT_ON set.

* Thu Apr 02 2009 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.29.1
- Removed upstream commit d64260d58865004c6354e024da3450fdd607ea07
  from v4l-dvb-fixes: merged in 2.6.29.1

* Thu Apr 02 2009 John W. Linville <linville@redhat.com>
- iwl3945: rely on priv->lock to protect priv access

* Thu Apr 02 2009 John W. Linville <linville@redhat.com>
- back-port iwlwifi rfkill while device down patches

* Thu Apr 02 2009 Josef Bacik <josef@toxicpanda.com>
- linux-2.6-btrfs-fix-umount-hang.patch: fix hang on umount

* Wed Apr 01 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-shut-up-efifb.patch: avoid efifb errors on unsupported hardware

* Wed Apr 01 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-acpi-video-didl-intel-outputs.patch: fix enabling of asle

* Wed Apr 01 2009 Dave Airlie <airlied@redhat.com>
- drm-radeon-reorder-bm.patch: attempt PM fix for PCI/AGP cards

* Tue Mar 31 2009 Matthew Garrett <mjg@redhat.com> 2.6.29.1-35.rc1
- linux-2.6.29-alsa-update-quirks.patch: Backport some HDA quirk support

* Tue Mar 31 2009 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.29.1-rc1
- Dropped patches, merged upstream:
    linux-2.6-net-fix-gro-bug.patch
    linux-2.6-net-xfrm-fix-spin-unlock.patch
    linux-2.6.29-pat-change-is_linear_pfn_mapping-to-not-use-vm_pgoff.patch
    linux-2.6.29-pat-pci-change-prot-for-inherit.patch

* Tue Mar 31 2009 Eric Sandeen <sandeen@redhat.com>
- add fiemap.h to kernel-headers
- build ext4 (and jbd2 and crc16) into the kernel

* Tue Mar 31 2009 Matthew Garrett <mjg@redhat.com>
- update the sony laptop code

* Tue Mar 31 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: support version 3.0 pll limits table
  may help with rh#492575

* Mon Mar 30 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-input-wacom-bluetooth.patch: Add support for Bluetooth wacom pads

* Mon Mar 30 2009 Chuck Ebbert <cebbert@redhat.com>
- Make the .shared-srctree file a list so more than two checkouts
  can share source files.

* Mon Mar 30 2009 Chuck Ebbert <cebbert@redhat.com>
- Separate PAT fixes that are headed for -stable from our out-of-tree ones.

* Mon Mar 30 2009 Dave Jones <davej@redhat.com>
- Make io schedulers selectable at boot time again. (#492817)

* Mon Mar 30 2009 Dave Jones <davej@redhat.com>
- Add a strict-devmem=0 boot argument (#492803)

* Mon Mar 30 2009 Adam Jackson <ajax@redhat.com>
- linux-2.6.29-pat-fixes.patch: Fix PAT/GTT interaction

* Mon Mar 30 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- some fixes of troubles caused by v4l2 subdev conversion

* Mon Mar 30 2009 Mark McLoughlin <markmc@redhat.com> 2.6.29-21
- Fix guest->remote network stall with virtio/GSO (#490266)

* Mon Mar 30 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch
  - rewrite nouveau PCI(E) GART functions, should fix rh#492492
  - kms: kernel option to allow dual-link dvi
  - modinfo descriptions for module parameters

* Sun Mar 29 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- more v4l/dvb updates: v4l subdev conversion and some driver improvements

* Sun Mar 29 2009 Chuck Ebbert <cebbert@redhat.com>
- More fixes for ALSA hardware pointer updating.

* Sat Mar 28 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- linux-2.6-revert-dvb-net-kabi-change.patch: attempt to fix dvb net breakage
- update v4l fixes patch to reflect what's ready for 2.6.30
- update v4l devel patch to reflect what will be kept on linux-next for a while

* Fri Mar 27 2009 Alexandre Oliva <lxoliva@fsfla.org> -libre.16 Mon Mar 30
- Deblobbed 2.6.29.
- Deblobbed drm-nouveau.patch.
- Deblobbed drm-next.patch.
- Deblobbed drm-modesetting-radeon.patch.
- Deblobbed linux-2.6-at76.patch.
- Deblobbed linux-2.6.27-lirc.patch.
- Deblobbed linux-2.6.29-lirc.patch.
- Deblobbed linux-2.6-v4l-dvb-experimental.patch
- Deblobbed linux-2.6-v4l-dvb-fixes.patch
- Updated URL, thanks to Tomek Chrzczonowicz.

* Fri Mar 27 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-16
- Fix 2.6.29 networking lockups.
- Fix locking in net/xfrm/xfrm_state.c (#489764)

* Fri Mar 27 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: do nothing for dac_{prepare,commit}, it's useless
  and breaks some things in strange ways.

* Fri Mar 27 2009 Ben Skeggs <bskeggs@redhat.com>
- nv50: clear 0x1900/8 on init, possible fix for rh#492240
- forcibly disable GEM also if KMS requested where not supported
- inform the user if we disable KMS because of it not being supported

* Thu Mar 26 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-relatime-by-default.patch: Backport relatime code from 2.6.30

* Thu Mar 26 2009 Dave Jones <davej@redhat.com>
- Check for modesetting enabled before forcing mode on 915. (#490336)

* Thu Mar 26 2009 Dave Jones <davej@redhat.com>
- Set kernel-PAE as default in grub. (#487578)

* Thu Mar 26 2009 Dave Jones <davej@redhat.com>
- Enable CONFIG_MOUSE_PS2_ELANTECH (#492163)

* Thu Mar 26 2009 Kyle McMartin <kyle@redhat.com>
- linux-2.6-v4l-pvrusb2-fixes.patch: fix build for uncle steve.

* Thu Mar 26 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- Move all 2.6.30 stuff into linux-2.6-v4l-dvb-fixes.patch, in
  preparation for upstream pull;
- Added two new drivers: gspca sq905c and DVB Intel ce6230
- Updated to the latest v4l-dvb drivers.

* Wed Mar 25 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- remove duplicated Cinergy T2 entry at config-generic

* Wed Mar 25 2009 Neil Horman <nhorman@redhat.com>
- Add dropmonitor/dropwatch protocol from 2.6.30

* Wed Mar 25 2009 Kyle McMartin <kyle@redhat.com>
- alsa-rewrite-hw_ptr-updaters.patch: snd_pcm_update_hw_ptr() tries to
  detect the unexpected hwptr jumps more strictly to avoid the position
  mess-up, which often results in the bad quality I/O with pulseaudio.

* Wed Mar 25 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: idle channels better before destroying them

* Tue Mar 24 2009 Kyle McMartin <kyle@redhat.com>
- Disable DMAR by default until suspend & resume is fixed.

* Tue Mar 24 2009 Josef Bacik <josef@toxicpanda.com>
- fsync replay fixes for btrfs

* Mon Mar 23 2009 Dave Jones <davej@redhat.com>
- 2.6.29

* Mon Mar 23 2009 Mauro Carvalho Chehab <mchehab@rehat.com>
- Some fixes on drivers/media
- Removed inexistent drivers/media items from config-generic
- Cinergy T2 option were renamed. Use the current syntax

* Mon Mar 23 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-sony-laptop-rfkill.patch
   Update to support hotkeys and rfkill switch

* Mon Mar 23 2009 Bill Nottingham <notting@redhat.com>
- build ide-gd_mod in on PPC (#491380)

* Mon Mar 23 2009 Mark McLoughlin <markmc@redhat.com>
- Add /sys/bus/pci/devices/*/remove_id for KVM (#487103)

* Mon Mar 23 2009 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.29-rc8-git6

* Mon Mar 23 2009 Roland McGrath <roland@redhat.com>
- utrace update, fixes /proc/pid/status format (#491508)

* Mon Mar 23 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: fix GEM object leak, and display shutdown issue

* Fri Mar 20 2009 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.29-rc8-git5

* Thu Mar 19 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- Removed v4l-build-fixes.patch

* Thu Mar 19 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- update v4l-dvb to reflect changes at linux-next

* Thu Mar 19 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-acpi-video-didl-intel-outputs.patch
   don't attempt to re-register the backlight device on resume
- linux-2.6-sony-laptop-rfkill.patch
   provide rfkill control on current vaios

* Thu Mar 19 2009 Dave Jones <davej@redhat.com>
- Switch x86-32 back to using 8k stacks.

* Thu Mar 19 2009 Chuck Ebbert <cebbert@redhat.com>
- Enable the sfc 10GbE network driver.

* Thu Mar 19 2009 Kyle McMartin <kyle@redhat.com>
- dma-api debug fixes for e1000 and e1000e from tip.
- fix dma leak in tulip request_irq error path.

* Thu Mar 19 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.267.rc8.git4
- build fixes for v4l tree.

* Thu Mar 19 2009 Roland McGrath <roland@redhat.com> 2.6.29-0.266.rc8.git4
- utrace update, add ftrace process-tracer widget, drop utrace-ptrace

* Thu Mar 19 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: kms fixes and cleanups

* Thu Mar 19 2009 Chuck Ebbert <cebbert@redhat.com>
- 2.6.29-rc8-git4
- Dropped patches, merged upstream:
    linux-2.6-ext4-extent-header-check-fix.patch
    linux-2.6-ext4-print-warning-once.patch
    linux-2.6-net-velocity-dma.patch

* Wed Mar 18 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- merge hdpvr patch into v4l-dvb updates
- update v4l-dvb to reflect changes at linux-next

* Wed Mar 18 2009 Jarod Wilson <jarod@redhat.com>
- Update hdpvr patch to version targeted for v4l-dvb merge
- Re-sort patches to add hdpvr after v4l-dvb updates

* Wed Mar 18 2009 Dave Airlie <airlied@redhat.com>
- drm-next.patch: fix rs600 GART setup
- drm-modesetting-radeon.patch: allocator fixups

* Wed Mar 18 2009 Ben Skeggs <bskeggs@redhat.com>
- enable CONFIG_MMIOTRACE on x86/x86_64

* Tue Mar 17 2009 Kyle McMartin <kyle@redhat.com>
- increase MAX_LOCKDEP_ENTRIES to 10240.

* Mon Mar 16 2009 Josef Bacik <josef@toxicpanda.com> 2.6.29-0.258.rc8.git2
- update btrfs code so it doesn't pop the stack on x86

* Mon Mar 16 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.255.rc8.git2
- 2.6.29-rc8-git2

* Sun Mar 15 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-0.254.rc8.git1
- 2.6.29-rc8-git1
- Entire v4l-dvb-fixes patch was merged upstream.

* Fri Mar 13 2009 Mauro Carvalho Chehab <mchehab@redhat.com
- v4l-dvb patches: better conflict solving with hdpvr, more fixes and updates

* Fri Mar 13 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-0.252.rc8
- Quiet down an ext4 warning message.

* Fri Mar 13 2009 Dave Jones <davej@redhat.com> 2.6.29-0.250.rc8
- Fix DMA leak in Velocity TX path

* Fri Mar 13 2009 Mauro Carvalho Chehab <mchehab@redhat.com
- v4l-dvb patches: Add experimental cx231xx, plus more fixes and updates

* Fri Mar 13 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: support needed for multiple xservers

* Fri Mar 13 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.247.rc8
- Linux 2.6.29-rc8
- squashfs-fix-page-aligned-data.patch: upstream-ish
- unifdef-rename-getline-symbol.patch: upstream-ish

* Thu Mar 12 2009 Dave Airlie <airlied@redhat.com>
- drm-next.patch: r600 fixes for suspend/resume
- drm-modesetting-radeon.patch: rebase on drm-next

* Thu Mar 12 2009 Eric Sandeen <sandeen@redhat.com>
- Add patches for ext4 flush behavior in some situations
  (Safer for apps which aren't fsyncing properly)

* Thu Mar 12 2009 Jarod Wilson <jarod@redhat.com>
- Updated lirc patch to kill a slew of compile warnings and
  make lirc_serial behave properly w/kfifos

* Thu Mar 12 2009 Dave Jones <davej@redhat.com>
- Drop no longer needed 'crash' driver.

* Thu Mar 12 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: kms display lockup fixes for a heap of chipsets

* Thu Mar 12 2009 Dave Airlie <airlied@redhat.com>
- radeon kms - bug fix irq and ib handling on r300 + suspend test hook
- fix sparc build with kms from spot

* Wed Mar 11 2009 Chuck Ebbert <cebbert@redhat.com>
- 2.6.29-rc7-git5
- Dropped ingo-fix-atom-failures.patch, merged upstream.

* Wed Mar 11 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-acpi-video-didl-intel-outputs.patch
   Initialise the DIDL field in Intel opregion before bringing up ACPI video
- linux-2.6-acpi-video-dos.patch
   Reenable display switch support now that above patch is included

* Wed Mar 11 2009 Kristian Høgsberg <krh@redhat.com>
- Add patch to issue a wbinvd in the GEM execbuffer patch to work
  around insufficient flushing on i855 and i865 chipsets.

* Wed Mar 11 2009 Steve Dickson <steved@redhat.com> 2.6.29-0.236.rc7.git4
- Removed both the lockd-svc-register.patch and
  sunrpc-ipv6-rpcbind.patch patches in favor of simply turning
  off CONFIG_SUNRPC_REGISTER_V4 config variable.

* Wed Mar 11 2009 Eric Sandeen <sandeen@redhat.com> 2.6.29-0.235.rc7.git4
- Fix incorrect header check values in ext4_ext_search_right()
  Should address kernel.org bugzilla #12821

* Wed Mar 11 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.234.rc7.git4
- ingo-fix-atom-failures.patch:
   Work around Atom errata when splitting large pages.

* Wed Mar 11 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.233.rc7.git4
- linux-2.6-execshield.patch:
   Fix typo. Oops.

* Wed Mar 11 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.232.rc7.git4
- linux-2.6-execshield.patch:
   Fix from H.J. Lu, we were doing 32-bit randomization on 64-bit vaddr
- unifdef-rename-getline-symbol.patch:
   glibc 2.9.90-10 changes what symbols are exposed in stdio.h, causing
   getline collision. rename the unifdef symbol to parseline.

* Wed Mar 11 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc7-git4

* Tue Mar 10 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.230.rc7.git3
- Add -git3 to sources...

* Tue Mar 10 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.229.rc7.git3
- squashfs-fix-page-aligned-data.patch:
   Theoretically valid images are hitting the if (page == pages) check,
   which is suspect happens when the data is page aligned... We should
   probably be checking for page+1.

* Tue Mar 10 2009 Tom "spot" Callaway <tcallawa@redhat.com>
- disable CONFIG_AGP on sparc64

* Tue Mar 10 2009 Dave Jones <davej@redhat.com> 2.6.29-0.227.rc7.git3
- 2.6.29-rc7-git3

* Tue Mar 10 2009 Dave Jones <davej@redhat.com>
- HID: add support for another version of 0e8f:0003 device in hid-pl

* Tue Mar 10 2009 Mauro Carvalho Cheahb <mchehab@redhat.com>
- updates V4L/DVB from linux-next and adds git log summary on patches

* Tue Mar 10 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: more kms fixes/cleanups
- now works decently on all the G8x chips I have

* Tue Mar 10 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: another 8800GTX kms fix

* Tue Mar 10 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: fix kms issues seen while testing original 8800gtx

* Tue Mar 10 2009 Dave Airlie <airlied@redhat.com>
- radeon: enable kms on r100/r200

* Tue Mar 10 2009 Tom "spot" Callaway <tcallawa@redhat.com>
- linux-2.6.29-sparc-IOC_TYPECHECK.patch: missing function in sparc specific ioctl.h

* Tue Mar 10 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: fix ppc

* Mon Mar 09 2009 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.29-rc7-git2

* Mon Mar 09 2009 Dave Airlie <airlied@redhat.com>
- drm-next.patch: fix r600 writeback test

* Mon Mar 09 2009 Ben Skeggs <bskeggs@gmail.com>
- drm-nouveau.patch: nv50 and kms fixes

* Mon Mar 09 2009 Dave Airlie <airlied@redhat.com>
- drm-next.patch: fixes from upstream queue for r600 support
- drm-modesetting-radeon.patch: make 2D/3D on PCIE faster

* Fri Mar 06 2009 Jarod Wilson <jarod@redhat.com>
- add atom to p4-clockmod for thermal mgmt benefits for the atom
  procs that don't do freq scaling (lookin' at you, atom 330)

* Fri Mar 06 2009 Jarod Wilson <jarod@redhat.com>
- kernel-devel also needs include/crypto headers

* Fri Mar 06 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- drivers/media: remove frontends from config-generic

* Fri Mar 06 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- drivers/media: Kconfig cleanups and fixes

* Fri Mar 06 2009 Dave Jones <davej@redhat.com>
- kernel-devel needs include/trace headers. (Josh Stone <jistone@redhat.com>)

* Fri Mar 06 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- drivers/media: Some fixes and a cleanup on F11 v4l/dvb config.generic

* Fri Mar 06 2009 Matthew Garrett <mjg@redhat.com>
- linux-2.6-input-fix-toshiba-hotkeys.patch: Avoid polling for hotkey events
  on Toshibas

* Thu Mar 05 2009 Dave Airlie <airlied@redhat.com>
- drm-radeon-modesetting.patch: add new relocation for Xv sync

* Thu Mar 05 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: fix some issues mainly seen on earlier chipsets

* Thu Mar 05 2009 Eric Sandeen <sandeen@redhat.com>
- Fix ext4 race between inode bitmap set/clear

* Thu Mar 05 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: fix <nv50 chipsets, and ppc.

* Wed Mar  4 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- drivers/media: fixes and improvements on devel tree

* Wed Mar  4 2009  <krh@redhat.com>
- Add fix for deadlock in drm setmaster.

* Wed Mar 04 2009 Kyle McMartin <kyle@redhat.com>
- Rebase git-bluetooth.patch from bluetooth-next-2.6 instead of
  bluetooth-testing, per Marcel's suggestions.

* Wed Mar 04 2009 Ben Skeggs <bskeggs@redhat.com>
- drm-nouveau.patch: lvds, dpms and gamma_set fixes

* Wed Mar 04 2009 Dave Jones <davej@redhat.com> 2.6.29-0.197.rc7
- 2.6.29-rc7

* Wed Mar 04 2009 Dennis Gilmore <dennis@ausil.us> - 2.6.29-0.195.rc6.git7
- sparc kernel is now in arch/sparc/boot/image

* Wed Mar 04 2009 Ben Skeggs <bskeggs@redhat.com>
- drm: fix bustage of _DRM_SHM maps

* Wed Mar 04 2009 Kyle McMartin <kyle@redhat.com>
- Tony Breeds says CONFIG_GEF_SBC610 breaks USB. I, for one, believe
  him. Looks to be fairly useless anyway... (#486511)

* Wed Mar 04 2009 Dave Airlie <airlied@redhat.com>
- rebase drm properly kill -pm it breaks my laptop

* Wed Mar 04 2009 Dave Airlie <airlied@redhat.com> 2.6.29-0.185.rc6.git7
- rebase drm patches on git7

* Tue Mar 03 2009 Dave Jones <davej@redhat.com> 2.6.29-0.184.rc6.git7
- 2.6.29-rc6-git7

* Tue Mar 03 2009 Matthew Garrett <mjg@redhat.com>
- Add dynamic power management for later r500-based Radeons

* Tue Mar 03 2009 Kyle McMartin <kyle@redhat.com>
- Allocate a bigger default DMA buffer for snd-hda_intel. Azalia uses a
  32-bit or wider DMA mask, so this is fine.

* Tue Mar 03 2009 Kyle McMartin <kyle@redhat.com>
- Add bluetooth-testing git tree. Mostly bugfixes...

* Tue Mar 03 2009 Dave Airlie <airlied@redhat.com>
- pull patch from Linus to fix build

* Tue Mar 03 2009 Ben Skeggs <bskeggs@redhat.com>
- nouveau: some gf8/9 and kms fixes

* Tue Mar 03 2009 Dave Airlie <airlied@redhat.com> 2.6.29-0.184.rc6.git6
- drm-modesetting-radeon.patch: fix suspend/resume, proc->debugfs

* Mon Mar 02 2009 Dave Jones <davej@redhat.com> 2.6.29-0.183.rc6.git6
- 2.6.29-rc6-git6

* Mon Mar 02 2009 Ben Skeggs <bskeggs@redhat.com>
- CONFIG_DRM_NOUVEAU_KMS got enabled somewhere, lets not do that yet

* Mon Mar 02 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.179.rc6.git5
- fix oops in ipv6 when NET_NS is enabled.

* Mon Mar 02 2009 Ben Skeggs <bskeggs@redhat.com>
- nouveau fixes for recent drm-next changes, and various kms issues

* Sat Feb 28 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-0.176.rc6.git5
- Linux 2.6.29-rc6-git5
- Removed parts of v4l-fixes patch that were merged upstream.
- Added hotfix from lkml to export symbols needed by i915.
- New driver: CONFIG_W1_SLAVE_DS2431=m

* Fri Feb 27 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- Update V4L/DVB to the latest development tree (fixes and updates for linux-next)

* Fri Feb 27 2009 Jarod Wilson <jarod@redhat.com>
- minor lirc and hdpvr patch updates from respective upstreams

* Fri Feb 27 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.172.rc6.git4
- avoid a 64-bit divide which generates a libgcc __udivdi3 call
  in drm-nouveau. maybe.

* Fri Feb 27 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.171.rc6.git4
- fixt drm-nouveau for drm-next.

* Fri Feb 27 2009 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.29-rc6-git4

* Fri Feb 27 2009 Ben Skeggs <bskeggs@redhat.com>
- nouveau/kms: hopefully fix dac outputs

* Fri Feb 27 2009 Dave Airlie <airlied@redhat.com>
- radeon: disable r600 kms by default - we want accel to work

* Fri Feb 27 2009 Ben Skeggs <bskeggs@redhat.com>
- Bring in nouveau update
  - "getting there" nv50 modesetting rework
  - nv50 stability improvements
  - support for >512MiB VRAM on nv50
  - nv40/nv50 backlight support

* Fri Feb 27 2009 Dave Airlie <airlied@redhat.com>
- agp-set_memory_ucwb.patch - enable GEM/KMS on PAE hopefully

* Fri Feb 27 2009 Dave Airlie <airlied@redhat.com>
- drm-next.patch: bring in drm-next tree for r600 support
- rebased drm-modesetting-radeon.patch and drm-nouveau.patch

* Thu Feb 26 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-0.163.rc6.git3
- Add support for sharing source trees between CVS checkouts.

* Thu Feb 26 2009 Dave Jones <davej@redhat.com> 2.6.29-0.162.rc6.git3
- Own the /usr/src/kernels dir in devel packages.

* Thu Feb 26 2009 Kyle McMartin <kyle@redhat.com>
- myoung points out we're not returning a value in the fbcon quiet patch.

* Thu Feb 26 2009 John W. Linville <linville@redhat.com>
- Add dcbw's back-port patches to age scan results on resume

* Thu Feb 26 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.159.rc6.git3
- Update to -git3.
- drm-edid-revision-0-should-be-valid.patch: merged upstream
- linux-2.6-iwlwifi-dma-direction.patch: merged upstream
- CONFIG_DMAR_DEFAULT_ON: enable on ia64

* Wed Feb 25 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-0.158.rc6.git2
- re-add modules.modesetting list.

* Wed Feb 25 2009 Jeremy Katz <katzj@redhat.com> 2.6.29-0.157.rc6.git2
- add dcbw's patch to fix wireless problems on the OLPC (from cjb, should
   be upstream "soon" but is currently blocking OLPC testing)

* Wed Feb 25 2009 Kyle McMartin <kyle@redhat.com>
- Disable CONFIG_DRM_RADEON_KMS on powerpc, needs work to support
  big endian.

* Wed Feb 25 2009 Dave Airlie <airlied@redhat.com>
- rebase drm-modesetting-radeon with fixes for TTM + DRI2

* Tue Feb 24 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.153.rc6.git2
- Update to -git2 snap.
- linux-2.6-ext4-ENOSPC-debug.patch: merged upstream (more or less)
- drm-intel-next.patch: merged upstream

* Tue Feb 24 2009 Mauro Carvalho Chehab <mchehab@redhat.com>
- Updated drivers/media to reflect the latest patches present on linux-next.

* Tue Feb 24 2009 Kyle McMartin <kyle@redhat.com>
- drm-edid-revision-0-should-be-valid.patch: bz476735, allow edid
  read on edid 1.0 monitors.

* Tue Feb 24 2009 Kyle McMartin <kyle@redhat.com>
- linux-2.6-debug-dma-api.patch: rebase to v3, had to hack it myself in
  places, so if it breaks, i get to keep all million pieces.

* Tue Feb 24 2009 Kristian Høgsberg <krh@redhat.com>
- Pull in new round of intel KMS fixes.

* Tue Feb 24 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.147.rc6
- Build the kernel (and modules via Kbuild) with -fno-dwarf2-cfi-asm.
  Should resolve bz#486545. Thanks to aoliva@ for pointing out this way
  to fix ppc64.

* Mon Feb 23 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-0.146.rc6
- Enable MTRR sanitizer by default.

* Mon Feb 23 2009 Kyle McMartin <kyle@redhat.com> 2.6.29-0.145.rc6
- Linux 2.6.29-rc6
- drm-intel-suspend.patch: drop, merged upstream
- selinux-netlabel_setsockopt_fix.patch: drop, merged upstream

* Mon Feb 23 2009 Kyle McMartin <kyle@redhat.com>
- regexp-ing up with_doc turns out to be fail. define a new var
  rawhide_skip_docs to 1 when we don't want to build them. tested with
  upcoming patch-2.6.29-rc6.bz2...

* Mon Feb 23 2009 Kyle McMartin <kyle@redhat.com>
- Build docs once per -rc rebase and when 'make release.'

* Sun Feb 22 2009 Kyle McMartin <kyle@redhat.com>
- Add patch from Paul Moore to fix setsockopt when netlabel is in use (ie:
   when selinux is enabled.) resolves bz#486225.

* Sun Feb 22 2009 Dave Airlie <airlied@redhat.com> 2.6.29-0.140.rc5.git5
- rebase drm bits
- temp disable drm-intel-next need krh to rebase - bump gitrev to 5

* Sat Feb 21 2009 Chuck Ebbert <cebbert@redhat.com>  2.6.29-0.139.rc5.git5
- 2.6.29-rc5-git5
- Temporarily disable drm-modesetting-radeon.

* Fri Feb 20 2009 John W. Linville <linville@redhat.com> 2.6.29-0.137.rc5.git4
- Drop CONFIG_WIRELESS_OLD_REGULATORY
- Add Conflicts for wireless-tools < 29-3 (used to pull-in crda)

* Thu Feb 19 2009 Dave Jones <davej@redhat.com> 2.6.29-0.136.rc5.git4
- 2.6.29-rc5-git4

* Thu Feb 19 2009 Eric Sandeen <sandeen@redhat.com>
- Enable CONFIG_STACK_TRACER

* Thu Feb 19 2009 Dave Jones <davej@redhat.com> 2.6.29-0.131.rc5.git3
- 2.6.29-rc5-git3

* Thu Feb 19 2009 Dave Airlie <airlied@redhat.com>
- radeon: add module device table

* Wed Feb 18 2009 Kyle McMartin <kyle@redhat.com>
- i915_suspend: force reset of mode on resume

* Wed Feb 18 2009 Eric Sandeen <sandeen@redhat.com> 2.6.29-0.131.rc5.git2
- Drop noisy printk from ext4 ENOSPC patch; for now it's a "fix"

* Wed Feb 18 2009 Kristian Høgsberg <krh@redhat.com>
- Add drm-intel-suspend.patch to fix suspend/resume with intel KMS.

* Wed Feb 18 2009 Chuck Ebbert <cebbert@redhat.com> 2.6.29-0.128.rc5.git2
- 2.6.29-rc5-git2

* Wed Feb 18 2009 Neil Horman <nhorman@redhat.com>
- Update config to turn on SHA1 HMACS in SCTP (bz 485933)

* Tue Feb 17 2009 Adam Jackson <ajax@redhat.com>
- Death to intelfb

* Mon Feb 16 2009 Dennis Gilmore <dennis@ausil.us> 2.6.29-0.122.rc5
- build kernel-headers on i586

* Mon Feb 16 2009 Eric Sandeen <sandeen@redhat.com>
- Add ext4 debug patch for ENOSPC issue

* Mon Feb 16 2009 Kristian Høgsberg <krh@redhat.com>
- Flip the switch on intel KMS.

* Mon Feb 16 2009 Dave Jones <davej@redhat.com> 2.6.29-0.121.rc5
- Disable CONFIG_SCSI_IPS on ppc/ppc64

* Mon Feb 16 2009 Jeremy Katz <katzj@redhat.com>
- Switch msr and cpuid to be built-in on x86 since there isn't anything to really autoload them (and if there
  was, they'd just end up always being loaded)

* Sat Feb 14 2009 Eric Sandeen <sandeen@redhat.com>
- Fix ext4 hang on livecd-creator (#484522)

* Sat Feb 14 2009 David Woodhouse <David.Woodhouse@intel.com>
- Enable DMAR by default again now that it works

* Fri Feb 13 2009 Dave Jones <davej@redhat.com> 2.6.29-0.117.rc5
- 2.6.29-rc5

* Fri Feb 13 2009 Kristian Høgsberg <krh@redhat.com>
- Update drm-intel-next patch with more modesetting fixes.

* Fri Feb 13 2009 David Woodhouse <David.Woodhouse@intel.com>
- Apply IOMMU write-buffer quirk. (#479996)

* Fri Feb 13 2009 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.29-rc4-git7

* Thu Feb 12 2009 Steve Dickson <steved@redhat.com>
- NFS: lockd fails to load causing mounts to fail

* Thu Feb 12 2009 Hans de Goede <hdegoede@redhat.com>
- Enable AMD Geode scx200 i2c bus controller driver (#485162)

* Wed Feb 11 2009 David Woodhouse <David.Woodhouse@intel.com>
- Fix iwlwifi DMA direction bug.

* Wed Feb 11 2009 John W. Linville <linville@redhat.com>
- Drop linux-2.6-at76.patch in favor of version from drivers/staging (#477927)

* Wed Feb 11 2009 Kristian Høgsberg <krh@redhat.com>
- Rebase drm-intel-next patch.

* Wed Feb 11 2009 Jarod Wilson <jarod@redhat.com> 2.6.29-0.107.rc4.git3
- 2.6.29-rc4-git3

* Wed Feb 11 2009 Ben Skeggs <bskeggs@redhat.com>
- nouveau update: GeForce 9 support, kms fixes improvements

* Tue Feb 10 2009 Jarod Wilson <jarod@redhat.com> 2.6.29-0.105.rc4.git1
- Enhance kernel's bumpspecfile.py to add VR to changelog when it can

* Tue Feb 10 2009 Dave Jones <davej@redhat.com>
- Enable FTRACE & DYNAMIC_FTRACE

* Mon Feb 09 2009 Chuck Ebbert <cebbert@redhat.com>
- Remove our private copy of the atl2 driver (#484458)

* Mon Feb 09 2009 Chuck Ebbert <cebbert@redhat.com>
- Fix type in header so iptables will build. (#484679)

* Mon Feb 09 2009 Kyle McMartin <kyle@redhat.com>
- Enable CONFIG_X86_BIGSMP, to enable support for more than 8 cpus,
  since we have NR_CPUS=32...

* Mon Feb  9 2009 Kristian Høgsberg <krh@redhat.com>
- Pull in more fixes from drm-intel-next.

* Sun Feb 08 2009 Kyle McMartin <kyle@redhat.com>
- drm-no-gem-on-i8xx.patch: disable GEM on i8xx series graphics cards, patch
  was lost at some point during the rebasing. :/

* Sun Feb 08 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc4-git1

* Sun Feb 08 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc4

* Sun Feb 08 2009 Chuck Ebbert <cebbert@redhat.com>
- 2.6.29-rc3-git12

* Sat Feb 07 2009 Chuck Ebbert <cebbert@redhat.com>
- Add not-yet-merged credentials suid exec fix. (#481783)

* Sat Feb 07 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc3-git11

* Fri Feb 06 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc3-git10

* Fri Feb 06 2009 Ben Skeggs <bskeggs@redhat.com>
- major nouveau snapshot update - 0.0.12

* Fri Feb 06 2009 Chuck Ebbert <cebbert@redhat.com>
- 2.6.29-rc3-git9

* Thu Feb 05 2009 Chuck Ebbert <cebbert@redhat.com>
- 2.6.29-rc3-git8

* Thu Feb 05 2009 Chuck Ebbert <cebbert@redhat.com>
- Build in the PCIE hotplug drivers so they load before ACPI hotplug.

* Thu Feb 05 2009 Dave Jones <davej@redhat.com>
- Kill off the 686 kernel.
  Users should now use either the 586 kernel, or 686-PAE.

* Thu Feb 05 2009 David Woodhouse <David.Woodhouse@intel.com>
- Import and enable Jörg's DMA API debugging patches

* Thu Feb 05 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc3-git7

* Wed Feb 04 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc3-git6

* Wed Feb 04 2009 Kyle McMartin <kyle@redhat.com>
- Turn PCI MSI default state into a config option, fribble targets in
  Makefile to sort it out for releases, and turn it on in rawhide.
  Let's watch the fireworks...

* Wed Feb 04 2009 Eric Sandeen <sandeen@redhat.com>
- Re-enable CONFIG_SND_HDA_POWER_SAVE_DEFAULT=5

* Tue Feb 03 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc3-git5

* Tue Feb  3 2009 Kristian Høgsberg <krh@redhat.com>
- Pull in drm-intel-next changes.

* Mon Feb 02 2009 Kyle McMartin <kyle@redhat.com>
- Re-enable CONFIG_DRM_RADEON_KMS.

* Sun Feb 01 2009 Chuck Ebbert <cebbert@redhat.com>
- Fold sata_sil build fix into compile-fixes.patch

* Sat Jan 31 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc3-git3

* Fri Jan 30 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc3-git2

* Fri Jan 30 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc3-git1

* Fri Jan 30 2009 Josef Bacik <josef@toxicpanda.com>
- add patch to keep btrfs from locking up when creating a new inode
- add patch to let readdir not screw up on 32bit boxes

* Fri Jan 30 2009 Hans de Goede <hdegoede@redhat.com>
- Completely drop gspca patches (all upstream now)
- Rebase atk0110 driver to latest from upstream
- Fix some broken error paths in superio hwmon drivers triggered by the
  new acpi_enforce_resources strict default (#483208)

* Thu Jan 29 2009 Chuck Ebbert <cebbert@redhat.com>
- Clean up EEPROM config.
- Disable CONFIG_DEBUG_NOTIFIERS release kernel.

* Thu Jan 29 2009 Chuck Ebbert <cebbert@redhat.com>
- Clean up config files a bit.

* Thu Jan 29 2009 Mark McLoughlin <markmc@redhat.com>
- Temporarily disable kvmclock, fix for upstream in progress (#475598)

* Wed Jan 28 2009 Jarod Wilson <jarod@redhat.com>
- make 2nd-gen ipod work via firewire again, but w/o neutering throughput
- add fix for firewire-sbp2 dma mapping leak in failure paths

* Wed Jan 28 2009 Kyle McMartin <kyle@redhat.com>
- drm-nouveau.patch: /me sighs in the general direction of ppc.

* Wed Jan 28 2009 Kyle McMartin <kyle@redhat.com>
- sata_sil-build-break-fix.patch: fix use of DMI.

* Wed Jan 28 2009 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.29-rc3

* Wed Jan 28 2009 Kyle McMartin <kyle@redhat.com>
- s/swab32/fswab32/ in nouveau

* Wed Jan 28 2009 Mark McLoughlin <markmc@redhat.com>
- Enable CONFIG_PCI_STUB for KVM (#482792)

* Tue Jan 27 2009 Jarod Wilson <jarod@redhat.com>
- Tell hiddev to leave antec-branded imon display/ir devices alone

* Tue Jan 27 2009 Kyle McMartin <kyle@redhat.com>
- Re-enable nouveau.

* Tue Jan 27 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc2-git3

* Mon Jan 26 2009 Jarod Wilson <jarod@redhat.com>
- Update to firewire to latest linux1394-2.6.git tree
  * Adds iso resource allocation code from our own Jay Fenlason
  * Increases AT retry attempts, should fix some cranky dv cams (#449252)
- Update lirc to latest git tree
  * Adds support for several new mceusb2 and imon devices
- Nuke some config cruft from EEPROM symbol renames

* Mon Jan 26 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc2-git2

* Mon Jan 26 2009 Kyle McMartin <kyle@redhat.com>
- Update git-linus.diff to bf50c903faba4ec7686ee8a570ac384b0f20814d.
- drm-next.patch merged.
- linux-2.6.28-sunrpc-ipv6-rpcbind.patch: update for Kconfig moves.

* Sat Jan 24 2009 Hans de Goede <hdegoede@redhat.com>
- Fix atk0110 sensor numbering

* Fri Jan 23 2009 Hans de Goede <hdegoede@redhat.com>
- Change acpi_enforce_resources default to strict, this will cause hwmon
  drivers which clash with io resources reserved by ACPI to no longer load,
  avoiding both the ACPI code and the native driver trying to drive the same
  IC at the same time
- Add ASUS ACPI hwmon interface driver (atk0110), this will give (restore)
  hwmon functionality on most ASUS boards through the firmware

* Fri Jan 23 2009 Dave Jones <davej@redhat.com>
- Make NFS work again.

* Fri Jan 23 2009 Eric Sandeen <sandeen@redhat.com>
- build the crc32c driver into the kernel
- add selinux fixes for btrfs

* Fri Jan 23 2009 Kyle McMartin <kyle@redhat.com>
- disable intel_iommu by default (enable with "intel_iommu=on")

* Thu Jan 22 2009 Dave Airlie <airlied@redhat.com>
- rebase drm patches - nouveau TODO

* Wed Jan 21 2009 John W. Linville <linville@redhat.com>
- rt2x00: back-port activity LED init patches

* Mon Jan 19 2009 Kyle McMartin <kyle@redhat.com>
- execshield fixes: should no longer generate spurious handled GPFs,
  fixes randomization of executables. also some clean ups.

* Sat Jan 17 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc2-git1

* Fri Jan 16 2009 Chuck Ebbert <cebbert@redhat.com>
- 2.6.29-rc2

* Fri Jan 16 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc1-git6
- linux-2.6-net-silence-noisy-printks.patch: merged

* Thu Jan 15 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc1-git5
- Fix module_ref patch, need to test before dereferencing possible
  (as opposed to online) percpu data.

* Thu Jan 15 2009 Kyle McMartin <kyle@redhat.com>
- New version of module_ref patch, uses per_cpu instead.
- Fix linux-2.6-hdpvr.patch up and re-enable.
    Nuke .flush, was useless, convert to v4l2_file_operations, fix ioctl ret
- Fix linux-2.6-debug-taint-vm.patch and re-enable.

* Wed Jan 14 2009 Dave Jones <davej@redhat.com>
- Test RODATA and NX during bootup.

* Tue Jan 13 2009 Kyle McMartin <kyle@redhat.com>
- Stop wasting so much disk space with a static module_ref array.

* Tue Jan 13 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc1-git4
- CONFIG_MFD_PCF50633 fails. disable it for now.

* Tue Jan 13 2009 Kyle McMartin <kyle@redhat.com>
- Enable CONFIG_DMAR (and GFX_WA and FLOPPY_WA) requested by Gerd.

* Tue Jan 13 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc1-git3

* Mon Jan 12 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc1-git2

* Mon Jan 12 2009 Adam Jackson <ajax@redhat.com>
- Define %%fedora_build a different (faster) way.

* Mon Jan 12 2009 Dave Jones <davej@redhat.com>
- 2.6.29-rc1-git1

* Sun Jan 09 2009 Kyle McMartin <kyle@redhat.com>
- Disable CONFIG_MAXSMP on x86_64.
- Fixup build failure in powerpc due to cpumask changes.

* Sun Jan 09 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.29-rc1

* Fri Jan 08 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.28-git14
- Rebased patches:
  linux-2.6-execshield.patch: simple sysctl reject
- Deleted patches:
  linux-2.6-squashfs.patch: upstream!

* Thu Jan 08 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.28-git12
- Revert ihex2fb moving patch, causes noarch target to
  epicly fail.

* Thu Jan 08 2009 Kyle McMartin <kyle@redhat.com>
- Fixup build failures due to warn/info macro removal.
- Revert patch which caused modules to bloat to 1GB of
  (mostly) zeroes.

* Thu Jan 08 2009 Dave Jones <davej@redhat.com>
- 2.6.28-git11

* Wed Jan  7 2009 Roland McGrath <roland@redhat.com>
- utrace update

* Wed Jan 07 2009 Dave Jones <davej@redhat.com>
- Build ACPI_BATTERY in on x86-64, as we do on x86-32

* Wed Jan 07 2009 Dave Jones <davej@redhat.com>
- Build ACPI_AC in on x86-64/ia64, as we do on x86-32

* Wed Jan 07 2009 Dave Jones <davej@redhat.com>
- Fix up broken rebase.

* Wed Jan 07 2009 Jarod Wilson <jarod@redhat.com>
- 2.6.28-git9
- Should resolve broken byteorder.h issues (#478663)

* Tue Jan 06 2009 Kyle McMartin <kyle@redhat.com>
- 2.6.28-git8

* Mon Jan 05 2009 Dave Jones <davej@redhat.com>
- 2.6.28-git7

* Sat Jan 03 2009 Dave Jones <davej@redhat.com>
- 2.6.28-git6

* Fri Jan 02 2009 Dave Jones <davej@redhat.com>
- 2.6.28-git5

* Thu Jan 01 2009 Dave Jones <davej@redhat.com>
- 2.6.28-git4

* Wed Dec 31 2008 Kyle McMartin <kyle@redhat.com>
- Rebase drm-modesetting-radeon.patch

* Wed Dec 31 2008 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.28-git3

* Mon Dec 29 2008 Dave Jones <davej@redhat.com>
- 2.6.28-git2

* Sun Dec 28 2008 Roland McGrath <roland@redhat.com>
- utrace rebase

* Sun Dec 28 2008 Dave Jones <davej@redhat.com>
- 2.6.28-git1
  Drop utrace temporarily.

* Fri Dec 26 2008 Hans de Goede <hdegoede@redhat.com>
- Rebase gspca git patch to latest gspca git
- Re-enable gspca git patch
- Add gscpa-stv06xx (qc-usb replacement) driver from its own git tree

* Thu Dec 25 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.2
- Deblobbed 2.6.28.

* Thu Dec 25 2008 Dave Jones <davej@redhat.com>
- Enable BOOT_TRACER during testing.

* Wed Dec 24 2008 Dave Jones <davej@redhat.com>
- 2.6.28
  Drop gspca-git temporarily.

* Wed Dec 24 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc9-git4

* Mon Dec 22 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc9-git3

* Mon Dec 22 2008 Bill Nottingham <notting@redhat.com>
- Fix linux/serial.h so it can be included from userspace (#476327)

* Sun Dec 21 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.140.rc9.git1
- Deblobbed 2.6.28-rc9.
- Adjusted drm-next.patch.

* Sat Dec 20 2008 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.28-rc9-git1
  Rebased patches:
   linux-2.6-pciehp-update.patch
   drm-next.patch

* Fri Dec 19 2008 Adam Jackson <ajax@redhat.com>
- config-generic: FB_VIRTUAL=m

* Thu Dec 18 2008 Dave Airlie <airlied@redhat.com>
- drm-next.patch/drm-modesetting-radeon.patch - rebase to upstream.
- config-generic: turn of KMS on radeon until we fixup userspace

* Thu Dec 18 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc9

* Thu Dec 18 2008 Jeremy Katz <katzj@redhat.com>
- explicitly prereq /sbin/new-kernel-pkg as opposed to just mkinitrd

* Thu Dec 18 2008 Kyle McMartin <kyle@redhat.com>
- Disable SND_HDA_BEEP by default

* Thu Dec 18 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc8-git6

* Wed Dec 17 2008 Dave Jones <davej@redhat.com>
- Disable PATA_HPT3X3_DMA as per Alan.

* Wed Dec 17 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc8-git5

* Wed Dec 17 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc8-git5

* Wed Dec 17 2008 John W. Linville <linville@redhat.com>
- iwlwifi: use GFP_KERNEL to allocate Rx SKB memory

* Tue Dec 16 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc8-git4

* Mon Dec 15 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc8-git3

* Sat Dec 13 2008 Kyle McMartin <kyle@redhat.com>
- 2.6.28-rc8-git2

* Sat Dec 13 2008 Tom "spot" Callaway <tcallawa@redhat.com>
- Add "scsi_esp_register" to the search terms for modules.block so we pick up sun_esp.ko

* Thu Dec 11 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc8-git1

* Thu Dec 11 2008 Dave Jones <davej@redhat.com>
- Remove noisy message from can network protocol.

* Thu Dec 11 2008 Hans de Goede <hdegoede@redhat.com>
- Add a patch updating the gspca driver to the latest "git" (mercurial
  actually) adding support for ov534 based cams, fixing support for
  spca501, finepix and vc0321 cams + many more bugfixes

* Thu Dec 11 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.124.rc8
- Deblobbed 2.6.28-rc8.

* Wed Dec 10 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc8

* Wed Dec 10 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc7-git8

* Wed Dec 10 2008 Kyle McMartin <kyle@redhat.com>
- 2.6.28-rc7-git8
- re-enable drm-nouveau...

* Tue Dec 09 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc7-git7

* Mon Dec 08 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc7-git6

* Mon Dec 08 2008 Kyle McMartin <kyle@redhat.com>
- properly fix headers in kernel-devel if headers are in arch/$arch/include,
  sadly was not able to kill off %%hdrarch since $Arch is undefined at the
  time we need it for headers install.

* Mon Dec 08 2008 Kyle McMartin <kyle@redhat.com>
- merging, merging, merging...

* Mon Dec 08 2008 Dave Jones <davej@redhat.com>
- enable CONFIG_CGROUP_MEM_RES_CTLR.

* Mon Dec 08 2008 Kyle McMartin <kyle@redhat.com>
- execshield re-merge. xen bits shoved into execshield patch
  (they belong there...)

* Mon Dec 08 2008 Dave Airlie <airlied@redhat.com>
- modesetting: rebase radeon patch

* Fri Dec 05 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc7-git5

* Fri Dec 05 2008 Dave Jones <davej@redhat.com>
- SELinux: check open perms in dentry_open not inode_permission

* Fri Dec 05 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc7-git4

* Thu Dec 04 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.110.rc7.git3
- Deblobbed 2.6.28-rc7.

* Thu Dec 04 2008 Kyle McMartin <kyle@redhat.com>
- linux-2.6-utrace.patch updates

* Thu Dec 04 2008 Kyle McMartin <kyle@redhat.com>
- 2.6.28-rc7-git3

* Tue Dec 02 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc7-git1

* Mon Dec 01 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc7

* Mon Dec 01 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.106.rc6.git4 Dec 03
- Deblobbed 2.6.28-rc6.

* Mon Dec 01 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc6-git4

* Sun Nov 30 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc6-git2

* Mon Nov 24 2008 Dave Jones <davej@redhat.com>
- 2.6.28-rc6-git1

* Mon Nov 24 2008 Jeremy Katz <katzj@redhat.com>
- Add modules.drm file so that we can determine the DRM modules to pull into
  initrds

* Wed Nov 19 2008 Neil Horman <nhorman@redhat.com>
- Enable Garmin gps serial module build (#471824)

* Tue Nov 18 2008 Dave Jones <davej@redhat.com>
- Reenable debugging config options.

* Tue Nov 18 2008 Dave Jones <davej@redhat.com>
- Only build the x86-64 optimised versions of aes/salsa/Twofish on 64bit.

* Tue Nov 18 2008 Dave Jones <davej@redhat.com>
- Disable autofs v3. (obsoleted by v4 some time ago.)

* Mon Nov 17 2008 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.28-rc5

* Fri Nov 14 2008 Roland McGrath <roland@redhat.com>
- x86 tracehook merged upstream
- utrace update (merge conflict only)

* Fri Nov 14 2008 Roland McGrath <roland@redhat.com>
- CONFIG_CORE_DUMP_DEFAULT_ELF_HEADERS=y replaces patch.

* Thu Nov 13 2008 Dave Jones <davej@redhat.com>
- Revert last change.

* Thu Nov 13 2008 Dave Jones <davej@redhat.com>
- Change CONFIG_SECURITY_DEFAULT_MMAP_MIN_ADDR to 4096 on PPC64. (#471478)

* Thu Nov 13 2008 Dave Jones <davej@redhat.com>
- Increase CONFIG_FORCE_MAX_ZONEORDER to 13 on ppc64. (#468982)

* Wed Nov 12 2008 Kyle McMartin <kyle@redhat.com>
- Linux 2.6.28-rc4

* Sun Nov 09 2008 Eric Sandeen <sandeen@redhat.com>
- Pull back ext4 updates from 2.6.28-rc3-git6

* Fri Nov 07 2008 Chuck Ebbert <cebbert@redhat.com>
- Update the r8169 network driver to the 2.6.28 version.

* Fri Nov 07 2008 John W. Linville <linville@redhat.com>
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

* Mon Sep 22 2008 Kristian Høgsberg <krh@redhat.com>
- Fix check for radeon external TMDS.

* Mon Sep 22 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Mon Sep 22 2008 Dave Jones <davej@redhat.com>
- Readd CONFIG_IWLCORE that disappeared for no apparent reason.

* Mon Sep 22 2008 Dave Jones <davej@redhat.com>
- Commit Bills demodularisation patch.

* Mon Sep 22 2008 Kristian Høgsberg <krh@redhat.com>
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

* Thu Sep 18 2008 Kristian Høgsberg <krh@redhat.com>
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

* Fri Aug 29 2008 Kristian Høgsberg <krh@redhat.com>
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

* Fri Aug  8 2008 Kristian Høgsberg <krh@redhat.com>
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

* Thu Jul 17 2008 Kristian Høgsberg <krh@redhat.com>
- Add conflicts for older libdrm-devel to kernel-headers.

* Thu Jul 17 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.156.rc0.git4
- Deblobbed 2.6.26-git4.
- Re-enabled kernel-firmware.

* Wed Jul 16 2008 Dave Jones <davej@redhat.com>
- Merge Linux-2.6 up to commit 8a0ca91e1db5de5eb5b18cfa919d52ff8be375af

* Wed Jul 16 2008 Kristian Høgsberg <krh@redhat.com>
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

* Thu Jul  3 2008 Kristian Høgsberg <krh@redhat.com>
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

###
# The following Emacs magic makes C-c C-e use UTC dates.
# Local Variables:
# rpm-change-log-uses-utc: t
# End:
###
