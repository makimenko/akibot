// Libraries:
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>  
#include <unistd.h>  
#include <signal.h>  
#include <string.h>  
#include <errno.h>  
#include <inttypes.h>
#include <math.h>
#include <iostream>
#include <thread>
#include <softPwm.h>
#include <sys/time.h>  
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <linux/i2c-dev.h>
#include <fcntl.h>

#include <wiringPi.h>  

// Akibot custom libraries:
#include "AkibotJniLibrary.h"
#include "DistanceMeterSRF05.h"
#include "Servo.h"
#include "AkibotUtils.h"
#include "AkibotException.h"
#include "PCA9685.h"
#include "EchoLocator.h"
