package com.lincolnpomper.tetris.animation;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OutlineTextTest extends JPanel {

    private static Font font1;
    private static Font font2;
    private static Font font3;

    public OutlineTextTest() {
    }

    public static void main(String[] args) {

        OutlineTextTest panel = new OutlineTextTest();

        font1 = new Font("INVASION2000", Font.BOLD, 20);
        font2 = new Font("INVASION2000", Font.BOLD, 40);
        font3 = new Font("INVASION2000", Font.BOLD, 70);

        JFrame frame = new JFrame();

        frame.setBackground(Color.black);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setSize(new Dimension(500, 500));
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {

        String text1 = "some text";
        String text2 = "other text";
        String text3 = "BIG TEXT";

        Color outlineColor = Color.orange.darker();
        Color fillColor = Color.orange;
        BasicStroke outlineStroke1 = new BasicStroke(4.0f);
        BasicStroke outlineStroke2 = new BasicStroke(10.0f);
        BasicStroke outlineStroke3 = new BasicStroke(22.0f);

        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;

            // remember original settings
            Color originalColor = g2.getColor();
            Stroke originalStroke = g2.getStroke();
            RenderingHints originalHints = g2.getRenderingHints();

            // create a glyph vector from your text
            GlyphVector glyphVector1 = font1.createGlyphVector(g2.getFontRenderContext(), text1);
            GlyphVector glyphVector2 = font2.createGlyphVector(g2.getFontRenderContext(), text2);
            GlyphVector glyphVector3 = font3.createGlyphVector(g2.getFontRenderContext(), text3);

            Shape textShape1 = glyphVector1.getOutline(50, 50);
            Shape textShape2 = glyphVector2.getOutline(50, 150);
            Shape textShape3 = glyphVector3.getOutline(50, 250);

            // activate anti aliasing for text rendering (if you want it to look nice)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g2.setColor(outlineColor);
            g2.setStroke(outlineStroke1);
            g2.draw(textShape1);
            g2.setStroke(outlineStroke2);
            g2.draw(textShape2);
            g2.setStroke(outlineStroke3);
            g2.draw(textShape3);

            g2.setColor(fillColor);
            g2.fill(textShape1);
            g2.fill(textShape2);
            g2.fill(textShape3);

            // reset to original settings after painting
            g2.setColor(originalColor);
            g2.setStroke(originalStroke);
            g2.setRenderingHints(originalHints);
        }
    }
}