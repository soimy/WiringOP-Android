LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_LDLIBS    := -llog

LOCAL_MODULE:= WiringOP
LOCAL_SRC_FILES:= com_example_wiringop_GPIOControl.c \
                  com_example_wiringop_I2cControl.c \
                  com_example_wiringop_SerialControl.c  \
                  com_example_wiringop_SpiControl.c

#LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)
