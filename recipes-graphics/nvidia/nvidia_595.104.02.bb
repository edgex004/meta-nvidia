include nvidia-base.inc
include nvidia-kernel-module.inc
include nvidia-libs.inc

SRC_URI[md5sum] = "3763ad79a6ca5e95fef6fae3fd2344b2"
SRC_URI[sha256sum] = "e421c202e4c79f58c3c7f3161bbe71454ebb3d88936f88205a0e327cd04c59ca"

# Provide the generic meta-package name used in images (RPROVIDES)
RPROVIDES:${PN} += "nvidia"
RREPLACES:${PN} += "nvidia"
RCONFLICTS:${PN} += "nvidia"