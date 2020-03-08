import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

/**
 * This is an abstract class that provides functionality for recording user's color selections.
 */
public abstract class ColorChooser extends JPanel implements ActionListener {

    private JColorChooser Jcc; // The color choosing panel
    private Color[] colors; // array in which to record the colors chosen by the user
    private Main menu; // the link to the menu class
    private JButton okButton;
    private JButton skipButton;

    protected String[] seq;  // sequence of letters/letter-combinations/sound shown to the user
    protected ArrayList<Integer> order; // order in which these are shown
    protected int seqId = 0; // next element to show


    /**
     * The constructor that defines how the panel will look like
     * @param menu          the link to the menu (Main) class
     * @param seq           sequence of letters/letter-combinations/sound shown to the user
     * @param colors        array in which to record the colors chosen by the user
     * @param shuffle       whether to show seq in a random order or not
     * @param promptText    prompt to show to the user (remains unchanged by default)
     */
    public ColorChooser(Main menu, String seq[], Color[] colors, boolean shuffle, String promptText) {
        super(new BorderLayout());

        this.menu = menu;
        this.seq = seq;
        this.colors = colors;

        initOrder(shuffle); // order in which sequences are shown

        JPanel textPanel = createTextPanel(promptText);
        JPanel buttonPanel = createButtonPanel();
        Jcc = new JColorChooser(Color.BLACK);

        add(textPanel, BorderLayout.PAGE_START);
        add(Jcc, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);

        this.setPreferredSize(Main.DEFAULT_DIMENSION);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource() != okButton) && (e.getSource() != skipButton))
            return;
        if (e.getSource() == okButton)
            colors[order.get(seqId)] = Jcc.getColor();
        else
            colors[order.get(seqId)] = null;
        if (seqId == seq.length - 1) {
            menu.save();
            return;
        }
        nextState();
    }

    /**
     * Creates a panel with text fields that is placed above everything else
     * @param promptText prompt to show to the user (remains unchanged by default)
     * @return a JPanel object
     */
    protected JPanel createTextPanel(String promptText) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        JLabel prompt = new JLabel(promptText, JLabel.CENTER);
        prompt.setForeground(Color.BLACK);
        prompt.setBackground(Color.WHITE);
        prompt.setOpaque(true);
        prompt.setFont(Main.DEFAULT_FONT);

        panel.add(prompt);
        return panel;
    }

    /**
     * Create a panel with buttons that is placed below everything else.
     * @return a JPanel object
     */
    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        skipButton = new JButton("Skip");
        skipButton.addActionListener(this);

        panel.add(okButton);
        panel.add(skipButton);
        return panel;
    }

    /**
     * This is ran whenever a user presses an ok or skip button
     */
    protected void nextState() {
        Jcc.setColor(Color.BLACK);
    }

    /**
     * Initialize the order field. Default = random
     * @param shuffle whether to show seq in a random order or not
     */
    private void initOrder(boolean shuffle) {
        order = new ArrayList<>(seq.length);
        for (int i = 0; i < seq.length; i++)
            order.add(i);
        if (shuffle)
            Collections.shuffle(order);
    }

}