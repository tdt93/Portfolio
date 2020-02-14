#include <MKL25Z4.h>
#include "leds.h"
#include "adc.h"
#include "pwm.h"

int main (void){
	
	volatile double X=1000, Y=1000, phase=0.2;
	uint32_t index =12;
	// BPM from: 10, 40 - 160
	uint32_t bpm[] ={12000000, 3000000, 2300000, 1900000, 1700000, 1500000, 1300000, 1100000, 1000000, 990000, 920000, 850000, 798000, 740000}; 
	
	LEDs_init();
	Welc_sequence(1000000);
	adc_Init();
	adc_Calibration();
	Init_pwm();
  Set_PWM_Value(phase);                                            
	
	while (1){
		
		delayMs(50);
 		X = adcReadX();		// X_portB pin0 in mV
		delayMs(10000);
		Y = adcReadY();		// Y_portB pin1 in mV
		delayMs(10000);
	
		
		if ((X <= 200)&&(phase > 0)){						// decreassing volume
			phase -=0.02;
			Set_PWM_Value(phase);
		}
		
		else if ((X > 2000)&&(phase < 1)){  		// increasing volume
			phase +=0.02;
			Set_PWM_Value(phase);
		}
			
		else if ((Y < 200)&&(index <= 12)){		// increasing BPM  
			index += 1;
		}
		
		else if ((Y > 2000)&&(index >= 1)){		// decreassing BPM  
			index -= 1;
		}
		
		delayMs(1000);
		Set_PWM_Value(phase);
		delayMs (bpm[index]);
		Set_PWM_Value(0);
		delayMs (bpm[index]);
	}
}
