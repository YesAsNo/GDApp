package Files.Code.GUIs;

import static Files.Code.Data.ToolData.artifacts;
import static Files.Code.Data.ToolData.changeFont;
import static Files.Code.Data.ToolData.getArtifact;
import static Files.Code.Data.ToolData.getCharacter;
import static Files.Code.Data.ToolData.getPlaceholderIcon;
import static Files.Code.Data.ToolData.getResizedResourceIcon;
import static Files.Code.Data.ToolData.getWeapon;
import static Files.Code.Data.ToolData.lookUpWeapons;
import static Files.Code.Data.ToolData.placeholderImageKeys;
import static Files.Code.GUIs.ToolGUI.CHARACTER_LIMIT;
import static Files.Code.GUIs.ToolGUI.EMPTY_SET_SELECTOR;
import static Files.Code.GUIs.ToolGUI.EMPTY_WEAPON_SELECTOR;
import static Files.Code.GUIs.ToolGUI.FIVE_STAR_WEAPON_DELIMITER;
import static Files.Code.GUIs.ToolGUI.FOUR_STAR_WEAPON_DELIMITER;

import Files.Code.Auxiliary.ComboBoxRenderer;
import Files.Code.Auxiliary.NotesListener;
import Files.Code.Auxiliary.NotesTextModel;
import Files.Code.Auxiliary.SaveButtonListener;
import Files.Code.Auxiliary.UpdateCharacterCardCheckBoxListener;
import Files.Code.Auxiliary.UpdateCharacterCardComboBoxListener;
import Files.Code.Auxiliary.UpdateLabelListener;
import Files.Code.Auxiliary.UpdateTextAreaListener;
import Files.Code.Auxiliary.WeaponSelectorComboBoxModel;
import Files.Code.Data.Character;
import Files.Code.Data.CharacterListing;
import Files.Code.Data.Item;
import Files.Code.Data.ToolData;
import Files.Code.Data.Weapon;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * This class creates character cards with fields that allow you to customize the items equipped on the character.
 */
public class CharacterCardGUI extends JPanel {
    /**
     * Save button color.
     */
    public static final int YES_CHANGES_SAVE_BUTTON_COLOR = 0xA3D691;
    private final JButton saveButton = new JButton();

    public final JPanel mainPanel;

    /**
     * Constructor of the class
     *
     * @param characterListing the character card that contains the required data to construct this GUI.
     */
    public CharacterCardGUI(CharacterListing characterListing) {
        mainPanel = generateCharacterPage(characterListing);
        mainPanel.setVisible(true);
    }

    /**
     * Adds allowed weapons (that is, wieldable by the specified character) to the list of options in the weapon combobox.
     *
     * @param dcmb weapon selector combo box model used by the combo box
     * @param character is the character's name
     */
    private void addAllowedWeapons(WeaponSelectorComboBoxModel dcmb, Character character) {
        JLabel label = new JLabel();
        label.setText(EMPTY_WEAPON_SELECTOR);
        ImageIcon default_img = getResizedResourceIcon(getPlaceholderIcon(placeholderImageKeys[2]), 20);
        label.setIcon(default_img);
        dcmb.addElement(label);
        for (ToolData.WEAPON_RARITY rarity : ToolData.WEAPON_RARITY.values()) {
            ToolData.WEAPON_TYPE weaponType = ToolData.WEAPON_TYPE.ALL_OPTIONS_BY_STRING.get(character.weaponType);
            label = new JLabel();
            switch (rarity) {
                case FIVE_STAR:
                    label.setText(FIVE_STAR_WEAPON_DELIMITER);
                    break;
                case FOUR_STAR:
                    label.setText(FOUR_STAR_WEAPON_DELIMITER);
                    break;
            }
            label.setIcon(default_img);
            dcmb.addElement(label);
            List<Weapon> weapons = lookUpWeapons(rarity, weaponType);
            for (Weapon weapon : weapons) {
                label = new JLabel();
                label.setIcon(getResizedResourceIcon(weapon.icon, 20));
                label.setText(weapon.name);
                dcmb.addElement(label);
            }
        }
    }

    private JPanel getMiddleSelectorPanel(JPanel jpanel) {
        JPanel middleSelectorPanel = new JPanel();
        middleSelectorPanel.setLayout(new GridLayoutManager(16, 1, new Insets(5, 5, 5, 5), -1, -1));
        middleSelectorPanel.setAlignmentY(0.5f);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(22, 0, 0, 0);
        middleSelectorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        jpanel.add(middleSelectorPanel, gbc);
        return middleSelectorPanel;
    }

    private JTextField getNotesTextField(CharacterListing selectedCharacterListing, JPanel jpanel) {
        JTextField notesTextField = new JTextField();
        changeFont(notesTextField, ToolData.AVAILABLE_FONTS.TEXT_FONT, 14.0F);
        notesTextField.setHorizontalAlignment(10);
        notesTextField.setInheritsPopupMenu(false);
        notesTextField.setMargin(new Insets(2, 6, 2, 6));
        notesTextField.setOpaque(true);
        notesTextField.setDocument(new NotesTextModel(CHARACTER_LIMIT, selectedCharacterListing.getCharacterNotes()));

        jpanel.add(notesTextField,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(50, -1), null, 0, false));
        return notesTextField;
    }

    private JComboBox<JLabel> getWeaponSelectionBox(CharacterListing characterListing, JPanel jpanel) {
        JComboBox<JLabel> weaponSelectionBox = new JComboBox<>();
        weaponSelectionBox.setAutoscrolls(false);
        weaponSelectionBox.setEditable(false);
        changeFont(weaponSelectionBox, ToolData.AVAILABLE_FONTS.HEADER_FONT, 14.0F);
        weaponSelectionBox.setInheritsPopupMenu(false);
        final WeaponSelectorComboBoxModel weaponSelectorComboBoxModel = new WeaponSelectorComboBoxModel();
        addAllowedWeapons(weaponSelectorComboBoxModel, getCharacter(characterListing.getCharacterName()));
        weaponSelectionBox.setModel(weaponSelectorComboBoxModel);
        weaponSelectionBox.setRenderer(new ComboBoxRenderer(weaponSelectionBox));
        setSelection(weaponSelectionBox, characterListing.getWeapon());
        jpanel.add(weaponSelectionBox,
                new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                        GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        return weaponSelectionBox;
    }

    private javax.swing.JLabel getNameLabel(CharacterListing characterListing, JPanel jpanel, GridConstraints gc,
                                            ToolData.CHARACTER_CARD_DATA_FIELD dataField) {
        javax.swing.JLabel setNameLabel = new javax.swing.JLabel();
        setNameLabel.setAutoscrolls(true);
        changeFont(setNameLabel, ToolData.AVAILABLE_FONTS.HEADER_FONT, 18.0F);

        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.SET_ONE) {
            setNameLabel.setText(characterListing.getArtifactSet1());
        }
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.SET_TWO) {
            setNameLabel.setText(characterListing.getArtifactSet2());
        }
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.WEAPON) {
            setNameLabel.setText(characterListing.getWeapon());
        }
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.NAME) {
            setNameLabel.setText(characterListing.getCharacterName());
        }

        jpanel.add(setNameLabel, gc);
        return setNameLabel;
    }

    private JComboBox<JLabel> getSetComboBox(CharacterListing characterListing, JPanel jpanel, GridConstraints gc,
                                             ToolData.CHARACTER_CARD_DATA_FIELD dataField) {
        JComboBox<JLabel> setComboBox = new JComboBox<>();
        setComboBox.setAutoscrolls(false);
        setComboBox.setEditable(false);
        setComboBox.setFocusable(false);
        changeFont(setComboBox, ToolData.AVAILABLE_FONTS.HEADER_FONT, 14.0F);
        setComboBox.setInheritsPopupMenu(false);
        final DefaultComboBoxModel<JLabel> setComboBoxModel = new DefaultComboBoxModel<>();
        JLabel label = new JLabel();
        label.setText(EMPTY_SET_SELECTOR);
        label.setIcon(getResizedResourceIcon(getPlaceholderIcon("artifact"), 20));
        setComboBoxModel.addElement(label);
        for (Item artifact : artifacts) {
            label = new JLabel();
            label.setText(artifact.name);
            label.setIcon(getResizedResourceIcon(artifact.icon, 20));
            setComboBoxModel.addElement(label);
        }
        setComboBox.setModel(setComboBoxModel);
        setComboBox.setRenderer(new ComboBoxRenderer(setComboBox));
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.SET_ONE) {
            setSelection(setComboBox, characterListing.getArtifactSet1());
        }
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.SET_TWO) {
            setSelection(setComboBox, characterListing.getArtifactSet2());
        }

        jpanel.add(setComboBox, gc);
        return setComboBox;
    }

    private void setSelection(JComboBox<JLabel> comboBox, String selectedItemName) {
        if (selectedItemName == null || selectedItemName.isEmpty()) {
            comboBox.setSelectedIndex(0);
            return;
        }
        boolean found = false;
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).getText().equalsIgnoreCase(selectedItemName)) {
                found = true;
                comboBox.setSelectedIndex(i);
            }
        }
        if (!found) {
            comboBox.setSelectedIndex(0);
        }
    }

    private void generateSpacer(JPanel jpanel, GridLayoutManager gridLayoutManager, GridConstraints gridConstraints) {
        JPanel characterWeaponSpacer = new JPanel();
        characterWeaponSpacer.setLayout(gridLayoutManager);
        jpanel.add(characterWeaponSpacer, gridConstraints);
    }

    private JLabel getWeaponIconLabel(CharacterListing characterListing, JPanel jpanel) {
        JLabel weaponIcon = new javax.swing.JLabel();
        weaponIcon.setHorizontalAlignment(4);
        weaponIcon.setHorizontalTextPosition(4);
        String savedWeaponName = characterListing.getWeapon();
        if (savedWeaponName.isEmpty()) {
            weaponIcon.setIcon(getPlaceholderIcon("weapon"));
        } else {
            weaponIcon.setIcon(getWeapon(savedWeaponName).icon);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 0, 0, 20);
        jpanel.add(weaponIcon, gbc);
        return weaponIcon;
    }

    private void getCharLabel(Icon charIcon, JPanel jpanel) {
        javax.swing.JLabel charLabel = new javax.swing.JLabel();
        charLabel.setIcon(charIcon);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(20, 0, 5, 20);
        jpanel.add(charLabel, gbc);
    }

    private JLabel getSetIconLabel(String savedArtifactSetName, JPanel jpanel, GridBagConstraints gbc) {
        JLabel setIcon = new JLabel();
        setIcon.setHorizontalAlignment(4);
        setIcon.setHorizontalTextPosition(4);
        if (savedArtifactSetName.isEmpty()) {
            setIcon.setIcon(getPlaceholderIcon("artifact"));
        } else {
            setIcon.setIcon(getArtifact(savedArtifactSetName).icon);
        }
        jpanel.add(setIcon, gbc);
        return setIcon;
    }

    private JPanel getRightPanel(JPanel jpanel) {
        JPanel checkboxAndButtonPanel = new JPanel();
        checkboxAndButtonPanel.setLayout(new GridLayoutManager(6, 2, new Insets(3, 3, 3, 3), -1, -1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(22, 0, 0, 100);
        jpanel.add(checkboxAndButtonPanel, gbc);
        checkboxAndButtonPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        return checkboxAndButtonPanel;
    }

    private JCheckBox getListingCheckBox(CharacterListing characterListing, JPanel jpanel, String title,
                                         GridConstraints gridConstraints,
                                         ToolData.CHARACTER_CARD_DATA_FIELD dataField) {
        JCheckBox listingCheckBox = new JCheckBox();
        changeFont(listingCheckBox, ToolData.AVAILABLE_FONTS.TEXT_FONT, 14.0F);
        listingCheckBox.setText(title);
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_SET_ONE) {
            if (characterListing.getArtifactSet1Status()) {
                listingCheckBox.doClick();
            }
            if (characterListing.getArtifactSet1().isEmpty()) {
                listingCheckBox.setSelected(false);
                listingCheckBox.setEnabled(false);
            }
        }
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_SET_TWO) {
            if (characterListing.getArtifactSet2Status()) {
                listingCheckBox.doClick();
            }
            if (characterListing.getArtifactSet2().isEmpty()) {
                listingCheckBox.setSelected(false);
                listingCheckBox.setEnabled(false);
            }
        }
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_TALENT_MATERIALS) {
            if (characterListing.getTalentStatus()) {
                listingCheckBox.doClick();
            }
        }
        if (dataField == ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_WEAPON_MATERIALS) {
            if (characterListing.getWeaponStatus()) {
                listingCheckBox.doClick();
            }
            if (characterListing.getWeapon().isEmpty()) {
                listingCheckBox.setSelected(false);
                listingCheckBox.setEnabled(false);
            }
        }
        jpanel.add(listingCheckBox, gridConstraints);
        return listingCheckBox;
    }

    private void getDomainListingsLabel(JPanel jpanel) {
        javax.swing.JLabel domainListingsLabel = new javax.swing.JLabel();
        changeFont(domainListingsLabel, ToolData.AVAILABLE_FONTS.HEADER_FONT, 18.0F);
        domainListingsLabel.setText("Set Information");
        jpanel.add(domainListingsLabel,
                new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
    }

    private void getSaveButton(JPanel jpanel) {
        saveButton.setBackground(new Color(YES_CHANGES_SAVE_BUTTON_COLOR));
        saveButton.setForeground(new Color(-394241));
        saveButton.setText("SAVE");
        changeFont(saveButton, ToolData.AVAILABLE_FONTS.HEADER_FONT, 12.0F);
        jpanel.add(saveButton,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    private JTextArea getSetDetailsTextArea(JPanel jpanel) {
        JTextArea setDetailsTextArea = new JTextArea();
        setDetailsTextArea.setEditable(false);
        setDetailsTextArea.setFocusable(false);
        setDetailsTextArea.setLineWrap(true);
        setDetailsTextArea.setWrapStyleWord(true);
        changeFont(setDetailsTextArea, ToolData.AVAILABLE_FONTS.TEXT_FONT, 12.0F);
        jpanel.add(setDetailsTextArea,
                new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW,
                        new Dimension(150, 380), new Dimension(150, 380), null, 0, false));
        return setDetailsTextArea;
    }

    /**
     * Generates a character page (in the tabbed pane).
     *
     * @param characterListing the character card
     * @return the JPanel that is the character page.
     */
    private JPanel generateCharacterPage(CharacterListing characterListing) {
        JPanel templateTab = new JPanel();
        assert characterListing != null;
        templateTab.setLayout(new GridBagLayout());
        JPanel middleSelectorPanel = getMiddleSelectorPanel(templateTab);
        JTextField notesTextField = getNotesTextField(characterListing, middleSelectorPanel);
        getNameLabel(characterListing, middleSelectorPanel,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false),
                ToolData.CHARACTER_CARD_DATA_FIELD.NAME);
        JComboBox<JLabel> weaponSelectionBox = getWeaponSelectionBox(characterListing, middleSelectorPanel);
        javax.swing.JLabel weaponNameLabel = getNameLabel(characterListing, middleSelectorPanel,
                new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(177, 23), null, 1, false), ToolData.CHARACTER_CARD_DATA_FIELD.WEAPON);
        javax.swing.JLabel set1NameLabel = getNameLabel(characterListing, middleSelectorPanel,
                new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(177, 23), null, 1, false), ToolData.CHARACTER_CARD_DATA_FIELD.SET_ONE);
        javax.swing.JLabel set2NameLabel = getNameLabel(characterListing, middleSelectorPanel,
                new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
                        new Dimension(177, 23), null, 1, false), ToolData.CHARACTER_CARD_DATA_FIELD.SET_TWO);
        JComboBox<JLabel> set1ComboBox = getSetComboBox(characterListing, middleSelectorPanel,
                new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                        GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false),
                ToolData.CHARACTER_CARD_DATA_FIELD.SET_ONE);
        JComboBox<JLabel> set2ComboBox = getSetComboBox(characterListing, middleSelectorPanel,
                new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                        GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false),
                ToolData.CHARACTER_CARD_DATA_FIELD.SET_TWO);

        generateSpacer(middleSelectorPanel, new GridLayoutManager(1, 1, new Insets(0, 0, 7, 0), -1, -1),
                new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
        generateSpacer(middleSelectorPanel, new GridLayoutManager(1, 1, new Insets(15, 0, 0, 0), -1, -1),
                new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
        generateSpacer(middleSelectorPanel, new GridLayoutManager(1, 1, new Insets(15, 0, 0, 0), -1, -1),
                new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));
        generateSpacer(middleSelectorPanel, new GridLayoutManager(1, 1, new Insets(20, 0, 0, 0), -1, -1),
                new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null,
                        0, false));

        javax.swing.JLabel weaponJLabel = getWeaponIconLabel(characterListing, templateTab);

        getCharLabel(getCharacter(characterListing.getCharacterName()).icon, templateTab);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(5, 0, 0, 20);
        javax.swing.JLabel set1Icon = getSetIconLabel(characterListing.getArtifactSet1(), templateTab, gbc);
        gbc.gridy++;
        javax.swing.JLabel set2Icon = getSetIconLabel(characterListing.getArtifactSet2(), templateTab, gbc);

        JPanel checkboxAndButtonPanel = getRightPanel(templateTab);
        JCheckBox artifactSet1ListingCheckBox =
                getListingCheckBox(characterListing, middleSelectorPanel, "Artifact Listing",
                        new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_SET_ONE);
        JCheckBox artifactSet2ListingCheckBox =
                getListingCheckBox(characterListing, middleSelectorPanel, "Artifact Listing",
                        new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_SET_TWO);
        JCheckBox talentListingCheckBox = getListingCheckBox(characterListing, middleSelectorPanel, "Talent Listing",
                new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_TALENT_MATERIALS);
        JCheckBox weaponMaterialListingCheckbox =
                getListingCheckBox(characterListing, middleSelectorPanel, "Weapon Material Listing",
                        new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE,
                                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false),
                        ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_WEAPON_MATERIALS);
        getDomainListingsLabel(checkboxAndButtonPanel);
        artifactSet1ListingCheckBox.addItemListener(new UpdateCharacterCardCheckBoxListener(characterListing,
                ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_SET_ONE, saveButton));
        artifactSet2ListingCheckBox.addItemListener(new UpdateCharacterCardCheckBoxListener(characterListing,
                ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_SET_TWO, saveButton));
        weaponMaterialListingCheckbox.addItemListener(new UpdateCharacterCardCheckBoxListener(characterListing,
                ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_WEAPON_MATERIALS, saveButton));
        talentListingCheckBox.addItemListener(new UpdateCharacterCardCheckBoxListener(characterListing,
                ToolData.CHARACTER_CARD_DATA_FIELD.FARMING_TALENT_MATERIALS, saveButton));
        set1ComboBox.addActionListener(new UpdateLabelListener(set1NameLabel, set1Icon, artifactSet1ListingCheckBox,
                ToolData.RESOURCE_TYPE.ARTIFACT));
        set2ComboBox.addActionListener(new UpdateLabelListener(set2NameLabel, set2Icon, artifactSet2ListingCheckBox,
                ToolData.RESOURCE_TYPE.ARTIFACT));
        set1ComboBox.addItemListener(
                new UpdateCharacterCardComboBoxListener(characterListing, ToolData.CHARACTER_CARD_DATA_FIELD.SET_ONE,
                        saveButton));
        set2ComboBox.addItemListener(
                new UpdateCharacterCardComboBoxListener(characterListing, ToolData.CHARACTER_CARD_DATA_FIELD.SET_TWO,
                        saveButton));
        weaponSelectionBox.addActionListener(
                new UpdateLabelListener(weaponNameLabel, weaponJLabel, weaponMaterialListingCheckbox,
                        ToolData.RESOURCE_TYPE.WEAPON_NAME));
        weaponSelectionBox.addItemListener(
                new UpdateCharacterCardComboBoxListener(characterListing, ToolData.CHARACTER_CARD_DATA_FIELD.WEAPON,
                        saveButton));

        getSaveButton(checkboxAndButtonPanel);
        JTextArea setDetailsTextArea = getSetDetailsTextArea(checkboxAndButtonPanel);

        saveButton.addActionListener(new SaveButtonListener(characterListing));
        saveButton.setEnabled(false);
        set1ComboBox.addActionListener(new UpdateTextAreaListener(setDetailsTextArea));
        set2ComboBox.addActionListener(new UpdateTextAreaListener(setDetailsTextArea));
        notesTextField.getDocument().addDocumentListener(new NotesListener(characterListing, saveButton));

        return templateTab;
    }
}
