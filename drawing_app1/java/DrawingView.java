package hello.itay.com.drawing;


import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.SurfaceView;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

/**
 * Created by Itayh on 10/18/2015.
 */
public class DrawingView extends SurfaceView {

    private float brushSize, lastBrushSize;

    private boolean erase=false;

    private Bitmap eraser;

    private int lastx, lasty;

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    protected Canvas drawCanvas;
    //canvas bitmap
    protected Bitmap canvasBitmap;

    protected Bitmap loadBitmap;
    protected boolean loaded = false;

    protected boolean initiateCat = false;
    private Bitmap catBMP;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();

        eraser = BitmapFactory.decodeResource(context.getResources(), R.drawable.eraser);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap( w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

        // cat image initialize
        catBMP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource( getResources(), R.drawable.cat) , w,h, false);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw view
        //canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        if (initiateCat)
        {
            if (catBMP == null)
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource( getResources(), R.drawable.cat) , canvas.getWidth(),
                        canvas.getHeight(), false);

            //initiateCat = false;
            canvas.drawBitmap(catBMP, 0, 0, canvasPaint);
        }

        if (loaded) {
            canvas.drawBitmap(loadBitmap, 0, 0, canvasPaint);
       }

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        canvas.drawPath(drawPath, drawPaint);

        if (erase)
            canvas.drawBitmap(eraser, lastx, lasty, canvasPaint);



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        lastx = (int)touchX;
        lasty = (int)touchY;
        invalidate();
        return true;

    }


    private void setupDrawing(){

        //get drawing area setup for interaction

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    public void setColor(String newColor){
        //set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setBrushSize(float newSize){
        //update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
        //set erase true or false
        erase=isErase;
        if(erase) {
            ///drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            //paintColor = Color.parseColor("#FFFFFF");
            drawPaint.setColor(Color.WHITE);
            drawPaint.setStrokeCap(Paint.Cap.SQUARE);
        }
        else {
           // drawPaint.setXfermode(null);
        }
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

}