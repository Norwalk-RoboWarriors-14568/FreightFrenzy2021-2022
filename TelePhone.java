package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name="Teleop", group="Linear Opmode")
public class TelePhone extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeft, motorLeft2,
            motorRight, motorRight2, motorXRail, motorLift, motorCollecter;
    private CRServo servoLeft, servoRight, servoMain;

    private boolean buttonG2APressedLast = false;

    public void drive(double left, double right){
        motorLeft.setPower(left);
        motorLeft2.setPower(left);
        motorRight.setPower(right);
        motorRight2.setPower(right);

    }


    private ElapsedTime timer;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        motorLeft  = hardwareMap.dcMotor.get("motor_0");
        motorLeft2 = hardwareMap.dcMotor.get( "motor_2");
        motorRight  = hardwareMap.dcMotor.get("motor_3");
        motorRight2 = hardwareMap.dcMotor.get("motor_1");
        motorXRail =hardwareMap.dcMotor.get("motor_5");
        motorLift = hardwareMap.dcMotor.get("motor_4");
        motorCollecter = hardwareMap.dcMotor.get("motor_6");
        //servoMain = hardwareMap.servo.get("servo_2");
        servoLeft = hardwareMap.crservo.get("servo_1");
        servoRight = hardwareMap.crservo.get("servo_0");
        
        
        timer = new ElapsedTime();//create a timer from the elapsed time class

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        //motorRight2.setDirection(DcMotor.Direction.REVERSE);

        motorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRight2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeft2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorXRail.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        reverseMotors();
        waitForStart();
        runtime.reset();
        timer.startTime();
       
        while (opModeIsActive()) {



            if(gamepad1.left_bumper){
                    drive(gamepad1.left_stick_y/2, gamepad1.right_stick_y/2);
                
            }else if(gamepad1.right_bumper){
                    drive(gamepad1.left_stick_y/8, gamepad1.right_stick_y/8);
                
            }else{
                drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
            }
            //Servo Control
            if(gamepad2.left_bumper){
                    servoLeft.setPower(1);
                    servoRight.setPower(1);
                
            } else if (gamepad2.right_bumper){
                    servoLeft.setPower(-1);
                    servoRight.setPower(-1);
                
            } else {
                servoLeft.setPower(0);
                servoRight.setPower(0);
            }

            if (gamepad2.right_stick_y >=  0.2|| gamepad2.right_stick_y <= -0.2){
                 telemetry.addData("Status", "In if");
                    telemetry.update();
                      telemetry.addData("Status", "Inside While");
                     telemetry.update();
                     motorXRail.setPower(gamepad2.right_stick_y); 
                
            } else {
                 motorXRail.setPower(0);
            }
            if (gamepad2.left_stick_y >=  0.1|| gamepad2.left_stick_y <= -0.1){
                 telemetry.addData("Status", "In if");
                    telemetry.update();
                      telemetry.addData("Status", "Inside While");
                     telemetry.update();
                     motorLift.setPower(-gamepad2.left_stick_y); 
                 
            } else {
                 motorLift.setPower(0);
            }
            if (gamepad2.right_trigger  >= 0.1){
                    motorCollecter.setPower(gamepad2.right_trigger);
                
            } else if (gamepad2.left_trigger  >= 0.1){
                    motorCollecter.setPower(-gamepad2.left_trigger);
                
            } else {
                motorCollecter.setPower(0);
            }
      
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("MotorsFront", "left (%.2f), right (%.2f)", motorLeft.getPower(), motorRight.getPower());
            telemetry.addData("MotorsBack ", "left (%.2f), right (%.2f)", motorLeft2.getPower(), motorRight2.getPower());
            telemetry.update();

        }
        
    }
    private void reverseMotors(){
            motorRight.setDirection(DcMotor.Direction.REVERSE);
            motorXRail.setDirection(DcMotor.Direction.REVERSE);
        
      }
}
