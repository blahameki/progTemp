package lambeer;

import java.io.*;
import java.util.*;

public class Main {

    private static ArrayList<Beer> beers = new ArrayList<Beer>();
    private static Map<String,Comparator<Beer>> comps = new HashMap<String,Comparator<Beer>>();
    private static List<String> lparams;

    static {
        /*
        comps.put("name",(b1,b2) -> b1.getName().compareTo(b2.getName()));
        comps.put("style", (b1,b2) -> b1.getStyle().compareTo(b2.getStyle()));
        comps.put("strength", (b1,b2) ->{
            double res = b2.getStrength()-b1.getStrength();
            if(res < 0) {
                return -1;
            } else if(res>0) {
                return 1;
            } else {
                return 0;
            }
        });
        */

        comps.put("strength",Comparator.comparing(Beer::getStrength));
        comps.put("style", Comparator.comparing(Beer::getStyle));
        comps.put("name", Comparator.comparing(Beer::getName));

        lparams = new LinkedList<String>();
        lparams.add("name");
        lparams.add("style");
        lparams.add("strength");
    }

    protected static void add(String[] cmd) {
        //hibakezeles
        if(cmd.length != 4) {
            System.out.println("Hibas argumentumok az 'add' parancsban!");
            return;
        }

        Beer temp = new Beer(cmd[1],cmd[2],Double.parseDouble(cmd[3]));
        if(beers.add(temp)) {
            System.out.println("A sor sikeresen hozza lett adva!");
        } else {
            System.out.println("Nem sikerult hozzaadni a sort!");
        }
    }

    protected static void list(String[] cmd) {
        if(beers.size() == 0) {
            System.out.println("A lista ures!");
        }

        if(cmd.length > 1) {
            // ellenorzi az argumentumok helyesseget
            for(int i=1;i<cmd.length;++i) {
                if(!lparams.contains(cmd[i])) {
                    System.out.println("Hibas rendezesi feltetel!");
                    return;
                }
            }

            // frissiti a sorrend prioritasi listajat
            for(int i=cmd.length-1;i>0;--i) {
                lparams.remove(cmd[i]);
                lparams.add(0,cmd[i]);
            }

            // kombinalt komparator
            Comparator<Beer> order = (b1,b2) -> 0;
            for(String cmp : lparams) {
                order = order.thenComparing(comps.get(cmp));
            }

            // rendezes
            Collections.sort(beers, order);
        }

        for(Beer beer : beers) {
            System.out.println(beer);
        }
    }

    protected static void save(String[] cmd)
            throws IOException{
        //hibakezeles
        if(cmd.length != 2) {
            System.out.println("Hibas argumentumok az 'save' parancsban!");
            return;
        }

        FileOutputStream f = new FileOutputStream(cmd[1]);
        ObjectOutputStream out = new ObjectOutputStream(f);
        out.writeObject(beers);
        out.close();

        System.out.println("Sikeresen mentve!");
    }

    @SuppressWarnings("unchecked")
    protected static void load(String[] cmd)
            throws IOException,ClassNotFoundException{
        if(cmd.length != 2) {
            System.out.println("Hibas argumentumok az 'load' parancsban!");
            return;
        }

        FileInputStream f = new FileInputStream(cmd[1]);
        ObjectInputStream in = new ObjectInputStream(f);
        beers =(ArrayList<Beer>)in.readObject();
        in.close();

        System.out.println("Sikeresen betoltve!");
    }

    protected static void search(String[] cmd) {
        if(cmd.length != 3) {
            System.out.println("Hibas parancs!");
        }
        for(Beer temp : beers) {
            switch(cmd[1]) {
                case "name":
                    if(temp.getName().equals(cmd[2])) {
                        System.out.println(temp.toString());
                    }
                    break;
                case "style":
                    if(temp.getStyle().equals(cmd[2])) {
                        System.out.println(temp.toString());
                    }
                    break;
                case "strength":
                    if(temp.getStrength() == Double.parseDouble(cmd[2])) {
                        System.out.println(temp.toString());
                    }
                    break;
            }
        }
    }

    protected static void find(String[] cmd) {
        if(cmd.length != 3) {
            System.out.println("Hibas parancs!");
        }
        for(Beer temp : beers) {
            switch(cmd[1]) {
                case "name":
                    if(temp.getName().contains(cmd[2])) {
                        System.out.println(temp.toString());
                    }
                    break;
                case "style":
                    if(temp.getStyle().contains(cmd[2])) {
                        System.out.println(temp.toString());
                    }
                    break;
                case "strength":
                    if(temp.getStrength() >= Double.parseDouble(cmd[2])) {
                        System.out.println(temp.toString());
                    }
                    break;
                case "weaker":
                    if(temp.getStrength() <= Double.parseDouble(cmd[2])) {
                        System.out.println(temp.toString());
                    }
                    break;
            }
        }
    }

    protected static void delete(String[] cmd) {
        if(cmd.length != 2) {
            System.out.println("Hibas parancs!");
        }
        Iterator<Beer> i = beers.iterator();
        while(i.hasNext()) {
            Beer temp = i.next();
            if(temp.getName().equals(cmd[1])) {
                i.remove();
            }
        }
    }

    public static void main(String[] args) {
//		1.feladat
//		Beer b1 = new Beer("Neumarkt","keseru",8.0);
//		Beer b2 = new Beer("Stella","barna",6.0);
//		System.out.println(b1.toString());
//		System.out.println(b2.toString());

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        try {
            HashMap<String,Command> commands = new HashMap<String,Command>();
            commands.put("exit", cmd -> System.exit(0));
            commands.put("add", Main::add);
            commands.put("list", Main::list);
            commands.put("load", Main::load);
            commands.put("save", Main::save);
            commands.put("search", Main::search);
            commands.put("find", Main::find);
            commands.put("delete", Main::delete);
            while(true) {
                String line = br.readLine();
                String[] splited = line.split(" ");

                if(commands.get(splited[0]) != null) {
                    commands.get(splited[0]).execute(splited);
                } else {
                    System.out.println("Ismeretlen parancsot adott meg!");
                }

            }

        } catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}

