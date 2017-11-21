package adk.selectorswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ADK96r on 11/20/2017.
 */

public class SelectorSwitch extends View {

    Context context;

    // Utilities
    SelectorUtil selectorUtil;

    // Component Properties
    private int space;
    private int centerX;
    private int centerY;

    // Base Properties
    private int baseRadius;
    private Paint basePaint;

    // Selector Dial
    private SelectorDial selectorDial;
    private RectF selectorDialRectF;

    // Selector Knob
    private SelectorKnob selectorKnob;
    private Matrix knobRotationMatrix;
    private float knobRotationAngle;
    private Paint knobPaint;

    // Current state of the switch
    private List<String> modes;
    private int currentMode;

    public SelectorSwitch(Context context) throws IllegalSelectorException {
        super(context);
        initialise(context, null);
    }

    public SelectorSwitch(Context context, @Nullable AttributeSet attrs) throws IllegalSelectorException {
        super(context, attrs);
        initialise(context, attrs);
    }

    private void initialise(Context context, AttributeSet attrs) throws IllegalSelectorException {

        this.context = context;
        this.selectorUtil = new SelectorUtil(context);

        // First get the modes for the selector switch.
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorSwitch);
        try {
            int resId = typedArray.getResourceId(R.styleable.SelectorSwitch_modes, R.array.modes);
            this.modes = Arrays.asList(context.getResources().getStringArray(resId));
        } finally {
            typedArray.recycle();
        }

        // Footprint of the switch
        space = selectorUtil.getPixelsFromDips(6);
        baseRadius = selectorUtil.getPixelsFromDips(16);
        centerX = space + baseRadius;
        centerY = space + baseRadius;
        basePaint = selectorUtil.createPaintFromColor(Color.WHITE, Paint.Style.FILL, true, Color.LTGRAY);
        setLayerType(LAYER_TYPE_SOFTWARE, basePaint);

        // Setup the Dial.
        selectorDial = new SelectorDial(selectorUtil, this.modes.size());
        int selectorDialRadius = selectorDial.getDialRadius();
        selectorDialRectF = new RectF(centerX - selectorDialRadius,
                centerY - selectorDialRadius,
                centerX + selectorDialRadius,
                centerY + selectorDialRadius);

        // Setup the Knob.
        selectorKnob = new SelectorKnob(centerX, centerY, selectorUtil);
        knobRotationAngle = 0.0f;
        knobRotationMatrix = new Matrix();
        knobPaint = selectorUtil.createPaintFromColor(Color.WHITE, Paint.Style.FILL, true,
                Color.argb(255, 100, 100, 100));
        setLayerType(LAYER_TYPE_SOFTWARE, knobPaint);

        // Setup the initial state for the switch.
        this.currentMode = 0;
        knobRotationMatrix = new Matrix();
        knobRotationMatrix.postRotate(0, centerX, centerY);

    }


    /**
     * Sets the structural properties of both the dial and the knob.
     * <p>
     * dialModeCount = 3
     * dialRadius = 16
     * knobRadius1 = 4
     * knobRadius2 = 1
     * knobLength = 3
     * knob initial angle = 0
     */

    /**
     * Calculate and set the dimensions of the view.
     *
     * @param widthMeasureSpec  Width
     * @param heightMeasureSpec Height
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

        // Draw the selector dial.
        for (int mode = 0; mode < selectorDial.getDialModeCount(); mode++) {
            canvas.drawArc(selectorDialRectF,
                    selectorDial.getModeStartingAngle(mode),
                    selectorDial.getModeSweepingAngle(),
                    true, selectorDial.getDailPaintForMode(mode));
        }

        // Draw the knob and the notch.
        canvas.drawPath(selectorKnob.getKnobPath(), knobPaint);

    }


    // Selector Dial Operations //

    public SelectorDial getSelectorDial() {
        return this.selectorDial;
    }

    /**
     * First computes a sequence of colors blending from the starting color to the ending color.
     * Then updates the dial colors and paints and finally redraws the view after updating the
     * colors and paints.
     *
     * @param startingColor Starting color for mode 0.
     * @param endingColor   Ending color for mode (dialModeCount-1).
     */
    public void setDialColors(int startingColor, int endingColor) {
        selectorDial.setDialColors(startingColor, endingColor);
        // Redraw
    }

    /**
     * Update the color for an individual mode in the dial and redraw the view.
     * Do nothing if the modeIndex is not valid.
     *
     * @param modeIndex Index of the mode in the dial.
     * @param color     New color of the mode.
     */
    public void setColorForDialMode(int modeIndex, int color) {

        if (modeIndex > selectorDial.getDialModeCount()) {
            return;
        }

        selectorDial.setModeColor(modeIndex, color);

        // Redraw the view.

    }

    /**
     * Returns the list of colors for the modes in the dial.
     *
     * @return dialColors
     */
    public List<Integer> getDialColors() {
        return this.selectorDial.getDialColors();
    }

    /**
     * Update the colors in the selector dial.
     *
     * @param dialColors List of colors.
     */
    public void setDialColors(@NonNull List<Integer> dialColors) {
        selectorDial.setDialColors(dialColors);
        // Redraw
    }

    /**
     * Updates the total number of modes in the selector switch and blends in the current starting
     * and ending color to accommodate the new modes. Setting the dial colors redraws the views.
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
        selectorDial.setDialModeCount(count);
    }

}

