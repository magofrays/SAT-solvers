package by.magofrays.sat.solvers;

import by.magofrays.cnf.evaluator.SimpleCNFEvaluator;
import lombok.Getter;

import java.util.*;
import java.util.stream.IntStream;


@Getter
public class DPLLSATSolver implements SATSolver<SimpleCNFEvaluator, DPLLSplitting>{

    @Override
    public DPLLSplitting solve(SimpleCNFEvaluator cnfEvaluator) {
        Stack<DPLLSplitting> splittings = new Stack<>();
        splittings.add(new DPLLSplitting(cnfEvaluator));
        while (!splittings.isEmpty()){
            DPLLSplitting splitting = splittings.pop();
            boolean simplified;
            boolean emptyClause = false;
            do {
                simplified = false;

                if (splitting.getEvaluator().getClauses().isEmpty()) {
                    splitting.setResult(true);
                    return splitting;
                }
                if (hasEmptyClause(splitting)) {
                    emptyClause = true;
                    break;
                }

                boolean singularDisjunction = unitPropagation(splitting);
                boolean pureLiterals =  pureLiteralElimination(splitting);
                simplified = singularDisjunction || pureLiterals;
            } while (simplified);

            if (emptyClause) {
                continue;
            }


            Integer index = findMostFrequentVariable(splitting);
            var left = splitting.clone();
            var right = splitting.clone();
            left.addNegative(-index);
            right.addPositive(index);
            splittings.add(left);
            splittings.add(right);
        }
        return new DPLLSplitting(false);
    }

private boolean hasEmptyClause(DPLLSplitting splitting) {
    for (List<Integer> clause : splitting.getEvaluator().getClauses()) {
        if (clause.isEmpty()) {
            return true;
        }
    }
    return false;
}

    public Integer findMostFrequentVariable(DPLLSplitting splitting){
        int[] variablesCount = IntStream.generate(() -> 0)
                .limit(splitting.getEvaluator().getNumVariables())
                .toArray();
        for (List<Integer> integers : splitting.getEvaluator().getClauses()) {
            for (Integer integer : integers) {
                variablesCount[Math.abs(integer) - 1]++;
            }
        }
        int maxIndex = 0;
        for (int i = 1; i < variablesCount.length; i++) {
            if (variablesCount[i] > variablesCount[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex+1;
    }

    public boolean pureLiteralElimination(DPLLSplitting splitting) {
        Map<Integer, Boolean> variableSigns = new HashMap<>();
        Set<Integer> impureVariables = new HashSet<>();
        boolean pureLiteralExists = false;

        for (var clause : splitting.getEvaluator().getClauses()) {
            for (Integer variableIndex : clause) {
                int variable = Math.abs(variableIndex);
                boolean currentSign = (variableIndex > 0);

                if (impureVariables.contains(variable)) continue;

                if (!variableSigns.containsKey(variable)) {
                    variableSigns.put(variable, currentSign);
                }
                else if (variableSigns.get(variable) != currentSign) {
                    impureVariables.add(variable);
                    variableSigns.remove(variable);
                }
            }
        }

        for (Map.Entry<Integer, Boolean> entry : variableSigns.entrySet()) {
            Integer freeVar = entry.getKey();
            Boolean expectedSign = entry.getValue();
            pureLiteralExists = true;
            if (expectedSign) {
                splitting.addPositive(freeVar);
            } else {
                splitting.addNegative(-freeVar);
            }
        }
        return pureLiteralExists;
    }

    public boolean unitPropagation(DPLLSplitting splitting){
        var clauses = splitting.getEvaluator().getClauses();
        boolean singularDisjunctionExists = false;


        List<Integer> unitClauses = new ArrayList<>();
        for (var clause : clauses) {
            if (clause.size() == 1) {
                unitClauses.add(clause.get(0));
            }
        }


        for (Integer literal : unitClauses) {
            singularDisjunctionExists = true;
            if (literal > 0) {
                splitting.addPositive(literal);
            } else {
                splitting.addNegative(literal);
            }
        }

        return singularDisjunctionExists;
    }


}
