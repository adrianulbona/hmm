package io.github.adrianulbona.hmm.solver;

import io.github.adrianulbona.hmm.sample.WikipediaViterbi;
import org.junit.Ignore;
import org.junit.Test;
import io.github.adrianulbona.hmm.solver.MostProbableStateSequenceFinder.OptimalTransition;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created by adrianbona on 1/14/16.
 */
public class MostProbableStateSequenceFinderTest {

	@Ignore
	@Test
	public void testSolveWikipediaViterti() throws Exception {
		final MostProbableStateSequenceFinder<WikipediaViterbi.MedicalState, WikipediaViterbi.Symptom>
				solver = new MostProbableStateSequenceFinder<>(WikipediaViterbi.INSTANCE.model);
		solver.addObserver(new Tracer());
		/*
		* 	Healthy: 0.30000 0.08400 0.00588
		*	Fever:   0.04000 0.02700 0.01512
		*/
		assertEquals(asList(WikipediaViterbi.MedicalState.HEALTHY, WikipediaViterbi.MedicalState.HEALTHY, WikipediaViterbi.MedicalState.FEVER), solver.solve());
	}

	private static class Tracer implements MostProbableStateSequenceFinder.Observer<WikipediaViterbi.MedicalState, WikipediaViterbi.Symptom> {
		@Override
		public void processingObservation(WikipediaViterbi.Symptom observation) {
			System.out.println("reducing observation: " + observation);
		}

		@Override
		public void foundOptimalTransitions(OptimalTransition<WikipediaViterbi.MedicalState> optimalTransition) {
			System.out.println(
					String.format("%s <- %.5f", optimalTransition.getState(), optimalTransition.getProbability()));
		}
	}
}