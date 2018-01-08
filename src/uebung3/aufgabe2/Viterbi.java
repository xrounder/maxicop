import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

import java.util.List;

/**
 * The Viterbi problem solved with dynamic programming.
 *
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Viterbi extends DynProJava<Integer> {

  public static void main(String[] args) {
    String[] rowLabels = {"1", "2", "3", "4", "5", "6"};
    String[] columnStatesLables = {"q0", "U", "F"};
    int[] states = {0, 1, 2};
    double[][] emission = {{1d/6d, 1d/6d, 1d/6d, 1d/6d, 1d/6d, 1d/6d},{1d/10d, 1d/10d, 1d/10d, 1d/10d, 1d/10d, 1d/2d}};
    double[][] transition = {{0d, 0.5, 0.5},{0d,19d/20d,1d/20d},{0d,1d/20d,19d/20d}};
    String diceRoll = "62231536341315646366643554665521346665366662653152" +
            "14645164515514243322164616543252155543352556446316" +
            "43624431132536562636341621646461666646663353561415" +
            "61615546641221146336656534225656666661664646553246" +
            "66166614552365632666221526116642434641314365361366" +
            "54361545356246424353356265613422546614536625161435" +
            "43633616256646611663523342426164146666614126664165" +
            "24666554254421335551142662564664541344365634665241" +
            "35653536663326666626536663662536366456666456661655" +
            "26434466434465351111221411466464423316135345662264";


    Viterbi dp = new Viterbi(rowLabels, columnStatesLables, states, emission, transition, diceRoll);
    // The maximum is expected at the last item (n-1)
    // with no capacity left (0);
    List<PathEntry<Integer>> solutionJava =
            dp.solutionAsList(new Idx(dp.n() - 10, dp.m()-1));
    System.out.println("Optimal Decisions:");
    for (PathEntry<Integer> entry : solutionJava) {
      System.out.print(entry.decision() + " ");
    }
    System.out.println("\n");
    System.out.println(dp.mkMatrixString(dp.solution(new Idx(dp.n() - 10, dp.m()-1))));
  }

  private int[] states;
  private double [][] emission;
  private double [][] transition;
  private String[] alphabet;
  private String[] columnStatesLables;
  private String path;

  public Viterbi(String[] alphabet, String[] columnStatesLables, int[] states, double[][]emission, double[][] transition, String path) {
    this.states = states;
    this.alphabet = alphabet;
    this.emission = emission;
    this.transition = transition;
    this.columnStatesLables = columnStatesLables;
    this.path = path;
    // Defines how values are formatted in the console output.
    // Formatter are: INT, ENGINEER, DECIMAL
    this.formatter_$eq(this.INT());
  }

  @Override
  public int n() {
    return path.length();
  }

  @Override
  public int m() {
    return states.length-1;
  }

  @Override
  public double value(Idx idx, Integer d) {
    return (emission[idx.j()][idx.i()] * transition[d][idx.j()]);
  }

  /**
   * If the remaining capacity (idx.j) plus the weight that could be taken
   * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
   * If not, we can only skip it (={0}).
   */
  @Override
  public Integer[] decisions(Idx idx) {
    if (idx.i() == 0) {
      return new Integer[]{states[0]};
    } else {
      Integer[] decisions = new Integer[states.length-1];

      for(int i = 1; i < states.length;i++){
        decisions[i-1] = states[i];
      }

      return decisions;
    }
  }

  /**
   * The prev. state is the previous item (idx.i-1) and the prev. capacity.
   * The prev. capacity is the remaining capacity (idx.j) plus weight that was
   * taken (or plus 0 if it was skipped).
   */
  @Override
  public Idx[] prevStates(Idx idx, Integer d) {
    if (idx.i() > 0) {
      Idx pidx = new Idx(idx.i() - 1, d);
      return new Idx[]{pidx};
    } else {
      return new Idx[]{};
    }
  }

  /**
   * Defines whether the minimum or maximum is calculated.
   *
   * @return
   */
  @Override
  public Function2 extremeFunction() {
    return this.MAX(); // oder MIN()
  }

  /**
   * Provide row labels, i.e. each row gets a short description.
   *
   * @return Array of size n with the labels.
   */
  @Override
  public String[] rowLabels() {
    return alphabet;
  }

  /**
   * Provide column labels, i.e. each columns gets a short description.
   * In this case, the column labels are the same as the column index.
   *
   * @return Array of size m with the labels.
   */
  @Override
  public Option<String[]> columnLabels() {
    String[] cArray = new String[states.length];
    for (int i = 0; i <= states.length; i++) {
      cArray[i] = columnStatesLables[i];
    }
    return new Some(cArray);
  }
}