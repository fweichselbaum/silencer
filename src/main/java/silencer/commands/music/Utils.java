package silencer.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import silencer.Bot;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@SuppressWarnings("ConstantConditions")
public class Utils {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    public static boolean joinToUserIfPossible(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member self = ctx.getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();
        Member member = ctx.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Du musst in einem VoiceChannel sein").queue();
            logger.warning("Join: user not in channel");
            return false;
        }

        if (memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            logger.info("Join: already joined");
            return false;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessageFormat("Connected zu `%s`", memberChannel.getName()).queue();
        logger.info("Join");

        return true;
    }

    public static boolean checkUserSelfSameVoiceChannel(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        Member self = ctx.getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            return false;
        }

        Member member = ctx.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!selfVoiceState.getChannel().equals(memberVoiceState.getChannel())) {
            channel.sendMessage("Ich muss im gleichen VoiceChannel sein").queue();
            return false;
        }

        return true;
    }

    public static void autoLeave(GuildVoiceLeaveEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();

        VoiceChannel channel = event.getChannelLeft();

        if (audioManager.isConnected()
                && audioManager.getConnectedChannel().equals(channel)
                && channel.getMembers().size() == 1
                && channel.getMembers().get(0).getUser().getId().equals(Bot.ID)) {
            audioManager.closeAudioConnection();
            logger.info("Autoleave");
        }
    }

    public static boolean checkURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException ignored) {
        }
        return false;
    }

    public static String createTimeBar(AudioTrack nowPlaying, Guild guild) {
        StringBuilder sb = new StringBuilder();
        long duration = nowPlaying.getDuration();
        long position = nowPlaying.getPosition();

        long minutesDur = TimeUnit.MILLISECONDS.toMinutes(duration);
        long secondsDur = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        long minutesPos = TimeUnit.MILLISECONDS.toMinutes(position);
        long secondsPos = TimeUnit.MILLISECONDS.toSeconds(position) % 60;

        sb.append(String.format("%d:%02d ", minutesPos, secondsPos));

        double percentage = (float) position / duration;
        int maxLen = 30;
        long sliderPos = Math.round(percentage * maxLen);
        int i = 0;

        for (; i < sliderPos; i++) {
            sb.append('-');
        }
        sb.append(PlayerManager.getInstance().getMusicManager(guild).getScheduler().getPlayer().isPaused() ? '⏸' : '▶');
        for (i++; i < maxLen; i++) {
            sb.append('-');
        }

        sb.append(String.format(" %d:%02d", minutesDur, secondsDur));

        return sb.toString();
    }
}
