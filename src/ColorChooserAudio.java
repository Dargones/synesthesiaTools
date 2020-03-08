import com.sun.media.sound.JavaSoundAudioClip;

import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A version of color chooser that plays an audio clip for each element in seq
 */
public class ColorChooserAudio extends ColorChooser {
    private static final String DIR = "src/audio/"; // the directory from which to load all audio clips
    // Extension of that the audion files use. Not all extensions are supported
    private static final String EXT = "wav";
    private AudioClip audioClips[]; // the audion clips to play
    private JButton replayButton;

    public ColorChooserAudio(Main menu, String[] seq, Color[] colors, boolean shuffle, String promptText) {
        super(menu, seq, colors, shuffle, promptText);
        try {
            initAudioClips();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == replayButton)
            audioClips[order.get(seqId)].play();
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
        audioClips[order.get(++seqId)].play();
    }

    /**
     * For each element in seq, load an audio track from audio directory
     */
    private void initAudioClips() throws IOException{
        audioClips = new AudioClip[seq.length];
        for (int i = 0; i < audioClips.length; i++)
            audioClips[i] = new JavaSoundAudioClip(new FileInputStream(DIR + seq[i] + '.' + EXT));
        audioClips[order.get(seqId)].play();
    }
}
