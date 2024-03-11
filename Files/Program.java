package Files;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class Program extends JFrame implements ActionListener {
    public Program() {
        $$$setupUI$$$();
        _openTabs.add("Characters");
        _openTabs.add("Artifacts");
        setContentPane(panel1);
        setTitle("Genshin Domain App!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        //search_cycle();
        /*characterNames.add("Nahida");
        characterNames.add("Xianyun");
        DefaultTableModel dm = new DefaultTableModel(characterNames, 1);
        dm.setValueAt(LabelNahida, 0, 0);
        dm.setValueAt(LabelXianyun, 0, 1);
        CharacterTable = new JTable(dm);*/

    }

    private JPanel panel1;
    private JLabel label1;
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JPanel CharacterTab;
    private JPanel ArtifactsTab;
    private JTextField characterSelectorField;
    private JButton CheckButton;
    private JLabel Result;
    private JPanel selectedCharacterPanel;
    private JTextField emptyNotesFieldTextField;
    private JComboBox comboBox1;
    private JCheckBox artifactListingCheckBox;
    private JCheckBox talentListingCheckBox;
    private Set<String> _openTabs;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel1 = new JPanel();
        _openTabs = new HashSet<>();
        CheckButton = new JButton("✓");
        CheckButton.addActionListener(this);
        characterSelectorField = new JTextField();
    }

    public void actionPerformed(ActionEvent e) {
        boolean matched = false;
        selectedCharacterPanel.removeAll();
        String userFieldInput = characterSelectorField.getText().toLowerCase();
        for (int i = 0; i < Data.characters.length; i++) {
            if (Data.characters[i].toLowerCase().contains(userFieldInput)) {
                generateCharacterLabel(Data.characters[i]);
                matched = true;
                //matchedCharacters = matchedCharacters + Data.characters[i] + ", ";
                //Result.setText(Data.characters[i]);
                //return;
            }
        }
        if (!matched) {
            Result.setText("No fighters >:(");
        }

    }

    private void generateCharacterLabel(String characterName) {
        String characterIconPath = "/Files/Images/" + characterName + ".png";
        JButton characterLabel = new JButton();
        Icon characterIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(characterIconPath)));
        characterLabel.setIcon(characterIcon);
        characterLabel.setText(characterName);
        characterLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        characterLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        characterLabel.setOpaque(false);
        characterLabel.setBorderPainted(false);
        characterLabel.setContentAreaFilled(false);
        characterLabel.addActionListener(e -> {
            if (_openTabs.add(characterName)) {
                JPanel selectedCharacterTab = new JPanel();
                selectedCharacterTab.setLayout(new GridLayout(2, 3));
                selectedCharacterTab.add(new JLabel(characterIcon));
                selectedCharacterTab.setBackground(new Color(-1));
                selectedCharacterTab.setEnabled(true);
                selectedCharacterTab.setFocusCycleRoot(false);
                selectedCharacterTab.setOpaque(true);
                selectedCharacterTab.setRequestFocusEnabled(true);
                tabbedPane1.addTab(characterName, selectedCharacterTab);
                tabbedPane1.setSelectedComponent(selectedCharacterTab);
            } else {
                tabbedPane1.setSelectedIndex(tabbedPane1.indexOfTab(characterName));
            }
        });
        selectedCharacterPanel.add(characterLabel);
        selectedCharacterPanel.updateUI();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-2702645));
        panel1.setEnabled(true);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Source Code Pro Black", Font.BOLD, 20, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-14940151));
        label1.setText("Genshin Domain Application");
        panel1.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setBackground(new Color(-3689540));
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        Font tabbedPane1Font = this.$$$getFont$$$("Source Code Pro Black", Font.BOLD, 20, tabbedPane1.getFont());
        if (tabbedPane1Font != null) tabbedPane1.setFont(tabbedPane1Font);
        tabbedPane1.setTabPlacement(1);
        panel2.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        CharacterTab = new JPanel();
        CharacterTab.setLayout(new GridBagLayout());
        CharacterTab.setBackground(new Color(-1));
        CharacterTab.setEnabled(true);
        CharacterTab.setFocusCycleRoot(false);
        Font CharacterTabFont = this.$$$getFont$$$(null, -1, -1, CharacterTab.getFont());
        if (CharacterTabFont != null) CharacterTab.setFont(CharacterTabFont);
        CharacterTab.setOpaque(true);
        CharacterTab.setRequestFocusEnabled(true);
        tabbedPane1.addTab("Characters", CharacterTab);
        characterSelectorField.setEnabled(true);
        Font characterSelectorFieldFont = this.$$$getFont$$$("Source Code Pro Black", Font.BOLD, 18, characterSelectorField.getFont());
        if (characterSelectorFieldFont != null) characterSelectorField.setFont(characterSelectorFieldFont);
        characterSelectorField.setInheritsPopupMenu(false);
        characterSelectorField.setMaximumSize(new Dimension(240, 33));
        characterSelectorField.setMinimumSize(new Dimension(240, 33));
        characterSelectorField.setPreferredSize(new Dimension(240, 33));
        characterSelectorField.setText("Choose your fighter!");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 250, 0, 0);
        CharacterTab.add(characterSelectorField, gbc);
        CheckButton.setMaximumSize(new Dimension(30, 30));
        CheckButton.setMinimumSize(new Dimension(30, 30));
        CheckButton.setPreferredSize(new Dimension(50, 30));
        CheckButton.setText("✓");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        CharacterTab.add(CheckButton, gbc);
        Result = new JLabel();
        Font ResultFont = this.$$$getFont$$$("Source Code Pro Black", Font.BOLD, 18, Result.getFont());
        if (ResultFont != null) Result.setFont(ResultFont);
        Result.setText("[Result character]");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 0);
        CharacterTab.add(Result, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        CharacterTab.add(panel3, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        selectedCharacterPanel = new JPanel();
        selectedCharacterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        scrollPane1.setViewportView(selectedCharacterPanel);
        ArtifactsTab = new JPanel();
        ArtifactsTab.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        ArtifactsTab.setBackground(new Color(-1));
        tabbedPane1.addTab("Artifacts", ArtifactsTab);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        tabbedPane1.addTab("Untitled", panel4);
        final JLabel label2 = new JLabel();
        label2.setIcon(new ImageIcon(getClass().getResource("/Files/Images/Albedo.png")));
        label2.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(20, 0, 20, 20);
        panel4.add(label2, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(panel5, gbc);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        emptyNotesFieldTextField = new JTextField();
        Font emptyNotesFieldTextFieldFont = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 14, emptyNotesFieldTextField.getFont());
        if (emptyNotesFieldTextFieldFont != null) emptyNotesFieldTextField.setFont(emptyNotesFieldTextFieldFont);
        emptyNotesFieldTextField.setHorizontalAlignment(10);
        emptyNotesFieldTextField.setInheritsPopupMenu(false);
        emptyNotesFieldTextField.setOpaque(true);
        emptyNotesFieldTextField.setRequestFocusEnabled(true);
        emptyNotesFieldTextField.setText("[ Empty Notes Field ]");
        panel5.add(emptyNotesFieldTextField, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        comboBox1 = new JComboBox();
        comboBox1.setAutoscrolls(false);
        comboBox1.setEditable(false);
        Font comboBox1Font = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 14, comboBox1.getFont());
        if (comboBox1Font != null) comboBox1.setFont(comboBox1Font);
        comboBox1.setInheritsPopupMenu(false);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("[ Empty Artifact Selector ]");
        comboBox1.setModel(defaultComboBoxModel1);
        comboBox1.setToolTipText("");
        panel5.add(comboBox1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setAutoscrolls(true);
        Font label3Font = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 18, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Albedo");
        panel5.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(3, 1, new Insets(3, 3, 3, 3), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 100);
        panel4.add(panel6, gbc);
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        artifactListingCheckBox = new JCheckBox();
        Font artifactListingCheckBoxFont = this.$$$getFont$$$("Source Code Pro", -1, 14, artifactListingCheckBox.getFont());
        if (artifactListingCheckBoxFont != null) artifactListingCheckBox.setFont(artifactListingCheckBoxFont);
        artifactListingCheckBox.setText("Artifact Listing");
        panel6.add(artifactListingCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        talentListingCheckBox = new JCheckBox();
        Font talentListingCheckBoxFont = this.$$$getFont$$$("Source Code Pro", -1, 14, talentListingCheckBox.getFont());
        if (talentListingCheckBoxFont != null) talentListingCheckBox.setFont(talentListingCheckBoxFont);
        talentListingCheckBox.setText("Talent Listing");
        panel6.add(talentListingCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 18, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Domain listings");
        panel6.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(4);
        label5.setHorizontalTextPosition(4);
        label5.setIcon(new ImageIcon(getClass().getResource("/Files/Images/Weapons/Amos' Bow.png")));
        label5.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(0, 0, 20, 20);
        panel4.add(label5, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(panel7, gbc);
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JTextField textField1 = new JTextField();
        Font textField1Font = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 14, textField1.getFont());
        if (textField1Font != null) textField1.setFont(textField1Font);
        textField1.setHorizontalAlignment(10);
        textField1.setInheritsPopupMenu(false);
        textField1.setOpaque(true);
        textField1.setRequestFocusEnabled(true);
        textField1.setText("[ Empty Notes Field ]");
        panel7.add(textField1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(50, -1), null, 0, false));
        final JComboBox comboBox2 = new JComboBox();
        comboBox2.setAutoscrolls(false);
        comboBox2.setEditable(false);
        Font comboBox2Font = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 14, comboBox2.getFont());
        if (comboBox2Font != null) comboBox2.setFont(comboBox2Font);
        comboBox2.setInheritsPopupMenu(false);
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("[ Amos' Bow Selected ]");
        comboBox2.setModel(defaultComboBoxModel2);
        comboBox2.setToolTipText("");
        panel7.add(comboBox2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setAutoscrolls(true);
        Font label6Font = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 18, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Amos's Bow");
        panel7.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(2, 1, new Insets(4, 4, 4, 4), -1, -1));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 100);
        panel4.add(panel8, gbc);
        panel8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JCheckBox checkBox1 = new JCheckBox();
        Font checkBox1Font = this.$$$getFont$$$("Source Code Pro", -1, 14, checkBox1.getFont());
        if (checkBox1Font != null) checkBox1.setFont(checkBox1Font);
        checkBox1.setText("Material Listing");
        panel8.add(checkBox1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Source Code Pro", Font.BOLD, 18, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Domain listings");
        panel8.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}

