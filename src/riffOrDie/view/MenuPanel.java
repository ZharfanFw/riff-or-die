package riffOrDie.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
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
 */
public class MenuPanel extends JPanel implements IMenuView {
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
        setBackground(new java.awt.Color(50, 50, 50));

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
        gbc.insets = new Insets(20, 0, 20, 0);

        JLabel titleLabel = new JLabel("Riff Or Die");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(java.awt.Color.WHITE);

        add(titleLabel, gbc);
    }

    private void addUsernameInput() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(java.awt.Color.WHITE);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(usernameField, gbc);
    }

    private void addScoreBoard() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] columnNames = { "Username", "Skor", "Peluru Meleset", "Sisa Peluru" };
        scoreboardModel = new DefaultTableModel(columnNames, 0);

        scoreboardTable = new JTable(scoreboardModel);
        scoreboardTable.setEnabled(false);
        scoreboardTable.setFont(new Font("Arial", Font.PLAIN, 12));
        scoreboardTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(scoreboardTable);
        add(scrollPane, gbc);
    }

    private void addButtons() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 14));
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (presenter != null) {
                    presenter.onPlayClicked(usernameField.getText());
                }
            }
        });
        add(playButton, gbc);

        gbc.gridx = 1;
        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.BOLD, 14));
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
