typedef struct
{
  int m[17];                        
  int seed;                             
  int i;                                /* originally = 4 */
  int j;                                /* originally =  16 */
  int /*boolean*/ haveRange;            /* = false; */
  float left;                          /*= 0.0; */
  float right;                         /* = 1.0; */
  float width;                         /* = 1.0; */
}
Random_struct, *Random;

Random new_Random_seed(int seed);
float Random_nextDouble(Random R);
void Random_delete(Random R);
float *RandomVector(int N, Random R);
float **RandomMatrix(int M, int N, Random R);
