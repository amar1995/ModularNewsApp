package com.amar.modularnewsapp.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.*
import androidx.core.graphics.drawable.toBitmap
import androidx.ui.core.ContentScale
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.Image
import androidx.ui.graphics.ImageAsset
import androidx.ui.graphics.asImageAsset
import androidx.ui.graphics.drawscope.drawCanvas
import androidx.ui.layout.preferredSize
import androidx.ui.unit.Dp
import com.amar.modularnewsapp.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@Composable
fun UrlImage(url: String, width: Dp, height: Dp) {
    val context = ContextAmbient.current
    var image by state<ImageAsset?> { null }
    var drawable by state<Drawable?> { null }
//    val sslContext: SSLContext = SSLContext.getInstance("TLS");
//    sslContext.init(null, trustAllCerts, java.security.SecureRandom ());
//    val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory();
//    val client = OkHttpClient.Builder()
//        .sslSocketFactory(sslSocketFactory).addInterceptor(Interceptor() {
//            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//                val original: Request = chain.request();
//                val authorized: Request = original.newBuilder().build();
//                return chain.proceed(authorized);
//            }
//        }).build();
//    println("Image url >>>> " + url)
    onCommit(url) {
        val picasso = Picasso.get()
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                // TODO(lmr): we could use the drawable below
                image = placeHolderDrawable!!.toBitmap(200, 200).asImageAsset()
//                drawable = placeHolderDrawable
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                image = errorDrawable!!.toBitmap(200, 200).asImageAsset()
//                drawable = errorDrawable
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap?.asImageAsset()
            }
        }

        // todo image caching
        picasso
            .load(url)
            .resize(
                (width.value * context.resources.displayMetrics.density).toInt(),
                (width.value * context.resources.displayMetrics.density).toInt()
            )
//            .networkPolicy(NetworkPolicy.OFFLINE)
            .placeholder(R.drawable.progress_loading)
            .error(R.drawable.not_available)
            .into(target)

        onDispose {
            image = null
            drawable = null
            picasso.cancelRequest(target)
        }
    }
    // TODO(lmr): what's the best way to do aspect ratio here, and have the image fill the available
    // width?

    if (image != null) {
        Image(
            asset = image!!,
            modifier = Modifier.preferredSize(height = height, width = width),
            contentScale = ContentScale.Fit
        )
    } else if (drawable != null) {
        androidx.ui.foundation.Canvas(
            modifier = Modifier.preferredSize(
                height = height,
                width = width
            )
        ) {
            drawCanvas { canvas, _ ->
                drawable!!.draw(
                    canvas.nativeCanvas
                )
            }
        }
    }
}