package org.salex.trafficlights;

import com.pi4j.io.gpio.*;

public class CarLights {
    private final GpioPinDigitalOutput red;
    private final GpioPinDigitalOutput yellow;
    private final GpioPinDigitalOutput green;

    public CarLights(GpioController controller, Pin red, Pin yellow, Pin green) {
        this.red = controller.provisionDigitalOutputPin(red);
        this.yellow = controller.provisionDigitalOutputPin(yellow);
        this.green = controller.provisionDigitalOutputPin(green);
    }

    public void green() {
        red.low();;
        yellow.low();
        green.high();
    }

    public void prepareGreen() {
        red.high();;
        yellow.high();
        green.low();
    }

    public void red() {
        red.high();;
        yellow.low();
        green.low();
    }

    public void prepareRed() {
        red.low();;
        yellow.high();
        green.low();
    }
}
