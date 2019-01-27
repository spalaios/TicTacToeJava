import java.awt.*;
import java.util.*;

public class TicTac {
    //player 1
    private Player player1;
    private Player player2;
    private int boardLength = 9;
    private HashMap<Cell, Integer> markedCellMap;
    private boolean pathMatched = false;
    private final int startIndex = 0;
    private int gridSize = (int) Math.sqrt(boardLength);
    private final int endIndex = gridSize - 1;

    public boolean isPathMatched() {
        return pathMatched;
    }

    public void setPathMatched(boolean pathMatched) {
        this.pathMatched = pathMatched;
    }

    public TicTac() {
        markedCellMap = new HashMap<>();
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setFirstPlayer(String playerType) {
        if(playerType.equalsIgnoreCase(Constants.USER)) {
            player1 = new Player(Constants.USER, 1);
            player2 = new Player(Constants.COMPUTER, 0);
        }else if(playerType.equalsIgnoreCase(Constants.COMPUTER)) {
            player1 = new Player(Constants.COMPUTER, 1);
            player2 = new Player(Constants.USER, 0);
        }
    }

    private boolean isCornerCells(int i, int j) {
        return isZeroOrTwo(i) && isZeroOrTwo(j);
    }

    private boolean isZeroOrTwo(int value) {
        if(value == 0 || value == 2) {
            return true;
        }
        return false;
    }

    public String getCellPositionType(Cell cell) {
        String postion = "NA";
        int i = cell.getX();
        int j = cell.getY();
//        for(int i=0; i<gridSize; i++) {
//            for(int j=0; j<gridSize; j++) {
                if(i == ((gridSize-1)/2) && j == ((gridSize-1)/2)) {
                    postion = Constants.CENTER;
                }else if(isCornerCells(i, j)){
                    postion = Constants.CORNER;
                }else {
                    postion = Constants.MIDDLE;
                }
//            }
//        }
        return postion;
    }

    public ArrayList<Cell> getAllSlotsOfTheBoard() {
//        int gridSize = (int) Math.sqrt(boardLength);
        ArrayList<Cell> cellList = new ArrayList<Cell>();
        for(int i=0; i<gridSize; i++) {
            for(int j=0; j<gridSize; j++) {
                String positionType = getCellPositionType(new Cell(i, j));
                switch (positionType) {
                    case Constants.CENTER: {
                        cellList.add(new Cell(i, j, Constants.CENTER));
                        break;
                    }
                    case Constants.CORNER: {
                        cellList.add(new Cell(i, j, Constants.CORNER));
                        break;
                    }
                    default:
                        cellList.add(new Cell(i, j, Constants.MIDDLE));
                        break;
                }
//                if(i == ((gridSize-1)/2) && j == ((gridSize-1)/2)) {
//                    cellList.add(new Cell(i, j, Constants.CENTER));
//                }else if(isCornerCells(i, j)){
//                    cellList.add(new Cell(i, j, Constants.CORNER));
//                }else {
//                    cellList.add(new Cell(i, j, Constants.MIDDLE));
//                }
            }
        }
        return cellList;
    }

    public void markCell(Cell cell, Player player) {
        this.markedCellMap.put(new Cell(cell.getX(), cell.getY(), player.getWeapon(), cell.getPosition()), player.getWeapon());
        player.increaseMoves();
        if(player.getMoves() > 2) {
            System.out.println("Match detection started");
            startMatchDetectionForCell(cell, player.getWeapon());
        }

    }

    private boolean isCellPresentInHashMap(Cell cell) {
        boolean found = false;
        Set<Cell> keySet = this.markedCellMap.keySet();
        for (Cell key : keySet) {
            if(key.getX() == cell.getX() && key.getY() == cell.getY()) {
                found = true;
                break;
            }
        }
        return found;
    }

    private int getCorrespondingValueForTheCellInHashMap(Cell cell) {
        int value = -1;
        Set<Cell> keySet = this.markedCellMap.keySet();
        for (Cell key : keySet) {
            if(key.getX() == cell.getX() && key.getY() == cell.getX()) {
                value = this.markedCellMap.get(key);
                break;
            }
        }
        return value;
    }

    private boolean detectPathMatch(ArrayList<Cell> path, int playerWeapon) {
        int count = 0;
        for (Cell cell : path) {
            //do the checking here...
            if(isCellPresentInHashMap(cell)) {
                int markedCellValue = getCorrespondingValueForTheCellInHashMap(cell);
                if(markedCellValue != -1) {
                    if(markedCellValue == playerWeapon) {
                        count++;
                    }
                }
            }
        }
        return  count == 3;
    }

    private void updateMatchStatus(ArrayList<ArrayList<Cell>> paths, int playerWeapon) {
        Set<Cell> markedCellKeys = this.markedCellMap.keySet();
        boolean pathMatched = false;
        if(markedCellKeys.size() > 0) {
            for (ArrayList<Cell> path : paths) {
                pathMatched = detectPathMatch(path, playerWeapon);
                if(pathMatched) {
                    System.out.println("Path matched");
                    this.pathMatched = true;
                    break;
                }
            }
        }
    }

    private void startMatchDetectionForCell(Cell cell, int playerWeapon) {
        System.out.println("inside match detection function");
        ArrayList<ArrayList<Cell>> paths = new ArrayList<>();
        for (ArrayList<Cell> adjacentPath: getAdjacentPathsForCell(cell)) {
            paths.add(adjacentPath);
        }
        updateMatchStatus(paths, playerWeapon);
    }

    private boolean isCellCoordinatesOfValue(Cell cell, int value) {
        return cell.getX() == value && cell.getY() == value;
    }

    private ArrayList<Cell> getRightDiagoalOfBoard() {
        ArrayList<Cell> rightDiagonalCells = new ArrayList<>();
        for (int i=startIndex; i<gridSize; i++) {
            rightDiagonalCells.add(new Cell(i, i));
        }
        return rightDiagonalCells;
    }

    private ArrayList<Cell> getLeftDiagoalOfBoard() {
        ArrayList<Cell> leftDiagonalCells = new ArrayList<>();
        int x = endIndex;
        int y = startIndex;
        for (int i=0; i<gridSize; i++) {
            leftDiagonalCells.add(new Cell(x, y));
            x--;
            y++;
        }
        return leftDiagonalCells;
    }

    private ArrayList<Cell> getClonedArrayList(ArrayList<Cell> arrayList) {
        ArrayList<Cell> clonedList = new ArrayList<>();
        for (Cell cell : arrayList) {
            clonedList.add(cell);
        }
        return clonedList;
    }

    private ArrayList<ArrayList<Cell>> generateStraightPathsForCornerCells(Cell cell) {
        ArrayList<ArrayList<Cell>> straightPaths = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();
        ArrayList<Cell> oneWayPath = new ArrayList<>();

        if(y == endIndex) {
            for (int i=endIndex; i>=startIndex; i--) {
                oneWayPath.add(new Cell(x, i));
            }
        }else if(y == startIndex) {
            for (int i=startIndex; i<gridSize; i++) {
                oneWayPath.add(new Cell(x, i));
            }
        }
        straightPaths.add(getClonedArrayList(oneWayPath));
        oneWayPath.clear();

        if(x == endIndex) {
            for (int i=endIndex; i>=startIndex; i--) {
                oneWayPath.add(new Cell(i, y));
            }
        }else if(x == startIndex) {
            for (int i=startIndex; i<gridSize; i++) {
                oneWayPath.add(new Cell(i, y));
            }
        }
        straightPaths.add(getClonedArrayList(oneWayPath));
//        oneWayPath.clear();

        return  straightPaths;
    }

    private ArrayList<ArrayList<Cell>> getAdjacentPathsForCell(Cell cell) {
        ArrayList<ArrayList<Cell>> paths = new ArrayList<>();
        switch (cell.getPosition()) {
            case Constants.CORNER: {
                if((isCellCoordinatesOfValue(cell, startIndex)) || isCellCoordinatesOfValue(cell, endIndex)) {
                    paths.add(getRightDiagoalOfBoard());
                }else {
                    paths.add(getLeftDiagoalOfBoard());
                }
                for (ArrayList<Cell> pathArray: generateStraightPathsForCornerCells(cell)) {
                    paths.add(pathArray);
                }
                break;
            }
            case Constants.CENTER: {
                break;
            }
            case Constants.MIDDLE: {
                break;
            }
            default:{
                break;
            }
        }

        return paths;
    }

    public HashMap<Cell, Integer> getMarkedCellMap() {
        return this.markedCellMap;
    }

    public int getPositionOfCellInCellList(Cell cell, ArrayList<Cell> cellList) {
        if(cellList.size() > 0) {
            for(int i=0; i<cellList.size(); i++) {
                Cell currentCellFromList = cellList.get(i);
                if(currentCellFromList.getX() == cell.getX() && currentCellFromList.getY() == cell.getY()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public ArrayList<Cell> getAvailableSlots() {
        ArrayList<Cell> cellList = getAllSlotsOfTheBoard();
        for(Cell c : this.markedCellMap.keySet()) {
            int cPositionInCellList = getPositionOfCellInCellList(c, cellList);
            cellList.remove(cPositionInCellList);
        }
        return cellList;
    }

    public Cell getNextSlot() {
        ArrayList<Cell> nextMoveSlots = getAvailableSlots();
        if(nextMoveSlots.size() > 0) {
            int randomNumber =  new Random().nextInt(nextMoveSlots.size());
            return nextMoveSlots.get(randomNumber);
        }
        return new Cell(-1, -1);
    }


}
