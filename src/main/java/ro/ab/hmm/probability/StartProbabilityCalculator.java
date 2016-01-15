package ro.ab.hmm.probability;

import ro.ab.hmm.Emission;
import ro.ab.hmm.Observation;
import ro.ab.hmm.State;


/**
 * Created by adrian.bona on 13/01/16.
 */
public interface StartProbabilityCalculator<S extends State> {

	Double probability(S state);
}
