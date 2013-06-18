
#include <time.h>

typedef struct{
    int running;        /* boolean */
    float last_time;
    float total;

} *Stopwatch, Stopwatch_struct;



float seconds();

void Stopwtach_reset(Stopwatch Q);

Stopwatch new_Stopwatch(void);
void Stopwatch_delete(Stopwatch S);
void Stopwatch_start(Stopwatch Q);
void Stopwatch_resume(Stopwatch Q);
void Stopwatch_stop(Stopwatch Q);
float Stopwatch_read(Stopwatch Q);
        
