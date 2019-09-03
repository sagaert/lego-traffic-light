package org.salex.trafficlights;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.util.ArrayList;
import java.util.List;

public class WalkerSignal {
    private final GpioPinDigitalOutput light;
    private final GpioPinDigitalInput button;
    private final List<WalkerSignalListener> listeners = new ArrayList<WalkerSignalListener>();

    private boolean locked = false;

    private Thread waitingThread = null;

    public WalkerSignal(GpioController controller, Pin light, Pin button) {
        this.light = controller.provisionDigitalOutputPin(light);
        this.light.low();
        this.button =  controller.provisionDigitalInputPin(button);
        this.button.addListener(new GpioPinListenerDigital() {
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if(!WalkerSignal.this.locked && event.getState().isHigh()) {
                    WalkerSignal.this.locked = true;
                    WalkerSignal.this.buttonPushed();
                }
            }
        });
    }

    private void buttonPushed() {
        this.waitingThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        WalkerSignal.this.light.low();
                        Thread.sleep(500);
                        WalkerSignal.this.light.high();
                        Thread.sleep(500);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.waitingThread.start();
        try {
            Thread.sleep(8000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        for(WalkerSignalListener listener : listeners) {
            listener.signal();
        }
    }

    public void unlock() {
        this.locked = false;
    }

    public void stopWaiting() {
        this.waitingThread.stop();
        this.waitingThread = null;
        this.light.low();
    }

    public void addListener(WalkerSignalListener listener) {
        this.listeners.add(listener);
    }
}
