package reactiveAgent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class ReactiveAgent implements Agent {

    private Cell cell;
    private ArrayList<Cell> cellsList;

    public ReactiveAgent(Cell cell) {
        this.cell = cell;
        this.cell.setAgent(this);
        cellsList = new ArrayList<>();
    }

    public void act(Environment environment) {
        Perception perception = buildPerception(environment);
        Action action = decide(perception);
        execute(action, environment);
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell.setAgent(null);
        this.cell = cell;
        cellsList.add(cell);

        if(cell.hasGarbage())
            cell.setGarbage(null);

        cell.incrementCellVisits();;

        this.cell.setAgent(this);
    }

    public Color getColor() {
        return Color.BLACK;
    }

    private Perception buildPerception(Environment environment) {
        return new Perception(
                environment.getNorthCell(cell),
                environment.getSouthCell(cell),
                environment.getEastCell(cell),
                environment.getWestCell(cell));
    }

    private Action decide(Perception perception) {
        Cell n = perception.getN(),
                s = perception.getS(),
                w = perception.getW(),
                e = perception.getE();

        int totalN, totalW, totalS, totalE;
        totalN = totalW  = totalS = totalE = Integer.MAX_VALUE;


        if(n != null && n.hasGarbage())
            return Action.NORTH;
        else if(s != null && s.hasGarbage())
            return Action.SOUTH;
        else if (w != null && w.hasGarbage())
            return Action.WEST;
        else if(e != null && e.hasGarbage())
            return Action.EAST;


        if(canMove(n)){
            totalN = Collections.frequency(cellsList, n);
        }
        if(canMove(w)){
            totalW = Collections.frequency(cellsList, w);
        }
        if(canMove(s)){
            totalS = Collections.frequency(cellsList, s);
        }
        if(canMove(e)){
            totalE = Collections.frequency(cellsList, e);
        }

        if(totalN <= totalW && totalN <= totalS && totalN <= totalE)
            return Action.NORTH;
        else if(totalW <= totalS && totalW <= totalE)
            return Action.WEST;
        else if(totalS <= totalE)
            return Action.SOUTH;
        else
            return Action.EAST;

    }

    private boolean canMove(Cell c) {
        return c != null && !c.hasWall() && !c.hasAgent();
    }

    private void execute(Action action, Environment environment) {

        // todo modify to improve the ReactiveAgent's decision process
        
        Cell nextCell = null;

        if (action == Action.NORTH && environment.hasNorthCell(cell)) {
            nextCell = environment.getNorthCell(cell);
        } else if (action == Action.SOUTH && environment.hasSouthCell(cell)) {
            nextCell = environment.getSouthCell(cell);
        } else if (action == Action.WEST && environment.hasWestCell(cell)) {
            nextCell = environment.getWestCell(cell);
        } else if (action == Action.EAST && environment.hasEastCell(cell)) {
            nextCell = environment.getEastCell(cell);
        }

        if (nextCell != null && !nextCell.hasWall() && !nextCell.hasAgent()) {
            setCell(nextCell);
        }
    }
}
