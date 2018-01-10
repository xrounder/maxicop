package extra;

import java.util.List;

import net.gumbix.dynpro.DynProJava;
import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.PathEntry;
import scala.Function2;
import scala.Option;
import scala.Some;

public class ViterbiAlgorithm extends DynProJava<Integer> {

	public static void main(String[] args) {
		int[] diceRolls = new int[] {
				6, 2, 2, 3, 1, 5, 3, 6, 3, 4, 1, 3, 1, 5, 6, 4, 6, 3, 6, 6, 6, 4, 3, 5, 5, 4, 6, 6, 5, 5, 2, 1, 3, 4, 6, 6, 6, 5, 3, 6, 6, 6, 6, 2, 6, 5, 3, 1, 5, 2,
				1, 4, 6, 4, 5, 1, 6, 4, 5, 1, 5, 5, 1, 4, 2, 4, 3, 3, 2, 2, 1, 6, 4, 6, 1, 6, 5, 4, 3, 2, 5, 2, 1, 5, 5, 5, 4, 3, 3, 5, 2, 5, 5, 6, 4, 4, 6, 3, 1, 6,
				4, 3, 6, 2, 4, 4, 3, 1, 1, 3, 2, 5, 3, 6, 5, 6, 2, 6, 3, 6, 3, 4, 1, 6, 2, 1, 6, 4, 6, 4, 6, 1, 6, 6, 6, 6, 4, 6, 6, 6, 3, 3, 5, 3, 5, 6, 1, 4, 1, 5,
				6, 1, 6, 1, 5, 5, 4, 6, 6, 4, 1, 2, 2, 1, 1, 4, 6, 3, 3, 6, 6, 5, 6, 5, 3, 4, 2, 2, 5, 6, 5, 6, 6, 6, 6, 6, 6, 1, 6, 6, 4, 6, 4, 6, 5, 5, 3, 2, 4, 6,
				6, 6, 1, 6, 6, 6, 1, 4, 5, 5, 2, 3, 6, 5, 6, 3, 2, 6, 6, 6, 2, 2, 1, 5, 2, 6, 1, 1, 6, 6, 4, 2, 4, 3, 4, 6, 4, 1, 3, 1, 4, 3, 6, 5, 3, 6, 1, 3, 6, 6,
				5, 4, 3, 6, 1, 5, 4, 5, 3, 5, 6, 2, 4, 6, 4, 2, 4, 3, 5, 3, 3, 5, 6, 2, 6, 5, 6, 1, 3, 4, 2, 2, 5, 4, 6, 6, 1, 4, 5, 3, 6, 6, 2, 5, 1, 6, 1, 4, 3, 5,
				4, 3, 6, 3, 3, 6, 1, 6, 2, 5, 6, 6, 4, 6, 6, 1, 1, 6, 6, 3, 5, 2, 3, 3, 4, 2, 4, 2, 6, 1, 6, 4, 1, 4, 6, 6, 6, 6, 6, 1, 4, 1, 2, 6, 6, 6, 4, 1, 6, 5,
				2, 4, 6, 6, 6, 5, 5, 4, 2, 5, 4, 4, 2, 1, 3, 3, 5, 5, 5, 1, 1, 4, 2, 6, 6, 2, 5, 6, 4, 6, 6, 4, 5, 4, 1, 3, 4, 4, 3, 6, 5, 6, 3, 4, 6, 6, 5, 2, 4, 1,
				3, 5, 6, 5, 3, 5, 3, 6, 6, 6, 3, 3, 2, 6, 6, 6, 6, 6, 2, 6, 5, 3, 6, 6, 6, 3, 6, 6, 2, 5, 3, 6, 3, 6, 6, 4, 5, 6, 6, 6, 6, 4, 5, 6, 6, 6, 1, 6, 5, 5,
				2, 6, 4, 3, 4, 4, 6, 6, 4, 3, 4, 4, 6, 5, 3, 5, 1, 1, 1, 1, 2, 2, 1, 4, 1, 1, 4, 6, 6, 4, 6, 4, 4, 2, 3, 3, 1, 6, 1, 3, 5, 3, 4, 5, 6, 6, 2, 2, 6, 4
		};
		
		// F = fair
		// U = unfair
		char[] diceTrait = new char[] {'F', 'U'};
		double[][] transitionChances = new double[][] {{0.5, 19.0 / 20.0, 1.0 / 20.0}, {0.5, 1.0 / 20.0, 19.0 / 20.0}};
		double[][] emmisionChances = new double[][] {{1.0 / 6.0, 1.0 / 10.0}, {1.0 / 6.0, 1.0 / 10.0}, {1.0 / 6.0, 1.0 / 10.0}, {1.0 / 6.0, 1.0 / 10.0}, {1.0 / 6.0, 1.0 / 10.0}, {1.0 / 6.0, 0.5}};
		ViterbiAlgorithm va = new ViterbiAlgorithm(diceRolls, diceTrait, transitionChances, emmisionChances);
	    // The maximum is expected at the last item (n-1)
	    // with no capacity left (0);
	    List<PathEntry<Integer>> solutionJava =
	    		va.solutionAsList(new Idx(va.n() - 1, va.m() - 1));
	   
	    System.out.println("Optimal Decisions:");
	    for (PathEntry<Integer> entry : solutionJava) {
	      System.out.print(entry.decision() + " ");
	    }
	    System.out.println("\n");
	    System.out.println(va.mkMatrixString(va.solution(new Idx(va.n() - 1, va.m() - 1))));
	    System.out.println(transitionChances.length + "  " + transitionChances[0].length);
	}
	
	
	private int[] diceRolls;
	private char[] diceTrait;
	private double[][] transitionChances;
	private double[][] emmisionChances;
	

	public ViterbiAlgorithm(int[] diceRolls, char[] diceTrait, double[][] transitionChances, double[][] emmisionChances) {
		this.diceRolls = diceRolls;
		this.diceTrait = diceTrait;
		this.transitionChances = transitionChances;
		this.emmisionChances = emmisionChances;

		this.formatter_$eq(this.ENGINEER());
	}
	
	
	@Override
	public int n() {
		//zeile
		return diceRolls.length;
	}

	@Override
	public int m() {
		//spalte
		return diceTrait.length;
	}

	@Override
	public double value(Idx idx, Integer d) {
		double value = 1.0;
		
		if (idx.i() == 0) {
			// d + 1 because transition table contains q0 state
			value *= this.transitionChances[0][d + 1];
			value *= this.emmisionChances[this.diceRolls[idx.i()] - 1][d];
			
		} else {
			// d + 1 because transition table contains q0 state
			value *= this.transitionChances[idx.j()][d + 1];
			value *= this.emmisionChances[this.diceRolls[idx.i()] - 1][d];
		}
		
		return Math.log10(value);
	}

	/**
	 * If the remaining capacity (idx.j) plus the weight that could be taken
	 * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
	 * If not, we can only skip it (={0}).
	 */
	@Override
	public Integer[] decisions(Idx idx) {
		return new Integer[] {0, 1};
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
		String strArray[] = new String[diceRolls.length];

		for (int i = 0; i < diceRolls.length; i++) {
			strArray[i] = String.valueOf(diceRolls[i]);
		}
			
	    return strArray;
	}

	/**
	 * Provide column labels, i.e. each columns gets a short description.
	 * In this case, the column labels are the same as the column index.
	 *
	 * @return Array of size m with the labels.
	 */
	@Override
	public Option<String[]> columnLabels() {
	    String[] cArray = new String[diceTrait.length];
	    for (int i = 0; i < diceTrait.length; i++) {
	    	cArray[i] = "" + diceTrait[i];
	    }
	    	return new Some(cArray);
	}

}
