package Files.Code.Auxiliary;

import static Files.Code.Data.ToolData.getArtifact;
import static Files.Code.Data.ToolData.getCharacter;
import static Files.Code.Data.ToolData.getTalentBook;
import static Files.Code.Data.ToolData.getWeapon;
import static Files.Code.Data.ToolData.getWeeklyTalentMaterial;
import static Files.Code.GUIs.ToolGUI.updateFarmedItemMap;

import Files.Code.Data.CharacterListing;
import Files.Code.Data.ToolData;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * This class serves as the listener for checkboxes generated in CharacterCard.java
 */
public class UpdateCharacterCardCheckBoxListener implements ItemListener {
    private final CharacterListing _characterListing;
    private final ToolData.CHARACTER_CARD_DATA_FIELD _changedData;
    private final JButton _saveButton;

    /**
     * Constructor of the class
     *
     * @param characterListing the character card where the checkbox is located
     * @param changedData the changed data type in the card
     * @param saveButton implementation side effect. Will set the button to active once a change has been made.
     */
    public UpdateCharacterCardCheckBoxListener(CharacterListing characterListing,
                                               ToolData.CHARACTER_CARD_DATA_FIELD changedData, JButton saveButton) {
        _characterListing = characterListing;
        _changedData = changedData;
        _saveButton = saveButton;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        boolean currentStatus = ((JCheckBox) e.getSource()).isSelected();
        _saveButton.setEnabled(true);
        switch (_changedData) {
            case FARMING_SET_ONE:
                _characterListing.setArtifactSet1Status(currentStatus);
                updateFarmedItemMap(ToolData.CHARACTER_CARD_DATA_FIELD.SET_ONE, _characterListing, currentStatus,
                        getArtifact(_characterListing.getArtifactSet1()));
                return;
            case FARMING_SET_TWO:
                _characterListing.setArtifactSet2Status(currentStatus);
                updateFarmedItemMap(ToolData.CHARACTER_CARD_DATA_FIELD.SET_TWO, _characterListing, currentStatus,
                        getArtifact(_characterListing.getArtifactSet2()));
                return;
            case FARMING_TALENT_MATERIALS:
                _characterListing.setTalentStatus(currentStatus);
                updateFarmedItemMap(_changedData, _characterListing, currentStatus,
                        getTalentBook(getCharacter(_characterListing.getCharacterName()).talentMaterial));
                updateFarmedItemMap(_changedData, _characterListing, currentStatus, getWeeklyTalentMaterial(
                        getCharacter(_characterListing.getCharacterName()).weeklyTalentMaterial));
                return;
            case FARMING_WEAPON_MATERIALS:
                _characterListing.setWeaponStatus(currentStatus);
                updateFarmedItemMap(_changedData, _characterListing, currentStatus,
                        getWeapon(_characterListing.getWeapon()));
                return;
            default:
        }
    }
}
