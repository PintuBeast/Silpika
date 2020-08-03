package com.example.silpika_v2;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import android.view.View;


import androidx.annotation.RequiresApi;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.examples.posenet.lib.BodyPart;
import org.tensorflow.lite.examples.posenet.lib.Device;
import org.tensorflow.lite.examples.posenet.lib.KeyPoint;
import org.tensorflow.lite.examples.posenet.lib.Person;
import org.tensorflow.lite.examples.posenet.lib.Posenet;
import org.tensorflow.lite.examples.posenet.lib.Position;

import static java.lang.StrictMath.abs;



public class DrawingTheBall extends View {

    int MODEL_WIDTH = 257;
    int MODEL_HEIGHT = 257;

    private Paint paint;
    Interpreter tflite;

    Posenet posenet;
    Person person;
    BodyPart bodypart;
    Canvas canvas1;

    Bitmap croppedBitmap,scaledBitmap,workingBitmap,mutableBitmap;

    public boolean paintFlag= false;
    int x,y;
int i;
MediaMetadataRetriever mediaMetadataRetriever,mediaMetadataRetriever_1;

public Bitmap bball,bball_1;
    @RequiresApi(api = Build.VERSION_CODES.P)
    public DrawingTheBall(Context context, Uri videoUri,Uri videoUri_1) {
        super(context);

        posenet = new Posenet(DrawingTheBall.this.getContext().getApplicationContext(),"posenet_model.tflite", Device.CPU);

        //video 1 frame extraction end
        mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(getContext(),videoUri);
        //video 1 frame extraction end


        //video 2 frame extraction start
        mediaMetadataRetriever_1 = new MediaMetadataRetriever();
        mediaMetadataRetriever_1.setDataSource(getContext(),videoUri_1);
        //video 2 frame extraction end



x=0;
y=0;

i=1;


        try{
            tflite=new Interpreter(loadModelFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect ourRect1=new Rect();
        Rect ourRect2=new Rect();

        canvas1=canvas;

paintFlag=false;

        //video 1 start
        ourRect1.set(0,0,canvas.getWidth(),canvas.getHeight()/2);
Paint blue=new Paint();
blue.setColor(Color.BLUE);
blue.setStyle(Paint.Style.FILL);
canvas.drawRect(ourRect1,blue);
bball = mediaMetadataRetriever.getFrameAtIndex(i);
processImage(bball);

        //video 1 end




        //video 2 start

        paintFlag=true;
        ourRect2.set(0,canvas.getHeight()/2,canvas.getWidth(),canvas.getHeight());
        Paint green=new Paint();
        green.setColor(Color.GRAY);
        green.setStyle(Paint.Style.FILL);
        canvas.drawRect(ourRect2,green);
        bball_1 = mediaMetadataRetriever_1.getFrameAtIndex(i);
     processImage(bball_1);
paintFlag=false;
        //video 2 end






        i+=1;
        invalidate();

    }


    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor=getContext().getApplicationContext().getAssets().openFd("posenet_model.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset=fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);


    }





    private void processImage(Bitmap bitmap){




        //imageView1=findViewById(R.id.image1);

        // Canvas canvas;

        croppedBitmap= cropBitmap(bitmap);
        scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, MODEL_WIDTH, MODEL_HEIGHT, true);
        person = posenet.estimateSinglePose(scaledBitmap);



        workingBitmap=Bitmap.createBitmap(croppedBitmap);
        mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //canvas=new Canvas(mutableBitmap);



        draw(person,mutableBitmap);

    }

    private void setPaint() {
        paint=new Paint();
        paint.setColor( Color.RED);
        paint.setTextSize (80.0f);
        paint.setStrokeWidth (5.0f);
    }

    private void draw( Person person, Bitmap bitmap1) {

      //  canvas1.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        // Draw `bitmap` and `person` in square canvas.
        int screenWidth;
        int screenHeight;
        int left;
        int right;
        int top;
        int bottom;

       // if (canvas1.getHeight() > canvas1.getWidth() ) {
       //     screenWidth = canvas1.getWidth();
       //     screenHeight = canvas1.getWidth();
       //     left = 0;
       //     top = (canvas1.getHeight() - canvas1.getWidth()) / 2;
       // } else {
       //     screenWidth = canvas1.getHeight();
       //     screenHeight = canvas1.getHeight();
       //     left = (canvas1.getWidth() - canvas1.getHeight()) / 2;
       //     top = 0;
       // }
       // right = left + screenWidth;
       // bottom = top + screenHeight;

        setPaint();
        Rect ourRect1 = new Rect();
        Rect ourRect2 = new Rect();

        if (!paintFlag) {

            ourRect1.set(0, 0, canvas1.getWidth(), canvas1.getHeight() / 2);
            canvas1.drawBitmap(bitmap1,0,0,paint);
            canvas1.drawText("Instructor Video",0,canvas1.getHeight()/2-100,paint);




            float widthRatio = (float) canvas1.getWidth() / MODEL_WIDTH;
            float heightRatio = (float) canvas1.getHeight() / (2*MODEL_HEIGHT);
            List<KeyPoint> keyPoint = person.getKeyPoints();
            float circleRadius = 8.0f;


            for (int i = 0; i < keyPoint.size(); i++) {
                if (keyPoint.get(i).getScore() > 0.5f) {
                    Position position = keyPoint.get(i).getPosition();
                    float adjustedX = (float) position.getX() * widthRatio;
                    float adjustedY = (float) position.getY() * heightRatio;
                    canvas1.drawCircle(adjustedX, adjustedY, circleRadius, paint);
                }
            }

            for (int i = 1; i <=11; i++) {
                int i1=1,i2=1;
                if(i==1)
                {i1 = 7;
                    i2 = 5;}

                if(i==2)
                { i1 = 6;
                    i2 = 5;}

                if(i==3)
                { i1 = 6;
                    i2 = 8;}

                if(i==4)
                { i1 = 8;
                    i2 = 10;}

                if(i==5)
                { i1 = 11;
                    i2 = 5;}

                if(i==6)
                {i1 =11;
                    i2 = 12;}

                if(i==7)
                {i1 = 6;
                    i2 = 12;}

                if(i==8)
                {i1 = 11;
                    i2 = 13;}

                if(i==9)
                {i1 = 13;
                    i2 = 15;}

                if(i==10)
                {i1 = 12;
                    i2 = 14;}

                if(i==11)
                {i1 = 16;
                    i2 = 14;}


                canvas1.drawLine(
                        keyPoint.get(i1).getPosition().getX() * widthRatio,
                        keyPoint.get(i1).getPosition().getY() * heightRatio,
                        keyPoint.get(i2).getPosition().getX() * widthRatio,
                        keyPoint.get(i2).getPosition().getY() * heightRatio,
                        paint
                );

            }


        }

        else
        if (paintFlag) {
            ourRect2.set(0, canvas1.getHeight() / 2, canvas1.getWidth(), canvas1.getHeight());
           canvas1.drawBitmap(bitmap1,0,canvas1.getHeight() / 2,paint);
            canvas1.drawText("Student Video",0,canvas1.getHeight()-100,paint);


            float widthRatio = (float) canvas1.getWidth() / MODEL_WIDTH;
            float heightRatio = (float) canvas1.getHeight() / (2*MODEL_HEIGHT);
            List<KeyPoint> keyPoint = person.getKeyPoints();
            float circleRadius = 8.0f;


            for (int i = 0; i < keyPoint.size(); i++) {
                if (keyPoint.get(i).getScore() > 0.5f) {
                    Position position = keyPoint.get(i).getPosition();
                    float adjustedX = (float) position.getX() * widthRatio;
                    float adjustedY = canvas1.getHeight()/2+(float) position.getY() * heightRatio;
                    canvas1.drawCircle(adjustedX, adjustedY, circleRadius, paint);
                }
            }

            for (int i = 1; i <=11; i++) {
                int i1=1,i2=1;
                if(i==1)
                {i1 = 7;
                    i2 = 5;}

                if(i==2)
                { i1 = 6;
                    i2 = 5;}

                if(i==3)
                { i1 = 6;
                    i2 = 8;}

                if(i==4)
                { i1 = 8;
                    i2 = 10;}

                if(i==5)
                { i1 = 11;
                    i2 = 5;}

                if(i==6)
                {i1 =11;
                    i2 = 12;}

                if(i==7)
                {i1 = 6;
                    i2 = 12;}

                if(i==8)
                {i1 = 11;
                    i2 = 13;}

                if(i==9)
                {i1 = 13;
                    i2 = 15;}

                if(i==10)
                {i1 = 12;
                    i2 = 14;}

                if(i==11)
                {i1 = 16;
                    i2 = 14;}


                canvas1.drawLine(
                        keyPoint.get(i1).getPosition().getX() * widthRatio,
                        canvas1.getHeight()/2+keyPoint.get(i1).getPosition().getY() * heightRatio,
                        keyPoint.get(i2).getPosition().getX() * widthRatio,
                        canvas1.getHeight()/2+keyPoint.get(i2).getPosition().getY() * heightRatio,
                        paint
                );

            }







        }









    }



    private Bitmap cropBitmap(Bitmap bitmap){

        float bitmapRatio = (float) bitmap.getHeight() / bitmap.getWidth();
        float modelInputRatio = (float) MODEL_HEIGHT / MODEL_WIDTH;
        Bitmap croppedBitmap = bitmap;

        // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
        double maxDifference = 1e-5;

        // Checks if the bitmap has similar aspect ratio as the required model input.
        if ( abs(modelInputRatio - bitmapRatio) < maxDifference )
            return croppedBitmap;
        else if (modelInputRatio < bitmapRatio )
        {
            // New image is taller so we are height constrained.
            float cropHeight = (float) bitmap.getHeight() - ((float) bitmap.getWidth() / modelInputRatio);
            croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    (int) (cropHeight / 5),
                    bitmap.getWidth(),
                    (int) (bitmap.getHeight() - cropHeight / 5)
            );
        }
        else if( modelInputRatio > bitmapRatio ) {
            float cropWidth = (float) bitmap.getWidth() - ((float) bitmap.getHeight() * modelInputRatio);
            croppedBitmap = Bitmap.createBitmap(
                    bitmap,
                    (int)(cropWidth / 5),
                    0,
                    (int) (bitmap.getWidth() - cropWidth / 5),
                    bitmap.getHeight()
            );
        }

        //Log.d(
        //    "IMGSIZE",
        //      "Cropped Image Size (" + string croppedBitmap.getWidth() + ", " + croppedBitmap.height.toString() + ")"
        //);
        return croppedBitmap;

    }


}