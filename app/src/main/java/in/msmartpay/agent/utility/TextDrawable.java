package in.msmartpay.agent.utility;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class TextDrawable extends Drawable {

private final String text;
private final Paint paint;

public TextDrawable(String text) {
    this.text = text;
    this.paint = new Paint();
    paint.setColor(Color.parseColor("#a94a51"));
    paint.setTextSize(50f);
    paint.setAntiAlias(true);
    paint.setTextAlign(Paint.Align.LEFT);
}

@Override
public void draw(Canvas canvas) {
    canvas.drawText(text, 0, 20, paint);
}

@Override
public void setAlpha(int alpha) {
    paint.setAlpha(alpha);
}

@Override
public void setColorFilter(ColorFilter cf) {
    paint.setColorFilter(cf);
}

@Override
public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
}
}