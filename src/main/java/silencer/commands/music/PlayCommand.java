package silencer.commands.music;

import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;
import java.util.logging.Logger;

public class PlayCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        Utils.joinToUserIfPossible(ctx);

        TextChannel channel = ctx.getChannel();

        if(ctx.getArgs().isEmpty()) {
            channel.sendMessage("Nach dem `play` Command muss ein YouTube Link übergeben werden").queue();
            logger.warning("Play: no song");
            return;
        }

        String link = String.join(" " , ctx.getArgs());

        if(!Utils.checkURL(link)) {
            link = "ytsearch:" + link;
            PlayerManager.getInstance().loadAndPlay(channel, link, true);
        } else {
            PlayerManager.getInstance().loadAndPlay(channel, link, false);
        }
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Spielt einen Song von YouTube [Link]";
    }

    @Override
    public List<String> getAliases() {
        return List.of("p");
    }
}
