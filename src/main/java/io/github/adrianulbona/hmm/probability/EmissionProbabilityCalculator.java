package io.github.adrianulbona.hmm.probability;

import io.github.adrianulbona.hmm.Emission;
import io.github.adrianulbona.hmm.Observation;
import io.github.adrianulbona.hmm.State;


/**
 * Created by adrian.bona on 13/01/16.
 */
public interface EmissionProbabilityCalculator<S extends State, O extends Observation> {

	Double probability(Emission<S, O> emission);
}
