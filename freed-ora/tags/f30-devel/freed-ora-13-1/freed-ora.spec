Name:           freed-ora
Version:        13
Release:        1

Summary:        Linux-libre Freed-ora packages
License:        BSD
URL:            https://linux-libre.fsfla.org/freed-ora
Source0:        RPM-GPG-KEY-freed-ora-linux-libre
Source1:        freed-ora.repo
Source2:        freed-ora-testing.repo
Source3:        freed-ora-detesting.repo
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-root-%(%{__id_u} -n)
BuildArch:      noarch

%description
Packages to configure dnf/yum to use Linux-libre's Freed-ora
repositories, and to avoid the installation of non-Free Software
distributed by Fedora.

%package release
Summary: Linux-libre Freed-ora Repository Configuration
Group: System Environment/Base

Provides: freed-ora
Obsoletes: freed-ora

%description release
The Freed-ora repositories, maintained by the Linux-libre project,
offer kernels that are as close as possible to those offered by
Fedora, except for changes made so that they do not contain or request
non-Free Software.

%prep
echo "Nothing to prep"

%build
echo "Nothing to build"

%install
rm -rf $RPM_BUILD_ROOT

# Create dirs
install -d -m755 \
  $RPM_BUILD_ROOT%{_sysconfdir}/pki/rpm-gpg  \
  $RPM_BUILD_ROOT%{_sysconfdir}/yum.repos.d

# GPG Key
%{__install} -Dp -m644 \
    %{SOURCE0} \
    $RPM_BUILD_ROOT%{_sysconfdir}/pki/rpm-gpg

# Yum .repo files
%{__install} -p -m644 %{SOURCE1} %{SOURCE2} %{SOURCE3} \
    $RPM_BUILD_ROOT%{_sysconfdir}/yum.repos.d


%clean
rm -rf $RPM_BUILD_ROOT

%files release
%defattr(-,root,root,-)
%{_sysconfdir}/pki/rpm-gpg/*
%config(noreplace) %{_sysconfdir}/yum.repos.d/*

%package freedom
Summary: Meta-package that conflicts with non-Free components in Fedora
Group: System Environment/Base

%define kernelpkgs kernel, kernel-PAE, kernel-PAE-core, kernel-PAE-debuginfo, kernel-PAE-devel, kernel-PAE-modules, kernel-PAE-modules-extra, kernel-PAEdebug, kernel-PAEdebug-core, kernel-PAEdebug-debuginfo, kernel-PAEdebug-devel, kernel-PAEdebug-modules, kernel-PAEdebug-modules-extra, kernel-core, kernel-debug, kernel-debug-core, kernel-debug-debuginfo, kernel-debug-devel, kernel-debug-modules, kernel-debug-modules-extra, kernel-debuginfo, kernel-debuginfo-common-i686, kernel-debuginfo-common-x86_64, kernel-devel, kernel-doc, kernel-firmware, kernel-headers < 4.17.11, kernel-cross-headers < 4.17.11, kernel-modules, kernel-modules-extra, kernel-tools < 4.15, kernel-tools-debuginfo < 4.15, kernel-tools-libs < 4.15, kernel-tools-libs-devel < 4.15, perf < 4.15, perf-debuginfo < 4.15, python-perf < 4.15, python-perf-debuginfo < 4.15
Obsoletes: kernel-libre-headers < 4.17.11
Obsoletes: kernel-libre-cross-headers < 4.17.11
Obsoletes: kernel-libre-tools < 4.15
Obsoletes: kernel-libre-tools-debuginfo < 4.15
Obsoletes: kernel-libre-tools-libs < 4.15
Obsoletes: kernel-libre-tools-libs-devel < 4.15
Obsoletes: perf-libre < 4.15
Obsoletes: perf-libre-debuginfo < 4.15
Obsoletes: python-perf-libre < 4.15
Obsoletes: python-perf-libre-debuginfo < 4.15

# We can't conflict with these, because kernel-libre packages provide them.
# Conflicts: %{kernelpkgs}

# Licenses for alsa-firmware and zd1211-firmware look fine, but there
# are plenty of sourceless binary images in the packages.
Conflicts: aic94xx-firmware
Conflicts: alsa-firmware
Conflicts: alsa-tools-firmware
Conflicts: ar9170-firmware
Conflicts: atmel-firmware
Conflicts: bcm283x-firmware
Conflicts: bfa-firmware
Conflicts: crystalhd-firmware
Conflicts: cx18-firmware
# These firmware-* packages could be ok, but their only use is to deal
# with non-Free Software:
Conflicts: firmware-addon-dell
Conflicts: firmware-extract
Conflicts: firmware-tools
Conflicts: iguanaIR-firmware
Conflicts: ipw2100-firmware
Conflicts: ipw2200-firmware
Conflicts: ipw3945-ucode
Conflicts: iscan-firmware
Conflicts: isight-firmware-tools
Conflicts: ivtv-firmware
Conflicts: iwl100-firmware
Conflicts: iwl1000-firmware
Conflicts: iwl105-firmware
Conflicts: iwl135-firmware
Conflicts: iwl2000-firmware
Conflicts: iwl2030-firmware
Conflicts: iwl3160-firmware
Conflicts: iwl3945-firmware
Conflicts: iwl4965-firmware
Conflicts: iwl5000-firmware
Conflicts: iwl5150-firmware
Conflicts: iwl6000-firmware
Conflicts: iwl6000g2a-firmware
Conflicts: iwl6000g2b-firmware
Conflicts: iwl6050-firmware
Conflicts: iwl7260-firmware
Conflicts: iwlwifi-3945-ucode
Conflicts: iwlwifi-4965-ucode
Conflicts: libertas-sd8686-firmware
Conflicts: libertas-sd8787-firmware
Conflicts: libertas-usb8388-firmware
Conflicts: libertas-usb8388-olpc-firmware
Conflicts: linux-firmware
# lulzbot-marlin-firmware is Free Software
Conflicts: microcode_ctl
Conflicts: midisport-firmware
Conflicts: netxen-firmware
Conflicts: ql2100-firmware
Conflicts: ql2200-firmware
Conflicts: ql23xx-firmware
Conflicts: ql2400-firmware
Conflicts: ql2500-firmware
Conflicts: rt61pci-firmware
Conflicts: rt73usb-firmware
Conflicts: sigrok-firmware
# This could be ok, but the license tag is wrong, and it mentions the
# non-Free bits in its README:
Conflicts: sigrok-firmware-filesystem
# These are ok:
# Conflicts: sigrok-firmware-free
# Conflicts: sigrok-firmware-fx2lafw
Conflicts: sigrok-firmware-nonfree
Conflicts: ueagle-atm4-firmware
Conflicts: uhd-firmware
# Hmm, I wonder if either of these became lulzbot-marlin-firmware,
# and was already Free Software.  Anyway, they are long gone.
Conflicts: ultimaker-marlin-firmware
Conflicts: ultimaker2-marlin-firmware
Conflicts: xorg-x11-drv-ati-firmware
Conflicts: zd1211-firmware

%description freedom
The freed-ora-freedom package is intended to conflict with all known
non-Free components in Fedora, except for kernel packages, that are
dealt with as an install-time error.

We cannot conflict with the kernel packages because the corresponding
kernel-libre packages must provide the same names to satisfy other
system-wide dependencies.  Unfortunately, this means it is up to you
to verify that you have none of kernel, kernel-core, kernel-modules*,
kernel-doc, kernel-headers, kernel-devel, kernel-firmware,
kernel-cross-headers, and their -PAE, -debug, etc variants
or their debuginfo.

Once you do that, you may want to
exclude=kernel*,*-firmware,*-ucode,microcode_ctl from Fedora
repos, to avoid accidents.  The conflict markers in this package will
actually prevent the installation of most of these packages, but not
of the kernel ones: these will only get warnings from triggers.

%files freedom
%defattr(-,root,root,-)

%pre freedom
if rpm -qa kernel kernel-\* |
   sed '
/-libre-/d;
/^kernel-tools-/d;
s,.*,Error: & conflicts with freed-ora-freedom,' |
   grep . >&2; then
  exit 1
fi

%post freedom
if test -f /etc/sysconfig/kernel &&
   grep '^DEFAULTKERNEL=kernel\(-\(debug\|PAE\)*\)\?$' \
     /etc/sysconfig/kernel > /dev/null; then
  sed -i 's,^\(DEFAULTKERNEL=kernel\)\(-\(debug\|PAE\)*\)\?$,\1-libre\2,' \
    /etc/sysconfig/kernel
fi

%triggerin freedom -- %{kernelpkgs}
echo Error: newly-installed package conflicts with freed-ora-freedom >&2
exit 1

%changelog
* Wed Aug 15 2018 Alexandre Oliva <lxoliva@fsfla.org> - 13-1
- Adjust pre conflicts to ignore kernel-headers and kernel-cross-headers.
- Update mirrors.

* Mon Mar 26 2018 Alexandre Oliva <lxoliva@fsfla.org> - 12-1
- Adjust pre conflicts to ignore kernel-tools.
- Drop kernel-tools and perf from package descriptions.

* Mon Mar 26 2018 Alexandre Oliva <lxoliva@fsfla.org> - 11-1
- Add kernel-cross-headers to pseudo-conflict trigger list.
- Drop kernel-tools* and perf* >= 4.15 from the pseudo-conflict trigger list.
- Obsolete kernel-libre-tools* and perf-libre* < 4.15.

* Mon Jan  8 2018 Alexandre Oliva <lxoliva@fsfla.org> - 10-1
- Update mirror list: espoch->uta.
- Use https rather than http where available.
- Shuffle mirror lists a bit, so as to spread the load.
- Update conflict list: added bcm283x-firmware.

* Wed Oct 21 2015 Alexandre Oliva <lxoliva@fsfla.org> - 9-1
- Switch from F- to f directories.

* Sun Dec  7 2014 Alexandre Oliva <lxoliva@fsfla.org> - 8-1
- Updated gnulinux.si mirror (was gnulinux.tv).
- Dropped lsd.ic.unicamp.br mirror.
- Added espoch and cedia mirrors in Ecuador.
- Added *-core, *-modules, *-modules-extra, kernel-tools*, *perf* packages 
to Fedora kernelpkgs, reported in triggers.
- Added conflicts to alsa-tools-firmware, firmware-addon-dell,
firmware-extract, firmware-tools, iguanaIR-firmware, ipw3945-ucode,
isight-firmware-tools, iwl105-firmware, iwl135-firmare,
iwl2000-firmware, iwl2030-firmware, iwl31360-firmware,
iwl7260-firmware, iwlwifi-3945-ucode, iwlwifi-4965-ucode,
libertas-sd8787-firmware, libertas-usb8388-olpc-firmware,
sigrok-firmware, sigrok-firmware-filesystem, sigrok-firmware-nonfree,
udh-firmware, ultimaker-marlin-firmware, and
ultimaker2-marlin-firmware.
- Justify conflicts with firmware-addon-dell, firmware-extract,
firmware-tools, and sigrok-firmware-filesystem.
- Do NOT conflict with sigrok-firmware-free and sigrok-firmware-fx2lafw.

* Fri Jul 22 2011 Alexandre Oliva <lxoliva@fsfla.org> - 7-1
- Added linux.org.tr mirror.

* Mon Feb  7 2011 Alexandre Oliva <lxoliva@fsfla.org> - 6-1
- Conflict with iwl100-firmware.

* Tue Jan 18 2011 Alexandre Oliva <lxoliva@fsfla.org> - 5-1
- Conflict with netxen-firmware.

* Fri Jan  7 2011 Alexandre Oliva <lxoliva@fsfla.org> - 4-1
- Conflict with iwl6000g2b-firmware.

* Fri Dec 24 2010 Alexandre Oliva <lxoliva@fsfla.org> - 3-1
- Conflict with iwl6000g2a-firmware.

* Wed Dec  1 2010 Alexandre Oliva <lxoliva@fsfla.org> - 2-1
- Moved repo configuration to new -release package, per convention.
- Package base name is now srpm only.
- Completed set of excluded firmware packages with those only in
Everything/ but not in Fedora/.
- Added pre script to -freedom, to report non-Free kernels.
- Trigger -freedom error report on non-Free kernel package installs.
- Change kernel default to -libre on -freedom.

* Mon Nov 29 2010 Alexandre Oliva <lxoliva@fsfla.org> - 1-1
- Initial RPM release.
