package com.lincolnpomper.tetris;

import com.lincolnpomper.tetris.gui.MenuPanel;
import com.lincolnpomper.tetris.util.Resolution;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements RoundChangedListener {

    private MenuPanel menuPanel;
    private Image titleScreenBackgroundImage;

    public MainPanel(Main main, Resolution resolution) {

        setBackground(Color.black);

        setPreferredSize(new Dimension(resolution.getWidth(), resolution.getHeight()));
        setLayout(new BorderLayout());
        setOpaque(false);

        nextRoundImage();

        menuPanel = new MenuPanel(main, resolution);
        add(menuPanel);

        this.invalidate();
    }

    public void activateMenu() {
        menuPanel.setMenuPanelEnabled();
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public Image getTitleScreenBackgroundImage() {
        return titleScreenBackgroundImage;
    }

    public void setTitleScreenBackgroundImage(Image titleScreenBackgroundImage) {
        this.titleScreenBackgroundImage = titleScreenBackgroundImage;
    }

    @Override
    public void nextRoundImage() {
        this.titleScreenBackgroundImage = BackGroundImagesFacade.getInstance().getNextImage();
        repaint();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(titleScreenBackgroundImage, 0, 0, this);
    }

    public void removeMenuPanel() {
        remove(menuPanel);
    }
}
