package src.main.java.parser;

import java.util.ArrayList;
import java.util.List;

public abstract class Clause {

    private String keyword;
    private List<String> parameters;

    public Clause(String keyword) {
        this.keyword = keyword;
        parameters = new ArrayList<>();
    }

    public String getKeyword() {
        return keyword;
    }

    public void addParameter(String parameter) {
        parameters.add(parameter);
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return keyword;
    }
}
