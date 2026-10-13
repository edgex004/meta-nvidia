meta-nvidia (x86_64)
====================

This layer adds support for NVIDIA graphics cards using the upstream binary drivers. It's a pile of targeted hacks and may break your build in new and exciting ways. This layer makes no attempt to support any platform other than x86_64.

What this layer does (on purpose)
- NVIDIA driver 570.x: kernel modules (`nvidia`, `nvidia-drm`, `nvidia-modeset`, `nvidia-uvm`) and core user-space bits
- CUDA 12.8 (modular): a minimal set of CUDA libraries packaged separately (e.g. `libcuda`, `libcublas`, `libnpp`)
- Containers: `docker-moby`, `containerd`, `nvidia-container-toolkit`, and sane defaults via `containerd_%.bbappend`
- Kernel config fragments to enable DRM/TTM helpers required by the NVIDIA DRM KMS stack
- X.Org snippets for simple, non-wayland setups (optional)

What this layer does not do (also on purpose)
- Support non-x86_64 targets
- Promise to work with any Yocto branch besides recent releases (tested on walnascar/5.2)
- Provide full GUI apps like `nvidia-settings` out-of-the-box (kept lean for embedded)
- Make QEMU magically grow a GPU (CUDA and real GL need real hardware)

Quick start
1) Add the layer to `bblayers.conf` and ensure required dependencies are present:
   - poky (meta, meta-poky, meta-yocto-bsp)
   - meta-openembedded: `meta-oe`, `meta-networking`, `meta-filesystems`, `meta-python`
   - `meta-virtualization`

2) In `conf/local.conf`, keep it simple:
```
MACHINE ??= "genericx86-64"

# Prefer GLVND for libGL/EGL/GLES
PREFERRED_PROVIDER_virtual/libgl = "libglvnd"
PREFERRED_PROVIDER_virtual/egl = "libglvnd"
PREFERRED_PROVIDER_virtual/libgles1 = "libglvnd"
PREFERRED_PROVIDER_virtual/libgles2 = "libglvnd"

# Containers/runtime
DISTRO_FEATURES:append = " virtualization"
PREFERRED_PROVIDER_virtual/docker = "docker-moby"

# Add the essentials to your image
IMAGE_INSTALL:append = " \
    kernel-modules \
    nvidia \
    cuda-libraries \
    nvidia-container-toolkit \
    docker-moby \
    docker-compose \
    docker-registry \
    libseccomp \
"

# If you want serial logs in QEMU
APPEND += " console=ttyS0,115200"
```

3) Build and (optionally) boot in QEMU (no real GPU inside QEMU):
```
bitbake core-image-minimal
runqemu genericx86-64 wic nographic slirp
```

Bring-up on real hardware (aka the fun part)
- Flash the WIC image and boot your x86_64 machine with an NVIDIA GPU
- Verify modules: `modprobe nvidia; lsmod | grep nvidia`
- Check userspace: `nvidia-smi` (if present in your image) or `ldd /usr/lib/libcuda.so.*`
- Containers with GPU: install runtime pieces and test with `nvidia-container-toolkit` (pull a CUDA base image, run `nvidia-smi` inside)

Notes
- Kernel options are provided via a fragment enabling `CONFIG_DRM`, `CONFIG_DRM_KMS_HELPER`, `CONFIG_DRM_TTM`, `CONFIG_DRM_TTM_HELPER`, `CONFIG_DRM_VRAM_HELPER`, and FBDEV emulation helpers.
- Mesa is expected to coexist with libglvnd; this layer prefers GLVND to keep provider conflicts at bay.
- QEMU boots are for sanity-checks only; they will not validate GPU/CUDA.

Compatibility
- Target: x86_64 (e.g. `genericx86-64`)
- Yocto: tested with current branches (walnascar/5.2); older branches may work but are not supported here.

Credits and lineage (a.k.a. who to blame)
- Inspired by and based on:
  - OakLabsInc meta-nvidia (pyro-era, gloriously honest README): [OakLabsInc/meta-nvidia](https://github.com/OakLabsInc/meta-nvidia)
  - kopernikusauto meta-nvidia (newer take for mickledore): [kopernikusauto/meta-nvidia](https://github.com/kopernikusauto/meta-nvidia)

Licensing
- NVIDIA bits are under NVIDIA’s proprietary license (see `custom-licenses/NVIDIA-Proprietary`).
- Everything else follows the licenses declared in individual recipes.

Bugs, footguns, and other adventures
- If it works: great. If it doesn’t: welcome to graphics on embedded Linux.
- PRs improving robustness without making it complicated are very welcome.


