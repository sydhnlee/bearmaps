package bearmaps.utils.graph;
import edu.princeton.cs.algs4.Stopwatch;
import java.util.*;

import bearmaps.utils.pq.NaiveMinPQ;
import bearmaps.utils.pq.MinHeapPQ;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private int numStates;
    private double timeSpent;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        Set<Vertex> visited = new HashSet<>();
        solution = new ArrayList<>();
        Map<Vertex, Double> distTo = new HashMap<>();
        Map<Vertex, Vertex> edgeTo = new HashMap<>();
        NaiveMinPQ<Vertex> fringe = new NaiveMinPQ<>();

        distTo.put(start, 0.0);
        fringe.insert(start, 0 + input.estimatedDistanceToGoal(start, end));
        timeSpent = sw.elapsedTime();

        while (fringe.size() != 0 && !fringe.peek().equals(end) && timeSpent <= timeout) {
            Vertex currVertex = fringe.poll();
            visited.add(currVertex);
            timeSpent = sw.elapsedTime();

            for (WeightedEdge e : input.neighbors(currVertex)) {
                Vertex neighbor = (Vertex) e.to();
                double weight = e.weight();
                if (!distTo.containsKey(neighbor) || distTo.get(neighbor) > distTo.get(currVertex) + weight) {
                    distTo.put(neighbor, (double) distTo.get(currVertex) + weight);
                    edgeTo.put(neighbor, currVertex);
                    double new_priorityvalue =  distTo.get(neighbor) + input.estimatedDistanceToGoal(neighbor, end);
                    if (!visited.contains(neighbor)) {
                        if (fringe.contains(neighbor)) {
                            fringe.changePriority(neighbor, new_priorityvalue);
                        } else {
                            fringe.insert(neighbor, new_priorityvalue);
                        }
                    }
                }
            }
        }
        if (timeSpent > timeout) {
            outcome = SolverOutcome.TIMEOUT;
            solutionWeight = 0;
            numStates = visited.size();
            timeSpent = sw.elapsedTime();
        } else if (fringe.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            solutionWeight = 0;
            numStates = visited.size();
            timeSpent = sw.elapsedTime();
        } else {
            solution.add(end);
            Vertex prevVertex = end;
            while (!prevVertex.equals(start)) {
                prevVertex = edgeTo.get(prevVertex);
                solution.add(prevVertex);
            }
            Collections.reverse(solution);
            outcome = SolverOutcome.SOLVED;
            solutionWeight = distTo.get(end);
            numStates = visited.size() + 1;
            timeSpent = sw.elapsedTime();
        }
    }
    public SolverOutcome outcome() {
        return outcome;
    }
    public List<Vertex> solution() {
        return solution;
    }
    public double solutionWeight() {
        return solutionWeight;
    }
    public int numStatesExplored() {
        return numStates;
    }
    public double explorationTime() {
        return timeSpent;
    }
}
