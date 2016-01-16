package io.github.adrianulbona.hmm.solver;

import io.github.adrianulbona.hmm.sample.WikipediaViterbi;
import io.github.adrianulbona.hmm.sample.WikipediaViterbi.MedicalState;
import io.github.adrianulbona.hmm.sample.WikipediaViterbi.Symptom;
import org.junit.Ignore;
import org.junit.Test;
import io.github.adrianulbona.hmm.solver.MostProbableStateSequenceFinder.OptimalTransition;

import java.util.List;

import static io.github.adrianulbona.hmm.sample.WikipediaViterbi.MedicalState.FEVER;
import static io.github.adrianulbona.hmm.sample.WikipediaViterbi.MedicalState.HEALTHY;
import static io.github.adrianulbona.hmm.sample.WikipediaViterbi.Symptom.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

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
		assertEquals(asList(HEALTHY, HEALTHY, FEVER), solver.forObservations(asList(NORMAL, COLD, DIZZY)));
	}

	private static class Tracer implements MostProbableStateSequenceFinder.Observer<MedicalState,
			Symptom> {
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