package com.example.client.netty.data;

import java.net.InetSocketAddress;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.client.data.machinestate.MachineStateManager;
import com.example.client.data.sensor.abrasion.AbrasioManager;
import com.example.client.data.sensor.air.AirInKpaManager;
import com.example.client.data.sensor.air.AirOutKpaManager;
import com.example.client.data.sensor.air.AirOutMpaManager;
import com.example.client.data.sensor.load.LoadManager;
import com.example.client.data.sensor.motor.MotorManager;
import com.example.client.data.sensor.vacumm.VacuumManager;
import com.example.client.data.sensor.velocity.VelocityManager;
import com.example.client.data.sensor.water.WaterManager;
import com.example.client.netty.global.socket.AbstractNettySocket;

import io.netty.bootstrap.Bootstrap;

@Component
public class DataNettySocket extends AbstractNettySocket {
	private final MotorManager motorManager;
	private final VacuumManager vacuumManager;
	private final AirOutKpaManager airOutKpaManager;
	private final AirOutMpaManager airOutMpaManager;
	private final AirInKpaManager airInKpaManager;
	private final WaterManager waterManager;
	private final AbrasioManager abrasioManager;
	private final LoadManager loadManager;
	private final VelocityManager velocityManager;
	private final MachineStateManager machineState;

	public DataNettySocket(
		@Qualifier("dataServerBootstrap") Bootstrap bootstrap,
		@Qualifier("dataInetSocketAddress") InetSocketAddress tcpPort,
		MotorManager motorManager,
		VacuumManager vacuumManager,
		AirOutKpaManager airOutKpaManager,
		AirOutMpaManager airOutMpaManager,
		AirInKpaManager airInKpaManager,
		WaterManager waterManager,
		AbrasioManager abrasioManager,
		LoadManager loadManager,
		VelocityManager velocityManager,
		MachineStateManager machineState) {

		super(bootstrap, tcpPort);
		this.motorManager = motorManager;
		this.vacuumManager = vacuumManager;
		this.airOutKpaManager = airOutKpaManager;
		this.airOutMpaManager = airOutMpaManager;
		this.airInKpaManager = airInKpaManager;
		this.waterManager = waterManager;
		this.abrasioManager = abrasioManager;
		this.loadManager = loadManager;
		this.velocityManager = velocityManager;
		this.machineState = machineState;
	}

	@PostConstruct
	private void init() {
		managers = Arrays.asList(
			motorManager,
			vacuumManager,
			airOutKpaManager,
			airOutMpaManager,
			airInKpaManager,
			waterManager,
			abrasioManager,
			loadManager,
			velocityManager,
			machineState
		);
	}
}
