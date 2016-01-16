package io.github.adrianulbona.hmm.sample;

import io.github.adrianulbona.hmm.*;
import io.github.adrianulbona.hmm.probability.ProbabilityCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static io.github.adrianulbona.hmm.sample.WikipediaHMM.Activity.CLEAN;
import static io.github.adrianulbona.hmm.sample.WikipediaHMM.Activity.SHOP;
import static io.github.adrianulbona.hmm.sample.WikipediaHMM.Activity.WALK;

/**
 * Created by adrianbona on 1/14/16.
 */
public enum WikipediaHMM {
	INSTANCE;

	public final Model<Weather, Activity> model;

	WikipediaHMM() {
		final double fairProbability = 1.0 / Weather.values().length;
		final ProbabilityCalculator<Weather, Activity> probabilityCalculator = new ProbabilityCalculator<>(
				s -> fairProbability,
				EmissionProbabilities.INSTANCE.data::get,
				TransitionProbabilities.INSTANCE.data::get);
		final ReachableStateFinder<Weather, Activity> reachableStateFinder = observation -> asList(Weather.values());
		this.model = new Model<>(probabilityCalculator, reachableStateFinder);
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
