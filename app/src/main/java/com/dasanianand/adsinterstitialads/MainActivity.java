package com.dasanianand.adsinterstitialads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private AdView bannerAdView;
    public static String TAG = "MyAds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        //show the interstitial ads when going to next screen
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //show the ad
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");

                    //if ad is not loaded then start second activity (No other option)
                    startActivity(new Intent(MainActivity.this, SecondActivity.class)); //Opening Second Activity
                }
            }
        });
    }

    private void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.interstitial_video_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");

                        // --------------------------------------------------------
                        //OnAdLoaded Successfully the full screen content call back
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d(TAG, "The ad was dismissed.");

                                //Here comes the main code that what to do after ad is shown successfully
                                startActivity(new Intent(MainActivity.this, SecondActivity.class)); //Opening Second Activity
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d(TAG, "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d(TAG, "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;

                        //reload the ad after some time (5 sec) of waiting if Ad failed to load
                        new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.d(TAG, millisUntilFinished / 1000 + " Sec");
                            }

                            @Override
                            public void onFinish() {
                                //reload the ad
                                loadInterstitialAd();
                            }
                        }.start();
                    }
                });
    }

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView = findViewById(R.id.adViewBanner);
        bannerAdView.loadAd(adRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResumed");

        //Initializing Here because everytime coming back to 1st activity and going to again next activity the ad must be shown
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Load Banner Ads
                loadBannerAd();

                //Load Interstitial Ads
                Log.d(TAG, "Loading Interstitial ad...");
                loadInterstitialAd();
            }
        });
    }
}

/*
LOGS

2022-02-19 14:53:38.293 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: onCreate
2022-02-19 14:53:38.304 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: onResumed Called
2022-02-19 14:53:39.537 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: Loading Interstitial ad...
2022-02-19 14:53:42.606 19941-19941/com.dasanianand.adsinterstitialads I/MyAds: onAdLoaded
2022-02-19 14:56:06.206 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: The ad was shown.
2022-02-19 14:56:52.595 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: The ad was dismissed.
2022-02-19 14:57:16.392 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: onResumed Called
2022-02-19 14:57:16.593 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: Loading Interstitial ad...
2022-02-19 14:57:19.523 19941-19941/com.dasanianand.adsinterstitialads I/MyAds: onAdLoaded
2022-02-19 14:57:27.966 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: The ad was shown.
2022-02-19 14:57:49.761 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: The ad was dismissed.
2022-02-19 14:57:50.563 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: onResumed Called
2022-02-19 14:57:50.655 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: Loading Interstitial ad...
2022-02-19 14:57:51.078 19941-19941/com.dasanianand.adsinterstitialads I/MyAds: Error while connecting to ad server: Unable to resolve host "googleads.g.doubleclick.net": No address associated with hostname
2022-02-19 14:57:51.081 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 4 Sec
2022-02-19 14:57:52.084 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 3 Sec
2022-02-19 14:57:53.085 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 2 Sec
2022-02-19 14:57:54.086 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 1 Sec
2022-02-19 14:57:55.087 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 0 Sec
2022-02-19 14:57:56.282 19941-19941/com.dasanianand.adsinterstitialads I/MyAds: Error while connecting to ad server: Unable to resolve host "googleads.g.doubleclick.net": No address associated with hostname
2022-02-19 14:57:56.282 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 4 Sec
2022-02-19 14:57:57.284 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 3 Sec
2022-02-19 14:57:58.285 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 2 Sec
2022-02-19 14:57:59.288 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 1 Sec
2022-02-19 14:58:00.289 19941-19941/com.dasanianand.adsinterstitialads D/MyAds: 0 Sec
2022-02-19 14:58:03.138 19941-19941/com.dasanianand.adsinterstitialads I/MyAds: onAdLoaded

 */
