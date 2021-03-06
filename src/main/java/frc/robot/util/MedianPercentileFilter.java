/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/**
 * Copied from WPILib's Median filter with changes. Would have used inheritance 
 * but m_valueBuffer, m_orderdValues and m_size were set to private.
 */

package frc.robot.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.wpi.first.wpiutil.CircularBuffer;
import edu.wpi.first.wpiutil.math.MathUtil;

/**
 * A class that implements a moving-window median filter. Useful for reducing
 * measurement noise, especially with processes that generate occasional,
 * extreme outliers (such as values from vision processing, LIDAR, or ultrasonic
 * sensors).
 */
public class MedianPercentileFilter {
    private final CircularBuffer m_valueBuffer;
    private final List<Double> m_orderedValues;
    private final int m_size;

    /**
     * Creates a new MedianFilter.
     *
     * @param size The number of samples in the moving window.
     */
    public MedianPercentileFilter(int size) {
        // Circular buffer of values currently in the window, ordered by time
        m_valueBuffer = new CircularBuffer(size);
        // List of values currently in the window, ordered by value
        m_orderedValues = new ArrayList<>(size);
        // Size of rolling window
        m_size = size;
    }

    /**
     * Calculates the moving-window median for the next value of the input stream.
     *
     * @param next The next input value.
     * @return The median of the moving window, updated to include the next value.
     */
    public double calculate(double next) {
        // Find insertion point for next value
        int index = Collections.binarySearch(m_orderedValues, next);

        // Deal with binarySearch behavior for element not found
        if (index < 0) {
            index = Math.abs(index + 1);
        }

        // Place value at proper insertion point
        m_orderedValues.add(index, next);

        int curSize = m_orderedValues.size();

        // If buffer is at max size, pop element off of end of circular buffer
        // and remove from ordered list
        if (curSize > m_size) {
            m_orderedValues.remove(m_valueBuffer.removeLast());
            curSize = curSize - 1;
        }

        // Add next value to circular buffer
        m_valueBuffer.addFirst(next);

        if (curSize % 2 == 1) {
            // If size is odd, return middle element of sorted list
            return m_orderedValues.get(curSize / 2);
        } else {
            // If size is even, return average of middle elements
            return (m_orderedValues.get(curSize / 2 - 1) + m_orderedValues.get(curSize / 2)) / 2.0;
        }
    }

    public double getMedian(){
        int curSize = m_orderedValues.size();

        if (curSize % 2 == 1) {
            // If size is odd, return middle element of sorted list
            return m_orderedValues.get(curSize / 2);
        } else {
            // If size is even, return average of middle elements
            return (m_orderedValues.get(curSize / 2 - 1) + m_orderedValues.get(curSize / 2)) / 2.0;
        }
    }

    /**
     * @param percentileRank The percentile to get, between 0 and 100, with 0 being the
     *                   smallest value.
     */
    public double getPercentile(double percentileRank) {

        // Ensure between 0 and 100
        percentileRank = MathUtil.clamp(percentileRank, 0, 100);

        // Size of ordered list
        int curSize = m_orderedValues.size();

        // If list size is 1, returns only value. Prevents divide by 0 error
        if (curSize == 1) {
            return m_orderedValues.get(0);
        }

        double percentage = Math.round((percentileRank / 100) / (curSize - 1));

        int lowerNum = (int) Math.floor(percentage);
        double partialNum = Math.IEEEremainder(percentage, 1);

        double weightedAvg = m_orderedValues.get(lowerNum) * (1 - partialNum)
                + m_orderedValues.get(lowerNum + 1) * partialNum;
        return weightedAvg;
    }

    public double getQ1() {
        return getPercentile(25);
    }

    public double getQ3() {
        return getPercentile(75);
    }

    public double getInterquartileRange() {
        return getQ3() - getQ1();
    }

    /**
     * Resets the filter, clearing the window of all elements.
     */
    public void reset() {
        m_orderedValues.clear();
        m_valueBuffer.clear();
    }
}
