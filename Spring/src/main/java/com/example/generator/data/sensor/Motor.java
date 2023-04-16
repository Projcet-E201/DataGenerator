package com.example.generator.data.sensor;

import com.example.generator.util.SensorType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Motor {

    private final long id;
    private final int generateDataInterval;
    private final Random rand;
    private final Channel channel;

    public Motor(long id, int generateDataInterval, Channel channel) {
        this.id = id;
        this.generateDataInterval = generateDataInterval;
        this.rand = new Random();
        this.channel = channel;
    }

    public void start() {
        ScheduledExecutorService dataGenerator = Executors.newSingleThreadScheduledExecutor();
        dataGenerator.scheduleAtFixedRate(this::motorDataGenerator, 0, generateDataInterval, TimeUnit.SECONDS);
    }

    private void motorDataGenerator() {
        double mean = 0;
        double stdDev = 0.1;

        int data = (int) Math.round(rand.nextGaussian() * stdDev * 600 + mean);
        data = Math.max(Math.min(data, 300), -300);

        // Motor 데이터를 ByteBuf로 변환
        String sensorDataString = SensorType.MOTOR + "," + this.id + "," + data;
        ByteBuf message = Unpooled.copiedBuffer(sensorDataString, CharsetUtil.UTF_8);
        channel.writeAndFlush(message);
    }

}
