package by.magofrays.cnf.evaluator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class SimpleCNFEvaluator implements CNFEvaluator{

    private Integer numVariables;
    private Integer numClauses;
    private List<List<Integer>> clauses;

    @Override
    public Boolean evaluate(List<Boolean> args) {
        for(var clause : clauses){
            Boolean clauseRes = false;
            for(Integer variableIndex : clause){
                if(variableIndex > 0)
                    clauseRes = (args.get(variableIndex-1) || clauseRes);
                else
                    clauseRes = (!args.get(-(variableIndex+1)) || clauseRes);
                if(clauseRes)
                    break;
            }
            if(!clauseRes)
                return false;
        }
        return true;
    }

    @Override
    public SimpleCNFEvaluator clone() {
        return SimpleCNFEvaluator.builder()
                .numVariables(numVariables)
                .numClauses(numClauses)
                .clauses(clauses.stream()
                        .map(ArrayList::new)
                        .collect(Collectors.toList()
                        ))
                .build();
    }
    public String toString(){
        return "{%d, ".formatted(clauses.size()) + clauses.toString() + "}";
    }
}
