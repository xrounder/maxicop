package uebung1.aufgabe3;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import static org.apache.commons.collections4.CollectionUtils.permutations;

/**
 * Klasse zum loesen des Double Digest Problems
 */
public class DoubleDigestProblem{
    //<editor-fold desc="Felder und Listen">
    /**
     * Scanner für Benutzereingaben an der Tastatur
     */
    public static Scanner in = new Scanner(System.in);

    /**
     * Liste von Fragmentlaengen, welche anhand von Enzym A entstanden sind
     */
    private static List<Integer> fragmentListeA = new ArrayList<Integer>();

    /**
     * Liste von Fragmentlaengen, welche anhand von Enzym B entstanden sind
     */
    private static List<Integer> fragmentListeB = new ArrayList<Integer>();

    /**
     * Liste von Fragmentlaengen, welche anhand von den Enzymen A und B (AB) entstanden sind
     */
    private static List<Integer> fragmentListeAB = new ArrayList<Integer>();

    /**
     * Stranglaenge
     */
    private static int strangLaenge;

    /**
     * Listen fuer gueltige Fragmentreihenfolgen und Schnittpositionen
     */
    private static ArrayList<List<Integer>> gueltigeKombisA = new ArrayList<List<Integer>>();
    private static ArrayList<List<Integer>> gueltigeKombisB = new ArrayList<List<Integer>>();
    private static  ArrayList<List<Integer>> gueltigeSchnittpositionenA = new ArrayList<List<Integer>>(); // Vielleicht nicht noetig
    private static ArrayList<List<Integer>> gueltigeSchnittpositionenB = new ArrayList<List<Integer>>(); // Vielleicht nicht noetig
    private static ArrayList<List<Integer>> gueltigeSchnittpositionenAB = new ArrayList<List<Integer>>(); // Vielleicht nicht noetig

    /**
     * Listen fuer ungueltige Fragmentreihenfolgen und Schnittpositionen
     */
    private static ArrayList<List<Integer>> ungueltigeKombisA = new ArrayList<List<Integer>>();
    private static ArrayList<List<Integer>> ungueltigeKombisB = new ArrayList<List<Integer>>();
    private static ArrayList<List<Integer>> ungueltigeSchnittpositionenA = new ArrayList<List<Integer>>(); // Vielleicht nicht noetig
    private static ArrayList<List<Integer>> ungueltigeSchnittpositionenB = new ArrayList<List<Integer>>(); // Vielleicht nicht noetig
    private static ArrayList<List<Integer>> ungueltigeSchnittpositionenAB = new ArrayList<List<Integer>>(); // Vielleicht nicht noetig
    //</editor-fold>

    /**
     * Hauptprogramm
     *
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) throws Exception {
        //sortiereFragmentListen();
        befuelleListen();

        if(!pruefeFragmentlaengen()){
            throw new Exception("Die Summen aller Fragmente in allen Listen muessen identisch sein!");
        }

        Collection<List<Integer>> permutationenA = permutations(fragmentListeA);
        Collection<List<Integer>> permutationenB = permutations(fragmentListeB);

        auswertungDoubleDigest(permutationenA, permutationenB);

        ausgabeFragmentListe(fragmentListeA, "A");
        ausgabeFragmentListe(fragmentListeB, "B");
        ausgabeFragmentListe(fragmentListeAB, "AB");

        ausgabeFragmentreihenfolgen(gueltigeKombisA, "A", "gueltig");
        ausgabeFragmentreihenfolgen(gueltigeKombisB, "B", "gueltig");

        ausgabeFragmentreihenfolgen(ungueltigeKombisA, "A", "ungueltig");
        ausgabeFragmentreihenfolgen(ungueltigeKombisB, "B", "ungueltig");

        ausgabeAnzahlGueltigerUndUngueltigerKombis();
    }

    //<editor-fold desc="Listeniniziierung und -sortierung">
    /**
     * Befuellung der Listen
     */
    public static void befuelleListen(){
        fragmentListeA.add(1);
        fragmentListeA.add(3);
        fragmentListeA.add(5);
        fragmentListeA.add(8);

        fragmentListeB.add(2);
        fragmentListeB.add(3);
        fragmentListeB.add(5);
        fragmentListeB.add(7);

        fragmentListeAB.add(1);
        fragmentListeAB.add(1);
        fragmentListeAB.add(2);
        fragmentListeAB.add(2);
        fragmentListeAB.add(3);
        fragmentListeAB.add(4);
        fragmentListeAB.add(4);
    }

    /**
     * Prueft, ob die Summen aller Fragmente in allen Listen gleich sind
     *
     * @return Liefert true, wenn alle Summen gleich sind
     */
    public static boolean pruefeFragmentlaengen(){
        int summeFragmenteA = fragmentListeA.stream().mapToInt(Integer::intValue).sum();
        int summeFragmenteB = fragmentListeB.stream().mapToInt(Integer::intValue).sum();
        int summeFragmenteAB = fragmentListeAB.stream().mapToInt(Integer::intValue).sum();

        boolean alleSummenGleich = false;

        if((summeFragmenteA == summeFragmenteB) && (summeFragmenteB == summeFragmenteAB)){
            strangLaenge = summeFragmenteA;
            alleSummenGleich = true;
        }

        return alleSummenGleich;
    }
    //</editor-fold>

    //<editor-fold desc="Methoden, welche das Double Digest Problem loesen">
    /**
     * Tragt die Schnittpositionen eines Enzyms ein.
     * @param schnittpositionen Liste der markierten Schnittpositionen.
     * @return Liste mit allen Schnittpositionen
     */
    public static List<Integer> trageSchnittPositionenEin(int[] schnittpositionen){
        List<Integer> listeDerSchnittpositionen = new ArrayList<Integer>();

        for(int i = 1; i < strangLaenge+1; i++){
            if(schnittpositionen[i] == 1){
                listeDerSchnittpositionen.add(i);
            }
        }

        return listeDerSchnittpositionen;
    }

    /**
     * Markiert die Schnittpositionen eines Enzyms.
     * @param fragmentListe Fragmentliste mit der die Schnittpositionen ermittelt werden.
     * @return Liste mit allen Schnittpositionen
     */
    public static int[] markiereSchnittPositionen(List<Integer> fragmentListe) {
        int[] listeSchnittpositionen = new int[strangLaenge + 1];

        int index = 0;

        // Schnittstellen ergeben sich aus Fragmentlaengen
        for (int fragment : fragmentListe) {
            index += fragment;

            if(index < strangLaenge) {
                listeSchnittpositionen[index] = 1;
            }
        }

        return listeSchnittpositionen;
    }

    /**
     * Prueft, ob zwei Fragmentlisten von AB gleich sind.
     * @param fragmentLaengenAB Liste die auf Gleichheit geprueft werden soll
     * @return Den Wahrheitswert
     */
    public static boolean pruefeABFragmentlistenAufGleicheit(List<Integer> fragmentLaengenAB){
        boolean sindGleich = false;

        Collections.sort(fragmentLaengenAB);

        if(fragmentListeAB.equals(fragmentLaengenAB)){
            sindGleich = true;
        }

        return sindGleich;
    }

    /**
     * AUswertung des Double Digest
     * @param permutationenA Liste aller moeglichen Fragmentreihenfolgen von Enzym A
     * @param permutationenB Liste aller moeglichen Fragmentreihenfolgen von Enzym B
     */
    public static void auswertungDoubleDigest(Collection<List<Integer>> permutationenA, Collection<List<Integer>> permutationenB){
        for(List<Integer> permutationVonA : permutationenA){
            for(List<Integer> permutationVonB : permutationenB){
                int[] schnittpositionenA = markiereSchnittPositionen(permutationVonA);
                int[] schnittpositionenB = markiereSchnittPositionen(permutationVonB);
                int[] schnittpositionenAB = new int[strangLaenge+1];

                // Bestimmt Schnittpositionen mit Enzymen A und B - AB
                for(int i = 1; i < strangLaenge+1; i++){
                    if(schnittpositionenA[i] == 1 || schnittpositionenB[i] == 1){
                        schnittpositionenAB[i] = 1;
                    }
                }

                List<Integer> listeDerSchnittpositionenA = trageSchnittPositionenEin(schnittpositionenA);
                List<Integer> listeDerSchnittpositionenB = trageSchnittPositionenEin(schnittpositionenB);
                List<Integer> listeDerSchnittpositionenAB = trageSchnittPositionenEin(schnittpositionenAB);

                List<Integer> fragmentLaengenAB = new ArrayList<Integer>();
                int fragmentLaengeAB = 0;
                for(int i = 1; i < strangLaenge+1; i++){
                    fragmentLaengeAB++;

                    if(schnittpositionenAB[i] == 1 || i == strangLaenge){
                        fragmentLaengenAB.add(fragmentLaengeAB);
                        fragmentLaengeAB = 0;
                    }
                }

                if(pruefeABFragmentlistenAufGleicheit(fragmentLaengenAB)){
                    gueltigeKombisA.add(permutationVonA);
                    gueltigeKombisB.add(permutationVonB);
                    gueltigeSchnittpositionenA.add(listeDerSchnittpositionenA);
                    gueltigeSchnittpositionenB.add(listeDerSchnittpositionenB);
                    gueltigeSchnittpositionenAB.add(listeDerSchnittpositionenAB);
                } else {
                    ungueltigeKombisA.add(permutationVonA);
                    ungueltigeKombisB.add(permutationVonB);
                    ungueltigeSchnittpositionenA.add(listeDerSchnittpositionenA);
                    ungueltigeSchnittpositionenB.add(listeDerSchnittpositionenB);
                    ungueltigeSchnittpositionenAB.add(listeDerSchnittpositionenAB);
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Ausgabemethoden">
    /**
     * Gibt eine Fragmentliste aus
     *
     * @param fragmentListe Fragmentliste die ausgegeben werden soll
     * @param listenName Name der fragment liste
     */
    public static void ausgabeFragmentListe(List<Integer> fragmentListe, String listenName){
        System.out.print("\nFragmente von " + listenName + ": (");
        for(int fragment : fragmentListe) {
            System.out.print(fragment + ", ");
        }
        System.out.print(")");
    }

    /**
     * Gibt Reihenfolgen der Fragmentlaengen einer Liste aus
     * @param fragmentListen Liste von Fragmentlisten
     * @param listenName Name der Liste
     * @param listenArt Gibt an ob es eine Liste gueltiger oder ungueltiger Reihenfolgen ist
     */
    public static void ausgabeFragmentreihenfolgen(ArrayList<List<Integer>> fragmentListen, String listenName, String listenArt){
        System.out.print("\n\nAnzahl " + listenArt + "er Fragment-Reihenfolgen fuer " + listenName + ": " + fragmentListen.size());
        int nummerReihenfolge = 1;
        for(List<Integer> fragmentreihe : fragmentListen){
            System.out.print("\n" + nummerReihenfolge + ". Fragment-Reihenfolge " + listenName + " = (");
            for(int fragment : fragmentreihe) {
                System.out.print(fragment + ", ");
            }
            System.out.print(").");

            nummerReihenfolge++;
        }
    }

    /**
     * Gibt die Anzahl aller gueltiger und ungueltiger Kombinationen aus
     */
    public static void ausgabeAnzahlGueltigerUndUngueltigerKombis(){
        System.out.println("\n\nAnzahl der gueltigen Kombinationen: " + gueltigeSchnittpositionenA.size());
        System.out.println("Anzahl der ungueltigen Kombinationen: " + ungueltigeKombisA.size());
    }
    //</editor-fold>

    //<editor-fold desc="Wahrscheinlich nicht mehr gebrauchte Methoden">
    /**
     * Befuellt eine Fragmentliste mit Benutzereingaben.
     *
     * @param fragmentListe Fragmentliste die befuellt werden soll
     * @param listenName Name der Fragmentliste
     */
    public static void befuelleListenMitBenutzereingaben(List<Integer> fragmentListe, String listenName){
        System.out.println("\nWie viele Fragmente für Liste " + listenName + "?");
        int listenLaenge = in.nextInt();

        // Schleife laeuft solange, bsi die Fragmentliste voll ist
        do{
            System.out.println("\nGeben Sie die Laenge eines Fragments fuer " + listenName + " ein:");
            int fragmentLaenge = in.nextInt();

            if(fragmentLaenge <= 0){
                System.out.println("Versuchen Sie es bitte noch einmal. Nur positive Zahlen sind erlaubt.");
            }else{
                System.out.println("Okay.");
                fragmentListe.add(fragmentLaenge);
                listenLaenge--;
            }
        }while(listenLaenge > 0);
    }

    /**
     * Sortiert die Fragmentlisten nach ihrer Befuellung
     */
    public static void sortiereFragmentListen(){
        befuelleListenMitBenutzereingaben(fragmentListeA, "A");
        befuelleListenMitBenutzereingaben(fragmentListeB, "B");
        befuelleListenMitBenutzereingaben(fragmentListeAB, "AB");

        Collections.sort(fragmentListeA);
        Collections.sort(fragmentListeB);
        Collections.sort(fragmentListeAB);
    }

    /**
     * Gibt alle moeglichen Reihenfolgen der Fragmentlaengen einer Liste aus
     * @param permutationen Liste von allen Permutationen
     * @param listenName Name der Liste
     */
    public static void ausgabeAllerFragmentreihenfolgen(Collection<List<Integer>> permutationen, String listenName){
        System.out.print("\n\nAnzahl gueltiger Fragment-Reihenfolgen fuer " + listenName + ": " + permutationen.size());
        int nummerReihenfolge = 1;
        for(List<Integer> permutation : permutationen){
            System.out.print("\n" + nummerReihenfolge + ". Fragment-Reihenfolge " + listenName + " = (");
            for(int fragment : permutation) {
                System.out.print(fragment + ", ");
            }
            System.out.print(").");
        }
    }

    /**
     * Gibt alle gueltige Kombinationen aus
     */
    public static void ausgabeGueltigerKombinationen(){
        System.out.println("+\n\n---------------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                  Gueltige Reihenfolgen und Schnittpositionen                                  |");
        System.out.println("+---------------------------------------------------------------------------------------------------------------+\n");

        for(int i = 0; i < gueltigeKombisA.size(); i++){
            System.out.print("\nReihenfolge A: (");
            for(int fragment : gueltigeKombisA.get(i)){
                System.out.print(fragment + ", ");
            }
            System.out.print(")");

            System.out.print("\nReihenfolge B: (");
            for(int fragment : gueltigeKombisB.get(i)){
                System.out.print(fragment + ", ");
            }
            System.out.print(")");

            System.out.print("\nSchnittpositionen A: (");
            for(int position : gueltigeSchnittpositionenA.get(i)){
                System.out.print(position + ", ");
            }
            System.out.print(")");

            System.out.print("\nSchnittpositionen B: (");
            for(int position : gueltigeSchnittpositionenB.get(i)){
                System.out.print(position + ", ");
            }
            System.out.print(")");

            System.out.print("\nSchnittpositionen AB: (");
            for(int position : gueltigeSchnittpositionenAB.get(i)){
                System.out.print(position + ", ");
            }
            System.out.print(")\n\n");
        }
    }

    /**
     * Gibt alle ungueltige Kombinationen aus
     */
    public static void ausgabeUngueltigerKombinationen(){
        System.out.println("+---------------------------------------------------------------------------------------------------------------+");
        System.out.println("|                                 Ungueltige Reihenfolgen und Schnittpositionen                                 |");
        System.out.println("+---------------------------------------------------------------------------------------------------------------+");

        for(int i = 0; i < ungueltigeKombisA.size(); i++){
            System.out.print("\nReihenfolge A: (");
            for(int fragment : ungueltigeKombisA.get(i)){
                System.out.print(fragment + ", ");
            }
            System.out.print(")");

            System.out.print("\nReihenfolge B: (");
            for(int fragment : ungueltigeKombisB.get(i)){
                System.out.print(fragment + ", ");
            }
            System.out.print(")");

            System.out.print("\nSchnittpositionen A: (");
            for(int position : ungueltigeSchnittpositionenA.get(i)){
                System.out.print(position + ", ");
            }
            System.out.print(")");

            System.out.print("\nSchnittpositionen B: (");
            for(int position : ungueltigeSchnittpositionenB.get(i)){
                System.out.print(position + ", ");
            }
            System.out.print(")");

            System.out.print("\nSchnittpositionen AB: (");
            for(int position : ungueltigeSchnittpositionenAB.get(i)){
                System.out.print(position + ", ");
            }
            System.out.print(")\n");
        }
    }
    //</editor-fold>
}
