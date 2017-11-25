package adk.selectorswitch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SelectorSwitch is a UI component for android (5.0+) inspired by the
 * {@link <a href="http://goaheadtakethewheel.com/wp-content/uploads/2016/10/20161018_144815.jpg">ACTIVE DYNAMICS PANEL</a>}
 * found in most of the McLaren road cars. A SelectorSwitch has
 * {@link <a href="https://www.github.com/adk96r/Selector-Switch-UI-Component/">this</a>} structure.
 * <p>
 * The user can rotate the rotary knob around a dial to select the various modes. The developer
 * can choose to have anywhere from one to eight modes in the switch's dial and each mode has to
 * have a name and a colour.
 * <p>
 * The names and colors for the modes can be provided in form two array resources. These resources
 * are then linked to the switch's XML code in the layout file via the :modes and the :colors
 * attributes respectively.
 * <p>
 * If no modes or colors are specified, or if only either one of them is specified, or if both of
 * them are specified but their lengths are unequal the SelectorSwitch will use the default modes
 * {@link #DEFAULT_MODES} and the {@link #DEFAULT_DIAL_COLORS}.
 */

public class SelectorSwitch extends View {

    /**
     * The default colors for the dial of the selector.
     */
    private static final List<Integer> DEFAULT_DIAL_COLORS =
            SelectorUtil.arrayToList(new int[]{Color.HSVToColor(new float[]{29f, 23.1f, 94.9f}),
                    Color.HSVToColor(new float[]{59f, 26.3f, 89.4f}),
                    Color.HSVToColor(new float[]{129f, 21.7f, 77.6f})});

    /**
     * The default modes for the selector switch.
     */
    private static final List<String> DEFAULT_MODES =
            Arrays.asList("LOW", "MID", "HIGH");

    /**
     * The default mode is 0.
     */
    private static final int DEFAULT_MODE = 0;

    /**
     * The default step angle used while animating the rotation of the selector knob.
     */
    private static final float STEP_ANGLE = 0.1f;


    /**
     * The default delay, in nanoseconds, between successive increments in knob rotation.
     */
    private static final int STEP_WAIT_NANOS = 400;

    /**
     * The default space in DP Units used as an additional padding on top of the
     * padding specified in the layout.
     */
    private static final int SPACE_DIP = 6;

    /**
     * The default radius of the switch's base in DP Units.
     */
    private static final int BASE_RADIUS_DIP = 16;

    /**
     * The default shadow radius of the switch's base in DP Units.
     */
    private static final int BASE_SHADOW_RADIUS = 1;

    /**
     * The default shadow radius of the knob's base in DP Units.
     */
    private static final int KNOB_SHADOW_RADIUS = 1;

    /**
     * The default color of the base's shadow.
     */
    private static final int BASE_SHADOW_COLOR = Color.LTGRAY;

    /**
     * The default color of the knob's shadow.
     */
    private static final int KNOB_SHADOW_COLOR = Color.DKGRAY;

    /**
     * The default top and bottom margin of the
     * mode's base in DP units.
     */
    private static final int MODE_BASE_MARGIN_DP = 4;

    /**
     * The default top and bottom padding for
     * the mode's base in DP units.
     */
    private static final int MODE_BASE_PADDING_V_DP = 2;

    /**
     * The default left and right padding for
     * the mode's base in DP units.
     */
    private static final int MODE_BASE_PADDING_H_DP = 4;

    /**
     * The default height of the mode's base in DP units.
     */
    private static final int MODE_BASE_HEIGHT_DP = 8;

    /**
     * The default text size for the mode's name.
     */
    private static final int MODE_TEXT_SIZE = 6;

    /**
     * The default text color for the mode's name.
     */
    private static final int MODE_TEXT_COLOR = Color.BLACK;

    /**
     * Stores a context to get the screen's density for conversion of DP
     * units to pixels.
     */
    Context context;

    /**
     * Stores the density of the screen obtained from the {@link #context}.
     */
    float screenDensity;


    /* Switch Properties */

    /**
     * Stores the additional padding, used by the switch on top of the padding
     * from layout XML, in pixels.
     */
    private int space;

    /**
     * Stores the X coordinate of the center of the switch's position on the canvas.
     */
    private int centerX;

    /**
     * Stores the Y coordinate of the center of the switch's position on the canvas.
     */
    private int centerY;

    /**
     * Stores the list of the names of the modes used in the switch.
     */
    private List<String> modes;

    /**
     * Stores the mode the selector switch is currently in, i.e, the mode currently
     * selected.
     */
    private int currentMode;

    /**
     * Stores the total number of modes in the switch.
     */
    private int totalModes;

    /* Base Properties */
    /**
     * Stores the radius of the switch's base in pixels.
     */
    private int baseRadius;

    /**
     * Stores the paint used to draw the switch's base on the canvas.
     */
    private Paint basePaint;

    /* Dial Properties. */
    /**
     * Stores the selector dial used in the selector switch. This has all the data
     * associated with the dial.
     *
     * @see SelectorDial
     */
    private SelectorDial selectorDial;

    /**
     * Stores the int[] of colors for the different modes of the switch.
     */
    private List<Integer> selectorDialColors;

    /**
     * Stores the boundaries that define the position of the selector dial on the
     * canvas.
     *
     * @see RectF
     */
    private RectF selectorDialRectF;

    /* Knob Properties. */

    /**
     * Stores the selector knob used in the selector switch. This stores all the data
     * associated to the knob.
     *
     * @see SelectorKnob
     */
    private SelectorKnob selectorKnob;

    /**
     * Stores the paint used to draw the knob on the canvas.
     */
    private Paint knobPaint;

    /**
     * Stores the angle needed to rotate by the knob between two successive modes of
     * the switch.
     */
    private float knobSweepAngle;

    /**
     * Stores the boundaries of the mode base.
     */
    private RectF modeBaseRectF;

    /**
     * Stores the Paint used to draw the mode's base.
     */
    private Paint modeBasePaint;

    /**
     * Stores the top & bottom padding of the mode's base in pixels.
     */
    private int modeBasePaddingV;

    /**
     * Stores the height of the mode's base in pixels.
     */
    private int modeBaseHeight;

    /**
     * Stores the mode base's left coordinates for the various
     * modes.
     */
    private List<Float> modeBaseLefts;

    /**
     * Stores the mode base's right coordinates for the various
     * modes.
     */
    private List<Float> modeBaseRights;

    /**
     * Stores the top coordinate of the mode name's base.
     */
    private float modeNameTop;

    /**
     * Stores the left coordinates of the mode names when drawn on
     * the canvas.
     */
    private List<Float> modeNameLefts;

    /**
     * Paint used to write the name of the mode onto the canvas.
     */
    private Paint modeNamePaint;

    /**
     * Initialises all the parameters of the selector switch. First obtains a screen density
     * and then initiates the modes and colors to default values by calling
     * {@link #initModesAndColors(AttributeSet)}, followed by call to {@link #initComponents()}
     * to initialise the main components - the knob, the dial and the base.
     *
     * @param context <tt>Context</tt> The context used to get the screen's density.
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
     * Initialises all the parameters of the selector switch. First obtains a screen density
     * and then initiates the modes and colors by calling {@link #initModesAndColors(AttributeSet)}
     * ,followed by call to {@link #initComponents()} to initialise the main components - the knob
     * ,the dial and the base.
     *
     * @param context <tt>Context</tt> The context used to get the screen's density.
     * @param attrs   <tt>AttributeSet</tt> The attributes obtained from the component's
     *                XML in the layout file.
     * @throws IllegalSelectorException If the the number of modes and the number of colors
     *                                  provided are not equal.
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
     * component's XML code from the layout files. Unless both the modes and colors are
     * specified default values will be used.
     * <p>
     * Throws an illegal selector exception if there isn't a color for each mode.
     *
     * @param attrs <tt>AttributeSet</tt> The attribute set obtained from the XML of the
     *              switch in the layout file.
     * @throws IllegalSelectorException If the the number of modes and the number of colors
     *                                  provided are not equal.
     * @see android.content.Context#obtainStyledAttributes(AttributeSet, int[])
     * @see SelectorUtil
     */
    private void initModesAndColors(AttributeSet attrs) throws IllegalSelectorException {

        this.modes = DEFAULT_MODES;
        this.selectorDialColors = DEFAULT_DIAL_COLORS;
        this.currentMode = 0;
        this.totalModes = modes.size();

        int refColors;
        int refModes;
        TypedArray xmlCode = context.obtainStyledAttributes(attrs, R.styleable.SelectorSwitch);

        if (attrs == null)
            return;

        try {
            // Check if any reference to XML having the modes and their colors
            // has been provided in the component's layout xml.
            refColors = xmlCode.getResourceId(R.styleable.SelectorSwitch_colors, 0);
            refModes = xmlCode.getResourceId(R.styleable.SelectorSwitch_modes, 0);

            // Not provided
            if (refColors == 0 || refModes == 0) return;

            this.modes = Arrays.asList(context.getResources().getStringArray(refModes));
            this.totalModes = modes.size();
            this.selectorDialColors =
                    SelectorUtil.arrayToList(context.getResources().getIntArray(refColors));

        } finally {
            xmlCode.recycle();
        }

        // Check if every mode has a color.
        if (this.modes.size() != this.selectorDialColors.size()) {
            throw new IllegalSelectorException("Unequal number of modes and colors.");
        }

    }

    /**
     * Initialises the structural properties of various parts of the selector switch which
     * include the base, the dial and the knob. Also initialises the paints used to draw
     * these parts on the canvas.
     * <p>
     * Uses a few utility static methods provided in {@link SelectorUtil} class.
     *
     * @throws IllegalSelectorException If the the number of modes and the number of colors
     *                                  provided are not equal.
     * @see SelectorDial
     * @see SelectorKnob
     * @see SelectorUtil
     */
    private void initComponents() throws IllegalSelectorException {

        // First, the initial state of the selector switch.
        this.currentMode = DEFAULT_MODE;
        this.totalModes = this.modes.size();

        // Then the footprint of the component.
        space = SelectorUtil.getPixelsFromDips(SPACE_DIP, screenDensity);
        baseRadius = SelectorUtil.getPixelsFromDips(BASE_RADIUS_DIP, screenDensity);
        centerX = space + baseRadius;
        centerY = space + baseRadius;

        // Next, the dial.
        selectorDial = new SelectorDial(context, this.modes.size(), selectorDialColors);
        selectorDial.setDialColors(selectorDialColors);
        int selectorDialRadius = selectorDial.getDialRadius();
        selectorDialRectF = new RectF(centerX - selectorDialRadius,
                centerY - selectorDialRadius,
                centerX + selectorDialRadius,
                centerY + selectorDialRadius);

        // After that the knob.
        selectorKnob = new SelectorKnob(context, centerX, centerY);
        knobSweepAngle = SelectorUtil.getSweepingAngle(totalModes);

        // Now the paints.
        basePaint = SelectorUtil.createPaintFromColor(Color.WHITE, Paint.Style.FILL, true,
                BASE_SHADOW_COLOR, SelectorUtil.getPixelsFromDips(BASE_SHADOW_RADIUS, screenDensity));
        setLayerType(LAYER_TYPE_SOFTWARE, basePaint);

        knobPaint = SelectorUtil.createPaintFromColor(Color.WHITE, Paint.Style.FILL, true,
                KNOB_SHADOW_COLOR, SelectorUtil.getPixelsFromDips(KNOB_SHADOW_RADIUS, screenDensity));
        setLayerType(LAYER_TYPE_SOFTWARE, knobPaint);

        // Next, the mode base.

        int modeBaseMargin = SelectorUtil.getPixelsFromDips(MODE_BASE_MARGIN_DP, screenDensity);
        modeBasePaddingV = SelectorUtil.getPixelsFromDips(MODE_BASE_PADDING_V_DP, screenDensity);

        int modeBasePaddingH = SelectorUtil.getPixelsFromDips(MODE_BASE_PADDING_H_DP, screenDensity);
        modeBaseLefts = new ArrayList<>(totalModes);
        modeBaseRights = new ArrayList<>(totalModes);
        modeBaseHeight = SelectorUtil.getPixelsFromDips(MODE_BASE_HEIGHT_DP, screenDensity);
        modeBaseRectF = new RectF(0, centerY + baseRadius + modeBaseMargin,
                0, centerY + baseRadius + modeBaseMargin + modeBaseHeight);
        modeBasePaint = SelectorUtil.createPaintFromColor(Color.WHITE, Paint.Style.FILL, true,
                BASE_SHADOW_COLOR, SelectorUtil.getPixelsFromDips(BASE_SHADOW_RADIUS, screenDensity));


        // Finally the mode's name's properties.
        modeNameTop = modeBaseRectF.top + modeBasePaddingV + modeBaseHeight / 2;

        modeNamePaint = SelectorUtil.createPaintFromColor(MODE_TEXT_COLOR, Paint.Style.FILL,
                false, 0, 0);
        modeNamePaint.setTextSize(SelectorUtil.getPixelsFromDips(MODE_TEXT_SIZE, screenDensity));

        modeNameLefts = new ArrayList<>(totalModes);
        Float wid;
        for (String mode : modes) {
            wid = modeNamePaint.measureText(mode) / 2;
            modeBaseLefts.add(centerX - wid - modeBasePaddingH);
            modeBaseRights.add(centerX + wid + modeBasePaddingH);
            modeNameLefts.add(centerX - wid);
        }
    }


    /**
     * Calculates and sets the dimensions of the view. The component is padded by
     * an additional padding {@link #space} on top of the padding specified in the
     * component's XML code in the layout.
     *
     * @param widthMeasureSpec  <tt>int</tt> : Width
     * @param heightMeasureSpec <tt>int</tt> : Height
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

        width += paddingLeft + space + (2 * baseRadius) + space + paddingRight;
        height += paddingTop + space + (2 * baseRadius) +
                modeBasePaddingV + modeBaseHeight + modeBasePaddingV + space + paddingBottom;

        setMeasuredDimension(width, height);
    }

    /**
     * Draws the 3 main selector components onto the canvas. Gets the required
     * parameters and angles from the respective components.
     *
     * @param canvas <tt>Canvas</tt> : The canvas to be drawn upon.
     * @see SelectorKnob
     * @see SelectorDial
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

        // Draw the mode's base and show the current mode.
        canvas.drawRoundRect(modeBaseLefts.get(currentMode), modeBaseRectF.top,
                modeBaseRights.get(currentMode), modeBaseRectF.bottom,
                modeBaseHeight / 2, modeBaseHeight / 2, modeBasePaint);

        // Show the current mode name.
        canvas.drawText(modes.get(currentMode), modeNameLefts.get(currentMode),
                modeNameTop, modeNamePaint);

    }

    /**
     * Updates the colors used for the modes and re-initialises the paints which will
     * be used to draw the modes. Finally redraws the view.
     *
     * @param startingColor <tt>int</tt> : The starting color for the first mode.
     * @param endingColor   <tt>int</tt> : The ending color for the mode.
     */
    public void setDialColors(int startingColor, int endingColor) {
        this.selectorDialColors = SelectorUtil.generateBlendingColors(totalModes,
                startingColor, endingColor);
        selectorDial.setDialColors(startingColor, endingColor);
        invalidate();
    }

    /**
     * Updates the color and the paint for an individual mode in the dial,
     * indicated by its index, and redraws the view. Does nothing if the
     * index of the mode is not valid.
     *
     * @param modeIndex <tt>int</tt> : The index of the mode whose color has to be updated.
     * @param color     <tt>int</tt> : The new color of the mode.
     */
    public void setColorForDialMode(int modeIndex, int color) {

        if (modeIndex > selectorDial.getDialModeCount()) {
            return;
        }

        selectorDial.setModeColor(modeIndex, color);
        invalidate();

    }

    /**
     * Returns the list of colors used for the modes in the dial.
     *
     * @return {@link #selectorDialColors}
     */
    public List<Integer> getDialColors() {
        return this.selectorDial.getDialColors();
    }

    /**
     * Updates {@link #selectorDialColors} for the selector dial and redraws the
     * view.
     *
     * @param dialColors <tt>List<Integer></tt> : The new list of colors.
     */
    public void setDialColors(@NonNull List<Integer> dialColors) {
        this.selectorDialColors = dialColors;
        selectorDial.setDialColors(dialColors);
        invalidate();
    }

    /**
     * Updates the total number of modes in the selector switch and blends in the current
     * starting and ending color to create new colors for the new intermediate modes (if any).
     * Updates the selector dial's colors redraws the view.
     *
     * @param count <tt>int</tt> : The new count of total modes in the switch.
     * @throws IllegalSelectorException
     */
    public void setModeCount(int count) throws IllegalSelectorException {
        this.selectorDialColors = SelectorUtil.generateBlendingColors(count,
                selectorDialColors.get(0), selectorDialColors.get(totalModes - 1));
        this.totalModes = count;
        selectorDial.setDialModeCount(count);
    }


    /**
     * Selects the specified mode in the switch and rotates the knob to point to
     * the specified mode.
     *
     * @param newMode New mode to select.
     * @see #animateKnob(float)
     */
    public void selectMode(int newMode) {

        float angle;

        if (newMode > currentMode) {
            // Next mode
            angle = (newMode - currentMode) * knobSweepAngle;
            currentMode = newMode % totalModes;
        } else {
            // Previous mode
            angle = (newMode - currentMode) * knobSweepAngle;
            currentMode = (newMode < 0) ? (totalModes - 1) : newMode;
        }

        animateKnob(angle);
        invalidate();

    }

    /**
     * Rotates the knob by a specified angle and animates the rotation.
     *
     * @param rotateBy <tt>float</tt> The angle to rotate the knob by.
     * @see SelectorKnobAnimator
     */
    private void animateKnob(float rotateBy) {

        new SelectorKnobAnimator(this, selectorKnob, rotateBy,
                STEP_ANGLE, STEP_WAIT_NANOS).execute();

    }

    /**
     * Returns the mode the switch is currently in.
     *
     * @return {@link #currentMode}
     */
    public int getCurrentMode() {
        return this.currentMode;
    }

    /**
     * Returns the name of the mode at the specified index.
     *
     * @param index <tt>int</tt> The index of the mode.
     * @return string The name of the mode at the given index.
     */
    public String getCurrentModeName(int index) {
        return modes.get(index);
    }

    /**
     * Switches to the next mode in the dial.
     */
    public void selectNextMode() {
        selectMode(currentMode + 1);
    }

    /**
     * Switches to the previous mode in the dial.
     */
    public void selectPreviousMode() {
        selectMode(currentMode - 1);
    }

    /**
     * Switches to the default mode.
     */
    public void selectDefaultMode() {
        selectMode(DEFAULT_MODE);
    }
}

