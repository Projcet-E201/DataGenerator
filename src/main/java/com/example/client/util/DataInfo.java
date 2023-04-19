package com.example.client.util;

import java.util.concurrent.TimeUnit;

public class DataInfo {

	// Motor 정보
	public static final int MOTOR_COUNT = 10;
	public static final int MOTOR_GENERATE_TIME = 5;
	public static final int MOTOR_CALCULATE_TIME = 5;
	public static final TimeUnit MOTOR_GENERATE_TIME_UNIT = TimeUnit.MILLISECONDS;
	public static final TimeUnit MOTOR_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// Vacuum 정보
	public static final int VACUUM_COUNT = 30;
	public static final int VACUUM_GENERATE_TIME = 5;
	public static final int VACUUM_CALCULATE_TIME = 10;
	public static final TimeUnit VACUUM_GENERATE_TIME_UNIT = TimeUnit.MILLISECONDS;
	public static final TimeUnit VACUUM_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// AirOutKpa 정보
	public static final int AIR_OUT_KPA_COUNT = 5;
	public static final int AIR_OUT_KPA_GENERATE_TIME = 5;
	public static final int AIR_OUT_KPA_CALCULATE_TIME = 30;
	public static final TimeUnit AIR_OUT_KPA_GENERATE_TIME_UNIT = TimeUnit.MILLISECONDS;
	public static final TimeUnit AIR_OUT_KPA_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// AirOutMpa 정보
	public static final int AIR_OUT_MPA_COUNT = 5;
	public static final int AIR_OUT_MPA_GENERATE_TIME = 1;
	public static final int AIR_OUT_MPA_CALCULATE_TIME = 10;
	public static final TimeUnit AIR_OUT_MPA_GENERATE_TIME_UNIT = TimeUnit.SECONDS;
	public static final TimeUnit AIR_OUT_MPA_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// AirInKpa 정보
	public static final int AIR_IN_KPA_COUNT = 10;
	public static final int AIR_IN_KPA_GENERATE_TIME = 1;
	public static final int AIR_IN_KPA_CALCULATE_TIME = 1;
	public static final TimeUnit AIR_IN_KPA_GENERATE_TIME_UNIT = TimeUnit.SECONDS;
	public static final TimeUnit AIR_IN_KPA_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// Water 정보
	public static final int WATER_COUNT = 10;
	public static final int WATER_GENERATE_TIME = 1;
	public static final int WATER_CALCULATE_TIME = 10;
	public static final TimeUnit WATER_GENERATE_TIME_UNIT = TimeUnit.SECONDS;
	public static final TimeUnit WATER_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;

	// Abrasio (마모량) 정보
	public static final int ABRASION_COUNT = 5;
	public static final int ABRASION_GENERATE_TIME = 1;
	public static final TimeUnit ABRASION_GENERATE_TIME_UNIT = TimeUnit.MINUTES;
	public static final int ABRASION_CALCULATE_TIME = 48;
	public static final TimeUnit ABRASION_CALCULATE_TIME_UNIT = TimeUnit.HOURS;

	// 부하량 정보
	public static final int LOAD_COUNT = 5;
	public static final int LOAD_GENERATE_TIME = 1;
	public static final int LOAD_CALCULATE_TIME = 1;
	public static final TimeUnit LOAD_GENERATE_TIME_UNIT = TimeUnit.MINUTES;
	public static final TimeUnit LOAD_CALCULATE_TIME_UNIT = TimeUnit.MINUTES;

	// Velocity (회전속도) 정보
	public static final int VELOCITY_COUNT = 5;
	public static final int VELOCITY_GENERATE_TIME = 1;
	public static final int VELOCITY_CALCULATE_TIME = 1;
	public static final TimeUnit VELOCITY_GENERATE_TIME_UNIT = TimeUnit.MINUTES;
	public static final TimeUnit VELOCITY_CALCULATE_TIME_UNIT = TimeUnit.MINUTES;

	// MachineState 정보
	public static final int MACHINE_STATE_COUNT = 1;
	public static final int MACHINE_STATE_GENERATE_TIME = 1;
	public static final int MACHINE_STATE_CALCULATE_TIME = 1;
	public static final TimeUnit MACHINE_STATE_GENERATE_TIME_UNIT = TimeUnit.SECONDS;
	public static final TimeUnit MACHINE_STATE_CALCULATE_TIME_UNIT = TimeUnit.SECONDS;


	public static int getDataCountByType(DataType dataType) {
		switch (dataType) {
			case MACHINE_STATE:
				return MACHINE_STATE_COUNT;
			case MOTOR:
				return MOTOR_COUNT;
			case VACUUM:
				return VACUUM_COUNT;
			case AIR_OUT_KPA:
				return AIR_OUT_KPA_COUNT;
			case AIR_OUT_MPA:
				return AIR_OUT_MPA_COUNT;
			case AIR_IN_KPA:
				return AIR_IN_KPA_COUNT;
			case WATER:
				return WATER_COUNT;
			case ABRASION:
				return ABRASION_COUNT;
			case LOAD:
				return LOAD_COUNT;
			case VELOCITY:
				return VELOCITY_COUNT;

			default:
				throw new IllegalArgumentException("Unknown data type: " + dataType);
		}
	}
}
