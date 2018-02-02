package drod.creatnru.gsaet.view_layer;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;

import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;


import drod.creatnru.gsaet.game_layer.Custom_R;
import drod.creatnru.gsaet.other_layer.ButtonItems;


public class Setting extends CCLayer
{
	ButtonItems on1;
	ButtonItems off1;
	ButtonItems on2;
	ButtonItems off2;
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		scene.addChild(new Setting());
		return scene;
	}
/*****************************************************************************************************************************************************************************************************************/	
	public Setting()
	{
		super();
		CCSprite im_back = CCSprite.sprite(Custom_R._getImg("setting/setting"));
		Custom_R.setScale(im_back);
		im_back.setAnchorPoint(0, 0);
		im_back.setPosition(0, 0);
		addChild(im_back);	
		
		on1 = ButtonItems.button(Custom_R._getImg("setting/onBtn"), Custom_R._getImg("setting/onBtn"),this,"setOnOff1",0);
		off1= ButtonItems.button(Custom_R._getImg("setting/off"), Custom_R._getImg("setting/off"),this,"setOnOff1",0);
		on1.setPosition(Custom_R._getX(768), Custom_R._getY(332));
		off1.setPosition(Custom_R._getX(768), Custom_R._getY(332));
		addChild(on1);
		addChild(off1);
		
		
		on2 = ButtonItems.button(Custom_R._getImg("setting/onBtn"), Custom_R._getImg("setting/onBtn"),this,"setOnOff2",0);
		off2= ButtonItems.button(Custom_R._getImg("setting/off"), Custom_R._getImg("setting/off"),this,"setOnOff2",0);
		on2.setPosition(Custom_R._getX(768), Custom_R._getY(194));
		off2.setPosition(Custom_R._getX(768), Custom_R._getY(194));
		addChild(on2);
		addChild(off2);
		
		initVisible();		
		ButtonItems back = ButtonItems.button(Custom_R._getImg("setting/backBtn"), Custom_R._getImg("setting/backBtn"),this,"back",0);
		back.setPosition(Custom_R._getX(877), Custom_R._getY(55));
		addChild(back);		
	}
/*****************************************************************************************************************************************************************************************************************/
	public void initVisible(){
		if(Custom_R.bgmState){
			on2.setVisible(true);
			off2.setVisible(false);
		}else{
			on2.setVisible(false);
			off2.setVisible(true);
		}
		
		if(Custom_R.effectState){
			on1.setVisible(true);
			off1.setVisible(false);
		}else{
			on1.setVisible(false);
			off1.setVisible(true);
		}		
		
	}
/*****************************************************************************************************************************************************************************************************************/
	public void getStateBgm(){
		if(Custom_R.bgmState){
			on2.setVisible(false);
			off2.setVisible(true);
			Custom_R.bgmState = false;
			Custom_R.pauseSound();
			Custom_R.stopSound = true;
		}else{
			on2.setVisible(true);
			off2.setVisible(false);
			Custom_R.bgmState = true;
			if(Custom_R.stopSound){
				Custom_R.resumeSound();
				Custom_R.stopSound = false;
			}else{
				Custom_R.playSound();
			}
		}
		Custom_R.saveSetting();
	}
/*****************************************************************************************************************************************************************************************************************/
	public void getStateEffect(){
		if(Custom_R.effectState){
			Custom_R.effectState = false;
			on1.setVisible(false);
			off1.setVisible(true);
		}else{
			Custom_R.effectState = true;
			on1.setVisible(true);
			off1.setVisible(false);			
		}
		Custom_R.saveSetting();
	}
/*****************************************************************************************************************************************************************************************************************/
	public void back(Object sender){	
		Custom_R.playEffect(Custom_R.click);
		Custom_R.titleState = false;
		if(Custom_R.GAME_STATE.equals("title"))
			CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, TextDataSingle.scene()));
		else
			CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, GameView.scene()));
	}
/*****************************************************************************************************************************************************************************************************************/
	public void setOnOff1(Object sender){
		Custom_R.playEffect(Custom_R.click);
		getStateEffect();
	}
/*****************************************************************************************************************************************************************************************************************/
	public void setOnOff2(Object sender){
		Custom_R.playEffect(Custom_R.click);
		getStateBgm();
	}
}