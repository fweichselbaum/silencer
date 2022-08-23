package silencer.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.audio.GuildMusicManager;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;
import java.util.logging.Logger;

public class NowPlayingCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        TextChannel channel = ctx.getChannel();
        StringBuilder sb = new StringBuilder();

        AudioTrack nowPlaying = musicManager.getScheduler().nowPlaying();

        if(nowPlaying == null) {
            channel.sendMessage("Im Moment wird kein Song abgespielt").queue();
            logger.info("Now playing: no song");
            return;
        }

        EmbedBuilder eb = new EmbedBuilder().setTitle("Jetzt spielt");

        eb.addField(nowPlaying.getInfo().title, Utils.createTimeBar(nowPlaying, ctx.getGuild()), false);

        channel.sendMessage(eb.build()).queue();
        logger.info("Now playing: " + nowPlaying);
    }



    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp() {
        return "Zeigt den aktuellen Song und Position an";
    }

    @Override
    public List<String> getAliases() {
        return List.of("np", "now");
    }
}
