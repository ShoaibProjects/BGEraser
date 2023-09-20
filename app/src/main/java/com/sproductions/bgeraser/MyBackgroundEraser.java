package com.sproductions.bgeraser;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class MyBackgroundEraser {
    private static final String MODEL_PATH = "tfmodel.tflite";
    private static final int IMAGE_MEAN = 127;
    private static final float IMAGE_STD = 128.0f;
    private static final int INPUT_IMAGE_SIZE = 513;
    private static final int OUTPUT_MASK_SIZE = 513;
    private static final int NUM_CLASSES = 21;
    private static final int BYTES_PER_CHANNEL = 4;
    private static final int NUM_THREADS = 4;

    private final Interpreter tflite;

    public MyBackgroundEraser(Context context) throws IOException {
        // Load the model from assets folder
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(NUM_THREADS);
        tflite = new Interpreter(loadModelFile(context), options);
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    public Bitmap eraseBackground(Bitmap inputImage) {
        // Rotate the input image based on its EXIF metadata
        Matrix matrix = new Matrix();
        try {
            ExifInterface exif = new ExifInterface(inputImage.toString());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap rotatedImage = Bitmap.createBitmap(inputImage, 0, 0, inputImage.getWidth(), inputImage.getHeight(), matrix, true);

        // Preprocess the input image
        Bitmap resizedImage = Bitmap.createScaledBitmap(rotatedImage, INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE, true);
        ByteBuffer inputBuffer = preprocessImage(resizedImage);

        // Allocate memory for the output mask
        ByteBuffer outputBuffer = ByteBuffer.allocateDirect(OUTPUT_MASK_SIZE * OUTPUT_MASK_SIZE * NUM_CLASSES * BYTES_PER_CHANNEL);
        outputBuffer.order(ByteOrder.nativeOrder());

        // Run the inference
        Object[] inputArray = {inputBuffer};
        tflite.runForMultipleInputsOutputs(inputArray, getOutputMap(outputBuffer));

        // Postprocess the output mask and apply it to the input image
        Bitmap outputMask = postprocessOutputMask(outputBuffer);
        Bitmap outputImage = Bitmap.createBitmap(outputMask.getWidth(), outputMask.getHeight(), Bitmap.Config.ARGB_8888);
        RectF srcRect = new RectF(0, 0, outputMask.getWidth(), outputMask.getHeight());
        RectF dstRect = new RectF(0, 0, inputImage.getWidth(), inputImage.getHeight());
        Matrix maskToImageMatrix = new Matrix();
        maskToImageMatrix.setRectToRect(srcRect, dstRect, Matrix.ScaleToFit.CENTER);
        maskToImageMatrix.postConcat(matrix);
        Bitmap maskedImage = Bitmap.createBitmap(inputImage.getWidth(), inputImage.getHeight(), Bitmap.Config.ARGB_8888);
        outputMask = Bitmap.createScaledBitmap(outputMask, inputImage.getWidth(), inputImage.getHeight(), true);
        maskedImage.setHasAlpha(true);
        Canvas canvas = new Canvas(maskedImage);
        canvas.drawBitmap(outputMask, maskToImageMatrix, null);

        // Release resources
        inputImage.recycle();
        rotatedImage.recycle();
        resizedImage.recycle();
        outputMask.recycle();
        inputBuffer.clear();
        outputBuffer.clear();

        return maskedImage;
    }

    private ByteBuffer preprocessImage(Bitmap image) {
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(1 * INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE * 3 * BYTES_PER_CHANNEL);
        inputBuffer.order(ByteOrder.nativeOrder());
        inputBuffer.rewind();

        int[] pixels = new int[INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE];
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        int pixel = 0;
        for (int i = 0; i < INPUT_IMAGE_SIZE; ++i) {
            for (int j = 0; j < INPUT_IMAGE_SIZE; ++j) {
                final int val = pixels[pixel++];
                inputBuffer.putFloat(((val >> 16) & 0xFF) / IMAGE_STD);
                inputBuffer.putFloat(((val >> 8) & 0xFF) / IMAGE_STD);
                inputBuffer.putFloat((val & 0xFF) / IMAGE_STD);
            }
        }
        return inputBuffer;
    }

    private Map<Integer, Object> getOutputMap(ByteBuffer outputBuffer) {
        Map<Integer, Object> outputMap = new HashMap<>();
        outputMap.put(0, outputBuffer);
        return outputMap;
    }

    private Bitmap postprocessOutputMask(ByteBuffer outputBuffer) {
        outputBuffer.rewind();
        int[] colors = new int[NUM_CLASSES];
        for (int i = 0; i < NUM_CLASSES; i++) {
            colors[i] = outputBuffer.getInt();
        }

        int[] pixels = new int[OUTPUT_MASK_SIZE * OUTPUT_MASK_SIZE];
        int pixel = 0;
        for (int i = 0; i < OUTPUT_MASK_SIZE; ++i) {
            for (int j = 0; j < OUTPUT_MASK_SIZE; ++j) {
                int argmaxIndex = 0;
                int argmaxValue = Integer.MIN_VALUE;
                for (int k = 0; k < NUM_CLASSES; ++k) {
                    int value = (colors[k] >> (8 * i)) & 0xFF;
                    if (value > argmaxValue) {
                        argmaxIndex = k;
                        argmaxValue = value;
                    }
                }
                pixels[pixel++] = argmaxIndex == 0 ? 0x00000000 : (0xFF000000 | colors[argmaxIndex]);
            }
        }
        return Bitmap.createBitmap(pixels, OUTPUT_MASK_SIZE, OUTPUT_MASK_SIZE, Bitmap.Config.ARGB_8888);
    }
}
