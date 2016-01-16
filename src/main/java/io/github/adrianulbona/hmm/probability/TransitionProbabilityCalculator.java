package io.github.adrianulbona.hmm.probability;

import io.github.adrianulbona.hmm.State;
import io.github.adrianulbona.hmm.Transition;


/**
 * Created by adrian.bona on 13/01/16.
 */
public interface TransitionProbabilityCalculator<S extends State> {

	Double probability(Transition<S> transition);
}
