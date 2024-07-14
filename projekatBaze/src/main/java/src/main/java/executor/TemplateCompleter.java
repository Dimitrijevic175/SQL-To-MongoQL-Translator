package src.main.java.executor;

import com.mongodb.client.MongoCursor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import src.main.java.adapter.mapper.MongoQuery;
import src.main.java.adapter.parametarConverter.ParametrizedSQLQuery;
import src.main.java.data.Row;
import src.main.java.parser.Clause;
import src.main.java.parser.SQLQuery;
import src.main.java.parser.clause.OrderBy;
import src.main.java.parser.clause.Where;
import src.main.java.parser.clause.whereOperatori.ComparisonOperator;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Getter
@Setter

public class TemplateCompleter {

    private MongoQuery mongoQuery;
    private SQLQuery sqlQuery;
    private ParametrizedSQLQuery parametrizedSQLQuery;
    private String projection;
    private List<String> kljucneReci = new ArrayList<>();

    private MongoClient connection;
    private DefaultTableModel defaultTableModel;

    List<Row> rows = new ArrayList<>();

    private MongoCursor<Document> cursor;

    public TemplateCompleter(MongoQuery mongoQuery, SQLQuery sqlQuery, ParametrizedSQLQuery parametrizedSQLQuery,MongoClient connection){

        this.mongoQuery = mongoQuery;
        this.sqlQuery = sqlQuery;
        this.parametrizedSQLQuery = parametrizedSQLQuery;
        this.connection = connection;
    }


    public MongoCursor<Document> templateMetoda(){

        for(Clause clause : sqlQuery.getClauses()){
            kljucneReci.add(clause.getKeyword());
        }
        StringBuilder stringBuilder = new StringBuilder();


        if(kljucneReci.contains("select") && kljucneReci.contains("from") &&
                kljucneReci.size() == 2 && mongoQuery.getSelectFields().size() == 1 && mongoQuery.getSelectFields().contains("*")){
                projection = String.format(MongoQueryTemplates.SELECT_TEMPLATE_OSNOVNI,mongoQuery.getCollectionName());
            ///"db.%s.find({}).projection({%s})";
            MongoDatabase database = connection.getDatabase("bp_tim55");
            String tabela = mongoQuery.getCollectionName().toString();
            cursor = database.getCollection(tabela).find(Document.parse("{}")).projection(Document.parse("{}")).iterator();
            while (cursor.hasNext()){
                Document d = cursor.next();
                System.out.println(d.toJson());
            }


        }

        else if(kljucneReci.contains("select") && kljucneReci.contains("from") &&
        kljucneReci.size() == 2){
            stringBuilder.append("{");
            for (Map.Entry<String, Integer> entry : parametrizedSQLQuery.getParameterMap().entrySet()) {
                for (String parametar : mongoQuery.getSelectFields()) {
                    if (entry.getKey().equalsIgnoreCase(parametar)) {
                        stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                    }
                }
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append("}");
            projection = String.format(MongoQueryTemplates.SELECT_TEMPLATE, mongoQuery.getCollectionName(), stringBuilder.toString());
            MongoDatabase database = connection.getDatabase("bp_tim55");
            String tabela = mongoQuery.getCollectionName().toString();
            cursor = database.getCollection(tabela).find(Document.parse("{}")).projection(Document.parse(stringBuilder.toString())).iterator();

        }


        ///order by sa i bez proj


        else if((kljucneReci.contains("select") && kljucneReci.contains("from") && kljucneReci.contains("order by") &&
                kljucneReci.size() == 3)){
                if(!mongoQuery.getSelectFields().contains("*")){
                    int broj = 0;
                    String lista = "";
                    for(Clause clause : sqlQuery.getClauses()){
                        if(clause instanceof OrderBy){
                            OrderBy ob = (OrderBy)clause;
                            ob.popuniListu();
                            lista = ob.toString();
                            broj = ob.getSort();
                        }
                    }
                    String pomocni = "{" + lista + ":" + broj + "}";
                    StringBuilder sb = new StringBuilder();
                    sb.append("{");
                    for (Map.Entry<String, Integer> entry : parametrizedSQLQuery.getParameterMap().entrySet()) {
                        for (String parametar : mongoQuery.getSelectFields()) {
                            if (entry.getKey().equalsIgnoreCase(parametar)) {
                                sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                            }
                        }
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    sb.append("}");
                    projection = String.format(MongoQueryTemplates.SORT_TEMPLATE,mongoQuery.getCollectionName(),sb.toString(),lista,broj);
                    MongoDatabase database = connection.getDatabase("bp_tim55");
                    String tabela = mongoQuery.getCollectionName().toString();
                    cursor = database.getCollection(tabela).find(Document.parse("{}")).projection(Document.parse(sb.toString())).sort(Document.parse(pomocni)).iterator();



            }
                else{

                    int broj = 0;
                    String lista = "";
                    for(Clause clause : sqlQuery.getClauses()){
                        if(clause instanceof OrderBy){
                            OrderBy ob = (OrderBy)clause;
                            ob.popuniListu();
                            lista = ob.toString();
                            broj = ob.getSort();
                        }
                    }
                    String pomocni = "{" + lista + ":" + broj + "}";
                    //"db.%s.find({}).sort({%s:%s})";
                    projection = String.format(MongoQueryTemplates.SORT_TEMP_BEZPROJ,mongoQuery.getCollectionName(),lista,broj);
                    MongoDatabase database = connection.getDatabase("bp_tim55");
                    String tabela = mongoQuery.getCollectionName().toString();
                    cursor = database.getCollection(tabela).find(Document.parse("{}")).projection(Document.parse("{}")).sort(Document.parse(pomocni)).iterator();

                }
        }

        ///where deo bez icega u selektu


        else if(kljucneReci.contains("select") && kljucneReci.contains("from") && kljucneReci.contains("where") &&
                kljucneReci.size() == 3){

            if(mongoQuery.getSelectFields().contains("*")){
                for(Clause rec : sqlQuery.getClauses()){

                    if(rec instanceof Where){
                        ///db.restaurants.find({cuisine:"Japanese"})
                        ///db.employees.find({plata:100})
                        ///takodje radi i za and
                        ///db.restaurants.find({cuisine:"Japanese",plata:100})
                        if(rec.getParameters().size() >= 3  && !((Where) rec).getLogickiOperatori().contains("or")  && (((Where) rec).getOperatori().contains(ComparisonOperator.LIKE)  || ((Where) rec).getOperatori().contains(ComparisonOperator.EQUAL))){
                            //System.out.println("Usao u prvi");
                            stringBuilder.append("{");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("}}");
                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE, mongoQuery.getCollectionName(), stringBuilder.toString());
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String tabela = mongoQuery.getCollectionName().toString();
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse("{}")).iterator();

                        }

                        ///db.employees.find({$or: [{plata: 100}, {auto: 200}]})

                        else if(rec.getParameters().size() >= 3 && ((Where) rec).getLogickiOperatori().contains("or") && (((Where) rec).getOperatori().contains(ComparisonOperator.LIKE) || ((Where) rec).getOperatori().contains(ComparisonOperator.EQUAL))){
                            stringBuilder.append("{$or : [");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append("{").append(entry.getKey()).append(":").append(entry.getValue()).append("}").append(", ");
                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("]}");
                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE_JEDNAKO_OR_BEZPROJ, mongoQuery.getCollectionName(), stringBuilder.toString());
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String tabela = mongoQuery.getCollectionName().toString();
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse("{}")).iterator();

                        }

                        ///uradjen and i or za jednako i like BEZ projekcije


                        //db.restaurants.find({stars:{$gte:3.5}})
                        else if(rec.getParameters().size() == 3 && ((Where) rec).getOperatori().contains(ComparisonOperator.GREATER_THAN)){
                            stringBuilder.append("{");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append(entry.getKey()).append(":").append("{$gte:").append(entry.getValue()).append(", ");
                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("}}");
                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE_VECIMANJI, mongoQuery.getCollectionName(), stringBuilder.toString());
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String tabela = mongoQuery.getCollectionName().toString();
                            System.out.println(stringBuilder.toString());
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse("{}")).iterator();

                        }
                        //db.restaurants.find({stars:{$lt:3}})

                        else if(rec.getParameters().size() == 3 && ((Where) rec).getOperatori().contains(ComparisonOperator.LESS_THAN)){
                            stringBuilder.append("{");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append(entry.getKey()).append(":").append("{$lt:").append(entry.getValue()).append(", ");
                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("}}");
                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE_VECIMANJI, mongoQuery.getCollectionName(), stringBuilder.toString());
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String tabela = mongoQuery.getCollectionName().toString();
                            System.out.println(stringBuilder.toString());
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse("{}")).iterator();
                        }

                    }
                }
            }
            ///where deo sa projekcijom
            else{
                for(Clause rec : sqlQuery.getClauses()){

                    if(rec instanceof Where){
                        ///db.restaurants.find({cuisine:"Japanese"}).projection({name:1})
                        ///db.employees.find({plata:100}).projection({name:1})
                        ///radi i za and


                        if(rec.getParameters().size() >= 3 && !((Where) rec).getLogickiOperatori().contains("or") && (((Where) rec).getOperatori().contains(ComparisonOperator.LIKE) || ((Where) rec).getOperatori().contains(ComparisonOperator.EQUAL))){

                            stringBuilder.append("{");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("}}");
                            StringBuilder sb = new StringBuilder();
                            sb.append("{");
                            for (Map.Entry<String, Integer> entry : parametrizedSQLQuery.getParameterMap().entrySet()) {
                                for (String parametar : mongoQuery.getSelectFields()) {
                                    if (entry.getKey().equalsIgnoreCase(parametar)) {
                                        sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                                    }
                                }
                            }
                            sb.delete(sb.length() - 2, sb.length());
                            sb.append("}");
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String projekcija = sb.toString();
                            String tabela = mongoQuery.getCollectionName().toString();
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse(projekcija)).iterator();
                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE_SAPROJ, mongoQuery.getCollectionName(), stringBuilder.toString(),sb.toString());

                        }

                        else if(rec.getParameters().size() >= 3 && ((Where) rec).getLogickiOperatori().contains("or") && (((Where) rec).getOperatori().contains(ComparisonOperator.LIKE) || ((Where) rec).getOperatori().contains(ComparisonOperator.EQUAL))){
                            //System.out.println("usao u drugi");
                            stringBuilder.append("{$or : [");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append("{").append(entry.getKey()).append(":").append(entry.getValue()).append("}").append(", ");

                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("]}");
                            StringBuilder sb = new StringBuilder();
                            sb.append("{");
                            for (Map.Entry<String, Integer> entry : parametrizedSQLQuery.getParameterMap().entrySet()) {
                                for (String parametar : mongoQuery.getSelectFields()) {
                                    if (entry.getKey().equalsIgnoreCase(parametar)) {
                                        sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                                    }
                                }
                            }
                            sb.delete(sb.length() - 2, sb.length());
                            sb.append("}");
                            System.out.println(stringBuilder.toString());
                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE_JEDNAKO_OR_SAPROJ, mongoQuery.getCollectionName(), stringBuilder.toString(),sb.toString());
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String projekcija = sb.toString();
                            String tabela = mongoQuery.getCollectionName().toString();
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse(projekcija)).iterator();

                        }


                        //db.restaurants.find({stars:{$gte:3.5}}).projection({ime:1})

                        else if(rec.getParameters().size() == 3 && ((Where) rec).getOperatori().contains(ComparisonOperator.GREATER_THAN)){
                            stringBuilder.append("{");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append(entry.getKey()).append(":").append("{$gte:").append(entry.getValue()).append(", ");
                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("}}");
                            StringBuilder sb = new StringBuilder();

                            sb.append("{");

                            for (Map.Entry<String, Integer> entry : parametrizedSQLQuery.getParameterMap().entrySet()) {
                                for (String parametar : mongoQuery.getSelectFields()) {
                                    if (entry.getKey().equalsIgnoreCase(parametar)) {
                                        sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                                    }
                                }
                            }
                            sb.delete(sb.length() - 2, sb.length());

                            sb.append("}");
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String projekcija = sb.toString();
                            String tabela = mongoQuery.getCollectionName().toString();
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse(projekcija)).iterator();
                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE_VECIMANJI_SAPROJ, mongoQuery.getCollectionName(), stringBuilder.toString(),sb.toString());

                        }

                        //db.restaurants.find({stars:{$lt:3}})
                        else if(rec.getParameters().size() == 3 && ((Where) rec).getOperatori().contains(ComparisonOperator.LESS_THAN)){
                            stringBuilder.append("{");
                            for (Map.Entry<Object, Object> entry : mongoQuery.getWhereConditions().entrySet()) {

                                stringBuilder.append(entry.getKey()).append(":").append("{$lt:").append(entry.getValue()).append(", ");
                            }
                            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                            stringBuilder.append("}}");
                            StringBuilder sb = new StringBuilder();

                            sb.append("{");

                            for (Map.Entry<String, Integer> entry : parametrizedSQLQuery.getParameterMap().entrySet()) {
                                for (String parametar : mongoQuery.getSelectFields()) {
                                    if (entry.getKey().equalsIgnoreCase(parametar)) {
                                        sb.append(entry.getKey()).append(":").append(entry.getValue()).append(", ");
                                    }
                                }
                            }
                            sb.delete(sb.length() - 2, sb.length());

                            sb.append("}");
                            MongoDatabase database = connection.getDatabase("bp_tim55");
                            String projekcija = sb.toString();
                            String tabela = mongoQuery.getCollectionName().toString();
                            cursor = database.getCollection(tabela).find(Document.parse(stringBuilder.toString())).projection(Document.parse(projekcija)).iterator();

                            projection = String.format(MongoQueryTemplates.WHERE_TEMPLATE_VECIMANJI_SAPROJ, mongoQuery.getCollectionName(), stringBuilder.toString(),sb.toString());

                        }

                    }
                }
            }
        }
       while (cursor.hasNext()){
            Document d = cursor.next();
            System.out.println(d.toJson());
        }
        System.out.println(projection);
        return cursor;

    }

}

