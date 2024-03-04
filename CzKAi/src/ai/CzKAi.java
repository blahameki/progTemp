package ai;

import enums.Iranyok;
import enums.PalyaElemek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.awt.geom.Point2D;


/**
 * A saját snake mesterséges intelligenciám
 */
public class CzKAi implements interfaces.Ai {
    interfaces.Adat adat;
    interfaces.Test test;

    /**
     * Konstruktor, null pointerrel inicializálja mindkét változót
     */
    public CzKAi() {
    	this.adat = null;
    	this.test = null;
    }

    /**
     * Adat inicializáló metódusa
     * @param adat Tartalmazza a játék információit
     */
    public void initAdat(interfaces.Adat adat) { this.adat = adat; }

    /**
     * Kukac inicializáló metódusa
     * @param kukac Tartalmazza a kukacunk információit
     */
    public void initKukac(interfaces.Test kukac) { this.test = kukac; }

    /**
     * Visszaadja a legjobbnak tűnő lépés irányát
     * @return legjobbIrany - Az Ai által legjobbnak megítélt lépési irány
     */
    public enums.Iranyok setIrany() {
    	enums.Iranyok[] iranyok = enums.Iranyok.values();
    	enums.Iranyok legjobbIrany = null;
    	ArrayList<Iranyok> lehetsegesIranyok = new ArrayList<Iranyok>();
    	
    	for (Iranyok i : iranyok) {
    		lehetsegesIranyok.add(i);
    	}
    	/*Feltételezem, hogy a getIrany() metódus az érkezési irányt 
    	 * adja vissza, nem volt egyértelmű*/
    	lehetsegesIranyok.remove(ellentetesIrany(test.getIrany()));
    	
    	//Szomszédok azonosítása
    	for (Iranyok i : lehetsegesIranyok) {
    		int x = (int)leendoKoordinata(test.getFej(), i).getX();
    		int y = (int)leendoKoordinata(test.getFej(), i).getY();
    		switch (adat.getPalyaElem(x, y)){
    			case KAJA: return i; //greedy, de legrosszabb esetben a másik Ai is velünk hal
    			case URES: break;
    			default: lehetsegesIranyok.remove(i);
    		}
    	}
    	
    	//leendoIranyok-hoz rendeljük az irány lelépése utáni távolságot a kajától
    	Map<Iranyok, Integer> tavolsagok = new HashMap<>();

    	boolean mindenErtekMinuszEgy = true;

    	for (Iranyok irany : lehetsegesIranyok) {
    	    int x = (int) leendoKoordinata(test.getFej(), irany).getX();
    	    int y = (int) leendoKoordinata(test.getFej(), irany).getY();

    	    // Ellenőrizze, hogy a távolság -1
    	    int tavolsag = tavolsagKajatol(leendoKoordinata(test.getFej(), irany), createPalya(adat));

    	    tavolsagok.put(irany, tavolsag);

    	    if (tavolsag != -1) {
    	        mindenErtekMinuszEgy = false;
    	    }
    	}

    	// Ellenőrizze, hogy minden érték -1
    	if (mindenErtekMinuszEgy) {
    	    // Visszatérés az iránnyal, amely a legközelebb van a kajához
    	    Iranyok legkozelebbiIrany = null;
    	    double minTavolsag = Double.MAX_VALUE;

    	    for (Iranyok irany : lehetsegesIranyok) {
    	        double manhattanTavolsag = legvonalKajatol(leendoKoordinata(test.getFej(), irany), adat.getEtel());
    	        if (manhattanTavolsag < minTavolsag) {
    	            minTavolsag = manhattanTavolsag;
    	            legkozelebbiIrany = irany;
    	        }
    	    }

    	    return legkozelebbiIrany;
    	} else {
    	    // Törölje az értékeket, ahol -1
    	    tavolsagok.values().removeIf(value -> value == -1);
    	}
    	
    	int minTavolsag = Integer.MAX_VALUE;
    	for (Map.Entry<Iranyok, Integer> entry : tavolsagok.entrySet()) {
    	        Iranyok irany = entry.getKey();
    	        int tavolsag = entry.getValue();

    	        if (tavolsag < minTavolsag) {
    	            minTavolsag = tavolsag;
    	            legjobbIrany = irany;
    	        }
    	 }
    	return legjobbIrany;
    }
    
    /**
     * Visszaadja a kapott irány ellentétét
     * @param irany
     * @return A paraméterként megadott irány ellentéte
     */
    public Iranyok ellentetesIrany(Iranyok irany) {
    	switch(irany) {
	    	case JOBB: return Iranyok.BAL; 
	    	case FEL: return Iranyok.LE; 
	    	case BAL: return Iranyok.JOBB; 
	    	case LE: return Iranyok.FEL;
	    	default: return null;
    	}
    }
    
    /**
     * Visszaadja a kígyónk és a kaja távolságát légvonalban
     * @param fej - A kígyónk fejének koordinátája
     * @param kaja - Kaja koordinátája
     * @return Manhattan távolság a kajától, figyelmen kívül hagyva, hogy mik vannak az egyes cellákban
     */
    //Manhattan távolság!!!
    public static int legvonalKajatol(Point2D fej, Point2D kaja) {
    	 int x1 = (int) fej.getX();
         int y1 = (int) fej.getY();
         int x2 = (int) kaja.getX();
         int y2 = (int) kaja.getY();

         return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    /**
     * Visszaadja a kígyónk és a kaja távolságát
     * @param fej - Kígyónk fejének koordinátája
     * @param palya - Két dimenziós tömb a pályáról, minden pályaelemmel
     * @return Manhattan távolság a kajától, figyelembe véve, hogy adott pillanatban van-e út oda
     */
    //Manhattan távolság!!!
    public static int tavolsagKajatol(Point2D fej, PalyaElemek[][] palya) {
        Queue<Point2D> sor = new LinkedList<>();
        boolean[][] latogatott = new boolean[palya.length][palya[0].length];

        sor.add(fej);
        latogatott[(int)fej.getX()][(int)fej.getY()] = true;

        int tavolsag = 0;

        while (!sor.isEmpty()) {
            int szintMeret = sor.size();

            for (int i = 0; i < szintMeret; i++) {
                Point2D current = sor.poll();

                if (palya[(int)current.getX()][(int)current.getX()] == PalyaElemek.KAJA) {
                    return tavolsag; // A kaja megtalálva
                }

                // Szomszédok hozzáadása a sorhoz
                for (Iranyok irany : Iranyok.values()) {
                    Point2D szomszed = leendoKoordinata(current, irany);

                    if (bejarhatoPont(szomszed, palya) && !latogatott[(int)szomszed.getX()][(int)szomszed.getY()]) {
                        sor.add(szomszed);
                        latogatott[(int)szomszed.getX()][(int)szomszed.getY()] = true;
                    }
                }
            }

            tavolsag++; // A következő szintre lépünk
        }

        return -1; // A kaja nem elérhető
    }
    /**
     * Az adott mezőre lehet lépni, vagy sem?
     * @param pont - Vizsgált pont
     * @param palya - Két dimenziós tömb a pályáról, minden pályaelemmel
     * @return T/F - Az adott mező bejárható-e
     */
    // Ellenőrzi, hogy a pont bejárható-e (nincs fal vagy kígyó test)
    private static boolean bejarhatoPont(Point2D pont, PalyaElemek[][] palya) {
        double x = pont.getX();
        double y = pont.getY();

        // Ha a pont a pályán kívül van, vagy a pályaelem nem üres, akkor nem bejárható
        if (x < 0 || y < 0 || x >= palya.length || y >= palya[0].length || palya[(int)x][(int)y] != PalyaElemek.URES) {
            return false;
        }

        return true;
    }
    
    /**
     * Adott pontból adott irányba lépünk, mi lesz az új koordináta?
     * @param pont - Melyik pontból lépünk
     * @param irany - Melyik irányba lépünk
     * @return - Új koordináta
     */
    //Adott irányba való lépés után mi lesz a koordináta
    public static Point2D leendoKoordinata(Point2D pont, Iranyok irany) {
        double ujX = pont.getX();
        double ujY = pont.getY();

        switch (irany) {
            case JOBB:
                ujX += 1.0;
                break;
            case BAL:
                ujX -= 1.0;
                break;
            case FEL:
                ujY += 1.0;
                break;
            case LE:
                ujY -= 1.0;
                break;
        }

        return new Point2D.Double(ujX, ujY);
    }
    
    /**
     * Pálya 2 dimenziós legenerálása tömb segítségével
     * @param adat - A pálya fontosabb tulajdonságai
     * @return 2D tömb a pályáról, pályaelemekkel együtt
     */
    //Pálya létrehozása 2D tömbbel
    public static PalyaElemek[][]createPalya(interfaces.Adat adat){
    	int width = adat.getPalyaMeret().width;
        int height = adat.getPalyaMeret().height;
        PalyaElemek[][] palya = new PalyaElemek[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                PalyaElemek elem = adat.getPalyaElem(x, y);
                palya[x][y] = elem;
            }
        }

        return palya;
    }

    /**
     * @author Czégény Imre Kornél
     * @return Nevem
     */
    public java.lang.String getNev() { return "Czégény Imre Kornél"; }
}
