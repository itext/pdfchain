package ui.model;

import blockchain.Record;

import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 * Created by Joris Schellekens on 7/18/2017.
 */
public class JBlockchainTableModel extends DefaultTableModel {

    private List<Record> records = new ArrayList<>();
    private List<String> columns = new ArrayList<>();

    public JBlockchainTableModel() {

    }

    public JBlockchainTableModel setData(Collection<Record> records) {
        // put records
        this.records.clear();
        this.records.addAll(records);

        // determine common columns
        Map<String, Integer> columnNames = new HashMap<>();
        for (Record r : records) {
            for (String col : r.keySet()) {
                if (!columnNames.containsKey(col))
                    columnNames.put(col, 0);
                columnNames.put(col, columnNames.get(col) + 1);
            }
        }
        columns.clear();
        for (Map.Entry<String, Integer> en : columnNames.entrySet()) {
            if (en.getValue() == records.size())
                columns.add(en.getKey());
        }
        java.util.Collections.sort(columns);

        return this;
    }

    @Override
    public int getRowCount() {
        return records == null ? 0 : records.size();
    }

    @Override
    public int getColumnCount() {
        return columns == null ? 0 : columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return records.get(rowIndex).get(columns.get(columnIndex));
    }

}
