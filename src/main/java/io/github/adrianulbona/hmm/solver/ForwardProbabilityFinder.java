package io.github.adrianulbona.hmm.solver;

import io.github.adrianulbona.hmm.*;

import java.util.*;

/**
 * Created by adrianbona on 1/23/16.
 */

public class ForwardProbabilityFinder<S extends State, O extends Observation> implements Solver<O, Map<S, Double>> {

	private final Model<S, O> model;
	private final ArrayList<Observer<S, O>> observers;

	public ForwardProbabilityFinder(Model<S, O> model) {
		this.model = model;
		this.observers = new ArrayList<>();
	}

	@Override
	public Map<S, Double> basedOn(List<O> observations) {
		final Map<S, Double> forwardProbabilities = new HashMap<>();
		final O firstObservation = observations.get(0);
		observers.forEach(o -> o.processingObservation(firstObservation));
		model.getReachableStatesFor(firstObservation)
				.forEach(s -> {
					final Double emissionProbability = model.probability(new Emission<>(s, firstObservation));
					final double probability = model.initialProbabilityFor(s) * emissionProbability;
					forwardProbabilities.put(s, probability);
					observers.forEach(o -> o.foundForwardProbability(s, probability));
				});


		for (O observation : observations.subList(1, observations.size())) {
			observers.forEach(o -> o.processingObservation(observation));
			final HashMap<S, Double> fps = new HashMap<>();
			model.getReachableStatesFor(observation)
					.forEach(s -> {
						final Double emissionProbability = model.probability(new Emission<>(s, observation));
						final Double aggPrevStepProbabilities = forwardProbabilities.entrySet()
								.stream()
								.map(fp -> fp.getValue() * model.probability(new Transition<>(fp.getKey(), s)))
								.reduce((p1, p2) -> p1 + p2)
								.get();
						final double probability = emissionProbability * aggPrevStepProbabilities;
						fps.put(s, probability);
						observers.forEach(o -> o.foundForwardProbability(s, probability));
					});
			forwardProbabilities.clear();
			forwardProbabilities.putAll(fps);
		}
		return forwardProbabilities;
	}

	public void addObserver(Observer<S, O> observer) {
		observers.add(observer);
	}

	public void removeObserver(Observer<S, O> observer) {
		observers.remove(observer);
	}

	public interface Observer<S extends State, O extends Observation> {
		void processingObservation(O observation);

		void foundForwardProbability(S state, Double probability);
	}
}
