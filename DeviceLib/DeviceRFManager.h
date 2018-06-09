// DeviceRFManager
// (c) Ivanov Aleksandr, 2018

#ifndef _DeviceRFManager_H_
#define _DeviceRFManager_H_

#include <RH_ASK.h>
#include <RHReliableDatagram.h>
#include <SPI.h>

#define BAZA_ADDRESS 0

#define RADIO_TX_PIN 12
#define RADIO_RX_PIN 11
//MINI2
//#define RADIO_FREG 4000
#define RADIO_FREG 2000
#define INIT_ADRESS 1

//#define DEBUG

class RH_ASK;
class RHReliableDatagram;
class DeviceDataBase;

struct DataInfo {
	long  _uniqID; //INPUT/OUTPUT
	uint8_t  _deviceID; //INPUT/OUTPUT
	float _deviceAck; //OUTPUT
	float _deviceControl; //INPUT
	bool  _adjustable; //OUTPUT
	bool  _rotatable; //OUTPUT
	bool  _radioError; //OUTPUT
};



class DeviceRFManager
{
private:
	union DataInfoUnion {  
		DataInfo dataInfo;  
		uint8_t byteBuffer[sizeof(DataInfo)];  
	} dataInfoUnion;
	RH_ASK _driver;
	RHReliableDatagram _radioMngr;
	DeviceDataBase* _dataBase;
	bool _initError;
	
public:
	DeviceRFManager();
	~DeviceRFManager();
	
	// interface impl for controllers events
	bool identifyDevice();
	void sendInfo();
	void setDataBase(DeviceDataBase* _dataBase);
	long getUniqID();
	uint8_t getDeviceID();
	void setNewAdressAndHeadersInfo(uint8_t deviceNumber);

private:
	//methods
	void init();	
	void setUniqID(long pUniqID);	
	void setDeviceID(uint8_t pDeviceID);
	bool isRightUniqIdAndFrom(uint8_t from, long _uniqID);
	void prepareDataForKnowingTransmit();
	void prepareDataForWorkingTransmit();
};

#endif