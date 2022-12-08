LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
 
LOCAL_SRC_FILES := $(call all-subdir-java-files)
 

LOCAL_PACKAGE_NAME := wiringop
LOCAL_DEX_PREOPT := false 
LOCAL_CERTIFICATE := platform
LOCAL_MODULE_TAGS := optional
#LOCAL_CFLAGS := -lWiringOP
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_JNI_SHARED_LIBRARIES := libWiringOP
LOCAL_REQUIRED_MODULES := libWiringOP

LOCAL_DEX_PREOPT := false

#LOCAL_PRIVILEGED_MODULE := true
include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))
