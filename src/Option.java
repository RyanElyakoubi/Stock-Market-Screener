
import java.text.DecimalFormat;

public class Option
{
    private static DecimalFormat df2 = new DecimalFormat("#00.00");
    private double bid = 0;
    private double ask = 0;
    private double volToOIRatio = 0;
    private int volume  = 0;
    private int openInterest  = 0;
    private String ratio = null;
    private double CallToPutPerc = 100;
    int optionCount = 1;



    int index = 0;
    private long dateMill = 0;
    private String symbol = null;
    private String date = null;
    private String type = null;
    private double price = 0;
    private double strike = 0;
    private double breakEven = 0;
    private int purchase = 0;
    private String newIndicator = "NO";



    public void setOptionCount(int optionCount) {
        this.optionCount = optionCount;
    }

    public int getOptionCount() {
        return optionCount;
    }

    public void IncrementOptionCount()
    {
        this.optionCount++;
    }

    public void setCallToPutPerc(double CallToPutPerc) {
        this.CallToPutPerc = CallToPutPerc;
    }

    public double getCallToPutPerc() {
        return CallToPutPerc;
    }
    public void setNewIndicator(String newIndicator)
    {
        this.newIndicator = newIndicator;
    }

    public String getNewIndicator()
    {
        return newIndicator;
    }

    public void setPurchase(int purchase)
    {
        this.purchase = purchase;
    }

    public int getPurchase()
    {
        return purchase;
    }

    public void setBreakEven(double breakEven)
    {
        this.breakEven = breakEven;
    }

    public double getBreakEven()
    {
        return breakEven;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setDateMill(long dateMill)
    {
        this.dateMill = dateMill;
    }

    public long getDateMill()
    {
        return dateMill;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    public Option()
    {
        super();
    }

    public Option(String theSymbol,
                  String theDate,
                  double thePrice,
                  String theType,
                  double theStrike,
                  double theBid ,
                  double theAsk,
                  int theVolume ,
                  int theOpenInterest)
    {
        symbol = theSymbol;
        date = theDate;
        price = thePrice;
        type = theType;
        if (type == null)
        {
            type = "Callyx";
        }
        strike = theStrike;
        bid = theBid;
        ask = theAsk;
        volume  = theVolume;
        openInterest  = theOpenInterest;
        double ratioD = (double) volume / openInterest;
        ratio = df2.format(ratioD);
        //volToOIRatio = Double.parseDouble(ratio);
        volToOIRatio = ratioD;//Double.parseDouble(ratio);
        System.out.println(toString());
    }

    public static void setDf2(DecimalFormat df2)
    {
        Option.df2 = df2;
    }

    public static DecimalFormat getDf2()
    {
        return df2;
    }

    public void setRatio(String ratio)
    {
        this.ratio = ratio;
        volToOIRatio = Double.parseDouble(ratio);
    }

    public String getRatio()
    {
        return ratio;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getPrice()
    {
        return price;
    }

    public void setStrike(double strike)
    {
        this.strike = strike;
    }

    public double getStrike()
    {
        return strike;
    }

    public void setBid(double bid)
    {
        this.bid = bid;
    }

    public double getBid()
    {
        return bid;
    }

    public void setAsk(double ask)
    {
        this.ask = ask;
    }

    public double getAsk()
    {
        return ask;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
    }

    public int getVolume()
    {
        return volume;
    }

    public void setOpenInterest(int openInterest)
    {
        this.openInterest = openInterest;
    }

    public int getOpenInterest()
    {
        return openInterest;
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        double ratio = (double) volume / openInterest;
        String ratioStr = df2.format(ratio);
        sb.append("symbol = " +  symbol + " date = " +  date + " timeMil = " +  dateMill + " price = " +  price +
                " strike = " +  strike + " bid = " +  bid + " ask = " +  ask + " volume = " +  volume +
                " openInterest = " +  openInterest + " volumetoOpenInterest = "  + ratioStr  + " optionType = "  + type);

        return sb.toString();

    }

    public void setVolToOIRatio(double volToOIRatio)
    {
        this.volToOIRatio = volToOIRatio;
    }

    public double getVolToOIRatio()
    {
        return volToOIRatio;
    }
}