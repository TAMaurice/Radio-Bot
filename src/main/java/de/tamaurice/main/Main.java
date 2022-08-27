package de.tamaurice.main;

import de.tamaurice.listeners.MessageListener;
import de.tamaurice.listeners.SlashCommandListener;
import de.tamaurice.utils.SlashCommands;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {
    DiscordApi api = new DiscordApiBuilder()
            .setToken("")
            .login().join();

    public Main() {
        initListeners();
        new SlashCommands(api).init();
    }

    public static void main(String args[]) {
        new Main();
    }


    private void initListeners() {
        api.addListener(new MessageListener(api));
        api.addSlashCommandCreateListener(new SlashCommandListener(api));
    }
}
