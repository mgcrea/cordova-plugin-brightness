/**
 * @author Evgeniy Lukovsky
 *
 */

#import <UIKit/UIScreen.h>
#import "Brightness.h"

@implementation Brightness : CDVPlugin

- (void)getBrightness:(CDVInvokedUrlCommand *)command
{
    CDVPluginResult * pluginResult = nil;
    float brightness = [UIScreen mainScreen].brightness;
    NSString *result = [NSString stringWithFormat:@"%f", brightness];
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:result];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setBrightness:(CDVInvokedUrlCommand *)command
{
    CDVPluginResult * pluginResult = nil;
    NSString *value = [command.arguments objectAtIndex:0];
    float brightness = [value floatValue];
    [UIScreen mainScreen].brightness = brightness;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)setKeepScreenOn:(CDVInvokedUrlCommand *)command
{
    CDVPluginResult* pluginResult = nil;
    BOOL value = [[command.arguments objectAtIndex:0] boolValue];
    [UIApplication sharedApplication].idleTimerDisabled = value;
    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
