package Files.Code.GUIs;

import static Files.Code.Data.ToolData.changeFont;
import static Files.Code.Data.ToolData.characters;
import static Files.Code.Data.ToolData.getCharacter;
import static Files.Code.Data.ToolData.getPlaceholderIcon;
import static Files.Code.GUIs.ToolGUI.NO_CHARACTERS_MATCH_MESSAGE;
import static Files.Code.GUIs.ToolGUI.checkIfCharacterCardHasBeenGenerated;
import static Files.Code.GUIs.ToolGUI.formatString;
import static Files.Code.GUIs.ToolGUI.getCharacterCard;

import Files.Code.Auxiliary.ComboBoxRenderer;
import Files.Code.Auxiliary.SearchBarListener;
import Files.Code.Data.Character;
import Files.Code.Data.CharacterListing;
import Files.Code.Data.ToolData;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class constructs the character tab GUI. (inside the main application window)
 */
public final class CharacterTabGUI implements ActionListener {

    private final JPanel mainPanel = new JPanel(new GridBagLayout());
    private static final String ALL_ELEMENTS = "All Elements";
    private final JPanel searchResultPanel = new JPanel(new GridBagLayout());
    private final JTextField searchField = new JTextField();
    private final JScrollPane searchScrollPane = new JScrollPane();
    private final Map<String, ImageIcon> elementIcons = new TreeMap<>();
    private final JComboBox<JLabel> elementFilterBox = new JComboBox<>();
    private final JLabel matchesLabel = new JLabel();

    /**
     * Constructor of the class.
     */
    public CharacterTabGUI() {
        mainPanel.setBackground(new Color(-1));
        mainPanel.setEnabled(true);
        mainPanel.setFocusCycleRoot(false);
        changeFont(mainPanel, ToolData.AVAILABLE_FONTS.REGULAR_FONT, 15.0F);
        mainPanel.setOpaque(true);
        mainPanel.setRequestFocusEnabled(true);
        parseElementIcons();

        elementFilterBox.setBackground(new Color(-2702646));
        elementFilterBox.setEnabled(true);
        changeFont(elementFilterBox, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
        final DefaultComboBoxModel<JLabel> elementFilterComboBoxModel = new DefaultComboBoxModel<>();
        elementFilterComboBoxModel.addElement(new JLabel(ALL_ELEMENTS));
        for (String element : elementIcons.keySet()) {
            JLabel elementLabel = new JLabel();
            changeFont(elementLabel, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
            elementLabel.setText(element);
            elementLabel.setIcon(elementIcons.get(element));
            elementFilterComboBoxModel.addElement(elementLabel);
        }
        elementFilterBox.setModel(elementFilterComboBoxModel);
        elementFilterBox.setRenderer(new ComboBoxRenderer(elementFilterBox));
        elementFilterBox.setSelectedIndex(0);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 255, 0, 5);
        mainPanel.add(elementFilterBox, gbc);

        changeFont(matchesLabel, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
        matchesLabel.setForeground(new Color(-15072759));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        mainPanel.add(matchesLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(searchResultPanel, gbc);

        JButton searchConfirmButton = new JButton();
        searchConfirmButton.setMinimumSize(new Dimension(50, 30));
        searchConfirmButton.setPreferredSize(new Dimension(50, 30));
        searchConfirmButton.setText("✓");
        searchConfirmButton.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(searchConfirmButton, gbc);

        searchField.addMouseListener(new SearchBarListener());
        searchField.setEnabled(true);
        changeFont(searchField, ToolData.AVAILABLE_FONTS.BLACK_FONT, 18.0F);
        searchField.setInheritsPopupMenu(false);
        searchField.setMaximumSize(new Dimension(240, 33));
        searchField.setMinimumSize(new Dimension(240, 33));
        searchField.setPreferredSize(new Dimension(240, 33));
        searchField.setText("Choose your fighter!");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        mainPanel.add(searchField, gbc);
    }

    /**
     * Returns the main panel of this tab
     *
     * @return main panel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Generates a character button for the character specified by name and the index of the match.
     *
     * @param characterName the name of the character
     * @param index which character by count it is
     */
    private void generateCharacterButton(String characterName, int index) {
        JButton characterButton = getjButton(characterName);
        addCharacterButtonToSelectedCharacterPanel(characterButton, index);

    }

    private void parseElementIcons() {
        final String iconFolderAddress = "/Files/Images/Icons";
        final String[] elements = {"Anemo", "Cryo", "Dendro", "Electro", "Geo", "Hydro", "Pyro"};
        for (String element : elements) {
            elementIcons.put(element, new ImageIcon(new ImageIcon(Objects.requireNonNull(
                    ToolData.class.getResource(iconFolderAddress + "/Element_" + element + ".png"))).getImage()
                    .getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        }
    }

    /**
     * Creates a JButton for the specified character.
     *
     * @param characterName the name of the character
     * @return JButton for the character.
     */
    private JButton getjButton(String characterName) {
        JButton characterButton = new JButton();
        CharacterListing characterListing;
        if (!checkIfCharacterCardHasBeenGenerated(characterName)) {
            characterListing = new CharacterListing(characterName);
            ToolGUI.addCharacterListing(characterListing);
        } else {
            characterListing = getCharacterCard(characterName);
        }
        assert characterListing != null;
        characterButton.setIcon(getCharacter(characterListing.getCharacterName()).icon);
        characterButton.setText(formatString(characterName));
        changeFont(characterButton, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
        characterButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        characterButton.setHorizontalTextPosition(SwingConstants.CENTER);
        characterButton.setOpaque(false);
        characterButton.setBorderPainted(false);
        characterButton.setContentAreaFilled(false);
        characterButton.addActionListener(e -> {
            if (isWindowAlreadyOpen(characterName)) {
                Objects.requireNonNull(getOpenWindow(characterName)).setVisible(true);
            } else {
                new CharacterCardGUI(characterListing);
            }
        });

        return characterButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String userFieldInput = searchField.getText().toLowerCase();
        int matchedCount = 0;
        searchResultPanel.removeAll();
        searchScrollPane.updateUI();
        JLabel label = (JLabel) elementFilterBox.getSelectedItem();
        assert label != null;
        String element = label.getText();
        List<Character> eligibleCharacters = new ArrayList<>();
        if (element.equalsIgnoreCase(ALL_ELEMENTS)) {
            eligibleCharacters.addAll(characters);
        } else {
            for (Character character : characters) {
                if (character.element.equalsIgnoreCase(element)) {
                    eligibleCharacters.add(character);
                }
            }
        }

        if (userFieldInput.equals("choose your fighter!")) {
            userFieldInput = "";
        }

        for (Character character : eligibleCharacters) {
            if (character.name.toLowerCase().contains(userFieldInput)) {
                {
                    generateCharacterButton(character.name, matchedCount);
                }
                matchedCount++;
            }
        }
        matchesLabel.setText("Matches: " + matchedCount);
        if (matchedCount == 0) {
            generateNoMatchesLabel();
        } else {
            searchScrollPane.setViewportView(searchResultPanel);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 4;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;

            mainPanel.add(searchScrollPane, gbc);
        }

    }

    private void generateNoMatchesLabel() {
        JLabel unknownCharacterLabel = new JLabel(getPlaceholderIcon("character"));
        unknownCharacterLabel.setText(NO_CHARACTERS_MATCH_MESSAGE);
        unknownCharacterLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        unknownCharacterLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        searchResultPanel.add(unknownCharacterLabel);
        searchResultPanel.updateUI();
    }

    /**
     * Verifies if a window for the specified character has already been opened.
     *
     * @param charName the specified character
     * @return true if the window is open, otherwise false.
     */

    public boolean isWindowAlreadyOpen(String charName) {
        Frame[] createdWindows = Frame.getFrames();
        for (Frame window : createdWindows) {
            if (window != null && window.getTitle().contains(charName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the open window for the specified character.
     *
     * @param charName the name of the character
     * @return window with the loaded character card for the specified character
     */
    public Window getOpenWindow(String charName) {
        assert isWindowAlreadyOpen(charName);
        Frame[] createdWindows = Frame.getFrames();
        for (Frame frame : createdWindows) {
            if (frame != null && frame.getTitle().contains(charName)) {
                return frame;
            }
        }
        return null;
    }

    /**
     * Adds a character button to the selected character panel (after triggering actionPerformed)
     *
     * @param charButton the button to add
     * @param index the index of the match
     */
    private void addCharacterButtonToSelectedCharacterPanel(JButton charButton, int index) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = index % 6;
        gbc.gridy = index / 6;
        searchResultPanel.add(charButton, gbc);
        searchResultPanel.updateUI();

    }

}
