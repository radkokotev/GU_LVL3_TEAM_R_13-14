package custom_java_utils;

/**
 * A class to keep CHECK utilities. These are used for detecting runtime
 * unexpected behaviour. Since this system is designed to be user interactive
 * a failure would not cause the system to crash, but instead throw an 
 * exception.
 */
public class CheckUtils {
	
	/**
	 * Checks if the passed parameter is true.
	 * @throws CheckFailException in the case when the parameter is false
	 */
	public static void check(boolean param) throws CheckFailException {
		if (!param) {
			throw new CheckFailException("Unexpected value of 'FALSE'");
		}
	}

	/**
	 * Checks if the passed parameter is false.
	 * @throws CheckFailException in the case when the parameter is true
	 */
	public static void checkFalse(boolean param) throws CheckFailException {
		if (param) {
			throw new CheckFailException("Unexpected value of 'TRUE'");
		}
	}
	
	/**
	 * Checks if the passed parameters are equal.
	 * @throws CheckFailException in the case when the parameters are not equal
	 */
	public static void checkEqual(Object one, Object other) 
			throws CheckFailException {
		if (!one.equals(other)) {
			throw new CheckFailException(
					"Values were expected to be equal. Actual: " +
					one.toString() + " vs " + other.toString());
		}
	}

	/**
	 * Checks if the passed parameters are NOT equal.
	 * @throws CheckFailException in the case when the parameters are equal
	 */
	public static void checkNotEqual(Object one, Object other) 
			throws CheckFailException {
		if (one.equals(other)) {
			throw new CheckFailException(
					"Values were NOT expected to be equal. Actual: " +
					one.toString() + " vs " + other.toString());
		}
	}
	
	/**
	 * Checks if the first parameter is less than the second one.
	 * @throws CheckFailException when the above condition does not hold.
	 */
	public static <E extends Comparable<E>> void checkLess(E left, E right)
			throws CheckFailException {
		if (left.compareTo(right) >= 0) {
			throw new CheckFailException(
					"Expected 'left < right'. Actual: " +
					left.toString() + " vs " + left.toString());
		}
	}
	
	/**
	 * Checks if the first parameter is less than or equal to the second one.
	 * @throws CheckFailException when the above condition does not hold.
	 */
	public static <E extends Comparable<E>> void 
			checkLessOrEqual(E left, E right) throws CheckFailException {
		if (left.compareTo(right) > 0) {
			throw new CheckFailException(
					"Expected 'left <= right'. Actual: " +
					left.toString() + " vs " + left.toString());
		}
	}
	
	/**
	 * Checks if the first parameter is greater than the second one.
	 * @throws CheckFailException when the above condition does not hold.
	 */
	public static <E extends Comparable<E>> void checkGreater(E left, E right)
			throws CheckFailException {
		if (left.compareTo(right) <= 0) {
			throw new CheckFailException(
					"Expected 'left > right'. Actual: " +
					left.toString() + " vs " + left.toString());
		}
	}
	
	/**
	 * Checks if the first parameter is greater than or equal to the second.
	 * @throws CheckFailException when the above condition does not hold.
	 */
	public static <E extends Comparable<E>> void 
			checkGreaterOrEqual(E left, E right) throws CheckFailException {
		if (left.compareTo(right) < 0) {
			throw new CheckFailException(
					"Expected 'left >= right'. Actual: " +
					left.toString() + " vs " + left.toString());
		}
	}

}
