/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeper;

import java.io.InputStream;
import java.util.Scanner;


/**
 *
 * @author Andy
 */
public class Minesweeper {

    Board board;
    
    public void basicGame(int size, int mines)
    {
        board = new Board(size);
        board.setMines(mines);
        while(true) {
            System.out.println("make your move: enter row, follower by col");
            Scanner in = new Scanner(System.in);
            try {
                int row = in.nextInt();
                int col = in.nextInt();
                System.out.println("r="+row+" col="+col);//debug
                if (!board.open(row, col))
                    break;
                if( board.won()) {
                    board.drawGrid(true);
                    System.out.println("You won");
                    return;
                }
                board.drawGrid(false);
            } catch (Exception e)
            {
                System.out.println("exception"+e.toString()+".");
                continue;
            }
        }//end while
        board.drawGrid(true);
        System.out.println("Game over. Try again soon");
    }
    public void testBoard()
    {
        Board board = new Board(10);
        board.setMines(10);  // create 10 mines
        board.open(2, 4);    // coordinate 1,1 is top left corner
        board.draw(false); // draw # if it is not hidden
        board.draw(true);  // draw all #
        board.drawGrid(false);
//        board.computeMinesNearby();
        board.drawGrid(true);
    }
    
    void StartGame()
    {       
        Scanner in = new Scanner(System.in);
        int level=0;
        for (int i=0; i<3; i++) {
            System.out.println("Choose Game level 1 - easy, 2- medium, 3-hard");
            try {
                level = in.nextInt();
            }
            catch (Exception e)
            {
            }
            if (level >0 && level <=3 )
                break;
        }
        switch(level) {
            default:
                System.out.println("Invalid level "+level + ". defaylt to easy");// fall through to next case
            case 1:
                new Minesweeper().basicGame(10, 4);
                break;
            case 2:
                new Minesweeper().basicGame(10, 10);
                break;
            case 3:
                new Minesweeper().basicGame(10, 20);
                break;
        } 
        
    }
    /**
     * @param args the command line arguments
     */
/*    public static void main(String[] args) {
        // TODO code application logic here
        StartGame();
    }*/
}
