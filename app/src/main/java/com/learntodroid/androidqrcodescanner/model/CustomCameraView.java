package com.learntodroid.androidqrcodescanner.model;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomCameraView extends SurfaceView
{
    Camera camera;
    SurfaceHolder previewHolder;

    public void zoom()
    {
        Parameters params=camera.getParameters();
        params.setZoom(params.getMaxZoom());
        camera.setParameters(params);
    }

    public void unzoom()
    {
        Parameters params=camera.getParameters();
        params.setZoom(0);
        camera.setParameters(params);
    }

    public CustomCameraView(Context context)
    {
        super(context);
        previewHolder=this.getHolder();
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        SurfaceHolder.Callback surfaceHolderListener=new SurfaceHolder.Callback()
        {
            public void surfaceCreated(SurfaceHolder holder)
            {
                camera=Camera.open();
                try{
                    camera.setPreviewDisplay(previewHolder);
                }
                catch (Exception e) {
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                Parameters params=camera.getParameters();
                params.setPreviewSize(width, height);
                params.setPictureFormat(PixelFormat.JPEG);

                //if you want the preview to be zoomed from start :
                params.setZoom(params.getMaxZoom());

                camera.setParameters(params);
                camera.startPreview();
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
            }
        };
        previewHolder.addCallback(surfaceHolderListener);
    }

}