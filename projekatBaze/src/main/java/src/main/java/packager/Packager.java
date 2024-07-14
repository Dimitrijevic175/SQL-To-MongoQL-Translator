package src.main.java.packager;


import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Packager {

    public static void populateTable(MongoCursor<Document> cursor, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        while (cursor.hasNext()) {
            Document document = cursor.next();
            Object[] rowData = new Object[document.size()];

            int columnIndex = 0;
            for (String key : document.keySet()) {
                rowData[columnIndex++] = document.get(key);
            }

            model.addRow(rowData);
        }
        table.setModel(model);
    }

}
