package sudoku;

import java.util.HashSet;
import java.util.Set;

public class SudokuEntry implements Comparable<SudokuEntry>{

	private Set<Integer> possibles;
	private final static int c = 9; //sudoku constant
	private int iIndex;
	private int jIndex;

	public SudokuEntry(int i, int[] row, int[] col, int[] square, int iIndex, int jIndex){
		this.iIndex = iIndex;
		this.jIndex = jIndex;

		possibles = new HashSet<Integer>();
		
		if(i==0){
			boolean[] b = new boolean[c+1];
			b[0] = true;
			for(int j = 0; j < c; j++){
				b[row[j]] = true;
				b[col[j]] = true;
				b[square[j]] = true;
			}
			for(int j = 0; j < c+1; j++){
				if(!b[j]) possibles.add(j);
			}
		}
	}

	public Set<Integer> getPossibles(){
		return possibles;
	}

	public int getSize(){
		return possibles.size();
	}

	public int getI(){
		return iIndex;
	}

	public int getJ(){
		return jIndex;
	}

	public String toString(){
		return "" + getSize() + " " + iIndex + " " + jIndex;
	}

	@Override
	public int compareTo(SudokuEntry other) {
		return this.getSize() - other.getSize();
	}
}
