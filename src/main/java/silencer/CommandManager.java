package silencer;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;
import silencer.commands.music.*;
import silencer.commands.text.AliasCommand;
import silencer.commands.text.HSCommand;
import silencer.commands.text.HelpCommand;
import silencer.commands.text.PingCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new HelpCommand(this));
        addCommand(new AliasCommand(this));
        addCommand(new PingCommand());
        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new SkipCommand());
        addCommand(new ClearCommand());
        addCommand(new ShuffleCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new QueueCommand());
        //addCommand(new RepeatCommand());
        addCommand(new HSCommand());
    }

    private void addCommand(ICommand cmd) {
        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Bot.PREFIX), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            try {
                cmd.handle(ctx);
            } catch (InsufficientPermissionException ex) {
                event.getChannel().sendMessage(ex.getMessage()).queue();
            }
        }
    }

}
