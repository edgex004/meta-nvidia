inherit module
do_compile[depends] += "virtual/kernel:do_shared_workdir"

# Override ARCH for NVIDIA - it expects x86_64, not x86
export ARCH = "${TARGET_ARCH}"
EXTRA_OEMAKE:append = " SYSSRC=${STAGING_KERNEL_DIR} KERNEL_OUTPUT=${STAGING_KERNEL_BUILDDIR} ARCH=${TARGET_ARCH}"

# Build and install via module.bbclass helpers
# Install prebuilt NVIDIA .ko files to /lib/modules so kernel-module-split picks them up
do_install:append() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    # Copy any .ko present in the extracted runfile
    if ls ${S}/*.ko >/dev/null 2>&1; then
        install -m 0644 ${S}/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    fi
    if ls ${S}/kernel/*.ko >/dev/null 2>&1; then
        install -m 0644 ${S}/kernel/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    fi
}

# Ensure the built modules are packaged in ${PN}
FILES:${PN}:append = " ${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*.ko"

# Make ${PN} satisfy the expected kernel-module provides used by the image
RPROVIDES:${PN}:append = " \
    kernel-module-nvidia-${KERNEL_VERSION} \
    kernel-module-nvidia-drm-${KERNEL_VERSION} \
    kernel-module-nvidia-modeset-${KERNEL_VERSION} \
    kernel-module-nvidia-uvm-${KERNEL_VERSION} \
    kernel-module-nvidia-peermem-${KERNEL_VERSION} \
"