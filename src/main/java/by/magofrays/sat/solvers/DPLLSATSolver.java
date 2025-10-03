package by.magofrays.sat.solvers;

import by.magofrays.cnf.evaluator.SimpleCNFEvaluator;
import lombok.Getter;



@Getter
public class DPLLSATSolver implements SATSolver<SimpleCNFEvaluator, DPLLSplitting>{

    @Override
    public DPLLSplitting solve(SimpleCNFEvaluator splitting) {
        return null;
    }

    public boolean removePureLiterals(DPLLSplitting splitting) {
        var iterator = splitting.getFreeVariables().iterator();
        boolean pureLiteralExists = false;

        while (iterator.hasNext()) {
            Integer freeVar = iterator.next();
            var clausesIterator = splitting.getEvaluator().getClauses().iterator();
            Boolean expectedSign = null;
            boolean isPure = true;
            while (clausesIterator.hasNext() && isPure) {
                var clause = clausesIterator.next();
                var clauseIterator = clause.iterator();

                while (clauseIterator.hasNext() && isPure) {
                    Integer variableIndex = clauseIterator.next();

                    if (Math.abs(variableIndex) == Math.abs(freeVar)) {
                        boolean currentSign = (variableIndex > 0);

                        if (expectedSign == null) {
                            expectedSign = currentSign;
                        } else if (expectedSign != currentSign) {
                            isPure = false;
                        }
                    }
                }
            }
            if (isPure && expectedSign != null) {
                pureLiteralExists = true;
                if (expectedSign) {
                    splitting.addPositive(freeVar);
                } else {
                    splitting.addNegative(freeVar);
                }
                return pureLiteralExists;
            }
        }
        return pureLiteralExists;
    }

    public boolean singularDisjunction(DPLLSplitting splitting){
        var clauses = splitting.getEvaluator().getClauses();
        var iterator = clauses.iterator();
        boolean singularDisjunctionExists = false;
        while (iterator.hasNext()){
            var clause = iterator.next();
            if(clause.size() == 1){
                Integer index = clause.get(0);
                if(index > 0){
                    splitting.addPositive(index);
                }
                else{
                    splitting.addNegative(index);
                }
                singularDisjunctionExists = true;
            }
        }
        return singularDisjunctionExists;
    }



}
