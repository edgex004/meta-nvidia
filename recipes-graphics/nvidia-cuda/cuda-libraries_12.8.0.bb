DESCRIPTION = "Meta-package for bringing in CUDA libraries"
LICENSE = "MIT"

CUDA_COMPONENTS = " \
    libcuda \
    libcublas \
    libnpp \
"

DEPENDS = "${CUDA_COMPONENTS}"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"  
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"

COMPATIBLE_MACHINE:class-target = "(x86|x86-64|qemux86|qemux86-64|genericx86|genericx86-64)"

PACKAGES = "${PN} ${PN}-dev"
ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} = "${CUDA_COMPONENTS}"
RDEPENDS:${PN}-dev = "${@' '.join(['%s-dev' % pkg for pkg in d.getVar('CUDA_COMPONENTS').split()])}"

BBCLASSEXTEND = "native nativesdk"
