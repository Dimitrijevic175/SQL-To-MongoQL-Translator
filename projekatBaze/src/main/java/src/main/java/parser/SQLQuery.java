package src.main.java.parser;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter

public class SQLQuery {

    private List<Clause> clauses;

    public SQLQuery() {
        clauses = new ArrayList<>();
    }

    public void addClause(Clause clause) {
        clauses.add(clause);
    }

    public List<Clause> getClauses() {
        return clauses;
    }
}
