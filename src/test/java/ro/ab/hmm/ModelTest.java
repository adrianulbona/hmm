package ro.ab.hmm;

import lombok.Data;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ro.ab.hmm.probability.ProbabilityCalculator;
import ro.ab.hmm.solver.MostProbableStateSequenceFinder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;


/**
 * Created by adrianbona on 1/13/16.
 */
public class ModelTest {

	private Model<Weather, Activity> hmm;

	@Before
	public void setup() {
		hmm = new Model<>(observations(), probabilityCalculator(), allStatesReachable());
	}

	@Test
	public void testViterbi() {
		new MostProbableStateSequenceFinder<>(hmm).solve();
	}

	@Test
	public void testObservationsCount() {
		assertEquals(4, hmm.observationCount());
	}

	@Test
	public void testDistinctStatesCount() {
		assertEquals(2, hmm.numberOfDistinctStates());
	}

	@Test
	public void testRetrievalOfEmissionProbabilities() {
		stream(Activity.values()).forEach(o -> {
			final Map<Emission<Weather, Activity>, Double> emissionProbabilities = hmm.emissionProbabilitiesFor(o);
			emissionProbabilities.forEach((e, p) -> assertEquals(EmissionProbabilities.INSTANCE.data.get(e), p, .001));
		});
	}

	@Test
	public void testRetrievalOfReachableStates() {
		stream(Activity.values()).forEach(o ->
				assertEquals(allStatesReachable().reachableFor(o), hmm.getReachableStatesFor(o)));
	}

	@Test
	public void testRetrievalOfTransitionProbabilities() {
		allPossibleObservationPairs().stream()
				.map(op -> hmm.transitionProbabilitiesFor(op.getFirst(), op.getSecond())
						.entrySet())
				.forEach(transitionProbabilities -> transitionProbabilities.forEach(tp -> {
					final Double expected = TransitionProbabilities.INSTANCE.data.get(tp.getKey());
					assertEquals(expected, tp.getValue(), .001);
				}));
	}

	private List<ObservationPair<Activity>> allPossibleObservationPairs() {
		return stream(Activity.values())
				.flatMap(a -> stream(Activity.values()).map(na -> new ObservationPair<>(a, na)))
				.collect(toList());
	}

	private List<Activity> observations() {
		return asList(Activity.CLEAN, Activity.SHOP, Activity.SHOP, Activity.WALK);
	}

	private ReachableStateFinder<Weather, Activity> allStatesReachable() {
		return observation -> asList(Weather.values());
	}

	private ProbabilityCalculator<Weather, Activity> probabilityCalculator() {
		return new ProbabilityCalculator<>(EmissionProbabilities.INSTANCE.data::get,
				TransitionProbabilities.INSTANCE.data::get);
	}

	private enum Weather implements State {
		SUNNY,
		RAINY
	}


	private enum Activity implements Observation {
		WALK,
		SHOP,
		CLEAN
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

	@Data
	private static class ObservationPair<O extends Observation> {
		private final O first;
		private final O second;
	}
}