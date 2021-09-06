package io.github.TAMaurice.Main;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.List;

public class CommandListener implements MessageCreateListener {

    private static final String RADIOLINK = "https://..."; // Add Radio Link here

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        switch(event.getMessageContent()) {
            case "!" + Main.RADIONAME: // Replace RTL with a radio name of your choice
                event.getChannel().sendMessage(Main.RADIONAME);
                System.out.println(event.getMessageAuthor() + " sent: " + event.getMessageContent());
                break;

            case "!"+Main.RADIONAME+"radio":
                System.out.println(event.getMessageAuthor() + " sent: " + event.getMessageContent());

                User user = event.getMessage().getAuthor().asUser().get();
                long userVoiceChannelId = 0;

                List<ServerVoiceChannel> voiceChannels = event.getServer().get().getVoiceChannels();
                for (ServerVoiceChannel chan : voiceChannels) {
                    if (user.isConnected(chan)) {
                        userVoiceChannelId = chan.getId();
                    }
                }
                // If User is connected
                if (userVoiceChannelId != 0) {
                    ServerVoiceChannel channel = event.getApi().getServerVoiceChannelById(userVoiceChannelId).get();
                    channel.connect().thenAcceptAsync(audioConnection -> {
                        System.out.println("Connected to " + channel.getName());
                        event.getChannel().sendMessage("Joined " + channel.getName());

                        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                        playerManager.registerSourceManager(new HttpAudioSourceManager());
                        AudioPlayer player = playerManager.createPlayer();

                        AudioSource source = new LavaPlayerAudioSource(event.getApi(), player);
                        audioConnection.setAudioSource(source);

                        playerManager.loadItem(RADIOLINK, new AudioLoadResultHandler() {
                            @Override
                            public void trackLoaded(AudioTrack track) {
                                player.setVolume(100);
                                player.setFrameBufferDuration(1);
                                player.playTrack(track);

                            }

                            @Override
                            public void playlistLoaded(AudioPlaylist playlist) {
                                for (AudioTrack track : playlist.getTracks()) {
                                    player.setVolume(50);
                                    player.setFrameBufferDuration(1);
                                    player.playTrack(track);
                                }
                            }

                            @Override
                            public void noMatches() {
                                // Notify the user that we've got nothing
                            }

                            @Override
                            public void loadFailed(FriendlyException throwable) {
                                // Notify the user that everything exploded
                            }

                        });

                    }).exceptionally(e -> {
                        // Failed to connect to voice channel (no permissions?)
                        e.printStackTrace();
                        return null;
                    });

                } else if (userVoiceChannelId == 0) {
                    event.getChannel().sendMessage("You're not connected to a voice channel!");
                    System.out.println(event.getMessageAuthor() + " not connected to VC.");
                }
                break;
        }
    }
}