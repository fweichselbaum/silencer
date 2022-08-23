package silencer.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.Queue;
import java.util.logging.Logger;

public class ShuffleCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        if (!Utils.checkUserSelfSameVoiceChannel(ctx)) {
            return;
        }

        Queue<AudioTrack> queue = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).getScheduler().getQueue();
        TextChannel channel = ctx.getChannel();

        if(queue.isEmpty()) {
            channel.sendMessage("Die Warteschlange ist leer").queue();
            logger.warning("Shuffle: no tracks");
            return;
        }

        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).getScheduler().shuffle();
        channel.sendMessage("Die Warteschlange wurde geshuffled").queue();
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getHelp() {
        return "Shuffled die Warteschlange";
    }
}
