package adk.selectorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adu on 11/20/2017.
 */

public class SelectorSwitch extends View {

    // Other
    Context context;

    // Component Properties
    int space;
    int shadowRadius = 8;
    int shadowColor = Color.LTGRAY;
    int centerX;
    int centerY;

    // Base Properties
    int baseCenterX;
    int baseCenterY;
    int baseRadius;
    Paint basePaint;

    // Selector Dial
    int maxModes;
    Float modeSweepingAngle;
    List<Float> modeStartingAngles;


    public SelectorSwitch(Context context) {
        super(context);
        init(context);
    }

    public SelectorSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        this.context = context;

        space = dpsToPixels(8);
        baseRadius = dpsToPixels(20);

        centerX = space + baseRadius;
        centerY = space + baseRadius;

        basePaint = getPaint(Color.WHITE, Paint.Style.FILL, true);


        maxModes = 3;
        modeSweepingAngle = getSweepingAngle(maxModes);
        modeStartingAngles = getStartingAngles(maxModes, modeSweepingAngle);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getSuggestedMinimumWidth();
        int height = getSuggestedMinimumHeight();

        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();

        width += space + paddingLeft + (2 * baseRadius) + paddingRight + space;
        height += space + paddingTop + (2 * baseRadius) + paddingBottom + space;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the base with shadow.
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint);

        // Draw the dial with different colored modes.
    }

    /**
     * Converts DPs to Pixels.
     *
     * @param dps DP / DIP
     * @return pixels   Pixels equivalent to dps
     */
    private int dpsToPixels(int dps) {
        float density = context.getTheme().getResources().getDisplayMetrics().density;
        return (int) (density * dps / 0.5);
    }

    /**
     * Return a Paint instance generated as per the required specs.
     *
     * @param color         Color of the paint.
     * @param style         Fill, Stroke, etc.
     * @param shadowEnabled Enables shadow for anything using this paint.
     * @return paint           Paint instance.
     */
    private Paint getPaint(int color, Paint.Style style, boolean shadowEnabled) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        if (shadowEnabled) {
            paint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
            setLayerType(LAYER_TYPE_SOFTWARE, paint);
        }
        return paint;
    }

    private Float getSweepingAngle(int maxModes) {
        return (float) 360 / maxModes;
    }

    private List<Float> getStartingAngles(int maxModes, Float sweepingAngle) {
        List<Float> startingAngles = new ArrayList<>(maxModes);
        for (int i = 0; i < maxModes; i++) {
            startingAngles.add(i * sweepingAngle);
        }
        return startingAngles;
    }
}
