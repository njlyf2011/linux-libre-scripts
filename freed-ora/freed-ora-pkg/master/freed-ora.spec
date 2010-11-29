Name:           freed-ora
Version:        1
Release:        1
Summary:        Linux-libre Freed-ora Repository Configuration

Group:          System Environment/Base
License:        BSD
URL:            http://linux-libre.fsfla.org/freed-ora
Source0:        RPM-GPG-KEY-freed-ora-linux-libre
Source1:        freed-ora.repo
Source2:        freed-ora-testing.repo
Source3:        freed-ora-detesting.repo
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}-root-%(%{__id_u} -n)
BuildArch:      noarch

%description
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

%files
%defattr(-,root,root,-)
%{_sysconfdir}/pki/rpm-gpg/*
%config(noreplace) %{_sysconfdir}/yum.repos.d/*

%package freedom
Summary: Meta-package that conflicts with non-Free components in Fedora
Group: System Environment/Base

# We can't conflict with these, because kernel-libre packages provide them.
# Conflicts: kernel kernel-firmware kernel-devel kernel-headers kernel-doc perf
# Conflicts: kernel-PAE kernel-PAE-devel
# Conflicts: kernel-debug kernel-debug-devel

# Licenses for alsa-firmware and zd1211-firmware look fine, but there
# are plenty of sourceless binary images in the packages.
Conflicts: alsa-firmware
Conflicts: ar9170-firmware
Conflicts: atmel-firmware
Conflicts: ipw2100-firmware
Conflicts: ipw2200-firmware
Conflicts: ivtv-firmware
Conflicts: iwl1000-firmware
Conflicts: iwl3945-firmware
Conflicts: iwl4965-firmware
Conflicts: iwl5000-firmware
Conflicts: iwl5150-firmware
Conflicts: iwl6000-firmware
Conflicts: iwl6050-firmware
Conflicts: libertas-usb8388-firmware
Conflicts: linux-firmware
Conflicts: ql2100-firmware
Conflicts: ql2200-firmware
Conflicts: ql23xx-firmware
Conflicts: ql2400-firmware
Conflicts: ql2500-firmware
Conflicts: rt61pci-firmware
Conflicts: rt73usb-firmware
Conflicts: zd1211-firmware
Conflicts: microcode_ctl

%description freedom 
The freed-ora-freedom package is intended to conflict with all known
non-Free components in Fedora, except for kernel packages.

We cannot conflict with the kernel packages because the corresponding
kernel-libre packages must provide the same names to satisfy other
system-wide dependencies.  Unfortunately, this means it is up to you
to verify that you have none of kernel, kernel-doc, kernel-headers,
kernel-devel, kernel-firmware, perf, and their -PAE, -debug, etc
variants.  

Once you do that, you may want to
exclude=kernel*,perf,*-firmware,microcode_ctl from Fedora repos.

%files freedom
%defattr(-,root,root,-)

%package freedom-report
Summary: Meta-package that conflicts with kernel components in Fedora
Group: System Environment/Base

Requires: freed-ora-freedom

Conflicts: kernel kernel-firmware kernel-devel kernel-headers kernel-doc perf
Conflicts: kernel-PAE kernel-PAE-devel
Conflicts: kernel-debug kernel-debug-devel

%description freedom-report
This package is not meant to be installed, but rather to generate an
error report with conflicts when you attempt to install it.

The freed-ora-freedom package cannot conflict with the non-Free kernel
packages from Fedora because the Freed-ora kernel packages need to
provide the same names, to satisfy other system-wide dependencies.

Thus, freed-ora-freedom-report will conflict with known kernel
packages you may have installed on your system, even the -libre ones.
From the conflict errors you'd get when attempting to install it, you
can tell whether you have any non-Libre packages remaining, and then
install the corresponding -libre packages and remove the non-Free
ones.  It is wise to reboot into a newly-installed kernel before
removing the one you're running.

%files freedom-report
%defattr(-,root,root,-)

%changelog
* Sun Nov 29 2010 Alexandre Oliva <lxoliva@fsfla.org> - 1-1
- Initial RPM release.
