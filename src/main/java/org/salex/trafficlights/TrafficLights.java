package org.salex.trafficlights;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class TrafficLights {
    public static void main(String[] args) {
        final GpioController gpio = GpioFactory.getInstance();
        final CarLights carLights = new CarLights(gpio, RaspiPin.GPIO_13, RaspiPin.GPIO_12, RaspiPin.GPIO_02);
        final WalkerLights walkerLights = new WalkerLights(gpio, RaspiPin.GPIO_09, RaspiPin.GPIO_08);
        final WalkerSignal walkerSignal = new WalkerSignal(gpio, RaspiPin.GPIO_15, RaspiPin.GPIO_16);
        carLights.green();
        walkerLights.red();
        walkerSignal.addListener(new WalkerSignalListener() {
            public void signal() {
                try {
                    carLights.prepareRed();
                    Thread.sleep(2000);
                    carLights.red();
                    Thread.sleep(2000);
                    walkerLights.green();
                    walkerSignal.stopWaiting();
                    Thread.sleep(5000);
                    walkerLights.red();
                    Thread.sleep(2000);
                    carLights.prepareGreen();
                    Thread.sleep(2000);
                    carLights.green();
                    walkerSignal.unlock();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        while(true) {}
    }
}
