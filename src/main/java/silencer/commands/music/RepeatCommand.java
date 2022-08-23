package silencer.commands.music;

import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.PlayerManager;
import silencer.audio.TrackScheduler;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;
import java.util.logging.Logger;

public class RepeatCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        if (!Utils.checkUserSelfSameVoiceChannel(ctx)) {
            return;
        }

        TrackScheduler scheduler = PlayerManager.getInstance().getMusicManager(ctx.getGuild()).getScheduler();
        boolean repeating = scheduler.toggleRepeating();

        TextChannel channel = ctx.getChannel();
        channel.sendMessage("Warteschlange wiederholen " + (repeating?"aktiviert":"deaktiviert")).queue();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Wiederholt die Queue";
    }

    @Override
    public List<String> getAliases() {
        return List.of("r");
    }
}
