package by.magofrays;


import by.magofrays.cnf.evaluator.SimpleCNFEvaluator;
import by.magofrays.cnf.parser.CNFParser;
import static org.junit.jupiter.api.Assertions.*;
import by.magofrays.cnf.parser.SimpleCNFParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ParserTest {
    @Test
    public void parseAndCheckSimpleFormulaEvaluation() {
        CNFParser<SimpleCNFEvaluator> parser = new SimpleCNFParser();

        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("test.cnf");

        if (inputStream == null) {
            throw new RuntimeException("Test file not found in resources");
        }

        SimpleCNFEvaluator evaluator = parser.parse(inputStream);

        Boolean result1 = evaluator.evaluate(true, false, true);
        Boolean result2 = evaluator.evaluate(false, false, false);
        assertTrue(result1);
        assertFalse(result2);
    }

    @Test
    public void parseImportCNFFile(){
        CNFParser<SimpleCNFEvaluator> parser = new SimpleCNFParser();

        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("uuf50-03.cnf");
        SimpleCNFEvaluator evaluator = parser.parse(inputStream);
        if (inputStream == null) {
            throw new RuntimeException("Test file not found in resources");
        }
        assertEquals(50, (int) evaluator.getNumVariables());
        assertEquals(218, evaluator.getClauses().size());
    }

}
