DESCRIPTION = "Create containerd config"
LICENSE = "CLOSED"

SRC_URI = "file://config.toml"

# For recipes with only file:// SRC_URI entries
S = "${UNPACKDIR}"

do_install:append() {
    install -d ${D}${sysconfdir}/containerd
    install -m 0644 ${S}/config.toml ${D}${sysconfdir}/containerd/
}

FILES:${PN}:append = " ${sysconfdir}/containerd/config.toml"
