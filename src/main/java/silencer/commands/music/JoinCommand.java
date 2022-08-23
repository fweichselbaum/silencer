package silencer.commands.music;

import silencer.commands.CommandContext;
import silencer.commands.ICommand;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        Utils.joinToUserIfPossible(ctx);
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Bot joint einem VoiceChannel";
    }

    @Override
    public List<String> getAliases() {
        return List.of("j");
    }
}
