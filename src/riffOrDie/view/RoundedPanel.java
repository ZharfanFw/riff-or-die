package riffOrDie.view;

/**
 * ROUNDEDPANEL - Custom JPanel dengan rounded corners
 * 
 * Fungsi:
 * - Override paintComponent() dengan Graphics2D
 * - Draw rounded rectangle shape
 * - Fill dengan warna custom
 * 
 * Benefit: Beautiful rounded UI component (radius 15px)
 */
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.RenderingHints;

/**
 * RoundedPanel - Custom JPanel dengan rounded corners
 * Digunakan untuk membuat input field, buttons, dan components lain dengan
 * corner radius
 */
public class RoundedPanel extends JPanel {
    /** Ukuran radius corners (pixel) */
    private int cornerRadius;
    
    /** Warna border */
    private Color borderColor;
    
    /** Ketebalan border (pixel) */
    private int borderThickness;

    /**
     * Constructor untuk RoundedPanel
     * 
     * @param cornerRadius    - ukuran radius corners (pixel)
     * @param borderColor     - warna border
     * @param borderThickness - ketebalan border (pixel)
     */
    public RoundedPanel(int cornerRadius, Color borderColor, int borderThickness) {
        this.cornerRadius = cornerRadius;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
        setOpaque(false); // Transparent background untuk show rounded corners
    }

    /**
     * Override paintComponent untuk draw rounded panel
     * 
     * @param g Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw background dengan rounded corners
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

        // Draw border dengan rounded corners
        if (borderThickness > 0) {
            g2d.setColor(borderColor);
            g2d.setStroke(new java.awt.BasicStroke(borderThickness));
            g2d.drawRoundRect(borderThickness / 2, borderThickness / 2,
                    width - borderThickness, height - borderThickness,
                    cornerRadius, cornerRadius);
        }

        super.paintComponent(g);
    }
}
