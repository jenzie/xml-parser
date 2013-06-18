#ifndef KERNEL_H
#define KERNEL_H

float kernel_measureFFT( int FFT_size, float min_time, Random R);
float kernel_measureSOR( int SOR_size, float min_time, Random R);
float kernel_measureMonteCarlo( float min_time, Random R);
float kernel_measureSparseMatMult(int Sparse_size_N,
    int Sparse_size_nz, float min_time, Random R);
float kernel_measureLU( int LU_size, float min_time, Random R);

#endif
