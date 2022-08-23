package silencer.commands.music;

import silencer.Bot;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.logging.Logger;

public class PauseCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        if (!Utils.checkUserSelfSameVoiceChannel(ctx)) {
            logger.warning("Pause: no song");
            return;
        }
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).getScheduler().getPlayer().setPaused(true);
        ctx.getChannel().sendMessage("Die Wiedergabe wurde pausiert").queue();
        logger.info("Pause");
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp() {
        return "Pausiert die Musikwiedergabe";
    }
}
