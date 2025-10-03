package by.magofrays.sat.solvers;

import by.magofrays.cnf.evaluator.CNFEvaluator;
import by.magofrays.cnf.evaluator.SimpleCNFEvaluator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class DPLLSplitting {

    private final SimpleCNFEvaluator evaluator;
    private final List<Integer> positive = new ArrayList<>();
    private final List<Integer> negative = new ArrayList<>();
    private final List<Integer> freeVariables;
    private Boolean result = false;

    public DPLLSplitting(SimpleCNFEvaluator evaluator){
        this.evaluator = evaluator;
        freeVariables = IntStream.rangeClosed(1, evaluator.getNumVariables())
                .boxed()
                .collect(Collectors.toList());
    }

    public void addPositive(Integer index){
        positive.add(index);
        removeIndex(index);
        freeVariables.remove(index);
    }
    public void addNegative(Integer index){
        negative.add(index);
        removeIndex(index);
        freeVariables.remove(index);
    }
    public void removeIndex(Integer index){
        var iterator = evaluator.getClauses().iterator();
        while(iterator.hasNext()){
            var clause = iterator.next();
            clause.removeIf(varIndex -> Math.abs(varIndex) == Math.abs(index));
            if(clause.isEmpty()){
                iterator.remove();
            }
        }
    }
}
