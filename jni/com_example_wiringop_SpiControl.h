/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_orangepi_api_SpiControl */

#ifndef _Included_com_example_orangepi_api_SpiControl
#define _Included_com_example_orangepi_api_SpiControl
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPIGetFd
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPIGetFd
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPIDataRW
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPIDataRW
  (JNIEnv *, jclass, jint, jbyteArray, jint);

/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPISetupMode
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPISetupMode
  (JNIEnv *, jclass, jint, jint, jint);

/*
 * Class:     com_example_orangepi_api_SpiControl
 * Method:    wiringPiSPISetup
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SpiControl_wiringPiSPISetup
  (JNIEnv *, jclass, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
