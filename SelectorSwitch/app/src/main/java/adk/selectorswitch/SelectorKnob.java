package adk.selectorswitch;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;


/**
 * Cooked by ADK96r on 21 Nov '17.
 */

class SelectorKnob {

    private final static int knobRadius1DIP = 4;
    private final static int knobRadius2DIP = 1;
    private final static int knobLengthDIP = 3;
    private final static int knobCentralStartingAngle = 230;
    private final static int knobCentralSweepingAngle = 260;
    private final static int notchStartingAngle = 90;
    private final static int notchSweepingAngle = 180;

    private float screenDensity;
    private Path knobPath;
    private int centerX, centerY;

    /**
     * Initialises the knob based on another path.
     *
     * @param context  Context to get the density of the screen.
     * @param knobPath Path of a knob.
     */
    public SelectorKnob(Context context, Path knobPath) {
        this.knobPath = knobPath;
        this.screenDensity = context.getTheme().getResources().getDisplayMetrics().density;
    }

    /**
     * Initialises the knob based on default values.
     *
     * @param context Context to get the density of the screen.
     * @param centerX Horizontal center of the knob.
     * @param centerY Vertical center of the knob.
     */
    SelectorKnob(Context context, int centerX, int centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.screenDensity = context.getTheme().getResources().getDisplayMetrics().density;
        initiateKnob();
    }

    /**
     * Sets the dimensions and completely defines the knob path
     * which can be drawn on a canvas later.
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
     * Returns the knob's path for drawing purposes.
     *
     * @return knobPath
     */
    Path getKnobPath() {
        return this.knobPath;
    }

    /**
     * Applies a rotation transformation to the knob's path
     * to rotate it.
     *
     * @param rotationMatrix Transformation matrix.
     */
    public void rotateKnob(Matrix rotationMatrix) {
        this.knobPath.transform(rotationMatrix);
    }


}
