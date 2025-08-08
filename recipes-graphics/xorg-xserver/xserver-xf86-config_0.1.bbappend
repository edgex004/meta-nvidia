FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://nvidia.conf \
                   file://xorg.conf.nvidia"

do_install:append() {
    install -m 0644 -D ${S}/nvidia.conf ${D}${sysconfdir}/X11/xorg.conf.d/nvidia.conf
    install -m 0644 -D ${S}/xorg.conf.nvidia ${D}${sysconfdir}/X11/xorg.conf
}
