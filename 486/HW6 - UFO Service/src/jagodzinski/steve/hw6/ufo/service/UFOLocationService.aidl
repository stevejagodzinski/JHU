package jagodzinski.steve.hw6.ufo.service;
import jagodzinski.steve.hw6.ufo.service.UFOLocationServiceReporter;

interface UFOLocationService {
	void add(UFOLocationServiceReporter reporter);
	void remove(UFOLocationServiceReporter reporter);
}
