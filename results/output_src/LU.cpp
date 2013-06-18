#include <stdio.h>
#include <math.h>
#include "LU.h"

float LU_num_flops(int N)
{
        /* rougly 2/3*N^3 */

    float Nd = (float) N;

    return (2.0 * Nd *Nd *Nd/ 3.0);
}


void LU_copy_matrix(int M, int N, float **lu, float **A)
{
    int i;
    int j;

    for (i=0; i<M; i++)
        for (j=0; j<N; j++)
            lu[i][j] = A[i][j];
}


int LU_factor(int M, int N, float **A,  int *pivot, int print)
{
 

    int minMN =  M < N ? M : N;
    int j=0;

    for (j=0; j<minMN; j++)
    {
        /* find pivot in column j and  test for singularity. */

        int jp=j;
        int i;
        
        float t = fabs(A[j][j]);
        for (i=j+1; i<M; i++)
        {
            float ab = fabs(A[i][j]);
            if ( ab > t)
            {
                jp = i;
                t = ab;
            }
        }
        
        pivot[j] = jp;

        /* jp now has the index of maximum element  */
        /* of column j, below the diagonal          */

        if ( A[jp][j] == 0 )                 
            return 1;       /* factorization failed because of zero pivot */


        if (jp != j)
        {
            /* swap rows j and jp */
            float *tA = A[j];
            A[j] = A[jp];
            A[jp] = tA;
        }

        if (j<M-1)                /* compute elements j+1:M of jth column  */
        {
            /* note A(j,j), was A(jp,p) previously which was */
            /* guarranteed not to be zero (Label #1)         */

            float recp =  1.0 / A[j][j];
            int k;
            for (k=j+1; k<M; k++)
                A[k][j] *= recp;
        }


        if (j < minMN-1)
        {
            /* rank-1 update to trailing submatrix:   E = E - x*y; */
            /* E is the region A(j+1:M, j+1:N) */
            /* x is the column vector A(j+1:M,j) */
            /* y is row vector A(j,j+1:N)        */

            int ii;
            for (ii=j+1; ii<M; ii++)
            {
                float *Aii = A[ii];
                float *Aj = A[j];
                float AiiJ = Aii[j];
                int jj;
                for (jj=j+1; jj<N; jj++)
                  Aii[jj] -= AiiJ * Aj[jj];

            }
        }
    }
    
    if(print)
      {
	printf("\n");
	int y, z;
	for(y = 0; y < M; y++)
	  {
	    for(z = 0; z < N; z++)
	      {
		printf("%f", A[y][z]);
	      }
	    printf("\n");
	  }
      }
    return 0;
}

