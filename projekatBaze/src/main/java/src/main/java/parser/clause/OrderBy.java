package src.main.java.parser.clause;

import lombok.Getter;
import lombok.Setter;
import src.main.java.parser.Clause;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderBy extends Clause {

    private List<String> parametri = new ArrayList<>();
    private int sort;

    public OrderBy(String keyword) {
        super(keyword);
    }
    public void popuniListu(){

        for(String rec : super.getParameters()){
            if(!rec.contains("asc") && !rec.contains("desc")){
                parametri.add(rec);
            }
            else{
                if(rec.contains("asc"))
                    sort = 1;
                else
                    sort = -1;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String rec : parametri){
            sb.append(rec);
        }

        return sb.toString();
    }
}

