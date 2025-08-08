require cuda-x86-shared-binaries.inc

SUMMARY = "CUDA Basic Linear Algebra Subprograms (BLAS) library"

# Enable on generic x86 64-bit machines too
COMPATIBLE_MACHINE:class-target = "(x86|x86-64|qemux86|qemux86-64|genericx86|genericx86-64)"

do_install() {
    install -d ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}
    install -d ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}/stubs
    
    # Install cublas libraries
    if [ -d "${S}/lib64" ]; then
        cp ${S}/lib64/libcublas*.so* ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}/ || true
        cp ${S}/lib64/stubs/libcublas*.so ${D}${prefix}/local/cuda-${CUDA_VERSION}/${baselib}/stubs/ || true
    fi
}

# Handle stubs properly
PRIVATE_LIBS:${PN}-stubs = "libcublas.so.12 libcublasLt.so.12"

BBCLASSEXTEND = "native nativesdk"
