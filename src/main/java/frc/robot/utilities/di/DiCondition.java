package frc.robot.utilities.di;

abstract class DiCondition {
    /**
    * Returns whether the Condition applies to a specific Context
    *
    * @param context The Context to check against
    * @return Whether or not the rule applies to the Context
    * @see DiRule
    */
    abstract Boolean check(DiContext context);
}