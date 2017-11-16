package uebung1.aufgabe3;

import java.util.ArrayList;
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
     * Anzahl aller moeglichen Loesungen
     */
    private static int anzahlMoeglicherLoesungen;

    /**
     * Anzahl aller gueltigen Loesungen
     */
    private static int anzahlGueltigerLoesungen;

    /**
     * Listen fuer gueltige Fragmentreihenfolgen
     */
    private static ArrayList<List<Integer>> gueltigeReihenfolgenA = new ArrayList<List<Integer>>();
    private static ArrayList<List<Integer>> gueltigeReihenfolgenB = new ArrayList<List<Integer>>();
    //</editor-fold>

    /**
     * Hauptprogramm
     *
     * @param args Kommandozeilenargumente
     */
    public static void main(String[] args) throws Exception {
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

        ausgabeGueltigeFragmentreihenfolgen(gueltigeReihenfolgenA, "A");
        ausgabeGueltigeFragmentreihenfolgen(gueltigeReihenfolgenB, "B");

        ausgabeAnzahlReihenfolgen();
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
     * Speichert die Schnittpositionen eines Enzyms.
     * @param fragmentListe Fragmentliste mit der die Schnittpositionen ermittelt werden.
     * @return Liste mit allen Schnittpositionen
     */
    public static List<Integer> speicherSchnittpositionen(List<Integer> fragmentListe) {
        List<Integer> listeSchnittpositionen = new ArrayList<Integer>();

        int schnittIndex = 0;

        // Schnittstellen ergeben sich aus der Aufsummierung der Fragmentlaengen
        for(int fragment : fragmentListe){
            schnittIndex += fragment;

            // Das Ende des Strangs ist kein Schnitt
            if(schnittIndex < strangLaenge){
                listeSchnittpositionen.add(schnittIndex);
            }
        }

        return listeSchnittpositionen;
    }

    /**
     * Prueft, ob zwei Fragmentlisten von AB gleich sind.
     * @param fragmentLaengenAB Liste die auf Gleichheit geprueft werden soll
     * @return Liefert true wenn beide Listen gleich sind
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
     * Auswertung des Double Digest Problems
     * @param permutationenA Liste aller moeglichen Fragmentreihenfolgen von Enzym A
     * @param permutationenB Liste aller moeglichen Fragmentreihenfolgen von Enzym B
     */
    public static void auswertungDoubleDigest(Collection<List<Integer>> permutationenA, Collection<List<Integer>> permutationenB){
        for(List<Integer> permutationVonA : permutationenA){
            for(List<Integer> permutationVonB : permutationenB){
                // Schnittpositionen koennen schonmal in AB uebernommen werden
                List<Integer> schnittpositionenAB = speicherSchnittpositionen(permutationVonA);
                List<Integer> schnittpositionenB = speicherSchnittpositionen(permutationVonB);

                // Schnittpositionen von B zu AB hinzufuegen
                for(int schnittposition : schnittpositionenB){
                    if(!schnittpositionenAB.contains(schnittposition)){
                        schnittpositionenAB.add(schnittposition);
                    }
                }

                Collections.sort(schnittpositionenAB);

                List<Integer> fragmentLaengenAB = new ArrayList<Integer>();

                fragmentLaengenAB.add(schnittpositionenAB.get(0));

                // Ermittelt Fragmentlaengen von AB aus den Schnittpositionen
                for(int i = 0; i < schnittpositionenAB.size()-1; i++){
                    int fragmentLaengeAB = schnittpositionenAB.get(i+1) - schnittpositionenAB.get(i);

                    fragmentLaengenAB.add(fragmentLaengeAB);
                }

                fragmentLaengenAB.add(strangLaenge - schnittpositionenAB.get(schnittpositionenAB.size()-1));

                anzahlMoeglicherLoesungen++;

                // Fragment Liste von AB muss mit der gegebenen Fragmentliste von AB uebereinstimmen
                // damit die Reihenfolgen von A und B fuer gueltig erklaert werden koennen
                if(pruefeABFragmentlistenAufGleicheit(fragmentLaengenAB)){
                    anzahlGueltigerLoesungen++;

                    if(!gueltigeReihenfolgenA.contains(permutationVonA)){
                        gueltigeReihenfolgenA.add(permutationVonA);
                    }

                    if(!gueltigeReihenfolgenB.contains(permutationVonB)){
                        gueltigeReihenfolgenB.add(permutationVonB);
                    }
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

        String ausgabe = "";
        for(int fragment : fragmentListe) {
            ausgabe += fragment + ", ";
        }

        System.out.print(ausgabe.substring(0, ausgabe.length() - 2) + ")");
    }

    /**
     * Gibt alle gueltige Reihenfolgen der Fragmentlaengen einer Liste aus
     * @param fragmentListen Liste von Fragmentlisten
     * @param listenName Name der Liste
     */
    public static void ausgabeGueltigeFragmentreihenfolgen(ArrayList<List<Integer>> fragmentListen, String listenName){
        System.out.print("\n\nAnzahl gueltiger Fragment-Reihenfolgen fuer " + listenName + ": " + fragmentListen.size());
        String ausgabe;
        int nummerReihenfolge = 1;
        for(List<Integer> fragmentreihe : fragmentListen){
            System.out.print("\n" + nummerReihenfolge + ". Fragment-Reihenfolge " + listenName + " = (");
            ausgabe = "";
            for(int fragment : fragmentreihe) {
                ausgabe += fragment + ", ";
            }
            System.out.print(ausgabe.substring(0, ausgabe.length() - 2) + ").");

            nummerReihenfolge++;
        }
    }

    /**
     * Gibt die Anzahl aller moeglichen, gueltigen und ungueltigen Reihenfolgen aus
     */
    public static void ausgabeAnzahlReihenfolgen(){
        System.out.println("\n\nAnzahl der moeglichen Reihenfolgen: " + anzahlMoeglicherLoesungen);
        System.out.println("Anzahl der gueltigen Reihenfolgen: " + anzahlGueltigerLoesungen);
        System.out.println("Anzahl der ungueltigen Reihenfolgen: " + (anzahlMoeglicherLoesungen - anzahlGueltigerLoesungen));
    }
    //</editor-fold>
}
