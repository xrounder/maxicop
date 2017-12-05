package uebung2.aufgabe1;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Klasse, welche das Greedy-Superstring-Verfahren simuliert
 */
public class GreedySuperString {
    /**
     * Hauptprogramm.
     *
     * @param args Kommandozeilen-Argumente
     */
    public static void main(String[] args) throws IOException{
        File dir = new File(".");
        File unbekannterTextDatei = new File(dir.getCanonicalPath() + File.separator + "src" + File.separator + "uebung2" + File.separator + "aufgabe1" + File.separator + "unbekannterText.txt");
        File unbekannteDNA1Datei = new File(dir.getCanonicalPath() + File.separator + "src" + File.separator + "uebung2" + File.separator + "aufgabe1" + File.separator + "DNAFragmente1.txt");
        File unbekannteDNA2Datei = new File(dir.getCanonicalPath() + File.separator + "src" + File.separator + "uebung2" + File.separator + "aufgabe1" + File.separator + "DNAFragmente2.txt");
        File unbekannteDNA3Datei = new File(dir.getCanonicalPath() + File.separator + "src" + File.separator + "uebung2" + File.separator + "aufgabe1" + File.separator + "DNAFragmente3.txt");

        List<String> unbekannterText = ladeFragmente(unbekannterTextDatei);
        List<String> unbekannteDNA1 = ladeFragmente(unbekannteDNA1Datei);
        List<String> unbekannteDNA2 = ladeFragmente(unbekannteDNA2Datei);
        List<String> unbekannteDNA3 = ladeFragmente(unbekannteDNA3Datei);

        List<String> unbekannterTextTeilstringfrei = entferneTeilstrings(unbekannterText);
        List<String> unbekannteDNA1Teilstringfrei = entferneTeilstrings(unbekannteDNA1);
        List<String> unbekannteDNA2Teilstringfrei = entferneTeilstrings(unbekannteDNA2);
        List<String> unbekannteDNA3Teilstringfrei = entferneTeilstrings(unbekannteDNA3);

        String superStringUnbekannterText = GreedySuperstring(unbekannterTextTeilstringfrei);
        String superStringUnbekannteDNA1 = GreedySuperstring(unbekannteDNA1Teilstringfrei);
        String superStringUnbekannteDNA2 = GreedySuperstring(unbekannteDNA2Teilstringfrei);
        String superStringUnbekannteDNA3 = GreedySuperstring(unbekannteDNA3Teilstringfrei);

        ausgabedesSuperstrings(superStringUnbekannterText);
        ausgabedesSuperstrings(superStringUnbekannteDNA1);
        ausgabedesSuperstrings(superStringUnbekannteDNA2);
        ausgabedesSuperstrings(superStringUnbekannteDNA3);
    }

    //<editor-fold desc="Laden und Verarbeitung der Fragmente">
    /**
     * Liest eine Datei Zeile fuer Zeile ein.
     *
     * @param datei Datei, welche eingelesen werden soll
     * @throws IOException IOException, welche geworfen werden kann
     */
    public static List<String> ladeFragmente(File datei) throws IOException{
        List<String> fragmente = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new FileReader(datei));

        String zeile = null;

        //Liest die Datei Zeile fuer Zeile ein
        while((zeile = br.readLine()) != null) {
            fragmente.add(zeile);
        }

        return fragmente;
    }

    /**
     * Entfernt alle Teilstrings aus der Fragmentliste
     *
     * @param fragmentListe Fragmentliste, aus welcher die Teilstrings entfernt werden muessen
     * @return Liste mit Teilstring-freien Fragmenten
     */
    public static List<String> entferneTeilstrings(List<String> fragmentListe){
        List<String> teilstringFreieListe = new ArrayList<String>();

        Set<String> uniqueFragmentListe = new HashSet<String>(fragmentListe);

        // Durchlaeuft alle Fragmente der Fragmentliste
        for(String fragment : uniqueFragmentListe){
            // Nur wenn das Fragment eine Laenge groesser 0 hat, muss es betrachtet werden
            if(fragment.length() > 0){
                List<String> fragmenteMitTeilstrings = new ArrayList<String>();

                // Sucht nach allen Fragmenten, welche das aktuelle Fragment als Teilstring haben
                for(String vergleichsFragment : uniqueFragmentListe){
                    // Wenn Vergleichsfragment eine Teilstring beinhaltet merken
                    if(vergleichsFragment.length() > 0 && vergleichsFragment.contains(fragment)){
                        fragmenteMitTeilstrings.add(vergleichsFragment);
                    }
                }

                // Wenn es keine weiteren Fragmente mit Teilstrings gibt, dann zu Teilstring-freien Liste hinzufuegen
                if(fragmenteMitTeilstrings.size() == 1){
                    teilstringFreieListe.add(fragmenteMitTeilstrings.get(0));
                }
            }
        }

        return teilstringFreieListe;
    }
    //</editor-fold>

    //<editor-fold desc="Der Greedy-Substring-Algorithmus">
    /**
     * Fuehrt den Greedy-Superstring-Algorithmus durch
     * @param fragmentListe Fragmentliste, die bearbeitet wird
     * @return Superstring
     */
    public static String GreedySuperstring(List<String> fragmentListe){
        List<Integer> fragmentIndizes;

        // Laeuft solange mehrere Fragmente vorhanden sind
        while(fragmentListe.size() > 1){
            int besteDeckung = 0;
            int indexBestesOverlap = 0;

            String fragment1 = null;
            String fragment2 = null;

            // Alle Fragmente muessen miteinander verglichen werden
            for(String fragment : fragmentListe) {
                for (String vergleichsFragment : fragmentListe) {

                    // Das gleiche Fragment darf nicht mit sich selbst verglichen werden
                    if (!fragment.equals(vergleichsFragment)) {
                        int indexAktuellesOverlap = 0;
                        int indexVergleichsFragment = 0;

                        int aktuelleDeckung = 0;

                        // Prueft jeden Buchstaben auf Ueberlappung
                        for (int k = 0; k < fragment.length(); k++) {
                            if(indexVergleichsFragment >= vergleichsFragment.length()){
                                indexAktuellesOverlap = 0;
                                aktuelleDeckung = 0;
                                indexVergleichsFragment = 0;
                            }

                            if (fragment.charAt(k) == vergleichsFragment.charAt(indexVergleichsFragment)) {
                                if(indexAktuellesOverlap == 0){
                                    indexAktuellesOverlap = k;
                                }

                                aktuelleDeckung++;
                                indexVergleichsFragment++;
                            } else {
                                indexAktuellesOverlap = 0;
                                indexVergleichsFragment = 0;
                                aktuelleDeckung = 0;

                                if(fragment.charAt(k) == vergleichsFragment.charAt(indexVergleichsFragment)){
                                    indexAktuellesOverlap = k;
                                    aktuelleDeckung++;
                                    indexVergleichsFragment++;
                                }
                            }
                        }

                        if (aktuelleDeckung >= besteDeckung) {
                            besteDeckung = aktuelleDeckung;
                            indexBestesOverlap = indexAktuellesOverlap;

                            fragment1 = new String(fragment);
                            fragment2 = new String(vergleichsFragment);
                        }
                    }
                }
            }

            String gemergetesFragment = mergeFragmente(fragment1, fragment2, besteDeckung, indexBestesOverlap);

            // Loesche einzelfragmente
            fragmentListe.remove(fragment1);
            fragmentListe.remove(fragment2);

            // Fuege gemergetes, ganzes Fragment hinzu
            fragmentListe.add(gemergetesFragment);
        }

        return fragmentListe.get(0);
    }

    /**
     * Merged das aktuelle Fragment mit dem optimalen Fragment
     *
     * @param fragment1 Aktuelles Fragment
     * @param fragment2 Optimales Fragment
     * @param deckung Deckungsrate
     * @param indexBestesOverlap Index an dem der Overlap im aktuellen Fragment beginnt
     * @return Das gemergete Fragment
     */
    public static String mergeFragmente(String fragment1, String fragment2, int deckung, int indexBestesOverlap){
        String gemergetesFragment = null;

        if(fragment1 != null && fragment2 != null){
            if(deckung != 0){
                int indexRest = ((fragment1.length() - 1) - indexBestesOverlap) + 1;
                gemergetesFragment = fragment1 + fragment2.substring(indexRest);
            } else {
                gemergetesFragment = fragment1 + fragment2;
            }
        }

        return gemergetesFragment;
    }
    //</editor-fold>

    //<editor-fold desc="Ausgabemethoden">
    /**
     * Gibt einen Superstring aus.
     * @param superString Der Superstring der ausgegeben werden soll.
     */
    public static void ausgabedesSuperstrings(String superString){
        System.out.println("Superstring: " + superString);
    }
    //</editor-fold>
}
