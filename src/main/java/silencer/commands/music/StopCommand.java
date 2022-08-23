package silencer.commands.music;

import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.GuildMusicManager;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;
import java.util.logging.Logger;

public class StopCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        if (!Utils.checkUserSelfSameVoiceChannel(ctx)) {
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.getScheduler().getPlayer().stopTrack();
        musicManager.getScheduler().getQueue().clear();

        TextChannel channel = ctx.getChannel();
        channel.sendMessage("Die Wiedergabe wurde gestoppt").queue();
        logger.info("Stop");

    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Stoppt den Song und cleared die Warteschlange";
    }

    @Override
    public List<String> getAliases() {
        return List.of("st");
    }
}
