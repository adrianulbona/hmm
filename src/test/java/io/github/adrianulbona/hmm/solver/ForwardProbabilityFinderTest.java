package io.github.adrianulbona.hmm.solver;

import io.github.adrianulbona.hmm.Emission;
import io.github.adrianulbona.hmm.Model;
import io.github.adrianulbona.hmm.sample.WikipediaViterbi;
import io.github.adrianulbona.hmm.sample.WikipediaViterbi.MedicalState;
import io.github.adrianulbona.hmm.sample.WikipediaViterbi.Symptom;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.adrianulbona.hmm.sample.WikipediaViterbi.MedicalState.FEVER;
import static io.github.adrianulbona.hmm.sample.WikipediaViterbi.MedicalState.HEALTHY;
import static io.github.adrianulbona.hmm.sample.WikipediaViterbi.Symptom.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created by adrianbona on 1/14/16.
 */
public class ForwardProbabilityFinderTest {

	@Test
	public void test() throws Exception {
		final Model<MedicalState, Symptom> model = WikipediaViterbi.INSTANCE.model;
		final ForwardProbabilityFinder<MedicalState, Symptom> solver =
				new ForwardProbabilityFinder<>(model);
		solver.addObserver(new Checker());
		/*
		* 	Healthy: 0.30000 0.08400 0.00588
		*	Fever:   0.04000 0.02700 0.01512
		*/
		final List<Symptom> observations = asList(NORMAL, COLD, DIZZY);
		solver.basedOn(observations);
	}

	private static class Checker implements ForwardProbabilityFinder.Observer<MedicalState, Symptom> {

		private Map<MedicalState, Double> expectedProbabilities;

		@Override
		public void processingObservation(Symptom observation) {
			System.out.println("processing observation: " + observation);
			expectedProbabilities = new HashMap<>();
			final Model<MedicalState, Symptom> model = WikipediaViterbi.INSTANCE.model;
			switch (observation) {
				case NORMAL:
					expectedProbabilities.put(HEALTHY, 0.3);
					expectedProbabilities.put(FEVER, 0.04);
					break;
				case COLD:
					expectedProbabilities.put(HEALTHY, 0.0904);
					expectedProbabilities.put(FEVER, 0.0342);
					break;
				case DIZZY:
					expectedProbabilities.put(HEALTHY, 0.007696);
					expectedProbabilities.put(FEVER, 0.028584);
					break;
			}
		}

		@Override
		public void foundForwardProbability(MedicalState state, Double probability) {
			assertEquals(expectedProbabilities.get(state), probability, 0.000005);
			System.out.println(state + " - " + probability);
		}
	}
}