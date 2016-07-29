/*
Quoc Le
COP 3503-0001
Sudoku
21 March 2016
*/
import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sudoku extends Applet implements ActionListener{

	Button solve;
	Button load;
	boolean loaded =false;
	static final int NUMROWS = 9;
	static final int NUMCOLS = 9;
	static final int  SQUAREWIDTH = 50;
	static final int SQUAREHEIGHT = 50;
	static  final int BOARDLEFT = 50;
	static final int BOARDTOP = 50;
	int Board[][] = new int[9][9];
	public void LoadData() throws FileNotFoundException
	{
		// Open the file for reading. Will almost always be input.txt
		Scanner objScanner = new Scanner(new File("input.txt"));
		
		// Iterate through the rows.
		for( int nRow=0; nRow<NUMROWS; nRow++ )
		{
			// Iterate through the columns.
			for( int nColumn=0; nColumn<NUMCOLS; nColumn++ )
			{
				// Read each value. Note that this is [row][col] but could be [col][row] in
				//   other contexts.
				Board[nRow][nColumn] = objScanner.nextInt();
			}
		}
		// Closing is good practice and avoids an eclipse warning.
		objScanner.close();
	}
	public void init()
	{
		setSize(1020, 700);
		solve = new Button("Solve it");
		load = new Button("Load");
		load.addActionListener(this);
		solve.addActionListener(this);
		add(solve);
		add(load);
	}

	
	public void paint(Graphics canvas)
	{
		DrawSquares(canvas);
	}
	void DrawSquares( Graphics canvas )
	{
		canvas.setColor(Color.BLACK);
		for( int nRow=0; nRow<NUMROWS; nRow++ )
		{
			for( int nCol=0; nCol<NUMCOLS; nCol++ )
			{
				canvas.drawRect( BOARDLEFT + nCol * SQUAREWIDTH,
					BOARDTOP + nRow * SQUAREHEIGHT, SQUAREWIDTH, SQUAREHEIGHT );
				canvas.drawString(""+Board[nRow][nCol], BOARDLEFT/2 + (nCol+1) * SQUAREWIDTH, BOARDTOP/2 + (nRow+1) * SQUAREHEIGHT);
			}
		}
	}
	public int FindUnassignedLocation( )
	{
		  for( int row=0; row<NUMROWS; row++ )
		  {
		    for( int col=0; col<NUMCOLS; col++ )
		    {
		      if( Board[row][col] == 0 )
		      {
		        return( col | ( row << 8 ) );
		      }
		    }
		  }

		return( -1 );
	}
	public boolean SolveSudoku()
	{
		int result = FindUnassignedLocation();
		if( result == -1 ) return true;
		int row = result >> 8;
		int col = result & 0xff;
		
		for( int num=1; num<=9; num++ )
		{
			
			if( IsPromising(  row, col, num ) )
			{
				Board[row][col] = num;
				fix(row, col);
				if( SolveSudoku() )
				{
					return true;
				}
				Board[row][col] = 0;
			}
			
		}

		return false; // this triggers backtracking
	}
	public boolean IsPromising(  int row, int col, int num )
	{
		if( !UsedInRow(  row, num ) &&
			!UsedInCol( col, num ) &&
			!UsedInBox( row-(row%3), col-(col%3), num ) )
		{
			return( true );
		}
		
		return false;
	}
	 boolean UsedInRow( int row, int num)
	{
		for( int col=0; col<9; col++ )
		{
			if( Board[row][col] == num ) return true;
		}
		
		return false;
	}
	 
	/* Returns a boolean which indicates whether any assigned entry
	   in the specified column matches the given number. */
	public boolean UsedInCol(int col, int num)
	{
		for( int row=0; row<9; row++ )
		{
			if( Board[row][col] == num ) return true;
		}

		return false;
	}
	 
	/* Returns a boolean which indicates whether any assigned entry
	   within the specified 3x3 box matches the given number. */
	public boolean UsedInBox(  int boxStartRow, int boxStartCol, int num )
	{
		for( int row=boxStartRow; row<boxStartRow+3; row++ )
		{
			for( int col=boxStartCol; col<boxStartCol+3; col++ )
			{
				if( Board[row][col] == num ) return true;
			}
		}
		
		return false;
	}
	 
	
	public void clear()//clear the board
	{
		Graphics graphic = getGraphics();
		Color c = getBackground();
		Dimension d = getSize();
		graphic.setColor(c);
		graphic.fillRect(0, 0, d.width, d.height);
		paint(graphic);
		
	}
	public void fix(int row, int col)
	{
		//clear the square
		Graphics canvas = getGraphics();
		Color c = getBackground();
		canvas.setColor(c);
		canvas.fillRect( BOARDLEFT + col* SQUAREWIDTH +1,
				BOARDTOP + row * SQUAREHEIGHT+1, SQUAREWIDTH-2, SQUAREHEIGHT -2);
		
		//Make it apparent which value is changed 
		Font currentFont = canvas.getFont();
		canvas.setColor(Color.red);
		canvas.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
		canvas.drawString(""+Board[row][col], BOARDLEFT/2 + (col+1) * SQUAREWIDTH, BOARDTOP/2 + (row+1) * SQUAREHEIGHT);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//clear the square again
		c = getBackground();
		canvas.setColor(c);
		canvas.fillRect( BOARDLEFT + col* SQUAREWIDTH +1,
				BOARDTOP + row * SQUAREHEIGHT+1, SQUAREWIDTH-2, SQUAREHEIGHT -2);
		
		//print the value in the normal font
		canvas.setColor(Color.black);
		canvas.setFont(currentFont);
		canvas.drawString(""+Board[row][col], BOARDLEFT/2 + (col+1) * SQUAREWIDTH, BOARDTOP/2 + (row+1) * SQUAREHEIGHT);

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Graphics canvas = getGraphics();
		if (e.getSource() == load)//if the load button is click, load the information
		{
			try {
				LoadData();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		clear();
		
		loaded = true;
		}
		if (e.getSource() == solve)
		{
			if (loaded)// if load, solve the sudoku
			{
				clear();
				if( SolveSudoku() == true )
				{
					clear();
					canvas.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
					canvas.drawString("Solution", 50, 45);
				}
			    else //if it doesn't work, tell user that there is no solution
			    {
					Font currentFont = canvas.getFont();
					canvas.setColor(Color.red);
					canvas.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
					canvas.drawString("No Solution", 50, 45);
					canvas.setColor(Color.black);
					canvas.setFont(currentFont);
			    }
			}
			else //tell user to load the information
			{
				Font currentFont = canvas.getFont();
				canvas.setColor(Color.red);
				canvas.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
				canvas.drawString("Load Data", 50, 45);
				canvas.setColor(Color.black);
				canvas.setFont(currentFont);
			}
		}
	}
	
}
