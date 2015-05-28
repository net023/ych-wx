/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.jfinal;

import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;

/**
 * MsgControllerAdapter 对 MsgController 部分方法提供了默认实现，
 * 以便开发者不去关注 MsgController 中不需要处理的抽象方法，节省代码量
 */
public abstract class MsgControllerAdapter extends MsgController {

	@Override
	protected abstract void processInFollowEvent(InFollowEvent inFollowEvent);

	@Override
	protected abstract void processInTextMsg(InTextMsg inTextMsg);

	@Override
	protected abstract void processInMenuEvent(InMenuEvent inMenuEvent);

	@Override
	protected void processInImageMsg(InImageMsg inImageMsg) {

	}

	@Override
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {

	}

	@Override
	protected void processInVideoMsg(InVideoMsg inVideoMsg) {

	}

	@Override
	protected void processInLocationMsg(InLocationMsg inLocationMsg) {

	}

	@Override
	protected void processInLinkMsg(InLinkMsg inLinkMsg) {

	}

	@Override
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {

	}

	@Override
	protected void processInLocationEvent(InLocationEvent inLocationEvent) {

	}

	@Override
	protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults) {

	}
}
