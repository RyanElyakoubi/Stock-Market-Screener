



import javax.swing.table.AbstractTableModel;

import java.util.List;

public class StockUpgradeModel extends AbstractTableModel
{
    public static final int COLUMN_SYMBOL      = 0;
    public static final int COLUMN_BROKERAGE     = 1;
    public static final int COLUMN_ACTION    = 2;
    public static final int COLUMN_PREV_RATING     = 3;
    public static final int COLUMN_NEW_RATING    = 4;
    public static final int COLUMN_PREV_PRICE      = 5;
    public static final int COLUMN_NEW_PRICE   = 6;
    public static final int COLUMN_PECENT   = 6;


    /*
     * Columbia Banking  (COLB)
  Brokerage Firm:  DA Davidson  |  Action:  Upgrade  |  Ratings Change:  Neutral � Buy  |  Price Tgt:  $39 � $41
     */
    public static final String[] columnNames = {"Symbol", "Brokerage", "Action", "PrevRating", "NewRating", "PrevPrice", "NewPrice", "Percentage"};

    private List<StockUpgradeData> myList;
    public StockUpgradeModel(List<StockUpgradeData> theList)
    {
        myList = theList;
    }
    public void setStockUpgradeDataList(List<StockUpgradeData> theLlistStockUpgradeDatas)
    {
        this.myList = theLlistStockUpgradeDatas;

    }
    public int getColumnCount() {
        return columnNames.length;
    }
    public int getRowCount() {
        int size;
        if (myList == null) {
            size = 0;
        }
        else {
            size = myList.size();
        }
        return size;
    }
    public Object getValueAt(int row, int col) {
        Object temp = null;
        if (col == 0)
        {
            temp = myList.get(row).getSymbol();
        }


        else if (col == 1)
        {
            temp = myList.get(row).getBrokerage();
        }

        else if (col == 2)
        {
            temp = myList.get(row).getAction();
        }

        else if (col == 3)
        {
            temp = myList.get(row).getPrevRating();
        }
        else if (col == 4)
        {
            temp = myList.get(row).getNewRating();
        }
        else if (col == 5)
        {
            temp = myList.get(row).getPrevPrice();
        }
        else if (col == 6)
        {
            temp = myList.get(row).getNewPrice();
        }
        else if (col == 7)
        {
            temp = myList.get(row).getPercentage();
        }

        return temp;
    }
    // needed to show column names in JTable
    public String getColumnName(int col)
    {
        return columnNames[col];
    }
    public Class getColumnClass(int col)
    {
        //   return String.class;

        if (col == 5 || col == 6 || col == 7)
        {
            return Double.class;
        }
        else {
            return String.class;
        }

    }
}
