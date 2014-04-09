package com.example.ship2;

import java.util.Date;


import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * �Զ���MsgListView���̳���ListView��
 * �������listview��ͷ����������ˢ����ʽ����ʵ���书��
 * @author yanbo
 *
 */

public class MsgListView extends ListView implements OnScrollListener {
	
	 private final static int RELEASE_To_REFRESH = 0;
	 private final static int PULL_To_REFRESH = 1;
	 private final static int REFRESHING = 2;
	 private final static int DONE = 3;

		/**ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���**/
		private final static int RATIO = 2;

	 private LayoutInflater inflater;

	 public LinearLayout headView; // ͷ��

	 private TextView tipsTextview;//����ˢ��
	 private TextView lastUpdatedTextView;//���¸���
	 private ImageView arrowImageView;//��ͷ
	 private ProgressBar progressBar;//ˢ�½�����

	 private RotateAnimation animation;//��ת��Ч ˢ���м�ͷ��ת ���±�����
	 private RotateAnimation reverseAnimation;

	 // ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	 private boolean isRecored;

	 private int headContentWidth;//ͷ�����
	 public int headContentHeight;//ͷ���߶�

	 private int startY;//�߶���ʼλ�ã�������¼��ͷ������
	 private int firstItemIndex;//�б�������������������¼����ͷ������

	 public int state;//����ˢ���С��ɿ�ˢ���С�����ˢ���С����ˢ��

	 private boolean isBack;

//	 public OnRefreshListener refreshListener;//ˢ�¼���

	 private final static String TAG = "abc";
	 private final static boolean log = false;
	 
	 /*****************����*****************/
	 private static final int START_PULL_DEVIATION = 50; // �ƶ����
//	 private static final int WHAT_DID_MORE = 5; // Handler what �Ѿ���ȡ�����
//	 private static final int WHAT_DID_REFRESH = 3; // Handler what �Ѿ�ˢ����
	 /**�ײ�����İ���**/
	 private LinearLayout mFooterView;
	 /**�ײ�����İ���**/
	 private TextView mFooterTextView;
	 /**�ײ�����İ���**/
	 private ProgressBar mFooterLoadingView;
	 /**ˢ�º͸�����¼��ӿ�**/
	 private OnPullDownListener mOnPullDownListener;

	 private float mMotionDownLastY; // ����ʱ���Y������
	 private boolean mIsFetchMoreing; // �Ƿ��ȡ������
	 private boolean mIsPullUpDone; // �Ƿ�������
	 @SuppressWarnings("unused")
	 private boolean mEnableAutoFetchMore; // �Ƿ������Զ���ȡ����
	 /*****************����*****************/

	 /** ˢ�ºͻ�ȡ�����¼��ӿ�*/
	 public interface OnPullDownListener {
		 /**ˢ���¼��ӿ�  ����Ҫע����ǻ�ȡ������ Ҫ�ر� ˢ�µĽ�����RefreshComplete()**/
		 public void onRefresh();
		 /**ˢ���¼��ӿ�  ����Ҫע����ǻ�ȡ������ Ҫ�ر� ����Ľ����� notifyDidMore()**/
		 public void onMore();
	 }
	 
	 /**
		 * ���ü�����
		 * 
		 * @param listener
		 */
	 public void setOnPullDownListener(OnPullDownListener listener) {
		  this.mOnPullDownListener = listener;
		 }
	 
	 public void enableAutoFetchMore(boolean enable, int index) {
			if (enable) {
//				this.setBottomPosition(index);
				mFooterLoadingView.setVisibility(View.VISIBLE);
			} else {
				mFooterTextView.setText("�����ȡ����");
				mFooterLoadingView.setVisibility(View.GONE);
			}
			mEnableAutoFetchMore = enable;
		}
	 
	 public MsgListView(Context context, AttributeSet attrs) {
	  super(context, attrs);
	  init(context);
	 }
	 
	 /**���صײ� ������������**/
		public void setHideFooter() {
			mFooterView.setVisibility(View.GONE);
			mFooterTextView.setVisibility(View.GONE);
			mFooterLoadingView.setVisibility(View.GONE);
			enableAutoFetchMore(false, 1);
		}
		
		
		/**��ʾ�ײ� ʹ����������**/
		public void setShowFooter() {
			mFooterView.setVisibility(View.VISIBLE);
			mFooterTextView.setVisibility(View.VISIBLE);
			mFooterLoadingView.setVisibility(View.VISIBLE);
			enableAutoFetchMore(true, 1);
		}
		
//		private Handler mUIHandler = new Handler() {
//
//			@Override
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case WHAT_DID_REFRESH: {
////					mListView.onRefreshComplete();
//					return;
//				}
//
//				case WHAT_DID_MORE: {
//					mIsFetchMoreing = false;
//					mFooterTextView.setText("�����ʼ�");
//					mFooterLoadingView.setVisibility(View.GONE);
//				}
//				}
//			}
//
//		};

	 synchronized private void init(Context context) {
		 
	  inflater = LayoutInflater.from(context);

	  headView = (LinearLayout) inflater.inflate(R.layout.head, null);//listviewƴ��headview
	  headView.setFocusable(false);


		mOnPullDownListener = new OnPullDownListener() {
			@Override
			synchronized public void onRefresh() {
			}

			@Override
			synchronized public void onMore() {
			}
		};
		
		 /*** �Զ���Ų��ļ�*/
			mFooterView = (LinearLayout)inflater.inflate(R.layout.pulldown_footer, null);
			mFooterTextView = (TextView) mFooterView.findViewById(R.id.pulldown_footer_text);
			mFooterLoadingView = (ProgressBar) mFooterView.findViewById(R.id.pulldown_footer_loading);
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!mIsFetchMoreing) {

						mIsFetchMoreing = true;
						mFooterTextView.setText("���ظ�����...");
						mFooterLoadingView.setVisibility(View.VISIBLE);
						mOnPullDownListener.onMore();
					}
				}
			});
			
			addFooterView(mFooterView);
			 /*** �Զ���Ų��ļ�*/
	  
	  headView.setOnClickListener(new OnClickListener() {
		  
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
		}
	});

	  /***************����ͷ��****************/
	  arrowImageView = (ImageView) headView
	    .findViewById(R.id.head_arrowImageView);//headview�и�view
	  arrowImageView.setMinimumWidth(75);
	  arrowImageView.setMinimumHeight(75);
	  progressBar = (ProgressBar) headView
	    .findViewById(R.id.head_progressBar);//headview�и�view
	  tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);//headview�и�view
	  lastUpdatedTextView = (TextView) headView
	    .findViewById(R.id.head_lastUpdatedTextView);//headview�и�view

	  measureView(headView);
	  headContentHeight = headView.getMeasuredHeight();//ͷ���߶�
	  headContentWidth = headView.getMeasuredWidth();//ͷ�����

	  headView.setPadding(0, -1 * headContentHeight, 0, 0);//setPadding(int left, int top, int right, int bottom) 
	  headView.invalidate();//Invalidate the whole view

	  if(log)Log.v("size", "width:" + headContentWidth + " height:"
	    + headContentHeight);

	  addHeaderView(headView);//��ӽ�headview
	  setOnScrollListener(this);//��������

	  animation = new RotateAnimation(0, -180,
	    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
	    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
	  animation.setInterpolator(new LinearInterpolator());
	  animation.setDuration(250);
	  animation.setFillAfter(true);//��Чanimation����

	  reverseAnimation = new RotateAnimation(-180, 0,
	    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
	    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
	  reverseAnimation.setInterpolator(new LinearInterpolator());
	  reverseAnimation.setDuration(250);
	  reverseAnimation.setFillAfter(true);//��ЧreverseAnimation����
	  /***************����ͷ��****************/
	 }

	 public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,//�����¼�
	   int arg3) {
	  firstItemIndex = firstVisiableItem;//�õ���item����
	 }

	 public void onScrollStateChanged(AbsListView arg0, int arg1) {
		 switch (arg1) {           
		 // ��������ʱ           
		 case OnScrollListener.SCROLL_STATE_IDLE:                
			 // �жϹ������ײ�                
			 if (arg0.getLastVisiblePosition() == (arg0.getCount() - 1)&& mMotionDownLastY-startY < 0 && isFillScreenItem()) {    
				 //����
					mIsFetchMoreing = true;
					mFooterTextView.setText("���ظ�����...");
					mFooterLoadingView.setVisibility(View.VISIBLE);
					mOnPullDownListener.onMore();      
//				 isLastisNext++;              
				 }              
			 break;
		 }
	 }

	 /***********************ͷ������******************************/
	 synchronized public boolean onTouchEvent(MotionEvent event) {//�����¼�
		 

	  switch (event.getAction()) {
	  case MotionEvent.ACTION_DOWN://�ְ���  ��Ӧ����ˢ��״̬
	   if (firstItemIndex == 0 && !isRecored) {//�����item����Ϊ0������δ��¼startY,��������ʱ��¼֮����ִ��isRecored = true;
	    startY = (int) event.getY();
	    isRecored = true;

	    if(log)Log.v(TAG, "��downʱ���¼��ǰλ�á�");
	   }

		 mIsPullUpDone = false;
	   break;

	  case MotionEvent.ACTION_UP://���ɿ�  ��Ӧ�ɿ�ˢ��״̬

	   if (state != REFRESHING) {//���ɿ���4��״̬������ˢ�¡��ɿ�ˢ�¡�����ˢ�¡����ˢ�¡������ǰ��������ˢ��
	    if (state == DONE) {//�����ǰ�����ˢ�£�ʲô������
	    }
	    if (state == PULL_To_REFRESH) {//�����ǰ������ˢ�£�״̬��Ϊ���ˢ�£��⼴����ˢ���о��ɿ��ˣ�ʵ��δ���ˢ�£���ִ��changeHeaderViewByState()
	     state = DONE;
	     changeHeaderViewByState();

	     if(log)Log.v(TAG, "������ˢ��״̬����done״̬");
	    }
	    if (state == RELEASE_To_REFRESH) {//�����ǰ���ɿ�ˢ�£�״̬��Ϊ����ˢ�£��⼴�ɿ�ˢ�����ɿ��֣�����������ˢ�£���ִ��changeHeaderViewByState()
	     state = REFRESHING;
	     changeHeaderViewByState();
	     onRefresh();//����ˢ�£�����ִ��onrefresh��ִ�к�״̬��Ϊ���ˢ��

	     if(log)Log.v(TAG, "���ɿ�ˢ��״̬����done״̬");
	    }
	   }

	   isRecored = false;//���ɿ����������������������¼�¼startY,��ΪֻҪ���ɿ�����Ϊһ��ˢ�������
	   isBack = false;

		 mMotionDownLastY = event.getRawY();
//		if (mMotionDownLastY-event.getRawY()>500) {
//			return true;
//		}
	   break;

	  case MotionEvent.ACTION_MOVE://���϶����϶������в��ϵ�ʵʱ��¼��ǰλ��
	   int tempY = (int) event.getY();
	   if (!isRecored && firstItemIndex == 0) {//�����item����Ϊ0������δ��¼startY,�����϶�ʱ��¼֮����ִ��isRecored = true;
	    if(log)Log.v(TAG, "��moveʱ���¼��λ��");
	    isRecored = true;
	    startY = tempY;
	   }
	   if (state != REFRESHING && isRecored) {//���״̬��������ˢ�£����Ѽ�¼startY��tempYΪ�϶�������һֱ�ڱ�ĸ߶ȣ�startYΪ�϶���ʼ�߶�
	    // ��������ȥˢ����
	    if (state == RELEASE_To_REFRESH) {//���״̬���ɿ�ˢ��
	     // �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�
	     if (((tempY - startY) / RATIO  < headContentHeight)//���ʵʱ�߶ȴ�����ʼ�߶ȣ�������֮��С��ͷ���߶ȣ���״̬��Ϊ����ˢ��
	       && (tempY - startY) > 0) {
	      state = PULL_To_REFRESH;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");
	     }
	     // һ�����Ƶ�����
	     else if (tempY - startY <= 0) {//���ʵʱ�߶�С�ڵ�����ʼ�߶��ˣ���˵�������ˣ�״̬��Ϊ���ˢ��
	      state = DONE;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");
	     }
	     // �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�
	     else {//�����ǰ�϶������м�û�е�����ˢ�µĵز���Ҳû�е����ˢ�£��������ĵز����򱣳��ɿ�ˢ��״̬
	      // ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������
	     }
	    }
	    // ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬
	    if (state == PULL_To_REFRESH) {//���״̬������ˢ��
	     // ���������Խ���RELEASE_TO_REFRESH��״̬
	     if ((tempY - startY) / RATIO >= headContentHeight) {//���ʵʱ�߶�����ʼ�߶�֮����ڵ���ͷ���߶ȣ���״̬��Ϊ�ɿ�ˢ��
	      state = RELEASE_To_REFRESH;
	      isBack = true;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");
	     }
	     // ���Ƶ�����
	     else if (tempY - startY <= 0) {//���ʵʱ�߶�С�ڵ�����ʼ�߶��ˣ���˵�������ˣ�״̬��Ϊ���ˢ��
	      state = DONE;
	      changeHeaderViewByState();

	      if(log)Log.v(TAG, "��DOne��������ˢ��״̬ת�䵽done״̬");
	     }
	    }

	    // done״̬��
	    if (state == DONE) {//���״̬�����ˢ��
	     if (tempY - startY > 0) {//���ʵʱ�߶ȴ�����ʼ�߶��ˣ���״̬��Ϊ����ˢ��
	      state = PULL_To_REFRESH;
	      changeHeaderViewByState();
	     }
	    }

	    // ����headView��size
	    if (state == PULL_To_REFRESH) {//���״̬������ˢ�£�����headview��size           ?
	     headView.setPadding(0, -1 * headContentHeight
	       + (tempY - startY) / RATIO, 0, 0);
	     headView.invalidate();
	    }

	    // ����headView��paddingTop
	    if (state == RELEASE_To_REFRESH) {//���״̬���ɿ�ˢ�£����� headview��paddingtop      ?
	     headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight,
	       0, 0);
	     headView.invalidate();
	    }
	   }
	   if (mIsPullUpDone)
			return true;

		// �����ʼ���µ��������벻�������ֵ���򲻻���
		final int absMotionY = (int) Math.abs(event.getRawY() - mMotionDownLastY);
		if (absMotionY < START_PULL_DEVIATION)
			return true;

//		return false;
	   break;
	  }
	  return super.onTouchEvent(event);
	 }
	 
	 // ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
	 public void changeHeaderViewByState() {
	  switch (state) {
	  case RELEASE_To_REFRESH:
	   arrowImageView.setVisibility(View.VISIBLE);
	   progressBar.setVisibility(View.GONE);
	   tipsTextview.setVisibility(View.VISIBLE);
	   lastUpdatedTextView.setVisibility(View.VISIBLE);

	   arrowImageView.clearAnimation();
	   arrowImageView.startAnimation(animation);

	   tipsTextview.setText("�ɿ�ˢ��");

	   if(log)Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");
	   break;
	  case PULL_To_REFRESH:
	   progressBar.setVisibility(View.GONE);
	   tipsTextview.setVisibility(View.VISIBLE);
	   lastUpdatedTextView.setVisibility(View.VISIBLE);
	   arrowImageView.clearAnimation();
	   arrowImageView.setVisibility(View.VISIBLE);
	   // ����RELEASE_To_REFRESH״̬ת������
	   if (isBack) {
	    isBack = false;
	    arrowImageView.clearAnimation();
	    arrowImageView.startAnimation(reverseAnimation);

	    tipsTextview.setText("����ˢ��");
	   } else {
	    tipsTextview.setText("����ˢ��");
	   }
	   if(log)Log.v(TAG, "��ǰ״̬������ˢ��");
	   break;

	  case REFRESHING:

	   headView.setPadding(0, 0, 0, 0);
	   headView.invalidate();

	   progressBar.setVisibility(View.VISIBLE);
	   arrowImageView.clearAnimation();
	   arrowImageView.setVisibility(View.GONE);
	   tipsTextview.setText("����ˢ��...");
	   lastUpdatedTextView.setVisibility(View.VISIBLE);

	   if(log)Log.v(TAG, "��ǰ״̬,����ˢ��...");
	   break;
	  case DONE:
	   headView.setPadding(0, -1 * headContentHeight, 0, 0);
	   headView.invalidate();

	   progressBar.setVisibility(View.GONE);
	   arrowImageView.clearAnimation();
	   arrowImageView
	     .setImageResource(R.drawable.ic_pulltorefresh_arrow);
	   tipsTextview.setText("����ˢ��");
	   lastUpdatedTextView.setVisibility(View.VISIBLE);

	   if(log)Log.v(TAG, "��ǰ״̬��done");
	   break;
	  }
	 }
	 /***********************ͷ������******************************/

//	 public void setonRefreshListener(OnRefreshListener onRefreshListener) {
//	  this.onRefreshListener = onRefreshListener;
//	 }

//	 public boolean onMotionDown(MotionEvent ev) {
//			mIsPullUpDone = false;
//			mMotionDownLastY = ev.getRawY();
//
//			return false;
//		}

//		public boolean onMotionMove(MotionEvent ev, int delta) {
//			// ��ͷ���ļ�������ʧ��ʱ�򣬲��������
//			if (mIsPullUpDone)
//				return true;
//
//			// �����ʼ���µ��������벻�������ֵ���򲻻���
//			final int absMotionY = (int) Math.abs(ev.getRawY() - mMotionDownLastY);
//			if (absMotionY > START_PULL_DEVIATION)
//				return true;
//
//			return false;
//		}

//		public boolean onMotionUp(MotionEvent ev) {
////			if (ScrollOverListView.canRefleash) {
////				ScrollOverListView.canRefleash = false;
////				mOnPullDownListener.onRefresh();
////			}
//			return false;
//		}
		
		
		
//	 public interface OnRefreshListener {
//	  public void onRefresh();
//	 }

	 @SuppressWarnings("deprecation")
	public void onRefreshComplete() {
	  state = DONE;
	  lastUpdatedTextView.setText("�������:" + new Date().toLocaleString());//ˢ�����ʱ��ͷ�����ѵ�ˢ������
	  changeHeaderViewByState();
		if(log)Log.v("msgListView","onRefreshComplete");
	 }
	 
	 public void onMoreComplete() {
			mIsFetchMoreing = false;
			mFooterTextView.setText("�����ȡ����");
			mFooterLoadingView.setVisibility(View.GONE);
		 }

	 private void onRefresh() {
	  if (mOnPullDownListener != null) {
		  mOnPullDownListener.onRefresh();
	  }
	 }
	 
	 @SuppressWarnings("unused")
	private void onMore() {
//		  if (mOnPullDownListener != null) {
//			  mOnPullDownListener.onMore();
//		  }

			if (!mIsFetchMoreing) {
				mIsFetchMoreing = true;
				mFooterTextView.setText("���ظ�����...");
				mFooterLoadingView.setVisibility(View.VISIBLE);
				mOnPullDownListener.onMore();
			}
		 }
	 
	 public boolean onListViewBottomAndPullUp(int delta) {
//			if (!mEnableAutoFetchMore || mIsFetchMoreing)
//				return false;
			// ����������Ļ�Ŵ���
//			if (isFillScreenItem()) {
//				mIsFetchMoreing = true;
//				mFooterTextView.setText("���ظ�����...");
//				mFooterLoadingView.setVisibility(View.VISIBLE);
//				mOnPullDownListener.onMore();
//				return true;
//			}
			return false;
		}
	 
	 private boolean isFillScreenItem() {
			final int firstVisiblePosition = this.getFirstVisiblePosition();
			final int lastVisiblePostion = this.getLastVisiblePosition()
					- this.getFooterViewsCount();
			final int visibleItemCount = lastVisiblePostion - firstVisiblePosition
					+ 1;
			final int totalItemCount = this.getCount()
					- this.getFooterViewsCount();

			if (visibleItemCount < totalItemCount)
				return true;
			return false;
		}

	 //�˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height
	 @SuppressWarnings("deprecation")
	private void measureView(View child) {
	  ViewGroup.LayoutParams p = child.getLayoutParams();
	  if (p == null) {
	   p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
	     ViewGroup.LayoutParams.WRAP_CONTENT);
	  }
	  int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
	  int lpHeight = p.height;
	  int childHeightSpec;
	  if (lpHeight > 0) {
	   childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
	     MeasureSpec.EXACTLY);
	  } else {
	   childHeightSpec = MeasureSpec.makeMeasureSpec(0,
	     MeasureSpec.UNSPECIFIED);
	  }
	  child.measure(childWidthSpec, childHeightSpec);
	 }
	}