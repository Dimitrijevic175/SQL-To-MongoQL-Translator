package src.main.java.validator.validacije;

import src.main.java.gui.LiveSQLApp;
import src.main.java.parser.Clause;
import src.main.java.parser.SQLQuery;
import src.main.java.parser.clause.GroupBy;
import src.main.java.parser.clause.Join;
import src.main.java.parser.clause.Where;
import src.main.java.validator.Rule;

public class JoinRule extends Rule {

    boolean flagUsing = false;
    boolean flagOn = false;

    public JoinRule(String name) {
        super(name);
    }

    @Override
    public boolean check(SQLQuery query) {

        Clause join = null;
        boolean flag = false;
        for(Clause clause : query.getClauses()){
            if(clause instanceof Join){
                join = clause;
                flag = true;
            }
        }
        if(!flag){
            return true;
        }

        for(String par : join.getParameters()){
            if(par.equalsIgnoreCase("USING")){
                flagUsing = true;
            }
            if(par.equalsIgnoreCase("ON")){
                flagOn = true;
            }
        }

        if(flagUsing || flagOn){
            return true;
        }
        else
            return false;

    }

    @Override
    public String poruka() {
        if(!flagUsing && !flagOn){
            super.setMessage("Spajanje tabela ima uslov za spajanje (USING ili ON).");
        }
        return super.getMessage();
    }
}
