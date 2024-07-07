package events;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class interactionEventListener extends ListenerAdapter {

    // Initializing variables
    private int warning_1 = 10, warning_2 = 10; // Changes if the user changes it
    private String default_warning_msg = "You've been warned";

    private final Map<User, Integer> warningMap = new HashMap<>();
    private final Map<User, Integer> timeoutMap = new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private SlashCommandInteractionEvent event;

    public void output_msg_private(String value) {
        event.reply(value).setEphemeral(true).queue();
    }

    public void output_msg_public(String value) {
        event.reply(value).setEphemeral(false).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        this.event = event;
        super.onSlashCommandInteraction(event);
        System.out.println("interaction made with the bot");
        String slashName = event.getName(); // Returns the name of the command

        if (timeoutMap.containsKey(event.getUser())) {
            output_msg_private("You are currently timed out from using the bot.");
            return;
        }

        Guild guild = event.getGuild();

        switch (slashName) {
            case "warn":
                User user = Objects.requireNonNull(event.getOption("username")).getAsUser();
                int warnings = warningMap.getOrDefault(user, 0) + 1;
                warningMap.put(user, warnings);

                String warning = event.getOption("warning") != null ? Objects.requireNonNull(event.getOption("warning")).getAsString() : "";
                String mention = user.getAsMention(); // Get the mention tag
                if (warning.isEmpty())
                    output_msg_public(mention + " was warned | NO: **" + warnings + "** | MESSAGE: **" + default_warning_msg + "**");
                else
                    output_msg_public(mention + " was warned | NO: **" + warnings + "** | MESSAGE: **" + warning + "**");

                // 3 warnings and the user is timed out for 10 seconds, 10 warnings = 10 minutes
                if (warnings == 3) {
                    timeoutMap.put(user, 1); // Puts the user into the hashmap
                    scheduler.schedule(() -> timeoutMap.remove(user), warning_1, TimeUnit.SECONDS);
                } else if (warnings == 10) {
                    timeoutMap.put(user, 1);
                    scheduler.schedule(() -> timeoutMap.remove(user), warning_2, TimeUnit.MINUTES);
                }
                break;

            case "check-warnings":
                User checkUser = Objects.requireNonNull(event.getOption("username")).getAsUser();
                int userWarnings = warningMap.getOrDefault(checkUser, 0);
                if (userWarnings == 0) {
                    output_msg_private(checkUser.getName()+" has no warnings");
                } else {
                    output_msg_private(checkUser.getName()+" has " + userWarnings + " warnings");
                }
                break;

            case "ban":
                output_msg_public("WIP!!!");
                break;

            case "set-pref":
                warning_1 = Objects.requireNonNull(event.getOption("first_warning_timeout")).getAsInt();
                warning_2 = Objects.requireNonNull(event.getOption("second_warning_timeout")).getAsInt();
                if (event.getOption("default-warning-message") != null)
                    default_warning_msg = Objects.requireNonNull(event.getOption("default-warning-message")).getAsString();
                output_msg_private("Preferences have been set!");
                break;

            case "current-prefs":
                output_msg_private("Current timeouts:\n**warning 1**: " + warning_1 + " seconds\n**warning_2**: " + warning_2 + " minutes\n**Default Message**: " + default_warning_msg);
                break;

            case "roles":
                long role_to_assign = Objects.requireNonNull(event.getOption("assign-role")).getAsLong();
                User user_to_assign = Objects.requireNonNull(event.getOption("user-to-assign")).getAsUser();
                Role role_to_add = guild.getRoleById(role_to_assign);
                guild.addRoleToMember(user_to_assign,role_to_add).queue();
                output_msg_private("Role assigned!");
                break;

            case "remove-role":
                long role_to_absolve = Objects.requireNonNull(event.getOption("absolve-role")).getAsLong();
                User user_to_absolve = Objects.requireNonNull(event.getOption("user-to-absolve")).getAsUser();
                Role role_to_remove = guild.getRoleById(role_to_absolve);
                if (role_to_remove != null) {
                    Member member = guild.getMember(user_to_absolve);
                    if (member != null && member.getRoles().contains(role_to_remove)) {
                        guild.removeRoleFromMember(member, role_to_remove).queue();
                        output_msg_private("Role removed!");
                    } else {
                        output_msg_private("User does not have the specified role.");
                    }
                } else {
                    output_msg_private("Role not found.");
                }
                break;
        }
    }
}
