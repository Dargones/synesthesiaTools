import javax.swing.*;
import java.awt.*;

/**
 * The original version of color chooser that shows the user a letter or a sequence of letters
 */
public class ColorChooserTextual extends ColorChooser{

    private static final Font INPUT_TEXT_FONT = new Font("SansSerif", Font.BOLD, 70);
    private JLabel inputText; // The Text Field with the character sequence

    public ColorChooserTextual(Main menu, String[] seq, Color[] colors, boolean shuffle, String promptText) {
        super(menu, seq, colors, shuffle, promptText);
    }

    @Override
    protected JPanel createTextPanel(String promptText) {
        JPanel panel = super.createTextPanel(promptText);
        inputText = new JLabel(seq[order.get(seqId)], JLabel.CENTER);
        inputText.setForeground(Color.BLACK);
        inputText.setBackground(Color.WHITE);
        inputText.setOpaque(true);
        inputText.setFont(INPUT_TEXT_FONT);
        panel.add(inputText);
        return panel;
    }

    @Override
    protected void nextState() {
        super.nextState();
        inputText.setText(seq[order.get(++seqId)]);
    }
}
