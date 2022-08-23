package silencer;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
/*
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent ev) {
        switch (ev.getMessage().getContentRaw().toLowerCase()) {
            case PREFIX + "join":
                joinChannel(ev);
                break;
            case PREFIX + "leave":
                leaveChannel(ev);
                break;
            case PREFIX + "stfu":
                play(ev, "stfu.mp3");
                break;
            case PREFIX + "shotgun":
                play(ev, "shotgun.mp3");
                break;
        }
    }

    public void play(GuildMessageReceivedEvent ev, String audio) {
        VoiceChannel userChannel = ev.getMember().getVoiceState().getChannel();
        GuildVoiceState botVoiceState = ev.getGuild().getSelfMember().getVoiceState();
        VoiceChannel joinedChannel = botVoiceState.getChannel();

        if (userChannel == null) {
            return;
        }

        if (!botVoiceState.inVoiceChannel()) {
            joinedChannel = joinChannel(ev);
        }

        if (!userChannel.equals(joinedChannel)) {
            return;
        }

        PlayerManager.getInstance().loadAndPlay(ev.getChannel(), audio);
    }

    public VoiceChannel joinChannel(GuildMessageReceivedEvent ev) {
        VoiceChannel connectedChannel = ev.getMember().getVoiceState().getChannel();

        if (connectedChannel == null) {
            return null;
        }

        if (!ev.getGuild().getSelfMember().hasPermission(connectedChannel, Permission.VOICE_CONNECT)) {
            return null;
        }
        ev.getGuild().getAudioManager().openAudioConnection(connectedChannel);
        return connectedChannel;
    }

    public void leaveChannel(GuildMessageReceivedEvent ev) {
        VoiceChannel connectedChannel = ev.getGuild().getSelfMember().getVoiceState().getChannel();

        if (connectedChannel == null) {
            return;
        }

        ev.getGuild().getAudioManager().closeAudioConnection();
    }*/
}