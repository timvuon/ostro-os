SECTION = "libs"
require glibc_${PV}.bb

DEFAULT_PREFERENCE = "-1"

do_install () {
	:
}

PACKAGES = ""
PACKAGES_DYNAMIC = ""
PROVIDES = "virtual/${TARGET_PREFIX}libc-for-gcc"
DEPENDS = "virtual/${TARGET_PREFIX}gcc-initial linux-libc-headers"
GLIBC_ADDONS = "nptl,ports"
GLIBC_EXTRA_OECONF = ""
