package jagodzinski.steve.hw4.fishing.service;

public class LineDepthService {
	private static final LineDepthService INSTANCE = new LineDepthService();

	private LineDepthService() {
	}

	public static LineDepthService getInstance() {
		return INSTANCE;
	}

	public double calculateLineDepth(double lineLength, double angle) {
		return lineLength * Math.cos(Math.toRadians(angle));
	}
}
