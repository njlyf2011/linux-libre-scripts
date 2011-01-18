Name:           freed-ora
Version:        5
Release:        1

Summary:        Linux-libre Freed-ora packages
License:        BSD
URL:            http://linux-libre.fsfla.org/freed-ora
Source0:        RPM-GPG-KEY-freed-ora-linux-libre
Source1:        freed-ora.repo
Source2:        freed-ora-testing.repo
Source3:        freed-ora-detesting.repo
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-root-%(%{__id_u} -n)
BuildArch:      noarch

%description
Packages to configure yum to use Linux-libre's Freed-ora repositories,
and to avoid the installation of non-Free Software distributed by
Fedora.

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

%define kernelpkgs kernel-firmware, kernel-headers, kernel-doc, kernel, kernel-devel, kernel-PAE, kernel-PAE-devel, kernel-debug, kernel-debug-devel, kernel-PAEdebug, kernel-PAEdebug-devel, kernel-debuginfo, kernel-debug-debuginfo, kernel-PAE-debuginfo, kernel-PAEdebug-debuginfo, kernel-debuginfo-common-i686, kernel-debuginfo-common-x86_64, perf

# We can't conflict with these, because kernel-libre packages provide them.
# Conflicts: %{kernelpkgs}

# Licenses for alsa-firmware and zd1211-firmware look fine, but there
# are plenty of sourceless binary images in the packages.
Conflicts: aic94xx-firmware
Conflicts: alsa-firmware
Conflicts: ar9170-firmware
Conflicts: atmel-firmware
Conflicts: bfa-firmware
Conflicts: crystalhd-firmware
Conflicts: cx18-firmware
Conflicts: ipw2100-firmware
Conflicts: ipw2200-firmware
Conflicts: iscan-firmware
Conflicts: ivtv-firmware
Conflicts: iwl1000-firmware
Conflicts: iwl3945-firmware
Conflicts: iwl4965-firmware
Conflicts: iwl5000-firmware
Conflicts: iwl5150-firmware
Conflicts: iwl6000-firmware
Conflicts: iwl6000g2a-firmware
Conflicts: iwl6000g2b-firmware
Conflicts: iwl6050-firmware
Conflicts: libertas-sd8686-firmware
Conflicts: libertas-usb8388-firmware
Conflicts: linux-firmware
Conflicts: midisport-firmware
Conflicts: netxen-firmware
Conflicts: ql2100-firmware
Conflicts: ql2200-firmware
Conflicts: ql23xx-firmware
Conflicts: ql2400-firmware
Conflicts: ql2500-firmware
Conflicts: rt61pci-firmware
Conflicts: rt73usb-firmware
Conflicts: ueagle-atm4-firmware
Conflicts: xorg-x11-drv-ati-firmware
Conflicts: zd1211-firmware
Conflicts: microcode_ctl

%description freedom
The freed-ora-freedom package is intended to conflict with all known
non-Free components in Fedora, except for kernel packages, that are
dealt with as an install-time error.

We cannot conflict with the kernel packages because the corresponding
kernel-libre packages must provide the same names to satisfy other
system-wide dependencies.  Unfortunately, this means it is up to you
to verify that you have none of kernel, kernel-doc, kernel-headers,
kernel-devel, kernel-firmware, perf, and their -PAE, -debug, etc
variants.  

Once you do that, you may want to
exclude=kernel*,perf,*-firmware,microcode_ctl from Fedora repos, to
avoid accidents.  The conflict markers in this package will actually
prevent the installation of most of these packages, but not of the
kernel ones: these will only get warnings from triggers.

%files freedom
%defattr(-,root,root,-)

%pre freedom
if rpm -qa kernel kernel-\* perf | 
   sed '/-libre-/d; s,.*,Error: & conflicts with freed-ora-freedom,' |
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

* Sun Nov 29 2010 Alexandre Oliva <lxoliva@fsfla.org> - 1-1
- Initial RPM release.
