package silencer.commands.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import silencer.Bot;
import silencer.audio.GuildMusicManager;
import silencer.audio.PlayerManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("ConstantConditions")
public class LeaveCommand implements ICommand {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            logger.warning("Leave: not in channel");
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel selfChannel = selfVoiceState.getChannel();

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        musicManager.getScheduler().getPlayer().stopTrack();
        musicManager.getScheduler().getQueue().clear();
        musicManager.getScheduler().setRepeating(false);

        audioManager.closeAudioConnection();
        channel.sendMessageFormat("`%s` verlassen", selfChannel.getName()).queue();
        logger.info("Leave");
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Bot verlässt einen VoiceChannel";
    }

    @Override
    public List<String> getAliases() {
        return List.of("l");
    }
}
