package src.main.java.gui;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import src.main.java.adapter.Adapter;
import src.main.java.adapter.mapper.MongoQuery;
import src.main.java.adapter.parametarConverter.ParametrizedSQLQuery;
import src.main.java.executor.TemplateCompleter;
import src.main.java.database.MongoDBController;
import src.main.java.packager.Packager;
import src.main.java.parser.SQLQuery;
import src.main.java.parser.SQLQueryParser;
import src.main.java.validator.QueryValidator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@Getter
@Setter


public class LiveSQLApp extends JFrame {

    private JTextArea codeTextArea;
    private JTable resultTable;
    private static LiveSQLApp instance = null;
    private SQLQuery sqlQuery;

    private LiveSQLApp() {


    }

    public static LiveSQLApp getInstance() {

        if (instance == null) {
            instance = new LiveSQLApp();
            instance.init();
        }
        return instance;
    }

    private void init() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        codeTextArea = new JTextArea();
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);

        JButton executeButton = new JButton("Execute");
        resultTable = new JTable();
        JScrollPane resultScrollPane = new JScrollPane(resultTable);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(codeScrollPane, BorderLayout.CENTER);
        mainPanel.add(executeButton, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(resultScrollPane, BorderLayout.SOUTH);
        this.setVisible(true);

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = codeTextArea.getText();
                SQLQueryParser parser = new SQLQueryParser();
                sqlQuery = parser.parseQuery(code);
                QueryValidator queryValidator = new QueryValidator();
                if (queryValidator.validateQuery(sqlQuery)) {

                    Adapter adapter = new Adapter();
                    MongoQuery mongoQuery = adapter.translate(sqlQuery);
                    ParametrizedSQLQuery parametrizedSQLQuery = adapter.getParametrizedQuery();
                    MongoClient mongoClient = MongoDBController.getConnection();
                    TemplateCompleter templateCompleter = new TemplateCompleter(mongoQuery, sqlQuery, parametrizedSQLQuery,mongoClient);
                    MongoCursor<Document> lista = templateCompleter.templateMetoda();
                    Packager.populateTable(lista,resultTable);
                    mongoClient.close();
                }
            }
        });
    }
}





