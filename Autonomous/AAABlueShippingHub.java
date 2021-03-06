package org.firstinspires.ftc.teamcode.OfficalGitHub;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


@Autonomous(name = "BLUEHUB")

public class AAABlueShippingHub extends LinearOpMode {
    // Declare OpMode members.
    int hubLevel = 1;
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeftBACK = null;
    private DcMotor motorRightBACK = null;
    private DcMotor motorLeftFRONT = null;
    private DcMotor motorRightFRONT = null;
    private DcMotor motorLift = null;
    private DcMotor motorXRail = null;
    private DcMotor motorCollector;
    private CRServo servoLeft, servoRight;
    private DistanceSensor distance;

    //private boolean buttonG2APressest = false;
    //private boolean buttonG2XPressedLast = false;
    private ElapsedTime timer;
    private final int hexCoreCPR = 288;

    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double CPI_ATV_DT, CPI_OMNI_DT, CPI_CORE_HEX, CPI_GOBILDA26TO1;
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
        CPI_GOBILDA26TO1 = 180.81*2.6/2.5;//gear ratio adjustment
        CPI_CORE_HEX = hexCoreCPR/4.4;

        motorLeftBACK = hardwareMap.dcMotor.get("motor_0");
        motorRightBACK = hardwareMap.dcMotor.get("motor_1");
        motorLeftFRONT = hardwareMap.dcMotor.get( "motor_2");
        motorRightFRONT = hardwareMap.dcMotor.get("motor_3");
        motorLift = hardwareMap.dcMotor.get("motor_4");
        motorXRail =hardwareMap.dcMotor.get("motor_5");
        motorCollector = hardwareMap.dcMotor.get("motor_6");
        servoRight = hardwareMap.crservo.get("servo_0");
        servoLeft = hardwareMap.crservo.get("servo_1");
        distance = hardwareMap.get(DistanceSensor.class, "distance");

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
            encoderDrive(0.3, 0.3, 13,13);
            if (distance.getDistance(DistanceUnit.INCH) < 10){
                hubLevel = 2;

            }
            encoderDrive(0.1, 0.5, 2.5, -4.5);
            sleep(500);

            if (distance.getDistance(DistanceUnit.INCH) < 10 && hubLevel != 2){
                hubLevel = 3;

            }
            encoderDrive(0.5, 0, 5.50,3);
            sleep(500);

            telemetry.addData("Distance (IN)", distance.getDistance(DistanceUnit.INCH));
            telemetry.addData("HL", hubLevel);

            telemetry.update();
            sleep(200);
            sleep(200);
            encoderDrive(0.6, 0.5, 3, 2.5);

            hubLevel(hubLevel);
            encoderDrive(0.5, 0.5, 2,2);
            sleep(200);
            encoderDrive(0.3,0.3, 4.5,-4.5);
            sleep(200);

            encoderDrive(0.9,0.9, -45, -45);
            drive(0,0);
                /*
             while(opModeIsActive()) {
                telemetry.addData("Distance (IN)", distance.getDistance(DistanceUnit.INCH));


                telemetry.update();
             }*/
                /*
            sleep(2000);
            backIntoWH();
            sleep(2000);
            rotateLeft90();
            sleep(2000);
            telemetry.update();
            */
        }
    }
    public void armHeight(double armSpeed, double armInches) {

        int newArmTarget = motorLift.getCurrentPosition() + (int) (CPI_GOBILDA26TO1 * armInches);
        motorLift.setPower(armSpeed);
        while (opModeIsActive() && !IsInRange(motorLift.getCurrentPosition(), newArmTarget)){
            telemetry.addData("Target Left: ", newArmTarget);
            telemetry.addData("Current Pos Right:", motorLift.getCurrentPosition());
            telemetry.addData("left power: ", motorLift.getPower());
            telemetry.update();
        }
        motorLift.setPower(0);

    }

    public void armDrive(double armSpeed, double armInches){
        int newArmTarget = motorXRail.getCurrentPosition() + (int) (CPI_CORE_HEX * armInches);
        motorXRail.setPower(armSpeed);
        while (opModeIsActive() && !IsInRange(motorXRail.getCurrentPosition(), newArmTarget)){
            telemetry.addData("Target Left: ", newArmTarget);
            telemetry.addData("Current Pos Right:", motorXRail.getCurrentPosition());
            telemetry.addData("left power: ", motorXRail.getPower());
            telemetry.update();
        }
        motorXRail.setPower(0);
    }
    public void encoderDrive(double leftDTSpeed, double rightDTSpeed, double mtrLeftInches, double mtrRightInches) {
        int newLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPI_ATV_DT * mtrLeftInches);
        int newRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPI_ATV_DT * mtrRightInches);
        drive(mtrLeftInches < 0 ? -leftDTSpeed : leftDTSpeed, mtrRightInches < 0 ? -rightDTSpeed : rightDTSpeed);
        while (opModeIsActive() && !IsInRange(motorLeftBACK.getCurrentPosition(), newLeftTarget)
                && !IsInRange(motorRightBACK.getCurrentPosition(), newRightTarget)) {
            telemetry.addData("Hub Level", hubLevel);
            telemetry.addData("Hub Level", hubLevel);
            telemetry.addData("Hub Level", hubLevel);
            telemetry.addData("Distance (IN)", distance.getDistance(DistanceUnit.INCH));

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
    public void elevateArm(double seconds, double power){

        motorLift.setPower(power);
        sleep((long) (seconds * 1000));
        motorLift.setPower(0);


    }

    public void hubLevel(int level){
        double armHeightSpeed = 0;
        double armHeightInches = 0;
        double armXSpeed = 0;
        double armXInches = 0;
        double spitSpeed = -0.4;

        switch (level) {
            case 1: //Lower
                armHeightSpeed = -0.5;
                armHeightInches = -6;
                armXSpeed = 0.5;
                armXInches = 6;
                break;
            case 2: //Middle
                armHeightSpeed = -0.5;
                armHeightInches = -3;
                armXSpeed = 0.5;
                armXInches = 8.9;
                break;
            case 3: //Top
                armHeightSpeed = 0;
                armHeightInches = 0;
                armXSpeed = 1;
                armXInches = 16;
                break;
            default:
                break;

        }
        telemetry.addData("xinches ", armXInches);


        armHeight(armHeightSpeed, armHeightInches);
        armDrive(armXSpeed, armXInches);
        spitOut(spitSpeed);
        armDrive(-armXSpeed, -armXInches);
        armHeight(-armHeightSpeed, -armHeightInches);
    }
    /*
    public void hubLevel(int level){
        if (level == 1){

            elevateArm(0.9, -0.8);
            extendOrRetract(1.4, 0.8);//layer 2 = 1 seconds, lsyer 1 is 0 secondselevateArm(1, -0.6);
            spitOut(-0.2);//100 is middle, 10 is bottom

            extendOrRetract(1.2, -1);
            elevateArm(0.9, 0.5);

        } else if (level == 2){
            extendOrRetract(1.5, 1);//layer 2 = 1 seconds, lsyer 1 is 0 seconds
            elevateArm(1, -0.6);
            sleep(2000);
            spitOut(-.3);
            elevateArm(1, 0.6);
            extendOrRetract(1, -1);
        } else {
            extendOrRetract(2, 1);//layer 2 = 1 seconds, lsyer 1 is 0 seconds
            spitOut(-.4);//100 is middle, 10 is bottom
            extendOrRetract(2.5, -0.5);
        }
    }
    */
    public void drive(double left, double right  ) {
        motorLeftBACK.setPower(left);
        motorRightBACK.setPower(right);
        motorRightFRONT.setPower(right);
        motorLeftFRONT.setPower(left);
    }
    private void toHub(){
        encoderDrive( 0.33, 0.6, 19, 35 );//Arc
    }
    private void turnToWH(){
        encoderDrive( 0.7, 0.06, -15, -8 );//Turn to Warehouse
    }
    private void rotateLeft90() {
        encoderDrive( 0.5, 0.5, -10,9);//Back straight
    }
    private void backIntoWH() {
        encoderDrive( 1, 0.95, -48, -48);//Back straight
    }
    public void motorSetModes(DcMotor.RunMode modeName) {
        motorLeftBACK.setMode(modeName);
        motorRightBACK.setMode(modeName);
        motorLeftFRONT.setMode(modeName);
        motorRightFRONT.setMode(modeName);
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
    public void extendOrRetract(double seconds,double power){

        motorXRail.setPower(power);//30 %
        sleep((long) (seconds * 1000));
        motorXRail.setPower(0);

    }

    private void reverseMotors(){
        motorLeftBACK.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFRONT.setDirection(DcMotor.Direction.REVERSE);

    }
    private void brakeMotors(){
        motorLeftBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorXRail.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

}
