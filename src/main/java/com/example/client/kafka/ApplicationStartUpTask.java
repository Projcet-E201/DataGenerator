package com.example.client.kafka;

import com.example.client.data.analog.AnalogManager;
import com.example.client.data.global.AbstractDataManager;
import com.example.client.data.image.ImageManager;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartUpTask implements ApplicationListener<ApplicationReadyEvent> {

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
    private final AnalogManager analogManager;
    private final ImageManager imageManager;
    protected List<AbstractDataManager<?>> managers = new ArrayList<>();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
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
                machineState,
                analogManager,
                imageManager
        );

        managers.forEach(AbstractDataManager::sendData);
    }
}
