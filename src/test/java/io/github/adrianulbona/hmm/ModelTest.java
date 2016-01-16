package io.github.adrianulbona.hmm;

import lombok.Data;
import org.junit.Test;
import io.github.adrianulbona.hmm.sample.WikipediaHMM;
import io.github.adrianulbona.hmm.sample.WikipediaHMM.Activity;
import io.github.adrianulbona.hmm.sample.WikipediaHMM.Weather;

import java.util.List;
import java.util.Map;

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
		assertEquals(6, WikipediaHMM.INSTANCE.model.observationCount());
	}

	@Test
	public void testDistinctStatesCount() {
		assertEquals(2, WikipediaHMM.INSTANCE.model.numberOfDistinctStates());
	}

	@Test
	public void testRetrievalOfEmissionProbabilities() {
		stream(Activity.values()).forEach(o -> {
			final Map<Emission<Weather, Activity>, Double> emissionProbabilities =
					WikipediaHMM.INSTANCE.model.emissionProbabilitiesFor(o);
			emissionProbabilities.forEach(
					(e, p) -> assertEquals(WikipediaHMM.EmissionProbabilities.INSTANCE.data.get(e), p, .001));
		});
	}

	@Test
	public void testRetrievalOfReachableStates() {
		stream(Activity.values()).forEach(o ->
				assertEquals(asList(Weather.values()), WikipediaHMM.INSTANCE.model.getReachableStatesFor(o)));
	}

	@Test
	public void testRetrievalOfTransitionProbabilities() {
		allPossibleObservationPairs().stream()
				.map(op -> WikipediaHMM.INSTANCE.model.transitionProbabilitiesFor(op.getFirst(), op.getSecond())
						.entrySet())
				.forEach(transitionProbabilities -> transitionProbabilities.forEach(tp -> {
					final Double expected = WikipediaHMM.TransitionProbabilities.INSTANCE.data.get(tp.getKey());
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