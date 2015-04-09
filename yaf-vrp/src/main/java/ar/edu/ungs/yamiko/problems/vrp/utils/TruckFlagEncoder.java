package ar.edu.ungs.yamiko.problems.vrp.utils;

import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodedDoubleValue;
import com.graphhopper.util.BitUtil;

public class TruckFlagEncoder extends CarFlagEncoder
{

    private EncodedDoubleValue reverseSpeed;

    public TruckFlagEncoder() {
        super();
	}

    public TruckFlagEncoder( String propertiesStr )
    {
        super(propertiesStr);
    }

    public TruckFlagEncoder( int speedBits, double speedFactor, int maxTurnCosts )
    {
        super(speedBits, speedFactor, maxTurnCosts);
    }

    @Override
    public int defineWayBits( int index, int shift )
    {
        shift = super.defineWayBits(index, shift);
        speedEncoder = new EncodedDoubleValue("Speed", shift, speedBits, speedFactor, defaultSpeedMap.get("secondary"), maxPossibleSpeed);
        reverseSpeed = new EncodedDoubleValue("Reverse Speed", shift, speedBits, speedFactor, defaultSpeedMap.get("secondary"), maxPossibleSpeed);
        shift += speedEncoder.getBits()+reverseSpeed.getBits();
        return shift;
    }

    @Override
    public double getReverseSpeed( long flags )
    {
        return reverseSpeed.getDoubleValue(flags);
    }

    @Override
    public long setReverseSpeed( long flags, double speed )
    {
        if (speed < 0)
            throw new IllegalArgumentException("Speed cannot be negative: " + speed + ", flags:" + BitUtil.LITTLE.toBitString(flags));

        if (speed > getMaxSpeed())
            speed = getMaxSpeed();

        return reverseSpeed.setDoubleValue(flags, speed);
    }

    @Override
    public long flagsDefault( boolean forward, boolean backward )
    {
        long flags = super.flagsDefault(forward, backward);
        if (backward)
            return reverseSpeed.setDefaultValue(flags);

        return flags;
    }

    @Override
    public long setProperties( double speed, boolean forward, boolean backward )
    {
        long flags = super.setProperties(speed, forward, backward);
        if (backward)
            return setReverseSpeed(flags, speed);

        return flags;
    }

    @Override
    public long reverseFlags( long flags )
    {
        // swap access
        flags = super.reverseFlags(flags);

        // swap speeds 
        double otherValue = reverseSpeed.getDoubleValue(flags);
        flags = setReverseSpeed(flags, speedEncoder.getDoubleValue(flags));
        return setSpeed(flags, otherValue);
    }

    @Override
    public String toString()
    {
        return "truck";
    }


}
