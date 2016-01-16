package io.github.adrianulbona.hmm.probability;

import io.github.adrianulbona.hmm.State;


/**
 * Created by adrian.bona on 13/01/16.
 */
public interface StartProbabilityCalculator<S extends State> {

	Double probability(S state);
}
