

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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ComboBoxEditor;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


class UnusualOptionMain extends JFrame
{
    private ArrayList<Option> finalOptionList = new ArrayList<Option>();
    private ArrayList<Option> finalPutOptionList = new ArrayList<Option>();
    private ArrayList<Option> filteredOptionList = null;
    private HashMap<String,ArrayList<Option>> optionsMap = new HashMap<String,ArrayList<Option>>();
    private HashMap<String,ArrayList<Option>> putOptionsMap = new HashMap<String,ArrayList<Option>>();
    private HashMap<String,ArrayList<Option>> previousOptionsMap = new HashMap<String,ArrayList<Option>>();

    private String title = null;
    private static final String WALL_STREET_FILE = "/Users/ryanelyakoubi/Downloads/WallStreetUpgrade.csv";



    private static final String SCAN_DIR = "/Users/ryanelyakoubi/Downloads/";
    //  private static final String REPORT_DIR = "C:\\Users\\relya\\Documents\\HistoricalOptions\\";
    private static final String REPORT_DIR = "/Users/ryanelyakoubi/Downloads/";
    //unusual-stock-options-activity-01-13-2022.csv
    private String csvFile1 =  SCAN_DIR + "unusual-stock-options-activity-";
    private String csvFile2 = SCAN_DIR + "unusual-stock-options-activity-";
    private String outputFile = REPORT_DIR + "OptionOutputFile";
    private String dateString = null;

    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private JTable table;
    private OptionTableModel tableModel = null;

    private JComboBox searchComboBox = new JComboBox();
    private JComboBox optionTypeComboBox = new JComboBox();
    private  JTextField priceField = new JTextField(5);
    private  JTextField ratioField = new JTextField(5);
    private  JButton updateData = new JButton("Update");

    private static final int GREEN_RATIO = 7;
    private static final int GREEN_DOLLAR = 250000;
    private static final int YELLOW_RATIO = 4;


    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem;
    private JMenuItem exitMenuItem;

    private JMenu webMenu ;
    private JMenuItem trendMenuItem;
    private JMenuItem  fdaMenuItem;
    private JMenuItem earnMenuItem;


    private JMenu spacMenu;
    private JMenuItem spacMenuItem;


    private static final String newline = "\n";
    private JTextArea log;
    private JFileChooser fileChooser;

    private ArrayList<String> nasdaqList = new ArrayList<String>();
    private ArrayList<String> nyseList = new ArrayList<String>();

    private HashMap<String, Integer> indexMap = new  HashMap<String, Integer>();

    private  JTextField stockInfoField = new JTextField(5);
    // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   // yyyy/MM/dd
    Date todayDate = null;


    public UnusualOptionMain()
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
        nasdaqList.add("VFF");

        nyseList.add("LMND");
        nyseList.add("WBAI");
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

            System.out.println("outputFile => " + outputFile);
            csvWriter = new FileWriter(outputfileName);

            if (OptionTableModel.columnNames != null)
                for (int i = 0; i < OptionTableModel.columnNames.length ; i++)
                {
                    csvWriter.append(OptionTableModel.columnNames[i]+ ",");
                }
            csvWriter.append("\n");
            if (finalOptionList != null && finalOptionList.size() > 0)
            {
                for (Option singleOption: finalOptionList)
                {
                    csvWriter.append(singleOption.getSymbol() + ",");
                    csvWriter.append(singleOption.getRatio() + ",");
                    csvWriter.append(singleOption.getDate() + ",");
                    csvWriter.append(singleOption.getPrice() + ",");
                    csvWriter.append(df2.format(singleOption.getStrike()) + ",");
                    //   double ratio = ((singleOption.getStrike() / singleOption.getPrice()  ) - 1 ) * 100;
                    //   csvWriter.append(df2.format(ratio) + "%,");
                    csvWriter.append(df2.format(singleOption.getBid()) + ",");
                    csvWriter.append(df2.format(singleOption.getAsk()) + ",");
                    csvWriter.append(singleOption.getVolume() + ",");
                    csvWriter.append(singleOption.getOpenInterest() + ",");
                    double breakEven = singleOption.getStrike()+ singleOption.getAsk();
                    singleOption.setBreakEven(breakEven);
                    csvWriter.append(df2.format(breakEven) + ",");
                    int purchase = (int) (singleOption.getVolume() * singleOption.getAsk() * 100);
                    csvWriter.append(df2.format(purchase) + ",");
                    singleOption.setPurchase(purchase);
                    csvWriter.append(singleOption.getType() + ",");
                    csvWriter.append(df2.format(singleOption.getCallToPutPerc()) + ",");
                    csvWriter.append(singleOption.getNewIndicator() + "\n");
                    double volToOiRatio = singleOption.getVolume() / singleOption.getOpenInterest();
                    singleOption.setVolToOIRatio(volToOiRatio);
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
        //Used to get the data from wsj stock upgrade/downgrade    processWallStreetFile();
        processOptions();
        generateOptionsReport();
        generatePutOptionsReport();
        try
        {
            System.out.println(" not saving the file yet...");
            saveOutputFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        tableModel = new OptionTableModel(finalOptionList);
        table = new JTable(tableModel)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {

                int modelRow = table.convertRowIndexToModel(row);
                int modelColumn = table.convertColumnIndexToModel(column);
                Component c = super.prepareRenderer(renderer, row, column);

                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);

                if (!isRowSelected(modelRow))
                {
                    String ratioString = (String) table.getModel().getValueAt(modelRow, OptionTableModel.COLUMN_RATIO);
                    double ratio = Double.parseDouble(ratioString);

                    int purchase = (Integer) table.getModel().getValueAt(modelRow, OptionTableModel.COLUMN_PURCHASE);
                    //  System.out.println("breakEven => " + purchase + " ratio => " + ratio);
                    // double breakEven = Double.parseDouble(ratioString);


                    double inputRatio = Double.parseDouble(ratioField.getText());
                    if (ratio > GREEN_RATIO &&  purchase > GREEN_DOLLAR)
                    {
                        c.setBackground(Color.GREEN);
                    }
                    else if (ratio > YELLOW_RATIO)
                    {
                        c.setBackground(Color.YELLOW);
                    }
                    else
                    {
                        c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);
                    }
                }

                                   /*
                                    Component c = super.prepareRenderer(renderer, row, column);

                                     ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);

                                   if (!isRowSelected(row))
                                   {
                                     String ratioString = (String) table.getModel().getValueAt(row, OptionTableModel.COLUMN_RATIO);

                                     double ratio = Double.parseDouble(ratioString);
                                     if (ratio > 12)
                                     {
                                       c.setBackground(Color.GREEN);
                                     }
                                     else
                                     {
                                       c.setBackground(row % 2 == 0 ? getBackground() : Color.LIGHT_GRAY);
                                     }
                                   }
                                */
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
                    String tickerString = (String) table.getValueAt(row, OptionTableModel.COLUMN_SYMBOL);


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
                                //   Runtime.getRuntime().exec(new String[]{"bash", "-c", "google-chrome http://finance.yahoo.com/quote/"+tickerString  });
                            }
                        }
                        else
                        {
                        /*
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
                                   //   Runtime.getRuntime().exec(new String[]{"bash", "-c", "google-chrome http://www.tradingview.com/symbols/NYSE-"+tickerString});
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
                                   //   Runtime.getRuntime().exec(new String[]{"bash", "-c", "google-chrome http://www.tradingview.com/symbols/NASDAQ-"+tickerString});
                               }
                         }
                         */
                            if (Desktop.isDesktopSupported()) {
                                // Windows
                                Desktop.getDesktop().browse(new URI("https://stockanalysis.com/stocks/"+tickerString+"/"));
                            } else {
                                // Ubuntu
                                Runtime runtime = Runtime.getRuntime();
                                runtime.exec("/usr/bin/firefox -new-window " + "https://stockanalysis.com/stocks/"+tickerString);
                                //   Runtime.getRuntime().exec(new String[]{"bash", "-c", "google-chrome http://www.tradingview.com/symbols/NASDAQ-"+tickerString});
                            }
                            //  Desktop.getDesktop().browse(new URI("https://marketchameleon.com/Overview/"+tickerString+"/OptionChain"));
                        }
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }

                  /*

                  String message = "The row is => " + row + " col => " + col  + " ticker => " + tickerString;
                  JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                      JOptionPane.INFORMATION_MESSAGE);
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


        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        int columnIndexToSort = table.convertColumnIndexToModel(OptionTableModel.COLUMN_DATE);
        sorter.setComparator(columnIndexToSort, new Comparator<String>()
        {
            @Override
            public int compare(String o1, String o2)
            {
                try
                {
                    Date filterDate1 = sdf.parse(o1);
                    Date filterDate2 = sdf.parse(o2);
                    return filterDate1.compareTo(filterDate2);
                }
                catch (ParseException e)
                {
                    System.out.println("Exception comparing dates");
                }
                return 1;
            }

        });



        menuBar = new JMenuBar();
        // build the File menu
        fileMenu = new JMenu("File");
        openMenuItem = new JMenuItem("Open");
        exitMenuItem = new JMenuItem("Exit");

        spacMenu = new JMenu("SPAC");
        spacMenuItem = new JMenuItem("spac");
        spacMenu.add(spacMenuItem);


//openMenuItem.addActionListener(this);
        fileMenu.add(openMenuItem);
        fileMenu.add(exitMenuItem);



        webMenu = new JMenu("Info");
        trendMenuItem = new JMenuItem("Trend");
        fdaMenuItem = new JMenuItem("FDA");
        earnMenuItem = new JMenuItem("Earn");

        webMenu.add(trendMenuItem);
        webMenu.add(fdaMenuItem);
        webMenu.add(earnMenuItem);





        // add menus to menubar
        menuBar.add(fileMenu);
        menuBar.add(webMenu);
        menuBar.add(spacMenu);


        priceField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateOptionModel();

            }
        });

        ratioField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateOptionModel();

            }
        });

        updateData.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateFromFile(null);

            }
        });

        Calendar calendar = Calendar.getInstance();
        todayDate =  calendar.getTime();
        try
        {
            searchComboBox.addItem("ALL");
            for (int i = 0; i < 21; ++i)
            {
                calendar.add(Calendar.DATE, 1);
                Date date = calendar.getTime();
                searchComboBox.addItem(sdf.format(date));
            }
        }
        catch (Exception e1)
        {
            e1.printStackTrace();

        }
        searchComboBox.setEditable(true);
        searchComboBox.setPreferredSize(new Dimension(100, 30));
        searchComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateOptionModel();
            }
        });

        optionTypeComboBox.addItem("CALL");
        optionTypeComboBox.addItem("PUT");
        optionTypeComboBox.setPreferredSize(new Dimension(100, 30));
        optionTypeComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event)
            {
                updateOptionModel();
            }
        });
        //    searchComboBox.addActionListener(createSearchActionListener(searchComboBox, table));

        //wrap comboBox in a panel
        JPanel panel = new JPanel();




        JLabel filterLabel = new JLabel("Filter");
        filterLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        searchComboBox.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(filterLabel);
        panel.add(searchComboBox);

        JLabel optionTypeLabel = new JLabel("  Type");
        optionTypeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        optionTypeComboBox.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(optionTypeLabel);
        panel.add(optionTypeComboBox);


        priceField.setText("4000");
        ratioField.setText("4");
        JLabel fakeLabel = new JLabel("       ");
        JLabel priceLabel = new JLabel("  Price");
        JLabel stockInfoLabel = new JLabel("  StockInfo");
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel ratioLabel = new JLabel("  Ratio");
        ratioLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        stockInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        priceField.setFont(new Font("SansSerif", Font.BOLD, 20));
        stockInfoField.setFont(new Font("SansSerif", Font.BOLD, 20));
        ratioField.setFont(new Font("SansSerif", Font.BOLD, 20));
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
                chooser.setCurrentDirectory(new java.io.File(SCAN_DIR));
                chooser.setSelectedFile(new File(""));
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.csv","csv");
                // chooser.addChoosableFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
                chooser.addChoosableFileFilter(filter);
                if (chooser.showOpenDialog(null) == JFileChooser.OPEN_DIALOG)
                {
                    try
                    {
                        file1 = chooser.getSelectedFile(); // **** YES ****
                        title = file1.getName() ;
                        System.out.println("GUI file name => " + file1.getName()  +
                                "\n abs getAbsoluteFile => " + file1.getAbsoluteFile() +
                                "\n abs getCanonicalFile => " + file1.getCanonicalFile()+
                                "\n abs getCanonicalPath => " + file1.getCanonicalFile());
                        updateFromFile(file1.getCanonicalPath());
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
      /*
      spacMenuItem.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e)
                 {
                     SpacMain  spac = new SpacMain();
                     spac.createJTable();
                 }
                 });

*/
        trendMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {

                try
                {
                    Desktop.getDesktop().browse(new URI("https://stockbeep.com/trending-stocks"));
                }
                catch (Exception f)
                {
                    f.printStackTrace();
                }
            }
        });

        fdaMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    Desktop.getDesktop().browse(new URI("https://www.biopharmcatalyst.com/calendars/fda-calendar"));
                }
                catch (Exception f)
                {
                    f.printStackTrace();
                }
            }
        });

        earnMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    // Desktop.getDesktop().browse(new URI("https://www.nasdaq.com/market-activity/earnings"));
                    Desktop.getDesktop().browse(new URI("https://stocktwits.com/discover/earnings-calendar"));
                }
                catch (Exception f)
                {
                    f.printStackTrace();
                }
            }
        });


        panel.add(fakeLabel);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(fakeLabel);
        panel.add(ratioLabel);
        panel.add(ratioField);
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

        setPreferredSize(new Dimension(1500, 900));
        //   setTitle("UnusualOption Activity " + sdf.format(todayDate));
        //   setTitle("UnusualOption Activity " +dateString);
        setTitle(title);
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

    public void updateFromFile(String theFileName)
    {
        if ( theFileName == null)
        {
            processOptions();
            try
            {
                saveOutputFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            processOptionsHistory(theFileName);
        }
        generateOptionsReport();
        generatePutOptionsReport();
        setTitle(title);
        System.out.println("title => " + title);

        priceField.setText("4000");
        ratioField.setText("1");
        searchComboBox.setSelectedItem("ALL");

        tableModel.setOptionList(finalOptionList);
        table.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }


    public void updateOptionModel()
    {
        String priceStringRaw =  priceField.getText();
        String ratioStringRaw =  ratioField.getText();

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


        int ratio;
        try
        {
            minPrice = Integer.parseInt(minPriceStringRaw);
            price = Integer.parseInt(priceStringRaw);
            ratio = Integer.parseInt(ratioStringRaw);
        }
        catch (Exception e)
        {
            String message = "The price/ratio fields must be a number";
            JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String stockInfoString =  (String) optionTypeComboBox.getSelectedItem();
        String dateStringRaw = (String) searchComboBox.getSelectedItem();

        String dateString = dateStringRaw.replaceAll("\\s+","");
        Calendar filterCalendar = Calendar.getInstance();
        boolean allIndicator = false;
        Date filterDate =  null;
        if ("ALL".equals(dateString))
        {
            allIndicator = true;
        }
        else
        {
            try
            {
                filterDate = sdf.parse(dateString);
                filterCalendar.setTime(filterDate);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        ArrayList<Option> optionList = finalOptionList;
        if ("PUT".equals(stockInfoString))
        {
            optionList = finalPutOptionList;
        }

        if (optionList != null && optionList.size() > 0)
        {
            filteredOptionList = new ArrayList<Option>();
            Date optionDate  = null;
            for (Option singleOption: optionList)
            {
                String optionDateSt = singleOption.getDate().replaceAll("\\s+","");

                try
                {
                    optionDate = sdf.parse(optionDateSt);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }


                //    if ((allIndicator || (optionDate.compareTo(dateString))  <= 0 )
                //    if ((allIndicator || (singleOption.getDateMill() <= filterCalendar.getTimeInMillis()))

                if ((allIndicator || ( optionDate.compareTo(filterDate) ) <= 0)
                        && ( singleOption.getPrice() >= minPrice )
                        && ( singleOption.getPrice() <= price )
                        && ( singleOption.getVolToOIRatio() >= ratio ))
                {
                    filteredOptionList.add(singleOption);
                }

            }
            tableModel.setOptionList(filteredOptionList);
            table.setModel(tableModel);
            tableModel.fireTableDataChanged();
            table.repaint();

        }

    }

    public boolean isTherePutOptionBeforeCallOption(Option theOption)
    {
        boolean result = false;
        if(putOptionsMap.containsKey(theOption.getSymbol()))
        {
            ArrayList<Option>  multiOptions = putOptionsMap.get(theOption.getSymbol());
            for (Option singleOption: multiOptions)
            {
                if (singleOption.getDateMill() <= theOption.getDateMill())
                {
                    System.out.println(" $$$$$ excluding stock as its put date is less than put date " + singleOption.toString() + "\n call data " + theOption.toString());
                    return true;
                }
            }
        }
        return result;
    }

    public boolean isThereCallOptionBeforePutOption(Option theOption)
    {
        boolean result = false;
        if(optionsMap.containsKey(theOption.getSymbol()))
        {
            ArrayList<Option>  multiOptions = optionsMap.get(theOption.getSymbol());
            for (Option singleOption: multiOptions)
            {
                if (singleOption.getDateMill() <= theOption.getDateMill())
                {
                    System.out.println(" $$$$$ excluding stock as its put date is less than put date " + singleOption.toString() + "\n put data " + theOption.toString());
                    return true;
                }
            }
        }
        return result;
    }

    public void generateOptionsReport()
    {
        System.out.println(" begin generateOptionsReport...");
        finalOptionList.clear();
        for (Map.Entry<String, ArrayList<Option>> entry : optionsMap.entrySet())
        {

            String symbol = entry.getKey();
            System.out.println("\n\n\n\n processing stock "  + symbol);
            ArrayList<Option> optionList  = entry.getValue();
            if (optionList != null && optionList.size() > 0)
            {
                int totalCallVolume = 0;
                int totalCallOpenInterest =  0;
                double totalCallStrike =  0;
                double totalPrice =  0;
                double totalCallAsk =  0;
                double totalCallSale =  0;
                int totalCallCount =  1;
                double totalCallBid =  0;
                String callDate  = null;
                long tempCallDate = Long.MAX_VALUE;
                Option tempCallOption = null;
                String type = "Call";

                for (Option singleOption: optionList)
                {
                    if (singleOption.getType().equals("Call"))
                    {
                        //   System.out.println(" Parse Map option Call : " + singleOption.toString());
                        if (singleOption.getDateMill() < tempCallDate)
                        {
                            //    System.out.println(" Parse Map option Call : Date is less than tempDate " + singleOption.toString());
                            tempCallDate = singleOption.getDateMill();
                            callDate = singleOption.getDate();
                            tempCallOption = new Option(singleOption.getSymbol(),
                                    singleOption.getDate(),
                                    singleOption.getPrice(),
                                    singleOption.getType(),
                                    singleOption.getStrike(),
                                    singleOption.getBid() ,
                                    singleOption.getAsk(),
                                    singleOption.getVolume() ,
                                    singleOption.getOpenInterest());
                            tempCallOption.setDateMill(singleOption.getDateMill());
                            totalCallVolume = singleOption.getVolume();
                            totalCallOpenInterest =  singleOption.getOpenInterest();
                            totalCallStrike = singleOption.getStrike();
                            totalPrice =  singleOption.getPrice();
                            totalCallAsk = singleOption.getAsk();
                            totalCallBid =  singleOption.getBid();
                            totalCallCount = 1;
                            totalCallSale = (tempCallOption.getVolume() * totalCallAsk * 100);
                        }
                        else if (singleOption.getDateMill() == tempCallOption.getDateMill())
                        {
                            System.out.println(" Parse Map option Call : Date is is same as tempDate " + singleOption.toString());

                            totalCallVolume = singleOption.getVolume() + totalCallVolume;
                            totalCallOpenInterest =  singleOption.getOpenInterest() + totalCallOpenInterest;
                            totalCallStrike = singleOption.getStrike() + totalCallStrike;
                            totalPrice =  singleOption.getPrice() + totalPrice;
                            totalCallAsk = singleOption.getAsk() + totalCallAsk;
                            totalCallBid =  singleOption.getBid() + totalCallBid;
                            totalCallSale = (singleOption.getVolume() * totalCallAsk * 100) + totalCallSale;
                            totalCallCount++;

                        }
                    }
                    else
                    {
                        System.out.println(" Undefined option : " );
                    }
                }    //end for options loop

                if (( tempCallOption != null) && !isTherePutOptionBeforeCallOption(tempCallOption))
                {
                    totalCallStrike = totalCallStrike / totalCallCount;
                    totalPrice =  totalPrice / totalCallCount;
                    totalCallAsk = totalCallAsk / totalCallCount;
                    totalCallBid =  totalCallBid / totalCallCount;
                    Option option = new Option(symbol,
                            callDate,
                            totalPrice,
                            type,
                            totalCallStrike,
                            totalCallBid ,
                            totalCallAsk,
                            totalCallVolume ,
                            totalCallOpenInterest);
                    double breakEven = totalCallStrike + totalCallAsk ;
                    int purchase = (int) (option.getVolume() * totalCallAsk * 100);

                    //   int purchase = (int) totalCallSale;
                    System.out.println( symbol + " totalCallVolume "  + totalCallVolume + " totalCallOpenInterest " + totalCallOpenInterest +
                            " totalCallAsk => " + totalCallAsk + " volume => " + option.getVolume() + " purchase => " + purchase );
                    option.setPurchase(purchase);
                    option.setBreakEven(breakEven);

                    // option.setCallToPutPerc(totalCallCount);
                    option.setCallToPutPerc(option.getPrice() /breakEven * 100);

                    if (!previousOptionsMap.isEmpty())
                    {
                        if ((optionsMap.containsKey(symbol)) &&
                                (!previousOptionsMap.containsKey(symbol)))
                        {
                            option.setNewIndicator("YES");
                        }

                    }
                    finalOptionList.add(option);
                }
            }
        }
        System.out.println("final report %%%%%%%%%");
        if (finalOptionList != null && finalOptionList.size() > 0)
        {
            for (Option singleOption: finalOptionList)
            {
                System.out.println(singleOption.toString());
            }
        }
        System.out.println(" end generateOptionsReport...");
    }

    public void generatePutOptionsReport()
    {
        System.out.println(" begin generatePutOptionsReport...");
        finalPutOptionList.clear();
        for (Map.Entry<String, ArrayList<Option>> entry : putOptionsMap.entrySet())
        {

            String symbol = entry.getKey();
            System.out.println("\n\n\n\n processing stock "  + symbol);
            ArrayList<Option> optionList  = entry.getValue();
            if (optionList != null && optionList.size() > 0)
            {
                int totalCallVolume = 0;
                int totalCallOpenInterest =  0;
                double totalCallStrike =  0;
                double totalPrice =  0;
                double totalCallAsk =  0;
                double totalCallSale =  0;
                int totalCallCount =  1;
                double totalCallBid =  0;
                String callDate  = null;
                long tempCallDate = Long.MAX_VALUE;
                Option tempCallOption = null;
                String type = "Put";

                for (Option singleOption: optionList)
                {
                    if (singleOption.getType().equals("Put"))
                    {
                        //   System.out.println(" Parse Map option Call : " + singleOption.toString());
                        if (singleOption.getDateMill() < tempCallDate)
                        {
                            //    System.out.println(" Parse Map option Call : Date is less than tempDate " + singleOption.toString());
                            tempCallDate = singleOption.getDateMill();
                            callDate = singleOption.getDate();
                            tempCallOption = new Option(singleOption.getSymbol(),
                                    singleOption.getDate(),
                                    singleOption.getPrice(),
                                    singleOption.getType(),
                                    singleOption.getStrike(),
                                    singleOption.getBid() ,
                                    singleOption.getAsk(),
                                    singleOption.getVolume() ,
                                    singleOption.getOpenInterest());
                            tempCallOption.setDateMill(singleOption.getDateMill());
                            totalCallVolume = singleOption.getVolume();
                            totalCallOpenInterest =  singleOption.getOpenInterest();
                            totalCallStrike = singleOption.getStrike();
                            totalPrice =  singleOption.getPrice();
                            totalCallAsk = singleOption.getAsk();
                            totalCallBid =  singleOption.getBid();
                            totalCallCount = 1;
                            totalCallSale = (tempCallOption.getVolume() * totalCallAsk * 100);
                        }
                        else if (singleOption.getDateMill() == tempCallOption.getDateMill())
                        {
                            System.out.println(" Parse Map option Put : Date is is same as tempDate " + singleOption.toString());

                            totalCallVolume = singleOption.getVolume() + totalCallVolume;
                            totalCallOpenInterest =  singleOption.getOpenInterest() + totalCallOpenInterest;
                            totalCallStrike = singleOption.getStrike() + totalCallStrike;
                            totalPrice =  singleOption.getPrice() + totalPrice;
                            totalCallAsk = singleOption.getAsk() + totalCallAsk;
                            totalCallBid =  singleOption.getBid() + totalCallBid;
                            totalCallSale = (singleOption.getVolume() * totalCallAsk * 100) + totalCallSale;
                            totalCallCount++;

                        }
                    }
                    else
                    {
                        System.out.println(" Undefined option : " );
                    }
                }    //end for options loop

                if (( tempCallOption != null) && !isThereCallOptionBeforePutOption(tempCallOption))
                {
                    totalCallStrike = totalCallStrike / totalCallCount;
                    totalPrice =  totalPrice / totalCallCount;
                    totalCallAsk = totalCallAsk / totalCallCount;
                    totalCallBid =  totalCallBid / totalCallCount;
                    Option option = new Option(symbol,
                            callDate,
                            totalPrice,
                            type,
                            totalCallStrike,
                            totalCallBid ,
                            totalCallAsk,
                            totalCallVolume ,
                            totalCallOpenInterest);
                    double breakEven = totalCallStrike + totalCallAsk ;
                    int purchase = (int) (option.getVolume() * totalCallAsk * 100);

                    //   int purchase = (int) totalCallSale;
                    System.out.println( symbol + " totalCallVolume "  + totalCallVolume + " totalCallOpenInterest " + totalCallOpenInterest +
                            " totalCallAsk => " + totalCallAsk + " volume => " + option.getVolume() + " purchase => " + purchase );
                    option.setPurchase(purchase);
                    option.setBreakEven(breakEven);

                    // option.setCallToPutPerc(totalCallCount);
                    option.setCallToPutPerc(option.getPrice() /breakEven * 100);


                    finalPutOptionList.add(option);
                }
            }
        }
        System.out.println("final put option report %%%%%%%%%");
        if (finalPutOptionList != null && finalPutOptionList.size() > 0)
        {
            for (Option singleOption: finalOptionList)
            {
                System.out.println(singleOption.toString());
            }
        }
        System.out.println(" end generateOptionsReport...");
    }



    public void processOptions( )
    {
        previousOptionsMap.clear();
        previousOptionsMap.putAll(optionsMap);
        optionTypeComboBox.setSelectedItem("Call");
        finalOptionList.clear();
        optionsMap.clear();
        putOptionsMap.clear();
        indexMap.clear();
        int count = 0;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Calendar calCurrentTime = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 60);
        Date date1 = cal.getTime();
        String inActiveDate = null;
        try
        {
            inActiveDate = sdf.format(date1);
            System.out.println(inActiveDate );
        }
        catch (Exception e1)
        {
            e1.printStackTrace();

        }
        System.out.println("Calendar => " + cal.toString());
        Calendar cal2 = Calendar.getInstance();
        String symbol = null;
        String date = null;
        Date newDate = null;
        String volumeStr = null;
        String strikeStr =  null;
        String priceStr =  null;
        String type = null;
        String openInterestStr =  null;
        String askStr =  null;
        String bidStr =  null;
        int volume = 0;
        int openInterest =  0;
        double strike =  0;
        double price =  0;
        double ask =  0;
        double bid =  0;
        double factor = 0;
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        dateString = simpleDateFormat.format(new Date());
        String fileName1 = csvFile1 + dateString + ".csv";
        String fileName2 = csvFile2 + dateString + ".csv";
        System.out.println("reading  file name => " + fileName1);
        String fileName = fileName2;
        File file = new File(fileName1);
        if (file.exists())
        {
            fileName = fileName1;
        }
        title = fileName ;
        int lineCount = 1;
        //    String fileName = getLatestFileName(dateString);
        //    System.out.println("reading  file name => " + fileName);

        try {
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {

                if (line.contains(";"))
                {
                    cvsSplitBy = ";";
                }
                // use comma as separator
                String[] options = line.split(cvsSplitBy);

                if (lineCount < 5)
                {
                    System.out.println("Riahi 1085 reading line => " + line);
                    count++;
                }

                if ( options != null && options.length > 10 && (line.contains("Price") && line.contains("Symbol")))
                {
                    for (int i = 0; i < options.length; i++)
                    {
                        String key = options[i].replaceAll("\"", "");
                        if (key.contains("Date"))
                        {
                            key = "Exp Date";
                        }
                        else if (key.contains("Open"))
                        {
                            key = "Open Int";
                        }
                        indexMap.put(key, i);
                        System.out.println(" map index => " + i + " key => "+ key);
                    }
                }
                else if ( options != null && options.length > 10 && (line.contains("Call") || line.contains("Put")))
                {
                    //BA,252.47,Put,235,06/18/21,78,11.25,11.35,11.45,11.35,681,446,1.53,41.99%,"13:59 ET"

                    //Symbol	Price	Type	Strike	Exp Date	DTE	Bid	Midpoint	Ask	Last	Volume	Open Int
                    //as of 12/9/20 Symbol	Symbol	Price	Type	Type	Strike	Exp Date	DTE	Bid	Midpoint	Ask	Last	Volume	Open Int	Vol/OI	IV	Last Trade
                    //   0                         1      2       3       4         5       6             7       8        9              10     11       12       13
                    try {
                        // trying to parse current date here
                        // newDate = dateFormatter.parse(cal.getTime().toString()); //throws exception


                        symbol = options[indexMap.get("Symbol")];
                        priceStr = options[indexMap.get("Price")];
                        type = options[indexMap.get("Type")];
                        strikeStr = options[indexMap.get("Strike")];
                        date = options[indexMap.get("Exp Date")];
                        bidStr = options[indexMap.get("Bid")];
                        askStr = options[indexMap.get("Ask")];
                        volumeStr  = options[indexMap.get("Volume")];
                        openInterestStr = options[indexMap.get("Open Int")];



                        System.out.println(" exp date => " + date + "  raw date => " + date);

                        newDate = sdf.parse(date); //no exception
                        newDate.setHours(0);
                        newDate.setMinutes(0);
                        newDate.setSeconds(0);



                        volume = Integer.parseInt(volumeStr);
                        openInterest = Integer.parseInt(openInterestStr);

                        strike = Double.parseDouble(strikeStr);
                        price =  Double.parseDouble(priceStr);

                        factor = openInterest * 2.5;
                        cal2.setTime(newDate);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(newDate);

                        Option option1 = new Option(symbol,
                                date,
                                price,
                                type,
                                strike,
                                bid ,
                                ask,
                                volume ,
                                openInterest);
                        option1.setDateMill(cal2.getTimeInMillis());


                        String cal2String = dateFormat.format(cal2.getTime());
                        String currTimeString = dateFormat.format(calCurrentTime.getTime());
                        String calString = dateFormat.format(cal.getTime());
                        String calendarString = dateFormat.format(calendar.getTime());

                        long cal22 = newDate.getTime();
                        System.out.println(" cal2 => " + cal2String + " calCurrentTime -> " + currTimeString + " cal => " + calString + " calendarString => " + calendarString);
                        if  (  (cal22 > calCurrentTime.getTimeInMillis()) &&
                                (cal22 < cal.getTimeInMillis()))
                        {
                            ask = Double.parseDouble(askStr);
                            bid =  Double.parseDouble(bidStr);
                            Option option = new Option(symbol,
                                    date,
                                    price,
                                    type,
                                    strike,
                                    bid ,
                                    ask,
                                    volume ,
                                    openInterest);
                            option.setDateMill(cal2.getTimeInMillis());

                            //         System.out.println("Roajo type=> " + type+ " line => "+ line);

                            if (type.equals("Call"))
                            {
                                if(optionsMap.containsKey(symbol))
                                {
                                    ArrayList<Option>  multiOption = optionsMap.get(symbol);
                                    multiOption.add(option);

                                }
                                else
                                {
                                    ArrayList<Option> multiOption = new ArrayList<Option>();
                                    multiOption.add(option);
                                    optionsMap.put(symbol,multiOption);
                                }
                            }
                            else  if (type.equals("Put"))
                            {
                                if(putOptionsMap.containsKey(symbol))
                                {
                                    ArrayList<Option>  multiOption = putOptionsMap.get(symbol);
                                    multiOption.add(option);

                                }
                                else
                                {
                                    ArrayList<Option> multiOption = new ArrayList<Option>();
                                    multiOption.add(option);
                                    putOptionsMap.put(symbol,multiOption);
                                }
                            }
                            else
                            {
                                System.out.println("UNDEFINED TYPE.... => " + line);
                            }
                            //    System.out.println("Adding this line line => " + line);


                        }
                        else
                        {
                            System.out.println("Riahi The time doesn't meet the criteria ");

                        }

                    }
                    catch (ParseException e) {
                        e.printStackTrace(System.out);
                    }



                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void processOptionsHistory(String theFileName )
    {
        previousOptionsMap.clear();
        previousOptionsMap.putAll(optionsMap);
        finalOptionList.clear();
        finalPutOptionList.clear();
        indexMap.clear();
        optionsMap.clear();
        putOptionsMap.clear();
        int count = 0;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Calendar cal2 = Calendar.getInstance();
        String symbol = null;
        String date = null;
        Date newDate = null;
        String volumeStr = null;
        String strikeStr =  null;
        String priceStr =  null;
        String type = null;
        String openInterestStr =  null;
        String askStr =  null;
        String bidStr =  null;
        int volume = 0;
        int openInterest =  0;
        double strike =  0;
        double price =  0;
        double ask =  0;
        double bid =  0;
        double factor = 0;
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        dateString = simpleDateFormat.format(new Date());
        String  fileName = theFileName;

        int lineCount = 1;
        //    String fileName = getLatestFileName(dateString);
        //    System.out.println("reading  file name => " + fileName);
        //  Symbol  Ratio   Date    Price   Strike  Bid     Ask     Volume  OI      BreakEven       Purchase$       Option  PriceToBrkPerc  New
        type = "Call";
        try {
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {

                if (line.contains("PriceToBrkPerc"))
                {
                    continue;
                }

                // use comma as separator
                String[] options = line.split(cvsSplitBy);

                if (lineCount < 6)
                {
                    System.out.println("reading line => " + line);
                    count++;
                }

                if ( options != null && options.length > 10 && (line.contains("Price") && line.contains("Symbol")))
                {
                    for (int i = 0; i < options.length; i++)
                    {
                        String key = options[i].replaceAll("\"", "");
                        if (key.contains("Date"))
                        {
                            key = "Exp Date";
                        }
                        else if (key.contains("Open"))
                        {
                            key = "Open Int";
                        }
                        indexMap.put(key, i);
                        System.out.println(" map index => " + i + " key => "+ key);
                    }
                }
                if ( options != null && options.length > 10 )
                {
                    //  Symbol  Ratio   Date    Price   Strike  Bid     Ask     Volume  OI      BreakEven       Purchase$       Option  PriceToBrkPerc  New                     1      2       3       4         5       6             7       8        9              10     11       12       13
                    try {
                        // trying to parse current date here
                        // newDate = dateFormatter.parse(cal.getTime().toString()); //throws exception
                        symbol = options[indexMap.get("Symbol")];
                        priceStr = options[indexMap.get("Price")];
                        type = options[indexMap.get("Type")];
                        strikeStr = options[indexMap.get("Strike")];
                        date = options[indexMap.get("Exp Date")];
                        bidStr = options[indexMap.get("Bid")];
                        askStr = options[indexMap.get("Ask")];
                        volumeStr  = options[indexMap.get("Volume")];
                        openInterestStr = options[indexMap.get("Open Int")];
                        newDate = sdf.parse(date); //no exception
                        newDate.setHours(0);
                        newDate.setMinutes(0);
                        newDate.setSeconds(0);
                        volume = Integer.parseInt(volumeStr);
                        openInterest = Integer.parseInt(openInterestStr);

                        strike = Double.parseDouble(strikeStr);
                        price =  Double.parseDouble(priceStr);

                        factor = openInterest * 2.5;
                        cal2.setTime(newDate);
                        //  if ((volume > factor) &&
                        //  if    ( (strike > price) &&

                        ask = Double.parseDouble(askStr);
                        bid =  Double.parseDouble(bidStr);
                        Option option = new Option(symbol,
                                date,
                                price,
                                type,
                                strike,
                                bid ,
                                ask,
                                volume ,
                                openInterest);
                        option.setDateMill(cal2.getTimeInMillis());

                        if (type.equals("Call"))
                        {
                            if(optionsMap.containsKey(symbol))
                            {
                                ArrayList<Option>  multiOption = optionsMap.get(symbol);
                                multiOption.add(option);

                            }
                            else
                            {
                                ArrayList<Option> multiOption = new ArrayList<Option>();
                                multiOption.add(option);
                                optionsMap.put(symbol,multiOption);
                            }
                        }
                        else  if (type.equals("Put"))
                        {
                            if(putOptionsMap.containsKey(symbol))
                            {
                                ArrayList<Option>  multiOption = putOptionsMap.get(symbol);
                                multiOption.add(option);

                            }
                            else
                            {
                                ArrayList<Option> multiOption = new ArrayList<Option>();
                                multiOption.add(option);
                                putOptionsMap.put(symbol,multiOption);
                            }
                        }
                        else
                        {
                            System.out.println("UNDEFINED TYPE.... => " + line);
                        }
                        System.out.println("Adding this line line => " + line);

                    }
                    catch (ParseException e) {
                        e.printStackTrace(System.out);
                    }



                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    public String getLatestFileName(String theFileDate)
    {
        System.out.println("begin main");


        String target_file ;  // fileThatYouWantToFilter
        File folderToScan = new File(SCAN_DIR);
        File[] listOfFiles = folderToScan.listFiles();

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                target_file = listOfFiles[i].getName();
                if (((target_file.startsWith("unusual-")) &&
                        (target_file.endsWith(theFileDate + ".csv"))))

                {
                    System.out.println("found" + " " + target_file);
                    return target_file;
                }
            }
        }
        return null;

    }

    class CustomRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setForeground(Color.blue);
            return c;
        }
    }


    public File determineLatestFile(String theDateAndExtension)
    {
        File fl = new File(SCAN_DIR);
        File choice = null;
        if (fl.listFiles().length>0)
        {
            File[] files = fl.listFiles(new FileFilter()
            {
                public boolean accept(File file)
                {
                    return file.isFile();
                }
            });
            long lastMod = Long.MIN_VALUE;

            for (File file : files)
            {
                if (file.getName().endsWith( theDateAndExtension))
                {
                    System.out.println("loop file name => " + file.getName() + " modified => " +file.lastModified());
                    if ((file.lastModified() > lastMod) &&
                            (file.getName().endsWith( theDateAndExtension)))

                    {
                        choice = file;
                        lastMod = file.lastModified();
                    }
                }
            }
        }
        System.out.println("file name => " + choice.getName());
        return choice;
    }



    public void processWallStreetFile()
    {
        previousOptionsMap.putAll(optionsMap);
        finalOptionList.clear();
        optionsMap.clear();
        int count = 0;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        Calendar calCurrentTime = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 60);
        Date date1 = cal.getTime();
        String inActiveDate = null;
        try
        {
            inActiveDate = sdf.format(date1);
            System.out.println(inActiveDate );
        }
        catch (Exception e1)
        {
            e1.printStackTrace();

        }
        System.out.println("Calendar => " + cal.toString());
        Calendar cal2 = Calendar.getInstance();
        String symbol = null;
        String date = null;
        Date newDate = null;
        String volumeStr = null;
        String strikeStr =  null;
        String priceStr =  null;
        String type = null;
        String openInterestStr =  null;
        String askStr =  null;
        String bidStr =  null;
        int volume = 0;
        int openInterest =  0;
        double strike =  0;
        double price =  0;
        double ask =  0;
        double bid =  0;
        double factor = 0;
        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        dateString = simpleDateFormat.format(new Date());


        String fileName = WALL_STREET_FILE;
        File file = new File(fileName);

        int lineCount = 1;
        //    String fileName = getLatestFileName(dateString);
        //    System.out.println("reading  file name => " + fileName);

        try {
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null)
            {

                if (line.contains(";"))
                {
                    cvsSplitBy = ";";
                }
                // use comma as separator
                String[] options = line.split(cvsSplitBy);

                if (lineCount < 1000)
                {
                    System.out.println("reading line => " + line);
                    count++;
                }
                if ((line.contains(">>Overweight")) || (line.contains(">>Outperform")))
                {
                    System.out.println("$$$$$Riahi Special Line  => " + line + "\n");
                    count++;
                }



                if ( options != null && options.length > 10 && (line.contains("PriceXX") && line.contains("SymbolXX")))
                {
                    for (int i = 0; i < options.length; i++)
                    {
                        String key = options[i].replaceAll("\"", "");
                        if (key.contains("Date"))
                        {
                            key = "Exp Date";
                        }
                        else if (key.contains("Open"))
                        {
                            key = "Open Int";
                        }
                        indexMap.put(key, i);
                        System.out.println(" map index => " + i + " key => "+ key);
                    }
                }
                else if ( options != null && options.length > 10 && (line.contains("CallXX") || line.contains("PutXX")))
                {
                    //Symbol  Price   Type    Strike  Exp Date        DTE     Bid     Midpoint        Ask     Last    Volume  Open Int
                    //as of 12/9/20 Symbol      Symbol  Price   Type    Type    Strike  Exp Date        DTE     Bid     Midpoint        Ask     Last    Volume  Open Int        Vol/OI  IV      Last Trade
                    //   0                         1      2       3       4         5       6             7       8        9              10     11       12       13
                    try {
                        // trying to parse current date here
                        // newDate = dateFormatter.parse(cal.getTime().toString()); //throws exception


                        symbol = options[indexMap.get("Symbol")];
                        priceStr = options[indexMap.get("Price")];
                        type = options[indexMap.get("Type")];
                        strikeStr = options[indexMap.get("Strike")];
                        date = options[indexMap.get("Exp Date")];
                        bidStr = options[indexMap.get("Bid")];
                        askStr = options[indexMap.get("Ask")];
                        volumeStr  = options[indexMap.get("Volume")];
                        openInterestStr = options[indexMap.get("Open Int")];

                   /*
                    symbol = options[0];
                    priceStr = options[1];
                     type = options[2];
                     strikeStr = options[3];
                     date = options[4];
                     bidStr = options[6];
                     askStr = options[8];
                     volumeStr  = options[10];


                   symbol = options[1];
                   priceStr = options[2];
                    type = options[4];
                    strikeStr = options[5];
                    date = options[6];
                    bidStr = options[8];
                    askStr = options[10];
                    volumeStr  = options[12];
                    openInterestStr = options[13];
                   */
                        newDate = sdf.parse(date); //no exception
                        newDate.setHours(0);
                        newDate.setMinutes(0);
                        newDate.setSeconds(0);
                        volume = Integer.parseInt(volumeStr);
                        openInterest = Integer.parseInt(openInterestStr);

                        strike = Double.parseDouble(strikeStr);
                        price =  Double.parseDouble(priceStr);

                        factor = openInterest * 2.5;
                        cal2.setTime(newDate);
                        //  if ((volume > factor) &&
                        //  if    ( (strike > price) &&
                        if  (  (cal2.getTimeInMillis() > calCurrentTime.getTimeInMillis()) &&
                                (cal2.getTimeInMillis() < cal.getTimeInMillis()))
                        {

                            ask = Double.parseDouble(askStr);
                            bid =  Double.parseDouble(bidStr);
                            Option option = new Option(symbol,
                                    date,
                                    price,
                                    type,
                                    strike,
                                    bid ,
                                    ask,
                                    volume ,
                                    openInterest);
                            option.setDateMill(cal2.getTimeInMillis());

                            if (type.equals("Call"))
                            {
                                if(optionsMap.containsKey(symbol))
                                {
                                    ArrayList<Option>  multiOption = optionsMap.get(symbol);
                                    multiOption.add(option);

                                }
                                else
                                {
                                    ArrayList<Option> multiOption = new ArrayList<Option>();
                                    multiOption.add(option);
                                    optionsMap.put(symbol,multiOption);
                                }
                            }
                            else  if (type.equals("Put"))
                            {
                                if(putOptionsMap.containsKey(symbol))
                                {
                                    ArrayList<Option>  multiOption = putOptionsMap.get(symbol);
                                    multiOption.add(option);

                                }
                                else
                                {
                                    ArrayList<Option> multiOption = new ArrayList<Option>();
                                    multiOption.add(option);
                                    putOptionsMap.put(symbol,multiOption);
                                }
                            }
                            else
                            {
                                System.out.println("UNDEFINED TYPE.... => " + line);
                            }
                            System.out.println("Adding this line line => " + line);


                        }

                    }
                    catch (ParseException e) {
                        e.printStackTrace(System.out);
                    }



                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static void main(String[] args)
    {
        System.out.println("begin main");
        UnusualOptionMain  unusualOption = new UnusualOptionMain();
        unusualOption.createJTable();
        System.out.println("end main  ...that's all folks");
    }

}