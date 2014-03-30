package jagodzinski.steve.hw4.fishing.service;


public class LineAngleService {

	private static final LineAngleService INSTANCE = new LineAngleService();

	private LineAngleService() {
	}

	public static LineAngleService getInstance() {
		return INSTANCE;
	}

	public double calculateLineAngle(float fishingRodTipX, float fishingRodTipY, float fishHookX, float fishHookY) {
		return arctan(calculateOppositeLeg(fishingRodTipX, fishHookX) / calculateAdjacentLeg(fishingRodTipY, fishHookY));
	}

	private float calculateOppositeLeg(float fishingRodTipX, float fishHookX) {
		return fishHookX - fishingRodTipX;
	}

	private float calculateAdjacentLeg(float fishingRodTipY, float fishHookY) {
		return fishHookY - fishingRodTipY;
	}

	private double arctan(float argument) {
		return Math.toDegrees(Math.atan(argument));
	}
}
