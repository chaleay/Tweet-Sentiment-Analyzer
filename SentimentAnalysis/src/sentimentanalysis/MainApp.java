package sentimentanalysis;

import sun.java2d.pipe.SpanShapeRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
  Main Application for Assignment 2
  @author vangelis
  @author jelena
  @author junye
 */
public class MainApp{


    // Used to read from System's standard input
    private static final Scanner CONSOLEINPUT = new Scanner(System.in);

    public static TweetHandler HANDLER = new TweetHandler();

    private static final Logger logger = Logger.getLogger(TweetHandler.class.getName());

    private static FileHandler fh;

    private static String [] header = {"Target", "ID", "Date", "Flag", "user", "Text"};
    /**
     * Main method demonstrates how to use Stanford NLP library classifier.
     * @param args the command line arguments
     */

    /**
     * Main class that handles gui implementation
     * @param args
     * @throws IOException
     */
    public static void main(String[] args)  throws IOException {

        //adjusting settings for logger first
        Logger logger = Logger.getLogger(TweetHandler.class.getName());
        fh = new FileHandler("TweetHandler.log");
        logger.addHandler(fh);
        logger.setLevel(Level.ALL);


        //ALL AVAILABLE BUTTONS IN THE PROGRAM --
        JButton viewTemp = new JButton("View tweetBuffer");
        JButton viewDataBase = new JButton("View Database");
        JButton loadTweets = new JButton("Load Tweets from CSV");
        JButton addTweetsToDB = new JButton("Add tweets to database");
        JButton deleteTweet = new JButton("Delete tweet from database");
        JButton classifyTweets = new JButton("Classify Tweets");
        JButton changeTweet = new JButton("Change tweet label");
        JButton searchSubstring = new JButton("Search by Substring...");
        JButton searchUser= new JButton("Search by user...");
        JButton searchFlag = new JButton("Search by flag...");
        JButton searchDate = new JButton("Search by date...");

        //creating our gui template
        JFrame frame = new JFrame("Tweet Sentiment Analyzer");

            //window paneel

        frame.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        GridLayout newLayout = new GridLayout(0, 5);
        labelPanel.setPreferredSize(new Dimension(0, 50));
        labelPanel.setLayout(newLayout);

        JLabel mainLabel = new JLabel("      Tweet Menu");
        JLabel logLabel = new JLabel("");
        labelPanel.add(mainLabel);
        labelPanel.add(logLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(16, 0));


        //ADD BUTTONS TO PANELL
        buttonPanel.add((loadTweets));
        buttonPanel.add(viewTemp);
        buttonPanel.add(viewDataBase);
        buttonPanel.add(addTweetsToDB);
        buttonPanel.add(deleteTweet);
        buttonPanel.add(classifyTweets);
        buttonPanel.add(changeTweet);
        buttonPanel.add(searchSubstring);
        buttonPanel.add(searchDate);
        buttonPanel.add(searchUser);
        buttonPanel.add(searchFlag);

            //CENTER PANEL -- LOG PANEL
        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.setPreferredSize(new Dimension(600, 800));



        JTextArea loggingArea = new JTextArea();
        loggingArea.setEditable(false);

        JScrollPane logScroll = new JScrollPane(loggingArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        frame.add(labelPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(logPanel, BorderLayout.CENTER);


        logPanel.add(logScroll);

        /**
         * main class that handles listneing for user input with buttons on screen
         */
        class ButtonListener implements ActionListener {
            /**
             * main method that checks to see what the button is that was clicked
              * @param e
             */
            public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == loadTweets) {
                        logger.log(Level.INFO, "Clicked on Load tweets from csv.");
                        loggingArea.append("Proceeding to load tweets. Please Specify the directory.\n");
                        if (loadTweets()) {
                            logger.log(Level.INFO, "File read and opened successfully.");
                            loggingArea.append("File read and opened successfully.\n");
                        } else {
                            logger.log(Level.SEVERE, "File was not read. The file does not exist at that directory.");
                            loggingArea.append("Failed to load tweets from file.\n");
                        }

                    }
                    if (e.getSource() == viewTemp) {
                        logger.log(Level.INFO, "Clicked on viewTemp button.");
                        if (HANDLER.getTweetBuffer().size() != 0) {
                            JTablePop pop = new JTablePop("Tweet Buffer", HANDLER, true);
                            loggingArea.append("Tweet Table opened successful. Displaying " + HANDLER.getTweetBuffer().size() + " tweets in buffer.\n");
                            logger.log(Level.INFO, "Tweet table opened successfully. All tweets are displayed.");
                        } else {
                            loggingArea.append("No tweets in buffer. Try loading tweets into the database first.\n");
                            logger.log(Level.INFO, "Table of tweets not opened since buffer is empty.");
                        }
                    }
                    if (e.getSource() == viewDataBase) {
                        logger.log(Level.INFO, "Clicked on viewDatabase button.");
                        if (HANDLER.getTweetDB().size() != 0) {
                            JTablePop pop = new JTablePop("Tweet Database", HANDLER, false);
                            loggingArea.append("Tweet Table opened successful. Displaying " + HANDLER.getTweetDB().size() + " tweets in database.\n");
                            logger.log(Level.INFO, "Tweet database table opened successfully. All tweets are displayed.");
                        } else {
                            loggingArea.append("The database is empty. Trying loading tweets from the buffer onto the database first.\n");
                            logger.log(Level.INFO, "Table of tweets not opened since buffer is empty.");
                        }
                    }
                    if (e.getSource() == addTweetsToDB) {
                        logger.log(Level.INFO, "Clicked on addTweets button.");
                        int oldSize = HANDLER.getTweetDB().size();
                        if (HANDLER.getTweetBuffer().size() != 0) {
                            addTweets();
                            if (HANDLER.getTweetDB().size() != oldSize) {
                                loggingArea.append("Tweets successfully added to the database.\n");
                                loggingArea.append(HANDLER.getTweetDB().size() - oldSize + " tweets were added.\n");
                                logger.log(Level.INFO, HANDLER.getTweetDB().size() - oldSize + " tweets were added.");
                            } else {
                                loggingArea.append("No new tweets have been added to the Database.");
                                logger.log(Level.INFO, "No new tweets have been added to the database.");
                            }
                        } else {
                            logger.log(Level.INFO, "No tweets added, since buffer list is empty.");
                            loggingArea.append("No tweets added to database, since the current buffer is empty.\n");
                        }

                    }
                    if (e.getSource() == deleteTweet) {
                        String id = JOptionPane.showInputDialog("Enter the ID of the tweet you wish to delete.");
                        if(id.equals(""))
                            return;
                        try {

                            deleteTweet(Integer.parseInt(id));
                        } catch (NumberFormatException num) {
                            loggingArea.append("Warning! NumberFormatException. Numbers only, please.\b");
                        }
                    }
                    if (e.getSource() == classifyTweets) {
                        logger.log(Level.INFO, "Clicked on Classify Tweets");
                        if (HANDLER.getTweetBuffer().size() != 0) {
                            loggingArea.append("Classifying tweets, please wait...\n");
                            classifyTweets(loggingArea);
                        } else {
                            logger.log(Level.WARNING, "No tweets to classify.");
                            loggingArea.append("No tweets to classify.");
                        }
                        logger.log(Level.INFO, "Finished classifying all tweets.");
                    }
                    if (e.getSource() == changeTweet) {
                        JTextField xField = new JTextField(5);
                        JTextField yField = new JTextField(5);
                        JPanel myPanel = new JPanel();
                        myPanel.add(new JLabel("ID:"));
                        myPanel.add(xField);
                        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                        myPanel.add(new JLabel("Polarity:"));
                        myPanel.add(yField);
                        int result = JOptionPane.showConfirmDialog(frame, myPanel,
                                "Please Enter ID and Polarity", JOptionPane.OK_CANCEL_OPTION);
                        try {
                            changeTweet(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()), loggingArea);
                        } catch (NumberFormatException num) {
                            loggingArea.append("Warning! NumberFormatException. Numbers only, please.\n");
                        }
                    }
                    if (e.getSource() == searchSubstring) {
                        logger.log(Level.INFO, "Clicked on search by substring.");
                        String substring = JOptionPane.showInputDialog("Enter the substring to search by");
                        ArrayList<AbstractTweet> temp = HANDLER.searchBySubstring(substring);
                        if (!temp.isEmpty()) {
                            loggingArea.append(temp.size() + " search results were found.\b");
                            JTablePop tab = new JTablePop("Substring Search Results", temp);
                        } else {
                            loggingArea.append("No mnatches were found.\n");
                        }

                    }
                    if (e.getSource() == searchFlag) {
                        logger.log(Level.INFO, "Clicked on search by flag.");
                        String flag= JOptionPane.showInputDialog("Enter the flag to search by");
                        ArrayList<AbstractTweet> temp = HANDLER.searchByFlag(flag);
                        if (!temp.isEmpty()) {
                            loggingArea.append(temp.size() + " search results were found.\b");
                            JTablePop tab = new JTablePop("Flag Search Results", temp);
                        } else {
                            loggingArea.append("No mnatches were found.\n");
                        }


                    }
                    if (e.getSource() == searchUser) {
                        logger.log(Level.INFO, "Clicked on search by user.");
                        String User= JOptionPane.showInputDialog("Enter the user to search by");
                        ArrayList<AbstractTweet> temp = HANDLER.searchByUser(User);
                        if (!temp.isEmpty()) {
                            loggingArea.append(temp.size() + " search results were found.\n");
                            JTablePop tab = new JTablePop("User Search Results", temp);
                        } else {
                            loggingArea.append("No mnatches were found.\n");
                        }


                    }
                    if (e.getSource() == searchDate) {
                    logger.log(Level.INFO, "Clicked on search by Date.");
                    String date = JOptionPane.showInputDialog("Enter the date to search by");
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                    ArrayList<AbstractTweet> temp;
                    try {
                        Date dateInput = formatter.parse(date);
                        temp = HANDLER.searchByDate(dateInput);
                        if (!temp.isEmpty()) {
                            loggingArea.append(temp.size() + " search results were found.\b");
                            JTablePop tab = new JTablePop("User Search Results", temp);
                        }
                    }
                        catch (ParseException P) {
                        P.printStackTrace();
                        loggingArea.append("Invalid Date format entered. Please try again.\b");
                        logger.log(Level.WARNING, "Parsing failed on date. Try again...");
                        }
                    }
                }
            }


            //Close Operation - Save DB or no
        /**
         * used to detect when the user is quitting the program.
         *
         */
        WindowAdapter closer = new WindowAdapter()
            {
                @Override
                /**
                 * used to detect when the user is about to quit the program. this code will prompt the user to save their database
                 */
                public void windowClosing(WindowEvent e)
                {
                    int exit = JOptionPane.showConfirmDialog(frame, "Save changes to Database before exiting?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
                    if(JOptionPane.YES_OPTION == exit) {
                        HANDLER.saveSerialDB();
                        System.exit(1);
                    }
                    else if(JOptionPane.NO_OPTION == exit) {
                        System.exit(1);
                    }
                }
            };

            loadTweets.addActionListener(new ButtonListener());
            viewTemp.addActionListener(new ButtonListener());
            viewDataBase.addActionListener(new ButtonListener());
            addTweetsToDB.addActionListener(new ButtonListener());
            deleteTweet.addActionListener(new ButtonListener());
            classifyTweets.addActionListener(new ButtonListener());
            changeTweet.addActionListener(new ButtonListener());
            searchDate.addActionListener(new ButtonListener());
            searchFlag.addActionListener(new ButtonListener());
            searchSubstring.addActionListener(new ButtonListener());
            searchUser.addActionListener(new ButtonListener());

            frame.pack();
            frame.addWindowListener(closer);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //intial check to see if there is a DB
            loggingArea.append("Checking to see if there is a preexisting Database...");
            if(HANDLER.loadSerialDB())
            {
                loggingArea.append("Loaded " +HANDLER.getTweetDB().size() + " tweets from existing database.\n");
                logger.log(Level.INFO, "Tweets loaded from DB.");
            }
            else{
                loggingArea.append("Database not detected. A new one will be created upon closing.\n");
                logger.log(Level.INFO, "Database not detected. Creating one...");
            }

    }

    /**
     * Method 1: load new tweet text file.
     *
     */
    public static boolean loadTweets()
    {

        String path = JOptionPane.showInputDialog("CSV Path File", "Enter the exact file path of your CSV");
        if(path.equals(""))
            return false;
        HANDLER.setTweetBuffer(HANDLER.loadTweetsFromText(path));
        if(HANDLER.getTweetBuffer() == null)
            return false;
        else return true;

    }

    /**
     * Method 2: classify tweets using NLP library and report accuracy.
     *
     */
    public static void classifyTweets(JTextArea logArea) {
        int countCorrect = 0;
        int countWrong = 0;

        for (AbstractTweet t : HANDLER.getTweetBuffer()) {
            t.setPredictedPolarity(HANDLER.classifyTweet(t));
            logArea.append("Target: " + t.getTarget() + "\n");
            logArea.append("Prediction: " + t.getPredictedPolarity() + "\n");
            logArea.append("==================================================" + "\n");
            if (t.getPredictedPolarity() == t.getTarget()) {
                countCorrect++;
            } else {
                countWrong++;
            }
        }

        logArea.append("Correct classified tweets: " + countCorrect + "\n");
        logArea.append("Incorrect classified tweets: " + countWrong + "\n" );
        double correctRate = (double)countCorrect / ((double)countWrong + (double)countCorrect) * 100;
        logArea.append("Correct prediction rate: " + String.format("%.2f", correctRate) + "%" + "\n");

    }

    /**
     * Method 3: manually change tweet class label.
     *
     */
    public static void changeTweet(int id, int newPolarity, JTextArea logArea) {

        int targetIndex = -1;
        int i = 0;
        for (AbstractTweet tweet : HANDLER.getTweetBuffer()) {
            if (tweet.getId() == id)
                targetIndex = i;
            i++;
        }

        if(targetIndex == -1 || HANDLER.getTweetBuffer().size() == 0)
        {
           logArea.append("No Tweet with that ID found. \n");
           logger.log(Level.INFO, "No tweet with that ID found.");
            return;
        }

        logArea.append("Current predicted polarity: " + HANDLER.getTweetBuffer().get(targetIndex).getPredictedPolarity() + "\n");
        if (newPolarity != 0 && newPolarity != 2 && newPolarity != 4) {
            logArea.append("Invalid input. Data will not be changed.\n");
            return;
        }
        HANDLER.getTweetBuffer().get(targetIndex).setPredictedPolarity(newPolarity);
        logArea.append("Predicted polarity updated.\n");

    }

    /**
     * Method 4: add new tweets to database.
     *
     */
    public static void addTweets() {
        // Add all tweets in memory into database list
        HANDLER.addTweetsToDB(HANDLER.getTweetBuffer());
        logger.log(Level.FINE,"Tweet buffer is now empty.");

        //String menu = "\nDo you want to save the database to your file?\n\n"
          //      + "\t 1. yes.\n"
          //      + "\t 2. no.\n";
         /*
        //switch (selection) {
           // case "1":
                // 1. save.
                HANDLER.saveSerialDB();
                break;
            default:
                // Others. do not save.
                System.out.println("Data will not be saved to the file now.");
                break;
        */
        }

    /**
     * Method 5: delete tweet from database (given its id).
     *
     */
    public static void deleteTweet(int id) {

        HANDLER.deleteTweet(id);

    }

    /**
     * Method 6: search tweets by user, date, flag, or a matching substring.
     @return list of tweets that match the search
     */
    public static List<AbstractTweet> searchTweet() {
        List<AbstractTweet> resultList = new ArrayList<AbstractTweet>();

        String menu = "\nThis function will search both data list. Choose one of the following functions:\n\n"
                + "\t 1. Search by user.\n"
                + "\t 2. Search by date.\n"
                + "\t 3. Search by flag.\n"
                + "\t 4. Search by substring\n";
        System.out.println(menu);
        String selection = MainApp.CONSOLEINPUT.nextLine();
        String inputInfo;
        switch (selection) {
            case "1":
                // 1. search by user.
                System.out.println("Please input the user name.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                resultList = HANDLER.searchByUser(inputInfo);
                break;
            case "2":
                // 2. search by date.
                System.out.println("Please input the date.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                try {
                    Date dateInput = formatter.parse(inputInfo);
                    resultList = HANDLER.searchByDate(dateInput);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "3":
                // 3. search by flag.
                System.out.println("Please input the flag.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                resultList = HANDLER.searchByFlag(inputInfo);
                break;
            case "4":
                // 4. search by substring.
                System.out.println("Please input the substring.");
                inputInfo = MainApp.CONSOLEINPUT.nextLine();
                resultList = HANDLER.searchBySubstring(inputInfo);
                break;
            default:
                System.out.println("That is not a recognized command.");
                break;
        }

        return resultList;
    }

    /**
     * Print out the formatted table for list
     @param list
     */
    public static void printList(TextArea logArea, List<AbstractTweet> list) {
        String line = "----------------------------------------------------------------------------------"
                + "----------------------------------------------------------------------------------";
        String information = "| ";
        information = information + String.format("%6s", "Target") + " | ";
        information = information + String.format("%6s", "Class") + " | ";
        information = information + String.format("%6s", "ID") + " | ";
        information = information + String.format("%30s", "Date") + " | ";
        information = information + String.format("%10s", "Flag") + " | ";
        information = information + String.format("%15s", "User") + " | ";
        information = information + String.format("%70s", "Text") + " |";

        logArea.append(line + "\n");
        logArea.append(information + "\n");
        logArea.append(line + "\n");
        for (AbstractTweet t : list) {
            System.out.println(t);
            System.out.println(line);
        }
    }
}
