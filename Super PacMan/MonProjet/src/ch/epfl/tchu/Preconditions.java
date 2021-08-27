package ch.epfl.tchu;

public final class Preconditions {

    /**
     * private constructor to prohibit the creation of an instance of the class
     */
    private Preconditions () {}

    /**
     * Throws IllegalArgumentException if shouldBeTrue is false
     *
     * @param shouldBeTrue boolean that should be true
     */
    public static void checkArgument(boolean shouldBeTrue)
    {
        if (!shouldBeTrue)
        {
            throw new IllegalArgumentException();
        }

    }
}
