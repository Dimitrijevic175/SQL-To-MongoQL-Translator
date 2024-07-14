package src.main.java.executor;

public class MongoQueryTemplates {


    // Šablon za SELECT upit
    public static final String SELECT_TEMPLATE = "db.%s.find({}).projection(%s)";

    public static final String SELECT_TEMPLATE_OSNOVNI = "db.%s.find({})";


    // Šablon za WHERE upit
    public static final String WHERE_TEMPLATE = "db.%s.find(%s)";
    public static final String WHERE_TEMPLATE_VECIMANJI = "db.%s.find(%s)";

    public static final String WHERE_TEMPLATE_JEDNAKO_OR_BEZPROJ = "db.%s.find(%s)";
    public static final String WHERE_TEMPLATE_JEDNAKO_OR_SAPROJ = "db.%s.find(%s).projection(%s)";
    public static final String WHERE_TEMPLATE_SAPROJ = "db.%s.find(%s).projection(%s)";
    public static final String WHERE_TEMPLATE_VECIMANJI_SAPROJ = "db.%s.find(%s).projection(%s)";


    // Šablon za SORT upit
    public static final String SORT_TEMPLATE = "db.%s.find({}).projection(%s).sort(%s)";
    public static final String SORT_TEMP_BEZPROJ = "db.%s.find({}).sort(%s)";


}
