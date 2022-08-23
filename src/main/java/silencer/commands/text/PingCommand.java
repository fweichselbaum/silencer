package silencer.commands.text;

import net.dv8tion.jda.api.JDA;
import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                ping -> ctx.getChannel()
                        .sendMessageFormat("Reset ping: %sms\nGateway ping: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getHelp() {
        return "Zeigt den Ping an";
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public List<String> getAliases() {
        return List.of("pi");
    }
}
