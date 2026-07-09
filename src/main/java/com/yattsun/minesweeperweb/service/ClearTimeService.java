package com.yattsun.minesweeperweb.service;

import com.yattsun.minesweeperweb.entity.ClearTime;
import com.yattsun.minesweeperweb.repository.ClearTimeRepository;
import org.springframework.stereotype.Service;

@Service
public class ClearTimeService {
    private final ClearTimeRepository clearTimeRepository;

    public ClearTimeService(ClearTimeRepository clearTimeRepository) {
        this.clearTimeRepository = clearTimeRepository;
    }

    public void saveClearTime(int seconds) {
        ClearTime clearTime = new ClearTime();

        clearTime.setClearTime(seconds);
        clearTimeRepository.save(clearTime);
    }
}
