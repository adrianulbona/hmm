package io.github.adrianulbona.hmm.probability;

import io.github.adrianulbona.hmm.State;
import lombok.RequiredArgsConstructor;
import io.github.adrianulbona.hmm.Emission;
import io.github.adrianulbona.hmm.Observation;
import io.github.adrianulbona.hmm.Transition;


/**
 * Created by adrian.bona on 13/01/16.
 */

@RequiredArgsConstructor
public class ProbabilityCalculatorImpl<S extends State, O extends Observation>
		implements ProbabilityCalculator<S, O> {

	private final StartProbabilityCalculator<S> startProbabilityCalculator;
	private final EmissionProbabilityCalculator<S, O> emissionProbabilityCalculator;
	private final TransitionProbabilityCalculator<S> transitionProbabilityCalculator;


	@Override
	public Double probability(S state) {
		final Double probability = startProbabilityCalculator.probability(state);
		return probability == null ? 0.0 : probability;
	}

	@Override
	public Double probability(Emission<S, O> emission) {
		final Double probability = emissionProbabilityCalculator.probability(emission);
		return probability == null ? 0.0 : probability;
	}

	@Override
	public Double probability(Transition<S> transition) {
		final Double probability = transitionProbabilityCalculator.probability(transition);
		return probability == null ? 0.0 : probability;
	}
}
