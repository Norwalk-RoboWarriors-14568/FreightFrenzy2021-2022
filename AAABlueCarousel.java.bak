package Autonomous;
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


@Autonomous(name = "Blue Car")
public class AAABlueCarousel extends LinearOpMode {
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
    private double CPI_ATV_DT, CPI_OMNI_DT;
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
       // vs  = this.hardwareMap.voltageSensor.iterator().next();
        timer = new ElapsedTime();//create a timer from the elapsed time class


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        

      
        brakeMotors();
        reverseMotors();
        telemetry.update();
        waitForStart();
        runtime.reset();

       
        //run autonomous
        if (opModeIsActive()) {
            motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);  //stop and reset encoders
            motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);       //start encoders
            
                
                encoderDrive(0.53, 1, 2, -2);//STRAIGHT
                spinDuck(1);
                sleep(300);
                encoderDrive(0.6, 1 ,28, 36);//Arc To shipping Hub
                sleep(500);
                extendOrRetract(2);//extend or retract - is extend and + is retract// based off time do not put over 4
                spitOut(-1);
                encoderDrive( 1, 0,-14,4);////
                                
                sleep(500);

                encoderDrive( 0.5, 0.5, -30,-30);////
                telemetry.update();
                sleep(20000);

                
                //odometryDrive(2, -0.37,0.37,10, 10,false);//Turn
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
////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 20;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }
    public void spitOut(double power){
        motorCollector.setPower(power);
        sleep(1500);
        motorCollector.setPower(0);
        
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


