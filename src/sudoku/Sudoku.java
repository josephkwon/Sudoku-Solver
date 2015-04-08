package sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Sudoku {

	private final static String defaultFilePath = "src/sudoku/importSudoku.txt";
	private final static String defaultOutputPath = "src/sudoku/answerSudoku.txt";
	private final static int c = 9; //sudoku constant
	private final static int[][] squares = {{0,0},{0,3},{0,6},{3,0},{3,3},{3,6},{6,0},{6,3},{6,6}};
	private int[][] board;
	private PriorityQueue<SudokuEntry> q;
	private boolean[] b;

	public Sudoku(int[][] in){
		board = in;
		q = new PriorityQueue<SudokuEntry>();
		b = new boolean[c];
		int[][] colBoard = new int[c][c];
		int[][] squaresBoard = new int[c][c];
		for(int i = 0; i < c; i++){
			colBoard[i] = getCol(i);
			squaresBoard[i] = getSquare(i);
		}
		for(int i = 0; i < c; i++){
			for(int j = 0; j < c; j++){
				SudokuEntry n = new SudokuEntry(board[i][j], board[i], colBoard[j], squaresBoard[whatSquare(i,j)], i, j);
				if(n.getSize()>0) q.add(n);
			}
		}
	}

	private static int whatSquare(int i, int j){
		int ans = 0;
		if(j>2) ans++;
		if(j>5) ans++;
		if(i>2) ans+=3;
		if(i>5) ans+=3;
		return ans;
	}

	private boolean miniCheck(int[] a){
		for(int i = 0; i < c; i++){
			b[i] = false;
		}
		for(int i = 0; i < c; i++){
			if(a[i]==0) return false;
			b[a[i]-1] = true;
		}
		for(int i = 0; i < c; i++){
			if(!b[i]) return false;
		}
		return true;
	}

	private int[] getSquare(int index){
		if(index > 8 || index < 0) throw new RuntimeException("Wrong index in getSqaure");
		int[] r = new int[c];
		int endi = squares[index][0];
		int endj = squares[index][1];
		int t = 0;
		for(int i = endi; i < endi+3; i++){
			for(int j = endj; j < endj+3; j++){
				r[t++] = board[i][j];
			}
		}
		return r;
	}

	private int[] getRow(int index){
		if(index > 8 || index < 0) throw new RuntimeException("Wrong index in getRow");
		return board[index];
	}

	private int[] getCol(int index){
		if(index > 8 || index < 0) throw new RuntimeException("Wrong index in getCol");
		int[] temp = new int[c];
		for(int j = 0; j < c; j++){
			temp[j] = board[j][index];
		}
		return temp;
	}

	public boolean checkSudoku(){
		for(int i = 0; i < c; i++){
			//check row
			if(!miniCheck(getRow(i))) return false;

			//check column
			if(!miniCheck(getCol(i))) return false;

			//check square
			if(!miniCheck(getSquare(i))) return false;
		}
		return true;
	}

	public static Sudoku importSudoku(String filePath){
		int[][] in = new int[c][c];
		try {
			String s = "";
			int i = 0;
			BufferedReader b = new BufferedReader(new FileReader(filePath));
			while((s = b.readLine()) != null){ //should only go to nine.
				char[] temp = s.replaceAll(" ", "").toCharArray();
				for(int j = 0; j < s.length(); j++){
					int t = (int)temp[j]-48;
					if(t>9||t<0) {
						b.close();
						throw new RuntimeException("Wrong numbers. Only 0-9");
					}
					in[i][j] = t;
				}
				i++;
			}
			b.close();
			if(i != 9) throw new RuntimeException("Wrong Number of Lines!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File Not Found!");
		} catch (IOException ie) {
			ie.printStackTrace();
			System.out.println("Cannot Read!");
		} catch (Throwable t){
			t.printStackTrace();
			System.out.println("Not in Sudoku Format!");
		}
		System.out.println("Successfully Imported!");
		return new Sudoku(in);
	}

	public static Sudoku importSudoku(){
		return importSudoku(defaultFilePath);
	}

	public static void writeSudoku(Sudoku s){
		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(defaultOutputPath));
			b.write(s.toString());
			b.close();
		} catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	private boolean vIsNull(){
		if(checkSudoku()){
			writeSudoku(this);
			return true;
		}else{
			return false;
		}
	}

	public boolean solve(){
		SudokuEntry v = q.poll();
		if(v == null) {
			return vIsNull();
		}
		
//		while(v.getSize() == 0){ //only add >0 in constructor
//			v = q.poll();
//			if(v == null) {
//				return vIsNull();
//			}
//		}
		
		while(v.getSize() == 1){
			int temp = (int) v.getPossibles().iterator().next();
			board[v.getI()][v.getJ()] = temp;
			v = q.poll();
			if(v == null) {
				return vIsNull();
			}
		}
		
		for(int i : v.getPossibles()){
			int[][] temp = new int[c][c];
			for(int j = 0; j < c; j++){
				temp[j] = Arrays.copyOf(board[j], c);
			}
			temp[v.getI()][v.getJ()] = i;
			Sudoku s = new Sudoku(temp);
			if(s.checkSudoku()) {
				writeSudoku(s);
				return true;
			}
			else {
				if(s.solve()){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void start(){
		if(solve()) System.out.println("Answer Found!!");
		else System.out.println("Something went wrong :(");
	}

	public String toString(){
		String s = "";
		for(int i = 0; i < c; i++){
			for(int j = 0; j < c; j++){
				s += board[i][j] + " ";
			}
			s += "\n";
		}
		return s.substring(0,s.length()-1);
	}

	public static void main(String[] args) {
		Sudoku s = importSudoku();
		s.start();
	}

}
