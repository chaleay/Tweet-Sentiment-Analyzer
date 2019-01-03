package sentimentanalysis;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is used for the Jtable that appears with tempbuffer as well as the database.
 * It is also used for all arraylist returned by the search functions as well, to format them and make them easy to read
 */
public class JTablePop extends JFrame{

    private static String [] header = {"Target", "ID", "Date", "Flag", "user", "Text", "PredPolarity"};

    /**
     * Constructor for the JTablePop GUI class. Used for constructing a table of either buffer or db input
     * @param title
     * @param HANDLER
     * @param temp
     */
    public JTablePop(String title, TweetHandler HANDLER, boolean temp)

    {
        super(title);
        setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DefaultTableModel tableModel = new DefaultTableModel(header, 0);
        JTable buffer = new JTable(tableModel);
        if(temp) {
            for (AbstractTweet tweet : HANDLER.getTweetBuffer()) {
                int predictedPolarity = tweet.getPredictedPolarity();
                int target = tweet.getTarget();
                int id = tweet.getId();
                Date date = tweet.getDate();
                String flag = tweet.getFlag();
                String user = tweet.getUser();
                String text = tweet.getText();

                Object data[] = {target, id, date, flag, user, text, predictedPolarity};
                tableModel.addRow(data);
            }
        }
        else{
            for (AbstractTweet tweet : HANDLER.getTweetDB()) {
                int predictedPolarity = tweet.getPredictedPolarity();
                int target = tweet.getTarget();
                int id = tweet.getId();
                Date date = tweet.getDate();
                String flag = tweet.getFlag();
                String user = tweet.getUser();
                String text = tweet.getText();

                Object data[] = {target, id, date, flag, user, text, predictedPolarity};
                tableModel.addRow(data);
            }
        }
        buffer.setFillsViewportHeight(true);
        add(buffer);
        JScrollPane scroll = new JScrollPane(buffer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVisible(true);
        add(scroll);
    }


    /**
     * used for constructing a table from any ArrayList input. Used for our search functions in TweetHandler.
     * @param title
     * @param myList
     */
    public JTablePop(String title, ArrayList<AbstractTweet> myList)

    {
        super(title);
        setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        DefaultTableModel tableModel = new DefaultTableModel(header, 0);
        JTable buffer = new JTable(tableModel);
            for (AbstractTweet tweet : myList)
            {
                int predictedPolarity = tweet.getPredictedPolarity();
                int target = tweet.getTarget();
                int id = tweet.getId();
                Date date = tweet.getDate();
                String flag = tweet.getFlag();
                String user = tweet.getUser();
                String text = tweet.getText();

                Object data[] = {target, id, date, flag, user, text, predictedPolarity};
                tableModel.addRow(data);
            }

        buffer.setFillsViewportHeight(true);
        add(buffer);
        JScrollPane scroll = new JScrollPane(buffer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVisible(true);
        add(scroll);
    }

}

