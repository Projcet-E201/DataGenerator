package sensor;

import config.Sensor;

public class SensorDataDto {

	private Sensor sensor;
	private Long id;
	private Integer value;

	public SensorDataDto(Sensor sensor,Long id, Integer value) {
		this.sensor = sensor;
		this.id = id;
		this.value = value;
	}

	public Sensor getSensorDataType() {
		return sensor;
	}

	public void setSensorDataType(Sensor sensorDataDto) {
		this.sensor = sensorDataDto;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
