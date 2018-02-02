package drod.creatnru.gsaet.view_layer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;

import drod.creatnru.gsaet.game_layer.Custom_R;

public class LayerViewLogo extends CCLayer
{
	public static CCScene scene(){		
		Custom_R.setScale();
		Custom_R.loadSetting();
		CCScene scene = CCScene.node();
		scene.addChild(new LayerViewLogo());
		return scene;
	}
/***************************************************************************************************************************************************************************************************************/
	public LayerViewLogo()
	{
		super();		
		CCSprite sprite = CCSprite.sprite(Custom_R._getImg("backImages/splash-hd"));
		Custom_R.setScale(sprite);
		sprite.setAnchorPoint(0, 0);
		sprite.setPosition(0, 0);
		addChild(sprite);
		schedule("logoTimer", 1);
	}

/***************************************************************************************************************************************************************************************************************/
	public void logoTimer(float dt)
	{
		unschedule("logoTimer");
		Custom_R.playSound();
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.5f, TextDataSingle.scene()));
	}
}