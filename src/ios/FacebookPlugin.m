#import "FacebookPlugin.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>

@interface FacebookPlugin ()

@end

@implementation FacebookPlugin

- (CDVPlugin *)initWithWebView:(UIWebView *)theWebView {
    NSLog(@"Init FacebookConnect Session");
    self = (InvitePlugin *)[super initWithWebView:theWebView];
    
    return self;
}

- (void)login:(CDVInvokedUrlCommand*)command {
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    [login
     logInWithReadPermissions: @[@"public_profile"]
     handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
         if (error) {
             NSLog(@"Process error");
         } else if (result.isCancelled) {
             NSLog(@"Cancelled");
         } else {
             NSLog(@"Logged in");
         }
     }];
}

- (void)invite:(CDVInvokedUrlCommand*)command {
    FBSDKAppInviteContent *content =[[FBSDKAppInviteContent alloc] init];
    content.appLinkURL = [NSURL URLWithString:@"https://fb.me/400072263520020"];
    //optionally set previewImageURL
    content.appInvitePreviewImageURL = [NSURL URLWithString:@"http://wzzrb.nl/appinvite/1.png"];
    
    // present the dialog. Assumes self implements protocol `FBSDKAppInviteDialogDelegate`
    [FBSDKAppInviteDialog showWithContent:content
                                 delegate:self];
}

@end