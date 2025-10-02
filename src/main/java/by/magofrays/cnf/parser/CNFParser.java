package by.magofrays.cnf.parser;

import by.magofrays.cnf.evaluator.CNFEvaluator;

import java.io.InputStream;

public interface CNFParser<CNF extends CNFEvaluator> {
    public CNF parse(InputStream inputStream);
}
