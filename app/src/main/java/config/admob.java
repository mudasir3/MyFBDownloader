package config;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.widget.LinearLayout;

public class admob {
	
	private static String admBanner = "ca-app-pub-3940256099942544/6300978111";
	public static String Interstitial = "ca-app-pub-3940256099942544/1033173712";
	
	public static void admobBannerCall(Activity acitivty , LinearLayout linerlayout){
		
        AdView adView = new AdView(acitivty);
        adView.setAdUnitId(admBanner);
        adView.setAdSize(AdSize.BANNER);
        AdRequest.Builder builder = new AdRequest.Builder();
        adView.loadAd(builder.build());
        linerlayout.addView(adView);

	}
	
}