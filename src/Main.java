import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Main extends JPanel implements ActionListener {

    // constants
    static final Font DEFAULT_FONT = new Font("SansSerif", Font.BOLD, 20); // font
    static final Dimension DEFAULT_DIMENSION = new Dimension(1000, 500); // screen
    private static final boolean SHUFFLE = true; // whether to shuffle the strings within a set
    private static final boolean AUDIO_NOT_TEXT = true; // if true, play audio, else display text

    private static final JFrame frame = new JFrame("Main"); // displays content

    private String[][] sets; // letter combinations
    private Color[][] colors; // associated colors
    private JButton[] buttons; // associated buttons
    private JTextField textField; // textFiled to which the user can write
    private JLabel prompt; // prompt shown to the user
    private File file; // for saving purposes

    public Main() {
        super(new GridLayout(3,1));

        // the text displayed at the top of the window
        prompt = new JLabel("Enter you UID and choose letter-set", JLabel.CENTER);
        prompt.setForeground(Color.BLACK);
        prompt.setBackground(Color.WHITE);
        prompt.setOpaque(true);
        prompt.setFont(DEFAULT_FONT);
        this.add(prompt);

        // text field to enter the Unique ID (name of the file to which the data will be written)
        textField = new JTextField("");
        textField.setFont(DEFAULT_FONT);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBackground(Color.WHITE);
        this.add(textField);

        // Letter Sets
        sets = new String[3][];
        sets[0] = new String[] {"Con-00b", "Con-01a", "Con-11b"};
        sets[1] = new String[] {"Con-27b", "Con-36b"};
        sets[2] = new String[] {"Con-53a"};

        // This is to store the selected colors
        colors = new Color[sets.length][];
        Arrays.fill(colors, null);

        // A Button for each letter set
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new GridBagLayout());
        buttons = new JButton[sets.length];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton("Set " + i);
            buttons[i].addActionListener(this);
            buttonPane.add(buttons[i]);
        }
        this.add(buttonPane);

        // preferred size of the window in pixels
        this.setPreferredSize(DEFAULT_DIMENSION);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // check that the entered UID has not been used before (no overwritten files)
        if (textField.isEditable()) {
            file = new File(textField.getText() + ".csv");
            if ((textField.getText().length() == 0) || (file.exists())) {
                prompt.setText("Please enter the UID that is valid");
                return;
            }
            textField.setEditable(false);
            textField.setBackground(Color.GRAY);
            prompt.setText("Choose a letter set");
        }

        int bID = 0; // id of the button triggered
        for (int i = 1; i < buttons.length; i++)
            if (e.getSource() == buttons[i])
                bID = i;
        buttons[bID].setBackground(Color.GRAY);
        buttons[bID].setEnabled(false);

        colors[bID] = new Color[sets[bID].length];
        Arrays.fill(colors[bID], null);

        JComponent newContentPane;
        String promptText = "Please choose the color: ";

        if (AUDIO_NOT_TEXT)
            newContentPane = new ColorChooserAudio(this, sets[bID], colors[bID], SHUFFLE, promptText);
        else
            newContentPane = new ColorChooserTextual(this, sets[bID], colors[bID], SHUFFLE, promptText);

        frame.setContentPane(newContentPane);
        frame.pack();

    }

    /**
     * Save data to a CSV file of a given name
     */
    public void save() {
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("string,red,green,blue"); // header
            for (int i=0; i < sets.length; i++)
                if (colors[i] != null)
                    for (int j=0; j < sets[i].length; j++)
                        pw.println(sets[i][j] + "," + colorToString(colors[i][j]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        frame.setContentPane(this);
        boolean setsLeft = false;
        for (JButton button: buttons)
            if (button.isEnabled())
                setsLeft = true;

        if (!setsLeft)
            prompt.setText("Thank you");
    }

    public String colorToString(Color color) {
        if (color == null)
            return "-1,-1,-1";
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    public static void main(String args[]) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new Main();
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }
}
