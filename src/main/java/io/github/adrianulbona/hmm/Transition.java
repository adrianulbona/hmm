package io.github.adrianulbona.hmm;

import lombok.Data;

/**
 * Created by adrianbona on 1/13/16.
 */

@Data
public class Transition<S extends State> {

	private final S from;
	private final S to;
}
