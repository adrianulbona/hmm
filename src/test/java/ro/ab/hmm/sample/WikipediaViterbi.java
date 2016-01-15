package ro.ab.hmm.sample;

import ro.ab.hmm.*;
import ro.ab.hmm.probability.ProbabilityCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by adrianbona on 1/14/16.
 */
public enum WikipediaViterbi {
	INSTANCE;

	public final Model<MedicalState, SYMPTOM> model;

	WikipediaViterbi() {
		final List<SYMPTOM> observations = asList(SYMPTOM.NORMAL, SYMPTOM.COLD, SYMPTOM.DIZZY);
		final ProbabilityCalculator<MedicalState, SYMPTOM> probabilityCalculator = new ProbabilityCalculator<>(
				StartProbabilities.INSTANCE.data::get,
				EmissionProbabilities.INSTANCE.data::get,
				TransitionProbabilities.INSTANCE.data::get);
		final ReachableStateFinder<MedicalState, SYMPTOM> reachableStateFinder = observation -> asList(MedicalState.values());
		this.model = new Model<>(observations, probabilityCalculator, reachableStateFinder);
	}

	public enum MedicalState implements State {
		HEALTHY,
		FEVER
	}

	public enum SYMPTOM implements Observation {
		NORMAL,
		COLD,
		DIZZY
	}

	public enum StartProbabilities {
		INSTANCE;

		public final Map<MedicalState, Double> data;

		StartProbabilities() {
			data = new HashMap<>();
			data.put(MedicalState.HEALTHY, 0.6);
			data.put(MedicalState.FEVER, 0.4);
		}
	}

	public enum TransitionProbabilities {
		INSTANCE;

		public final Map<Transition<MedicalState>, Double> data;

		TransitionProbabilities() {
			data = new HashMap<>();
			data.put(new Transition<>(MedicalState.HEALTHY, MedicalState.HEALTHY), 0.7);
			data.put(new Transition<>(MedicalState.HEALTHY, MedicalState.FEVER), 0.3);
			data.put(new Transition<>(MedicalState.FEVER, MedicalState.HEALTHY), 0.4);
			data.put(new Transition<>(MedicalState.FEVER, MedicalState.FEVER), 0.6);
		}
	}

	public enum EmissionProbabilities {
		INSTANCE;

		public final Map<Emission<MedicalState, SYMPTOM>, Double> data;

		EmissionProbabilities() {
			data = new HashMap<>();
			data.put(new Emission<>(MedicalState.HEALTHY, SYMPTOM.NORMAL), 0.5);
			data.put(new Emission<>(MedicalState.HEALTHY, SYMPTOM.COLD), 0.4);
			data.put(new Emission<>(MedicalState.HEALTHY, SYMPTOM.DIZZY), 0.1);
			data.put(new Emission<>(MedicalState.FEVER, SYMPTOM.NORMAL), 0.1);
			data.put(new Emission<>(MedicalState.FEVER, SYMPTOM.COLD), 0.3);
			data.put(new Emission<>(MedicalState.FEVER, SYMPTOM.DIZZY), 0.6);
		}
	}
}
