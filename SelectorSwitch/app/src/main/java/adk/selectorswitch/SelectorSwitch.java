package adk.selectorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adu on 11/20/2017.
 */

public class SelectorSwitch extends View {

    private final static int SIZE_NORMAL = -1;
    private final static int SIZE_MINI = -2;
    private final static int SIZE_MAX = -3;

    Context context;

    // Component Properties
    int space;
    int shadowRadius = 8;
    int lightShadow = Color.LTGRAY;
    int darkShadow = Color.DKGRAY;
    int centerX;
    int centerY;

    // Base Properties
    int baseRadius;
    Paint basePaint;

    // Selector Dial
    int modeCount;
    int modeRadius;
    Float modeSweepingAngle;
    List<Float> modeStartingAngles;
    List<Paint> modePaints;
    List<Integer> modeColors;

    // Knob
    int knobRadius;
    int notchRadius;
    int notchHandleLength;
    Paint knobPaint;
    Path knobAndNotchPath;
    Matrix knobRotationMatrix;


    public SelectorSwitch(Context context) {
        super(context);
        init(context, 0);
    }

    public SelectorSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, 0);
    }

    private void init(Context context, int size) {

        this.context = context;

        // Common for all sizes
        space = getPixelsFromDips(6);
        baseRadius = getPixelsFromDips(18);
        centerX = space + baseRadius;
        centerY = space + baseRadius;

        basePaint = createPaint(Color.WHITE, Paint.Style.FILL, true, Color.LTGRAY);


        // Size specific properties
        switch (size) {
            case SIZE_MINI:
            case SIZE_MAX:
            case SIZE_NORMAL:
            default:
                initNormalMode();
        }



    }

    /**
     * Initiates the selector for a normal size, where the properties are -
     *
     * modeCount = 3
     * modeRadius = 16
     * knobRadius = 4
     * notchRadius = 1
     * notchHandleLength = 3
     * knob initial angle = 0
     *
     */
    void initNormalMode() {

        modeCount = 3;
        modeSweepingAngle = getSweepingAngle(modeCount);
        modeStartingAngles = getStartingAngles(modeCount, modeSweepingAngle);

        // Set default 3 colors.
        float[][] colors = {{29f, 23.1f, 94.9f}, {59f, 26.3f, 89.4f}, {129f, 21.7f, 77.6f}};
        modeColors = new ArrayList<>(this.modeCount);
        modeColors.add(Color.HSVToColor(colors[0]));
        modeColors.add(Color.HSVToColor(colors[1]));
        modeColors.add(Color.HSVToColor(colors[2]));
        modePaints = getModePaints(modeCount, modeColors);

        // Exclusive for Normal Mode Selector Switch.
        modeRadius = getPixelsFromDips(16);
        knobRadius = getPixelsFromDips(4);
        notchRadius = getPixelsFromDips(1);
        notchHandleLength = getPixelsFromDips(3);
        knobPaint = createPaint(Color.WHITE, Paint.Style.FILL, true, Color.argb(255, 100, 100, 100));

        // Draw the knob and the notch.
        knobAndNotchPath = new Path();

        // Central Knob.
        knobAndNotchPath.addArc(centerX - knobRadius, centerY - knobRadius, centerX + knobRadius, centerY + knobRadius, 230, 260);

        // The End Notch.
        knobAndNotchPath.addArc(centerX - knobRadius - notchHandleLength - notchRadius, centerY - notchRadius,
                centerX - knobRadius - notchHandleLength + notchRadius, centerY + notchRadius, 90, 180);

        // The Handle between them.
        knobAndNotchPath.moveTo(centerX - notchHandleLength + notchRadius / 2, centerY - knobRadius * 3 / 4);
        knobAndNotchPath.lineTo(centerX - knobRadius - notchHandleLength, centerY - notchRadius);
        knobAndNotchPath.lineTo(centerX - knobRadius - notchHandleLength, centerY + notchRadius);
        knobAndNotchPath.lineTo(centerX - notchHandleLength + notchRadius / 2, centerY + knobRadius * 3 / 4);
        knobAndNotchPath.close();

        knobRotationMatrix = new Matrix();
        knobRotationMatrix.postRotate(0, centerX, centerY);
        knobAndNotchPath.transform(knobRotationMatrix);

    }

    /**
     * Calculate and set the dimensions of the view.
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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
        for (int i = 0; i < modeCount; i++) {
            canvas.drawArc(centerX - modeRadius, centerY - modeRadius, centerX + modeRadius, centerY + modeRadius,
                    modeStartingAngles.get(i), modeSweepingAngle, true, modePaints.get(i));
        }

        // Draw the knob and the notch.
        canvas.drawPath(knobAndNotchPath, knobPaint);

    }


    /**
     * Converts DIPs to Pixels.
     *
     * @param dps DIPs.
     * @return pixels   Equivalent to the given DIPs.
     */
    private int getPixelsFromDips(int dps) {
        float density = context.getTheme().getResources().getDisplayMetrics().density;
        return (int) (density * dps / 0.5);
    }

    /**
     * Returns a Paint instance created as per the required specs.
     *
     * @param color         Color of the paint.
     * @param style         Fill, Stroke, etc.
     * @param shadowEnabled Enables shadow for anything using this paint.
     * @param shadowColor   Color of the shadow if shadow is enabled.
     * @return paint        Paint instance.
     */
    private Paint createPaint(int color, Paint.Style style, boolean shadowEnabled, int shadowColor) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(style);
        paint.setAntiAlias(true);
        if (shadowEnabled) {
            paint.setShadowLayer(shadowRadius, 0, 0, shadowColor);
            setLayerType(LAYER_TYPE_SOFTWARE, paint);
        }
        return paint;
    }

    /**
     * Calculates the angle between two successive modes in the selector dial.
     *
     * @param maxModes Number of modes in the selector dial.
     * @return sweepingAngle
     */
    private Float getSweepingAngle(int maxModes) {
        return (float) 360 / maxModes;
    }

    /**
     * Generates the starting angle for each mode position on the dial of the selector.
     *
     * @param modeCount     Number of modes in the selector dial.
     * @param sweepingAngle Angle between the starting angles of two
     *                      successive modes on the dial.
     * @return startingAngles   List of starting angles for each mode in the selector dial.
     */
    private List<Float> getStartingAngles(int modeCount, Float sweepingAngle) {
        List<Float> startingAngles = new ArrayList<>(modeCount);
        for (int i = 0; i < modeCount; i++) {
            startingAngles.add(i * sweepingAngle);
        }
        return startingAngles;
    }

    /**
     * Converts the mode Colors into Paints instances.
     *
     * @param modeCount Number of modes in the selector switch.
     * @param colors    List of Color(integers).
     * @return modePaints  List of Paint instances.
     */
    private List<Paint> getModePaints(int modeCount, List<Integer> colors) {
        List<Paint> modePaints = new ArrayList<>(modeCount);
        for (int i = 0; i < modeCount; i++) {
            if (i < colors.size())
                modePaints.add(createPaint(colors.get(i), Paint.Style.FILL, false, lightShadow));
            else
                modePaints.add(createPaint(Color.BLACK, Paint.Style.FILL, false, lightShadow));
        }
        return modePaints;
    }


    /**
     * Updates the total number of modes in the selector switch.
     * <p>
     * Calls setModeColors after updating the count to assign default colors for the new modes.
     * <p>
     * Finally redraws the view.
     *
     * @param count The new count of selectable modes in the switch (dial).
     * @throws IllegalSelectorException
     */
    public void setModeCount(int count) throws IllegalSelectorException {

        // Check the validity of the new count.
        if (count <= 0) {
            throw new IllegalSelectorException("Too few modes to select!");
        } else if (count > 10) {
            throw new IllegalSelectorException("Too many modes to accommodate!");
        }

        // Update the properties of the selector.
        this.modeCount = count;
        this.modeSweepingAngle = getSweepingAngle(count);
        this.modeStartingAngles = getStartingAngles(count, this.modeSweepingAngle);

        // Assign colors for new mode states.
        setModeColors(this.modeColors);

        // Redraw the view.
    }

    /**
     * Sets the colors the each of the mode positions on the selector dial.
     * <p>
     * If the size of the list is more than the mode count, ignore the extra colors.
     * If the size of the list is less than the mode cound, initialise the remaining
     * colors as a default black.
     * <p>
     * Also update the modePaints list to use the new colors while drawing.
     *
     * @param modeColors List of colors.
     */
    public void setModeColors(@NonNull List<Integer> modeColors) {

        if (modeColors.size() >= this.modeCount) {
            this.modeColors = modeColors.subList(0, this.modeCount);
        } else {
            this.modeColors = modeColors;
            for (int i = 0; i < this.modeCount - modeColors.size(); i++) {
                this.modeColors.add(Color.BLACK);
            }
        }
        this.modePaints = getModePaints(this.modeCount, this.modeColors);
    }

    /**
     * Sets a sequence of colors blending from the starting color to the ending color.
     *
     * @param startingColor Starting color for mode 0.
     * @param endingColor   Ending color for mode (modeCount-1).
     */
    public void setModeColors(@NonNull Color startingColor, @NonNull Color endingColor) {

    }
    
}

