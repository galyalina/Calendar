package com.iotta.mydays.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Galina Litkin on 06/08/2017.
 * POJO class, custom date object, can be exchanged by standard Date object it case we'll need to add more detailed information
 */

public class MyDate {
    private int day;
    private String month;

    public MyDate(int day, String month) {
        this.day = day;
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    @Override
    public String toString() {
        return getDay() + " " + getMonth();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MyDate)) {
            return false;
        }
        return new EqualsBuilder().append(day, ((MyDate) (obj)).day).append(month, ((MyDate) (obj)).month).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(day)
                .append(month).toHashCode();
    }
}
