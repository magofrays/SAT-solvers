package by.magofrays;

import by.magofrays.cnf.evaluator.CNFEvaluator;
import by.magofrays.cnf.parser.CNFParser;
import by.magofrays.cnf.parser.SimpleCNFParser;
import by.magofrays.sat.solvers.DPLLSATSolver;
import by.magofrays.sat.solvers.SATResult;
import by.magofrays.sat.solvers.SATSolver;

import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        CNFParser parser = new SimpleCNFParser();
        InputStream inputStream = Main.class.getClassLoader()
                .getResourceAsStream("uf150-01.cnf");
        CNFEvaluator cnf = parser.parse(inputStream);

        System.out.println(cnf);
        SATSolver satSolver = new DPLLSATSolver();
        Instant start = Instant.now();
        SATResult result = satSolver.solve(cnf);
        Instant end = Instant.now();
        double seconds = Duration.between(start, end).toNanos() / 1_000_000_000.0;
        System.out.printf("Время: %.10f секунд%n", seconds);
        System.out.println(result.getAllAssignments());
        System.out.println(result);
    }
}