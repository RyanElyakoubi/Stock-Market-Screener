


import java.text.DecimalFormat;

public class StockUpgradeData
{
    private static DecimalFormat df2 = new DecimalFormat("#00.00");
    //{"Symbol", "Brokerage", "Action", "PrevRating", "NewRating", "PrevPrice", "NewPrice"};
    int index = 0;
    private String symbol = null;
    private String brokerage = null;
    private String action = null;
    private String prevRating = null;
    private String newRating = null;
    private double prevPrice = 0;
    private double newPrice = 0;
    private double percentage = 0;



    public StockUpgradeData()
    {
        super();
    }

    public StockUpgradeData(String theSymbol,
                            String theBrokerage,
                            String theAction,
                            String thePrevRating,
                            String theNewRating,
                            double thePrevPrice,
                            double theNewPrice)
    {
        symbol = theSymbol;
        brokerage = theBrokerage;
        String actionup = theAction.toUpperCase();
        action   = actionup;
        prevRating   = thePrevRating;
        String newRatingup = theNewRating.toUpperCase();
        newRating   = newRatingup;
        prevPrice   = thePrevPrice;
        newPrice   = theNewPrice;
        if (newPrice > 0 && prevPrice > 0 )
        {
            percentage = (newPrice - prevPrice) / prevPrice * 100;
        }
    }


    public static void setDf2(DecimalFormat df2) {
        StockUpgradeData.df2 = df2;
    }

    public static DecimalFormat getDf2() {
        return df2;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
    public String getBrokerage() {
        return brokerage;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setPrevRating(String prevRating) {
        this.prevRating = prevRating;
    }

    public String getPrevRating() {
        return prevRating;
    }

    public void setNewRating(String newRating) {
        this.newRating = newRating;
    }

    public String getNewRating() {
        return newRating;
    }

    public void setPrevPrice(double prevPrice) {
        this.prevPrice = prevPrice;
    }

    public double getPrevPrice() {
        return prevPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        //  String valueStr = df2.format(value);
        // String capStr = df2.format(cap);
        //   String priceStr = df2.format(price);
        sb.append("symbol = " +  symbol + " brokerage = " +  brokerage + " action = " +  action+
                " prevRating = " +  prevRating +   " newRating = " +  newRating  + " prevPrice = " +  prevPrice +  " newPrice = " +  newPrice);

        return sb.toString();

    }

}
