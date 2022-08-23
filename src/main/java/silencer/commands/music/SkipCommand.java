package silencer.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.GuildMusicManager;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;
import java.util.logging.Logger;

public class SkipCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        if (!Utils.checkUserSelfSameVoiceChannel(ctx)) {
            return;
        }

        AudioTrack nowPlaying = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).getScheduler().nowPlaying();
        TextChannel channel = ctx.getChannel();

        if(nowPlaying == null) {
            channel.sendMessage("Kein Song wird gerade gespielt").queue();
            logger.info("Skip: no tracks");
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.getScheduler().nextTrack();
        channel.sendMessage("Der aktuelle Song wurde geskipped").queue();
        logger.info("Skip");
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Skipped den aktuellen Song";
    }

    @Override
    public List<String> getAliases() {
        return List.of("s");
    }
}
