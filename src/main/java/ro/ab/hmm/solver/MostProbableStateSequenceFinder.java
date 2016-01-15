package ro.ab.hmm.solver;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ro.ab.hmm.*;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

/**
 * Created by adrianbona on 1/14/16.
 */

@RequiredArgsConstructor
public class MostProbableStateSequenceFinder<S extends State, O extends Observation> implements Solver<List<S>> {

	private final Model<S, O> hmm;

	@Override
	public List<S> solve() {
		final List<OptimalTransition<S>> probabilityLattice = buildCompleteLattice();
		final OptimalTransition<S> lastTransitionForOptimum = probabilityLattice.stream()
				.max((ot1, ot2) -> Double.compare(ot1.probability, ot2.probability))
				.get();
		return optimalStateSequence(lastTransitionForOptimum);
	}

	private List<S> optimalStateSequence(OptimalTransition<S> lastTransitionForOptimum) {
		if (lastTransitionForOptimum.isStart()) {
			return new ArrayList<>();
		}
		final List<S> sequence = optimalStateSequence(lastTransitionForOptimum.getSource());
		sequence.add(0, lastTransitionForOptimum.state);
		return sequence;
	}

	private List<OptimalTransition<S>> buildCompleteLattice() {
		final List<O> observations = hmm.getObservations();
		final List<ReducibleObservation<O>> reducibleObservations = range(0, observations.size() - 1)
				.mapToObj(i -> new ReducibleObservation<>(observations.get(i), observations.get(i + 1)))
				.collect(toList());
		return reducibleObservations.stream()
				.reduce(optimalTransitions(observations.get(0)),
						this::reduceObservation,
						(irrelevant, currentOptimum) -> currentOptimum);
	}

	private List<OptimalTransition<S>> optimalTransitions(O observation) {
		final Map<Emission<S, O>, Double> emissionProbabilities = hmm.emissionProbabilitiesFor(observation);
		return hmm.getReachableStatesFor(observation)
				.stream()
				.map(state -> {
					final Emission<S, O> emission = new Emission<>(state, observation);
					final Double probability = emissionProbabilities.get(emission) * hmm.initialProbabilityFor(state);
					return new OptimalTransition<>(OptimalTransition.start(), state, probability);
				})
				.collect(toList());
	}

	private List<OptimalTransition<S>> reduceObservation(List<OptimalTransition<S>> current,
			ReducibleObservation<O> reducibleObservation) {
		final O observation = reducibleObservation.observation;
		final O previousObservation = reducibleObservation.previousObservation;
		final Map<Emission<S, O>, Double> emissions = hmm.emissionProbabilitiesFor(observation);
		final Map<Transition<S>, Double> transitions = hmm.transitionProbabilitiesFor(previousObservation,
				observation);
		final List<S> reachableStates = emissions.keySet()
				.stream()
				.map(Emission::getState)
				.collect(toList());
		return reachableStates.stream()
				.map(s -> findOptimalTransitionForState(s, observation, emissions, transitions, current))
				.collect(toList());
	}

	private OptimalTransition<S> findOptimalTransitionForState(S state, O observation,
			Map<Emission<S, O>, Double> emissions, Map<Transition<S>, Double> transitions,
			List<OptimalTransition<S>> optimalTransitions) {
		return optimalTransitions.stream()
				.map(ot -> {
					final Double previousStateOptimum = ot.getProbability();
					final Double transitionProbability = transitions.get(new Transition<>(ot.getState(), state));
					final Double emissionProbability = emissions.get(new Emission<>(ot.getState(), observation));
					return new OptimalTransition<>(ot, state,
							previousStateOptimum * transitionProbability * emissionProbability
					);
				})
				.max((ps1, ps2) -> ps1.getProbability()
						.compareTo(ps2.getProbability()))
				.get();
	}

	@Data
	private static class OptimalTransition<S extends State> {
		private final OptimalTransition<S> source;
		private final S state;
		private final Double probability;

		public boolean isStart() {
			return false;
		}

		public static <S extends State> OptimalTransition<S> start() {
			return new OptimalTransition<S>(null, null, 0.0) {
				@Override
				public boolean isStart() {
					return true;
				}
			};
		}

	}

	@Data
	public static class ReducibleObservation<O extends Observation> {
		private final O previousObservation;
		private final O observation;
	}
}
