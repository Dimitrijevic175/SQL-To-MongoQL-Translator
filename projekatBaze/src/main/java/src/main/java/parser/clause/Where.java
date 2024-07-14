package src.main.java.parser.clause;

import src.main.java.parser.Clause;
import src.main.java.parser.clause.whereOperatori.ComparisonOperator;

import java.util.ArrayList;
import java.util.List;

public class Where extends Clause {

    private List<ComparisonOperator> operatori = new ArrayList<>();
    private List<String> logickiOperatori = new ArrayList<>();

    public Where(String keyword) {
        super(keyword);
    }

    public List<ComparisonOperator> getOperatori() {
        return operatori;
    }

    public void setOperatori(List<ComparisonOperator> operatori) {
        this.operatori = operatori;
    }

    public List<String> getLogickiOperatori() {
        return logickiOperatori;
    }

    public void setLogickiOperatori(List<String> logickiOperatori) {
        this.logickiOperatori = logickiOperatori;
    }
}
