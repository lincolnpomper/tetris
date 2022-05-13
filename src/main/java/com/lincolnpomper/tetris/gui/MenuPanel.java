package com.lincolnpomper.tetris.gui;

import com.lincolnpomper.tetris.FontFactory;
import com.lincolnpomper.tetris.GameStarterListener;
import com.lincolnpomper.tetris.core.GameMode;
import com.lincolnpomper.tetris.core.KeyConfig;
import com.lincolnpomper.tetris.core.Position;
import com.lincolnpomper.tetris.graphics.BlurDegree;
import com.lincolnpomper.tetris.graphics.BlurDegreeFactory;
import com.lincolnpomper.tetris.graphics.EffectsFactory;
import com.lincolnpomper.tetris.graphics.animation.TextAnimation;
import com.lincolnpomper.tetris.util.Logger;
import com.lincolnpomper.tetris.util.Messages;
import com.lincolnpomper.tetris.util.Resolution;

import java.awt.RenderingHints;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class MenuPanel extends JComponent implements KeyListener {

    private static final int CHOSE_MODE_CONFIGURING_KEYS = 2;
    private static final int CHOSE_MODE_GAME_MODE = 0;
    private static final Color COLOR_BACKGROUND_1 = new Color(92, 93, 166);
    private static final Color COLOR_BACKGROUND_2 = new Color(59, 58, 75);
    private static final Color COLOR_BACKGROUND_EDIT_1 = new Color(81, 80, 108);
    private static final Color COLOR_BACKGROUND_EDIT_2 = new Color(65, 69, 75);
    private static final Color COLOR_BACKGROUND_EDIT_HIGHLIGHT_1 = new Color(88, 126, 201, 140);
    private static final Color COLOR_BACKGROUND_EDIT_HIGHLIGHT_2 = new Color(67, 111, 171, 140);
    private static final Color COLOR_EDITING = new Color(55, 154, 60, 255);
    private static final Color COLOR_EDITING_A_KEY_IN_USE = new Color(196, 144, 98, 255);
    private static final Color COLOR_EDIT_KEYS_OTHER_BUTTONS = new Color(51, 101, 182, 255);
    private static final Color COLOR_MENU_ITEM = new Color(45, 115, 199, 255);
    private static final Color COLOR_SUBMENU_ITEM = new Color(55, 154, 60, 255);
    private static final int MAX_HORIZONTAL_POS_FOR_EDIT_KEYS = 2;
    private static final int MAX_HORIZONTAL_POS_FOR_GAME_TYPE = 2;
    private static final int MAX_HORIZONTAL_POS_FOR_PLAYERS = 1;
    private static final int MAX_VERTICAL_POS = 3;
    private static final int MAX_VERTICAL_POS_FOR_EDIT_KEYS = 7;
    private static final int MENU_ITEMS_ROWS = MAX_VERTICAL_POS + 1;
    private static final int MIN_HORIZONTAL_POS = 0;
    private static final int MIN_HORIZONTAL_POS_FOR_EDIT_KEYS = 1;
    private static final int MIN_VERTICAL_POS = 0;
    private static final int MIN_VERTICAL_POS_FOR_EDIT_KEYS = 1;
    private static final int OPTION_CONFIG_BUTTON_1 = 5;
    private static final int OPTION_CONFIG_BUTTON_2 = 6;
    private static final int OPTION_CONFIG_DOWN = 2;
    private static final int OPTION_CONFIG_LEFT = 1;
    private static final int OPTION_CONFIG_RIGHT = 3;
    private static final int OPTION_CONFIG_UP = 4;
    private static final int OPTION_EDIT_KEYS = 2;
    private static final int OPTION_EXIT = 3;
    private static final int OPTION_GAME_TYPE = 1;
    private static final int OPTION_PLAYERS_QUANTITY = 0;
    private static final int SUBMENU_ITEMS_COLS = MAX_HORIZONTAL_POS_FOR_GAME_TYPE + 1;
    private static final int SUBMENU_ITEMS_ROWS = 2;
    private static boolean active = false;
    private ImageWithPosition rectangleMenu = new ImageWithPosition();
    private ImageWithPosition rectangleMenuHighlightRow = new ImageWithPosition();
    private ImageWithPosition rectangleEditingKeys = new ImageWithPosition();
    private ImageWithPosition rectangleEditingKeysHighlightRow = new ImageWithPosition();
    private ImageWithPosition rectangleEditingKeysHighlightColumn = new ImageWithPosition();
    private TextAnimation buttonBackToTitleScreen;
    private TextAnimation buttonKeyInUse;
    private TextAnimation buttonPressEnterToEdit;
    private TextAnimation buttonPressEnterToStart;
    private TextAnimation buttonTypeKeyButtons;
    private int chooseOptionGameType = 0;
    private int chooseOptionPlayers = 0;
    private int choseMode = CHOSE_MODE_GAME_MODE;
    private TextAnimation[][] editKeysAnimations;
    private boolean editingAKey = false, editingAKeyInUse = false;
    private GameStarterListener gameStarterListener;
    private int horizontalPositionForEditKeys = 1;
    private TextAnimation[] menuAnimations;
    private KeyConfig[] playersKeys;
    private Resolution resolution;
    private TextAnimation[][] subMenuAnimations;
    private int verticalPosition = 0;
    private int x1;
    private int x2;
    private int x3;
    private int x4;
    private int y1;
    private int y2;
    private int y3;
    private int y4;
    private int yKeys8;
    private int yKeys1;
    private int yKeys2;
    private int yKeys3;
    private int yKeys4;
    private int yKeys5;
    private int yKeys6;
    private int yKeys7;
    private int yKeys9;

    public MenuPanel(GameStarterListener gameStarterListener, Resolution resolution) {

        int blockSize = resolution.getBlockSize();
        this.gameStarterListener = gameStarterListener;
        this.resolution = resolution;

        this.addKeyListener(this);

        setPreferredSize(new Dimension(resolution.getWidth(), resolution.getHeight()));

        setLayout(new BorderLayout());
        setBackground(Color.black);
        setDoubleBuffered(true);

        int width = blockSize * 37;
        int height = blockSize * 19;
        Color[] colors = new Color[]{COLOR_BACKGROUND_1, COLOR_BACKGROUND_2};
        rectangleMenu.image = createRectangleBackground(blockSize, width, height, colors);
        rectangleMenu.position = new Position(getCenteredXBasedOnImage(rectangleMenu), blockSize * 8);

        height = blockSize * 5;
        colors = new Color[]{COLOR_BACKGROUND_EDIT_HIGHLIGHT_1, COLOR_BACKGROUND_EDIT_HIGHLIGHT_2};
        rectangleMenuHighlightRow.image = createRectangleBackground(blockSize, width, height, colors);
        rectangleMenuHighlightRow.position = new Position(getCenteredXBasedOnImage(rectangleMenuHighlightRow), this.resolution.getBlockSize() * 9);

        height = blockSize * 22;
        colors = new Color[]{COLOR_BACKGROUND_EDIT_1, COLOR_BACKGROUND_EDIT_2};
        rectangleEditingKeys.image = createRectangleBackground(blockSize, width, height, colors);
        rectangleEditingKeys.position = new Position(getCenteredXBasedOnImage(rectangleEditingKeys), blockSize * 7);

        height = blockSize * 4;
        colors = new Color[]{COLOR_BACKGROUND_EDIT_HIGHLIGHT_1, COLOR_BACKGROUND_EDIT_HIGHLIGHT_2};
        rectangleEditingKeysHighlightRow.image = createRectangleBackground(blockSize, width, height, colors);
        rectangleEditingKeysHighlightRow.position = new Position(getCenteredXBasedOnImage(rectangleEditingKeysHighlightRow), blockSize * 8);

        width = blockSize * 11;
        height = blockSize * 14;
        int startingX = rectangleMenu.position.getX();
        colors = new Color[]{COLOR_BACKGROUND_EDIT_HIGHLIGHT_1, COLOR_BACKGROUND_EDIT_HIGHLIGHT_2};
        rectangleEditingKeysHighlightColumn.image = createRectangleBackground(blockSize, width, height, colors);
        rectangleEditingKeysHighlightColumn.position = new Position(startingX + blockSize * 2, blockSize * 10);

        x1 = rectangleMenu.position.getX() + blockSize * 2;
        x2 = x1 + blockSize * 9;
        x3 = x2 + blockSize * 9;
        x4 = x3 + blockSize * 9;

        y1 = blockSize * 10;
        y2 = blockSize * 13;
        y3 = blockSize * 16;
        y4 = blockSize * 19;

        yKeys1 = blockSize * 9;
        yKeys2 = blockSize * 11;
        yKeys3 = blockSize * 13;
        yKeys4 = blockSize * 15;
        yKeys5 = blockSize * 17;
        yKeys6 = blockSize * 19;
        yKeys7 = blockSize * 21;
        yKeys8 = blockSize * 23;
        yKeys9 = blockSize * 25;

        KeyConfig keyConfig1 = new KeyConfig(KeyConfig.DEFAULT_PLAYER1_KEY_CONFIG);
        KeyConfig keyConfig2 = new KeyConfig(KeyConfig.DEFAULT_PLAYER2_KEY_CONFIG);
        playersKeys = new KeyConfig[]{keyConfig1, keyConfig2};

        Button[] menuButtons = buildMenuButtons();
        menuAnimations = buildMenuAnimations(menuButtons);
        updateMenuItemSelection(menuAnimations);

        buildTitleScreenEnterButton();

        buildOtherMenuButtons();

        Button[][] subMenuButtons = buildSubMenuButtons();
        subMenuAnimations = buildSubMenuAnimations(subMenuButtons);
        updateItemSubmenuSelection(subMenuAnimations);

        Button[][] buttonsEditKeys = buildEditKeysButtons();
        editKeysAnimations = buildEditKeysAnimations(buttonsEditKeys);
    }

    private int getCenteredXBasedOnImage(ImageWithPosition imageWithPosition) {
        return (resolution.getWidth() - imageWithPosition.image.getWidth()) / 2;
    }

    private TextAnimation[][] buildEditKeysAnimations(Button[][] buttons) {

        TextAnimation[][] animations = new TextAnimation[buttons.length][];
        BufferedImage[] images;

        for (int i = 0; i < animations.length; i++) {

            animations[i] = new TextAnimation[buttons[i].length];

            for (int j = 0; j < animations[i].length; j++) {

                Position position = new Position(buttons[i][j].x, buttons[i][j].y);
                images = generateImagesForEditingKey(buttons[i][j].text);
                animations[i][j] = new TextAnimation(position, images, 40, this);
            }
        }

        return animations;
    }

    private Button[][] buildEditKeysButtons() {

        return new Button[][]{{new Button(Messages.getString("MenuPanel.left"), x1, yKeys2), new Button(Messages.getString("MenuPanel.down"), x1, yKeys3),
                new Button(Messages.getString("MenuPanel.right"), x1, yKeys4), new Button(Messages.getString("MenuPanel.up"), x1, yKeys5),
                new Button(Messages.getString("MenuPanel.button_1"), x1, yKeys6), new Button(Messages.getString("MenuPanel.button_2"), x1, yKeys7)},
                {new Button(Messages.getString("MenuPanel.player_1"), x2, yKeys1), new Button(KeyEvent.getKeyText(playersKeys[0].left), x2, yKeys2),
                        new Button(KeyEvent.getKeyText(playersKeys[0].down), x2, yKeys3), new Button(KeyEvent.getKeyText(playersKeys[0].right), x2, yKeys4),
                        new Button(KeyEvent.getKeyText(playersKeys[0].up), x2, yKeys5), new Button(KeyEvent.getKeyText(playersKeys[0].button1), x2, yKeys6),
                        new Button(KeyEvent.getKeyText(playersKeys[0].button2), x2, yKeys7)},
                {new Button(Messages.getString("MenuPanel.player_2"), x3, yKeys1), new Button(KeyEvent.getKeyText(playersKeys[1].left), x3, yKeys2),
                        new Button(KeyEvent.getKeyText(playersKeys[1].down), x3, yKeys3), new Button(KeyEvent.getKeyText(playersKeys[1].right), x3, yKeys4),
                        new Button(KeyEvent.getKeyText(playersKeys[1].up), x3, yKeys5), new Button(KeyEvent.getKeyText(playersKeys[1].button1), x3, yKeys6),
                        new Button(KeyEvent.getKeyText(playersKeys[1].button2), x3, yKeys7)}};
    }

    private TextAnimation[] buildMenuAnimations(Button[] buttons) {

        TextAnimation[] animations = new TextAnimation[MENU_ITEMS_ROWS];

        int delay = 60;

        for (int i = 0; i < MENU_ITEMS_ROWS; i++) {

            String text = buttons[i].text;
            final Font font = FontFactory.getInstance().fontMenuKeys;
            final BlurDegree blurDegree = BlurDegreeFactory.getInstance().menu;
            BufferedImage[] images = EffectsFactory.getInstance().buildImages(text, COLOR_MENU_ITEM, font, blurDegree);

            Position position = new Position(buttons[i].x, buttons[i].y);
            animations[i] = new TextAnimation(position, images, delay, this);
        }
        return animations;
    }

    private Button[] buildMenuButtons() {

        Button[] buttons = new Button[MENU_ITEMS_ROWS];

        buttons[0] = new Button(Messages.getString("MenuPanel.players"), x1, y1);
        buttons[1] = new Button(Messages.getString("MenuPanel.game_type"), x1, y2);
        buttons[2] = new Button(Messages.getString("MenuPanel.edit_keys"), x1, y3);
        buttons[3] = new Button(Messages.getString("MenuPanel.exit"), x1, y4);

        return buttons;
    }

    private void buildOtherMenuButtons() {

        BufferedImage[] images;
        int delay = 40;
        int x;

        images = generateImagesForEditingKeyOther(Messages.getString("MenuPanel.type_a_new_key"));
        int width = resolution.getWidth();
        x = (width / 2) - images[0].getWidth() / 2;
        Position position = new Position(x, yKeys8);
        buttonTypeKeyButtons = new TextAnimation(position, images, delay, this);

        images = generateImagesForEditingKeyOther(Messages.getString("MenuPanel.press_enter_to_edit"));
        x = (width / 2) - images[0].getWidth() / 2;
        position = new Position(x, yKeys8);
        buttonPressEnterToEdit = new TextAnimation(position, images, delay, this);

        images = generateImagesForEditingKeyOther(Messages.getString("MenuPanel.key_in_use"));
        x = (width / 2) - images[0].getWidth() / 2;
        position = new Position(x, yKeys8);
        buttonKeyInUse = new TextAnimation(position, images, delay, this);

        position = new Position(x1, yKeys9);
        images = generateImagesForTitleScreenButton();
        buttonBackToTitleScreen = new TextAnimation(position, images, delay, this);
    }

    private TextAnimation[][] buildSubMenuAnimations(Button[][] buttons) {

        TextAnimation[][] animations = new TextAnimation[SUBMENU_ITEMS_ROWS][SUBMENU_ITEMS_COLS];

        for (int i = 0; i < SUBMENU_ITEMS_ROWS; i++) {
            for (int j = 0; j < SUBMENU_ITEMS_COLS; j++) {

                if (buttons[i][j] == null) {
                    continue;
                }

                String text = buttons[i][j].text;

                BufferedImage[] images = EffectsFactory.getInstance().buildImages(text, COLOR_SUBMENU_ITEM, FontFactory.getInstance().fontMenuKeys,
                        BlurDegreeFactory.getInstance().menu);

                Position position = new Position(buttons[i][j].x, buttons[i][j].y);
                animations[i][j] = new TextAnimation(position, images, 30, this);
            }
        }

        return animations;
    }

    private Button[][] buildSubMenuButtons() {

        Button[][] buttons = new Button[SUBMENU_ITEMS_ROWS][SUBMENU_ITEMS_COLS];

        buttons[0][0] = new Button(Messages.getString("MenuPanel.players.1_player"), x2, y1);
        buttons[0][1] = new Button(Messages.getString("MenuPanel.players.2_players"), x3, y1);

        buttons[1][0] = new Button(Messages.getString("MenuPanel.game_type.normal"), x2, y2);
        buttons[1][1] = new Button(Messages.getString("MenuPanel.game_type.master"), x3, y2);
        buttons[1][2] = new Button(Messages.getString("MenuPanel.game_type.ultra"), x4, y2);

        return buttons;
    }

    private void buildTitleScreenEnterButton() {

        BufferedImage[] images = generateImagesForEditingKeyOther(Messages.getString("MenuPanel.press_enter_to_start"));

        int width = resolution.getWidth();
        int x = (width / 2) - images[0].getWidth() / 2;

        Position position = new Position(x, yKeys8);

        buttonPressEnterToStart = new TextAnimation(position, images, 40, this);
    }

    private void changeSelectedKey(int keyCode) {

        BufferedImage[] images = generateImagesForEditingKey(KeyEvent.getKeyText(keyCode));
        editKeysAnimations[horizontalPositionForEditKeys][verticalPosition].setImages(images);
    }

    private void changeSelectedKeyInConfig(int keyCode) {

        if (verticalPosition == OPTION_CONFIG_LEFT) {
            playersKeys[horizontalPositionForEditKeys].left = keyCode;
        } else if (verticalPosition == OPTION_CONFIG_DOWN) {
            playersKeys[horizontalPositionForEditKeys].down = keyCode;
        } else if (verticalPosition == OPTION_CONFIG_RIGHT) {
            playersKeys[horizontalPositionForEditKeys].right = keyCode;
        } else if (verticalPosition == OPTION_CONFIG_UP) {
            playersKeys[horizontalPositionForEditKeys].up = keyCode;
        } else if (verticalPosition == OPTION_CONFIG_BUTTON_1) {
            playersKeys[horizontalPositionForEditKeys].button1 = keyCode;
        } else if (verticalPosition == OPTION_CONFIG_BUTTON_2) {
            playersKeys[horizontalPositionForEditKeys].button2 = keyCode;
        }
    }

    private void changeToSetupKeys() {

        horizontalPositionForEditKeys = MIN_HORIZONTAL_POS_FOR_EDIT_KEYS;
        verticalPosition = MIN_VERTICAL_POS_FOR_EDIT_KEYS;

        resetMenuItemsSelection();
        updateKeySetupSelection();
    }

    private BufferedImage createRectangleBackground(int blockSize, int width, int height, Color[] colors) {

        Logger.info(String.format("Creating rectangle background for blockSize %d and colors %s %s %n", blockSize, colors[0].toString(), colors[1].toString()));

        GradientPaint gradient = new GradientPaint(0, 0, colors[0], (float) width, 0, colors[1], false);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        graphics.setPaint(gradient);
        graphics.fillRect(blockSize, blockSize, width - (blockSize * 2), height - (blockSize * 2));

        return EffectsFactory.getInstance().changeImageAlpha(image, 0.85f);
    }

    private void drawBackgroundRectangleHighlightRow(BufferedImage image, Position position, Graphics graphics) {

        int y = position.getY() + verticalPosition * this.resolution.getBlockSize() * 3;
        graphics.drawImage(image, position.getX(), y, this);
    }

    private void drawBackgroundRectangle(BufferedImage image, Position position, Graphics graphics) {
        graphics.drawImage(image, position.getX(), position.getY(), this);
    }

    private void drawBackgroundRectangleHighlightColumn(BufferedImage image, Position position, Graphics graphics) {
        int x = position.getX() + horizontalPositionForEditKeys * this.resolution.getBlockSize() * 9;
        graphics.drawImage(image, x, position.getY(), this);
    }

    private void drawBackgroundRectangleHighlightEditKeysRow(BufferedImage image, Position position, Graphics graphics) {

        int y = position.getY() + verticalPosition * this.resolution.getBlockSize() * 2;
        graphics.drawImage(image, position.getX(), y, this);
    }

    private void drawEditKeyMenuButtons(Graphics graphics) {

        int gapX = resolution.getBlockSize();
        int gapY = resolution.getBlockSize() / 2;

        for (TextAnimation[] row : editKeysAnimations) {
            for (TextAnimation item : row) {
                int x = item.getPosition().getX();
                int y = item.getPosition().getY();
                x += gapX;
                y -= gapY;
                graphics.drawImage(item.getDraw(), x, y, this);
            }
        }
    }

    private void drawEditKeysScreen(Graphics graphics) {

        int x;
        int y;

        if (editingAKeyInUse) {

            x = buttonKeyInUse.getPosition().getX();
            y = buttonKeyInUse.getPosition().getY();
            graphics.drawImage(buttonKeyInUse.getDraw(), x, y, this);

        } else {

            if (editingAKey) {
                x = buttonTypeKeyButtons.getPosition().getX();
                y = buttonTypeKeyButtons.getPosition().getY();
                graphics.drawImage(buttonTypeKeyButtons.getDraw(), x, y, this);

            } else if (verticalPosition != MAX_VERTICAL_POS_FOR_EDIT_KEYS) {
                x = buttonPressEnterToEdit.getPosition().getX();
                y = buttonPressEnterToEdit.getPosition().getY();
                graphics.drawImage(buttonPressEnterToEdit.getDraw(), x, y, this);
            }
        }

        x = buttonBackToTitleScreen.getPosition().getX();
        y = buttonBackToTitleScreen.getPosition().getY();
        graphics.drawImage(buttonBackToTitleScreen.getDraw(), x, y, this);
    }

    private void drawOtherTitleScreenButtons(Graphics graphics) {

        if (verticalPosition == MIN_VERTICAL_POS) {
            int x = buttonPressEnterToStart.getPosition().getX();
            int y = buttonPressEnterToStart.getPosition().getY();
            graphics.drawImage(buttonPressEnterToStart.getDraw(), x, y, this);
        }
    }

    private void drawTitleScreenMenuButtons(Graphics graphics) {
        for (TextAnimation itemMenuAnimation : menuAnimations) {
            int x = itemMenuAnimation.getPosition().getX();
            int y = itemMenuAnimation.getPosition().getY();
            graphics.drawImage(itemMenuAnimation.getDraw(), x, y, this);
        }
    }

    private void drawTitleScreenSubMenuButtons(Graphics graphics) {
        for (int i = 0; i < SUBMENU_ITEMS_ROWS; i++) {
            for (int j = 0; j < SUBMENU_ITEMS_COLS; j++) {
                if (subMenuAnimations[i][j] != null) {
                    int x = subMenuAnimations[i][j].getPosition().getX();
                    int y = subMenuAnimations[i][j].getPosition().getY();
                    graphics.drawImage(subMenuAnimations[i][j].getDraw(), x, y, this);
                }
            }
        }
    }

    private BufferedImage[] generateImagesForEditingKey(String text) {

        Color color = editingAKeyInUse ? COLOR_EDITING_A_KEY_IN_USE : COLOR_EDITING;
        return EffectsFactory.getInstance().buildImages(text, color, FontFactory.getInstance().fontMenuKeysWhenEditing,
                BlurDegreeFactory.getInstance().editKeys);
    }

    private BufferedImage[] generateImagesForEditingKeyOther(String text) {
        return EffectsFactory.getInstance().buildImages(text, COLOR_EDIT_KEYS_OTHER_BUTTONS, FontFactory.getInstance().fontOtherMenu,
                BlurDegreeFactory.getInstance().other);
    }

    private BufferedImage[] generateImagesForTitleScreenButton() {
        String text = Messages.getString("MenuPanel.go_back_title_screen");
        return EffectsFactory.getInstance().buildImages(text, COLOR_EDIT_KEYS_OTHER_BUTTONS, FontFactory.getInstance().fontOtherMenu,
                BlurDegreeFactory.getInstance().editKeys);
    }

    private void handleKeyPressedEditKeysScreen(int keyCode) {

        if (keyCode == KeyEvent.VK_DOWN && !editingAKey) {
            if (verticalPosition < MAX_VERTICAL_POS_FOR_EDIT_KEYS) {
                verticalPosition++;
            }
        } else if (keyCode == KeyEvent.VK_UP && !editingAKey) {
            if (verticalPosition > MIN_VERTICAL_POS_FOR_EDIT_KEYS) {
                verticalPosition--;
            }
        } else if (keyCode == KeyEvent.VK_RIGHT && !editingAKey) {
            if (horizontalPositionForEditKeys < MAX_HORIZONTAL_POS_FOR_EDIT_KEYS && verticalPosition < MAX_VERTICAL_POS_FOR_EDIT_KEYS) {
                horizontalPositionForEditKeys++;
            }
        } else if (keyCode == KeyEvent.VK_LEFT && !editingAKey) {
            if (horizontalPositionForEditKeys > MIN_HORIZONTAL_POS_FOR_EDIT_KEYS) {
                horizontalPositionForEditKeys--;
            }
        } else if (keyCode == KeyEvent.VK_ESCAPE) {

            if (editingAKey) {
                editingAKey = false;
                editingAKeyInUse = false;
            }

        } else if (keyCode == KeyEvent.VK_ENTER) {

            if (verticalPosition == MAX_VERTICAL_POS_FOR_EDIT_KEYS) {

                choseMode = CHOSE_MODE_GAME_MODE;
                verticalPosition = OPTION_EDIT_KEYS;

                resetKeyConfigSelection();
                updateMenuItemSelection(menuAnimations);

            } else if (!editingAKey) {

                editingAKey = true;
            }

        } else {

            if (keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_DOWN || keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9 ||
                    keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_DELETE || keyCode >= KeyEvent.VK_KP_UP && keyCode <= KeyEvent.VK_KP_RIGHT ||
                    keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z || keyCode >= KeyEvent.VK_SHIFT && keyCode <= KeyEvent.VK_PAUSE ||
                    keyCode >= KeyEvent.VK_SPACE && keyCode <= KeyEvent.VK_HOME || keyCode == KeyEvent.VK_INSERT) {

                if (editingAKey) {

                    if (isCurrentKey(keyCode)) {

                        editingAKey = false;
                        editingAKeyInUse = false;
                        changeSelectedKeyInConfig(keyCode);
                        changeSelectedKey(keyCode);

                    } else if (isNewKey(keyCode)) {

                        if (isInUse(keyCode)) {
                            editingAKeyInUse = true;

                        } else {
                            editingAKey = false;
                            editingAKeyInUse = false;
                            changeSelectedKeyInConfig(keyCode);
                        }
                        changeSelectedKey(keyCode);
                    }
                }
            }
        }

        if (keyCode != KeyEvent.VK_ESCAPE) {
            updateKeySetupSelection();
        }
    }

    private void handleKeyPressedTitleScreen(int keyCode) {

        if (keyCode == KeyEvent.VK_DOWN) {
            if (verticalPosition < MAX_VERTICAL_POS) {
                verticalPosition++;
            }
        } else if (keyCode == KeyEvent.VK_UP) {
            if (verticalPosition > MIN_VERTICAL_POS) {
                verticalPosition--;
            }

        } else if (verticalPosition == OPTION_PLAYERS_QUANTITY || verticalPosition == OPTION_GAME_TYPE) {

            if (verticalPosition == OPTION_PLAYERS_QUANTITY) {
                if (keyCode == KeyEvent.VK_RIGHT) {

                    if (chooseOptionPlayers < MAX_HORIZONTAL_POS_FOR_PLAYERS) {
                        chooseOptionPlayers++;
                    }

                } else if (keyCode == KeyEvent.VK_LEFT) {

                    if (chooseOptionPlayers > MIN_HORIZONTAL_POS) {
                        chooseOptionPlayers--;
                    }
                }

            } else if (verticalPosition == OPTION_GAME_TYPE) {

                if (keyCode == KeyEvent.VK_RIGHT) {

                    if (chooseOptionGameType < MAX_HORIZONTAL_POS_FOR_GAME_TYPE) {
                        chooseOptionGameType++;

                    }
                } else if (keyCode == KeyEvent.VK_LEFT) {

                    if (chooseOptionGameType > MIN_HORIZONTAL_POS) {
                        chooseOptionGameType--;
                    }
                }
            }
        }

        if (keyCode == KeyEvent.VK_ENTER) {

            if (verticalPosition == OPTION_PLAYERS_QUANTITY) {

                startNewGame();

            } else if (verticalPosition == OPTION_EDIT_KEYS) {

                choseMode = CHOSE_MODE_CONFIGURING_KEYS;
                changeToSetupKeys();

            } else if (verticalPosition == OPTION_EXIT) {
                System.exit(0);
            }
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {

            if (verticalPosition == OPTION_EXIT) {
                System.exit(0);
            } else {
                verticalPosition = OPTION_EXIT;
                updateMenuItemSelection(menuAnimations);
            }
        }

        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP) {
            updateMenuItemSelection(menuAnimations);
        }
        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT) {
            updateItemSubmenuSelection(subMenuAnimations);
        }
    }

    private boolean isCurrentKey(int keyCode) {

        boolean isCurrent = false;

        if (verticalPosition == OPTION_CONFIG_LEFT) {
            isCurrent = playersKeys[horizontalPositionForEditKeys].left != keyCode;
        } else if (verticalPosition == OPTION_CONFIG_DOWN) {
            isCurrent = playersKeys[horizontalPositionForEditKeys].down == keyCode;
        } else if (verticalPosition == OPTION_CONFIG_RIGHT) {
            isCurrent = playersKeys[horizontalPositionForEditKeys].right == keyCode;
        } else if (verticalPosition == OPTION_CONFIG_UP) {
            isCurrent = playersKeys[horizontalPositionForEditKeys].up == keyCode;
        } else if (verticalPosition == OPTION_CONFIG_BUTTON_1) {
            isCurrent = playersKeys[horizontalPositionForEditKeys].button1 == keyCode;
        } else if (verticalPosition == OPTION_CONFIG_BUTTON_2) {
            isCurrent = playersKeys[horizontalPositionForEditKeys].button2 == keyCode;
        }

        return isCurrent;
    }

    private boolean isInUse(int keyCode) {

        for (KeyConfig playersKeyConfig : playersKeys) {
            for (int key : playersKeyConfig.getAllKeys()) {
                if (key == keyCode) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isNewKey(int keyCode) {

        boolean isNew = true;

        if (verticalPosition == OPTION_CONFIG_LEFT) {
            isNew = playersKeys[horizontalPositionForEditKeys].left != keyCode;
        } else if (verticalPosition == OPTION_CONFIG_DOWN) {
            isNew = playersKeys[horizontalPositionForEditKeys].down != keyCode;
        } else if (verticalPosition == OPTION_CONFIG_RIGHT) {
            isNew = playersKeys[horizontalPositionForEditKeys].right != keyCode;
        } else if (verticalPosition == OPTION_CONFIG_UP) {
            isNew = playersKeys[horizontalPositionForEditKeys].up != keyCode;
        } else if (verticalPosition == OPTION_CONFIG_BUTTON_1) {
            isNew = playersKeys[horizontalPositionForEditKeys].button1 != keyCode;
        } else if (verticalPosition == OPTION_CONFIG_BUTTON_2) {
            isNew = playersKeys[horizontalPositionForEditKeys].button2 != keyCode;
        }
        return isNew;
    }

    private void resetKeyConfigSelection() {

        for (TextAnimation[] optionConfigAnimation : editKeysAnimations) {
            for (int j = 0; j < editKeysAnimations[0].length; j++) {
                optionConfigAnimation[j].stop();
            }
        }
    }

    private void resetMenuItemsSelection() {

        for (int i = 0; i < MENU_ITEMS_ROWS; i++) {
            menuAnimations[i].reset();
            menuAnimations[i].stop();
        }

        for (int i = 0; i < SUBMENU_ITEMS_ROWS; i++) {
            for (int j = 0; j < SUBMENU_ITEMS_COLS; j++) {

                if (subMenuAnimations[i][j] != null) {
                    subMenuAnimations[i][j].reset();
                    subMenuAnimations[i][j].stop();
                }
            }
        }
    }

    private void updateItemSubmenuSelection(TextAnimation[][] subMenuAnimations) {

        for (int i = 0; i < SUBMENU_ITEMS_ROWS; i++) {
            for (int j = 0; j < SUBMENU_ITEMS_COLS; j++) {

                if (subMenuAnimations[i][j] != null) {
                    if (i == verticalPosition) {

                        if ((verticalPosition == OPTION_PLAYERS_QUANTITY) && (j == chooseOptionPlayers) ||
                                (verticalPosition == OPTION_GAME_TYPE) && (j == chooseOptionGameType)) {
                            subMenuAnimations[i][j].start();
                        } else {
                            subMenuAnimations[i][j].reset();
                            subMenuAnimations[i][j].stop();
                        }
                    } else {
                        subMenuAnimations[i][j].stop();
                    }
                }
            }
        }
    }

    private void updateKeySetupSelection() {

        buttonBackToTitleScreen.reset();
        buttonBackToTitleScreen.stop();

        for (int i = 0; i < editKeysAnimations.length; i++) {
            for (int j = 0; j < editKeysAnimations[i].length; j++) {

                if (i == horizontalPositionForEditKeys && j == verticalPosition) {
                    editKeysAnimations[i][j].start();
                } else {
                    editKeysAnimations[i][j].reset();
                    editKeysAnimations[i][j].stop();
                }
            }
        }

        if (verticalPosition == MAX_VERTICAL_POS_FOR_EDIT_KEYS) {
            buttonBackToTitleScreen.start();
        }
    }

    private void updateMenuItemSelection(TextAnimation[] menuAnimations) {

        for (int i = 0; i < menuAnimations.length; i++) {

            if (i == verticalPosition) {
                menuAnimations[i].start();
            } else {
                menuAnimations[i].reset();
                menuAnimations[i].stop();
            }
        }
    }

    public static boolean isMenuEnabled() {
        return active;
    }

    public void keyPressed(KeyEvent e) {

        if (!active) {
            return;
        }

        int keyCode = e.getKeyCode();

        if (choseMode == CHOSE_MODE_GAME_MODE) {
            handleKeyPressedTitleScreen(keyCode);
        } else if (choseMode == CHOSE_MODE_CONFIGURING_KEYS) {
            handleKeyPressedEditKeysScreen(keyCode);
        }

        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void paintComponent(Graphics graphics) {

        if (!active) {
            return;
        }

        super.paintComponent(graphics);

        if (choseMode == CHOSE_MODE_GAME_MODE) {

            drawBackgroundRectangle(rectangleMenu.image, rectangleMenu.position, graphics);
            drawBackgroundRectangleHighlightRow(rectangleMenuHighlightRow.image, rectangleMenuHighlightRow.position, graphics);

            drawTitleScreenMenuButtons(graphics);
            drawTitleScreenSubMenuButtons(graphics);
            drawOtherTitleScreenButtons(graphics);

        } else if (choseMode == CHOSE_MODE_CONFIGURING_KEYS) {

            drawBackgroundRectangle(rectangleEditingKeys.image, rectangleEditingKeys.position, graphics);

            if (verticalPosition != MAX_VERTICAL_POS_FOR_EDIT_KEYS) {
                drawBackgroundRectangleHighlightEditKeysRow(rectangleEditingKeysHighlightRow.image, rectangleEditingKeysHighlightRow.position, graphics);
                drawBackgroundRectangleHighlightColumn(rectangleEditingKeysHighlightColumn.image, rectangleEditingKeysHighlightColumn.position, graphics);
            }

            drawEditKeyMenuButtons(graphics);
            drawEditKeysScreen(graphics);
        }
    }

    public void setMenuPanelEnabled() {
        active = true;
    }

    public void startNewGame() {

        resetMenuItemsSelection();

        int numberOfPlayers = chooseOptionPlayers + 1;

        GameMode gameMode = GameMode.from(chooseOptionGameType);
        gameStarterListener.startGame(numberOfPlayers, gameMode, playersKeys);
    }
}