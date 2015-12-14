#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=GNU-Linux-x86
CND_DLIB_EXT=so
CND_CONF=Library
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/AkibotJniLibrary.o \
	${OBJECTDIR}/AkibotUtils.o \
	${OBJECTDIR}/DistanceMeterSRF05.o \
	${OBJECTDIR}/EchoLocator.o \
	${OBJECTDIR}/PCA9685.o \
	${OBJECTDIR}/Servo.o \
	${OBJECTDIR}/main.o


# C Compiler Flags
CFLAGS=-shared -lwiringPi

# CC Compiler Flags
CCFLAGS=-shared -lwiringPi -std=c++0x
CXXFLAGS=-shared -lwiringPi -std=c++0x

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk /home/pi/share/netbeans/akibot-jni-cpp.${CND_DLIB_EXT}

/home/pi/share/netbeans/akibot-jni-cpp.${CND_DLIB_EXT}: ${OBJECTFILES}
	${MKDIR} -p /home/pi/share/netbeans
	${LINK.cc} -o /home/pi/share/netbeans/akibot-jni-cpp.${CND_DLIB_EXT} ${OBJECTFILES} ${LDLIBSOPTIONS} -shared -fPIC

${OBJECTDIR}/AkibotJniLibrary.o: AkibotJniLibrary.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux -fPIC  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/AkibotJniLibrary.o AkibotJniLibrary.cpp

${OBJECTDIR}/AkibotUtils.o: AkibotUtils.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux -fPIC  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/AkibotUtils.o AkibotUtils.cpp

${OBJECTDIR}/DistanceMeterSRF05.o: DistanceMeterSRF05.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux -fPIC  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/DistanceMeterSRF05.o DistanceMeterSRF05.cpp

${OBJECTDIR}/EchoLocator.o: EchoLocator.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux -fPIC  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/EchoLocator.o EchoLocator.cpp

${OBJECTDIR}/PCA9685.o: PCA9685.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux -fPIC  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/PCA9685.o PCA9685.cpp

${OBJECTDIR}/Servo.o: Servo.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux -fPIC  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/Servo.o Servo.cpp

${OBJECTDIR}/main.o: main.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include -I/usr/lib/jvm/jdk-8-oracle-arm-vfp-hflt/include/linux -fPIC  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/main.o main.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} /home/pi/share/netbeans/akibot-jni-cpp.${CND_DLIB_EXT}

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
