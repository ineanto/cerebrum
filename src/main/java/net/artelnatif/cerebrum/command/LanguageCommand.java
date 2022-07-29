package net.artelnatif.cerebrum.command;

import net.artelnatif.cerebrum.Cerebrum;
import net.artelnatif.cerebrum.command.framework.Command;
import net.artelnatif.cerebrum.command.framework.CommandExecutor;
import net.artelnatif.cerebrum.embed.EmbedMaker;
import net.artelnatif.cerebrum.i18n.I18N;
import net.artelnatif.cerebrum.i18n.Messages;
import net.artelnatif.cerebrum.i18n.Language;
import net.artelnatif.cerebrum.jda.api.ReactionListener;
import net.artelnatif.cerebrum.user.CerebrumUser;
import net.artelnatif.cerebrum.utils.Color;
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
public class LanguageCommand {
    @CommandExecutor
    public void execute(User user) {
        final CerebrumUser cerebrumUser = Cerebrum.getUserManager().getUser(user);
        final PrivateChannel channel = user.openPrivateChannel().complete();

        MessageEmbed.Field selectLanguage = new MessageEmbed.Field(I18N.get(cerebrumUser, Messages.Language.MESSAGE), I18N.get(cerebrumUser, Messages.Language.DESC), true);
        MessageEmbed lang = EmbedMaker.make(Color.GREEN, I18N.get(cerebrumUser, Messages.Language.TITLE), null, selectLanguage);
        channel.sendMessageEmbeds(lang).queue(message ->
        {
            // FR FLAG: 🇫🇷
            // EN FLAG: 🇬🇧
            ReactionListener<String> handler = new ReactionListener<>(user.getIdLong(), message.getId());
            handler.setExpiresIn(TimeUnit.SECONDS, 30);
            handler.registerReaction("\uD83C\uDDEB\uD83C\uDDF7", (ret) -> updateLanguage(user, message, Language.FRENCH));
            handler.registerReaction("\uD83C\uDDEC\uD83C\uDDE7", (ret) -> updateLanguage(user, message, Language.ENGLISH));

            Cerebrum.getReactionManager().addReactionListener(Cerebrum.GUILD.getIdLong(), message, handler);
        });

        channel.delete().complete();
    }

    private void updateLanguage(User user, Message message, Language language) {
        final CerebrumUser cerebrumUser = Cerebrum.getUserManager().getUser(user);
        final PrivateChannel channel = user.openPrivateChannel().complete();

        MessageEmbed.Field langUpdated = new MessageEmbed.Field(I18N.get(cerebrumUser, Messages.Language.UPDATE_MSG), language == Language.ENGLISH ? "English" : "Français", true);
        MessageEmbed lang = EmbedMaker.make(Color.GREEN, I18N.get(cerebrumUser, Messages.Global.SUCCESS), null, langUpdated);
        channel.sendMessageEmbeds(lang).queue();

        cerebrumUser.setLanguage(language.ordinal());
        channel.delete().complete();
    }
}
