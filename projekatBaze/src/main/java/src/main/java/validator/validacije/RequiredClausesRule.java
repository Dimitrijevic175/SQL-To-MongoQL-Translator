package src.main.java.validator.validacije;

import src.main.java.parser.Clause;
import src.main.java.parser.SQLQuery;
import src.main.java.validator.Rule;

public class RequiredClausesRule extends Rule {


    boolean flagSelect = false;
    boolean flagFrom = false;

    public RequiredClausesRule(String name) {
        super(name);
    }

    @Override
    public boolean check(SQLQuery query) {



        for(Clause clause : query.getClauses()){
            if(clause.getKeyword().equalsIgnoreCase("SELECT")){
                flagSelect = true;
            }
            if(clause.getKeyword().equalsIgnoreCase("FROM")){
                flagFrom = true;
            }
        }
        if(flagSelect && flagFrom){
            return true;
        }
        else
            return false;
    }


    @Override
    public String poruka() {
        if(!flagSelect && !flagFrom){
            super.setMessage("Upit mora imati sve obavezne delove,nedostaju SELECT I FROM");
        }
        else if(!flagSelect){
            super.setMessage("Upit mora imati sve obavezne delove,nedostaje SELECT deo");
        }
        else if(!flagFrom){
            super.setMessage("Upit mora imati sve obavezne delove,nedostaje FROM deo");
        }
        return super.getMessage();
    }
}
