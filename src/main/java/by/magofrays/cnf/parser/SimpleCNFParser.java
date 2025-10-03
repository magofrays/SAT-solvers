package by.magofrays.cnf.parser;

import by.magofrays.cnf.evaluator.SimpleCNFEvaluator;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SimpleCNFParser implements CNFParser<SimpleCNFEvaluator>{
    @SneakyThrows
    public SimpleCNFEvaluator parse(InputStream inputStream) {
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Integer numVariables = null;
        Integer numClauses = null;
        List<List<Integer>> clauses = new ArrayList<>();
        List<Integer> currentClause = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("%"))
                break;
            if (line.startsWith("c") || line.isEmpty()) continue;

            if (line.startsWith("p")) {
                // Problem line: p cnf 3 2
                String[] parts = line.split("\\s+");
                if (parts.length < 4) {
                    throw new RuntimeException("Invalid problem line: " + line);
                }
                numVariables = Integer.parseInt(parts[2]);
                numClauses = Integer.parseInt(parts[3]);
                continue;
            }

            // Parse clause literals
            String[] tokens = line.split("\\s+");
            for (String token : tokens) {
                int literal = Integer.parseInt(token);
                if (literal == 0) {
                    // End of current clause
                    if (!currentClause.isEmpty()) {
                        clauses.add(currentClause);
                        currentClause = new ArrayList<>();
                    }
                } else {
                    currentClause.add(literal);
                }
            }
        }

        // Handle last clause without terminating 0
        if (!currentClause.isEmpty()) {
            clauses.add(currentClause);
        }

        // Validate
        if (numVariables == null || numClauses == null) {
            throw new RuntimeException("Missing problem line");
        }

        if (clauses.size() != numClauses) {
            System.err.printf("Warning: expected %d clauses, found %d%n", numClauses, clauses.size());
        }

        return SimpleCNFEvaluator.builder()
                .numVariables(numVariables)
                .numClauses(numClauses)
                .clauses(clauses)
                .build();
    }
}
