package Files.Code.Auxiliary;

import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class removes the text in a search bar upon mouse-clicking within its boundaries.
 */
public class SearchBarListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e){
        JTextField src = (JTextField) e.getSource();
        src.setText("");
    }
}
