package uebung1.aufgabe2;

import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

import java.util.Arrays;
import java.util.List;

/**
 * AUFGABE:
 * Angenommen, wir haben DNA-Fragmente unterschiedlicher Längen. Die Länge ci wird
 * als ganze Zahl gröÿer 0 gemessen. Es gebe immer Fragmente mit der Länge 1 sowie
 * weitere Längen. Alle vorhandenen Längen werden in der Menge F = f1; c2; c3; : : : ; cng
 * beschrieben sind. Weiterhin unterstellen wir, dass es unendlich viele Fragmente einer
 * bestimmten Länge gibt. Sei P(F) die (unendlich groÿe) Menge aller Multi-Mengen, die
 * sich aus Elementen aus F bilden lässt.
 * Gesucht ist die kleinste Multi-Menge C 2 P(F), deren Fragmente in der Summe
 * eine Gesamtlänge l haben. Klein bezieht sich dabei auf die Anzahl der Elemente.
 * Beispiel: Sei
 * F = f1; 2; 3g und l = 5
 * Mögliche Multi-Mengen unter der Randbedingung l = 5 sind C1 = f1; 1; 1; 1; 1g;C2 =
 * f1; 1; 1; 2g;M3 = f1; 1; 3g;C4 = f1; 2; 2g sowie C5 = f2; 3g. C = C5, da hier nur zwei
 * Fragmente benötigt werden.
 */
public class DNA_Fragmente extends DynProJava<Integer> {

    private String[] elements;
    private int[] weights;
    private int capacity;

    public static void main(String[] args) {
        //Anfangsbeispiel
        int capacity = 5;
        int[] weights = {1, 2, 3};
        //Zusatzbeispiel Prof. Gumbel
//        int capacity = 50;
//        int[] weights = {5, 25, 30};
        //Aufgabe 2.3
//        int capacity = 58;
//        int[] weights = {1, 3, 5, 6, 9};
        //Aufgabe 2.4//
        //int capacity = 533;
        //int[] weights = {1, 3, 5, 9, 15};
        String[] rowLabels = Arrays.toString(weights).split("[\\[\\]]")[1].split(", ");

        DNA_Fragmente dp = new DNA_Fragmente(rowLabels, weights, capacity);
        // The minimum is expected at the last item (n-1)
        // when maximum capacity is reached;
        List<PathEntry<Integer>> solutionJava =
                dp.solutionAsList(new Idx(dp.n() - 1, 0));
        System.out.println("Optimal Decisions:");
        for (PathEntry<Integer> entry : solutionJava) {
            System.out.print(entry.decision() + " ");
        }
        System.out.println("\n");
        //draw * solution path
        System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n() - 1, 0))));
    }

    public DNA_Fragmente(String[] elements, int[] weights,
                         int capacity) {
        this.elements = elements;
        this.weights = weights;
        this.capacity = capacity;
        // Defines how values are formatted in the console output.
        // Formatter are: INT, ENGINEER, DECIMAL
        this.formatter_$eq(this.INT());
    }

    @Override
    public Object decisions(Idx idx) {
        int value = (capacity - idx.j()) / weights[idx.i()];

        //no decisions to make at the beginning
        if (idx.i() == 0) {
            return new Integer[]{value};

            //gives back list of possible decisions
            //e.g. when an item with weight 2 fits up to twice in the capacity, then result is {0, 1, 2}, because you can either pick the item 0 times, once or twice
        } else {
            Integer[] values = new Integer[value + 1];
            for (int i = 0; i < value + 1; i++) {
                values[i] = i;
            }
            return values;
        }
    }

    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
        //greater than zero to be sure to not be in the very first line, because there is no previous state in the first line
        if (idx.i() > 0) {
            Idx pidx = new Idx(idx.i() - 1, idx.j() + d * weights[idx.i()]);
            return new Idx[]{pidx};
        } else {
            return new Idx[]{};
        }
    }

    @Override
    public double value(Idx idx, Integer d) {
        //just returning the decision, because the decision implies the amount of times we pick the item. And the amount of elements is our value
        //e.g. decision 2 is picking the item twice resulting in having two elements; decision 1 is picking the item once resulting in having one element
        return d;
    }

    @Override
    public int n() {
        return weights.length;
    }

    @Override
    public int m() {
        return capacity + 1;
    }

    @Override
    public Option<String[]> columnLabels() {
        String[] cArray = new String[capacity + 1];
        for (int i = 0; i <= capacity; i++) {
            cArray[i] = "" + i;
        }
        return new Some(cArray);
    }

    @Override
    public String[] rowLabels() {
        return elements;
    }

    @Override
    public Function2 extremeFunction() {
        //unlike the Knapsack problem, we are seeking the minimum in here
        return this.MIN(); // or MAX()
    }
}
