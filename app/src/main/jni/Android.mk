LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_MODULE:= libWiringOP
# All of the shared libraries we link against.
LOCAL_SRC_FILES:= com_example_wiringop_GPIOControl.c \
                  com_example_wiringop_I2cControl.c \
                  com_example_wiringop_SerialControl.c  \
                  com_example_wiringop_SpiControl.c

LOCAL_LDLIBS    := -llog

LOCAL_CFLAGS += -Wno-unused-parameter -Wno-uninitialized

# Also need the JNI headers.
JNI_H_INCLUDE := libnativehelper/include_jni
LOCAL_C_INCLUDES += \
        $(JNI_H_INCLUDE)

LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)
