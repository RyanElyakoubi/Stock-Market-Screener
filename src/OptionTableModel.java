
import java.util.List;

import javax.swing.table.AbstractTableModel;


/**
 * A table model implementation for a list of Option objects.
 * @author www.codejava.net
 *
 */
public class OptionTableModel extends AbstractTableModel {
    public static final int COLUMN_SYMBOL      = 0;
    public static final int COLUMN_RATIO     = 1;
    public static final int COLUMN_DATE    = 2;
    private static final int COLUMN_PRICE     = 3;
    private static final int COLUMN_STRIKE     = 4;
    private static final int COLUMN_BID      = 5;
    //  private static final int COLUMN_OPTION_TYPE    = 6;
    private static final int COLUMN_ASK   = 6;
    private static final int COLUMN_VOLUME    = 7;
    private static final int COLUMN_OI     = 8;
    public static final int COLUMN_BREAK_EVEN    = 9;
    public static final int COLUMN_PURCHASE    = 10;
    private static final int COLUMN_CALL_PERC    = 11;
    private static final int COLUMN_NEW_OPTION    = 12;
    //  public static String[] columnNames = {"Symbol", "Ratio", "Date", "Price", "Strike","Bid",
    //                                 "Ask","Volume", "OI", "BreakEven", "Purchase$", "Option", "PriceToBrkPerc", "New"};
    public static String[] columnNames = {"Symbol", "Ratio", "Date", "Price", "Strike","Bid",
            "Ask","Volume", "OI", "BreakEven", "Purchase$", "PriceToBrkPerc", "New"};
    private List<Option> listOptions;

    public OptionTableModel(List<Option> listOptions)
    {
        this.listOptions = listOptions;

        int indexCount = 1;
        for (Option option : listOptions)
        {
            option.setIndex(indexCount++);
        }
    }

    public void setOptionList(List<Option> theLlistOptions)
    {
        this.listOptions = theLlistOptions;

        int indexCount = 1;
        for (Option option : listOptions)
        {
            option.setIndex(indexCount++);
        }
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return listOptions.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }




    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (listOptions.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Option option = listOptions.get(rowIndex);
        Object returnValue = null;

        switch (columnIndex) {
            case COLUMN_SYMBOL:
                returnValue = option.getSymbol();
                break;
            case COLUMN_DATE:
                returnValue = option.getDate();
                break;
            case COLUMN_PRICE:
                returnValue = option.getPrice();
                break;
            case COLUMN_STRIKE:
                returnValue = option.getStrike();
                break;
            case COLUMN_BID:
                returnValue = option.getBid();
                break;

            case COLUMN_ASK:
                returnValue = option.getAsk();
                break;
            case COLUMN_VOLUME:
                returnValue = option.getVolume();
                break;
            case COLUMN_OI:
                returnValue = option.getOpenInterest();
                break;
            case COLUMN_RATIO:
                returnValue = option.getRatio();
                break;
            case COLUMN_BREAK_EVEN:
                returnValue = option.getBreakEven();
                break;

            case COLUMN_PURCHASE:
                returnValue = option.getPurchase();
                break;
            case COLUMN_NEW_OPTION:
                returnValue = option.getNewIndicator();
                break;

            case COLUMN_CALL_PERC:
                returnValue = option.getCallToPutPerc();
                break;


            default:
                throw new IllegalArgumentException("Invalid column index");
        }

        return returnValue;
    }


    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        Option option = listOptions.get(rowIndex);
        if (columnIndex == COLUMN_SYMBOL || columnIndex == COLUMN_PURCHASE)
        {
            option.setIndex( rowIndex);
        }
    }
 /*
  @Override
  public Class<?> getColumnClass(int columnIndex) {
      if (listEmployees.isEmpty()) {
          return Object.class;
      }
      return getValueAt(0, columnIndex).getClass();
  }
*/
}
