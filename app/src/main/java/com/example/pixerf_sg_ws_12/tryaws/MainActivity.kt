package com.example.pixerf_sg_ws_12.tryaws

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager
import android.media.MediaSyncEvent.createEvent
import com.amazonaws.mobile.auth.ui.SignInUI
import com.amazonaws.mobile.client.AWSStartupHandler
import com.amazonaws.mobile.client.AWSStartupResult
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent
import com.amazonaws.mobileconnectors.pinpoint.analytics.monetization.AmazonMonetizationEventBuilder
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration







class MainActivity : AppCompatActivity() {

    private lateinit var pinpointManager: PinpointManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init
        //AWSMobileClient.getInstance().initialize(this).execute()

        //Pinpoint
//        val pinpointConfig = PinpointConfiguration(applicationContext,
//                AWSMobileClient.getInstance().credentialsProvider,
//                AWSMobileClient.getInstance().configuration)
//
//        pinpointManager = PinpointManager(pinpointConfig)
//
//        // Start a session with Pinpoint
//        pinpointManager.sessionClient.startSession()
//
//        // Stop the session and submit the default app started event
//        pinpointManager.sessionClient.stopSession()
//        pinpointManager.analyticsClient.submitEvents()
//
//        logEvent()
//        logMonetizationEvent()

        //Sign In
        AWSMobileClient.getInstance().initialize(this) {

            val config = AuthUIConfiguration.Builder()
                    .userPools(true)  // true? show the Email and Password UI
                    .logoResId(R.drawable.ic_launcher_foreground) // Change the logo
                    .backgroundColor(Color.BLUE) // Change the backgroundColor
                    .isBackgroundColorFullScreen(true) // Full screen backgroundColor the backgroundColor full screenff
                    .fontFamily("sans-serif-light") // Apply sans-serif-light as the global font
                    .canCancel(true)
                    .build()

            val signIn = AWSMobileClient.getInstance().getClient(this, SignInUI::class.java) as SignInUI
            signIn.login(this, NextActivity::class.java).authUIConfiguration(config).execute()
        }.execute()
    }

    fun logEvent() {
        pinpointManager.sessionClient.startSession()
        val event = pinpointManager.analyticsClient.createEvent("JKY_EventName")
                .withAttribute("DemoAttribute1", "DemoAttributeValue1")
                .withAttribute("DemoAttribute2", "DemoAttributeValue2")
                .withMetric("DemoMetric1", Math.random())

        pinpointManager.analyticsClient.recordEvent(event)
        pinpointManager.sessionClient.stopSession()
        pinpointManager.analyticsClient.submitEvents()
    }

    fun logMonetizationEvent() {
        pinpointManager.sessionClient.startSession()

        val event = AmazonMonetizationEventBuilder.create(pinpointManager.analyticsClient)
                .withFormattedItemPrice("$10.00")
                .withProductId("DEMO_PRODUCT_ID")
                .withQuantity(1.0)
                .withProductId("DEMO_TRANSACTION_ID").build()

        pinpointManager.analyticsClient.recordEvent(event)
        pinpointManager.sessionClient.stopSession()
        pinpointManager.analyticsClient.submitEvents()
    }
}