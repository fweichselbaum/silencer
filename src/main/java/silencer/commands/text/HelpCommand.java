package silencer.commands.text;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import silencer.Bot;
import silencer.CommandManager;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            EmbedBuilder eb = new EmbedBuilder().setTitle("Liste von Commands");

            manager.getCommands().forEach(
                    cmd -> eb.addField(Bot.PREFIX + cmd.getName(),
                            cmd.getHelp(),
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

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Zeigt alle Commands an";
    }

    @Override
    public List<String> getAliases() {
        return List.of("h", "cmds", "commands");
    }
}
