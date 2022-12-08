//
// Created by csy on 19-9-24.
//

#include <jni.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <unistd.h>
#include <android/log.h>

#include "com_example_wiringop_GPIOControl.h"

#define TAG "jni_gpio"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

#define IN              0
#define OUT             1
#define LOW             0
#define HIGH            1

#define BUFFER_MAX    3
#define DIRECTION_MAX 48

/*
 * Class:     com_example_gpio_demo_GPIOControl
 * Method:    doExport
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_GPIOControl_doExport
(JNIEnv *env, jclass instance, jint gpio)
{
    char buffer[BUFFER_MAX];
    int len;
    int fd;

    if(access("/sys/class/gpio/export",W_OK)<0)
        LOGE("/sys/class/gpio/export 不可写.");
    else
        LOGE("/sys/class/gpio/export可写.");
    fd = open("/sys/class/gpio/export", O_WRONLY);

    if (fd < 0) {
       LOGE("Failed to open export for writing!\n");
        return(0);
    }
    LOGE("export pin[%d] surcess!\n", gpio);
    len = snprintf(buffer, BUFFER_MAX, "%d", gpio);
    if (write(fd, buffer, len) < 0) {
        LOGE("Fail to export gpio!\n");
        return 0;
    }
    fsync(fd);
    close(fd);
    return 1;
}

/*
 * Class:     com_example_gpio_demo_GPIOControl
 * Method:    pinMode
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_GPIOControl_pinMode
  (JNIEnv *env, jclass instance, jint gpio, jint direction)
{
        static const char dir_str[]  = "in\0out";
        char path[DIRECTION_MAX];
        int fd;

        snprintf(path, DIRECTION_MAX, "/sys/class/gpio/gpio%d/direction", gpio);
        LOGE("path = %s.", path);
        if(access(path,F_OK)<0)
            LOGE("path = %s 不存在.", path);
        else
            LOGE("path = %s 存在.", path);
        if(access(path,W_OK)<0)
            LOGE("path = %s 不可写.", path);
        else
            LOGE("path = %s 可写.",path);
        if(access(path,R_OK)<0)
            LOGE("path = %s 不可读取.", path);
        else
            LOGE("path = %s 可读取.", path);
        fd = open(path, O_WRONLY);
        if (fd < 0) {
            LOGE("failed to open gpio direction for writingaa!\n");
            return 0;
        }

        if (write(fd, &dir_str[direction == IN ? 0 : 3], direction == IN ? 2 : 3) < 0) {
           // LOGE("failed to set direction!\n");
            return 0;
        }
        fsync(fd);
        close(fd);
        return 1;
}

/*
 * Class:     com_example_gpio_demo_GPIOControl
 * Method:    digitalRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_GPIOControl_digitalRead
  (JNIEnv *env, jclass instance, jint pin)
{
    char path[DIRECTION_MAX];
    char value_str[3];
    int fd;

    snprintf(path, DIRECTION_MAX, "/sys/class/gpio/gpio%d/value", pin);
    fd = open(path, O_RDONLY);
    if (fd < 0) {
        LOGE("failed to open gpio value for reading!\n");
        return -1;
    }

    if (read(fd, value_str, 3) < 0) {
        //LOGE("failed to read value!\n");
        return -1;
    }

    close(fd);
    return (atoi(value_str));
}

/*
 * Class:     com_example_gpio_demo_GPIOControl
 * Method:    digitalWrite
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_GPIOControl_digitalWrite
  (JNIEnv *env, jclass instance, jint pin, jint value)
{
    static const char values_str[] = "01";
    char path[DIRECTION_MAX];
    int fd;

    snprintf(path, DIRECTION_MAX, "/sys/class/gpio/gpio%d/value", pin);
    if(access(path,F_OK)<0)
        LOGE("path = %s 不存在.", path);
    else
        LOGE("path = %s 存在.", path);
    if(access(path,W_OK)<0)
        LOGE("path = %s 不可写.", path);
    else
        LOGE("path = %s 可写.",path);
    if(access(path,R_OK)<0)
        LOGE("path = %s 不可读取.", path);
    else
        LOGE("path = %s 可读取.", path);
    fd = open(path, O_WRONLY);
    if (fd < 0) {
        LOGE("failed to open gpio value for writing!\n");
        return 0;
    }

    if (write(fd, &values_str[value == LOW ? 0 : 1], 1) < 0) {
      //  LOGE("failed to write value!\n");
        return 0;
    }
    fsync(fd);
    close(fd);
    return 1;
}

/*
 * Class:     com_example_gpio_demo_GPIOControl
 * Method:    doUnexport
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_GPIOControl_doUnexport
  (JNIEnv *env, jclass instance, jint gpio)
{
    char buffer[BUFFER_MAX];
    int len;
    int fd;

    fd = open("/sys/class/gpio/unexport", O_WRONLY);
    if (fd < 0) {
        LOGE("Failed to open unexport for writing!\n");
        return 0;
    }

    len = snprintf(buffer, BUFFER_MAX, "%d", gpio);
    if (write(fd, buffer, len) < 0) {
        LOGE("Fail to unexport gpio!");
        return 0;
    }
    fsync(fd);
    close(fd);
    return 1;
}
