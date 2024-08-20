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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
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
    private final JPanel searchResultPanel = new JPanel(new GridBagLayout());
    private static final String ALL_ELEMENTS = "All Elements";
    private final JTabbedPane mainTabbedPane = new JTabbedPane(SwingConstants.TOP);
    private final JTextField searchField = new JTextField();
    private final Map<String, ImageIcon> elementIcons = new TreeMap<>();
    private final JComboBox<JLabel> elementFilterBox = new JComboBox<>();
    private final JLabel matchesLabel = new JLabel();

    /**
     * Constructor of the class.
     */
    public CharacterTabGUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(mainTabbedPane, gbc);
        JPanel searchTab = new JPanel(new GridBagLayout());
        mainTabbedPane.addTab("SEARCH", searchTab);
        searchTab.setEnabled(true);
        searchTab.setFocusCycleRoot(false);
        changeFont(searchTab, ToolData.AVAILABLE_FONTS.REGULAR_FONT, 15.0F);
        searchTab.setOpaque(true);
        searchTab.setRequestFocusEnabled(true);

        parseElementIcons();

        JPanel searchBarPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        searchTab.add(searchBarPanel,gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane searchScrollPane = new JScrollPane(searchResultPanel);
        searchTab.add(searchScrollPane,gbc);

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
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 200, 0, 5);
        searchBarPanel.add(elementFilterBox, gbc);

        changeFont(matchesLabel, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
        matchesLabel.setForeground(new Color(-15072759));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        searchBarPanel.add(matchesLabel, gbc);

        JButton searchConfirmButton = new JButton();
        searchConfirmButton.setMinimumSize(new Dimension(50, 30));
        searchConfirmButton.setPreferredSize(new Dimension(50, 30));
        searchConfirmButton.setText("✓");
        searchConfirmButton.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        searchBarPanel.add(searchConfirmButton, gbc);

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
        searchBarPanel.add(searchField, gbc);

        searchTab.updateUI();
    }

    /**
     * Returns the main panel of this tab
     *
     * @return main panel
     */
    public JPanel getCharacterTabTabbedPaneAsPanel() {
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
            int tabIndex = mainTabbedPane.indexOfTab(characterName);
            if (tabIndex != -1)
            {
                mainTabbedPane.setSelectedIndex(tabIndex);
            }
            else
            {
                mainTabbedPane.addTab(characterName,new CharacterCardGUI(characterListing).mainPanel);
                int newTabIndex = mainTabbedPane.indexOfTab(characterName);
                JPanel panelTab = new JPanel(new GridBagLayout());
                panelTab.setOpaque(false);
                JLabel labelTitle = new JLabel(characterName);
                JButton closeButton = new JButton("x");

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1;

                panelTab.add(labelTitle,gbc);

                gbc.gridx++;
                gbc.weightx = 0;
                panelTab.add(closeButton,gbc);

                mainTabbedPane.setTabComponentAt(newTabIndex,panelTab);

                closeButton.addActionListener(new TabCloseButtonActionHandler(characterName,mainTabbedPane));

            }
        });

        return characterButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String userFieldInput;
        int matchedCount = 0;
        userFieldInput = searchField.getText().toLowerCase();
        searchResultPanel.removeAll();
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
class TabCloseButtonActionHandler implements ActionListener {

    private final String characterName;
    private final JTabbedPane tabbedPane;

    public TabCloseButtonActionHandler(String characterName,JTabbedPane tabbedPane){
        this.characterName = characterName;
        this.tabbedPane = tabbedPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int index = tabbedPane.indexOfTab(characterName);
        if (index >= 1){
            tabbedPane.removeTabAt(index);
        }
    }
}
