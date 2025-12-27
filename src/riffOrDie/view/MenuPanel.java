package riffOrDie.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.LineBorder;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import riffOrDie.model.GameScore;
import riffOrDie.presenter.IMenuPresenter;
import riffOrDie.presenter.IMenuView;
import riffOrDie.config.GameConstants;

/**
 * MenuPanel - Pure View, implements IMenuView
 * Hanya bertanggung jawab untuk display, semua logic di MenuPresenter
 * Features: Dark theme dengan accent red, clickable scoreboard table
 */
public class MenuPanel extends JPanel implements IMenuView {
    // Color Constants
    private static final Color DARK_BG = new Color(42, 42, 42);
    private static final Color DARKER_BG = new Color(26, 26, 26);
    private static final Color ACCENT_RED = new Color(231, 76, 60);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color HOVER_GRAY = new Color(58, 58, 58);
    private static final Color SUBTLE_GRAY = new Color(85, 85, 85);

    private GameFrame gameFrame;
    private JTextField usernameField;
    private JTable scoreboardTable;
    private DefaultTableModel scoreboardModel;
    private JButton playButton;
    private JButton quitButton;
    private IMenuPresenter presenter;

    public MenuPanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        setLayout(new GridBagLayout());
        setBackground(DARK_BG);

        addTitle();
        addUsernameInput();
        addScoreBoard();
        addButtons();
    }

    private void addTitle() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 30, 0);

        JLabel titleLabel = new JLabel("Riff Or Die");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(ACCENT_RED);

        add(titleLabel, gbc);
    }

    private void addUsernameInput() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(TEXT_WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBackground(DARKER_BG);
        usernameField.setForeground(TEXT_WHITE);
        usernameField.setBorder(new LineBorder(ACCENT_RED, 2, true)); // border radius effect
        usernameField.setCaretColor(ACCENT_RED);
        add(usernameField, gbc);
    }

    private void addScoreBoard() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 20, 30, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] columnNames = { "Username", "Skor", "Peluru Meleset", "Sisa Peluru" };
        scoreboardModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scoreboardTable = new JTable(scoreboardModel);
        scoreboardTable.setEnabled(true); // Enable table untuk selection
        scoreboardTable.setFont(new Font("Arial", Font.PLAIN, 12));
        scoreboardTable.setRowHeight(28);
        scoreboardTable.setBackground(DARKER_BG);
        scoreboardTable.setForeground(TEXT_WHITE);
        scoreboardTable.setSelectionBackground(ACCENT_RED);
        scoreboardTable.setSelectionForeground(TEXT_WHITE);
        scoreboardTable.setGridColor(SUBTLE_GRAY);

        // Style table header
        scoreboardTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        scoreboardTable.getTableHeader().setBackground(ACCENT_RED);
        scoreboardTable.getTableHeader().setForeground(TEXT_WHITE);
        scoreboardTable.getTableHeader().setOpaque(true);

        // Custom cell renderer untuk styling
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setBackground(isSelected ? ACCENT_RED : DARKER_BG);
                cell.setForeground(TEXT_WHITE);
                return cell;
            }
        };

        for (int i = 0; i < scoreboardTable.getColumnCount(); i++) {
            scoreboardTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Add ListSelectionListener untuk auto-fill username
        scoreboardTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = scoreboardTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String selectedUsername = (String) scoreboardModel.getValueAt(selectedRow, 0);
                        usernameField.setText(selectedUsername);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(scoreboardTable);
        scrollPane.setBackground(DARK_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);
        add(scrollPane, gbc);
    }

    private void addButtons() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 20, 30, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Play Button
        gbc.gridx = 0;
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 14));
        playButton.setBackground(ACCENT_RED);
        playButton.setForeground(TEXT_WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorder(new LineBorder(ACCENT_RED, 1, true));
        playButton.setOpaque(true);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (presenter != null) {
                    presenter.onPlayClicked(usernameField.getText());
                }
            }
        });
        add(playButton, gbc);

        // Quit Button
        gbc.gridx = 1;
        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 14));
        quitButton.setBackground(SUBTLE_GRAY);
        quitButton.setForeground(TEXT_WHITE);
        quitButton.setFocusPainted(false);
        quitButton.setBorder(new LineBorder(SUBTLE_GRAY, 1, true));
        quitButton.setOpaque(true);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (presenter != null) {
                    presenter.onQuitClicked();
                }
            }
        });
        add(quitButton, gbc);
    }

    /**
     * IMenuView Implementation Methods
     */

    @Override
    public void setPresenter(IMenuPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateScoreboard(List<GameScore> scores) {
        // Clear existing rows
        scoreboardModel.setRowCount(0);

        // Add new rows
        for (GameScore score : scores) {
            Object[] row = {
                    score.getUsername(),
                    score.getSkor(),
                    score.getPeluruMeleset(),
                    score.getSisaPeluru()
            };
            scoreboardModel.addRow(row);
        }

        // Clear selection setelah update
        scoreboardTable.clearSelection();
        usernameField.setText("");
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public String getUsername() {
        return usernameField.getText().trim();
    }

    @Override
    public void setPlayButtonEnabled(boolean enabled) {
        playButton.setEnabled(enabled);
    }

    /**
     * Navigate to game screen (called by presenter)
     * This is a special method not in IMenuView because we need frame reference
     */
    public void navigateToGame(String username, int sisaPeluru) {
        gameFrame.showGamePanel(username, sisaPeluru);
    }
}
