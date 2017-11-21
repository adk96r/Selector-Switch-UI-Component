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
 * Cooked by ADK96r on 21 Nov'17.
 */

public class SelectorSwitch extends View {

    private static final int[] DEFAULT_DIAL_COLORS = new int[]{Color.HSVToColor(new float[]{29f, 23.1f, 94.9f}),
            Color.HSVToColor(new float[]{59f, 26.3f, 89.4f}),
            Color.HSVToColor(new float[]{129f, 21.7f, 77.6f})};

    private static final List<String> DEFAULT_MODES =
            Arrays.asList(new String[]{"LOW", "MID", "HIGH"});


    Context context;
    float screenDensity;
    private int SPACE_DIP = 6;
    private int BASE_RADIUS_DIP = 16;
    private int BASE_SHADOW_RADIUS = 2;
    private int KNOB_SHADOW_RADIUS = 1;
    private int BASE_SHADOW_COLOR = Color.LTGRAY;
    private int KNOB_SHADOW_COLOR = Color.argb(255, 100, 100, 100);

    // Switch Properties
    private int space;
    private int centerX;
    private int centerY;

    // Base Properties
    private int baseRadius;
    private Paint basePaint;

    // Dial Properties.
    private SelectorDial selectorDial;
    private int[] selectorDialColors;
    private RectF selectorDialRectF;

    // Knob Properties.
    private SelectorKnob selectorKnob;
    private Matrix knobRotationMatrix;
    private float knobRotationAngle;
    private Paint knobPaint;

    // Current state of the switch
    private List<String> modes;
    private int currentMode;

    /**
     * Initialises the selector switch components.
     *
     * @param context Context to get the screen density.
     * @throws IllegalSelectorException
     */
    public SelectorSwitch(Context context) throws IllegalSelectorException {
        super(context);

        this.context = context;
        this.screenDensity = context.getTheme().getResources().getDisplayMetrics().density;
        initModesAndColors(null);
        initComponents();
    }

    /**
     * Initialises the selector switch components.
     *
     * @param context Context to get the screen density.
     * @param attrs   Attributes from the component's XML in the layout files.
     * @throws IllegalSelectorException
     */
    public SelectorSwitch(Context context, @Nullable AttributeSet attrs) throws IllegalSelectorException {
        super(context, attrs);

        this.context = context;
        this.screenDensity = context.getTheme().getResources().getDisplayMetrics().density;
        initModesAndColors(attrs);
        initComponents();
    }

    /**
     * Initialises the modes and colors for each mode from the resource references in the
     * component's XML code from the layout files. If no modes or colors are specified default
     * values are used for both.
     * <p>
     * Throws an illegal selector exception if there isn't a color for each mode.
     *
     * @param attrs Attribute set of the XML.
     * @throws IllegalSelectorException
     */
    private void initModesAndColors(AttributeSet attrs) throws IllegalSelectorException {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectorSwitch);
        int resId;

        this.modes = DEFAULT_MODES;
        this.selectorDialColors = DEFAULT_DIAL_COLORS;

        if (attrs == null)
            return;

        try {
            // First get the modes in the dial.
            resId = typedArray.getResourceId(R.styleable.SelectorSwitch_modes, 0);
            if (resId != 0)
                this.modes = Arrays.asList(context.getResources().getStringArray(resId));

            // Next get the colors for each mode in the dial.
            resId = typedArray.getResourceId(R.styleable.SelectorSwitch_dialColors, 0);
            if (resId != 0)
                this.selectorDialColors = context.getResources().getIntArray(resId);

        } finally {
            typedArray.recycle();
        }

        // Check if every mode has a color.
        if (this.modes.size() != this.selectorDialColors.length) {
            throw new IllegalSelectorException("Unequal number of modes and colors.");
        }

    }

    /**
     * Initialises the structural properties of various parts of the selector switch which
     * include the base, the dial and the knob. Also initialise the paints used on these
     * parts.
     * <p>
     * Could rethrow an illegal selector exception thrown while constructing the selection
     * dial.
     *
     * @throws IllegalSelectorException
     */
    private void initComponents() throws IllegalSelectorException {

        // First the footprint of the component.
        space = SelectorUtil.getPixelsFromDips(SPACE_DIP, screenDensity);
        baseRadius = SelectorUtil.getPixelsFromDips(BASE_RADIUS_DIP, screenDensity);
        centerX = space + baseRadius;
        centerY = space + baseRadius;


        // Next, the dial.
        selectorDial = new SelectorDial(context, this.modes.size(), selectorDialColors);
        int selectorDialRadius = selectorDial.getDialRadius();
        selectorDialRectF = new RectF(centerX - selectorDialRadius,
                centerY - selectorDialRadius,
                centerX + selectorDialRadius,
                centerY + selectorDialRadius);

        // Then, the knob.
        selectorKnob = new SelectorKnob(context, centerX, centerY);

        // Now the paints.
        basePaint = SelectorUtil.createPaintFromColor(Color.WHITE, Paint.Style.FILL, true,
                BASE_SHADOW_COLOR, SelectorUtil.getPixelsFromDips(BASE_SHADOW_RADIUS, screenDensity));
        setLayerType(LAYER_TYPE_SOFTWARE, basePaint);

        knobPaint = SelectorUtil.createPaintFromColor(Color.WHITE, Paint.Style.FILL, true,
                KNOB_SHADOW_COLOR, SelectorUtil.getPixelsFromDips(KNOB_SHADOW_RADIUS, screenDensity));
        setLayerType(LAYER_TYPE_SOFTWARE, knobPaint);

        // Finally, the initial state of the selector switch.
        this.currentMode = 0;
        this.knobRotationAngle = 0.0f;
        this.knobRotationMatrix = new Matrix();
        this.knobRotationMatrix.postRotate(0, centerX, centerY);

    }


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

    /**
     * Draws the selector component on the canvas
     *
     * @param canvas Canvas to be drawn upon.
     */
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
                    true, selectorDial.getDialPaintForMode(mode));
        }

        // Draw the knob and the notch.
        canvas.drawPath(selectorKnob.getKnobPath(), knobPaint);

    }

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

