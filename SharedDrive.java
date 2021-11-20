package org.firstinspires.ftc.teamcode.OfficalGitHub;

import com.qualcomm.robotcore.hardware.DcMotor;
//edit
public class SharedDrive {
    private TelemetryDisplay _telemetryDisplay;
    private Motors _motors;
    private double _CPI_ATV_DT;
    private DynamicVoltage _vs;
    public SharedDrive(TelemetryDisplay telemetryDisplay, Motors motors, double CPI_ATV_DT, DynamicVoltage vs) {
        _telemetryDisplay = telemetryDisplay;
        _motors = motors;
        _CPI_ATV_DT = CPI_ATV_DT;
        _vs = vs;
    }

    public void odometryDrive( double leftDTSpeed, double rightDTSpeed, double mtrLeftInches, double mtrRightInches) {
        int newLeftTarget = _motors.backLeft.getCurrentPosition() + (int) (_CPI_ATV_DT * mtrLeftInches);
        int newRightTarget = _motors.backRight.getCurrentPosition() + (int) (_CPI_ATV_DT * mtrRightInches);
        drive(mtrLeftInches < 0 ? -leftDTSpeed : leftDTSpeed, mtrRightInches < 0 ? -rightDTSpeed : rightDTSpeed);
        while (!IsInRange(_motors.backLeft.getCurrentPosition(), newLeftTarget) //opModeIsActive() &&  How to call?
                && !IsInRange(_motors.backRight.getCurrentPosition(), newRightTarget)) {
            _telemetryDisplay.addData("Target Left: ", newLeftTarget);
            _telemetryDisplay.addData("Target right: ", newRightTarget);
            _telemetryDisplay.addData("Current Pos Left:", _motors.backLeft.getCurrentPosition());
            _telemetryDisplay.addData("Current Pos Right:", _motors.backRight.getCurrentPosition());
            _telemetryDisplay.addData("right power: ", _motors.backRight.getPower());
            _telemetryDisplay.addData("left power: ", _motors.backLeft.getPower());
            _telemetryDisplay.update();
        }
        // Stop all motion;
        drive(0, 0);
    }

    private boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 20;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }
//THkso ea test
//Test

    private void drive(double left, double right  ) {
        _motors.backLeft.setPower(_vs.setVoltage(left));
        _motors.backRight.setPower(_vs.setVoltage(right));
        _motors.frontRight.setPower(_vs.setVoltage(right));
        _motors.frontRight.setPower(_vs.setVoltage(left));
    }

}
