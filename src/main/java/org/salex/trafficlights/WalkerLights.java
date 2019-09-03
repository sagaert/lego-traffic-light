package org.salex.trafficlights;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class WalkerLights {
    private final GpioPinDigitalOutput red;
    private final GpioPinDigitalOutput green;

    public WalkerLights(GpioController controller, Pin red, Pin green) {
        this.red = controller.provisionDigitalOutputPin(red);
        this.green = controller.provisionDigitalOutputPin(green);
    }

    public void red() {
        red.high();;
        green.low();
    }

    public void green() {
        red.low();;
        green.high();
    }
}
