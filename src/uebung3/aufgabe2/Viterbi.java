package uebung3.aufgabe2;

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
    String[] columnStatesLables = {"q0", "F", "U"};
    String[] hmmLabels = {"Wurf-Nr.", "Würfelzahl", "Zustand"};
    int[] states = {0, 1};
    double[][] emission = {{1d/6d, 1d/6d, 1d/6d, 1d/6d, 1d/6d, 1d/6d},{1d/10d, 1d/10d, 1d/10d, 1d/10d, 1d/10d, 1d/2d}};
    double[][] transition = {{0.5,19d/20d,1d/20d}, {0.5,1d/20d,19d/20d}};

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

    //check which j cell has the highest value and save index j
    double bestValue = -1 * Double.POSITIVE_INFINITY;
    int bestIndex = dp.m()-1;
    for (int j=0; j < dp.m(); j++) {
      double tempValue = 0;
      List<PathEntry<Integer>> entries = dp.solutionAsList(new Idx(dp.n() - 1, j));
      //one cell holds one value, but not the accumulated one; so let's sum it up
      for (PathEntry<Integer> entry : entries){
        tempValue += entry.value();
      }
      if(tempValue >= bestValue){
        bestValue = tempValue;
        bestIndex = entries.get(dp.n()-1).currCell().j();
      }
    }

    //print optimal decisions
    List<PathEntry<Integer>> solutionJava =
            dp.solutionAsList(new Idx(dp.n() - 1, bestIndex));
    System.out.println("Optimal Decisions:");
    for (PathEntry<Integer> entry : solutionJava) {
      System.out.print(entry.decision() + " ");
    }

    //print matrix
    System.out.println("\n");
    scala.collection.immutable.List<PathEntry<Integer>> solution = dp.solution(new Idx(dp.n() - 1, bestIndex));
    System.out.println(dp.mkMatrixString(solution));

    //print hidden-markov-modell
    dp.printHMM(solutionJava, hmmLabels);
  }

  /**
   * prints results in Hidde-Markoc-Model format
   * @param solution the solution containing the calculated states
   * @param rowLabels the labels to label the three different rows
   */
  private void printHMM(List<PathEntry<Integer>> solution, String[] rowLabels) {
    System.out.println();
    System.out.print(rowLabels[0] + "   |");
    for(int i = 0; i < this.n(); i++) {
      System.out.print(i+1 + "|");
    }
    System.out.println();
    System.out.print(rowLabels[1] + " |");
    char[] diceNumbers = this.path.toCharArray();
    for(int i = 0; i < this.path.length(); i++) {
      System.out.print(diceNumbers[i] + "|");
    }
    System.out.println();
    System.out.print(rowLabels[2] + "    |");
    for(PathEntry<Integer> entry : solution) {
      System.out.print(this.columnStatesLables[entry.decision()+1] + "|");
    }
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
    this.formatter_$eq(this.ENGINEER());
  }

  @Override
  public int n() {
    return path.length();
  }

  @Override
  public int m() {
    return states.length;
  }

  @Override
  public double value(Idx idx, Integer d) {

    double value = 0;
    char[] array = path.toCharArray();
    char currentNumber = array[idx.i()];
    int number = Integer.parseInt(currentNumber + "");
    if (idx.i() > 0) {
      value = Math.log10(emission[d][number - 1]) + Math.log10(transition[idx.j()][d + 1]);
    } else {
      value = Math.log10(emission[d][number - 1]) + Math.log10(transition[idx.j()][d]);
    }
    return value;
  }

  /**
   * If the remaining capacity (idx.j) plus the weight that could be taken
   * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
   * If not, we can only skip it (={0}).
   */
  @Override
  public Integer[] decisions(Idx idx) {
    if (idx.i() == 0){
      return new Integer[]{0};
    } else {
      Integer[] decisions = new Integer[this.m()];
      for (int i = 0; i < this.m(); i++){
        decisions[i] = states[i];
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
    String[] rowLabels = new String[path.length()];
    char[] pathLabels = path.toCharArray();

    for (int i = 0; i < path.length(); i++) {
      rowLabels[i] =  pathLabels[i] + "";
    }

    return rowLabels;
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
    for (int i = 0; i < states.length; i++) {
      cArray[i] = columnStatesLables[i+1];
    }
    return new Some(cArray);
  }

}