package org.firstinspires.ftc.teamcode.OfficalGitHub;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class DynamicVoltage {
    private VoltageSensor vs;
    private HardwareMap hardwareMap;


    public void runOpMode() {
    vs  = this.hardwareMap.voltageSensor.iterator().next();
}
    public double setVoltage(double speed){
        double voltage = vs.getVoltage();
        double voltageScale = 0.03;
        double result = 1 + ((14 - voltage) * voltageScale);
        //.setPower(speed * result);
        return speed * result ;
    }

    double getBatteryVoltage() {
        double result = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                result = Math.min(result, voltage);
            }
        }
        return result;
    }
}

