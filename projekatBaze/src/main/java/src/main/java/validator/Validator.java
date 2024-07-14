package src.main.java.validator;

import src.main.java.parser.SQLQuery;

public interface Validator {

    boolean validateQuery(SQLQuery query);
}
