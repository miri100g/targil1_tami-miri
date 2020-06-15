package primitives;
/**
 *class that have to every geomtry which represent the rank  of the brightness of the geometry
 *
 */
public class Material {
	/**
	 * distance factor
	 */
	private double _kD;
	/**
	 * distance factor
	 */
	private double _kS;
	/**
	 * transparency factor
	 */
	private double _kT;
	/**
	 * reflection factor
	 */
	private double _kR;
	/**
	 *the rank  of the brightness of the material 
	 */
	private int _nShininess;
	
	/**
	 * Material constructor
	 * @param kd _kD value
	 * @param ks _kS value
	 * @param shininess _nShininess value
	 * @param kt _kT value
	 * @param kr _kR value
	 */
	public Material(double kd, double ks, int shininess,double kt, double kr) {
		_kD=kd;
		_kS=ks;
		_kT=kt;
		_kR=kr;
		_nShininess=shininess;
	}
	
	/**
	 *Material constructor with default _kR _kT values
	 * @param kd _kD value
	 * @param ks _kS value
	 * @param shininess _nShininess value
	 */
	public Material(double kd, double ks, int shininess) {
		this(kd,ks,shininess,0,0);
	}
	/** 
	 * @return_kD value
	 */
	public double getKd() {
		return +_kD;
	}
	/**
	 * @return _kS value
	 */
	public double getKs() {
		return _kS;
	}
	/**
	 * @return _kT value
	 */
	public double getKt() {
		return _kT;
	}
	/**
	 * @return _kR value
	 */
	public double getKr() {
		return _kR;
	}
	/**
	 * @return _nShininess value
	 */
	public int getShininess() {
		return _nShininess;
	}

}
