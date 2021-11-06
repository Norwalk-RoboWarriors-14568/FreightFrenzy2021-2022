package org.firstinspires.ftc.teamcode.OfficalGitHub;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.WebcamConfiguration;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TfodCurrentGame;
import static java.lang.Math.abs;


@Autonomous(name = "ZDONOTUSEHIGHLYEXPERIMENTAL")
public class TestAuto extends LinearOpMode {
    // Declare OpMode members.

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeft, motorRight, motorLeft2, motorRight2,
            motorLift, motorXRail,motorCollecter;

    private CRServo servoLeft, servoRight;
    // private Servo servomain;

    //private boolean buttonG2APressest = false;
    //private boolean buttonG2XPressedLast = false;
    private ElapsedTime timer;


    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double CPI_DRIVE_TRAIN;
    private double cpiOdometry;
    private double leftPos = 0;
    private double rightPos = 0;
    private int timeOutCount = 0;
    // private VoltageSensor vs;
    private double gameTimeSnapShot = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        cpiOdometry  = CPR_ODOMETRY / (ODOMETRY_WHEEL_DIAMETER * Math.PI);
        //CPI =     ticksPerRev / (circumerence);
        CPI_DRIVE_TRAIN = 537.7/ ( 4.75 * Math.PI);

        motorLeft  = hardwareMap.dcMotor.get("motor_0");
        motorLeft2 = hardwareMap.dcMotor.get( "motor_2");
        motorRight  = hardwareMap.dcMotor.get("motor_1");
        motorRight2 = hardwareMap.dcMotor.get("motor_3");
        motorXRail =hardwareMap.dcMotor.get("motor_5");
        motorLift = hardwareMap.dcMotor.get("motor_4");
        motorCollecter = hardwareMap.dcMotor.get("motor_6");
        //servoMain = hardwareMap.servo.get("servo_2");
        servoLeft = hardwareMap.crservo.get("servo_1");
        servoRight = hardwareMap.crservo.get("servo_0");
        // vs  = this.hardwareMap.voltageSensor.iterator().next();
        timer = new ElapsedTime();//create a timer from the elapsed time class


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorXRail.setDirection(DcMotor.Direction.REVERSE);

        motorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        // Wait for the game to start (driver presses PLAY)

        //telemetry.addLine("");
        sleep(550);
        telemetry.update();
        waitForStart();
        runtime.reset();


        //run autonomous
        if (opModeIsActive()) {
            motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  //stop and reset encoders
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);       //start encoders
            encoderDrive(10, 0.8, 0.8, 10,10);


            telemetry.update();
        }

    }//LEX IS VERY COOL 
    public void encoderDrive(double timeOut, double leftSpeed, double rightSpeed, double mtrLeftInches, double mtrRightInches) {
        int newLeftTarget, newRightTarget;
        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //stop and reset encoders
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER); //start encoders

            // Determine new target position, and pass to motor controller
            newLeftTarget = motorLeft.getCurrentPosition() + (int)(CPI_DRIVE_TRAIN * mtrLeftInches);
            newRightTarget = motorRight.getCurrentPosition() + (int)(CPI_DRIVE_TRAIN * mtrRightInches);
            //  newWGTarget = motorwg.getCurrentPosition() + (int)(wgmotor * 3892);

            motorSetTargetPos(newLeftTarget, newRightTarget);


            // Turn On RUN_TO_POSITION
            motorSetModes(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.


            //motorLeft.setPower(-1 * Math.abs(speed));
            //motorLeft2.setPower(Math.abs(speed));
            //motorRight.setPower(Math.abs(speed));
            //motorRight2.setPower(-1 * Math.abs(speed));

            double thisTimeOut = this.time + timeOut;

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() && (motorLeft.isBusy() || motorRight.isBusy())) {

                if (this.time >= thisTimeOut) {
                    /*
                    relativeLayout.post(new Runnable() {
                        public void run() {
                            relativeLayout.setBackgroundColor(Color.YELLOW);
                        }
                    });
                    */
                    //timeOutCount++;
                    //telemetry.addData("Num of cuts", timeOutCount);
                    break;
                }

                // Display data for the driver.
                telemetry.addData("GTime", (int)(this.time - gameTimeSnapShot));
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        motorLeft.getCurrentPosition(),
                        motorRight.getCurrentPosition());
                telemetry.addData("L1-Tar", motorLeft.getTargetPosition() + "L1-Cur" + motorLeft.getCurrentPosition());
                telemetry.addData("L2-Tar", motorLeft2.getTargetPosition() + "L2-Cur" + motorLeft2.getCurrentPosition());
                telemetry.addData("R1-Tar", motorRight.getTargetPosition() + "R1-Cur" + motorRight.getCurrentPosition());
                telemetry.addData("R2-Tar", motorRight2.getTargetPosition() + "R2-Cur" + motorRight2.getCurrentPosition());
                telemetry.addData("Lmtr-PWR:" + motorLeft.getPower(), " Rmtr-PWR:" + motorRight.getPower());

                telemetry.update();
            }

            // Stop all motion;
            drive(0, 0);

            // Turn off RUN_TO_POSITION
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);
            //motorSkystoneGrab.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(250); // optional pause after each move in milliseconds
        }

    }

    public void drive(double left, double right) {
        //telemetry.addData("Left/right power: ", left, right);
        telemetry.update();

        motorLeft.setPower(left);
        motorLeft2.setPower(left);
        motorRight.setPower(right);
        motorRight2.setPower(right);

    }

    public void motorSetModes(DcMotor.RunMode modeName) {
        motorLeft.setMode(modeName);
        motorRight.setMode(modeName);
    }

    public void motorSetTargetPos(int targetLeft, int targetRight) {
        motorLeft.setTargetPosition(targetLeft);
        motorRight.setTargetPosition(targetRight);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 20;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }
    public void spitOut(double power){
        motorCollecter.setPower(power);
        sleep(1500);
        motorCollecter.setPower(0);

    }
    public void spinDuck(double power){//for red Carousel the value needs to be negative
        servoRight.setPower(power);
        sleep(5500);
        servoRight.setPower(0);

    }
    public void extendOrRetract(int power){
        if (power > 0) {
            motorXRail.setPower(0.5);//30 %
            sleep(power * 1000);
            motorXRail.setPower(0);
        }else if (power < 0){
            motorXRail.setPower(-0.5);//30 %
            sleep(power * -1000);
            motorXRail.setPower(0);


        } else {
            power = 0;
        }



    }

    private void displayInfo(double i, Recognition recognition) {
        // Display label info.
        // Display the label and index number for the recognition.
        telemetry.addData("label " + i, recognition.getLabel());
        telemetry.addData("width: ", recognition.getWidth() );
        telemetry.addData("height: ", recognition.getHeight() );
        telemetry.addData("H/W Ratio: ", recognition.getHeight()/recognition.getWidth() );
    }
}


