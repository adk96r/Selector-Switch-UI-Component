package adk.selectorswitch;

/**
 * IllegalSelectorException is built on top of {@link Exception} to offer
 * basic exception handling to the SelectorSwitch UI Component.
 *
 * @see SelectorSwitch
 * @see SelectorDial
 * @see SelectorKnob
 */
public class IllegalSelectorException extends Exception {

    public IllegalSelectorException() {
        super();
    }

    public IllegalSelectorException(String message) {
        super(message);
    }
}
