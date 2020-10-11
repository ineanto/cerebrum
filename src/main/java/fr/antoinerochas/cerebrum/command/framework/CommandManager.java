package fr.antoinerochas.cerebrum.command.framework;

import fr.antoinerochas.cerebrum.Cerebrum;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.internal.utils.Checks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 28/02/2020
 * @since 1.0
 */
public class CommandManager
{
    /**
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(CommandManager.class);

    private static final Map<Class<?>, Function<Message, ?>> ARGUMENTS_TYPE = new HashMap<>();

    private Map<String, JDACommand> commands     = new HashMap<>();
    private Map<String, JDACommand> commandAlias = new HashMap<>();

    public CommandManager()
    {
        LOGGER.debug("Loading CommandManager...");
        registerArgumentType(Message.class, m -> m);
        registerArgumentType(User.class, Message::getAuthor);
        registerArgumentType(Member.class, Message::getMember);
        registerArgumentType(TextChannel.class, Message::getTextChannel);
        registerArgumentType(Guild.class, Message::getGuild);
        registerArgumentType(JDA.class, Message::getJDA);
        registerArgumentType(String[].class, m -> new String[0]);
    }

    public void registerAll(Object... objects)
    {
        for (Object o : objects)
        {
            register(o);
        }
    }

    public void register(Object o)
    {
        Class<?> clazz = o.getClass();

        if (clazz.isAnnotationPresent(Command.class))
        {
            String label = clazz.getAnnotation(Command.class).label();

            Method[] methods = Arrays.stream(clazz.getMethods())
                    .filter(m -> m.isAnnotationPresent(CommandExecutor.class))
                    .toArray(Method[]::new);

            Checks.check(methods.length > 0, "No CommandExecutor found for command " + label);
            Checks.check(methods.length < 2, "More than one CommandExecutor found for command " + label);

            String[] alias = clazz.getAnnotation(Command.class).alias();

            checkArguments(methods[0]);
            register(new JDACommand(label, alias, methods[0], o));
        }
        else
        {
            for (Method method : clazz.getDeclaredMethods())
            {
                if (method.isAnnotationPresent(Command.class))
                {
                    String label = method.getAnnotation(Command.class).label();
                    String[] alias = clazz.getAnnotation(Command.class).alias();

                    checkArguments(method);
                    register(new JDACommand(label, alias, method, o));
                }
            }
        }
    }

    private void checkArguments(Method method)
    {
        for (Parameter parameter : method.getParameters())
        {
            Class<?> type = parameter.getType();

            Checks.check(ARGUMENTS_TYPE.get(type) != null, "Invalid argument type: " + type.getName());
        }
    }

    private void register(JDACommand command)
    {
        commands.put(command.name().toLowerCase(), command);

        for (String alias : command.alias())
        {
            commandAlias.put(alias.toLowerCase(), command);
        }

        LOGGER.info("Registered " + command.toString() + ".");
    }

    public void unregister(String commandName)
    {
        JDACommand command = commands.get(commandName);

        if (command != null)
        {
            unregister(command);
        }
    }

    public void unregister(JDACommand command)
    {
        Checks.notNull(command, "command");

        commands.remove(command.name());

        for (String alias : command.alias())
        {
            String s = alias.toLowerCase();

            while (commands.get(s) != null)
            {
                commands.remove(s);
            }
        }
    }

    public boolean executeCommand(Message message)
    {
        Checks.notNull(message, "message");

        String prefix = Cerebrum.PREFIX;
        if (!message.getContentRaw().startsWith(prefix)) { return false; }

        String content = message.getContentRaw().substring(prefix.length());
        String[] split = content.split(" ");

        if (content.isEmpty() || split.length == 0 || split[0].isEmpty()) { return false; }

        String commandName = split[0].toLowerCase();
        JDACommand command = commands.get(commandName);

        if (command == null)
        {
            command = commandAlias.get(commandName);
            if (command == null) { return false; }
        }

        LOGGER.info("{} issued command: \"{}\"", message.getAuthor().getName(), message.getContentRaw());

        String[] args = Arrays.copyOfRange(split, 1, split.length);

        Object[] parameters = Arrays.stream(command.method().getParameters())
                .map(p -> p.getType() == String[].class ? args : ARGUMENTS_TYPE.get(p.getType()).apply(message))
                .toArray(Object[]::new);

        try
        {
            command.method().invoke(command.object(), parameters);
        }
        catch (Exception e)
        {
            LOGGER.error("Error while dispatching command {}", command, e);
        }

        return true;
    }

    public Collection<JDACommand> getCommands()
    {
        return Collections.unmodifiableCollection(commands.values());
    }

    private static <T> void registerArgumentType(Class<T> clazz, Function<Message, T> function)
    {
        ARGUMENTS_TYPE.put(clazz, function);
    }
}
