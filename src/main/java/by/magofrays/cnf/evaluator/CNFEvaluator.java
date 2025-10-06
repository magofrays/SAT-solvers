package by.magofrays.cnf.evaluator;

import java.util.List;

public interface CNFEvaluator {
    Boolean evaluate(List<Boolean> args);
    CNFEvaluator clone();
}
