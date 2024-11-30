package io.retrofail

/**
 * Provides a comparison function for sorting network candidates prior to network request execution.
 * By default, the order from [NetworkPriorityConfig] is used.
 */
public fun interface NetworkPriorityResolver {
    /**
     * Compares two [NetworkDetails] objects for order. Returns a negative integer, zero, or a
     * positive integer as the first argument has higher, equal, or lower priority than the second.
     *
     * @param lhs The first [NetworkDetails] to be compared.
     * @param rhs The second [NetworkDetails] to be compared.
     *
     * @return A negative integer, zero, or a positive integer as the first [NetworkDetails] has
     * higher, equal, or lower priority than the second.
     **/
    public fun compare(
        lhs: NetworkDetails,
        rhs: NetworkDetails,
    ): Int
}
