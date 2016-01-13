package ro.ab.hmm;

import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * Created by adrianbona on 1/13/16.
 */
public class ModelTest {

    @Test
    public void testCreation() {
        final Map<Transition<Weather>, Double> transitionProbabilities = transitionProbabilities();
        final List<Activity> observations = asList(Activity.CLEAN, Activity.SHOP, Activity.SHOP, Activity.WALK);
        final List<ObservationProbabilities<Activity, Weather>> observationProbabilities = observations.stream()
                .map(o -> new ObservationProbabilities<>(o, transitionProbabilities, emissionProbabilitiesFor(o)))
                .collect(toList());
        final Model<Activity, Weather> hmm = new Model<>(observationProbabilities);

        assertEquals(4, hmm.observationCount());
        assertEquals(2, hmm.numberOfDistinctStates());
        assertEquals(3, hmm.numberOfDistinctObservations());
    }

    private Map<Transition<Weather>, Double> transitionProbabilities() {
        final Map<Transition<Weather>, Double> transitionProbabilities = new HashMap<>();
        transitionProbabilities.put(new Transition<>(Weather.RAINY, Weather.RAINY), 0.7);
        transitionProbabilities.put(new Transition<>(Weather.RAINY, Weather.SUNNY), 0.3);
        transitionProbabilities.put(new Transition<>(Weather.SUNNY, Weather.SUNNY), 0.6);
        transitionProbabilities.put(new Transition<>(Weather.SUNNY, Weather.RAINY), 0.4);
        return transitionProbabilities;
    }

    private Map<Weather, Double> emissionProbabilitiesFor(Activity observation) {
        final Map<Weather, Double> emissionProbabilities = new HashMap<>();
        switch (observation) {
            case WALK:
                emissionProbabilities.put(Weather.SUNNY, 0.1);
                emissionProbabilities.put(Weather.RAINY, 0.6);
                break;
            case SHOP:
                emissionProbabilities.put(Weather.SUNNY, 0.4);
                emissionProbabilities.put(Weather.RAINY, 0.3);
                break;
            case CLEAN:
                emissionProbabilities.put(Weather.SUNNY, 0.5);
                emissionProbabilities.put(Weather.RAINY, 0.1);
                break;
        }
        return emissionProbabilities;
    }

    private enum Weather implements State {
        SUNNY, RAINY
    }

    private enum Activity implements Observation {
        WALK, SHOP, CLEAN
    }
}