package filters.particles;



import filters.particles.internal.IRobot;
import utils.RoboMathUtils;
import utils.Util;

import java.util.Random;

public class ParticleFilter {
	double senseNoise = 5.0;// For sense function.
	double steeringNoise = 0.05;// For move function.
	double forwardNoise = 0.05;// For move function

	public ParticleFilter() {
	}

	public void setNoise(double senseNoise, double steeringNoise, double forwardNoise) {
		this.senseNoise = senseNoise;
		this.steeringNoise = steeringNoise;
		this.forwardNoise = forwardNoise;
	}

	public double[] getAveragePosition(IRobot[] p) {
		double xSum = 0.0;
		double ySum = 0.0;
		double orSum = 0.0;
		for (int j = 0; j < p.length; j++) {
			xSum += p[j].getX();
			ySum += p[j].getY();
			// System.out.println(p.get(j));
			// orientation is tricky because it is cyclic. By normalizing
			// around the first particle we are somewhat more robust to
			// the 0=2pi problem
			orSum += (RoboMathUtils.modulus((p[j].getOrientation() - p[0].getOrientation() + Math.PI), (2.0 * Math.PI))
					+ p[0].getOrientation() - Math.PI);
		}
		if (p != null && p.length > 0)
			return new double[] { xSum / p.length, ySum / p.length, orSum / p.length };
		else
			return new double[] { xSum, ySum, orSum };
	}

	public double eval(IRobot myrobot, IRobot[] p) {
		double sum = 0.0;
		for (int i = 0; i < p.length; i++) {
			double norm = World.getWidth() / 2.0;
			double dx = RoboMathUtils.modulus(p[i].getX() - myrobot.getX() + norm, World.getWidth()) - norm;
			norm = World.getHeight() / 2.0;
			double dy = RoboMathUtils.modulus(p[i].getY() - myrobot.getY() + norm, World.getHeight()) - norm;
			double err = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
			sum += err;
		}
		return sum / p.length;
	}

	public double filter(IRobot myrobot, IRobot[] p, IRobot ghost, double[][] motions, int particles,
			double newParticlesRatio, double unSampledRatio) {

		// To solve the robot kidnaped problem
		// this will randomly generate new particles
		int newRatio = (int) (particles * newParticlesRatio);
		// UnSampled particles to this ratio
		int unSampledParticles = (int) (particles * unSampledRatio);
		// Actual sampling ratio
		int reSampledParticles = particles - newRatio - unSampledParticles;

		// System.out.println("Initial Position " + myrobot);
		// # --------
		// #
		// # Update particles
		// #

		for (int t = 0; t < motions.length; t++) {
			myrobot.move(motions[t]);
			double[] z = myrobot.sense(true);
			// System.out.println("measurement " + Arrays.toString(z));

			// # motion update (prediction)
			// System.out.println("Before Sampling");
			for (int i = 0; i < particles; i++) {
				// System.out.println(p[i]);
				p[i].move(motions[t]);
			}

			// measurement update
			// double sum = 0;
			double[] w = new double[reSampledParticles];
			for (int i = 0; i < reSampledParticles; i++) {
				w[i] = p[i].measurementProb(z);
				// sum += w[i];
				// System.out.println(i + " : Prob = " + w[i]);
			}
			// for (int i = 0; i < particles; i++) {
			// System.out.println(p[i] + " " + w[i]);
			// }

			if (particles > 0) {
				IRobot[] p3 = new IRobot[reSampledParticles];

				// Re-Sampling Step ///////////////
				try {
					reSample(w, p, p3);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// /////////////////////////

				for (int i = 0; i < reSampledParticles; i++) {
					p[i].update(p3[i]);
				}
				for (int i = 0; i < newRatio; i++) {
					p[i + reSampledParticles].random();
				}

				// Update Ghost
				ghost.setLocation(getAveragePosition(p));
			}

		}
		double error = eval(myrobot, p);
		// System.out.println("Error " + error);
		// System.out.println("------------------");
		return error;
	}

	public void reSample(double[] w, IRobot[] src, IRobot[] dest) {
		double beta = 0;
		double mw = Util.findMax(w);
		Random r = new Random();
		int index = r.nextInt(dest.length);
		for (int i = 0; i < dest.length; i++) {
			beta += r.nextDouble() * 2 * mw;
			while (beta > w[index]) {
				beta -= w[index];
				index = (index + 1) % dest.length;
			}
			dest[i] = src[index];
		}
	}

	// public void reSample(double[] w, IRobot[] src, IRobot[] dest) {
	// // calculate cumulative weights
	// double sum = w[0];
	// double[] cw = new double[w.length];
	// cw[0] = w[0];
	// for (int i = 1; i < cw.length; i++) {
	// DEBUG("W " + w[i]);
	// cw[i] = cw[i - 1] + w[i];
	// sum += w[i];
	// }
	// DEBUG("Sum = " + sum + " and " + cw[cw.length - 1]);
	// // Normalize weights
	// for (int i = 0; i < cw.length; i++) {
	// cw[i] = cw[i] / sum;
	// DEBUG("CW " + cw[i]);
	// }
	//
	// for (int i = 0; i < cw.length; i++) {
	// sum += cw[i];
	// }
	// DEBUG("Total of Norm weights : " + sum);
	//
	// double beta = 0;
	// double mw = Util.findMax(w);
	// Random r = new Random();
	// int index = r.nextInt(dest.length);
	// for (int i = 0; i < dest.length; i++) {
	// beta += r.nextDouble() * 2 * mw;
	// while (beta > w[index]) {
	// beta -= w[index];
	// index = (index + 1) % dest.length;
	// }
	// dest[i] = src[index];
	// }
	// }

	// private void DEBUG(String msg) {
	// if (false) {
	// System.out.println(msg);
	// }
	// }

}

// Normalizing here
// for (int i = 0; i < particles; i++) {
// w[i] = w[i] / sum;
// }
// // Calculate Cumulative Frequencies
// double[] cf = new double[particles];
// cf[0] = w[0];
// for (int i = 1; i < particles; i++) {
// cf[i] = cf[i - 1] + w[i];
// }
// Robot[] p3 = new Robot[particles];
// Random r = new Random();
// for (int i = 0; i < particles; i++) {
// double sample = r.nextDouble();
// int index = r.nextInt(particles);
// while (sample > cf[index]) {
// index = (index + 1) % particles;
// }
// p3[i] = p[index];
// }

// Curt Welch Fast Resample Code
// Robot[] p3 = new Robot[particles];
// int index = 0;
// double step = sum / particles;
// double beta = (sum * sum) % step;
// for (int j = 0; j < particles; j++) {
// while (beta > w[index]) {
// beta -= w[index];
// index = (index + 1) % particles;
// }
// beta += step;
// p3[j] = p[index];
// }
