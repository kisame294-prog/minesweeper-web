package com.yattsun.minesweeperweb.service;

import com.yattsun.minesweeperweb.domain.Difficulty;
import com.yattsun.minesweeperweb.entity.ClearTime;
import com.yattsun.minesweeperweb.repository.ClearTimeRepository;
import org.springframework.stereotype.Service;

@Service
public class ClearTimeService {
    private final ClearTimeRepository clearTimeRepository;

    public ClearTimeService(ClearTimeRepository clearTimeRepository) {
        this.clearTimeRepository = clearTimeRepository;
    }

    //クリアタイムをリポジトリに保存
    public void saveClearTime(Difficulty difficulty, int seconds) {
        ClearTime clearTime = new ClearTime();

        clearTime.setDifficulty(difficulty);
        clearTime.setClearTime(seconds);
        clearTimeRepository.save(clearTime);
    }

    public Integer getBestTime(Difficulty difficulty) {
        return clearTimeRepository
                .findFirstByDifficultyOrderByClearTimeAsc(difficulty)   //ソートして昇順にした、一番上のものを取得
                .map(ClearTime::getClearTime)     //クリアタイムだけ所得
                .orElse(null);      //何もなければnull
    }
}
