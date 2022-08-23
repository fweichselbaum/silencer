package silencer.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import silencer.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class TrackScheduler extends AudioEventAdapter {
    private static final Logger logger = Logger.getLogger(Bot.LOGGER_NAME);
    private final AudioPlayer player;
    private final Queue<AudioTrack> queue;
    private boolean repeating = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    public AudioTrack nowPlaying() {
        return player.getPlayingTrack();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(repeating) {
            queue.add(track.makeClone());
            logger.info("Queue poll: next track with repeating");
        }
        if (endReason.mayStartNext) {
            nextTrack();
            logger.info("Queue poll: next track");
        }
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public Queue<AudioTrack> getQueue() {
        return queue;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
        logger.info("Set repeating: " + this.repeating);
    }

    public boolean toggleRepeating() {
        setRepeating(!repeating);
        return repeating;
    }

    public void shuffle() {
        List<AudioTrack> saved = new ArrayList<>(queue);
        Collections.shuffle(saved);
        queue.clear();
        queue.addAll(saved);
        logger.info("Queue shuffle");
    }
}
