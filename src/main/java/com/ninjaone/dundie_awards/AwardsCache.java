package com.ninjaone.dundie_awards;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;


@Component
public class AwardsCache {
    private final AtomicInteger totalAwards = new AtomicInteger();

    public void setTotalAwards(int totalAwards) {
        this.totalAwards.set(totalAwards);
    }

    public int getTotalAwards(){
        return totalAwards.get();
    }

    public void addOneAward(){
        this.totalAwards.incrementAndGet();
    }
}
