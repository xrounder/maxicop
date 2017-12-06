package uebung2.aufgabe2;

import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;
import scala.collection.concurrent.Debug;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dennis on 29.11.2017.
 */
public class GlobalAlignment extends net.gumbix.dynpro.DynProJava<Integer> {

    //Nucleotid Sequences
    private String[] s = (" CGATCCTGT").split("");
    private String[] t = (" CATCGCCTT").split("");

    public static void main(String[] args) {

        GlobalAlignment ga = new GlobalAlignment();

        List<PathEntry<Integer>> solutionJava = ga.solutionAsList(new Idx(ga.n() - 1, ga.m() - 1));
        System.out.println("Optimal Decisions:");
        for (PathEntry<Integer> entry : solutionJava) {
            System.out.print(entry.decision() + " ");

        }

        System.out.println("\n");
        System.out.println(ga.mkMatrixString(ga.solution(new Idx(ga.n() - 1, ga.m() - 1))));
    }

    //TODO check why values of diagonal (idx.i() = idx.j()) are wrong
    @Override
    public Object decisions(Idx idx) {
        //Start
        if (idx.i() == 0 && idx.j() == 0) {
            System.out.println(idx.i() + " " + idx.j() + " START");
            return new Integer[]{0};
            //Insertion
        } else if (idx.i() == 0 && idx.j() > 0) {
            System.out.println(idx.i() + " " + idx.j() + " INSERT");
            return new Integer[]{1};
            //Deletion
        } else if (idx.j() == 0 && idx.i() > 0) {
            System.out.println(idx.i() + " " + idx.j() + " DELETION");
            return new Integer[]{2};
            //1 Match, -1 Missmatch
        } else {
            System.out.println(idx.i() + " " + idx.j() + " INSERT, DELETION, BOTH");
            //TODO Check...
            return new Integer[]{1,2,3};
        }
    }

    //should be correct, just like on the slides "BIM-40" page 18
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {

        //1 for Insert
        if(d == 1){
            return new Idx[]{new Idx(idx.i(), idx.j() - 1)};
            //2 for Deletion
        } else if(d == 2){
            return new Idx[]{new Idx(idx.i() - 1, idx.j())};
            //3 for Both
        } else if (d == 3){
            return new Idx[]{new Idx(idx.i() - 1, idx.j() - 1)};
            //0 for Start
        } else {
            return new Idx[]{};
        }
    }

    @Override
    public double value(Idx idx, Integer d) {

        //Start
        if (d == 0) {
            return 0;
            //Insert or Deletion
        } else if (d == 1 || d == 2) {
            return -2;
            //Both
        } else {
            return similarity(s[idx.i()], t[idx.j()]);
        }
    }

    /**
     * checks if sequences at given index is a match or a mismatch
     *
     * @param s   sequence s
     * @param t   sequence t
     * @return 1 for match, -1 for mismatch
     */
    private int similarity(String s, String t) {
        if (s.equals(t)) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int n() {
        return s.length;
    }

    @Override
    public int m() {
        return t.length;
    }

    @Override
    public Function2 extremeFunction() {
        return this.MAX();
    }

    @Override
    public String[] rowLabels() {
        return s;
    }

    @Override
    public Option<String[]> columnLabels() {
        String[] cArray = new String[t.length];
        for (int i = 0; i < t.length; i++) {
            cArray[i] = "" + t[i];
        }
        return new Some(cArray);
    }

}
