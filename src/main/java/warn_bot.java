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

        jda.upsertCommand("check-warnings","checks the number of warnings you currently have")
                .addOption(OptionType.USER,"username","Mention the user to see how many warnings they have",true)
                .setGuildOnly(true)
                .queue();

        jda.upsertCommand("ban","bans a user from a server for a set amount of time")
                .addOption(OptionType.USER,"user_to_ban","Mention the user to ban",true)
                .setGuildOnly(true)
                .queue();

        jda.upsertCommand("set-pref","Sets the base timeouts for users")
                .addOption(OptionType.INTEGER,"first_warning_timeout","Sets the first timeout(secs)",true)
                .addOption(OptionType.INTEGER,"second_warning_timeout","Sets the second timeout(mins)",true)
                .addOption(OptionType.STRING,"default-warning-message","Sets the default warning message to the user",false)
                .setGuildOnly(true)
                .queue();

        jda.upsertCommand("current-prefs","displays the current prefs and timeouts").setGuildOnly(true).queue();

        jda.upsertCommand("roles","assigns roles to users that each have unique permissions")
                .addOption(OptionType.ROLE,"assign-role","role that is to be assigned",true)
                .addOption(OptionType.USER,"user-to-assign","mentions the user to have a role assigned to",true)
                .setGuildOnly(true)
                .queue();
    }
}