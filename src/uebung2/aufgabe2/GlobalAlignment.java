package uebung2.aufgabe2;

import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dennis on 29.11.2017.
 */
public class GlobalAlignment extends net.gumbix.dynpro.DynProJava<Integer>{

    //Nucleotid Sequences
    private String[] s = (" CGATCCTGT").split("");
    private String[] t = (" CATCGCCTT").split("");

    public static void main(String[] args) {

        GlobalAlignment ga = new GlobalAlignment();

        List<PathEntry<Integer>> solutionJava = ga.solutionAsList(new Idx(ga.n() - 1, 0));
        System.out.println("Optimal Decisions:");
        for (PathEntry<Integer> entry : solutionJava) {
            System.out.print(entry.decision() + " ");
        }

        System.out.println("\n");
        System.out.println(ga.mkMatrixString(ga.solution(new Idx(ga.n() - 1, 0))));
    }

    //TODO check why the values for i=0 or j=0 are different to the values below in the comment
    //gap penalty: -2, should be 0, -2, -4, -8 etc. for i = 0 or j = 0
    @Override
    public Object decisions(Idx idx) {
        //Start
        if(idx.i() == 0 && idx.j() == 0){
            return new Integer[]{0};
        //Deletion
        }else if(idx.i() == 0 && idx.j() > 0){
            return new Integer[]{-2*idx.j()};
        //Insertion
        }else if(idx.j() == 0 && idx.i() > 0){
            return new Integer[]{-2*idx.i()};
        }else{
            //TODO Deletion, Insertion, Both.. no idea how to calculate the value here
            return new Integer[]{0};
        }
    }

    //should be correct, just like on the slides "BIM-40" page 18
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
        if (idx.i() == 0 && idx.j() > 0) {
            return new Idx[]{new Idx(idx.i(), idx.j() - 1)};
        }else if(idx.j() == 0 && idx.i() > 0) {
            return new Idx[]{new Idx(idx.i() - 1,idx.j())};
        }else if(idx.j() > 0 && idx.i() > 0) {
            return new Idx[]{new Idx(idx.i() - 1, idx.j() - 1)};
        }else{
            return new Idx[]{};
        }
    }

    @Override
    public double value(Idx idx, Integer integer) {
        return integer;
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
    public Function2 extremeFunction(){
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
