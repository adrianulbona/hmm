package io.github.adrianulbona.hmm.probability;

import io.github.adrianulbona.hmm.Observation;
import io.github.adrianulbona.hmm.State;

/**
 * Created by adrianbona on 1/23/16.
 */
public interface ProbabilityCalculator<S extends State, O extends Observation> extends
		StartProbabilityCalculator<S>, EmissionProbabilityCalculator<S, O>,
		TransitionProbabilityCalculator<S> {
}
