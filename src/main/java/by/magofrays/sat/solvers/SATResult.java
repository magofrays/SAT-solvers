package by.magofrays.sat.solvers;

import java.util.List;

public interface SATResult {
    SATResult clone();
    List<List<Integer>> getAllAssignments();
}
