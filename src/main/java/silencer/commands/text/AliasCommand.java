package silencer.commands.text;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.CommandManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;

public class AliasCommand implements ICommand {

    private final CommandManager manager;

    public AliasCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            EmbedBuilder eb = new EmbedBuilder().setTitle("Abkürzungen von Commands");

            manager.getCommands().forEach(
                    cmd -> eb.addField(Bot.PREFIX + cmd.getName(),
                            String.join(",", cmd.getAliases()),
                            false)
            );

            channel.sendMessage(eb.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Kein Command `" + search + "` gefunden").queue();
            return;
        }

        channel.sendMessage(command.getAliases().toString()).queue();
    }

    @Override
    public String getName() {
        return "alias";
    }

    @Override
    public String getHelp() {
        return "Zeigt alle Abküzungen für Commands an";
    }

    @Override
    public List<String> getAliases() {
        return List.of("al");
    }
}
