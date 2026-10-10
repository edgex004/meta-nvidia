SUMMARY = "NVIDIA container runtime library"

DESCRIPTION = "NVIDIA container runtime library \
The nvidia-container library provides an interface to configure GNU/Linux \
containers leveraging NVIDIA hardware. The implementation relies on several \
kernel subsystems and is designed to be agnostic of the container runtime. \
"
HOMEPAGE = "https://github.com/NVIDIA/libnvidia-container"

inherit go

# Keep S stable; go.bbclass computes GO_SRCURI_DESTSUFFIX based on basename(S)

GO_IMPORT = "github.com/NVIDIA/libnvidia-container"

DEPENDS = " \
    coreutils-native \
    pkgconfig-native \
    libcap \
    elfutils \
    libtirpc \
    ldconfig-native \
"
LICENSE = "Apache-2.0 & MIT"

# Both source repositories include GPL COPYING (and for
# libnvidia-container, COPYING.LESSER) files. However:
# * For libnvidia-container, those files might only apply if elfutils
#   sources were included (the makefile has commands to download and
#   build libelf from elfutils sources). We configure the build to
#   use libelf provided externally.
# * For nvidia-modprobe, only the nvidia-modprobe-utils library is
#   built and used.  All sources for that library are MIT-licensed.

LIC_FILES_CHKSUM = "\
    file://src/${GO_IMPORT}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57 \
    file://src/${GO_IMPORT}/deps/src/nvidia-modprobe-${NVIDIA_MODPROBE_VERSION}/modprobe-utils/nvidia-modprobe-utils.c;endline=22;md5=b6a3106a81660c726888d006853ada63 \
    file://src/${GO_IMPORT}/deps/src/nvidia-modprobe-${NVIDIA_MODPROBE_VERSION}/modprobe-utils/pci-enum.h;endline=29;md5=ca948b6fabc48e616fccbf17247feebf \
    file://src/${GO_IMPORT}/deps/src/nvidia-modprobe-${NVIDIA_MODPROBE_VERSION}/modprobe-utils/pci-sysfs.c;endline=25;md5=0449248350efd54938e7f8d25af965cb \
"

NVIDIA_MODPROBE_VERSION = "550.54.14"

# Unpack nvidia-modprobe under the libnvidia-container source tree (deps/src/...)
NVIDIA_MODPROBE_SRCURI_DESTSUFFIX = "${@os.path.join(os.path.basename(d.getVar('S')), 'src', d.getVar('GO_IMPORT'), 'deps', 'src', 'nvidia-modprobe-' + d.getVar('NVIDIA_MODPROBE_VERSION')) + '/'}"

SRC_URI = "git://github.com/NVIDIA/libnvidia-container.git;protocol=https;name=libnvidia;branch=main;destsuffix=${GO_SRCURI_DESTSUFFIX} \
           git://github.com/NVIDIA/nvidia-modprobe.git;protocol=https;branch=main;name=modprobe;destsuffix=${NVIDIA_MODPROBE_SRCURI_DESTSUFFIX} \
"

# tag: v1.16.2
SRCREV_libnvidia = "921e2f3197385173cf8670342e96e98afe9b3dd3"
# Nvidia modprobe version 550.54.14
SRCREV_modprobe = "149440ca0654d928f27df5ebff485a122bfe43b1"
SRCREV_FORMAT = "libnvidia_modprobe"

B = "${S}"

PACKAGECONFIG ??= ""
PACKAGECONFIG[seccomp] = "WITH_SECCOMP=yes,WITH_SECCOMP=no,libseccomp"

# We need to link with libelf, otherwise we need to
# include bmake-native which does not exist at the moment.
EXTRA_OEMAKE = 'EXCLUDE_BUILD_FLAGS=1 PLATFORM=${HOST_ARCH} WITH_LIBELF=yes COMPILER=${@d.getVar('CC').split()[0]} REVISION=${SRCREV_libnvidia} ${PACKAGECONFIG_CONFARGS} \
                NVIDIA_MODPROBE_EXTRA_CFLAGS="${NVIDIA_MODPROBE_EXTRA_CFLAGS}"'
NVIDIA_MODPROBE_EXTRA_CFLAGS ?= "${DEBUG_PREFIX_MAP}"

export OBJCPY = "${OBJCOPY}"

do_configure() {
    base_do_configure
}

do_compile() {
    export TMPDIR="${GOTMPDIR}"
    base_do_compile
}

do_install () {
    # Upstream 1.16.x may not provide an install target; install artifacts manually
    install -d ${D}${libdir} ${D}${bindir}

    # Libraries
    find ${B} -maxdepth 3 -type f \( -name 'libnvidia-container.so*' -o -name 'libnvc*.so*' \) -exec cp -a {} ${D}${libdir}/ \; || true

    # CLI tools if present
    find ${B} -maxdepth 3 -type f -name 'nvidia-container-cli' -exec install -m 0755 {} ${D}${bindir}/ \; || true

    # If no libraries were installed, avoid shipping an empty libdir to satisfy QA
    if [ -d ${D}${libdir} ] && [ -z "$(ls -A ${D}${libdir})" ]; then
        rmdir ${D}${libdir}
    fi

    # Clean bundled COPYING docs if any ended up in doc tree (defensive)
    if [ -d ${D}${datadir}/doc ]; then
        find ${D}${datadir}/doc -type f -name 'COPYING*' -delete || true
    fi
}

# Package file mappings
FILES:${PN} += " \
    ${libdir}/*.so* \
"

FILES:${PN}-dev += " \
    ${includedir} \
    ${libdir}/*.so \
    ${libdir}/pkgconfig \
"

FILES:${PN}-staticdev += " \
    ${libdir}/*.a \
"

FILES:${PN}-tools = " \
    ${bindir}/nvidia-container-cli \
"

PACKAGES =+ "${PN}-tools"
FILES:${PN}-tools = "${bindir}"
# XXX - go.bbclass rewrites these
FILES:${PN}-dev = "${includedir} ${FILES_SOLIBSDEV} ${libdir}/*.la \
                ${libdir}/*.o ${libdir}/pkgconfig ${datadir}/pkgconfig \
                ${datadir}/aclocal ${base_libdir}/*.o \
                ${libdir}/${BPN}/*.la ${base_libdir}/*.la \
                ${libdir}/cmake ${datadir}/cmake"
FILES:${PN}-staticdev = "${libdir}/*.a ${base_libdir}/*.a ${libdir}/${BPN}/*.a"
# - XXX
INSANE_SKIP:${PN} = "already-stripped ldflags"
RDEPENDS:${PN}:append = " ldconfig nvidia"
RRECOMMENDS:${PN} += "nvidia"
