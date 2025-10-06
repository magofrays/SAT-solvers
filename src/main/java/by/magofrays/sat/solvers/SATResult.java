package by.magofrays.sat.solvers;

import java.util.List;

public interface SATResult {
    SATResult clone();
    List<List<Boolean>> getAllAssignments();
}
