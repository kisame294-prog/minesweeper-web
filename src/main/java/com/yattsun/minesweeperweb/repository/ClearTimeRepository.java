package com.yattsun.minesweeperweb.repository;

import com.yattsun.minesweeperweb.domain.Difficulty;
import com.yattsun.minesweeperweb.entity.ClearTime;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClearTimeRepository
        extends CrudRepository<ClearTime, Long> {
    Optional<ClearTime> findFirstByDifficultyOrderByClearTimeAsc(Difficulty difficulty);
}
