package silencer.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

public class ClearCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        if (!Utils.checkUserSelfSameVoiceChannel(ctx)) {
            return;
        }

        Queue<AudioTrack> queue = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).getScheduler().getQueue();
        TextChannel channel = ctx.getChannel();

        if (queue.isEmpty()) {
            channel.sendMessage("Die Warteschlange ist leer").queue();
            logger.warning("Queue: already clear");
            return;
        }

        queue.clear();
        channel.sendMessage("Die Warteschlange wurde gecleared").queue();
        logger.info("Queue: cleared");
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getHelp() {
        return "Cleared die Warteschlange";
    }

    @Override
    public List<String> getAliases() {
        return List.of("c");
    }
}
