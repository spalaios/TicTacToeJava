import java.util.ArrayList;
import java.util.Scanner;
public class TicTacMain {
    public static void main(String[] args) {
        TicTac ticTac = new TicTac();
        ticTac.setFirstPlayer(Constants.USER);
        int boardLength = 9;
        Scanner sc = new Scanner(System.in);
        while (boardLength != 0) {
            System.out.println("Player 1 make your move");
            String firstPlayerMove = sc.nextLine();
            String [] cellArray = firstPlayerMove.split(",");
            Cell cell;
            String positionType = ticTac.getCellPositionType(new Cell(Integer.parseInt(cellArray[0]), Integer.parseInt(cellArray[1]), 1));
            switch (positionType) {
                case Constants.CENTER: {
                    cell = new Cell(Integer.parseInt(cellArray[0]), Integer.parseInt(cellArray[1]), Constants.CENTER);
                    break;
                }
                case Constants.CORNER: {
                    cell = new Cell(Integer.parseInt(cellArray[0]), Integer.parseInt(cellArray[1]), Constants.CORNER);
                    break;
                }
                default:
                    cell = new Cell(Integer.parseInt(cellArray[0]), Integer.parseInt(cellArray[1]), Constants.MIDDLE);
                    break;
            }
            System.out.println("Player 1 slot given -> "+cell.getX()+" "+cell.getY());
            ticTac.markCell(cell, ticTac.getPlayer1());
            if (ticTac.isPathMatched()) {
//                System.out.println("Path matched");
                System.out.println("Player 1 wins");
                System.out.println("Game Over");
                break;
            }
            Cell cellForComputer = ticTac.getNextSlot();
            if(cellForComputer.getX() == -1 && cellForComputer.getY() == -1) {
                System.out.println("Game Over");
                System.out.println("Match draw");
                break;
            }
            System.out.println("Player 2 slot given -> "+cellForComputer.getX()+" "+cellForComputer.getY());
            ticTac.markCell(cellForComputer, ticTac.getPlayer2());
            if (ticTac.isPathMatched()) {
//                System.out.println("Path matched");
                System.out.println("Player 2 wins");
                System.out.println("Game Over");
                break;
            }
            boardLength--;
        }
//        ArrayList<Cell> al = ticTac.getAllSlotsOfTheBoard();
//        for (Cell c : al) {
//            System.out.println("Cell details"+c.getX()+","+c.getY()+"position -> "+c.getPosition());
//        }



    }
}
