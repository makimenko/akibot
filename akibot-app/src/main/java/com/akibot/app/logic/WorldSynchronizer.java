package com.akibot.app.logic;

import com.akibot.app.exception.WorkflowException;
import com.akibot.common.element.Vector3D;

public interface WorldSynchronizer {

	void syncGyroscope(String nodeName, Vector3D gyroscopeValue) throws WorkflowException;
}
