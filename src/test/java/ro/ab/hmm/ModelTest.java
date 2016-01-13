package ro.ab.hmm;

import org.junit.Test;
import ro.ab.hmm.probability.ProbabilityCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;


/**
 * Created by adrianbona on 1/13/16.
 */
public class ModelTest {

    @Test
    public void testCreation() {
        final Model<Weather, Activity> hmm = new Model<>(observations(), probabilityCalculator(), allStatesReachable());

        assertEquals(4, hmm.observationCount());
        assertEquals(2, hmm.numberOfDistinctStates());
        assertEquals(3, hmm.numberOfDistinctObservations());
    }

    private List<Activity> observations() {
        return asList(Activity.CLEAN, Activity.SHOP, Activity.SHOP, Activity.WALK);
    }

    private ReachableStateFinder<Weather, Activity> allStatesReachable() {
        return o -> asList(Weather.values());
    }

    private ProbabilityCalculator<Weather, Activity> probabilityCalculator() {
        return new ProbabilityCalculator<>(EmissionProbabilities.INSTANCE.data::get,
                TransitionProbabilities.INSTANCE.data::get);
    }

    private enum Weather implements State {
        SUNNY, RAINY
    }


    private enum Activity implements Observation {
        WALK, SHOP, CLEAN
    }


    private enum TransitionProbabilities {
        INSTANCE;

        private final Map<Transition<Weather>, Double> data;

        TransitionProbabilities() {
            data = new HashMap<>();
            data.put(new Transition<>(Weather.RAINY, Weather.RAINY), 0.7);
            data.put(new Transition<>(Weather.RAINY, Weather.SUNNY), 0.3);
            data.put(new Transition<>(Weather.SUNNY, Weather.SUNNY), 0.6);
            data.put(new Transition<>(Weather.SUNNY, Weather.RAINY), 0.4);
        }
    }


    private enum EmissionProbabilities {
        INSTANCE;

        private final Map<Emission<Weather, Activity>, Double> data;

        EmissionProbabilities() {
            data = new HashMap<>();
            data.put(new Emission<>(Weather.SUNNY, Activity.WALK), 0.1);
            data.put(new Emission<>(Weather.SUNNY, Activity.SHOP), 0.4);
            data.put(new Emission<>(Weather.SUNNY, Activity.CLEAN), 0.5);
            data.put(new Emission<>(Weather.RAINY, Activity.WALK), 0.6);
            data.put(new Emission<>(Weather.RAINY, Activity.SHOP), 0.3);
            data.put(new Emission<>(Weather.RAINY, Activity.CLEAN), 0.1);
        }
    }
}