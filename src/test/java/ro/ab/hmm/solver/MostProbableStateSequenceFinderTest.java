package ro.ab.hmm.solver;

import org.junit.Test;
import ro.ab.hmm.sample.Wikipedia;
import ro.ab.hmm.sample.Wikipedia.Activity;
import ro.ab.hmm.sample.Wikipedia.Weather;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static ro.ab.hmm.sample.Wikipedia.Weather.RAINY;
import static ro.ab.hmm.sample.Wikipedia.Weather.SUNNY;

/**
 * Created by adrianbona on 1/14/16.
 */
public class MostProbableStateSequenceFinderTest {

	@Test
	public void testSolve() throws Exception {
		// todo: check manually
		final List<Weather> mostProbableStates = new MostProbableStateSequenceFinder<>(Wikipedia.INSTANCE.hmm).solve();
		assertEquals(asList(RAINY, RAINY, RAINY, SUNNY, SUNNY, SUNNY), mostProbableStates);
	}
}