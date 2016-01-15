package ro.ab.hmm.solver;

import org.junit.Ignore;
import org.junit.Test;
import ro.ab.hmm.sample.WikipediaHMM;
import ro.ab.hmm.sample.WikipediaHMM.Weather;
import ro.ab.hmm.sample.WikipediaViterbi;
import ro.ab.hmm.sample.WikipediaViterbi.MedicalState;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static ro.ab.hmm.sample.WikipediaHMM.Weather.RAINY;
import static ro.ab.hmm.sample.WikipediaHMM.Weather.SUNNY;

/**
 * Created by adrianbona on 1/14/16.
 */
public class MostProbableStateSequenceFinderTest {

	@Ignore
	@Test
	public void testSolveWikipediaHMM() throws Exception {
		// todo: check manually
		final List<Weather> mostProbableStates = new MostProbableStateSequenceFinder<>(
				WikipediaHMM.INSTANCE.model).solve();
		assertEquals(asList(RAINY, RAINY, RAINY, SUNNY, SUNNY, SUNNY), mostProbableStates);
	}

	@Ignore
	@Test
	public void testSolveWikipediaViterti() throws Exception {
		// todo: check manually
		final List<MedicalState> mostProbableStates = new MostProbableStateSequenceFinder<>(
				WikipediaViterbi.INSTANCE.model).solve();
		assertEquals(asList(MedicalState.HEALTHY, MedicalState.HEALTHY, MedicalState.FEVER), mostProbableStates);
	}
}