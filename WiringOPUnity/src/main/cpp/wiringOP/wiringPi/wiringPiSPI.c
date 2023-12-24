/*
 * wiringPiSPI.c:
 *	Simplified SPI access routines
 *	Copyright (c) 2012-2015 Gordon Henderson
 ***********************************************************************
 * This file is part of wiringPi:
 *	https://projects.drogon.net/raspberry-pi/wiringpi/
 *
 *    wiringPi is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    wiringPi is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with wiringPi.
 *    If not, see <http://www.gnu.org/licenses/>.
 ***********************************************************************
 */


#include <stdint.h>
#include <fcntl.h>
#include <errno.h>
#include <string.h>
#include <stdio.h>
#include <sys/ioctl.h>
#include <asm/ioctl.h>
#include <linux/spi/spidev.h>

#include "wiringPi.h"

#include "wiringPiSPI.h"

#if defined(__ANDROID__)
#include <android/log.h>
#define LOG_TAG "wiringPi"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##args)
#endif

// The SPI bus parameters
//	Variables as they need to be passed as pointers later on

static const uint8_t     spiBPW   = 8 ;
static const uint16_t    spiDelay = 0 ;

static uint32_t    spiSpeeds [5] ;
static int         spiFds [5] ;


/*
 * wiringPiSPIGetFd:
 *	Return the file-descriptor for the given channel
 *********************************************************************************
 */

int wiringPiSPIGetFd (int channel)
{
  return spiFds [channel] ;
}


/*
 * wiringPiSPIDataRW:
 *	Write and Read a block of data over the SPI bus.
 *	Note the data ia being read into the transmit buffer, so will
 *	overwrite it!
 *	This is also a full-duplex operation.
 *********************************************************************************
 */

int wiringPiSPIDataRW (int channel, unsigned char *data, int len)
{
  struct spi_ioc_transfer spi ;

  //channel &= 1 ;

// Mentioned in spidev.h but not used in the original kernel documentation
//	test program )-:

  memset (&spi, 0, sizeof (spi)) ;

  spi.tx_buf        = (unsigned long)data ;
  spi.rx_buf        = (unsigned long)data ;
  spi.len           = len ;
  spi.delay_usecs   = spiDelay ;
  spi.speed_hz      = spiSpeeds [channel] ;
  spi.bits_per_word = spiBPW ;

#if defined(__ANDROID__)
    LOGI("wiringPiSPIDataRW ==> speed: %d, fd: %d", spi.speed_hz, spiFds [channel]);
#endif

  return ioctl (spiFds [channel], SPI_IOC_MESSAGE(1), &spi) ;
}


/*
 * wiringPiSPISetupMode:
 *	Open the SPI device, and set it up, with the mode, etc.
 *********************************************************************************
 */

void getDevice(char* spidev, int channel, int port) {
    sprintf(spidev, "/dev/spidev%i.%i", channel, port);
}

int wiringPiSPISetupMode (int channel, int port, int speed, int mode)
{
  int fd ;

  //mode    &= 3;
  //channel &= 7;

  static char spidev[15];

  getDevice(spidev, channel, port);
  printf("Opening device %s", spidev);
#if defined(__ANDROID__)
  LOGI("Opening device %s\n", spidev);
#endif

  if ((fd = open (spidev, O_RDWR)) < 0){
#if defined(__ANDROID__)
    LOGI("Unable to open SPI device: %s", strerror (errno));
#endif
    return wiringPiFailure (WPI_ALMOST, "Unable to open SPI device: %s\n", strerror (errno)) ;
  }

  spiSpeeds [channel] = speed ;
  spiFds    [channel] = fd ;

#if defined(__ANDROID__)
    LOGI("speed: %d, fd: %d", speed, fd);
#endif
// Set SPI parameters.

  if (ioctl (fd, SPI_IOC_WR_MODE, &mode)            < 0)
    return wiringPiFailure (WPI_ALMOST, "SPI Mode Change failure: %s\n", strerror (errno)) ;
  
  if (ioctl (fd, SPI_IOC_WR_BITS_PER_WORD, &spiBPW) < 0)
    return wiringPiFailure (WPI_ALMOST, "SPI BPW Change failure: %s\n", strerror (errno)) ;

  if (ioctl (fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed)   < 0)
    return wiringPiFailure (WPI_ALMOST, "SPI Speed Change failure: %s\n", strerror (errno)) ;

  return fd ;
}


/*
 * wiringPiSPISetup:
 *	Open the SPI device, and set it up, etc. in the default MODE 0
 *********************************************************************************
 */

int wiringPiSPISetup (int channel, int speed)
{
  return wiringPiSPISetupMode (channel, 0, speed, 0) ;
}
