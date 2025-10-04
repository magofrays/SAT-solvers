package by.magofrays.cnf.evaluator;

public interface CNFEvaluator {
    Boolean evaluate(Boolean ...args);
    CNFEvaluator clone();
}
