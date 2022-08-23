package silencer.commands.music;

import silencer.Bot;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.logging.Logger;

public class ResumeCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        if (!Utils.checkUserSelfSameVoiceChannel(ctx)) {
            return;
        }
        PlayerManager.getInstance().getMusicManager(ctx.getGuild()).getScheduler().getPlayer().setPaused(false);
        ctx.getChannel().sendMessage("Die Wiedergabe wurde fortgesetzt").queue();
        logger.info("Resume");
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return "Setzt die Musikwiedergabe fort";
    }
}
