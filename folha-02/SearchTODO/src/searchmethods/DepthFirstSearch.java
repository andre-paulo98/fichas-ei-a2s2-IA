package searchmethods;

import agent.Problem;
import agent.Solution;
import agent.State;
import java.util.List;
import utils.NodeLinkedList;

public class DepthFirstSearch extends GraphSearch<NodeLinkedList> {

    public DepthFirstSearch() {
        frontier = new NodeLinkedList();
    }

    //Graph Search without explored list
    @Override
    protected Solution graphSearch(Problem problem) {
        frontier.clear();

        frontier.add(new Node(problem.getInitialState()));

        while(!frontier.isEmpty() && !stopped) {
            Node n = frontier.poll();
            State state = n.getState();

            if(problem.isGoal(state))
                return new Solution(problem, n);

            List<State> successors = problem.executeActions(state);
            addSuccessorsToFrontier(successors, n);
            computeStatistics(successors.size());
        }

        return null;
    }

    @Override
    public void addSuccessorsToFrontier(List<State> successors, Node parent) {
        for (State s: successors) {
            if(!frontier.containsState(s) && !parent.isCycle(s)){
                frontier.addFirst(new Node(s, parent));
            }
        }
    }

    @Override
    public String toString() {
        return "Depth first search";
    }
}
