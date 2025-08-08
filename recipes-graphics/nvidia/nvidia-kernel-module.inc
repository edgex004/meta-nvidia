inherit module
do_compile[depends] += "virtual/kernel:do_shared_workdir"

# Override ARCH for NVIDIA - it expects x86_64, not x86
export ARCH = "${TARGET_ARCH}"
EXTRA_OEMAKE:append = " SYSSRC=${STAGING_KERNEL_DIR} KERNEL_OUTPUT=${STAGING_KERNEL_BUILDDIR} ARCH=${TARGET_ARCH}"