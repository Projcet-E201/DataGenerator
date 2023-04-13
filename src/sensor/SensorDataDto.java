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

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
