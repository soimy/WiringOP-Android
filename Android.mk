LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

res_dir := app/src/main/res
LOCAL_MODULE_TAGS := optional

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/$(res_dir)

LOCAL_SRC_FILES := \
        $(call all-java-files-under, app/src/main/java)

LOCAL_MANIFEST_FILE := app/src/main/AndroidManifest.xml

LOCAL_STATIC_JAVA_LIBRARIES := \
        androidx.appcompat_appcompat \

LOCAL_CERTIFICATE:=platform
LOCAL_PACKAGE_NAME := wiringop
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_PRIVILEGED_MODULE := true
LOCAL_JNI_SHARED_LIBRARIES := libWiringOP
LOCAL_REQUIRED_MODULES := libWiringOP

include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH)/app/src/main/jni)
