package spoty.game.machine.view_layer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;

import android.app.AlertDialog;
import android.content.DialogInterface;

import spoty.game.machine.game_layer.Custom_R;

import spoty.game.machine.other_layer.ButtonItems;

import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.ccColor3B;

public class GameView extends CCLayer
{
	
	public final int	      z_backgraound = 0;
	public final int	      z_frame       = 1;
	public final int	      z_particle    = 2;
	public final int	      z_button      = 3;
	public final int	      z_label       = 4;
	public final int	      z_hold        = 5;
	public final int          z_coin        = 6;
	public final int	      tag_Frame     = 1;
	public final int	      tag_coin      = 2;
	
	public SettingEngine m_Eng;
	public CCSprite []        m_sprCharacter = new CCSprite[Custom_R.CHARACTER_COUNT];
	public CCSprite           sprLine;
	public Vector<SpinAnimation>   arrCoin = new Vector<SpinAnimation>();
	public CCSprite[]         m_arrLine = new CCSprite[9];
	public CCSprite[]         m_arrHold = new CCSprite[5];	
 	public CCSprite[][]       m_arrLocked = new CCSprite[4][2];
 	public Vector<CCSprite>   m_arrRect = new Vector<CCSprite>();
	
	public CCLabel            m_lblCoin;
	public CCLabel            m_lblLines;
	public CCLabel            m_lblBets;
	public CCLabel            m_lblMaxLines;
	public CCLabel            m_lblWin;		
	
	public int                m_nTouchCol;
	public int                coinCount     = 0;
	public int                curRctCount   = 0;
    public int                m_nSlotTick;	
	public int                m_nFrameCount;
	public int                m_nCurScore;
	public int                m_nOldScore;
	public int                m_nIncrease;
	
	public float              m_nSTPointY;
	public float              lastPosX = Custom_R._getX(319);
	public float              lastPosY = Custom_R._getY(534);
	
	public boolean            m_bDrawRuleLine;   
	
	public boolean            m_bIsAnim;
	
	public boolean            m_bIncrease;
	public boolean            m_ArrRectState;
	 
 	
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		scene.addChild(new GameView());
		return scene;
	}
/***************************************************CONSTRACTOR*******************************************************************************************************************************************************/	
	public GameView()
	{
		super();	
		
		m_Eng = new SettingEngine();
		initVariables();
		initImages();
		initButton();
		initLabels();
		drawLine(true);	
		schedule("onTime", 1.0f/60.0f);
	}
/***************************************************VALUE FORMART*******************************************************************************************************************************************************/
	public void initVariables(){
		isTouchEnabled_= true;
		m_nSlotTick = -1;				
		m_Eng.setCardBettwenY(Custom_R.CARD_BETWEEN_Y);
		
	}
/***************************************************LOAD IMAGES*******************************************************************************************************************************************************/
	public void initImages(){
		int nCurStage = Custom_R.curLevel;
		CCSprite im_back = CCSprite.sprite(Custom_R._getImg(String.format("backImages/game_bg%d-hd",nCurStage)));
		Custom_R.setScale(im_back);
		im_back.setAnchorPoint(0, 0);
		im_back.setPosition(0, 0);
		addChild(im_back);
		
		for(int i = 0; i < Custom_R.CHARACTER_COUNT ; i++){
			m_sprCharacter[i] = CCSprite.sprite(Custom_R._getImg(String.format("character/stage%d/%s",  Custom_R.curLevel, Custom_R.strIconName[i])));
			Custom_R.setScale(m_sprCharacter[i]);
			m_sprCharacter[i].setAnchorPoint(0, 0);
		}
		for(int i = 0; i < Custom_R.RULE_LINE_COUNT ; i++){
			m_arrLine[i] = CCSprite.sprite(Custom_R._getImg(String.format("lines/line%d", i + 1)));
			Custom_R.setScale(m_arrLine[i]);
			m_arrLine[i].setAnchorPoint(0, 0);
			addChild(m_arrLine[i]);
			m_arrLine[i].setPosition(Custom_R._getX(Custom_R.lineX), Custom_R._getY(Custom_R.lineY[i]));
			m_arrLine[i].setVisible(false);			
		}
		for(int i = 0; i < Custom_R.COL_ ; i++){
			m_arrHold[i] = CCSprite.sprite(Custom_R._getImg("Buttons/hold"));
			Custom_R.setScale(m_arrHold[i]);
			m_arrHold[i].setAnchorPoint(0, 0);
			m_arrHold[i].setPosition(Custom_R._getX(116 + i * 146), Custom_R._getY(125));
			this.addChild(m_arrHold[i],z_hold);		
		}
	}
/***************************************************BUTTONS LOAD*******************************************************************************************************************************************************/
	public void initButton(){
		ButtonItems back = ButtonItems.button(Custom_R._getImg("Buttons/back1"), Custom_R._getImg("Buttons/back2"),this,"onBack",0);
		ButtonItems coin = ButtonItems.button(Custom_R._getImg("Buttons/addCoin1"), Custom_R._getImg("Buttons/addCoin2"),this,"onCoinBuy",0);
		//ButtonItems paytable = ButtonItems.button(Custom_R._getImg("Buttons/paytable1"), Custom_R._getImg("Buttons/paytable2"),this,"onPlayTable",0);
		ButtonItems line = ButtonItems.button(Custom_R._getImg("Buttons/line1"), Custom_R._getImg("Buttons/line2"),this,"onLines",0);
		ButtonItems maxline = ButtonItems.button(Custom_R._getImg("Buttons/maxlines1"), Custom_R._getImg("Buttons/maxlines2"),this,"onMaxLines",0);
		ButtonItems bet = ButtonItems.button(Custom_R._getImg("Buttons/bet1"), Custom_R._getImg("Buttons/bet2"),this,"onBet",0);
		ButtonItems spin = ButtonItems.button(Custom_R._getImg("Buttons/spin1"), Custom_R._getImg("Buttons/spin1"),this,"onSpin",0);
		ButtonItems setting = ButtonItems.button(Custom_R._getImg("Buttons/setting1"), Custom_R._getImg("Buttons/setting2"), this, "setting",0);
		setting.setAnchorPoint(0, 0);
		setting.setPosition(Custom_R._getX(50), Custom_R._getY(586));
		
		back.setPosition(Custom_R._getX(908), Custom_R._getY(602));
		coin.setPosition(Custom_R._getX(76), Custom_R._getY(58));
		//paytable.setPosition(Custom_R._getX(76),Custom_R._getY(58));
		if(Custom_R.curLevel == 1 || Custom_R.curLevel == 3){
			line.setPosition(Custom_R._getX(232), Custom_R._getY(34));
			maxline.setPosition(Custom_R._getX(431), Custom_R._getY(34));
			bet.setPosition(Custom_R._getX(630), Custom_R._getY(34));
			spin.setPosition(Custom_R._getX(908), Custom_R._getY(55));
		}else if(Custom_R.curLevel == 2){
			line.setPosition(Custom_R._getX(255), Custom_R._getY(34));
			maxline.setPosition(Custom_R._getX(436), Custom_R._getY(34));
			bet.setPosition(Custom_R._getX(612), Custom_R._getY(34));
			spin.setPosition(Custom_R._getX(908), Custom_R._getY(55));
		}else if(Custom_R.curLevel == 4){
			line.setPosition(Custom_R._getX(254), Custom_R._getY(34));
			maxline.setPosition(Custom_R._getX(427), Custom_R._getY(34));
			bet.setPosition(Custom_R._getX(610), Custom_R._getY(34));
			spin.setPosition(Custom_R._getX(860), Custom_R._getY(55));
		}else if(Custom_R.curLevel == 5){
			line.setPosition(Custom_R._getX(252), Custom_R._getY(34));
			maxline.setPosition(Custom_R._getX(427), Custom_R._getY(34));
			bet.setPosition(Custom_R._getX(609), Custom_R._getY(34));
			spin.setPosition(Custom_R._getX(870), Custom_R._getY(55));
		}else if(Custom_R.curLevel == 6){
			line.setPosition(Custom_R._getX(252), Custom_R._getY(34));
			maxline.setPosition(Custom_R._getX(431), Custom_R._getY(34));
			bet.setPosition(Custom_R._getX(610), Custom_R._getY(34));
			spin.setPosition(Custom_R._getX(860), Custom_R._getY(55));
		}
		addChild(back);
		addChild(setting);
		addChild(coin);
		//addChild(paytable);	
		addChild(line);	
		addChild(maxline);
		addChild(bet);
		addChild(spin);
	}
/*********************************************************************************LABELS LOAD**************************************************************************************************************************/
	public void initLabels(){
		ccColor3B clr = ccColor3B.ccc3(255, 255, 255);
		m_lblCoin = CCLabel.makeLabel(String.format("%d", m_Eng.m_nGameCoin), Custom_R._getFont("Imagica"), 40);
		Custom_R.setScale(m_lblCoin);
		m_lblCoin.setAnchorPoint(0, 0);
		m_lblCoin.setPosition(Custom_R._getX(200), Custom_R._getY(544));
		m_lblCoin.setColor(clr);
		addChild(m_lblCoin);
		
		m_lblLines = CCLabel.makeLabel(String.format("%d", m_Eng.m_nRuleLineCount), Custom_R._getFont("Imagica"), 30);
		Custom_R.setScale(m_lblLines);
		m_lblLines.setAnchorPoint(0, 0);
		m_lblLines.setPosition(Custom_R._getX(225), Custom_R._getY(65));
		m_lblLines.setColor(clr);
		addChild(m_lblLines);
		
		m_lblMaxLines = CCLabel.makeLabel(String.format("%d", m_Eng.m_nMaxLineCount), Custom_R._getFont("Imagica"), 30);
		Custom_R.setScale(m_lblMaxLines);
		m_lblMaxLines.setAnchorPoint(0, 0);
		m_lblMaxLines.setPosition(Custom_R._getX(421), Custom_R._getY(65));
		m_lblMaxLines.setColor(clr);
		addChild(m_lblMaxLines);
		
		m_lblBets = CCLabel.makeLabel(String.format("%d",m_Eng.m_nBet), Custom_R._getFont("Imagica"), 30);
		Custom_R.setScale(m_lblBets);
		m_lblBets.setAnchorPoint(0, 0);
		m_lblBets.setPosition(Custom_R._getX(615), Custom_R._getY(65));
		m_lblBets.setColor(clr);
		addChild(m_lblBets);
		
		m_lblWin = CCLabel.makeLabel(String.format("%d",m_Eng.m_nWin), Custom_R._getFont("Imagica"), 30);
		Custom_R.setScale(m_lblWin);
		m_lblWin.setAnchorPoint(0, 0);
		if(Custom_R.curLevel == 4 || Custom_R.curLevel == 5 || Custom_R.curLevel ==6)
			m_lblWin.setPosition(Custom_R._getX(760), Custom_R._getY(25));
		else
			m_lblWin.setPosition(Custom_R._getX(800), Custom_R._getY(25));
		m_lblWin.setColor(clr);
		addChild(m_lblWin);			
	}
/*********************************************************************************SCHEDULE**************************************************************************************************************************/
	public void onTime(float dt){			
		controlSlot();
	}
	
/*********************************************************************************DRAW RECTS**************************************************************************************************************************/
	public void effectRect(float dt){		
		for(int i = 0 ; i < m_arrRect.size() ; i++){			
			if( m_arrRect.get(i).getVisible())
				m_arrRect.get(i).setVisible(false);
			else
				m_arrRect.get(i).setVisible(true);
				
		}		
	}
/*********************************************************************************START SLOT**************************************************************************************************************************/
	public void startSlot(){
		if(m_Eng.m_bStartSlot)
			return;
		if(m_Eng.m_nGameCoin < m_Eng.m_nBet * m_Eng.m_nRuleLineCount){
			showAlert();
			return;
		}
		m_Eng.m_nWin = 0;
		m_lblWin.setString(String.format("%d", m_Eng.m_nWin));
		if(m_bIsAnim){
			m_bIsAnim = false;
		}
		if(m_ArrRectState){
			for(int i = 0 ; i < m_arrRect.size() ; i++){
				m_arrRect.get(i).setVisible(false);
			}			
			unschedule("effectRect");
			m_arrRect.clear();			
			m_ArrRectState = false;
		}
		m_nSlotTick = 0;
		Custom_R.TGameResult.clear();
		drawLine(false);
		for(int i = 0; i < Custom_R.COL_ ; i++)
			m_Eng.m_bSloting[i] = true;		
	}
/*********************************************************************************SLOT CONTROL**************************************************************************************************************************/
	public void controlSlot(){		
		m_Eng.processSlot(m_nSlotTick);
		if(m_Eng.isStopAllSlots()){			
			if(m_Eng.m_bStartSlot){	
				m_Eng.m_bStartSlot = false;
				schedule("compareCards", 0.5f);
			}
		}else
			m_Eng.m_bStartSlot = true;		
		if(m_nSlotTick > -1){
			m_nSlotTick++;
			if(m_nSlotTick > 2 * Custom_R.TICK){
				int nCols = m_nSlotTick / Custom_R.TICK -1;
				if(m_nSlotTick % 10 == 0){
					if(nCols < 6)
						m_Eng.m_strState[nCols - 1] = "last";
					else if(m_Eng.m_bSloting[Custom_R.COL_ - 1] == false)
						m_nSlotTick = -1;
				}
				
			}
		}
	}
/*************************************************************************************************************************************************************************************************************************/
	public void changeLabel(float dt){
		m_nOldScore += m_nIncrease;
		if(m_nOldScore >= m_nCurScore){
			m_nOldScore  = m_nCurScore;
			m_lblCoin.setString(String.format("%d", m_nCurScore));
			m_bIncrease = false;
			unschedule("changeLabel");			
		}else
			m_lblCoin.setString(String.format("%d", m_nOldScore));
	}
/*********************************************************************************COMPARE CARDS**************************************************************************************************************************/	
	public void compareCards(float dt){		
		float nCardX = Custom_R.CARD_STRT_X ;
	    float nCardY = Custom_R.CARD_STRT_Y;
	    float nCardBetweenX = Custom_R.CARD_BETWEEN_X;
	    float nCardBetweenY = Custom_R.CARD_BETWEEN_Y;
	    unschedule("compareCards");
	    int nPrevCoin = m_Eng.m_nGameCoin;
	    m_Eng.compareCards();
	    if(m_Eng.m_bHit) {	  	    	
	    	schedule("coinAnim", 1.0f / 15.0f);
	    }
	    m_lblWin.setString(String.format("%d", m_Eng.m_nWin));
	    if(m_Eng.m_nGameCoin > nPrevCoin){
	    	m_bIncrease = true;
	    	m_nCurScore = m_Eng.m_nGameCoin;
	    	m_nOldScore = nPrevCoin;
	    	if(m_nCurScore - m_nOldScore >= 5000){
	    		m_nIncrease = 1253;
	    	}else if(m_nCurScore - m_nOldScore < 5000 && m_nCurScore - m_nOldScore >= 1000){
				m_nIncrease = 125;
			}else if(m_nCurScore - m_nOldScore < 1000 && m_nCurScore - m_nOldScore >= 100){
				m_nIncrease = 15;
			}else{
				m_nIncrease = 2;
			}
	    	schedule("changeLabel", 1.0f / 60.0f);	    	
	    }else
	    	m_lblCoin.setString(String.format("%d", m_Eng.m_nGameCoin));
	    if(m_Eng.isStopAllSlots()) {
	        if( (Custom_R.TGameResult.size() > 0) && !m_bIsAnim ){
	            m_bIsAnim= true;
	           for(int i = 0; i < Custom_R.TGameResult.size() ; i++){
	        	   int nRuleIndex = Custom_R.TGameResult.get(i).nRuleLineIndex;
	        	   int nEqualCount = Custom_R.TGameResult.get(i).nEqualCount;
	        	   //int nCharIndex = Custom_R.TGameResult.get(i).nCharacterIndex;
	        	   m_arrLine[nRuleIndex].setVisible(true);
	        	   for(int j = 0 ; j < nEqualCount ; j++){
	        		   CGPoint ptCardPos = new CGPoint();
	        		   ptCardPos.set(Custom_R._getX(nCardX + m_Eng.nArrRules[nRuleIndex][j][1] * nCardBetweenX),
	        				   Custom_R._getY(nCardY - (m_Eng.nArrRules[nRuleIndex][j][0] - 1) * nCardBetweenY));
	        		   CCSprite rect = CCSprite.sprite(Custom_R._getImg("Buttons/rect"));
	        	       Custom_R.setScale(rect);
	        		   rect.setAnchorPoint(0,0);
	        		   rect.setPosition(ptCardPos.x, ptCardPos.y);
	        		   addChild(rect, z_frame, tag_Frame);	        		  
	        		   m_arrRect.add(rect);    
	        		   m_ArrRectState = true;
	        	   }	        	   
	           }            
	        }
	    }	
	    if(m_ArrRectState)
	    	schedule("effectRect", 0.5f);
	    	
	}	
/***********************************************************************************************************************************************************************************************************/	
	@Override public void draw(GL10 gl) {		
		drawCharacters(gl);
		drawHolds();
	}
/*********************************************************************************DRAW CHARACTERS**************************************************************************************************************************/
	public void drawCharacters(GL10 gl){
		gl.glColor4f(0, 0, 0, 1);		
		gl.glLineWidth(Custom_R._getX(0.6f));
		float nCardX = Custom_R.CARD_STRT_X;
	    float nCardY = Custom_R.CARD_STRT_Y;
	    float nCardBetweenX = Custom_R.CARD_BETWEEN_X;
	    float nCardBetweenY = Custom_R.CARD_BETWEEN_Y;
	    for(int i = 0; i < Custom_R.ROW_; i++){
	        for(int j = 0; j < Custom_R.COL_; j++){
	            int nCardType = m_Eng.m_nArrSlot[i][j];
	            CCSprite spr = m_sprCharacter[nCardType];
	            spr.setPosition(Custom_R._getX(nCardX) + j * Custom_R._getX(nCardBetweenX), Custom_R._getY(nCardY) - (i - 1)  * Custom_R._getY(nCardBetweenY) - m_Eng.m_fMovingY[j]);
	            spr.visit(gl);
	            
	        }
	    }    
	}
/*********************************************************************************DRAW HOLDS**************************************************************************************************************************/
	public void drawHolds(){
		for (int i = 0; i < Custom_R.COL_; i++) {
	        if (!m_Eng.m_bSloting[i]) {
	        	m_arrHold[i].setVisible(false);	           
	        }
	        else{
	        	m_arrHold[i].setVisible(true);	            
	        }
	            
	    }
	}
/*********************************************************************************DRAW LINS**************************************************************************************************************************/
	public void drawLine(boolean bDrawRuleLine){		   
		    for(int i = 0; i < Custom_R.RULE_LINE_COUNT; i++){
		        if (i <  m_Eng.m_nRuleLineCount)
		            m_arrLine[i].setVisible(bDrawRuleLine) ;
		        else 
		        	m_arrLine[i].setVisible(false);
		    }
		
	}
/*********************************************************************************COIN ANIMATIONS**************************************************************************************************************************/
	public void coinAnim(float dt){
		if(arrCoin.size() < 15){
			SpinAnimation coin = new SpinAnimation();
			addChild(coin, z_coin, tag_coin);
			coin.setPosition(CCDirector.sharedDirector().winSize().width / 2, Custom_R._getY(90));
			arrCoin.add(coin);
		}else if(arrCoin.get(14).getPosition().y > CCDirector.sharedDirector().winSize().height - Custom_R._getY(40)){
			arrCoin.clear();
			unschedule("coinAnim");
		}
	}
/******************************************************************SET INFO**************************************************************************************************************************/
	public void setInfo(){
		Custom_R.allCoin = m_Eng.m_nGameCoin;
		Custom_R.curLine = m_Eng.m_nRuleLineCount;
		Custom_R.maxline = m_Eng.m_nMaxLineCount;
		Custom_R.bet = m_Eng.m_nBet;
		Custom_R.saveSetting();
	}
/*********************************************************************************BUTTONS DEFINE**************************************************************************************************************************/
	public void onBack(Object sender){
		if(m_Eng.m_bStartSlot || m_bIncrease)
			return;
		Custom_R.playEffect(Custom_R.click);
		Custom_R.titleState = false;
		setInfo();			
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, TextDataSingle.scene()));
	}
	
	public void onCoinBuy(Object sender){
		if(m_Eng.m_bStartSlot || m_bIncrease)
			return;
		Custom_R.GAME_STATE = "game";
		Custom_R.playEffect(Custom_R.click);
		setInfo();
		Custom_R.payTableFlag = true;
		for(int i = 0; i < Custom_R.CHARACTER_COUNT ; i++){
			for(int j = 0; j < Custom_R.COL_ ; j++){
				if(i == 0){
					Custom_R.rowIndex[i] = m_Eng.m_nRowIndex[i];
				}
				if(i < Custom_R.ROW_)
					Custom_R.arrSlot[i][j] = m_Eng.m_nArrSlot[i][j];
				Custom_R.arrTempSlot[i][j] = m_Eng.m_nArrTempSlot[i][j];
			}
		}		
		//if (VunglePub.isVideoAvailable(true))
		//	VunglePub.displayIncentivizedAdvert(true);	
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, MoneyItem.scene()));
		
	}
	public void onPlayTable(Object sender){
		if(m_Eng.m_bStartSlot || m_bIncrease)
			return;
		Custom_R.playEffect(Custom_R.click);
		setInfo();
		Custom_R.payTableFlag = true;
		for(int i = 0; i < Custom_R.CHARACTER_COUNT ; i++){
			for(int j = 0; j < Custom_R.COL_ ; j++){
				if(i == 0){
					Custom_R.rowIndex[i] = m_Eng.m_nRowIndex[i];
				}
				if(i < Custom_R.ROW_)
					Custom_R.arrSlot[i][j] = m_Eng.m_nArrSlot[i][j];
				Custom_R.arrTempSlot[i][j] = m_Eng.m_nArrTempSlot[i][j];
			}
		}		
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, TableItems.scene()));
	}
	public void onLines(Object sender){
		if(m_Eng.m_bStartSlot || m_bIncrease)
			return;
		Custom_R.playEffect(Custom_R.click);
		m_Eng.m_nRuleLineCount++;
		if(m_Eng.m_nRuleLineCount > m_Eng.m_nMaxLineCount)
			m_Eng.m_nRuleLineCount = 1;
		m_lblLines.setString(String.format("%d", m_Eng.m_nRuleLineCount));
		drawLine(true);
	}
	public void onMaxLines(Object sender){
		if(m_Eng.m_bStartSlot || m_bIncrease)
			return;
		Custom_R.playEffect(Custom_R.click);
		m_Eng.m_nMaxLineCount++;
		if (m_Eng.m_nMaxLineCount > Custom_R.RULE_LINE_COUNT)
			m_Eng.m_nMaxLineCount = 1;			
		m_Eng.m_nRuleLineCount = m_Eng.m_nMaxLineCount;
		m_lblLines.setString(String.format("%d", m_Eng.m_nRuleLineCount));
		m_lblMaxLines.setString(String.format("%d", m_Eng.m_nMaxLineCount));
		drawLine(true);
		
	}
	
	public void onBet(Object sender){
		if(m_Eng.m_bStartSlot || m_bIncrease)
			return;
		Custom_R.playEffect(Custom_R.click);
		m_Eng.m_nBet++;
		if(m_Eng.m_nBet > 10)
			m_Eng.m_nBet = 1;
		m_lblBets.setString(String.format("%d", m_Eng.m_nBet));
	}
	public void onSpin(Object sender){
		if(m_Eng.m_bStartSlot || m_bIncrease)
			return;
		Custom_R.playEffect(Custom_R.click);
		startSlot();	
			
	}
/***************************************************************************************************************************************************************************************************************/
	public void setting(Object sender){
		Custom_R.playEffect(Custom_R.click);
		Custom_R.titleState = true;
		Custom_R.GAME_STATE = "game";
		CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, Setting.scene()));
	}
/*********************************************************************************ALERT**************************************************************************************************************************/
	public void showAlert(){
		CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(CCDirector.sharedDirector().getActivity());
					builder.setMessage("Insufficient.Get some free coins.")
						.setCancelable(false)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									CCDirector.sharedDirector().replaceScene(CCFadeTransition.transition(0.7f, MoneyItem.scene()));
									Custom_R.GAME_STATE = "game";
									dialog.cancel();
	                             }
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {								
									if(Custom_R.allCoin <= 0 && !Custom_R.setTimeState){
										Custom_R.currentTime = System.currentTimeMillis() / 1000 ;
										Custom_R.setTimeState = true;
										Custom_R.saveSetting();
									}
									dialog.cancel();
								}
						});
						AlertDialog alert = builder.create();
						alert.show();
			}
		});
			
	}
	
	
	
	
}