import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
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
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import Autonomous.OpenCvRed;

@Autonomous(name = "Blue Car")
public class AAABlueCarousel extends LinearOpMode {
    // Declare OpMode members.
    //Tages

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeftBACK = null;
    private DcMotor motorRightBACK = null;
    private DcMotor motorLeftFRONT = null;
    private DcMotor motorRightFRONT = null;
    private DcMotor motorLift = null;
    private DcMotor motorXRail = null;
    private DcMotor motorCollector;
    private DcMotor motorDuck;
    private DistanceSensor distance;
    int hubLevel = 1;
    //OpenCVBLUE CVBlue;
    //private CRServo servoLeft, servoRight;
    private double CPI_ATV_DT, CPI_OMNI_DT;
    // private Servo servomain;
    //private boolean buttonG2APressest = false;
    //private boolean buttonG2XPressedLast = false;
    private ElapsedTime timer;

    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int hexCoreCPR = 288;
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double CPI_DRIVE_TRAIN, CPI_CORE_HEX, CPI_GOBILDA26TO1;
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
        CPI_CORE_HEX = hexCoreCPR/4.4;
        CPI_ATV_DT = 537.7/ ( 4.75 * Math.PI);
        CPI_OMNI_DT = 537.7/ (3.75 * Math.PI);
        CPI_GOBILDA26TO1 = 180.81*2.6;
        motorLeftBACK = hardwareMap.dcMotor.get("motor_0");
        motorRightBACK = hardwareMap.dcMotor.get("motor_1");
        motorLeftFRONT = hardwareMap.dcMotor.get( "motor_2");
        motorRightFRONT = hardwareMap.dcMotor.get("motor_3");
        motorLift = hardwareMap.dcMotor.get("motor_4");
        motorXRail =hardwareMap.dcMotor.get("motor_5");
        motorCollector = hardwareMap.dcMotor.get("motor_6");
        motorDuck = hardwareMap.dcMotor.get("motor_7");
        distance = hardwareMap.get(DistanceSensor.class, "distance");
        //servoRight = hardwareMap.crservo.get("servo_0");
        //servoLeft = hardwareMap.crservo.get("servo_1");
        // vs  = this.hardwareMap.voltageSensor.iterator().next();
        timer = new ElapsedTime();//create a timer from the elapsed time class


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        cvRed = new OpenCvRed();
        cvRed.readyRed(hardwareMap, telemetry);


        brakeMotors();
        reverseMotors();
        telemetry.update();
        waitForStart();
        runtime.reset();


        //run autonomous
        if (opModeIsActive()) {
            motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  //stop and reset encoders
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);       //start encoders

            encoderDrive(1, 1, 1, -3);//back to carosel
            spinDuck(0.7);
            sleep(300);
            encoderDrive(0.3, 0.3 ,14, 14);//drive to first duck spot
            sleep(500);

            if (distance.getDistance(DistanceUnit.INCH) < 10){
                hubLevel = 1;
                sleep(500);

            }

            telemetry.addData("Distance",distance.getDistance(DistanceUnit.INCH) );
            telemetry.update();
            encoderDrive(0.3, 0.3, -3 , 2.5);//drive to second duck spot
            sleep(500);

            if (hubLevel != 1 && distance.getDistance(DistanceUnit.INCH) < 10){
                hubLevel = 2;
                sleep(500);

            }
            //use camera here for turn to hub
            while (cvRed.analysis() != 2){
                drive(-0.05,0.05);//turn towards hu
                telemetry.addData("Pos", cvRed.analysis());
                telemetry.update();

            }
            telemetry.addData("Pos", cvRed.analysis());
            telemetry.addLine("Hub Found!");
            telemetry.update();
            drive(0,0);
            sleep(500);

            encoderDrive(0.1, 0.1 ,16, 16); //drive towards hub

            //get rid of these 2 encoder drives when camera is implemented


            hubLevel(hubLevel); //drop preload in hub at hubLevel
            encoderDrive( 0, 0.5,-8,5); //turn to warehouse
            sleep(5000);

            encoderDrive( 0.95, 1,90,90); //drive to warehouse
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
            telemetry.addData("Target Left: ", newLeftTarget);
            telemetry.addData("Target right: ", newRightTarget);
            telemetry.addData("Current Pos Left:", motorLeftBACK.getCurrentPosition());
            telemetry.addData("Current Pos Right:", motorRightBACK.getCurrentPosition());
            telemetry.addData("right power: ", motorRightBACK.getPower());
            telemetry.addData("left power: ", motorLeftBACK.getPower());
            telemetry.addData("Pos", cvRed.analysis());
            telemetry.update();
        }
        // Stop all motion;
        drive(0, 0);
    }
    public void sensorDrive(double leftDTSpeed, double rightDTSpeed,int inches) {
        drive(leftDTSpeed, rightDTSpeed);
        while (distance.getDistance(DistanceUnit.INCH) < inches) {

            telemetry.addData("Current Pos Left:", motorLeftBACK.getCurrentPosition());
            telemetry.addData("Current Pos Right:", motorRightBACK.getCurrentPosition());
            telemetry.addData("right power: ", motorRightBACK.getPower());
            telemetry.addData("left power: ", motorLeftBACK.getPower());
            telemetry.update();
        }

        // Stop all motion;
        drive(0, 0);
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
    }

    public void spitOut(double power){
        motorCollector.setPower(power);
        sleep(2500);
        motorCollector.setPower(0);

    }

    public void spinDuck(double power){//for red Carousel the value needs to be negative
        motorDuck.setPower(power);
        sleep(3000);
        motorDuck.setPower(0);
    }

    public void extendOrRetract(double seconds,double power){
        motorXRail.setPower(power);//30 %
        sleep((long) (seconds * 1000));
        motorXRail.setPower(0);

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
        double spitSpeed = -0.2;

        switch (level) {
            case 1: //Lower
                armHeightSpeed = -0.5;
                armHeightInches = -5;
                armXSpeed = 0.5;
                armXInches = 8;
                break;
            case 2: //Middle
                armHeightSpeed = -0.5;
                armHeightInches = -3;
                armXSpeed = 0.5;
                armXInches =10;
                break;
            case 3: //Top
                armHeightSpeed = 0;
                armHeightInches = 0;
                armXSpeed = 0.5;
                armXInches = 16;
                break;
            default:
                break;
        }

        armHeight(armHeightSpeed, armHeightInches);
        armDrive(armXSpeed, armXInches);
        spitOut(spitSpeed);
        armDrive(-armXSpeed, -armXInches);
        armHeight(-armHeightSpeed, -armHeightInches);
    }

    private void displayInfo(double i, Recognition recognition) {
        // Display label info.
        // Display the label and index number for the recognition.
        telemetry.addData("label " + i, recognition.getLabel());
        telemetry.addData("width: ", recognition.getWidth() );
        telemetry.addData("height: ", recognition.getHeight() );
        telemetry.addData("H/W Ratio: ", recognition.getHeight()/recognition.getWidth() );
    }

    private void reverseMotors(){
        motorLeftBACK.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFRONT.setDirection(DcMotor.Direction.REVERSE);

    }

    private void brakeMotors(){
        motorLeftBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeftFRONT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightFRONT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        motorXRail.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
}
