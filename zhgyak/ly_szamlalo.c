#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(int argc, char const *argv[]){

    typedef enum Allapotok{
    alap, l_elotte, ll_elotte
    } Allapotok;

    int c;
    Allapotok allapot = alap;
    int szamlalo = 0;

    while (c = getchar() != EOF){
        switch (allapot){
            case alap:
                if (c = 'l') allapot = l_elotte; break;

            case l_elotte:
                switch (c){
                    case 'l': allapot = ll_elotte; break;
                    case 'y': allapot = alap; szamlalo++; break;
                    default: allapot = alap; break;
                }
                break;
            
            case ll_elotte:
                switch (c){
                case 'y': allapot = alap; szamlalo += 2; break;
                case 'l': break;
                default: allapot = alap; break;
                }
            break;
        }    
    }

    printf("%d darab ly volt.\n", szamlalo);
    
    return 0;
}

