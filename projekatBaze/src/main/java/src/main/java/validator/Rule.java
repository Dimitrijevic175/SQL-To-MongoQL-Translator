package src.main.java.validator;

import src.main.java.parser.SQLQuery;

public abstract class Rule {

    private String name;
    private String message;

    public Rule(String name) {
        this.name = name;
    }

    public abstract boolean check(SQLQuery query);

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public abstract String poruka();
}
