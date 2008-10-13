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
%define fedora_cvs_origin 727
%define fedora_build %(R="$Revision: 1.795 $"; R="${R%% \$}"; R="${R##: 1.}"; expr $R - %{fedora_cvs_origin})

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 26

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
# kernel-bootwrapper (for creating zImages from kernel + initrd)
%define with_bootwrapper %{?_without_bootwrapper: 0} %{?!_without_bootwrapper: 1}

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

# should we do C=1 builds with sparse
%define with_sparse	%{?_with_sparse:      1} %{?!_with_sparse:      0}

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

%define xen_hv_cset 11633
%define xen_flags verbose=y crash_debug=y
%define xen_target vmlinuz
%define xen_image vmlinuz

%define KVERREL %{PACKAGE_VERSION}-libre.%{PACKAGE_RELEASE}.%{_target_cpu}
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
# they can just use i386 and ppc64 headers
%ifarch i586 i686 ppc64iseries
%define with_headers 0
%endif

# don't build noarch kernels or headers (duh)
%ifarch noarch
%define with_up 0
%define with_headers 0
%define all_arch_configs kernel-%{version}-*.config
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
# we build always xen i686 HV with pae
%define xen_flags verbose=y crash_debug=y pae=y
%define kernel_image arch/x86/boot/bzImage
%endif

%ifarch x86_64
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
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, mkinitrd >= 6.0.39-1

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
Provides: kernel-drm-nouveau = 10\
Provides: kernel-modeset = 1\
Provides: kernel-uname-r = %{KVERREL}%{?1:.%{1}}\
Requires(pre): %{kernel_prereq}\
Conflicts: %{kernel_dot_org_conflicts}\
Conflicts: %{package_conflicts}\
%{?1:%{expand:%%{?kernel_%{1}_conflicts:Conflicts: %%{kernel_%{1}_conflicts}}}}\
%{?1:%{expand:%%{?kernel_%{1}_obsoletes:Obsoletes: %%{kernel_%{1}_obsoletes}}}}\
%{?1:%{expand:%%{?kernel_%{1}_provides:Provides: %%{kernel_%{1}_provides}}}}\
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
%if %{with_doc}
BuildRequires: xmlto
%endif
%if %{with_sparse}
BuildRequires: sparse >= 0.4.1
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

# we always need nonintconfig, even for -vanilla kernels
Patch06: linux-2.6-build-nonintconfig.patch

# we also need compile fixes for -vanilla
Patch07: linux-2.6-compile-fixes.patch
Patch08: linux-2.6-compile-fix-gcc-43.patch

# build tweak for build ID magic, even for -vanilla
Patch05: linux-2.6-makefile-after_link.patch

%if !%{nopatches}

# revert upstream patches we get via other methods
Patch09: linux-2.6-upstream-reverts.patch
Patch10: linux-2.6-hotfixes.patch
# patches queued for the next -stable release
#Patch11: linux-2.6-stable-queue.patch

Patch20: linux-2.6-ptrace-cleanup.patch
Patch21: linux-2.6-tracehook.patch
Patch22: linux-2.6-utrace.patch

Patch41: linux-2.6-sysrq-c.patch
Patch42: linux-2.6-sched-clock-fix-nohz-interaction.patch

Patch70: linux-2.6-x86-tune-generic.patch
Patch75: linux-2.6-x86-debug-boot.patch
Patch87: linux-2.6-x86-apic-dump-all-regs-v3.patch
Patch97: linux-2.6-x86-hpet-04-workaround-sb700-bios.patch
Patch100: linux-2.6-x86-pci-detect-end_bus_number.patch
Patch101: linux-2.6-x86-check-for-null-irq-context.patch
Patch102: linux-2.6-x86-improve-up-kernel-when-cpu-hotplug-and-smp.patch
Patch103: linux-2.6-x86-avoid-dereferencing-beyond-stack-THREAD_SIZE.patch
Patch104: linux-2.6-x86-Reserve-FIRST_DEVICE_VECTOR-in-used_vectors-bit.patch

Patch120: linux-2.6-pci-disable-aspm-per-acpi-fadt-setting.patch
Patch121: linux-2.6-pci-disable-aspm-on-pre-1.1-devices.patch
Patch122: linux-2.6-pci-add-an-option-to-allow-aspm-enabled-forcibly.patch
Patch123: linux-2.6-pci-check-mapped-ranges-on-sysfs-resource-files.patch

# ppc
Patch140: linux-2.6-ps3-ehci-iso.patch
Patch141: linux-2.6-ps3-storage-alias.patch
Patch142: linux-2.6-ps3-legacy-bootloader-hack.patch
Patch143: linux-2.6-g5-therm-shutdown.patch
Patch144: linux-2.6-vio-modalias.patch
Patch147: linux-2.6-imac-transparent-bridge.patch
Patch148: linux-2.6-powerpc-zImage-32MiB.patch
Patch150: linux-2.6-fbdev-teach-offb-about-palette-on-radeon-r500-r600.patch
Patch151: linux-2.6-powerpc-fix-OF-parsing-of-64-bits-pci-addresses.patch

Patch160: linux-2.6-execshield.patch
Patch250: linux-2.6-debug-sizeof-structs.patch
Patch260: linux-2.6-debug-nmi-timeout.patch
Patch270: linux-2.6-debug-taint-vm.patch
Patch280: linux-2.6-debug-spinlock-taint.patch
Patch330: linux-2.6-debug-no-quiet.patch
Patch340: linux-2.6-debug-vm-would-have-oomkilled.patch
Patch370: linux-2.6-crash-driver.patch
Patch380: linux-2.6-defaults-pci_no_msi.patch
Patch400: linux-2.6-scsi-cpqarray-set-master.patch
Patch402: linux-2.6-scsi-mpt-vmware-fix.patch


# filesystem patches
Patch420: linux-2.6-fs-cifs-turn-off-unicode-during-session-establishment.patch
Patch421: linux-2.6-squashfs.patch
Patch422: linux-2.6-fs-cifs-fix-plaintext-authentication.patch
Patch423: linux-2.6-dont-allow-splice-to-files-opened-with-o_append.patch

Patch430: linux-2.6-net-silence-noisy-printks.patch

Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch451: linux-2.6-input-fix_fn_key_on_macbookpro_4_1_and_mb_air.patch
Patch452: linux-2.6-hwmon-applesmc-remove-debugging-messages.patch
Patch460: linux-2.6-serial-460800.patch
Patch510: linux-2.6-silence-noise.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch580: linux-2.6-sparc-selinux-mprotect-checks.patch
Patch610: linux-2.6-defaults-fat-utf8.patch

# libata
Patch670: linux-2.6-ata-quirk.patch
Patch671: linux-2.6-libata-pata_it821x-driver-updates-and-reworking.patch
Patch674: linux-2.6-sata-eeepc-faster.patch
Patch675: linux-2.6-libata-pata_marvell-play-nice-with-ahci.patch
Patch676: linux-2.6-libata-fix-a-large-collection-of-DMA-mode-mismatches.patch
Patch677: linux-2.6-libata-lba-28-48-off-by-one-in-ata.h.patch

Patch680: linux-2.6-wireless.patch
Patch681: linux-2.6-wireless-pending.patch
#Patch682: linux-2.6-wireless-fixups.patch
Patch683: linux-2.6-wireless-stable-backports.patch
Patch685: linux-2.6-wireless-rt2500pci-restoring-missing-line.patch
Patch686: linux-2.6-wireless-p54-fix-regression-due-to-delete-NETDEVICES_MULTIQUEUE-option.patch
Patch687: linux-2.6-wireless-revert-b43-add-RFKILL_STATE_HARD_BLOCKED-support.patch

Patch698: linux-2.6-rt2500usb-fix.patch
Patch699: linux-2.6-at76.patch

Patch700: linux-2.6-nfs-client-mounts-hang.patch

Patch810: linux-2.6-cpuidle-1-do-not-use-poll_idle-unless-user-asks-for-it.patch
Patch820: linux-2.6-cpuidle-2-menu-governor-fix-wrong-usage-of-measured_us.patch
Patch830: linux-2.6-cpuidle-3-make-ladder-governor-honor-latency-requirements.patch

#mm

Patch1101: linux-2.6-default-mmf_dump_elf_headers.patch
Patch1400: linux-2.6-smarter-relatime.patch
Patch1515: linux-2.6-lirc.patch

# nouveau + drm fixes
Patch1801: drm-fedora9-rollup.patch

# kludge to make ich9 e1000 work
Patch2000: linux-2.6-e1000-ich9.patch
Patch2001: linux-2.6-netdev-e1000e-fix-drv-load-issues-amt.patch

# write protect e1000e nvm
Patch2002: linux-2.6-e1000e-write-protect-nvm.patch

# atl2 network driver
Patch2020: linux-2.6-netdev-atl2.patch
Patch2021: linux-2.6-netdev-atl1e.patch

Patch2030: linux-2.6-net-tulip-interrupt.patch

Patch2040: linux-2.6-netdev-e1000e-add-support-for-82567lm-4.patch

# linux1394 git patches
Patch2200: linux-2.6-firewire-git-update.patch

# make USB EHCI driver respect "nousb" parameter
Patch2300: linux-2.6-usb-ehci-hcd-respect-nousb.patch
# uvc video buffer overflow
Patch2301: linux-2.6-uvcvideo-return-sensible-min-max-values.patch
Patch2302: linux-2.6-uvcvideo-dont-use-stack-based-buffers.patch
Patch2303: linux-2.6-uvcvideo-fix-another-buffer-overflow.patch

Patch2501: linux-2.6-ppc-use-libgcc.patch

# get rid of imacfb and make efifb work everywhere it was used
Patch2600: linux-2.6-merge-efifb-imacfb.patch

Patch2700: linux-2.6-intel-msr-backport.patch
Patch2701: linux-2.6-libata-sff-kill-spurious-WARN_ON-in-ata_hsm_move.patch

# for kerneloops reports
Patch2800: linux-2.6-net-print-module-name-as-part-of-the-message.patch
Patch2801: linux-2.6-warn-add-WARN-macro.patch
Patch2802: linux-2.6-warn-Turn-the-netdev-timeout-WARN_ON-into-WARN.patch
Patch2803: linux-2.6-warn-rename-WARN-to-WARNING.patch

# backported version of http://git.kernel.org/?p=linux/kernel/git/davem/sparc-2.6.git;a=commitdiff;h=73ccefab8a6590bb3d5b44c046010139108ab7ca
# needed to build sparc64 kernel
Patch2900: linux-sparc-tracehook-syscall.patch
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

%package bootwrapper
Summary: Boot wrapper files for generating combined kernel + initrd images
Group: Development/System
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
  # Ok, first time we do a make prep.
  rm -f pax_global_header
%setup -q -n kernel-%{kversion} -c
  mv linux-%{kversion} vanilla-%{vanillaversion}
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
  if [ -d linux-%{kversion}.%{_target_cpu} ]; then
     # Just in case we ctrl-c'd a prep already
     rm -rf deleteme.%{_target_cpu}
     # Move away the stale away, and delete in background.
     mv linux-%{kversion}.%{_target_cpu} deleteme.%{_target_cpu}
     rm -rf deleteme.%{_target_cpu} &
  fi
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
  for i in kernel-%{version}-{i586,i686,i686-PAE,x86_64}*.config
  do
    echo i is this file  $i
    mv $i $i.tmp
    ./merge.pl config-rhel-x86-generic $i.tmp > $i
    rm $i.tmp
  done
%endif

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
ApplyPatch linux-2.6-compile-fix-gcc-43.patch

%if !%{nopatches}

ApplyPatch linux-2.6-hotfixes.patch
#ApplyPatch linux-2.6-stable-queue.patch

# revert patches from upstream that conflict or that we get via other means
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-upstream-reverts.patch | awk '{print $1}')
if [ "$C" -gt 10 ]; then
ApplyPatch linux-2.6-upstream-reverts.patch -R
fi

# Roland's utrace ptrace replacement.
ApplyPatch linux-2.6-ptrace-cleanup.patch
ApplyPatch linux-2.6-tracehook.patch
ApplyPatch linux-2.6-utrace.patch

# enable sysrq-c on all kernels, not only kexec
ApplyPatch linux-2.6-sysrq-c.patch

# fix sched clock monotonicity bugs
ApplyPatch linux-2.6-sched-clock-fix-nohz-interaction.patch

# Architecture patches
# x86(-64)
# Compile 686 kernels tuned for Pentium4.
ApplyPatch linux-2.6-x86-tune-generic.patch
# dump *PIC state at boot with apic=debug
ApplyPatch linux-2.6-x86-apic-dump-all-regs-v3.patch
# hpet fixes from 2.6.27
ApplyPatch linux-2.6-x86-hpet-04-workaround-sb700-bios.patch
# fix e820 reservation checking
ApplyPatch linux-2.6-x86-pci-detect-end_bus_number.patch
# don't oops if there's no IRQ stack available
ApplyPatch linux-2.6-x86-check-for-null-irq-context.patch
# add config option to disable adding CPUs after boot
ApplyPatch linux-2.6-x86-improve-up-kernel-when-cpu-hotplug-and-smp.patch
# fix oops in get_wchan()
ApplyPatch linux-2.6-x86-avoid-dereferencing-beyond-stack-THREAD_SIZE.patch
# reserve first device vector on x86-32
ApplyPatch linux-2.6-x86-Reserve-FIRST_DEVICE_VECTOR-in-used_vectors-bit.patch

# disable ASPM on devices that don't support it
ApplyPatch linux-2.6-pci-disable-aspm-per-acpi-fadt-setting.patch
ApplyPatch linux-2.6-pci-disable-aspm-on-pre-1.1-devices.patch
ApplyPatch linux-2.6-pci-add-an-option-to-allow-aspm-enabled-forcibly.patch
# check range on pci mmap
ApplyPatch linux-2.6-pci-check-mapped-ranges-on-sysfs-resource-files.patch

#
# PowerPC
#
###  UPSTREAM PATCHES:
### NOT (YET) UPSTREAM:
# The EHCI ISO patch isn't yet upstream but is needed to fix reboot
# FIXME: needs work
#ApplyPatch linux-2.6-ps3-ehci-iso.patch
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
# Work around PCIe bridge setup on iSight
ApplyPatch linux-2.6-imac-transparent-bridge.patch
# Link zImage at 32MiB (for POWER machines, Efika)
ApplyPatch linux-2.6-powerpc-zImage-32MiB.patch
# fixes for quad PPC 970 powerstation (#457467)
ApplyPatch linux-2.6-fbdev-teach-offb-about-palette-on-radeon-r500-r600.patch
ApplyPatch linux-2.6-powerpc-fix-OF-parsing-of-64-bits-pci-addresses.patch

#
# SPARC64
#

#
# Exec shield
#
ApplyPatch linux-2.6-execshield.patch

#
# bugfixes to drivers and filesystems
#

# USB
# actually honor the nousb parameter
ApplyPatch linux-2.6-usb-ehci-hcd-respect-nousb.patch
# uvcvideo buffer overflow
ApplyPatch linux-2.6-uvcvideo-return-sensible-min-max-values.patch
ApplyPatch linux-2.6-uvcvideo-dont-use-stack-based-buffers.patch
ApplyPatch linux-2.6-uvcvideo-fix-another-buffer-overflow.patch

# ACPI
# fix cpuidle misbehavior
ApplyPatch linux-2.6-cpuidle-1-do-not-use-poll_idle-unless-user-asks-for-it.patch
ApplyPatch linux-2.6-cpuidle-2-menu-governor-fix-wrong-usage-of-measured_us.patch
ApplyPatch linux-2.6-cpuidle-3-make-ladder-governor-honor-latency-requirements.patch

# mm

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
# /dev/crash driver for the crashdump analysis tool
#
ApplyPatch linux-2.6-crash-driver.patch

#
# PCI
#
# disable message signaled interrupts
ApplyPatch linux-2.6-defaults-pci_no_msi.patch

#
# SCSI Bits.
#
# fix cpqarray pci enable
ApplyPatch linux-2.6-scsi-cpqarray-set-master.patch
# fix vmware emulated scsi controller
#ApplyPatch linux-2.6-scsi-mpt-vmware-fix.patch

# ALSA
#

# block/bio
#

# Filesystem patches.
# cifs
ApplyPatch linux-2.6-fs-cifs-turn-off-unicode-during-session-establishment.patch
# Squashfs
ApplyPatch linux-2.6-squashfs.patch
# fix CIFS plaintext passwords
ApplyPatch linux-2.6-fs-cifs-fix-plaintext-authentication.patch
# don't allow splice to files opened with O_APPEND
ApplyPatch linux-2.6-dont-allow-splice-to-files-opened-with-o_append.patch

# Networking
# Disable easy to trigger printk's.
ApplyPatch linux-2.6-net-silence-noisy-printks.patch

# Misc fixes
# The input layer spews crap no-one cares about.
ApplyPatch linux-2.6-input-kill-stupid-messages.patch
# add support for macbook pro 4,1 and macbook air keyboards
ApplyPatch linux-2.6-input-fix_fn_key_on_macbookpro_4_1_and_mb_air.patch
# kill annoying applesmc debug messages
ApplyPatch linux-2.6-hwmon-applesmc-remove-debugging-messages.patch

# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch
# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch

# Fix the SELinux mprotect checks on executable mappings
ApplyPatch linux-2.6-selinux-mprotect-checks.patch
# Fix SELinux for sparc
ApplyPatch linux-2.6-sparc-selinux-mprotect-checks.patch

# Changes to upstream defaults.
# Use UTF-8 by default on VFAT.
ApplyPatch linux-2.6-defaults-fat-utf8.patch

# libata
# ia64 ata quirk
ApplyPatch linux-2.6-ata-quirk.patch
# fix it821x
ApplyPatch linux-2.6-libata-pata_it821x-driver-updates-and-reworking.patch
# Make Eee disk faster.
ApplyPatch linux-2.6-sata-eeepc-faster.patch
# don't use ahci for pata_marvell adapters
ApplyPatch linux-2.6-libata-pata_marvell-play-nice-with-ahci.patch
# fix drivers making wrong assumptions about what dma values mean
ApplyPatch linux-2.6-libata-fix-a-large-collection-of-DMA-mode-mismatches.patch
# libata breaks lba28 rules
ApplyPatch linux-2.6-libata-lba-28-48-off-by-one-in-ata.h.patch

# wireless patches headed for 2.6.26
#ApplyPatch linux-2.6-wireless.patch
# wireless patches headed for 2.6.27
ApplyPatch linux-2.6-wireless-pending.patch

# Add misc wireless bits from upstream wireless tree
ApplyPatch linux-2.6-at76.patch

# fixups to make current wireless patches build on this kernel
#ApplyPatch linux-2.6-wireless-fixups.patch

# backports of upstream -stable wireless patches that we need
# (reverted in -upstream-reverts)
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-wireless-stable-backports.patch | awk '{print $1}')
if [ "$C" -gt 10 ]; then
ApplyPatch linux-2.6-wireless-stable-backports.patch
fi

# fix for long-standing rt2500usb issues
ApplyPatch linux-2.6-rt2500usb-fix.patch

# bf4634afd8bb72936d2d56425ec792ca1bfa92a2
ApplyPatch linux-2.6-wireless-rt2500pci-restoring-missing-line.patch
# e95926d05d028a6bf0ab60b21b484c3d622fdcd1
ApplyPatch linux-2.6-wireless-revert-b43-add-RFKILL_STATE_HARD_BLOCKED-support.patch
# aaa1553512b9105699113ea7e2ea726f3d9d4de2
ApplyPatch linux-2.6-wireless-p54-fix-regression-due-to-delete-NETDEVICES_MULTIQUEUE-option.patch

# implement smarter atime updates support.
ApplyPatch linux-2.6-smarter-relatime.patch

# NFS Client mounts hang when exported directory do not exist
ApplyPatch linux-2.6-nfs-client-mounts-hang.patch

# build id related enhancements
ApplyPatch linux-2.6-default-mmf_dump_elf_headers.patch

# http://www.lirc.org/
ApplyPatch linux-2.6-lirc.patch

ApplyPatch linux-2.6-e1000-ich9.patch
ApplyPatch linux-2.6-netdev-e1000e-fix-drv-load-issues-amt.patch
ApplyPatch linux-2.6-e1000e-write-protect-nvm.patch

ApplyPatch linux-2.6-netdev-atl2.patch
ApplyPatch linux-2.6-netdev-atl1e.patch

ApplyPatch linux-2.6-net-tulip-interrupt.patch

ApplyPatch linux-2.6-netdev-e1000e-add-support-for-82567lm-4.patch

# Nouveau DRM + drm fixes
ApplyPatch drm-fedora9-rollup.patch

# ext4dev stable patch queue, slated for 2.6.25
#ApplyPatch linux-2.6-ext4-stable-queue.patch

# linux1394 git patches
ApplyPatch linux-2.6-firewire-git-update.patch
#C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-firewire-git-pending.patch | awk '{print $1}')
#if [ "$C" -gt 10 ]; then
#ApplyPatch linux-2.6-firewire-git-pending.patch
#fi

ApplyPatch linux-2.6-ppc-use-libgcc.patch

# get rid of imacfb and make efifb work everywhere it was used
ApplyPatch linux-2.6-merge-efifb-imacfb.patch

ApplyPatch linux-2.6-intel-msr-backport.patch
ApplyPatch linux-2.6-libata-sff-kill-spurious-WARN_ON-in-ata_hsm_move.patch

# for kerneloops reports
ApplyPatch linux-2.6-net-print-module-name-as-part-of-the-message.patch
ApplyPatch linux-2.6-warn-add-WARN-macro.patch
ApplyPatch linux-2.6-warn-Turn-the-netdev-timeout-WARN_ON-into-WARN.patch
ApplyPatch linux-2.6-warn-rename-WARN-to-WARNING.patch

# backport syscall tracing to use the new tracehook.h entry points.
ApplyPatch linux-sparc-tracehook-syscall.patch
# END OF PATCH APPLICATIONS

%endif

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl

cp %{SOURCE10} Documentation/

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

# Unpack the Xen tarball.
%if %{includexen}
cp %{SOURCE2} .
if [ -d xen ]; then
  rm -rf xen
fi
%setup -D -T -q -n kernel-%{kversion} -a1
cd xen
# Any necessary hypervisor patches go here

%endif


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
    make -s ARCH=$Arch %{?_smp_mflags} $MakeTarget %{?sparse_mflags}
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
    if [ -f arch/$Arch/boot/zImage.stub ]; then
      cp arch/$Arch/boot/zImage.stub $RPM_BUILD_ROOT/%{image_install_path}/zImage.stub-$KernelVer || :
    fi
    $CopyKernel $KernelImage \
    		$RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer
    chmod 755 $RPM_BUILD_ROOT/%{image_install_path}/$InstallName-$KernelVer

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
    if [ -s Module.markers ]; then
      cp Module.markers $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    fi
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
%ifarch ppc
    cp -a --parents arch/powerpc/lib/crtsavres.[So] $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
%endif
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

    if [ -d arch/%{_arch}/include ]; then
      cp -a --parents arch/%{_arch}/include  $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/
    fi
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

%if %{includexen}
%if %{with_xen}
  cd xen
  mkdir -p $RPM_BUILD_ROOT/%{image_install_path} $RPM_BUILD_ROOT/boot
  make %{?_smp_mflags} %{xen_flags}
  install -m 644 xen.gz $RPM_BUILD_ROOT/%{image_install_path}/xen.gz-%{KVERREL}.xen
  install -m 755 xen-syms $RPM_BUILD_ROOT/boot/xen-syms-%{KVERREL}.xen
  cd ..
%endif
%endif

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

%if %{includexen}
%if %{with_xen}
BuildKernel %xen_target %xen_image xen
%endif
%endif

%if %{with_kdump}
BuildKernel vmlinux vmlinux kdump vmlinux
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
rm -f $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf
cat > $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf <<\EOF
# This directive teaches ldconfig to search in nosegneg subdirectories
# and cache the DSOs there with extra bit 0 set in their hwcap match
# fields.  In Xen guest kernels, the vDSO tells the dynamic linker to
# search in nosegneg subdirectories and to match this extra hwcap bit
# in the ld.so.cache file.
hwcap 0 nosegneg
EOF
chmod 444 $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf
%endif
%endif

%if %{with_doc}
mkdir -p $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}/Documentation

# sometimes non-world-readable files sneak into the kernel source tree
chmod -R a+r *
# copy the source over
tar cf - Documentation | tar xf - -C $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}

# Make man pages for the kernel API.
make mandocs
mkdir -p $RPM_BUILD_ROOT/usr/share/man/man9
mv Documentation/DocBook/man/*.9.gz $RPM_BUILD_ROOT/usr/share/man/man9
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
    (cd /usr/src/kernels/%{KVERREL}%{?1:.%{1}} &&\
     /usr/bin/find . -type f | while read f; do\
       hardlink -c /usr/src/kernels/*.fc*.*/$f $f\
     done)\
fi\
%{nil}

# This macro defines a %%posttrans script for a kernel package.
#	%%kernel_variant_posttrans [-v <subpackage>] [-s <s> -r <r>] <mkinitrd-args>
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_posttrans(s:r:v:) \
%{expand:%%posttrans %{?-v*}}\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --rpmposttrans %{KVERREL}%{?-v:.%{-v*}} || exit $?\
%{nil}

#
# This macro defines a %%post script for a kernel package and its devel package.
#	%%kernel_variant_post [-v <subpackage>] [-s <s> -r <r>] <mkinitrd-args>
# More text can follow to go at the end of this variant's %%post.
#
%define kernel_variant_post(s:r:v:) \
%{expand:%%kernel_devel_post %{?-v*}}\
%{expand:%%kernel_variant_posttrans %{?-v*}}\
%{expand:%%post %{?-v*}}\
%{-s:\
if [ `uname -i` == "x86_64" -o `uname -i` == "i386" ] &&\
   [ -f /etc/sysconfig/kernel ]; then\
  /bin/sed -i -e 's/^DEFAULTKERNEL=%{-s*}$/DEFAULTKERNEL=%{-r*}/' /etc/sysconfig/kernel || exit $?\
fi}\
/sbin/new-kernel-pkg --package kernel-libre%{?-v:-%{-v*}} --mkinitrd --depmod --install %{*} %{KVERREL}%{?-v:.%{-v*}} || exit $?\
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
%kernel_variant_post -s kernel-smp -r kernel

%kernel_variant_preun smp
%kernel_variant_post -v smp

%kernel_variant_preun PAE
%kernel_variant_post -v PAE -s kernel-smp -r kernel-PAE

%kernel_variant_preun debug
%kernel_variant_post -v debug

%kernel_variant_post -v PAEdebug -s kernel-smp -r kernel-PAEdebug
%kernel_variant_preun PAEdebug

%kernel_variant_preun xen
%kernel_variant_post -v xen -s kernel-xen[0U] -r kernel-xen -- `[ -d /proc/xen -a ! -e /proc/xen/xsd_kva ] || echo --multiboot=/%{image_install_path}/xen.gz-%{KVERREL}.xen`
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
%{_datadir}/doc/kernel-doc-%{kversion}/Documentation/*
%dir %{_datadir}/doc/kernel-doc-%{kversion}/Documentation
%dir %{_datadir}/doc/kernel-doc-%{kversion}
%{_datadir}/man/man9/*
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
/%{image_install_path}/%{?-k:%{-k*}}%{!?-k:vmlinuz}-%{KVERREL}%{?2:.%{2}}\
/boot/System.map-%{KVERREL}%{?2:.%{2}}\
#/boot/symvers-%{KVERREL}%{?2:.%{2}}.gz\
/boot/config-%{KVERREL}%{?2:.%{2}}\
%{?-a:%{-a*}}\
%dir /lib/modules/%{KVERREL}%{?2:.%{2}}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/kernel\
/lib/modules/%{KVERREL}%{?2:.%{2}}/build\
/lib/modules/%{KVERREL}%{?2:.%{2}}/source\
/lib/modules/%{KVERREL}%{?2:.%{2}}/extra\
/lib/modules/%{KVERREL}%{?2:.%{2}}/updates\
/lib/modules/%{KVERREL}%{?2:.%{2}}/weak-updates\
%ifarch %{vdso_arches}\
/lib/modules/%{KVERREL}%{?2:.%{2}}/vdso\
%endif\
/lib/modules/%{KVERREL}%{?2:.%{2}}/modules.*\
%ghost /boot/initrd-%{KVERREL}%{?2:.%{2}}.img\
%{?-e:%{-e*}}\
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
%kernel_variant_files -a /%{image_install_path}/xen*-%{KVERREL}.xen -e /etc/ld.so.conf.d/kernelcap-%{KVERREL}.xen.conf %{with_xen} xen

%changelog
* Sat Oct 11 2008 Dennis Gilmore <dennis@ausil.us> 2.6.26.6-68
- disable atl1e on sparc64
- backport syscall tracing to use the new tracehook.h entry points on sparc64
- syscall tracing patch is already upstream in 2.6.27

* Fri Oct 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-67
- libata: pata_marvell: use the upstream patch for playing nice with ahci

* Fri Oct 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-66
- x86: Reserve FIRST_DEVICE_VECTOR in used_vectors bitmap.

* Fri Oct 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-65
- pci: check range on sysfs mmapped resources

* Fri Oct 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-64
- Don't allow splice to files opened with O_APPEND.

* Fri Oct 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-63
- Fix buffer overflow in uvcvideo driver.

* Fri Oct 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-62
- Fix possible oops in get_wchan()

* Thu Oct 09 2008 Kyle McMartin <kyle@redhat.com> 2.6.26.6-61
- add e1000e: write protect nvram to prevent corruption patch from upstream

* Thu Oct 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-60
- x86: switch to UP mode when only one CPU is present at boot time

* Thu Oct 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-59
- 2.6.26.6

* Wed Oct 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-58.rc1
- Copy dwmw2's build fixes from rawhide:
    Include arch/$ARCH/include/ directories in kernel-devel (F10#465486)
    Include arch/powerpc/lib/crtsavres.[So] too (#464613)

* Tue Oct  7 2008 Roland McGrath <roland@redhat.com> 2.6.26.6-57.rc1
- Fix build ID fiddling magic. (#465873)

* Tue Oct 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.6-56.rc1
- 2.6.26.6-rc1
  Dropped patches:
    linux-2.6-sched-fix-process-time-monotonicity.patch
    linux-2.6-x86-64-fix-overlap-of-modules-and-fixmap-areas.patch
    linux-2.6-x86-fdiv-bug-detection-fix.patch
    linux-2.6-x86-io-delay-add-hp-f700-quirk.patch
    linux-2.6-x86-fix-oprofile-and-hibernation-issues.patch
    linux-2.6-x86-32-amd-c1e-force-timer-broadcast-late.patch
    linux-2.6-x86-pat-proper-tracking-of-set_memory_uc.patch
    linux-2.6-x86-hpet-01-fix-moronic-32-64-bit-thinko.patch
    linux-2.6-x86-hpet-02-read-back-compare-register.patch
    linux-2.6-x86-hpet-03-make-minimum-reprogramming-delta-useful.patch
    linux-2.6-x86-fix-memmap-exactmap-boot-argument.patch
    linux-2.6-usb-fix-hcd-interrupt-disabling.patch
    linux-2.6-acpi-processor-use-signed-int.patch
    linux-2.6-mm-dirty-page-tracking-race-fix.patch
    linux-2.6-mm-mark-correct-zone-full-when-scanning-zonelists.patch
    linux-2.6-block-submit_bh-discards-barrier-flag.patch
    linux-2.6-pcmcia-fix-broken-abuse-of-dev-driver_data.patch

* Mon Oct 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-55
- Fix more fallout from the WARN() macro.

* Mon Oct 06 2008 John W. Linville <linville@redhat.com> 2.6.26.5-54
- Re-revert at76_usb to version from before attempted mac80211 port

* Mon Oct 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-53
- Add missing WARN() macro.

* Fri Oct 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-52
- Disable the snd-aw2 module: it conflicts with video drivers. (#462919)

* Fri Oct 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-51
- Support building -stable RC kernels.
  Kernel versioning example: 2.6.26.6-52.rc1.fc9

* Wed Oct 01 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-50
- Add config option to disallow adding CPUs after booting.

* Mon Sep 29 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-49
- Don't oops if no IRQ stack is available (#461846)

* Mon Sep 29 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-48
- Two patches to help make kerneloops bug reports more useful.
  (requested by Arjan)

* Tue Sep 23 2008 Kyle McMartin <kyle@redhat.com> 2.6.26.5-47
- two more wireless fixes from John
   p54: Fix regression due to "net: Delete NETDEVICES_MULTIQUEUE kconfig option
   Revert "b43/b43legacy: add RFKILL_STATE_HARD_BLOCKED support"
  
* Mon Sep 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-46
- pcmcia: Fix broken abuse of dev->driver_data (#462178)

* Fri Sep 19 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-45
- pci: three patches to disable PCIe ASPM on old devices/systems (#462210)

* Fri Sep 19 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-44
- x86: pci: detect end_bus_number according to acpi/e820 reserved, v2 (#462210)

* Fri Sep 19 2008 Kyle McMartin <kyle@redhat.com> - 2.6.26.5-43
- libata-sff: kill spurious WARN_ON() in ata_hsm_move()
   Pointed-out-by: Arjan van de Ven (9c2676b61a5a4b6d99e65fb2f438fb3914302eda)

* Wed Sep 17 2008 Kyle McMartin <kyle@redhat.com> - 2.6.26.5-42
- From Chris Lalancette <clalance@redhat.com>:
   Backport KVM Intel MSR fix (efa67e0d1f51842393606034051d805ab9948abd)

* Mon Sep 15 2008 Roland McGrath <roland@redhat.com> - 2.6.26.5-41
- utrace: Fix common oops in ptrace EPERM case.

* Sun Sep 14 2008 Kyle McMartin <kyle@redhat.com> 2.6.26.5-40
- wireless: rt2500pci: restoring missing line

* Sat Sep 13 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-39
- libata: fix DMA mode mistmatches
- libata: interpret the LBA28 spec properly

* Sat Sep 13 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-38
- Add two patches scheduled for 2.6.26-stable:
  cifs: fix plaintext authentication
  mm:   mark correct zone full when scanning zonelists

* Sat Sep 13 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-37
- Fix problems with scheduler clock going backwards (#453257)

* Sat Sep 13 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-36
- x86: fix memmap=exactmap argument, fixing kdump in some cases (#459103)

* Sat Sep 13 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-35
- Fix problems with AMT on e1000e (#453023)

* Thu Sep 11 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-34
- Don't use ahci driver for marvell controllers. (#455833)

* Wed Sep 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-33
- Add support for 82567LM-4 to the e1000e driver (#461438)

* Mon Sep 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-32
- HPET fixes from 2.6.27

* Mon Sep 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.5-31
- Linux 2.6.26.5

* Wed Sep 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-30
- Linux 2.6.26.4-rc1
  Dropped patches:
    linux-2.6-bio-fix-__bio_copy_iov-handling-of-bv_len.patch
    linux-2.6-bio-fix-bio_copy_kern-handling-of-bv_len.patch
    linux-2.6-netdev-atl1-disable-tso-by-default.patch

* Wed Sep 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-29
- [CIFS] Turn off Unicode during session establishment for plaintext authentication
- atl1: disable TSO by default
- x86: PAT proper tracking of set_memory_uc and friends

* Tue Sep 02 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-28
- Restore most of the drivers dropped in the 2.6.26 update. (F8#460853)

* Sat Aug 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-27
- x86-32: amd c1e force timer broadcast late
  (fixes failure to disable local apic timer)

* Sat Aug 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-26
- mm: dirty page tracking race fix

* Sat Aug 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-25
- block: submit_bh() inadvertently discards barrier flag on a sync write

* Sat Aug 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-24
- Fix cpuidle misbehavior. (#459214)

* Sat Aug 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-23
- Add two bio patches scheduled for -stable.

* Sat Aug 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-22
- specfile: don't use the latest stable update as the vanilla directory.

* Sat Aug 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-21
- x86: fix oprofile + hibernation badness (#459413)

* Fri Aug 29 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-20
- x86: add Presario F700 to io_delay quirk list. (#459546)

* Fri Aug 29 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-19
- USB: fix hcd interrupt disabling (#457165)

* Fri Aug 29 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-18
- x86-64: fix overlap of modules and fixmap areas
- x86: fdiv bug detection fix (#197455)

* Fri Aug 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.3-17
- Linux 2.6.26.3
  Dropped patches:
    linux-2.6-usb-storage-nikond80-quirk.patch
  Upstream revert:
    rtl8187-fix-lockups-due-to-concurrent-access-to-config-routine.patch
  New wireless patch:
    linux-2.6-wireless-stable-backports.patch

* Fri Aug 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.2-16
- Disable the snd_pcsp driver. (#459477)

* Thu Aug 14 2008 Dave Jones <davej@redhat.com> 2.6.26.2-15
- Silence MMCONFIG printk during boot.

* Wed Aug 13 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.14
- Updated deblobbing of drm-fedora9-rollup.patch.

* Sat Aug 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.2-14
- Fix obvious bug in ACPI processor driver.

* Sat Aug 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.2-13
- Disable VIA Padlock driver for now; it can cause oopses.

* Wed Aug 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.2-12
- Linux 2.6.26.2

* Wed Aug 06 2008 Dave Jones <davej@redhat.com>
- Own all the modules.* files in /lib/modules. (#456857)

* Wed Aug 06 2008 Dave Airlie <airlied@redhat.com> 2.6.26.1-10
- fix drm patch to not export a static.

* Tue Aug 05 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.9
- Deblobbed 2.6.26.
- Update deblobbing of linux-2.6-wireless-pending.patch.
- Deblob drm-fedora9-rollup.patch

* Tue Aug 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.1-9
- Hopefully fix build on ppc64.

* Tue Aug 05 2008 Jarod Wilson <jwilson@redhat.com> 2.6.26.1-8
- updated firewire stack w/bugfixes and iso timestamp support
- updated lirc drivers w/new device support

* Tue Aug 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.1-7
- Drop already-merged patches, fix up PS3 bootloader patch.

* Tue Aug 05 2008 Dave Airlie <airlied@redhat.com> 2.6.26.1-6
- rebase drm into 2.6.26 in one super patch

* Mon Aug 04 2008 Dave Jones <davej@redhat.com> 2.6.26.1-5
- Fix bogus printk in execshield noticed by Brendan Lynch.

* Mon Aug 04 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.1-4
- Update libata pata_it821x driver.
- Re-enable *PIC debug code.

* Mon Aug 04 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.1-3
- Fix up the smarter-relatime patch.

* Mon Aug 04 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.1-2
- Fixes for quad PPC970 Powerstation (#457467)

* Mon Aug 04 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.26.1-1
- Linux 2.6.26.1

* Mon Aug 04 2008 John W. Linville <linville@redhat.com> 2.6.25.14-108
- fix for long-standing rt2500usb issues (#411481)

* Sun Aug 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.14-107
- Add patches queued for 2.6.25.15.
- Add conflict against older iwl4965 firmware.

* Fri Aug 01 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.14-106
- Linux 2.6.25.14
  Dropped patches:
    linux-2.6-alsa-trident-spdif.patch
    linux-2.6-libata-retry-enabling-ahci.patch
    linux-2.6-libata-pata_atiixp-dont-disable.patch
  Reverted from 2.6.25.14:
    ath5k-don-t-enable-msi-we-cannot-handle-it-yet.patch
    b43legacy-release-mutex-in-error-handling-code.patch

* Fri Aug 01 2008 John W. Linville <linville@redhat.com> 2.6.25.13-105
- Revert at76_usb to version from before attempted mac80211 port

* Wed Jul 30 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.104
- Deblob linux-2.6-wireless-pending.patch.

* Wed Jul 30 2008 John W. Linville <linville@redhat.com> 2.6.25.13-104
- Upstream wireless fixes from 2008-07-29
  (http://marc.info/?l=linux-wireless&m=121737750023195&w=2)

* Mon Jul 28 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.13-103
- Linux 2.6.25.13

* Fri Jul 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.12-102
- Fix 64-bit resource checking on 32-bit kernels. (#447143)

* Fri Jul 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.12-101
- Set default powersave timeout to 0 for the AC97 driver. (#450395)

* Thu Jul 24 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.12-100
- Linux 2.6.25.12

* Tue Jul 22 2008 Kyle McMartin <kmcmartin@redhat.com>
- libata-acpi: fix calling sleeping function in irq context (#451896, #454954)

* Mon Jul 21 2008 Dave Jones <davej@redhat.com>
- Change yenta to modular instead of built-in. (#456173)

* Mon Jul 21 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.97
- Fix provides from pkgrelease to pkg_release.

* Sun Jul 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.11-97
- USB: fix timer regression
- x86: dump APIC/IOAPIC/PIC state at boot time

* Sun Jul 20 2008 Kyle McMartin <kmcmartin@redhat.com>
- Add atl1e network driver for eee 901.

* Fri Jul 19 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.11-95
- Add sysrq-l to show backtraces on all CPUs.

* Thu Jul 17 2008 Dave Jones <davej@redhat.com> 2.6.25.11-94
- Fix SHIRQ debug trace in tulip driver. (#454575)

* Tue Jul 15 2008 John W. Linville <linville@redhat.com> 2.6.25.11-93
- Upstream wireless updates from 2008-07-14
  (http://marc.info/?l=linux-wireless&m=121606436000705&w=2)

* Tue Jul 15 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.92.1 Jul 16
- Updated deblobbing to -libre3.

* Mon Jul 14 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.92
- Updated deblobbing to -libre2.

* Sun Jul 13 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.11-92
- Linux 2.6.25.11

* Thu Jul 10 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.91
- Deblobbed rtl8187b_reg_table in linux-2.6-wireless-pending.patch.

* Thu Jul 10 2008 John W. Linville <linville@redhat.com>  2.6.25.9-91
- Upstream wireless fixes from 2008-07-09
  (http://marc.info/?l=linux-netdev&m=121563769208664&w=2)
- Upstream wireless updates from 2008-07-08
  (http://marc.info/?l=linux-wireless&m=121554411325041&w=2)

* Thu Jul 10 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-90
- libata: don't let ata_piix driver attach to ICH6M in ahci mode (#430916)

* Wed Jul 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-89
- hdaps: support new Lenovo notebook models (#449888)

* Tue Jul 08 2008 Eric Sandeen <sandeen@redhat.com> 2.6.25.10-88
- Fix reiserfs list corruption (#453699)

* Tue Jul 08 2008 John W. Linville <linville@redhat.com> 2.6.25.10-87
- Upstream wireless fixes from 2008-07-07
  (http://marc.info/?l=linux-wireless&m=121546143025524&w=2)

* Mon Jul 07 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-86
- Fix USB interrupt handling with shared interrupts.

* Fri Jul 04 2008 John W. Linville <linville@redhat.com> 2.6.25.10-85
- Upstream wireless fixes from 2008-07-02
  (http://marc.info/?l=linux-netdev&m=121503163124089&w=2)
- Apply Stefan Becker's fix for bad hunk of wireless build fixups for 2.6.25
  (https://bugzilla.redhat.com/show_bug.cgi?id=453390#c36)

* Fri Jul 04 2008 Dave Jones <davej@redhat.com> 2.6.25.10-84
- Better fix for the Nikon D80 usb-storage quirk.

* Thu Jul 03 2008 Dave Jones <davej@redhat.com> 2.6.25.10-83
- Add USB Storage quirk for Nikon D40 with new firmware.

* Thu Jul 03 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.10-82
- Linux 2.6.25.10
- Reverted stable patch, not needed with utrace:
	x86_64-ptrace-fix-sys32_ptrace-task_struct-leak.patch
- Reverted part of this stable patch against drivers/net/wireless/strip.c
  (the driver eventually gets removed as part of the wireless updates):
	tty-fix-for-tty-operations-bugs.patch

* Wed Jul 02 2008 John W. Linville <linville@redhat.com> 2.6.25.9-81
- Upstream wireless updates from 2008-06-30
  (http://marc.info/?l=linux-wireless&m=121486432315033&w=2)

* Tue Jul 01 2008 Dave Jones <davej@redhat.com>
- Shorten summary in specfile.

* Mon Jun 30 2008 John W. Linville <linville@redhat.com> 2.6.25.9-79
- Upstream wireless fixes from 2008-06-30
  (http://marc.info/?l=linux-wireless&m=121485709702728&w=2)
- Upstream wireless updates from 2008-06-27
  (http://marc.info/?l=linux-wireless&m=121458164930953&w=2)

* Mon Jun 30 2008 Dave Jones <davej@redhat.com>
- Disable rio500 driver (bz 451567)

* Mon Jun 30 2008 Dave Jones <davej@redhat.com>
- ALSA: trident - pause s/pdif output (#453464)

* Fri Jun 27 2008 John W. Linville <linville@redhat.com> 2.6.25.9-76
- Upstream wireless fixes from 2008-06-27
  (http://marc.info/?l=linux-wireless&m=121459423021061&w=2)

* Fri Jun 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.9-75
- Fix bluetooth keyboard disconnect (#449872)

* Wed Jun 25 2008 John W. Linville <linville@redhat.com> 2.6.25.9-74
- Upstream wireless fixes from 2008-06-25
  (http://marc.info/?l=linux-wireless&m=121440912502527&w=2)

* Tue Jun 24 2008 John W. Linville <linville@redhat.com> 2.6.25.9-73
- Upstream wireless updates from 2008-06-14
  (http://marc.info/?l=linux-netdev&m=121346686508160&w=2)

* Tue Jun 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.9-72
- Linux 2.6.25.9

* Tue Jun 24 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.8-71
- pppolt2p: fix heap overflow (CVE-2008-2750) (#452111)

* Mon Jun 23 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.8-70
- libata: retry enable of AHCI mode before reporting an error (#452595)

* Mon Jun 23 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.8-69
- Linux 2.6.25.8
- Patches reverted from 2.6.25.8, already in Fedora:
    b43-fix-noise-calculation-warn_on.patch
    b43-fix-possible-null-pointer-dereference-in-dma-code.patch

* Sun Jun 22 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.68
- Deblob microcodes in new drm-radeon-update.patch.

* Sun Jun 22 2008 Dave Airlie <airlied@redhat.com> 2.6.25.7-68
- update drm update to fix a bug.

* Fri Jun 20 2008 Dave Jones <davej@redhat.com> 2.6.25.7-67
- Fix hpwdt driver to not oops on init. (452183)

* Fri Jun 20 2008 Jarod Wilson <jwilson@redhat.com> 2.6.25.7-66
- firewire: add phy config packet send timeout, prevents deadlock
  with flaky ALi controllers (#446763, #444694)

* Thu Jun 19 2008 Dave Airlie <airlied@redhat.com> 2.6.25.7-65
- update radeon patches to newer upstream

* Mon Jun 16 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.7-64
- Linux 2.6.25.7
- Don't apply upstream-reverts patch to -vanilla kernels.
- Dropped patches:
    linux-2.6-alsa-emu10k1-fix-audigy2.patch
    linux-2.6-netlink-fix-parse-of-nested-attributes.patch
    linux-2.6-af_key-fix-selector-family-initialization.patch
    linux-2.6-mmc-wbsd-fix-request_irq.patch
- Reverted wireless patches from 2.6.25.7, already in Fedora:
    b43-fix-controller-restart-crash.patch
    mac80211-send-association-event-on-ibss-create.patch
    ssb-fix-context-assertion-in-ssb_pcicore_dev_irqvecs_enable.patch

* Sun Jun 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-63
- Make rsync able to write to VFAT partitions again. (#449080)

* Sat Jun 14 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-62
- Replace eeepc driver with upstream eeepc_laptop driver.

* Sat Jun 14 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-61
- Enable Controller Area Networking (F8#451179)

* Fri Jun 13 2008 John W. Linville <linville@redhat.com> 2.6.25.6-60
- Upstream wireless fixes from 2008-06-13
  (http://marc.info/?l=linux-wireless&m=121339101523260&w=2)

* Tue Jun 10 2008 Roland McGrath <roland@redhat.com> - 2.6.25.6-58
- Fix i386 syscall tracing and PTRACE_SYSEMU, had broken UML. (#449909)

* Tue Jun 10 2008 John W. Linville <linville@redhat.com> 2.6.25.6-57
- Upstream wireless fixes from 2008-06-09
  (http://marc.info/?l=linux-kernel&m=121304710726632&w=2)
- Upstream wireless updates from 2008-06-09
  (http://marc.info/?l=linux-netdev&m=121304710526613&w=2)

* Tue Jun 10 2008 Roland McGrath <roland@redhat.com> - 2.6.25.6-54
- Fix spurious BUG_ON in tracehook_release_task. (#443552)

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-53
- Fix oops in wbsd MMC driver when card is present during boot (#449817)

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-52
- Fix init if af_key sockets (F8#450499)

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-51
- Sync ACPI patches with F-8 kernel.

* Mon Jun 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.6-50
- Linux 2.6.25.6
- Dropped patches:
    linux-2.6-x86-fix-asm-constraint-in-do_IRQ.patch
    linux-2.6-x86-pci-revert-remove-default-rom-allocation.patch
    linux-2.6-x86-dont-read-maxlvt-if-apic-unmapped.patch
    linux-2.6-x86-fix-setup-of-cyc2ns-in-tsc_64.patch
    linux-2.6-x86-prevent-pge-flush-from-interruption.patch
    linux-2.6-cifs-fix-unc-path-prefix.patch
    linux-2.6-ext34-xattr-fix.patch
    linux-2.6-xfs-small-buffer-reads.patch
    linux-2.6-net-iptables-add-xt_iprange-aliases.patch
    linux-2.6-caps-remain-source-compatible-with-32-bit.patch
    linux-2.6-libata-force-hardreset-in-sleep-mode.patch
- Updated patches:
    linux-2.6-input-fix_fn_key_on_macbookpro_4_1_and_mb_air.patch

* Fri Jun 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.5-49
- Fix the specfile to match the kernel version.

* Fri Jun 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.5-48
- Linux 2.6.25.5

* Fri Jun 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-47
- Make 64-bit capabilities backwards-compatible with old user programs (#447518)

* Fri Jun 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-46
- Fix Audigy2 ZS audio adapter hang (#242208)

* Thu Jun 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-45
- Kill spurious applesmc debug messages (#448056)

* Thu Jun 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-44
- Make DMA work again on atiixp PATA devices (#450191)

* Thu Jun 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-43
- Fix docking when docking station has a bay device (bug reported on IRC.)

* Tue Jun 03 2008 John W. Linville <linville@redhat.com> 2.6.25.4-42
- Upstream wireless fixes from 2008-06-03
  (http://marc.info/?l=linux-wireless&m=121252137324941&w=2)

* Mon Jun 02 2008 Jarod Wilson <jwilson@redhat.com> 2.6.25.4-41
- Fix oops in lirc_i2c module
- Add lirc support for latest MCE receivers

* Thu May 29 2008 John W. Linville <linville@redhat.com> 2.6.25.4-39
- Upstream wireless fixes from 2008-05-28
  (http://marc.info/?l=linux-wireless&m=121201250110162&w=2)

* Thu May 29 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.38
- Deblob microcodes in drm-radeon-update.patch.

* Wed May 28 2008 Dave Airlie <airlied@redhat.com> 2.6.25.4-38
- drm-radeon-update.patch - Add R500 support along with updated radeon driver

* Tue May 28 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-37
- Fix parsing of netlink messages (#447812)

* Tue May 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-36
- Fix two hard-to-reproduce x86 bugs:
  x86: fix sched_clock when calibrated against PIT
  x86: don't allow flush_tlb_all to be interrupted

* Tue May 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-35
- input: fix function keys on macbook pro 4,1 and air (#445761)

* Tue May 27 2008 John W. Linville <linville@redhat.com> 2.6.25.4-34
- Upstream wireless updates from 2008-05-22
  (http://marc.info/?l=linux-wireless&m=121146112404515&w=2)

* Tue May 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-33
- libata: fix hangs on undock (#439197)
- libata: fix problems with some old/broken CF hardware (F8 #224005)

* Thu May 22 2008 Dave Jones <davej@redhat.com> 2.6.25.4-32
- Disable CONFIG_DMAR. This is terminally broken in the presence of a broken BIOS

* Wed May 21 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-31
- Clean up specfile a bit.

* Wed May 21 2008 John W. Linville <linville@redhat.com> 2.6.25.4-30
- libertas: Fix ethtool statistics
- mac80211: fix NULL pointer dereference in ieee80211_compatible_rates
- mac80211: don't claim iwspy support
- rtl8187: resource leak in error case
- hostap_cs: add ID for Conceptronic CON11CPro
- orinoco_cs: add ID for SpeedStream wireless adapters

* Tue May 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-29
- virtio_net: free transmit skbs in a timer (#444765)

* Tue May 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-28
- Disable the group scheduler (CONFIG_GROUP_SCHED) (#446192)
- x86: don't read the APIC if it's not mapped (#447183)

* Tue May 20 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-27
- x86: don't map VDSO into userspace when it's disabled (#229304)
- x86: fix ASM constraint in do_IRQ()
- x86: map PCI ROM by default again (F8 #440644)

* Mon May 19 2008 John W. Linville <linville@redhat.com> 2.6.25.4-26
- Re-sync wireless bits w/ current upstream

* Mon May 19 2008 Dave Jones <davej@redhat.com> 2.6.25.4-24
- Disable PATA_ISAPNP (it's busted).

* Sun May 19 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.23
- Rebase to libre1.

* Fri May 16 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-23
- ALSA: add support for AD1883/1884A/1984A/1984B codecs and Thinkpad X300 (#445954)

* Fri May 16 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-22
- iptables: make firewall scripts using iprange work again (#446827)
- Enable the snd-serial-u16550 audio driver (#446783)

* Fri May 16 2008 Eric Sandeen <esandeen@redhat.com> 2.6.25.4-21
- xfs: Fix memory corruption with small buffer reads (kernel.org #10421)

* Thu May 15 2008 Eric Sandeen <esandeen@redhat.com> 2.6.25.4-20
- ext3/4: fix uninitialized bs in ext3/4_xattr_set_handle()

* Thu May 15 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.4-19
- Linux 2.6.25.4
   Dropped patches:
   - linux-2.6.25-sparc64-semctl.patch
   - linux-2.6-libata-ata_piix-check-sidpr.patch

* Mon May 12 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.3-18
- CIFS: fix UNC path prefix to have the correct slash (#443681)

* Mon May 12 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25.3-17
- Linux 2.6.25.3
  Drop patches merged in 2.6.25.3:
   linux-2.6.25-sparc64-mmap_check_fix.patch
   linux-2.6-md-fix-oops-in-rdev_attr_store.patch

* Wed May 07 2008 Tom "spot" Callaway <tcallawa@redhat.com> 2.6.25.2-16
- Fix sparc64 kernel crash in mmap_check (Dave Miller)

* Wed May 07 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25.2-15
- Linux 2.6.25.2

* Wed May  7 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.14.1 May  8
- Rebase to linux-2.6.25-libre.tar.bz2.

* Thu May 01 2008 Dave Airlie <airlied@redhat.com> 2.6.25-14
- fix radeon fast-user-switch oops + i915 breadcrumb oops

* Wed Apr 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25-13
- Fix drive detection on some Macbook models (#439398)
- Fix oops in RAID code (#441765)

* Tue Apr 29 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25-12
- Fix CVE-2008-1675; patches taken from 2.6.25.1-rc1.

* Mon Apr 28 2008 Alexandre Oliva <lxoliva@fsfla.org>
- Provide kernel-headers from kernel-libre-headers.
- Provide kernel-doc from kernel-libre-doc.

* Fri Apr 25 2008 Tom "spot" Callaway <tcallawa@redhat.com> 2.6.25-11
- add sparc64 semctl fix (David Miller)
  (it will be in git shortly, and can be dropped on the next git merge)

* Thu Apr 24 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.10 Apr 28
- Updated deblob and deblob-check.
- Deblobbed linux-2.6.25, nouveau-drm.patch and nouveau-drm-update.patch.

* Thu Apr 24 2008 John W. Linville <linville@redhat.com> 2.6.25-10
- mac80211: Fix n-band association problem
- net/mac80211/rx.c: fix off-by-one
- mac80211: MAINTAINERS update
- ssb: Fix all-ones boardflags
- mac80211: update mesh EID values
- b43: Workaround invalid bluetooth settings
- b43: Fix HostFlags data types
- b43: Add more btcoexist workarounds
- mac80211: Fix race between ieee80211_rx_bss_put and lookup routines.
- prism54: prism54_get_encode() test below 0 on unsigned index
- wireless: rndis_wlan: modparam_workaround_interval is never below 0.
- iwlwifi: Don't unlock priv->mutex if it isn't locked
- mac80211: fix use before check of Qdisc length

* Wed Apr 23 2008 Dave Airlie <airlied@redhat.com> 2.6.25-8
- drm fixup oops in modesetting code and stable fixes for i915 code from upstream

* Tue Apr 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25-7
- Enable machine check exception handling on x86_64.

* Tue Apr 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25-6
- Force hard reset on sleeping SATA links during probe (#436099)

* Tue Apr 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.25-5
- Disable PCI MSI interrupts by default again.

* Mon Apr 21 2008 Eric Paris <eparis@redhat.com>
- Don't BUG_ON() in selinux_clone_mnt_opts inside the installer because its init is wonky

* Fri Apr 18 2008 Kyle McMartin <kmcmartin@redhat.com>
- Enable CONFIG_RT_GROUP_SCHED (#442959)

* Thu Apr 17 2008 Jarod Wilson <jwilson@redhat.com> 2.6.25-2
- Back out FireWire patch requiring successive selfID complete
  events, needs more work to keep from causing sbp2 issues (#435550)

* Thu Apr 17 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.25-1
- Linux 2.6.25
- linux-2.6-wireless.patch merged upstream

* Wed Apr 16 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc9-git2

* Tue Apr 15 2008 John W. Linville <linville@redhat.com>
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
- rt2x00: Use lib->config_filter() during scheduled packet filter config

* Tue Apr 15 2008 John W. Linville <linville@redhat.com>
- Reenable wireless patches

* Tue Apr 15 2008 Jarod Wilson <jwilson@redhat.com>
- Fix kernel_variant_preun() to properly remove flavoured kernel
  entries from bootloader config (Mark McLoughlin)

* Tue Apr 15 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc9-git1

* Tue Apr 15 2008 Dave Airlie <airlied@redhat.com>
- fix oops on nouveau startup and make build again (#442122)
- fix radeon oops seen on kerneloops (#442227)

* Mon Apr 14 2008 Dave Airlie <airlied@redhat.com>
- update to latest nouveau drm from git

* Sun Apr 13 2008 David Woodhouse <dwmw2@redhat.com>
- Remove 'CHRP' from /proc/cpuinfo on Efika, to fix platform detection
  in anaconda

* Sat Apr 12 2008 Jarod Wilson <jwilson@redhat.com>
- Resync with latest FireWire git tree
- Add work-around patch for wrong generation in bus reset packets
  with TI controllers (#243081). Might also help #435550...

* Sat Apr 12 2008 Dave Jones <davej@redhat.com>
- Silence some noisy printks caused by BIOS bugs.

* Sat Apr 12 2008 Dave Jones <davej@redhat.com>
- Fix NFS Client mounts hang when exported directory do not exist

* Sat Apr 12 2008 Dave Jones <davej@redhat.com>
- Enable framepointers for better backtraces.

* Fri Apr 11 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.25-rc9
- Temporarily disabled wireless patches.

* Fri Apr 11 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc8-git9

* Thu Apr 10 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc8-git8

* Wed Apr 09 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.218 Apr 11
- Deblobbed patch-2.6.25-rc8-git7, references removed config variables.
- Deblobbed linux-2.6-drm-i915-modeset.patch, references removed files.

* Wed Apr 09 2008 Chuck Ebbert <cebbert@redhat.com>
- Bump version.

* Wed Apr 09 2008 Kyle McMartin <kmcmartin@redhat.com>
- Update uvcvideo to svn rev200

* Wed Apr 09 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.25-rc8-git7

* Tue Apr 08 2008 Jarod Wilson <jwilson@redhat.com>
- One more update to FireWire JMicron work-around patches

* Tue Apr 08 2008 Jarod Wilson <jwilson@redhat.com>
- Leave debug config files alone when building noarch

* Tue Apr 08 2008 Dave Airlie <airlied@redhat.com>
- maybe fix i686 PAEdebug build issue

* Tue Apr 08 2008 Dave Airlie <airlied@redhat.com>
- fix oops in radeon code caused by fixing the mappings

* Tue Apr 08 2008 Dave Airlie <airlied@redhat.com>
- drm fixes, should fix radeon mappings problem and nouveau crashes

* Tue Apr 08 2008 Chuck Ebbert <cebbert@redhat.com>
- Remove pata_ali ATAPI DMA disable patch, now upstream.
- Fix build of pvrusb2 driver.

* Mon Apr 07 2008 John W. Linville <linville@redhat.com>
- iwlwifi: fix n-band association problem
- ipw2200: set MAC address on radiotap interface
- libertas: fix mode initialization problem
- nl80211: fix STA AID bug
- b43legacy: fix bcm4303 crash

* Mon Apr 07 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix CONFIG_USB_DEBUG so it's disabled properly with 'make release'.

* Mon Apr 07 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.25-rc8-git6

* Mon Apr 07 2008 Jarod Wilson <jwilson@redhat.com>
- Turn off the usual extra rawhide debugging in preparation for F9 release

* Mon Apr 07 2008 Chuck Ebbert <cebbert@redhat.com>
- Increase SMP boot delay, hopefully solving bug #431882.

* Mon Apr 07 2008 Chuck Ebbert <cebbert@redhat.com>
- Enable the 1-wire drivers (except for the Matrox driver which conflicts
  with the Matrox framebuffer driver.)

* Mon Apr 07 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.201
- Enable CONFIG_EEPRO100.

* Sun Apr 06 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc8-git4

* Fri Apr 04 2008 Chuck Ebbert <cebbert@redhat.com>
- Remove VIA VT6212 sleep time fix, already in mainline.

* Fri Apr 04 2008 Dave Jones <davej@redhat.com>
- USB: VIA VT6212 10us EHCI sleep time select. (#438599)

* Fri Apr 04 2008 Jarod Wilson <jwilson@redhat.com>
- firewire: add logging of register access failures to debug spew
- firewire: don't append AT packets to halted contexts (because certain
  crappy controllers lock up if you do).

* Fri Apr 04 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc8-git3

* Thu Apr 03 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc8-git2

* Wed Apr 02 2008 Eric Sandeen <sandeen@redhat.com>
- Fix mis-read of xfs attr2 superblock flag which was causing
  corruption in some cases. (#437968)

* Wed Apr 02 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc8-git1

* Wed Apr 02 2008 Jarod Wilson <jwilson@redhat.com>
- Resync FireWire patches with current linux1394 git tree

* Wed Apr  2 2008 Mark McLoughlin <markmc@redhat.com>
- Sync some spec file changes from kernel-xen

* Tue Apr 01 2008 John W. Linville <linville@redhat.com>
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

* Tue Apr 01 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc8

* Tue Apr 01 2008 John W. Linville <linville@redhat.com>
- avoid endless loop while compiling on ia64 and some other arches

* Tue Apr 01 2008 Peter Jones <pjones@redhat.com>
- get rid of imacfb and make efifb work everywhere it was used

* Tue Apr 01 2008 Jarod Wilson <jwilson@redhat.com>
- Don't apply utrace bits on ia64, doesn't build there atm

* Mon Mar 31 2008 Dave Jones <davej@redhat.com>
- Support UDMA66 on Asus Eee. (experimental)

* Mon Mar 31 2008 Chuck Ebbert <cebbert@redhat.com>
- Disable HDA audio power save by default. (#433495)
  (Users can still enable it manually.)

* Mon Mar 31 2008 Chuck Ebbert <cebbert@redhat.com>
- Re-enable CONFIG_PCI_LEGACY so some additional drivers get built.

* Mon Mar 31 2008 Jarod Wilson <jwilson@redhat.com>
- Make split debuginfo packages build correctly again

* Mon Mar 31 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.177
- Deblobbed patch-2.6.25-rc7-git6, modifies removed files.

* Mon Mar 31 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc7-git6

* Mon Mar 31 2008 Dave Airlie <airlied@redhat.com>
- add fix for radeon oops (#439656)

* Sun Mar 30 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.175.rc7.git5
- Deblobbed patch-2.6.25-rc7-git5, modifies removed files.

* Sat Mar 29 2008 Dave Jones <davej@redhat.com>
- Add a 'would have oomkilled' sysctl for debugging.

* Sat Mar 29 2008 Jarod Wilson <jwilson@redhat.com>
- Add new virtual Provides to go with the new uname scheme to
  make life easier for 3rd-party kernel module packaging.

* Sat Mar 29 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc7-git5

* Fri Mar 28 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc7-git4

* Fri Mar 28 2008 John W. Linville <linville@redhat.com>
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

* Fri Mar 28 2008 Jarod Wilson <jwilson@redhat.com>
- Fix up Requires/Provides for debuginfo bits

* Fri Mar 28 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc7-git3

* Fri Mar 28 2008 Dave Jones <davej@redhat.com>
- Disable cyblafb (439400)

* Thu Mar 27 2008 Dave Jones <davej@redhat.com>
- Disable page allocation debugging.

* Thu Mar 27 2008 Jarod Wilson <jwilson@redhat.com>
- One more try at fixing scriptlets for flavoured kernels (#439036)

* Thu Mar 27 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc7-git2

* Thu Mar 27 2008 Dave Jones <davej@redhat.com>
- Enable USB debug in debug kernels.

* Thu Mar 27 2008 John W. Linville <linville@redhat.com>
- cfg80211: don't export ieee80211_get_channel

* Wed Mar 26 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc7-git1

* Wed Mar 26 2008 John W. Linville <linville@redhat.com>
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

* Wed Mar 26 2008 Jarod Wilson <jwilson@redhat.com>
- Fix buglet in posttrans hooks (#439036)
- Tweak arch-in-uname setup to use dot delimiter for flavoured
  kernels, eliminates a needless symlink and reads cleaner.

* Wed Mar 26 2008 Chuck Ebbert <cebbert@redhat.com>
- Remove a now unnecessary gcc43 compile fix.
- Apply compile fixes to vanilla kernels.

* Wed Mar 26 2008 David Woodhouse <dwmw2@redhat.com>
- Link PowerPC zImage at 32MiB (#239658 on POWER5, also fixes Efika)

* Wed Mar 26 2008 Alexandre Oliva <lxoliva@fsfla.org> -libre.0.156.rc7
- Deblob linux tarball.
- Deblob patch-2.6.25-rc7.bz2.
- Adjust linux-2.6-netdev-atl2.patch for deblobbing.

* Wed Mar 26 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc7

* Tue Mar 25 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc6-git8

* Tue Mar 25 2008 Jarod Wilson <jwilson@redhat.com>
- Put %%{_target_cpu} into kernel uname, and tack it onto assorted
  files and directories. Makes it possible to do parallel installs
  of say both i686 and x86_64 kernels of the same version on x86_64
  x86_64 hardware (#197065).
- Plug DMA memory leak in firewire async receive handler

* Tue Mar 25 2008 John W. Linville <linville@redhat.com>
- wavelan_cs arm fix
- arlan: fix warning when PROC_FS=n
- rt2x00: Add id for Corega CG-WLUSB2GPX
- b43: Fix DMA mapping leakage
- b43: Remove irqs_disabled() sanity checks
- iwlwifi: fix a typo in Kconfig message
- MAINTAINERS: update iwlwifi git url
- iwlwifi: fix __devexit_p points to __devexit functions
- iwlwifi: mac start synchronization issue

* Mon Mar 24 2008 Roland McGrath <roland@redhat.com>
- utrace update

* Mon Mar 24 2008 Tom "spot" Callaway <tcallawa@redhat.com>
- Add linux-2.6-sparc64-big-kernel.patch, to support bigger sparc64 kernels (DaveM)

* Mon Mar 24 2008 Dave Jones <davej@redhat.com>
- Add man pages for kernel API to kernel-doc package.

* Mon Mar 24 2008 Jeremy Katz <katzj@redhat.com>
- Update the kvm patch to a more final one

* Mon Mar 24 2008 Jarod Wilson <jwilson@redhat.com>
- firewire not totally merged yet, fix up patch accordingly

* Mon Mar 24 2008 Kyle McMartin <kmcmartin@redhat.com>
- wireless & firewire were merged.

* Mon Mar 24 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc6-git7

* Mon Mar 24 2008 Kyle McMartin <kmcmartin@redhat.com>
- Add fix-kvm-2.6.25-rc6-leak.patch, requested by jkatz.

* Sun Mar 23 2008 Tom "spot" Callaway <tcallawa@redhat.com>
- fix selinux mprotect for sparc

* Sun Mar 23 2008 Roland McGrath <roland@redhat.com>
- utrace update with sparc64 support

* Sun Mar 23 2008 David Woodhouse <dwmw2@redhat.com>
- Support fan, fix PCIe bridge setup on iMac G5 (iSight)
- Disable CONFIG_XMON_DEFAULT

* Fri Mar 21 2008 Roland McGrath <roland@redhat.com>
- reenable utrace

* Fri Mar 21 2008 Jarod Wilson <jwilson@redhat.com>
- firewire: make sure phy config packets get sent before initiating bus
  reset. Fixes bugzilla.kernel.org #10128.

* Fri Mar 21 2008 Dave Jones <davej@redhat.com>
- Enable PIIX4 I2C driver on x86-64.

* Thu Mar 20 2008 Roland McGrath <roland@redhat.com>
- utrace rebase

* Thu Mar 20 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc6-git5

* Thu Mar 20 2008 Jarod Wilson <jwilson@redhat.com>
- Updated firewire debugging patch with added logging of interrupt
  event codes and cancelled AT packets

* Thu Mar 20 2008 David Woodhouse <dwmw2@redhat.com>
- Enable MPC5200 support again for Efika

* Thu Mar 20 2008 John W. Linville <linville@redhat.com>
- Prevent iwlwifi drivers from registering bands with no channels (#438273)

* Thu Mar 20 2008 Dave Airlie <airlied@redhat.com>
- bring back drm modeset

* Wed Mar 19 2008 Roland McGrath <roland@redhat.com>
- utrace is back, rebased

* Wed Mar 19 2008 Dave Jones <davej@redhat.com>
- Add kernel posttrans and preun hooks for other packages (#433121)
  Bump mkinitrd requires accordingly.

* Wed Mar 19 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc6-git3

* Tue Mar 18 2008 Jarod Wilson <jwilson@redhat.com>
- Turn on CONFIG_MODULE_SRCVERSION_ALL for dkms and others (#427311)

* Mon Mar 17 2008 Jarod Wilson <jwilson@redhat.com>
- Fix up firewire patches for 2.6.25-rc6

* Sun Mar 16 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.25-rc6
- Temporarily drop firewire patches.

* Sun Mar 16 2008 Jarod Wilson <jwilson@redhat.com>
- firewire: fix remaining panic in handle_at_packet (bz.kernel.org #9617)

* Fri Mar 14 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc5-git4

* Fri Mar 14 2008 John W. Linville <linville@redhat.com>
- b43: phy.c fix typo in register write
- prism54: support for 124a:4025 - another version of IOGear GWU513 802.11g
- PS3: gelic: change the prefix of the net interface for wireless
- ath5k: disable irq handling in ath5k_hw_detach()
- revert "tkip: remove unused function, other cleanups"
- revert "mac80211: remove Hi16, Lo16 helpers"
- revert "mac80211: remove Hi8/Lo8 helpers, add initialization vector helpers"

* Fri Mar 14 2008 Jarod Wilson <jwilson@redhat.com>
- Resync firewire patches w/linux1394-2.6.git
- Update firewire selfID/AT/AR debug patch to version
  that can be enabled via module options

* Fri Mar 14 2008 Adam Jackson <ajax@redhat.com>
- usb: additional quirk for Microsoft wireless receiver

* Thu Mar 13 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc5-git3
- linux-2.6-ppc32-ucmpdi2.patch: nuked, merged upstream

* Wed Mar 12 2008 Jarod Wilson <jwilson@redhat.com>
- firewire: fix DMA coherency on x86_64 systems w/memory
  mapped over the 4GB boundary (may fix #434830)
- firewire: sync DMA for device, resolves at least one cause
  of panics in handle_at_packet() (bz.kernel.org #9617)

* Wed Mar 12 2008 Chuck Ebbert <cebbert@redhat.com>
- Kill annoying ALSA debug messages

* Wed Mar 12 2008 Dave Airlie <airlied@redhat.com>
- drm: fix oops on i915 driver when X crashes

* Tue Mar 11 2008 John W. Linville <linville@redhat.com>
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

* Tue Mar 11 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc5-git2

* Tue Mar 11 2008 John W. Linville <linville@redhat.com>
- rt2x00: never disable multicast because it disables broadcast too
- rt2x00: Add new D-Link USB ID
- drivers/net/Kconfig: fix whitespace for GELIC_WIRELESS entry
- libertas: fix the 'compare command with itself' properly

* Tue Mar 11 2008 Dave Jones <davej@redhat.com>
- Print values when we overflow resource allocation.

* Tue Mar 11 2008 Dave Jones <davej@redhat.com>
- Recognise Appletouch trackpad in Macbook 3.1

* Tue Mar 11 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc5-git1

* Tue Mar 11 2008 Dave Airlie <airlied@redhat.com>
- make flicker a lot less on startup for X

* Tue Mar 11 2008 Dave Airlie <airlied@redhat.com>
- fix i965 cursor support in drm modesetting

* Mon Mar 10 2008 John W. Linville <linville@redhat.com>
- Use correct "Dual BSD/GPL" license tag for iwlcore.ko

* Mon Mar 10 2008 John W. Linville <linville@redhat.com>
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

* Mon Mar 10 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc5

* Mon Mar 10 2008 Jarod Wilson <jwilson@redhat.com>
- firewire: additional debug output if config ROM read fails

* Sat Mar  8 2008 Chuck Ebbert <cebbert@redhat.com>
- 2.6.25-rc4-git3

* Fri Mar  7 2008 Roland McGrath <roland@redhat.com>
- x86_64: fix 32-bit process syscall restart via 64-bit ptrace (#436183)

* Fri Mar 07 2008 Dave Jones <davej@redhat.com>
- Enable unused symbols.

* Fri Mar 07 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc4-git2

* Fri Mar 07 2008 Dave Airlie <airlied@redhat.com>
- drm/modeset - fix unload and overly-large size alloc

* Thu Mar 06 2008 Dave Airlie <airlied@redhat.com>
- Add initial i915 drm modesetting tree - needs i915.modeset=1 on command
  line + new libdrm + new intel driver to actually be useful

* Wed Mar 05 2008 Jarod Wilson <jwilson@redhat.com>
- firewire-sbp2: improved ability to reconnect to devices
  following a bus reset
- firewire-sbp2: set proper I/O retry limits in SBP-2 devices

* Wed Mar 05 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc4

* Wed Mar 05 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc3-git6

* Wed Mar 05 2008 David Woodhouse <dwmw2@redhat.com>
- Add modalias in sysfs for vio devices (#431045)

* Tue Mar 04 2008 John W. Linville <linville@redhat.com>
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

* Tue Mar 04 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc3-git5

* Mon Mar  3 2008 Roland McGrath <roland@redhat.com>
- x86_64: fix 32-bit process syscall restart (#434995)

* Mon Mar 03 2008 John W. Linville <linville@redhat.com>
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

* Mon Mar 03 2008 Jarod Wilson <jwilson@redhat.com>
- Rebase firewire to latest linux1394-2.6.git tree
- firewire-sbp2: permit drives to suspend (#243210)
- firewire: restore bus power on resume on older PowerPC Macs

* Mon Mar 03 2008 Kyle McMartin <kmcmartin@redhat.com>
- Add virtio_blk patch from Jeremy Katz

* Mon Mar 03 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc3-git4

* Mon Mar 03 2008 Dave Airlie <airlied@redhat.com>
- drm/i915: remove intel sarea priv refs for DRI2

* Mon Mar 03 2008 Dave Airlie <airlied@redhat.com>
- drm: revert nopfn->fault conversion to fix oops on intel

* Sat Mar 01 2008 Jarod Wilson <jwilson@redhat.com>
- firewire: fix suspend/resume on older PowerPC Macs (#312871)
- firewire: support for first-gen Apple UniNorth controller
- firewire: add option for remote debugging

* Fri Feb 29 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc3-git2

* Fri Feb 29 2008 Dave Jones <davej@redhat.com>
- chmod 755 the installed kernel image. (#435319).

* Fri Feb 29 2008 Dave Airlie <airlied@redhat.com>
- update nouveau bits to the latest drm tree

* Wed Feb 27 2008 Dave Airlie <airlied@redhat.com>
- oops committed some bits wrong

* Wed Feb 27 2008 Dave Airlie <airlied@redhat.com>
- linux-2.6-drm-git-mm.patch - update to latest -mm queue - adds DRI2

* Wed Feb 27 2008 Dave Airlie <airlied@redhat.com>
- linux-2.6-drm-git-mm.patch - Add upstream git queue

* Wed Feb 27 2008 John W. Linville <linville@redhat.com>
- Use a separate config option for the b43 pci to ssb bridge.
- Don't build bcm43xx if SSB is static and b43 PCI-SSB bridge is enabled.
- Fix b43 driver build for arm
- rt2x00: Fix antenna diversity
- rt2x00: Add link tuner safe RX toggle states
- rt2x00: Don't switch to antenna with low rssi
- rt2x00: Fix rt2x00lib_reset_link_tuner()
- rndis_wlan: fix sparse warnings
- mac80211: fix kmalloc vs. net_ratelimit
- PS3: gelic: Link the wireless net_device structure to the corresponding device structure
- libertas: Remove unused exports
- gelic wireless driver needs WIRELESS_EXT support
- at76_usb: fix missing newlines in printk, improve some messages
- at76_usb: remove unneeded code
- at76_usb: add more MODULE_AUTHOR entries
- at76_usb: reindent, reorder initializers for readability
- at76_usb: make the driver depend on MAC80211

* Wed Feb 27 2008 Jarod Wilson <jwilson@redhat.com>
- firewire-sbp2: fix refcounting bug that prevented module unload
- firewire-sbp2: fix use-after-free bug

* Tue Feb 26 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc3-git1

* Tue Feb 26 2008 Dave Jones <davej@redhat.com>
- kludge to make ich9 e1000 work
- Drop older unnecessary e1000 workarounds.

* Mon Feb 25 2008 Jarod Wilson <jwilson@redhat.com>
- firewire: fix crashes in workqueue jobs
- firewire: endian fixes

* Mon Feb 25 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc3

* Sat Feb 23 2008 Dave Jones <davej@redhat.com>
- 2.6.25-rc2-git7

* Thu Feb 21 2008 Kyle McMartin <kmcmartin@redhat.com>
- crypto_blkcipher: big hack caused module dep loop, try another fix
- Linux 2.6.25-rc2-git5

* Thu Feb 21 2008 John W. Linville <linville@redhat.com>
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

* Thu Feb 21 2008 Peter Jones <pjones@redhat.com>
- Require newer mkinitrd version.

* Thu Feb 21 2008 Kyle McMartin <kmcmartin@redhat.com>
- hopefully fix the last thinko in the libgcc ppc patch

* Wed Feb 20 2008 Jarod Wilson <jwilson@redhat.com>
- Update to latest FireWire git bits

* Wed Feb 20 2008 Kyle McMartin <kmcmartin@redhat.com>
- fix thinko in the libgcc patch

* Wed Feb 20 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc2-git4
- Enable VIRTIO modules.
- lguest: fix asm-offsets.h symbols
- ppc: link in libgcc.a for now
- crypto_blkcipher: add a hack to make it depend on chainiv

* Wed Feb 20 2008 Eric Sandeen <sandeen@redhat.com>
- More ext4 updates/fixes from the "stable" ext4 patchqueue

* Tue Feb 19 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc2-git2

* Mon Feb 18 2008 John W. Linville <linville@redhat.com>
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

* Mon Feb 18 2008 Dave Jones <davej@redhat.com>
- More exec-shield cleanup.

* Sun Feb 17 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix Powerbook function keys on x86-64.

* Fri Feb 15 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc2

* Fri Feb 15 2008 Chuck Ebbert <cebbert@redhat.com>
- Copy i386 CONFIG_PHYSICAL_START setting from F8.

* Fri Feb 15 2008 Kyle McMartin <kmcmartin@redhat.com>
- Fix linux-2.6-execshield.patch, fastcall has been nuked.

* Fri Feb 15 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc1-git4

* Thu Feb 14 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix broken Megahertz PCMCIA Ethernet adapter.

* Thu Feb 14 2008 Jarod Wilson <jwilson@redhat.com>
- Build powernow-k8 as a module

* Thu Feb 14 2008 Chuck Ebbert <cebbert@redhat.com>
- Disable KVM on i586 kernel.

* Thu Feb 14 2008 Jarod Wilson <jwilson@redhat.com>
- Make KVM modules build again.

* Thu Feb 14 2008 Dave Jones <davej@redhat.com>
- Set minimum mmap address to 64K.

* Wed Feb 13 2008 Dave Jones <davej@redhat.com>
- Drop some pointless bits of the execshield patch.

* Wed Feb 13 2008 Kyle McMartin <kmcmartin@redhat.com>
- Fix up squashfs, hopefully...

* Wed Feb 13 2008 Kyle McMartin <kmcmartin@redhat.com>
- Added patch from Jakub to fix sigcontext.h export on x86.

* Wed Feb 13 2008 Dennis Gilmore <dennis@ausil.us>
- build headers on sparc64 because the buildsys needs them

* Tue Feb 12 2008 Dave Jones <davej@redhat.com>
- Add latest version of almost-upstream restricted devmem patch.

* Tue Feb 12 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc1-git2

* Tue Feb 12 2008 Jarod Wilson <jwilson@redhat.com>
- Fix up firewire bits to apply to 2.6.25

* Tue Feb 12 2008 Kyle McMartin <kmcmartin@redhat.com>
- Bump.

* Tue Feb 12 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.25-rc1

* Mon Feb 11 2008 Roland McGrath <roland@redhat.com>
- Fix debuginfo sorting regexp.
- Strip ELF vmlinux used as /boot image.
- Reenable ppc build.

* Sun Feb 10 2008 Chuck Ebbert <cebbert@redhat.com>
- Enable Infiniband connected mode support. (F8#432196)

* Sun Feb 10 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix PPC Pegasos libata again (#430802)

* Sun Feb 10 2008 Dave Airlie <airlied@redhat.com>
- CVE-2008-0600 - local root vulnerability in vmsplice

* Sun Feb 10 2008 Jarod Wilson <jwilson@redhat.com>
- firewire-core: improve logging of device connections
- firewire-sbp2: handle device reconnections more smoothly
- firewire-sbp2: disconnect and re-login when scsi probes
  fails due to a bus reset

* Fri Feb 08 2008 Chuck Ebbert <cebbert@redhat.com>
- Restore futex patch that was dropped from 2.6.24.1

* Fri Feb 08 2008 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.24.1

* Fri Feb 08 2008 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.24.1-rc1

* Wed Feb 06 2008 John W. Linville <linville@redhat.com>
- at76_usb: Add ID for Uniden PCW100
- b43: fix build with CONFIG_SSB_PCIHOST=n
- b43: Fix DMA for 30/32-bit DMA engines
- b43legacy: fix PIO crash
- b43legacy: fix suspend/resume
- b43legacy: drop packets we are not able to encrypt
- b43legacy: fix DMA slot resource leakage
- iwl3945-base.c: fix off-by-one errors
- wireless/iwlwifi/iwl-4965.c: add parentheses
- mac80211: Is not EXPERIMENTAL anymore
- rt2x00: don't write past the end when writing short descriptors on rt61
- rt2x00: Update copyright notice
- rt2x00: Add new USB ID to rt2500usb
- rt2x00: Fix tx parameter initialization
- rt2x00: Enable master and adhoc mode again
- rt2x00: Driver requiring firmware should select crc algo
- rt2x00: Add per-interface structure
- rt2x00: Remove TX_MGMT queue usage
- rt2x00: Initialize QID from queue->qid
- rt2x00: Move beacon and atim queue defines into rt2x00
- rt2x00: Fix queue index handling
- rt2x00: Queue handling overhaul

* Tue Feb 05 2008 Jarod Wilson <jwilson@redhat.com>
- Make FireWire I/O survive bus resets and device
  reconnections better

* Tue Feb 05 2008 Chuck Ebbert <cebbert@redhat.com>
- atl2 network driver 2.0.4
- ASUS Eeepc ACPI hotkey driver

* Mon Feb 04 2008 Eric Sandeen <sandeen@redhat.com>
- Update ext4 patch queue, now picking up from Linus' git

* Mon Feb 04 2008 Chuck Ebbert <cebbert@redhat.com>
- Temporarily disable build for ppc.

* Mon Feb 04 2008 Eric Sandeen <sandeen@redhat.com>
- Add back in some xfs stack-reduction changes which got
  lost; they are upstream and will be in 2.6.25.

* Fri Feb 01 2008 John W. Linville <linville@redhat.com>
- mac80211: make alignment warning optional
- mac80211 rate control: fix section mismatch
- mac80211: fix initialisation error path
- ath5k: fix section mismatch warning
- iwlwifi: fix merge sequence: exit on error before state change
- iwlwifi: fix sparse warning in iwl 3945
- iwlwifi: Fix MIMO PS mode
- iwlwifi: remove ieee80211 types from iwl-helpers.h
- mac80211: dissolve pre-rx handlers

* Fri Feb 01 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix build with GCC 4.3, part 2.

* Thu Jan 31 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix build with GCC 4.3

* Thu Jan 31 2008 Chuck Ebbert <cebbert@redhat.com>
- Fix null pointer dereference in bonding driver (#430391)

* Wed Jan 30 2008 Eric Sandeen <sandeen@redhat.com>
- Allow xattrs in body of root ext4 inode (#429857)
  Should fix selinux installs.

* Tue Jan 29 2008 Roland McGrath <roland@redhat.com>
- Fix i686 exec-shield vs fixmap vDSO (vm.vdso_enabled=2). (#427641)

* Tue Jan 29 2008 John W. Linville <linville@redhat.com>
- A few more wireless fixes for 2.6.25
- Some post-2.6.25 wireless updates
- Actually, we support the new b43 firmware...

* Mon Jan 28 2008 Chuck Ebbert <cebbert@redhat.com>
- Strip extra leading slashes in selinux filenames.
- wireless: reject too-new b43 firmware

* Mon Jan 28 2008 Chuck Ebbert <cebbert@redhat.com>
- Build in the CMOS RTC driver.

* Mon Jan 28 2008 Jarod Wilson <jwilson@redhat.com>
- Update firewire with latest pending bits
- Add epoll lockdep annotation (#323411)

* Mon Jan 28 2008 Eric Sandeen <sandeen@redhat.com>
- Update ext4 patch to latest stable patch queue

* Fri Jan 25 2008 Jarod Wilson <jwilson@redhat.com>
- Try login to firewire-sbp2 devices using device-provided timeout

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com>
- Bump revision.

* Fri Jan 25 2008 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.24

* Thu Jan 24 2008 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.24-rc8-git7

* Wed Jan 23 2008 Dave Jones <davej@redhat.com>
- Drop GFS1 export patch.  Really, use GFS2 instead.

* Wed Jan 23 2008 Eric Sandeen <sandeen@redhat.com>
- When probing for root fs, don't mount ext3 as ext4dev;
  if the extents flag is not set, refuse. (#429782)

* Wed Jan 23 2008 Kyle McMartin <kmcmartin@redhat.com>
- Add uvcvideo driver for USB webcams.

* Wed Jan 23 2008 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.24-rc8-git6

* Wed Jan 23 2008 Jarod Wilson <jwilson@redhat.com>
- improve locking in firewire config rom read routines
  to fix (most?) 'giving up on config rom' problems (#429598)

* Wed Jan 23 2008 Chuck Ebbert <cebbert@redhat.com>
- Make the USB EHCI driver repect the "nousb" parameter. (F8#429863)

* Tue Jan 22 2008 John W. Linville <linville@redhat.com>
- Latest wireless updates from upstream
- Tidy-up wireless patches
- Remove obsolete ath5k and rtl8180 patches
- Add rndis_wext driver

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com>
- Disable the p4-clockmod driver -- it causes system hangs. (F8#428895)

* Mon Jan 21 2008 Dave Jones <davej@redhat.com>
- Remove a boatload of dead CONFIG_ options.

* Mon Jan 21 2008 Chuck Ebbert <cebbert@redhat.com>
- Linux 2.6.24-rc8-git4

* Mon Jan 21 2008 Adam Jackson <ajax@redhat.com>
- Disable MODULE_DEVICE_TABLE patch for i915 and radeon, it makes things
  very unhappy.

* Mon Jan 21 2008 Eric Sandeen <sandeen@redhat.com>
- Update ext4 patch to latest stable patch queue

* Fri Jan 18 2008 Jarod Wilson <jwilson@redhat.com>
- Increase management orb reply timeout in firewire-sbp2 driver,
  should make a lot of firewire drives behave a LOT better with
  the juju stack (#428554, #238606, #391701)

* Thu Jan 17 2008 Kyle McMartin <kmcmartin@redhat.com>
- Enable CONFIG_TCG_INFINEON on ia64-generic.

* Thu Jan 17 2008 Kyle McMartin <kmcmartin@redhat.com>
- update linux-2.6-drm-mm.patch, need to use wbinvd on cpus which
  don't support clflush (otherwise we get a nasty invalid op oops.)

* Thu Jan 17 2008 John W. Linville <linville@redhat.com>
- More wireless fixes headed for 2.6.24
- More wireless updates headed for 2.6.25

* Wed Jan 16 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc8

* Wed Jan 16 2008 Dave Airlie <airlied@redhat.com>
- update r500 patch to remove dup pciids.

* Mon Jan 14 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc7-git6

* Mon Jan 14 2008 Kyle McMartin <kmcmartin@redhat.com>
- Remerge linux-2.6-wireless-pending bits.
- Fixup rt2x00 build due to automerge lossage.

* Mon Jan 14 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc7-git5

* Mon Jan 14 2008 Eric Sandeen <sandeen@redhat.com>
- Update ext4 patch to latest stable patch queue

* Sat Jan 12 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc7-git4

* Fri Jan 11 2008 Jarod Wilson <jwilson@redhat.com>
- Add linux1394/firewire git patches
- FireWire IR dynamic buffer alloc (David Moore)

* Thu Jan 10 2008 Chuck Ebbert <cebbert@redhat.com>
- temporarily fix up utrace breakage

* Thu Jan 10 2008 John W. Linville <linville@redhat.com>
- rt2500usb thinko fix
- b43 N phy pre-support updates
- ath5k cleanups and beacon fixes

* Thu Jan 10 2008 Eric Sandeen <sandeen@redhat.com>
- ext4 updates slated for 2.6.25

* Thu Jan 10 2008 Jarod Wilson <jwilson@redhat.com>
- Update lirc to latest upstream plus kthread support

* Thu Jan 10 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc7-git2

* Wed Jan 09 2008 John W. Linville <linville@redhat.com>
- A few more wireless bits destined for 2.6.25
- Enable CONFIG_NL80211

* Tue Jan 08 2008 John W. Linville <linville@redhat.com>
- More wireless fixes headed for 2.6.24
- More wireless updates headed for 2.6.25

* Sun Jan  6 2008 Roland McGrath <roland@redhat.com>
- Reenable utrace after pulling current patches that already applied fine.

* Sun Jan 6 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc7

* Sat Jan 5 2008 Kyle McMartin <kmcmartin@redhat.com>
- Disable utrace until fixed (doesn't apply to git12 yet.)
- Nuke linux-2.6-proc-self-maps-fix.patch, applied upstream.
- Respin linux-2.6-smarter-relatime.patch.

* Sat Jan 5 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc6-git12

* Wed Jan 2 2008 Kyle McMartin <kmcmartin@redhat.com>
- Un-disabled -doc builds.

* Wed Jan 2 2008 Kyle McMartin <kmcmartin@redhat.com>
- Temporarily disable -doc builds in hopes of getting new rpms into
  rawhide until koji is fixed.

* Wed Jan 2 2008 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc6-git8

* Sun Dec 30 2007 Dave Jones <davej@redhat.com>
- PAEdebug needs to obsolete PAE-debug

* Sun Dec 30 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc6-git7

* Thu Dec 27 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc6-git4

* Wed Dec 26 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc6-git3

* Mon Dec 24 2007 Dave Jones <davej@redhat.com>
- Anaconda should be fixed now, so disable SYSFS_DEPRECATED again.

* Mon Dec 24 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc6-git2

* Fri Dec 21 2007 Chuck Ebbert <cebbert@redhat.com>
- Don't apply the ALSA update for now.

* Fri Dec 21 2007 David Woodhouse <dwmw2@redhat.com>
- Disable CONFIG_PS3_USE_LPAR_ADDR to fix PS3 memory probing

* Fri Dec 21 2007 John W. Linville <linville@redhat.com>
- Yet another round of wireless updates...

* Thu Dec 20 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc6

* Thu Dec 20 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc5-git7

* Thu Dec 20 2007 Jarod Wilson <jwilson@redhat.com>
- Fix up lirc to build w/2.6.24 (Stefan Lippers-Hollmann)

* Thu Dec 20 2007 Dave Airlie <airlied@redhat.com>
- add MODULE_DEVICE_TABLE to radeon and i915 drivers

* Wed Dec 19 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc5-git6

* Wed Dec 19 2007 Dave Airlie <airlied@redhat.com>
- Update drm upstream patches and add basic r500 drm support

* Tue Dec 18 2007 Chuck Ebbert <cebbert@redhat.com>
- Enable CIFS upcall support.

* Tue Dec 18 2007 Kyle McMartin <kmcmartin@redhat.com>
- add --with sparse for people^Wsomeone who might care

* Tue Dec 18 2007 Kyle McMartin <kmcmartin@redhat.com>
- really disable sparse this time...

* Tue Dec 18 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc5-git5

* Mon Dec 17 2007 John W. Linville <linville@redhat.com>
- Some wireless stack and driver fixes headed towards 2.6.24
- Some wireless stack and driver bits destined for 2.6.25

* Mon Dec 17 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc5-git4

* Fri Dec 14 2007 Roland McGrath <roland@redhat.com>
- Clean up vDSO install patches.

* Fri Dec 14 2007 David Woodhouse <dwmw2@redhat.com>
- Fix OProfile on non-Cell ppc64
- Fix EHCI on PS3 to allow rebooting

* Fri Dec 14 2007 Dave Jones <davej@redhat.com>
- Anaconda still needs the old sysfs files, so reenable SYSFS_DEPRECATED
  for now.

* Fri Dec 14 2007 David Woodhouse <dwmw2@redhat.com>
- Enable PS3 wireless again now that it's saner
- Update powermac suspend via /sys/power/state patches

* Thu Dec 13 2007 Dave Jones <davej@redhat.com>
- Update to squashfs 3.3

* Thu Dec 13 2007 Dave Jones <davej@redhat.com>
- bridge: assign random address.

* Thu Dec 13 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc5-git3

* Wed Dec 12 2007 Chuck Ebbert <cebbert@redhat.com>
- Disable e1000 link power management.

* Wed Dec 12 2007 Dave Jones <davej@redhat.com>
- Better version of the e1000 bad eeprom workaround.

* Wed Dec 12 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc5-git2

* Tue Dec 11 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc5-git1

* Tue Dec 11 2007 Dave Jones <davej@redhat.com>
- Lets see what (still) breaks when we disable SYSFS_DEPRECATED again.

* Tue Dec 11 2007 Chuck Ebbert <cebbert@redhat.com>
- Enable the USB IO-Warrior driver.

* Tue Dec 11 2007 Chuck Ebbert <cebbert@redhat.com>
- ALSA update

* Tue Dec 11 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc5
- remove linux-2.6-firewire-ohci-1.0-iso-receive.patch, merged in -rc5

* Mon Dec 10 2007 Dave Jones <davej@redhat.com>
- Rename PAE-debug flavour to PAEdebug (#388321)

* Mon Dec 10 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc4-git7

* Sat Dec 08 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc4-git6

* Fri Dec 07 2007 Peter Jones <pjones@redhat.com>
- Turn on CONFIG_FB_IMAC .

* Fri Dec 07 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc4-git5

* Thu Dec 06 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc4-git4

* Wed Dec 05 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc4-git3

* Wed Dec 05 2007 Dave Jones <davej@redhat.com>
- Don't do sparse builds by default.
  The output is rarely looked at, and it adds time to the build.

* Wed Dec 05 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc4-git2
- remove linux-2.6-pasemi-mac.patch, merged into -git2
- remove linux-2.6-fec_mpc52xx-fix.patch, merged into -git2

* Tue Dec 04 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc4-git1

* Tue Dec 04 2007 John W. Linville <linville@redhat.com>
- Some wireless driver bits headed for 2.6.25

* Mon Dec 04 2007 David Woodhouse <dwmw2@redhat.com>
- Fix HW csum on pasemi_mac
- Fix i2c bus reservation on pasemi

* Mon Dec 03 2007 Kyle McMartin <kmcmartin@redhat.com>
- Linux 2.6.24-rc4

* Mon Dec 03 2007 Dave Jones <davej@redhat.com>
- Enable telclock.

* Mon Dec 03 2007 David Woodhouse <dwmw2@redhat.com>
- Add kernel-bootwrapper subpackage for building PowerPC zImages
- PA Semi updates destined for 2.6.25 (RTC, MDIO, etc.)
- Remove obsolete pmac_zilog patch, unset CONFIG_SERIAL_PMACZILOG_TTYS
- Switch to generic RTC class (again)

* Mon Dec 03 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc3-git7
- turn off linux-2.6-wireless.patch, merged into -git7
- remove linux-2.6-phy-ioctl-fix.patch, merged into -git7
- remove SET_NETDEV hunk in linux-2.6-fec_mpc52xx-fix.patch, merged in -git7

* Mon Dec 03 2007 Jarod Wilson <jwilson@redhat.com>
- Fix regression on FireWire OHCI 1.1 controllers introduced
  by 1.0 support (#344851)

* Mon Dec 03 2007 David Woodhouse <dwmw2@redhat.com>
- Enable PASemi support

* Sat Dec 01 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc3-git6

* Sat Dec 01 2007 John W. Linville <linville@redhat.com>
- Some wireless bits headed for 2.6.25
- Make ath5k use software WEP

* Fri Nov 30 2007 Kyle McMartin <kmcmartin@redhat.com>
- Oops! Local make-build-go-faster kernel.spec patch slipped in,
  reverted.

* Fri Nov 30 2007 Jarod Wilson <jwilson@redhat.com>
- FireWire OHCI 1.0 Isochronous Receive support (#344851)

* Fri Nov 30 2007 John W. Linville <linville@redhat.com>
- Some wireless bits headed for 2.6.24

* Fri Nov 30 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc3-git5

* Thu Nov 29 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc3-git4

* Thu Nov 29 2007 John W. Linville <linville@redhat.com>
- Resync wireless bits headed for 2.6.25

* Thu Nov 29 2007 Dave Airlie <airlied@linux.ie>
- update drm-mm-git.patch to fix 64-bit truncation

* Thu Nov 29 2007 Dave Airlie <airlied@linux.ie>
- update drm-mm-git.patch to enable sysfs udev device creation (#401961)

* Wed Nov 28 2007 Kyle McMartin <kmcmartin@redhat.com>
- Update linux-2.6-debug-acpi-os-write-port.patch for changes to
  drivers/acpi/osl.c

* Wed Nov 28 2007 Chuck Ebbert <cebbert@redhat.com>
- Add support for SiS 7019 audio for K12LTSP project

* Wed Nov 28 2007 David Woodhouse <dwmw2@redhat.com>
- Fix phy code to not return success to unknown ioctls

* Wed Nov 28 2007 David Woodhouse <dwmw2@redhat.com>
- Fix net: symlink in sysfs for fec_mpc52xx

* Wed Nov 28 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc3-git3

* Tue Nov 27 2007 Kyle McMartin <kmcmartin@redhat.com>
- Some USB disks spin themselves down automatically and need
  scsi_device.allow_restart enabled so they'll spin back up.

* Tue Nov 27 2007 John W. Linville <linville@redhat.com>
- Fix NULL ptr reference in iwlwifi (CVE-2007-5938)

* Tue Nov 27 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc3-git2

* Tue Nov 27 2007 Kyle McMartin <kmcmartin@redhat.com>
- Slurp up drm-mm/agp-mm from git.

* Mon Nov 26 2007 David Woodhouse <dwmw2@redhat.com>
- Build libertas wireless driver
- Include flash translation layers in modules.block list

* Sat Nov 24 2007 David Woodhouse <dwmw2@redhat.com>
- Fix fec_mpc52xx Ethernet driver to use the right skbs

* Wed Nov 21 2007 John W. Linville <linville@redhat.com>
- Resync wireless bits from upstream

* Wed Nov 21 2007 David Woodhouse <dwmw2@redhat.com>
- Fix <linux/kd.h> in userspace.

* Sun Nov 18 2007 Kyle McMartin <kmcmartin@redhat.com>
- 2.6.24-rc3-git1

* Fri Nov 16 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc2-git6

* Thu Nov 15 2007 Chuck Ebbert <cebbert@redhat.com>
- Add DMI based autoloading for the Dell dcdbas driver (#248257)

* Thu Nov 15 2007 Dave Jones <davej@redhat.com>
- Change 'make prep' so that vanilla/ is always the latest upstream.

* Thu Nov 15 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc2-git5

* Wed Nov 14 2007 Dave Jones <davej@redhat.com>
- Reenable sparse checking, v0.4.1 is available.

* Wed Nov 14 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc2-git4

* Tue Nov 13 2007 Dave Jones <davej@redhat.com>
- Disable CONFIG_SECURITY_SELINUX_AVC_STATS in production builds.

* Mon Nov 12 2007 Roland McGrath <roland@redhat.com>
- utrace rebased

* Sun Nov 11 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc2-git2

* Wed Nov 07 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc2

* Mon Nov 05 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc1-git14

* Wed Oct 31 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc1-git8

* Mon Oct 29 2007 Dave Jones <davej@redhat.com>
- 2.6.24-rc1-git5

* Mon Oct 29 2007 Chuck Ebbert <cebbert@redhat.com>
- Enable hamradio drivers.

* Mon Oct 22 2007 Dave Jones <davej@redhat.com>
- 2.6.23-git17

* Thu Oct 18 2007 John W. Linville <linville@redhat.com>
- Latest wireless updates from upstream
- Update rt2x00 to 2.0.11
- Convert some wireless drivers to use round_jiffies_relative

* Wed Oct 17 2007 Dave Jones <davej@redhat.com>
- 2.6.23-git12

* Mon Oct 15 2007 Dave Jones <davej@redhat.com>
- Work around E1000 corrupt EEPROM problem.

* Fri Oct 12 2007 Dave Jones <davej@redhat.com>
- 2.6.23-git2

* Fri Oct 12 2007 Dave Jones <davej@redhat.com>
- Start F9 branch.
