LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

res_dir := app/src/main/res
LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/$(res_dir)
LOCAL_SRC_FILES := \
        $(call all-java-files-under, app/src/main/java)

LOCAL_MANIFEST_FILE := app/src/main/AndroidManifest.xml

LOCAL_PACKAGE_NAME := wiringop
LOCAL_MODULE_TAGS := optional
LOCAL_CERTIFICATE:=platform
LOCAL_PRIVATE_PLATFORM_APIS := true

#LOCAL_CFLAGS := -lWiringOP
LOCAL_JNI_SHARED_LIBRARIES := libWiringOP
LOCAL_REQUIRED_MODULES := libWiringOP

LOCAL_STATIC_JAVA_LIBRARIES := \
        androidx.appcompat_appcompat \
        com.google.android.material_material

LOCAL_DEX_PREOPT := false

include $(BUILD_PACKAGE)
include $(call all-makefiles-under, $(LOCAL_PATH)/app/src/main)
