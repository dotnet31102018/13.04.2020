package hello.itay.com.drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton currPaint;

    private hello.itay.com.drawing.DrawingView drawView;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton drawBtn;
    private ImageButton eraseBtn;
    private ImageButton newBtn;
    private ImageButton saveBtn, loadBtn, catBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (hello.itay.com.drawing.DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        drawView.setBrushSize(mediumBrush);

        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        loadBtn = (ImageButton)findViewById(R.id.load_btn);
        loadBtn.setOnClickListener(this);

        catBtn = (ImageButton)findViewById(R.id.cat_btn);
        catBtn.setOnClickListener(this);


    }


    public void paintClicked(View view){

        drawView.setErase(false);

        //use chosen color
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();

            drawView.setColor(color);

            imgView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.paint_pressed, null));
            currPaint.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.paint, null));

            currPaint=(ImageButton)view;

            drawView.setBrushSize(drawView.getLastBrushSize());
        }

    }

    @Override
    public void onClick(View view){
        //respond to clicks
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.eraser_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.loaded = false;
                    drawView.startNew();
                    dialog.dismiss();

                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }
        else if(view.getId()==R.id.save_btn){
            //save drawing
            //AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            //saveDialog.setTitle("Save drawing");
            //saveDialog.setMessage("Save drawing to device Gallery?");
            //saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            //  public void onClick(DialogInterface dialog, int which) {
            //save drawing
            drawView.setDrawingCacheEnabled(true);

            //String imgSaved = MediaStore.Images.Media.insertImage(
            //        getContentResolver(), drawView.getDrawingCache(),
             //       UUID.randomUUID().toString()+".png", "drawing");

            ImageSaver saver = new ImageSaver(this);
            saver.setFileName("myImage.png").setDirectoryName("images");
            saver.save(drawView.getDrawingCache());

                Toast savedToast = Toast.makeText(getApplicationContext(),
                        "Drawing saved to Pictures folder", Toast.LENGTH_SHORT);
                savedToast.show();


            drawView.destroyDrawingCache();
        }

        else if(view.getId()==R.id.load_btn){
            drawView.setDrawingCacheEnabled(true);

            ImageSaver saver = new ImageSaver(this);
            saver.setFileName("myImage.png").setDirectoryName("images");

            drawView.loadBitmap = saver.load();
            drawView.loaded = true;

            drawView.canvasBitmap = Bitmap.createBitmap( drawView.canvasBitmap.getWidth(), drawView.canvasBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            drawView.drawCanvas = new Canvas(drawView.canvasBitmap);

            drawView.invalidate();

            Toast savedToast = Toast.makeText(getApplicationContext(),
                    "Image loaded", Toast.LENGTH_SHORT);
            savedToast.show();

        }
        else if(view.getId()==R.id.cat_btn){

            drawView.initiateCat = !drawView.initiateCat;
            drawView.invalidate();


        }

    }



}
