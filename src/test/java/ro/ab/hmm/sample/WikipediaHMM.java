package ro.ab.hmm.sample;

import ro.ab.hmm.*;
import ro.ab.hmm.probability.ProbabilityCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static ro.ab.hmm.sample.WikipediaHMM.Activity.CLEAN;
import static ro.ab.hmm.sample.WikipediaHMM.Activity.SHOP;
import static ro.ab.hmm.sample.WikipediaHMM.Activity.WALK;

/**
 * Created by adrianbona on 1/14/16.
 */
public enum WikipediaHMM {
	INSTANCE;

	public final Model<Weather, Activity> model;

	WikipediaHMM() {
		final List<Activity> observations = asList(CLEAN, CLEAN, CLEAN, CLEAN, WALK, WALK);
		final ProbabilityCalculator<Weather, Activity> probabilityCalculator = new ProbabilityCalculator<>(
				s -> 1.0 / Weather.values().length,
				EmissionProbabilities.INSTANCE.data::get,
				TransitionProbabilities.INSTANCE.data::get);
		final ReachableStateFinder<Weather, Activity> reachableStateFinder = observation -> asList(Weather.values());
		this.model = new Model<>(observations, probabilityCalculator, reachableStateFinder);
	}

	public enum Weather implements State {
		SUNNY,
		RAINY
	}

	public enum Activity implements Observation {
		WALK,
		SHOP,
		CLEAN
	}


	public enum TransitionProbabilities {
		INSTANCE;

		public final Map<Transition<Weather>, Double> data;

		TransitionProbabilities() {
			data = new HashMap<>();
			data.put(new Transition<>(Weather.RAINY, Weather.RAINY), 0.7);
			data.put(new Transition<>(Weather.RAINY, Weather.SUNNY), 0.3);
			data.put(new Transition<>(Weather.SUNNY, Weather.SUNNY), 0.6);
			data.put(new Transition<>(Weather.SUNNY, Weather.RAINY), 0.4);
		}
	}

	public enum EmissionProbabilities {
		INSTANCE;

		public final Map<Emission<Weather, Activity>, Double> data;

		EmissionProbabilities() {
			data = new HashMap<>();
			data.put(new Emission<>(Weather.SUNNY, WALK), 0.1);
			data.put(new Emission<>(Weather.SUNNY, SHOP), 0.4);
			data.put(new Emission<>(Weather.SUNNY, CLEAN), 0.5);
			data.put(new Emission<>(Weather.RAINY, WALK), 0.6);
			data.put(new Emission<>(Weather.RAINY, SHOP), 0.3);
			data.put(new Emission<>(Weather.RAINY, CLEAN), 0.1);
		}
	}
}
