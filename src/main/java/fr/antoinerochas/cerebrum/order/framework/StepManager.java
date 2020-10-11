package fr.antoinerochas.cerebrum.order.framework;

import fr.antoinerochas.cerebrum.order.step.TypeStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * Log4J's {@link Logger} instance.
     */
    public static final Logger LOGGER = LogManager.getLogger(StepManager.class);

    /**
     * List users and their associated current {@link Step}s.
     */
    private final HashMap<Class<? extends Step>, Integer> positions = new HashMap<>();

    /**
     * List users and their associated current {@link Step}s.
     */
    private final HashMap<Integer, Class<? extends Step>> steps = new HashMap<>();

    public StepManager()
    {
        registerStep(TypeStep.class);
    }

    /**
     * Register a new step.
     *
     * @param step the step
     */
    public void registerStep(Class<? extends Step> step)
    {
        final int position = positions.size() + 1;
        LOGGER.info("Registering new Step: " + step.getSimpleName() + " with pos.: " + position);
        positions.put(step, positions.size() + 1);
        steps.put(position, step);
    }

    /**
     * Get the position of a {@link Step}.
     *
     * @param step the step
     * @return the step's position
     */
    public int getStepPosition(Class<? extends Step> step)
    {
        return positions.get(step);
    }

    /**
     * Get the next {@link Step} relative to the current position.
     *
     * @param position the position
     * @return the next {@link Step} instance
     */
    public <T> T getNextStep(int position)
    {
        return (T) steps.get(position + 1);
    }
}
