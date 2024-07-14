package src.main.java.adapter.parametarConverter;

import src.main.java.parser.Clause;
import src.main.java.parser.SQLQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParametarConverter {


    public ParametrizedSQLQuery convert(SQLQuery sqlQuery) {

        Map<String, Integer> parameterMap = new HashMap<>();
        extractParameters(sqlQuery, parameterMap);

        ParametrizedSQLQuery parametrizedQuery = new ParametrizedSQLQuery(sqlQuery, parameterMap);
        return parametrizedQuery;
    }

    private void extractParameters(SQLQuery sqlQuery, Map<String, Integer> parameterMap) {
        List<Clause> clauses = sqlQuery.getClauses();
        for (Clause clause : clauses) {
            List<String> parameters = clause.getParameters();
            for (String parameter : parameters) {
                parameterMap.put(parameter, 1);
            }
        }
    }
}
