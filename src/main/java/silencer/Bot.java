package silencer;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Bot {

    // https://discord.com/api/oauth2/authorize?client_id=792052038610452531&permissions=8&scope=bot

    public static final String PREFIX = "°";
    public static final String VERSION = "1.4.1";
    public static final String ID = "792052038610452531";
    public static final String LOGGER_NAME = "inc.biedermann.silencer.logger";

    public static void main(String[] args) throws LoginException, IOException {

        FileHandler handler = new FileHandler(System.getProperty("user.home") + "/silencer.log", true);
        Logger logger = Logger.getLogger(LOGGER_NAME);
        logger.addHandler(handler);

        logger.info("Startup");

        JDABuilder.createDefault(
                        Dotenv.load().get("JDA_TOKEN"),
                        //GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_VOICE_STATES)
                .enableCache(CacheFlag.VOICE_STATE)
                .disableCache(CacheFlag.EMOTE)
                .setActivity(Activity.playing("v." + VERSION + " -> " + PREFIX + "help"))
                .addEventListeners(new Listener())
                .build();
    }
}
