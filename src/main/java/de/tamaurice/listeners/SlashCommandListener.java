package de.tamaurice.listeners;

import de.tamaurice.utils.RadioStations;
import de.tamaurice.utils.TuneIn;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.List;

public class SlashCommandListener implements SlashCommandCreateListener {
    DiscordApi api;
    TuneIn speaker;
    RadioStations radioStations = new RadioStations();
    AudioConnection audioConnection;

    EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Radio-Bot")
            .setDescription("Listen to your favorite radio stations on Discord!")
            .addField("help", "Shows this manual")
            .addInlineField("join", "Makes it join your VC")
            .addInlineField("leave", "Makes it leave your VC")
            .addInlineField("tune STATION-NAME", "Starts playing the station provided")
            .addInlineField("add STATION-NAME STATION-LINK", "Adds your station to the bot [Careful! Has to be direct audio link like https://shoutcast.rtl.lu/rtl!]")
            .addInlineField("/remove STATION-NAME", "Removes the station from the list");

    public SlashCommandListener(DiscordApi api) {
        this.api = api;
        speaker = new TuneIn("0", "0", api);
    }

    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent event) {
        SlashCommandInteraction interaction = event.getSlashCommandInteraction();

        if (interaction.getCommandName().equals("help")) {
            interaction.getUser().sendMessage(embed);
            interaction
                    .createImmediateResponder()
                    .setContent("Message sent in DMs!")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }

        if (interaction.getCommandName().equals("join")) {
            List<ServerVoiceChannel> voiceChannels = interaction.getServer().get().getVoiceChannels();
            long voiceChannelID = 0;

            for (ServerVoiceChannel voiceChannel : voiceChannels) {
                if (interaction.getUser().isConnected(voiceChannel)) {
                    voiceChannelID = voiceChannel.getId();
                }
            }

            if(voiceChannelID != 0) {
                ServerVoiceChannel vc = interaction.getApi().getServerVoiceChannelById(voiceChannelID).get();
                vc.connect().thenAcceptAsync(audioConnection -> {
                    this.audioConnection = audioConnection;
                });

                interaction
                        .createImmediateResponder()
                        .setContent("Joined your VC!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();

            } else {
                interaction
                        .createImmediateResponder()
                        .setContent("Couldn't join your VC!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }

        if (interaction.getCommandName().equals("leave")) {
            List<ServerVoiceChannel> voiceChannels = interaction.getServer().get().getVoiceChannels();

            long voiceChannelID = 0;

            for (ServerVoiceChannel voiceChannel : voiceChannels) {
                if (interaction.getApi().getYourself().isConnected(voiceChannel)) {
                    voiceChannelID = voiceChannel.getId();
                }
            }

            if(voiceChannelID != 0) {
                ServerVoiceChannel vc = interaction.getApi().getServerVoiceChannelById(voiceChannelID).get();
                vc.disconnect();

                interaction
                        .createImmediateResponder()
                        .setContent("Left my current VC!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            } else {
                interaction
                        .createImmediateResponder()
                        .setContent("I'm not currently in a VC!")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            }
        }

        if (interaction.getCommandName().equals("list")) {
            interaction.getUser().sendMessage((new RadioStations().getRadioStations()).toString());
            interaction
                    .createImmediateResponder()
                    .setContent("Message sent in DMs!")
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }

        if (interaction.getCommandName().startsWith("add")) {
            String name = interaction.getArguments().get(0).getStringValue().toString();
            name = name.replace("Optional[", "");
            name = name.replace("]", "");

            String link = interaction.getArguments().get(1).getStringValue().toString();
            link = link.replace("Optional[", "");
            link = link.replace("]", "");

            radioStations.addStation(name, link);

            interaction
                    .createImmediateResponder()
                    .setContent("Added station " + name + " | " + link)
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }

        if (interaction.getCommandName().startsWith("remove")) {
            String name = interaction.getArguments().get(0).getStringValue().toString();
            name = name.replace("Optional[", "");
            name = name.replace("]", "");

            radioStations.removeStation(name);

            interaction
                    .createImmediateResponder()
                    .setContent("Removed station " + name)
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }

        if (interaction.getCommandName().startsWith("tune")) {
            String name = interaction.getArguments().get(0).getStringValue().toString();
            name = name.replace("Optional[", "");
            name = name.replace("]", "");

            speaker = new TuneIn(name, radioStations.getStationLink(name), api);
            speaker.play(audioConnection);

            interaction
                    .createImmediateResponder()
                    .setContent("Started playing " + name)
                    .setFlags(MessageFlag.EPHEMERAL)
                    .respond();
        }

        if (interaction.getCommandName().equals("stop")) {
            if(speaker.stopPlaying())
                interaction
                        .createImmediateResponder()
                        .setContent("Stopped playing")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
            api.updateActivity(ActivityType.PLAYING, "nothing right now");
            } else {
                interaction
                        .createImmediateResponder()
                        .setContent("Nothing is playing")
                        .setFlags(MessageFlag.EPHEMERAL)
                        .respond();
        }
    }
}
