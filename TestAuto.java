package org.firstinspires.ftc.teamcode.OfficalGitHub;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


@Autonomous(name = "Blue Shipping Hub")
public class AAABlueShippingHub extends LinearOpMode {
    // Declare OpMode members.

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeftBACK = null;
    private DcMotor motorRightBACK = null;
    private DcMotor motorLeftFRONT = null;
    private DcMotor motorRightFRONT = null;
    private DcMotor motorLift = null;
    private DcMotor motorXRail = null;
    private DcMotor motorCollector;
    private CRServo servoLeft, servoRight;

    //private boolean buttonG2APressest = false;
    //private boolean buttonG2XPressedLast = false;
    private ElapsedTime timer;
    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double CPI_DRIVE_TRAIN;
    private int timeOutCount = 0;
    // private VoltageSensor vs;
    private double gameTimeSnapShot = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //CPI =     ticksPerRev / (circumerence);
        CPI_DRIVE_TRAIN = 537.7/ ( 4.75 * Math.PI);
        
        motorLeftBACK = hardwareMap.dcMotor.get("motor_0");
        motorRightBACK = hardwareMap.dcMotor.get("motor_1");
        motorLeftFRONT = hardwareMap.dcMotor.get( "motor_2");
        motorRightFRONT = hardwareMap.dcMotor.get("motor_3");
        motorLift = hardwareMap.dcMotor.get("motor_4");
        motorXRail =hardwareMap.dcMotor.get("motor_5");
        motorCollector = hardwareMap.dcMotor.get("motor_6");
        servoRight = hardwareMap.crservo.get("servo_0");
        servoLeft = hardwareMap.crservo.get("servo_1");
        //servoMain = hardwareMap.servo.get("servo_2");

        timer = new ElapsedTime();//create a timer from the elapsed time class

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        motorLeftBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reverseMotors();
        waitForStart();
        runtime.reset();
        //run autonomous
        if (opModeIsActive()) {
            motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);
            odometryDrive(5, 0.8, 0.2, 34, 17, false);//Arc
            sleep(2000);
            extendOrRetract(-2);//extend or retract - is extend and + is retract// based off time do not put over 4
            spitOut(-1);

            odometryDrive(2.6, -0.1, -0.7, -8, -8, false);//Back straight
            sleep(2000);
            odometryDrive(4, -1, -1, -80, -84, false);//Back straight
            sleep(2000);
            odometryDrive(2.6, -1, 1, -20, 27, false);//Back straight
            sleep(2000);
            //odometryDrive(2, -0.37,0.37,10, 10,false);//Turn
            telemetry.update();
        }
    }

    public void odometryDrive(double timeOut, double leftDTSpeed, double rightDTSpeed, double mtrLeftInches, double mtrRightInches, boolean strafe) {
        int newLeftTarget = motorLeftFRONT.getCurrentPosition() + (int) (CPI_DRIVE_TRAIN * mtrLeftInches);
        int newRightTarget = motorRightFRONT.getCurrentPosition() + (int) (CPI_DRIVE_TRAIN * mtrRightInches);
        double thisTimeOut = this.time + timeOut;

        drive(leftDTSpeed, rightDTSpeed, strafe);
        while (opModeIsActive() && !IsInRange(motorLeftBACK.getCurrentPosition(),newLeftTarget)
                && !IsInRange(motorRightBACK.getCurrentPosition(),newRightTarget)) {
            if (this.time >= thisTimeOut) {
                break;
            }

            //telemetry.addData("Right motor: ",motorRight.getCurrentPosition() );
            telemetry.addData("Target Left: ", newLeftTarget);
            telemetry.addData("Target right: ", newRightTarget);
            telemetry.addData("Current Pos Left:", motorLeftBACK.getCurrentPosition());
            telemetry.addData("Current Pos Right:", motorRightBACK.getCurrentPosition());
            telemetry.addData("right power: ", motorRightBACK.getPower());
            telemetry.addData("left power: ", motorLeftBACK.getPower());
            telemetry.update();
        }

        // Stop all motion;
        drive(0, 0, strafe);


    }

    public void drive(double left, double right, boolean strafe) {
        //telemetry.addData("Left/right power: ", left, right);
        telemetry.update();
        if(!strafe){
            motorLeftBACK.setPower(left);
            motorRightBACK.setPower(right);
            // motorLeft2.setPower(left);
            // motorRight2.setPower(right);
        }else{
            motorLeftBACK.setPower(-left);
            motorRightBACK.setPower(right);
            //  motorLeft2.setPower(left);
            // motorRight2.setPower(right);
        }
    }

    public void motorSetModes(DcMotor.RunMode modeName) {
        motorLeftBACK.setMode(modeName);
        motorRightBACK.setMode(modeName);
    }

    public void motorSetTargetPos(int targetLeft, int targetRight) {
        motorLeftBACK.setTargetPosition(targetLeft);
        motorRightBACK.setTargetPosition(targetRight);
    }

    public boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 20;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }public void spitOut(double power){
        motorCollector.setPower(power);
        sleep(1500);
        motorCollector.setPower(0);

    }
    public void spinDuck(double power){//for red Carousel the value needs to be negative
        servoLeft.setPower(power);
        sleep(5500);
        servoLeft.setPower(0);

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

    private void reverseMotors(){
        motorRightBACK.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFRONT.setDirection(DcMotor.Direction.REVERSE);
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


