package de.tamaurice.listeners;

import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class MessageListener implements MessageCreateListener {
    DiscordApi api;

    public MessageListener(DiscordApi api) {
        this.api = api;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

    }
}
