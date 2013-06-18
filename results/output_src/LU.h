#ifndef LU_H
#define LU_H

float LU_num_flops(int N);
void LU_copy_matrix(int M, int N, float **lu, float **A);
int LU_factor(int M, int N, float **A, int *pivot, int print);


#endif
