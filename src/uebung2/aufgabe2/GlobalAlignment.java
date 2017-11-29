package uebung2.aufgabe2;

import net.gumbix.dynpro.Idx;

/**
 * Created by Dennis on 29.11.2017.
 */
public class GlobalAlignment extends net.gumbix.dynpro.DynPro<Integer>{

    public static void main(String[] args) {

    }

    @Override
    public Object decisions(Idx idx) {
        return null;
    }

    @Override
    public Idx[] prevStates(Idx idx, Integer integer) {
        return new Idx[0];
    }

    @Override
    public double value(Idx idx, Integer integer) {
        return 0;
    }

    @Override
    public int n() {
        return 0;
    }

    @Override
    public int m() {
        return 0;
    }
}
