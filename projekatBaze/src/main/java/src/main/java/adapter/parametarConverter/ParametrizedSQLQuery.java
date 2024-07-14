package src.main.java.adapter.parametarConverter;

import src.main.java.parser.SQLQuery;

import java.util.Map;

public class ParametrizedSQLQuery {

    private SQLQuery sqlQuery;
    private Map<String, Integer> parameterMap;


    ///ova klasa cuva popunjenu hes mapu sa parametrima koju smo prethodno popunili u klasi ParametarConverter

    public ParametrizedSQLQuery(SQLQuery sqlQuery, Map<String, Integer> parameterMap) {
        this.sqlQuery = sqlQuery;
        this.parameterMap = parameterMap;
    }

    public SQLQuery getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(SQLQuery sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public Map<String, Integer> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Integer> parameterMap) {
        this.parameterMap = parameterMap;
    }

}
