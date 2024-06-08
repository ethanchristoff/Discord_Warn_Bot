import events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class warn_bot {
    public static void main(String[] args) {
        final String token = System.getenv("token");
        JDABuilder jdaBuilder = JDABuilder.createDefault(token);

        JDA jda = jdaBuilder
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new interactionEventListener())
                .build();

        jda.upsertCommand("warn","Warns a user, if warnings greater than 3 the user will be timed out")
                .addOption(OptionType.USER,"username","choose a user to warn",true)
                .addOption(OptionType.STRING,"warning","type the warning the user is to see",false)
                .setGuildOnly(true)
                .queue();

        jda.upsertCommand("check-warnings","checks the number of warnings you currently have").setGuildOnly(true).queue();
    }
}