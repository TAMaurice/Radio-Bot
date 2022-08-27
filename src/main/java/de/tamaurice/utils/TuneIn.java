package de.tamaurice.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;

public class TuneIn {
    String name, link;
    DiscordApi api;
    AudioPlayerManager audioPlayerManager;
    AudioSource source;
    AudioPlayer player;

    boolean isPlaying = false;

    public TuneIn(String name, String link, DiscordApi api) {
        this.name = name;
        this.link = link;
        this.api = api;

        audioPlayerManager = new DefaultAudioPlayerManager();
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());

        player = audioPlayerManager.createPlayer();

        source = new LavaplayerAudioSource(api, player);
    }

    public void play(AudioConnection audioConnection) {
        audioConnection.setAudioSource(source);

        audioPlayerManager.loadItem(link, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);
                isPlaying = true;
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }

    public boolean stopPlaying() {
        if(isPlaying) {
            audioPlayerManager.shutdown();
            return isPlaying;
        } else {
            return !isPlaying;
        }
    }
}
