#include <stdio.h>
#include <stdlib.h>
 
typedef struct BiFa {
    int ertek;
    struct BiFa *bal, *jobb;
} BiFa;
 
BiFa *beszur(BiFa *gyoker, int ertek) {
    if (gyoker == NULL) {
        BiFa *uj = (BiFa*) malloc(sizeof(BiFa));
        uj->ertek = ertek;
        uj->bal = uj->jobb = NULL;
        return uj;
    }
    if (ertek < gyoker->ertek) {        /* balra szur */
        gyoker->bal = beszur(gyoker->bal, ertek);
    }
    else if (ertek > gyoker->ertek) {   /* jobbra szur */
        gyoker->jobb = beszur(gyoker->jobb, ertek);
    }
    else {
        /* mar benne van */
    }
    return gyoker;
}
 
void sorban_kiir(BiFa *gyoker) {
    if (gyoker == NULL)   /* leállási feltétel */
       return;
 
    sorban_kiir(gyoker->bal);     /* 1 */
    printf("%d ", gyoker->ertek);     /* 2 */
    sorban_kiir(gyoker->jobb);    /* 3 */
}
 
void felszabadit(BiFa *gyoker) {
    if (gyoker == NULL)   /* leállási feltétel */
        return;
    
    felszabadit(gyoker->bal);     /* 1 */
    felszabadit(gyoker->jobb);       /* 2 */
    free(gyoker);
}

int main(void) {
    int minta[] = {15, 96, 34, 12, 14, 56, 21, 11, 10, 9, 78, 43, 0};
    BiFa *gyoker = NULL;
    for (int i = 0; minta[i] > 0; i++)
        gyoker = beszur(gyoker, minta[i]);
 
    sorban_kiir(gyoker);
 
    return 0;
}