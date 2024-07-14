package src.main.java.validator.validacije;

import src.main.java.gui.LiveSQLApp;
import src.main.java.parser.Clause;
import src.main.java.parser.SQLQuery;
import src.main.java.parser.clause.GroupBy;
import src.main.java.parser.clause.Select;
import src.main.java.parser.clause.Where;
import src.main.java.validator.Rule;

import java.util.ArrayList;
import java.util.List;

public class GroupByRule extends Rule {

    private List<String> pomocnaLista = new ArrayList<>();
    private List<String> nedostaju = new ArrayList<>();

    public GroupByRule(String name) {
        super(name);
    }

    @Override
    public boolean check(SQLQuery query) {

        boolean flag = false;
        for(Clause clause : query.getClauses()){
            if(clause instanceof GroupBy){
                flag = true;
            }
        }
        if(!flag){
            return true;
        }

        for(Clause clause : query.getClauses()){
            if(clause instanceof Select){

                for(String parametar : clause.getParameters()){
                    if(!parametar.contains("sum") && !parametar.contains("avg") &&
                            !parametar.contains("min") && !parametar.contains("max") && !parametar.contains("count"))
                    {
                        pomocnaLista.add(parametar);
                    }
                }
            }
        }

        boolean flag1 = false;

        for(Clause clause : query.getClauses()){
            if(clause instanceof GroupBy){
                {
                    for(String s : pomocnaLista){

                        flag1 = false;
                        for(String ss : clause.getParameters()){

                            if(s.equals(ss)){
                                flag1 = true;
                                break;
                            }
                        }
                        if(!flag1) {
                            nedostaju.add(s);
                        }
                    }

                }
            }
        }
        if(nedostaju.isEmpty()){
            return true;
        }
        else
            return false;
    }

    @Override
    public String poruka() {
        super.setMessage("Sve što je selektovano a nije pod funkcijom agregacije, mora ući u GROUP BY. Nedostaju " + nedostaju);
        return super.getMessage();
    }
}
