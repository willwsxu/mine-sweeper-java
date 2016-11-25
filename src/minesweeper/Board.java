
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.util.Random;

/**
 *
 * @author WXU
 */
public class Board {
    int[][] values;  // number of mines around this spot, -1 means mine, 
    boolean[][] hidden;  // don't print unless is opened
    
    int rows, cols; //row and column of a board
    
    {}
    {
    }
    
    public Board(int r, int c)
    {
        rows=r;
        cols=c;
        values = new int[rows][cols];
        hidden = new boolean[rows][cols];
        /*  don't set value in this kind of loop
        for (int[] rows: values)
        {
            for (int value: rows)
                value=1;
        }*/
        for (int i=0; i<rows;i++)
        {
            for (int j=0; j<cols; j++) {
                hidden[i][j]=true;
                values[i][j]=0;
            }
        }
    }
    // overloaded constructor for square board
    public Board(int rc)
    {
        this(rc,rc); // call overloaded constructor with two params to avoid code duplicarion
    }
    
    public boolean isOpened(int i, int j)
    {
        return !hidden[i][j];
    }
    public int getMines(int i, int j)
    {
        return values[i][j];
    }
    
    // r from 1 to rows, r from 1 to cols
    // open a spot on the board
    // return false if it hits a mine
    public boolean open(int r, int c)
    {
        if (r>rows || c>cols || r<1 || c<1)
            return false;
        if (hidden[r-1][c-1])
            hidden[r-1][c-1]=false;
        else
            System.out.println("Already open. try a new square");
        openAround0(r-1,c-1);
        return values[r-1][c-1]!=-1;
    }
    
    // randomly sett mines on the board
    public void setMines(int mines)
    {
        if ( mines > rows*cols)
            mines = rows*mines;// mines cannot be more than squares on the board
        Random  rand = new Random();
        while (mines>0){
            int r=rand.nextInt(rows); // random number from 0 to rows-1 inclusive
            int c=rand.nextInt(cols);
            // check if it is not a mine already
            if (values[r][c] != -1) {  // without this check, we could have less
                values[r][c] = -1;
                mines--;
            }
        }
        computeMinesNearby();
    }
    
    // compute mines adjacent to a square, max 8
    private void computeMinesNearby()
    {
        for (int i=0; i<rows;i++)
        {
            for (int j=0; j<cols; j++) {
                int mines=0;
                if (values[i][j]==-1)  // no calculation if it is a mine
                    continue;  // skip to next on this row
                if (i>0) {// check row above
                    if (j>0 && values[i-1][j-1]==-1)
                        mines++;
                    if (values[i-1][j]==-1)
                        mines++;
                    if (j<cols-1 && values[i-1][j+1]==-1)
                        mines++;
                }
                if (j>0 && values[i][j-1]==-1)
                    mines++;
                if (j<cols-1 && values[i][j+1]==-1)
                    mines++;
                if (i<rows-1) {// check row below
                    if (j>0 && values[i+1][j-1]==-1)
                        mines++;
                    if (values[i+1][j]==-1)
                        mines++;
                    if (j<cols-1 && values[i+1][j+1]==-1)
                        mines++;
                }
                values[i][j]=mines;
                // debugging code, helps find out why the calulation is wrong
                //System.out.print("("+i+","+j+")="+mines);
            }
            //System.out.print("\n");//debugging code
        }
    }
    
    // helper method: open it if it is not a mine or warning
    private void setOpen(int r, int c)
    {
        if ( !hidden[r][c])
            return; // already
   
        if (values[r][c]!=-1) {
            hidden[r][c]=false;
            if (values[r][c]==0)
                openAround0(r, c);  // recursively call to open squares around all 0
        }
        else
            System.out.println("("+r+","+c+") expect no mine");
    }
    
    // if a square has 0 mines around, we should go ahead to open all its surrounding
    // borrow logic from computeMinesNearby to find surrounding squares
    private void openAround0(int i, int j)
    {
        if (values[i][j]!=0)
            return;   // should be 0
        if (i>0) {// check row above
            if (j>0)
                setOpen(i-1, j-1);
            setOpen(i-1, j);
            if (j<cols-1)
                setOpen(i-1, j+1);
        }
        if (j>0)
            setOpen(i, j-1);
        if (j<cols-1)
            setOpen(i, j+1);
        if (i<rows-1) {// check row below
            if (j>0)
                setOpen(i+1, j-1);
            setOpen(i+1, j);
            if (j<cols-1)
                setOpen(i+1, j+1);
        }
    }
    
    // won if all squares are open except those mines
    public boolean won()
    {
        for (int i=0; i<rows;i++)
        {
            for (int j=0; j<cols; j++) {
                if (hidden[i][j] && values[i][j]!=-1) // find a closed and not mine 
                    return false;
            }
        }
        return true;
    }
    
    // draw # if not hidden or showAll is true
    public void draw(boolean showAll)
    {
        for (int i=0; i<rows;i++)
        {
            for (int j=0; j<cols; j++) {
                if (showAll || !hidden[i][j]) {
                    if (values[i][j]==-1)
                        System.out.print("! ");  // it is a mine
                    else
                        System.out.print(values[i][j]+" ");
                }
                else
                    System.out.print("  ");
            }
            System.out.print("\n");
        }
    }
       
    // draw a full line of dashes
    private void drawLine()
    {
        for (int j=0; j<cols*2+1;j++)
            System.out.print("-");
        System.out.print("\n");
    }
    // draw with grid so we can tell unopened space, use dash and bar
    // showAll=true: draw all squares, else just draw opened squares
    public void drawGrid(boolean showAll)
    {
        for (int i=0; i<rows;i++)
        {
//            drawLine();
            for (int j=0; j<cols; j++) {
                System.out.print("|");
                if (showAll || !hidden[i][j]) {
                    if (values[i][j]==-1)
                        System.out.print("!");  // it is a mine
                    else
                        System.out.print(values[i][j]);
                }
                else
                    System.out.print(" ");
            }
            System.out.print("|\n");
        }
        drawLine();
    }
}
