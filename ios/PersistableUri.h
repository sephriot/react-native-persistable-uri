
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNPersistableUriSpec.h"

@interface PersistableUri : NSObject <NativePersistableUriSpec>
#else
#import <React/RCTBridgeModule.h>

@interface PersistableUri : NSObject <RCTBridgeModule>
#endif

@end
