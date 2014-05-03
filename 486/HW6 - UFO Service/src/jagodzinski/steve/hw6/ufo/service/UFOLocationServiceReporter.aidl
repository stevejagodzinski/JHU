package jagodzinski.steve.hw6.ufo.service;

import jagodzinski.steve.hw6.ufo.model.UFOPosition;
import java.util.List;

interface UFOLocationServiceReporter {
	void report(in List<UFOPosition> ufoPositions);
}