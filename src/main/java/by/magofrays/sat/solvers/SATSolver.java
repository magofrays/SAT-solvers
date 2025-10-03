package by.magofrays.sat.solvers;

import by.magofrays.cnf.evaluator.CNFEvaluator;

public interface SATSolver <CNF extends CNFEvaluator, Result>{
    Result solve(CNF cnf);
}
