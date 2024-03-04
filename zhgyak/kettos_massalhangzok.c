#include <stdio.h>

int main(int argc, char const *argv[]){
    
    typedef enum Kettos{
        alap, s, l, z
    } Kettos;

    int c;
    Kettos allapot = alap; 
    int szamlalo = 0;

    while (c = getchar() != EOF){
        switch (allapot){
            case alap:
                switch(c){ 
                    case 's': allapot = s; break;
                    case 'l': allapot = l; break;
                    default: break;
                }
            
            case s:
                switch(c){
                    case 's': break;
                    case 'z': szamlalo++; allapot = alap; break;
                    default: allapot = alap; break;
                }
                
            case l:
                switch(c){
                    case 'l': break;
                    case 'y': szamlalo++; allapot = alap; break;
                    default: allapot = alap; break;
                }

            case z:
                switch(c){
                    case 'z': break;
                    case 's': szamlalo++; allapot = alap; break;
                    default: allapot = alap; break;
        }
    }
    
    return 0;
}
