package com.cm.utility.circularprogressbarlibrary;

/**
 * @author Mahesh.CHunkhade
 * @Createdon  Created on: 11 Aug, 2014
 * @LastUpdatedon: 20 Aug, 2014
 * @version 1.0
 * @About: Android do not having widget for circular progress bar, this widget helps to draw circular progress with progress at center if bar.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
public class CircularProgressBar extends View{

	/**
	 * Background circle paint.
	 */
	private Paint backgroudCircle = new Paint();

	/**
	 * Progress circle paint
	 */
	private Paint progressCircle = new Paint();

	/**
	 * overlay circle paint
	 */
	private Paint overlayCircle = new Paint();


	private RectF rectF = new RectF();

	/**
	 * Background color of progress bar
	 */
	private int backgroudColor;

	/**
	 * Progress color of progress bar
	 */
	private int progressColor;

	/**
	 * Maximum progress of progress bar
	 */
	private static final int MAX_PROGRESS = 100;

	private float maxProgress = MAX_PROGRESS;

	/**
	 * Total progress of progress bar
	 */
	private float progress = 0.0f;

	/**
	 * Progress stroke width/progress circle size
	 */
	private float progressStrokeWidth;

	/**
	 * Height of view
	 */
	private float width;

	/**
	 * Width of view
	 */
	private float height;

	/**
	 * CenterX of view
	 */
	private float centerX;

	/**
	 * CenterY of view
	 */
	private float centerY;

	/**
	 * Radius of circular progress bar
	 */
	private float backgroudCircleRadius;

	/**Color of center circle
	 * 
	 */
	private int progressBarCenterColor;

	/**
	 * Text to set the center of circle
	 */
	private String centerText;

	/**Color of text
	 * 
	 */
	private int textColor;

	/**
	 * Size of text
	 */
	private float textSize;

	private TextPaint textPaint = new TextPaint();

	/**
	 * Runnable to update progress from another thread
	 */
	private RefreshProgressRunnable refreshProgressRunnable;

	/**
	 * Currently running thread ID
	 */
	private long mCurrentThread;

	/**
	 * Layout to draw text on canvas
	 */
	private StaticLayout mCenterTextLayout;

	private RectF rect = new RectF();
	
	/**
	 * Check to enable or disable visibility of text
	 */
	private boolean isTextVisible = true;
	
	/**
	 * Paint to draw initial mark
	 */
	private Paint markCirclePaint = new Paint();
	
	private boolean isMarkerVisible = true;
	
	/**
	 * @return set marker visible of progress bar
	 */

	public boolean isMarkerVisible() {
		return isMarkerVisible;
	}

	/**
	 * @param isTextVisible get text is visible in center or not
	 */
	public void setMarkerVisible(boolean isMarkerVisible) {
		this.isMarkerVisible = isMarkerVisible;
	}
	
	
	/**
	 * @param isTextVisible get text is visible in center
	 */
	public synchronized boolean isTextVisible() {
		return isTextVisible;
	}

	/**
	 * @param isTextVisible get text is visible in center or not
	 */
	public synchronized void setTextVisible(boolean isTextVisible) {
		this.isTextVisible = isTextVisible;
	}

	/**
	 * @return text color of progress bar
	 */
	public synchronized int getTextColor() {
		return textColor;
	}

	/**
	 * 
	 * @param textColor to progress bar
	 */
	public synchronized void setTextColor(int textColor) {
		drawCurrentProgress();
		this.textColor = textColor;
	}

	/**
	 * 
	 * @return text size of text of progress bar
	 */
	public synchronized float getTextSize() {
		return textSize;
	}

	/**
	 * 
	 * @param textSize to progress bar text
	 */
	public synchronized void setTextSize(float textSize) {
		drawCurrentProgress();
		this.textSize = textSize;
	}

	/**
	 * 
	 * @return center text of pregress bar
	 */
	public synchronized String getCenterText() {
		return centerText;
	}

	/**
	 * 
	 * @param centerText text to progress bar
	 */
	public synchronized void setCenterText(String centerText) {
		this.centerText = centerText;
		drawCurrentProgress();
	}

	/**
	 * 
	 * @return center color of progress bar
	 */
	public synchronized int getProgressBarCenterColor() {


		if(progressBarCenterColor ==0) {


			TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[] {  
					android.R.attr.colorBackground, 
					android.R.attr.textColorPrimary, 
			}); 
			progressBarCenterColor = array.getColor(0, 0xFF00FF); 

		}

		return progressBarCenterColor;
	}

	/**
	 * 
	 * @param progressBarCenterColor set center color of progress bar
	 */
	public synchronized void setProgressBarCenterColor(int progressBarCenterColor) {
		drawCurrentProgress();
		this.progressBarCenterColor = progressBarCenterColor;
	}

	/**
	 * 
	 * @return Progress stroke width
	 */
	public synchronized float getProgressStrokeWidth() {
		drawCurrentProgress();
		return progressStrokeWidth;
	}

	/**
	 * 
	 * @param progressStrokeWidth - set progress width to progress bar
	 */
	public synchronized void setProgressStrokeWidth(float progressStrokeWidth) {
		this.progressStrokeWidth = progressStrokeWidth;
	}

	/**
	 * 
	 * @return background color of progress bar
	 */
	public synchronized int getBackgroudColor() {
		drawCurrentProgress();
		return backgroudColor;
	}

	/**
	 * 
	 * @param backgroudColor of progress bar
	 */
	public synchronized void setBackgroudColor(int backgroudColor) {
		drawCurrentProgress();
		this.backgroudColor = backgroudColor;
	}

	/**
	 * 
	 * @return progress bar color
	 */
	public synchronized int getProgressColor() {
		return progressColor;
	}
	
	/**
	 * 
	 * @param progressColor set progressbar color
	 */

	public synchronized void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
	}

	/**
	 * 
	 * @return maximum progress of progress bar
	 */
	public synchronized float getMaxProgress() {
		return maxProgress;
	}

	/**
	 * @param maxProgress to set maximum progress to progress bar
	 */
	public synchronized void setMaxProgress(float maxProgress) {
		drawCurrentProgress();
		this.maxProgress = maxProgress;
	}

	/**
	 * @return current progress of the circular progress bar
	 */
	public synchronized float getProgress() {
		return progress;
	}

	/**
	 * @param progress - to set the progress to progress bar
	 */
	public synchronized void setProgress(float progress) {

		if(progress>maxProgress) {
			return;
		}

		drawCurrentProgress();
		this.progress = progress;
	}

	public CircularProgressBar(Context context) {
		super(context);

		initProgressBar();
		drawCurrentProgress();


	}

	public CircularProgressBar(final Context context,
			final AttributeSet attrs) {
		this(context, attrs, R.attr.circularProgressBarStyle);
		initProgressBar();
		drawCurrentProgress();
	}

	/**
	 * Circular progress bar constructor with attributes
	 */
	public CircularProgressBar(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		// load the styled attributes and set their properties
		final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar,
				defStyleAttr, 0);

		setProgressColor(attributes.getColor(R.styleable.CircularProgressBar_progressColor, Color.CYAN));
		setProgress(attributes.getFloat(R.styleable.CircularProgressBar_progress,
				Color.MAGENTA));
		setBackgroudColor(attributes.getColor(R.styleable.CircularProgressBar_progressBackgroundColor,  Color.GRAY));
		setMaxProgress(attributes.getFloat(R.styleable.CircularProgressBar_max_progress, 100.0f));
		setProgressStrokeWidth(attributes.getDimension(R.styleable.CircularProgressBar_stroke_width, 10.0f));
		setProgressBarCenterColor(attributes.getColor(R.styleable.CircularProgressBar_progressCenterColor, getProgressBarCenterColor()));
		setCenterText(attributes.getString(R.styleable.CircularProgressBar_progressText));
		setTextColor(attributes.getColor(R.styleable.CircularProgressBar_textColor, Color.WHITE));
		setTextSize(attributes.getDimension(R.styleable.CircularProgressBar_textSize, 20.0f));
		attributes.recycle();
		initProgressBar();
		drawCurrentProgress();
	}

	/**
	 * onDraw overridden method to draw progress bar on canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		initProgressBar();
		backgroudCircleRadius = height > width ? width/2:height/2;
		centerX = width/2 ;
		centerY = height/2 ;
		
		backgroudCircleRadius-=progressStrokeWidth;
		
		canvas.drawCircle(centerX, centerY, backgroudCircleRadius, backgroudCircle);

		float left = centerX-backgroudCircleRadius;
		float top = centerY-backgroudCircleRadius;
		float right = centerX + backgroudCircleRadius;
		float bottom = centerY + backgroudCircleRadius;

		rectF.set(left, top, right, bottom);

		canvas.drawArc(rectF, 0, getProgressInDegree(), true, progressCircle);
		canvas.drawCircle(centerX, centerY, backgroudCircleRadius-progressStrokeWidth, overlayCircle);


		if(isMarkerVisible)
			canvas.drawCircle(centerX+backgroudCircleRadius-progressStrokeWidth/2, centerX,progressStrokeWidth, markCirclePaint);
		
		if(!TextUtils.isEmpty(centerText) && isTextVisible()) {
			rect.left= centerX-backgroudCircleRadius+progressStrokeWidth*2;
			rect.right=centerX+backgroudCircleRadius - progressStrokeWidth*2;
			rect.top = centerY-backgroudCircleRadius/4;
			rect.bottom = centerY+backgroudCircleRadius/4;
			mCenterTextLayout = getStaticLayout(centerText, rect.width());
			canvas.save();
			canvas.translate(rect.left, rect.top);
			mCenterTextLayout.draw(canvas);
		}

		canvas.restore();

	}

	/**
	 * Static layout to draw text
	 */
	public StaticLayout getStaticLayout(String text, float f) {
		return new StaticLayout(text, textPaint, (int) f, Layout.Alignment.ALIGN_CENTER, 1, 1, false);
	}

	/**
	 * 
	 * get the progress in degree to draw current angle
	 */
	public float getProgressInDegree() {

		float progress = getProgress()*360/getMaxProgress();

		return progress;

	}

	/**
	 * Init component of progress bar
	 */
	private void initProgressBar() {

		mCurrentThread = Thread.currentThread().getId();

		backgroudCircle.setColor(getBackgroudColor());

		backgroudCircle.setAntiAlias(true);

		progressCircle.setColor(getProgressColor());

		progressCircle.setAntiAlias(true);
		
		progressCircle.setMaskFilter(new BlurMaskFilter(getProgressStrokeWidth()/3, Blur.NORMAL));

		overlayCircle.setColor(getProgressBarCenterColor());

		overlayCircle.setAntiAlias(true);
		
		markCirclePaint.setAntiAlias(true);
		
		markCirclePaint.setColor(getProgressColor());

		textPaint.setColor(getTextColor());

		textPaint.setTextSize(getTextSize());

		textPaint.setAntiAlias(true);

		refreshProgressRunnable = new RefreshProgressRunnable();

	}

	/**
	 * Draw the current progress of the progress bar
	 */
	private synchronized void drawCurrentProgress() {

		if(mCurrentThread == Thread.currentThread().getId()){
			invalidate();
		}else{

			RefreshProgressRunnable r;

			if(refreshProgressRunnable!=null) {
				r = refreshProgressRunnable;
				refreshProgressRunnable = null;
			}else{
				r = new RefreshProgressRunnable();
			}

			post(r);
		}
	}

	/**
	 * Runnable to draw the progress
	 */
	private class RefreshProgressRunnable implements Runnable {

		@Override
		public void run() {

			invalidate();

			refreshProgressRunnable = this;

		}

	}

	/**
	 * Parcelable to save state
	 */
	static class SavedState extends BaseSavedState {
		float progress;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			progress = in.readFloat();
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeFloat(progress);
		}

		public static final Parcelable.Creator<SavedState> CREATOR
		= new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	
	/**
	 * OnSaveInstanceState overriend method
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.progress = progress;

		return ss;
	}

	/**
	 * onRestoreInstate to restore state of progress bar
	 */
	@Override
	protected void onRestoreInstanceState(Parcelable state) {

		SavedState ss = (SavedState)state;

		super.onRestoreInstanceState(ss.getSuperState());

		setProgress(ss.progress);
	}

	/**
	 * Measure overridden method
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		height = getHeight() + getPaddingTop()+ getPaddingBottom();

		width = getWidth() + getPaddingLeft()+ getPaddingRight();

		setMeasuredDimension(resolveSize((int)width, widthMeasureSpec), resolveSize((int)height, heightMeasureSpec));

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

	}

}
