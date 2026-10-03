import javax.swing.*;
import java.awt.*;

/**
 * seating chart panel
 *
 * displays a simple visual representation of restaurant tables
 * available tables shown in green using JLabels in a grid
 *
 * @author Krishna Vijay
 * @version Dec 6th, 2025
 */
public class SeatingChartPanel extends JPanel implements SeatingChartPanelInterface {

    private JPanel tableGrid;
    private JLabel messageLabel;

    public SeatingChartPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        messageLabel = new JLabel("Search to see available tables", SwingConstants.CENTER);
        messageLabel.setForeground(Color.GRAY);

        tableGrid = new JPanel();
        tableGrid.setBackground(Color.WHITE);

        add(messageLabel, BorderLayout.CENTER);
    }

    @Override
    public void setAvailableTables(String[] tables) {
        removeAll();

        if (tables == null || tables.length == 0 || (tables.length == 1 && tables[0].isEmpty())) {
            messageLabel.setText("No tables available");
            add(messageLabel, BorderLayout.CENTER);
        } else {
            // create grid of table labels
            int cols = 3;
            int rows = (tables.length + cols - 1) / cols;
            tableGrid = new JPanel(new GridLayout(rows, cols, 5, 5));
            tableGrid.setBackground(Color.WHITE);
            tableGrid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            for (String table : tables) {
                if (table != null && !table.isEmpty()) {
                    JLabel tableLabel = createTableLabel(table);
                    tableGrid.add(tableLabel);
                }
            }

            add(tableGrid, BorderLayout.CENTER);

            // legend at bottom
            JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT));
            legend.setBackground(Color.WHITE);
            JLabel greenBox = new JLabel("  ");
            greenBox.setOpaque(true);
            greenBox.setBackground(new Color(144, 238, 144));
            greenBox.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            legend.add(greenBox);
            legend.add(new JLabel("Available"));
            add(legend, BorderLayout.SOUTH);
        }

        revalidate();
        repaint();
    }

    private JLabel createTableLabel(String tableStr) {
        // parse table info from format: "Table T4 - Capacity: 4 - Section: Main"
        String id = "T?";
        String capacity = "?";

        if (tableStr.contains("Table ")) {
            int start = tableStr.indexOf("Table ") + 6;
            int end = tableStr.indexOf(" -");
            if (end > start) {
                id = tableStr.substring(start, end);
            }
        }
        if (tableStr.contains("Capacity: ")) {
            int start = tableStr.indexOf("Capacity: ") + 10;
            int end = tableStr.indexOf(" -", start);
            if (end == -1) end = tableStr.length();
            capacity = tableStr.substring(start, end).trim();
        }

        JLabel label = new JLabel(id + " (" + capacity + ")", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(144, 238, 144)); // light green
        label.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        label.setPreferredSize(new Dimension(60, 40));

        return label;
    }
}
