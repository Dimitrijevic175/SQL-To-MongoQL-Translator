package src.main.java.validator;

import src.main.java.parser.SQLQuery;
import src.main.java.validator.validacije.GroupByRule;
import src.main.java.validator.validacije.JoinRule;
import src.main.java.validator.validacije.RequiredClausesRule;
import src.main.java.validator.validacije.WhereRule;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class QueryValidator implements Validator{

    private List<Rule> rules;

    public QueryValidator() {
        rules = new ArrayList<>();
        rules.add(new RequiredClausesRule("Required Clauses"));
        rules.add(new GroupByRule("GROUP BY"));
        rules.add(new WhereRule("Where rule"));
        rules.add(new JoinRule("Join rule"));

    }

    public boolean validateQuery(SQLQuery query) {
        for (Rule rule : rules) {
            if (!rule.check(query)) {
                JOptionPane.showMessageDialog(null, rule.poruka());
                return false;
            }
        }
        return true;
    }
}
