package Files.Code.GUIs;

import static Files.Code.Data.ToolData.AVAILABLE_FONTS;
import static Files.Code.Data.ToolData.SAVE_LOCATION;
import static Files.Code.Data.ToolData.WEAPON_TYPE.ALL_OPTIONS_BY_ENUM;
import static Files.Code.Data.ToolData.WEAPON_TYPE.ALL_OPTIONS_BY_STRING;
import static Files.Code.Data.ToolData.WEAPON_TYPE.NO_FILTER;
import static Files.Code.Data.ToolData.changeFont;
import static Files.Code.Data.ToolData.getWeapon;
import static Files.Code.Data.ToolData.getWeaponMaterial;
import static Files.Code.GUIs.ToolGUI.WEAPON_SAVE_FILE_NAME;
import static Files.Code.GUIs.ToolGUI.formatString;
import static Files.Code.GUIs.ToolGUI.isSomeoneFarmingForTheWeapon;

import Files.Code.Auxiliary.ComboBoxRenderer;
import Files.Code.Auxiliary.SearchBarListener;
import Files.Code.Auxiliary.WeaponAdapter;
import Files.Code.Auxiliary.WeaponTabGUIListener;
import Files.Code.Data.ToolData;
import Files.Code.Data.Weapon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class generates the weapon tab of the main application window.
 */

public class WeaponTabGUI implements ItemListener, ActionListener {

    private final JPanel mainPanel = new JPanel(new GridBagLayout());
    private final JScrollPane devWeaponTabScrollPane = new JScrollPane();
    private final JTextField devWeaponsTabSearchbar = new JTextField();
    private final JButton devWeaponTabSearchButton = new JButton();
    private final JPanel devWeaponTabScrollPanePanel = new JPanel();
    private final JCheckBox showListedCheckBox = new JCheckBox();
    private final JCheckBox showUnlistedCheckBox = new JCheckBox();
    private final JLabel showMatchedAmountLabel = new JLabel();
    private static final JComboBox<JLabel> devFilterComboBox = new JComboBox<>();
    private static final Set<Weapon> unassignedFarmedWeapons = new HashSet<>();

    /**
     * Search flag enum.
     */
    public enum SEARCH_FLAG {
        /**
         * All weapons
         */
        ALL,
        /**
         * Listed weapons only
         */
        LISTED_ONLY,
        /**
         * Unlisted weapons only
         */
        UNLISTED_ONLY
    }

    /**
     * Constructor of the class.
     */
    public WeaponTabGUI() {
        setUpWeaponsPanel();
        parseWeaponsMap();
    }

    /**
     * Returns the main panel. Only used in ToolGUI.java
     *
     * @return main panel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    private static void parseWeaponsMap() {
        TypeToken<Set<Weapon>> token = new TypeToken<Set<Weapon>>() {
        };
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(token.getType(), new WeaponAdapter()).create();
        File f = new File(SAVE_LOCATION + WEAPON_SAVE_FILE_NAME);
        if (!f.exists()) {
            return;
        }
        try {
            JsonReader reader = new JsonReader(new FileReader(f));

            TreeSet<Weapon> map = new TreeSet<>(ToolData.comparator);
            map.addAll(gson.fromJson(reader, token));

            if (!map.isEmpty()) {
                for (Weapon weapon : map) {
                    unassignedFarmedWeapons.add(getWeapon(weapon.name));
                }
            }
        } catch (IOException ex) {
            System.out.println("The weapon save file failed to parse.");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        showListedCheckBox.setEnabled(false);
        showUnlistedCheckBox.setEnabled(false);
        parseSearch(getSearchFlag());
        showListedCheckBox.setEnabled(true);
        showUnlistedCheckBox.setEnabled(true);
    }

    private SEARCH_FLAG getSearchFlag() {
        if (showListedCheckBox.isSelected() && !showUnlistedCheckBox.isSelected()) {
            return SEARCH_FLAG.LISTED_ONLY;
        } else if (showUnlistedCheckBox.isSelected() && !showListedCheckBox.isSelected()) {
            return SEARCH_FLAG.UNLISTED_ONLY;
        } else {
            return SEARCH_FLAG.ALL;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton triggerButton = (JButton) e.getSource();
        triggerButton.setEnabled(false);
        parseSearch(getSearchFlag());
        triggerButton.setEnabled(true);
    }

    /**
     * Returns the mapping that contains all weapons which are listed on this tab only.
     * Note it is saved separately from character cards.
     *
     * @return set of all weapons
     */
    public static Set<Weapon> getUnassignedFarmedWeapons() {
        return unassignedFarmedWeapons;
    }

    private void parseSearch(SEARCH_FLAG flag) {
        String userFieldInput = devWeaponsTabSearchbar.getText().toLowerCase();

        if (userFieldInput.equals("search by name!")) {
            userFieldInput = "";
        }

        devWeaponTabScrollPanePanel.removeAll();
        devWeaponTabScrollPane.updateUI();
        parseWeaponsMap();
        int matchedCount = 0;
        for (Weapon weapon : ToolData.weapons) {
            JLabel label = (JLabel) devFilterComboBox.getSelectedItem();
            assert label != null;
            ToolData.WEAPON_TYPE filter = ALL_OPTIONS_BY_STRING.get(label.getText());
            assert filter != null;

            if (inputMatchesFilters(userFieldInput, weapon, filter, flag)) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = matchedCount % 3;
                gbc.gridy = (matchedCount - gbc.gridx) / 3;
                gbc.anchor = GridBagConstraints.NORTH;
                gbc.insets = new Insets(10, 10, 10, 10);
                devWeaponTabScrollPanePanel.add(generateWeaponCard(weapon), gbc);

                matchedCount++;
            }

        }
        showMatchedAmountLabel.setText("Matches: " + matchedCount);
        changeFont(showMatchedAmountLabel, AVAILABLE_FONTS.BLACK_FONT, 12);
    }

    private boolean inputMatchesFilters(String input, Weapon weapon, ToolData.WEAPON_TYPE filter, SEARCH_FLAG flag) {
        if (weapon.name.toLowerCase().contains(input.toLowerCase()) &&
                (weapon.weaponType.equalsIgnoreCase(filter.stringToken) || filter == NO_FILTER)) {
            boolean isTheWeaponListed =
                    isSomeoneFarmingForTheWeapon(weapon) || unassignedFarmedWeapons.contains(weapon);
            if (isTheWeaponListed && flag == SEARCH_FLAG.LISTED_ONLY) {
                return true;
            }
            if (!isTheWeaponListed && flag == SEARCH_FLAG.UNLISTED_ONLY) {
                return true;
            }
            return flag == SEARCH_FLAG.ALL;
        }
        return false;
    }

    private JPanel generateWeaponCard(Weapon weapon) {

        // WEAPON CARD PANEL
        JPanel devWeaponCard = new JPanel();
        devWeaponCard.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
        devWeaponCard.setBackground(new Color(-1));
        devWeaponCard.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        devWeaponCard.setPreferredSize(new Dimension(300, 190));

        // WEAPON ICON AND NAME
        JLabel devWeaponIcon = new JLabel();
        devWeaponIcon.setHorizontalAlignment(0);
        devWeaponIcon.setHorizontalTextPosition(0);
        devWeaponIcon.setIcon(weapon.icon);
        devWeaponIcon.setText(formatString(weapon.name));
        changeFont(devWeaponIcon, AVAILABLE_FONTS.BLACK_FONT, 12);
        devWeaponIcon.setVerticalAlignment(0);
        devWeaponIcon.setVerticalTextPosition(3);
        devWeaponCard.add(devWeaponIcon,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));

        // WEAPON LISTING CHECK BOX
        JCheckBox devWepMatListingCheckbox = new JCheckBox();
        devWepMatListingCheckbox.setBackground(new Color(-1));
        if (isSomeoneFarmingForTheWeapon(weapon)) {
            devWepMatListingCheckbox.setSelected(true);
            devWepMatListingCheckbox.setEnabled(false);
            devWepMatListingCheckbox.setText("Already Farmed");
        } else {
            devWepMatListingCheckbox.setSelected(unassignedFarmedWeapons.contains(weapon));
            devWepMatListingCheckbox.setText("List weapon?");
        }
        changeFont(devWepMatListingCheckbox, AVAILABLE_FONTS.TEXT_FONT, 12);
        devWepMatListingCheckbox.addItemListener(new WeaponTabGUIListener(weapon));
        devWeaponCard.add(devWepMatListingCheckbox,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));

        // WEAPON MATERIAL PREVIEW ICON
        JLabel devWepMaterialPreview = new JLabel();
        devWepMaterialPreview.setHorizontalAlignment(0);
        devWepMaterialPreview.setHorizontalTextPosition(0);
        devWepMaterialPreview.setIcon(getWeaponMaterial(weapon.ascensionMaterial).icon);
        devWepMaterialPreview.setText("");
        devWepMaterialPreview.setVerticalAlignment(0);
        devWepMaterialPreview.setVerticalTextPosition(3);
        devWeaponCard.add(devWepMaterialPreview,
                new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));

        // WEAPON TYPE LABEL
        JLabel devWepTypeLabel = new JLabel();
        devWepTypeLabel.setText("Type: " + weapon.weaponType);
        changeFont(devWepTypeLabel, AVAILABLE_FONTS.TEXT_FONT, 12);
        devWeaponCard.add(devWepTypeLabel,
                new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        return devWeaponCard;
    }

    private void setUpWeaponsPanel() {

        // WEAPON PANEL
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel overviewPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(overviewPanel, gbc);

        // SCROLL PANE
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        overviewPanel.add(devWeaponTabScrollPane, gbc);
        devWeaponTabScrollPanePanel.setLayout(new GridBagLayout());
        devWeaponTabScrollPane.setViewportView(devWeaponTabScrollPanePanel);
        devWeaponTabScrollPane.updateUI();

        // SEARCH BAR
        devWeaponsTabSearchbar.setEnabled(true);
        devWeaponsTabSearchbar.setInheritsPopupMenu(false);
        devWeaponsTabSearchbar.setMaximumSize(new Dimension(240, 33));
        devWeaponsTabSearchbar.setMinimumSize(new Dimension(240, 33));
        devWeaponsTabSearchbar.setPreferredSize(new Dimension(240, 33));
        devWeaponsTabSearchbar.setText("Search by name!");
        changeFont(devWeaponsTabSearchbar, AVAILABLE_FONTS.BLACK_FONT, 18);
        devWeaponsTabSearchbar.addMouseListener(new SearchBarListener());
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(devWeaponsTabSearchbar, gbc);

        // SEARCH BUTTON
        devWeaponTabSearchButton.setMinimumSize(new Dimension(50, 30));
        devWeaponTabSearchButton.setPreferredSize(new Dimension(50, 30));
        devWeaponTabSearchButton.setText("✓");
        devWeaponTabSearchButton.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(devWeaponTabSearchButton, gbc);

        // FILTER COMBO BOX
        devFilterComboBox.setBackground(new Color(-2702645));
        devFilterComboBox.setEnabled(true);
        final DefaultComboBoxModel<JLabel> weaponFilterComboBoxModel = new DefaultComboBoxModel<>();

        for (ToolData.WEAPON_TYPE options : ALL_OPTIONS_BY_ENUM.keySet()) {
            JLabel label = new JLabel();
            label.setText(options.stringToken);
            weaponFilterComboBoxModel.addElement(label);
        }
        changeFont(devFilterComboBox, AVAILABLE_FONTS.BLACK_FONT, 12);
        devFilterComboBox.setModel(weaponFilterComboBoxModel);
        devFilterComboBox.addItemListener(this);
        devFilterComboBox.setRenderer(new ComboBoxRenderer(devFilterComboBox));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 5, 0, 5);
        mainPanel.add(devFilterComboBox, gbc);

        // SHOW UNLISTED/LISTED COMBO BOX
        showListedCheckBox.setBackground(new Color(-2702645));
        showListedCheckBox.setForeground(new Color(-15072759));
        showListedCheckBox.setText("Show listed ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        mainPanel.add(showListedCheckBox, gbc);
        showUnlistedCheckBox.setBackground(new Color(-2702645));
        showUnlistedCheckBox.setForeground(new Color(-15072759));
        showUnlistedCheckBox.setText("Show unlisted ");
        changeFont(showListedCheckBox, AVAILABLE_FONTS.BLACK_FONT, 12);
        changeFont(showUnlistedCheckBox, AVAILABLE_FONTS.BLACK_FONT, 12);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        mainPanel.add(showUnlistedCheckBox, gbc);

        showListedCheckBox.addItemListener(this);
        showUnlistedCheckBox.addItemListener(this);
        showMatchedAmountLabel.setForeground(new Color(-15072759));

        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 5, 0, 5);
        mainPanel.add(showMatchedAmountLabel, gbc);
    }

}
