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


#include "com_example_wiringop_SerialControl.h"


#define TAG "jni_serial"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, TAG, __VA_ARGS__)


/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialOpen
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SerialControl_serialOpen
    (JNIEnv *env, jclass type, jstring dev, jint baud)
{
    const char *str;
    struct termios options ;
    speed_t myBaud ;
    int status, fd;

    str = (*env)->GetStringUTFChars(env, dev, NULL);

    LOGI ("[serialOpen] dev: %s!\n",str);

    switch (baud)
    {
        case      50:	myBaud =      B50 ; break ;
        case      75:	myBaud =      B75 ; break ;
        case     110:	myBaud =     B110 ; break ;
        case     134:	myBaud =     B134 ; break ;
        case     150:	myBaud =     B150 ; break ;
        case     200:	myBaud =     B200 ; break ;
        case     300:	myBaud =     B300 ; break ;
        case     600:	myBaud =     B600 ; break ;
        case    1200:	myBaud =    B1200 ; break ;
        case    1800:	myBaud =    B1800 ; break ;
        case    2400:	myBaud =    B2400 ; break ;
        case    4800:	myBaud =    B4800 ; break ;
        case    9600:	myBaud =    B9600 ; break ;
        case   19200:	myBaud =   B19200 ; break ;
        case   38400:	myBaud =   B38400 ; break ;
        case   57600:	myBaud =   B57600 ; break ;
        case  115200:	myBaud =  B115200 ; break ;
        case  230400:	myBaud =  B230400 ; break ;
        case  460800:	myBaud =  B460800 ; break ;
        case  500000:	myBaud =  B500000 ; break ;
        case  576000:	myBaud =  B576000 ; break ;
        case  921600:	myBaud =  B921600 ; break ;
        case 1000000:	myBaud = B1000000 ; break ;
        case 1152000:	myBaud = B1152000 ; break ;
        case 1500000:	myBaud = B1500000 ; break ;
        case 2000000:	myBaud = B2000000 ; break ;
        case 2500000:	myBaud = B2500000 ; break ;
        case 3000000:	myBaud = B3000000 ; break ;
        case 3500000:	myBaud = B3500000 ; break ;
        case 4000000:	myBaud = B4000000 ; break ;

        default:
            return -2 ;
    }

    if ((fd = open (str, O_RDWR | O_NOCTTY | O_NDELAY | O_NONBLOCK)) == -1)
    {
        LOGE ("[serialOpen] Failed to open serial!\n");

        return -1 ;
    }

    fcntl (fd, F_SETFL, O_RDWR) ;

// Get and modify current options:

    tcgetattr (fd, &options) ;

    cfmakeraw   (&options) ;
    cfsetispeed (&options, myBaud) ;
    cfsetospeed (&options, myBaud) ;

    options.c_cflag |= (CLOCAL | CREAD) ;
    options.c_cflag &= ~PARENB ;
    options.c_cflag &= ~CSTOPB ;
    options.c_cflag &= ~CSIZE ;
    options.c_cflag |= CS8 ;
    options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG) ;
    options.c_oflag &= ~OPOST ;

    options.c_cc [VMIN]  =   0 ;
    options.c_cc [VTIME] = 100 ;	// Ten seconds (100 deciseconds)

    tcsetattr (fd, TCSANOW, &options) ;

    ioctl (fd, TIOCMGET, &status);

    status |= TIOCM_DTR ;
    status |= TIOCM_RTS ;

    ioctl (fd, TIOCMSET, &status);

    usleep (10000) ;	// 10mS

    return fd;
}

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialClose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialClose
    (JNIEnv *env, jclass type, jint fd)
{
    close(fd);
    return;
}

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialFlush
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialFlush
  (JNIEnv *env, jclass type, jint fd)
{
    tcflush(fd, TCIOFLUSH);

    return;
}

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialPutchar
 * Signature: (IC)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialPutchar
  (JNIEnv *env, jclass type, jint fd, jchar c)
{
    int ret = -1;

    ret = write(fd, &c, 1);
    if (ret < 0)
    LOGE ("[serialPutchar] Failed to write serial!\n");

    return;
}

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialPuts
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialPuts
  (JNIEnv *env, jclass type, jint fd , jstring s)
{
    write(fd, s, strlen(s));

    return;
}

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialDataAvail
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SerialControl_serialDataAvail
  (JNIEnv *env, jclass type, jint fd)
{
    int result;

    if(ioctl(fd, FIONREAD, &result) == -1)
        return -1;

    return result;
}

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialGetchar
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SerialControl_serialGetchar
  (JNIEnv *env, jclass type, jint fd)
{
    uint8_t x;

    if(read(fd, &x, 1) != 1)
        return -1;

    return ((int)x) & 0xFF;
}


