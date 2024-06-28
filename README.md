# warn_bot

> You can add the script to a bot you issued by changing the token (which is an environmental variable) in the config files.
> Just ensure that you don't make the token public

warn_bot is a simple Discord bot designed to assist server moderators in maintaining a positive and respectful environment. The bot provides functionality to warn users who violate the server's rules or guidelines. When a user receives a warning, the bot logs the warning and keeps track of the user's warning count. If a user accumulates a certain number of warnings, the bot can take further action, such as muting the user or restricting their access to certain channels.

warn_bot helps moderators by automating the warning process, making it easier to manage and track user behavior. It also helps to ensure that warnings are applied consistently and fairly across all users. Additionally, warn_bot can provide feedback to users who receive warnings, reminding them of the server's rules and encouraging them to follow them in the future.

Overall, warn_bot is a useful tool for server moderation, helping to maintain a friendly and respectful community for all users.

# Features
1. Timeouts can be adjusted to the server owners preferance
2. Users may be timedout and banned from the server
3. Sessions between each user may be kept private to each user but public to the server admin
4. Total number of warnings issued per user may be seen 
5. Perma-bans and timeouts may be issued through the bot.
6. Users may be limited from accessing other bots through the use of `warn_bot`.

Interaction included in the bot may be customized through prompts, just ensure that the required priviledges are assigned to the bot beforehand. (the feature mentioned here will be included eventually as it is still a proposed project in the feature branch)
