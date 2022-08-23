package silencer.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.GuildMusicManager;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class QueueCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        TextChannel channel = ctx.getChannel();

        AudioTrack nowPlaying = musicManager.getScheduler().nowPlaying();

        if(nowPlaying == null) {
            channel.sendMessage("Die Warteschlange ist leer").queue();
            logger.warning("Queue: no track");
            return;
        }

        EmbedBuilder eb = new EmbedBuilder().setTitle("Warteschlange");

        long minutesDur = TimeUnit.MILLISECONDS.toMinutes(nowPlaying.getDuration());
        long secondsDur = TimeUnit.MILLISECONDS.toSeconds(nowPlaying.getDuration()) % 60;
        long minutesPos = TimeUnit.MILLISECONDS.toMinutes(nowPlaying.getPosition());
        long secondsPos = TimeUnit.MILLISECONDS.toSeconds(nowPlaying.getPosition()) % 60;

        eb.addField("Jetzt",
                String.format("%s (%d:%02d / %d:%02d)", nowPlaying.getInfo().title, minutesPos, secondsPos, minutesDur, secondsDur),
                false);

        Queue<AudioTrack> fullQueue = musicManager.getScheduler().getQueue();
        List<AudioTrack> queue = new ArrayList<>(fullQueue).subList(0, Math.min(10, fullQueue.size()));
        int index = 1;
        for (AudioTrack track : queue) {
            long min = TimeUnit.MILLISECONDS.toMinutes(track.getDuration());
            long sec = TimeUnit.MILLISECONDS.toSeconds(track.getDuration()) % 60;
            eb.addField(String.format("#%d (%d:%02d)", (index++), min, sec), track.getInfo().title + " - " + track.getInfo().author, false);
        }

        if (queue.size() == 10) {
            eb.addField("...", "", false);
        }

        channel.sendMessage(eb.build()).queue();
        logger.info("Queue: " + queue.size());
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Zeigt die aktuelle Warteschlange an";
    }

    @Override
    public List<String> getAliases() {
        return List.of("q");
    }
}
