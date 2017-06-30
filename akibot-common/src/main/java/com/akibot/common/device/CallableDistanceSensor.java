package com.akibot.common.device;

import com.akibot.common.element.Distance;

public interface CallableDistanceSensor extends DistanceSensor {

	Distance getDistance();

}
