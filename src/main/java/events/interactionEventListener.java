package events;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class interactionEventListener extends ListenerAdapter {

    // Initializing variables
    private int warnings;

    private Map<User, Integer> timeoutMap = new HashMap<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private SlashCommandInteractionEvent event;

    public void output_msg_private(String value){event.reply(value).setEphemeral(true).queue();}
    public void output_msg_public(String value){event.reply(value).setEphemeral(true).queue();}

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        this.event = event;
        super.onSlashCommandInteraction(event);
        System.out.println("interaction made with the bot");
        String slashName = event.getName();// Returns the name of the command
        if (timeoutMap.containsKey(event.getUser())) {
            output_msg_private("You are currently timed out from using the bot.");
            return;
        }
        switch (slashName){
            case "warn":
                warnings++;
                String user = Objects.requireNonNull(event.getOption("username")).getAsUser().getName();
                String warning = event.getOption("warning") !=null ? Objects.requireNonNull(event.getOption("warning")).getAsString() : "";
                if (warning == "")
                    output_msg_public("**"+user+"**"+ " was warned | NO: **"+warnings+"**");
                else
                    output_msg_public("**"+user+"**" + " was warned | NO: **"+warnings+"** | MESSAGE: **"+warning+"**");

                // 3 warnings and the user is timed out for 10 seconds, 10 warnings = 10 minutes
                if (warnings == 3) {
                    timeoutMap.put(event.getUser(), 1);
                    scheduler.schedule(() -> timeoutMap.remove(event.getUser()), 10, TimeUnit.SECONDS);
                } else if (warnings==10) {
                    timeoutMap.put(event.getUser(), 1);
                    scheduler.schedule(() -> timeoutMap.remove(event.getUser()),10, TimeUnit.MINUTES);
                }

            case "check-warnings":
                switch (warnings){
                    case 0:
                        output_msg_private("Congrats, you have no warnings!");
                        break;
                    default:
                        output_msg_private("You have "+warnings+" warnings.");
                }
        }
    }
}
