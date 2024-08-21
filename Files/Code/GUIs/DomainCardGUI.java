package Files.Code.GUIs;

import static Files.Code.Data.ToolData.changeFont;
import static Files.Code.GUIs.DomainTabGUI.getAllCounterLabel;
import static Files.Code.GUIs.DomainTabGUI.getDomainResourceType;
import static Files.Code.GUIs.DomainTabGUI.getDomainTargetResourceType;
import static Files.Code.GUIs.DomainTabGUI.getListedCounterLabel;
import static Files.Code.GUIs.DomainTabGUI.whoNeedsThisItem;
import static Files.Code.GUIs.ToolGUI.getCharacterCard;

import Files.Code.Data.Character;
import Files.Code.Data.CharacterListing;
import Files.Code.Data.Domain;
import Files.Code.Data.FarmableItem;
import Files.Code.Data.Item;
import Files.Code.Data.ToolData;
import Files.Code.Data.Weapon;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Objects;

/**
 * This class constructs a domain card GUI (shown after clicking on a domain card).
 */
public class DomainCardGUI{
    private final DomainTabGUI.DOMAIN_THEME domainTheme;
    private final Domain domain;
    private final JPanel mainPanel = new JPanel(new GridBagLayout());

    /**
     * Constructor of the class.
     *
     * @param domain domain
     * @param domainTheme domain type. There are currently 4 types in the game
     */
    public DomainCardGUI(Domain domain, DomainTabGUI.DOMAIN_THEME domainTheme) {
        this.domainTheme = domainTheme;
        this.domain = domain;
        mainPanel.setVisible(true);
    }

    /**
     * Puts the info elements into the main panel.
     */
    private JPanel generateDomainCard() {

        JScrollPane mainPanelScrollPane = new JScrollPane();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(mainPanelScrollPane, gbc);
        JPanel mainScrollPaneViewport = new JPanel();
        mainScrollPaneViewport.setLayout(new GridBagLayout());
        mainScrollPaneViewport.setBackground(new Color(-465419));
        mainPanelScrollPane.setViewportView(mainScrollPaneViewport);

        // TITLE PANEL
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setBackground(new Color(domainTheme.panelBackgroundColor));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 50, 0, 50);
        mainScrollPaneViewport.add(titlePanel, gbc);
        titlePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

        // DOMAIN NAME
        JLabel domainNameLabel = new JLabel();
        domainNameLabel.setForeground(new Color(domainTheme.panelForegroundColor));
        domainNameLabel.setText(domain.name);
        changeFont(domainNameLabel, ToolData.AVAILABLE_FONTS.HEADER_FONT, 20);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        titlePanel.add(domainNameLabel, gbc);

        // OVERVIEW PANEL
        JPanel itemOverviewPanel = new JPanel();
        itemOverviewPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        itemOverviewPanel.setBackground(new Color(domainTheme.panelBackgroundColor));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        titlePanel.add(itemOverviewPanel, gbc);
        JTabbedPane itemOverviewTabbedPane = new JTabbedPane();
        itemOverviewTabbedPane.setBackground(new Color(-1));
        itemOverviewTabbedPane.setTabPlacement(2);
        itemOverviewPanel.add(itemOverviewTabbedPane,
                new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
                        new Dimension(200, 200), null, 0, false));
        for (FarmableItem domainMat : domain.materials) {
            JPanel innerUnlistedPanel = new JPanel(new GridBagLayout());
            JPanel innerListedPanel = new JPanel(new GridBagLayout());
            JPanel dayTab = new JPanel();
            dayTab.setLayout(new GridBagLayout());
            dayTab.setBackground(new Color(-1));
            itemOverviewTabbedPane.addTab("", domainMat.icon, dayTab, domainMat.name);
            JLabel listedWeaponHeadline = new JLabel();
            listedWeaponHeadline.setForeground(new Color(domainTheme.panelForegroundColor));
            if (getDomainTargetResourceType(domainTheme) == ToolData.RESOURCE_TYPE.WEAPON_NAME) {
                listedWeaponHeadline.setText("Listed weapons");
            } else {
                listedWeaponHeadline.setText("Listed characters");
            }
            changeFont(listedWeaponHeadline, ToolData.AVAILABLE_FONTS.HEADER_FONT, 15);

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.anchor = GridBagConstraints.NORTH;
            dayTab.add(listedWeaponHeadline, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            gbc.weighty = 0.5;
            gbc.fill = GridBagConstraints.BOTH;
            dayTab.add(innerListedPanel, gbc);
            if (domainTheme != DomainTabGUI.DOMAIN_THEME.ARTIFACT_DOMAIN_THEME) {
                JLabel unlistedWeaponHeadline = new JLabel();
                unlistedWeaponHeadline.setBackground(new Color(domainTheme.panelForegroundColor));

                if (getDomainTargetResourceType(domainTheme) == ToolData.RESOURCE_TYPE.WEAPON_NAME) {
                    unlistedWeaponHeadline.setText("Other Weapons");
                } else {
                    unlistedWeaponHeadline.setText("Other Characters");
                }
                changeFont(unlistedWeaponHeadline, ToolData.AVAILABLE_FONTS.HEADER_FONT, 15);
                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.weightx = 1.0;
                gbc.anchor = GridBagConstraints.NORTH;
                dayTab.add(unlistedWeaponHeadline, gbc);
                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.weightx = 1.0;
                gbc.weighty = 0.5;
                gbc.fill = GridBagConstraints.BOTH;
                dayTab.add(innerUnlistedPanel, gbc);
            }

            int i = 0;
            int k = 0;
            if (domain.isArtifactDomain()) {
                for (Item character : whoNeedsThisItem(domain, domainMat, true)) {
                    assert character instanceof Character;
                    generateDomainItemLabel(character.name, character.icon, i, innerListedPanel);
                    i++;
                }
            } else if (domain.isWeaponMaterialDomain()) {
                for (Item weapon : whoNeedsThisItem(domain, domainMat, true)) {
                    assert weapon instanceof Weapon;
                    generateDomainItemLabel(weapon.name, weapon.icon, i, innerListedPanel);
                    i++;
                }
                for (Item weapon : whoNeedsThisItem(domain, domainMat, false)) {
                    assert weapon instanceof Weapon;
                    generateDomainItemLabel(weapon.name, weapon.icon, k, innerUnlistedPanel);
                    k++;
                }
            } else if (domain.isWeeklyTalentMaterialDomain() || domain.isTalentMaterialDomain()) {
                for (Item character : whoNeedsThisItem(domain, domainMat, true)) {
                    assert character instanceof Character;
                    generateDomainItemLabel(character.name, character.icon, i, innerListedPanel);
                    i++;
                }
                for (Item character : whoNeedsThisItem(domain, domainMat, false)) {
                    assert character instanceof Character;
                    generateDomainItemLabel(character.name, character.icon, k, innerUnlistedPanel);
                    k++;
                }
            } else {
                throw new IllegalArgumentException("The domain type is unknown");
            }
        }

        // DOMAIN LISTINGS INFO
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        assert getDomainResourceType(domainTheme) != null;
        JLabel label = new JLabel(getListedCounterLabel(domain));
        changeFont(label, ToolData.AVAILABLE_FONTS.REGULAR_FONT, 12);
        titlePanel.add(label, gbc);
        if (!domain.isArtifactDomain()) {
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.weightx = 1.0;
            assert getDomainResourceType(domainTheme) != null;
            JLabel label2 = new JLabel(getAllCounterLabel(domain));
            changeFont(label2, ToolData.AVAILABLE_FONTS.REGULAR_FONT, 12);
            titlePanel.add(label2, gbc);
        }

        return mainPanel;
    }

    private String formatLabel(String characterName, String characterNotes) {
        final String HTML_BEGINNING = "<html><center>";
        final String HTML_END = "</center></html>";
        final String HTML_BREAK = "<br>";
        StringBuilder formattedNotes = new StringBuilder("<b>" + characterName + "</b>");
        for (int i = 0; i < characterNotes.length(); i++) {
            if (i % 13 == 0) {
                formattedNotes.append(HTML_BREAK);
            }
            formattedNotes.append(characterNotes.charAt(i));
        }
        return HTML_BEGINNING + formattedNotes + HTML_END;
    }
    // GENERATED WEAPONS/CHARACTERS

    private void generateDomainItemLabel(String itemName, ImageIcon itemIcon, int index, JPanel panel) {
        JLabel domainItemLabel = new JLabel();
        changeFont(domainItemLabel, ToolData.AVAILABLE_FONTS.TEXT_FONT, 12.0F);
        if (getDomainResourceType(domainTheme) == ToolData.RESOURCE_TYPE.ARTIFACT) {
            CharacterListing card = getCharacterCard(itemName);
            if (card != null && !card.getCharacterNotes().isEmpty()) {
                domainItemLabel.setText(formatLabel(itemName, card.getCharacterNotes()));
            } else {
                domainItemLabel.setText(formatLabel(itemName, ""));
            }
        }
        domainItemLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        domainItemLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        domainItemLabel.setVerticalTextPosition(SwingConstants.CENTER);
        domainItemLabel.setVerticalAlignment(SwingConstants.TOP);
        GridBagConstraints gbc = new GridBagConstraints();
        if (domainTheme == DomainTabGUI.DOMAIN_THEME.ARTIFACT_DOMAIN_THEME) {
            gbc.gridx = index % 3;
            gbc.gridy = index / 3;
        } else {
            gbc.gridx = index % 5;
            gbc.gridy = index / 5;
        }
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 5, 0, 5);
        domainItemLabel.setIcon(itemIcon);
        panel.add(domainItemLabel, gbc);
    }
    public JPanel getMainPanel(){
        return mainPanel;
    }
}
