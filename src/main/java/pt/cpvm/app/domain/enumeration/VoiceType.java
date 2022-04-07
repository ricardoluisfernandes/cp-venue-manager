package pt.cpvm.app.domain.enumeration;

/**
 * The VoiceType enumeration.
 */
public enum VoiceType {
    SOPRANO("1"),
    ALTO("2"),
    TENOR("3"),
    BASS("4"),
    BEATBOX("5");

    private final String value;

    VoiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
