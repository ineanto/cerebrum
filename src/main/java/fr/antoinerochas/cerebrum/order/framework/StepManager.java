package fr.antoinerochas.cerebrum.order.framework;

import fr.antoinerochas.cerebrum.user.CerebrumUser;

import java.util.HashMap;

/**
 * This file is part of Cerebrum.
 * Manages {@link Step}.
 *
 * @author aro on 17/03/2020
 * @since 1.0
 */
public class StepManager
{
    /**
     * List users and their associated current {@link Step}s.
     */
    private final HashMap<String, Step> steps = new HashMap<>();

    /**
     * Return the current {@link Step} the {@link CerebrumUser} is at.
     *
     * @return the current {@link Step}
     */
    public Step getCurrentStep(final String id)
    {
        return steps.get(id);
    }
}
