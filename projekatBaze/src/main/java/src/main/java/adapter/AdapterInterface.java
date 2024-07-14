package src.main.java.adapter;

import src.main.java.adapter.mapper.MongoQuery;
import src.main.java.parser.SQLQuery;

public interface AdapterInterface {

    MongoQuery translate(SQLQuery sqlQuery);
}
