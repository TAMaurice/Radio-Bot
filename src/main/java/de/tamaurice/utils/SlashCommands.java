package de.tamaurice.utils;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Arrays;

public class SlashCommands {
    SlashCommand    help, join, leave, add, remove, list, tune, stop;
    DiscordApi      api;

    public SlashCommands(DiscordApi api) {
        this.api = api;
    }

    public void init() {
        help = SlashCommand.with("help", "Returns a manual of all the commands")
                .createGlobal(api)
                .join();

        join = SlashCommand.with("join", "The bot joins your current VC")
                .createGlobal(api)
                .join();

        leave = SlashCommand.with("leave", "The bot leaves their current VC")
                .createGlobal(api)
                .join();

        add = SlashCommand.with("add", "Adds a new station to the bot",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "modifiers", "Station-Name and Station-Link",
                            Arrays.asList(
                                SlashCommandOption.create(SlashCommandOptionType.STRING, "name", "Put in the name of the station (NO SPACES!)"),
                                SlashCommandOption.create(SlashCommandOptionType.STRING, "link", "Put in a link which directly plays the radio! (like https://shoutcast.rtl.lu/rtl)")))))

                .createGlobal(api)
                .join();

        remove = SlashCommand.with("remove", "Remove a station from the bot",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "modifier", "Station-Name",
                                Arrays.asList(
                                                SlashCommandOption.create(SlashCommandOptionType.STRING, "name", "Put in the name of the station (NO SPACES!)")))))
                .createGlobal(api)
                .join();

        list = SlashCommand.with("list", "Lists all the stations!")
                .createGlobal(api)
                .join();

        tune = SlashCommand.with("tune", "Tune into a station from the bot",
                        Arrays.asList(
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "modifier", "Station-Name",
                                        Arrays.asList(
                                                SlashCommandOption.create(SlashCommandOptionType.STRING, "name", "Put in the name of the station")))))
                .createGlobal(api)
                .join();
    }
}
