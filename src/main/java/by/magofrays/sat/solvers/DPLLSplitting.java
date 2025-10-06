package by.magofrays.sat.solvers;

import by.magofrays.cnf.evaluator.CNFEvaluator;
import by.magofrays.cnf.evaluator.SimpleCNFEvaluator;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Builder
@AllArgsConstructor
public class DPLLSplitting implements SATResult {

    private final SimpleCNFEvaluator evaluator;
    private final List<Integer> positiveVariables;
    private final List<Integer> negativeVariables;
    private final List<Integer> freeVariables;
    private Boolean result;

    public DPLLSplitting(boolean result){
        evaluator = null;
        positiveVariables = null;
        negativeVariables = null;
        freeVariables = null;
        this.result = false;
    }

    public DPLLSplitting(SimpleCNFEvaluator evaluator){
        this.evaluator = evaluator;
        this.positiveVariables = new ArrayList<>();
        this.negativeVariables = new ArrayList<>();
        this.result = false;
        freeVariables = IntStream.rangeClosed(1, evaluator.getNumVariables())
                .boxed()
                .collect(Collectors.toList());
    }

    public void addPositive(Integer index){
        positiveVariables.add(index);
        removeLiteralOrClause(index);
        freeVariables.remove(index);
    }
    public void addNegative(Integer index){
        negativeVariables.add(index);
        removeLiteralOrClause(index);
        freeVariables.remove((Integer)(-index));
    }

    public void removeLiteralOrClause(Integer index){
        var clausesIterator = evaluator.getClauses().iterator();
        while(clausesIterator.hasNext()){
            var clauseIterator = clausesIterator.next().iterator();
            while(clauseIterator.hasNext()){
                Integer varIndex = clauseIterator.next();
                if(varIndex.equals(index)){
                    clausesIterator.remove();
                    break;
                }
                if(varIndex.equals(-index)){
                    clauseIterator.remove();
                }
            }
        }
    }

    @Override
    public DPLLSplitting clone() {
        return DPLLSplitting.builder()
                .evaluator(evaluator.clone())
                .positiveVariables(new ArrayList<>(positiveVariables))
                .negativeVariables(new ArrayList<>(negativeVariables))
                .freeVariables(new ArrayList<>(freeVariables))
                .result(result)
                .build();
    }


    @Override
    public List<List<Boolean>> getAllAssignments() {
        if (positiveVariables == null) {
            return null;
        }

        List<List<Boolean>> allAssignments = new ArrayList<>();
        int totalVariables = positiveVariables.size() + negativeVariables.size() + freeVariables.size();
        int n = freeVariables.size();
        int totalCombinations = (int) Math.pow(2, n);

        Map<Integer, Boolean> fixedValues = new HashMap<>();
        for (Integer var : positiveVariables) {
            fixedValues.put(var, true);
        }
        for (Integer var : negativeVariables) {
            fixedValues.put(var, false);
        }

        for (int i = 0; i < totalCombinations; i++) {
            List<Boolean> assignment = new ArrayList<>();
            for (int var = 1; var <= totalVariables; var++) {
                if (fixedValues.containsKey(var)) {
                    assignment.add(fixedValues.get(var));
                } else {
                    int freeVarIndex = freeVariables.indexOf(var);
                    boolean bitIsSet = ((i >> freeVarIndex) & 1) == 1;
                    assignment.add(bitIsSet);
                }
            }

            allAssignments.add(assignment);
        }

        return allAssignments;
    }
}
