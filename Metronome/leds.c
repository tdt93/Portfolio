#include <MKL25Z4.h>
#include "leds.h"

void LEDs_init(void){
	SIM_SCGC5 |= SIM_SCGC5_PORTD(1)|SIM_SCGC5_PORTB(1);					// enable CLK PORT B, D
	
	PORTB->PCR[RED_LED] |= PORT_PCR_MUX(1);		// set PIN 18 PORT B as GPIO
	PORTB->PCR[GREEN_LED] |= PORT_PCR_MUX(1);	// set PIN 19	PORT B
	PORTD->PCR[BLUE_LED] |= PORT_PCR_MUX(1);	// set PIN 1 PORT D
	
	GPIOB->PDDR |= (1<<RED_LED);							//set PIN 18 GPIO as output
	GPIOB->PDDR |= (1<<GREEN_LED);						//set PIN 19
	GPIOD->PDDR |= (1<<BLUE_LED);	
	
	GPIOB->PDOR |= (1<<RED_LED);
	GPIOB->PDOR |= (1<<GREEN_LED);
	GPIOD->PDOR |= (1<<BLUE_LED);
}
	
void Welc_sequence (int nr){
	
	GPIOB->PDOR &= ~(1<<RED_LED);							//turn on PIN 18 
	delayMs(nr);
	GPIOB->PDOR |= (1<<RED_LED);							//turn off PIN 18 
	GPIOB->PDOR &= ~(1<<GREEN_LED);
	delayMs(nr);
	GPIOB->PDOR |= (1<<GREEN_LED);
	GPIOD->PDOR &= ~(1<<BLUE_LED);
	delayMs(nr);
	GPIOD->PDOR |= (1<<BLUE_LED);
	delayMs(nr);
}

void delayMs(uint32_t s){										//delay loop
	while (s--)
		asm("nop");
}
