package src.main.java.validator.validacije;

import src.main.java.gui.LiveSQLApp;
import src.main.java.parser.Clause;
import src.main.java.parser.SQLQuery;
import src.main.java.parser.clause.GroupBy;
import src.main.java.parser.clause.Where;
import src.main.java.validator.Rule;

import java.util.ArrayList;
import java.util.List;

public class WhereRule extends Rule {

    private List<String> izbaci = new ArrayList<>();

    public WhereRule(String name) {
        super(name);
    }

    @Override
    public boolean check(SQLQuery query) {

        boolean flag = false;
        for(Clause clause : query.getClauses()){
            if(clause instanceof Where){
                flag = true;
            }
        }
        if(!flag){
            return true;
        }

        for(Clause clause : query.getClauses()){
            if(clause instanceof Where){
                for(String parametar : clause.getParameters()){
                    if(parametar.contains("sum") || parametar.contains("avg") || parametar.contains("min") || parametar.contains("max") || parametar.contains("count"))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String poruka() {
        super.setMessage("U WHERE iskazu nije funkcija agregacije.");
        return super.getMessage();
    }
}
