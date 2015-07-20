package com.akibot.tanktrack.component.gyroscope;

/**
 * Constants from:
 * http://www51.honeywell.com/aero/common/documents/myaerospacecatalog-documents/Defense_Brochures-documents/HMC5883L_3-Axis_Digital_Compass_IC.pdf
 */
public class GyroscopeConstantsHMC5883L {

	// HMC5883L Register Addresses:
	public static final int CONFIGURATION_REGISTER_A = 0;
	public static final int CONFIGURATION_REGISTER_B = 1;
	public static final int MODE_REGISTER = 2;
	public static final int DATA_OUTPUT_MSB_REGISTER_X = 3;
	public static final int DATA_OUTPUT_MSB_REGISTER_Z = 5;
	public static final int DATA_OUTPUT_MSB_REGISTER_Y = 7;

	// HMC5883L: Configuration Register A:
	public static final byte CRA_8SAMPLE_15MHZ_NORMAL = 0b01110000; // Set to 8 samples @ 15Hz
	public static final byte CRA_8SAMPLE_15MHZ_TEST_POSITIVE = 0b01110001; // Set to 8 samples @ 15Hz, Positive bias configuration
	public static final byte CRA_8SAMPLE_15MHZ_TEST_NEGATIVE = 0b01110010; // Set to 8 samples @ 15Hz, Positive bias configuration

	// HMC5883L Configuration Register B: Gain Configuration Bits
	public static final byte GAIN_0_88 = (byte) 0b00000000;
	public static final byte GAIN_1_3_DEFAULT = (byte) 0b00100000;
	public static final byte GAIN_1_9 = (byte) 0b01000000;
	public static final byte GAIN_2_5 = (byte) 0b01100000;
	public static final byte GAIN_4_0 = (byte) 0b10000000;
	public static final byte GAIN_4_7 = (byte) 0b10100000;
	public static final byte GAIN_5_6 = (byte) 0b11000000;
	public static final byte GAIN_8_1 = (byte) 0b11100000;

	// HMC5883L Operating Mode
	public static final byte CONTINUOUS_MEASUREMENT_MODE = 0b00;
	public static final byte SINGLE_MEASUREMENT_MODE = 0b01;
	public static final byte IDLE_MODE = 0b11;

	public static final long SLEEP_MINI = 7;
	public static final long SLEEP_MAXI = 80;

}
