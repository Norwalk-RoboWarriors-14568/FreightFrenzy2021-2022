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
    private DcMotor motorLeftFront = null;
    private DcMotor motorRightFront = null;
    private DcMotor motorLeftBack = null;
    private DcMotor motorRightBack = null;
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


        motorLeftFront = hardwareMap.dcMotor.get("motor_0");
        motorLeftBack = hardwareMap.dcMotor.get( "motor_2");
        motorRightFront = hardwareMap.dcMotor.get("motor_1");
        motorRightBack = hardwareMap.dcMotor.get("motor_3");
        motorXRail =hardwareMap.dcMotor.get("motor_5");
        motorLift = hardwareMap.dcMotor.get("motor_4");
        motorCollector = hardwareMap.dcMotor.get("motor_6");
        //servoMain = hardwareMap.servo.get("servo_2");
        servoLeft = hardwareMap.crservo.get("servo_1");
        servoRight = hardwareMap.crservo.get("servo_0");

        // vs  = this.hardwareMap.voltageSensor.iterator().next();
        timer = new ElapsedTime();//create a timer from the elapsed time class


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        motorRightFront.setDirection(DcMotor.Direction.REVERSE);

        motorLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        // Wait for the game to start (driver presses PLAY)
        telemetry.addLine("Ducks are pretty great");
        sleep(550);
        telemetry.update();
        waitForStart();
        runtime.reset();


        //run autonomous
        if (opModeIsActive()) {
            motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  //stop and reset encoders
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);       //start encoders
            //if(stackSize == 0 || stackSize == -1){
            // ZERO RING
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


/*
 odometryDrive(10, 0.35,0.2,5,3,false);//Arc
                sleep(1000);
                odometryDrive(3, -0.0,-0.4,-9, -10.5,false);//Arc back
                sleep(1000);
                odometryDrive(2.6, -0.58,-0.58,-10, -10,false);//Back straight
                sleep(1000);
                odometryDrive(2, -0.37,0.37,10, -10,false);//Turn
    public void revDurflinger(float speed){
        double voltage = vs.getVoltage();
        double voltageScale = 0.075;
        double result = 1 + ((14 - voltage) * voltageScale);
    }
*/
    /*
    public void rightTapeToShoot(){
        kickerServo.setPosition(SERVO_INITIAL_POS);
        odometryDrive(1,0.5,0.5,3,3,false);
        odometryDrive(3, -0.4, -0.4, -11, -11, true);//arc to the right
        sleep(300);
        revDurflinger(0.72f);
        odometryDrive(5,0.5,0.5,45,45,false);
        sleep(400);
        odometryDrive(3, 0.4, 0.4, 19, 19, true);//arc to the right
        odometryDrive(5,0.3,-0.3,3,-3,false);
        odometryDrive(3, 0.4, 0.4, 3, 3, false);
        shootRings();
    }
*/ /*
    public void startIntake(){
        motorInBelt.setPower(1);
        inSpinnerMotor.setPower(1);
    }
*//*
    public void stopIntake(){
        motorInBelt.setPower(0);
        inSpinnerMotor.setPower(0);
    }
    public void shootRings(){
        sleep(1500);
        fireRing();
        sleep(500);
        fireRing();
        sleep(500);
        fireRing();
        revDurflinger(0f);
    }

    public void moveArm(double power, double time){
        wobbleMotor.setPower(power);
        double startTime = timer.time();
        while(timer.time() - startTime < time){}
        wobbleMotor.setPower(0);
    }

    public void fireRing(){
        double startTime = timer.time();
        kickerServo.setPosition(1);
        while(timer.time() - startTime < 0.5){

        }
        kickerServo.setPosition(SERVO_INITIAL_POS);
        startTime = timer.time();
        while(timer.time() - startTime < 0.5){

        }
    }

    public void dropGoal(boolean bringBackUp){
        double startTime = timer.time();
        wobbleMotor.setPower(0.8);
        while(timer.time() - startTime < 1){//move arm down

        }
        wobbleMotor.setPower(0);
        sleep(400);
        wobbleServo.setPosition(WOBBLE_INITIAL_POS);//release thingo
        startTime = timer.time();
        while(timer.time() - startTime < 0.6){
        }
        odometryDrive(1, 0.4,0.4,5,5, false);//drive back
        if(bringBackUp){
            wobbleMotor.setPower(-0.8);
            startTime = timer.time();
            while(timer.time() - startTime < 0.5){
            }
            wobbleMotor.setPower(0);
        }
    } */



        // Stop all motion;



        // Turn off RUN_TO_POSITION
        motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);
        //motorSkystoneGrab.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sleep(250);   // optional pause after each move in milliseconds
    }


    public void odometryDrive(double timeOut, double leftDTSpeed, double rightDTSpeed, double mtrLeftInches, double mtrRightInches, boolean strafe) {
        motorSetModes(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);
        int newLeftTarget = motorLeftFront.getCurrentPosition() + (int) (CPI_DRIVE_TRAIN * mtrLeftInches);
        int newRightTarget = motorRightFront.getCurrentPosition() + (int) (CPI_DRIVE_TRAIN * mtrRightInches);
        double thisTimeOut = this.time + timeOut;



        drive(leftDTSpeed, rightDTSpeed, strafe);
        while (opModeIsActive() && !IsInRange(motorLeftFront.getCurrentPosition(),newLeftTarget)
                && !IsInRange(motorRightFront.getCurrentPosition(),newRightTarget)) {
            if (this.time >= thisTimeOut) {
                break;
            }

            //telemetry.addData("Right motor: ",motorRight.getCurrentPosition() );
            telemetry.addData("Target Left: ", newLeftTarget);
            telemetry.addData("Target right: ", newRightTarget);
            telemetry.addData("Current Pos Left:", motorLeftFront.getCurrentPosition());
            telemetry.addData("Current Pos Right:", motorRightFront.getCurrentPosition());
            telemetry.addData("right power: ", motorRightFront.getPower());
            telemetry.addData("left power: ", motorLeftFront.getPower());
            telemetry.update();
        }

        // Stop all motion;
        drive(0, 0, strafe);


    }

    public void drive(double left, double right, boolean strafe) {
        //telemetry.addData("Left/right power: ", left, right);
        telemetry.update();
        if(!strafe){
            motorLeftFront.setPower(left);
            motorRightFront.setPower(right);
            // motorLeft2.setPower(left);
            // motorRight2.setPower(right);
        }else{
            motorLeftFront.setPower(-left);
            motorRightFront.setPower(right);
            //  motorLeft2.setPower(left);
            // motorRight2.setPower(right);
        }
    }

    public void motorSetModes(DcMotor.RunMode modeName) {
        motorLeftFront.setMode(modeName);
        motorRightFront.setMode(modeName);
    }

    public void motorSetTargetPos(int targetLeft, int targetRight) {
        motorLeftFront.setTargetPosition(targetLeft);
        motorRightFront.setTargetPosition(targetRight);
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

    private void displayInfo(double i, Recognition recognition) {
        // Display label info.
        // Display the label and index number for the recognition.
        telemetry.addData("label " + i, recognition.getLabel());
        telemetry.addData("width: ", recognition.getWidth() );
        telemetry.addData("height: ", recognition.getHeight() );
        telemetry.addData("H/W Ratio: ", recognition.getHeight()/recognition.getWidth() );
    }
}


