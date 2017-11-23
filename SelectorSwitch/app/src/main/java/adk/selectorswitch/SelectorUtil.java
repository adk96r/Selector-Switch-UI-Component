package adk.selectorswitch;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectorUtil is a utility helper class used mainly by {@link SelectorDial} and
 * {@link SelectorKnob} classes. It offers a range of useful static methods that
 * either transform data from one form to another or return objects constructed
 * using some given data.
 *
 * @see SelectorSwitch
 * @see SelectorDial
 * @see SelectorKnob
 */

class SelectorUtil {

    /**
     * Converts an <tt>int[]</tt> to a <tt>List<Integer></tt> and returns it.
     *
     * @param array <tt>int[]</tt> : array to be converted.
     * @return list List<Integer> constructed from array.
     */
    static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>(array.length);
        for (int anArray : array) {
            list.add(anArray);
        }

        return list;
    }

    /**
     * Calculates the angle between two successive modes in the selector dial. Every mode
     * in the circular dial is separated equally.
     *
     * @param maxModes <tt>int</tt> : Number of modes in the selector dial.
     * @return sweepingAngle The angle between 2 adjacent modes.
     */
    static float getSweepingAngle(int maxModes) {
        return (float) 360 / maxModes;
    }

    /**
     * Generates the starting angle for each mode on the dial of the selector.
     *
     * @param dialModeCount <tt>int</tt> : Number of modes in the selector dial.
     * @param sweepingAngle <tt>float</tt> : Angle between the starting angles of two
     *                      successive modes on the dial.
     * @return startingAngles   List of starting angles for each mode in the selector dial.
     */
    static List<Float> generateStartingAngles(int dialModeCount, float sweepingAngle) {
        List<Float> startingAngles = new ArrayList<>(dialModeCount);
        for (int i = 1; i <= dialModeCount; i++) {
            startingAngles.add((i * sweepingAngle));
        }
        return startingAngles;
    }

    /**
     * Generates a list of paint instances based on the mode colors (ints).
     *
     * @param dialModeCount <tt>int</tt> : Number of modes in the selector switch.
     * @param colors        <tt>List<Integer></tt> : List of Color(integers).
     * @return dialPaints  List of Paint instances.
     * @see #createPaintFromColor(int, Paint.Style, boolean, int, int)
     */
    static List<Paint> generateDialPaints(int dialModeCount, List<Integer> colors) {
        List<Paint> dialPaints = new ArrayList<>(dialModeCount);
        for (int i = 0; i < dialModeCount; i++) {
            if (i < colors.size())
                dialPaints.add(createPaintFromColor(colors.get(i), Paint.Style.FILL, false, 0, 0));
            else
                dialPaints.add(createPaintFromColor(Color.BLACK, Paint.Style.FILL, false, 0, 0));
        }
        return dialPaints;
    }

    /**
     * Generates and returns a single paint object, having the given color, used to draw
     * modes in the selector dial. This paint has {@link android.graphics.Paint.Style#FILL}
     * style with no shadow enabled.
     *
     * @param color <tt>int</tt> : Color of the mode.
     * @return paint Paint instance built using the given color.
     * @see SelectorDial#setModeColor(int, int)
     * @see #createPaintFromColor(int, Paint.Style, boolean, int, int)
     */
    static Paint generateDialPaint(int color) {
        return createPaintFromColor(color, Paint.Style.FILL, false, 0, 0);
    }

    /**
     * Generates a list of colors that blend from a starting color to an ending color. The
     * size of the list is same as dialModeCount.
     *
     * @param dialModeCount <tt>int</tt> : Size of the list.
     * @param startingColor <tt>int</tt> : Color to start blending from.
     * @param endingColor   <tt>int</tt> : Color to finally blend into.
     * @return colorList    List of Integers representing the blended colors.
     * @see Color#HSVToColor(float[])
     */
    static List<Integer> generateBlendingColors(int dialModeCount, int startingColor, int endingColor) {

        List<Integer> colorList = new ArrayList<>(dialModeCount);
        float[] startingColorHSV = new float[3];
        float[] endingColorHSV = new float[3];

        Color.colorToHSV(startingColor, startingColorHSV);
        Color.colorToHSV(endingColor, endingColorHSV);

        float hInc = endingColorHSV[0] - startingColorHSV[0];
        float sInc = endingColorHSV[1] - startingColorHSV[1];
        float vInc = endingColorHSV[2] - startingColorHSV[2];
        float[] temp;

        // Generate the blending colors.
        for (int i = 0; i < dialModeCount; i++) {
            temp = new float[]{startingColorHSV[0] + i * hInc,
                    startingColorHSV[1] + i * sInc,
                    startingColorHSV[2] + i * vInc};
            colorList.add(Color.HSVToColor(temp));
        }

        return colorList;
    }

    /**
     * Converts DPs to Pixels based on the screen's pixel density.
     *
     * @param dps <tt>int</tt> : DP units.
     * @return pixels   Pixel count equivalent to the given DP units.
     */
    static int getPixelsFromDips(int dps, float density) {
        return (int) (density * dps / 0.5);
    }

    /**
     * Returns a paint instance created as per the required specs.
     *
     * @param color         <tt>int</tt> : Color of the paint.
     * @param style         <tt>Paint.Style></tt> : Style of the paint. Eg - Fill, Stroke, etc.
     * @param shadowEnabled <tt>boolean</tt> : Enables shadow for the objects using this paint if
     *                      true.
     * @param shadowColor   <tt>int</tt> : Color of the shadow if the shadow is enabled.
     * @param shadowLength  <tt>int</tt> : Length of the shadow in pixels.
     * @return paint        Paint instance created.
     * @see <a href="https://developer.android.com/reference/android/graphics/Paint.Style.html">Paint.Style</a>
     */
    static Paint createPaintFromColor(int color, Paint.Style style, boolean shadowEnabled,
                                      int shadowColor, int shadowLength) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(style);
        paint.setAntiAlias(true);
        if (shadowEnabled) {
            paint.setShadowLayer(shadowLength, 0, 0, shadowColor);
        }
        return paint;
    }
}
