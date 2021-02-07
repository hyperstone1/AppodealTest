package com.example.myapplication213;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeAdView;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.api.App;
import com.appodeal.ads.native_ad.views.NativeAdViewAppWall;
import com.appodeal.ads.native_ad.views.NativeAdViewContentStream;
import com.appodeal.ads.utils.Log;
import com.explorestack.consent.Consent;
import com.explorestack.consent.ConsentForm;
import com.explorestack.consent.ConsentFormListener;
import com.explorestack.consent.ConsentInfoUpdateListener;
import com.explorestack.consent.ConsentManager;
import com.explorestack.consent.exception.ConsentManagerException;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements BannerCallbacks, RewardedVideoCallbacks {

    private final static String TAG = MainActivity.class.getSimpleName();

    private List<NativeAd> nativeAds = new ArrayList<>();
    /*private final int FULLSCREEN_WAS_LOADED ="Fullscreen ad was loaded";
    private final int FULLSCREEN_HAST_LOADED ="Fullscreen ad hasn't loaded";*/
    private final boolean DEFAULT_CONSENT_VALUE = false;
    private Button showBannerAd;
    private Button showNativeAd;
    private Button showRewardedAd;
    private Button showInterstitialAd;
    private List<NativeAd> showNativeAdsList;
    private ConsentForm consentForm;
    private static final String APP_KEY = "44e1d02ef129efad9c127a22acfdc227634a4f3ef182527b" ;
    private int counter = 0;
    private int counter1 = 0;
    private int counter2 = 0;
    private boolean a = false;
    private Button ButtonHide;
    // private boolean a = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (savedInstanceState != null){
        counter = savedInstanceState.getInt("Counter", 0);
        counter1 = savedInstanceState.getInt("Counter1", 0);
        counter2 = savedInstanceState.getInt("Counter2", 0);

//        a =savedInstanceState.getBoolean("button", false);
  }
//showBannerAd =(Button) getLastNonConfigurationInstance();
//if (showBannerAd == null){
//    showBannerAd = new Button();
//    showBannerAd return;
//}

        List<NativeAd> nativeAds = new ArrayList<>();
        Appodeal.setTesting(true);
        Appodeal.setAutoCache(Appodeal.NATIVE, false);
        Appodeal.initialize(this, "44e1d02ef129efad9c127a22acfdc227634a4f3ef182527b",  Appodeal.NATIVE , true);
        setNativeAD();

        InitViews();
        InitListeners();
        configureAmpedSDKS();
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Counter", counter);

        savedInstanceState.putInt("Counter1", counter1);
        savedInstanceState.putInt("Counter2", counter2);
       // savedInstanceState.putBoolean("button", a);



    }

//    @Nullable
//    @Override
//    public Object onRetainCustomNonConfigurationInstance() {
//        return showBannerAd;
//    }

    private void InitListeners() {


        showBannerAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (Appodeal.show(MainActivity.this, Appodeal.BANNER_TOP, "ban"));{
//                Appodeal.hide(MainActivity.this, Appodeal.NATIVE);

                counter++;}

                if (counter >= 5) {
                    showBannerAd.setEnabled(false);
                Appodeal.hide(MainActivity.this, Appodeal.BANNER_TOP);
            }
            }
        });


        showInterstitialAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
                    if (Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL, "interstitials"))
                        showInterstitialAd.setEnabled(false);
                    Appodeal.hide(MainActivity.this, Appodeal.BANNER_TOP);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showInterstitialAd.setEnabled(true);
                    }


                }, 60000);
            }

        });


        //setTimeout(anInstance() {showBannerAd.setEnabled(true)}, 1000);


        ;

        showRewardedAd.setOnClickListener(v -> {
                    counter1++;
                    if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {


                        Appodeal.show(MainActivity.this, Appodeal.REWARDED_VIDEO, "rewardedVideo");

                        if (counter1 >= 3) {
                            showRewardedAd.setEnabled(false);
                        }
                    }
                    Appodeal.hide(this, Appodeal.BANNER_TOP);
                }
        );



        showNativeAd.setOnClickListener(v -> {

            counter2++;

                Appodeal.cache(this, Appodeal.NATIVE, 1);
                nativeAds = Appodeal.getNativeAds(1);
                Appodeal.getAvailableNativeAdsCount();
                RelativeLayout holder = (RelativeLayout) findViewById(R.id.native_holder);
                NativeAdViewContentStream nativeAdView = new NativeAdViewContentStream(MainActivity.this, nativeAds.get(0));
                holder.addView(nativeAdView);
               // Appodeal.hide(this, Appodeal.NATIVE);
            if(counter2 >=3 ){showNativeAd.setEnabled(false); }
                Toast.makeText(MainActivity.this, "onNativeLoaded!", Toast.LENGTH_SHORT).show();
                Appodeal.hide(this, Appodeal.BANNER_TOP);

       });
    }

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }}).start();

        }

    private void setNativeAD(){

        Appodeal.setAutoCache(Appodeal.NATIVE,false);
        Appodeal.cache(this, Appodeal.NATIVE);
        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                Toast.makeText(MainActivity.this, "onNativeLoaded!", Toast.LENGTH_SHORT).show();
                }


            @Override
            public void onNativeFailedToLoad() {
                Toast.makeText(MainActivity.this, "onNativeFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {
                Toast.makeText(MainActivity.this, "onNativeShown", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNativeShowFailed(NativeAd nativeAd) {

            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {
                Toast.makeText(MainActivity.this, "onNativeClicked", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNativeExpired() {

            }
        });

    }
    private void configureAmpedSDKS(){
        Appodeal.setLogLevel(Log.LogLevel.verbose);
        Appodeal.setTesting(true);
        Appodeal.setBannerCallbacks(this);
        Appodeal.setRewardedVideoCallbacks(this);

        initializeAppeal(true);
    }

    private void initializeAppeal(boolean hasConsent) {
        int Ad_types = Appodeal.REWARDED_VIDEO | Appodeal.BANNER |Appodeal.INTERSTITIAL | Appodeal.NATIVE;
        Appodeal.initialize(this, APP_KEY, Ad_types, hasConsent);
    }

    @Override
    public void onBannerLoaded(int i, boolean b) {
        android.util.Log.d(TAG, "onBannerLoaded: ");
    }

    @Override
    public void onBannerFailedToLoad() {
        android.util.Log.d(TAG, "onBannerFailedToLoad: ");
    }

    @Override
    public void onBannerShown() {
        android.util.Log.d(TAG, "onBannerShown: ");
    }

    @Override
    public void onBannerShowFailed() {
        android.util.Log.d(TAG, "onBannerShowFailed: ");
    }

    @Override
    public void onBannerClicked() {
        android.util.Log.d(TAG, "onBannerClicked: ");

    }

    @Override
    public void onBannerExpired() {
        android.util.Log.d(TAG, "onBannerExpired: ");
    }

    @Override
    public void onRewardedVideoLoaded(boolean b) {
        android.util.Log.d(TAG, "onRewardedVideoLoaded: ");
    }

    @Override
    public void onRewardedVideoFailedToLoad() {
        android.util.Log.d(TAG, "onRewardedVideoFailedToLoad: ");
    }

    @Override
    public void onRewardedVideoShown() {
        android.util.Log.d(TAG, "onRewardedVideoShown: ");
    }

    @Override
    public void onRewardedVideoShowFailed() {
        android.util.Log.d(TAG, "onRewardedVideoShowFailed: ");
    }

    @Override
    public void onRewardedVideoFinished(double v, String s) {
        android.util.Log.d(TAG, "onRewardedVideoFinished: reward =" + v + s);
    }

    @Override
    public void onRewardedVideoClosed(boolean b) {
        android.util.Log.d(TAG, "onRewardedVideoClosed: " + b);
    }

    @Override
    public void onRewardedVideoExpired() {
        android.util.Log.d(TAG, "onRewardedVideoExpired: ");
    }

    @Override
    public void onRewardedVideoClicked() {
        android.util.Log.d(TAG, "onRewardedVideoClicked: ");
    }
    private void checkConsentZone() {
        ConsentManager consentManager = ConsentManager.getInstance(this);
        consentManager.requestConsentInfoUpdate(
                APP_KEY,
                new ConsentInfoUpdateListener() {
                    @Override
                    public void onConsentInfoUpdated(Consent consent) {
                        Consent.ShouldShow consentShouldShow =
                                consentManager.shouldShowConsentDialog();
                        android.util.Log.d(TAG,"onConsentInfoUpdated: consentShouldShow" + consentShouldShow.name());
                        android.util.Log.d(TAG,"onConsentInfoUpdated: consentValue" + consent.getStatus().name());
                        if (consentShouldShow == Consent.ShouldShow.TRUE){
                            buildConsentDialog();
                        } else{
                            if(consent.getStatus() == Consent.Status.UNKNOWN){
                                initializeAppeal(DEFAULT_CONSENT_VALUE);

                            } else{
                                boolean hasConsent = consent.getStatus() == Consent.Status.PERSONALIZED;
                                initializeAppeal(hasConsent);
                            }
                        }


                    }

                    @Override
                    public void onFailedToUpdateConsentInfo(ConsentManagerException exception) {
                        android.util.Log.e(TAG,"onFailedToUpdateConsentInfo: " + exception.getReason());
                        initializeAppeal(DEFAULT_CONSENT_VALUE);

                    }
                }
        );
    }

    private void buildConsentDialog() {
        consentForm = new ConsentForm.Builder(this)
                .withListener(
                        new  ConsentFormListener(){
                            @Override
                            public void onConsentFormLoaded(){
                                android.util.Log.d(TAG,"on consentFormLoaded");
                                consentForm.showAsActivity();
                            }
                            @Override
                            public void onConsentFormOpened(){
                                android.util.Log.d(TAG, "onConsentFormOpened");
                            }

                            @Override
                            public void onConsentFormClosed(Consent consent){
                                boolean hasConsent = consent.getStatus() == Consent.Status.PERSONALIZED;
                                initializeAppeal((hasConsent));
                                android.util.Log.d(TAG, "onConsentFormClosed");
                            }

                            @Override
                            public void onConsentFormError(ConsentManagerException exception){
                                android.util.Log.d(TAG, "on ConsentFormError" + exception.getReason());
                            }
                        }
                ).build();
        consentForm.load();
    }


    private void InitViews(){
        ///showNativeAdsList= findViewById(R.id.show_native_ads);
        ButtonHide = findViewById(R.id.buttonHide);
        showBannerAd = findViewById(R.id.show_banner);
        showNativeAd = findViewById(R.id.show_native);
        showRewardedAd = findViewById(R.id.show_rewarded);
        showInterstitialAd = findViewById(R.id.show_interstitial);

        Appodeal.disableLocationPermissionCheck();
        Appodeal.disableWriteExternalStoragePermissionCheck();
    }


}