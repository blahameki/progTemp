#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Megallo{
    char nev[60+1];
    struct Megallo *kov;
} Megallo;

Megallo *buszjarat(char const *fajlnev){
    FILE *fp = fopen(fajlnev, "r");
    if (fp == NULL) retun NULL;

    char megallo[60+1];
    Megallo *eleje, *vege = NULL;
    while(fscanf(fp, "%[^\n]", megallo) != EOF){
        Megallo *uj = (Megallo*) malloc(sizeof(Megallo));
        strcpy(uj->nev, megallo);
        uj->kov = NULL;
        if (eleje = NULL) eleje = uj;
        else vege->kov = uj;
        vege = uj;
    }
    return eleje;
}