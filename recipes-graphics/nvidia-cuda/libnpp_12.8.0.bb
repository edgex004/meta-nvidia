require cuda-x86-shared-binaries.inc

SUMMARY = "CUDA NPP (NVIDIA Performance Primitives) library"

# Enable on generic x86 64-bit machines too
COMPATIBLE_MACHINE:class-target = "(x86|x86-64|qemux86|qemux86-64|genericx86|genericx86-64)"

do_install() {
    install -d ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}
    install -d ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}/stubs
    
    # Install NPP libraries
    if [ -d "${S}/lib64" ]; then
        cp ${S}/lib64/libnpp*.so* ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}/ || true
        cp ${S}/lib64/stubs/libnpp*.so ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}/stubs/ || true
    fi
}

# Handle stubs properly  
PRIVATE_LIBS:${PN}-stubs = "libnppc.so.12 libnppial.so.12 libnppicc.so.12 libnppidei.so.12 libnppif.so.12 libnppig.so.12 libnppim.so.12 libnppist.so.12 libnppisu.so.12 libnppitc.so.12 libnpps.so.12"

BBCLASSEXTEND = "native nativesdk"
