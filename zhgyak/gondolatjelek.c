#include <stdio.h>

int main(int argc, char const *argv[]){
    typedef enum Gondolatjel{
        alap, lehet, gondolat, vege_lehet
    } Gondolatjel;

    int c;
    Gondolatjel allapot = alap;

    while (c = getchar() != EOF){
        switch (allapot){

            case alap:
                if (c = ' ') allapot = lehet;
                else putchar(c);
                break;
            
            case lehet: 
                switch(c){
                    case ' ': putchar(' '); break;
                    case '-': allapot = gondolat;
                    default: putchar(' '); putchar(c); break;
                }
            
            case gondolat:
                if (c = ' ') allapot = vege_lehet; break;
            
            case vege_lehet:
                switch(c){
                    case ' ': break;
                    case '-': allapot = alap; break;
                    default: allapot = gondolat; break;
                }
        }
    }
    
    return 0;
}
