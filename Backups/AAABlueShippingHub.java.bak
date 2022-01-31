package org.firstinspires.ftc.teamcode.OfficalGitHub;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


@Autonomous(name = "BlueHUB🟥🔪🟩")

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
    private double CPI_ATV_DT, CPI_OMNI_DT;
    private int timeOutCount = 0;
    // private VoltageSensor vs;
    private double gameTimeSnapShot = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //CPI =     ticksPerRev / (circumerence);
        CPI_ATV_DT = 537.7/ ( 4.75 * Math.PI);
        CPI_OMNI_DT = 537.7/ (3.75 * Math.PI);
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

        brakeMotors();
        reverseMotors();
        waitForStart();
        runtime.reset();
        //run autonomous
        if (opModeIsActive()) {
            motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);
            toHub();
            sleep(2000);
            extendOrRetract(2, 0.5, false);
            spitOut(-0.4);
            extendOrRetract(1, -0.5, false);

            turnToWH();
            sleep(2000);
            backIntoWH();
            sleep(2000);
            rotateRight90();
            sleep(2000);
            telemetry.update();

        }
    }

    public void encoderDrive(double leftDTSpeed, double rightDTSpeed, double mtrLeftInches, double mtrRightInches) {
        int newLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPI_ATV_DT * mtrLeftInches);
        int newRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPI_ATV_DT * mtrRightInches);
        drive(mtrLeftInches < 0 ? -leftDTSpeed : leftDTSpeed, mtrRightInches < 0 ? -rightDTSpeed : rightDTSpeed);
        while (opModeIsActive() && !IsInRange(motorLeftBACK.getCurrentPosition(), newLeftTarget)
                && !IsInRange(motorRightBACK.getCurrentPosition(), newRightTarget)) {
            telemetry.addData("Target Left: ", newLeftTarget);
            telemetry.addData("Target right: ", newRightTarget);
            telemetry.addData("Current Pos Left:", motorLeftBACK.getCurrentPosition());
            telemetry.addData("Current Pos Right:", motorRightBACK.getCurrentPosition());
            telemetry.addData("right power: ", motorRightBACK.getPower());
            telemetry.addData("left power: ", motorLeftBACK.getPower());
            telemetry.update();
        }
        // Stop all motion;
        drive(0, 0);
    }

    
    private void toHub(){
        encoderDrive( 0.8, 0.32, 30, 16 );//Arc
    }
    private void turnToWH(){
        encoderDrive( 0.1, 0.7, -8, -12 );//Turn to Warehouse
    }
    private void rotateRight90() {
        encoderDrive( 1, 1, 13.5, -10);//Back straight
    }
    private void backIntoWH() {
        encoderDrive( 1, 1, -58, -58);//Back straight
    }
    public void motorSetModes(DcMotor.RunMode modeName) {
        motorLeftBACK.setMode(modeName);
        motorRightBACK.setMode(modeName);
        motorLeftFRONT.setMode(modeName);
        motorRightFRONT.setMode(modeName);
    }
    public void drive(double left, double right  ) {
            motorLeftBACK.setPower(left);
            motorRightBACK.setPower(right);
            motorRightFRONT.setPower(right);
            motorLeftFRONT.setPower(left);
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
    public void extendOrRetract(double seconds,double power,  boolean in){
        if (!in) {
            motorXRail.setPower(power);//30 %
            sleep((long) (seconds * 1000));
            motorXRail.setPower(0);
        }else if (in) {
            motorXRail.setPower(-power);//30 %
            sleep((long) seconds * -1000);
            motorXRail.setPower(0);
        }
    }

    private void reverseMotors(){
        motorLeftBACK.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFRONT.setDirection(DcMotor.Direction.REVERSE);
        motorXRail.setDirection(DcMotor.Direction.REVERSE);
        
    }
    private void brakeMotors(){
        motorLeftBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorXRail.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
    }

}
