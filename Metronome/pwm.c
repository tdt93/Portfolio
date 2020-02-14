#include <MKL25Z4.h>
#include "pwm.h"
#include "leds.h"
#include "math.h"

void Init_pwm(void) {
	
	MCG_BASE_PTR->C1 = MCG_C1_IREFS_MASK | MCG_C1_IRCLKEN_MASK;
	MCG_BASE_PTR->C2 = MCG_C2_IRCS_MASK;																	// internal CLK with 4MHz
	SIM_BASE_PTR->SCGC6 |= SIM_SCGC6_TPM2_MASK;														//clock to TPM2 clk module
	SIM_BASE_PTR->SOPT2 |= SIM_SOPT2_TPMSRC(3) ;
	
	TPM2_BASE_PTR->SC |= TPM_SC_PS(3);                                    //dision by 128
	TPM2_BASE_PTR->MOD = mod;																							//Set a range of the TMP0 counter to 4095 (MOD) 
																																				// ->reset itself 7 times per sec
																																				//TPM period = 2*MOD*Period counter clk
	TPM2_BASE_PTR->SC |= TPM_SC_CMOD(1); 																	//enable counter
	
	SIM_BASE_PTR->SCGC5 |= SIM_SCGC5_PORTA_MASK;
	PORTA_BASE_PTR->PCR[pin1] = PORT_PCR_MUX(3);															//set multiplexer to connect TPM2 Ch 0 to PTA1
	PORTA_BASE_PTR->PCR[pin2] = PORT_PCR_MUX(3);															//set multiplexer to connect TPM2 Ch 1 to PTA2  !!!
	PTA_BASE_PTR->PDDR |= (1<<pin1|(1<<pin2));
							
	TPM2_BASE_PTR->CONTROLS[1].CnSC = TPM_CnSC_MSB_MASK|TPM_CnSC_ELSB_MASK;		// Read low pulse on channel 1 pin2 portA
	TPM2_BASE_PTR->CONTROLS[0].CnSC = TPM_CnSC_MSB_MASK|TPM_CnSC_ELSA_MASK;		// Read high pulse on channel 0 pin1 portA
	 
}

void Set_PWM_Value(volatile double phase) {																	// phase from 0 to 1 (min to max range)
	volatile double n;
	
	n = 1/2 + sin(phase)/2;																										// changing phase
	TPM2_BASE_PTR->CONTROLS[0].CnV = TPM2_BASE_PTR->CONTROLS[1].CnV = TPM2_BASE_PTR->MOD*n;
	
}

