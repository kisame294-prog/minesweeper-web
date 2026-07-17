package com.yattsun.minesweeperweb.repository;

import com.yattsun.minesweeperweb.domain.Difficulty;
import com.yattsun.minesweeperweb.entity.ClearTime;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClearTimeRepository
        extends CrudRepository<ClearTime, Long> {

    // 難易度別にクリアタイムを昇順に表示し、一番上に来たものを取得する流れを一行で
    Optional<ClearTime> findFirstByDifficultyOrderByClearTimeAsc(Difficulty difficulty);
}
