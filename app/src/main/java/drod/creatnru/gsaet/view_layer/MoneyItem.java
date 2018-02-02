package drod.creatnru.gsaet.view_layer;

///import org.cocos2d.nodes.CCDirector;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;

import android.content.Intent;
import android.net.Uri;

import com.google.ads.InterstitialAd;

import drod.creatnru.gsaet.game_layer.Custom_R;
import drod.creatnru.gsaet.other_layer.ButtonItems;

import drod.creatnru.gsaet.R;

public class MoneyItem extends CCLayer {
	public int coinCount = 0;
	
	private static long lastTime = 0;

	// Admob
	private InterstitialAd interstitial;


	public static CCScene scene() {
		CCScene scene = CCScene.node();
		scene.addChild(new MoneyItem());
		return scene;
	}

	/*****************************************************************************************************************************************************************************************************************/
	public MoneyItem() {
		super();
		schedule("getInfo", 1.0f / 10.0f);
	}

	public void getInfo(float dt) {
		unschedule("getInfo");
		CCSprite img_back = CCSprite.sprite(Custom_R._getImg("setting/coinSetting"));
		Custom_R.setScale(img_back);
		img_back.setAnchorPoint(0, 0);
		img_back.setPosition(0, 0);
		addChild(img_back);

		ButtonItems buyBtn = ButtonItems.button(Custom_R._getImg("setting/buyBtn"),
				Custom_R._getImg("setting/buyBtn"), this, "coinBuy", 0);

		buyBtn.setPosition(Custom_R._getX(717), Custom_R._getY(320));
		addChild(buyBtn);

		ButtonItems backBtn = ButtonItems.button(Custom_R._getImg("setting/PlusBack"),
				Custom_R._getImg("setting/PlusBack"), this, "backLayer", 0);
		// backBtn.setColor(new ccColor3b(0,0,0));
		backBtn.setPosition(Custom_R._getX(900), Custom_R._getY(50));
		addChild(backBtn);
	}

	/*****************************************************************************************************************************************************************************************************************/
	public void coinBuy(Object sender) {

//		if (VunglePub.isVideoAvailable(true))
//			VunglePub.displayIncentivizedAdvert(true);

//		admobInterstitial();
		
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(Custom_R.g_Context.getResources().getString(R.string.more_apps_hammyliem)));
		Custom_R.g_Context.startActivity(i);

		long currentTime = System.currentTimeMillis();
		if ((currentTime - lastTime) > 15 * 60 * 1000) {
			Custom_R.allCoin += 1000;
			Custom_R.saveSetting();
			lastTime = currentTime;
		}

	}
	
	/*****************************************************************************************************************************************************************************************************************/
	public void backLayer(Object sender) {
		Custom_R.playEffect(Custom_R.click);
		if (Custom_R.GAME_STATE.equals("title")) {
			Custom_R.titleState = false;
			CCDirector.sharedDirector().replaceScene(
					CCFadeTransition.transition(0.7f, TextDataSingle.scene()));
		} else if (Custom_R.GAME_STATE.equals("game")) {
			CCDirector.sharedDirector().replaceScene(
					CCFadeTransition.transition(0.7f, GameView.scene()));
		}
	}

}
