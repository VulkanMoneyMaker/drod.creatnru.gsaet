package drod.creatnru.gsaet.view_layer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;


import drod.creatnru.gsaet.game_layer.Custom_R;
import drod.creatnru.gsaet.other_layer.ButtonItems;

public class TableItems extends CCLayer
{
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		scene.addChild(new TableItems());
		return scene;
	}
/***************************************************************************************************************************************************************************************************************/	
	public TableItems()
	{
		super();
		
		CCSprite im_back = CCSprite.sprite(Custom_R._getImg(String.format("backImages/pay_table%d-hd", Custom_R.curLevel)));
		Custom_R.setScale(im_back);
		im_back.setAnchorPoint(0, 0);
		im_back.setPosition(0, 0);
		addChild(im_back);	
		
		ButtonItems retu = ButtonItems.button(Custom_R._getImg("Buttons/return"), Custom_R._getImg("Buttons/return"),this,"returnPayTable",0);
	
		retu.setPosition(Custom_R._getX(889), Custom_R._getY(540));
		addChild(retu);
		
	}
/***************************************************************************************************************************************************************************************************************/	
	public void returnPayTable(Object sender){
		Custom_R.playEffect(Custom_R.click);
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, GameView.scene()));
	}
}