package io.github.TAMaurice.Main;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.UserStatus;

public class Main {

    public final static String RADIONAME = "X"; // Insert Radio Name here.

    public static void main(String[] args) {
        new Main();
        System.out.println(RADIONAME + " started!");
    }

    public Main() {
        DiscordApi api = new DiscordApiBuilder().setToken(BotToken.token).login().join();

        api.updateStatus(UserStatus.ONLINE);
        api.updateActivity(RADIONAME + "!");

        api.addListener(new CommandListener());

    }
}
