include nvidia-base.inc
include nvidia-kernel-module.inc
include nvidia-libs.inc

SRC_URI[md5sum] = "27c62c49264e1a1da92ebcede9cf76bc"
SRC_URI[sha256sum] = "2d43e64c581be5ef554de9888b1aa90037ef6d45f54284d3d9dcedc08dc4dc26"

# Provide the generic meta-package name used in images (RPROVIDES)
RPROVIDES:${PN} += "nvidia"
RREPLACES:${PN} += "nvidia"
RCONFLICTS:${PN} += "nvidia"