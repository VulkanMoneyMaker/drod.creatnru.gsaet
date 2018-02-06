package spoty.game.machine.view_layer;


import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenuItem;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.nodes.CCLabel;

import spoty.game.machine.game_layer.Custom_R;
import spoty.game.machine.other_layer.ButtonItems;


public class TextDataSingle extends CCLayer
{

	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		scene.addChild(new TextDataSingle());
		return scene;
	}
/***************************************************************************************************************************************************************************************************************/
	public TextDataSingle()
	{
		super();
		
		schedule("getInfo", 1.0f / 10.0f);
	}	
	public void getInfo(float dt){
		unschedule("getInfo");
		createBG();
		createButton();
		createLabel();	
		getTime();
		
	}
	public void getTime(){
		 if(Custom_R.allCoin == 0){
			 if(Custom_R.setTimeState){
				 long time = System.currentTimeMillis() / 1000;
				 if((time - Custom_R.currentTime) / 3600 >= 24){
					 Custom_R.allCoin = 250;
					 Custom_R.setTimeState = false;
					 Custom_R.saveSetting();
				 }
			 }			 
		 }		
	}
/***************************************************************************************************************************************************************************************************************/
	public void createBG(){
		CCSprite im_back = CCSprite.sprite(Custom_R._getImg("backImages/menu_bg-hd"));
		Custom_R.setScale(im_back);
		im_back.setAnchorPoint(0, 0);
		im_back.setPosition(0, 0);
		addChild(im_back);
	}
/***************************************************************************************************************************************************************************************************************/
	public void createButton(){
		String [] str = {"Buttons/zombies","Buttons/pirates","Buttons/jewels","Buttons/fruit","Buttons/cash","Buttons/dragons"};
		ButtonItems selectBtn;
		for(int i = 0 ; i < 6 ; i++){
			if (i == 4) {
				selectBtn = ButtonItems.button(Custom_R._getImg(str[i]), Custom_R._getImg(str[i]),this,"startGame",(i+1));
				float fx = 900;
				float fy = 350;
				selectBtn.setAnchorPoint(0, 0);
				selectBtn.setPosition(fx, fy);
				addChild(selectBtn);
			}

		}
		
		CCSprite img_txt = CCSprite.sprite(Custom_R._getImg("Buttons/text_box"));
		Custom_R.setScale(img_txt);
		img_txt.setAnchorPoint(0, 0);
		img_txt.setPosition(Custom_R._getX(52), Custom_R._getY(564));
		addChild(img_txt);
		
		
		CCSprite img_usd = CCSprite.sprite(Custom_R._getImg("Buttons/usd3"));
		Custom_R.setScale(img_usd);
		img_usd.setAnchorPoint(0, 0);
		img_usd.setPosition(Custom_R._getX(40), Custom_R._getY(564));
		addChild(img_usd);		
		
		ButtonItems plus = ButtonItems.button(Custom_R._getImg("Buttons/plus1"), Custom_R._getImg("Buttons/plus2"),this,"plusCoin",0);
		plus.setAnchorPoint(0, 0);
		plus.setPosition(Custom_R._getX(288), Custom_R._getY(597));
		addChild(plus);
		
		ButtonItems setting = ButtonItems.button(Custom_R._getImg("Buttons/setting1"), Custom_R._getImg("Buttons/setting1"), this, "setting",0);
		setting.setAnchorPoint(0, 0);
		setting.setPosition(Custom_R._getX(100), Custom_R._getY(38));
		addChild(setting);
	}
/***************************************************************************************************************************************************************************************************************/
	public void createLabel(){
		ccColor3B clr = ccColor3B.ccc3(255, 255, 255);
		CCLabel coinLabel = CCLabel.makeLabel(String.format("%d", Custom_R.allCoin), Custom_R._getFont("Imagica"), 30);
		Custom_R.setScale(coinLabel);
		coinLabel.setAnchorPoint(0, 0);
		coinLabel.setPosition(Custom_R._getX(160), Custom_R._getY(580));
		coinLabel.setColor(clr);
		addChild(coinLabel);	
			
	}
/***************************************************************************************************************************************************************************************************************/
	public void startGame(Object sender) {
		Custom_R.playEffect(Custom_R.click);
		Custom_R.titleState = true;
		Custom_R.curLevel = ((CCMenuItem)sender).getTag();
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, GameView.scene()));
		
	}
/***************************************************************************************************************************************************************************************************************/
	public void plusCoin(Object sender) {
		Custom_R.playEffect(Custom_R.click);
		Custom_R.GAME_STATE = "title";
		Custom_R.titleState = true;
		
		//	
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, MoneyItem.scene()));
		
	}
/***************************************************************************************************************************************************************************************************************/
	public void setting(Object sender){
		Custom_R.playEffect(Custom_R.click);
		Custom_R.titleState = true;
		Custom_R.GAME_STATE = "title";
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, Setting.scene()));
	}
/***************************************************************************************************************************************************************************************************************/
	public void moreGame(Object sender){
		Custom_R.playEffect(Custom_R.click);
	}
	
	
}