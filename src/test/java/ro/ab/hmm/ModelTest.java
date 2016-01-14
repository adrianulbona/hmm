package ro.ab.hmm;

import lombok.Data;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ro.ab.hmm.probability.ProbabilityCalculator;
import ro.ab.hmm.sample.Wikipedia;
import ro.ab.hmm.sample.Wikipedia.Activity;
import ro.ab.hmm.sample.Wikipedia.Weather;
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

	@Test
	public void testObservationsCount() {
		assertEquals(6, Wikipedia.INSTANCE.hmm.observationCount());
	}

	@Test
	public void testDistinctStatesCount() {
		assertEquals(2, Wikipedia.INSTANCE.hmm.numberOfDistinctStates());
	}

	@Test
	public void testRetrievalOfEmissionProbabilities() {
		stream(Activity.values()).forEach(o -> {
			final Map<Emission<Weather, Activity>, Double> emissionProbabilities =
					Wikipedia.INSTANCE.hmm.emissionProbabilitiesFor(o);
			emissionProbabilities.forEach(
					(e, p) -> assertEquals(Wikipedia.EmissionProbabilities.INSTANCE.data.get(e), p, .001));
		});
	}

	@Test
	public void testRetrievalOfReachableStates() {
		stream(Activity.values()).forEach(o ->
				assertEquals(asList(Weather.values()), Wikipedia.INSTANCE.hmm.getReachableStatesFor(o)));
	}

	@Test
	public void testRetrievalOfTransitionProbabilities() {
		allPossibleObservationPairs().stream()
				.map(op -> Wikipedia.INSTANCE.hmm.transitionProbabilitiesFor(op.getFirst(), op.getSecond())
						.entrySet())
				.forEach(transitionProbabilities -> transitionProbabilities.forEach(tp -> {
					final Double expected = Wikipedia.TransitionProbabilities.INSTANCE.data.get(tp.getKey());
					assertEquals(expected, tp.getValue(), .001);
				}));
	}

	private List<ObservationPair<Activity>> allPossibleObservationPairs() {
		return stream(Activity.values())
				.flatMap(a -> stream(Activity.values()).map(na -> new ObservationPair<>(a, na)))
				.collect(toList());
	}

	@Data
	private static class ObservationPair<O extends Observation> {
		private final O first;
		private final O second;
	}
}