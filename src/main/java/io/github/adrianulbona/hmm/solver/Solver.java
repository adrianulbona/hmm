package io.github.adrianulbona.hmm.solver;

import io.github.adrianulbona.hmm.Observation;

import java.util.List;

/**
 * Created by adrianbona on 1/14/16.
 */
public interface Solver<O extends Observation, T> {
	T basedOn(List<O> observations);
}
