package filters.kalman;


import utils.Util;

public class KalmanFilterGaussian {
    
    private KalmanFilterGaussian() {
        
    }

	/**
	 * 
	 * @param mean1
	 * @param var1
	 * @param mean2
	 * @param var2
	 * @return measurement update
	 */
	public static MuSigma update(double mean1, double var1, double mean2, double var2) {
		double newMean = (mean1 * var2 + mean2 * var1) / (var1 + var2);
		double newVar = 1 / (1 / var1 + 1 / var2);
		return new MuSigma(newMean, newVar);
	}

	/**
	 * 
	 * @param mean1
	 * @param var1
	 * @param mean2
	 * @param var2
	 * @return Motion/Prediction Update(Correction)
	 */
	public static MuSigma predict(double mean1, double var1, double mean2, double var2) {
		double newMean = mean1 + mean2;
		double newVar = var1 + var2;
		return new MuSigma(newMean, newVar);
	}

	public static void main(String[] args) {

		double[] measurements = { 5.0, 6.0, 7.0, 9.0, 10.0 };
		double measurementSig = 4.0;
		double[] motions = { 1.0, 1.0, 2.0, 1.0, 1.0 };
		double motionSig = 2.0;

		// initial parameters
		double mu = 0;
		double var = 10000.0;

		MuSigma newVal = new MuSigma(mu, var);
		for (int j = 0; j < measurements.length; j++) {
			// System.out.println("Actual "+(newVal.mu));
			newVal = update(newVal.mu, newVal.var, measurements[j], measurementSig);
			System.out.println("Update  " + newVal);
			newVal = predict(newVal.mu, newVal.var, motions[j], motionSig);
			System.out.println("Predict " + newVal);
		}

	}
}

class MuSigma {
	double mu;
	double var;

	public MuSigma(double mu, double var) {
		this.mu = mu;
		this.var = var;
	}

	@Override
	public String toString() {
		return "Mu_Sigma [mu=" + Util.df.format(mu) + ", sigma=" + Util.df.format(var) + "]";
	}

}
