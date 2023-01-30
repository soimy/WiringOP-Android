LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := libwiringPi
LOCAL_C_INCLUDES += \
    $(LOCAL_PATH)/wiringOP/wiringPi

LOCAL_SRC_FILES := \
    wiringOP/wiringPi/wiringSerial.c \
    wiringOP/wiringPi/wiringPi.c \
    wiringOP/wiringPi/wiringShift.c \
    wiringOP/wiringPi/piHiPri.c \
    wiringOP/wiringPi/piThread.c \
    wiringOP/wiringPi/wiringPiSPI.c \
    wiringOP/wiringPi/wiringPiI2C.c \
    wiringOP/wiringPi/mcp23008.c \
    wiringOP/wiringPi/mcp23016.c \
    wiringOP/wiringPi/mcp23017.c \
    wiringOP/wiringPi/mcp23s08.c \
    wiringOP/wiringPi/mcp23s17.c \
    wiringOP/wiringPi/sr595.c \
    wiringOP/wiringPi/pcf8574.c \
    wiringOP/wiringPi/pcf8591.c \
    wiringOP/wiringPi/mcp3002.c \
    wiringOP/wiringPi/mcp3004.c \
    wiringOP/wiringPi/mcp4802.c \
    wiringOP/wiringPi/mcp3422.c \
    wiringOP/wiringPi/max5322.c \
    wiringOP/wiringPi/sn3218.c \
    wiringOP/wiringPi/drcSerial.c \
    wiringOP/wiringPi/htu21d.c \
    wiringOP/wiringPi/bmp180.c \
    wiringOP/wiringPi/ds18b20.c \
    wiringOP/wiringPi/rht03.c \
    wiringOP/wiringPi/softPwm.c \
    wiringOP/wiringPi/wpiExtensions.c
    #wiringOP/wiringPi/ads1115.c \
    #wiringOP/wiringPi/pseudoPins.c \
    #wiringOP/wiringPi/drcNet.c \
    #wiringOP/wiringPi/max31855.c \
    #wiringOP/wiringPi/softTone.c \

LOCAL_CFLAGS    += -DDEBUG -DANDROID -DTINKER_BOARD -Wno-unused-function -Wno-unused-parameter -Wno-uninitialized -Wno-unused-variable -Wno-macro-redefined

LOCAL_LDLIBS    := -ldl -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_C_INCLUDES += \
    $(LOCAL_PATH)/wiringOP/wiringPi \
    $(LOCAL_PATH)/wiringOP/devLib/

LOCAL_MODULE    := libwiringPiDev
LOCAL_SRC_FILES := \
    wiringOP/devLib/ds1302.c \
    wiringOP/devLib/maxdetect.c \
    wiringOP/devLib/piNes.c \
    wiringOP/devLib/gertboard.c \
    wiringOP/devLib/piFace.c \
    wiringOP/devLib/lcd128x64.c \
    wiringOP/devLib/lcd.c \
    wiringOP/devLib/piGlow.c

LOCAL_SHARED_LIBRARIES := libwiringPi

LOCAL_LDLIBS    := -ldl -llog

LOCAL_CFLAGS    += -UNDEBUG -DANDROID  -DTINKER_BOARD -Wno-unused-function -Wno-unused-parameter -Wno-uninitialized -Wno-unused-variable -Wno-macro-redefined
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_C_INCLUDES += \
   $(LOCAL_PATH)/wiringOP/wiringPi \
   $(LOCAL_PATH)/wiringOP/devLib/

LOCAL_MODULE    := libWiringOP
LOCAL_SRC_FILES := \
    com_example_wiringop_wpiControl.c
LOCAL_LDLIBS    := -ldl -llog
LOCAL_SHARED_LIBRARIES := libwiringPi libwiringPiDev
# Also need the JNI headers.
JNI_H_INCLUDE := libnativehelper/include_jni
LOCAL_C_INCLUDES += \
        $(JNI_H_INCLUDE)
LOCAL_CFLAGS    += -Wno-unused-function -Wno-unused-parameter -Wno-uninitialized -Wno-unused-variable -Wno-macro-redefined
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := gpiox
LOCAL_SRC_FILES := wiringOP/gpio/gpio.c wiringOP/gpio/readall.c
LOCAL_C_INCLUDES += \
    $(LOCAL_PATH)/wiringOP/wiringPi \
    $(LOCAL_PATH)/wiringOP/devLib

LOCAL_MODULE_TAGS := optional
LOCAL_CFLAGS    += -UNDEBUG -DANDROID  -DTINKER_BOARD -Wno-unused-function -Wno-unused-parameter -Wno-uninitialized -Wno-unused-variable -Wno-unreachable-code-loop-increment
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := libwiringPi libwiringPiDev

include $(BUILD_EXECUTABLE)
