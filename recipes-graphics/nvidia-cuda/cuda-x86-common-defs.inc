# Support common x86 machines, including genericx86(-64)
COMPATIBLE_MACHINE:class-target = "(x86|x86-64|qemux86|qemux86-64|genericx86|genericx86-64)"
COMPATIBLE_HOST = "(x86_64).*-linux"
CUDA_VERSION ??= "12.8"

S = "${UNPACKDIR}"
B = "${S}"

PR = "r0"

# Common CUDA installer info for x86_64
CUDA_VERSION_DASHED = "${@d.getVar('CUDA_VERSION').replace('.','-')}"
CUDA_INSTALLER_VERSION = "12.8.0_570.86.10"
CUDA_INSTALLER_NAME = "cuda_${CUDA_INSTALLER_VERSION}_linux.run"

# Common source for all CUDA packages
SRC_URI = "https://developer.download.nvidia.com/compute/cuda/${CUDA_VERSION}.0/local_installers/${CUDA_INSTALLER_NAME}"
SRC_URI[sha256sum] = "610867dcd6d94c4e36c4924f1d01b9db28ec08164e8af6c764f21b84200695f8"

# Common QA skips for CUDA packages
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_SYSROOT_STRIP = "1"

# License info - point to CUDA EULA in extracted installer  
LICENSE = "NVIDIA-Proprietary"
LIC_FILES_CHKSUM = "file://EULA.txt;md5=ee8d6d5fdc9f5bb6a4b7b9b4b5e26c5b"
