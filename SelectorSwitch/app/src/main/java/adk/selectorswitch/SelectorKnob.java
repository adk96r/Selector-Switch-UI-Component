package adk.selectorswitch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;


/**
 * SelectorKnob describes the knob used in the selector switch and has the structure shown
 * {@link <a href="https://www.github.com/adk96r/Selector-Switch-UI-Component/">here</a>}.
 * <p>
 * As specified in the diagram, the knob has a set of values that define its structure in form
 * of a path. This path is drawn on the selector switch's canvas when the selector switch is
 * rendered. The selector knob also stores a rotation transformation matrix which is used to
 * rotate it every time the selector switch's mode is changed, as the knob has to point to the
 * correct mode upon the change.
 *
 * @see SelectorSwitch
 */

class SelectorKnob {

    /**
     * Knob has a central radius of {@value knobRadius1DIP} DP units.
     */
    private final static int knobRadius1DIP = 4;

    /**
     * Knob has an outer radius of {@value knobRadius2DIP} DP units.
     */
    private final static int knobRadius2DIP = 1;

    /**
     * Knob has a handle length of {@value knobLengthDIP} DP units.
     */
    private final static int knobLengthDIP = 3;

    /**
     * Knob has a central starting angle of {@value knobCentralStartingAngle} degrees.
     */
    private final static int knobCentralStartingAngle = 230;

    /**
     * Knob has a central sweeping angle of {@value knobCentralSweepingAngle} degrees.
     */
    private final static int knobCentralSweepingAngle = 260;

    /**
     * Knob has a notch starting angle of {@value notchStartingAngle} degrees.
     */
    private final static int notchStartingAngle = 90;

    /**
     * Knob has a notch sweeping angle of {@value notchSweepingAngle} degrees.
     */
    private final static int notchSweepingAngle = 180;

    /**
     * Stores the density of the screen for converting DPs into pixels.
     *
     * @see SelectorUtil#getPixelsFromDips(int, float)
     */
    private float screenDensity;

    /**
     * Stores the complete structure of the knob and is drawn onto the canvas
     * when the switch is rendered.
     *
     * @see SelectorSwitch#onDraw(Canvas)
     */
    private Path knobPath;

    /**
     * Stores the X and Y coordinates of the point about which the knob is rotated.
     * <p>
     * Note: This point is not the center of the knob!
     */
    private int centerX, centerY;
    private float currentAngle;
    private Matrix rotationMatrix;

    /**
     * Initialises the knob's properties based on default values.
     *
     * @param context <tt>Context</tt> A context to get the density of the screen.
     * @param centerX <tt>int</tt> : The X coordinate of the point about which the knob
     *                would rotate.
     * @param centerY <tt>int</tt> : The Y coordinate of the point about which the knob
     *                would rotate.
     */
    SelectorKnob(Context context, int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.screenDensity = context.getTheme().getResources().getDisplayMetrics().density;
        this.currentAngle = 0;
        this.rotationMatrix = new Matrix();
        rotationMatrix.postRotate(0, centerX, centerY);
        initiateKnob();
    }

    /**
     * Sets the default dimensions and constructs the path which can be used later while
     * drawing the switch.
     *
     * @see SelectorSwitch#onDraw(Canvas)
     */
    private void initiateKnob() {

        // Set knob dimensions.
        int knobRadius1 = SelectorUtil.getPixelsFromDips(knobRadius1DIP, screenDensity);
        int knobRadius2 = SelectorUtil.getPixelsFromDips(knobRadius2DIP, screenDensity);
        int knobLength = SelectorUtil.getPixelsFromDips(knobLengthDIP, screenDensity);
        knobPath = new Path();

        // The central knob.
        knobPath.addArc(centerX - knobRadius1,
                centerY - knobRadius1,
                centerX + knobRadius1,
                centerY + knobRadius1,
                knobCentralStartingAngle,
                knobCentralSweepingAngle);

        // The end notch.
        knobPath.addArc(centerX - knobRadius1 - knobLength - knobRadius2,
                centerY - knobRadius2,
                centerX - knobRadius1 - knobLength + knobRadius2,
                centerY + knobRadius2,
                notchStartingAngle,
                notchSweepingAngle);

        // The handle between them.
        knobPath.moveTo(centerX - knobLength + knobRadius2 / 2, centerY - knobRadius1 * 3 / 4);
        knobPath.lineTo(centerX - knobRadius1 - knobLength, centerY - knobRadius2);
        knobPath.lineTo(centerX - knobRadius1 - knobLength, centerY + knobRadius2);
        knobPath.lineTo(centerX - knobLength + knobRadius2 / 2, centerY + knobRadius1 * 3 / 4);
        knobPath.close();
    }

    /**
     * Returns the path of the knob.
     *
     * @return {@link #knobPath}
     */
    public Path getKnobPath() {
        return this.knobPath;
    }

    /**
     * Rotates the knob by a specified angle about the point whose coordinates are
     * {@link #centerX} and {@link #centerY}.
     *
     * @param delta <tt>float</tt> : The angle to rotate the knob by.
     */
    public void rotateBy(float delta) {
        this.currentAngle = (currentAngle + delta) % 360;
        this.rotationMatrix.setRotate(delta, centerX, centerY);
        this.knobPath.transform(rotationMatrix);
    }

    /**
     * Returns the current angle the knob is at.
     *
     * @return {@link #currentAngle}
     */
    public float getRotation() {
        return this.currentAngle;
    }


}
