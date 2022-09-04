package de.tamaurice.main;

import de.tamaurice.listeners.MessageListener;
import de.tamaurice.listeners.SlashCommandListener;
import de.tamaurice.utils.SlashCommands;
import org.apache.http.ExceptionLogger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

public class Main {
    public static void main(String args[]) {
        new DiscordApiBuilder()
                .setToken("")
                .setCurrentShard(0)
                .setTotalShards(2)
                .loginAllShards()
                .forEach(shardFuture -> shardFuture
                        .thenAcceptAsync(Main::onShardLogin));
    }

    private static void onShardLogin(DiscordApi api) {
        System.out.println("Shard: " + api.getCurrentShard() + " logged in!");
        api.addListener(new MessageListener(api));
        api.addSlashCommandCreateListener(new SlashCommandListener(api));
        new SlashCommands(api).init();
        api.updateActivity(ActivityType.PLAYING, "Radio");
    }
}
