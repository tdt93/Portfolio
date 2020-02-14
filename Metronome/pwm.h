#include <MKL25Z4.h>

#define mod 4095
#define pin1 1
#define pin2 2

void Init_pwm(void);
void Set_PWM_Value(volatile double phase);
