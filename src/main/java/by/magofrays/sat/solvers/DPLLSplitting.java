package by.magofrays.sat.solvers;

import by.magofrays.cnf.evaluator.CNFEvaluator;
import by.magofrays.cnf.evaluator.SimpleCNFEvaluator;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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
        removeIndexOrLiteral(index);
        freeVariables.remove(index);
    }
    public void addNegative(Integer index){
        negativeVariables.add(index);
        removeIndexOrLiteral(index);
        freeVariables.remove((Integer)(-index));
    }

    public void removeIndexOrLiteral(Integer index){
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
                .positiveVariables(new ArrayList<>(positiveVariables)) // Копируем существующие
                .negativeVariables(new ArrayList<>(negativeVariables)) // Копируем существующие
                .freeVariables(new ArrayList<>(freeVariables))         // Копируем существующие
                .result(result)                                        // Копируем результат
                .build();
    }

    @Override
    public List<List<Integer>> getAllAssignments() {
        if(positiveVariables == null){
            return null;
        }
        List<List<Integer>> allAssignments = new ArrayList<>();
        int totalVariables = positiveVariables.size() + negativeVariables.size() + freeVariables.size();
        int n = freeVariables.size();
        int totalCombinations = (int) Math.pow(2, n);

        for (int i = 0; i < totalCombinations; i++) {
            List<Integer> assignment = new ArrayList<>();

            // Добавляем зафиксированные положительные переменные как 1
            for (Integer fixedPos : positiveVariables) {
                assignment.add(1);
            }

            // Добавляем зафиксированные отрицательные переменные как 0
            for (Integer fixedNeg : negativeVariables) {
                assignment.add(0);
            }

            // Добавляем комбинации для свободных переменных
            for (int j = 0; j < n; j++) {
                boolean bitIsSet = ((i >> j) & 1) == 1;
                assignment.add(bitIsSet ? 1 : 0);
            }

            allAssignments.add(assignment);
        }

        return allAssignments;
    }
}
