package adk.selectorswitch;

import android.content.Context;
import android.graphics.Paint;

import java.util.List;

/**
 * Cooked by ADK96r on 21 Nov'17.
 */

public class SelectorDial {

    private static final int DIAL_RADIUS_DP = 14;
    private static final int MIN_MODES = 1;
    private static final int MAX_MODES = 8;

    private Context context;
    private float screenDensity;
    private int dialModeCount;
    private int dialRadius;
    private Float modeSweepingAngle;
    private List<Float> modeStartingAngles;
    private List<Paint> dialPaints;
    private List<Integer> dialColors;

    public SelectorDial(Context context, int dialModeCount, int[] dialColors)
            throws IllegalSelectorException {

        this.context = context;
        this.screenDensity = context.getTheme().getResources().getDisplayMetrics().density;

        initialiseDial(dialModeCount);
        this.dialColors = SelectorUtil.arrayToList(dialColors);
        this.dialPaints = SelectorUtil.generateDialPaints(dialModeCount, this.dialColors);
    }

    public SelectorDial(Context context, int dialModeCount, int startingColor, int endingColor)
            throws IllegalSelectorException {

        this.context = context;
        this.screenDensity = context.getTheme().getResources().getDisplayMetrics().density;

        initialiseDial(dialModeCount);
        this.dialColors = SelectorUtil.generateBlendingColors(dialModeCount, startingColor, endingColor);
        this.dialPaints = SelectorUtil.generateDialPaints(dialModeCount, this.dialColors);
    }

    private void initialiseDial(int dialModeCount) throws IllegalSelectorException {

        if (dialModeCount <= 0) {
            throw new IllegalSelectorException("Not enough modes in the selector dial!");
        } else if (dialModeCount > 8) {
            throw new IllegalSelectorException("Too many modes to accommodate in the selector dial!");
        }

        this.dialModeCount = dialModeCount;
        this.dialRadius = SelectorUtil.getPixelsFromDips(DIAL_RADIUS_DP, screenDensity);
        this.modeSweepingAngle = SelectorUtil.getSweepingAngle(this.dialModeCount);
        this.modeStartingAngles = SelectorUtil.generateStartingAngles(this.dialModeCount,
                this.modeSweepingAngle);
    }

    /**
     * Returns the radius of the dial in pixels.
     *
     * @return dialRadius
     */
    public int getDialRadius() {
        return this.dialRadius;
    }

    /**
     * Returns the list of colors (Integers) for the modes of the dial.
     *
     * @return dialColors   List of colors
     */
    public List<Integer> getDialColors() {
        return this.dialColors;
    }

    /**
     * Updates the colors of the dial and re-initialises the paints based on the new
     * colors.
     *
     * @param dialColors New list of colors(Integers).
     */
    public void setDialColors(List<Integer> dialColors) {
        this.dialColors = dialColors;
        this.dialPaints = SelectorUtil.generateDialPaints(dialModeCount, dialColors);
    }

    /**
     * Generates a list of colors blending from the starting color to the ending color.
     * Updates the colors of the dial and re-initialises the paints based on the this
     * generated list of colors.
     *
     * @param startingColor Color to start blending from.
     * @param endingColor   Color to end blending at.
     */
    public void setDialColors(int startingColor, int endingColor) {
        this.dialColors = SelectorUtil.generateBlendingColors(dialModeCount, startingColor, endingColor);
        this.dialPaints = SelectorUtil.generateDialPaints(dialModeCount, dialColors);
    }

    /**
     * Sets a particular mode's color.
     *
     * @param index Index of the mode.
     * @param color New color for the mode.
     */
    public void setModeColor(int index, int color) {
        this.dialColors.set(index, color);
        this.dialPaints = SelectorUtil.generateDialPaints(dialModeCount, dialColors);
    }

    /**
     * Returns the number of modes in the dial.
     *
     * @return dialModeCount
     */
    public int getDialModeCount() {
        return dialModeCount;
    }

    /**
     * Updates the number of modes in the dial. If the new count is more than the current
     * count, current starting and ending colors are used to create a list of blending
     * colors to spread across the new set of modes. If the new count is less or equal to
     * than the current count, the colors list is replaced by its subset of a size equal
     * to the new count. Also updates the sweeping and the starting angles.
     * <p>
     * Throws an exception if the new number of states not between MIN_MODES and MAX_MODES.
     *
     * @param dialModeCount New number of modes.
     * @throws IllegalSelectorException
     */
    public void setDialModeCount(int dialModeCount) throws IllegalSelectorException {

        // Check the validity of the new count.
        if (dialModeCount < MIN_MODES) {
            throw new IllegalSelectorException("Too few modes to select!");
        } else if (dialModeCount > MAX_MODES) {
            throw new IllegalSelectorException("Too many modes to accommodate!");
        }

        if (this.dialModeCount < dialModeCount) {   // Blend.
            this.dialColors = SelectorUtil.generateBlendingColors(dialModeCount, this.dialColors.get(0),
                    this.dialColors.get(this.dialColors.size() - 1));
        } else {                                    // Slice.
            this.dialColors = dialColors.subList(0, dialModeCount);
        }

        // Update the dial count and angles.
        this.dialModeCount = dialModeCount;
        this.modeSweepingAngle = SelectorUtil.getSweepingAngle(dialModeCount);
        this.modeStartingAngles = SelectorUtil.generateStartingAngles(dialModeCount, modeSweepingAngle);
    }

    /**
     * Returns the angle between two successive modes in the dial.
     *
     * @return modeSweepingAngle
     */
    public Float getModeSweepingAngle() {
        return modeSweepingAngle;
    }

    /**
     * Returns the list of starting angles for each mode in the dial.
     *
     * @return modeStartingAngles
     */
    public List<Float> getModeStartingAngles() {
        return modeStartingAngles;
    }

    /**
     * Returns the starting angle of a particular mode in the dial.
     *
     * @param mode Index of the mode.
     * @return startingAngle
     */
    public Float getModeStartingAngle(int mode) {
        if (mode < 0) mode = 0;
        if (mode >= dialModeCount) mode = dialModeCount - 1;
        return modeStartingAngles.get(mode);
    }

    /**
     * Returns the List of paints for each mode in the dial.
     *
     * @return dialPaints
     */
    public List<Paint> getDialPaints() {
        return dialPaints;
    }

    /**
     * Returns the Paint instance for a particular mode in the dial.
     *
     * @param mode Index of the mode.
     * @return paint
     */
    public Paint getDialPaintForMode(int mode) {
        if (mode < 0) mode = 0;
        if (mode >= dialModeCount) mode = dialModeCount - 1;
        return dialPaints.get(mode);
    }

}
