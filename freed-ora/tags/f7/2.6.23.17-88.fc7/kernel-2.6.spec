Summary: The Linux kernel (the core of the GNU/Linux operating system)

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
%define fedora_cvs_origin 3351
%define fedora_build %(R="$Revision: 1.3439 $"; R="${R%% \$}"; R="${R##: 1.}"; expr $R - %{fedora_cvs_origin})

# base_sublevel is the kernel version we're starting with and patching
# on top of -- for example, 2.6.22-rc7-git1 starts with a 2.6.21 base,
# which yields a base_sublevel of 21.
%define base_sublevel 23

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
#define libres

## If this is a released kernel ##
%if 0%{?released_kernel}
# Do we have a 2.6.x.y update to apply?
%define stable_update 17
# Set rpm version accordingly
%if 0%{?stable_update}
%define stablerev .%{stable_update}
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
%define with_debug     %{?_without_debug:     0} %{!?_without_debug:     1}
# kernel-doc
%define with_doc       %{?_without_doc:       0} %{?!_without_doc:       1}
# kernel-headers
%define with_headers   %{?_without_headers:   0} %{?!_without_headers:   1}
# kernel-debuginfo
%define with_debuginfo %{?_without_debuginfo: 0} %{!?_without_debuginfo: 1}

# Additional options for user-friendly one-off kernel building:
#
# Only build the base kernel (--with baseonly):
%define with_baseonly  %{?_with_baseonly:     1} %{?!_with_baseonly:     0}
# Only build the smp kernel (--with smponly):
%define with_smponly   %{?_with_smponly:      1} %{?!_with_smponly:      0}
# Only build the xen kernel (--with xenonly):
%define with_xenonly   %{?_with_xenonly:      1} %{?!_with_xenonly:      0}

# Whether or not to gpg sign modules
%define with_modsign 1

# Whether or not to do C=1 builds with sparse
%define usesparse 0
%if "%fedora" >= "7"
%define usesparse 1
%endif

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
%define pkg_release %{fedora_build}%{?buildid}%{?dist}%{?libres}
%else
%if 0%{?rcrev}
%define rctag .rc%rcrev
%endif
%if 0%{?gitrev}
%define gittag .git%gitrev
%if !0%{?rcrev}
%define rctag .rc0
%endif
%endif
%define pkg_release 0.%{fedora_build}%{?buildid}%{?rctag}%{?gittag}%{?dist}%{?libres}
%endif

# The kernel tarball/base version
%define kversion 2.6.%{base_sublevel}

%define make_target bzImage
%define kernel_image x86

%define xen_hv_cset 11633
%define xen_flags verbose=y crash_debug=y
%define xen_target vmlinuz
%define xen_image vmlinuz

%define KVERREL %{PACKAGE_VERSION}-libre.%{PACKAGE_RELEASE}
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
%define variant -vanilla
%else
%define variant -libre
%define variant_fedora -libre-fedora
%endif

%define using_upstream_branch 0
%if 0%{?upstream_branch:1}
%define using_upstream_branch 1
%define variant -%{upstream_branch}%{?variant_fedora}
%define pkg_release %{upstream_branch_release}.%{pkg_release}
%endif

%if !%{debugbuildsenabled}
%define with_debug 0
%endif

%if !%{with_debuginfo}
%define _enable_debug_packages 0
%endif

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

# if requested, only build xen kernel
%if %{with_xenonly}
%define with_up 0
%define with_smp 0
%define with_pae 0
%define with_kdump 0
%define with_debug 0
%endif

%define all_x86 i386 i586 i686

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
%ifarch i586 i686
%define with_headers 0
%endif

# don't build noarch kernels or headers (duh)
%ifarch noarch
%define with_up 0
%define with_headers 0
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-*.config
%endif

# don't sign modules on these platforms
%ifarch s390x sparc sparc64 ppc alpha
%define with_modsign 0
%endif

# sparse blows up on ppc64
%ifarch ppc64 ppc alpha
%define usesparse 0
%endif

# Per-arch tweaks

%ifarch %{all_x86}
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-i?86*.config
%define image_install_path boot
%define hdrarch i386
# we build always xen i686 HV with pae
%define xen_flags verbose=y crash_debug=y pae=y
%endif

%ifarch x86_64
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-x86_64*.config
%define image_install_path boot
%endif

%ifarch ppc64
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-ppc64*.config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%define kernel_image_elf 1
%define hdrarch powerpc
%endif

%ifarch s390x
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-s390x.config
%define image_install_path boot
%define make_target image
%define kernel_image arch/s390/boot/image
%define hdrarch s390
%endif

%ifarch sparc
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-sparc.config
%define make_target image
%define kernel_image image
%endif

%ifarch sparc64
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-sparc64*.config
%define make_target image
%define kernel_image image
%endif

%ifarch ppc
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-ppc{-,.}*config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%define kernel_image_elf 1
%define hdrarch powerpc
%endif

%ifarch ia64
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-ia64*.config
%define image_install_path boot/efi/EFI/redhat
%define make_target compressed
%define kernel_image vmlinux.gz
# ia64 xen HV doesn't building with debug=y at the moment
%define xen_flags verbose=y crash_debug=y
%define xen_target compressed
%define xen_image vmlinux.gz
%endif

%ifarch alpha alphaev56
%define all_arch_configs $RPM_SOURCE_DIR/kernel-%{version}-alpha*.config
%define image_install_path boot
%define make_target vmlinux
%define kernel_image vmlinux
%endif

%if %{nopatches}
%define with_modsign 0
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
# and we no longer build for 31bit S390.
%define nobuildarches i386 s390

%ifarch %nobuildarches
%define with_up 0
%define with_smp 0
%define with_pae 0
%define with_xen 0
%define with_kdump 0
%define with_debuginfo 0
%define _enable_debug_packages 0
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
%define package_conflicts initscripts < 7.23, udev < 063-6, iptables < 1.3.2-1, ipw2200-firmware < 2.4, selinux-policy-targeted < 1.25.3-14, cpuspeed < 1.2.1-2

#
# The ld.so.conf.d file we install uses syntax older ldconfig's don't grok.
#
%define xen_conflicts glibc < 2.3.5-1, xen < 3.0.1

#
# Packages that need to be installed before the kernel is, because the %post
# scripts use them.
#
%define kernel_prereq  fileutils, module-init-tools, initscripts >= 8.11.1-1, mkinitrd >= 6.0.9-7.1

Name: kernel%{?variant}
Group: System Environment/Kernel
License: GPLv2
Version: %{rpmversion}
Release: %{pkg_release}
# DO NOT CHANGE THE 'ExclusiveArch' LINE TO TEMPORARILY EXCLUDE AN ARCHITECTURE BUILD.
# SET %nobuildarches (ABOVE) INSTEAD
ExclusiveArch: noarch %{all_x86} x86_64 ppc ppc64 ia64 sparc sparc64 s390x alpha alphaev56
ExclusiveOS: Linux
Provides: kernel-drm = 4.3.0
Provides: kernel-drm-nouveau = 6
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{release}
Prereq: %{kernel_prereq}
Conflicts: %{kernel_dot_org_conflicts}
Conflicts: %{package_conflicts}
# We can't let RPM do the dependencies automatic because it'll then pick up
# a correct but undesirable perl dependency from the module headers which
# isn't required for the kernel proper to function
AutoReq: no
AutoProv: yes


#
# List the packages used during the kernel build
#
BuildPreReq: module-init-tools, patch >= 2.5.4, bash >= 2.03, sh-utils, tar
BuildPreReq: bzip2, findutils, gzip, m4, perl, make >= 3.78, diffutils
%if %{with_modsign}
BuildPreReq: gnupg
%endif
BuildRequires: gcc >= 3.4.2, binutils >= 2.12, redhat-rpm-config
%if %{usesparse}
BuildRequires: sparse >= 0.3
%endif
BuildConflicts: rhbuildsys(DiskFree) < 500Mb


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

Source20: kernel-%{version}-i586.config
Source21: kernel-%{version}-i686.config
Source22: kernel-%{version}-i686-debug.config
Source23: kernel-%{version}-i686-PAE.config
Source24: kernel-%{version}-i686-PAE-debug.config

Source25: kernel-%{version}-x86_64.config
Source26: kernel-%{version}-x86_64-debug.config

Source28: kernel-%{version}-ppc.config
Source29: kernel-%{version}-ppc-smp.config
Source30: kernel-%{version}-ppc64.config
Source31: kernel-%{version}-ppc64-kdump.config

Source35: kernel-%{version}-s390x.config

Source36: kernel-%{version}-ia64.config

Source37: kernel-%{version}-i686-xen.config
Source38: kernel-%{version}-x86_64-xen.config
Source39: kernel-%{version}-ia64-xen.config

#Source50: kernel-%{version}-sparc.config
#Source51: kernel-%{version}-sparc64.config
#Source52: kernel-%{version}-sparc64-smp.config

#Source60: kernel-%{version}-alpha.config
#Source61: kernel-%{version}-alphaev56.config
#Source62: kernel-%{version}-alphaev56-smp.config

Source80: config-rhel-generic
Source81: config-rhel-x86-generic

%if %{using_upstream_branch}
### BRANCH PATCH ###
%else
# Here should be only the patches up to the upstream canonical Linus tree.

# For a stable release kernel
%if 0%{?stable_update}
Patch00: patch%{?stablelibre}-2.6.%{base_sublevel}.%{stable_update}.bz2

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

%endif

# these apply even to -vanilla kernels
Patch02: linux-2.6-build-nonintconfig.patch
# CVE-2008-1669
Patch03: linux-2.6.25.2-incremental.patch

%if !%{nopatches}

# Revert -stable pieces we get from elsewhere here
Patch05: linux-2.6-upstream-reverts.patch

Patch10: linux-2.6-utrace-tracehook.patch
Patch11: linux-2.6-utrace-tracehook-ia64.patch
Patch12: linux-2.6-utrace-tracehook-sparc64.patch
Patch13: linux-2.6-utrace-tracehook-s390.patch
Patch14: linux-2.6-utrace-tracehook-um.patch
Patch15: linux-2.6-utrace-tracehook-avr32.patch
Patch16: linux-2.6-utrace-regset.patch
Patch17: linux-2.6-utrace-regset-ia64.patch
Patch18: linux-2.6-utrace-regset-sparc64.patch
Patch19: linux-2.6-utrace-regset-s390.patch
Patch20: linux-2.6-utrace-regset-avr32.patch
Patch21: linux-2.6-utrace-core.patch
Patch22: linux-2.6-utrace-ptrace-compat.patch
Patch23: linux-2.6-utrace-ptrace-compat-ia64.patch
Patch24: linux-2.6-utrace-ptrace-compat-sparc64.patch
Patch25: linux-2.6-utrace-ptrace-compat-s390.patch
Patch26: linux-2.6-utrace-ptrace-compat-avr32.patch
#Patch20: nouveau-drm.patch
Patch30: linux-2.6-sysrq-c.patch

Patch70: linux-2.6-x86-tune-generic.patch
Patch72: linux-2.6-x86-tsc-calibration-2.patch

Patch100: linux-2.6-g5-therm-shutdown.patch
Patch120: linux-2.6-ppc32-ucmpdi2.patch
Patch130: linux-2.6-ibmvscsi-schizo.patch
Patch140: linux-2.6-pmac-zilog.patch

Patch160: linux-2.6-execshield.patch
Patch170: linux-2.6-modsign-mpilib.patch
Patch180: linux-2.6-modsign-crypto.patch
Patch190: linux-2.6-modsign-include.patch
Patch200: linux-2.6-modsign-verify.patch
Patch210: linux-2.6-modsign-ksign.patch
Patch220: linux-2.6-modsign-core.patch
Patch230: linux-2.6-modsign-script.patch

Patch240: linux-2.6-modules-modalias-platform.patch

Patch250: linux-2.6-debug-sizeof-structs.patch
Patch260: linux-2.6-debug-nmi-timeout.patch
Patch270: linux-2.6-debug-taint-vm.patch
Patch280: linux-2.6-debug-spinlock-taint.patch
Patch330: linux-2.6-debug-no-quiet.patch
Patch340: linux-2.6-debug-boot-delay.patch
Patch350: linux-2.6-devmem.patch
Patch370: linux-2.6-crash-driver.patch


Patch400: linux-2.6-scsi-cpqarray-set-master.patch
Patch401: linux-2.6-scsi-async-double-add.patch
Patch404: linux-2.6-scsi-mpt-vmware-fix.patch
Patch405: linux-2.6-scsi-initio-fix-hang-on-load.patch

Patch420: linux-2.6-squashfs.patch
Patch422: linux-2.6-gfs-locking-exports.patch
Patch424: linux-2.6-cifs-fix-incomplete-rcv.patch
Patch425: linux-2.6-cifs-typo-in-cifs_reconnect-fix.patch
Patch426: linux-2.6-cifs-fix-bad-handling-of-EAGAIN.patch
Patch427: linux-2.6-cifs-fix-oops-on-second-mount.patch

Patch430: linux-2.6-net-silence-noisy-printks.patch
Patch431: linux-2.6-netfilter-really-fix-oops-in-nf_nat_move_storage.patch

Patch440: linux-2.6-sha_alignment.patch
Patch450: linux-2.6-input-kill-stupid-messages.patch
Patch451: linux-2.6-input-fix-sync-loss-acer-aspire.patch
Patch452: linux-2.6-input-appletouch-macbook3-trackpad.patch
Patch460: linux-2.6-serial-460800.patch
Patch461: linux-2.6-serial_pnp-add-new-wacom-ids.patch
Patch480: linux-2.6-proc-self-maps-fix.patch
Patch510: linux-2.6-silence-noise.patch
Patch570: linux-2.6-selinux-mprotect-checks.patch
Patch571: linux-2.6-selinux-fix-netlabel-leak.patch
Patch590: linux-2.6-unexport-symbols.patch

Patch600: linux-2.6-vm-silence-atomic-alloc-failures.patch
Patch601: linux-2.6-input-ff-create-limit-memory.patch
# Patch602: linux-2.6-mm-fix-ptrace-access-beyond-vma.patch
Patch603: linux-2.6-dio-fix-cache-invalidation-after-sync-writes.patch
Patch604: linux-2.6-slub-provide-proc-slabinfo.patch
Patch605: linux-2.6-futex-fix-fixups.patch

Patch610: linux-2.6-defaults-fat-utf8.patch
Patch620: linux-2.6-defaults-unicode-vt.patch
Patch630: linux-2.6-defaults-nonmi.patch
Patch635: linux-2.6-defaults-nommconf.patch

Patch660: linux-2.6-libata-ali-atapi-dma.patch
Patch662: linux-2.6-ata-quirk.patch
Patch663: linux-2.6-libata-add-dma-disable-option.patch
Patch664: linux-2.6-libata-dont-fail-revalidation-for-bad-gtf-methods.patch
Patch665: linux-2.6-libata-pata_serverworks-fix-drive-combinations.patch
Patch666: linux-2.6-ppc-pegasos-via-ata-legacy-irq.patch
Patch667: linux-2.6-libata-correct-iordy-handling.patch
Patch671: linux-2.6-libata-tape-max-sectors.patch
Patch672: linux-2.6-libata-work-around-drq-1-err-1-for-tapes.patch
Patch673: linux-2.6-libata-use-stuck-err-for-tapes.patch
# Patch674: linux-2.6-libata-scsi-allow-short-commands.patch
Patch675: linux-2.6-libata-ahci-enable-ahci-mode-before-reset.patch
Patch676: linux-2.6-libata-fix-bogus-lba48-disks.patch

Patch680: linux-2.6-wireless.patch
Patch681: linux-2.6-wireless-pending.patch
Patch690: linux-2.6-at76.patch
Patch691: linux-2.6-rndis_wext.patch
Patch700: linux-2.6-bcm43xx-pci-neuter.patch
Patch701: linux-2.6-zd1211rw-module-alias.patch
Patch702: linux-2.6-b43-module-alias.patch
Patch703: linux-2.6-ath5k-use-soft-wep.patch

Patch710: linux-2.6-netdev-e1000e-01.patch
Patch711: linux-2.6-netdev-e1000e-02.patch
Patch712: linux-2.6-netdev-e1000e-03.patch
Patch713: linux-2.6-netdev-e1000e-04.patch
Patch714: linux-2.6-netdev-e1000e-05.patch
Patch715: linux-2.6-netdev-e1000e-06.patch
Patch716: linux-2.6-netdev-e1000e-07.patch
Patch717: linux-2.6-netdev-e1000e-08.patch
Patch718: linux-2.6-netdev-e1000e-09.patch
Patch719: linux-2.6-netdev-e1000e-10.patch
Patch721: linux-2.6-netdev-e1000-disable-alpm.patch
Patch730: linux-2.6-net-e100-disable-polling.patch

Patch740: linux-2.6-snd-ad1988-fix-spdif-output.patch
Patch741: linux-2.6-alsa-drivers-set-device-links.patch

# Updated firewire stack from linux1394 git
# snap from http://me.in-berlin.de/~s5r6/linux1394/updates/2.6.23/
Patch750: linux-2.6-firewire-git-update.patch

Patch760: linux-2.6-acpi-button-send-initial-state.patch
Patch761: linux-2.6-acpi-video-backlight-rationalize.patch
Patch762: linux-2.6-acpi-video-brightness-bigger-buffer.patch
# Patch763: linux-2.6-acpi-video-fix-multiple-busses.patch
Patch764: linux-2.6-acpi-git-ec-init-fixes.patch
Patch765: linux-2.6-acpi-dont-init-ec-early-with-no-ini.patch
# Patch766: linux-2.6-acpi_ec_early_init_fix.patch

Patch780: linux-2.6-usb-storage-initialize-huawei-e220-properly.patch
Patch781: linux-2.6-usb-autosuspend-default-disable.patch
Patch782: linux-2.6-usb-storage-always-set-the-allow_restart-flag.patch
Patch783: linux-2.6-usb-huawei-fix-init-in-modem-mode.patch

Patch790: linux-2.6-nfs-close-to-open.patch

Patch800: linux-2.6-wakeups-hdaps.patch
Patch801: linux-2.6-wakeups.patch

Patch820: linux-2.6-compile-fixes.patch

# PS3 updates from 2.6.23
# PS3 Wireless support hasn't yet been merged
Patch1300: linux-2.6-ps3-gelic-wireless.patch
# Handle booting from the 2.6.16 kboot
Patch1310: linux-2.6-ps3-legacy-bootloader-hack.patch
# Give the initrd a fighting chance of handling the change to 'ps3disk'
Patch1320: linux-2.6-ps3-storage-alias.patch

Patch1500: linux-2.6-pmtrace-time-fix.patch

Patch1520: linux-2.6-dcdbas-autoload.patch

# Patch up paranoid iret (#431314)
Patch1530: linux-2.6-paranoid-iret-crash-fix.patch

# Patch2300: linux-2.6-freezer-fix-apm-emulation-breakage.patch

%endif

BuildRoot: %{_tmppath}/kernel-%{KVERREL}-root-%{_target_cpu}

%description
The kernel package contains the Linux kernel (vmlinuz), the core of any
GNU/Linux operating system.  The kernel handles the basic functions
of the operating system:  memory allocation, process allocation, device
input and output, etc.

The kernel-libre package is the upstream kernel without the non-Free
blobs it includes by default.

%package debuginfo
Summary: Debug information for package %{name}
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
Provides: %{name}-debuginfo-%{_target_cpu} = %{KVERREL}
%description debuginfo
This package provides debug information for package %{name}
This is required to use SystemTap with %{name}-%{KVERREL}.

%package debuginfo-common
Summary: Kernel source files used by %{name}-debuginfo packages
Group: Development/Debug
Provides: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
%description debuginfo-common
This package is required by %{name}-debuginfo subpackages.
It provides the kernel source files common to all builds.

%package devel
Summary: Development package for building kernel modules to match the kernel.
Group: System Environment/Kernel
AutoReqProv: no
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}
Prereq: /usr/bin/find
%description devel
This package provides kernel headers and makefiles sufficient to build modules
against the kernel package.

%package smp
Summary: The Linux kernel compiled for SMP machines.
Group: System Environment/Kernel
Provides: kernel = %{version}
Provides: kernel-drm = 4.3.0
Provides: kernel-drm-nouveau = 6
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{release}smp
Prereq: %{kernel_prereq}
Conflicts: %{kernel_dot_org_conflicts}
Conflicts: %{package_conflicts}
# upto and including kernel 2.4.9 rpms, the 4Gb+ kernel was called kernel-enterprise
# now that the smp kernel offers this capability, obsolete the old kernel
Obsoletes: kernel-enterprise < 2.4.10
# We can't let RPM do the dependencies automatic because it'll then pick up
# a correct but undesirable perl dependency from the module headers which
# isn't required for the kernel proper to function
AutoReq: no
AutoProv: yes
%description smp
This package includes a SMP version of the Linux kernel. It is
required only on machines with two or more CPUs as well as machines with
hyperthreading technology.

The kernel-libre-smp package is the upstream kernel without the
non-Free blobs it includes by default.

Install the kernel-libre-smp package if your machine uses two or more
CPUs.

%package smp-debuginfo
Summary: Debug information for package %{name}-smp
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
Provides: %{name}-%smp-debuginfo-%{_target_cpu} = %{KVERREL}
%description smp-debuginfo
This package provides debug information for package %{name}-smp
This is required to use SystemTap with %{name}-smp-%{KVERREL}.

%package smp-devel
Summary: Development package for building kernel modules to match the SMP kernel.
Group: System Environment/Kernel
Provides: kernel-smp-devel-%{_target_cpu} = %{rpmversion}-%{release}
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}smp
Provides: kernel-devel = %{rpmversion}-%{release}smp
AutoReqProv: no
Prereq: /usr/bin/find
%description smp-devel
This package provides kernel headers and makefiles sufficient to build modules
against the SMP kernel package.


%package PAE
Summary: The Linux kernel compiled for PAE capable machines.
Group: System Environment/Kernel
Provides: kernel = %{version}
Provides: kernel-drm = 4.3.0
Provides: kernel-drm-nouveau = 6
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{release}PAE
Prereq: %{kernel_prereq}
Conflicts: %{kernel_dot_org_conflicts}
Conflicts: %{package_conflicts}
Obsoletes: kernel-smp < 2.6.17
# We can't let RPM do the dependencies automatic because it'll then pick up
# a correct but undesirable perl dependency from the module headers which
# isn't required for the kernel proper to function
AutoReq: no
AutoProv: yes
%description PAE
This package includes a version of the Linux kernel with support for up to
64GB of high memory. It requires a CPU with Physical Address Extensions (PAE).
The non-PAE kernel can only address up to 4GB of memory.

The kernel-libre-PAE package is the upstream kernel without the
non-Free blobs it includes by default.

Install the kernel-PAE package if your machine has more than 4GB of memory.

%package PAE-debuginfo
Summary: Debug information for package %{name}-PAE
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
Provides: %{name}-%PAE-debuginfo-%{_target_cpu} = %{KVERREL}
%description PAE-debuginfo
This package provides debug information for package %{name}-PAE
This is required to use SystemTap with %{name}-PAE-%{KVERREL}.

%package PAE-devel
Summary: Development package for building kernel modules to match the PAE kernel.
Group: System Environment/Kernel
Provides: kernel-PAE-devel-%{_target_cpu} = %{rpmversion}-%{release}
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}PAE
Provides: kernel-devel = %{rpmversion}-%{release}PAE
AutoReqProv: no
Prereq: /usr/bin/find
%description PAE-devel
This package provides kernel headers and makefiles sufficient to build modules
against the PAE kernel package.

%if %{with_debug}
%package PAE-debug
Summary: The Linux kernel compiled with extra debugging enabled for PAE capable machines.
Group: System Environment/Kernel
Provides: kernel = %{version}
Provides: kernel-drm = 4.3.0
Provides: kernel-drm-nouveau = 6
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{release}PAE-debug
Prereq: %{kernel_prereq}
Conflicts: %{kernel_dot_org_conflicts}
Conflicts: %{package_conflicts}
AutoReq: no
AutoProv: yes
%description PAE-debug
This package includes a version of the Linux kernel with support for up to
64GB of high memory. It requires a CPU with Physical Address Extensions (PAE).
The non-PAE kernel can only address up to 4GB of memory.

The kernel-libre-PAEdebug package is the upstream kernel without the
non-Free blobs it includes by default.

Install the kernel-PAE package if your machine has more than 4GB of memory.

This variant of the kernel has numerous debugging options enabled.
It should only be installed when trying to gather additional information
on kernel bugs, as some of these options impact performance noticably.

%package PAE-debug-debuginfo
Summary: Debug information for package %{name}-PAE-debug
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
Provides: %{name}-debug-debuginfo-%{_target_cpu} = %{KVERREL}
%description PAE-debug-debuginfo
This package provides debug information for package %{name}-PAE-debug

%package PAE-debug-devel
Summary: Development package for building kernel modules to match the kernel.
Group: System Environment/Kernel
AutoReqProv: no
Prereq: /usr/bin/find
Provides: kernel-PAE-debug-devel-%{_target_cpu} = %{rpmversion}-%{release}
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}PAE-debug
Provides: kernel-devel = %{rpmversion}-%{release}PAE-debug
%description PAE-debug-devel
This package provides kernel headers and makefiles sufficient to build modules
against the kernel package.
%endif


%package doc
Summary: Various documentation bits found in the kernel source.
Group: Documentation
Provides: kernel-doc = %{rpmversion}-%{pkgrelease}
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
Provides: kernel-headers = %{rpmversion}-%{pkgrelease}
%description headers
Kernel-headers includes the C header files that specify the interface
between the Linux kernel and userspace libraries and programs.  The
header files define structures and constants that are needed for
building most standard programs and are also needed for rebuilding the
glibc package.


%if %{with_debug}
%package debug
Summary: The Linux kernel compiled with extra debugging enabled.
Group: System Environment/Kernel
Provides: kernel = %{version}
Provides: kernel-drm = 4.3.0
Provides: kernel-drm-nouveau = 6
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{release}debug
Prereq: %{kernel_prereq}
Conflicts: %{kernel_dot_org_conflicts}
Conflicts: %{package_conflicts}
AutoReq: no
AutoProv: yes
%description debug
The kernel package contains the Linux kernel (vmlinuz), the core of any
GNU/Linux operating system.  The kernel handles the basic functions
of the operating system:  memory allocation, process allocation, device
input and output, etc.

The kernel-libre-debug package is the upstream kernel without the
non-Free blobs it includes by default.

This variant of the kernel has numerous debugging options enabled.
It should only be installed when trying to gather additional information
on kernel bugs, as some of these options impact performance noticably.

%package debug-debuginfo
Summary: Debug information for package %{name}-debug
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
Provides: %{name}-debug-debuginfo-%{_target_cpu} = %{KVERREL}
%description debug-debuginfo
This package provides debug information for package %{name}-debug

%package debug-devel
Summary: Development package for building kernel modules to match the kernel.
Group: System Environment/Kernel
AutoReqProv: no
Prereq: /usr/bin/find
Provides: kernel-debug-devel-%{_target_cpu} = %{rpmversion}-%{release}
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}debug
Provides: kernel-devel = %{rpmversion}-%{release}debug
%description debug-devel
This package provides kernel headers and makefiles sufficient to build modules
against the kernel package.
%endif


%package xen
Summary: The Linux kernel compiled for Xen VM operations
Group: System Environment/Kernel
Provides: kernel = %{version}
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{release}xen
Prereq: %{kernel_prereq}
Conflicts: %{kernel_dot_org_conflicts}
Conflicts: %{package_conflicts}
Conflicts: %{xen_conflicts}
# We can't let RPM do the dependencies automatic because it'll then pick up
# a correct but undesirable perl dependency from the module headers which
# isn't required for the kernel proper to function
AutoReq: no
AutoProv: yes
%description xen
This package includes a version of the Linux kernel which
runs in Xen VM. It works for both priviledged and unpriviledged guests.

The kernel-libre-xen package is the upstream kernel without the
non-Free blobs it includes by default.

%package xen-debuginfo
Summary: Debug information for package %{name}-xen
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
Provides: %{name}-xen-debuginfo-%{_target_cpu} = %{KVERREL}
%description xen-debuginfo
This package provides debug information for package %{name}-xen
This is required to use SystemTap with %{name}-xen-%{KVERREL}.

%package xen-devel
Summary: Development package for building kernel modules to match the kernel.
Group: System Environment/Kernel
AutoReqProv: no
Provides: kernel-xen-devel-%{_target_cpu} = %{rpmversion}-%{release}
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}xen
Provides: kernel-devel = %{rpmversion}-%{release}xen
Prereq: /usr/bin/find
%description xen-devel
This package provides kernel headers and makefiles sufficient to build modules
against the kernel package.

%package kdump
Summary: A minimal Linux kernel compiled for kernel crash dumps.
Group: System Environment/Kernel
Provides: kernel = %{version}
Provides: kernel-drm = 4.3.0
Provides: kernel-drm-nouveau = 6
Provides: kernel-%{_target_cpu} = %{rpmversion}-%{release}kdump
Prereq: %{kernel_prereq}
Conflicts: %{kernel_dot_org_conflicts}
Conflicts: %{package_conflicts}
# We can't let RPM do the dependencies automatic because it'll then pick up
# a correct but undesirable perl dependency from the module headers which
# isn't required for the kernel proper to function
AutoReq: no
AutoProv: yes
%description kdump
This package includes a kdump version of the Linux kernel. It is
required only on machines which will use the kexec-based kernel crash dump
mechanism.

The kernel-libre-kdump package is the upstream kernel without the
non-Free blobs it includes by default.

%package kdump-debuginfo
Summary: Debug information for package %{name}-kdump
Group: Development/Debug
Requires: %{name}-debuginfo-common-%{_target_cpu} = %{KVERREL}
Provides: %{name}-kdump-debuginfo-%{_target_cpu} = %{KVERREL}
%description kdump-debuginfo
This package provides debug information for package %{name}-kdump
This is required to use SystemTap with %{name}-kdump-%{KVERREL}.

%package kdump-devel
Summary: Development package for building kernel modules to match the kdump kernel.
Group: System Environment/Kernel
Provides: kernel-kdump-devel-%{_target_cpu} = %{rpmversion}-%{release}
Provides: kernel-devel-%{_target_cpu} = %{rpmversion}-%{release}kdump
Provides: kernel-devel = %{rpmversion}-%{release}kdump
AutoReqProv: no
Prereq: /usr/bin/find
%description kdump-devel
This package provides kernel headers and makefiles sufficient to build modules
against the kdump kernel package.


%prep
#if a rhel kernel, apply the rhel config options
%if 0%{?rhel}
  for i in %{all_arch_configs}
  do
    mv $i $i.tmp
    $RPM_SOURCE_DIR/merge.pl $RPM_SOURCE_DIR/config-rhel-generic $i.tmp > $i
    rm $i.tmp
  done
  for i in $RPM_SOURCE_DIR/kernel-%{version}-{i586,i686,i686-PAE,x86_64}*.config
  do
    echo i is this file  $i
    mv $i $i.tmp
    $RPM_SOURCE_DIR/merge.pl $RPM_SOURCE_DIR/config-rhel-x86-generic $i.tmp > $i
    rm $i.tmp
  done
%endif

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

%if %{with_xenonly}
%if !%{with_xen}
echo "Cannot build --with xenonly, xen build is disabled"
exit 1
%endif
%endif

# First we unpack the kernel tarball.
# If this isn't the first make prep, we use links to the existing clean tarball
# which speeds things up quite a bit.
if [ ! -d kernel-%{kversion}/vanilla ]; then
  # Ok, first time we do a make prep.
  rm -f pax_global_header
%setup -q -n kernel-%{kversion} -c
  mv linux-%{kversion} vanilla
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

cp -rl vanilla linux-%{kversion}.%{_target_cpu}

cd linux-%{kversion}.%{_target_cpu}

patch_command='patch -p1 -F1 -s'
ApplyPatch()
{
  local patch=$1
  shift
  if [ ! -f $RPM_SOURCE_DIR/$patch ]; then
    exit 1;
  fi
  case "$patch" in
  *.bz2) bunzip2 < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *.gz) gunzip < "$RPM_SOURCE_DIR/$patch" | $patch_command ${1+"$@"} ;;
  *) $patch_command ${1+"$@"} < "$RPM_SOURCE_DIR/$patch" ;;
  esac
}

%if %{using_upstream_branch}
### BRANCH APPLY ###
%else

perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION =/" Makefile

# Update to latest upstream.
# released_kernel with stable_update available case
%if 0%{?stable_update}
ApplyPatch patch%{?stablelibre}-2.6.%{base_sublevel}.%{stable_update}.bz2

# non-released_kernel case
%else
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
%endif

%endif

# -stable RC
#ApplyPatch patch-2.6.23.9-rc1.bz2

  $RPM_SOURCE_DIR/deblob-check $RPM_SOURCE_DIR/$patch || exit 1
# This patch adds a "make nonint_oldconfig" which is non-interactive and
# also gives a list of missing options at the end. Useful for automated
# builds (as used in the buildsystem).
ApplyPatch linux-2.6-build-nonintconfig.patch

# fixes applied to -vanilla
# CVE-2008-1669
ApplyPatch linux-2.6.25.2-incremental.patch

%if !%{nopatches}

# Revert -stable pieces we get from elsewhere here
ApplyPatch linux-2.6-upstream-reverts.patch -R

# Roland's utrace ptrace replacement.
ApplyPatch linux-2.6-utrace-tracehook.patch -F2
ApplyPatch linux-2.6-utrace-tracehook-ia64.patch
ApplyPatch linux-2.6-utrace-tracehook-sparc64.patch
ApplyPatch linux-2.6-utrace-tracehook-s390.patch
ApplyPatch linux-2.6-utrace-tracehook-um.patch
ApplyPatch linux-2.6-utrace-tracehook-avr32.patch
ApplyPatch linux-2.6-utrace-regset.patch
ApplyPatch linux-2.6-utrace-regset-ia64.patch
ApplyPatch linux-2.6-utrace-regset-sparc64.patch
ApplyPatch linux-2.6-utrace-regset-s390.patch
ApplyPatch linux-2.6-utrace-regset-avr32.patch
ApplyPatch linux-2.6-utrace-core.patch
ApplyPatch linux-2.6-utrace-ptrace-compat.patch
ApplyPatch linux-2.6-utrace-ptrace-compat-ia64.patch
ApplyPatch linux-2.6-utrace-ptrace-compat-sparc64.patch
ApplyPatch linux-2.6-utrace-ptrace-compat-s390.patch
ApplyPatch linux-2.6-utrace-ptrace-compat-avr32.patch

# setuid /proc/self/maps fix. (dependent on utrace)
ApplyPatch linux-2.6-proc-self-maps-fix.patch

# Disable USB autosuspend by default.
ApplyPatch linux-2.6-usb-autosuspend-default-disable.patch

# some usb disks spin down automatically and need allow_restart
ApplyPatch linux-2.6-usb-storage-always-set-the-allow_restart-flag.patch

# enable sysrq-c on all kernels, not only kexec
ApplyPatch linux-2.6-sysrq-c.patch

# Architecture patches
# x86(-64)
# Compile 686 kernels tuned for Pentium4.
ApplyPatch linux-2.6-x86-tune-generic.patch
# fix tsc calibration
ApplyPatch linux-2.6-x86-tsc-calibration-2.patch

# add vidfail capability;
# without this patch specifying a framebuffer on the kernel prompt would
# make the boot stop if there's no supported framebuffer device; this is bad
# for the installer cd that wants to automatically fall back to textmode
# in that case

#
# PowerPC
#
# Alleviate G5 thermal shutdown problems
ApplyPatch linux-2.6-g5-therm-shutdown.patch
# Temporary hack to work around GCC PR #25724 / #21237
ApplyPatch linux-2.6-ppc32-ucmpdi2.patch
# Fix up ibmvscsi for combined pSeries/iSeries build
ApplyPatch linux-2.6-ibmvscsi-schizo.patch
# Move pmac_zilog to its newly-registered device number
ApplyPatch linux-2.6-pmac-zilog.patch

# Exec shield
ApplyPatch linux-2.6-execshield.patch

#
# GPG signed kernel modules
#
ApplyPatch linux-2.6-modsign-mpilib.patch
ApplyPatch linux-2.6-modsign-crypto.patch
ApplyPatch linux-2.6-modsign-include.patch
ApplyPatch linux-2.6-modsign-verify.patch
ApplyPatch linux-2.6-modsign-ksign.patch
ApplyPatch linux-2.6-modsign-core.patch
ApplyPatch linux-2.6-modsign-script.patch

# re-enable platform driver module aliases
ApplyPatch linux-2.6-modules-modalias-platform.patch

#
# bugfixes to drivers and filesystems
#
# Various low-impact patches to aid debugging.
ApplyPatch linux-2.6-debug-sizeof-structs.patch
ApplyPatch linux-2.6-debug-nmi-timeout.patch
ApplyPatch linux-2.6-debug-taint-vm.patch
ApplyPatch linux-2.6-debug-spinlock-taint.patch

ApplyPatch linux-2.6-debug-no-quiet.patch
ApplyPatch linux-2.6-debug-boot-delay.patch

#
# Make /dev/mem a need-to-know function
#
ApplyPatch linux-2.6-devmem.patch

#
# /dev/crash driver for the crashdump analysis tool
#
ApplyPatch linux-2.6-crash-driver.patch

#
# bluetooth
#

#
# driver core
#

#
# SCSI Bits.
#
# fix cpqarray pci enable
ApplyPatch linux-2.6-scsi-cpqarray-set-master.patch
# fix locking problems w/ async scsi scan
ApplyPatch linux-2.6-scsi-async-double-add.patch
# fix vmware's broken emulation of SCSI controller
ApplyPatch linux-2.6-scsi-mpt-vmware-fix.patch
# fin initio driver
ApplyPatch linux-2.6-scsi-initio-fix-hang-on-load.patch

# Filesystem patches.
# Squashfs
ApplyPatch linux-2.6-squashfs.patch
# gfs2 update
# export symbols for gfs2 locking modules
ApplyPatch linux-2.6-gfs-locking-exports.patch
# CIFS
# fix incomplete rcv and handling of EAGAIN
ApplyPatch linux-2.6-cifs-fix-incomplete-rcv.patch
ApplyPatch linux-2.6-cifs-typo-in-cifs_reconnect-fix.patch
ApplyPatch linux-2.6-cifs-fix-bad-handling-of-EAGAIN.patch
# fix oops mounting filesystem a second time
ApplyPatch linux-2.6-cifs-fix-oops-on-second-mount.patch

# Networking
# Disable easy to trigger printk's.
ApplyPatch linux-2.6-net-silence-noisy-printks.patch
# fix oops in netfilter again
ApplyPatch linux-2.6-netfilter-really-fix-oops-in-nf_nat_move_storage.patch

# Misc fixes
# Fix SHA1 alignment problem on ia64
ApplyPatch linux-2.6-sha_alignment.patch
# The input layer spews crap no-one cares about.
ApplyPatch linux-2.6-input-kill-stupid-messages.patch
# Fix loss of sync on some touchpads
ApplyPatch linux-2.6-input-fix-sync-loss-acer-aspire.patch
# Add support for new macbook trackpads
ApplyPatch linux-2.6-input-appletouch-macbook3-trackpad.patch
# Allow to use 480600 baud on 16C950 UARTs
ApplyPatch linux-2.6-serial-460800.patch
# support new wacom pads
ApplyPatch linux-2.6-serial_pnp-add-new-wacom-ids.patch
# Silence some useless messages that still get printed with 'quiet'
ApplyPatch linux-2.6-silence-noise.patch

# Fix the SELinux mprotect checks on executable mappings
ApplyPatch linux-2.6-selinux-mprotect-checks.patch
# Fix leak in netlabel
ApplyPatch linux-2.6-selinux-fix-netlabel-leak.patch

# Remove kernel-internal functionality that nothing external should use.
ApplyPatch linux-2.6-unexport-symbols.patch

#
# VM related fixes.
#
# Silence GFP_ATOMIC failures.
ApplyPatch linux-2.6-vm-silence-atomic-alloc-failures.patch
# don't let input FF drivers allocate too much memory
ApplyPatch linux-2.6-input-ff-create-limit-memory.patch
# fix ptrace access beyond vma
#ApplyPatch linux-2.6-mm-fix-ptrace-access-beyond-vma.patch
# fix invalid data read after direct IO write
ApplyPatch linux-2.6-dio-fix-cache-invalidation-after-sync-writes.patch
# re-add /proc/slabinfo
ApplyPatch linux-2.6-slub-provide-proc-slabinfo.patch
# fix oops with futex on uniprocessor machine
ApplyPatch linux-2.6-futex-fix-fixups.patch

# Changes to upstream defaults.
# Use UTF-8 by default on VFAT.
ApplyPatch linux-2.6-defaults-fat-utf8.patch
# Use unicode VT's by default.
ApplyPatch linux-2.6-defaults-unicode-vt.patch
# Disable NMI watchdog by default.
ApplyPatch linux-2.6-defaults-nonmi.patch
# disable mmconf
ApplyPatch linux-2.6-defaults-nommconf.patch

# Disable ATAPI DMA on ALI chipsets.
ApplyPatch linux-2.6-libata-ali-atapi-dma.patch
# libata: don't initialize sg in ata_exec_internal() if DMA_NONE
# ia64 ata quirk
ApplyPatch linux-2.6-ata-quirk.patch
# add libata.dma kernel option
ApplyPatch linux-2.6-libata-add-dma-disable-option.patch
# fix Pegasos libata glitches
ApplyPatch linux-2.6-ppc-pegasos-via-ata-legacy-irq.patch
# fix resume failure on some systems                                  
ApplyPatch linux-2.6-libata-dont-fail-revalidation-for-bad-gtf-methods.patch   
# serverworks is broken with some drive combinations                  
ApplyPatch linux-2.6-libata-pata_serverworks-fix-drive-combinations.patch 
# fix libata IORDY handling
ApplyPatch linux-2.6-libata-correct-iordy-handling.patch
# fix ATA tape drives
ApplyPatch linux-2.6-libata-tape-max-sectors.patch
ApplyPatch linux-2.6-libata-work-around-drq-1-err-1-for-tapes.patch
ApplyPatch linux-2.6-libata-use-stuck-err-for-tapes.patch
# allow 12-byte SCSI commands for ATAPI devices
# ApplyPatch linux-2.6-libata-scsi-allow-short-commands.patch
# fix ahci reset
ApplyPatch linux-2.6-libata-ahci-enable-ahci-mode-before-reset.patch
# work around broken lba48 disks
ApplyPatch linux-2.6-libata-fix-bogus-lba48-disks.patch

# post-2.6.23 wireless patches from upstream
ApplyPatch linux-2.6-wireless.patch
# pre-2.6.25 wireless patches from upstream
ApplyPatch linux-2.6-wireless-pending.patch

# Add misc wireless bits from upstream wireless tree
ApplyPatch linux-2.6-at76.patch
ApplyPatch linux-2.6-rndis_wext.patch

# avoid bcm3xx vs bcm43xx-mac80211 PCI ID conflicts
ApplyPatch linux-2.6-bcm43xx-pci-neuter.patch
# add module alias for "zd1211rw-mac80211"
ApplyPatch linux-2.6-zd1211rw-module-alias.patch
# add module alias for "bcm43xx-mac80211"
ApplyPatch linux-2.6-b43-module-alias.patch
# Make ath5k use software WEP
ApplyPatch linux-2.6-ath5k-use-soft-wep.patch

# e1000e goes here
# latest Intel driver for ich9
ApplyPatch linux-2.6-netdev-e1000e-01.patch
ApplyPatch linux-2.6-netdev-e1000e-02.patch
ApplyPatch linux-2.6-netdev-e1000e-03.patch
ApplyPatch linux-2.6-netdev-e1000e-04.patch
ApplyPatch linux-2.6-netdev-e1000e-05.patch
ApplyPatch linux-2.6-netdev-e1000e-06.patch
ApplyPatch linux-2.6-netdev-e1000e-07.patch
ApplyPatch linux-2.6-netdev-e1000e-08.patch
ApplyPatch linux-2.6-netdev-e1000e-09.patch
ApplyPatch linux-2.6-netdev-e1000e-10.patch
# disable link power savings, should fix bad eeprom checksum too
ApplyPatch linux-2.6-netdev-e1000-disable-alpm.patch
# fix e100 crash on init
ApplyPatch linux-2.6-net-e100-disable-polling.patch

# ALSA
#
# Fix #253777 (again)
ApplyPatch linux-2.6-snd-ad1988-fix-spdif-output.patch
# Fix missing controls on some devices caused by missing sysfs links
ApplyPatch linux-2.6-alsa-drivers-set-device-links.patch

# misc
#
# FireWire updates and fixes
ApplyPatch linux-2.6-firewire-git-update.patch

# USB
#
# fix init of huawei device
ApplyPatch linux-2.6-usb-storage-initialize-huawei-e220-properly.patch
ApplyPatch linux-2.6-usb-huawei-fix-init-in-modem-mode.patch

# ACPI patches
# suspend/resume fixes
ApplyPatch linux-2.6-acpi-button-send-initial-state.patch
# fix multiple ACPI brightness problems (F8#427518)
ApplyPatch linux-2.6-acpi-video-backlight-rationalize.patch
ApplyPatch linux-2.6-acpi-video-brightness-bigger-buffer.patch
# copy EC fixes from F8
ApplyPatch linux-2.6-acpi-git-ec-init-fixes.patch
ApplyPatch linux-2.6-acpi-dont-init-ec-early-with-no-ini.patch

# NFSv4: Ensure that we wait for the CLOSE request to complete
ApplyPatch linux-2.6-nfs-close-to-open.patch

# Fix excessive wakeups
# Make hdaps timer only tick when in use.
ApplyPatch linux-2.6-wakeups-hdaps.patch
ApplyPatch linux-2.6-wakeups.patch

# Pending PS3 patches
ApplyPatch linux-2.6-ps3-gelic-wireless.patch
# Temporary hacks
ApplyPatch linux-2.6-ps3-legacy-bootloader-hack.patch
ApplyPatch linux-2.6-ps3-storage-alias.patch

# Fix time distortion in pm_trace (bz 250238)
ApplyPatch linux-2.6-pmtrace-time-fix.patch

# autload the Dell dcdbas driver via DMI
ApplyPatch linux-2.6-dcdbas-autoload.patch

# Patch up paranoid iret (#431314)
ApplyPatch linux-2.6-paranoid-iret-crash-fix.patch

#
# misc small stuff to make things compile
#
C=$(wc -l $RPM_SOURCE_DIR/linux-2.6-compile-fixes.patch | awk '{print $1}')
if [ "$C" -gt 10 ]; then
ApplyPatch linux-2.6-compile-fixes.patch
fi

# END OF PATCH APPLICATIONS

# Any further pre-build tree manipulations happen here.

chmod +x scripts/checkpatch.pl

%endif

cp %{SOURCE10} Documentation/

mkdir configs

cp -f %{all_arch_configs} .

%if !%{with_debug}
rm -f kernel-%{version}-*-debug.config
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

# get rid of unwanted files resulting from patch fuzz
find . \( -name "*.orig" -o -name "*~" \) -exec rm -f {} \; >/dev/null

cd ..

# Unpack the Xen tarball.
%if %{includexen}
cp %{SOURCE2} .
if [ -d xen ]; then
  rm -rf xen
fi
%setup -D -T -q -n kernel-%{version} -a1
cd xen
# Any necessary hypervisor patches go here

%endif


###
### build
###
%build

%if %{usesparse}
%define sparse_mflags	C=1
%endif

#
# Create gpg keys for signing the modules
#
%if %{with_modsign}
gpg --homedir . --batch --gen-key %{SOURCE11}
gpg --homedir . --export --keyring ./kernel.pub Red > extract.pub
make linux-%{kversion}.%{_target_cpu}/scripts/bin2c
linux-%{kversion}.%{_target_cpu}/scripts/bin2c ksign_def_public_key __initdata < extract.pub > linux-%{kversion}.%{_target_cpu}/crypto/signature/key.h
%endif

BuildKernel() {
    MakeTarget=$1
    KernelImage=$2
    Flavour=$3

    # Pick the right config file for the kernel we're building
    if [ -n "$Flavour" ] ; then
      Config=kernel-%{version}-%{_target_cpu}-$Flavour.config
      DevelDir=/usr/src/kernels/%{KVERREL}-$Flavour-%{_target_cpu}
      DevelLink=/usr/src/kernels/%{KVERREL}$Flavour-%{_target_cpu}
    else
      Config=kernel-%{version}-%{_target_cpu}.config
      DevelDir=/usr/src/kernels/%{KVERREL}-%{_target_cpu}
      DevelLink=
    fi

    KernelVer=%{version}-libre.%{release}$Flavour
    echo BUILDING A KERNEL FOR $Flavour %{_target_cpu}...

    # make sure EXTRAVERSION says what we want it to say
    perl -p -i -e "s/^EXTRAVERSION.*/EXTRAVERSION = %{?stablerev}-libre.%{release}$Flavour/" Makefile

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

    if [ "$KernelImage" == "x86" ]; then
       KernelImage=arch/$Arch/boot/bzImage
    fi

    make -s ARCH=$Arch %{oldconfig_target} > /dev/null
    make -s ARCH=$Arch %{?_smp_mflags} $MakeTarget %{?sparse_mflags}
    make -s ARCH=$Arch %{?_smp_mflags} modules %{?sparse_mflags} || exit 1

    # Start installing the results
%if %{with_debuginfo}
    mkdir -p $RPM_BUILD_ROOT/usr/lib/debug/boot
    mkdir -p $RPM_BUILD_ROOT/usr/lib/debug/%{image_install_path}
%endif
    mkdir -p $RPM_BUILD_ROOT/%{image_install_path}
    install -m 644 .config $RPM_BUILD_ROOT/boot/config-$KernelVer
    install -m 644 System.map $RPM_BUILD_ROOT/boot/System.map-$KernelVer
    touch $RPM_BUILD_ROOT/boot/initrd-$KernelVer.img
    cp $KernelImage $RPM_BUILD_ROOT/%{image_install_path}/vmlinuz-$KernelVer
    if [ -f arch/$Arch/boot/zImage.stub ]; then
      cp arch/$Arch/boot/zImage.stub $RPM_BUILD_ROOT/%{image_install_path}/zImage.stub-$KernelVer || :
    fi

    if [ "$Flavour" == "kdump" ]; then
        cp vmlinux $RPM_BUILD_ROOT/%{image_install_path}/vmlinux-$KernelVer
        rm -f $RPM_BUILD_ROOT/%{image_install_path}/vmlinuz-$KernelVer
    fi

    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer
    make -s ARCH=$Arch INSTALL_MOD_PATH=$RPM_BUILD_ROOT modules_install KERNELRELEASE=$KernelVer

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
    mkdir -p $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cd include
    cp -a acpi config keys linux math-emu media mtd net pcmcia rdma rxrpc scsi sound video asm asm-generic $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    cp -a `readlink asm` $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    if [ "$Arch" = "x86_64" ]; then
      cp -a asm-i386 $RPM_BUILD_ROOT/lib/modules/$KernelVer/build/include
    fi
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
    mkdir -p $RPM_BUILD_ROOT/usr/lib/debug/lib/modules/$KernelVer
    cp vmlinux $RPM_BUILD_ROOT/usr/lib/debug/lib/modules/$KernelVer
%endif

    find $RPM_BUILD_ROOT/lib/modules/$KernelVer -name "*.ko" -type f >modnames

    # gpg sign the modules
%if %{with_modsign}
    gcc -o scripts/modsign/mod-extract scripts/modsign/mod-extract.c -Wall
    KEYFLAGS="--no-default-keyring --homedir .."
    KEYFLAGS="$KEYFLAGS --secret-keyring ../kernel.sec"
    KEYFLAGS="$KEYFLAGS --keyring ../kernel.pub"
    export KEYFLAGS

    for i in `cat modnames`
    do
      sh ./scripts/modsign/modsign.sh $i Red
      mv -f $i.signed $i
    done
    unset KEYFLAGS
%endif
    # mark modules executable so that strip-to-file can strip them
    cat modnames | xargs chmod u+x

    # Generate a list of modules for SCSI, sata/pata, and networking.
    touch $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.scsi
    touch $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.libata
    touch $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.networking
    for i in `cat modnames | grep drivers | grep -v drivers\/ata`
    do
      if [ $(nm $i |grep --count scsi_add_host) -ne 0 ];
      then
        basename `echo $i` >> $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.scsi
      fi
    done

    for i in `cat modnames | grep drivers\/ata`
    do
      if [ $(nm $i |grep --count ata_device_add) -ne 0 -o $(nm $i |grep --count ata_pci_init_one) -ne 0 ];
      then
        basename `echo $i` >> $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.libata
      fi
    done

    for i in `cat modnames |grep drivers`
    do
      if [ $(nm $i |grep --count register_netdev) -ne 0 ];
      then
        basename `echo $i` >> $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.networking
      fi
    done

    # detect missing or incorrect license tags
    for i in `cat modnames`
    do
      echo -n "$i "
      /sbin/modinfo -l $i >> modinfo
    done
    cat modinfo |\
      grep -v "^GPL" |
      grep -v "^Dual BSD/GPL" |\
      grep -v "^Dual MPL/GPL" |\
      grep -v "^GPL and additional rights" |\
      grep -v "^GPL v2" && exit 1
    rm -f modinfo
    rm -f modnames
    # remove files that will be auto generated by depmod at rpm -i time
    for i in alias ccwmap dep ieee1394map inputmap isapnpmap ofmap pcimap seriomap symbols usbmap
    do
      rm -f $RPM_BUILD_ROOT/lib/modules/$KernelVer/modules.$i
    done

    # Move the devel headers out of the root file system
    mkdir -p $RPM_BUILD_ROOT/usr/src/kernels
    mv $RPM_BUILD_ROOT/lib/modules/$KernelVer/build $RPM_BUILD_ROOT/$DevelDir
    ln -sf ../../..$DevelDir $RPM_BUILD_ROOT/lib/modules/$KernelVer/build
    [ -z "$DevelLink" ] || ln -sf `basename $DevelDir` $RPM_BUILD_ROOT/$DevelLink
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
  install -m 644 xen.gz $RPM_BUILD_ROOT/%{image_install_path}/xen.gz-%{KVERREL}
  install -m 755 xen-syms $RPM_BUILD_ROOT/boot/xen-syms-%{KVERREL}
  cd ..
%endif
%endif

cd linux-%{kversion}.%{_target_cpu}

%if %{with_debug}
BuildKernel %make_target %kernel_image debug
%if %{with_pae}
BuildKernel %make_target %kernel_image PAE-debug
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
BuildKernel %make_target %kernel_image kdump
%endif

###
### Special hacks for debuginfo subpackages.
###

# This macro is used by %%install, so we must redefine it before that.
%define debug_package %{nil}

%if %{with_debuginfo}
%ifnarch noarch
%global __debug_package 1
%files debuginfo-common
%defattr(-,root,root)
/usr/src/debug/kernel-%{kversion}/linux-%{kversion}.%{_target_cpu}
%if %{includexen}
%if %{with_xen}
/usr/src/debug/kernel-%{kversion}/xen
%endif
%endif
%dir /usr/src/debug
%dir /usr/lib/debug
%dir /usr/lib/debug/%{image_install_path}
%dir /usr/lib/debug/lib
%dir /usr/lib/debug/lib/modules
%dir /usr/lib/debug/usr/src/kernels
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
rm -f $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf
cat > $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf <<\EOF
# This directive teaches ldconfig to search in nosegneg subdirectories
# and cache the DSOs there with extra bit 0 set in their hwcap match
# fields.  In Xen guest kernels, the vDSO tells the dynamic linker to
# search in nosegneg subdirectories and to match this extra hwcap bit
# in the ld.so.cache file.
hwcap 0 nosegneg
EOF
chmod 444 $RPM_BUILD_ROOT/etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf
%endif
%endif

%if %{with_doc}
mkdir -p $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}/Documentation

# sometimes non-world-readable files sneak into the kernel source tree
chmod -R a+r *
# copy the source over
tar cf - Documentation | tar xf - -C $RPM_BUILD_ROOT/usr/share/doc/kernel-doc-%{kversion}
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
###
### clean
###

%clean
rm -rf $RPM_BUILD_ROOT

###
### scripts
###

%post
if [ `uname -i` == "x86_64" -o `uname -i` == "i386" ]; then
  if [ -f /etc/sysconfig/kernel ]; then
    /bin/sed -i -e 's/^DEFAULTKERNEL=kernel-smp$/DEFAULTKERNEL=kernel/' /etc/sysconfig/kernel || exit $?
  fi
fi
/sbin/new-kernel-pkg --package kernel-libre --mkinitrd --depmod --install %{KVERREL} || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --add-kernel %{KVERREL} || exit $?
#fi

%post devel
if [ -f /etc/sysconfig/kernel ]
then
    . /etc/sysconfig/kernel || exit $?
fi
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ] ; then
  pushd /usr/src/kernels/%{KVERREL}-%{_target_cpu} > /dev/null
  /usr/bin/find . -type f | while read f; do hardlink -c /usr/src/kernels/*FC*/$f $f ; done
  popd > /dev/null
fi

%post smp
/sbin/new-kernel-pkg --package kernel-libre-smp --mkinitrd --depmod --install %{KVERREL}smp || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --add-kernel %{KVERREL}smp || exit $?
#fi

%post smp-devel
if [ -f /etc/sysconfig/kernel ]
then
    . /etc/sysconfig/kernel || exit $?
fi
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ] ; then
  pushd /usr/src/kernels/%{KVERREL}-smp-%{_target_cpu} > /dev/null
  /usr/bin/find . -type f | while read f; do hardlink -c /usr/src/kernels/*FC*/$f $f ; done
  popd > /dev/null
fi

%post PAE
if [ -f /etc/sysconfig/kernel ]; then
    /bin/sed -i -e 's/^DEFAULTKERNEL=kernel-smp$/DEFAULTKERNEL=kernel-PAE/' /etc/sysconfig/kernel
fi
/sbin/new-kernel-pkg --package kernel-libre-PAE --mkinitrd --depmod --install %{KVERREL}PAE || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --add-kernel %{KVERREL}PAE || exit $?
#fi

%post PAE-devel
if [ -f /etc/sysconfig/kernel ]
then
    . /etc/sysconfig/kernel || exit $?
fi
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ] ; then
  pushd /usr/src/kernels/%{KVERREL}-PAE-%{_target_cpu} > /dev/null
  /usr/bin/find . -type f | while read f; do hardlink -c /usr/src/kernels/*FC*/$f $f ; done
  popd > /dev/null
fi


%if %{with_debug}
%post debug
/sbin/new-kernel-pkg --package kernel-libre-debug --mkinitrd --depmod --install %{KVERREL}debug || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --add-kernel %{KVERREL}debug || exit $?
#fi

%post debug-devel
if [ -f /etc/sysconfig/kernel ]
then
    . /etc/sysconfig/kernel || exit $?
fi
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ] ; then
  pushd /usr/src/kernels/%{KVERREL}-debug-%{_target_cpu} > /dev/null
  /usr/bin/find . -type f | while read f; do hardlink -c /usr/src/kernels/*FC*/$f $f ; done
  popd > /dev/null
fi

%post PAE-debug
if [ -f /etc/sysconfig/kernel ]; then
    /bin/sed -i -e 's/^DEFAULTKERNEL=kernel-smp$/DEFAULTKERNEL=kernel-PAE/' /etc/sysconfig/kernel
fi
/sbin/new-kernel-pkg --package kernel-libre-PAE --mkinitrd --depmod --install %{KVERREL}PAE-debug || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --add-kernel %{KVERREL}PAE || exit $?
#fi

%post PAE-debug-devel
if [ -f /etc/sysconfig/kernel ]
then
    . /etc/sysconfig/kernel || exit $?
fi
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ] ; then
  pushd /usr/src/kernels/%{KVERREL}-PAE-debug-%{_target_cpu} > /dev/null
  /usr/bin/find . -type f | while read f; do hardlink -c /usr/src/kernels/*FC*/$f $f ; done
  popd > /dev/null
fi
%endif

%post xen
if [ `uname -i` == "x86_64" -o `uname -i` == "i386" ]; then
  if [ -f /etc/sysconfig/kernel ]; then
    /bin/sed -i -e 's/^DEFAULTKERNEL=kernel-xen[0U]/DEFAULTKERNEL=kernel-xen/' /etc/sysconfig/kernel || exit $?
  fi
fi
if [ -e /proc/xen/xsd_kva -o ! -d /proc/xen ]; then
	/sbin/new-kernel-pkg --package kernel-libre-xen --mkinitrd --depmod --install --multiboot=/%{image_install_path}/xen.gz-%{KVERREL} %{KVERREL}xen || exit $?
else
	/sbin/new-kernel-pkg --package kernel-libre-xen --mkinitrd --depmod --install %{KVERREL}xen || exit $?
fi
if [ -x /sbin/ldconfig ]
then
    /sbin/ldconfig -X || exit $?
fi
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --add-kernel %{KVERREL}xen || exit $?
#fi

%post xen-devel
if [ -f /etc/sysconfig/kernel ]
then
    . /etc/sysconfig/kernel || exit $?
fi
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ] ; then
  pushd /usr/src/kernels/%{KVERREL}-xen-%{_target_cpu} > /dev/null
  /usr/bin/find . -type f | while read f; do hardlink -c /usr/src/kernels/*FC*/$f $f ; done
  popd > /dev/null
fi

%post kdump
/sbin/new-kernel-pkg --package kernel-libre-kdump --mkinitrd --depmod --install %{KVERREL}kdump || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --add-kernel %{KVERREL}kdump || exit $?
#fi

%post kdump-devel
if [ -f /etc/sysconfig/kernel ]
then
    . /etc/sysconfig/kernel || exit $?
fi
if [ "$HARDLINK" != "no" -a -x /usr/sbin/hardlink ] ; then
  pushd /usr/src/kernels/%{KVERREL}-kdump-%{_target_cpu} > /dev/null
  /usr/bin/find . -type f | while read f; do hardlink -c /usr/src/kernels/*FC*/$f $f ; done
  popd > /dev/null
fi


%preun
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL} || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --remove-kernel %{KVERREL} || exit $?
#fi

%preun smp
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}smp || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --remove-kernel %{KVERREL}smp || exit $?
#fi

%preun PAE
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}PAE || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --remove-kernel %{KVERREL}PAE || exit $?
#fi

%preun kdump
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}kdump || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --remove-kernel %{KVERREL}kdump || exit $?
#fi

%if %{with_debug}
%preun debug
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}debug || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --remove-kernel %{KVERREL}debug || exit $?
#fi

%preun PAE-debug
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}PAE-debug || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --remove-kernel %{KVERREL}PAE || exit $?
#fi
%endif

%preun xen
/sbin/new-kernel-pkg --rminitrd --rmmoddep --remove %{KVERREL}xen || exit $?
#if [ -x /sbin/weak-modules ]
#then
#    /sbin/weak-modules --remove-kernel %{KVERREL}xen || exit $?
#fi

###
### file lists
###

# This is %{image_install_path} on an arch where that includes ELF files,
# or empty otherwise.
%define elf_image_install_path %{?kernel_image_elf:%{image_install_path}}

%if %{with_up}
%if %{with_debuginfo}
%ifnarch noarch
%files debuginfo
%defattr(-,root,root)
%if "%{elf_image_install_path}" != ""
/usr/lib/debug/%{elf_image_install_path}/*-%{KVERREL}.debug
%endif
/usr/lib/debug/lib/modules/%{KVERREL}
/usr/lib/debug/usr/src/kernels/%{KVERREL}-%{_target_cpu}
%endif
%endif

%files
%defattr(-,root,root)
/%{image_install_path}/vmlinuz-%{KVERREL}
/boot/System.map-%{KVERREL}
#/boot/symvers-%{KVERREL}.gz
/boot/config-%{KVERREL}
%dir /lib/modules/%{KVERREL}
/lib/modules/%{KVERREL}/kernel
/lib/modules/%{KVERREL}/build
/lib/modules/%{KVERREL}/source
/lib/modules/%{KVERREL}/extra
/lib/modules/%{KVERREL}/updates
/lib/modules/%{KVERREL}/weak-updates
/lib/modules/%{KVERREL}/modules.scsi
/lib/modules/%{KVERREL}/modules.libata
/lib/modules/%{KVERREL}/modules.networking
%ghost /boot/initrd-%{KVERREL}.img

%files devel
%defattr(-,root,root)
%verify(not mtime) /usr/src/kernels/%{KVERREL}-%{_target_cpu}
%endif


%if %{with_headers}
%files headers
%defattr(-,root,root)
/usr/include/*
%endif


%if %{with_debug}
%if %{with_debuginfo}
%ifnarch noarch
%files debug-debuginfo
%defattr(-,root,root)
%if "%{elf_image_install_path}" != ""
/usr/lib/debug/%{elf_image_install_path}/*-%{KVERREL}debug.debug
%endif
/usr/lib/debug/lib/modules/%{KVERREL}debug
/usr/lib/debug/usr/src/kernels/%{KVERREL}-debug-%{_target_cpu}
%endif

%files debug
%defattr(-,root,root)
/%{image_install_path}/vmlinuz-%{KVERREL}debug
/boot/System.map-%{KVERREL}debug
#/boot/symvers-%{KVERREL}debug.gz
/boot/config-%{KVERREL}debug
%dir /lib/modules/%{KVERREL}debug
/lib/modules/%{KVERREL}debug/kernel
/lib/modules/%{KVERREL}debug/build
/lib/modules/%{KVERREL}debug/source
/lib/modules/%{KVERREL}debug/extra
/lib/modules/%{KVERREL}debug/updates
/lib/modules/%{KVERREL}debug/weak-updates
/lib/modules/%{KVERREL}debug/modules.scsi
/lib/modules/%{KVERREL}debug/modules.libata
/lib/modules/%{KVERREL}debug/modules.networking
%ghost /boot/initrd-%{KVERREL}debug.img

%files debug-devel
%defattr(-,root,root)
%verify(not mtime) /usr/src/kernels/%{KVERREL}-debug-%{_target_cpu}
/usr/src/kernels/%{KVERREL}debug-%{_target_cpu}
%endif
%endif



%if %{with_pae}
%if %{with_debuginfo}
%ifnarch noarch
%files PAE-debuginfo
%defattr(-,root,root)
%if "%{elf_image_install_path}" != ""
/usr/lib/debug/%{elf_image_install_path}/*-%{KVERREL}PAE.debug
%endif
/usr/lib/debug/lib/modules/%{KVERREL}PAE
/usr/lib/debug/usr/src/kernels/%{KVERREL}-PAE-%{_target_cpu}
%endif
%endif

%files PAE
%defattr(-,root,root)
/%{image_install_path}/vmlinuz-%{KVERREL}PAE
/boot/System.map-%{KVERREL}PAE
#/boot/symvers-%{KVERREL}PAE.gz
/boot/config-%{KVERREL}PAE
%dir /lib/modules/%{KVERREL}PAE
/lib/modules/%{KVERREL}PAE/kernel
/lib/modules/%{KVERREL}PAE/build
/lib/modules/%{KVERREL}PAE/source
/lib/modules/%{KVERREL}PAE/extra
/lib/modules/%{KVERREL}PAE/updates
/lib/modules/%{KVERREL}PAE/weak-updates
/lib/modules/%{KVERREL}PAE/modules.scsi
/lib/modules/%{KVERREL}PAE/modules.libata
/lib/modules/%{KVERREL}PAE/modules.networking
%ghost /boot/initrd-%{KVERREL}PAE.img

%files PAE-devel
%defattr(-,root,root)
%verify(not mtime) /usr/src/kernels/%{KVERREL}-PAE-%{_target_cpu}
/usr/src/kernels/%{KVERREL}PAE-%{_target_cpu}

%if %{with_debug}
%if %{with_debuginfo}
%ifnarch noarch
%files PAE-debug-debuginfo
%defattr(-,root,root)
%if "%{elf_image_install_path}" != ""
/usr/lib/debug/%{elf_image_install_path}/*-%{KVERREL}PAE-debug.debug
%endif
/usr/lib/debug/lib/modules/%{KVERREL}PAE-debug
/usr/lib/debug/usr/src/kernels/%{KVERREL}-PAE-debug-%{_target_cpu}
%endif

%files PAE-debug
%defattr(-,root,root)
/%{image_install_path}/vmlinuz-%{KVERREL}PAE-debug
/boot/System.map-%{KVERREL}PAE-debug
#/boot/symvers-%{KVERREL}PAE-debug.gz
/boot/config-%{KVERREL}PAE-debug
%dir /lib/modules/%{KVERREL}PAE-debug
/lib/modules/%{KVERREL}PAE-debug/kernel
/lib/modules/%{KVERREL}PAE-debug/build
/lib/modules/%{KVERREL}PAE-debug/source
/lib/modules/%{KVERREL}PAE-debug/extra
/lib/modules/%{KVERREL}PAE-debug/updates
/lib/modules/%{KVERREL}PAE-debug/weak-updates
/lib/modules/%{KVERREL}PAE-debug/modules.scsi
/lib/modules/%{KVERREL}PAE-debug/modules.libata
/lib/modules/%{KVERREL}PAE-debug/modules.networking
%ghost /boot/initrd-%{KVERREL}PAE-debug.img

%files PAE-debug-devel
%defattr(-,root,root)
%verify(not mtime) /usr/src/kernels/%{KVERREL}-PAE-debug-%{_target_cpu}
/usr/src/kernels/%{KVERREL}PAE-debug-%{_target_cpu}
%endif
%endif
# PAE
%endif


%if %{with_smp}
%if %{with_debuginfo}
%ifnarch noarch
%files smp-debuginfo
%defattr(-,root,root)
%if "%{elf_image_install_path}" != ""
/usr/lib/debug/%{elf_image_install_path}/*-%{KVERREL}smp.debug
%endif
/usr/lib/debug/lib/modules/%{KVERREL}smp
/usr/lib/debug/usr/src/kernels/%{KVERREL}-smp-%{_target_cpu}
%endif
%endif

%files smp
%defattr(-,root,root)
/%{image_install_path}/vmlinuz-%{KVERREL}smp
/boot/System.map-%{KVERREL}smp
#/boot/symvers-%{KVERREL}smp.gz
/boot/config-%{KVERREL}smp
%dir /lib/modules/%{KVERREL}smp
/lib/modules/%{KVERREL}smp/kernel
/lib/modules/%{KVERREL}smp/build
/lib/modules/%{KVERREL}smp/source
/lib/modules/%{KVERREL}smp/extra
/lib/modules/%{KVERREL}smp/updates
/lib/modules/%{KVERREL}smp/weak-updates
/lib/modules/%{KVERREL}smp/modules.scsi
/lib/modules/%{KVERREL}smp/modules.libata
/lib/modules/%{KVERREL}smp/modules.networking
%ghost /boot/initrd-%{KVERREL}smp.img

%files smp-devel
%defattr(-,root,root)
%verify(not mtime) /usr/src/kernels/%{KVERREL}-smp-%{_target_cpu}
/usr/src/kernels/%{KVERREL}smp-%{_target_cpu}
%endif


%if %{includexen}
%if %{with_xen}
%if %{with_debuginfo}
%ifnarch noarch
%files xen-debuginfo
%defattr(-,root,root)
%if "%{elf_image_install_path}" != ""
/usr/lib/debug/%{elf_image_install_path}/*-%{KVERREL}xen.debug
%endif
/usr/lib/debug/lib/modules/%{KVERREL}xen
/usr/lib/debug/usr/src/kernels/%{KVERREL}-xen-%{_target_cpu}
/usr/lib/debug/boot/xen*-%{KVERREL}.debug
%endif
%endif

%files xen
%defattr(-,root,root)
/%{image_install_path}/vmlinuz-%{KVERREL}xen
/boot/System.map-%{KVERREL}xen
#/boot/symvers-%{KVERREL}xen.gz
/boot/config-%{KVERREL}xen
/%{image_install_path}/xen.gz-%{KVERREL}
/boot/xen-syms-%{KVERREL}
%dir /lib/modules/%{KVERREL}xen
/lib/modules/%{KVERREL}xen/kernel
%verify(not mtime) /lib/modules/%{KVERREL}xen/build
/lib/modules/%{KVERREL}xen/source
/etc/ld.so.conf.d/kernelcap-%{KVERREL}.conf
/lib/modules/%{KVERREL}xen/extra
/lib/modules/%{KVERREL}xen/updates
/lib/modules/%{KVERREL}xen/weak-updates
/lib/modules/%{KVERREL}xen/modules.scsi
/lib/modules/%{KVERREL}xen/modules.libata
/lib/modules/%{KVERREL}xen/modules.networking
%ghost /boot/initrd-%{KVERREL}xen.img

%files xen-devel
%defattr(-,root,root)
%verify(not mtime) /usr/src/kernels/%{KVERREL}-xen-%{_target_cpu}
/usr/src/kernels/%{KVERREL}xen-%{_target_cpu}
%endif
%endif

%if %{with_kdump}
%if %{with_debuginfo}
%ifnarch noarch
%files kdump-debuginfo
%defattr(-,root,root)
%if "%{image_install_path}" != ""
/usr/lib/debug/%{image_install_path}/*-%{KVERREL}kdump.debug
%endif
/usr/lib/debug/lib/modules/%{KVERREL}kdump
/usr/lib/debug/usr/src/kernels/%{KVERREL}-kdump-%{_target_cpu}
%endif
%endif

%files kdump
%defattr(-,root,root)
/%{image_install_path}/vmlinux-%{KVERREL}kdump
/boot/System.map-%{KVERREL}kdump
#/boot/symvers-%{KVERREL}kdump.gz
/boot/config-%{KVERREL}kdump
%dir /lib/modules/%{KVERREL}kdump
/lib/modules/%{KVERREL}kdump/kernel
/lib/modules/%{KVERREL}kdump/build
/lib/modules/%{KVERREL}kdump/source
/lib/modules/%{KVERREL}kdump/extra
/lib/modules/%{KVERREL}kdump/updates
/lib/modules/%{KVERREL}kdump/weak-updates
/lib/modules/%{KVERREL}kdump/modules.scsi
/lib/modules/%{KVERREL}kdump/modules.libata
/lib/modules/%{KVERREL}kdump/modules.networking
%ghost /boot/initrd-%{KVERREL}kdump.img

%files kdump-devel
%defattr(-,root,root)
%verify(not mtime) /usr/src/kernels/%{KVERREL}-kdump-%{_target_cpu}
/usr/src/kernels/%{KVERREL}kdump-%{_target_cpu}
%endif

# only some architecture builds need kernel-doc

%if %{with_doc}
%files doc
%defattr(-,root,root)
%{_datadir}/doc/kernel-doc-%{kversion}/Documentation/*
%dir %{_datadir}/doc/kernel-doc-%{kversion}/Documentation
%dir %{_datadir}/doc/kernel-doc-%{kversion}
%endif

%changelog
* Wed May 14 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.17-88
- Increment version.

* Wed May 14 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.17-87
- Security fix: CVE-2008-1669 (taken from 2.6.25.2)

* Mon Mar 31 2008 Jarod Wilson <jwilson@redhat.com> 2.6.23.17-86
- Patch up paranoid iret cs reg corruption crasher on x86_64 (F8 #431314)

* Thu Mar 27 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.17-85
- Revert ACPI EC init patch that has been reverted in F8.

* Wed Mar 26 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.17-84
- Revert ACPI video change that was reverted in 2.6.25.

* Wed Mar 26 2008 Dave Jones <davej@redhat.com>
- NFSv4: Ensure that we wait for the CLOSE request to complete (#439095)

* Thu Feb 28 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.17-81
- Linux 2.6.23.17

* Sun Feb 10 2008 Alexandre Oliva <lxoliva@fsfla.org> 2.6.23.15-libre.80 Thu May  8 2008
- Deblob linux tarball.
- Provide kernel-headers from kernel-libre-headers.
- Provide kernel-doc from kernel-libre-doc.

* Sun Feb 10 2008 Dave Airlie <airlied@redhat.com> 2.6.23.15-80
- CVE-2008-0600 - local root vulnerability in vmsplice

* Fri Feb 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.15-79
- Linux 2.6.23.15

* Wed Feb 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-78
- Bump version.

* Wed Feb 06 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-77
- Use the upstream fix for futex locking.
- Fix oops in netfilter (#259501)

* Tue Feb 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-76
- ACPI: sync EC patches with F8

* Tue Feb 05 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-75
- ACPI: fix multiple problems with brightness controls (F8#427518)

* Tue Feb 05 2008 Jarod Wilson <jwilson@redhat.com> 2.6.23.14-74
- FireWire updates from linux1394-git tree, should resolve a
  bunch of FireWire disk issues for people

* Wed Jan 30 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-73
- Fix segfaults from using vdso=2 (F8#427641)

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-72
- Fix the initio driver broken in 2.6.23. (F8#390531)

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-71
- Drop obsolete ptrace patch.

* Fri Jan 25 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-70
- Add support for new Macbook touchpads (F8#426574)

* Wed Jan 23 2008 John W. Linville <linville@redhat.com> 2.6.23.14-69
- Latest wireless updates from upstream
- Remove obsolete ath5k and rtl8180 patches
- Add rndis_wext driver

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-68
- Fix futex oops on uniprocessor machine. (F8#429412)

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-67
- Work around broken Seagate LBA48 disks (F8#429364)

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-66
- Fix memory leak in netlabel code (#352281)

* Tue Jan 22 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-65
- Autoload the Dell dcdbas driver like in F8 (#326041)

* Sat Jan 19 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.14-64
- Revert CONFIG_PHYSICAL_START on x86_64.

* Fri Jan 18 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-63
- Fix loss of mouse sync on some systems (F8#427697)
- Revert "libata: allow short SCSI commands for ATAPI devices" (#429353)

* Thu Jan 17 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.14-62
- Set x86 CONFIG_PHYSICAL_START=0x400000 (#309751)

* Thu Jan 17 2008 John W. Linville <linville@redhat.com> 2.6.23.14-61
- More wireless fixes headed for 2.6.24
- More wireless updates headed for 2.6.25

* Mon Jan 14 2008 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.14.60
- Linux 2.6.23.14

* Thu Jan 10 2008 John W. Linville <linville@redhat.com> 2.6.23.13-59
- rt2500usb thinko fix
- b43 N phy pre-support updates
- ath5k cleanups and beacon fixes

* Wed Jan 09 2008 John W. Linville <linville@redhat.com> 2.6.23.13-58
- More wireless fixes for 2.6.24
- More wireless update for 2.6.25
- Enable CONFIG_NL80211

* Wed Jan 09 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.13-57
- Linux 2.6.23.13

* Tue Jan 08 2008 Chuck Ebbert <cebbert@redhat.com> 2.6.23.12-56
- Restore /proc/slabinfo

* Fri Jan 04 2008 John W. Linville <linville@redhat.com> 2.6.23.12-55
- Another round of wireless fixes headed for 2.6.24
- Another round of wireless updates headed for 2.6.25

>>>>>>> 1.3424
* Fri Dec 21 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.12-54
- USB: Use upstream version of the Huawei USB modem fix.

* Wed Dec 19 2007 John W. Linville <linville@redhat.com> 2.6.23.12-53
- Some wireless fixes headed for 2.6.24
- Some wireless updates headed for 2.6.25

* Tue Dec 18 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.12-52
- Linux 2.6.23.12
- Add fixed version of APM emulation patch removed in 2.6.23.10

* Thu Dec 14 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.10-51
- Add compile fixes.

* Thu Dec 14 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.10-50
- Linux 2.6.23.10

* Thu Dec 14 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-48
- Update utrace to latest.

* Tue Dec 11 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-47
- libata: fix AHCI controller reset
- libata: fix ATAPI tape drives (#243568)
- libata: allow short SCSI commands for ATAPI devices

* Wed Dec 05 2007 John W. Linville <linville@redhat.com> 2.6.23.9-45
- Update wireless bits from current upstream (resync w/ F-8)
- Drop patch to use "old format" firwmare for b43 (unmaintainable)

* Mon Dec 03 2007 Jarod Wilson <jwilson@redhat.com> 2.6.23.9-44
- Improved FireWire OHCI 1.0 Isochronous Receive support (#344851)

* Fri Nov 30 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-43
- ALSA: fix missing controls on some drivers
- ACPI: send initial button state on startup

* Fri Nov 30 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-42
- Disable e1000 link power management

* Wed Nov 28 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-41
- Fix further bugs in init of Huawei USB modem
- Fix libata handling of IO ready test

* Tue Nov 27 2007 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.9-40
- Some USB disks spin themselves down automatically and need
  scsi_device.allow_restart enabled so they'll spin back up.

* Tue Nov 27 2007 John W. Linville <linville@redhat.com> 2.6.23.9-39
- Fix NULL ptr reference in iwlwifi (CVE-2007-5938)

* Tue Nov 27 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.9-37
- Fix SPDIF audio on AD1988 codec (#253777)

* Mon Nov 26 2007 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.9-36
- Linux 2.6.23.9

* Mon Nov 26 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.8-35
- Set CONFIG_USB_DEVICE_CLASS (#362221)

* Thu Nov 22 2007 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.8-33
- Linux 2.6.23.9-rc1

* Wed Nov 21 2007 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.8-32
- Update linux-2.6-wireless.patch to match F-8.
- Fix oops in netfilter NAT module (#259501)
- libata: fix resume on some systems
- libata: fix pata_serverworks with some drive combinations

* Tue Nov 20 2007 Kyle McMartin <kmcmartin@redhat.com> 2.6.23.8-31
- Linux 2.6.23.8

* Thu Nov 15 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-30
- Build in the Crypto crc32c algorithm for iSCSI.

* Wed Nov 14 2007 Jarod Wilson <jwilson@redhat.com> 2.6.23.1-29
- Initial FireWire OHCI 1.0 Isochronous Receive support (#344851)

* Mon Nov 12 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-27
- Disable USB autosuspend by default.
- Fix oops in CIFS when mounting a filesystem a second time.
- Restore platform module autoloading, e.g. pcspkr.
- Fix failure to boot on 486DX4 (and possibily other CPUs.)

* Fri Nov 09 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-26
- Fix the earlier misapplied raid5 patch.
- Fix race in NFS that can cause data corruption.

* Fri Nov 09 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-25
- libata sata_sis:
   fix SCSI transfer lengths and SCR read function.

* Fri Nov 09 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-24
- Fix oopses in the networking code (l2tp, teql).

* Fri Nov 09 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-23
- Update utrace, fixing some powerpc bugs.
- Disable precise scheduler CPU accounting (#340161).

* Tue Nov 06 2007 Dave Airlie <airlied@linux.ie> 2.6.23.1-22
- copy drm fixes from Fedora 8 (bug 228414) + i915 allocation

* Wed Oct 31 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-20
- Copy iwl wireless updates from Fedora 8 (#349981).
- Fix read after direct IO write returning stale data.
- Fix hang in ptrace trying to access beyond end of VMA.
- Add Powerbook HID input driver to x86_64 build (#358721).

* Tue Oct 30 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-18
- Use upstream fixes for monotonic stime/utime.
- Another ACPI suspend/resume fix.
- Disable PCI mmconfig by default (like Fedora 8.)

* Tue Oct 30 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-17
- CIFS: fix reconnect when incomplete packet is received

* Mon Oct 29 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-15
- Fix oops for some x86_64 systems with AGP (#249174, #336281)
- Properly synchronize IRQs
- CIFS: fix file corruption when server returns EAGAIN (F8 #357001)
- serial: support new Wacom pads (F8 #352811)
- md/raid5: fix data corruption race
- i386: TSC calibration fixes
- x86_64: fix races in TLB flushing code

* Mon Oct 29 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-14
- Enable hamradio drivers.

* Wed Oct 24 2007 Roland McGrath <roland@redhat.com> 2.6.23.1-13
- Install System.map in kernel-devel packages.

* Tue Oct 23 2007 John W. Linville <linville@redhat.com> 2.6.23.1-12
- remove problematic hunk from ath5k fixes patch

* Mon Oct 22 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-11
- Don't create /sys/module/nousb/ (bz #242715)

* Fri Oct 19 2007 John W. Linville <linville@redhat.com> 2.6.23.1-10
- iwl4965-base.c: fix off-by-one errors
- ipw2100: send WEXT scan events
- rt2x00: Add new rt73usb USB ID
- zd1211rw, fix oops when ejecting install media
- rt2x00: Fix residual check in PLCP calculations.
- rtl8187: Fix more frag bit checking, rts duration calc
- iwlwifi: set correct base rate for A band in rs_dbgfs_set_mcs
- iwlwifi: Fix rate setting in probe request for HW scan

* Thu Oct 18 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-9
- Add missing ACPI patch.
- Switch to upstream libata DMA disable code
  (libata.pata_dma parameter is now libata.dma)
- Remove some new/experimental items from config.

* Thu Oct 18 2007 John W. Linville <linville@redhat.com> 2.6.23.1-7
- avoid null ptr dereference in rx path of zd1201 driver

* Wed Oct 17 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-6
- latest ath5k from Fedora 8
- fix another suspend/resume bug

* Wed Oct 17 2007 John W. Linville <linville@redhat.com> 2.6.23.1-5
- iee80211: fix an endian bug
- mac80211: change BSSID list key to (BSSID, SSID, frequency) tuple
- mac80211: make honor IW_SCAN_THIS_ESSID
- mac80211: don't fail IE parse if junk at end of frame

* Fri Oct 12 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23.1-4
- Linux 2.6.23.1
- Firewire multi-LUN fixes (#242254)
- Fix lockdep problems in Firewire
- Fix CPU time accounting

* Wed Oct 10 2007 Chuck Ebbert <cebbert@redhat.com> 2.6.23-1
- Linux 2.6.23
