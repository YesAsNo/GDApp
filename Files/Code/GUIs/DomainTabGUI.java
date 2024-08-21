package Files.Code.GUIs;

import static Files.Code.Data.ToolData.RESOURCE_TYPE.WEEKLY_BOSS_MATERIAL;
import static Files.Code.Data.ToolData.changeFont;
import static Files.Code.Data.ToolData.comparator;
import static Files.Code.Data.ToolData.domains;
import static Files.Code.Data.ToolData.getCharacter;
import static Files.Code.Data.ToolData.getWeapon;
import static Files.Code.GUIs.DomainTabGUI.DOMAIN_FILTER_OPTIONS.ALL_OPTIONS_BY_ENUM;
import static Files.Code.GUIs.DomainTabGUI.DOMAIN_FILTER_OPTIONS.ALL_OPTIONS_BY_STRING;
import static Files.Code.GUIs.DomainTabGUI.DOMAIN_FILTER_OPTIONS.NO_FILTER;
import static Files.Code.GUIs.ToolGUI.farmedArtifacts;
import static Files.Code.GUIs.ToolGUI.farmedTalentBooks;
import static Files.Code.GUIs.ToolGUI.farmedWeapons;
import static Files.Code.GUIs.ToolGUI.farmedWeeklyTalentMaterials;
import static Files.Code.GUIs.WeaponTabGUI.getUnassignedFarmedWeapons;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

import Files.Code.Auxiliary.ComboBoxRenderer;
import Files.Code.Data.Artifact;
import Files.Code.Data.Character;
import Files.Code.Data.Domain;
import Files.Code.Data.FarmableItem;
import Files.Code.Data.Item;
import Files.Code.Data.TalentMaterial;
import Files.Code.Data.ToolData;
import Files.Code.Data.Weapon;
import Files.Code.Data.WeaponMaterial;
import Files.Code.Data.WeeklyTalentMaterial;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class constructs the domain tab GUI, available from the Domains tab in the main application.
 */
public class DomainTabGUI implements ActionListener {
    private final JPanel domainTab = new JPanel(new GridBagLayout());
    private final JComboBox<JLabel> filterBox = new JComboBox<>();
    private final JPanel domainsPanelOverview = new JPanel(new GridBagLayout());
    private static final JRadioButton wedSatButton = new JRadioButton();
    private static final JRadioButton tueFriButton = new JRadioButton();
    private static final JRadioButton monThuButton = new JRadioButton();
    private static final JRadioButton allButton = new JRadioButton();
    private static final ButtonGroup bg_dayFilter = new ButtonGroup();

    static {
        bg_dayFilter.add(wedSatButton);
        bg_dayFilter.add(tueFriButton);
        bg_dayFilter.add(monThuButton);
        bg_dayFilter.add(allButton);
    }

    private static final JRadioButton showAllButton = new JRadioButton();
    private static final JRadioButton showListedButton = new JRadioButton();
    private static final ButtonGroup bg_listedFilter = new ButtonGroup();

    static {
        bg_listedFilter.add(showAllButton);
        bg_listedFilter.add(showListedButton);
        showAllButton.setSelected(true);
    }
    private static final JTabbedPane mainTabbedPane = new JTabbedPane(SwingConstants.TOP);


    @Override
    public void actionPerformed(ActionEvent e) {
        JLabel label = (JLabel) filterBox.getSelectedItem();
        DOMAIN_FILTER_OPTIONS option = ALL_OPTIONS_BY_STRING.get(label.getText());
        parseFilter(option, getDayFilter(), showAllButton.isSelected());
    }

    /**
     * Enum for Day filter.
     */
    public enum DAY_FILTER {
        /**
         * Monday/Thursday only
         */
        MONDAY_THURSDAY("Mon/Thu"),
        /**
         * Tuesday/Friday only
         */
        TUESDAY_FRIDAY("Tues/Fri"),
        /**
         * Wednesday/Saturday only
         */
        WEDNESDAY_SATURDAY("Wed/Sat"),
        /**
         * Sunday only. (On Sunday, every domain and every material within can be farmed)
         */
        SUNDAY_ALL("All");
        /**
         * The string token for the respective enum value.
         */
        public final String stringToken;

        DAY_FILTER(String token) {
            stringToken = token;
        }

        /**
         * Map containing all enum values, mapped to the respective string values.
         */
        public static final TreeMap<DAY_FILTER, String> ALL_OPTIONS_BY_ENUM = new TreeMap<>();

        static {
            for (DAY_FILTER v : DAY_FILTER.values()) {
                ALL_OPTIONS_BY_ENUM.put(v, v.stringToken);
            }
        }

    }

    /**
     * Enum containing different domain themes. There are four domain types available ingame, represented by the enums.
     */
    public enum DOMAIN_THEME {
        /**
         * Weapon material domain theme.
         */
        WEAPON_MATERIAL_THEME(-10301, -13494016, -26768, "♥"),
        /**
         * Talent book domain theme.
         */
        TALENT_BOOK_THEME(-1068, -14541824, -10640, "♣"),
        /**
         * Weekly boss domain theme.
         */
        WEEKLY_BOSS_DOMAIN_THEME(-11811, -13236722, -36698, "♠"),
        /**
         * Artifact domain theme.
         */
        ARTIFACT_DOMAIN_THEME(-2756865, -16575201, -9382145, "♦");
        /**
         * Panel background color for a theme.
         */
        public final int panelBackgroundColor;
        /**
         * Panel foreground color for a theme.
         */
        public final int panelForegroundColor;
        /**
         * Margin panel background color.
         */
        public final int marginBackgroundColor;
        /**
         * Margin panel symbol.
         */
        public final String marginSymbol;

        DOMAIN_THEME(int bgColor, int fgColor, int marginBgColor, String symbol) {
            this.panelBackgroundColor = bgColor;
            this.panelForegroundColor = fgColor;
            this.marginBackgroundColor = marginBgColor;
            this.marginSymbol = symbol;
        }
    }

    /**
     * Enum representing domain filter options.
     */
    public enum DOMAIN_FILTER_OPTIONS {
        /**
         * All domains
         */
        NO_FILTER("All Domains"),
        /**
         * Artifact domains only
         */
        ARTIFACT("Artifact"),
        /**
         * Talent book domains only
         */
        TALENT("Talent Book"),
        /**
         * Weekly boss domains only
         */
        WEEKLY("Weekly Boss Material"),
        /**
         * Weapon material domains only
         */
        WEAPON_MAT("Weapon Material");
        /**
         * Mapping containing all filter options, mapped to their string tokens.
         */
        public static final Map<DOMAIN_FILTER_OPTIONS, String> ALL_OPTIONS_BY_ENUM = new TreeMap<>();
        /**
         * Mapping containing all filter options, mapped to their enums
         */
        public static final Map<String, DOMAIN_FILTER_OPTIONS> ALL_OPTIONS_BY_STRING = new TreeMap<>();

        static {
            for (DOMAIN_FILTER_OPTIONS e : values()) {
                ALL_OPTIONS_BY_ENUM.put(e, e.stringToken);
                ALL_OPTIONS_BY_STRING.put(e.stringToken, e);
            }
        }

        /**
         * The string token for every enum
         */
        public final String stringToken;

        DOMAIN_FILTER_OPTIONS(String stringToken) {
            this.stringToken = stringToken;
        }
    }

    /**
     * Constructor of the class.
     */
    public DomainTabGUI() {

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        domainTab.add(mainTabbedPane, gbc);
        JPanel searchTab = new JPanel(new GridBagLayout());
        mainTabbedPane.addTab("SEARCH", searchTab);
        searchTab.setEnabled(true);
        changeFont(searchTab, ToolData.AVAILABLE_FONTS.REGULAR_FONT, 15.0F);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        searchTab.add(filterPanel,gbc);

        // SHOW UNLISTED (ALL) BUTTON
        showAllButton.setBackground(new Color(-2702645));
        showAllButton.setForeground(new Color(-13236722));
        showAllButton.setText("Show all ");
        changeFont(showAllButton, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 50, 0, 0);
        filterPanel.add(showAllButton, gbc);

        // SHOW LISTED BUTTON
        showListedButton.setBackground(new Color(-2702645));
        showListedButton.setForeground(new Color(-13236722));
        showListedButton.setText("Show listed ");
        changeFont(showListedButton, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        filterPanel.add(showListedButton, gbc);

        // FILTER COMBO BOX
        filterBox.setBackground(new Color(-2702645));
        filterBox.setEnabled(true);
        final DefaultComboBoxModel<JLabel> filterBoxModel = new DefaultComboBoxModel<>();
        for (DOMAIN_FILTER_OPTIONS option : ALL_OPTIONS_BY_ENUM.keySet()) {
            JLabel label = new JLabel();
            label.setText(option.stringToken);
            filterBoxModel.addElement(label);
        }
        filterBox.setModel(filterBoxModel);
        filterBox.setSelectedIndex(0);
        filterBox.addActionListener(this);
        filterBox.setRenderer(new ComboBoxRenderer(filterBox));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 5, 0, 5);
        changeFont(filterBox, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
        filterPanel.add(filterBox, gbc);
        filterPanel.setBackground(new Color(-1));

        // DAY FILTER BUTTONS
        NavigableSet<DAY_FILTER> dayFilters = DAY_FILTER.ALL_OPTIONS_BY_ENUM.navigableKeySet();
        Iterator<DAY_FILTER> i = dayFilters.iterator();
        int c = 0;
        for (Enumeration<AbstractButton> it = bg_dayFilter.getElements(); it.hasMoreElements(); ) {
            AbstractButton button = it.nextElement();
            DAY_FILTER f = i.next();
            button.setText(f.stringToken);
            if (f.stringToken.equalsIgnoreCase(getDayFilterToken())) {
                button.setSelected(true);
                button.setBackground(new Color(-5275240));
                button.setForeground(new Color(-1));
                button.setToolTipText("(It is today)");
                changeFont(button, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
            } else {
                button.setBackground(new Color(-2702645));
                button.setForeground(new Color(-13236722));
                changeFont(button, ToolData.AVAILABLE_FONTS.BLACK_FONT, 12);
            }
            gbc = new GridBagConstraints();
            gbc.gridx = 3 + c;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 5);
            gbc.anchor = GridBagConstraints.WEST;
            c++;
            filterPanel.add(button, gbc);
        }

        // DOMAINS TAB PANEL

        JScrollPane domainsScrollPane = new JScrollPane(domainsPanelOverview);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        searchTab.add(domainsScrollPane, gbc);

        wedSatButton.addActionListener(this);
        monThuButton.addActionListener(this);
        tueFriButton.addActionListener(this);
        allButton.addActionListener(this);
        showAllButton.addActionListener(this);
        showListedButton.addActionListener(this);
    }

    private String getDayFilter() {
        for (Enumeration<AbstractButton> it = bg_dayFilter.getElements(); it.hasMoreElements(); ) {
            AbstractButton button = it.nextElement();
            if (button.getModel() == bg_dayFilter.getSelection()) {
                return button.getText();
            }
        }
        return allButton.getText();
    }

    private void parseFilter(DOMAIN_FILTER_OPTIONS filter, String dayFilter, boolean status) {
        Set<Domain> filteredDomains = new TreeSet<>(ToolData.comparator);
        domainsPanelOverview.removeAll();

        int i = 0;
        assert filter != null;
        if (filter == NO_FILTER) {
            filteredDomains = domains;
        } else {
            for (Domain domain : domains) {
                if (domain.type.equalsIgnoreCase(filter.stringToken)) {
                    filteredDomains.add(domain);
                }
            }
        }

        for (Domain domain : filteredDomains) {
            if (status || isSomethingFarmedInThisDomain(domain)) {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = i++;
                gbc.weightx = 1.0;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(5, 100, 5, 100);
                domainsPanelOverview.add(generateDomainCard(domain, dayFilter), gbc);

            }
        }
        domainsPanelOverview.updateUI();
    }

    /**
     * Returns whether something is farmed in this domain.
     *
     * @param domain domain
     * @return true if something is farmed, false if not.
     */
    public static boolean isSomethingFarmedInThisDomain(Domain domain) {
        return !whoNeedsThisDomain(domain, true).isEmpty();
    }

    /**
     * Returns a domain theme based on the domain supplied.
     *
     * @param domain Domain
     * @return domain theme
     */
    public static DOMAIN_THEME getDomainTheme(Domain domain) {
        if (domain.isArtifactDomain()) {
            return DOMAIN_THEME.ARTIFACT_DOMAIN_THEME;
        } else if (domain.isTalentMaterialDomain()) {
            return DOMAIN_THEME.TALENT_BOOK_THEME;
        } else if (domain.isWeaponMaterialDomain()) {
            return DOMAIN_THEME.WEAPON_MATERIAL_THEME;
        } else if (domain.isWeeklyTalentMaterialDomain()) {
            return DOMAIN_THEME.WEEKLY_BOSS_DOMAIN_THEME;
        } else {
            throw new IllegalArgumentException("Unknown domain type");
        }
    }

    private JPanel generateDomainCard(Domain domain, String dayFilter) {
        DOMAIN_THEME dt = getDomainTheme(domain);
        JPanel domainCard = new JPanel(new GridBagLayout());
        domainCard.setBackground(new Color(dt.panelBackgroundColor));
        domainCard.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        int i = 0;
        for (FarmableItem item : domain.materials) {

            JLabel materialIconLabel = new JLabel();

            if (dayFilter.equalsIgnoreCase(DAY_FILTER.SUNDAY_ALL.stringToken) || !domain.rotates ||
                    item.availability.equalsIgnoreCase(dayFilter)) {
                materialIconLabel.setIcon(item.icon);
            } else {
                materialIconLabel.setIcon(new ImageIcon(GrayFilter.createDisabledImage(item.icon.getImage())));
            }
            materialIconLabel.setToolTipText(item.name);
            materialIconLabel.setText("");
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 2 + i;
            gbc.gridy = 0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER;
            domainCard.add(materialIconLabel, gbc);
            i++;
        }

        // DOMAIN INFO PANEL
        JPanel domainInfoPanel = new JPanel();
        domainInfoPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        domainInfoPanel.setAlignmentX(0.5f);
        domainInfoPanel.setAlignmentY(0.5f);
        domainInfoPanel.setBackground(new Color(dt.panelBackgroundColor));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        domainCard.add(domainInfoPanel, gbc);
        JLabel domainNameLabel = new JLabel();
        changeFont(domainNameLabel, ToolData.AVAILABLE_FONTS.HEADER_FONT, 18);
        domainNameLabel.setForeground(new Color(dt.panelForegroundColor));
        domainNameLabel.setText(domain.name);
        domainInfoPanel.add(domainNameLabel,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));

        // CHARACTERS LISTED FOR THIS DOMAIN LABEL
        JLabel domainListedCounterLabel = new JLabel();
        changeFont(domainListedCounterLabel, ToolData.AVAILABLE_FONTS.TEXT_FONT, 16);
        domainListedCounterLabel.setForeground(new Color(dt.panelForegroundColor));
        domainListedCounterLabel.setText(getListedCounterLabel(domain));
        domainInfoPanel.add(domainListedCounterLabel,
                new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));

        // PANEL WITH ICON
        JPanel marginPanel = new JPanel();
        marginPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 5, 0, 5), -1, -1));
        marginPanel.setAlignmentX(0.5f);
        marginPanel.setAlignmentY(0.5f);
        marginPanel.setBackground(new Color(dt.marginBackgroundColor));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 0);
        domainCard.add(marginPanel, gbc);
        JLabel domainIconLabel = new JLabel();
        domainIconLabel.setAlignmentX(0.5f);
        domainIconLabel.setFocusTraversalPolicyProvider(false);
        domainIconLabel.setFocusable(false);
        changeFont(domainIconLabel, ToolData.AVAILABLE_FONTS.BLACK_FONT, 16);
        domainIconLabel.setForeground(new Color(-1));
        domainIconLabel.setText(dt.marginSymbol);
        marginPanel.add(domainIconLabel,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
                        GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
                        false));
        domainCard.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //TODO: fix
                //new DomainCardGUI(domain, dt);
                int tabIndex = mainTabbedPane.indexOfTab(domain.name);
                if (tabIndex != -1)
                {
                    mainTabbedPane.setSelectedIndex(tabIndex);
                }
                else
                {
                    mainTabbedPane.addTab(domain.name,new DomainCardGUI(domain,dt).getMainPanel());
                    int newTabIndex = mainTabbedPane.indexOfTab(domain.name);
                    JPanel panelTab = new JPanel(new GridBagLayout());
                    panelTab.setOpaque(false);
                    JLabel labelTitle = new JLabel(domain.name);

                    // ADD ICON AND RESIZE
                    String partialPath = "Files/Images/Placeholders/x_icon.png";
                    String iconPath = System.getProperty("user.dir") + "/" + partialPath;

                    ImageIcon closeIcon = new ImageIcon(iconPath);
                    Image resizedImage = closeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    ImageIcon resizedIcon = new ImageIcon(resizedImage);

                    // TAB CLOSE BUTTON
                    JButton closeButton = new JButton(resizedIcon);

                    closeButton.setMinimumSize(new Dimension(20, 20));
                    closeButton.setPreferredSize(new Dimension(20, 20));

                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.weightx = 1;

                    panelTab.add(labelTitle,gbc);

                    gbc.gridx++;
                    gbc.weightx = 0;

                    // Add space around the button (outside the button itself)
                    gbc.insets = new Insets(2, 5, 2, 5); // Top, Left, Bottom, Right (5 pixels of space on the left and right)

                    panelTab.add(closeButton,gbc);

                    mainTabbedPane.setTabComponentAt(newTabIndex,panelTab);

                    closeButton.addActionListener(new TabCloseButtonActionHandler(domain.name,mainTabbedPane));

                }
            }
        });
        return domainCard;
    }

    /**
     * Returns the TYPE of the resources farmed in this domain.
     *
     * @param dt domain theme
     * @return respective resource type
     */
    public static ToolData.RESOURCE_TYPE getDomainResourceType(DOMAIN_THEME dt) {
        switch (dt) {
            case WEAPON_MATERIAL_THEME:
                return ToolData.RESOURCE_TYPE.WEAPON_MATERIAL;
            case TALENT_BOOK_THEME:
                return ToolData.RESOURCE_TYPE.TALENT_BOOK;
            case WEEKLY_BOSS_DOMAIN_THEME:
                return WEEKLY_BOSS_MATERIAL;
            case ARTIFACT_DOMAIN_THEME:
                return ToolData.RESOURCE_TYPE.ARTIFACT;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the type of the "benecifiary" of the farmed resoruces. For weapon materials, these are the weapons, and for everything else, these are characters.
     *
     * @param dt domain theme
     * @return the target resource type.
     */
    public static ToolData.RESOURCE_TYPE getDomainTargetResourceType(DOMAIN_THEME dt) {
        switch (dt) {
            case WEAPON_MATERIAL_THEME:
                return ToolData.RESOURCE_TYPE.WEAPON_NAME;
            case TALENT_BOOK_THEME:
            case WEEKLY_BOSS_DOMAIN_THEME:
            case ARTIFACT_DOMAIN_THEME:
                return ToolData.RESOURCE_TYPE.CHARACTER;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns a set of items from this domain.
     *
     * @param domain domain
     * @param farmedOnly flag to show only items in listings
     * @return set of items
     */
    public static Set<? extends Item> whoNeedsThisDomain(Domain domain, boolean farmedOnly) {
        if (domain.isWeaponMaterialDomain()) {
            Set<Weapon> possibleWeapons = new HashSet<>();
            for (FarmableItem weaponMaterial : domain.materials) {
                assert weaponMaterial instanceof WeaponMaterial;
                for (String weaponName : ((WeaponMaterial) weaponMaterial).usedBy) {
                    Weapon weapon = getWeapon(weaponName);
                    if (!farmedOnly || !farmedWeapons.get(weapon).isEmpty() ||
                            getUnassignedFarmedWeapons().contains(weapon)) {
                        possibleWeapons.add(weapon);
                    }
                }
            }
            return possibleWeapons;
        } else if (domain.isWeeklyTalentMaterialDomain()) {
            Set<Character> possibleCharacters = new HashSet<>();
            for (FarmableItem weeklyTalentMaterial : domain.materials) {
                assert weeklyTalentMaterial instanceof WeeklyTalentMaterial;
                if (!farmedOnly || !farmedWeeklyTalentMaterials.get(weeklyTalentMaterial).isEmpty()) {
                    possibleCharacters.addAll(farmedWeeklyTalentMaterials.get(weeklyTalentMaterial));
                }
            }
            return possibleCharacters;
        } else if (domain.isTalentMaterialDomain()) {
            Set<Character> possibleCharacters = new HashSet<>();
            for (FarmableItem talentMaterial : domain.materials) {
                assert talentMaterial instanceof TalentMaterial;
                if (!farmedOnly || !farmedTalentBooks.get(talentMaterial).isEmpty()) {
                    possibleCharacters.addAll(farmedTalentBooks.get(talentMaterial));
                }
            }
            return possibleCharacters;
        } else if (domain.isArtifactDomain()) {
            Set<Character> possibleCharacters = new HashSet<>();
            for (FarmableItem artifact : domain.materials) {
                assert artifact instanceof Artifact;
                if (!farmedOnly || !farmedArtifacts.get(artifact).isEmpty()) {
                    possibleCharacters.addAll(farmedArtifacts.get(artifact));
                }
            }
            return possibleCharacters;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns a set of items from this domain.
     *
     * @param domain domain
     * @param item specified item available from the domain
     * @param farmedOnly flag to show only items in listings
     * @return set of items
     */
    public static Set<? extends Item> whoNeedsThisItem(Domain domain, FarmableItem item, boolean farmedOnly) {
        assert domain.materials.contains(item);
        if (domain.isWeaponMaterialDomain()) {

            assert item instanceof WeaponMaterial;

            Set<Weapon> allPossibleWeapons = new TreeSet<>(comparator);
            Set<Weapon> allListedWeapons = new TreeSet<>(comparator);

            for (String weaponName : ((WeaponMaterial) item).usedBy) {
                Weapon weapon = getWeapon(weaponName);
                if (!farmedWeapons.get(weapon).isEmpty() || getUnassignedFarmedWeapons().contains(weapon)) {
                    allListedWeapons.add(weapon);
                }
            }
            for (String weaponName : ((WeaponMaterial) item).usedBy) {
                Weapon weapon = getWeapon(weaponName);
                allPossibleWeapons.add(weapon);
            }

            if (farmedOnly) {
                return allListedWeapons;
            } else {
                allPossibleWeapons.removeAll(allListedWeapons);
                return allPossibleWeapons;
            }

        } else if (domain.isWeeklyTalentMaterialDomain()) {
            Set<Character> possibleCharacters = new HashSet<>();
            assert item instanceof WeeklyTalentMaterial;

            if (farmedOnly) {
                possibleCharacters.addAll(farmedWeeklyTalentMaterials.get(item));
            } else {
                for (String characterName : ((WeeklyTalentMaterial) item).usedBy) {
                    Character character = getCharacter(characterName);
                    if (!farmedWeeklyTalentMaterials.get(item).contains(character)) {
                        possibleCharacters.add(getCharacter(characterName));
                    }
                }
            }
            return possibleCharacters;
        } else if (domain.isTalentMaterialDomain()) {
            Set<Character> possibleCharacters = new HashSet<>();
            assert item instanceof TalentMaterial;
            if (farmedOnly) {
                possibleCharacters.addAll(farmedTalentBooks.get(item));
            } else {
                for (String characterName : ((TalentMaterial) item).usedBy) {
                    Character character = getCharacter(characterName);
                    if (!farmedTalentBooks.get(item).contains(character)) {
                        possibleCharacters.add(getCharacter(characterName));
                    }
                }
            }
            return possibleCharacters;
        } else if (domain.isArtifactDomain()) {
            assert item instanceof Artifact;
            if (farmedOnly) {
                return farmedArtifacts.get(item);
            } else {
                throw new IllegalArgumentException(
                        "There is no point in listing all characters for an artifact domain, anyone can equip any set.");
            }
        } else {
            throw new IllegalArgumentException("Unknown domain as parameter in whoNeedsThisDomain");
        }
    }

    /**
     * Returns a counter label text that tells how many "benefactors" are there per domain.
     * E.g. for a talent domain: how many characters exist in the game that require talent books from the given domain
     *
     * @param domain domain
     * @return the text
     */
    public static String getAllCounterLabel(Domain domain) {
        int counter = 0;
        for (FarmableItem material : domain.materials) {
            if (!domain.isArtifactDomain()) {
                counter += whoNeedsThisItem(domain, material, false).size();
            }
            counter += whoNeedsThisItem(domain, material, true).size();
        }
        return "<html>" + "All" + " " + (domain.isWeaponMaterialDomain() ? "weapons" : "characters") + " " +
                "that need it: " + "<u>" + counter + "</u>" + "</html>";

    }

    /**
     * Returns a counter label text that tells how many listed "benefactors" are there per domain.
     * E.g. for a talent domain: how many characters are currently farming talents from that domain.
     *
     * @param domain domain name
     * @return the text
     */
    public static String getListedCounterLabel(Domain domain) {
        int counter = 0;
        for (FarmableItem material : domain.materials) {
            counter += whoNeedsThisItem(domain, material, true).size();
        }
        return "<html>" + "Listed" + " " + (domain.isWeaponMaterialDomain() ? "weapons" : "characters") + ":" + " " +
                "<u>" + counter + "</u>" + "</html>";
    }

    /**
     * Retrieves the main panel. Only used in ToolGUI.
     *
     * @return main panel
     */
    public JPanel getMainPanel() {
        return domainTab;
    }

    /**
     * Returns a day filter string token based on the current system day.
     *
     * @return day filter string.
     */
    public static String getDayFilterToken() {
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentDay = currentTime.get(Calendar.DAY_OF_WEEK);
        if (currentHour < 4) {
            currentDay = (currentDay - 1) % 7;
        }
        switch (currentDay) {
            case MONDAY:
            case THURSDAY:
                return DAY_FILTER.MONDAY_THURSDAY.stringToken;
            case TUESDAY:
            case FRIDAY:
                return DAY_FILTER.TUESDAY_FRIDAY.stringToken;
            case WEDNESDAY:
            case SATURDAY:
                return DAY_FILTER.WEDNESDAY_SATURDAY.stringToken;
            default:
                return DAY_FILTER.SUNDAY_ALL.stringToken;
        }
    }
}
