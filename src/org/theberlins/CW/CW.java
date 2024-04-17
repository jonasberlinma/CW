package org.theberlins.cw;

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class CW {

    public static int takt = 80;
    public static double tempo = 320 / takt;
    public static HashMap<String, String> letters = new HashMap<String, String>();
    public static void main(String[] args) throws LineUnavailableException {
        String[][] codes = {{" ", " "}, {"a", ".-"}, {"b", "-..."}, {"c", "-.-."}, {"d", "-.."}, {"e", "."}, 
        {"f", "..-."}, {"g", "--."}, {"h", "...."}, {"i", ".."}, {"j", ".---"}, {"k", "-.-"}, {"l", ".-.."}, 
        {"m", "--"}, {"n", "-."}, {"o", "---"}, {"p", ".--."}, {"q", "--.-"}, {"r", ".-."}, {"s", "..."}, {"t", "-"}, 
        {"u", "..-"} , {"v", "...-"}, {"w", ".--"}, {"x", "-..-"}, {"y", "-.--"}, {"z", "--.."}
        , {"0", "-----"}, {"1", ".----"}, {"2", "..---"}, {"3", "...--"}, {"4", "....-"}, {"5", "....."}, {"6", "-...."},
         {"7", "--..."}, {"8", "---.."}, {"9", "----."}, {".", ".-.-.-"}, {",", "--..--"}, {"?", "..--.."}, {"!", "-.-.--"}};
        for (String[] code : codes) {
            letters.put(code[0], code[1]);
        }  

  
        String message = "How the phuck are you doing today?";

        playLetter(" ");
        for (int i = 0; i < message.length(); i++) {
            playLetter(message.substring(i, i + 1).toLowerCase());
        }   
    
    }

    public static void playLetter(String letter) throws LineUnavailableException{
        final AudioFormat af =
        new AudioFormat(Note.SAMPLE_RATE, 8, 1, true, true);
        SourceDataLine line = AudioSystem.getSourceDataLine(af);

    line.open(af, Note.SAMPLE_RATE);
    line.start();
        String code = letters.get(letter);  
        System.out.println(" " + letter + " " + code);
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '.') {
                dit(line);
            } else if (code.charAt(i) == '-') {
                da(line);
            } else if (code.charAt(i) == ' ') {
                play(line, Note.WORD_REST, (int) (50 * tempo)); 
            }
        }
        play(line, Note.CHARACTER_REST, (int)(16 * tempo));
        line.drain();
        line.close();
    }
    public static void da(SourceDataLine line){
        play(line, Note.DA, (int) (50 * tempo));
        play(line, Note.TONE_REST, (int)(16 * tempo));
    }
    public static void dit(SourceDataLine line){
        play(line, Note.DIT, (int)(16 * tempo));
        play(line, Note.TONE_REST, (int)(16 * tempo));
    }


    private static void play(SourceDataLine line, Note note, int ms) {
        ms = Math.min(ms, Note.SECONDS * 1000);
        int length = Note.SAMPLE_RATE * ms / 1000;
        line.write(note.data(), 0, length);
    }

enum Note {

    TONE_REST, CHARACTER_REST, WORD_REST, DA, DIT;
    public static final int SAMPLE_RATE = 16 * 1024; // ~16KHz
    public static final int SECONDS = 2;
    private byte[] sin = new byte[SECONDS * SAMPLE_RATE];

    Note() {
        int n = this.ordinal();
        double f = 440d;
        double period = (double)SAMPLE_RATE / f;
        switch(n){
            case 0:
                for (int i = 0; i < sin.length; i++) {
                    sin[i] = 0;
                }
                break;
            case 1:
                for (int i = 0; i < sin.length; i++) {
                    sin[i] = 0;
                }
                break;
            case 2:
                for (int i = 0; i < sin.length; i++) {
                    sin[i] = 0;
                }
                break;
            case 3: // DA
                for (int i = 0; i < sin.length; i++) {
                    double angle = 2.0 * Math.PI * i / period;
                    sin[i] = (byte)(Math.sin(angle)  * 127f) ;
                }
                break;
            case 4: // DIT
            for (int i = 0; i < sin.length; i++) {
                double angle = 2.0 * Math.PI * i / period;
                sin[i] = (byte)(Math.sin(angle)  * 127f) ;
                }
            }
        } 
        byte[] data(){
            return sin;
        }
    }
}


