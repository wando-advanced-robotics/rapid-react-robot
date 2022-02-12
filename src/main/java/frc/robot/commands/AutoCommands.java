package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;

public class AutoCommands {

    public AutoCommands() {
    }
        
    public static Command driveDistanceAutoCommand(double distance, Direction direction) {
        return new FunctionalCommand(
            null,
            () -> Robot.drive.driveDistance(),
            (interrupt) -> Robot.drive.tank(0, 0),
            () -> Robot.drive.reachedPosition(),
            Robot.drive
        );
    }

    public static Command turnCommand(Direction direction) {
        return new FunctionalCommand(
            null,
            () -> Robot.drive.turn(0.5),
            (interrupt) -> Robot.drive.tank(0, 0),
            () -> Robot.drive.reachedTurn(),
            Robot.drive
        );
    }

    public static Command driveToPosition(double xMeters, double yMeters) {
        return new InstantCommand(
            () -> Robot.drive.driveToPosition(xMeters, yMeters),
            Robot.drive
        );
    }

    public static Command driveToPosAutoCommand(double xMeters, double yMeters) {
        return new SequentialCommandGroup(
            driveToPosition(xMeters, yMeters),
            turnCommand(null),
            driveDistanceAutoCommand(0.0, null)
        );
    }

    public static Command angularTurnAutoCommand(double speed, double angle, Direction direction) {
        return new FunctionalCommand(
            () -> Robot.drive.resetAngle(),
            () -> Robot.drive.turn(speed),
            (interrupt) -> Robot.drive.tank(0, 0),
            () -> Robot.drive.getAngle() >= angle,
            Robot.drive
        );
    }

    public static Command curveTurnAutoCommand(double left, double right, Direction direction) {
        return new FunctionalCommand(
            () -> Robot.drive.resetPosition(), 
            () -> Robot.drive.driveCurve(left, right, direction), 
            (interrupt) -> Robot.drive.tank(0,0), 
            () -> Robot.drive.reachedCurve(left, right), 
            Robot.drive
        );
    }

    public static Command circleTurnAutoCommand(double speed, double angle, Direction direction, double radius) {
        return new FunctionalCommand(
            () -> Robot.drive.resetPosition(),
            () -> Robot.drive.driveCircle(speed, angle, direction, radius), 
            (interrupted) -> Robot.drive.tank(0,0), 
            () -> Robot.drive.reachedCircle(angle, radius, direction), 
            Robot.drive
        );
    }

    public static Command ballExitCommand(int balls) {
        return new FunctionalCommand(
            () -> Robot.shooter.setFlywheelReset(),
            () -> Robot.shooter.flywheelAndFlywheelIntakeOut(),
            (interrupt) -> Robot.shooter.flywheelAndFlywheelIntakeStop(),
            () -> Robot.shooter.flywheelDoneShootBalls(balls),
            Robot.shooter
        );
    }

    public static Command flywheelWait(int seconds) {
        return new WaitCommand(seconds);
    }

    public static Command activateFlywheel() {
        return new InstantCommand(
            () -> Robot.shooter.flywheelOut(), 
            Robot.shooter
        );
    }

    public static Command shootBallAutoCommand(int balls) {
        return new SequentialCommandGroup(
            activateFlywheel(),
            flywheelWait(3),
            ballExitCommand(balls)
        );
    }

//Mesurements in Inches and Degrees, all auto needs to be tested.

    public static Command threeBallAutoBottom() {
        return new SequentialCommandGroup(
            // shootBallAutoCommand(1),
            driveDistanceAutoCommand(41, Direction.BACKWARD),
            angularTurnAutoCommand(0.2, 90, Direction.LEFT),
            circleTurnAutoCommand(0.1, 30, Direction.RIGHT, 153),
            angularTurnAutoCommand(0.2, 90, Direction.RIGHT)
            // shootBallAutoCommand(2)
        );
    }

    public static Command twoBallAutoTop() {
        return new SequentialCommandGroup(
            //shootBallAutoCommand(1),
            driveDistanceAutoCommand(41, Direction.BACKWARD),
            angularTurnAutoCommand(0.2, 90, Direction.RIGHT),
            circleTurnAutoCommand(0.1, 10, Direction.LEFT, 153),
            angularTurnAutoCommand(0.2, 90, Direction.LEFT)
            // shootBallAutoCommand(1)
        );
    }

    public static Command twoBallAutoBottom() {
        return new SequentialCommandGroup(
            // shootBallAutoCommand(1),
            driveDistanceAutoCommand(41, Direction.BACKWARD),
            angularTurnAutoCommand(0.2, 90, Direction.LEFT),
            circleTurnAutoCommand(0.1, 10, Direction.RIGHT, 153),
            angularTurnAutoCommand(0.2, 90, Direction.RIGHT)
            // shootBallAutoCommand(1)
        );
    }
}