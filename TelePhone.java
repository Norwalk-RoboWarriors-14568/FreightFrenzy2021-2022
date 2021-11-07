package org.firstinspires.ftc.teamcode.OfficalGitHub;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Teleop", group="Linear Opmode")
public class TelePhone extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeftBack, motorLeftFront,
            motorRightBack, motorRightFront, motorXRail, motorLift, motorCollecter;
    private CRServo servoLeft, servoRight, servoMain;

    //private DcMotor ExtraMotor = null;
    //private DcMotor ExtraMotor2 = null;
    //private Servo ExtraServo = null;//Servo to push rings into Durflinger
    //private Servo ExtraServo = null;
    //static float SERVO_INITIAL_POS = 0.7f;
    //static float GRABBER_INITIAL_POS = 0.0f;//wobble graber servo initial pos
    private boolean buttonG2APressedLast = false;

    public void drive(double left, double right){
        motorLeftBack.setPower(left);
        motorLeftFront.setPower(left);
        motorRightBack.setPower(right);
        motorRightFront.setPower(right);

    }


    private ElapsedTime timer;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        motorLeftBack = hardwareMap.dcMotor.get("motor_0");
        motorLeftFront = hardwareMap.dcMotor.get( "motor_2");
        motorRightBack = hardwareMap.dcMotor.get("motor_3");
        motorRightFront = hardwareMap.dcMotor.get("motor_1");
        motorXRail =hardwareMap.dcMotor.get("motor_5");
        motorLift = hardwareMap.dcMotor.get("motor_4");
        motorCollecter = hardwareMap.dcMotor.get("motor_6");
        //servoMain = hardwareMap.servo.get("servo_2");
        servoLeft = hardwareMap.crservo.get("servo_1");
        servoRight = hardwareMap.crservo.get("servo_0");

        // ExtraMotor = hardwareMap.get(DcMotor.class, "motor_4");
        /// ExtraMotor2 = hardwareMap.get(DcMotor.class, "motor_5");
        // ExtraServo = hardwreMap.get(Servo.class, "servo_0");
        // ExtraServo2 = hardwareMap.get(Servo.class, "servo_1" );
        timer = new ElapsedTime();//create a timer from the elapsed time class

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        reverseMotors();
        //motorRight2.setDirection(DcMotor.Direction.REVERSE);

        motorRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //ExtraMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //ExtraMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //ExtraServo.setDirection(Servo.Direction.FORWARD);//set the servo direction
        //ExtraServo2.setDirection(Servo.Direction.FORWARD);//set the servo direction



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        timer.startTime();
        //ExtraServo.setPosition(SERVO_INITIAL_POS);
        //ExtraServo2.setPosition(GRABBER_INITIAL_POS);
        //intakeFrontMotor.setPosition(INTAKE_INITIAL_POS);

        //double startKickTime = 9999;//the time the button was pressed to move the servo (set high so it doesn't hit)
        // run until the end of the match (driver presses STOP)
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

            if (gamepad2.left_stick_y >=  0.2|| gamepad2.left_stick_y <= -0.2){
                telemetry.addData("Status", "In if");
                telemetry.update();
                telemetry.addData("Status", "Inside While");
                telemetry.update();
                motorXRail.setPower(gamepad2.left_stick_y);

            } else {
                motorXRail.setPower(0);
            }
            if (gamepad2.right_stick_y >=  0.1|| gamepad2.right_stick_y <= -0.1){
                telemetry.addData("Status", "In if");
                telemetry.update();
                telemetry.addData("Status", "Inside While");
                telemetry.update();
                motorLift.setPower(-gamepad2.right_stick_y);

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



            /*
            if(gamepad2.y){//if b button pressed
                startKickTime = timer.time();//start the timer so it knows when to turn around
            }
            if(timer.time() - startKickTime > 0.3){//if its been moving for > 0.3 seconds
                kickerServo.setPosition(SERVO_INITIAL_POS);//reset back to zero
                startKickTime = 99999;//set kickTime back to value that doesn't trigger this
            }

            if(gamepad2.x && !buttonG2XPressedLast){
                if(Math.abs(wobbleServo.getPosition() - GRABBER_INITIAL_POS) < 0.01){
                    wobbleServo.setPosition(0.52);
                }
                else if(Math.abs(wobbleServo.getPosition() - 0.52) < 0.01){
                    wobbleServo.setPosition(GRABBER_INITIAL_POS);
                }
            }
            buttonG2XPressedLast = gamepad2.x;

            if(gamepad2.a && !buttonG2APressedLast){
                if (motorIntakeBelt.getPower() != 0) {
                    motorIntakeBelt.setPower(0);
                    intakeFrontMotor.setPower(0);
                }else{
                    motorIntakeBelt.setPower(1);
                    intakeFrontMotor.setPower(1);
                }
            }
            buttonG2APressedLast = gamepad2.a;

            if(gamepad2.dpad_right){//if holding right on dpad, reverse it
                motorIntakeBelt.setPower(-1);
                intakeFrontMotor.setPower(-1);
            }
            buttonG2APressedLast = gamepad2.a;

            if(gamepad2.dpad_up){
                wobbleMotor.setPower(-0.6);
            }
            else if(gamepad2.dpad_down){
                wobbleMotor.setPower(0.6);
            }
            else{
                wobbleMotor.setPower(0);
            }
            //Thrower code to set different speeds
            if(gamepad2.left_bumper){
                throwerPower = 0f;
            }
            else if(gamepad2.left_trigger > 0.2f){
                throwerPower = 0.65;
            }
            else if(gamepad2.right_bumper){
                throwerPower = 0.7f;
            }
            else if(gamepad2.right_trigger > 0.2f){
                throwerPower = 0.8f;
            }
            throwerMotor.setPower(throwerPower);

*/






            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("MotorsFront", "left (%.2f), right (%.2f)", motorLeftBack.getPower(), motorRightBack.getPower());
            telemetry.addData("MotorsBack ", "left (%.2f), right (%.2f)", motorLeftFront.getPower(), motorRightFront.getPower());
            telemetry.update();

        }

    }
    private void reverseMotors(){
        motorRightBack.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
    }
}
