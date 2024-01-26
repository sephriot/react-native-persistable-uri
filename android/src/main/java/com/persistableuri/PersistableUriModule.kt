package com.persistableuri

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.facebook.react.bridge.BaseActivityEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class PersistableUriModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun selectDocument(type: String?, promise: Promise) {
    val reactContext: ReactApplicationContext = getReactApplicationContext()

    val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.addFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
            Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
    )

    if (type != null) {
      intent.setType(type)
    } else {
      intent.setType("*/*")
    }
    val READABLE_REQUEST_CODE = 10000
    val WRITABLE_REQUEST_CODE = 20000

    val currentActivity: Activity? = reactContext.getCurrentActivity()

    if (currentActivity != null) {
      reactContext.addActivityEventListener(
          object : BaseActivityEventListener() {
            override fun onActivityResult(
                activity: Activity,
                requestCode: Int,
                resultCode: Int,
                data: Intent?
            ) {
              reactContext.removeActivityEventListener(this)

              if (requestCode == READABLE_REQUEST_CODE || requestCode == WRITABLE_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                  if (data == null) {
                    promise.resolve(null)
                  } else {
                    val uri: Uri? = data.getData()
                    promise.resolve(uri?.toString())
                  }
                } else {
                  promise.reject("SELECT_CANCELLED", resultCode.toString())
                }
              } else {
                super.onActivityResult(activity, requestCode, resultCode, data)
              }
            }
          }
      )

      try {
        currentActivity.startActivityForResult(intent, READABLE_REQUEST_CODE)
      } catch (e: Exception) {
        promise.reject("SELECT_FAILED", e.message)
      }
    } else {
      promise.reject("INVALID_ACTIVITY", "Activity is null.")
    }
  }

  companion object {
    const val NAME = "PersistableUri"
  }
}
