package ro.ab.hmm.solver;

import org.junit.Ignore;
import org.junit.Test;
import ro.ab.hmm.sample.WikipediaHMM;
import ro.ab.hmm.sample.WikipediaHMM.Weather;
import ro.ab.hmm.sample.WikipediaViterbi;
import ro.ab.hmm.sample.WikipediaViterbi.MedicalState;
import ro.ab.hmm.sample.WikipediaViterbi.Symptom;
import ro.ab.hmm.solver.MostProbableStateSequenceFinder.OptimalTransition;

import java.text.NumberFormat;
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
	public void testSolveWikipediaViterti() throws Exception {
		final MostProbableStateSequenceFinder<MedicalState, Symptom>
				solver = new MostProbableStateSequenceFinder<>(WikipediaViterbi.INSTANCE.model);
		solver.addObserver(new Tracer());
		/*
		* 	Healthy: 0.30000 0.08400 0.00588
		*	Fever:   0.04000 0.02700 0.01512
		*/
		assertEquals(asList(MedicalState.HEALTHY, MedicalState.HEALTHY, MedicalState.FEVER), solver.solve());
	}

	private static class Tracer implements MostProbableStateSequenceFinder.Observer<MedicalState, Symptom> {
		@Override
		public void processingObservation(Symptom observation) {
			System.out.println("reducing observation: " + observation);
		}

		@Override
		public void foundOptimalTransitions(OptimalTransition<MedicalState> optimalTransition) {
			System.out.println(
					String.format("%s <- %.5f", optimalTransition.getState(), optimalTransition.getProbability()));
		}
	}
}