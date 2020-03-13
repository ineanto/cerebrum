package fr.antoinerochas.cerebrum.command;

import fr.antoinerochas.cerebrum.Cerebrum;
import fr.antoinerochas.cerebrum.command.api.Command;
import fr.antoinerochas.cerebrum.command.api.CommandExecutor;
import fr.antoinerochas.cerebrum.i18n.I18NManager;
import fr.antoinerochas.cerebrum.i18n.Language;
import fr.antoinerochas.cerebrum.jda.api.ReactionListener;
import fr.antoinerochas.cerebrum.user.CerebrumUser;
import fr.antoinerochas.cerebrum.utils.Color;
import fr.antoinerochas.cerebrum.utils.EmbedMaker;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.TimeUnit;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 02/03/2020
 * @since 1.0
 */
@Command(label = "language")
public class LanguageCommand
{
    @CommandExecutor
    public void execute(User user)
    {
        final CerebrumUser cerebrumUser = Cerebrum.getUserManager().getUser(user);
        final PrivateChannel channel = user.openPrivateChannel().complete();

        MessageEmbed.Field selectLanguage = new MessageEmbed.Field(I18NManager.getValue(cerebrumUser.getUserLanguage(), "langSelectionDesc"), I18NManager.getValue(cerebrumUser.getUserLanguage(), "langSelectionEmbed"), true);
        MessageEmbed lang = EmbedMaker.make(Color.GREEN, I18NManager.getValue(cerebrumUser.getUserLanguage(), "langSelectionTitle"), null, selectLanguage);
        channel.sendMessage(lang).queue(message ->
        {
            // FR FLAG: 🇫🇷
            // EN FLAG: 🇬🇧
            ReactionListener<String> handler = new ReactionListener<>(user.getIdLong(), message.getId());
            handler.setExpiresIn(TimeUnit.SECONDS, 30);
            handler.registerReaction("\uD83C\uDDEB\uD83C\uDDF7", (ret) -> updateLanguage(user, message, Language.FRENCH));
            handler.registerReaction("\uD83C\uDDEC\uD83C\uDDE7", (ret) -> updateLanguage(user, message, Language.ENGLISH));

            Cerebrum.getReactionManager().addReactionListener(Cerebrum.GUILD.getIdLong(), message, handler);
        });

        channel.close().complete();
    }

    private void updateLanguage(User user, Message message, Language language)
    {
        final CerebrumUser cerebrumUser = Cerebrum.getUserManager().getUser(user);
        final PrivateChannel channel = user.openPrivateChannel().complete();

        MessageEmbed.Field selectLanguage = new MessageEmbed.Field(I18NManager.getValue(cerebrumUser.getUserLanguage(), "langSelectionSuccessDesc"), language == Language.ENGLISH ? "English" : "Français", true);
        MessageEmbed lang = EmbedMaker.make(Color.GREEN, I18NManager.getValue(cerebrumUser.getUserLanguage(), "opSuccess"), null, selectLanguage);
        channel.sendMessage(lang).queue();

        cerebrumUser.setLanguage(language.ordinal());
        channel.close().complete();
    }
}
