package silencer.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    public List<String> getArgs() {
        return args;
    }

    public TextChannel getChannel() {
        return getEvent().getChannel();
    }

    public JDA getJDA() {
        return getEvent().getJDA();
    }

    public Member getMember() {
        return getEvent().getMember();
    }

    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    public Guild getGuild() {
        return getEvent().getGuild();
    }

    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

}
