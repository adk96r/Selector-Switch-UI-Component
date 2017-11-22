package adk.selectorswitch;

import android.os.AsyncTask;

/**
 * Cooked by ADK96r on 22 Nov'17.
 */

public class SelectorKnobAnimator extends AsyncTask {

    private SelectorSwitch selectorSwitch;
    private SelectorKnob selectorKnob;
    private float startingAngle;
    private float endingAngle;
    private float stepAngle;

    /**
     * Create an Async task to animate the knob's rotation.
     *
     * @param selectorSwitch SelectorSwitch whose knob has to be animated.
     * @param selectorKnob   SelectorKnob to be animated.
     * @param startingAngle  Starting angle of the knob.
     * @param endingAngle    Ending angle of the knob.
     * @param stepAngle      Angle increment to rotate the knob in.
     */
    public SelectorKnobAnimator(SelectorSwitch selectorSwitch, SelectorKnob selectorKnob, float startingAngle,
                                float endingAngle, float stepAngle) {
        this.selectorSwitch = selectorSwitch;
        this.selectorKnob = selectorKnob;
        this.startingAngle = startingAngle;
        this.endingAngle = endingAngle;
        this.stepAngle = stepAngle;
    }

    /**
     * Animates the knob by rotating it in increments and waiting a bit between each increment.
     * Actual rotation takes place in the onProgressUpdate(Object...).
     *
     * @param params Params.
     * @return null
     */
    @Override
    protected Object doInBackground(Object[] params) {
        for (float angle = startingAngle; angle < endingAngle; angle += stepAngle) {
            synchronized (this) {
                try {
                    wait(1);
                    publishProgress(stepAngle);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Rotates the knob by a step angle.
     *
     * @param values Values.
     */
    @Override
    protected void onProgressUpdate(Object[] values) {
        selectorKnob.rotateBy(stepAngle);
        selectorSwitch.invalidate();
    }
}
