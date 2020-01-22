package crawler;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Модель таблицы
 */

public class TableUrlWebPage extends AbstractTableModel {

    //Количество колонок
    private int columnCount = 2;

    //Массив в котором хранятся данные таблицы
    private ArrayList<String[]> array;

    TableUrlWebPage(){
        this.array = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return array.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        String[] row = array.get(i);
        return row[i1];
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "URL";
            case 1:
                return "Title";
            default:
                return "";
        }
    }

    public void addAllMap(Map<String, String> map){
        this.array = new ArrayList<>(map.size());
        AtomicInteger i = new AtomicInteger();
        map.forEach((k, v) -> array.add(i.getAndIncrement(), new String[]{k, v}));
    }

    public void addRow(String[] row){
        String[] rowTable = new String[columnCount];
        rowTable = row;
        array.add(rowTable);
    }

    public void removeAll(){
        array.clear();
    }
}