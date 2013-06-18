#include "SOR.h"
#include <stdio.h>

    float SOR_num_flops(int M, int N, int num_iterations)
    {
        float Md = (float) M;
        float Nd = (float) N;
        float num_iterD = (float) num_iterations;

        return (Md-1)*(Nd-1)*num_iterD*6.0;
    }

// MODIFIED FOR TEST CODE
    void SOR_execute(int M, int N, float omega, float **G, int 
		     num_iterations, int print)
    {

        float omega_over_four = omega * 0.25;
        float one_minus_omega = 1.0 - omega;

        /* update interior points */

        int Mm1 = M-1;
        int Nm1 = N-1; 
        int p;
        int i;
        int j;
        float *Gi;
        float *Gim1;
        float *Gip1;

        for (p=0; p<num_iterations; p++)
        {
            for (i=1; i<Mm1; i++)
            {
                Gi = G[i];
                Gim1 = G[i-1];
                Gip1 = G[i+1];
                for (j=1; j<Nm1; j++)
                    Gi[j] = omega_over_four * (Gim1[j] + Gip1[j] + Gi[j-1] 
                                + Gi[j+1]) + one_minus_omega * Gi[j];
            }
        }

	// ADDED TEST OUTPUT, print the results
	if( print )
	  {
	    printf("\n");
	    int y, z;
	    for(y=0; y<M; y++)
	      {
		for(z=0; z<N; z++)
		  {
		    printf("%f", G[y][z]);
		  }
		printf("\n");
	      }
	  }
    }
            