
package org.firstinspires.ftc.teamcode.OfficalGitHub.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import static java.lang.Math.abs;

/**
 * All the code aside from importing, goes within a class file - essentially telling Android Studio-
 * to run this using java
 */

@TeleOp(name = "TeleOp_12783-4") //This is the name of the program that is going to be on the phone.


public class AndrewJacobClarke extends OpMode {


//*********** Experimental          Mecanums only
//*********** Rev Hub count: 2


    //TETRIX Motors		-recording the motor type as if this file ran autonomous

    private DcMotor motorLeft, motorLeft2,
            motorRight, motorRight2, motorSuck;


    private boolean mecanunDriveMode = true, coastMotors = true;
    private float mecanumStrafe = 0, dominantXJoystick = 0;


    /*
     * Code to run when the op mode is first enabled goes here
     * This is all automatic- it prepares the robot to run loop() without error
     */
    @Override
    public void init() {


//rev hub 1
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft2 = hardwareMap.dcMotor.get("motor_3");
        motorRight2 = hardwareMap.dcMotor.get("motor_4");
        motorSuck = hardwareMap.dcMotor.get("motor_5");

        //so you don't have to wire red to black, to maintain program logic
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorLeft2.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addLine("Drive Base TeleOp\nInit Opmode");
    }


    /**
     * The next 2 lines ends the current state of Opmode, and starts Opmode.loop()
     * This enables control of the robot via the gamepad or starts autonomous functions
     */
    @Override
    public void loop() {

        telemetry.addLine("Loop OpMode\ndPadLeft: disable mecanum strafe is " + !mecanunDriveMode +
                "\ndPadRight: enable mecanum strafe is " + mecanunDriveMode +
                "\nX: brake motors is " + !coastMotors + "\nY: coast motors is " + coastMotors);

        telemetry.addData("LeftMTR  PWR: ", motorLeft.getPower());
        telemetry.addData("RightMTR PWR: ", motorRight.getPower());
//gamepad1		  -specifying the section of code giving control to gamepad1-this reduces confusion
        //else if's are required, so that a motor doesn't receive the power of multiple lines


        //allows for 3-speed joystick control


        if (abs(gamepad1.left_stick_x) > 0.25 || abs(gamepad1.right_stick_x) > 0.25) {
            //removes negatives from joystick values, to set variable to +/- for determing stick farther from zero
            dominantXJoystick = (abs(gamepad1.left_stick_x) - abs(gamepad1.right_stick_x));
            mecanunDriveMode = true;
        } else {
            mecanunDriveMode = false;
        }

        if (mecanunDriveMode) {     //when enabled, motors will only hit 100% when strafing and driving

            if (dominantXJoystick > 0) {
                mecanumStrafe = gamepad1.left_stick_x;
            } else if (dominantXJoystick < 0) {
                mecanumStrafe = gamepad1.right_stick_x;
            }

            if (gamepad1.left_bumper) {
                if ((gamepad1.left_stick_y < 0.3 && gamepad1.left_stick_y > -0.3) && (gamepad1.right_stick_y < 0.3 && gamepad1.right_stick_y > -0.3)) {
                    motorLeft.setPower(-mecanumStrafe);
                    motorLeft2.setPower(mecanumStrafe);
                    motorRight.setPower(mecanumStrafe);
                    motorRight2.setPower(-mecanumStrafe);
                } else {
                    motorLeft.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 2);
                    motorLeft2.setPower((gamepad1.left_stick_y + mecanumStrafe) / 2);
                    motorRight.setPower((gamepad1.right_stick_y + mecanumStrafe) / 2);
                    motorRight2.setPower((gamepad1.right_stick_y + -mecanumStrafe) / 2);
                }


            } else if (gamepad1.right_bumper) {
                motorLeft.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 2 * 0.5);
                motorLeft2.setPower((gamepad1.left_stick_y + mecanumStrafe) / 2 * 0.5);
                motorRight.setPower((gamepad1.right_stick_y + mecanumStrafe) / 2 * 0.5);
                motorRight2.setPower((gamepad1.right_stick_y + -mecanumStrafe) / 2 * 0.5);
            } else {
                motorLeft.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 2 * 0.75);
                motorLeft2.setPower((gamepad1.left_stick_y + mecanumStrafe) / 2 * 0.75);
                motorRight.setPower((gamepad1.right_stick_y + mecanumStrafe) / 2 * 0.75);
                motorRight2.setPower((gamepad1.right_stick_y + -mecanumStrafe) / 2 * 0.75);
            }
        } else if (!mecanunDriveMode) {
            if (gamepad1.left_bumper) {
                drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
            } else if (gamepad1.right_bumper) {
                drive(gamepad1.left_stick_y * 0.5, gamepad1.right_stick_y * 0.5);
            } else {
                drive(gamepad1.left_stick_y * 1, gamepad1.right_stick_y * 1);
            }
        }

        //button code to manipulate other code/robot
        if (gamepad1.dpad_up) {
            motorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorLeft2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorRight2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            coastMotors = false;
        } else if (gamepad1.dpad_down) {
            motorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorLeft2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            motorRight2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            coastMotors = true;
        } else if (gamepad1.dpad_left) {
            mecanunDriveMode = false;
        } else if (gamepad1.dpad_right) {
            mecanunDriveMode = true;
        }

        if (gamepad2.right_trigger > 0) {
        motorSuck.setPower(1);
        }else {
            motorSuck.setPower(0);
        }
//end of loop opmode programing
    }


    @Override
    public void stop() {
        telemetry.clearAll();
        telemetry.addLine("Stopped");
    }

    public void drive(double left, double right) {
        motorLeft.setPower(left);
        motorLeft2.setPower(left);
        motorRight.setPower(right);
        motorRight2.setPower(right);

    }

}