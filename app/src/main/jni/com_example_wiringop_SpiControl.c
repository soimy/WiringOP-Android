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
#include <asm/ioctl.h>
#include <linux/spi/spidev.h>

#include "com_example_wiringop_SpiControl.h"

#define TAG "jni_spi"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, TAG, __VA_ARGS__)

// The SPI bus parameters
// Variables as they need to be passed as pointers later on
static const char       *spiDev0  = "/dev/spidev0.0";
static const char       *spiDev1  = "/dev/spidev1.0";
static const char       *spiDev2  = "/dev/spidev2.0";
static const char       *spiDev3  = "/dev/spidev3.0";
static const char       *spiDev4  = "/dev/spidev4.0";
static const uint8_t    spiBPW   = 8;
static const uint16_t   spiDelay = 0;
static uint32_t         spiSpeeds;
static int              spiFds;

/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPIGetFd
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPIGetFd
    (JNIEnv *env, jclass type, jint channel)
{
    return spiFds;
}

/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPIDataRW
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPIDataRW
    (JNIEnv *env, jclass type, jint channel, jbyteArray data, jint len)
{
    struct spi_ioc_transfer spi;
    unsigned char *cdata = NULL;
    int ret = -1;

    channel &= 1;

// Mentioned in spidev.h but not used in the original kernel documentation
// test program )-:
    memset (&spi, 0, sizeof (spi));

    cdata = (unsigned char*)(*env)->GetByteArrayElements(env, data, NULL);

    LOGI("----lee cdata0 = %d\n", cdata[0]);
    LOGI("----lee cdata1 = %d\n", cdata[1]);
    LOGI("----lee cdata2 = %d\n", cdata[2]);
    LOGI("----lee cdata3 = %d\n", cdata[3]);

    spi.tx_buf        = (unsigned long)cdata;
    spi.rx_buf        = (unsigned long)cdata;
    spi.len           = len;
    spi.delay_usecs   = spiDelay;
    spi.speed_hz      = spiSpeeds;
    spi.bits_per_word = spiBPW;

    LOGI("----lee spiFds[channel] = %d\n", spiFds);
    ret = ioctl (spiFds, SPI_IOC_MESSAGE(1), &spi);

    LOGI("----lee aftercdata0 = %x\n", cdata[0]);
    LOGI("----lee aftercdata1 = %x\n", cdata[1]);
    LOGI("----lee aftercdata2 = %x\n", cdata[2]);
    LOGI("----lee aftercdata3 = %x\n", cdata[3]);

    (*env)->ReleaseByteArrayElements(env, data, (jbyte *)cdata, 1);

    LOGI("----lee ret = %d\n", ret);

    return ret;

}

/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPISetupMode
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPISetupMode
    (JNIEnv *env, jclass type, jint channel, jint port, jint speed, jint mode)
{
    int fd;
    static char dev[15];
    LOGI("csy ---------------------------------- \n");
    mode &= 3;	// Mode is 0, 1, 2 or 3
    sprintf(dev, "/dev/spidev%i.%i", channel, port);
    LOGI("csy ---------------------------------- cccc dev = %s\n", dev);
    if(access(dev,W_OK)<0)
        LOGE("%s 不可写.", dev);
    else
        LOGE("%s 可写.", dev);

    if(access(dev,R_OK)<0)
        LOGE("%s 不可读取.", dev);
    else
        LOGE("%s 可读取.", dev);

    if ((fd = open (dev, O_RDWR)) < 0){
        LOGI("Unable to open SPI device %s\n", dev);

        return -1;
    }
    spiSpeeds = speed;
    spiFds = fd;

    // Set SPI parameters.
    if (ioctl (fd, SPI_IOC_WR_MODE, &mode) < 0){
        LOGI("SPI Mode Change failure: %s\n", dev);

        return -1;
    }


    if (ioctl (fd, SPI_IOC_WR_BITS_PER_WORD, &spiBPW) < 0){
        LOGI ("SPI BPW Change failure: %s\n", dev);

        return -1;
    }

    if (ioctl (fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed) < 0){
        LOGI ("SPI Speed Change failure: %s\n", dev);

        return -1;
    }

    return fd;
}

/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPISetup
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPISetup
    (JNIEnv *env, jclass type, jint channel, jint speed)
{
    int fd ;
    int spiMode = 0;
    const char *dev;
    channel &= 1 ;
    if(access(spiDev1,W_OK)<0)
        LOGE("spiDev1不可写.");
    else
        LOGE("spiDev1可写.");

    if(access(spiDev1,R_OK)<0)
        LOGE("spiDev1 不可读取.");
    else
        LOGE("spiDev1可读取.");

    switch (channel) {
        case 0:
            dev = spiDev0;
        case 1:
            dev = spiDev1;
        case 2:
            dev = spiDev2;
        case 3:
            dev = spiDev3;
        case 4:
            dev = spiDev4;
    }

    if ((fd = open (dev, O_RDWR)) < 0)
        return -1;

    spiSpeeds = speed;
    spiFds  = fd;

// Set SPI parameters.
//	Why are we reading it afterwriting it? I've no idea, but for now I'm blindly
//	copying example code I've seen online...

    if (ioctl (fd, SPI_IOC_WR_MODE, &spiMode)         < 0)
        return -1;

    if (ioctl (fd, SPI_IOC_WR_BITS_PER_WORD, &spiBPW) < 0)
        return -1;

    if (ioctl (fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed)   < 0)
        return -1;

    return fd ;
}

JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_testSpi
        (JNIEnv *env, jclass type, jint channel)
{
    unsigned char data[4];
    int rc;
    memset(data,0,sizeof(data));
    data[0] = 0x9f;
    LOGI("----lee SPI tset\n");
    rc = Java_com_example_wiringop_SpiControl_wiringPiSPIDataRW(env, type, 0, data, 4);

    LOGI("----lee rc = %d\n", rc);

//  spcDump("readManufacturer",rc,data,4);
    LOGI("----lee data1 = %d\n", data[1]);
    LOGI("----lee data2 = %d\n", data[2]);
    LOGI("----lee data3 = %d\n", data[3]);

    return 0;
}
