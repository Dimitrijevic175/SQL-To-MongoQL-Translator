package src.main.java.parser;

import lombok.Getter;
import lombok.Setter;
import src.main.java.parser.clause.*;

public class SQLQueryParser implements Parser{

    @Getter
    @Setter

    private SQLQuery sqlQuery;
    private Clause currentClause = null;
    private boolean flagOrder = false;
    private boolean flagGroup = false;

    public SQLQuery parseQuery(String queryString) {
        sqlQuery = new SQLQuery();

        String[] parts = queryString.split("\\s+");


        for (String part : parts) {

            if (isKeyword(part)) {
                if(part.equalsIgnoreCase("order")){
                    flagOrder = true;
                }
                if(part.equalsIgnoreCase("group")){
                    flagGroup = true;
                }
            }
            else if(flagOrder){
                flagOrder = false;
            }
            else if(flagGroup){
                flagGroup = false;
            }
            else {
                String noviPart = "";
                for(int i = 0; i < part.length(); i++){
                    if(part.charAt(i) != ','){
                        noviPart += part.charAt(i);
                    }
                }
                if(currentClause != null)
                    currentClause.addParameter(noviPart);
            }
        }
        return sqlQuery;
    }

    private boolean isKeyword(String part) {

        if(part.equalsIgnoreCase("SELECT")){
            currentClause = new Select(part);
            sqlQuery.addClause(currentClause);
            return true;
        }
        else if(part.equalsIgnoreCase("FROM")){
            currentClause = new From(part);
            sqlQuery.addClause(currentClause);
            return true;
        }
        else if(part.equalsIgnoreCase("Where")){
            currentClause = new Where(part);
            sqlQuery.addClause(currentClause);
            return true;
        }
        else if(part.equalsIgnoreCase("order")){
            currentClause = new OrderBy("order by");
            sqlQuery.addClause(currentClause);
            return true;
        }
        else if(part.equalsIgnoreCase("Group")){
            currentClause = new GroupBy("group by");
            sqlQuery.addClause(currentClause);
            return true;
        }
        else if(part.equalsIgnoreCase("Join")){
            currentClause = new Join(part);
            sqlQuery.addClause(currentClause);
            return true;
        }
        return false;
    }




}
