
#ifndef SPARSE_COMPROW_H
#define SPARSE_COMPROW_H

float SparseCompRow_num_flops(int N, int nz, int num_iterations);

void SparseCompRow_matmult( int M, float *y, float *val, int *row,
			    int *col, float *x, int NUM_ITERATIONS, int print);

#endif
