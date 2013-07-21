SUMMARY = "A parallel build system"
DESCRIPTION = "distcc is a parallel build system that distributes \
compilation of C/C++/ObjC code across machines on a network."
SECTION = "devel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
PR = "r0"

DEPENDS = "avahi"

GTKCONFIG = "gtk"
GTKCONFIG_libc-uclibc = ""

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'x11', '${GTKCONFIG}', '', d)} popt"
PACKAGECONFIG[gtk] = "--with-gtk,--without-gtk --without-gnome,gtk+"
# use system popt by default
PACKAGECONFIG[popt] = "--without-included-popt,--with-included-popt,popt"

RRECOMMENDS_${PN} = "avahi-daemon"

SRC_URI = "http://distcc.googlecode.com/files/${BPN}-${PV}.tar.bz2 \
           file://separatebuilddir.patch \
           file://default \
           file://distccmon-gnome.desktop \
           file://distcc"

SRC_URI[md5sum] = "a1a9d3853df7133669fffec2a9aab9f3"
SRC_URI[sha256sum] = "f55dbafd76bed3ce57e1bbcdab1329227808890d90f4c724fcd2d53f934ddd89"

inherit autotools pkgconfig update-rc.d useradd

EXTRA_OECONF += "--disable-Werror PYTHON=/dev/null"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system \
                       --home /dev/null \
                       --no-create-home \
                       --gid nogroup \
                       distcc"

INITSCRIPT_NAME = "distcc"

do_install_append() {
    install -d ${D}${sysconfdir}/init.d/
    install -d ${D}${sysconfdir}/default
    install -m 0755 ${WORKDIR}/distcc ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/default ${D}${sysconfdir}/default/distcc
    ${DESKTOPINSTALL}
}
DESKTOPINSTALL = ""
DESKTOPINSTALL_libc-glibc () {
    install -d ${D}${datadir}/distcc/
    install -m 0644 ${WORKDIR}/distccmon-gnome.desktop ${D}${datadir}/distcc/
}
PACKAGES += "distcc-distmon-gnome"

FILES_${PN} = " ${sysconfdir} \
		${bindir}/distcc \
    ${bindir}/lsdistcc \
		${bindir}/distccd \
		${bindir}/distccmon-text"
FILES_distcc-distmon-gnome = "  ${bindir}/distccmon-gnome \
				${datadir}/distcc"

pkg_postrm_${PN} () {
	deluser distcc || true
}
