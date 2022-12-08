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
#include <sys/mman.h>
#include <unistd.h>
#include <android/log.h>
#include <stdarg.h>
#include <termios.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>

#include "com_example_wiringop_I2cControl.h"
//#include "../../../../../Android/Sdk/ndk/20.0.5594570/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/include/jni.h"
//#include "../../../../../Android/Sdk/ndk/20.0.5594570/toolchains/llvm/prebuilt/linux-x86_64/sysroot/usr/include/stdint.h"

#define TAG "jni_i2c"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, TAG, __VA_ARGS__)

// I2C definitions

#define I2C_SLAVE	0x0703
#define I2C_SMBUS	0x0720	/* SMBus-level access */

#define I2C_SMBUS_READ	1
#define I2C_SMBUS_WRITE	0

// SMBus transaction types

#define I2C_SMBUS_QUICK		    0
#define I2C_SMBUS_BYTE		    1
#define I2C_SMBUS_BYTE_DATA	    2
#define I2C_SMBUS_WORD_DATA	    3
#define I2C_SMBUS_PROC_CALL	    4
#define I2C_SMBUS_BLOCK_DATA	    5
#define I2C_SMBUS_I2C_BLOCK_BROKEN  6
#define I2C_SMBUS_BLOCK_PROC_CALL   7		/* SMBus 2.0 */
#define I2C_SMBUS_I2C_BLOCK_DATA    8

// SMBus messages

#define I2C_SMBUS_BLOCK_MAX	32	/* As specified in SMBus standard */
#define I2C_SMBUS_I2C_BLOCK_MAX	32	/* Not specified but we use same structure */

// Structures used in the ioctl() calls

union i2c_smbus_data
{
    uint8_t  byte ;
    uint16_t word ;
    uint8_t  block [I2C_SMBUS_BLOCK_MAX + 2] ;	// block [0] is used for length + one more for PEC
} ;

struct i2c_smbus_ioctl_data
{
    char read_write ;
    uint8_t command ;
    int size ;
    union i2c_smbus_data *data ;
} ;

static inline int i2c_smbus_access (int fd, char rw, uint8_t command, int size, union i2c_smbus_data *data)
{
    struct i2c_smbus_ioctl_data args ;

    args.read_write = rw ;
    args.command    = command ;
    args.size       = size ;
    args.data       = data ;
    return ioctl (fd, I2C_SMBUS, &args) ;
}

/*
 * Class:     com_example_orangepi_api_I2cControl
 * Method:    wiringPiI2CRead
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_I2cControl_wiringPiI2CRead
        (JNIEnv *env, jclass type, jint fd)
{
    union i2c_smbus_data data;

    if (i2c_smbus_access(fd, I2C_SMBUS_READ, 0, I2C_SMBUS_BYTE, &data))
        return -1;
    else
        return data.byte & 0xFF;
}

/*
 * Class:     com_example_orangepi_api_I2cControl
 * Method:    wiringPiI2CReadReg8
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_I2cControl_wiringPiI2CReadReg8
        (JNIEnv *env, jclass type, jint fd, jint reg)
{
    union i2c_smbus_data data;

    if (i2c_smbus_access (fd, I2C_SMBUS_READ, reg, I2C_SMBUS_BYTE_DATA, &data))
        return -1 ;
    else
        return data.byte & 0xFF ;
}

/*
 * Class:     com_example_orangepi_api_I2cControl
 * Method:    wiringPiI2CReadReg16
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_I2cControl_wiringPiI2CReadReg16
        (JNIEnv *env, jclass type, jint fd, jint reg)
{
    union i2c_smbus_data data;

    if (i2c_smbus_access(fd, I2C_SMBUS_READ, reg, I2C_SMBUS_WORD_DATA, &data))
        return -1 ;
    else
        return data.word & 0xFFFF ;
}

/*
 * Class:     com_example_orangepi_api_I2cControl
 * Method:    wiringPiI2CWrite
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_I2cControl_wiringPiI2CWrite
        (JNIEnv *env, jclass type, jint fd, jint data)
{
    return i2c_smbus_access(fd, I2C_SMBUS_WRITE, data, I2C_SMBUS_BYTE, NULL) ;
}

/*
 * Class:     com_example_orangepi_api_I2cControl
 * Method:    wiringPiI2CWriteReg8
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_I2cControl_wiringPiI2CWriteReg8
        (JNIEnv *env, jclass type, jint fd, jint reg, jint value)
{
    union i2c_smbus_data data;

    data.byte = value;

    return i2c_smbus_access(fd, I2C_SMBUS_WRITE, reg, I2C_SMBUS_BYTE_DATA, &data);
}

/*
 * Class:     com_example_orangepi_api_I2cControl
 * Method:    wiringPiI2CWriteReg16
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_I2cControl_wiringPiI2CWriteReg16
        (JNIEnv *env, jclass type, jint fd, jint reg, jint value)
{
    union i2c_smbus_data data;

    data.word = value;

    return i2c_smbus_access(fd, I2C_SMBUS_WRITE, reg, I2C_SMBUS_WORD_DATA, &data);
}

/*
 * Class:     com_example_orangepi_api_I2cControl
 * Method:    wiringPiI2CSetup
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_I2cControl_wiringPiI2CSetup
        (JNIEnv *env, jclass type, jstring device, jint devId)
{
    int fd ;
    const char *str;

    str = (*env)->GetStringUTFChars(env, device, NULL);

    LOGI("Open I2C device: %s\n", str);
    if ((fd = open (str, O_RDWR)) < 0) {
        LOGI("Unable to open I2C device: %s\n", str);

        return -1;
    }

    if (ioctl (fd, I2C_SLAVE, devId) < 0) {
        LOGI("Unable to select I2C device: %d\n", devId);

        return -1;
    }

    return fd;
}
