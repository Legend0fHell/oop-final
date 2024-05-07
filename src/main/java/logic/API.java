package logic;

import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class API {
    // string const for language code
    public static final String VIETNAMESE = "vi-vn";
    public static final String ENGLISH_US = "en-us";
    public static final String ENGLISH_UK = "en-gb";
    public static final String AUTO = "";
    private static final String API_KEY = "ee1a861047db41e3aed6cca75554a826";
    private static final String AUDIO_PATH = "src/main/resources/media/audio.wav";
    public static String translate(String text, String lang_from, String lang_to) throws Exception {
        if (text == null || text.isEmpty()) {
            throw new Exception("Invalid text");
        }
        if (lang_from == null || lang_from.isEmpty()) {
            lang_from = AUTO;
        }
        if (lang_to == null || lang_to.isEmpty()) {
            throw new Exception("Invalid language code");
        }

        // take only the first part of the language code
        lang_from = lang_from.split("-")[0];
        lang_to = lang_to.split("-")[0];

        String urlScript = "https://script.google.com/macros/s/AKfycbw1qSfs1Hvfnoi3FzGuoDWijwQW69eGcMM_iGDF7p5vu1oN_CaFqIDFmCGzBuuGCk_N/exec" +
                "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                "&target=" + lang_to +
                "&source=" + lang_from;
        URI uri = new URI(urlScript);
        URL url = uri.toURL();
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static void speech(String text, String lang, float speed) throws Exception {
        if (text == null || text.isEmpty()) {
            throw new Exception("Invalid text");
        }
        if (lang == null || lang.isEmpty()) {
            throw new Exception("Invalid language code");
        }
        if (speed < 0.1 || speed > 5) {
            throw new Exception("Invalid speed");
        }
        VoiceProvider tts = new VoiceProvider(API_KEY);
        VoiceParameters params = new VoiceParameters(text, AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
        params.setBase64(false);
        params.setLanguage(lang);
        params.setVoice("Linda");
        params.setRate((int) Math.round(-2.9936 * speed * speed + 15.2942 * speed - 12.7612));

        byte[] voice = tts.speech(params);

        File audioFile = new File(AUDIO_PATH);
        URI audioURI = audioFile.toURI();

        FileOutputStream fos = new FileOutputStream(AUDIO_PATH);
        fos.write(voice, 0, voice.length);
        fos.flush();
        fos.close();

        Media input = new Media(audioURI.toString());
        MediaPlayer audio = new MediaPlayer(input);
        audio.play();
    }
}
