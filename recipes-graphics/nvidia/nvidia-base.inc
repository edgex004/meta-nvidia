SUMMARY = "NVidia Binary Graphics Driver"
LICENSE = "NVIDIA-Proprietary"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=92aa2e2af6aa0bcba1c3fe49da021937"

NVIDIA_ARCHIVE_NAME = "NVIDIA-Linux-${TARGET_ARCH}-${PV}"
NVIDIA_SRC = "${WORKDIR}/${NVIDIA_ARCHIVE_NAME}"

# Support for multiple architectures
# Support common x86 machine names (full-match regex semantics)
COMPATIBLE_MACHINE = "(x86|x86-64|qemux86|qemux86-64|genericx86|genericx86-64)"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    https://download.nvidia.com/XFree86/Linux-${TARGET_ARCH}/${PV}/${NVIDIA_ARCHIVE_NAME}.run \
"

S = "${NVIDIA_SRC}/kernel"

do_unpack() {
    chmod +x ${DL_DIR}/${NVIDIA_ARCHIVE_NAME}.run
    rm -rf ${NVIDIA_SRC}
    ${DL_DIR}/${NVIDIA_ARCHIVE_NAME}.run -x --target ${NVIDIA_SRC}
}

do_make_scripts[noexec] = "1"
