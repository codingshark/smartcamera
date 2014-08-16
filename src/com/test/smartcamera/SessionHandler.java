package com.test.smartcamera;

import java.util.Stack;

import com.test.smartcamera.resources.DefaultValues;
import com.test.smartcamera.session.AbstractSession;
import com.test.smartcamera.session.SessionAuto;
import com.test.smartcamera.session.SessionProPhoto;
import com.test.smartcamera.session.SessionSelfTimer;
import com.test.smartcamera.session.SessionVideo;

public class SessionHandler
{
	//-----------------------------------------------------------
	//name convention for session id:
	//session + [ex|im] + [session name]
	public static final int session_ex_prophoto = 0;
	public static final int session_ex_auto = 1;
	public static final int session_im_video_rec = 2;
	public static final int session_im_self_timer = 3;
	//TODO add new sessions here
	
	private static final int SESSION_WAREHOUSE_SIZE = 4; //TODO update this value after adding new sessions
	//-----------------------------------------------------------
	
	private Stack<AbstractSession> mSessionStack = new Stack<AbstractSession>(); 
	private AbstractSession[] mSessionWarehouse = new AbstractSession[SESSION_WAREHOUSE_SIZE];;

	//sessions
	private AbstractSession mCurrentSession = null;
	
	public void onCreate()
	{
		installSessions();
//      mPreferencesSerializer.close();/* deserialize/read do not need to close*/
	}

	public void stopSessions()
	{
		for(int i = 0; i < SESSION_WAREHOUSE_SIZE; ++i)
		{
			if(mSessionWarehouse[i] != null)
			{
				mSessionWarehouse[i].onStop();//then stop
			}
		}
	}
	
	protected void installExplicitSessions()
	{
		mSessionWarehouse[session_ex_prophoto] = new SessionProPhoto();
		mSessionWarehouse[session_ex_auto] = new SessionAuto();
	}
	
	protected void installImplicitSessions()
	{
		mSessionWarehouse[session_im_video_rec] = new SessionVideo();
		mSessionWarehouse[session_im_self_timer] = new SessionSelfTimer();
		//TODO new implicit session here
	}
	
	protected void installSessions()
	{
		installExplicitSessions();
		installImplicitSessions();
		//Starts the default session
		mCurrentSession = mSessionWarehouse[DefaultValues.default_session];
		mCurrentSession.resumeAndCreate();
	}
	
	public void pushAndPlaySession(int aNewSessionId)
	{
		mSessionStack.push(mCurrentSession);
		playSession(aNewSessionId);
	}
	
	public void popAndPlaySession()
	{
		assert(mSessionStack.size() != 0);
		playSession(mSessionStack.peek());
		mSessionStack.pop();
	}
	
	public void popSession()//Pop but do not play
	{
		assert(mSessionStack.size() != 0);
		mCurrentSession = mSessionStack.peek();
		mSessionStack.pop();
	}
	
	protected void playSession(AbstractSession aSession)
	{
		if(mCurrentSession != aSession)
		{
			if(mCurrentSession != null)
			{
				mCurrentSession.onPause();
			}
			mCurrentSession = aSession;
			mCurrentSession.resumeAndCreate();
		}
	}
	
	public void playSession(int sessionId)
	{
		playSession(mSessionWarehouse[sessionId]);
	}
	
	public void onResume()
	{
		mCurrentSession.onResume();
	}
	public void onPause()
	{
		mCurrentSession.onPause();
	}
	
	public void onStop()
	{
		stopSessions();
	}
	
	public AbstractSession getCurrentSession()
	{
		return mCurrentSession;
	}
}
