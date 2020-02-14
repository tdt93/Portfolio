#include <MKL25Z4.h>
#include "adc.h"

void adc_Init(void){
	SIM->SCGC6 |= SIM_SCGC6_ADC0_MASK;
	SIM->SCGC5 |= SIM_SCGC5_PORTB_MASK;
	
	ADC0->CFG1 |= ADC_CFG1_ADLSMP_MASK;																//Long sampling
	ADC0->CFG1 |= ADC_CFG1_ADICLK(01);																//Bus Clock/2 (24Mhz/2)
	ADC0->CFG1 |= ADC_CFG1_ADIV(10);																	//Division by 4 -> 3MHz 
	
	ADC0->CFG2 |= ADC_CFG2_ADHSC_MASK;																//Wspomaganie dla wys. czestotliwosci
	ADC0->CFG2 |= ADC_CFG2_ADLSTS(0);																	//Additional cycles
	
	ADC0->SC2 |= ADC_SC2_REFSEL(00);																	//Outside referece voltage
	
	ADC0->SC3 |= ADC_SC3_AVGE_MASK;																		//System averaging
	ADC0->SC3 |= ADC_SC3_AVGS(11);																		//32 samples
	
}

void adc_Calibration(void){
	uint32_t measurement;
	
	while(1){
		ADC0->SC3 |= ADC_SC3_CAL_MASK;																	//Start of calibration
		while(ADC0->SC3 & ADC_SC3_CAL_MASK){														//Stop the calibration if successful
		}
															
		if (!(ADC0->SC3 & ADC_SC3_CALF_MASK))
			break;
	}
	
	measurement = ADC0->CLP0 + ADC0->CLP1 + ADC0->CLP2 + ADC0->CLP3 + ADC0->CLP4 + ADC0->CLPD + ADC0->CLPS; //summing
	measurement = measurement/2;
	measurement |= (1UL << 15); 						//Setting the MSB
	ADC0->PG = ADC_PG_PG(measurement);			//To PG register
	
	measurement = ADC0->CLM0+ ADC0->CLM1 + ADC0->CLM2 + ADC0->CLM3 + ADC0->CLM4 + ADC0->CLMD + ADC0->CLMS;	
	measurement = measurement/2;
	measurement |= (1UL << 15);
	ADC0->MG = ADC_MG_MG(measurement);			//To MG register
	
	ADC0->CFG1 |= ADC_CFG1_MODE(1);					//BUS_CLOCK/2, disvison by 1, long sampling, resolution 12 bits
	
}

float adcReadX (void){
			ADC0->SC1[0] = 9;										//Channel for measuring/conversion the voltage, PORTB pin1 ch 9
			float x = ADC0->R[0]*3300/4096;
			return x; 													// convert result to mV
}

float adcReadY (void){
			ADC0->SC1[0] = 8;										//Channel for measuring/conversion the voltage, PORTB pin0 ch 8 
			float y = ADC0->R[0]*3000/4096;
			return y;
}

