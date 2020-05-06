package elements;

import primitives.Color;

public class AmbientLight {
	private Color  _intensity;
	/**
	 * @return the value of the ambient lighting type of color.
	 */	
	public Color getIntensity() 
	{
		return _intensity;
	}
	/**
	 * ctr
	 * @param ia filling Light Intensity
	 * @param ka coefficient of attenuation of filler light
	 */
	public AmbientLight(Color ia, double ka)
	{
		_intensity=ia.scale(ka);
	}

}
