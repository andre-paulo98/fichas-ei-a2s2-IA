package searchmethods;

import agent.Problem;
import agent.Solution;
import agent.State;
import java.util.List;

public class IterativeDeepeningSearch extends DepthFirstSearch {
    /*
     * We do not use the code from DepthLimitedSearch because we can optimize
     * so that the algorithm only verifies if a state is a goal if its depth is
     * equal to the limit. Note that given a limit X we are sure not to
     * encounter a solution below this limit because a (failed) limited depth
     * search has already been done. That's why we do not extend this class from
     * DepthLimitedSearch. We extend from DepthFirstSearch so that we don't need
     * to rewrite method insertSuccessorsInFrontier again.
     * After the class, please see a version of the search algorithm without
     * this optimization.
     */    
    
    private int limit;

    @Override
    public Solution search(Problem problem) {
        statistics.reset();
        stopped = false;
        Solution solution;
        limit = 0;

        do {
            solution = graphSearch(problem);
            limit++;
        } while (solution == null);

        return solution;
    }
    
    @Override
    protected Solution graphSearch(Problem problem) {
        frontier.clear();

        frontier.add(new Node(problem.getInitialState()));

        while(!frontier.isEmpty() && !stopped) {
            Node n = frontier.poll();
            State state = n.getState();

            if(n.getDepth() == limit && problem.isGoal(state))
                return new Solution(problem, n);

            int numSucessors = 0;
            if(n.getDepth() < limit) {
                List<State> successors = problem.executeActions(state);
                addSuccessorsToFrontier(successors, n);
                numSucessors = successors.size();
            }
            computeStatistics(numSucessors);

        }

        return null;
    }

    @Override
    public String toString() {
        return "Iterative deepening search";
    }
}


