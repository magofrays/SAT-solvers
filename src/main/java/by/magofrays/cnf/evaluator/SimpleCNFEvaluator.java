package by.magofrays.cnf.evaluator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SimpleCNFEvaluator implements CNFEvaluator{

    private Integer numVariables;
    private Integer numClauses;
    private List<List<Integer>> clauses;

    @Override
    public Boolean evaluate(Boolean... args) {
        for(var clause : clauses){
            Boolean clauseRes = false;
            for(Integer variableIndex : clause){
                if(variableIndex > 0)
                    clauseRes = (args[variableIndex-1] || clauseRes);
                else
                    clauseRes = (!args[-(variableIndex+1)] || clauseRes);
                if(clauseRes)
                    break;
            }
            if(!clauseRes)
                return false;
        }
        return true;
    }
}
