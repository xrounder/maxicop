package uebung1;

import net.gumbix.dynpro.DynPro;
import net.gumbix.dynpro.Idx;

public class DNA_Fragmente extends DynPro {
    @Override
    public Object decisions(Idx idx) {
        return null;
    }

    @Override
    public Idx[] prevStates(Idx idx, Object o) {
        return new Idx[0];
    }

    @Override
    public double value(Idx idx, Object o) {
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
