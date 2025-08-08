FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Ensure DRM fbdev TTM symbols are available for NVIDIA's nvidia-drm.ko
SRC_URI += "file://nvidia-drm.cfg"

# Explicitly merge the fragment
KERNEL_CONFIG_FRAGMENTS:append = " nvidia-drm.cfg"

# nvidia module build fails if the scripts/basic/fixdep binary isn't present in
# the kernel build outputs

do_shared_workdir:append() {
    cp -r scripts $kerneldir/
    cp include/config/auto.conf $kerneldir/include/config/auto.conf
}
