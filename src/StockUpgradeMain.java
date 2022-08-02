

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;


public class StockUpgradeMain extends JFrame
{
    private ArrayList<StockUpgradeData> finalStockUpgradeDataList = new ArrayList<StockUpgradeData>();
    private ArrayList<StockUpgradeData> filterStockUpgradeDataList = null;

    //  private static final String SCAN_DIR = "C:\\Users\\relya\\Downloads\\";
    private String scanDirectory = "C:\\Users\\relya\\OneDrive\\Desktop\\";
    private  String inputFile =  scanDirectory + "StockUpgrade.txt";  // go to https://spactrack.net/activespacs/  then select all, copy and paste it to spac122520.txt file
    public static String splitter = "  +";

    //  public static  String splitter="\\ss+";
    //   "TPGY	TPG Pace Beneficial Finance Corp.	Definitive Agreement	Attractive fundamentals with strong ES"
    private String outputFile = scanDirectory + "StockUpgradeOutputFile";
    private String dateString = null;

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private JTable table;
    private StockUpgradeModel tableModel = null;
    private JComboBox ratingComboBox = new JComboBox();
    private JComboBox searchComboBox = new JComboBox();
    private  JTextField priceField = new JTextField(5);
    private  JTextField ratioField = new JTextField(5);
    private  JButton updateData = new JButton("Update");

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem;
    private JMenuItem exitMenuItem;

    private JMenu helpMenu;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;

    private static final String newline = "\n";
    private JTextArea log;
    private JFileChooser fileChooser;

    private ArrayList<String> nasdaqList = new ArrayList<String>();
    private ArrayList<String> nyseList = new ArrayList<String>();

    private HashMap<String, Integer> indexMap = new  HashMap<String, Integer>();

    private  JTextField stockInfoField = new JTextField(5);
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    Date todayDate = null;


    public StockUpgradeMain()
    {
        super();
        nasdaqList.add("FB");
        nasdaqList.add("RXT");
        nasdaqList.add("JD");
        nasdaqList.add("ADV");
        nasdaqList.add("Z");
        nasdaqList.add("ZM");
        nasdaqList.add("LI");
        nasdaqList.add("NCLH");
        nasdaqList.add("INO");
        nasdaqList.add("IQ");
        nasdaqList.add("GOLD");
        nasdaqList.add("DBX");

        nyseList.add("LMND");
        nyseList.add("HYLN");
        nyseList.add("CLDR");
        nyseList.add("RVP");
        nyseList.add("TWLO");
        nyseList.add("HUYA");
        nyseList.add("FUBO");
        nyseList.add("SSTK");
        nyseList.add("DMYT");
        nyseList.add("PLNT");
        nyseList.add("XXII");
        nyseList.add("COTY");
        nyseList.add("CHWY");
        nyseList.add("SNOW");
        nyseList.add("SNAP");
        nyseList.add("KODK");
        nyseList.add("PINS");
        nyseList.add("UNFI");
        nyseList.add("VIPS");

    }

    public void saveOutputFile()
            throws IOException
    {

        FileWriter csvWriter = null;
        try
        {
            String outputfileName = outputFile + dateString + ".csv";

            csvWriter = new FileWriter(outputfileName);

            if (StockUpgradeModel.columnNames != null)
            {
                for (int i = 0; i < StockUpgradeModel.columnNames.length ; i++)
                {
                    csvWriter.append(StockUpgradeModel.columnNames[i]+ ",");
                }
            }
            csvWriter.append("\n");
            if (finalStockUpgradeDataList != null && finalStockUpgradeDataList.size() > 0)
            {
                for (StockUpgradeData singleStockUpgradeData: finalStockUpgradeDataList)
                {
                    csvWriter.append(singleStockUpgradeData.getSymbol() + ",");
                    csvWriter.append(singleStockUpgradeData.getBrokerage() + ",");
                    csvWriter.append(singleStockUpgradeData.getAction() + ",");
                    csvWriter.append(singleStockUpgradeData.getPrevRating() + ",");
                    csvWriter.append(singleStockUpgradeData.getNewRating()+ ",");
                    csvWriter.append(singleStockUpgradeData.getPrevPrice() + ",");
                    csvWriter.append(singleStockUpgradeData.getNewPrice()  + "\n");

            /*
          csvWriter.append(df2.format(singleStockUpgradeData.getValue()) + ",");
          csvWriter.append(df2.format(singleStockUpgradeData.getCap()) + ",");
          csvWriter.append(df2.format(singleStockUpgradeData.getPrice())  + "\n");
           */

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            csvWriter.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        csvWriter.close();
    }

    public void createJTable()
    {
        processStockUpgradeDatas();
        tableModel = new StockUpgradeModel(finalStockUpgradeDataList);

        table = new JTable(tableModel)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {

                Component c = super.prepareRenderer(renderer, row, column);
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);


                c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);

                return c;
            }
        };






        table.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0)
                {
                    String tickerString = (String) table.getValueAt(row, StockUpgradeModel.COLUMN_SYMBOL);


                    try
                    {
                        //   Desktop.getDesktop().browse(new URI("http://www.codejava.net"));

                        //    Desktop.getDesktop().browse(new URI("https://search.yahoo.com/search?p="+tickerString+ "&fr=yfp-t-s&ei=UTF-8&fp=1"));
                        if (col % 2 == 0)
                        {
                            // Desktop.getDesktop().browse(new URI("https://finance.yahoo.com/quote/"+tickerString));


                            if (Desktop.isDesktopSupported()) {
                                // Windows
                                Desktop.getDesktop().browse(new URI("https://finance.yahoo.com/quote/"+tickerString));
                            } else {
                                // Ubuntu
                                Runtime runtime = Runtime.getRuntime();
                                runtime.exec("/usr/bin/firefox -new-window " + "https://finance.yahoo.com/quote/"+tickerString);
                            }
                        }
                        else
                        {
                            if ((tickerString != null && tickerString.length() < 4 &&
                                    !nasdaqList.contains(tickerString)) || nyseList.contains(tickerString))
                            {
                                //    Desktop.getDesktop().browse(new URI("https://www.tradingview.com/symbols/NYSE-"+tickerString+"/"));

                                if (Desktop.isDesktopSupported()) {
                                    // Windows
                                    Desktop.getDesktop().browse(new URI("https://www.tradingview.com/symbols/NYSE-"+tickerString+"/"));
                                } else {
                                    // Ubuntu
                                    Runtime runtime = Runtime.getRuntime();
                                    runtime.exec("/usr/bin/firefox -new-window " + "https://www.tradingview.com/symbols/NYSE-"+tickerString);
                                }
                            }
                            else
                            {
                                //  Desktop.getDesktop().browse(new URI("https://www.tradingview.com/symbols/NASDAQ-"+tickerString+"/"));

                                if (Desktop.isDesktopSupported()) {
                                    // Windows
                                    Desktop.getDesktop().browse(new URI("https://www.tradingview.com/symbols/NASDAQ-"+tickerString+"/"));
                                } else {
                                    // Ubuntu
                                    Runtime runtime = Runtime.getRuntime();
                                    runtime.exec("/usr/bin/firefox -new-window " + "https://www.tradingview.com/symbols/NASDAQ-"+tickerString);
                                }
                            }
                            //  Desktop.getDesktop().browse(new URI("https://marketchameleon.com/Overview/"+tickerString+"/StockUpgradeDataChain"));
                        }
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }

                  /*

                  String message = "The row is => " + row + " col => " + col  + " ticker => " + tickerString;
                  JStockUpgradeDataPane.showMessageDialog(new JFrame(), message, "Dialog",
                      JStockUpgradeDataPane.INFORMATION_MESSAGE);
                  */
                }
            }
        });

        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        table.setRowHeight(30);
        // table.setRowHeight(3, 60);
        table.setFont(new Font("Arial", Font.BOLD, 20));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));
        table.getTableHeader().setBackground(Color.ORANGE);
        table.setAutoCreateRowSorter(true);


        //  TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        //        table.setRowSorter(sorter);




        menuBar = new JMenuBar();
        // build the File menu
        fileMenu = new JMenu("File");
        openMenuItem = new JMenuItem("Open");
        exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);

        // build the help menu
        helpMenu = new JMenu("Help");
        helpMenuItem = new JMenuItem("Help");
        aboutMenuItem = new JMenuItem("About");
        helpMenu.add(helpMenuItem);
        helpMenu.add(aboutMenuItem);

        // add menus to menubar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        priceField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateStockUpgradeModel();

            }
        });

        ratioField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateStockUpgradeModel();

            }
        });

        updateData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateFromFile();

            }
        });
        searchComboBox.addItem("ALL");
        searchComboBox.addItem("UPGRADE");
        searchComboBox.addItem("DOWNGRADE");
        searchComboBox.addItem("INITIATED");
        searchComboBox.addItem("REITERATED");
        searchComboBox.addItem("RESUMED");


        searchComboBox.setEditable(true);
        searchComboBox.setPreferredSize(new Dimension(100, 30));
        searchComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateStockUpgradeModel();
            }
        });

        ratingComboBox.addItem("ALL");
        ratingComboBox.addItem("BUY");
        ratingComboBox.addItem("SELL");
        ratingComboBox.addItem("HOLD");
        ratingComboBox.addItem("OVERWEIGHT");
        ratingComboBox.addItem("NEUTRAL");
        ratingComboBox.addItem("OUTPERFORM");
        ratingComboBox.addItem("UNDERPERFORM");
        ratingComboBox.addItem("EQUAL-WEIGHT");
        ratingComboBox.addItem("OVERWEIGHT");
        ratingComboBox.setEditable(true);
        ratingComboBox.setPreferredSize(new Dimension(100, 30));
        ratingComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateStockUpgradeModel();
            }
        });
        //    searchComboBox.addActionListener(createSearchActionListener(searchComboBox, table));

        //wrap comboBox in a panel
        JPanel panel = new JPanel();




        JLabel filterLabel = new JLabel("Action");
        filterLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        searchComboBox.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel ratingLabel = new JLabel("Rating");
        ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        ratingComboBox.setFont(new Font("SansSerif", Font.BOLD, 20));

        priceField.setText("4000");
        ratioField.setText("4");
        panel.add(filterLabel);
        panel.add(searchComboBox);

        panel.add(ratingLabel);
        panel.add(ratingComboBox);

        JLabel fakeLabel = new JLabel("           ");
        JLabel priceLabel = new JLabel("Price");
        JLabel stockInfoLabel = new JLabel("StockInfo");
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        stockInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        priceField.setFont(new Font("SansSerif", Font.BOLD, 20));
        stockInfoField.setFont(new Font("SansSerif", Font.BOLD, 20));
        updateData.setFont(new Font("SansSerif", Font.BOLD, 20));

        stockInfoField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    try
                    {
                        String tickerString =  stockInfoField.getText();
                        if (tickerString == null || tickerString.length() == 0 || tickerString.equalsIgnoreCase("EARN"))
                        {
                            // Desktop.getDesktop().browse(new URI("https://www.nasdaq.com/market-activity/earnings"));
                            Desktop.getDesktop().browse(new URI("https://stocktwits.com/discover/earnings-calendar"));
                        }
                        else if (tickerString == null || tickerString.length() == 0 || tickerString.equalsIgnoreCase("FDA"))
                        {
                            // Desktop.getDesktop().browse(new URI("https://www.nasdaq.com/market-activity/earnings"));
                            Desktop.getDesktop().browse(new URI("https://www.biopharmcatalyst.com/calendars/fda-calendar"));
                        }
                        else if (tickerString == null || tickerString.length() == 0 || tickerString.equalsIgnoreCase("UP"))
                        {
                            // Desktop.getDesktop().browse(new URI("https://www.nasdaq.com/market-activity/earnings"));
                            Desktop.getDesktop().browse(new URI("https://stockbeep.com/stocks-moving-up-now-sstime-desc"));
                        }
                        else if (tickerString == null || tickerString.length() == 0 || tickerString.equalsIgnoreCase("SPAC"))
                        {
                            // Desktop.getDesktop().browse(new URI("https://www.nasdaq.com/market-activity/earnings"));
                            Desktop.getDesktop().browse(new URI("https://spacinsider.com/stats/?utm_source=markets&utm_medium=ingest"));
                        }
                        else if (tickerString == null || tickerString.length() == 0 || tickerString.equalsIgnoreCase("SP"))
                        {
                            // Desktop.getDesktop().browse(new URI("https://www.nasdaq.com/market-activity/earnings"));
                            Desktop.getDesktop().browse(new URI("https://spactrack.net"));
                        }
                        else if (tickerString == null || tickerString.length() == 0 || tickerString.equalsIgnoreCase("TD"))
                        {
                            // Desktop.getDesktop().browse(new URI("https://www.nasdaq.com/market-activity/earnings"));
                            Desktop.getDesktop().browse(new URI("https://www.tdameritrade.com/home.page"));
                        }
                        else
                        {
                            Desktop.getDesktop().browse(new URI("https://finance.yahoo.com/quote/" + tickerString));
                        }
                    }
                    catch (Exception f)
                    {
                        f.printStackTrace();
                    }
                }
            }

        });

        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser(); // *** Keep
                File file1;
                //   JFileChooser selection = null;        // *** get rid of
                chooser.setCurrentDirectory(new java.io.File(scanDirectory));
                chooser.setSelectedFile(new File(""));
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv","csv","*.txt");
                // chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
                chooser.addChoosableFileFilter(filter);
                if (chooser.showOpenDialog(null) == JFileChooser.OPEN_DIALOG)
                {
                    try
                    {
                        file1 = chooser.getSelectedFile(); // **** YES ****

                        BufferedReader in = new BufferedReader(new FileReader(file1));
                        String line = in.readLine();
                        while (line != null)
                        {
                            System.out.println(line + "\n");
                            line = in.readLine();
                        }
                        System.out.println( "end folechooser \n");
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        aboutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(new JFrame(),
                        "Beta Version 1.0 \n Ryan Elyakoubi & Dad Joint Project.",
                        "About Info",
                        JOptionPane.INFORMATION_MESSAGE,
                        null);
            }
        });

        helpMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(new JFrame(),
                        "This is used to generate stick uograde summary data. You need to perform the follwoing steps:\n " +
                                "1. Go to https://www.briefing.com\n" +
                                "2. Copy thde page content and save it to your computer as a .txt file\n" +
                                "3. Run the java program to display the gui",
                        "Help Info",
                        JOptionPane.INFORMATION_MESSAGE,
                        null);
            }
        });
        //      openMenuItem.addActionListener(actionListener);

        panel.add(fakeLabel);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(fakeLabel);
        panel.add(fakeLabel);
        panel.add(stockInfoLabel);

     /*
      String values[] = { "SP", "SPAC", "FDA", "UP" };
      JComboBox comboBox = new JComboBox(values );
      comboBox.setEditable( true );
      ComboBoxEditor editor = comboBox.getEditor();
      stockInfoField = (JTextField)editor.getEditorComponent();
      */

        panel.add(stockInfoField);
        panel.add(fakeLabel);

        panel.add(updateData);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(table));

        // put the menubar on the frame
        setJMenuBar(menuBar);

        Calendar calendar = Calendar.getInstance();
        todayDate =  calendar.getTime();
        setPreferredSize(new Dimension(1500, 900));
        setTitle("StockUpgradeData Status " + sdf.format(todayDate));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean doesURLExist(URL url) throws IOException
    {

        //   URL u = new URL ( "http://www.example.com/");
        URL u = url;
        HttpURLConnection huc =  ( HttpURLConnection )  u.openConnection ();
        huc.setRequestMethod ("GET");  //OR  huc.setRequestMethod ("HEAD");
        huc.connect () ;
        int code = huc.getResponseCode() ;
        System.out.println(code);
        return code == HttpURLConnection.HTTP_OK;
    }

    public void updateFromFile()
    {
        processStockUpgradeDatas();
        try
        {
            saveOutputFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        priceField.setText("4000");
        ratioField.setText("1");
        searchComboBox.setSelectedItem("ALL");

        tableModel.setStockUpgradeDataList(finalStockUpgradeDataList);
        table.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }



    public void updateStockUpgradeModel()
    {
        String priceStringRaw =  priceField.getText();
        String minPriceStringRaw = "0";
        int minPrice = 0;
        int price = 0;
        if (priceStringRaw.contains("-"))
        {
            String[] priceRange = priceStringRaw.split("-");
            minPriceStringRaw = priceRange[0];
            priceStringRaw = priceRange[1];
        }
        else if (priceStringRaw.contains(" "))
        {
            String[] priceRange = priceStringRaw.split(" ");
            minPriceStringRaw = priceRange[0];
            priceStringRaw = priceRange[1];
        }


        try
        {
            minPrice = Integer.parseInt(minPriceStringRaw);
            price = Integer.parseInt(priceStringRaw);
        }
        catch (Exception e)
        {
            String message = "The price/ratio fields must be a number";
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String action = (String) searchComboBox.getSelectedItem();
        boolean allIndicator = false;
        if ("ALL".equals(action))
        {
            allIndicator = true;
        }

        String rating = (String) ratingComboBox.getSelectedItem();
        boolean ratingIndicator = false;
        if ("ALL".equals(rating))
        {
            ratingIndicator = true;
        }


        if (finalStockUpgradeDataList != null && finalStockUpgradeDataList.size() > 0)
        {
            filterStockUpgradeDataList = new ArrayList<StockUpgradeData>();
            for (StockUpgradeData singleStockUpgradeData: finalStockUpgradeDataList)
            {

                System.out.println("singleStockUpgradeData.getAction() => " + singleStockUpgradeData.getAction() + " status => " + action +   " allIndicator => " + allIndicator);
                double stockNewPrice =  singleStockUpgradeData.getNewPrice();

                if ((allIndicator || (singleStockUpgradeData.getAction().equals(action))) &&
                        (ratingIndicator || (singleStockUpgradeData.getNewRating().equals(rating))) &&
                        (( stockNewPrice >= minPrice ) &&
                                ( stockNewPrice <= price )))
                {
                    filterStockUpgradeDataList.add(singleStockUpgradeData);
                }

            }

            tableModel.setStockUpgradeDataList(filterStockUpgradeDataList);
            table.setModel(tableModel);
            tableModel.fireTableDataChanged();
            table.repaint();

        }

    }


    public void processStockUpgradeDatas()
    {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        try
        {

            inputFile = extractFileName();

            File file = new File(inputFile);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String symbol = null;
            String brokerage = null;
            String action = null;
            String prevRating = null;
            String newRating = null;
            double prevPrice = 0;
            double newPrice = 0;
            String prevPriceStr =  null;
            String newPriceStr =  null;
            finalStockUpgradeDataList.clear();
            String st;
            int lineNum = 1;
            while ((st = br.readLine()) != null)
            {

                //    symbol = null;
                brokerage = null;
                action = null;
                prevRating = null;
                newRating = null;
                prevPrice = 0;
                newPrice = 0;
                prevPriceStr =  null;
                newPriceStr =  null;

                if ((st.contains(" (")) && (st.contains(")")))
                {

                    int index1=st.indexOf("(") + 1;//returns the index of is substring
                    int index2=st.indexOf(")");//returns the index of index substring
                    System.out.println(index1+"  "+index2);//2 8
                    symbol = st.substring(index1, index2) ;
                    System.out.println("symbol value => " + symbol);
                    lineNum++;
                    String[] fields = st.split("\t");
                    //  {"Symbol", "Name", "Company", "Leader", "Value","Cap" , "Price"};
                }
                else if (st.contains("Action:"))
                {
                    //    Brokerage Firm:  DA Davidson  |  Action:  Upgrade  |  Ratings Change:  Neutral � Buy  |  Price Tgt:  $39 � $41

                    String newString =  st.replace(" ", "");
                    String [] temp = newString.split("\\|");


                    System.out.println("newString => " + newString);
                    //    System.out.println("temp[0] => " + temp[0] + "temp[1] => " + temp[1] );
                    brokerage = extractValue(temp[0], ":");
                    action = extractValue(temp[1], ":");
                    prevRating = extractRating(temp[2]);
                    String [] temp1 = prevRating.split(";");


                    if (temp1  != null && temp1.length == 2)
                    {
                        prevRating = temp1[0];
                        newRating = temp1[1];

                    }
                    else if (temp1  != null && temp1.length == 1)
                    {
                        prevRating = "NULL";
                        newRating = temp1[0];

                    }

                    if (temp.length > 3)
                    {
                        prevPriceStr = extractPrice(temp[3]);

                        String [] temp2 = prevPriceStr.split(";");


                        if (temp2  != null && temp2.length == 2)
                        {
                            prevPriceStr = temp2[0];
                            newPriceStr = temp2[1];

                        }
                        else if (temp2  != null && temp2.length == 1)
                        {
                            prevPriceStr = "0";
                            newPriceStr = temp2[0];

                        }
                        if (isStringNumeric(prevPriceStr))
                            prevPrice = Double.parseDouble(prevPriceStr);

                        if (isStringNumeric(newPriceStr))
                            newPrice = Double.parseDouble(newPriceStr);
                    }
                    lineNum++;
                    //    System.out.println("brokerage => " +  brokerage + "  action => " +  action + "  prevRating => " +  prevRating + "  prevPriceStr => " +  prevPriceStr);

                    StockUpgradeData stockUpgradeData = new StockUpgradeData(symbol,brokerage,action, prevRating, newRating, prevPrice,newPrice);
                    System.out.println("stockUpgradeData => " +  stockUpgradeData.toString());

                    finalStockUpgradeDataList.add(stockUpgradeData);
                    //   System.out.println("add spac to list  " +  stockUpgradeData.toString());

                }


            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean isStringNumeric(String strNum)
    {
        if (strNum == null && strNum.length() > 0)
        {
            return false;
        }
        try
        {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public String extractPrice(String theData)
    {

        String data = theData;
        String returnValue = "";

        String newString = null;
        if (data  != null && data.contains(":"))
        {
            String [] temp = data.split(":");
            if (temp  != null && temp.length == 2)
                newString = temp[1];
        }
        newString =  newString.replace("$", "");
        System.out.println("extractPrice theData => " + theData + " theData Length => " + theData.length()  +
                " newString => " + newString + " newString Length => " + newString.length());

        String first = "";
        String second =  "";
        char character;
        boolean specialCharInd = false;
        for (int i = 0; i < newString.length(); i++)
        {
            character = newString.charAt(i);
            isNumeric(character);
            if ((!isNumeric(character)))
            {
                specialCharInd = true;
            }
            else if (specialCharInd &&
                    (isNumeric(character))) //returns true if both conditions returns true
            {
                second = second + character;
            }
            else if (!specialCharInd &&
                    (isNumeric(character)))
            {
                first = first + character;
            }
        }

        if (!first.equals("") && first.length() > 0)
        {
            returnValue = first;

        }
        if (!second.equals("") && second.length() > 0)
        {
            returnValue = first + ";" + second;

        }
        System.out.println("extractPrice returnValue=> "  + returnValue + " first1=> " + first  +  " second1=> " + second +  " data=> " + data);
        if (returnValue == null)
            return data;
        return returnValue;
    }
    public String extractRating(String theData)
    {
        System.out.println("extractRating theData => " + theData);
        StringBuffer buff = new StringBuffer();
        String data = theData;
        String returnValue = "";

        //  String newString =  theData.replace("�", "");
        String newString =  theData;
        if (data  != null && data.contains(":"))
        {
            String [] temp = data.split(":");
            if (temp  != null && temp.length == 2)
                newString = temp[1];
        }


        String first = "";
        String second =  "";

        boolean specialCharInd = false;
        for (int i = 0; i < newString.length(); i++)
        {
            if (!(newString.charAt(i)>64 && newString.charAt(i)<=122))
            {
                specialCharInd = true;
            }
            else if (specialCharInd &&
                    (newString.charAt(i)>64 && newString.charAt(i)<=122)) //returns true if both conditions returns true
            {
                buff.append(newString.charAt(i));
                second = second + newString.charAt(i);
            }
            else if (!specialCharInd &&
                    (newString.charAt(i)>64 && newString.charAt(i)<=122))
            {
                first = first + newString.charAt(i);
            }
        }

        if (first  != null && first.length() > 1)
        {
            returnValue = first;

        }
        if (second  != null && second.length() > 1)
        {
            returnValue = first + ";" + second;

        }
        // System.out.println("extractRating returnValue=> " + returnValue + " data=> " + data);
        if (returnValue == null)
            return data;
        return returnValue;
    }


    public static boolean isNumeric(String maybeNumeric)
    {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }

    public static boolean isNumeric(char  theChar)
    {
        return Character.isDigit(theChar);
     /*
      boolean ind = false;
      if ((theChar == '0') ||
          (theChar == '1') ||
          (theChar == '2') ||
          (theChar == '3') ||
          (theChar == '4') ||
          (theChar == '5') ||
          (theChar == '6') ||
          (theChar == '7') ||
          (theChar == '8') ||
          (theChar == '9'))
      {
          ind = true;
      }

      return ind;  //match a number with optional '-' and decimal.
     */
    }
    public String extractValue(String theData,String theDelimiter)
    {
        //     System.out.println("extractValue theData => " + theData);
        String returnValue = null;
        String [] temp = theData.split(":");
        if (temp  != null && temp.length == 2)
            returnValue = temp[1];
        //     System.out.println("extractValue returnValue=> " + returnValue);
        return returnValue;
    }

    public String extractFileName()
    {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);
       /*
        fileChooser.setFileFilter(new FileFilter(){
            @Override
            public boolean accept(File file){
                // always accept directorys
                if(file.isDirectory())
                    return true;
                // but only files with specific name _SomeFixedFormat.def
                return file.getName().equals("_SomeFixedFormat.txt");
            }
            @Override
            public String getDescription() {
                return ".txt";
            }
        });
        */
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        String returnFileName = null;
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println( " file chooser  getAbsolutePath=> " + fileChooser.getCurrentDirectory().getAbsolutePath()+"    Selected file: " + selectedFile.getAbsolutePath());
            returnFileName = selectedFile.getAbsolutePath();

            scanDirectory = fileChooser.getCurrentDirectory().getAbsolutePath();
            inputFile =  scanDirectory + "StockUpgrade.txt";  // go to https://spactrack.net/activespacs/  then select all, copy and paste it to spac122520.txt file
            outputFile = scanDirectory + "StockUpgradeOutputFile";
        }
        return returnFileName;
    }
    public static void main(String[] args)
    {
        System.out.println("begin main");
        StockUpgradeMain  stockUpgrade = new StockUpgradeMain();
        stockUpgrade.createJTable();


        System.out.println("end main  ...that's all folks");
    }

}