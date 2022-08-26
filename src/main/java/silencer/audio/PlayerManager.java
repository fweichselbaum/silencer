package silencer.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PlayerManager {

    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager audioPlayerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private PlayerManager() {
        musicManagers = new HashMap<>();
        audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), guildID -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackname, boolean singleTrack) {

        final GuildMusicManager musicManager = getMusicManager(channel.getGuild());
        audioPlayerManager.loadItemOrdered(musicManager, trackname, new AudioLoadResultHandler() {
            private final TrackScheduler scheduler = musicManager.getScheduler();

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                scheduler.queue(audioTrack);
                channel.sendMessage("`" + audioTrack.getInfo().title + "` von `" + audioTrack.getInfo().author +
                        "` zur Warteschlange hinzugefügt").queue();
                logger.info("Queue added: " + audioTrack.getInfo().title);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (singleTrack) {
                    final AudioTrack firstTrack = audioPlaylist.getTracks().get(0);
                    scheduler.queue(firstTrack);
                    channel.sendMessage("`" + firstTrack.getInfo().title + "` von `" + firstTrack.getInfo().author +
                            "` zur Warteschlange hinzugefügt").queue();
                    logger.info("Queue added first track: " + firstTrack.getInfo());
                } else {
                    List<AudioTrack> tracks = audioPlaylist.getTracks();
                    tracks.forEach(scheduler::queue);
                    channel.sendMessage(tracks.size() + " zur Warteschlange hinzugefügt").queue();
                    logger.info("Queue added playlist: " + audioPlaylist.getName());
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Kein Song `" + trackname + "` gefunden").queue();
                logger.warning("Not found: " + trackname);
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Irgendwas is hinig").queue();
                logger.severe("Loading failed: " + e.getMessage());
            }
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}