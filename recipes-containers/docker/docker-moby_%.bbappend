# Drop bridge-utils runtime deps to avoid build breakage on walnascar
RDEPENDS::remove = "bridge-utils"
RRECOMMENDS::remove = "bridge-utils"
