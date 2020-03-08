package fr.antoinerochas.cerebrum.command.api;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This file is part of Cerebrum.
 *
 * @author Aro at/on 28/02/2020
 * @since 1.0
 */
public class JDACommand
{
    private String name;
    private String[] alias;
    private Method method;
    private Object object;

    public JDACommand(String name, String[] alias, Method method, Object object)
    {
        this.name = name;
        this.alias = alias;
        this.method = method;
        this.object = object;
    }

    public String getName()
    {
        return name;
    }

    public String[] getAlias()
    {
        return alias;
    }

    public Method getMethod()
    {
        return method;
    }

    public Object getObject()
    {
        return object;
    }

    @Override
    public String toString()
    {
        return "JDACommand{" +
                "name='" + name + '\'' +
                ", alias=" + Arrays.toString(alias) +
                '}';
    }
}
