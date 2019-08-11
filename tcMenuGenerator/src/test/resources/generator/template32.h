/*
    The code in this file uses open source libraries provided by thecoderscorner

    DO NOT EDIT THIS FILE, IT WILL BE GENERATED EVERY TIME YOU USE THE UI DESIGNER
    INSTEAD EITHER PUT CODE IN YOUR SKETCH OR CREATE ANOTHER SOURCE FILE.

    All the variables you may need access to are marked extern in this file for easy
    use elsewhere.
 */

#ifndef MENU_GENERATED_CODE_H
#define MENU_GENERATED_CODE_H

#include <tcMenu.h>
#include <header1.h>
#include <RuntimeMenuItem.h>

// all define statements needed
#define A_DEFINE 2

// all variables that need exporting
extern VarType varName;

// all menu item forward references.
extern IpAddressMenuItem menuSubIpItem;
extern TextMenuItem menuSubTextItem;
extern AnalogMenuItem menuSubTest2;
extern BackMenuItem menuBackSub;
extern SubMenuItem menuSub;
extern ListRuntimeMenuItem menuAbc;
extern AnalogMenuItem menuTest;
extern EnumMenuItem menuExtra;
extern const ConnectorLocalInfo applicationInfo;

// Callback functions must always include CALLBACK_FUNCTION after the return type
#define CALLBACK_FUNCTION

int fnAbcRtCall(RuntimeMenuItem* item, uint8_t row, RenderFnMode mode, char* buffer, int bufferSize);
void CALLBACK_FUNCTION callback1(int id);
void CALLBACK_FUNCTION callback2(int id);

void setupMenu();

#endif // MENU_GENERATED_CODE_H
