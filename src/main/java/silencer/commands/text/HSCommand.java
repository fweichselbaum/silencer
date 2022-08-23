package silencer.commands.text;

import net.dv8tion.jda.api.entities.TextChannel;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

public class HSCommand implements ICommand{
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        channel.sendMessage("\"Du Hurensohn\" -<@404209807671820288>").queue();
    }

    @Override
    public String getName() {
        return "hs";
    }

    @Override
    public String getHelp() {
        return "HS";
    }
}
