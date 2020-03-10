import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * A version of color chooser that plays an audio clip for each element in seq
 */
public class ColorChooserAudio extends ColorChooser {
    private static final String DIR = "audio/"; // the directory from which to load all audio clips
    // Extension of that the audion files use. Not all extensions are supported
    private static final String EXT = "AIFF";
    private Clip[] audioClips; // the audion clips to play
    private JButton replayButton;

    public ColorChooserAudio(Main menu, String[] seq, Color[] colors, boolean shuffle, String promptText) {
        super(menu, seq, colors, shuffle, promptText);
        try {
            initAudioClips();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == replayButton) {
            Clip clip = audioClips[order.get(seqId)];
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    @Override
    protected JPanel createButtonPanel() {
        JPanel panel = super.createButtonPanel();
        replayButton = new JButton("Replay Audio");
        replayButton.addActionListener(this);
        panel.add(replayButton);
        return panel;
    }

    @Override
    protected void nextState() {
        super.nextState();
        audioClips[order.get(seqId)].stop();
        audioClips[order.get(++seqId)].start();
    }

    /**
     * For each element in seq, load an audio track from audio directory
     */
    private void initAudioClips() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioClips = new Clip[seq.length];
        for (int i = 0; i < audioClips.length; i++) {
            audioClips[i] = AudioSystem.getClip();
            AudioInputStream tmp = AudioSystem.getAudioInputStream(new File(DIR + seq[i] + '.' + EXT));
            audioClips[i].open(tmp);
        }
        audioClips[order.get(seqId)].start();
    }
}
