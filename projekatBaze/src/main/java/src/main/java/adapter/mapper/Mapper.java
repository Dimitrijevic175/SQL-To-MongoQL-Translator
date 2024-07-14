package src.main.java.adapter.mapper;

import src.main.java.adapter.mapper.MongoQuery;
import src.main.java.adapter.parametarConverter.ParametrizedSQLQuery;
import src.main.java.parser.Clause;
import src.main.java.parser.clause.Where;
import src.main.java.parser.clause.whereOperatori.ComparisonOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapper {

    public MongoQuery map(ParametrizedSQLQuery parametrizedQuery) {
        List<Clause> kljucneReci = parametrizedQuery.getSqlQuery().getClauses();

        MongoQuery mongoQuery = new MongoQuery();


        for (Clause kljucnaRec : kljucneReci) {
            String keyword = kljucnaRec.getKeyword();
            List<String> parameters = kljucnaRec.getParameters();

            if (keyword.equalsIgnoreCase("SELECT")) {

                List<String> selectFields = new ArrayList<>();
                for (String parameter : parameters) {
                    selectFields.add(parameter);
                }
                mongoQuery.setSelectFields(selectFields);

            } else if (keyword.equalsIgnoreCase("FROM")) {

                String collectionName = parameters.get(0);
                String imeTabele = "";
                for(int i = 3; i < collectionName.length(); i++){
                    imeTabele += collectionName.charAt(i);
                }
                mongoQuery.setCollectionName(imeTabele);

            } else if (keyword.equalsIgnoreCase("WHERE")) {

                Map<Object, Object> whereConditions = new HashMap<>();

                if (parameters.size() >= 3) {
                    String field = parameters.get(0);
                    ComparisonOperator operator = mapComparisonOperator(parameters.get(1));
                    if(kljucnaRec instanceof Where){
                        ((Where) kljucnaRec).getOperatori().add(operator);
                    }
                    Object value = parseValue(parameters.get(2));

                    whereConditions.put(field, value);
                }
                if(parameters.size() == 7){
                    String logickiOperator = parameters.get(3);
                    String field = parameters.get(4);
                    ComparisonOperator operator = mapComparisonOperator(parameters.get(5));
                    if(kljucnaRec instanceof Where){
                        ((Where) kljucnaRec).getLogickiOperatori().add(logickiOperator);
                        ((Where) kljucnaRec).getOperatori().add(operator);
                    }
                    Object value = parseValue(parameters.get(6));

                    whereConditions.put(field, value);
                }

                mongoQuery.setWhereConditions(whereConditions);

            } else if (keyword.equalsIgnoreCase("GROUP BY")) {
                List<String> groupByFields = new ArrayList<>();
                for (String parameter : parameters) {
                    groupByFields.add(parameter);
                }
                mongoQuery.setGroupByFields(groupByFields);
            }
        }

        return mongoQuery;
    }

    private ComparisonOperator mapComparisonOperator(String operatorString) {
        switch (operatorString) {
            case "=":
                return ComparisonOperator.EQUAL;
            case "!=":
                return ComparisonOperator.NOT_EQUAL;
            case ">":
                return ComparisonOperator.GREATER_THAN;
            case "<":
                return ComparisonOperator.LESS_THAN;
            case ">=":
                return ComparisonOperator.GREATER_THAN_OR_EQUAL;
            case "<=":
                return ComparisonOperator.LESS_THAN_OR_EQUAL;
            case "like":
                return ComparisonOperator.LIKE;
            default:
                throw new IllegalArgumentException("Nepodržan operator poređenja: " + operatorString);
        }
    }

    private Object parseValue(String valueString) {


        if (valueString.startsWith("'") && valueString.endsWith("'")) {
            String novi = valueString.substring(1, valueString.length() - 1);
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            sb.append(novi);
            sb.append("\"");
            return sb.toString();

        }
        return valueString;
    }
}
