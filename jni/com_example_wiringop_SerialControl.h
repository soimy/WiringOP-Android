/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_orangepi_api_SerialControl */

#ifndef _Included_com_example_orangepi_api_SerialControl
#define _Included_com_example_orangepi_api_SerialControl
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialOpen
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SerialControl_serialOpen
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialClose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialClose
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialFlush
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialFlush
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialPutchar
 * Signature: (IC)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialPutchar
  (JNIEnv *, jclass, jint, jchar);

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialPuts
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_example_wiringop_SerialControl_serialPuts
  (JNIEnv *, jclass, jint, jstring);

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialDataAvail
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SerialControl_serialDataAvail
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_example_orangepi_api_SerialControl
 * Method:    serialGetchar
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_example_wiringop_SerialControl_serialGetchar
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif