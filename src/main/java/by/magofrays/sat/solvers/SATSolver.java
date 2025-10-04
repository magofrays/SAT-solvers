package by.magofrays.sat.solvers;

import by.magofrays.cnf.evaluator.CNFEvaluator;

public interface SATSolver <T extends CNFEvaluator, R extends SATResult>{
    R solve(T cnfEvaluator);
}
