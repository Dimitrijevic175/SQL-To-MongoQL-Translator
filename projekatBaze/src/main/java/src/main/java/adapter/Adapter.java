package src.main.java.adapter;

import src.main.java.adapter.mapper.Mapper;
import src.main.java.adapter.mapper.MongoQuery;
import src.main.java.adapter.parametarConverter.ParametarConverter;
import src.main.java.adapter.parametarConverter.ParametrizedSQLQuery;
import src.main.java.parser.SQLQuery;


public class Adapter implements AdapterInterface{

    private ParametrizedSQLQuery parametrizedQuery;

    public MongoQuery translate(SQLQuery sqlQuery) {
        ParametarConverter parametarConverter = new ParametarConverter();
        Mapper mapper = new Mapper();

        // Izvrši konverziju parametara SQLQuery objekta
        parametrizedQuery = parametarConverter.convert(sqlQuery);

        // Izvrši mapiranje konvertovanih parametara u MongoQuery
        MongoQuery mongoQuery = mapper.map(parametrizedQuery);

        return mongoQuery;
    }

    public ParametrizedSQLQuery getParametrizedQuery() {
        return parametrizedQuery;
    }
}
